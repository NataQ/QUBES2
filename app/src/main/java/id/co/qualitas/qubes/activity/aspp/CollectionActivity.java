package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CollectionAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class CollectionActivity extends BaseActivity {
    private CollectionAdapter mAdapter;
    private List<Invoice> mList;
    private TextView txtDate, txtTotalInvoice, txtTotalPaid;
    private double totalInvoice = 0;
    private double totalPaid = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_collection);

        initialize();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CollectionActivity.this);
        });

        swipeLayout.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAdapter();
                swipeLayout.setRefreshing(false);
            }
        });

        txtTitle.setOnClickListener(view -> {
            database.deleteInvoiceHeader();
            database.deleteInvoiceDetail();
            getFirstDataOffline();
        });
    }

    private void setAdapter() {
        mAdapter = new CollectionAdapter(this, mList, header -> {
            SessionManagerQubes.setCollectionHeader(header);
            SessionManagerQubes.setCollectionSource(1);
            Intent intent = new Intent(this, CollectionFormActivity.class);
            startActivity(intent);
        });

        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Invoice("DKA495486", "TOKO SIDIK HALIM", "0GV43", 884736, 0, "2023-06-01", true, initDataMaterial()));
        mList.add(new Invoice("DKA496933", "TOKO SIDIK HALIM", "0GV43", 3929664, 0, "2023-07-01", false, initDataMaterial()));
        mList.add(new Invoice("DKA492540", "SARI SARI (TK)", "0WJ42", 14024448, 0, "2023-09-01", true, initDataMaterial()));
        mList.add(new Invoice("DKA402541", "TOKO SIDIK HALIM", "0WJ42", 9363600, 0, "2023-11-01", false, initDataMaterial()));

        for (Invoice param : mList) {
            int idHeader = database.addInvoiceHeader(param, user.getUsername());
            for (Material param1 : param.getMaterialList()) {
                database.addInvoiceDetail(param1, String.valueOf(idHeader), user.getUsername());
            }
        }
    }

    private List<Material> initDataMaterial() {
        List<Material> mList = new ArrayList<>();
        mList.add(new Material("31001", "You C1000 Vitamin Lemon", 1200000));
        mList.add(new Material("11001", "Kratingdaeng", 50000));
        mList.add(new Material("11030", "Redbull", 740000));
        mList.add(new Material("21001", "Torpedo Aneka Buah", 350000));
        return mList;
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtDate = findViewById(R.id.txtDate);
        txtTotalInvoice = findViewById(R.id.txtTotalInvoice);
        txtTotalPaid = findViewById(R.id.txtTotalPaid);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        txtTitle = findViewById(R.id.txtTitle);
        progressCircle = findViewById(R.id.progressCircle);
        swipeLayout = findViewById(R.id.swipeLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFirstDataOffline();
    }

    private void getFirstDataOffline() {
        getData();
        setAdapter();

        if (mList == null || mList.isEmpty()) {
            progressCircle.setVisibility(View.VISIBLE);
            new AsyncLoading().execute();//1
        }
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllInvoiceHeaderNotPaid();

        for (Invoice inv : mList) {
            totalInvoice = totalInvoice + 1;
            totalPaid = totalPaid + inv.getTotal_paid();
        }

        txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        txtTotalPaid.setText("Rp. " + format.format(totalPaid));
        txtTotalInvoice.setText(format.format(totalInvoice));
    }

    private class AsyncLoading extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                initData();
                return true;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("invoice", ex.getMessage());
                }
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {

            progressCircle.setVisibility(View.GONE);
            getData();
            mAdapter.setData(mList);
        }
    }
}
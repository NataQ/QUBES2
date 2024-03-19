package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import id.co.qualitas.qubes.model.StartVisit;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class CollectionActivity extends BaseActivity {
    private Button btnAdd;
    private CollectionAdapter mAdapter;
    private List<Invoice> mList;
    private TextView txtDate, txtTotalInvoice, txtTotalPaid;
    private double totalInvoice = 0;
    private double totalPaid = 0.0;
    private StartVisit startVisit;

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

        btnAdd.setOnClickListener(v -> {
            if (startVisit != null) {
                if (startVisit.getDate() != null) {
                    if (startVisit.getDate().equals(Helper.getTodayDate(Constants.DATE_FORMAT_3))) {
                        switch (startVisit.getStatus_visit()) {
                            case 0:
                                setToast("Silahkan memulai kunjungan");
                                break;
                            case 1:
//                                    SessionManagerQubes.setCollectionHeader(header);
                                SessionManagerQubes.setCollectionSource(1);
                                intent = new Intent(this, CollectionFormActivityNew.class);
                                startActivity(intent);
                                break;
                            case 2:
                            case 3:
                                setToast("Kunjungan hari ini sudah selesai.");
                                break;
                        }
                    } else {
                        setToast("Silahkan Start Visit/Next Day");
                    }
                } else {
                    setToast("Silahkan memulai kunjungan");
                }
            } else {
                setToast("Silahkan memulai kunjungan");
            }
        });
    }

    private void setAdapter() {
        mAdapter = new CollectionAdapter(this, mList, header -> {
            SessionManagerQubes.setCollectionHeader(header);
            Intent intent = new Intent(this, InvoiceDetailActivity.class);
            startActivity(intent);
        });

        recyclerView.setAdapter(mAdapter);
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        btnAdd = findViewById(R.id.btnAdd);
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
            setToast("Pastikan invoice sudah di verifikasi");
//            progressCircle.setVisibility(View.VISIBLE);
//            new AsyncLoading().execute();//1
        }
    }

    private void getData() {
        totalPaid = 0;
        totalInvoice = 0;
        mList = new ArrayList<>();
        mList = database.getAllInvoiceHeaderVerif();
//        startVisit = SessionManagerQubes.getStartDay();
        startVisit = database.getLastStartVisit();


        if (startVisit != null) {
            if (startVisit.getDate() != null) {
                if (startVisit.getDate().equals(Helper.getTodayDate(Constants.DATE_FORMAT_3))) {
                    switch (startVisit.getStatus_visit()) {
                        case 0:
                        case 2:
                        case 3:
                            btnAdd.setVisibility(View.GONE);
                            break;
                        case 1:
                            btnAdd.setVisibility(View.VISIBLE);
                            break;
                    }
                } else {
                    btnAdd.setVisibility(View.GONE);
                }
            } else {
                btnAdd.setVisibility(View.GONE);
            }
        } else {
            btnAdd.setVisibility(View.GONE);
        }

        for (Invoice inv : mList) {
            totalInvoice = totalInvoice + 1;
            totalPaid = totalPaid + inv.getTotal_paid();
        }

        txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        txtTotalPaid.setText("Rp. " + format.format(totalPaid));
        txtTotalInvoice.setText(format.format(totalInvoice));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Helper.setItemParam(Constants.FROM_VISIT, "1");
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

//    private class AsyncLoading extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            try {
//                initData();
//                return true;
//            } catch (Exception ex) {
//                if (ex.getMessage() != null) {
//                    Log.e("invoice", ex.getMessage());
//                }
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//
//            progressCircle.setVisibility(View.GONE);
//            getData();
//            mAdapter.setData(mList);
//        }
//    }
}
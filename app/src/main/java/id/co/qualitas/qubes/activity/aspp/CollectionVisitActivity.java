package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CollectionVisitAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionVisitHistoryAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class CollectionVisitActivity extends BaseActivity {
    private CollectionVisitAdapter mAdapter;
    private CollectionVisitHistoryAdapter mAdapterHistory;
    private List<Invoice> mList;
    private List<CollectionHeader> mListHistory;
    private TextView txtInvoice, txtInvoiceLine, txtHistory, txtHistoryLine;
    private LinearLayout llInvoice, llHistory;
    private RecyclerView recyclerViewInvoice, recyclerViewHistory;
    private TextView txtDateHistory, txtTotalInvoice, txtTotalAmountInvoice, txtNoDataInvoice;
    private TextView txtDateInvoice, txtTotalPaidHistory, txtNoDataHistory;
    private double totalInvoice = 0;
    private double totalPaid = 0, totalPaidHistory = 0;

    private SwipeRefreshLayout swipeLayoutHistory, swipeLayoutInvoice;
    private ProgressBar progressCircleHistory, progressCircleInvoice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_collection_visit);

        initialize();
        setViewInvoice();
        setViewHistory();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CollectionVisitActivity.this);
        });
    }

    private void setViewInvoice() {
        txtInvoice.setOnClickListener(v -> {
            txtInvoice.setTextColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtHistory.setTextColor(ContextCompat.getColor(this, R.color.gray8_aspp));
            txtInvoiceLine.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtHistoryLine.setBackgroundColor(ContextCompat.getColor(this, R.color.space_transparent));

            llInvoice.setVisibility(View.VISIBLE);
            llHistory.setVisibility(View.GONE);
        });

        swipeLayoutInvoice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RequestUrl().execute();
                swipeLayoutInvoice.setRefreshing(false);
            }
        });
    }

    private void setViewHistory() {
        txtHistory.setOnClickListener(v -> {
            txtInvoice.setTextColor(ContextCompat.getColor(this, R.color.gray8_aspp));
            txtHistory.setTextColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtInvoiceLine.setBackgroundColor(ContextCompat.getColor(this, R.color.space_transparent));
            txtHistoryLine.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_aspp));

            llInvoice.setVisibility(View.GONE);
            llHistory.setVisibility(View.VISIBLE);
        });

        swipeLayoutHistory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RequestUrl().execute();
                swipeLayoutHistory.setRefreshing(false);
            }
        });
    }

    private void setAdapterInvoice() {
        mAdapter = new CollectionVisitAdapter(this, mList, header -> {
            SessionManagerQubes.setCollectionHeader(header);
            SessionManagerQubes.setCollectionSource(2);

            Intent intent = new Intent(this, CollectionFormActivity.class);
            startActivity(intent);
        });

        recyclerViewInvoice.setAdapter(mAdapter);
    }

    private void setAdapterHistory() {
        mAdapterHistory = new CollectionVisitHistoryAdapter(this, mListHistory, header -> {
            SessionManagerQubes.setCollectionHistoryHeader(header);
            SessionManagerQubes.setCollectionSource(2);
            Intent intent = new Intent(this, CollectionDetailActivity.class);
            startActivity(intent);
        });

        recyclerViewHistory.setAdapter(mAdapterHistory);
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.addAll(database.getAllInvoiceCustomer(SessionManagerQubes.getOutletHeader().getId()));
        totalInvoice = 0;
        totalPaid = 0;
        for (Invoice inv : mList) {
            totalInvoice = totalInvoice + 1;
            totalPaid = totalPaid + inv.getTotal_paid();
        }

        txtDateInvoice.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        txtTotalAmountInvoice.setText("Rp. " + format.format(totalPaid));
        txtTotalInvoice.setText(format.format(totalInvoice));

        mListHistory = new ArrayList<>();
        mListHistory.addAll(database.getAllInvoiceHistoryCustomer(SessionManagerQubes.getOutletHeader().getId()));
        totalPaidHistory = 0;
        for (CollectionHeader inv : mListHistory) {
            totalPaidHistory = totalPaidHistory + inv.getTotalPaid();
        }

        txtDateHistory.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        txtTotalPaidHistory.setText("Rp. " + format.format(totalPaidHistory));
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        progressCircleHistory = findViewById(R.id.progressCircleHistory);
        progressCircleInvoice = findViewById(R.id.progressCircleInvoice);
        txtInvoice = findViewById(R.id.txtInvoice);
        txtInvoiceLine = findViewById(R.id.txtInvoiceLine);
        txtHistory = findViewById(R.id.txtHistory);
        txtHistoryLine = findViewById(R.id.txtHistoryLine);
        llHistory = findViewById(R.id.llHistory);
        llInvoice = findViewById(R.id.llInvoice);
        txtDateHistory = findViewById(R.id.txtDateHistory);
        txtDateInvoice = findViewById(R.id.txtDateInvoice);
        txtNoDataInvoice = findViewById(R.id.txtNoDataInvoice);
        txtNoDataHistory = findViewById(R.id.txtNoDataHistory);
        txtTotalPaidHistory = findViewById(R.id.txtTotalPaidHistory);
        txtTotalInvoice = findViewById(R.id.txtTotalInvoice);
        txtTotalAmountInvoice = findViewById(R.id.txtTotalAmountInvoice);

        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        recyclerViewInvoice = findViewById(R.id.recyclerViewInvoice);
        recyclerViewInvoice.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewInvoice.setHasFixedSize(true);

        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setHasFixedSize(true);

        swipeLayoutHistory = findViewById(R.id.swipeLayoutHistory);
        swipeLayoutHistory.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);

        swipeLayoutInvoice = findViewById(R.id.swipeLayoutInvoice);
        swipeLayoutInvoice.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapterInvoice();
        setAdapterHistory();

        if ((mList == null || mList.isEmpty()) && (mListHistory == null || mListHistory.isEmpty())) {
            progressCircleInvoice.setVisibility(View.VISIBLE);
            progressCircleHistory.setVisibility(View.VISIBLE);
            recyclerViewInvoice.setVisibility(View.GONE);
            recyclerViewHistory.setVisibility(View.GONE);
            new RequestUrl().execute();//1
        }
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                mList = new ArrayList<>();
                mListHistory = new ArrayList<>();
                initData();
                return null;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("Collection", ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage result) {
            progressCircleInvoice.setVisibility(View.GONE);
            progressCircleHistory.setVisibility(View.GONE);
            recyclerViewInvoice.setVisibility(View.VISIBLE);
            recyclerViewHistory.setVisibility(View.VISIBLE);
            mAdapter.setData(mList);
            mAdapterHistory.setData(mListHistory);
        }
    }
}
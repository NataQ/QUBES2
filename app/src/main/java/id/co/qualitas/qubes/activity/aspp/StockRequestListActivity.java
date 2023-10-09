package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.StockRequestHeaderAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class StockRequestListActivity extends BaseActivity {
    private StockRequestHeaderAdapter mAdapter;
    private List<StockRequest> mList;
    private Button btnAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_stock_request_list);

        initialize();

        btnAdd.setOnClickListener(v -> {
            intent = new Intent(this, StockRequestAddActivity.class);
            startActivity(intent);
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(StockRequestListActivity.this);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
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
            database.deleteStockRequestHeader();
            database.deleteStockRequestDetail();
            getFirstDataOffline();
        });
    }

    private void setAdapter() {
        mAdapter = new StockRequestHeaderAdapter(this, mList, header -> {
            SessionManagerQubes.setStockRequestHeader(header);
            intent = new Intent(this, StockRequestDetailActivity.class);
            startActivity(intent);
        });

        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        List<StockRequest> mList = new ArrayList<>();
        mList.add(new StockRequest("2023-05-01", "2023050101000010001", "2023-05-01", "SJ0001", "approve", false, false, true, false, null, initDataMaterial()));
        mList.add(new StockRequest("2023-05-01", "2023050201000010001", "2023-05-01", "SJ0001", "approve", false, false, true, false, null, initDataMaterial()));
        mList.add(new StockRequest("2023-05-01", "2023050301000010001", "2023-05-01", "SJ0001", "approve", false, false, true, false, null, initDataMaterial()));

        for (StockRequest param : mList) {
            int idHeader = database.addStockRequestHeader(param, user.getUsername());
            for (Material param1 : param.getMaterialList()) {
                database.addStockRequestDetail(param1, String.valueOf(idHeader), user.getUsername());
            }
        }
    }

    private List<Material> initDataMaterial() {
        List<Material> mList = new ArrayList<>();
        mList.add(new Material("31001", "You C1000 Vitamin Lemon", 3000, "BTL"));
        mList.add(new Material("11001", "Kratingdaeng", 100, "CAN"));
        mList.add(new Material("11030", "Redbull", 2200, "CAN"));
        mList.add(new Material("21001", "Torpedo Aneka Buah", 1000, "CAN"));
        return mList;
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtTitle = findViewById(R.id.txtTitle);
        progressCircle = findViewById(R.id.progressCircle);
        swipeLayout = findViewById(R.id.swipeLayout);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnAdd = findViewById(R.id.btnAdd);
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
            new RequestUrl().execute();
        }
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllStockRequestHeader();
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                String URL_ = Constants.API_GET_STOCK_REQUEST;
                final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("stockRequest", ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage WsMessage) {
            progressCircle.setVisibility(View.GONE);
            if (WsMessage != null) {
                if (WsMessage.getIdMessage() == 1) {
                    mList = new ArrayList<>();
                    StockRequest[] paramArray = Helper.ObjectToGSON(WsMessage.getResult(), StockRequest[].class);
                    Collections.addAll(mList, paramArray);
                    for (StockRequest param : mList) {
                        List<Material> listMat = new ArrayList<>();
                        Material[] matArray = Helper.ObjectToGSON(param.getMaterialList(), Material[].class);
                        Collections.addAll(listMat, matArray);
                        param.setMaterialList(listMat);
                    }
                    getData();
                    mAdapter.setData(mList);
                } else {
                    setToast(WsMessage.getMessage());
                }
            } else {
                setToast(getString(R.string.failedGetData));
            }

        }
    }
}
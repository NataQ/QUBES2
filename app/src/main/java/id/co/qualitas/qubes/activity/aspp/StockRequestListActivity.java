package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.StockRequestListAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class StockRequestListActivity extends BaseActivity {
    private StockRequestListAdapter mAdapter;
    private List<StockRequest> mList;
    private Button btnAdd;
    private WSMessage resultWsMessage;
    private boolean saveDataSuccess = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_stock_request_list);

        initialize();

        btnAdd.setOnClickListener(v -> {
            if(database.checkUnloadingRequest()) {
                intent = new Intent(this, StockRequestAddActivity.class);
                startActivity(intent);
            }else{
                setToast("Silahkan melakukan Unloading terlebih dahulu");
            }
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
                requestData();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void setAdapter() {
        mAdapter = new StockRequestListAdapter(this, mList, header -> {
            SessionManagerQubes.setStockRequestHeader(header);
            intent = new Intent(this, StockRequestDetailActivity.class);
            startActivity(intent);
        });

        recyclerView.setAdapter(mAdapter);
    }

    private void initialize() {
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
            requestData();
        }
    }

    private void requestData() {
        recyclerView.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        PARAM = 1;
        new RequestUrl().execute();
//        setDataDummyStock();
    }

    private void setDataDummyStock() {
        String jsonFileString = NetworkHelper.getJsonFromAssets(this, "stockRequest.json");
        Gson gson = new Gson();
        Type resultType = new TypeToken<WSMessage>(){}.getType();
        WSMessage resultWsMessage = gson.fromJson(jsonFileString, resultType);
        mList = new ArrayList<>();
        StockRequest[] paramArray = Helper.ObjectToGSON(resultWsMessage.getResult(), StockRequest[].class);
        Collections.addAll(mList, paramArray);
        database.deleteStockRequestHeader();
        database.deleteStockRequestDetail();

        for (StockRequest param : mList) {
            List<Material> listMat = new ArrayList<>();
            Material[] matArray = Helper.ObjectToGSON(param.getMaterialList(), Material[].class);
            Collections.addAll(listMat, matArray);
            param.setMaterialList(listMat);

            int idHeader = database.addStockRequestHeader(param, user.getUsername());
            for (Material mat : listMat) {
                database.addStockRequestDetail(mat, String.valueOf(idHeader), user.getUsername());
            }
        }
        getData();
        progressCircle.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        mAdapter.setData(mList);
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllStockRequestHeader();
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_ = Constants.API_STOCK_REQUEST_LIST;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                } else {
                    mList = new ArrayList<>();
                    StockRequest[] paramArray = Helper.ObjectToGSON(resultWsMessage.getResult(), StockRequest[].class);
                    Collections.addAll(mList, paramArray);
                    database.deleteStockRequestHeader();
                    database.deleteStockRequestDetail();

                    for (StockRequest param : mList) {
                        List<Material> listMat = new ArrayList<>();
                        Material[] matArray = Helper.ObjectToGSON(param.getMaterialList(), Material[].class);
                        Collections.addAll(listMat, matArray);
                        param.setMaterialList(listMat);

                        int idHeader = database.addStockRequestHeader(param, user.getUsername());
                        for (Material mat : listMat) {
                            database.addStockRequestDetail(mat, String.valueOf(idHeader), user.getUsername());
                        }
                    }
                    getData();
                    saveDataSuccess = true;
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("stockRequest", ex.getMessage());
                }
                if (PARAM == 2) {
                    saveDataSuccess = false;
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
            if (PARAM == 1) {
                if (result != null) {
                    if (result.getIdMessage() == 1) {
                        resultWsMessage = result;
                        PARAM = 2;
                        new RequestUrl().execute();
                    } else {
                        progressCircle.setVisibility(View.GONE);
                        setToast(result.getMessage());
                    }
                } else {
                    progressCircle.setVisibility(View.GONE);
                    setToast(getString(R.string.failedGetData));
                }
            } else {
                progressCircle.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (saveDataSuccess) {
                    mAdapter.setData(mList);
                } else {
                    setToast(getString(R.string.failedSaveData));
                }
            }

        }
    }
}
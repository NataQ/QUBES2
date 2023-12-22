package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
    private WSMessage resultWsMessage, logResult;
    private boolean saveDataSuccess = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_stock_request_list);

        initialize();

        btnAdd.setOnClickListener(v -> {
//            if (database.checkUnloadingRequest() == 0) {
            intent = new Intent(this, StockRequestAddActivity.class);
            startActivity(intent);
//            } else {
//                setToast("Silahkan melakukan Unloading terlebih dahulu");
//            }
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
            if (header.getStatus().equals(Constants.STATUS_DRAFT)) {
                intent = new Intent(this, StockRequestEditActivity.class);
            } else {
                intent = new Intent(this, StockRequestDetailActivity.class);
            }
            startActivity(intent);
        });

        recyclerView.setAdapter(mAdapter);

        if (Helper.isEmptyOrNull(mList)) {
            recyclerView.setVisibility(View.GONE);
            llNoData.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = findViewById(R.id.llNoData);
        txtTitle = findViewById(R.id.txtTitle);
        progressCircle = findViewById(R.id.progressCircle);
        swipeLayout = findViewById(R.id.swipeLayout);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        userRoleList = new ArrayList<>();
        userRoleList.addAll(getRoleUser());

        if (Helper.findRole(userRoleList, "MOBILE_REQUEST_STOCK_CREATE")) {
            btnAdd.setVisibility(View.VISIBLE);
        } else {
            btnAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getFirstDataOffline();
    }

    private void getFirstDataOffline() {
        getData();
        setAdapter();

//        if (mList == null || mList.isEmpty()) {
            requestData();
//        }
    }

    private void requestData() {
        llNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        PARAM = 1;
        new RequestUrl().execute();
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
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else {
                    mList = new ArrayList<>();
                    StockRequest[] paramArray = Helper.ObjectToGSON(resultWsMessage.getResult(), StockRequest[].class);
                    if (paramArray != null) {
                        Collections.addAll(mList, paramArray);
                        database.deleteStockRequestHeader();
                        database.deleteStockRequestDetail();
                    }

                    for (StockRequest param : mList) {
                        List<Material> listMat = new ArrayList<>();
                        Material[] matArray = Helper.ObjectToGSON(param.getMaterialList(), Material[].class);
                        if (matArray != null) {
                            Collections.addAll(listMat, matArray);
                        }
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
                } else {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                    logResult.setMessage("Stock Request error: " + exMess);
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
                if (logResult.getIdMessage() == 1) {
                    String message = "Stock Request : " + logResult.getMessage();
                    logResult.setMessage(message);
                }
                database.addLog(logResult);
                if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                    resultWsMessage = logResult;
                    PARAM = 2;
                    new RequestUrl().execute();
                } else {
                    progressCircle.setVisibility(View.GONE);
                    getData();
                    mAdapter.setData(mList);
                    if (Helper.isEmptyOrNull(mList)) {
                        recyclerView.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
                    }
                }
            } else {
                progressCircle.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (saveDataSuccess) {
                    mAdapter.setData(mList);
                }
            }
            if (Helper.isEmptyOrNull(mList)) {
                recyclerView.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                llNoData.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Helper.setItemParam(Constants.FROM_VISIT, "1");
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
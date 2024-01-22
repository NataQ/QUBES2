package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    int offset;
    LinearLayout loadingDataBottom;
    private LinearLayoutManager linearLayout;
    private boolean loading = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_stock_request_list);

        initialize();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0) {
                    itemCount();
                } else {
                    if (dy > 0) //check for scroll down
                    {
                        itemCount();
                    } else {
                        loadingDataBottom.setVisibility(View.GONE);
                        loading = true;
                    }
                }
            }
        });

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

    public void itemCount() {
        int visibleItemCount = linearLayout.getChildCount();
        int totalItemCount = linearLayout.getItemCount();
        int pastVisiblesItems = linearLayout.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                offset = totalItemCount;
                loadingDataBottom.setVisibility(View.VISIBLE);
                PARAM = 1;
                new RequestUrl().execute();//1 scroll bottom
            }
        }
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
        offset = 0;
        loading = true;
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = findViewById(R.id.llNoData);
        txtTitle = findViewById(R.id.txtTitle);
        progressCircle = findViewById(R.id.progressCircle);
        swipeLayout = findViewById(R.id.swipeLayout);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        loadingDataBottom = findViewById(R.id.loadingDataBottom);
        linearLayout = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

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
//        getData();//get first data offline
        offset = 0;
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
        new RequestUrl().execute();//request data
    }

    private void getData() {
        if(offset == 0 || Helper.isEmptyOrNull(mList)){
            mList = database.getAllStockRequestHeader(offset);
        }else{
            mList.addAll(database.getAllStockRequestHeader(offset));
        }

    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_ = Constants.API_STOCK_REQUEST_LIST;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    Map request = new HashMap();
                    request.put("page", offset);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, request);
                    return null;
                } else {
                    List<StockRequest> beList = new ArrayList<>();
                    StockRequest[] paramArray = Helper.ObjectToGSON(resultWsMessage.getResult(), StockRequest[].class);

                    if (paramArray != null) {
                        Collections.addAll(beList, paramArray);
                        if (offset == 0) {
                            database.deleteStockRequestHeader();
                            database.deleteStockRequestDetail();
                        }

                        for (StockRequest param : beList) {
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
                    }

                    getData();//param 2
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
                    new RequestUrl().execute();//param 1
                } else {
                    progressCircle.setVisibility(View.GONE);
                    getData();//param 1
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
                loadingDataBottom.setVisibility(View.GONE);
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
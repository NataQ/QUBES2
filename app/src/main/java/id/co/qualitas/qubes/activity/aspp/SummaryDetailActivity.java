package id.co.qualitas.qubes.activity.aspp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.SummaryDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class SummaryDetailActivity extends BaseActivity {
    private SummaryDetailAdapter mAdapter;
    private List<Material> mList;
    private WSMessage resultWsMessage;
    private boolean saveDataSuccess = false;
    private Order orderHeader;
    private LinearLayout llStatus, llNoData;
    private TextView txtOrderNo, txtDate, txtInvoiceNo, txtOutlet, txtOmzet, txtStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_summary_detail);

        initialize();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(SummaryDetailActivity.this);
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
        mAdapter = new SummaryDetailAdapter(this, mList, header -> {
        });

        recyclerView.setAdapter(mAdapter);
    }

    private void getData() {
        mList = new ArrayList<>();
        orderHeader = SessionManagerQubes.getOrder();
        String idCust = Helper.isEmpty(orderHeader.getId_customer(), "");
        String nameCust = Helper.isEmpty(orderHeader.getNama(), "");
        if (!Helper.isNullOrEmpty(orderHeader.getOrder_date())) {
            String requestDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, orderHeader.getOrder_date());
            txtDate.setText(requestDate);
        } else {
            txtDate.setText("");
        }
        txtOrderNo.setText(format.format(orderHeader.getId()));
        txtInvoiceNo.setText(Helper.isEmpty(orderHeader.getSoNo(), ""));
        txtOmzet.setText("Rp. " + format.format(orderHeader.getOmzet()));
        txtOutlet.setText(idCust + " - " + nameCust);
//        txtStatus.setText(!Helper.isEmpty(orderHeader.getStatus()) ? orderHeader.getStatus() : "-");

        if (!Helper.isEmpty(orderHeader.getStatus())) {
            switch (orderHeader.getStatus()) {
                case Constants.STATUS_APPROVE:
                    llStatus.setVisibility(View.VISIBLE);
                    llStatus.setBackgroundTintList(ContextCompat.getColorStateList(SummaryDetailActivity.this, R.color.green3_aspp));
                    txtStatus.setText("Approve");
                    txtStatus.setTextColor(ContextCompat.getColor(SummaryDetailActivity.this, R.color.green_aspp));
                    break;
                case Constants.STATUS_REJECTED:
                    txtStatus.setText("Reject");
                    llStatus.setVisibility(View.VISIBLE);
                    llStatus.setBackgroundTintList(ContextCompat.getColorStateList(SummaryDetailActivity.this, R.color.red_aspp));
                    txtStatus.setTextColor(ContextCompat.getColor(SummaryDetailActivity.this, R.color.red2_aspp));
                    break;
                case Constants.STATUS_PENDING:
                    txtStatus.setText("Pending");
                    llStatus.setVisibility(View.VISIBLE);
                    llStatus.setBackgroundTintList(ContextCompat.getColorStateList(SummaryDetailActivity.this, R.color.yellow3_aspp));
                    txtStatus.setTextColor(ContextCompat.getColor(SummaryDetailActivity.this, R.color.yellow_aspp));
                    break;
                case Constants.STATUS_SYNC_SUCCESS:
                    txtStatus.setText("Sync Success");
                    llStatus.setVisibility(View.VISIBLE);
                    llStatus.setBackgroundTintList(ContextCompat.getColorStateList(SummaryDetailActivity.this, R.color.blue8_aspp));
                    txtStatus.setTextColor(ContextCompat.getColor(SummaryDetailActivity.this, R.color.aspp_blue9));
                    break;
                case Constants.STATUS_DRAFT:
                    txtStatus.setText("Draft");
                    llStatus.setVisibility(View.VISIBLE);
                    llStatus.setBackgroundTintList(ContextCompat.getColorStateList(SummaryDetailActivity.this, R.color.gray12_aspp));
                    txtStatus.setTextColor(ContextCompat.getColor(SummaryDetailActivity.this, R.color.black1_aspp));
                    break;
                default:
                    llStatus.setVisibility(View.GONE);
                    llStatus.setBackgroundTintList(null);
                    txtStatus.setText("-");
            }
        } else {
            llStatus.setVisibility(View.GONE);
            llStatus.setBackgroundTintList(null);
            txtStatus.setText("-");
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        llNoData = findViewById(R.id.llNoData);
        llStatus = findViewById(R.id.llStatus);
        txtStatus = findViewById(R.id.txtStatus);
        txtOutlet = findViewById(R.id.txtOutlet);
        txtOmzet = findViewById(R.id.txtOmzet);
        txtInvoiceNo = findViewById(R.id.txtInvoiceNo);
        txtOrderNo = findViewById(R.id.txtOrderNo);
        txtDate = findViewById(R.id.txtDate);
        progressCircle = findViewById(R.id.progressCircle);
        swipeLayout = findViewById(R.id.swipeLayout);
        imgLogOut = findViewById(R.id.imgLogOut);
        imgBack = findViewById(R.id.imgBack);
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
        if (SessionManagerQubes.getOrder() != null) {
            getData();
            setAdapter();

            if (mList == null || mList.isEmpty()) {
                requestData();
            }
        } else {
            onBackPressed();
            setToast("Gagal mengambil data");
        }
    }

    private void requestData() {
        recyclerView.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        PARAM = 1;
        new RequestUrl().execute();
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    Map request = new HashMap();
                    request.put("id", orderHeader.getId());
                    request.put("username", user.getUsername());
                    String URL_ = Constants.API_GET_SUMMARY_DETAIL;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, request);
                } else {
                    mList = new ArrayList<>();
                    Material[] paramArray = Helper.ObjectToGSON(resultWsMessage.getResult(), Material[].class);
                    if (paramArray != null) {
                        Collections.addAll(mList, paramArray);
                    }
                    List<Material> listMat = new ArrayList<>();
                    List<Discount> discList = new ArrayList<>();
                    Material[] matArray;
                    Discount[] disc;
                    double totalDiscount = 0, amount = 0;
                    for (Material param : mList) {
                        listMat = new ArrayList<>();
                        matArray = Helper.ObjectToGSON(param.getExtraItemObject(), Material[].class);
                        if (matArray != null) {
                            Collections.addAll(listMat, matArray);
                        }
                        param.setExtraItem(listMat);

                        discList = new ArrayList<>();
                        disc = Helper.ObjectToGSON(param.getDiskonListObject(), Discount[].class);
                        if (disc != null) {
                            Collections.addAll(discList, disc);
                        }
                        param.setDiskonList(discList);
                        totalDiscount = 0;
                        for (Discount discount : discList) {
                            amount = Double.parseDouble(discount.getValuediskon());
                            totalDiscount = totalDiscount + amount;
                        }
                        param.setTotalDiscount(totalDiscount);
                        param.setTotal(param.getPrice() - totalDiscount);

                        if (Helper.isNotEmptyOrNull(param.getExtraItem())) {
                            for (Material extra : param.getExtraItem()) {
                                discList = new ArrayList<>();
                                disc = Helper.ObjectToGSON(extra.getDiskonListObject(), Discount[].class);
                                if (disc != null) {
                                    Collections.addAll(discList, disc);
                                }
                                extra.setDiskonList(discList);
                                totalDiscount = 0;
                                for (Discount discount : discList) {
                                    amount = Double.parseDouble(discount.getValuediskon());
                                    totalDiscount = totalDiscount + amount;
                                }
                                extra.setTotalDiscount(totalDiscount);
                                extra.setTotal(extra.getPrice() - totalDiscount);
                            }
                        }
                    }
                    saveDataSuccess = true;
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("summarydetail", ex.getMessage());
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
                        new RequestUrl().execute();//2
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
                if (saveDataSuccess) {
                    mAdapter.setData(mList);
                    if (Helper.isNotEmptyOrNull(mList)) {
                        recyclerView.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    setToast(getString(R.string.failedSaveData));
                }
            }

        }
    }
}
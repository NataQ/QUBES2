package id.co.qualitas.qubes.fragment.aspp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.activity.aspp.StockRequestListActivity;
import id.co.qualitas.qubes.adapter.aspp.PromotionAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;

public class PromotionFragment extends BaseFragment {
    private PromotionAdapter mAdapter;
    private List<Promotion> mList;
    private LinearLayout llNoData;
    private WSMessage resultWsMessage;
    private boolean saveDataSuccess = false;
    private WSMessage logResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_promotion, container, false);
        getActivity().setTitle(getString(R.string.promotion));

        initProgress();
        initFragment();
        initialize();

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

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    setEnabled(false);
//                    ((MainActivity) getActivity()).changePage(1);
                    ((MainActivity) getActivity()).backPress();
                }
            }
        });

        return rootView;
    }

    private void setAdapter() {
        mAdapter = new PromotionAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initialize() {
        database = new Database(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = rootView.findViewById(R.id.llNoData);
        progressCircle = rootView.findViewById(R.id.progressCircle);
        swipeLayout = rootView.findViewById(R.id.swipeLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
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

        if (Helper.isEmptyOrNull(mList)) {
            requestData();
        }
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
        mList = database.getAllPromotion();
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_ = Constants.API_GET_PROMOTION_DASHBOARD;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else {
                    mList = new ArrayList<>();
                    Promotion[] paramArray = Helper.ObjectToGSON(resultWsMessage.getResult(), Promotion[].class);
                    if (paramArray != null) {
                        Collections.addAll(mList, paramArray);
                        database.deleteMasterPromotion();
                    }

                    for (Promotion param : mList) {
                        int idHeader = database.addMasterPromotion(param, user.getUsername());
                    }
                    getData();
                    saveDataSuccess = true;
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("Promotion", ex.getMessage());
                }
                if (PARAM == 2) {
                    saveDataSuccess = false;
                } else {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                    logResult.setMessage("Promotion error: " + exMess);
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage r) {
            if (PARAM == 1) {
                if (logResult != null) {
                    if (logResult.getIdMessage() == 1) {
                        String message = "Promotion : " + logResult.getMessage();
                        logResult.setMessage(message);

                        if (logResult.getResult() != null) {
                            resultWsMessage = logResult;
                            PARAM = 2;
                            new RequestUrl().execute();
                        } else {
                            progressCircle.setVisibility(View.GONE);
                            getData();
                            mAdapter.setData(mList);
                        }
                    }
                    database.addLog(logResult);
                } else {
                    progressCircle.setVisibility(View.GONE);
                    setToast(getString(R.string.serverError));
                }

                if (Helper.isEmptyOrNull(mList)) {
                    recyclerView.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }
            } else {
                progressCircle.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (saveDataSuccess) {
                    mAdapter.setData(mList);
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
    }
}
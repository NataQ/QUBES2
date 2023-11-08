package id.co.qualitas.qubes.fragment.aspp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private WSMessage resultWsMessage;
    private boolean saveDataSuccess = false;

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
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

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

        if (mList == null || mList.isEmpty()) {
            requestData();
        }
    }

    private void requestData() {
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
                    return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
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
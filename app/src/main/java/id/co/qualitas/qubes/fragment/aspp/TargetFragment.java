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

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.aspp.TargetAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Target;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class TargetFragment extends BaseFragment {
    private LinearLayout llNoData;
    private TargetAdapter mAdapter;
    private List<Target> mList;
    private WSMessage logResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_target, container, false);

        getActivity().setTitle(getString(R.string.target));

        initProgress();
        initFragment();
        initialize();

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
        mAdapter = new TargetAdapter(this, mList);
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
        Helper.setItemParam(Constants.CURRENTPAGE, "1");
    }

    private void getFirstDataOffline() {
        getData();
        if (mList == null || mList.isEmpty()) {
            requestData();
        } else {
            setAdapter();
        }
    }

    private void getData() {
        mList = new ArrayList<>();
//        mList = database.getTodayCustomer(currentLocation, false);
    }

    private void requestData() {
        progressCircle.setVisibility(View.VISIBLE);
        llNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        new RequestUrl().execute();
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                String URL_ = Constants.API_GET_TARGET_DASHBOARD;
                final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                return null;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("target", ex.getMessage());
                }
                logResult = new WSMessage();
                logResult.setIdMessage(0);
                logResult.setResult(null);
                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                logResult.setMessage("Target error: " + exMess);
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage wsMessage) {
            progressCircle.setVisibility(View.GONE);
            if (logResult.getIdMessage() == 1) {
                String message = "Target : " + logResult.getMessage();
                logResult.setMessage(message);
            }
            database.addLog(logResult);
            if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                mList = new ArrayList<>();
                List<Target> arrayList = new ArrayList<>();
                Target[] matArray = Helper.ObjectToGSON(logResult.getResult(), Target[].class);
                if (matArray != null) {
                    Collections.addAll(arrayList, matArray);
                }
                mList.addAll(arrayList);
                setAdapter();
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
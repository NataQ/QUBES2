package id.co.qualitas.qubes.fragment.aspp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.DepoRegion;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class DashboardFragment extends BaseFragment {
    TextView txtName, txtDriver, txtTodayDate, txtRoute, txtJabatanArea, txtCoverageArea;
    TextView txtAsset, txtAssetRoute, txtAssetNonRute;
    TextView txtECS, txtAT;
    TextView txtCallRute, txtCallNonRute, txtTotalCall, txtNonVisit;
    TextView txtTotalInvoiceAmount, txtPaymentAmount, txtOutstandingAmount;
    private WSMessage logResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_dashboard, container, false);
        getActivity().setTitle(getString(R.string.navmenu1));
        initialize();
        requestData();

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

    private void requestData() {
//        progressCircle.setVisibility(View.VISIBLE);
//        setDataDummyCustomer();
        PARAM = 1;
        new RequestUrl().execute();
    }

    private void initialize() {
        database = new Database(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        swipeLayout = rootView.findViewById(R.id.swipeLayout);
        progressCircle = rootView.findViewById(R.id.progressCircle);
        txtName = rootView.findViewById(R.id.txtName);
        txtDriver = rootView.findViewById(R.id.txtDriver);
        txtJabatanArea = rootView.findViewById(R.id.txtJabatanArea);
        txtRoute = rootView.findViewById(R.id.txtRoute);
        txtTodayDate = rootView.findViewById(R.id.txtTodayDate);
        txtCoverageArea = rootView.findViewById(R.id.txtCoverageArea);
        txtAsset = rootView.findViewById(R.id.txtAsset);
        txtAssetRoute = rootView.findViewById(R.id.txtAssetRoute);
        txtAssetNonRute = rootView.findViewById(R.id.txtAssetNonRute);
        txtECS = rootView.findViewById(R.id.txtECS);
        txtAT = rootView.findViewById(R.id.txtAT);
        txtCallRute = rootView.findViewById(R.id.txtCallRute);
        txtCallNonRute = rootView.findViewById(R.id.txtCallNonRute);
        txtTotalCall = rootView.findViewById(R.id.txtTotalCall);
        txtNonVisit = rootView.findViewById(R.id.txtNonVisit);
        txtTotalInvoiceAmount = rootView.findViewById(R.id.txtTotalInvoiceAmount);
        txtPaymentAmount = rootView.findViewById(R.id.txtPaymentAmount);
        txtOutstandingAmount = rootView.findViewById(R.id.txtOutstandingAmount);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
//            }
//        });
    }

    private void setData() {
        database = new Database(getContext());
        setFormatSeparator();
        txtTodayDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        txtRoute.setText(Helper.getTodayRoute());
        txtName.setText(Helper.isEmpty(user.getFull_name(), "-"));
        txtDriver.setText(Helper.isEmpty(user.getDriver_name(), "-"));
        txtJabatanArea.setText(getDepoRegion());

        int today = database.getCountRouteCustomer(true);
        int nonRoute = database.getCountNonRoute();
        txtAsset.setText(format.format(today + nonRoute));//all
        txtAssetRoute.setText(format.format(database.getCountRouteCustomer(false)));//route
        txtAssetNonRute.setText(format.format(user.getMax_visit()));//global parameter add non route

        txtECS.setText(format.format(database.getECS()));
        txtAT.setText(format.format(user.getAt()));

        txtTotalInvoiceAmount.setText(format.format(database.getTotalInvoiceAmount()));
        txtPaymentAmount.setText(format.format(database.getTotalPaymentAmount()));
        txtOutstandingAmount.setText(format.format(database.getTotalOutstandingAmount()));

        txtCallRute.setText(format.format(database.getCallRoute(1)));
        txtCallNonRute.setText(format.format(database.getCallRoute(2)));
        txtTotalCall.setText(format.format(database.getCallRoute(3)));
        txtNonVisit.setText(format.format(database.getCallRoute(4)));
    }

    private String getDepoRegion() {
        String depo = "";
        if (user.getDepoRegionList() != null) {
            for (int i = 0; i < user.getDepoRegionList().size(); i++) {
                DepoRegion depoRegion = user.getDepoRegionList().get(i);
                depo = depo + String.valueOf(depoRegion.getId_depo()) + " - " + depoRegion.getDepo_name() + " (" + String.valueOf(depoRegion.getId_region()) + " - " + depoRegion.getRegion_name() + ")";
                if (i != user.getDepoRegionList().size() - 1) {
                    depo = depo.concat("\n");
                }
            }
        }
        return depo;
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                String URL_ = Constants.API_GET_AT_DASHBOARD;
                final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                return null;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("dashboard", ex.getMessage());
                }
                logResult = new WSMessage();
                logResult.setIdMessage(0);
                logResult.setResult(null);
                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                logResult.setMessage("Dashboard error: " + exMess);
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage wsMessage) {
//            progressCircle.setVisibility(View.GONE);
            if (logResult != null) {
                if (logResult.getIdMessage() == 1) {
                    String message = "Dashboard : " + logResult.getMessage();
                    logResult.setMessage(message);

                    if (logResult.getResult() != null) {
                        Map res = (Map) logResult.getResult();
                        if (res != null) {
                            double at = (double) res.get("at");
                            user.setAt(at);
                            SessionManagerQubes.setUserProfile(user);
                            try {
                                txtAT.setText(format.format(user.getAt()));
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                database.addLog(logResult);
            }
        }
    }
}
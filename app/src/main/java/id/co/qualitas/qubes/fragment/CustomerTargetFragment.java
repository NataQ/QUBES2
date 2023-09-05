package id.co.qualitas.qubes.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.adapter.CustomerTargetAdapter;
import id.co.qualitas.qubes.adapter.SalesTargetAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.SpinnerTargetResponse;
import id.co.qualitas.qubes.model.TargetResponse;
import id.co.qualitas.qubes.model.TargetSummaryRequest;
import id.co.qualitas.qubes.model.User;

public class CustomerTargetFragment extends BaseFragment {
    private CustomerTargetAdapter cAdapter;
    private SalesTargetAdapter klAdapter;
    private SpinnerTargetResponse spinnerResponse;
    private List<String> nameCustList, codeCustList, divProdList;
//    private TabHost tabHost;

    private TargetSummaryRequest targetSummaryRequest;

    private boolean month = false;
    private Button btnKL, btnCustomer;
    private LinearLayout layoutKLProduct, layoutCustomerTarget;
    private ProgressBar progressBarCT, progressBarKL;
    private TextView errDataKL, errDataCT;
    private int type = 0;
    CardView btnFilterTarget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_monthly_sales_target_summary, container, false);

//        getActivity().setTitle(getString(R.string.navmenu2a));
//        ((MainActivityDrawer) getActivity()).enableBackToolbar(true);
        initialize();

//        functionTab();

        btnKL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 0;
                setTypeLayout();
                setDataRequest();
            }
        });

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 1;
                setTypeLayout();
                setDataRequest();
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
//        Helper.setItemParam(Constants.SELECTED_TAB, tabHost.getCurrentTabTag());
        Helper.setItemParam(Constants.SELECTED_TAB, type);
        super.onPause();
    }

    @Override
    public void onResume() {
        targetSummaryRequest = new TargetSummaryRequest();
        if (Helper.getItemParam(Constants.TARGET_SUMMARY_REQUEST) != null) {
            targetSummaryRequest = (TargetSummaryRequest) Helper.getItemParam(Constants.TARGET_SUMMARY_REQUEST);
        } else {
            targetSummaryRequest.setPeriod(Constants.MONTH);
            if (nameCustList != null && !nameCustList.isEmpty()) {
                targetSummaryRequest.setIdOutlet(Constants.EMPTY_STRING);
            }
            if (divProdList != null && !divProdList.isEmpty()) {
                targetSummaryRequest.setMatClass(Constants.EMPTY_STRING);
            }
        }

        if (Helper.getItemParam(Constants.SELECTED_TAB) != null) {
            if (Helper.getItemParam(Constants.SELECTED_TAB).equals(getResources().getString(R.string.klProduct))) {
//                tabHost.setCurrentTab(0);
                type = 0;
            } else if (Helper.getItemParam(Constants.SELECTED_TAB).equals(getResources().getString(R.string.customer))) {
//                tabHost.setCurrentTab(1);
                type = 1;
            }
            setTypeLayout();
//            if (tabHost.getCurrentTab() == 0) {//KlProduct
            setDataRequest();
        } else {
            getDataAll();
        }
        super.onResume();
    }

    private void setDataRequest() {
        if (type == 0) {//KlProduct
            if (targetSummaryRequest.getPeriod().equals(Constants.MONTH)) {
                PARAM = 1;
                month = true;
            } else {
                PARAM = 4;
                month = false;
            }
        } else {//Customer
            if (targetSummaryRequest.getPeriod().equals(Constants.MONTH)) {
                PARAM = 2;
                month = true;
            } else {
                PARAM = 5;
                month = false;
            }
        }
        new RequestUrl().execute();
    }

    private void setTypeLayout() {
        switch (type) {
            case 0:
                btnKL.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_target_selected));
                btnKL.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                btnCustomer.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_target_unselected));
                btnCustomer.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));
                layoutKLProduct.setVisibility(View.VISIBLE);
                layoutCustomerTarget.setVisibility(View.GONE);
                break;
            case 1:
                btnKL.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_target_unselected));
                btnKL.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));
                btnCustomer.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_target_selected));
                btnCustomer.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                layoutKLProduct.setVisibility(View.GONE);
                layoutCustomerTarget.setVisibility(View.VISIBLE);
                break;
        }
    }

//    private void functionTab() {
//        tabHost.getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                tabHost.setCurrentTab(0);
//
//                TextView tv = tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
//                tv.setTextColor(getResources().getColor(R.color.white));
//
//                if (targetSummaryRequest.getPeriod().equals(Constants.MONTH)) {
//                    PARAM = 1;
//                    month = true;
//                } else {
//                    PARAM = 4;
//                    month = false;
//                }
//
//                new RequestUrl().execute();
//            }
//        });
//
//
//        tabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tabHost.setCurrentTab(1);
//
//                TextView tv = tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
//                tv.setTextColor(getResources().getColor(R.color.white));
//
//                if (targetSummaryRequest.getPeriod().equals(Constants.MONTH)) {
//                    PARAM = 2;
//                    month = true;
//                } else {
//                    PARAM = 5;
//                    month = false;
//                }
//                new RequestUrl().execute();
//            }
//        });
//    }
//
//    public void setTabColor(TabHost tabHost) {
//        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
//            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white)); //unselected
//            TextView tv = tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
//            tv.setTextColor(getResources().getColor(R.color.grey2));
//
//            tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_selector);
//        }
//    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        btnKL = rootView.findViewById(R.id.btnKL);
        btnCustomer = rootView.findViewById(R.id.btnCustomer);
        layoutKLProduct = rootView.findViewById(R.id.layoutKLProduct);
        layoutCustomerTarget = rootView.findViewById(R.id.layoutCustomerTarget);
        mRecyclerView = rootView.findViewById(R.id.klproduct_target_recycler_view);
        mRecyclerView2 = rootView.findViewById(R.id.customer_target_recycler_view);
        progressBarCT = rootView.findViewById(R.id.progressBarCT);
        progressBarKL = rootView.findViewById(R.id.progressBarKL);
        errDataKL = rootView.findViewById(R.id.errDataKL);
        errDataCT = rootView.findViewById(R.id.errDataCT);
        btnFilterTarget = getActivity().findViewById(R.id.btnFilterTarget);

        btnFilterTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preventDupeDialog();
                Helper.setItemParam(Constants.ACTIVE_MENU, getResources().getString(R.string.navmenu2));
//                Helper.setItemParam(Constants.SELECTED_TAB, tabHost.getCurrentTabTag());
                Helper.setItemParam(Constants.SELECTED_TAB, type);
                Helper.setItemParam(Constants.TARGET_TYPE, Constants.CUSTOMER_TARGET);
                openDialog(23);
            }
        });

//        RelativeLayout l1 = rootView.findViewById(R.id.tab1);
//        View v1 = getActivity().getLayoutInflater().inflate(R.layout.fragment_klproduct_target, null);
//        l1.addView(v1);
//
//        RelativeLayout l2 = rootView.findViewById(R.id.tab2);
//        View v2 = getActivity().getLayoutInflater().inflate(R.layout.fragment_customer_target, null);
//        l2.addView(v2);
//
//        tabHost = rootView.findViewById(R.id.tabHost);
//        tabHost.setup();
//
//        tabHost.setCurrentTab(0);
//
//        setTab(getResources().getString(R.string.klProduct), R.id.tab1, getResources().getString(R.string.klProduct));
//        setTab(getResources().getString(R.string.customer), R.id.tab2, getResources().getString(R.string.customer));
//
//        setTabColor(tabHost);
//        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String s) {
//                setTabColor(tabHost);
//            }
//        });
//
//        tabHost.getTabWidget().setStripEnabled(false);
//
//        mRecyclerView = v1.findViewById(R.id.klproduct_target_recycler_view);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView2 = v2.findViewById(R.id.customer_target_recycler_view);
//        mLayoutManager2 = new LinearLayoutManager(getActivity());
//
//        progressBarKL = v1.findViewById(R.id.progressBar);
//        errDataKL = v1.findViewById(R.id.errDataKL);
//        progressBarCT = v2.findViewById(R.id.progressBar);
//        errDataCT = v2.findViewById(R.id.errDataKL);
    }

//    public void setTab(String tag, int id, CharSequence label) {
//        TabHost.TabSpec spec = tabHost.newTabSpec(tag);
//        spec.setContent(id);
//        spec.setIndicator(label);
//        tabHost.addTab(spec);
//    }

    private void settingDisplay() {
        PARAM = 3;
        new RequestUrl().execute();
        errDataKL.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        progressBarKL.setVisibility(View.VISIBLE);
    }

    public void goToTargetDetailFragment() {
        ((NewMainActivity) getActivity()).changePage(22);
    }

    private class RequestUrl extends AsyncTask<Void, Void, TargetResponse[]> {

        @Override
        protected TargetResponse[] doInBackground(Void... voids) {
            try {
                Calendar cal = Helper.todayDate();
                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);
                String idEmployee = Constants.QUESTION.concat(Constants.EMPLOYEE_ID.concat(Constants.EQUALS)) + user.getIdEmployee();
                String bulan = Constants.MONTH.concat(Constants.EQUALS).concat(String.valueOf(month));
                String tahun = Constants.YEAR.concat(Constants.EQUALS).concat(String.valueOf(year));

                targetSummaryRequest.setIdEmployee(user.getIdEmployee());

                if (targetSummaryRequest.getMatClass() == null) {
                    targetSummaryRequest.setMatClass(Constants.EMPTY_STRING);
                }

                if (targetSummaryRequest.getIdOutlet() == null) {
                    targetSummaryRequest.setIdOutlet(Constants.EMPTY_STRING);
                }

                String url;
                if (PARAM == 1) {//klasifikasi monthly
                    String URL_GET_MONTHLY_TARGET_BY_KLASIFIKASI = Constants.API_GET_MONTH_TARGET_BY_KLASIFIKASI;

                    url = Constants.URL.concat(URL_GET_MONTHLY_TARGET_BY_KLASIFIKASI);
                    return (TargetResponse[]) Helper.postWebserviceWithBody(url, TargetResponse[].class, targetSummaryRequest);
                } else if (PARAM == 2) {//customer monthly
                    String URL_GET_MONTHLY_TARGET_BY_OUTLET = Constants.API_GET_MONTH_TARGET_BY_OUTLET;

                    url = Constants.URL.concat(URL_GET_MONTHLY_TARGET_BY_OUTLET);
                    return (TargetResponse[]) Helper.postWebserviceWithBody(url, TargetResponse[].class, targetSummaryRequest);
                } else if (PARAM == 3) {
                    String URL_GET_LIST_SPINNER = Constants.API_GET_LIST_SPINNER;
                    url = Constants.URL.concat(URL_GET_LIST_SPINNER).concat(idEmployee).concat(Constants.AND).concat(bulan).concat(Constants.AND).concat(tahun);
                    spinnerResponse = (SpinnerTargetResponse) Helper.getWebservice(url, SpinnerTargetResponse.class);
                    return null;
                } else if (PARAM == 4) {//klasifikasi daily
                    String URL_GET_DAILY_TARGET_BY_KLASIFIKASI = Constants.API_GET_DAILY_TARGET_BY_KLASIFIKASI;

                    url = Constants.URL.concat(URL_GET_DAILY_TARGET_BY_KLASIFIKASI);
                    return (TargetResponse[]) Helper.postWebserviceWithBody(url, TargetResponse[].class, targetSummaryRequest);
                } else {//customer daily
                    String URL_GET_DAILY_TARGET_BY_OUTLET = Constants.API_GET_DAILY_TARGET_BY_OUTLET;

                    url = Constants.URL.concat(URL_GET_DAILY_TARGET_BY_OUTLET);
                    return (TargetResponse[]) Helper.postWebserviceWithBody(url, TargetResponse[].class, targetSummaryRequest);
                }
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            progressBarKL.setVisibility(View.VISIBLE);
            progressBarCT.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mRecyclerView2.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(TargetResponse[] targetResponses) {

            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView2.setVisibility(View.VISIBLE);
            progressBarKL.setVisibility(View.GONE);
            progressBarCT.setVisibility(View.GONE);
            errDataKL.setVisibility(View.GONE);
            errDataCT.setVisibility(View.GONE);

            if (PARAM == 1 || PARAM == 4) {
                if (targetResponses != null) {
                    progressBarKL.setVisibility(View.GONE);
                    errDataKL.setVisibility(View.GONE);
                    ArrayList<TargetResponse> targetResponseArrayList = new ArrayList<>();

                    Collections.addAll(targetResponseArrayList, targetResponses);

                    if (!targetResponseArrayList.isEmpty()) {
                        boolean month = true;
                        month = PARAM == 1;

                        //ganti disini

                        setData(targetResponseArrayList, month);

                        mRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        errDataKL.setVisibility(View.VISIBLE);
                        progressBarKL.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);
                    }


                } else {
                    errDataKL.setVisibility(View.VISIBLE);
                    progressBarKL.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                }

            } else if (PARAM == 2 || PARAM == 5) {
                if (targetResponses != null) {
                    progressBarCT.setVisibility(View.GONE);
                    errDataCT.setVisibility(View.GONE);
                    ArrayList<TargetResponse> targetResponseArrayList = new ArrayList<>();

                    Collections.addAll(targetResponseArrayList, targetResponses);

                    if (!targetResponseArrayList.isEmpty()) {
                        boolean month = true;
                        month = PARAM == 2;

                        setDataCustomer(targetResponseArrayList, month);

                        mRecyclerView2.setVisibility(View.VISIBLE);
                    } else {
                        errDataCT.setVisibility(View.VISIBLE);
                        progressBarCT.setVisibility(View.GONE);
                        mRecyclerView2.setVisibility(View.GONE);
                    }
                } else {
                    errDataCT.setVisibility(View.VISIBLE);
                    progressBarCT.setVisibility(View.GONE);
                    mRecyclerView2.setVisibility(View.GONE);
                }
            } else if (PARAM == 3) {
                if (spinnerResponse != null) {
                    if (spinnerResponse.getListCustomer() != null && spinnerResponse.getListCustomer().size() != 0) {
                        nameCustList = new ArrayList<>();
                        codeCustList = new ArrayList<>();
                        nameCustList.add(0, getString(R.string.allCaps));

                        for (int i = 0; i < spinnerResponse.getListCustomer().size(); i++) {
                            nameCustList.add(spinnerResponse.getListCustomer().get(i).getName());
                            codeCustList.add(spinnerResponse.getListCustomer().get(i).getIdOutlet());
                        }
                        Helper.setItemParam(Constants.ITEM_CUSTOMER_NAME, nameCustList);
                        Helper.setItemParam(Constants.ITEM_CUSTOMER_ID, codeCustList);
                    }

                    if (spinnerResponse.getMatClass() != null && spinnerResponse.getMatClass().size() != 0) {
                        divProdList = new ArrayList<>();
                        divProdList.add(0, getString(R.string.allCaps));

                        for (int i = 0; i < spinnerResponse.getMatClass().size(); i++) {
                            divProdList.add(i + 1, spinnerResponse.getMatClass().get(i));
                        }
                        Helper.setItemParam(Constants.ITEM_DIV_PROD, divProdList);
                    }


                    PARAM = 1;
                    new RequestUrl().execute();

                }
            }
        }
    }

    private void setData(final ArrayList<TargetResponse> targetResponseArrayList, boolean month) {
        klAdapter = new SalesTargetAdapter(targetResponseArrayList, month, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(klAdapter);
        klAdapter.notifyDataSetChanged();
    }

    private void setDataCustomer(final ArrayList<TargetResponse> targetResponseArrayList, boolean month) {
        cAdapter = new CustomerTargetAdapter(targetResponseArrayList, month, this);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView2.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();
    }

    private void getDataAll() {
//        tabHost.setCurrentTab(0);
        type = 0;
        setTypeLayout();
        settingDisplay();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem save = menu.findItem(R.id.action_save);
        final MenuItem filter = menu.findItem(R.id.action_filter);
        final MenuItem search = menu.findItem(R.id.action_search);
        final MenuItem edit = menu.findItem(R.id.action_edit);
        final MenuItem setting = menu.findItem(R.id.action_settings);
        final MenuItem next = menu.findItem(R.id.action_next);

        save.setVisible(false);
        filter.setVisible(true);
        search.setVisible(false);
        edit.setVisible(false);
        setting.setVisible(true);
        next.setVisible(false);

        filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                preventDupeDialog();
//                Helper.setItemParam(Constants.SELECTED_TAB, tabHost.getCurrentTabTag());
                Helper.setItemParam(Constants.SELECTED_TAB, type);
                Helper.setItemParam(Constants.TARGET_TYPE, Constants.CUSTOMER_TARGET);
                openDialog(23);
                return false;
            }
        });
    }
}
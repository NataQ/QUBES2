package id.co.qualitas.qubes.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.adapter.ObjectTargetAdapterv2;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.SpinnerTargetResponse;
import id.co.qualitas.qubes.model.TargetResponse;
import id.co.qualitas.qubes.model.TargetSummaryRequest;
import id.co.qualitas.qubes.model.User;

public class ObjectTargetFragmentv2 extends BaseFragment {

    private ObjectTargetAdapterv2 mAdapter;

    private ArrayList<TargetResponse> list = new ArrayList<>();

    private int totalTarget = 0, totalActual = 0, totalGap = 0, gap = 0;

    private RecyclerView mRecyclerView;
    private TextView txtTarget, txtActual, txtGap, errData;
    private Button btnCall, btnEc, btnEa;

    private TargetSummaryRequest targetSummaryRequest;

    private ProgressBar spinner;
    private SpinnerTargetResponse spinnerResponse;
    private int posActive = 0;
    private String url;

    private List<String> nameCustList, codeCustList, divProdList;
    private CardView btnFilterTarget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_object_target_v2, container, false);

        getActivity().setTitle(getString(R.string.navmenu2a));
//        ((MainActivityDrawer) getActivity()).enableBackToolbar(true);

        initialize();

        setData();

        btnFilterTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preventDupeDialog();
                Helper.setItemParam(Constants.ACTIVE_MENU, getResources().getString(R.string.navmenu2));
                Helper.setItemParam(Constants.SELECTED_TAB, String.valueOf(posActive));
                Helper.setItemParam(Constants.TARGET_TYPE, Constants.OBJECT_TARGET);
                openDialog(23);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonState(0);
                posActive = 0;

                PARAM = 1;
                new RequestUrl().execute();
            }
        });

        btnEc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeButtonState(1);

                posActive = 1;
                PARAM = 2;
                new RequestUrl().execute();
            }
        });

        btnEa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeButtonState(2);

                posActive = 2;

                PARAM = 3;
                new RequestUrl().execute();

            }
        });

        return rootView;
    }


    private void changeButtonState(int pos) {
        switch (pos) {
            case 0:
                btnCall.setSelected(true);
                btnEc.setSelected(false);
                btnEa.setSelected(false);
                break;
            case 1:
                btnCall.setSelected(false);
                btnEc.setSelected(true);
                btnEa.setSelected(false);
                break;
            case 2:
                btnCall.setSelected(false);
                btnEc.setSelected(false);
                btnEa.setSelected(true);
                break;
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        btnFilterTarget = getActivity().findViewById(R.id.btnFilterTarget);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        txtTarget = rootView.findViewById(R.id.txtTarget);
        txtActual = rootView.findViewById(R.id.txtActual);
        txtGap = rootView.findViewById(R.id.txtGap);
        btnCall = rootView.findViewById(R.id.btnCall);
        btnEc = rootView.findViewById(R.id.btnEc);
        btnEa = rootView.findViewById(R.id.btnEa);
        spinner = rootView.findViewById(R.id.progressBar);
        errData = rootView.findViewById(R.id.errData);
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

                if (PARAM == 1) {
                    targetSummaryRequest.setType(Constants.TYPE_CALL);
                    url = Constants.URL.concat(Constants.API_GET_TARGET_CALL);
                    return (TargetResponse[]) Helper.postWebserviceWithBody(url, TargetResponse[].class, targetSummaryRequest);

                } else if (PARAM == 2) {
                    targetSummaryRequest.setType(Constants.TYPE_EC);
                    url = Constants.URL.concat(Constants.API_GET_TARGET_EC);
                    return (TargetResponse[]) Helper.postWebserviceWithBody(url, TargetResponse[].class, targetSummaryRequest);
                } else if (PARAM == 3) {
                    targetSummaryRequest.setType(Constants.TYPE_EA);
                    url = Constants.URL.concat(Constants.API_GET_TARGET_EA);
                    return (TargetResponse[]) Helper.postWebserviceWithBody(url, TargetResponse[].class, targetSummaryRequest);
                } else {
                    url = Constants.URL.concat(Constants.API_GET_LIST_SPINNER).concat(idEmployee).concat(Constants.AND).concat(bulan).concat(Constants.AND).concat(tahun);
                    spinnerResponse = (SpinnerTargetResponse) Helper.getWebservice(url, SpinnerTargetResponse.class);
                    return null;
                }
            } catch (Exception ex) {
                //connection = true;
                if (ex.getMessage() != null) {
                    Log.e("Target", ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(TargetResponse[] targetResponses) {
            if (PARAM != 4) {
                if (targetResponses != null) {
                    spinner.setVisibility(View.GONE);
                    errData.setVisibility(View.GONE);
                    ArrayList<TargetResponse> targetResponseArrayList = new ArrayList<>();

                    Collections.addAll(targetResponseArrayList, targetResponses);
                    setData(targetResponseArrayList, String.valueOf(posActive), "", targetSummaryRequest.getType());
                    mRecyclerView.setVisibility(View.VISIBLE);

                    BigDecimal totalTarget = BigDecimal.ZERO;
                    BigDecimal totalActual = BigDecimal.ZERO;
                    BigDecimal totalGap = BigDecimal.ZERO;

                    for(int i = 0 ; i < targetResponseArrayList.size(); i++){
                        if(targetResponseArrayList.get(i).getTarget() != null){
                            totalTarget = totalTarget.add(targetResponseArrayList.get(i).getTarget());
                        }

                        if(targetResponseArrayList.get(i).getActual() != null){
                            totalActual = totalActual.add(targetResponseArrayList.get(i).getActual());
                        }
                    }

                    txtTarget.setText(String.valueOf(totalTarget.setScale(0, BigDecimal.ROUND_HALF_UP)));

                    txtActual.setText(String.valueOf(totalActual));

                    if(totalTarget.compareTo(totalActual) == 1){
                        txtGap.setText(String.valueOf(totalTarget.setScale(0, BigDecimal.ROUND_HALF_UP).subtract(totalActual)));
                    }else{
                        txtGap.setText(Constants.ZERO);
                    }

                } else {
                    errData.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            } else {
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

    private void setData() {
        txtTarget.setText(String.valueOf(totalTarget));
        txtActual.setText(String.valueOf(totalActual));
        txtGap.setText(String.valueOf(totalGap));

        changeButtonState(0);
    }

    private void setData(final ArrayList<TargetResponse> targetResponseArrayList, String produk, String outlet, String type) {
        mAdapter = new ObjectTargetAdapterv2(targetResponseArrayList, produk, outlet, type);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

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
            switch (String.valueOf(Helper.getItemParam(Constants.SELECTED_TAB))) {
                case "0":
                    PARAM = 1;
                    break;
                case "1":
                    PARAM = 2;
                    break;
                case "2":
                    PARAM = 3;
                    break;
            }
        } else {
            PARAM = 4;
        }

        new RequestUrl().execute();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        Helper.setItemParam(Constants.SELECTED_TAB, String.valueOf(posActive));
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
        search.setVisible(false);
        edit.setVisible(false);
        next.setVisible(false);
        filter.setVisible(true);

        filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                preventDupeDialog();
                Helper.setItemParam(Constants.ACTIVE_MENU, getResources().getString(R.string.navmenu2));
                Helper.setItemParam(Constants.SELECTED_TAB, String.valueOf(posActive));
                Helper.setItemParam(Constants.TARGET_TYPE, Constants.OBJECT_TARGET);
                openDialog(23);
                return false;
            }
        });
    }
}
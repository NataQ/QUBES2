package id.co.qualitas.qubes.fragment.aspp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.aspp.RouteCustomerAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class RouteCustomerFragment extends BaseFragment implements LocationListener {
    private RouteCustomerAdapter mAdapter;
    private List<Customer> mList, mListFiltered;
    private Button btnCoverage;
    private EditText edtSearch;
    private Spinner spinnerRouteCustomer;
    private WSMessage resultWsMessage;
    private boolean saveDataSuccess = false;

    private boolean isLocationPermissionGranted = false;
    private LocationManager lm;
    private Location currentLocation = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_route_customer, container, false);

        getActivity().setTitle(getString(R.string.routeCustomer));

        initialize();
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
        checkLocationPermission();

        btnCoverage.setOnClickListener(v -> {
            ((MainActivity) getActivity()).changePage(23);
//            Intent intent = new Intent(getActivity(), CoverageActivity.class);
//            startActivity(intent);
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("Route");
        listSpinner.add("All");
        setSpinnerAdapter3(listSpinner, spinnerRouteCustomer);

//        spnRouteCustAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, listSpinner);
//        spnRouteCustAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerRouteCustomer.setAdapter(spnRouteCustAdapter);
        spinnerRouteCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                edtSearch.setText(null);
                if (mList != null) {
                    if (position == 0) {
                        filterData(false);//spinner false
                    } else {
                        filterData(true);//spinner true
                    }
//                    mAdapter.setData(mListFiltered);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

    public void moveDirection(Customer headerCustomer) {
        if (Utils.isGPSOn(getContext())) {
            SessionManagerQubes.setRouteCustomerHeader(headerCustomer);
            ((MainActivity) getActivity()).changePage(24);
        } else {
            Utils.turnOnGPS(getActivity());
        }
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        spinnerRouteCustomer = rootView.findViewById(R.id.spinnerRouteCustomer);
        edtSearch = rootView.findViewById(R.id.edtSearch);
        btnCoverage = rootView.findViewById(R.id.btnCoverage);
        swipeLayout = rootView.findViewById(R.id.swipeLayout);
        progressCircle = rootView.findViewById(R.id.progressCircle);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setHasFixedSize(true);
    }

    private void filterData(boolean all) {
        mListFiltered = new ArrayList<>();

        if (all) {
            mListFiltered.addAll(mList);
        } else {
            for (Customer routeCustomer : mList) {
                if (routeCustomer.isRoute()) {
                    mListFiltered.add(routeCustomer);
                }
            }
        }
        setAdapter();
    }

    private void getFirstDataOffline() {
        getData();
        if (mList == null || mList.isEmpty()) {
            requestData();
        } else {
            filterData(false);//getFirstDataOffline
//            mAdapter.setData(mListFiltered);
        }
    }

    private void requestData() {
        progressCircle.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        PARAM = 1;
        new RequestUrl().execute();
    }

    private void setAdapter() {
        mAdapter = new RouteCustomerAdapter(this, mListFiltered);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFirstDataOffline();
        Helper.setItemParam(Constants.CURRENTPAGE, "2");
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_ = Constants.API_GET_TODAY_CUSTOMER;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                } else {
                    mList = new ArrayList<>();
                    List<Customer> mListNonRoute = new ArrayList<>();
                    Map result = (Map) resultWsMessage.getResult();
                    if (result.get("visit") != null) {
                        LinkedTreeMap startDay = (LinkedTreeMap) result.get("visit");
                        double id = (double) startDay.get("id");
                        SessionManagerQubes.setStartDay((int) id);
                    } else {
                        SessionManagerQubes.setStartDay(0);
                    }
                    Customer[] param1Array = Helper.ObjectToGSON(result.get("customerNonRoute"), Customer[].class);
                    Collections.addAll(mListNonRoute, param1Array);
                    database.deleteMasterNonRouteCustomer();
                    database.deleteMasterNonRouteCustomerPromotion();

                    for (Customer param : mListNonRoute) {
                        List<Promotion> arrayList = new ArrayList<>();
                        Promotion[] matArray = Helper.ObjectToGSON(param.getPromoList(), Promotion[].class);
                        Collections.addAll(arrayList, matArray);
                        param.setPromoList(arrayList);

                        int idHeader = database.addNonRouteCustomer(param, user.getUserLogin());
                        for (Promotion mat : arrayList) {
                            database.addNonRouteCustomerPromotion(mat, String.valueOf(idHeader), user.getUserLogin());
                        }
                    }

                    Customer[] paramArray = Helper.ObjectToGSON(result.get("todayCustomer"), Customer[].class);
                    Collections.addAll(mList, paramArray);
                    database.deleteCustomer();
                    database.deleteCustomerPromotion();

                    for (Customer param : mList) {
                        List<Promotion> arrayList = new ArrayList<>();
                        Promotion[] matArray = Helper.ObjectToGSON(param.getPromoList(), Promotion[].class);
                        Collections.addAll(arrayList, matArray);
                        param.setPromoList(arrayList);

                        int idHeader = database.addCustomer(param, user.getUserLogin());
                        for (Promotion mat : arrayList) {
                            database.addCustomerPromotion(mat, String.valueOf(idHeader), user.getUserLogin());
                        }
                    }

                    getData();
                    saveDataSuccess = true;
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("routeCustomer", ex.getMessage());
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
        protected void onPostExecute(WSMessage WsMessage) {
            if (PARAM == 1) {
                if (WsMessage != null) {
                    if (WsMessage.getIdMessage() == 1) {
                        resultWsMessage = WsMessage;
                        PARAM = 2;
                        new RequestUrl().execute();
                    } else {
                        progressCircle.setVisibility(View.GONE);
                        setToast(WsMessage.getMessage());
                    }
                } else {
                    progressCircle.setVisibility(View.GONE);
                    setToast(getString(R.string.failedGetData));
                }
            } else {
                progressCircle.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (saveDataSuccess) {
                    filterData(false);//request url
//                    mAdapter.setData(mListFiltered);
                } else {
                    setToast(getString(R.string.failedSaveData));
                }
            }
        }
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllCustomerVisit(currentLocation, false);
    }

    private void checkLocationPermission() {
        List<String> permissionRequest = new ArrayList<>();
        isLocationPermissionGranted = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!isLocationPermissionGranted)
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (isLocationPermissionGranted) {
            if (!Helper.isGPSOn(getActivity())) {
                setToast("Please turn on GPS");
                Helper.turnOnGPS(getActivity());
            } else {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
                if (currentLocation == null) {
//                    setToast("Can't get your location.. Please try again..");
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }
}
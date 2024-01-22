package id.co.qualitas.qubes.fragment.aspp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.aspp.RouteCustomerAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.AddressResultReceiver;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.interfaces.LocationRequestCallback;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Role;
import id.co.qualitas.qubes.model.StartVisit;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class RouteCustomerFragment extends BaseFragment {
    private RouteCustomerAdapter mAdapter;
    private List<Customer> mList, mListFiltered;
    private Button btnCoverage;
    private EditText edtSearch;
    private Spinner spinnerRouteCustomer;
    private LinearLayout llNoData;
    private WSMessage resultWsMessage;
    private WSMessage logResult;
    private boolean saveDataSuccess = false;
    private boolean isLocationPermissionGranted = false;
    //location
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private LocationRequestCallback<String, Location> addressCallback;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean allCustomer = false;

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
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(2000)
                .build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
            }
        };
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
        getCurrentAddress();

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
//                if (SessionManagerQubes.getStartDay() == 0 || SessionManagerQubes.getStartDay() == 2) {
                getFirstDataOffline();
//                } else {
//                    setToast("Sudah start visit");
//                }
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
                        allCustomer = false;
                    } else {
                        allCustomer = true;
                    }
                    filterData(allCustomer);//spinner false
//                    mAdapter.setData(mListFiltered);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

    @SuppressWarnings("MissingPermission")
    private void getCurrentAddress() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                mLastLocation = location;
            }
        });
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
        database = new Database(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = rootView.findViewById(R.id.llNoData);
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

        if (Helper.isEmptyOrNull(mListFiltered)) {
            recyclerView.setVisibility(View.GONE);
            llNoData.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
        }
    }

    private void getFirstDataOffline() {
        getData();
        filterData(allCustomer);//getFirstDataOffline
//        if (mList == null || mList.isEmpty()) {
//            requestData();
//        } else {
//            filterData(false);//getFirstDataOffline
////            mAdapter.setData(mListFiltered);
//        }
    }

    private void requestData() {
        progressCircle.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
//        setDataDummyCustomer();
        PARAM = 1;
        new RequestUrl().execute();//1
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
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else {
                    mList = new ArrayList<>();
                    List<Customer> mListNonRoute = new ArrayList<>();
                    Map result = (Map) resultWsMessage.getResult();

//                    if (result.get("visit") != null) {
//                        StartVisit startDay = Helper.ObjectToGSON(result.get("visit"), StartVisit.class);
//                        database.addStartVisit(startDay);
////                        SessionManagerQubes.setStartDay(startDay);
//                    } else {
////                        SessionManagerQubes.setStartDay(null);
//                    }

//                    if (result.get("visit") != null) {
//                        LinkedTreeMap startDay = (LinkedTreeMap) result.get("visit");
//                        double id = (double) startDay.get("id");
//                        SessionManagerQubes.setStartDay((int) id);
//                    } else {
//                        SessionManagerQubes.setStartDay(0);
//                    }
                    Customer[] param1Array = Helper.ObjectToGSON(result.get("customerNonRoute"), Customer[].class);
                    if (param1Array != null) {
                        Collections.addAll(mListNonRoute, param1Array);
                        database.deleteMasterNonRouteCustomer();
                        database.deleteMasterNonRouteCustomerPromotion();
                        database.deleteMasterNonRouteCustomerDct();
                    }

                    for (Customer param : mListNonRoute) {
                        List<Promotion> arrayList = new ArrayList<>();
                        Promotion[] matArray = Helper.ObjectToGSON(param.getPromoList(), Promotion[].class);
                        if (matArray != null) {
                            Collections.addAll(arrayList, matArray);
                            param.setPromoList(arrayList);
                        }

                        List<Material> arrayDctList = new ArrayList<>();
                        Material[] dctArray = Helper.ObjectToGSON(param.getDctList(), Material[].class);
                        if (dctArray != null) {
                            Collections.addAll(arrayDctList, dctArray);
                        }
                        param.setDctList(arrayDctList);

                        int idHeader = database.addNonRouteCustomer(param, user.getUsername());
                        for (Promotion mat : arrayList) {
                            database.addNonRouteCustomerPromotion(mat, String.valueOf(idHeader), user.getUsername());
                        }
                        for (Material mat : arrayDctList) {
                            database.addNonRouteCustomerDct(mat, String.valueOf(idHeader), user.getUsername(), param.getId());
                        }
                    }

                    Customer[] paramArray = Helper.ObjectToGSON(result.get("todayCustomer"), Customer[].class);
                    if (paramArray != null) {
                        Collections.addAll(mList, paramArray);
                        database.deleteCustomer();
                        database.deleteCustomerPromotion();
                        database.deleteCustomerDct();
                        database.deleteVisitSalesman();
                        database.deleteNoo();
                    }

                    for (Customer param : mList) {
                        List<Promotion> arrayList = new ArrayList<>();
                        Promotion[] matArray = Helper.ObjectToGSON(param.getPromoList(), Promotion[].class);
                        if (matArray != null) {
                            Collections.addAll(arrayList, matArray);
                        }
                        param.setPromoList(arrayList);

                        List<Material> arrayDctList = new ArrayList<>();
                        Material[] dctArray = Helper.ObjectToGSON(param.getDctList(), Material[].class);
                        if (dctArray != null) {
                            Collections.addAll(arrayDctList, dctArray);
                        }
                        param.setDctList(arrayDctList);
                        int idHeader = database.addCustomer(param, user.getUsername());
                        for (Promotion mat : arrayList) {
                            database.addCustomerPromotion(mat, String.valueOf(idHeader), user.getUsername());
                        }

                        for (Material mat : arrayDctList) {
                            database.addCustomerDct(mat, param.getId(), user.getUsername());
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
                } else {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setMessage("Route Customer error: " + ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage re) {
            if (PARAM == 1) {
                if (logResult.getIdMessage() == 1) {
                    String message = "Route Customer : " + logResult.getMessage();
                    logResult.setMessage(message);
                }
                database.addLog(logResult);
                if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                    resultWsMessage = logResult;
                    PARAM = 2;
                    new RequestUrl().execute();//2
                } else {
                    progressCircle.setVisibility(View.GONE);
                    getData();
                    filterData(allCustomer);//no data request failed
                }
            } else {
                progressCircle.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (saveDataSuccess) {
                    filterData(allCustomer);//request url
                } else {
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

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getRouteCustomer(mLastLocation, false, 0, null);
    }

}
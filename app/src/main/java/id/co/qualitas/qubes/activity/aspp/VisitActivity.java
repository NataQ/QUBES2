package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerCustomerAdapter;
import id.co.qualitas.qubes.adapter.aspp.NooListAdapter;
import id.co.qualitas.qubes.adapter.aspp.ReasonNotVisitAdapter;
import id.co.qualitas.qubes.adapter.aspp.VisitListAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.CustomerNoo;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.LashPdfUtils;
import id.co.qualitas.qubes.utils.Utils;

public class VisitActivity extends BaseActivity implements LocationListener {
    private VisitListAdapter mAdapterVisit;
    private NooListAdapter mAdapterNoo;
    private List<Customer> mList, mListNonRoute;
    private List<CustomerNoo> mListNoo;
    private MovableFloatingActionButton btnAddVisit, btnAddNoo;
    private Button btnEndDay, btnNextDay, btnStartVisit, btnEndVisit;
    private TextView txtVisit, txtVisitLine, txtNOO, txtNOOLine;
    private EditText edtSearchVisit, edtSearchNoo;
    private LinearLayout llVisit, llNoo;
    private RelativeLayout rlInap, rlDaily;
    private RecyclerView recyclerViewVisit, recyclerViewNoo;
    private boolean isLocationPermissionGranted = false;
    private LocationManager lm;
    private Location currentLocation = null;
    private Customer outletClicked;
    private CustomerNoo outletNooClicked;
    private LashPdfUtils pdfUtils;
    private File pdfFile;
    private Boolean success = false;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private final static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private SwipeRefreshLayout swipeLayoutNoo, swipeLayoutVisit;
    private ProgressBar progressCircleNoo, progressCircleVisit;
    private WSMessage resultWsMessage;
    private boolean saveDataSuccess = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_visit);

        //set Map
        Configuration.getInstance().load(VisitActivity.this, PreferenceManager.getDefaultSharedPreferences(VisitActivity.this));
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(VisitActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(VisitActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);

        initialize();
//        initData();
        setViewVisit();
        setViewNoo();

        btnNextDay.setOnClickListener(v -> {
            openDialogReasonNotVisit();
        });

        btnEndDay.setOnClickListener(v -> {
            if (checkPermission()) {
                new AsyncTaskGeneratePDF().execute();
            } else {
                setToast(getString(R.string.pleaseEnablePermission));
                requestPermission();
            }
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(VisitActivity.this);
        });
    }

    private void setViewVisit() {
        txtVisit.setOnClickListener(v -> {
            txtVisit.setTextColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtNOO.setTextColor(ContextCompat.getColor(this, R.color.gray8_aspp));
            txtVisitLine.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtNOOLine.setBackgroundColor(ContextCompat.getColor(this, R.color.space_transparent));

            llVisit.setVisibility(View.VISIBLE);
            llNoo.setVisibility(View.GONE);
        });

        btnStartVisit.setOnClickListener(v -> {
            openDialogStartVisit();
        });

        btnEndVisit.setOnClickListener(v -> {
            openDialogEndVisit();
        });

        btnAddVisit.setOnClickListener(v -> {
            openDialogAdd();
        });

        swipeLayoutVisit.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                setAdapterVisit();
                swipeLayoutVisit.setRefreshing(false);
            }
        });

        edtSearchVisit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapterVisit != null) {
                    mAdapterVisit.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnAddVisit.setOnClickListener(v -> {
            openDialogAdd();
        });
    }

    private void setAdapterVisit() {
        mAdapterVisit = new VisitListAdapter(this, mList, header -> {
            outletClicked = header;
            checkLocationPermission();
        });

        recyclerViewVisit.setAdapter(mAdapterVisit);
    }

    private void checkLocationPermission() {
        List<String> permissionRequest = new ArrayList<>();
        isLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!isLocationPermissionGranted)
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (isLocationPermissionGranted) {
            if (!Helper.isGPSOn(VisitActivity.this)) {
                setToast("Please turn on GPS");
                Helper.turnOnGPS(VisitActivity.this);
            } else {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
                if (currentLocation == null) {
                    setToast("Can't get your location.. Please try again..");
                }
                openDialogMapCheckIn();
            }
        }
    }

    private void openDialogMapCheckIn() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_map_check_in);
        Button btnCheckIn = dialog.findViewById(R.id.btnCheckIn);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        MapView mapView = dialog.findViewById(R.id.mapView);
        ImageView btCenterMap = dialog.findViewById(R.id.btCenterMap);

        btnCheckIn.setOnClickListener(v -> {
            dialog.dismiss();
            openDialogOutRadius();
        });

        btnNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        IMapController mapController = mapView.getController();
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mapView.setMultiTouchControls(true);
        mapController.setZoom(Constants.ZOOM_LEVEL);

        //icon compass
        CompassOverlay mCompassOverlay = new CompassOverlay(VisitActivity.this, new InternalCompassOrientationProvider(VisitActivity.this), mapView);
        mCompassOverlay.enableCompass();
        mapView.getOverlays().add(mCompassOverlay);

        final List<Customer>[] custList = new List[]{new ArrayList<>()};
        custList[0].add(new Customer(outletClicked.getId(), outletClicked.getNama(), outletClicked.getAddress(), true, outletClicked.getPosition().latitude, outletClicked.getPosition().longitude));
        if (currentLocation != null) {
            GeoPoint myPosition = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
            custList[0].add(new Customer("", "You", "", false, currentLocation.getLatitude(), currentLocation.getLongitude()));
            mapView.getController().animateTo(myPosition);
        }

        final ArrayList<OverlayItem>[] items = new ArrayList[]{Helper.setOverLayItems(custList[0], VisitActivity.this)};

        //the overlay
        final ItemizedOverlayWithFocus<OverlayItem>[][] mOverlay = new ItemizedOverlayWithFocus[][]{new ItemizedOverlayWithFocus[]{new ItemizedOverlayWithFocus<OverlayItem>(items[0], new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                //do something
                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                return false;
            }
        }, VisitActivity.this)}};
        mOverlay[0][0].setFocusItemsOnTap(true);
        mOverlay[0][0].setOnFocusChangeListener(new ItemizedOverlay.OnFocusChangeListener() {
            @Override
            public void onFocusChanged(ItemizedOverlay<?> overlay, OverlayItem newFocus) {
                mOverlay[0][0].unSetFocusedItem();
                mOverlay[0][0].setFocusedItem(newFocus);
            }
        });

        mapView.getOverlays().add(mOverlay[0][0]);
        mapController.setCenter(Helper.computeCentroid(custList[0]));

        btCenterMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLocation != null) {
                    GeoPoint myPosition = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                    mapView.getController().animateTo(myPosition);

                    custList[0].clear();
                    custList[0].add(new Customer(outletClicked.getId(), outletClicked.getNama(), outletClicked.getAddress(), true, outletClicked.getPosition().latitude, outletClicked.getPosition().longitude));
                    custList[0].add(new Customer("", "You", "", false, currentLocation.getLatitude(), currentLocation.getLongitude()));

                    items[0] = Helper.setOverLayItems(custList[0], VisitActivity.this);
                    mOverlay[0] = new ItemizedOverlayWithFocus[]{new ItemizedOverlayWithFocus<OverlayItem>(items[0], new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                        @Override
                        public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                            //do something
                            return true;
                        }

                        @Override
                        public boolean onItemLongPress(final int index, final OverlayItem item) {
                            return false;
                        }
                    }, VisitActivity.this)};
                    mapView.getOverlays().add(mOverlay[0][0]);
                    mapView.invalidate();
                }
            }
        });

        dialog.show();
    }

    private void openDialogOutRadius() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_out_radius);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(VisitActivity.this, DailySalesmanActivity.class);
            startActivity(intent);
        });

        btnNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setViewNoo() {
        txtNOO.setOnClickListener(v -> {
            txtVisit.setTextColor(ContextCompat.getColor(this, R.color.gray8_aspp));
            txtNOO.setTextColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtVisitLine.setBackgroundColor(ContextCompat.getColor(this, R.color.space_transparent));
            txtNOOLine.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_aspp));

            llVisit.setVisibility(View.GONE);
            llNoo.setVisibility(View.VISIBLE);
        });

        btnAddNoo.setOnClickListener(v -> {
            Intent intent = new Intent(VisitActivity.this, CreateNooActivity.class);
            startActivity(intent);
        });

        swipeLayoutNoo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                setAdapterNoo();
                swipeLayoutNoo.setRefreshing(false);
            }
        });

        edtSearchNoo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapterNoo != null) {
                    mAdapterNoo.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnAddNoo.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateNooActivity.class);
            startActivity(intent);
        });
    }

    private void setAdapterNoo() {
        mAdapterNoo = new NooListAdapter(this, mListNoo, header -> {
            outletNooClicked = header;
            outletClicked = new Customer(header.getIdHeader(), header.getName(), header.getNamePemilik(), header.getAddress(), header.getPhone(), header.getCreditLimit(), header.getNoKtp(), header.getNoNpwp(), true, header.getLatitude(), header.getLongitude(), 0);
            outletClicked.setIdHeader(header.getIdHeader());
            checkLocationPermission();
        });

        recyclerViewNoo.setAdapter(mAdapterNoo);
    }

    private void openDialogReasonNotVisit() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_reason_not_visit);

        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        AutoCompleteTextView txtReasonAll = dialog.findViewById(R.id.txtReasonAll);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        List<Reason> reasonList = new ArrayList<>();
        reasonList.addAll(database.getAllReason("Not Visit"));

        final ArrayAdapter<Reason> arrayAdapter = new ArrayAdapter<Reason>(VisitActivity.this, android.R.layout.simple_dropdown_item_1line, reasonList);
        txtReasonAll.setAdapter(arrayAdapter);

        txtReasonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                txtReasonAll.showDropDown();
            }
        });

        ReasonNotVisitAdapter mAdapter = new ReasonNotVisitAdapter(this, mList, header -> {

        });

        recyclerView.setAdapter(mAdapter);

        btnSave.setOnClickListener(v -> {
            dialog.dismiss();
            btnEndDay.setVisibility(View.VISIBLE);
            btnNextDay.setVisibility(View.GONE);
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openDialogEndVisit() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_end_visit);
        Button btnEnd = dialog.findViewById(R.id.btnEnd);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        ImageView imgSelesai = dialog.findViewById(R.id.imgSelesai);
        ImageView imgAddSelesai = dialog.findViewById(R.id.imgAddSelesai);
        ImageView imgPulang = dialog.findViewById(R.id.imgPulang);
        ImageView imgAddPulang = dialog.findViewById(R.id.imgAddPulang);

        btnEnd.setOnClickListener(v -> {
            dialog.dismiss();
            btnStartVisit.setVisibility(View.VISIBLE);
            btnEndVisit.setVisibility(View.GONE);
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openDialogStartVisit() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_start_visit);
        Button btnStart = dialog.findViewById(R.id.btnStart);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        ImageView imgAdd = dialog.findViewById(R.id.imgAdd);
        ImageView imgBerangkat = dialog.findViewById(R.id.imgBerangkat);
        LinearLayout llImgBerangkat = dialog.findViewById(R.id.llImgBerangkat);

        imgAdd.setOnClickListener(v -> {
//            Intent camera = new Intent(this, Camera3PLActivity.class);//3
//            startActivityForResult(camera, Constants.REQUEST_CAMERA_CODE);
        });

        btnStart.setOnClickListener(v -> {
            dialog.dismiss();
            btnStartVisit.setVisibility(View.GONE);
            btnEndVisit.setVisibility(View.VISIBLE);
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openDialogAdd() {
        Dialog alertDialog = new Dialog(this);

        alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        EditText editText = alertDialog.findViewById(R.id.edit_text);
        RecyclerView listView = alertDialog.findViewById(R.id.list_view);

        List<Customer> groupList = new ArrayList<>();
        groupList.addAll(database.getAllNonRouteCustomer());

        FilteredSpinnerCustomerAdapter spinnerAdapter = new FilteredSpinnerCustomerAdapter(this, groupList, (header, adapterPosition) -> {
            int idHeader = database.addCustomer(header, user.getUsername());
            List<Promotion> promoList = database.getPromotionNonRouteByIdCustomer(header.getId());
            for (Promotion promo : promoList) {
                database.addCustomerPromotion(promo, String.valueOf(idHeader), user.getUsername());
            }
            database.deleteMasterNonRouteCustomerById(header.getIdHeader());
            database.deleteMasterNonRouteCustomerPromotionById(header.getIdHeader());
            getData();
            setAdapterVisit();
            alertDialog.dismiss();
        });

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mManager);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        listView.setAdapter(spinnerAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spinnerAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        pdfUtils = LashPdfUtils.getInstance(VisitActivity.this);

        progressCircleNoo = findViewById(R.id.progressCircleNoo);
        progressCircleVisit = findViewById(R.id.progressCircleVisit);
        txtNOOLine = findViewById(R.id.txtNOOLine);
        txtNOO = findViewById(R.id.txtNOO);
        txtVisit = findViewById(R.id.txtVisit);
        txtVisitLine = findViewById(R.id.txtVisitLine);
        rlDaily = findViewById(R.id.rlDaily);
        rlInap = findViewById(R.id.rlInap);
        btnEndVisit = findViewById(R.id.btnEndVisit);
        btnStartVisit = findViewById(R.id.btnStartVisit);
        btnEndDay = findViewById(R.id.btnEndDay);
        btnNextDay = findViewById(R.id.btnNextDay);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        edtSearchNoo = findViewById(R.id.edtSearchNoo);
        edtSearchVisit = findViewById(R.id.edtSearchVisit);
        llVisit = findViewById(R.id.llVisit);
        llNoo = findViewById(R.id.llNoo);
        btnAddVisit = findViewById(R.id.btnAddVisit);
        btnAddNoo = findViewById(R.id.btnAddNoo);

        recyclerViewVisit = findViewById(R.id.recyclerViewVisit);
        recyclerViewVisit.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVisit.setHasFixedSize(true);

        recyclerViewNoo = findViewById(R.id.recyclerViewNoo);
        recyclerViewNoo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNoo.setHasFixedSize(true);

        swipeLayoutNoo = findViewById(R.id.swipeLayoutNoo);
        swipeLayoutNoo.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);

        swipeLayoutVisit = findViewById(R.id.swipeLayoutVisit);
        swipeLayoutVisit.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFirstDataOffline();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            boolean check =
                    Environment.isExternalStorageManager();
//                            && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//                            && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            return check;
        } else {
            if (
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        }
    }

    private void requestPermission() {
        //  if(!Environment.isExternalStorageManager()){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
                } else {
                    setToast("Allow permission for storage access!");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    new AsyncTaskGeneratePDF().execute();
                } else {
                    setToast(getString(R.string.pleaseEnablePermission));
                }
                break;
        }
    }

    private class AsyncTaskGeneratePDF extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                pdfFile = new File(Utils.getDirLocPDF(getApplicationContext()) + "/lash.pdf");
                success = pdfUtils.createPDF(pdfFile);
                return success;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result == null) {
                setToast("Gagal membuat pdf.. Silahkan coba lagi..");
            } else {
                if (result) {
                    setToast("Downloaded to " + pdfFile.getAbsolutePath());
                    btnEndDay.setVisibility(View.GONE);
                    btnNextDay.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(VisitActivity.this, UnloadingActivity.class);
                    startActivity(intent);
                } else {
                    setToast("Gagal membuat pdf.. Silahkan coba lagi");
                }

            }

        }
    }

    private void getFirstDataOffline() {
        getData();
        setAdapterVisit();
        setAdapterNoo();

        if (mList == null || mList.isEmpty()) {
            progressCircleVisit.setVisibility(View.VISIBLE);
            new RequestUrl().execute();
        }
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllCustomerVisit(null, false);

        mListNoo = new ArrayList<>();
        mListNoo = database.getAllCustomerNoo();
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
                    mListNonRoute = new ArrayList<>();
                    Map result = (Map) resultWsMessage.getResult();
                    int startDay = (int) result.get("visit");
                    SessionManagerQubes.setStartDay(startDay);

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
                    Log.e("Customer", ex.getMessage());
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
                progressCircleVisit.setVisibility(View.GONE);
                if (saveDataSuccess) {
                    mAdapterVisit.setData(mList);
                    validateButton();
                } else {
                    setToast(getString(R.string.failedSaveData));
                }
            }

        }
    }

    private void validateButton() {
        if (user.getRute_inap() == 1) {
            rlInap.setVisibility(View.VISIBLE);
        } else {
            rlInap.setVisibility(View.GONE);
        }

        if (SessionManagerQubes.geStartDay() == 1) {
            btnStartVisit.setVisibility(View.GONE);
            btnNextDay.setVisibility(View.GONE);
        } else {
            btnEndVisit.setVisibility(View.VISIBLE);
            btnEndDay.setVisibility(View.VISIBLE);
        }
    }
}
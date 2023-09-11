package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.activity.SplashScreenActivity;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAdapter;
import id.co.qualitas.qubes.adapter.aspp.NooListAdapter;
import id.co.qualitas.qubes.adapter.aspp.ReasonNotVisitAdapter;
import id.co.qualitas.qubes.adapter.aspp.VisitListAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.User;

public class VisitActivity extends BaseActivity implements LocationListener {
    private VisitListAdapter mAdapterVisit;
    private NooListAdapter mAdapterNoo;
    private List<Customer> mList, mListNoo;
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

        init();
        initialize();
        initData();
        setViewVisit();
        setViewNoo();

        txtVisit.setOnClickListener(v -> {
            txtVisit.setTextColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtNOO.setTextColor(ContextCompat.getColor(this, R.color.gray8_aspp));
            txtVisitLine.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtNOOLine.setBackgroundColor(ContextCompat.getColor(this, R.color.space_transparent));

            llVisit.setVisibility(View.VISIBLE);
            llNoo.setVisibility(View.GONE);
        });

        txtNOO.setOnClickListener(v -> {
            txtVisit.setTextColor(ContextCompat.getColor(this, R.color.gray8_aspp));
            txtNOO.setTextColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtVisitLine.setBackgroundColor(ContextCompat.getColor(this, R.color.space_transparent));
            txtNOOLine.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_aspp));

            llVisit.setVisibility(View.GONE);
            llNoo.setVisibility(View.VISIBLE);
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

        btnNextDay.setOnClickListener(v -> {
            openDialogReasonNotVisit();
        });

        btnEndDay.setOnClickListener(v -> {
            btnEndDay.setVisibility(View.GONE);
            btnNextDay.setVisibility(View.VISIBLE);
            Intent intent = new Intent(VisitActivity.this, UnloadingActivity.class);
            startActivity(intent);
        });

        btnAddNoo.setOnClickListener(v -> {
            Intent intent = new Intent(VisitActivity.this, CreateNooActivity.class);
            startActivity(intent);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(VisitActivity.this);
        });
    }

    private void setViewVisit() {
        mAdapterVisit = new VisitListAdapter(this, mList, header -> {
            outletClicked = header;
            checkLocationPermission();
        });

        recyclerViewVisit.setAdapter(mAdapterVisit);

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
        custList[0].add(new Customer(outletClicked.getIdCustomer(), outletClicked.getNameCustomer(), outletClicked.getAddress(), true, outletClicked.getPosition().latitude, outletClicked.getPosition().longitude));
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
                    custList[0].add(new Customer(outletClicked.getIdCustomer(), outletClicked.getNameCustomer(), outletClicked.getAddress(), true, outletClicked.getPosition().latitude, outletClicked.getPosition().longitude));
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
        mAdapterNoo = new NooListAdapter(this, mListNoo, header -> {
            outletClicked = header;
            checkLocationPermission();
        });

        recyclerViewNoo.setAdapter(mAdapterNoo);

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
        reasonList.addAll(Helper.getDataReason());

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

        List<String> groupList = new ArrayList<>();
        groupList.add("OBC44 - YANTO (TK)\nJL. KOMODORE");
        groupList.add("0C258 - SUSU IBU (WR)\nJL. RAYA BOGOR");
        groupList.add("0AQ13 - TOSIN (WR)\nJL. JATIWARINGIN");
        groupList.add("0AM92 - ES TIGA (TK)\nJL. BINA MARGA");

        FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(this, groupList, (nameItem, adapterPosition) -> {
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

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Customer("BO", "Black Owl", "Golf Island Beach Theme Park, Jl. Pantai Indah Kapuk No.77, Kamal Muara, DKI Jakarta 14470", true, new LatLng(-6.090263984566263, 106.74593288657607)));
        mList.add(new Customer("PCP", "Pantjoran Chinatown PIK", "Unnamed Road, 14460", false, new LatLng(-6.09047339393416, 106.74535959301855)));
        mList.add(new Customer("CMP", "Central Market PIK", "Golf Island, Kawasan Pantai Maju, Jl, Jl. Boulevard Raya, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14470", true, new LatLng(-6.09102018270127, 106.74661148098058)));

        mListNoo = new ArrayList<>();
        mListNoo.add(new Customer("CHGI", "Cluster Harmony, Golf Island", "WP6W+7JR, Pantai Indah Kapuk St, Kamal Muara, Penjaringan, North Jakarta City, Jakarta 14460", false, new LatLng(-6.089065696336256, 106.74676357552187)));
        mListNoo.add(new Customer("MSGIP", "Monsieur Spoon Golf Island PIK", "Urban Farm, Unit 5, Kawasan Pantai Maju Jl. The Golf Island Boulevard, Kel, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14460", true, new LatLng(-6.09032214182743, 106.74191982249332)));
        mListNoo.add(new Customer("KMPP", "K3 Mart PIK Pantjoran", "Golf Island, Ruko Blok D No.02A, Kamal Muara, Jkt Utara, Daerah Khusus Ibukota Jakarta 11447", false, new LatLng(-6.088542162422348, 106.74239952686823)));

    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtNOO = findViewById(R.id.txtNOO);
        txtNOOLine = findViewById(R.id.txtNOOLine);
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }
}
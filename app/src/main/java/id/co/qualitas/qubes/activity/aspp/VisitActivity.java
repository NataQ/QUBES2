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
import android.view.LayoutInflater;
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

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

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
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerCustomerAdapter;
import id.co.qualitas.qubes.adapter.aspp.NooListAdapter;
import id.co.qualitas.qubes.adapter.aspp.ReasonNotVisitAdapter;
import id.co.qualitas.qubes.adapter.aspp.VisitListAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.StartVisit;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.LashPdfUtils;
import id.co.qualitas.qubes.utils.Utils;

public class VisitActivity extends BaseActivity implements LocationListener {
    private VisitListAdapter mAdapterVisit;
    private NooListAdapter mAdapterNoo;
    private List<Customer> mList, mListNonRoute;
    private List<Customer> mListNoo;
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
    private boolean outRadius = false;
    public static final int CAMERA_PERM_CODE = 102;
    private ImageType imageType;
    private Uri uriBerangkat, uriPulang, uriSelesai;
    private Map startDay, endDay;
    private String kmAwal, kmAkhir;
    private VisitSalesman visitSalesman;

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
            if (SessionManagerQubes.getStartDay() == 1) {
                openDialogAdd();
            } else {
                setToast("Please start visit first");
            }
        });
    }

    private void setAdapterVisit() {
        mAdapterVisit = new VisitListAdapter(this, mList, header -> {
            outletClicked = header;
            if (SessionManagerQubes.getStartDay() != 0) {
                if (header.getStatus() == 0) {//belum check in
                    checkLocationPermission();
                } else {
                    if (checkOutletStatus()) {
                        outletClicked.setStatus(Constants.CHECK_IN_VISIT);
                        SessionManagerQubes.setOutletHeader(outletClicked);
                        Intent intent = new Intent(VisitActivity.this, DailySalesmanActivity.class);
                        startActivity(intent);
                    } else {
                        setToast("Selesaikan customer yang sedang check in");
                    }
                }
            } else {
                setToast("Please start visit first");
            }
        });

        recyclerViewVisit.setAdapter(mAdapterVisit);
    }

    private boolean checkOutletStatus() {
        int statusCheckIn = 0;
        for (Customer customer : mList) {
            //0-> no status, 1 -> check in, 2 -> pause, 3-> check out
            if (customer.getStatus() == 1) {
                statusCheckIn++;
            }
        }

        for (Customer customer : mListNoo) {
            //0-> no status, 1 -> check in, 2 -> pause, 3-> check out
            if (customer.getStatus() == 1) {
                statusCheckIn++;
            }
        }
        return (statusCheckIn == 0);
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
            Location locCustomer = null;
            locCustomer.setLatitude(outletClicked.getLatitude());
            locCustomer.setLongitude(outletClicked.getLongitude());
            outRadius = Helper.checkRadius(currentLocation, locCustomer);

            visitSalesman = new VisitSalesman();
            visitSalesman.setStatus(Constants.CHECK_IN_VISIT);
            visitSalesman.setCheckInTime(Helper.getTodayDate(Constants.DATE_FORMAT_2));
            visitSalesman.setLatCheckIn(currentLocation.getLatitude());
            visitSalesman.setLongCheckIn(currentLocation.getLongitude());
            visitSalesman.setInside(outRadius);
            visitSalesman.setIdSalesman(user.getUsername());
            visitSalesman.setCustomerId(outletClicked.getId());
            visitSalesman.setDate(Helper.getTodayDate(Constants.DATE_FORMAT_3));
            visitSalesman.setSync(0);

            if (outRadius) {
                openDialogOutRadius();
            } else {
                database.addVisitSalesman(visitSalesman, user.getUsername());
                outletClicked.setStatus(Constants.CHECK_IN_VISIT);
                SessionManagerQubes.setOutletHeader(outletClicked);
                Intent intent = new Intent(VisitActivity.this, DailySalesmanActivity.class);
                startActivity(intent);
            }
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
            database.addVisitSalesman(visitSalesman, user.getUsername());
            outletClicked.setStatus(Constants.CHECK_IN_VISIT);
            SessionManagerQubes.setOutletHeader(outletClicked);
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
            if (SessionManagerQubes.getStartDay() == 1) {
                SessionManagerQubes.clearCustomerNooSession();
                Helper.removeItemParam(Constants.IMAGE_TYPE);
                Intent intent = new Intent(VisitActivity.this, CreateNooActivity.class);
                startActivity(intent);
            } else {
                setToast("Please start visit first");
            }
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
    }

    private void setAdapterNoo() {
        mAdapterNoo = new NooListAdapter(this, mListNoo, header -> {
            outletClicked = header;

            if (SessionManagerQubes.getStartDay() != 0) {
                if (header.getStatus() == 0) {//belum check in
                    checkLocationPermission();
                } else {
                    if (checkOutletStatus()) {
                        outletClicked.setStatus(Constants.CHECK_IN_VISIT);
                        SessionManagerQubes.setOutletHeader(outletClicked);
                        Intent intent = new Intent(VisitActivity.this, DailySalesmanActivity.class);
                        startActivity(intent);
                    } else {
                        setToast("Selesaikan customer yang sedang check in");
                    }
                }
            } else {
                setToast("Please start visit first");
            }
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
        EditText txtKmAkhir = dialog.findViewById(R.id.txtKmAkhir);
        LinearLayout llImgSelesai = dialog.findViewById(R.id.llImgSelesai);
        LinearLayout llImgPulang = dialog.findViewById(R.id.llImgPulang);
        ImageView imgSelesai = dialog.findViewById(R.id.imgSelesai);
        ImageView imgAddSelesai = dialog.findViewById(R.id.imgAddSelesai);
        ImageView imgPulang = dialog.findViewById(R.id.imgPulang);
        ImageView imgAddPulang = dialog.findViewById(R.id.imgAddPulang);

        if (uriSelesai != null) {
            imgSelesai.setImageURI(uriSelesai);
            imgAddSelesai.setVisibility(View.GONE);
        } else {
            imgAddSelesai.setVisibility(View.VISIBLE);
        }
        if (uriPulang != null) {
            imgPulang.setImageURI(uriPulang);
            imgAddPulang.setVisibility(View.GONE);
        } else {
            imgAddPulang.setVisibility(View.VISIBLE);
        }

        txtKmAkhir.setText(imageType.getKmAkhir());

        llImgPulang.setOnClickListener(v -> {
            ImageType imageType = new ImageType();
            imageType.setKmAkhir(txtKmAkhir.getText().toString().trim());
            imageType.setPhotoAkhir(uriPulang);
            imageType.setPhotoSelesai(uriSelesai);
            imageType.setPosImage(5);
            Helper.setItemParam(Constants.IMAGE_TYPE,imageType);
//            SessionManagerQubes.setImageType(imageType);
            askPermissionCamera();
        });

        llImgSelesai.setOnClickListener(v -> {
            ImageType imageType = new ImageType();
            imageType.setPosImage(6);
            imageType.setKmAkhir(txtKmAkhir.getText().toString().trim());
            imageType.setPhotoAkhir(uriPulang);
            imageType.setPhotoSelesai(uriSelesai);
            Helper.setItemParam(Constants.IMAGE_TYPE,imageType);
//            SessionManagerQubes.setImageType(imageType);
            askPermissionCamera();
        });

        btnEnd.setOnClickListener(v -> {
            dialog.dismiss();
            if (Helper.isEmptyEditText(txtKmAkhir)) {
                txtKmAkhir.setError(getString(R.string.emptyField));
            } else if (uriPulang == null) {
                setToast("Harus Foto KM Akhir");
            } else if (uriSelesai == null) {
                setToast("Harus Foto Selesai");
            } else {
                kmAkhir = txtKmAkhir.getText().toString().trim();
                PARAM = 4;
                new RequestUrl().execute();//4
                progress.show();
            }
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openDialogStartVisit() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog(this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_start_visit, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(500, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5

        Button btnStart = dialog.findViewById(R.id.btnStart);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        EditText txtKmAwal = dialog.findViewById(R.id.txtKmAwal);
        ImageView imgAdd = dialog.findViewById(R.id.imgAdd);
        ImageView imgBerangkat = dialog.findViewById(R.id.imgBerangkat);
        LinearLayout llImgBerangkat = dialog.findViewById(R.id.llImgBerangkat);

        if (uriBerangkat != null) {
            imgBerangkat.setImageURI(uriBerangkat);
            imgAdd.setVisibility(View.GONE);
        } else {
            imgAdd.setVisibility(View.VISIBLE);
        }

        txtKmAwal.setText(imageType.getKmAwal());

        llImgBerangkat.setOnClickListener(v -> {
            ImageType imageType = new ImageType();
            imageType.setKmAwal(txtKmAwal.getText().toString().trim());
            imageType.setPosImage(4);
            imageType.setPhotoKmAwal(uriBerangkat);
            Helper.setItemParam(Constants.IMAGE_TYPE,imageType);
//            SessionManagerQubes.setImageType(imageType);
            askPermissionCamera();
//            Intent camera = new Intent(this, Camera3PLActivity.class);//3
//            startActivityForResult(camera, Constants.REQUEST_CAMERA_CODE);
        });

        btnStart.setOnClickListener(v -> {
            dialog.dismiss();
            if (Helper.isEmptyEditText(txtKmAwal)) {
                txtKmAwal.setError(getString(R.string.emptyField));
            } else if (uriBerangkat == null) {
                setToast("Foto KM Awal");
            } else {
                kmAwal = txtKmAwal.getText().toString().trim();
                PARAM = 3;
                new RequestUrl().execute();//3
                progress.show();
            }
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    public void askPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, CAMERA_PERM_CODE);
        } else {
            Helper.takePhoto(VisitActivity.this);
        }
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
        if (Helper.getItemParam(Constants.IMAGE_TYPE) != null) {
            imageType = new ImageType();
            imageType = (ImageType) Helper.getItemParam(Constants.IMAGE_TYPE);
        } else {
            imageType = new ImageType();
        }

        if (getIntent().getExtras() != null) {
            Uri uri = (Uri) getIntent().getExtras().get(Constants.OUTPUT_CAMERA);
            getIntent().removeExtra(Constants.OUTPUT_CAMERA);

            switch (imageType.getPosImage()) {
                case 4:
                    uriBerangkat = uri;
                    openDialogStartVisit();
                    break;
                case 5:
                    uriPulang = uri;
                    openDialogEndVisit();
                    break;
                case 6:
                    uriSelesai = uri;
                    openDialogEndVisit();
                    break;
            }
        }
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

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    new AsyncTaskGeneratePDF().execute();
//                } else {
//                    setToast(getString(R.string.pleaseEnablePermission));
//                }
//                break;
//        }
//    }

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
        rlInap.setVisibility(View.GONE);
        rlDaily.setVisibility(View.GONE);
        getData();
        setAdapterVisit();
        setAdapterNoo();

        if (mList == null || mList.isEmpty()) {
            progressCircleVisit.setVisibility(View.VISIBLE);
            PARAM = 1;
            new RequestUrl().execute();//1
        }

        validateButton();
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
                } else if (PARAM == 2) {
                    mList = new ArrayList<>();
                    mListNonRoute = new ArrayList<>();
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
                } else if (PARAM == 3) {
//                    String photoKMAwal = Utils.compressImageUri(getApplicationContext(), uriBerangkat.toString());
//                    StartVisit startDay = new StartVisit();
//                    startDay.setKmAwal(kmAwal);
//                    startDay.setUsername(user.getUsername());
//                    startDay.setUserLogin(user.getUserLogin());
                    startDay = new HashMap();
                    startDay.put("kmAwal", kmAwal);
                    startDay.put("username", user.getUsername());
                    startDay.put("userLogin", user.getUserLogin());
                    startDay.put("photo", Utils.encodeImageBase64(VisitActivity.this, uriBerangkat));
//                    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//                    if (photoKMAwal != null) {
//                        map.add("kmAwal", new FileSystemResource(photoKMAwal));
//                    } else {
//                        map.add("kmAwal", "");
//                    }
//                    String json = new Gson().toJson(startDay);
//                    map.add("data", json);
//
//                    String URL_ = Constants.API_GET_START_DAY_MULTI_PART;
                    String URL_ = Constants.API_GET_START_DAY;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
//                    return (WSMessage) NetworkHelper.postWebserviceWithBodyMultiPart(url, WSMessage.class, startDay);
                    return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, startDay);
                } else {
                    endDay = new HashMap();
                    endDay.put("kmAkhir", kmAkhir);
                    endDay.put("username", user.getUsername());
                    endDay.put("photoKmAkhir", Utils.encodeImageBase64(VisitActivity.this, uriPulang));
                    endDay.put("photoCompleted", Utils.encodeImageBase64(VisitActivity.this, uriSelesai));

                    String URL_ = Constants.API_GET_END_DAY;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
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
                        new RequestUrl().execute();//2
                    } else {
                        progressCircle.setVisibility(View.GONE);
                        setToast(result.getMessage());
                    }
                } else {
                    progressCircle.setVisibility(View.GONE);
                    setToast(getString(R.string.failedGetData));
                }
            } else if (PARAM == 2) {
                progressCircleVisit.setVisibility(View.GONE);
                if (saveDataSuccess) {
                    mAdapterVisit.setData(mList);
                    validateButton();
                } else {
                    setToast(getString(R.string.failedSaveData));
                }
            } else if (PARAM == 3) {
                progress.dismiss();
                if (result != null) {
                    if (result.getIdMessage() == 1) {
                        SessionManagerQubes.setStartDay(1);
                        validateButton();
                    } else {
                        setToast(result.getMessage());
                    }
                } else {
                    setToast(getString(R.string.serverError));
                }
            } else {
                progress.dismiss();
                if (result != null) {
                    if (result.getIdMessage() == 1) {
                        SessionManagerQubes.setStartDay(2);
                        validateButton();
                    } else {
                        setToast(result.getMessage());
                    }
                } else {
                    setToast(getString(R.string.serverError));
                }
            }
        }
    }

    private void validateButton() {
        if (user.getRute_inap() == 1) {
            rlInap.setVisibility(View.VISIBLE);
            rlDaily.setVisibility(View.GONE);
        } else {
            rlInap.setVisibility(View.GONE);
            rlDaily.setVisibility(View.VISIBLE);
        }

        switch (SessionManagerQubes.getStartDay()) {
            case 0:
                btnStartVisit.setVisibility(View.VISIBLE);
                btnNextDay.setVisibility(View.VISIBLE);
                btnEndVisit.setVisibility(View.GONE);
                btnEndDay.setVisibility(View.GONE);
                btnAddVisit.setVisibility(View.GONE);
                btnAddNoo.setVisibility(View.GONE);
                break;
            case 1:
                btnStartVisit.setVisibility(View.GONE);
                btnNextDay.setVisibility(View.GONE);
                btnEndVisit.setVisibility(View.VISIBLE);
                btnEndDay.setVisibility(View.VISIBLE);
                btnAddVisit.setVisibility(View.VISIBLE);
                btnAddNoo.setVisibility(View.VISIBLE);
                break;
            case 2:
                btnStartVisit.setVisibility(View.GONE);
                btnNextDay.setVisibility(View.GONE);
                btnEndVisit.setVisibility(View.GONE);
                btnEndDay.setVisibility(View.GONE);
                btnAddVisit.setVisibility(View.GONE);
                btnAddNoo.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Helper.takePhoto(VisitActivity.this);
        } else if (requestCode == PERMISSION_REQUEST_CODE && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            new AsyncTaskGeneratePDF().execute();
        } else {
            setToast(getString(R.string.pleaseEnablePermission));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Helper.setItemParam(Constants.FROM_VISIT,"1");
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.textfield.TextInputLayout;

import org.osmdroid.api.IMapController;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoDctOutletAdapter;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoLimitFakturAdapter;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoPromoAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerReasonAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.aspp.ConfirmationDialogFragment;
import id.co.qualitas.qubes.helper.AddressResultReceiver;
import id.co.qualitas.qubes.helper.FetchAddressIntentService;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.interfaces.CallbackOnResult;
import id.co.qualitas.qubes.interfaces.LocationRequestCallback;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.GroupMaxBon;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class DailySalesmanActivity extends BaseActivity {
    private TextView txtOutlet, txtTypeOutlet, txtStatus, txtOrder;
    private TextView txtNamaPemilik, txtPhone, txtSisaKreditLimit, txtTotalTagihan, txtKTP, txtNPWP, txtKreditLimitAwal;
    private Button btnCheckOut;
    private LinearLayout llPause, llStoreCheck, llOrder, llCollection, llReturn, llTimer;
    private LinearLayout llLokasiToko, llLokasiGudang, llLokasiTagihan;
    private RelativeLayout llKTP, llNPWP, llOutlet;
    private ImageView imgKTP, imgNPWP, imgOutlet, imgPause;
    private ImageView imgDeleteKTP, imgDeleteNPWP, imgDeleteOutlet;
    private ImageView imgAddKTP, imgAddNPWP, imgAddOutlet;
    private RecyclerView rvPromo, rvLimitFaktur, rvDCTOutlet;
    private CustomerInfoPromoAdapter promoAdapter;
    private CustomerInfoLimitFakturAdapter limitFakturAdapter;
    private CustomerInfoDctOutletAdapter dctOutletAdapter;
    private List<GroupMaxBon> limitFakturList;
    private List<Material> dctOutletList;
    //    private List<Material> oustandingFaktur;
    private List<Promotion> promoList;
    static int h;
    static int m;
    static int s;
    @SuppressLint("StaticFieldLeak")
    static Chronometer timerValue;
    //how to save chronometer => checkInOutRequest.setTimer(String.valueOf(SystemClock.elapsedRealtime() - timerValue.getBase()));
    private Date curDate = new Date();
    private Date dCheckIn = null, dCurrent = null, dResume = null;
    public String checkInTime, pauseTime, curTime, continueTime, timeDuration;
    public boolean pause = false;
    private Customer outletHeader;
    private VisitSalesman visitSales;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private final static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int GALLERY_PERM_CODE = 101;
    public static final int CAMERA_PERM_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    private ImageType imageType;
    private String today;
    private String imagepath;
    private Uri uriImagePath;
    private int typeImage = 0;
    private boolean isLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private LocationRequestCallback<String, Location> addressCallback;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Map currentLocation;
    private int updateLocation = 0;
    private int outstandingFaktur;
    private double totalTagihan = 0;

    public static Chronometer getTimerValue() {
        return timerValue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_daily_salesman);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000).setWaitForAccurateLocation(false).setMinUpdateIntervalMillis(2000).setMaxUpdateDelayMillis(2000).build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
            }
        };
        mResultReceiver = new AddressResultReceiver(new Handler());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

        initialize();

        btnCheckOut.setOnClickListener(v -> {
//            if (validateStoreCheck() > 0) {
            int validateOut = validateCheckOut();
            switch (validateOut) {
                case 0:
                    Map req = new HashMap();
                    req.put("id", outletHeader.getId());
                    req.put("date", Helper.getTodayDate(Constants.DATE_FORMAT_3));

                    int storeCheck = database.getCountStoreCheck(req);
                    if (storeCheck == 0) {
                        setToast("Silahkan check stock toko!");
                    } else {
                        openDialogPhotoCheckOut();//button check out
                    }
                    break;
                case 1:
                    openDialogReasonCheckOut();
                    //reason not order
                    break;
                case 2:
                    setToast("Silahkan check stock toko!");
                    break;
//                case 2:
//                    openDialogReasonCheckOut(Constants.REASON_TYPE_NOT_PAY, "Reason Not Pay");
//                    //reason not pay
//                    break;
            }
//            } else {
//                setToast("Silahkan check stock toko!");
//            }
        });

        llPause.setOnClickListener(v -> {
            if (outletHeader.getStatus() == Constants.PAUSE_VISIT) {
                if (checkOutletStatus()) {
                    resumeTimer();
                } else {
                    setToast("Selesaikan customer yang sedang check in");
                }
            } else {
                if (database.getCountPauseCustomer() < database.getMaxPause()) {
                    openDialogPause();
                } else {
                    setToast("Selesaikan pause di customer lain, sebelum pause di customer ini");
                }
            }
        });

        llStoreCheck.setOnClickListener(v -> {
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                Intent intent = new Intent(this, StoreCheckActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, StoreCheckDetailActivity.class);
                startActivity(intent);
            }
        });

        llOrder.setOnClickListener(v -> {
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                if (!Helper.isNullOrEmpty(outletHeader.getNik()) || !Helper.isNullOrEmpty(outletHeader.getNo_npwp())) {
                    if (!Helper.isEmpty(user.getType_sales())) {
                        if (Helper.isCanvasSales(user)) {
//                        if (Helper.isEmptyOrNull(oustandingFaktur)) {
                            double tagihanInvoice = database.getTotalTagihanInvoiceCustomer(outletHeader.getId());
//                        if (outstandingFaktur == 0) {
                            if (tagihanInvoice == 0) {
                                moveOrder();
                            } else {
                                dialogConfirm();
                            }
                        } else {
                            moveOrder();
                        }
                    } else {
                        moveOrder();
                    }
                } else {
                    setToast("Customer ini tidak memiliki KTP atau NPWP");
                }
            } else {
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
            }
        });

        llCollection.setOnClickListener(v -> {
            SessionManagerQubes.clearCollectionHeaderSession();
            Intent intent = new Intent(this, CollectionVisitActivity.class);
            startActivity(intent);
        });

        llReturn.setOnClickListener(v -> {
            if (checkPermission()) {
                moveReturn();
            } else {
                setToast(getString(R.string.pleaseEnablePermission));
                requestPermission();
            }
        });

        llLokasiToko.setOnClickListener(v -> {
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                updateLocation = 1;
                checkLocationPermission();//1
            } else {
                setToast("Tidak bisa mengubah lokasi yang sudah selesai");
            }
        });

        llLokasiGudang.setOnClickListener(v -> {
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                updateLocation = 2;
                checkLocationPermission();//2
            } else {
                setToast("Tidak bisa mengubah lokasi yang sudah selesai");
            }
        });

        llLokasiTagihan.setOnClickListener(v -> {
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                updateLocation = 3;
                checkLocationPermission();//3
            } else {
                setToast("Tidak bisa mengubah lokasi yang sudah selesai");
            }
        });

        llKTP.setOnClickListener(view -> {
            typeImage = 8;
            imageType.setPosImage(typeImage);
            imageType.setIdName(user.getUsername());
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            openDialogPhoto();
        });

        llNPWP.setOnClickListener(view -> {
            typeImage = 9;
            imageType.setPosImage(typeImage);
            imageType.setIdName(user.getUsername());
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            openDialogPhoto();
        });

        llOutlet.setOnClickListener(view -> {
            typeImage = 10;
            imageType.setPosImage(typeImage);
            imageType.setIdName(user.getUsername());
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            openDialogPhoto();
        });

        imgDeleteKTP.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoKTP(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            Utils.loadImageFit(DailySalesmanActivity.this, null, imgKTP);
            imgAddKTP.setVisibility(View.VISIBLE);
            imgDeleteKTP.setVisibility(View.GONE);
            imgKTP.setVisibility(View.GONE);
            outletHeader.setPhotoKtp(null);
            SessionManagerQubes.setOutletHeader(outletHeader);

            Map req = new HashMap();
            req.put("photo", null);
            req.put("photoName", "ktp_" + outletHeader.getId());
            req.put("typePhoto", "ktp");
            req.put("customerID", outletHeader.getId());
            req.put("username", user.getUsername());
            req.put("idDB", visitSales.getIdHeader());
            database.addPhoto(req);//ktp

//            if (outletHeader.isNoo()) {
//                database.updatePhotoNoo(outletHeader, user.getUsername());
//            } else {
//                database.updatePhoto(outletHeader, user.getUsername());
//            }
        });

        imgDeleteNPWP.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoNPWP(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            Utils.loadImageFit(DailySalesmanActivity.this, null, imgNPWP);
            imgAddNPWP.setVisibility(View.VISIBLE);
            imgDeleteNPWP.setVisibility(View.GONE);
            imgNPWP.setVisibility(View.GONE);
            outletHeader.setPhotoNpwp(null);
            SessionManagerQubes.setOutletHeader(outletHeader);

            Map req = new HashMap();
            req.put("photo", null);
            req.put("photoName", "npwp_" + outletHeader.getId());
            req.put("typePhoto", "npwp");
            req.put("customerID", outletHeader.getId());
            req.put("username", user.getUsername());
            req.put("idDB", visitSales.getIdHeader());
            database.addPhoto(req);//npwp

//            if (outletHeader.isNoo()) {
//                database.updatePhotoNoo(outletHeader, user.getUsername());
//            } else {
//                database.updatePhoto(outletHeader, user.getUsername());
//            }
        });

        imgDeleteOutlet.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoOutlet(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            Utils.loadImageFit(DailySalesmanActivity.this, null, imgOutlet);
            imgAddOutlet.setVisibility(View.VISIBLE);
            imgDeleteOutlet.setVisibility(View.GONE);
            imgOutlet.setVisibility(View.GONE);
            outletHeader.setPhotoOutlet(null);
            SessionManagerQubes.setOutletHeader(outletHeader);

            Map req = new HashMap();
            req.put("photo", null);
            req.put("photoName", "outlet_" + outletHeader.getId());
            req.put("typePhoto", "outlet");
            req.put("customerID", outletHeader.getId());
            req.put("username", user.getUsername());
            req.put("idDB", visitSales.getIdHeader());
            database.addPhoto(req);//outlet
//            if (outletHeader.isNoo()) {
//                database.updatePhotoNoo(outletHeader, user.getUsername());
//            } else {
//                database.updatePhoto(outletHeader, user.getUsername());
//            }
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(DailySalesmanActivity.this);
        });
    }


    private boolean checkOutletStatus() {
        int statusCheckIn = 0;
        int statusCheckInVisit = database.getCountCheckInVisit();
        int statusCheckInNoo = database.getCountCheckInNoo();
        statusCheckIn = statusCheckInVisit + statusCheckInNoo;
        return (statusCheckIn == 0);
    }

    private int validateCheckOut() {
        int payment = 0, invoice = 0, orderOutlet = 0, result = 0, storeCheck = 0;
        Map req = new HashMap();
        req.put("id", outletHeader.getId());
        req.put("date", Helper.getTodayDate(Constants.DATE_FORMAT_3));

        orderOutlet = database.getCountOrderCustomer(req);
        payment = database.getCountCollectionCustomer(req);
        storeCheck = database.getCountStoreCheck(req);

//        if (storeCheck == 0) {
//            result = 2;
//        } else {
        if (orderOutlet == 0) {
            result = 1;// no order
        }
//        }
        return result;
    }

    private int validateStoreCheck() {
        int result = 0;
        Map req = new HashMap();
        req.put("id", outletHeader.getId());
        req.put("date", Helper.getTodayDate(Constants.DATE_FORMAT_3));

        result = database.getCountStoreCheck(req);
        return result;
    }

    private void moveOrder() {
        SessionManagerQubes.clearCollectionHeaderSession();
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    public void dialogConfirm() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog(this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDialog = dialog.findViewById(R.id.txtDialog);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        txtTitle.setText("Order");
        txtDialog.setText("Customer ini memiliki invoice yang belum di bayar. Anda yakin ingin melakukan order?");

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                moveOrder();
            }
        });

        dialog.show();
    }

    private void openDialogReasonCheckOut() {
        imageType = new ImageType();
        Dialog alertDialog = new Dialog(this);

        alertDialog.setContentView(R.layout.aspp_dialog_reason);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
        EditText editText = alertDialog.findViewById(R.id.edit_text);
        RecyclerView listView = alertDialog.findViewById(R.id.list_view);

        List<Reason> reasonList = new ArrayList<>();
        reasonList.addAll(database.getAllReason(Constants.REASON_TYPE_NOT_BUY));

        txtTitle.setText("Reason Not Buy");

        FilteredSpinnerReasonAdapter spinnerAdapter = new FilteredSpinnerReasonAdapter(this, reasonList, (reason, adapterPosition) -> {
            alertDialog.dismiss();
            visitSales.setIdNotBuyReason(String.valueOf(reason.getId()));
            visitSales.setNameNotBuyReason(reason.getDescription());

            Map req = new HashMap();
            req.put("id", outletHeader.getId());
            req.put("date", Helper.getTodayDate(Constants.DATE_FORMAT_3));

            int storeCheck = database.getCountStoreCheck(req);
            if (reason.getId() != Constants.ID_REASON_TOKO_TUTUP) {//toko tutup, di hard code, agar tdak usah store check
                if (storeCheck == 0) {
                    setToast("Silahkan check stock toko!");
                } else {
                    if (reason.getIs_freetext() == 1 || reason.getIs_photo() == 1) {
                        typeImage = 12;//not buy
                        imageType.setPosImage(typeImage);
                        imageType.setVisitSalesman(visitSales);
                        imageType.setReason(reason);
                        imageType.setType(2);//1 => pause, 2 => checkout
                        imageType.setIdName(user.getUsername());
                        Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
                        openDialogPhotoReason(2);//reason not buy
                    } else {
                        openDialogPhotoCheckOut();//dialog reason check out
                    }
                }
            } else {
                if (reason.getIs_freetext() == 1 || reason.getIs_photo() == 1) {
                    typeImage = 12;//not buy
                    imageType.setPosImage(typeImage);
                    imageType.setVisitSalesman(visitSales);
                    imageType.setReason(reason);
                    imageType.setType(2);//1 => pause, 2 => checkout
                    imageType.setIdName(user.getUsername());
                    Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
                    openDialogPhotoReason(2);//reason not buy
                } else {
                    openDialogPhotoCheckOut();//dialog reason check out
                }
            }
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

    private void checkOutCustomer() {
        Location locCustomer = new Location(LocationManager.GPS_PROVIDER);
        locCustomer.setLatitude(outletHeader.getLatitude());
        locCustomer.setLongitude(outletHeader.getLongitude());

        Location currLoc = new Location(LocationManager.GPS_PROVIDER);
        currLoc.setLatitude((Double) currentLocation.get("latitude"));
        currLoc.setLongitude((Double) currentLocation.get("longitude"));

        if (currentLocation != null) {
            boolean outRadius = Helper.checkRadius(DailySalesmanActivity.this, currLoc, locCustomer);
            visitSales.setInsideCheckOut(outRadius);
            visitSales.setLatCheckOut(currentLocation.get("latitude") != null ? (Double) currentLocation.get("latitude") : 0);
            visitSales.setLongCheckOut(currentLocation.get("longitude") != null ? (Double) currentLocation.get("longitude") : 0);
        }

        visitSales.setStatus(Constants.CHECK_OUT_VISIT);
        boolean notBuy = visitSales.getIdNotBuyReason() != null ? true : false;
        String typePhoto = visitSales.getIdNotBuyReason() != null ? "not_buy" : "not_visit";
        Map req = new HashMap();
        boolean result = database.updateVisit(visitSales, user.getUsername(), notBuy);
        if (result) {
            if (visitSales.getPhotoNotBuyReason() != null) {
                req = new HashMap();
                req.put("photo", visitSales.getPhotoNotBuyReason());
                req.put("photoName", "reason_" + typePhoto + "_" + visitSales.getIdHeader());
                req.put("typePhoto", typePhoto);
                req.put("idDB", visitSales.getIdHeader());
                req.put("customerID", visitSales.getCustomerId());
                req.put("username", user.getUsername());
                database.addPhoto(req);//reason not buy
            }

            if (visitSales.getPhotoCheckOut() != null) {
                req = new HashMap();
                req.put("photo", visitSales.getPhotoCheckOut());
                req.put("photoName", "check_out_" + visitSales.getIdHeader());
                req.put("typePhoto", "check_out");
                req.put("idDB", visitSales.getIdHeader());
                req.put("customerID", visitSales.getCustomerId());
                req.put("username", user.getUsername());
                database.addPhoto(req);//check out
            }

            outletHeader.setStatus(Constants.CHECK_OUT_VISIT);
            if (outletHeader.isNoo()) {
                database.updateStatusOutletNoo(outletHeader, user.getUsername());
            } else {
                database.updateStatusOutletVisit(outletHeader, user.getUsername());
            }
            SessionManagerQubes.setOutletHeader(outletHeader);
            onBackPressed();
            setToast("Sukses Check Out");
        } else {
            setToast("Gagal Check Out");
        }
    }

    public void openDialogPause() {
        imageType = new ImageType();
        Dialog alertDialog = new Dialog(this);

        alertDialog.setContentView(R.layout.aspp_dialog_reason);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
        EditText editText = alertDialog.findViewById(R.id.edit_text);
        RecyclerView listView = alertDialog.findViewById(R.id.list_view);

        List<Reason> reasonList = new ArrayList<>();
        reasonList.addAll(database.getAllReason(Constants.REASON_TYPE_PAUSE));

        txtTitle.setText("Reason Pause");

        FilteredSpinnerReasonAdapter spinnerAdapter = new FilteredSpinnerReasonAdapter(this, reasonList, (reason, adapterPosition) -> {
            visitSales.setIdPauseReason(String.valueOf(reason.getId()));
            visitSales.setNamePauseReason(reason.getDescription());
            if (reason.getIs_freetext() == 1 || reason.getIs_photo() == 1) {
                typeImage = 11;//pause
                imageType.setPosImage(typeImage);
                imageType.setVisitSalesman(visitSales);
                imageType.setReason(reason);
                imageType.setType(1);//1 => pause, 2 => checkout
                imageType.setIdName(user.getUsername());
                Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
                openDialogPhotoReason(1);//pause
            } else {
                pauseTimer();
            }
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

    public void openDialogPhotoReason(int type) {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_attach_photo_reason);

        LinearLayout layoutPhoto = dialog.findViewById(R.id.layoutPhoto);
        CardView layoutPhoto2 = dialog.findViewById(R.id.layoutPhoto2);
        LinearLayout layoutUpload = dialog.findViewById(R.id.layoutUpload);
        LinearLayout layoutCamera = dialog.findViewById(R.id.layoutCamera);
        EditText edtDescReason = dialog.findViewById(R.id.edtDescReason);
        TextInputLayout llReasonDesc = dialog.findViewById(R.id.llReasonDesc);
        ImageView imgDelete = dialog.findViewById(R.id.imgDelete);
        ImageView photo = dialog.findViewById(R.id.photo);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        final String[] descReason = {null};
        Reason reason = imageType.getReason();
        if (reason != null) {
            if (reason.getIs_freetext() == 1) {
                llReasonDesc.setVisibility(View.VISIBLE);
            } else {
                llReasonDesc.setVisibility(View.GONE);
            }
            if (reason.getIs_photo() == 1) {
                layoutPhoto.setVisibility(View.VISIBLE);
                layoutPhoto2.setVisibility(View.VISIBLE);
            } else {
                layoutPhoto.setVisibility(View.GONE);
                layoutPhoto2.setVisibility(View.GONE);
            }
        }

        if (imageType.getPhotoReason() != null) {
            photo.setImageURI(Uri.parse(imageType.getPhotoReason()));
//            Utils.loadImageFit(DailySalesmanActivity.this, imageType.getPhotoReason(), photo);
            photo.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.VISIBLE);
            layoutUpload.setVisibility(View.GONE);
        } else {
            photo.setVisibility(View.GONE);
            imgDelete.setVisibility(View.GONE);
            layoutUpload.setVisibility(View.VISIBLE);
        }

        imgDelete.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoReason(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            Utils.loadImageFit(DailySalesmanActivity.this, null, photo);
            layoutUpload.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.GONE);
            photo.setVisibility(View.GONE);

        });

        edtDescReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("") && !s.toString().equals("-")) {
                    descReason[0] = s.toString();
                }
            }
        });

        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                askPermissionCamera();//photo reason
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(v -> {
            int param = 0;
            if (reason != null) {
                if (reason.getIs_freetext() == 1) {
                    if (Helper.isNullOrEmpty(descReason[0])) {
                        param++;
                    }
                }
                if (reason.getIs_photo() == 1) {
                    if (Helper.isNullOrEmpty(imageType.getPhotoReason())) {
                        param++;
                    }
                }

                if (param == 0) {
                    if (type == 1) {
                        visitSales.setDescPauseReason(descReason[0]);
                        visitSales.setPhotoPauseReason(imageType.getPhotoReason());
                        pauseTimer();
                    } else {
                        visitSales.setDescNotBuyReason(descReason[0]);
                        visitSales.setPhotoNotBuyReason(imageType.getPhotoReason());
//                        Map req = new HashMap();
//                        req.put("id", outletHeader.getId());
//                        req.put("date", Helper.getTodayDate(Constants.DATE_FORMAT_3));
//
//                        int storeCheck = database.getCountStoreCheck(req);
//                        if (reason.getId() != Constants.ID_REASON_TOKO_TUTUP) {//toko tutup, di hard code, agar tdak usah store check
//                            if (storeCheck == 0) {
//                                setToast("Silahkan check stock toko!");
//                            } else {
//                                openDialogPhotoCheckOut();//dialog photo reason check out
//                            }
//                        } else {
                        openDialogPhotoCheckOut();//dialog photo reason check out
//                        }
                    }
                    dialog.dismiss();
                } else {
                    setToast("Semua field harus terisi");
                }
            } else {
                setToast("Silahkan coba lagi");
            }
        });

        dialog.show();
    }

    public void openDialogPhotoCheckOut() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_attach_photo_check_out);

        LinearLayout layoutCamera = dialog.findViewById(R.id.layoutCamera);
        LinearLayout layoutUpload = dialog.findViewById(R.id.layoutUpload);
        ImageView imgDelete = dialog.findViewById(R.id.imgDelete);
        ImageView photo = dialog.findViewById(R.id.photo);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        if (imageType.getPhotoCheckOut() != null) {
            photo.setImageURI(Uri.parse(imageType.getPhotoCheckOut()));
//            Utils.loadImageFit(DailySalesmanActivity.this, imageType.getPhotoCheckOut(), photo);
            photo.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.VISIBLE);
            layoutUpload.setVisibility(View.GONE);
        } else {
            photo.setVisibility(View.GONE);
            imgDelete.setVisibility(View.GONE);
            layoutUpload.setVisibility(View.VISIBLE);
        }

        imgDelete.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoCheckOut(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            Utils.loadImageFit(DailySalesmanActivity.this, null, photo);
            layoutUpload.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.GONE);
            photo.setVisibility(View.GONE);
        });

        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                typeImage = 16;//check out
                imageType.setPosImage(typeImage);
                imageType.setVisitSalesman(visitSales);
                imageType.setIdName(user.getUsername());
                Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
                askPermissionCamera();//photo check out
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(v -> {
            if (imageType.getPhotoCheckOut() != null) {
                visitSales.setPhotoCheckOut(imageType.getPhotoCheckOut());
                dialog.dismiss();
                updateLocation = 0;
                checkLocationPermission();//0, check out photo
            } else {
                setToast("Silahkan foto untuk check out");
            }
        });

        dialog.show();
    }

    private void moveReturn() {
        if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
            Intent intent = new Intent(this, ReturnAddActivity.class);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//for show image from picker
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ReturnDetailActivity.class);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(intent);
        }
    }

    private void setView() {
        limitFakturAdapter = new CustomerInfoLimitFakturAdapter(this, limitFakturList, header -> {
        });

        rvLimitFaktur.setAdapter(limitFakturAdapter);

        dctOutletAdapter = new CustomerInfoDctOutletAdapter(this, dctOutletList, header -> {
        });

        rvDCTOutlet.setAdapter(dctOutletAdapter);

        promoAdapter = new CustomerInfoPromoAdapter(this, promoList, header -> {
        });

        rvPromo.setAdapter(promoAdapter);
    }

    private void setData() {
        if (SessionManagerQubes.getOutletHeader() != null) {
            outletHeader = SessionManagerQubes.getOutletHeader();
            visitSales = database.getVisitSalesman(outletHeader);

            if (visitSales != null) {
                txtNPWP.setText(Helper.isEmpty(outletHeader.getNo_npwp(), ""));
                txtKTP.setText(Helper.isEmpty(outletHeader.getNik(), ""));
                totalTagihan = database.getTotalTagihanInvoiceCustomer(outletHeader.getId()) + database.getTotalTagihanOrderCustomer(outletHeader.getId());
                txtTotalTagihan.setText(format.format(totalTagihan));
                txtSisaKreditLimit.setText(format.format(database.getLKCustomer(outletHeader)));
                if (outletHeader.isNoo()) {
                    txtKreditLimitAwal.setText(format.format(outletHeader.getLimit_kredit()));
                } else {
                    txtKreditLimitAwal.setText(format.format(outletHeader.getLimit_kredit_awal()));
                }
                txtPhone.setText(Helper.isEmpty(outletHeader.getNo_tlp(), ""));
                txtNamaPemilik.setText(Helper.isEmpty(outletHeader.getNama_pemilik(), ""));
                String idOutlet = Helper.isEmpty(outletHeader.getId(), "");
                String nama = Helper.isEmpty(outletHeader.getNama(), "");
                txtOutlet.setText(idOutlet + " - " + nama);
                txtOrder.setText(getOrderType(outletHeader, user));

                String idTypeCust = Helper.isEmpty(outletHeader.getType_customer(), "");
                String nameTypeCust = Helper.isEmpty(outletHeader.getName_type_customer(), "");
                txtTypeOutlet.setText(idTypeCust + " - " + nameTypeCust);

                limitFakturList = new ArrayList<>();
                dctOutletList = new ArrayList<>();
//                oustandingFaktur = new ArrayList<>();
                promoList = new ArrayList<>();

                if (!outletHeader.isNoo()) {
                    promoList.addAll(database.getPromotionRouteByIdCustomer(outletHeader.getId()));
                    dctOutletList.addAll(database.getDctByIdCustomer(outletHeader.getId()));//getDctByIdCustomer
                }
                limitFakturList.addAll(database.getUsedBonByIdCustomer(outletHeader.getId()));//hitung bon yg sudah terpakai
//                limitFakturList.addAll(database.getMaxBonByIdCustomer(outletHeader.getId()));//hitung sisa bon yang masih bisa di pakai
//                oustandingFaktur.addAll(database.getOutstandingFaktur(outletHeader.getId()));//getOutstandingFaktur

                outstandingFaktur = database.getCountInvoiceCustomer(outletHeader.getId());

                setLayoutFromStatus();//first
                setView();
            } else {
                setToast("Gagal mengambil data");
                onBackPressed();
            }
        } else {
            setToast("Gagal mengambil data");
            onBackPressed();
        }
    }

    private void setLayoutFromStatus() {
        switch (outletHeader.getStatus()) {
            case Constants.CHECK_IN_VISIT:
                txtStatus.setText("Pause");
                imgPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_visit));
                btnCheckOut.setVisibility(View.VISIBLE);
                llKTP.setEnabled(true);
                llNPWP.setEnabled(true);
                llOutlet.setEnabled(true);
                break;
            case Constants.PAUSE_VISIT:
                visitSales.setResumeTime(null);
                txtStatus.setText("Resume");
                imgPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.aspp_ic_play_visit));
                btnCheckOut.setVisibility(View.VISIBLE);
                llKTP.setEnabled(false);
                llNPWP.setEnabled(false);
                llOutlet.setEnabled(false);
                break;
            case Constants.CHECK_OUT_VISIT:
                llPause.setVisibility(View.GONE);
                llTimer.setVisibility(View.GONE);
                btnCheckOut.setVisibility(View.GONE);
                llKTP.setEnabled(false);
                llNPWP.setEnabled(false);
                llOutlet.setEnabled(false);
                break;
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llLokasiToko = findViewById(R.id.llLokasiToko);
        llLokasiGudang = findViewById(R.id.llLokasiGudang);
        llLokasiTagihan = findViewById(R.id.llLokasiTagihan);
        rvDCTOutlet = findViewById(R.id.rvDCTOutlet);
        rvDCTOutlet.setLayoutManager(new LinearLayoutManager(this));
        rvDCTOutlet.setHasFixedSize(true);
        rvLimitFaktur = findViewById(R.id.rvLimitFaktur);
        rvLimitFaktur.setLayoutManager(new LinearLayoutManager(this));
        rvLimitFaktur.setHasFixedSize(true);
        rvPromo = findViewById(R.id.rvPromo);
        rvPromo.setLayoutManager(new LinearLayoutManager(this));
        rvPromo.setHasFixedSize(true);
        imgOutlet = findViewById(R.id.imgOutlet);
        llOutlet = findViewById(R.id.llOutlet);
        imgAddOutlet = findViewById(R.id.imgAddOutlet);
        imgDeleteOutlet = findViewById(R.id.imgDeleteOutlet);
        imgNPWP = findViewById(R.id.imgNPWP);
        llNPWP = findViewById(R.id.llNPWP);
        imgAddNPWP = findViewById(R.id.imgAddNPWP);
        imgDeleteNPWP = findViewById(R.id.imgDeleteNPWP);
        imgKTP = findViewById(R.id.imgKTP);
        llKTP = findViewById(R.id.llKTP);
        imgAddKTP = findViewById(R.id.imgAddKTP);
        imgDeleteKTP = findViewById(R.id.imgDeleteKTP);
        txtNPWP = findViewById(R.id.txtNPWP);
        txtKTP = findViewById(R.id.txtKTP);
        txtTotalTagihan = findViewById(R.id.txtTotalTagihan);
        txtSisaKreditLimit = findViewById(R.id.txtSisaKreditLimit);
        txtKreditLimitAwal = findViewById(R.id.txtKreditLimitAwal);
        txtPhone = findViewById(R.id.txtPhone);
        txtNamaPemilik = findViewById(R.id.txtNamaPemilik);
        timerValue = findViewById(R.id.timerValue);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        txtTypeOutlet = findViewById(R.id.txtTypeOutlet);
        txtOutlet = findViewById(R.id.txtOutlet);
        llPause = findViewById(R.id.llPause);
        llTimer = findViewById(R.id.llTimer);
        imgBack = findViewById(R.id.imgBack);
        llStoreCheck = findViewById(R.id.llStoreCheck);
        llOrder = findViewById(R.id.llOrder);
        llCollection = findViewById(R.id.llCollection);
        llReturn = findViewById(R.id.llReturn);
        txtOrder = findViewById(R.id.txtOrder);
        txtStatus = findViewById(R.id.txtStatus);
        imgPause = findViewById(R.id.imgPause);
        imgLogOut = findViewById(R.id.imgLogOut);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
        setTimerValue();
        if (Helper.getItemParam(Constants.IMAGE_TYPE) != null) {
            imageType = new ImageType();
            imageType = (ImageType) Helper.getItemParam(Constants.IMAGE_TYPE);
            if (imageType.getVisitSalesman() != null) {
                visitSales = imageType.getVisitSalesman();
            }
        } else {
            imageType = new ImageType();
        }

        typeImage = imageType.getPosImage();
        if (getIntent().getExtras() != null) {
            Uri uri = (Uri) getIntent().getExtras().get(Constants.OUTPUT_CAMERA);
            getIntent().removeExtra(Constants.OUTPUT_CAMERA);
            Map req = new HashMap();
            switch (typeImage) {
                case 8:
                    imageType.setPhotoKTP(uri.getPath());
                    outletHeader.setPhotoKtp(uri.getPath());
                    req = new HashMap();
                    req.put("photo", uri.getPath());
                    req.put("photoName", "ktp_" + outletHeader.getId());
                    req.put("typePhoto", "ktp");
                    req.put("customerID", outletHeader.getId());
                    req.put("username", user.getUsername());
                    req.put("idDB", visitSales.getIdHeader());
                    database.addPhoto(req);//ktp
                    break;
                case 9:
                    imageType.setPhotoNPWP(uri.getPath());
                    outletHeader.setPhotoNpwp(uri.getPath());
                    req = new HashMap();
                    req.put("photo", uri.getPath());
                    req.put("photoName", "npwp_" + outletHeader.getId());
                    req.put("typePhoto", "npwp");
                    req.put("customerID", outletHeader.getId());
                    req.put("username", user.getUsername());
                    req.put("idDB", visitSales.getIdHeader());
                    database.addPhoto(req);//npwp
                    break;
                case 10:
                    imageType.setPhotoOutlet(uri.getPath());
                    outletHeader.setPhotoOutlet(uri.getPath());
                    req = new HashMap();
                    req.put("photo", uri.getPath());
                    req.put("photoName", "outlet_" + outletHeader.getId());
                    req.put("typePhoto", "outlet");
                    req.put("customerID", outletHeader.getId());
                    req.put("username", user.getUsername());
                    req.put("idDB", visitSales.getIdHeader());
                    database.addPhoto(req);//outlet
                    break;
                case 11:
                    imageType.setPhotoReason(uri.getPath());
                    openDialogPhotoReason(1);//on resume pause
                    break;
                case 12:
                    imageType.setPhotoReason(uri.getPath());
                    openDialogPhotoReason(2);//on resume not buy
                    break;
                case 16:
                    imageType.setPhotoCheckOut(uri.getPath());
                    openDialogPhotoCheckOut();//onresume
                    break;
            }
//            if (outletHeader.isNoo()) {
//                database.updatePhotoNoo(outletHeader, user.getUsername());
//            } else {
//                database.updatePhoto(outletHeader, user.getUsername());
//            }
        }
        if (outletHeader.getPhotoKtp() != null) {
            imgKTP.setImageURI(Uri.parse(outletHeader.getPhotoKtp()));
//            Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoKtp(), imgKTP);
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgDeleteKTP.setVisibility(View.VISIBLE);
            } else {
                imgDeleteKTP.setVisibility(View.GONE);
            }
            imgAddKTP.setVisibility(View.GONE);
        } else {
            Utils.getImageCust(DailySalesmanActivity.this, imgKTP, outletHeader.getId(), Constants.TYPE_PHOTO_KTP);
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgAddKTP.setVisibility(View.VISIBLE);
            } else {
                imgAddKTP.setVisibility(View.GONE);
            }
        }

        if (outletHeader.getPhotoNpwp() != null) {
            imgNPWP.setImageURI(Uri.parse(outletHeader.getPhotoNpwp()));
//            Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoNpwp(), imgNPWP);
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgDeleteNPWP.setVisibility(View.VISIBLE);
            } else {
                imgDeleteNPWP.setVisibility(View.GONE);
            }
            imgAddNPWP.setVisibility(View.GONE);
        } else {
            Utils.getImageCust(DailySalesmanActivity.this, imgNPWP, outletHeader.getId(), Constants.TYPE_PHOTO_NPWP);
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgAddNPWP.setVisibility(View.VISIBLE);
            } else {
                imgAddNPWP.setVisibility(View.GONE);
            }
        }
        if (outletHeader.getPhotoOutlet() != null) {
            imgOutlet.setImageURI(Uri.parse(outletHeader.getPhotoOutlet()));
//            Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoOutlet(), imgOutlet);
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgDeleteOutlet.setVisibility(View.VISIBLE);
            } else {
                imgDeleteOutlet.setVisibility(View.GONE);
            }
            imgAddOutlet.setVisibility(View.GONE);
        } else {
            Utils.getImageCust(DailySalesmanActivity.this, imgOutlet, outletHeader.getId(), Constants.TYPE_PHOTO_OUTLET);
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgAddOutlet.setVisibility(View.VISIBLE);
            } else {
                imgAddOutlet.setVisibility(View.GONE);
            }
        }
        SessionManagerQubes.setOutletHeader(outletHeader);
        Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
    }

    private void pauseTimer() {
        visitSales.setStatus(Constants.PAUSE_VISIT);
        visitSales.setResumeTime(null);
        visitSales.setTimer(String.valueOf(SystemClock.elapsedRealtime() - timerValue.getBase()));
        boolean result = database.updateVisit(visitSales, user.getUsername(), false);
        if (result) {
            setToast("Pause Success");
            timerValue.stop();
            if (visitSales.getPhotoPauseReason() != null) {
                Map req = new HashMap();
                req.put("photo", visitSales.getPhotoPauseReason());
                req.put("photoName", "reason_pause_" + visitSales.getIdHeader());
                req.put("typePhoto", "pause");
                req.put("idDB", visitSales.getIdHeader());
                req.put("customerID", visitSales.getCustomerId());
                req.put("username", user.getUsername());
                database.addPhoto(req);//reason pause
            }
            outletHeader.setStatus(Constants.PAUSE_VISIT);
            if (outletHeader.isNoo()) {
                database.updateStatusOutletNoo(outletHeader, user.getUsername());
            } else {
                database.updateStatusOutletVisit(outletHeader, user.getUsername());
            }
            SessionManagerQubes.setOutletHeader(outletHeader);
//            txtStatus.setText("Resume");
//            imgPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.aspp_ic_play_visit));
            onResume();
//            setLayoutFromStatus();//abis pause
        } else {
            setToast("Pause Failed");
        }
    }

    private void resumeTimer() {
        if (Helper.valTime == 1) {
            Helper.tempTime = SystemClock.elapsedRealtime();
            Helper.valTime = 2;
        }
        outletHeader.setStatus(Constants.CHECK_IN_VISIT);
        if (outletHeader.isNoo()) {
            database.updateStatusOutletNoo(outletHeader, user.getUsername());
        } else {
            database.updateStatusOutletVisit(outletHeader, user.getUsername());
        }
        visitSales.setStatus(Constants.CHECK_IN_VISIT);
//        visitSales.setResumeTime(String.valueOf(curDate.getTime()));
//        visitSales.setTimer(String.valueOf(timerValue.getBase()));

        database.updateVisit(visitSales, user.getUsername(), false);
        SessionManagerQubes.setOutletHeader(outletHeader);

//        txtStatus.setText("Pause");
//        imgPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_visit));
        onResume();
//        setLayoutFromStatus();//abis resume
    }

    private static void formatTime() {
        long time = SystemClock.elapsedRealtime() - Helper.tempTime;
        h = (int) (time / 3600000);
        m = (int) (time - h * 3600000) / 60000;
        s = (int) (time - h * 3600000 - m * 60000) / 1000;
        Helper.mm = m < 10 ? "0" + m : m + "";
        Helper.ss = s < 10 ? "0" + s : s + "";
    }

    private void playTimerBy(long time) {
        timerValue.setBase(time);
        if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
            timerValue.start();
        } else {
            timerValue.stop();
        }
//        if (!PARAM_STATUS_OUTLET.equals(Constants.PAUSE)
//                && !PARAM_STATUS_OUTLET.equals(Constants.FINISHED)) {
//            timerValue.start();
//        } else {
//            timerValue.stop();
//        }
    }

    private void setTimerValue() {
        /*Timer*/
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_TYPE_6);
        timeDuration = visitSales.getTimer();
//        curDate = SecureDate.getInstance().getDate();
        curDate = Calendar.getInstance(Locale.getDefault()).getTime();

        if (curDate != null) {
//            String currentDate = Helper.convertDateToString(Constants.DATE_TYPE_5, curDate);
//            curTime = getTimeFromDate(currentDate);
            dCurrent = curDate;
        } else {
            curTime = null;
            dCurrent = null;
        }

        if (visitSales.getCheckInTime() != null) {
            try {
                dCheckIn = Helper.convertStringtoDate(Constants.DATE_FORMAT_2, visitSales.getCheckInTime());
//                dCheckIn = format.parse(Helper.changeDateFormat(Constants.DATE_FORMAT_2, Constants.DATE_TYPE_6, visitSales.getCheckInTime()));
//                String date = CalendarUtils.ConvertMilliSecondsToFormattedDate(visitSales.getCheckInTime());
//                checkInTime = getTimeFromDate(date);
            } catch (Exception ignored) {
                checkInTime = null;
            }
        } else {
            checkInTime = null;
        }

        if (visitSales.getPauseTime() != null) {
            try {
                pauseTime = Helper.changeDateFormat(Constants.DATE_FORMAT_2, Constants.DATE_TYPE_6, visitSales.getPauseTime());
//                String pauseDate = CalendarUtils.ConvertMilliSecondsToFormattedDate(visitSales.getPauseTime());
//                pauseTime = getTimeFromDate(pauseDate);
            } catch (Exception ignored) {
                pauseTime = null;
            }
        } else {
            pauseTime = null;
        }

        if (visitSales.getResumeTime() != null) {
            try {
                dResume = Helper.convertStringtoDate(Constants.DATE_FORMAT_2, visitSales.getResumeTime());
//                dResume = format.parse(Helper.changeDateFormat(Constants.DATE_FORMAT_2, Constants.DATE_TYPE_6, visitSales.getResumeTime()));
//                String continueDate = CalendarUtils.ConvertMilliSecondsToFormattedDate(visitSales.getResumeTime());
//                continueTime = getTimeFromDate(continueDate);
            } catch (Exception ignored) {
                continueTime = null;
                dResume = null;
            }
        } else {
            continueTime = null;
            dResume = null;
        }

//        try {
//            if (checkInTime != null) {
//                dCheckIn = format.parse(checkInTime);
//            }
//            if (curTime != null) {
//                dCurrent = format.parse(curTime);
//            }
//            if (continueTime != null) {
//                dResume = format.parse(continueTime);
//            }
//        } catch (ParseException ignored) {
//        }

        //in milliseconds
        long elapseTimeNow = dCheckIn != null ? dCurrent.getTime() - dCheckIn.getTime() : 0;

        switch (outletHeader.getStatus()) {
            case Constants.CHECK_IN_VISIT:
                if (visitSales.getResumeTime() != null) {
                    if (timeDuration != null) {
                        try {
                            playTimerBy(SystemClock.elapsedRealtime() - (Long.parseLong(timeDuration) + (dResume != null ? dCurrent.getTime() - dResume.getTime() : 0)));
                        } catch (NumberFormatException ignored) {

                        }
                    }
                } else {
                    playTimerBy(SystemClock.elapsedRealtime() - elapseTimeNow);
                }
                break;
            case Constants.PAUSE_VISIT:
                if (timeDuration != null) {
                    try {
                        playTimerBy(SystemClock.elapsedRealtime() - (Long.parseLong(timeDuration) + (dResume != null ? dCurrent.getTime() - dResume.getTime() : 0)));
                    } catch (NumberFormatException ignored) {
                    }
                }
                break;
            case Constants.CHECK_OUT_VISIT:
                if (timeDuration != null) {
                    try {
                        playTimerBy(SystemClock.elapsedRealtime() - Long.parseLong(timeDuration));
                    } catch (NumberFormatException ignored) {
                    }
                }
                break;

            default:
                timerValue.start();
                break;
        }
    }

    private String getTimeFromDate(String date) {
        String[] part = date.split(Constants.SPACE);
        return part[1];
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, VisitActivity.class);
        startActivity(intent);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            boolean check = Environment.isExternalStorageManager();
//                            && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
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
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getData() != null) {
                onSelectFromGalleryResult(data);
            }
        } else {
            setToast("Failed to Get Image");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            moveReturn();
        } else if (requestCode == GALLERY_PERM_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else if (requestCode == CAMERA_PERM_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            visitSales.setTimer(String.valueOf(SystemClock.elapsedRealtime() - timerValue.getBase()));
            Helper.takePhoto(DailySalesmanActivity.this);
        } else {
            setToast("This permission(s) required");
        }
    }

    public void openDialogPhoto() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_attach_photo);

        LinearLayout layoutUpload = dialog.findViewById(R.id.layoutUpload);
        LinearLayout layoutGallery = dialog.findViewById(R.id.layoutGallery);
        LinearLayout layoutCamera = dialog.findViewById(R.id.layoutCamera);
        ImageView photo = dialog.findViewById(R.id.photo);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        btnSave.setVisibility(View.GONE);
        photo.setVisibility(View.VISIBLE);
        layoutUpload.setVisibility(View.GONE);

        switch (typeImage) {
            case 8:
                if (outletHeader.getPhotoKtp() != null) {
                    photo.setImageURI(Uri.parse(outletHeader.getPhotoKtp()));
//                    Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoKtp(), photo);

//                    photo.setVisibility(View.VISIBLE);
//                    layoutUpload.setVisibility(View.GONE);
                } else {
                    Utils.getImageCust(DailySalesmanActivity.this, photo, outletHeader.getId(), Constants.TYPE_PHOTO_KTP);
//                    photo.setVisibility(View.GONE);
//                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
            case 9:
                if (outletHeader.getPhotoNpwp() != null) {
                    photo.setImageURI(Uri.parse(outletHeader.getPhotoNpwp()));
//                    Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoNpwp(), photo);

//                    photo.setVisibility(View.VISIBLE);
//                    layoutUpload.setVisibility(View.GONE);
                } else {
                    Utils.getImageCust(DailySalesmanActivity.this, photo, outletHeader.getId(), Constants.TYPE_PHOTO_NPWP);
//                    photo.setVisibility(View.GONE);
//                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
            case 10:
                if (outletHeader.getPhotoOutlet() != null) {
                    photo.setImageURI(Uri.parse(outletHeader.getPhotoOutlet()));
//                    Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoOutlet(), photo);

//                    photo.setVisibility(View.VISIBLE);
//                    layoutUpload.setVisibility(View.GONE);
                } else {
                    Utils.getImageCust(DailySalesmanActivity.this, photo, outletHeader.getId(), Constants.TYPE_PHOTO_OUTLET);
//                    photo.setVisibility(View.GONE);
//                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
        }

        layoutGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                askPermission();
            }
        });

        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                askPermissionCamera();//photo ktp,etc
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERM_CODE);
            }, GALLERY_PERM_CODE);
        } else {
            openGallery();
        }
    }

    private void askPermissionCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    ConfirmationDialogFragment.newInstance(R.string.camera_permission_confirmation,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                                    Constants.REQUEST_CAMERA_CODE,
                                    R.string.camera_and_storage_permission_not_granted)
                            .show(getSupportFragmentManager(), Constants.FRAGMENT_DIALOG);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES}, Constants.REQUEST_CAMERA_CODE);
                }
            } else {
                Helper.takePhoto(DailySalesmanActivity.this);
            }
        } else {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
//                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    ConfirmationDialogFragment.newInstance(R.string.camera_permission_confirmation,
//                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                    Constants.REQUEST_CAMERA_CODE,
//                                    R.string.camera_and_storage_permission_not_granted)
//                            .show(getSupportFragmentManager(), Constants.FRAGMENT_DIALOG);
//                } else {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CAMERA_CODE);
//                }
//            } else {
            Helper.takePhoto(DailySalesmanActivity.this);
//            }
        }
    }

    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        switch (typeImage) {
            case 8:
                imagepath = getDirLoc(getApplicationContext()) + "/ktp" + Helper.getTodayDate(Constants.DATE_TYPE_18) + ".png";
                break;
            case 9:
                imagepath = getDirLoc(getApplicationContext()) + "/npwp" + Helper.getTodayDate(Constants.DATE_TYPE_18) + ".png";
                break;
            case 10:
                imagepath = getDirLoc(getApplicationContext()) + "/outlet" + Helper.getTodayDate(Constants.DATE_TYPE_18) + ".png";
                break;
        }
        uriImagePath = Uri.fromFile(new File(imagepath));
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePath);
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.name());
        photoPickerIntent.putExtra("return-data", true);
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Log.d("onActivityResult", "uriImagePathGallery :" + data.getData().toString());
//        File f = new File(imagepath);
        File f = new File(uriImagePath.getPath());
        if (!f.exists()) {
            try {
                f.createNewFile();
                Utils.copyFile(new File(Utils.getRealPathFromURI(DailySalesmanActivity.this, data.getData())), f);
                Map req = new HashMap();
                switch (typeImage) {
                    case 8:
                        imageType.setPhotoKTP(uriImagePath.getPath());
                        Utils.loadImageFit(DailySalesmanActivity.this, imagepath, imgKTP);
                        outletHeader.setPhotoKtp(uriImagePath.getPath());
                        imgKTP.setVisibility(View.VISIBLE);
                        imgDeleteKTP.setVisibility(View.VISIBLE);
                        imgAddKTP.setVisibility(View.GONE);

                        req = new HashMap();
                        req.put("photo", uriImagePath.getPath());
                        req.put("photoName", "ktp_" + outletHeader.getId());
                        req.put("typePhoto", "ktp");
                        req.put("customerID", outletHeader.getId());
                        req.put("username", user.getUsername());
                        req.put("idDB", visitSales.getIdHeader());
                        database.addPhoto(req);//ktp
                        break;
                    case 9:
                        imageType.setPhotoNPWP(uriImagePath.getPath());
                        Utils.loadImageFit(DailySalesmanActivity.this, imageType.getPhotoNPWP(), imgNPWP);
                        outletHeader.setPhotoNpwp(uriImagePath.getPath());
                        imgNPWP.setVisibility(View.VISIBLE);
                        imgDeleteNPWP.setVisibility(View.VISIBLE);
                        imgAddNPWP.setVisibility(View.GONE);

                        req = new HashMap();
                        req.put("photo", uriImagePath.getPath());
                        req.put("photoName", "npwp_" + outletHeader.getId());
                        req.put("typePhoto", "npwp");
                        req.put("customerID", outletHeader.getId());
                        req.put("username", user.getUsername());
                        req.put("idDB", visitSales.getIdHeader());
                        database.addPhoto(req);//npwp
                        break;
                    case 10:
                        imageType.setPhotoOutlet(uriImagePath.getPath());
                        Utils.loadImageFit(DailySalesmanActivity.this, imageType.getPhotoOutlet(), imgOutlet);
                        outletHeader.setPhotoOutlet(uriImagePath.getPath());
                        imgOutlet.setVisibility(View.VISIBLE);
                        imgDeleteOutlet.setVisibility(View.VISIBLE);
                        imgAddOutlet.setVisibility(View.GONE);

                        req = new HashMap();
                        req.put("photo", uriImagePath.getPath());
                        req.put("photoName", "outlet_" + outletHeader.getId());
                        req.put("typePhoto", "outlet");
                        req.put("customerID", outletHeader.getId());
                        req.put("username", user.getUsername());
                        req.put("idDB", visitSales.getIdHeader());
                        database.addPhoto(req);//outlet
                        break;
                }
                SessionManagerQubes.setOutletHeader(outletHeader);
                Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//                if (outletHeader.isNoo()) {
//                    database.updatePhotoNoo(outletHeader, user.getUsername());
//                } else {
//                    database.updatePhoto(outletHeader, user.getUsername());
//                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void checkLocationPermission() {
        List<String> permissionRequest = new ArrayList<>();
        isLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!isLocationPermissionGranted)
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (isLocationPermissionGranted) {
            if (!Helper.isGPSOn(DailySalesmanActivity.this)) {
                setToast("Please turn on GPS");
                Helper.turnOnGPS(DailySalesmanActivity.this);
            } else {
                getLocationGPS();
            }
        }
    }


    private void getLocationGPS() {
        getAddressWithPermission((result, location) -> {
            Utils.backgroundTask(progress,
//                    () -> Utils.getCurrentAddress(CreateNooActivity.this, location.getLatitude(), location.getLongitude()),
                    () -> Utils.getCurrentAddressFull(DailySalesmanActivity.this, location.getLatitude(), location.getLongitude()), new CallbackOnResult<Address>() {
                        @Override
                        public void onFinish(Address result) {
                            if (location != null) {
                                currentLocation = new HashMap();
                                currentLocation.put("latitude", location.getLatitude());
                                currentLocation.put("longitude", location.getLongitude());
                                currentLocation.put("address", result.getAddressLine(0));
                            } else {
                                currentLocation = null;
                                setToast("Lokasi tidak di temukan");
                            }

                            if (updateLocation == 0) {
                                checkOutCustomer();//on finish
                            } else {
                                openDialogMap();//on finish
                            }
                        }

                        @Override
                        public void onFailed() {
                            currentLocation = null;
                            setToast("Lokasi tidak di temukan");
                            if (updateLocation == 0) {
                                checkOutCustomer();//on failed
                            } else {
                                openDialogMap();//on failed
                            }
                        }
                    });
        });
    }

    private void openDialogMap() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_map_update_location);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtAddress = dialog.findViewById(R.id.txtAddress);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        MapView mapView = dialog.findViewById(R.id.mapView);
        ImageView btCenterMap = dialog.findViewById(R.id.btCenterMap);
        String lokasi = "";
        switch (updateLocation) {
            case 1:
                txtTitle.setText("Update Lokasi Toko");
                lokasi = "Lokasi Toko";
                break;
            case 2:
                txtTitle.setText("Update Lokasi Gudang");
                lokasi = "Lokasi Gudang";
                break;
            case 3:
                txtTitle.setText("Update Lokasi Tagihan");
                lokasi = "Lokasi Tagihan";
                break;
        }

        if (currentLocation != null) {
            txtAddress.setText(currentLocation.get("address") != null
                    ? currentLocation.get("address").toString()
                    : "Lokasi tidak di temukan");
        } else {
            txtAddress.setText("Lokasi tidak di temukan");
        }

        String finalLokasi = lokasi;
        btnUpdate.setOnClickListener(v -> {
            if (currentLocation != null) {
                dialog.dismiss();
                database.updateLokasiVisitSalesman(currentLocation, visitSales, updateLocation);
                setToast("Sukses mengubah lokasi " + finalLokasi);
            } else {
                setToast("Can't get your location.. Please try again..");
            }
        });

        btnNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        IMapController mapController = mapView.getController();
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mapView.setMultiTouchControls(true);
        mapController.setZoom(Constants.ZOOM_LEVEL_LOCATION);

        //icon compass
        CompassOverlay mCompassOverlay = new CompassOverlay(DailySalesmanActivity.this, new InternalCompassOrientationProvider(DailySalesmanActivity.this), mapView);
        mCompassOverlay.enableCompass();
        mapView.getOverlays().add(mCompassOverlay);

        final List<Customer>[] custList = new List[]{new ArrayList<>()};
        if (currentLocation != null) {
            GeoPoint myPosition = new GeoPoint((Double) currentLocation.get("latitude"), (Double) currentLocation.get("longitude"));
            custList[0].add(new Customer("", "You", "", false, (Double) currentLocation.get("latitude"), (Double) currentLocation.get("longitude")));
            mapView.getController().animateTo(myPosition);
        }
        final ArrayList<OverlayItem>[] items = new ArrayList[]{Helper.setOverLayItems(custList[0], DailySalesmanActivity.this)};
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
        }, DailySalesmanActivity.this)}};
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
                    GeoPoint myPosition = new GeoPoint((Double) currentLocation.get("latitude"), (Double) currentLocation.get("longitude"));
                    mapView.getController().animateTo(myPosition);

                    custList[0].clear();
                    custList[0].add(new Customer("", "You", "", false, (Double) currentLocation.get("latitude"), (Double) currentLocation.get("longitude")));

                    items[0] = Helper.setOverLayItems(custList[0], DailySalesmanActivity.this);
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
                    }, DailySalesmanActivity.this)};
                    mapView.getOverlays().add(mOverlay[0][0]);
                    mapView.invalidate();
                } else {
                    setToast("Can't get your location.. Please try again..");
                }
            }
        });

        dialog.show();
    }

    public void getAddressWithPermission(LocationRequestCallback<String, Location> callbackOnResult) {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            mResultReceiver.setCallback(callbackOnResult);
            this.addressCallback = callbackOnResult;
            getAddress();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getAddress() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                mLastLocation = location;
                mResultReceiver.setLastLocation(mLastLocation);
                if (!Geocoder.isPresent()) {
                    if (addressCallback != null) {
                        addressCallback.onFinish("Geocoder not available", location);
                    }
                    return;
                }
                startIntentService();
            } else {
                addressCallback.onFinish(null, null);
                return;
            }
        }).addOnFailureListener(this, e -> {
            addressCallback.onFinish(null, null);
        });
    }

    private void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    public boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_REQUEST);
    }
}
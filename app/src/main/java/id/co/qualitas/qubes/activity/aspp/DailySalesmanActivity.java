package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

import com.google.android.material.textfield.TextInputLayout;

import org.osmdroid.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoDctOutletAdapter;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoOutstandingFakturAdapter;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoPromoAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerReasonAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class DailySalesmanActivity extends BaseActivity implements LocationListener {
    private TextView txtOutlet, txtTypeOutlet, txtStatus;
    private TextView txtNamaPemilik, txtPhone, txtSisaKreditLimit, txtTotalTagihan, txtKTP, txtNPWP;
    private Button btnCheckOut;
    private LinearLayout llPause, llStoreCheck, llOrder, llCollection, llReturn, llTimer;
    private RelativeLayout llKTP, llNPWP, llOutlet;
    private ImageView imgKTP, imgNPWP, imgOutlet, imgPause;
    private ImageView imgDeleteKTP, imgDeleteNPWP, imgDeleteOutlet;
    private ImageView imgAddKTP, imgAddNPWP, imgAddOutlet;
    private RecyclerView rvPromo, rvOutstandingFaktur, rvDCTOutlet;
    private CustomerInfoPromoAdapter promoAdapter;
    private CustomerInfoOutstandingFakturAdapter fakturAdapter;
    private CustomerInfoDctOutletAdapter dctOutletAdapter;
    private List<Material> fakturList;
    private List<Material> dctOutletList;
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
    private int typeImage = 0;
    private boolean isLocationPermissionGranted = false;
    private LocationManager lm;
    private Location currentLocation = null;

    public static Chronometer getTimerValue() {
        return timerValue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_daily_salesman);

        Configuration.getInstance().load(DailySalesmanActivity.this, PreferenceManager.getDefaultSharedPreferences(DailySalesmanActivity.this));
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(DailySalesmanActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DailySalesmanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);

        initialize();
        setView();

        btnCheckOut.setOnClickListener(v -> {
            checkLocationPermission();
        });

        llPause.setOnClickListener(v -> {
            if (outletHeader.getStatus() == Constants.PAUSE_VISIT) {
                resumeTimer();
            } else {
                openDialogPause();
            }
        });

        llStoreCheck.setOnClickListener(v -> {
            visitSales.setTimer(String.valueOf(SystemClock.elapsedRealtime() - timerValue.getBase()));
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                Intent intent = new Intent(this, StoreCheckActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, StoreCheckDetailActivity.class);
                startActivity(intent);
            }
        });

        llOrder.setOnClickListener(v -> {
            visitSales.setTimer(String.valueOf(SystemClock.elapsedRealtime() - timerValue.getBase()));
            SessionManagerQubes.clearCollectionHeaderSession();
            Intent intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
        });

        llCollection.setOnClickListener(v -> {
            visitSales.setTimer(String.valueOf(SystemClock.elapsedRealtime() - timerValue.getBase()));
            SessionManagerQubes.clearCollectionHeaderSession();
            Intent intent = new Intent(this, CollectionVisitActivity.class);
            startActivity(intent);
        });

        llReturn.setOnClickListener(v -> {
            visitSales.setTimer(String.valueOf(SystemClock.elapsedRealtime() - timerValue.getBase()));
            if (checkPermission()) {
                moveReturn();
            } else {
                setToast(getString(R.string.pleaseEnablePermission));
                requestPermission();
            }
        });

        llKTP.setOnClickListener(view -> {
            typeImage = 8;
            imageType.setPosImage(typeImage);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            openDialogPhoto();
        });

        llNPWP.setOnClickListener(view -> {
            typeImage = 9;
            imageType.setPosImage(typeImage);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            openDialogPhoto();
        });

        llOutlet.setOnClickListener(view -> {
            typeImage = 10;
            imageType.setPosImage(typeImage);
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
            if (outletHeader.isNoo()) {
                database.updatePhotoNoo(outletHeader, user.getUsername());
            } else {
                database.updatePhoto(outletHeader, user.getUsername());
            }
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
            if (outletHeader.isNoo()) {
                database.updatePhotoNoo(outletHeader, user.getUsername());
            } else {
                database.updatePhoto(outletHeader, user.getUsername());
            }
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
            if (outletHeader.isNoo()) {
                database.updatePhotoNoo(outletHeader, user.getUsername());
            } else {
                database.updatePhoto(outletHeader, user.getUsername());
            }
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(DailySalesmanActivity.this);
        });
    }

    private void openDialogNotOrder() {
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
            visitSales.setIdCheckOutReason(String.valueOf(reason.getId()));
            visitSales.setNameCheckOutReason(reason.getDescription());
            if (reason.getIs_freetext() == 1 || reason.getIs_photo() == 1) {
                typeImage = 11;
                imageType.setPosImage(typeImage);
                imageType.setVisitSalesman(visitSales);
                imageType.setReason(reason);
                imageType.setType(2);//1 => pause, 2 => checkout
                Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
                openDialogPhotoReason(1);
            } else {
                checkLocationPermission();
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
        outletHeader.setStatus(Constants.CHECK_OUT_VISIT);
        if (outletHeader.isNoo()) {
            database.updateStatusOutletNoo(outletHeader, user.getUsername());
        } else {
            database.updateStatusOutletVisit(outletHeader, user.getUsername());
        }
        Location locCustomer = new Location(LocationManager.GPS_PROVIDER);
        locCustomer.setLatitude(outletHeader.getLatitude());
        locCustomer.setLongitude(outletHeader.getLongitude());
        if (currentLocation != null) {
            boolean outRadius = Helper.checkRadius(currentLocation, locCustomer);
            visitSales.setInsideCheckOut(!outRadius);
        }

        visitSales.setStatus(Constants.CHECK_OUT_VISIT);
        visitSales.setLatCheckOut(currentLocation != null ? currentLocation.getLatitude() : null);
        visitSales.setLongCheckOut(currentLocation != null ? currentLocation.getLongitude() : null);
        database.updateVisit(visitSales, user.getUsername());
        SessionManagerQubes.setOutletHeader(outletHeader);
        onBackPressed();
    }


    public void openDialogPause() {
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
                typeImage = 11;
                imageType.setPosImage(typeImage);
                imageType.setVisitSalesman(visitSales);
                imageType.setReason(reason);
                imageType.setType(1);//1 => pause, 2 => checkout
                Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
                openDialogPhotoReason(1);
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
            Utils.loadImageFit(DailySalesmanActivity.this, imageType.getPhotoReason(), photo);
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
                askPermissionCamera();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(v -> {
            if (type == 1) {
                visitSales.setDescPauseReason(descReason[0]);
                visitSales.setPhotoPauseReason(imageType.getPhotoReason());
                pauseTimer();
            } else {
                visitSales.setDescCheckOutReason(descReason[0]);
                visitSales.setPhotoCheckOutReason(imageType.getPhotoReason());
                checkOutCustomer();
            }
            dialog.dismiss();
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
        fakturAdapter = new CustomerInfoOutstandingFakturAdapter(this, fakturList, header -> {
        });

        rvOutstandingFaktur.setAdapter(fakturAdapter);

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
                txtTotalTagihan.setText(format.format(database.getTotalTagihanCustomer(outletHeader.getId())));
                txtSisaKreditLimit.setText(format.format(outletHeader.getSisaCreditLimit()));
                txtPhone.setText(Helper.isEmpty(outletHeader.getNo_tlp(), ""));
                txtNamaPemilik.setText(Helper.isEmpty(outletHeader.getNama_pemilik(), ""));
                txtOutlet.setText(Helper.isEmpty(outletHeader.getNama(), ""));

                String idTypeCust = Helper.isEmpty(outletHeader.getType_customer(), "");
                String nameTypeCust = Helper.isEmpty(outletHeader.getName_type_customer(), "");
                txtTypeOutlet.setText(idTypeCust + " - " + nameTypeCust);

                fakturList = new ArrayList<>();
                dctOutletList = new ArrayList<>();
                promoList = new ArrayList<>();

                if (!outletHeader.isNoo()) {
                    promoList.addAll(database.getPromotionRouteByIdCustomer(outletHeader.getId()));
                    fakturList.addAll(database.getOutstandingProductFaktur(outletHeader.getId()));

                    dctOutletList.add(new Material("Kratingdaeng", 1));
                    dctOutletList.add(new Material("Redbull", 0));
                    dctOutletList.add(new Material("Vitamin", 2));
                    dctOutletList.add(new Material("Water", 0));
                    dctOutletList.add(new Material("Bat CZ", 1));
                    dctOutletList.add(new Material("Bat ALK", 0));
                }

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
            } else {
                setToast("Gagal mengambil data");
                onBackPressed();
            }
        } else {
            setToast("Gagal mengambil data");
            onBackPressed();
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        rvDCTOutlet = findViewById(R.id.rvDCTOutlet);
        rvDCTOutlet.setLayoutManager(new LinearLayoutManager(this));
        rvDCTOutlet.setHasFixedSize(true);
        rvOutstandingFaktur = findViewById(R.id.rvOutstandingFaktur);
        rvOutstandingFaktur.setLayoutManager(new LinearLayoutManager(this));
        rvOutstandingFaktur.setHasFixedSize(true);
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
//            }
            switch (typeImage) {
                case 8:
                    imageType.setPhotoKTP(uri.toString());
                    outletHeader.setPhotoKtp(uri.toString());
                    break;
                case 9:
                    imageType.setPhotoNPWP(uri.toString());
                    outletHeader.setPhotoNpwp(uri.toString());
                    break;
                case 10:
                    imageType.setPhotoOutlet(uri.toString());
                    outletHeader.setPhotoOutlet(uri.toString());
                    break;
                case 11:
                    imageType.setPhotoReason(uri.toString());
                    openDialogPhotoReason(1);
            }
        }
        if (outletHeader.getPhotoKtp() != null) {
            Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoKtp(), imgKTP);
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgDeleteKTP.setVisibility(View.VISIBLE);
            } else {
                imgDeleteKTP.setVisibility(View.GONE);
            }
        } else {
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgAddKTP.setVisibility(View.VISIBLE);
            } else {
                imgAddKTP.setVisibility(View.GONE);
            }
        }

        if (outletHeader.getPhotoNpwp() != null) {
            Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoNpwp(), imgNPWP);
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgDeleteNPWP.setVisibility(View.VISIBLE);
            } else {
                imgDeleteNPWP.setVisibility(View.GONE);
            }
        } else {
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgAddNPWP.setVisibility(View.VISIBLE);
            } else {
                imgAddNPWP.setVisibility(View.GONE);
            }
        }
        if (outletHeader.getPhotoOutlet() != null) {
            Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoOutlet(), imgOutlet);
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgDeleteOutlet.setVisibility(View.VISIBLE);
            } else {
                imgDeleteOutlet.setVisibility(View.GONE);
            }
        } else {
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
                imgAddOutlet.setVisibility(View.VISIBLE);
            } else {
                imgAddOutlet.setVisibility(View.GONE);
            }
        }
        if (outletHeader.isNoo()) {
            database.updatePhotoNoo(outletHeader, user.getUsername());
        } else {
            database.updatePhoto(outletHeader, user.getUsername());
        }
        SessionManagerQubes.setOutletHeader(outletHeader);
        Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
    }

    private void pauseTimer() {
        timerValue.stop();

        outletHeader.setStatus(Constants.PAUSE_VISIT);
        if (outletHeader.isNoo()) {
            database.updateStatusOutletNoo(outletHeader, user.getUsername());
        } else {
            database.updateStatusOutletVisit(outletHeader, user.getUsername());
        }

        SessionManagerQubes.setOutletHeader(outletHeader);

        visitSales.setStatus(Constants.PAUSE_VISIT);
//        visitSales.setPauseTime(Helper.getTodayDate(Constants.DATE_FORMAT_2));
        visitSales.setResumeTime(null);
        visitSales.setTimer(String.valueOf(SystemClock.elapsedRealtime() - timerValue.getBase()));
        database.updateVisit(visitSales, user.getUsername());

        txtStatus.setText("Resume");
        imgPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.aspp_ic_play_visit));
    }

    private void resumeTimer() {
        if (Helper.valTime == 1) {
            Helper.tempTime = SystemClock.elapsedRealtime();
            Helper.valTime = 2;
        }
        formatResumeTime();
        timerValue.start();
        Helper.resume = false;

        outletHeader.setStatus(Constants.CHECK_IN_VISIT);
        if (outletHeader.isNoo()) {
            database.updateStatusOutletNoo(outletHeader, user.getUsername());
        } else {
            database.updateStatusOutletVisit(outletHeader, user.getUsername());
        }
        visitSales.setStatus(Constants.CHECK_IN_VISIT);
//        visitSales.setResumeTime(String.valueOf(curDate.getTime()));
//        visitSales.setTimer(String.valueOf(timerValue.getBase()));

        database.updateVisit(visitSales, user.getUsername());
        SessionManagerQubes.setOutletHeader(outletHeader);

        txtStatus.setText("Pause");
        imgPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_visit));
    }

    private static void formatTime() {
        long time = SystemClock.elapsedRealtime() - Helper.tempTime;
        h = (int) (time / 3600000);
        m = (int) (time - h * 3600000) / 60000;
        s = (int) (time - h * 3600000 - m * 60000) / 1000;
        Helper.mm = m < 10 ? "0" + m : m + "";
        Helper.ss = s < 10 ? "0" + s : s + "";
    }

    private static void formatResumeTime() {
        timerValue.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChronometerTick(Chronometer cArg) {
                formatTime();
                int finalMinute = Integer.parseInt(Helper.mm) + Helper.tempMinute;
                int finalSecond = Integer.parseInt(Helper.ss) + Helper.tempSecond;

                if (finalSecond >= 60) {
                    finalMinute += 1;
                    finalSecond -= 60;
                }

                String fnMinute = finalMinute < 10 ? "0" + finalMinute : finalMinute + "";
                String fnSecond = finalSecond < 10 ? "0" + finalSecond : finalSecond + "";
                Helper.mm = fnMinute;
                Helper.ss = fnSecond;
                cArg.setText(fnMinute + ":" + fnSecond);
            }
        });
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

        switch (typeImage) {
            case 8:
                if (outletHeader.getPhotoKtp() != null) {
                    Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoKtp(), photo);
                    photo.setVisibility(View.VISIBLE);
                    layoutUpload.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.GONE);
                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
            case 9:
                if (outletHeader.getPhotoNpwp() != null) {
                    Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoNpwp(), photo);
                    photo.setVisibility(View.VISIBLE);
                    layoutUpload.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.GONE);
                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
            case 10:
                if (outletHeader.getPhotoOutlet() != null) {
                    Utils.loadImageFit(DailySalesmanActivity.this, outletHeader.getPhotoOutlet(), photo);
                    photo.setVisibility(View.VISIBLE);
                    layoutUpload.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.GONE);
                    layoutUpload.setVisibility(View.VISIBLE);
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
                askPermissionCamera();
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

    public void askPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, CAMERA_PERM_CODE);
        } else {
            Helper.takePhoto(DailySalesmanActivity.this);
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
        Uri uriImagePath = Uri.fromFile(new File(imagepath));
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePath);
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.name());
        photoPickerIntent.putExtra("return-data", true);
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Log.d("onActivityResult", "uriImagePathGallery :" + data.getData().toString());
        File f = new File(imagepath);
        if (!f.exists()) {
            try {
                f.createNewFile();
                Utils.copyFile(new File(Utils.getRealPathFromURI(DailySalesmanActivity.this, data.getData())), f);

                switch (typeImage) {
                    case 8:
                        imageType.setPhotoKTP(imagepath);
                        Utils.loadImageFit(DailySalesmanActivity.this, imagepath, imgKTP);
                        outletHeader.setPhotoKtp(imagepath);
                        imgKTP.setVisibility(View.VISIBLE);
                        imgDeleteKTP.setVisibility(View.VISIBLE);
                        imgAddKTP.setVisibility(View.GONE);
                        break;
                    case 9:
                        imageType.setPhotoNPWP(imagepath);
                        Utils.loadImageFit(DailySalesmanActivity.this, imageType.getPhotoNPWP(), imgNPWP);
                        outletHeader.setPhotoNpwp(imagepath);
                        imgNPWP.setVisibility(View.VISIBLE);
                        imgDeleteNPWP.setVisibility(View.VISIBLE);
                        imgAddNPWP.setVisibility(View.GONE);
                        break;
                    case 10:
                        imageType.setPhotoOutlet(imagepath);
                        Utils.loadImageFit(DailySalesmanActivity.this, imageType.getPhotoOutlet(), imgOutlet);
                        outletHeader.setPhotoOutlet(imagepath);
                        imgOutlet.setVisibility(View.VISIBLE);
                        imgDeleteOutlet.setVisibility(View.VISIBLE);
                        imgAddOutlet.setVisibility(View.GONE);
                        break;
                }
                SessionManagerQubes.setOutletHeader(outletHeader);
                Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
                if (outletHeader.isNoo()) {
                    database.updatePhotoNoo(outletHeader, user.getUsername());
                } else {
                    database.updatePhoto(outletHeader, user.getUsername());
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
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
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
                if (currentLocation == null) {
                    setToast("Can't get your location.. Please try again..");
                }
                if (database.getCountOrder(outletHeader) == 0) {
                    openDialogNotOrder();
                } else {
                    checkOutCustomer();
                }
            }
        }
    }
}
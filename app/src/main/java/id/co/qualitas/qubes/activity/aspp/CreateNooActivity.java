package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.SpinnerAllDropDownAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerDaerahTingkatAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerTypePriceAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.aspp.ConfirmationDialogFragment;
import id.co.qualitas.qubes.helper.AddressResultReceiver;
import id.co.qualitas.qubes.helper.FetchAddressIntentService;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.RecyclerViewMaxHeight;
import id.co.qualitas.qubes.interfaces.CallbackOnResult;
import id.co.qualitas.qubes.interfaces.LocationRequestCallback;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.CustomerType;
import id.co.qualitas.qubes.model.DaerahTingkat;
import id.co.qualitas.qubes.model.DepoRegion;
import id.co.qualitas.qubes.model.DropDown;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class CreateNooActivity extends BaseActivity {
    private Button btnSave;
    private TextView txtKodePos, txtKelurahan, txtKecamatan, txtKotaKabupaten, txtProvinsi;
    private TextView txtTypeToko, txtPriceListType, txtCreditLimit, txtGPSLocation;
    private EditText edtPhone, edtNamaToko, edtAddress, edtNamaNpwp, edtRoute;
    private EditText edtNoNpwp1, edtNoNpwp2, edtNoNpwp3, edtNoNpwp4;
    private EditText edtNik1, edtNik2, edtNik3, edtNik4;
    private EditText edtAlamatNpwp, edtNamaPemilik, edtLokasi, edtJenisUsaha, edtLamaUsaha;
    private Spinner spnSuku, spnStatusToko, spnStatusNpwp, spnUdf5, spnKelasOutlet, spnDay;
    private CheckBox cb1, cb2, cb3, cb4;
    private RelativeLayout llKTP, llNPWP, llOutlet;
    private LinearLayout llNoNpwp;
    private ImageType imageType;
    private int typeImage = 0;
    public static final int GALLERY_PERM_CODE = 101;
    public static final int CAMERA_PERM_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView imgKTP, imgNPWP, imgOutlet;
    ImageView imgAddKTP, imgAddNPWP, imgAddOutlet;
    ImageView imgDeleteKTP, imgDeleteNPWP, imgDeleteOutlet;
    private FilteredSpinnerDaerahTingkatAdapter kodePosAdapter, kelurahanAdapter, kecamatanAdapter, kabupatenAdapter, provinsiAdpater;
    private FilteredSpinnerTypePriceAdapter typeTokoAdapter, priceListAdapter;
    private DaerahTingkat daerahTingkat;
    private Customer customerNoo;
    //    private ArrayAdapter<String> sukuAdapter, statusNpwpAdapter, statusTokoAdapter;
    private ArrayAdapter<String> udf5Adapter;
    private SpinnerAllDropDownAdapter sukuAdapter, statusNpwpAdapter, statusTokoAdapter, dayAdapter, kelasOutletAdapter;
    private String selectedImage;
    //location
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private LocationRequestCallback<String, Location> addressCallback;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private List<DropDown> statusToko, statusNPWP, suku, day, kelasOutlet;
    int offset;
    LinearLayout loadingDataBottom;
    RecyclerViewMaxHeight rv;
    List<DaerahTingkat> daerahTingkatList;
    private LinearLayoutManager linearLayoutManMaterial;
    private boolean loading = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_create_noo);

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
        mResultReceiver = new AddressResultReceiver(new Handler());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CreateNooActivity.this);
        });

        btnSave.setOnClickListener(v -> {
            saveDataNoo();//save
            if (validateData() == 0) {
                progress.show();
                new RequestUrl().execute();
            }
        });

        llKTP.setOnClickListener(view -> {
            typeImage = 1;
            imageType.setPosImage(typeImage);
            imageType.setIdName(user.getUsername());
            saveDataNoo();//ktp
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            openDialogPhoto();
        });

        llNPWP.setOnClickListener(view -> {
            typeImage = 2;
            imageType.setPosImage(typeImage);
            imageType.setIdName(user.getUsername());
            saveDataNoo();//npwp
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            openDialogPhoto();
        });

        llOutlet.setOnClickListener(view -> {
            typeImage = 3;
            imageType.setPosImage(typeImage);
            imageType.setIdName(user.getUsername());
            saveDataNoo();//outlet
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            openDialogPhoto();
        });

        imgDeleteKTP.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoKTP(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            Utils.loadImageFit(CreateNooActivity.this, null, imgKTP);
            imgAddKTP.setVisibility(View.VISIBLE);
            imgDeleteKTP.setVisibility(View.GONE);
            imgKTP.setVisibility(View.GONE);
        });

        imgDeleteNPWP.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoNPWP(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            Utils.loadImageFit(CreateNooActivity.this, null, imgNPWP);
            imgAddNPWP.setVisibility(View.VISIBLE);
            imgDeleteNPWP.setVisibility(View.GONE);
            imgNPWP.setVisibility(View.GONE);
        });

        imgDeleteOutlet.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoOutlet(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            Utils.loadImageFit(CreateNooActivity.this, null, imgOutlet);
            imgAddOutlet.setVisibility(View.VISIBLE);
            imgDeleteOutlet.setVisibility(View.GONE);
            imgOutlet.setVisibility(View.GONE);
        });

        txtGPSLocation.setOnClickListener(view -> {
            getLocationGPS();
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
//            if (customerNoo == null) {
//                customerNoo = new Customer();
//                customerNoo.setLongitude(currentLocation.getLongitude());
//                customerNoo.setLatitude(currentLocation.getLatitude());
//                txtGPSLocation.setText(String.valueOf(currentLocation.getLatitude()) + "," + String.valueOf(currentLocation.getLongitude()));
//            } else {
//                setToast("Lokasi tidak di temukan");
//            }
        });

//        llNoNpwp.setOnClickListener( v-> {
//            edtNoNpwp1.requestFocus();
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(edtNoNpwp1, InputMethodManager.SHOW_IMPLICIT);
//        });

        InputFilter inputFilter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        edtNoNpwp1.setFilters(new InputFilter[]{inputFilter});
        edtNoNpwp2.setFilters(new InputFilter[]{inputFilter});
        edtNoNpwp3.setFilters(new InputFilter[]{inputFilter});
        edtNoNpwp4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

        setEditTextListener(edtNoNpwp1, edtNoNpwp2);
        setEditTextListener(edtNoNpwp2, edtNoNpwp3);
        setEditTextListener(edtNoNpwp3, edtNoNpwp4);
        setEditTextListener(edtNoNpwp4, null);

        edtNik1.setFilters(new InputFilter[]{inputFilter});
        edtNik2.setFilters(new InputFilter[]{inputFilter});
        edtNik3.setFilters(new InputFilter[]{inputFilter});
        edtNik4.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

        setEditTextListener(edtNik1, edtNik2);
        setEditTextListener(edtNik2, edtNik3);
        setEditTextListener(edtNik3, edtNik4);
        setEditTextListener(edtNik4, null);

        cb1.setOnCheckedChangeListener(new checkWeek1());
        cb2.setOnCheckedChangeListener(new checkWeek2());
        cb3.setOnCheckedChangeListener(new checkWeek3());
        cb4.setOnCheckedChangeListener(new checkWeek4());
    }

    class checkWeek1 implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setW1(cb1.isChecked());
        }
    }

    class checkWeek2 implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setW2(cb2.isChecked());
        }
    }

    class checkWeek3 implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setW3(cb3.isChecked());
        }
    }

    class checkWeek4 implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setW4(cb4.isChecked());
        }
    }

    private void setEditTextListener(EditText editText, final EditText nextEditText) {
        editText.addTextChangedListener(new TextWatcher() {
            private int position;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
                position = start;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    // Max length reached, move focus to the next EditText
                    if (nextEditText != null) {
                        nextEditText.requestFocus();
                    }
                } else if (s.length() == 0) {
                    // Content deleted, move focus to the previous EditText
                    View focusableView = editText.focusSearch(View.FOCUS_LEFT);
                    if (focusableView != null) {
                        focusableView.requestFocus();
                    }
                } else if (s.length() > 4) {
                    editText.setText(s.delete(position, position + 1));
                }
            }
        });
    }

    private void saveDataNoo() {
        if (customerNoo == null) {
            customerNoo = new Customer();
        }
        if (!Helper.isEmptyEditText(edtNamaToko)) {
            customerNoo.setNama(edtNamaToko.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtAddress)) {
            customerNoo.setAddress(edtAddress.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtPhone)) {
            customerNoo.setNo_tlp(edtPhone.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtNoNpwp1)) {
            customerNoo.setNo_npwp1(edtNoNpwp1.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtNoNpwp2)) {
            customerNoo.setNo_npwp2(edtNoNpwp2.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtNoNpwp3)) {
            customerNoo.setNo_npwp3(edtNoNpwp3.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtNoNpwp4)) {
            customerNoo.setNo_npwp4(edtNoNpwp4.getText().toString().trim());
        }
        String npwp = Helper.isEmpty(customerNoo.getNo_npwp1(), "") + Helper.isEmpty(customerNoo.getNo_npwp2(), "") + Helper.isEmpty(customerNoo.getNo_npwp3(), "") + Helper.isEmpty(customerNoo.getNo_npwp4(), "");
        customerNoo.setNo_npwp(npwp);
        if (!Helper.isEmptyEditText(edtNamaNpwp)) {
            customerNoo.setNpwp_name(edtNamaNpwp.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtAlamatNpwp)) {
            customerNoo.setNpwp_address(edtAlamatNpwp.getText().toString().trim());
        }
        //spnStatusNpwp,spnStatusToko,spnSuku, txtTypeToko, txtPriceListType
        if (!Helper.isEmptyEditText(edtNamaPemilik)) {
            customerNoo.setNama_pemilik(edtNamaPemilik.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtNik1)) {
            customerNoo.setNik1(edtNik1.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtNik2)) {
            customerNoo.setNik2(edtNik2.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtNik3)) {
            customerNoo.setNik3(edtNik3.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtNik4)) {
            customerNoo.setNik4(edtNik4.getText().toString().trim());
        }
        String nik = Helper.isEmpty(customerNoo.getNik1(), "") + Helper.isEmpty(customerNoo.getNik2(), "") + Helper.isEmpty(customerNoo.getNik3(), "") + Helper.isEmpty(customerNoo.getNik4(), "");
        customerNoo.setNik(nik);
        if (!Helper.isEmptyEditText(edtLokasi)) {
            customerNoo.setLocation(edtLokasi.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtJenisUsaha)) {
            customerNoo.setJenis_usaha(edtJenisUsaha.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtLamaUsaha)) {
            customerNoo.setLama_usaha(edtLamaUsaha.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtRoute)) {
            customerNoo.setRute(edtRoute.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(txtCreditLimit)) {
            customerNoo.setLimit_kredit(Double.parseDouble(txtCreditLimit.getText().toString().trim().replace(".", "")));
        }

        String week1 = "", week2 = "", week3 = "", week4 = "";
        String day = "H" + customerNoo.getDay();
        if (customerNoo.isW1()) {
            week1 = "P1" + day;
        }
        if (customerNoo.isW2()) {
            week2 = "P2" + day;
        }
        if (customerNoo.isW3()) {
            week3 = "P3" + day;
        }
        if (customerNoo.isW4()) {
            week4 = "P4" + day;
        }
        String rute = week1;
        rute = rute + (!Helper.isNullOrEmpty(week2) ? (!Helper.isNullOrEmpty(rute) ? "-" + week2 : week2) : "");
        rute = rute + (!Helper.isNullOrEmpty(week3) ? (!Helper.isNullOrEmpty(rute) ? "-" + week3 : week3) : "");
        rute = rute + (!Helper.isNullOrEmpty(week4) ? (!Helper.isNullOrEmpty(rute) ? "-" + week4 : week4) : "");

        customerNoo.setRute(rute);
        customerNoo.setSisaCreditLimit(0);
        customerNoo.setTotalTagihan(0);
        customerNoo.setIsSync(0);
        customerNoo.setStatus(0);

        SessionManagerQubes.setCustomerNoo(customerNoo);
    }

    private int validateData() {
        int error = 0;

        if (Helper.isEmptyEditText(edtNamaToko)) {
            error++;
            edtNamaToko.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(edtAddress)) {
            error++;
            edtAddress.setError(getString(R.string.emptyField));
        }
        if (txtKodePos.getText().toString().equals("")) {
            error++;
            txtKodePos.setError(getString(R.string.emptyField));
        }
        if (txtKelurahan.getText().toString().equals("")) {
            error++;
            txtKelurahan.setError(getString(R.string.emptyField));
        }
        if (txtKecamatan.getText().toString().equals("")) {
            error++;
            txtKecamatan.setError(getString(R.string.emptyField));
        }
        if (txtKotaKabupaten.getText().toString().equals("")) {
            error++;
            txtKotaKabupaten.setError(getString(R.string.emptyField));
        }
        if (txtProvinsi.getText().toString().equals("")) {
            error++;
            txtProvinsi.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(edtPhone)) {
            error++;
            edtPhone.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(edtNamaPemilik)) {
            error++;
            edtNamaPemilik.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(edtNik1) || Helper.isEmptyEditText(edtNik2) || Helper.isEmptyEditText(edtNik3) || Helper.isEmptyEditText(edtNik4)) {
            error++;
            setToast("NIK tidak boleh Kosong");
        }
        if (txtTypeToko.getText().toString().equals("")) {
            error++;
            txtTypeToko.setError(getString(R.string.emptyField));
        }
        if (txtPriceListType.getText().toString().equals("")) {
            error++;
            txtPriceListType.setError(getString(R.string.emptyField));
        }
        if (txtGPSLocation.getText().toString().equals("")) {
            error++;
            txtGPSLocation.setError(getString(R.string.emptyField));
        }

        if (Helper.isNullOrEmpty(customerNoo.getRute())) {
            error++;
            setToast("Harus Pilih Rute");
        }

        if (imageType != null) {
            if (imageType.getPhotoKTP() == null) {
                error++;
                setToast("Foto KTP belum ada");
            } else if (imageType.getPhotoOutlet() == null) {
                error++;
                setToast("Foto Outlet belum ada");
            }
        } else {
            error++;
            setToast("Foto KTP dan Outlet belum ada");
        }

        return error;
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
            case 1:
                if (imageType.getPhotoKTP() != null) {
                    Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoKTP(), photo);
                    photo.setVisibility(View.VISIBLE);
                    layoutUpload.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.GONE);
                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (imageType.getPhotoNPWP() != null) {
                    Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoNPWP(), photo);
                    photo.setVisibility(View.VISIBLE);
                    layoutUpload.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.GONE);
                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                if (imageType.getPhotoOutlet() != null) {
                    Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoOutlet(), photo);
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

    private void setDataDefault() {
        edtRoute.setText(Helper.getTodayRoute());
        txtCreditLimit.setText(format.format(Double.parseDouble(database.getCreditLimit() != null ? database.getCreditLimit() : "0")));

        statusToko = database.getDropDown(Constants.DROP_DOWN_STATUS_TOKO);
        statusNPWP = database.getDropDown(Constants.DROP_DOWN_STATUS_NPWP);
        suku = database.getDropDown(Constants.DROP_DOWN_SUKU);
        day = database.getDropDown(Constants.DROP_DOWN_DAY);
        kelasOutlet = database.getDropDown(Constants.DROP_DOWN_OUTLET_CLASS);

        List<String> udf5 = new ArrayList<>();
        udf5.add("GT");
        udf5.add("On Premise");

        sukuAdapter = new SpinnerAllDropDownAdapter(getApplicationContext(), suku);
        spnSuku.setAdapter(sukuAdapter);

        statusTokoAdapter = new SpinnerAllDropDownAdapter(getApplicationContext(), statusToko);
        spnStatusToko.setAdapter(statusTokoAdapter);

        statusNpwpAdapter = new SpinnerAllDropDownAdapter(getApplicationContext(), statusNPWP);
        spnStatusNpwp.setAdapter(statusNpwpAdapter);

        dayAdapter = new SpinnerAllDropDownAdapter(getApplicationContext(), day);
        spnDay.setAdapter(dayAdapter);

        kelasOutletAdapter = new SpinnerAllDropDownAdapter(getApplicationContext(), kelasOutlet);
        spnKelasOutlet.setAdapter(kelasOutletAdapter);

        udf5Adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                return view;
            }
        };
        udf5Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        udf5Adapter.addAll(udf5);
        spnUdf5.setAdapter(udf5Adapter);

        spnDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DropDown clickedItem = (DropDown) adapterView.getItemAtPosition(i);
                if (customerNoo == null) {
                    customerNoo = new Customer();
                }
                String id = clickedItem.getId();
                String text = clickedItem.getValue();
                customerNoo.setDay(id);
                customerNoo.setDay_pos(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnKelasOutlet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DropDown clickedItem = (DropDown) adapterView.getItemAtPosition(i);
                if (customerNoo == null) {
                    customerNoo = new Customer();
                }
                String id = clickedItem.getId();
                String text = clickedItem.getValue();
                customerNoo.setKelas_outlet(id);
                customerNoo.setKelas_outlet_pos(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnSuku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DropDown clickedItem = (DropDown) adapterView.getItemAtPosition(i);
                if (customerNoo == null) {
                    customerNoo = new Customer();
                }
                String id = clickedItem.getId();
                String text = clickedItem.getValue();
                customerNoo.setSuku(id);
                customerNoo.setSuku_pos(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnStatusToko.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DropDown clickedItem = (DropDown) adapterView.getItemAtPosition(i);
                if (customerNoo == null) {
                    customerNoo = new Customer();
                }
                String id = clickedItem.getId();
                String text = clickedItem.getValue();
                customerNoo.setStatus_toko(id);
                customerNoo.setStatus_toko_pos(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnStatusNpwp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // It returns the clicked item.
                DropDown clickedItem = (DropDown) adapterView.getItemAtPosition(i);
                if (customerNoo == null) {
                    customerNoo = new Customer();
                }
                String id = clickedItem.getId();
                String text = clickedItem.getValue();
                customerNoo.setStatus_npwp(id);
                customerNoo.setStatus_npwp_pos(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnUdf5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i != 0) {
                if (customerNoo == null) {
                    customerNoo = new Customer();
                }
                String text = adapterView.getItemAtPosition(i).toString();
//                customerNoo.setTop_khusus(text);
//                customerNoo.setUdf_5(text.equals("GT") ? "G" : "O");
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtKodePos.setOnClickListener(view -> {
            openDialogKodePos();
        });

        txtKelurahan.setOnClickListener(view -> {
            openDialogKelurahan();
        });

        txtKecamatan.setOnClickListener(view -> {
            openDialogKecamatan();
        });

        txtKotaKabupaten.setOnClickListener(view -> {
            openDialogKabupaten();
        });

        txtProvinsi.setOnClickListener(view -> {
            openDialogProvinsi();
        });

        txtTypeToko.setOnClickListener(view -> {
            openDialogTypeOutlet();
        });

        txtPriceListType.setOnClickListener(view -> {
            openDialogTypePrice();
        });
    }

    private void openDialogTypeOutlet() {
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        EditText editText = dialog.findViewById(R.id.edit_text);
        RecyclerView rv = dialog.findViewById(R.id.list_view);

        List<CustomerType> arrayList = new ArrayList<>();
        arrayList.addAll(database.getAllCustomerType());

        typeTokoAdapter = new FilteredSpinnerTypePriceAdapter(CreateNooActivity.this, 1, arrayList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setType_price(header.getId());
            customerNoo.setType_customer(header.getId());
            customerNoo.setName_type_customer(header.getName());
            txtTypeToko.setText(header.getName());
            txtPriceListType.setText(header.getName());
            dialog.dismiss();
        });

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(typeTokoAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                typeTokoAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void openDialogTypePrice() {
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.GONE);
        EditText editText = dialog.findViewById(R.id.edit_text);
        RecyclerView rv = dialog.findViewById(R.id.list_view);

        List<CustomerType> arrayList = new ArrayList<>();
        arrayList.addAll(database.getAllCustomerType());

        priceListAdapter = new FilteredSpinnerTypePriceAdapter(CreateNooActivity.this, 2, arrayList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setType_price(header.getId());
            customerNoo.setType_customer(header.getId());
            customerNoo.setName_type_customer(header.getName());
            txtTypeToko.setText(header.getName());
            txtPriceListType.setText(header.getName());
            dialog.dismiss();
        });

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(priceListAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                priceListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void openDialogProvinsi() {
        offset = 0;
        loading = true;
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        EditText editText = dialog.findViewById(R.id.edit_text);
        loadingDataBottom = dialog.findViewById(R.id.loadingDataBottom);
        rv = dialog.findViewById(R.id.list_view);
        linearLayoutManMaterial = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManMaterial);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        setDataDaerahTingkat();//rpvinsi

//        List<DaerahTingkat> arrayList = new ArrayList<>();
//        arrayList.addAll(database.getAllProvinsi(daerahTingkat));

        daerahTingkatList = new ArrayList<>();
        if (!Helper.isEmptyEditText(editText)) {
            daerahTingkat.setNama_provinsi(editText.getText().toString().trim());
        }
        daerahTingkatList = database.getAllProvinsi(daerahTingkat, offset);
        provinsiAdpater = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 5, daerahTingkatList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setProvinsi(header.getNama_provinsi());
            customerNoo.setIdProvinsi(header.getKode_provinsi());
            txtProvinsi.setText(header.getNama_provinsi());
            dialog.dismiss();
        });
        rv.setAdapter(provinsiAdpater);

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                provinsiAdpater.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0) {
                    itemCountProvinsi();
                } else {
                    if (dy > 0) //check for scroll down
                    {
                        itemCountProvinsi();
                    } else {
                        loadingDataBottom.setVisibility(View.GONE);
                        loading = true;
                    }
                }
            }
        });

        btnSearch.setOnClickListener(v -> {
            if (!Helper.isEmptyEditText(editText)) {
                daerahTingkat.setNama_provinsi(editText.getText().toString().trim());
                offset = 0;
                loading = true;
                daerahTingkatList = database.getAllProvinsi(daerahTingkat, offset);
                provinsiAdpater = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 5, daerahTingkatList, (header, adapterPosition) -> {
                    if (customerNoo == null) {
                        customerNoo = new Customer();
                    }
                    customerNoo.setProvinsi(header.getNama_provinsi());
                    customerNoo.setIdProvinsi(header.getKode_provinsi());
                    txtProvinsi.setText(header.getNama_provinsi());
                    dialog.dismiss();
                });
                rv.setAdapter(provinsiAdpater);
            }
        });
    }

    private void openDialogKabupaten() {
        offset = 0;
        loading = true;
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        EditText editText = dialog.findViewById(R.id.edit_text);
        loadingDataBottom = dialog.findViewById(R.id.loadingDataBottom);
        rv = dialog.findViewById(R.id.list_view);
        linearLayoutManMaterial = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManMaterial);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        setDataDaerahTingkat();//kota

//        List<DaerahTingkat> arrayList = new ArrayList<>();
//        arrayList.addAll(database.getAllKabupaten(daerahTingkat));

        daerahTingkatList = new ArrayList<>();
        if (!Helper.isEmptyEditText(editText)) {
            daerahTingkat.setNama_kabupaten(editText.getText().toString().trim());
        }
        daerahTingkatList = database.getAllKabupaten(daerahTingkat, offset);
        kabupatenAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 4, daerahTingkatList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setKota(header.getNama_kabupaten());
            customerNoo.setIdKota(header.getKode_kabupaten());
            txtKotaKabupaten.setText(header.getNama_kabupaten());
            dialog.dismiss();
        });
        rv.setAdapter(kabupatenAdapter);

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                kabupatenAdapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0) {
                    itemCountKota();
                } else {
                    if (dy > 0) //check for scroll down
                    {
                        itemCountKota();
                    } else {
                        loadingDataBottom.setVisibility(View.GONE);
                        loading = true;
                    }
                }
            }
        });

        btnSearch.setOnClickListener(v -> {
            if (!Helper.isEmptyEditText(editText)) {
                daerahTingkat.setNama_kabupaten(editText.getText().toString().trim());
                offset = 0;
                loading = true;
                daerahTingkatList = database.getAllKabupaten(daerahTingkat, offset);
                kabupatenAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 4, daerahTingkatList, (header, adapterPosition) -> {
                    if (customerNoo == null) {
                        customerNoo = new Customer();
                    }
                    customerNoo.setKota(header.getNama_kabupaten());
                    customerNoo.setIdKota(header.getKode_kabupaten());
                    txtKotaKabupaten.setText(header.getNama_kabupaten());
                    dialog.dismiss();
                });
                rv.setAdapter(kabupatenAdapter);
            }
        });
    }

    private void openDialogKecamatan() {
        offset = 0;
        loading = true;
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        EditText editText = dialog.findViewById(R.id.edit_text);
        loadingDataBottom = dialog.findViewById(R.id.loadingDataBottom);
        rv = dialog.findViewById(R.id.list_view);
        linearLayoutManMaterial = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManMaterial);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        setDataDaerahTingkat();//kecamatan

//        List<DaerahTingkat> arrayList = new ArrayList<>();
//        arrayList.addAll(database.getAllKecamatan(daerahTingkat));

        daerahTingkatList = new ArrayList<>();
        if (!Helper.isEmptyEditText(editText)) {
            daerahTingkat.setNama_kecamatan(editText.getText().toString().trim());
        }
        daerahTingkatList = database.getAllKecamatan(daerahTingkat, offset);
        kecamatanAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 3, daerahTingkatList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setKecamatan(header.getNama_kecamatan());
            customerNoo.setIdKecamatan(header.getKode_kecamatan());
            txtKecamatan.setText(header.getNama_kecamatan());
            dialog.dismiss();
        });
        rv.setAdapter(kecamatanAdapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0) {
                    itemCountKecamatan();
                } else {
                    if (dy > 0) //check for scroll down
                    {
                        itemCountKecamatan();
                    } else {
                        loadingDataBottom.setVisibility(View.GONE);
                        loading = true;
                    }
                }
            }
        });

        btnSearch.setOnClickListener(v -> {
            if (!Helper.isEmptyEditText(editText)) {
                daerahTingkat.setNama_kecamatan(editText.getText().toString().trim());
                offset = 0;
                loading = true;
                daerahTingkatList = database.getAllKecamatan(daerahTingkat, offset);
                kecamatanAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 3, daerahTingkatList, (header, adapterPosition) -> {
                    if (customerNoo == null) {
                        customerNoo = new Customer();
                    }
                    customerNoo.setKecamatan(header.getNama_kecamatan());
                    customerNoo.setIdKecamatan(header.getKode_kecamatan());
                    txtKecamatan.setText(header.getNama_kecamatan());
                    dialog.dismiss();
                });
                rv.setAdapter(kecamatanAdapter);
            }
        });

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                kecamatanAdapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    private void setDataDaerahTingkat() {
        daerahTingkat = new DaerahTingkat();
        daerahTingkat.setKode_pos(Integer.parseInt(!txtKodePos.getText().toString().equals("") ? txtKodePos.getText().toString() : "0"));
        daerahTingkat.setNama_kelurahan(txtKelurahan.getText().toString().trim());
        daerahTingkat.setNama_kecamatan(txtKecamatan.getText().toString().trim());
        daerahTingkat.setNama_kabupaten(txtKotaKabupaten.getText().toString().trim());
        daerahTingkat.setNama_provinsi(txtProvinsi.getText().toString().trim());
    }

    private void openDialogKodePos() {
        offset = 0;
        loading = true;
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        EditText editText = dialog.findViewById(R.id.edit_text);
        loadingDataBottom = dialog.findViewById(R.id.loadingDataBottom);
        rv = dialog.findViewById(R.id.list_view);
        linearLayoutManMaterial = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManMaterial);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        setDataDaerahTingkat();//kode pos

//        List<DaerahTingkat> arrayList = new ArrayList<>();
//       
//        arrayList.addAll(database.getAllKodePos(daerahTingkat, offset));
//
//        kodePosAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 1, arrayList, (header, adapterPosition) -> {
//            if (customerNoo == null) {
//                customerNoo = new Customer();
//            }
//            customerNoo.setKode_pos(String.valueOf(header.getKode_pos()));
//            customerNoo.setKelurahan(header.getNama_kelurahan());
//            customerNoo.setIdKelurahan(header.getKode_kelurahan());
//            customerNoo.setKecamatan(header.getNama_kecamatan());
//            customerNoo.setIdKecamatan(header.getKode_kecamatan());
//            customerNoo.setKota(header.getNama_kabupaten());
//            customerNoo.setIdKota(header.getKode_kabupaten());
//            customerNoo.setProvinsi(header.getNama_provinsi());
//            customerNoo.setIdProvinsi(header.getKode_provinsi());
//
//            txtKodePos.setText(String.valueOf(header.getKode_pos()));
//            txtKelurahan.setText(header.getNama_kelurahan());
//            txtKecamatan.setText(header.getNama_kecamatan());
//            txtKotaKabupaten.setText(header.getNama_kabupaten());
//            txtProvinsi.setText(header.getNama_provinsi());
//            dialog.dismiss();
//        });
//        rv.setAdapter(kodePosAdapter);
//
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                kodePosAdapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        daerahTingkatList = new ArrayList<>();
        if (!Helper.isEmptyEditText(editText)) {
            daerahTingkat.setKode_pos(Integer.parseInt(editText.getText().toString().trim()));
        } else {
            daerahTingkat.setKode_pos(null);
        }
        daerahTingkatList = database.getAllKodePos(daerahTingkat, offset);
        kodePosAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 1, daerahTingkatList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setKode_pos(String.valueOf(header.getKode_pos()));
            customerNoo.setKelurahan(header.getNama_kelurahan());
            customerNoo.setIdKelurahan(header.getKode_kelurahan());
            customerNoo.setKecamatan(header.getNama_kecamatan());
            customerNoo.setIdKecamatan(header.getKode_kecamatan());
            customerNoo.setKota(header.getNama_kabupaten());
            customerNoo.setIdKota(header.getKode_kabupaten());
            customerNoo.setProvinsi(header.getNama_provinsi());
            customerNoo.setIdProvinsi(header.getKode_provinsi());

            txtKodePos.setText(String.valueOf(header.getKode_pos()));
            txtKelurahan.setText(header.getNama_kelurahan());
            txtKecamatan.setText(header.getNama_kecamatan());
            txtKotaKabupaten.setText(header.getNama_kabupaten());
            txtProvinsi.setText(header.getNama_provinsi());
            dialog.dismiss();
        });
        rv.setAdapter(kodePosAdapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0) {
                    itemCountKodePos();
                } else {
                    if (dy > 0) //check for scroll down
                    {
                        itemCountKodePos();
                    } else {
                        loadingDataBottom.setVisibility(View.GONE);
                        loading = true;
                    }
                }
            }
        });

        btnSearch.setOnClickListener(v -> {
            if (!Helper.isEmptyEditText(editText)) {
                daerahTingkat.setKode_pos(Integer.parseInt(editText.getText().toString().trim()));
                offset = 0;
                loading = true;
                daerahTingkatList = database.getAllKodePos(daerahTingkat, offset);
                kodePosAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 1, daerahTingkatList, (header, adapterPosition) -> {
                    if (customerNoo == null) {
                        customerNoo = new Customer();
                    }
                    customerNoo.setKode_pos(String.valueOf(header.getKode_pos()));
                    customerNoo.setKelurahan(header.getNama_kelurahan());
                    customerNoo.setIdKelurahan(header.getKode_kelurahan());
                    customerNoo.setKecamatan(header.getNama_kecamatan());
                    customerNoo.setIdKecamatan(header.getKode_kecamatan());
                    customerNoo.setKota(header.getNama_kabupaten());
                    customerNoo.setIdKota(header.getKode_kabupaten());
                    customerNoo.setProvinsi(header.getNama_provinsi());
                    customerNoo.setIdProvinsi(header.getKode_provinsi());

                    txtKodePos.setText(String.valueOf(header.getKode_pos()));
                    txtKelurahan.setText(header.getNama_kelurahan());
                    txtKecamatan.setText(header.getNama_kecamatan());
                    txtKotaKabupaten.setText(header.getNama_kabupaten());
                    txtProvinsi.setText(header.getNama_provinsi());
                    dialog.dismiss();
                });
                rv.setAdapter(kodePosAdapter);
            }
        });
    }

    public void itemCountKodePos() {
        int visibleItemCount = linearLayoutManMaterial.getChildCount();
        int totalItemCount = linearLayoutManMaterial.getItemCount();
        int pastVisiblesItems = linearLayoutManMaterial.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                offset = totalItemCount;
                loadingDataBottom.setVisibility(View.VISIBLE);
                daerahTingkatList.addAll(database.getAllKodePos(daerahTingkat, offset));
                kodePosAdapter.notifyDataSetChanged();
                loadingDataBottom.setVisibility(View.GONE);
            }
        }
    }

    public void itemCountKelurahan() {
        int visibleItemCount = linearLayoutManMaterial.getChildCount();
        int totalItemCount = linearLayoutManMaterial.getItemCount();
        int pastVisiblesItems = linearLayoutManMaterial.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                offset = totalItemCount;
                loadingDataBottom.setVisibility(View.VISIBLE);
                daerahTingkatList = database.getAllKelurahan(daerahTingkat, offset);
                kelurahanAdapter.notifyDataSetChanged();
                loadingDataBottom.setVisibility(View.GONE);
            }
        }
    }

    public void itemCountKecamatan() {
        int visibleItemCount = linearLayoutManMaterial.getChildCount();
        int totalItemCount = linearLayoutManMaterial.getItemCount();
        int pastVisiblesItems = linearLayoutManMaterial.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                offset = totalItemCount;
                loadingDataBottom.setVisibility(View.VISIBLE);
                daerahTingkatList = database.getAllKecamatan(daerahTingkat, offset);
                kecamatanAdapter.notifyDataSetChanged();
                loadingDataBottom.setVisibility(View.GONE);
            }
        }
    }

    public void itemCountKota() {
        int visibleItemCount = linearLayoutManMaterial.getChildCount();
        int totalItemCount = linearLayoutManMaterial.getItemCount();
        int pastVisiblesItems = linearLayoutManMaterial.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                offset = totalItemCount;
                loadingDataBottom.setVisibility(View.VISIBLE);
                daerahTingkatList = database.getAllKabupaten(daerahTingkat, offset);
                kabupatenAdapter.notifyDataSetChanged();
                loadingDataBottom.setVisibility(View.GONE);
            }
        }
    }

    public void itemCountProvinsi() {
        int visibleItemCount = linearLayoutManMaterial.getChildCount();
        int totalItemCount = linearLayoutManMaterial.getItemCount();
        int pastVisiblesItems = linearLayoutManMaterial.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                offset = totalItemCount;
                loadingDataBottom.setVisibility(View.VISIBLE);
                daerahTingkatList = database.getAllProvinsi(daerahTingkat, offset);
                provinsiAdpater.notifyDataSetChanged();
                loadingDataBottom.setVisibility(View.GONE);
            }
        }
    }

    private void openDialogKelurahan() {
        offset = 0;
        loading = true;
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        EditText editText = dialog.findViewById(R.id.edit_text);
        loadingDataBottom = dialog.findViewById(R.id.loadingDataBottom);
        rv = dialog.findViewById(R.id.list_view);
        linearLayoutManMaterial = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManMaterial);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        setDataDaerahTingkat();//kelurahan
//        List<DaerahTingkat> arrayList = new ArrayList<>();
//        arrayList.addAll(database.getAllKelurahan(daerahTingkat));

        daerahTingkatList = new ArrayList<>();
        if (!Helper.isEmptyEditText(editText)) {
            daerahTingkat.setNama_kelurahan(editText.getText().toString().trim());
        }
        daerahTingkatList = database.getAllKelurahan(daerahTingkat, offset);
        kelurahanAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 2, daerahTingkatList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setKelurahan(header.getNama_kelurahan());
            customerNoo.setIdKelurahan(header.getKode_kelurahan());
            txtKelurahan.setText(header.getNama_kelurahan());
            dialog.dismiss();
        });
        rv.setAdapter(kelurahanAdapter);

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                kelurahanAdapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0) {
                    itemCountKelurahan();
                } else {
                    if (dy > 0) //check for scroll down
                    {
                        itemCountKelurahan();
                    } else {
                        loadingDataBottom.setVisibility(View.GONE);
                        loading = true;
                    }
                }
            }
        });

        btnSearch.setOnClickListener(v -> {
            if (!Helper.isEmptyEditText(editText)) {
                daerahTingkat.setNama_kelurahan(editText.getText().toString().trim());
                offset = 0;
                loading = true;
                daerahTingkatList = database.getAllKelurahan(daerahTingkat, offset);
                kelurahanAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 2, daerahTingkatList, (header, adapterPosition) -> {
                    if (customerNoo == null) {
                        customerNoo = new Customer();
                    }
                    customerNoo.setKelurahan(header.getNama_kelurahan());
                    customerNoo.setIdKelurahan(header.getKode_kelurahan());
                    txtKelurahan.setText(header.getNama_kelurahan());
                    dialog.dismiss();
                });
                rv.setAdapter(kelurahanAdapter);
            }
        });
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoNpwp = findViewById(R.id.llNoNpwp);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        txtGPSLocation = findViewById(R.id.txtGPSLocation);
        spnDay = findViewById(R.id.spnDay);
        spnKelasOutlet = findViewById(R.id.spnKelasOutlet);
        spnUdf5 = findViewById(R.id.spnUdf5);
        txtKodePos = findViewById(R.id.txtKodePos);
        txtKelurahan = findViewById(R.id.txtKelurahan);
        txtKecamatan = findViewById(R.id.txtKecamatan);
        txtKotaKabupaten = findViewById(R.id.txtKotaKabupaten);
        txtProvinsi = findViewById(R.id.txtProvinsi);
        edtPhone = findViewById(R.id.edtPhone);
        edtNamaToko = findViewById(R.id.edtNamaToko);
        edtAddress = findViewById(R.id.edtAddress);
        edtNoNpwp1 = findViewById(R.id.edtNoNpwp1);
        edtNoNpwp2 = findViewById(R.id.edtNoNpwp2);
        edtNoNpwp3 = findViewById(R.id.edtNoNpwp3);
        edtNoNpwp4 = findViewById(R.id.edtNoNpwp4);
        edtNamaNpwp = findViewById(R.id.edtNamaNpwp);
        edtNamaPemilik = findViewById(R.id.edtNamaPemilik);
        edtAlamatNpwp = findViewById(R.id.edtAlamatNpwp);
        edtNik1 = findViewById(R.id.edtNik1);
        edtNik2 = findViewById(R.id.edtNik2);
        edtNik3 = findViewById(R.id.edtNik3);
        edtNik4 = findViewById(R.id.edtNik4);
        edtLokasi = findViewById(R.id.edtLokasi);
        edtJenisUsaha = findViewById(R.id.edtJenisUsaha);
        edtLamaUsaha = findViewById(R.id.edtLamaUsaha);
        txtTypeToko = findViewById(R.id.txtTypeToko);
        txtCreditLimit = findViewById(R.id.txtCreditLimit);
        edtRoute = findViewById(R.id.edtRoute);
        txtPriceListType = findViewById(R.id.txtPriceListType);
        spnStatusNpwp = findViewById(R.id.spnStatusNpwp);
        spnSuku = findViewById(R.id.spnSuku);
        spnStatusToko = findViewById(R.id.spnStatusToko);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSave = findViewById(R.id.btnSave);
        llKTP = findViewById(R.id.llKTP);
        llNPWP = findViewById(R.id.llNPWP);
        llOutlet = findViewById(R.id.llOutlet);
        imgKTP = findViewById(R.id.imgKTP);
        imgNPWP = findViewById(R.id.imgNPWP);
        imgOutlet = findViewById(R.id.imgOutlet);
        imgAddKTP = findViewById(R.id.imgAddKTP);
        imgDeleteKTP = findViewById(R.id.imgDeleteKTP);
        imgAddNPWP = findViewById(R.id.imgAddNPWP);
        imgDeleteNPWP = findViewById(R.id.imgDeleteNPWP);
        imgAddOutlet = findViewById(R.id.imgAddOutlet);
        imgDeleteOutlet = findViewById(R.id.imgDeleteOutlet);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Helper.getItemParam(Constants.IMAGE_TYPE) != null) {
            imageType = new ImageType();
            imageType = (ImageType) Helper.getItemParam(Constants.IMAGE_TYPE);
        } else {
            imageType = new ImageType();
        }
        setDataDefault();
        if (SessionManagerQubes.getCustomerNoo() != null) {
            customerNoo = new Customer();
            customerNoo = SessionManagerQubes.getCustomerNoo();
            setDataToView();
        } else {
            customerNoo = new Customer();
            getLocationGPS();
        }
        typeImage = imageType.getPosImage();

        if (getIntent().getExtras() != null) {
            Uri uri = (Uri) getIntent().getExtras().get(Constants.OUTPUT_CAMERA);
            getIntent().removeExtra(Constants.OUTPUT_CAMERA);
//            Bitmap myBitmap = null;
//            Uri rotate = null;
//            try {
//                myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                myBitmap = Utils.rotateImageIfRequired(myBitmap, uri);
//                rotate = Utils.getImageUri(CreateNooActivity.this, myBitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            switch (typeImage) {
                case 1:
                    imageType.setPhotoKTP(uri.getPath());
                    break;
                case 2:
                    imageType.setPhotoNPWP(uri.getPath());
                    break;
                case 3:
                    imageType.setPhotoOutlet(uri.getPath());
                    break;
            }
        }
        if (imageType.getPhotoKTP() != null) {
            customerNoo.setPhotoKtp(imageType.getPhotoKTP());
            Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoKTP(), imgKTP);
            imgDeleteKTP.setVisibility(View.VISIBLE);
            imgAddKTP.setVisibility(View.GONE);
        }
        if (imageType.getPhotoNPWP() != null) {
            customerNoo.setPhotoNpwp(imageType.getPhotoNPWP());
            Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoNPWP(), imgNPWP);
            imgDeleteNPWP.setVisibility(View.VISIBLE);
            imgAddNPWP.setVisibility(View.GONE);
        }
        if (imageType.getPhotoOutlet() != null) {
            customerNoo.setPhotoOutlet(imageType.getPhotoOutlet());
            Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoOutlet(), imgOutlet);
            imgDeleteOutlet.setVisibility(View.VISIBLE);
            imgAddOutlet.setVisibility(View.GONE);
        }

        Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
        SessionManagerQubes.setCustomerNoo(customerNoo);
    }

    private void setDataToView() {
        if (Helper.isEmptyEditText(txtGPSLocation)) {
            getLocationGPS();
        }
        txtKodePos.setText(Helper.isEmpty(customerNoo.getKode_pos(), ""));
        txtGPSLocation.setText(String.valueOf(customerNoo.getLatitude()) + "," + String.valueOf(customerNoo.getLongitude()));
        txtKelurahan.setText(Helper.isEmpty(customerNoo.getKelurahan(), ""));
        txtKecamatan.setText(Helper.isEmpty(customerNoo.getKecamatan(), ""));
        txtKotaKabupaten.setText(Helper.isEmpty(customerNoo.getKota(), ""));
        txtProvinsi.setText(Helper.isEmpty(customerNoo.getProvinsi(), ""));
        edtPhone.setText(Helper.isEmpty(customerNoo.getNo_tlp(), ""));
        edtNamaToko.setText(Helper.isEmpty(customerNoo.getNama(), ""));
        edtAddress.setText(Helper.isEmpty(customerNoo.getAddress(), ""));
        edtNoNpwp1.setText(Helper.isEmpty(customerNoo.getNo_npwp1(), ""));
        edtNoNpwp2.setText(Helper.isEmpty(customerNoo.getNo_npwp2(), ""));
        edtNoNpwp3.setText(Helper.isEmpty(customerNoo.getNo_npwp3(), ""));
        edtNoNpwp4.setText(Helper.isEmpty(customerNoo.getNo_npwp4(), ""));
        edtNamaNpwp.setText(Helper.isEmpty(customerNoo.getNpwp_name(), ""));
        edtNamaPemilik.setText(Helper.isEmpty(customerNoo.getNama_pemilik(), ""));
        edtAlamatNpwp.setText(Helper.isEmpty(customerNoo.getNpwp_address(), ""));
        edtNik1.setText(Helper.isEmpty(customerNoo.getNik1(), ""));
        edtNik2.setText(Helper.isEmpty(customerNoo.getNik2(), ""));
        edtNik3.setText(Helper.isEmpty(customerNoo.getNik3(), ""));
        edtNik4.setText(Helper.isEmpty(customerNoo.getNik4(), ""));
        edtLokasi.setText(Helper.isEmpty(customerNoo.getLocation(), ""));
        edtJenisUsaha.setText(Helper.isEmpty(customerNoo.getJenis_usaha(), ""));
        edtLamaUsaha.setText(Helper.isEmpty(customerNoo.getLama_usaha(), ""));
        txtTypeToko.setText(Helper.isEmpty(customerNoo.getName_type_customer(), ""));
        txtPriceListType.setText(Helper.isEmpty(customerNoo.getName_type_customer(), ""));
        txtCreditLimit.setText(customerNoo.getLimit_kredit() != 0 ? format.format(customerNoo.getLimit_kredit()) : format.format(Double.parseDouble(database.getCreditLimit() != null ? database.getCreditLimit() : "0")));
        edtRoute.setText(Helper.isEmpty(customerNoo.getRute(), Helper.getTodayRoute()));

//        int spinnerPositionSuku = sukuAdapter.getPosition(customerNoo.getSuku());
//        int spinnerPositionStatusToko = statusTokoAdapter.getPosition(customerNoo.getStatus_toko());
//        int spinnerPositionStatusNpwp = statusNpwpAdapter.getPosition(customerNoo.getStatus_npwp());
        int spinnerPositionUdf5 = udf5Adapter.getPosition(customerNoo.getTop_khusus());

        spnSuku.setSelection(customerNoo.getSuku_pos());
        spnDay.setSelection(customerNoo.getDay_pos());
        spnKelasOutlet.setSelection(customerNoo.getKelas_outlet_pos());
        spnStatusToko.setSelection(customerNoo.getStatus_toko_pos());
        spnStatusNpwp.setSelection(customerNoo.getStatus_npwp_pos());
        spnUdf5.setSelection(spinnerPositionUdf5);

        cb1.setChecked(customerNoo.isW1());
        cb2.setChecked(customerNoo.isW2());
        cb3.setChecked(customerNoo.isW3());
        cb4.setChecked(customerNoo.isW4());
    }

    //foto
    public void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
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
                Helper.takePhoto(CreateNooActivity.this);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ConfirmationDialogFragment.newInstance(R.string.camera_permission_confirmation,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constants.REQUEST_CAMERA_CODE,
                                    R.string.camera_and_storage_permission_not_granted)
                            .show(getSupportFragmentManager(), Constants.FRAGMENT_DIALOG);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CAMERA_CODE);
                }
            } else {
                Helper.takePhoto(CreateNooActivity.this);
            }
        }
    }

    public void openGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
////        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        switch (typeImage) {
            case 1:
                selectedImage = getDirLoc(getApplicationContext()) + "/ktp" + Helper.getTodayDate(Constants.DATE_TYPE_18) + ".png";
                break;
            case 2:
                selectedImage = getDirLoc(getApplicationContext()) + "/npwp" + Helper.getTodayDate(Constants.DATE_TYPE_18) + ".png";
                break;
            case 3:
                selectedImage = getDirLoc(getApplicationContext()) + "/outlet" + Helper.getTodayDate(Constants.DATE_TYPE_18) + ".png";
                break;
        }
        Uri uriImagePath = Uri.fromFile(new File(selectedImage));
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePath);
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.name());
        photoPickerIntent.putExtra("return-data", true);
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GALLERY_PERM_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else if (requestCode == CAMERA_PERM_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Helper.takePhoto(CreateNooActivity.this);
        } else if (requestCode == Constants.LOCATION_PERMISSION_REQUEST
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            if (Utils.isGPSOn(this)) {
                getLocationGPS();
            } else {
                Utils.turnOnGPS(this);
            }
        } else {
            setToast("This permission(s) required");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getData() != null) {
                onSelectFromGalleryResult(data);
            }
        } else {
            setToast("Failed to Get Image");
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Log.d("onActivityResult", "uriImagePathGallery :" + data.getData().toString());
        File f = new File(selectedImage);
        if (!f.exists()) {
            try {
                f.createNewFile();
                Utils.copyFile(new File(Utils.getRealPathFromURI(CreateNooActivity.this, data.getData())), f);
                switch (typeImage) {
                    case 1:
                        imageType.setPhotoKTP(selectedImage);
                        Utils.loadImageFit(CreateNooActivity.this, selectedImage, imgKTP);
                        imgKTP.setVisibility(View.VISIBLE);
                        imgDeleteKTP.setVisibility(View.VISIBLE);
                        imgAddKTP.setVisibility(View.GONE);
                        break;
                    case 2:
                        imageType.setPhotoNPWP(selectedImage);
                        Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoNPWP(), imgNPWP);
                        imgNPWP.setVisibility(View.VISIBLE);
                        imgDeleteNPWP.setVisibility(View.VISIBLE);
                        imgAddNPWP.setVisibility(View.GONE);
                        break;
                    case 3:
                        imageType.setPhotoOutlet(selectedImage);
                        Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoOutlet(), imgOutlet);
                        imgOutlet.setVisibility(View.VISIBLE);
                        imgDeleteOutlet.setVisibility(View.VISIBLE);
                        imgAddOutlet.setVisibility(View.GONE);
                        break;
                }

                Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(this, VisitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private String getDepo() {
        String depo = "";
        if (user.getDepoRegionList() != null) {
            for (int i = 0; i < user.getDepoRegionList().size(); i++) {
                DepoRegion depoRegion = user.getDepoRegionList().get(i);
                depo = depo + depoRegion.getId_depo();
            }
        }
        return depo;
    }

    private class RequestUrl extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                boolean result = false;
                customerNoo.setId(Constants.ID_NOO_MOBILE.concat(user.getUsername()).concat(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
                customerNoo.setOrder_type(user.getType_sales());
                customerNoo.setRoute_order(getDepo());
                int header = database.addNoo(customerNoo, user.getUsername());
                if (header == -1) {
                    result = false;
                } else {
                    result = true;

                    Map req = new HashMap();
                    if (customerNoo.getPhotoKtp() != null) {
                        req = new HashMap();
                        req.put("photo", customerNoo.getPhotoKtp());
                        req.put("photoName", "ktp_" + customerNoo.getId());
                        req.put("typePhoto", "ktp");
                        req.put("idDB", customerNoo.getId());
                        req.put("customerID", customerNoo.getId());
                        req.put("username", user.getUsername());
                        database.addPhoto(req);//ktp
                    }

                    if (customerNoo.getPhotoNpwp() != null) {
                        req = new HashMap();
                        req.put("photo", customerNoo.getPhotoNpwp());
                        req.put("photoName", "npwp_" + customerNoo.getId());
                        req.put("typePhoto", "npwp");
                        req.put("idDB", customerNoo.getId());
                        req.put("customerID", customerNoo.getId());
                        req.put("username", user.getUsername());
                        database.addPhoto(req);//npwp
                    }

                    if (customerNoo.getPhotoOutlet() != null) {
                        req = new HashMap();
                        req.put("photo", customerNoo.getPhotoOutlet());
                        req.put("photoName", "outlet_" + customerNoo.getId());
                        req.put("typePhoto", "outlet");
                        req.put("idDB", customerNoo.getId());
                        req.put("customerID", customerNoo.getId());
                        req.put("username", user.getUsername());
                        database.addPhoto(req);//outlet
                    }
                }
                return result;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("CustomerNoo", ex.getMessage());
                }
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progress.dismiss();
            if (result) {
                SessionManagerQubes.clearCustomerNooSession();
                Helper.removeItemParam(Constants.IMAGE_TYPE);
                onBackPressed();
            } else {
                setToast(getString(R.string.failedSaveData));
            }
        }
    }

    private void getLocationGPS() {
        getAddressWithPermission((result, location) -> {
            Utils.backgroundTask(progress,
//                    () -> Utils.getCurrentAddress(CreateNooActivity.this, location.getLatitude(), location.getLongitude()),
                    () -> Utils.getCurrentAddressFull(CreateNooActivity.this, location.getLatitude(), location.getLongitude()),
                    new CallbackOnResult<Address>() {
                        @Override
                        public void onFinish(Address result) {
                            if (location != null) {
                                if (customerNoo == null) {
                                    customerNoo = new Customer();
                                }
//                                mAdminArea = "Banten"
//                                mCountryCode = "ID"
//                                mCountryName = "Indonesia"
//                                mFeatureName = "WM6W+GV7"
//                                mLocale = {Locale@32644} "en_US"
//                                mLocality = "Kecamatan Kosambi"
//                                mPostalCode = "15213"
//                                mPremises = null
//                                mSubAdminArea = "Kabupaten Tangerang"
//                                mSubLocality = "Kosambi Timur"
//                                mSubThoroughfare = null
//                                mThoroughfare = "Jalan Perumahan Taman Dadap Indah"
//                                mAddressLines = {HashMap@32639}  size = 1
//                                {Integer@32655} 0 -> "WM6W+GV7, Jl. Perumahan Taman Dadap Indah, Kosambi Tim., Kec. Kosambi, Kabupaten Tangerang, Banten 15213, Indonesia"
                                customerNoo.setLongitude(location.getLongitude());
                                customerNoo.setLatitude(location.getLatitude());
                                customerNoo.setAddress(result.getAddressLine(0));
                                customerNoo.setKode_pos(result.getPostalCode());
                                edtAddress.setText(result.getAddressLine(0));

                                DaerahTingkat daerahTingkat = database.getAllDaerahTingkat(result.getPostalCode());
                                if (daerahTingkat != null) {
                                    customerNoo.setKode_pos(String.valueOf(daerahTingkat.getKode_pos()));
                                    customerNoo.setKelurahan(daerahTingkat.getNama_kelurahan());
                                    customerNoo.setIdKelurahan(daerahTingkat.getKode_kelurahan());
                                    customerNoo.setKecamatan(daerahTingkat.getNama_kecamatan());
                                    customerNoo.setIdKecamatan(daerahTingkat.getKode_kecamatan());
                                    customerNoo.setKota(daerahTingkat.getNama_kabupaten());
                                    customerNoo.setIdKota(daerahTingkat.getKode_kabupaten());
                                    customerNoo.setProvinsi(daerahTingkat.getNama_provinsi());
                                    customerNoo.setIdProvinsi(daerahTingkat.getKode_provinsi());

                                    txtKodePos.setText(String.valueOf(daerahTingkat.getKode_pos()));
                                    txtKelurahan.setText(daerahTingkat.getNama_kelurahan());
                                    txtKecamatan.setText(daerahTingkat.getNama_kecamatan());
                                    txtKotaKabupaten.setText(daerahTingkat.getNama_kabupaten());
                                    txtProvinsi.setText(daerahTingkat.getNama_provinsi());
                                }
                                customerNoo.setLatitude(location.getLatitude());
                                customerNoo.setLongitude(location.getLongitude());
                                txtGPSLocation.setText(String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
                            } else {
                                setToast("Lokasi tidak di temukan");
                            }
                        }

                        @Override
                        public void onFailed() {
                            setToast("Lokasi tidak di temukan");
                        }
                    });
        });
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
        mFusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(this, location -> {
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
                })
                .addOnFailureListener(this, e -> {
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_REQUEST);
    }
}
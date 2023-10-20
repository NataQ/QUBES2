package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import org.osmdroid.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerDaerahTingkatAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerTypePriceAdapter;
import id.co.qualitas.qubes.adapter.aspp.NothingSelectedSpinnerAdapter;
import id.co.qualitas.qubes.adapter.aspp.SpinnerProductStockRequestAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Attachment;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.CustomerType;
import id.co.qualitas.qubes.model.DaerahTingkat;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class CreateNooActivity extends BaseActivity implements LocationListener {
    private Button btnSave;
    private TextView txtKodePos, txtKelurahan, txtKecamatan, txtKotaKabupaten, txtProvinsi;
    private TextView txtTypeToko, txtPriceListType, txtCreditLimit, txtRoute, txtGPSLocation;
    private EditText edtPhone, edtNamaToko, edtAddress, edtNoNpwp, edtNamaNpwp;
    private EditText edtAlamatNpwp, edtNamaPemilik, edtNIK, edtLokasi, edtJenisUsaha, edtLamaUsaha;
    private Spinner spnSuku, spnStatusToko, spnStatusNpwp, spnUdf5;
    private RelativeLayout llKTP, llNPWP, llOutlet;
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
    private ArrayAdapter<String> sukuAdapter, statusNpwpAdapter, statusTokoAdapter, udf5Adapter;
    private LocationManager lm;
    private Location currentLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_create_noo);

        initialize();

        Configuration.getInstance().load(CreateNooActivity.this, PreferenceManager.getDefaultSharedPreferences(CreateNooActivity.this));
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(CreateNooActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateNooActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);


        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CreateNooActivity.this);
        });

        btnSave.setOnClickListener(v -> {
            if (validateData() == 0) {
                saveDataNoo();
                database.addNoo(customerNoo, user.getUsername());
                SessionManagerQubes.clearCustomerNooSession();
                Helper.removeItemParam(Constants.IMAGE_TYPE);
                onBackPressed();
            }
        });

        llKTP.setOnClickListener(view -> {
            typeImage = 1;
            imageType.setPosImage(typeImage);
            saveDataNoo();
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            openDialogPhoto();
        });

        llNPWP.setOnClickListener(view -> {
            typeImage = 2;
            imageType.setPosImage(typeImage);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            openDialogPhoto();
        });

        llOutlet.setOnClickListener(view -> {
            typeImage = 3;
            imageType.setPosImage(typeImage);
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
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);
            if (customerNoo == null) {
                customerNoo = new Customer();
                customerNoo.setLongitude(currentLocation.getLongitude());
                customerNoo.setLatitude(currentLocation.getLatitude());
                txtGPSLocation.setText(String.valueOf(currentLocation.getLatitude()) + "," + String.valueOf(currentLocation.getLongitude()));
            } else {
                setToast("Lokasi tidak di temukan");
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
        if (!Helper.isEmptyEditText(edtNoNpwp)) {
            customerNoo.setNo_npwp(edtNoNpwp.getText().toString().trim());
        }
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
        if (!Helper.isEmptyEditText(edtNIK)) {
            customerNoo.setNik(edtNIK.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtLokasi)) {
            customerNoo.setLocation(edtLokasi.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtJenisUsaha)) {
            customerNoo.setJenis_usaha(edtJenisUsaha.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(edtLamaUsaha)) {
            customerNoo.setLama_usaha(edtLamaUsaha.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(txtRoute)) {
            customerNoo.setRute(txtRoute.getText().toString().trim());
        }
        if (!Helper.isEmptyEditText(txtCreditLimit)) {
            customerNoo.setCredit_limit(Double.parseDouble(txtCreditLimit.getText().toString().trim().replace(".", "")));
        }

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
        if (Helper.isEmptyEditText(txtKodePos)) {
            error++;
            txtKodePos.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(txtKelurahan)) {
            error++;
            txtKelurahan.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(txtKecamatan)) {
            error++;
            txtKecamatan.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(txtKotaKabupaten)) {
            error++;
            txtKotaKabupaten.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(txtProvinsi)) {
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
        if (Helper.isEmptyEditText(edtNIK)) {
            error++;
            edtNIK.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(txtTypeToko)) {
            error++;
            txtTypeToko.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(txtPriceListType)) {
            error++;
            txtPriceListType.setError(getString(R.string.emptyField));
        }
        if (Helper.isEmptyEditText(txtGPSLocation)) {
            error++;
            txtGPSLocation.setError(getString(R.string.emptyField));
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
                    photo.setImageURI(imageType.getPhotoKTP());
                    photo.setVisibility(View.VISIBLE);
                    layoutUpload.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.GONE);
                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (imageType.getPhotoNPWP() != null) {
                    photo.setImageURI(imageType.getPhotoNPWP());
                    photo.setVisibility(View.VISIBLE);
                    layoutUpload.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.GONE);
                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                if (imageType.getPhotoOutlet() != null) {
                    photo.setImageURI(imageType.getPhotoOutlet());
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
        txtRoute.setText(Helper.getTodayRouteDouble());
        txtCreditLimit.setText(format.format(Double.parseDouble(database.getCreditLimit() != null ? database.getCreditLimit() : "0")));

        List<String> suku = new ArrayList<>();
        suku.add("Indonesia");
        suku.add("Tiong Hua");

        List<String> statusToko = new ArrayList<>();
        statusToko.add("--");
        statusToko.add("Milik Sendiri");
        statusToko.add("Sewa");

        List<String> statusNPWP = new ArrayList<>();
        statusNPWP.add("--");
        statusNPWP.add("PKP");
        statusNPWP.add("Non PKP");

        List<String> udf5 = new ArrayList<>();
        udf5.add("GT");
        udf5.add("OP");

        sukuAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                return view;
            }
        };
        sukuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sukuAdapter.addAll(suku);
        spnSuku.setAdapter(sukuAdapter);

        statusTokoAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                return view;
            }
        };
        statusTokoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusTokoAdapter.addAll(statusToko);
        spnStatusToko.setAdapter(statusTokoAdapter);

        statusNpwpAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                return view;
            }
        };
        statusNpwpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusNpwpAdapter.addAll(statusNPWP);
        spnStatusNpwp.setAdapter(statusNpwpAdapter);

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

//        setSpinnerData(suku, spnSuku);
//        setSpinnerData(statusToko, spnStatusToko);
//        setSpinnerData(statusNPWP, spnStatusNpwp);

        spnSuku.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    if (customerNoo == null) {
                        customerNoo = new Customer();
                    }
                    String text = adapterView.getItemAtPosition(i).toString();
                    customerNoo.setSuku(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnStatusToko.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    if (customerNoo == null) {
                        customerNoo = new Customer();
                    }
                    String text = adapterView.getItemAtPosition(i).toString();
                    customerNoo.setStatus_toko(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnStatusNpwp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    if (customerNoo == null) {
                        customerNoo = new Customer();
                    }
                    String text = adapterView.getItemAtPosition(i).toString();
                    customerNoo.setStatus_npwp(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnUdf5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    if (customerNoo == null) {
                        customerNoo = new Customer();
                    }
                    String text = adapterView.getItemAtPosition(i).toString();
                    customerNoo.setUdf_5_desc(text);
                    customerNoo.setUdf_5(text.equals("GT") ? "G" : "O");
                }
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
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText editText = dialog.findViewById(R.id.edit_text);
        RecyclerView rv = dialog.findViewById(R.id.list_view);
        setDataDaerahTingkat();

        List<DaerahTingkat> arrayList = new ArrayList<>();
        arrayList.addAll(database.getAllProvinsi(daerahTingkat));

        provinsiAdpater = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 5, arrayList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setProvinsi(header.getNama_provinsi());
            customerNoo.setIdProvinsi(header.getKode_provinsi());
            txtProvinsi.setText(header.getNama_provinsi());
            dialog.dismiss();
        });

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(provinsiAdpater);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                provinsiAdpater.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void openDialogKabupaten() {
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText editText = dialog.findViewById(R.id.edit_text);
        RecyclerView rv = dialog.findViewById(R.id.list_view);
        setDataDaerahTingkat();

        List<DaerahTingkat> arrayList = new ArrayList<>();
        arrayList.addAll(database.getAllKabupaten(daerahTingkat));

        kabupatenAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 4, arrayList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setKota(header.getNama_kabupaten());
            customerNoo.setIdKota(header.getKode_kabupaten());
            txtKotaKabupaten.setText(header.getNama_kabupaten());
            dialog.dismiss();
        });

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(kabupatenAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                kabupatenAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void openDialogKecamatan() {
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText editText = dialog.findViewById(R.id.edit_text);
        RecyclerView rv = dialog.findViewById(R.id.list_view);
        setDataDaerahTingkat();

        List<DaerahTingkat> arrayList = new ArrayList<>();
        arrayList.addAll(database.getAllKecamatan(daerahTingkat));

        kecamatanAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 3, arrayList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setKecamatan(header.getNama_kecamatan());
            customerNoo.setIdKecamatan(header.getKode_kecamatan());
            txtKecamatan.setText(header.getNama_kecamatan());
            dialog.dismiss();
        });

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(kecamatanAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                kecamatanAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText editText = dialog.findViewById(R.id.edit_text);
        RecyclerView rv = dialog.findViewById(R.id.list_view);
        setDataDaerahTingkat();

        List<DaerahTingkat> arrayList = new ArrayList<>();
        arrayList.addAll(database.getAllKodePos(daerahTingkat));

        kodePosAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 1, arrayList, (header, adapterPosition) -> {
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

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(kodePosAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                kodePosAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void openDialogKelurahan() {
        Dialog dialog = new Dialog(CreateNooActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText editText = dialog.findViewById(R.id.edit_text);
        RecyclerView rv = dialog.findViewById(R.id.list_view);

        setDataDaerahTingkat();
        List<DaerahTingkat> arrayList = new ArrayList<>();
        arrayList.addAll(database.getAllKelurahan(daerahTingkat));

        kelurahanAdapter = new FilteredSpinnerDaerahTingkatAdapter(CreateNooActivity.this, 2, arrayList, (header, adapterPosition) -> {
            if (customerNoo == null) {
                customerNoo = new Customer();
            }
            customerNoo.setKelurahan(header.getNama_kelurahan());
            customerNoo.setIdKelurahan(header.getKode_kelurahan());
            txtKelurahan.setText(header.getNama_kelurahan());
            dialog.dismiss();
        });

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(kelurahanAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                kelurahanAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtGPSLocation = findViewById(R.id.txtGPSLocation);
        spnUdf5 = findViewById(R.id.spnUdf5);
        txtKodePos = findViewById(R.id.txtKodePos);
        txtKelurahan = findViewById(R.id.txtKelurahan);
        txtKecamatan = findViewById(R.id.txtKecamatan);
        txtKotaKabupaten = findViewById(R.id.txtKotaKabupaten);
        txtProvinsi = findViewById(R.id.txtProvinsi);
        edtPhone = findViewById(R.id.edtPhone);
        edtNamaToko = findViewById(R.id.edtNamaToko);
        edtAddress = findViewById(R.id.edtAddress);
        edtNoNpwp = findViewById(R.id.edtNoNpwp);
        edtNamaNpwp = findViewById(R.id.edtNamaNpwp);
        edtNamaPemilik = findViewById(R.id.edtNamaPemilik);
        edtAlamatNpwp = findViewById(R.id.edtAlamatNpwp);
        edtNIK = findViewById(R.id.edtNIK);
        edtLokasi = findViewById(R.id.edtLokasi);
        edtJenisUsaha = findViewById(R.id.edtJenisUsaha);
        edtLamaUsaha = findViewById(R.id.edtLamaUsaha);
        txtTypeToko = findViewById(R.id.txtTypeToko);
        txtCreditLimit = findViewById(R.id.txtCreditLimit);
        txtRoute = findViewById(R.id.txtRoute);
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
                    imageType.setPhotoKTP(uri);
                    break;
                case 2:
                    imageType.setPhotoNPWP(uri);
                    break;
                case 3:
                    imageType.setPhotoOutlet(uri);
                    break;
            }
        }
        if (imageType.getPhotoKTP() != null) {
            customerNoo.setPhotoKtp(Utils.compressImageUri(getApplicationContext(), imageType.getPhotoKTP().toString()));
            imgKTP.setImageURI(imageType.getPhotoKTP());
            imgDeleteKTP.setVisibility(View.VISIBLE);
        }
        if (imageType.getPhotoNPWP() != null) {
            customerNoo.setPhotoNpwp(Utils.compressImageUri(getApplicationContext(), imageType.getPhotoNPWP().toString()));
            imgNPWP.setImageURI(imageType.getPhotoNPWP());
            imgDeleteNPWP.setVisibility(View.VISIBLE);
        }
        if (imageType.getPhotoOutlet() != null) {
            customerNoo.setPhotoOutlet(Utils.compressImageUri(getApplicationContext(), imageType.getPhotoOutlet().toString()));
            imgOutlet.setImageURI(imageType.getPhotoOutlet());
            imgDeleteOutlet.setVisibility(View.VISIBLE);
        }

        Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//        SessionManagerQubes.setImageType(imageType);
        SessionManagerQubes.setCustomerNoo(customerNoo);

//        Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoKTP(), imgKTP);
//        Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoNPWP(), imgNPWP);
//        Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoOutlet(), imgOutlet);
    }

    private void setDataToView() {
        txtKodePos.setText(Helper.isEmpty(customerNoo.getKode_pos(), ""));
        txtKelurahan.setText(Helper.isEmpty(customerNoo.getKelurahan(), ""));
        txtKecamatan.setText(Helper.isEmpty(customerNoo.getKecamatan(), ""));
        txtKotaKabupaten.setText(Helper.isEmpty(customerNoo.getKota(), ""));
        txtProvinsi.setText(Helper.isEmpty(customerNoo.getProvinsi(), ""));
        edtPhone.setText(Helper.isEmpty(customerNoo.getNo_tlp(), ""));
        edtNamaToko.setText(Helper.isEmpty(customerNoo.getNama(), ""));
        edtAddress.setText(Helper.isEmpty(customerNoo.getAddress(), ""));
        edtNoNpwp.setText(Helper.isEmpty(customerNoo.getNo_npwp(), ""));
        edtNamaNpwp.setText(Helper.isEmpty(customerNoo.getNpwp_name(), ""));
        edtNamaPemilik.setText(Helper.isEmpty(customerNoo.getNama_pemilik(), ""));
        edtAlamatNpwp.setText(Helper.isEmpty(customerNoo.getNpwp_address(), ""));
        edtNIK.setText(Helper.isEmpty(customerNoo.getNik(), ""));
        edtLokasi.setText(Helper.isEmpty(customerNoo.getLocation(), ""));
        edtJenisUsaha.setText(Helper.isEmpty(customerNoo.getJenis_usaha(), ""));
        edtLamaUsaha.setText(Helper.isEmpty(customerNoo.getLama_usaha(), ""));
        txtTypeToko.setText(Helper.isEmpty(customerNoo.getType_customer(), ""));
        txtPriceListType.setText(Helper.isEmpty(customerNoo.getType_price(), ""));
        txtCreditLimit.setText(customerNoo.getLimit_kredit() != 0 ? format.format(customerNoo.getLimit_kredit()) : format.format(Double.parseDouble(database.getCreditLimit() != null ? database.getCreditLimit() : "0")));
        txtRoute.setText(Helper.isEmpty(customerNoo.getRute(), Helper.getTodayRouteDouble()));

        int spinnerPositionSuku = sukuAdapter.getPosition(customerNoo.getSuku());
        int spinnerPositionStatusToko = statusTokoAdapter.getPosition(customerNoo.getStatus_toko());
        int spinnerPositionStatusNpwp = statusNpwpAdapter.getPosition(customerNoo.getStatus_npwp());
        int spinnerPositionUdf5 = statusNpwpAdapter.getPosition(customerNoo.getUdf_5_desc());

        spnSuku.setSelection(spinnerPositionSuku);
        spnStatusToko.setSelection(spinnerPositionStatusToko);
        spnStatusNpwp.setSelection(spinnerPositionStatusNpwp);
        spnUdf5.setSelection(spinnerPositionUdf5);
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
            Helper.takePhoto(CreateNooActivity.this);
        }
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
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
        Uri selectedImage = data.getData();
//        String path = getImageFilePath(selectedImage);
//        File pathImg = new File(path);
//        byte[] imgByte = getByteArrayFromUriGallery(Uri.fromFile(pathImg));

        switch (typeImage) {
            case 1:
                imageType.setPhotoKTP(selectedImage);
                imgKTP.setImageURI(selectedImage);
//                Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoKTP(), imgKTP);
                imgKTP.setVisibility(View.VISIBLE);
                imgDeleteKTP.setVisibility(View.VISIBLE);
                imgAddKTP.setVisibility(View.GONE);
                break;
            case 2:
                imageType.setPhotoNPWP(selectedImage);
                imgNPWP.setImageURI(selectedImage);
//                Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoNPWP(), imgNPWP);
                imgNPWP.setVisibility(View.VISIBLE);
                imgDeleteNPWP.setVisibility(View.VISIBLE);
                imgAddNPWP.setVisibility(View.GONE);
                break;
            case 3:
                imageType.setPhotoOutlet(selectedImage);
                imgOutlet.setImageURI(selectedImage);
//                Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoOutlet(), imgOutlet);
                imgOutlet.setVisibility(View.VISIBLE);
                imgDeleteOutlet.setVisibility(View.VISIBLE);
                imgAddOutlet.setVisibility(View.GONE);
                break;
        }

        Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//        SessionManagerQubes.setImageType(imageType);
    }

    public String getImageFilePath(Uri uri) {
        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            @SuppressLint("Range") String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            cursor.close();
            return imagePath;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(this, VisitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        if (customerNoo == null) {
            customerNoo = new Customer();
            customerNoo.setLongitude(currentLocation.getLongitude());
            customerNoo.setLatitude(currentLocation.getLatitude());
            txtGPSLocation.setText(String.valueOf(currentLocation.getLatitude()) + "," + String.valueOf(currentLocation.getLongitude()));
        }
    }
}
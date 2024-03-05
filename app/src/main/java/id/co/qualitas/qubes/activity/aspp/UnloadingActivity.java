package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.UnloadingAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.aspp.ConfirmationDialogFragment;
import id.co.qualitas.qubes.helper.AddressResultReceiver;
import id.co.qualitas.qubes.helper.FetchAddressIntentService;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.interfaces.CallbackOnResult;
import id.co.qualitas.qubes.interfaces.LocationRequestCallback;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.OfflineData;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.StartVisit;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.services.NotiWorker;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.LashPdfUtils;
import id.co.qualitas.qubes.utils.UnloadingPdfUtils;
import id.co.qualitas.qubes.utils.Utils;

public class UnloadingActivity extends BaseActivity {
    private UnloadingAdapter mAdapter;
    private List<Material> mList;
    private Button btnSubmit;
    private UnloadingPdfUtils pdfUnloadingUtils;
    private LashPdfUtils pdfUtils;
    private File pdfFile;
    private Boolean success = false;
    private TextView txtDate, txtNoDoc, txtTglKirim, txtNoSuratJalan;
    private StockRequest header;
    private boolean isLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private ImageType imageType;
    private Uri uriPulang, uriSelesai;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Map currentLocation;
    private LocationRequestCallback<String, Location> addressCallback;
    private WSMessage logResult;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final int CAMERA_PERM_CODE = 102;
    private String kmAkhir;
    private StartVisit startVisit;
    private boolean setDataSyncSuccess = false;
    private ProgressDialog progressDialog;
    private List<Customer> nooList = new ArrayList<>();
    private List<VisitSalesman> visitSalesmanList = new ArrayList<>();
    private List<Map> storeCheckList = new ArrayList<>(), returnList = new ArrayList<>();
    private List<CollectionHeader> collectionList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();
    private List<Map> photoList = new ArrayList<>();
    private List<OfflineData> offlineData = new ArrayList<>();
    private int sizeData = 0;
    private final static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Map endDay;
    private int fromUnloading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_unloading);
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
        initialize();

        btnSubmit.setOnClickListener(v -> {
            openDialogUnloading();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(UnloadingActivity.this);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        swipeLayout.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataOffline();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    public void openDialogUnloading() {
        LayoutInflater inflater = LayoutInflater.from(UnloadingActivity.this);
        final Dialog dialog = new Dialog(UnloadingActivity.this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDialog = dialog.findViewById(R.id.txtDialog);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        txtTitle.setText("Unloading");
        txtDialog.setText("Are you sure want to unloading?");

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
                if (checkPermission()) {
                    progress.show();
                    PARAM = 1;
                    new RequestUrl().execute();//1
                } else {
                    setToast(getString(R.string.pleaseEnablePermission));
                    requestPermission();
                }

//                if (fromUnloading == 1) {
//                progress.show();
//                PARAM = 1;
//                new RequestUrl().execute();//1
//                } else {
//                    if (checkPermission()) {
//                        checkLocationPermission();//end visit
//                    } else {
//                        setToast(getString(R.string.pleaseEnablePermission));
//                        requestPermission();
//                    }
//                }
            }
        });

        dialog.show();
    }

    private class AsyncTaskGeneratePDF extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                if (PARAM == 4) {
                    String nameLash = user.getUsername() + Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_TYPE_7, startVisit.getDate());
                    pdfFile = new File(Utils.getDirLocPDF(getApplicationContext(), 2) + "/" + nameLash + ".pdf");
                    List<Map> lashList = new ArrayList<>();
                    lashList = database.getDatalash();
                    success = pdfUtils.createPDF(pdfFile, lashList, nameLash);
                    return success;
                } else {
                    header = database.getStockRequestHeader(header.getIdHeader());
                    header.setId_salesman(user.getUsername());
                    String pdfName = header.getId_salesman() + "_" + Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_4, header.getReq_date());
                    pdfFile = new File(Utils.getDirLocPDF(getApplicationContext(), 1) + "/unloading_" + pdfName + ".pdf");
                    success = pdfUnloadingUtils.createPDF(pdfFile, header, mList);
                    return success;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (PARAM == 4) {
                if (result == null) {
                    setToast("Gagal membuat pdf LASH.. ");
                } else {
                    if (result) {
                        setToast("Downloaded to " + pdfFile.getAbsolutePath());
                    } else {
                        setToast("Gagal membuat pdf LASH..");
                    }
                }

                progress.show();
                PARAM = 5;
                new AsyncTaskGeneratePDF().execute();//5
            } else {
                if (result == null) {
                    setToast("Gagal membuat pdf unloading.. ");
                } else {
                    if (result) {
                        setToast("Downloaded to " + pdfFile.getAbsolutePath());
                    } else {
                        setToast("Gagal membuat pdf unloading.. ");
                    }
                }

//                if (fromUnloading == 0) {
//                    progress.show();
//                    PARAM = 3;
//                    new RequestUrl().execute();//3 get data offline
//                }
            }
        }
    }

    private void initData() {
        header = SessionManagerQubes.getStockRequestHeader();
        startVisit = database.getLastStartVisit();
//        startVisit = SessionManagerQubes.getStartDay();
        fromUnloading = Helper.getItemParam(Constants.FROM_STOCK_REQUEST) != null ? (int) Helper.getItemParam(Constants.FROM_STOCK_REQUEST) : 1;
        if (header == null) {
            onBackPressed();
            setToast(getString(R.string.failedGetData));
        } else {
            txtNoSuratJalan.setText(Helper.isEmpty(header.getNo_surat_jalan(), "-"));
            txtNoDoc.setText(Helper.isEmpty(header.getNo_doc(), "-"));

            if (!Helper.isNullOrEmpty(header.getReq_date())) {
                String requestDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, header.getReq_date());
                txtDate.setText(requestDate);
            } else {
                txtDate.setText("-");
            }

            if (!Helper.isNullOrEmpty(header.getTanggal_kirim())) {
                String tglKirim = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, header.getTanggal_kirim());
                txtTglKirim.setText(tglKirim);
            } else {
                txtTglKirim.setText("-");
            }
            getDataOffline();
        }
    }

    private void initialize() {
        pdfUnloadingUtils = UnloadingPdfUtils.getInstance(UnloadingActivity.this);
        pdfUtils = LashPdfUtils.getInstance(UnloadingActivity.this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = findViewById(R.id.llNoData);
        txtNoSuratJalan = findViewById(R.id.txtNoSuratJalan);
        txtTglKirim = findViewById(R.id.txtTglKirim);
        txtNoDoc = findViewById(R.id.txtNoDoc);
        txtDate = findViewById(R.id.txtDate);
        swipeLayout = findViewById(R.id.swipeLayout);
        progressCircle = findViewById(R.id.progressCircle);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSubmit = findViewById(R.id.btnSubmit);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();

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
                case 5:
                    uriPulang = uri;
                    imageType.setPhotoAkhirString(Utils.encodeImageBase64(UnloadingActivity.this, uriPulang));
                    imageType.setPhotoAkhir(uriPulang.getPath());
                    openDialogEndVisit();//on resume
                    break;
                case 6:
                    uriSelesai = uri;
                    imageType.setPhotoSelesaiString(Utils.encodeImageBase64(UnloadingActivity.this, uriSelesai));
                    imageType.setPhotoSelesai(uriSelesai.getPath());
                    openDialogEndVisit();//on resume
                    break;
            }
        }
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
//                    checkLocationPermission();//end visit
                    progress.show();
                    PARAM = 1;
                    new RequestUrl().execute();//1
                } else {
                    setToast(getString(R.string.pleaseEnablePermission));
                }
                break;
            case Constants.LOCATION_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (Utils.isGPSOn(this)) {
                        getLocationGPS();//from onRequestPermissionsResult
                    } else {
                        Utils.turnOnGPS(this);
                    }
                } else {
                    setToast(getString(R.string.pleaseEnablePermission));
                }
                break;
            case CAMERA_PERM_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Helper.takePhoto(UnloadingActivity.this);
                } else {
                    setToast(getString(R.string.pleaseEnablePermission));
                }
                break;
        }
    }

    private void setAdapter() {
        mAdapter = new UnloadingAdapter(this, mList, header -> {

        });
        recyclerView.setAdapter(mAdapter);
    }

    private void getDataOffline() {
        mList = new ArrayList<>();
        mList = database.getAllStockRequestDetailUnloading(header);

        if (mList == null || mList.isEmpty()) {
            llNoData.setVisibility(View.VISIBLE);
        } else {
            llNoData.setVisibility(View.GONE);
            setAdapter();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), StockRequestListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_ = Constants.API_STOCK_REQUEST_UNLOADING;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    header.setMaterialList(mList);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, header);
                    return null;
                } else if (PARAM == 2) {
                    endDay = new HashMap();
                    endDay.put("kmAkhir", kmAkhir);
                    endDay.put("username", user.getUsername());

                    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                    if (imageType.getPhotoAkhir() != null) {
                        File file = FileUtils.getFile(String.valueOf(this), imageType.getPhotoAkhir());
                        map.add("photoKmAkhir", new FileSystemResource(imageType.getPhotoAkhir()));
                    } else {
                        map.add("photoKmAkhir", "");
                    }
                    if (imageType.getPhotoSelesai() != null) {
                        map.add("photoCompleted", new FileSystemResource(imageType.getPhotoSelesai()));
                    } else {
                        map.add("photoCompleted", "");
                    }
                    String json = new Gson().toJson(endDay);
                    map.add("data", json);

                    String URL_ = Constants.API_GET_END_DAY_MULTI_PART;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBodyMultiPart(url, WSMessage.class, map);
                    return null;
                } else {
                    setDataOffline();
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("unloading", ex.getMessage());
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
            progress.dismiss();
            if (PARAM == 1) {
                if (logResult != null) {
                    if (logResult.getIdMessage() == 1) {
                        String message = "Unloading Stock Request : " + logResult.getMessage();
                        logResult.setMessage(message);
                        header.setIs_unloading(1);
                        database.updateUnloading(header, user.getUsername());
                        setToast("Unloading sukses");
                        btnSubmit.setVisibility(View.GONE);

//                        if (fromUnloading == 1) {
                            PARAM = 5;
                            new AsyncTaskGeneratePDF().execute();//5
//                        } else {
//                            progress.show();
//                            PARAM = 3;
//                            new RequestUrl().execute();//3
//                        }
                    } else {
                        setToast(logResult.getMessage());
                    }
                    database.addLog(logResult);
                }
            } else if (PARAM == 2) {
                if (logResult.getIdMessage() == 1) {
                    String message = "End Visit : " + logResult.getMessage();
                    logResult.setMessage(message);
                    database.addLog(logResult);
                    startVisit.setEnd_time(Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    startVisit.setKm_akhir(kmAkhir);
                    startVisit.setPhoto_km_akhir(imageType.getPhotoAkhir());
                    startVisit.setPhoto_complete(imageType.getPhotoSelesai());
                    database.updateEndVisit(startVisit);
//                    SessionManagerQubes.setStartDay(startVisit);
                    if (workManager != null) {
                        workManager.cancelAllWork();
                    }
                    progress.show();
                    PARAM = 1;//unloading dulu
                    new RequestUrl().execute();//1
                } else {
                    database.addLog(logResult);
                    setToast(logResult.getMessage());

                    openDialogEndVisit();//if failed end visit
                }
            } else if (PARAM == 3) {
                new RequestUrlSync().execute();
            }
        }
    }

    //end visit
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
//        yg ud d foto bisa ilang kalau foto lagi, d check lagi

        if (imageType != null) {
            if (imageType.getPhotoSelesai() != null) {
                Utils.loadImageFit(UnloadingActivity.this, imageType.getPhotoSelesai(), imgSelesai);
//            imgSelesai.setImageURI(uriSelesai);
                imgAddSelesai.setVisibility(View.GONE);
            } else {
                imgAddSelesai.setVisibility(View.VISIBLE);
            }
        } else {
            imgAddSelesai.setVisibility(View.VISIBLE);
        }

        if (imageType != null) {
            if (imageType.getPhotoAkhir() != null) {
                Utils.loadImageFit(UnloadingActivity.this, imageType.getPhotoAkhir(), imgPulang);
                imgAddPulang.setVisibility(View.GONE);
            } else {
                imgAddPulang.setVisibility(View.VISIBLE);
            }
        } else {
            imgAddSelesai.setVisibility(View.VISIBLE);
        }

        txtKmAkhir.setText(imageType.getKmAkhir());

        llImgPulang.setOnClickListener(v -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setKmAkhir(txtKmAkhir.getText().toString().trim());
            imageType.setPhotoAkhir(uriPulang != null ? uriPulang.getPath() : imageType.getPhotoAkhir());
            imageType.setPhotoSelesai(uriSelesai != null ? uriSelesai.getPath() : imageType.getPhotoSelesai());
            imageType.setPosImage(14);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            askPermissionCamera();
        });

        llImgSelesai.setOnClickListener(v -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPosImage(15);
            imageType.setKmAkhir(txtKmAkhir.getText().toString().trim());
            imageType.setPhotoAkhir(uriPulang != null ? uriPulang.getPath() : imageType.getPhotoAkhir());
            imageType.setPhotoSelesai(uriSelesai != null ? uriSelesai.getPath() : imageType.getPhotoSelesai());
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            askPermissionCamera();
        });

        btnEnd.setOnClickListener(v -> {
            if (imageType == null) {
                imageType = new ImageType();
            }

            if (Helper.isEmptyEditText(txtKmAkhir)) {
                txtKmAkhir.setError(getString(R.string.emptyField));
            } else if (imageType.getPhotoAkhir() == null) {
                setToast("Harus Foto KM Akhir");
            } else if (imageType.getPhotoSelesai() == null) {
                setToast("Harus Foto Selesai");
            } else {
                dialog.dismiss();
                kmAkhir = txtKmAkhir.getText().toString().trim();
                PARAM = 2;
                new RequestUrl().execute();//2
                progress.show();
            }
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void checkLocationPermission() {
        List<String> permissionRequest = new ArrayList<>();
        isLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!isLocationPermissionGranted)
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (isLocationPermissionGranted) {
            if (!Helper.isGPSOn(UnloadingActivity.this)) {
                setToast("Please turn on GPS");
                Helper.turnOnGPS(UnloadingActivity.this);
            } else {
                getLocationGPS();//from checkLocationPermission
            }
        }
    }

    private void getLocationGPS() {
        getAddressWithPermission((result, location) -> {
            Utils.backgroundTask(progress, () -> Utils.getCurrentAddressFull(UnloadingActivity.this, location.getLatitude(), location.getLongitude()),
                    new CallbackOnResult<Address>() {
                        @Override
                        public void onFinish(Address result) {
                            if (location != null) {
                                currentLocation = new HashMap();
                                currentLocation.put("latitude", location.getLatitude());
                                currentLocation.put("longitude", location.getLongitude());
                                currentLocation.put("address", result.getAddressLine(0));
                                openDialogEndVisit();//get location
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
                Helper.takePhoto(UnloadingActivity.this);
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
                Helper.takePhoto(UnloadingActivity.this);
            }
        }
    }

    private void setDataOffline() {
        offlineData = new ArrayList<>();

        //noo
        nooList = new ArrayList<>();
        nooList = database.getAllNoo();
        if (nooList == null) {
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set offline data noo failed: " + exMess);
        } else {
            nooList = new ArrayList<>();
        }
        OfflineData offData = new OfflineData();
        offData.setNooList(nooList);
        offlineData.add(0, offData);

        //visit
        visitSalesmanList = new ArrayList<>();
        visitSalesmanList = database.getAllVisitSalesman();
        if (visitSalesmanList == null) {
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set offline data visit failed: " + exMess);
        } else {
            visitSalesmanList = new ArrayList<>();
        }
        offData = new OfflineData();
        offData.setVisitSalesmanList(visitSalesmanList);
        offlineData.add(1, offData);

        List<Customer> customerList = database.getAllCustomerCheckOut();

        if (customerList != null) {
            if (!customerList.isEmpty()) {
//                Map headerStoreCheck = new HashMap();
                storeCheckList = new ArrayList<>();
//                List<Material> storeCheck = new ArrayList<>();
                collectionList = new ArrayList<>();
                List<CollectionHeader> collection = new ArrayList<>();
                orderList = new ArrayList<>();
                List<Order> order = new ArrayList<>();
//                Map headerReturn = new HashMap();
                returnList = new ArrayList<>();
//                List<Material> returnO = new ArrayList<>();
                photoList = new ArrayList<>();
                List<Map> photos = new ArrayList<>();

                for (Customer customer : customerList) {
                    storeCheckList.addAll(database.getAllStoreCheckDate(customer.getId(), user.getUsername()));
                    if (storeCheckList != null) {
                        setDataSyncSuccess = true;
                    } else {
                        setDataSyncSuccess = false;
                    }
//                    storeCheck = database.getAllStoreCheckCheckOut(customer.getId());
//                    if (storeCheck != null) {
//                        if (!storeCheck.isEmpty()) {
//                            headerStoreCheck = new HashMap();
//                            headerStoreCheck.put("id_mobile", storeCheck.get(0).getIdheader());
//                            headerStoreCheck.put("date", storeCheck.get(0).getDate());
//                            headerStoreCheck.put("id_salesman", user.getUsername());
//                            headerStoreCheck.put("id_customer", storeCheck.get(0).getId_customer());
//                            headerStoreCheck.put("listData", storeCheck);
//                            storeCheckList.add(headerStoreCheck);
//                            setDataSyncSuccess = true;
//                        } else {
//                            setDataSyncSuccess = true;
//                        }
//                    } else {
//                        setDataSyncSuccess = false;
//                    }

                    //collection
                    collection = database.getAllCollectionHeader(customer.getId());
                    if (collection != null) {
                        if (!collection.isEmpty()) {
                            collectionList.addAll(collection);
                            setDataSyncSuccess = true;
                        } else {
                            setDataSyncSuccess = true;
                        }
                    } else {
                        setDataSyncSuccess = false;
                    }

                    //order
                    order = database.getAllOrderHeader(customer.getId(), user.getUsername());
                    if (order != null) {
                        if (!order.isEmpty()) {
                            orderList.addAll(order);
                            setDataSyncSuccess = true;
                        } else {
                            setDataSyncSuccess = true;
                        }
                    } else {
                        setDataSyncSuccess = false;
                    }

                    //return
                    returnList.addAll(database.getAllReturnDate(customer.getId(), user.getUsername()));
                    if (returnList != null) {
                        setDataSyncSuccess = true;
                    } else {
                        setDataSyncSuccess = false;
                    }

//                    returnO = database.getAllReturnCheckOut(customer.getId());
//                    if (returnO != null) {
//                        if (!returnO.isEmpty()) {
//                            headerReturn = new HashMap();
//                            headerReturn.put("id_mobile", returnO.get(0).getIdheader());
//                            headerReturn.put("date", returnO.get(0).getDate());
//                            headerReturn.put("id_salesman", user.getUsername());
//                            headerReturn.put("id_customer", returnO.get(0).getId_customer());
//                            headerReturn.put("listData", returnO);
//                            returnList.add(headerReturn);
//                            setDataSyncSuccess = true;
//                        } else {
//                            setDataSyncSuccess = true;
//                        }
//                    } else {
//                        setDataSyncSuccess = false;
//                    }

                    //photo
                    photos = database.getAllPhoto(customer.getId());
                    if (photos != null) {
                        if (!photos.isEmpty()) {
                            photoList.addAll(photos);
                            setDataSyncSuccess = true;
                        } else {
                            setDataSyncSuccess = true;
                        }
                    } else {
                        setDataSyncSuccess = false;
                    }
                }

                if (Helper.isNotEmptyOrNull(storeCheckList)) {
                    storeCheckList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(collectionList)) {
                    collectionList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(orderList)) {
                    orderList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(returnList)) {
                    returnList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(photoList)) {
                    photoList = new ArrayList<>();
                }
                offData = new OfflineData();
                offData.setStoreCheckList(storeCheckList);
                offlineData.add(2, offData);

                offData = new OfflineData();
                offData.setCollectionList(collectionList);
                offlineData.add(3, offData);

                offData = new OfflineData();
                offData.setOrderList(orderList);
                offlineData.add(4, offData);

                offData = new OfflineData();
                offData.setReturnList(returnList);
                offlineData.add(5, offData);

                offData = new OfflineData();
                offData.setPhotoList(photoList);
                offlineData.add(6, offData);

                if (!setDataSyncSuccess) {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                    logResult.setMessage("Set offline customer failed: " + exMess);
                }
            } else {
                setDataSyncSuccess = true;
                if (Helper.isNotEmptyOrNull(storeCheckList)) {
                    storeCheckList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(collectionList)) {
                    collectionList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(orderList)) {
                    orderList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(returnList)) {
                    returnList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(photoList)) {
                    photoList = new ArrayList<>();
                }

                offData = new OfflineData();
                offData.setStoreCheckList(storeCheckList);
                offlineData.add(2, offData);

                offData = new OfflineData();
                offData.setCollectionList(collectionList);
                offlineData.add(3, offData);

                offData = new OfflineData();
                offData.setOrderList(orderList);
                offlineData.add(4, offData);

                offData = new OfflineData();
                offData.setReturnList(returnList);
                offlineData.add(5, offData);

                offData = new OfflineData();
                offData.setPhotoList(photoList);
                offlineData.add(6, offData);
            }
        } else {
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set offline customer failed: " + exMess);

            if (Helper.isNotEmptyOrNull(storeCheckList)) {
                storeCheckList = new ArrayList<>();
            }
            if (Helper.isNotEmptyOrNull(collectionList)) {
                collectionList = new ArrayList<>();
            }
            if (Helper.isNotEmptyOrNull(orderList)) {
                orderList = new ArrayList<>();
            }
            if (Helper.isNotEmptyOrNull(returnList)) {
                returnList = new ArrayList<>();
            }
            if (Helper.isNotEmptyOrNull(photoList)) {
                photoList = new ArrayList<>();
            }

            offData = new OfflineData();
            offData.setStoreCheckList(storeCheckList);
            offlineData.add(2, offData);

            offData = new OfflineData();
            offData.setCollectionList(collectionList);
            offlineData.add(3, offData);

            offData = new OfflineData();
            offData.setOrderList(orderList);
            offlineData.add(4, offData);

            offData = new OfflineData();
            offData.setReturnList(returnList);
            offlineData.add(5, offData);

            offData = new OfflineData();
            offData.setPhotoList(photoList);
            offlineData.add(6, offData);
        }
        setToast("OFFLINEDATA");
    }

    private class RequestUrlSync extends AsyncTask<Void, Integer, List<WSMessage>> {

        @Override
        protected List<WSMessage> doInBackground(Void... voids) {
            try {
                List<WSMessage> listWSMsg = new ArrayList<>();
                String URL_ = null, url = null;
                Map req = new HashMap();
                int counter = 0;
                for (int i = 0; i < offlineData.size(); i++) {
                    switch (i) {
                        case 0:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_CUSTOMER_NOO);
                            req = new HashMap();
                            req.put("listData", offlineData.get(i).getNooList());
                            if (progressDialog != null) {
                                progressDialog.setMessage("Mengirim data noo offline...");
                            }
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Noo success");
                                for (Customer data : offlineData.get(i).getNooList()) {
                                    database.updateSyncNoo(data);
                                }
                            }
                            database.addLog(logResult);
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 1:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_VISIT);
                            counter++;
                            req = new HashMap();
                            req.put("listData", offlineData.get(i).getVisitSalesmanList());
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Visit Salesman success");
                                for (VisitSalesman data : offlineData.get(i).getVisitSalesmanList()) {
                                    database.updateSyncVisitSalesman(data);
                                }
                            }
                            database.addLog(logResult);
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 2:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_STORE_CHECK);
                            counter++;
                            for (Map data : offlineData.get(i).getStoreCheckList()) {
                                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                                if (logResult.getIdMessage() == 1) {
                                    logResult = new WSMessage();
                                    logResult.setIdMessage(1);
                                    logResult.setMessage("Sync Store Check " + data.get("id_mobile").toString() + " success");
                                    database.updateSyncStoreCheck(data);
                                }
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 3:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_COLLECTION);
                            counter++;
                            for (CollectionHeader data : offlineData.get(i).getCollectionList()) {
                                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                                if (logResult.getIdMessage() == 1) {
                                    logResult = new WSMessage();
                                    logResult.setIdMessage(1);
                                    logResult.setMessage("Sync Collection " + data.getIdHeader() + " success");
                                    database.updateSyncCollectionHeader(data);
                                }
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 4:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_ORDER);
                            counter++;
                            for (Order data : offlineData.get(i).getOrderList()) {
                                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                                if (logResult.getIdMessage() == 1) {
                                    logResult = new WSMessage();
                                    logResult.setIdMessage(1);
                                    logResult.setMessage("Sync Order " + data.getIdHeader() + " success");
                                    database.updateSyncOrderHeader(data);
                                }
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 5:
                            counter++;
//                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_RETURN);
//                            for (Map data : offlineData.get(i).getReturnList()) {
//                                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
//                                if (logResult.getIdMessage() == 1) {
//                                    logResult = new WSMessage();
//                                    logResult.setIdMessage(1);
//                                    logResult.setMessage("Sync Return " + data.get("id_customer").toString() + " success");
//                                    database.updateSyncReturn(data);
//                                }
//                                database.addLog(logResult);
//                            }
//                            listWSMsg.add(logResult);
                            publishProgress(counter);
                            break;
                        case 6:
                            counter++;
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_ONE_PHOTO);
                            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                            Map requestData = new HashMap();
                            String json;
                            for (Map data : offlineData.get(i).getPhotoList()) {
                                map = new LinkedMultiValueMap<String, Object>();
                                if (data.get("photo") != null) {
                                    map.add("photo", new FileSystemResource(data.get("photo").toString()));
                                } else {
                                    map.add("photo", "");
                                }
                                requestData = new HashMap();
                                requestData.put("typePhoto", data.get("typePhoto"));
                                requestData.put("id", user.getUsername());
                                requestData.put("customerId", data.get("customerId"));
                                requestData.put("idDB", data.get("idDB"));
                                json = new Gson().toJson(requestData);
                                map.add("data", json);

                                logResult = (WSMessage) NetworkHelper.postWebserviceWithBodyMultiPart(url, WSMessage.class, map);
                                if (logResult.getIdMessage() == 1) {
                                    logResult = new WSMessage();
                                    logResult.setIdMessage(1);
                                    logResult.setMessage("Sync Photo " + data.get("customerId").toString() + " success");
                                    database.updateSyncPhoto(data);
                                }
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                    }
                }
                return listWSMsg;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("Sync  offline", ex.getMessage());
                }
                logResult = new WSMessage();
                logResult.setIdMessage(0);
                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                logResult.setMessage("Sync offline data failed : " + exMess);
                database.addLog(logResult);
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UnloadingActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(offlineData.size());
            progressDialog.setMessage(getString(R.string.progress_checkout));
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(List<WSMessage> listResult) {
            if (listResult != null) {
                int error = 0;
                for (WSMessage msg : listResult) {
                    if (msg.getIdMessage() == 0) {
                        error++;
                    }
                }
                if (listResult.size() == offlineData.size()) {//ganti sizeData
                    if (error == 0) {
                        setToast("Sukses mengirim data " + String.valueOf(listResult.size()));
                    } else {
                        setToast("Gagal mengirim data : " + String.valueOf(error));
                    }
                } else {
                    setToast("Gagal mengirim data : " + String.valueOf(error));
                }
            } else {
                setToast("Gagal mengirim data");
            }
            progressDialog.dismiss();

//            new AsyncTaskGeneratePDF().execute();
        }
    }
}
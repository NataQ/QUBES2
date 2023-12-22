package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.StockRequestDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class StockRequestDetailActivity extends BaseActivity {
    private StockRequestDetailAdapter mAdapter;
    private WSMessage logResult;
    private List<Material> mList;
    private TextView txtDate, txtNoDoc, txtTglKirim, txtNoSuratJalan;
    private Button btnVerification, btnUnloading;
    private StockRequest header;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private final static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private boolean isSigned;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_stock_request_detail);

        initialize();

        btnVerification.setOnClickListener(v -> {
            if (checkPermission()) {
                openDialogSignature();
            } else {
                setToast(getString(R.string.pleaseEnablePermission));
                requestPermission();
            }
        });

        btnUnloading.setOnClickListener(v -> {
            Helper.setItemParam(Constants.FROM_STOCK_REQUEST, 1);
            Intent intent = new Intent(getApplicationContext(), UnloadingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(StockRequestDetailActivity.this);
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
                setAdapter();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void openDialogSignature() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_signature);
        Button btnClear, btnSubmit;
        btnClear = dialog.findViewById(R.id.btnClear);
        btnSubmit = dialog.findViewById(R.id.btnSubmit);
        final SignaturePad signaturePad = dialog.findViewById(R.id.signature_pad);

        Resources resources = getResources();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final boolean[] cleared = {false};

        btnClear.setOnClickListener(v -> {
            signaturePad.clear();
            cleared[0] = true;
        });

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                isSigned = true;
            }

            @Override
            public void onSigned() {
                isSigned = true;
                try {
                    cleared[0] = false;
                } catch (Exception ignored) {

                }
            }

            @Override
            public void onClear() {
                isSigned = false;
            }
        });

        btnSubmit.setOnClickListener(v -> {
            if (isSigned) {
                header.setSignature(Utils.encodeImageBase64Sign(signaturePad.getTransparentSignatureBitmap()));
                header.setIs_verif(1);
                header.setId_salesman(user.getUsername());
                dialog.dismiss();

                progress.show();
                PARAM = 1;
                new RequestUrl().execute();
            } else {
                setToast("Harus tanda tangan");
            }
        });
        dialog.show();
    }


    private void initData() {
        header = SessionManagerQubes.getStockRequestHeader();
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

            if (header.getStatus() != null) {
                switch (header.getStatus()) {
                    case Constants.STATUS_APPROVE:
//                        btnVerification.setVisibility(View.VISIBLE);
//                        btnUnloading.setVisibility(View.GONE);
                        if (header.getIs_verif() == 1) {
                            if (header.getIs_unloading() == 1) {
                                btnUnloading.setVisibility(View.GONE);
                                btnVerification.setVisibility(View.GONE);
                            } else {
                                btnUnloading.setVisibility(View.VISIBLE);
                                btnVerification.setVisibility(View.GONE);
                            }
                        } else {
                            btnUnloading.setVisibility(View.GONE);
                            btnVerification.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        btnUnloading.setVisibility(View.GONE);
                        btnVerification.setVisibility(View.GONE);
                        break;
                }
            } else {
                btnUnloading.setVisibility(View.GONE);
                btnVerification.setVisibility(View.GONE);
            }

            getDataOffline();
        }
    }

    private void initialize() {
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
        btnUnloading = findViewById(R.id.btnUnloading);
        btnVerification = findViewById(R.id.btnVerification);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void setAdapter() {
        mAdapter = new StockRequestDetailAdapter(this, mList, header -> {

        });
        recyclerView.setAdapter(mAdapter);
    }

    private void getDataOffline() {
        mList = new ArrayList<>();
        mList = database.getAllStockRequestDetail(header.getIdHeader());

        if (mList == null || mList.isEmpty()) {
            llNoData.setVisibility(View.VISIBLE);
        } else {
            llNoData.setVisibility(View.GONE);
            setAdapter();
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
                    openDialogSignature();
                } else {
                    setToast(getString(R.string.pleaseEnablePermission));
                }
                break;
        }
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_ = Constants.API_STOCK_REQUEST_VERIFICATION;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, header);
                    return null;
                } else {
                    String URL_ = Constants.API_STOCK_REQUEST_UPDATE;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, header);
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("verification", ex.getMessage());
                }
                logResult = new WSMessage();
                logResult.setIdMessage(0);
                logResult.setMessage("Verification Stock Request error: " + ex.getMessage());
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
            if (logResult != null) {
                if (logResult.getIdMessage() == 1) {
                    String message = "Verification Stock Request : " + logResult.getMessage();
                    logResult.setMessage(message);

                    database.updateStockRequestVerification(header, user.getUsername());
                    setToast("Verifikasi sukses");
                    onBackPressed();
                } else {
                    setToast(logResult.getMessage());
                }
                database.addLog(logResult);
            }
        }
    }
}
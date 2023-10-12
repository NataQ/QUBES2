package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.UnloadingAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.UnloadingPdfUtils;
import id.co.qualitas.qubes.utils.Utils;

public class UnloadingActivity extends BaseActivity {
    private UnloadingAdapter mAdapter;
    private List<Material> mList;
    private Button btnSubmit;
    private UnloadingPdfUtils pdfUnloadingUtils;
    private File pdfFile;
    private Boolean success = false;
    private TextView txtDate, txtNoDoc, txtTglKirim, txtNoSuratJalan;
    private StockRequest header;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private final static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_unloading);

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
                setAdapter();
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
                    callAPI();
                } else {
                    setToast(getString(R.string.pleaseEnablePermission));
                    requestPermission();
                }
            }
        });

        dialog.show();
    }

    private void callAPI() {
        progress.show();
        new RequestUrl().execute();
    }

    private class AsyncTaskGeneratePDF extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                header = database.getStockRequestHeader(header.getIdHeader());
                pdfFile = new File(Utils.getDirLocPDF(getApplicationContext()) + "/unloading.pdf");
                success = pdfUnloadingUtils.createPDF(pdfFile, header, mList);
                return success;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (result == null) {
                setToast("Gagal membuat pdf.. Silahkan coba lagi..");
            } else {
                if (result) {
                    setToast("Downloaded to " + pdfFile.getAbsolutePath());
                    onBackPressed();
                } else {
                    setToast("Gagal membuat pdf.. Silahkan coba lagi..");
                }
            }
        }
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
            getDataOffline();
        }
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        pdfUnloadingUtils = UnloadingPdfUtils.getInstance(UnloadingActivity.this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtNoData = findViewById(R.id.txtNoData);
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
                    callAPI();
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
        mList = database.getAllStockRequestDetail(header.getIdHeader());

        if (mList == null || mList.isEmpty()) {
            txtNoData.setVisibility(View.VISIBLE);
        } else {
            txtNoData.setVisibility(View.GONE);
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
                String URL_ = Constants.API_STOCK_REQUEST_UNLOADING;
                final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, header);
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
            if (WsMessage != null) {
                if (WsMessage.getIdMessage() == 1) {
                    header.setIs_unloading(1);
//                    header.setSync(0);
//                    header.setStatus(Constants.STATUS_UNLOADING);
                    database.updateUnloading(header, user.getUsername());
                    setToast("Unloading sukses");
                    progress.show();
                    new AsyncTaskGeneratePDF().execute();
                } else {
                    setToast(WsMessage.getMessage());
                }
            } else {
                setToast(getString(R.string.serverError));
            }
        }
    }
}
package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.activity.PDFTest;
import id.co.qualitas.qubes.adapter.aspp.StockRequestDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.UnloadingAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.utils.UnloadingPdfUtils;
import id.co.qualitas.qubes.utils.Utils;

public class UnloadingActivity extends BaseActivity {
    private UnloadingAdapter mAdapter;
    private List<Material> mList;
    private Button btnSubmit;
    private UnloadingPdfUtils pdfUnloadingUtils;
    private File pdfFile;
    private Boolean success = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_unloading);

        init();
        initialize();
        initData();

        mAdapter = new UnloadingAdapter(this, mList, header -> {

        });
        recyclerView.setAdapter(mAdapter);

        btnSubmit.setOnClickListener(v -> {
            openDialogUnloading();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(UnloadingActivity.this);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void openDialogUnloading() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final Dialog alertDialog = new Dialog(getApplicationContext());
        View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(dialogView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
        TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);
        Button btnNo = alertDialog.findViewById(R.id.btnNo);
        Button btnYes = alertDialog.findViewById(R.id.btnYes);

        txtTitle.setText("Unloading");
        txtDialog.setText("Are you sure want to unloading?");

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTaskGeneratePDF().execute();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private class AsyncTaskGeneratePDF extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                pdfFile = new File(Utils.getDirLocPDF(getApplicationContext()) + "/unloading.pdf");
                success = pdfUnloadingUtils.createPDF(pdfFile);
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
                setToast("generate pdf failed, please try again");
            } else {
                if (result) {
                    setToast("Downloaded to " + pdfFile.getAbsolutePath());
                } else {
                    setToast("Can't show pdf. Please try again later..");
                }
            }
        }
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Material("11001", "Kratingdaeng", "1", "BTL"));
        mList.add(new Material("11030", "Redbull", "1", "BTL"));
        mList.add(new Material("31020", "You C1000 Vitamin Orange", "1", "BTL"));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        pdfUnloadingUtils = UnloadingPdfUtils.getInstance(UnloadingActivity.this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSubmit = findViewById(R.id.btnSubmit);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
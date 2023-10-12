package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.InvoiceVerificationAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.utils.Utils;

public class InvoiceVerificationActivity extends BaseActivity {
    private InvoiceVerificationAdapter mAdapter;
    private List<Invoice> mList;
    private Button btnSubmit;
    private TextView txtDate, txtTotalInvoice, txtTotalAmount;
    private double totalInvoice = 0;
    private double totalAmount = 0.0;
    private Bitmap transSign;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_invoice_verification);
        initialize();

        btnSubmit.setOnClickListener(v -> {
            openDialogSignature();
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(InvoiceVerificationActivity.this);
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

        txtTitle.setOnClickListener(view -> {
            database.deleteInvoiceHeader();
            database.deleteInvoiceDetail();
            getFirstDataOffline();
        });
    }

    private void setAdapter() {
        mAdapter = new InvoiceVerificationAdapter(this, mList, header -> {
        });

        recyclerView.setAdapter(mAdapter);
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

            }

            @Override
            public void onSigned() {
                try {
                    cleared[0] = false;
                } catch (Exception ignored) {

                }
            }

            @Override
            public void onClear() {
            }
        });

        btnSubmit.setOnClickListener(v -> {
            //            Bitmap convertedImage = Utils.getResizedBitmap(signaturePad.getTransparentSignatureBitmap(), 200);
//            String signPath = Utils.saveImage(signaturePad.getTransparentSignatureBitmap()).getAbsolutePath();
//            if (!signPath.equals("null")) {
            transSign = signaturePad.getTransparentSignatureBitmap();
            dialog.dismiss();
            progress.show();
            PARAM = 2;
            new AsyncLoading().execute();//2
//            } else {
//                setToast("Gagal menyimpan ttd");
//            }
        });
        dialog.show();
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Invoice("DKA495486", "TOKO SIDIK HALIM", "0GV43", 884736, 0, "2023-06-01", true, initDataMaterial()));
        mList.add(new Invoice("DKA496933", "TOKO SIDIK HALIM", "0GV43", 3929664, 0, "2023-07-01", false, initDataMaterial()));
        mList.add(new Invoice("DKA492540", "SARI SARI (TK)", "0WJ42", 14024448, 0, "2023-09-01", true, initDataMaterial()));
        mList.add(new Invoice("DKA402541", "TOKO SIDIK HALIM", "0GV43", 9363600, 0, "2023-11-01", false, initDataMaterial()));

        for (Invoice param : mList) {
            int idHeader = database.addInvoiceHeader(param, user.getUsername());
            for (Material param1 : param.getMaterialList()) {
                database.addInvoiceDetail(param1, String.valueOf(idHeader), user.getUsername());
            }
        }
    }

    private List<Material> initDataMaterial() {
        List<Material> mList = new ArrayList<>();
        mList.add(new Material("31001", "You C1000 Vitamin Lemon", 1200000));
        mList.add(new Material("11001", "Kratingdaeng", 50000));
        mList.add(new Material("11030", "Redbull", 740000));
        mList.add(new Material("21001", "Torpedo Aneka Buah", 350000));
        return mList;
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtDate = findViewById(R.id.txtDate);
        txtTotalInvoice = findViewById(R.id.txtTotalInvoice);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtTitle = findViewById(R.id.txtTitle);
        progressCircle = findViewById(R.id.progressCircle);
        swipeLayout = findViewById(R.id.swipeLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFirstDataOffline();
    }

    private void getFirstDataOffline() {
        getData();
        setAdapter();

        if (mList == null || mList.isEmpty()) {
            progressCircle.setVisibility(View.VISIBLE);
            PARAM = 1;
            new AsyncLoading().execute();//1
        }
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllInvoiceHeaderNotVerif();

        int verif = 0;
        for (Invoice inv : mList) {
            if (inv.isVerification()) {
                verif++;
            }
        }

        if (verif == mList.size()) {
            btnSubmit.setVisibility(View.GONE);
        } else {
            btnSubmit.setVisibility(View.VISIBLE);
        }

        for (Invoice inv : mList) {
            totalInvoice = totalInvoice + 1;
            totalAmount = totalAmount + inv.getAmount();
        }

        txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        txtTotalAmount.setText("Rp. " + format.format(totalAmount));
        txtTotalInvoice.setText(format.format(totalInvoice));
    }

    private class AsyncLoading extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    initData();
                } else {
                    for (Invoice invoice : mList) {
                        invoice.setSignature(Utils.encodeImageBase64Sign(transSign));
                        invoice.setVerification(true);
                        invoice.setSync(false);
                        database.updateInvoiceVerification(invoice, user.getUsername());
                    }
                }
                return true;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("invoice", ex.getMessage());
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
            if (PARAM == 1) {
                progressCircle.setVisibility(View.GONE);
                getData();
                mAdapter.setData(mList);
            } else {
                progress.dismiss();
                setToast("Verifikasi sukses");
                btnSubmit.setVisibility(View.GONE);
            }
        }
    }
}
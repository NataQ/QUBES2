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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.InvoiceVerificationAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.utils.Utils;

public class InvoiceVerificationActivity extends BaseActivity {
    private InvoiceVerificationAdapter mAdapter;
    private List<Invoice> mList;
    private Button btnSubmit;
    private TextView txtDate, txtTotalInvoice, txtTotalAmount;
    private double totalInvoice = 0;
    private double totalAmount = 0.0;
    private Bitmap transSign;
    private WSMessage resultWsMessage;
    private boolean saveDataSuccess = false;
    private String signature;
    private boolean isSigned;

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
            //            Bitmap convertedImage = Utils.getResizedBitmap(signaturePad.getTransparentSignatureBitmap(), 200);
//            String signPath = Utils.saveImage(signaturePad.getTransparentSignatureBitmap()).getAbsolutePath();
//            if (!signPath.equals("null")) {
            if (isSigned) {
                transSign = signaturePad.getTransparentSignatureBitmap();
                dialog.dismiss();
                progress.show();
                PARAM = 3;
                new RequestUrl().execute();//3
            } else {
                setToast("Harus tanda tangan");
            }
//            } else {
//                setToast("Gagal menyimpan ttd");
//            }
        });
        dialog.show();
    }

    private void initialize() {
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
        setTotal();

        if (mList == null || mList.isEmpty()) {
            progressCircle.setVisibility(View.VISIBLE);
            PARAM = 1;
            new RequestUrl().execute();//1
        }
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllInvoiceHeaderNotVerif();
    }

    private void setTotal() {
        int verif = 0;
        for (Invoice inv : mList) {
            if (inv.getIs_verif() == 1) {
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
            totalAmount = totalAmount + (inv.getAmount() - inv.getTotal_paid());
        }

        txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        txtTotalAmount.setText("Rp. " + format.format(totalAmount));
        txtTotalInvoice.setText(format.format(totalInvoice));
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_ = Constants.API_INVOICE_LIST;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                } else if (PARAM == 2) {
                    mList = new ArrayList<>();
                    Invoice[] paramArray = Helper.ObjectToGSON(resultWsMessage.getResult(), Invoice[].class);
                    Collections.addAll(mList, paramArray);
                    database.deleteInvoiceHeader();
                    database.deleteInvoiceDetail();

                    for (Invoice param : mList) {
                        List<Material> listMat = new ArrayList<>();
                        Material[] matArray = Helper.ObjectToGSON(param.getMaterialList(), Material[].class);
                        Collections.addAll(listMat, matArray);
                        param.setMaterialList(listMat);

                        int idHeader = database.addInvoiceHeader(param, user.getUserLogin());
                        for (Material mat : listMat) {
                            database.addInvoiceDetail(mat, String.valueOf(idHeader), user.getUserLogin());
                        }
                    }
                    getData();
                    saveDataSuccess = true;
                    return null;
                } else if (PARAM == 3) {
                    String URL_ = Constants.API_INVOICE_VERIFICATION;
                    signature = Utils.encodeImageBase64Sign(transSign);
                    Map request = new HashMap();
                    request.put("header", mList);
                    request.put("signature", signature);

                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, request);
                } else {
                    for (Invoice invoice : mList) {
                        invoice.setSignature(signature);
                        invoice.setIs_verif(1);
                        database.updateInvoiceVerification(invoice, user.getUsername());
                    }
                    saveDataSuccess = true;
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("invoice", ex.getMessage());
                }
                if (PARAM == 2 || PARAM == 4) {
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
                progressCircle.setVisibility(View.GONE);
                if (saveDataSuccess) {
                    setTotal();
                    mAdapter.setData(mList);
                } else {
                    setToast(getString(R.string.failedSaveData));
                }
            } else if (PARAM == 3) {
                if (result != null) {
                    if (result.getIdMessage() == 1) {
                        setToast("Verifikasi sukses");
                        PARAM = 4;
                        new RequestUrl().execute();//4
                    } else {
                        progress.dismiss();
                        setToast(result.getMessage());
                    }
                } else {
                    progress.dismiss();
                    setToast(getString(R.string.serverError));
                }
            } else {
                progress.dismiss();
                if (saveDataSuccess) {
                    btnSubmit.setVisibility(View.GONE);
                } else {
                    setToast(getString(R.string.failedSaveData));
                }
            }
        }
    }
}
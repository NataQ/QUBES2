package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
import id.co.qualitas.qubes.model.StartVisit;
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
    private WSMessage resultWsMessage, logResult;
    private boolean saveDataSuccess = false;
    private String signature;
    private boolean isSigned;
    private StartVisit startVisit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_invoice_verification_list);
        initialize();

        btnSubmit.setOnClickListener(v -> {
            dialogConfirmation();
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(InvoiceVerificationActivity.this);
        });

        swipeLayout.setColorSchemeResources(R.color.blue_aspp, R.color.green_aspp, R.color.yellow_krang, R.color.red_krang);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
                swipeLayout.setRefreshing(false);
            }
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

    public void dialogConfirmation() {
        LayoutInflater inflater = LayoutInflater.from(InvoiceVerificationActivity.this);
        final Dialog dialog = new Dialog(InvoiceVerificationActivity.this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDialog = dialog.findViewById(R.id.txtDialog);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        txtTitle.setText("Verification");
        txtDialog.setText("Are you sure?");

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
                openDialogSignature();
            }
        });

        dialog.show();
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = findViewById(R.id.llNoData);
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
        startVisit = database.getLastStartVisit();
        getData();
        setAdapter();
        setTotal();

        if (mList == null || mList.isEmpty()) {
            requestData();
        }
    }

    private void requestData() {
        progressCircle.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        PARAM = 1;
        new RequestUrl().execute();//1
//        setDataDummy();
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllInvoiceHeaderNotVerif();
    }

    private void setTotal() {
        totalInvoice = 0;
        totalAmount = 0;

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
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else if (PARAM == 2) {
                    mList = new ArrayList<>();
                    Invoice[] paramArray = Helper.ObjectToGSON(resultWsMessage.getResult(), Invoice[].class);

                    if (paramArray != null) {
                        Collections.addAll(mList, paramArray);
                        if (startVisit.getStatus_visit() == 1) {
                            database.deleteInvoiceHeader();
                            database.deleteInvoiceDetail();
                        }
                    }

                    for (Invoice param : mList) {
                        List<Material> listMat = new ArrayList<>();
                        Material[] matArray = Helper.ObjectToGSON(param.getMaterialList(), Material[].class);
                        if (matArray != null) {
                            Collections.addAll(listMat, matArray);
                        }
                        param.setMaterialList(listMat);

                        int idHeader = database.addInvoiceHeader(param, user.getUsername());

                        Map header = new HashMap();
                        header.put("idHeader", String.valueOf(idHeader));
                        header.put("username", user.getUsername());
                        header.put("no_invoice", param.getNo_invoice());
                        for (Material mat : listMat) {
                            database.addInvoiceDetail(mat, header);
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
                    request.put("username", user.getUsername());

                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, request);
                    return null;
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
                String exMess = null;
                switch (PARAM) {
                    case 1:
                        logResult = new WSMessage();
                        logResult.setIdMessage(0);
                        logResult.setResult(null);
                        exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                        logResult.setMessage("Invoice List Error : " + exMess);
                        break;
                    case 2:
                    case 4:
                        saveDataSuccess = false;
                        break;
                    case 3:
                        logResult = new WSMessage();
                        logResult.setIdMessage(0);
                        logResult.setResult(null);
                        exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                        logResult.setMessage("Invoice Verification Error : " + exMess);
                        break;

                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage re) {
            if (PARAM == 1) {
                if (logResult.getIdMessage() == 1) {
                    String message = "Invoice List : " + logResult.getMessage();
                    logResult.setMessage(message);
                }
                database.addLog(logResult);
                if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                    resultWsMessage = logResult;
                    PARAM = 2;
                    new RequestUrl().execute();//2
                } else {
                    progressCircle.setVisibility(View.GONE);
                    setToast(logResult.getMessage());
                    mAdapter.setData(mList);
                    setTotal();
                    if (Helper.isEmptyOrNull(mList)) {
                        recyclerView.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
                    }
                }
            } else if (PARAM == 2) {
                progressCircle.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (saveDataSuccess) {
                    mAdapter.setData(mList);
                    setTotal();
                } else {
                    setToast(getString(R.string.failedSaveData));
                }

                if (Helper.isEmptyOrNull(mList)) {
                    recyclerView.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }
            } else if (PARAM == 3) {
                if (logResult.getIdMessage() == 1) {
                    String message = "Invoice Verification : " + logResult.getMessage();
                    logResult.setMessage(message);
                }
                database.addLog(logResult);
                if (logResult.getIdMessage() == 1) {
                    setToast("Verifikasi sukses");
                    PARAM = 4;
                    new RequestUrl().execute();//4
                } else {
                    progress.dismiss();
                    setToast(logResult.getMessage());
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
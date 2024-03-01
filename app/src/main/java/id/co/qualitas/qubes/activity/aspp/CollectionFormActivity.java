package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CollectionCashAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionChequeAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionGiroAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionKreditAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionLainAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionTransferAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.CollectionDetail;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.printer.AbstractConnector;
import id.co.qualitas.qubes.printer.ConnectorActivity;
import id.co.qualitas.qubes.printer.ConnectorAdapter;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class CollectionFormActivity extends BaseActivity {
    double totalPaymentCash, totalPaymentLain, leftCash, leftLain;
    private Button btnSubmit, btnAddTransfer, btnAddGiro, btnAddCheque;
    private RecyclerView recyclerViewCash, recyclerViewTransfer, recyclerViewGiro, recyclerViewCheque, recyclerViewLain, recyclerViewKredit;
    private TextView edtPaymentCash, edtPaymentLain;
    private TextView txtLeftCash, txtLeftLain;
    private TextView txtInvNo, txtDate, txtAmount, txtOrderNo;
    private LinearLayout llOrder, llInvoice;
    private TextView txtCash, txtTransfer, txtGiro, txtCheq, txtLain, txtKredit;
    private LinearLayout buttonCash, buttonTransfer, buttonCheq, buttonGiro, buttonLain, buttonKredit;
    private LinearLayout llCash, llTransfer, llGiro, llCheque, llLain, llKredit;
    private List<CollectionDetail> mListTransfer, mListGiro, mListCheque;
    //    private List<CollectionDetail> mListTransfer;
//    private List<CollectionDetail> mListGiro;
//    private List<CollectionDetail> mListCheque;
    private List<Material> mListCash, mListLain, mListKredit, mListMaster;
    private WSMessage logResult;
    private Invoice header;
    private CollectionKreditAdapter mAdapterKredit;
    private CollectionCashAdapter mAdapterCash;//14400
    private CollectionTransferAdapter mAdapterTransfer;//18000
    private CollectionGiroAdapter mAdapterGiro;
    private CollectionChequeAdapter mAdapterCheque;
    private CollectionLainAdapter mAdapterLain;
    private int colLFrom = 0;
    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;
    private static final int REQUEST_LOCATION_PERMISSION = 5;
    private static final int REQUEST_ENABLE_BT = 6;
    private SharedPreferences mPreferences;
    private RecyclerView mConnectorView;
    private SwipeRefreshLayout mSwipeLayout;
    private List<AbstractConnector> mConnectorList;
    private ConnectorAdapter mConnectorAdapter;

    List<Material> cashList = new ArrayList<>();
    List<Material> tfList = new ArrayList<>();
    List<Material> giroList = new ArrayList<>();
    List<Material> chequeList = new ArrayList<>();
    List<Material> lainList = new ArrayList<>();

    boolean kredit = false;
    double totalAmountPaid = 0;
    private boolean saveCollection = false, saveOrder = false;
    private Order orderHeader;
    private int isSync = 0;
    private double totalTagihan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_collection_form);

        initialize();

        btnSubmit.setOnClickListener(v -> {
            int res = validate();
            switch (res) {
                case 1:
                    if (colLFrom == 1) {
                        PARAM = 1;
                    } else {
                        PARAM = 2;
                        isSync = 0;
                    }
//                    setToast("OK");
                    progress.show();
                    new RequestUrl().execute();//save
                    break;
                case 2:
                    setToast("Tidak boleh melebihi total tagihan");
                    break;
                case 3:
                    setToast("Pembayaran tidak boleh ada sisa");
                    break;
                case 0:
                    setToast("Pastikan semua field sudah terisi");
                    break;
            }
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CollectionFormActivity.this);
        });
    }

//    public void updateLeft(int type, int idHeader) {
//        switch (type) {
//            case 2:
//                mAdapterTransfer.notifyDataSetChanged();
////                if (mListTransfer.size() > idHeader) {
////                    mAdapterTransfer.notifyItemRangeChanged(idHeader + 1, mListTransfer.size());
////                }
//                break;
//            case 3:
//                mAdapterGiro.notifyDataSetChanged();
////                if (mListGiro.size() > idHeader) {
////                    mAdapterGiro.notifyItemRangeChanged(idHeader + 1, mListGiro.size());
////                }
//                break;
//            case 4:
//                mAdapterCheque.notifyDataSetChanged();
////                if (mListCheque.size() > idHeader) {
////                    mAdapterCheque.notifyItemRangeChanged(idHeader + 1, mListCheque.size());
////                }
//                break;
//        }
//    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {
        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    Map request = prepareData();
                    String URL_ = Constants.API_SAVE_COLLECTION;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, request);
                    return null;
                } else {
                    Map request = new HashMap();
                    request.put("user", user);
                    request.put("totalAmountPaid", totalAmountPaid);
                    request.put("totalPaymentCash", totalPaymentCash);
                    request.put("leftCash", leftCash);
                    request.put("cashList", cashList);
                    request.put("totalPaymentLain", totalPaymentLain);
                    request.put("leftLain", leftLain);
                    request.put("lainList", lainList);
                    request.put("tfList", tfList);
                    request.put("mListTransfer", mListTransfer);
                    request.put("giroList", giroList);
                    request.put("mListGiro", mListGiro);
                    request.put("chequeList", chequeList);
                    request.put("mListCheque", mListCheque);
                    request.put("mListCash", mListCash);
                    request.put("isSync", isSync);
                    if (colLFrom == 3) {
                        double paid = 0;
                        if (Helper.isNotEmptyOrNull(mListMaster)) {
                            paid = orderHeader.getOmzet() - mListMaster.get(0).getTotalAmountPaid();
                        } else {
                            paid = orderHeader.getOmzet();
                        }
//                        if (totalAmountPaid > paid || totalAmountPaid == paid) {
////                        if (totalAmountPaid > nett || totalAmountPaid == nett) {
//                            request.put("isPaid", 1);
//                            orderHeader.setStatusPaid(true);
//                        } else {
//                            request.put("isPaid", 0);
//                            orderHeader.setStatusPaid(false);
//                        }
                        orderHeader.setId_driver(user.getId_driver());
                        SessionManagerQubes.setOrder(orderHeader);
                        request.put("header", orderHeader);
                        saveCollection = database.addCollectionOrder(request);
                    } else {
                        request.put("header", header);
                        saveCollection = database.addCollection(request);
                    }
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("Collection", ex.getMessage());
                }
                if (PARAM == 2) {
                    saveCollection = false;
                } else {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                    logResult.setMessage("Collection error: " + exMess);
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
            progress.dismiss();
            if (PARAM == 1) {
                if (logResult.getIdMessage() == 1) {
                    String message = "Route Customer : " + logResult.getMessage();
                    logResult.setMessage(message);
                }
                database.addLog(logResult);
                if (logResult.getIdMessage() == 1) {
                    isSync = 1;
                    progress.show();
                    PARAM = 2;
                    new RequestUrl().execute();//2
                } else {
                    setToast(getString(R.string.failedSaveData));
                }
            } else {
                if (saveCollection) {
                    setToast("Save Success");
                    switch (colLFrom) {
                        case 2:
                            intent = new Intent(CollectionFormActivity.this, CollectionVisitActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            if (SessionManagerQubes.getAlreadyPrint() || !Helper.isCanvasSales(user)) {
                                intent = new Intent(CollectionFormActivity.this, OrderActivity.class);
                                startActivity(intent);
                            } else {
                                if (ContextCompat.checkSelfPermission(CollectionFormActivity.this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(CollectionFormActivity.this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH);
                                } else if (ContextCompat.checkSelfPermission(CollectionFormActivity.this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(CollectionFormActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN);
                                } else if (ContextCompat.checkSelfPermission(CollectionFormActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        ActivityCompat.requestPermissions(CollectionFormActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_BLUETOOTH_CONNECT);
                                    }
                                } else if (ContextCompat.checkSelfPermission(CollectionFormActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        ActivityCompat.requestPermissions(CollectionFormActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_BLUETOOTH_SCAN);
                                    }
                                } else if (ContextCompat.checkSelfPermission(CollectionFormActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(CollectionFormActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                                } else {
                                    intent = new Intent(CollectionFormActivity.this, ConnectorActivity.class);
                                    startActivity(intent);
                                }
                            }
                            break;
                        case 1:
                        default:
                            intent = new Intent(CollectionFormActivity.this, CollectionActivity.class);
                            startActivity(intent);
                            break;
                    }
                } else {
                    setToast("Save Failed");
                }
            }
        }
    }

    private Map prepareData() {
        Map request = new HashMap();
        request.put("no_invoice", header.getNo_invoice());
        request.put("id_driver", user.getId_driver());
        request.put("date", Helper.getTodayDate(Constants.DATE_FORMAT_3));
        request.put("username", user.getUsername());
        request.put("totalAmount", totalAmountPaid);
        if (Helper.isNotEmptyOrNull(cashList)) {
            request.put("totalPaymentCash", totalPaymentCash);
            request.put("cashList", cashList);
        }
        if (Helper.isNotEmptyOrNull(lainList)) {
            request.put("totalPaymentLain", totalPaymentLain);
            request.put("lainList", lainList);
        }
        if (Helper.isNotEmptyOrNull(tfList)) {
            request.put("listTransfer", mListTransfer);
        }
        if (Helper.isNotEmptyOrNull(giroList)) {
            request.put("listGiro", mListGiro);
        }
        if (Helper.isNotEmptyOrNull(chequeList)) {
            request.put("listCheque", mListCheque);
        }
        return request;
    }

    private int validate() {
        int emptyText = 0, mat = 0;
        totalAmountPaid = 0;
        cashList = new ArrayList<>();
        tfList = new ArrayList<>();
        giroList = new ArrayList<>();
        chequeList = new ArrayList<>();
        lainList = new ArrayList<>();
        double totalPayment = 0;
        double leftPayment = 0;

        for (Material material : mListCash) {
//            if(material.isChecked() && material.getAmountPaid() != 0){
            if (material.getAmountPaid() != 0) {
                cashList.add(material);
                mat++;
                totalAmountPaid = totalAmountPaid + material.getAmountPaid();
            }
        }

        for (CollectionDetail collection : mListTransfer) {
            tfList = new ArrayList<>();
            totalPayment = totalPayment + collection.getTotalPayment();
            leftPayment = leftPayment + collection.getLeft();
            for (Material material : collection.getMaterialList()) {
//                if (material.isChecked() && material.getAmountPaid() != 0) {
                if (material.getAmountPaid() != 0) {
                    tfList.add(material);
                    mat++;
                    totalAmountPaid = totalAmountPaid + material.getAmountPaid();
                }
            }

            if (tfList.size() != 0) {
                if (Helper.isEmpty(collection.getTgl())) {
                    emptyText++;
                }
            }

            collection.setCheckedMaterialList(tfList);
        }

        for (CollectionDetail collection : mListGiro) {
            giroList = new ArrayList<>();
            totalPayment = totalPayment + collection.getTotalPayment();
            leftPayment = leftPayment + collection.getLeft();
            for (Material material : collection.getMaterialList()) {
//                if (material.isChecked() && material.getAmountPaid() != 0) {
                if (material.getAmountPaid() != 0) {
                    giroList.add(material);
                    mat++;
                    totalAmountPaid = totalAmountPaid + material.getAmountPaid();
                }
            }

            if (giroList.size() != 0) {
                if (Helper.isEmpty(collection.getTgl())) {
                    emptyText++;
                }

                if (Helper.isEmpty(collection.getTglCair())) {
                    emptyText++;
                }

                if (Helper.isEmpty(collection.getIdBankCust()) || Helper.isEmpty(collection.getBankCust())) {
                    emptyText++;
                }

                if (Helper.isEmpty(collection.getIdBankASPP()) || Helper.isEmpty(collection.getBankNameASPP())) {
                    emptyText++;
                }
            }

            collection.setCheckedMaterialList(giroList);
        }

        for (CollectionDetail collection : mListCheque) {
            chequeList = new ArrayList<>();
            totalPayment = totalPayment + collection.getTotalPayment();
            leftPayment = leftPayment + collection.getLeft();
            for (Material material : collection.getMaterialList()) {
//                if (material.isChecked() && material.getAmountPaid() != 0) {
                if (material.getAmountPaid() != 0) {
                    chequeList.add(material);
                    totalAmountPaid = totalAmountPaid + material.getAmountPaid();
                    mat++;
                }
            }

            if (chequeList.size() != 0) {
                if (Helper.isEmpty(collection.getTgl())) {
                    emptyText++;
                }

                if (Helper.isEmpty(collection.getTglCair())) {
                    emptyText++;
                }

                if (Helper.isEmpty(collection.getIdBankCust()) || Helper.isEmpty(collection.getBankCust())) {
                    emptyText++;
                }

                if (Helper.isEmpty(collection.getIdBankASPP()) || Helper.isEmpty(collection.getBankNameASPP())) {
                    emptyText++;
                }
            }

            collection.setCheckedMaterialList(chequeList);
        }

        for (Material material : mListLain) {
//            if(material.isChecked() && material.getAmountPaid() != 0){
            if (material.getAmountPaid() != 0) {
                lainList.add(material);
                totalAmountPaid = totalAmountPaid + material.getAmountPaid();
                mat++;
            }
        }

        totalPayment = totalPayment + totalPaymentLain + totalPaymentCash;
        leftPayment  = leftPayment + leftCash + leftLain;

        if (totalPayment > totalTagihan) {
            return 2;
        }else if(leftPayment != 0){
            return 3;
        } else if (mat > 0 && emptyText == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private void setCashView() {
        mAdapterCash = new CollectionCashAdapter(this, mListCash, header -> {

        });
        recyclerViewCash.setAdapter(mAdapterCash);

        buttonCash.setOnClickListener(v -> {
            setSelectView(1);
        });

        edtPaymentCash.setOnClickListener(v -> {
            openDialogTotalAmount(1);
        });

//        edtPaymentCash.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Helper.setDotCurrency(edtPaymentCash, this, s);
//                if (!s.toString().equals("") && !s.toString().equals("-")) {
//                    double qty = Double.parseDouble(s.toString().replace(",", ""));
//                    if (qty < 0) {
//                        Toast.makeText(CollectionFormActivity.this, "Tidak boleh kurang dari 0", Toast.LENGTH_SHORT).show();
//                    } else {
//                        totalPaymentCash = qty;
//                    }
//                } else {
//                    totalPaymentCash = 0;
//                }
//                setLeftCash();
//            }
//        });
    }

    private void openDialogTotalAmount(int typePayment) {
        LayoutInflater inflater = LayoutInflater.from(CollectionFormActivity.this);
        final Dialog dialog = new Dialog(CollectionFormActivity.this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_amount_total, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        TextView txtTypePayment = dialog.findViewById(R.id.txtTypePayment);
        EditText edtTotalPayment = dialog.findViewById(R.id.edtTotalPayment);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        if(typePayment == 1){
            txtTypePayment.setText("Cash");
            edtTotalPayment.setText(totalPaymentCash != 0 ? Helper.setDotCurrencyAmount(totalPaymentCash) : null);
        }else{
            txtTypePayment.setText("Lain-Lain");
            edtTotalPayment.setText(totalPaymentLain != 0 ? Helper.setDotCurrencyAmount(totalPaymentLain) : null);
        }

        edtTotalPayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Helper.setDotCurrency(edtTotalPayment, this, s);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double qty = Double.parseDouble(edtTotalPayment.getText().toString().replace(",", ""));
                if (typePayment == 1) {
                    edtPaymentCash.setText(Helper.setDotCurrencyAmount(qty));
                    totalPaymentCash = qty;
                    if (colLFrom == 3) {
                        mListCash = database.getAllDetailOrder(new Order(orderHeader.getIdHeader()));
                    } else {
                        mListCash = database.getAllInvoiceDetail(header);
                    }
                    mAdapterCash.setData(mListCash);
                    setLeftCash();
                } else {
                    edtPaymentLain.setText(Helper.setDotCurrencyAmount(qty));
                    totalPaymentLain = qty;
                    if (colLFrom == 3) {
                        mListLain = database.getAllDetailOrder(new Order(orderHeader.getIdHeader()));
                    } else {
                        mListLain = database.getAllInvoiceDetail(header);
                    }
                    mAdapterLain.setData(mListLain);
                    setLeftLain();
                }
                updateKurangBayarDelete();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setKreditView() {
        mAdapterKredit = new CollectionKreditAdapter(this, mListKredit, header -> {

        });
        recyclerViewKredit.setAdapter(mAdapterKredit);

        buttonKredit.setOnClickListener(v -> {
            //kalau kredit nya true, yg lain gak boleh di pilih
            //kalau di klik lagi kreditnya, enable nya baru bisa lepas
            if (kredit) {
                kredit = false;
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.GONE);

                buttonCash.setEnabled(true);
                buttonTransfer.setEnabled(true);
                buttonGiro.setEnabled(true);
                buttonCheq.setEnabled(true);
                buttonLain.setEnabled(true);
            } else {
                kredit = true;
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_disable));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_disable));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_disable));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_disable));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_disable));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.GONE);

                buttonCash.setEnabled(false);
                buttonTransfer.setEnabled(false);
                buttonGiro.setEnabled(false);
                buttonCheq.setEnabled(false);
                buttonLain.setEnabled(false);
            }

        });
    }

    private void setTransferView() {
        buttonTransfer.setOnClickListener(v -> {
            setSelectView(2);
        });

        mAdapterTransfer = new CollectionTransferAdapter(this, mListTransfer, header -> {

        });
        recyclerViewTransfer.setAdapter(mAdapterTransfer);

        btnAddTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        progress.show();
                        List<Material> newList = new ArrayList<>();
                        for (Material p : mListMaster) {
                            Material cloneMat = null;
//                            try {
                            cloneMat = (Material) p.clone();
                            cloneMat.setAmountPaid(0);
//                                cloneMat.setChecked(false);
//                            } catch (CloneNotSupportedException e) {
//                                e.printStackTrace();
//                            }
                            newList.add(cloneMat);
                        }

                        CollectionDetail colLTf = new CollectionDetail();
                        colLTf.setTgl(null);
                        colLTf.setTotalPayment(0);
                        colLTf.setLeft(0);
                        colLTf.setMaterialList(newList);
                        mListTransfer.add(colLTf);

                        mAdapterTransfer.notifyDataSetChanged();
                    }

                    public void onFinish() {
                        progress.dismiss();
                        recyclerViewTransfer.smoothScrollToPosition(recyclerViewTransfer.getAdapter().getItemCount() - 1);
                    }
                }.start();
            }
        });
    }

    private void setGiroView() {
        buttonGiro.setOnClickListener(v -> {
            setSelectView(3);
        });

        mAdapterGiro = new CollectionGiroAdapter(this, mListGiro, header -> {

        });
        recyclerViewGiro.setAdapter(mAdapterGiro);

        btnAddGiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        progress.show();
                        List<Material> newList = new ArrayList<>();
                        for (Material p : mListMaster) {
                            Material cloneMat = null;
//                            try {
                            cloneMat = (Material) p.clone();
                            cloneMat.setAmountPaid(0);
//                                cloneMat.setChecked(false);
//                            } catch (CloneNotSupportedException e) {
//                                e.printStackTrace();
//                            }
                            newList.add(cloneMat);
                        }

                        CollectionDetail colLTf = new CollectionDetail();
                        colLTf.setTgl(null);
                        colLTf.setTglCair(null);
                        colLTf.setIdBankCust(null);
                        colLTf.setBankCust(null);
                        colLTf.setIdBankASPP(null);
                        colLTf.setBankNameASPP(null);
                        colLTf.setTotalPayment(0);
                        colLTf.setLeft(0);
                        colLTf.setMaterialList(newList);
                        mListGiro.add(colLTf);

                        mAdapterGiro.notifyDataSetChanged();
                    }

                    public void onFinish() {
                        progress.dismiss();
                        recyclerViewGiro.smoothScrollToPosition(recyclerViewGiro.getAdapter().getItemCount() - 1);
                    }
                }.start();
            }
        });
    }

    private void setChequeView() {
        buttonCheq.setOnClickListener(v -> {
            setSelectView(4);
        });

        mAdapterCheque = new CollectionChequeAdapter(this, mListCheque, header -> {

        });
        recyclerViewCheque.setAdapter(mAdapterCheque);

        btnAddCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        progress.show();
                        List<Material> newList = new ArrayList<>();
                        for (Material p : mListMaster) {
                            Material cloneMat = null;
//                            try {
                            cloneMat = (Material) p.clone();
                            cloneMat.setAmountPaid(0);
//                                cloneMat.setChecked(false);
//                            } catch (CloneNotSupportedException e) {
//                                e.printStackTrace();
//                            }
                            newList.add(cloneMat);
                        }

                        CollectionDetail colLTf = new CollectionDetail();
                        colLTf.setTgl(null);
                        colLTf.setTglCair(null);
                        colLTf.setIdBankCust(null);
                        colLTf.setBankCust(null);
                        colLTf.setIdBankASPP(null);
                        colLTf.setBankNameASPP(null);
                        colLTf.setTotalPayment(0);
                        colLTf.setLeft(0);
                        colLTf.setMaterialList(newList);
                        mListCheque.add(colLTf);

                        mAdapterCheque.notifyDataSetChanged();
                    }

                    public void onFinish() {
                        progress.dismiss();
                        recyclerViewCheque.smoothScrollToPosition(recyclerViewCheque.getAdapter().getItemCount() - 1);
                    }
                }.start();
            }
        });
    }

    private void setLainView() {
        mAdapterLain = new CollectionLainAdapter(this, mListLain, header -> {

        });
        recyclerViewLain.setAdapter(mAdapterLain);

        buttonLain.setOnClickListener(v -> {
            setSelectView(5);
        });

        edtPaymentLain.setOnClickListener(view -> {
            openDialogTotalAmount(2);
        });

//        edtPaymentLain.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Helper.setDotCurrency(edtPaymentLain, this, s);
//                if (!s.toString().equals("") && !s.toString().equals("-")) {
//                    double qty = Double.parseDouble(s.toString().replace(",", ""));
//                    if (qty < 0) {
//                        Toast.makeText(CollectionFormActivity.this, "Tidak boleh kurang dari 0", Toast.LENGTH_SHORT).show();
//                    } else {
//                        totalPaymentLain = qty;
//                    }
//                } else {
//                    totalPaymentLain = 0;
//                }
//                setLeftLain();
//            }
//        });
    }

    private void initData() {
        colLFrom = SessionManagerQubes.getCollectionSource();

        if (colLFrom == 3) {
            mListTransfer = new ArrayList<>();
            mListGiro = new ArrayList<>();
            mListCheque = new ArrayList<>();
            mListCash = new ArrayList<>();
            mListLain = new ArrayList<>();
            mListKredit = new ArrayList<>();

            llOrder.setVisibility(View.VISIBLE);
            llInvoice.setVisibility(View.GONE);
            buttonKredit.setVisibility(View.VISIBLE);
            orderHeader = SessionManagerQubes.getOrder();

//            mListMaster = orderHeader.getMaterialList();
            mListMaster = database.getAllDetailOrder(new Order(orderHeader.getIdHeader()));
            mListCash = database.getAllDetailOrder(new Order(orderHeader.getIdHeader()));
            mListLain = database.getAllDetailOrder(new Order(orderHeader.getIdHeader()));
            mListKredit = database.getAllDetailOrder(new Order(orderHeader.getIdHeader()));

            double paid = 0;
            if (Helper.isNotEmptyOrNull(mListMaster)) {
                paid = orderHeader.getOmzet() - mListMaster.get(0).getTotalAmountPaid();
            } else {
                paid = orderHeader.getOmzet();
            }

//            for (Material obj : mListMaster) {
//                mListCash.add(obj.clone());
//                mListLain.add(obj.clone());
//                mListKredit.add(obj.clone());
//            }

            txtOrderNo.setText(Helper.isEmpty(orderHeader.getIdHeader(), "-"));
            txtAmount.setText("Rp." + format.format(paid));
            totalTagihan = paid;
            if (!Helper.isNullOrEmpty(orderHeader.getOrder_date())) {
                String requestDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, orderHeader.getOrder_date());
                txtDate.setText(requestDate);
            } else {
                txtDate.setText("-");
            }
        } else {
            if (SessionManagerQubes.getCollectionHeader() == null) {
                onBackPressed();
                setToast("Gagal ambil data. Silahkan coba lagi");
            } else {
                mListTransfer = new ArrayList<>();
                mListGiro = new ArrayList<>();
                mListCheque = new ArrayList<>();
                mListCash = new ArrayList<>();
                mListLain = new ArrayList<>();
                mListKredit = new ArrayList<>();


                llOrder.setVisibility(View.GONE);
                llInvoice.setVisibility(View.VISIBLE);
                buttonKredit.setVisibility(View.GONE);

                header = SessionManagerQubes.getCollectionHeader();
                mListMaster = database.getAllInvoiceDetail(header);
                mListCash = database.getAllInvoiceDetail(header);
                mListLain = database.getAllInvoiceDetail(header);
                mListKredit = database.getAllInvoiceDetail(header);

                double paid = 0;
                if (Helper.isNotEmptyOrNull(mListMaster)) {
                    paid = header.getAmount() - mListMaster.get(0).getTotalAmountPaid();
                } else {
                    paid = header.getAmount();
                }

                txtInvNo.setText(Helper.isEmpty(header.getNo_invoice(), "-"));
//                txtAmount.setText("Rp." + format.format(header.getNett()));
                txtAmount.setText("Rp." + format.format(paid));
                totalTagihan = paid;
                if (!Helper.isNullOrEmpty(header.getInvoice_date())) {
                    String requestDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, header.getInvoice_date());
                    txtDate.setText(requestDate);
                } else {
                    txtDate.setText("-");
                }
            }
        }

        setCashView();
        setTransferView();
        setGiroView();
        setChequeView();
        setLainView();
        setKreditView();
    }

    private void setSelectView(int posTab) {
        switch (posTab) {
            case 1:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.VISIBLE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.GONE);
//                mAdapterCash.notifyDataSetChanged();
                break;
            case 2:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.VISIBLE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.GONE);
//                mAdapterTransfer.notifyDataSetChanged();
                break;
            case 3:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.VISIBLE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.GONE);

//                mAdapterGiro.notifyDataSetChanged();
                break;
            case 4:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.VISIBLE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.GONE);
//                mAdapterCheque.notifyDataSetChanged();
                break;
            case 5:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.VISIBLE);
                llKredit.setVisibility(View.GONE);
//                mAdapterLain.notifyDataSetChanged();
                break;
            case 6:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        recyclerViewCash = findViewById(R.id.recyclerViewCash);
        recyclerViewCash.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCash.setHasFixedSize(true);
        recyclerViewKredit = findViewById(R.id.recyclerViewKredit);
        recyclerViewKredit.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewKredit.setHasFixedSize(true);
        recyclerViewTransfer = findViewById(R.id.recyclerViewTransfer);
        recyclerViewTransfer.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTransfer.setHasFixedSize(true);
        recyclerViewGiro = findViewById(R.id.recyclerViewGiro);
        recyclerViewGiro.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGiro.setHasFixedSize(true);
        recyclerViewCheque = findViewById(R.id.recyclerViewCheque);
        recyclerViewCheque.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCheque.setHasFixedSize(true);
        recyclerViewLain = findViewById(R.id.recyclerViewLain);
        recyclerViewLain.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLain.setHasFixedSize(true);
        imgLogOut = findViewById(R.id.imgLogOut);
        imgBack = findViewById(R.id.imgBack);
        txtOrderNo = findViewById(R.id.txtOrderNo);
        txtInvNo = findViewById(R.id.txtInvNo);
        txtAmount = findViewById(R.id.txtAmount);
        txtDate = findViewById(R.id.txtDate);
        txtCash = findViewById(R.id.txtCash);
        txtTransfer = findViewById(R.id.txtTransfer);
        txtGiro = findViewById(R.id.txtGiro);
        txtCheq = findViewById(R.id.txtCheq);
        txtLain = findViewById(R.id.txtLain);
        txtKredit = findViewById(R.id.txtKredit);
        edtPaymentCash = findViewById(R.id.edtPaymentCash);
        txtLeftCash = findViewById(R.id.txtLeftCash);
        btnAddTransfer = findViewById(R.id.btnAddTransfer);
        btnAddGiro = findViewById(R.id.btnAddGiro);
        btnAddCheque = findViewById(R.id.btnAddCheque);
        edtPaymentLain = findViewById(R.id.edtPaymentLain);
        txtLeftLain = findViewById(R.id.txtLeftLain);
        buttonCash = findViewById(R.id.buttonCash);
        buttonTransfer = findViewById(R.id.buttonTransfer);
        buttonCheq = findViewById(R.id.buttonCheq);
        buttonGiro = findViewById(R.id.buttonGiro);
        buttonLain = findViewById(R.id.buttonLain);
        buttonKredit = findViewById(R.id.buttonKredit);
        llLain = findViewById(R.id.llLain);
        llKredit = findViewById(R.id.llKredit);
        llCash = findViewById(R.id.llCash);
        llTransfer = findViewById(R.id.llTransfer);
        llGiro = findViewById(R.id.llGiro);
        llCheque = findViewById(R.id.llCheque);
        llOrder = findViewById(R.id.llOrder);
        llInvoice = findViewById(R.id.llInvoice);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void setLeftCash() {
        double totalPaid = 0;
        for (Material mat : mListCash) {
//            if (mat.isChecked()) {
            totalPaid = totalPaid + mat.getAmountPaid();
//            }
        }
        leftCash = totalPaymentCash - totalPaid;
        txtLeftCash.setText("Rp." + format.format(leftCash));
    }

    public void updateKurangBayarDelete() {
//        type : 1 cash, 2 tf, 3 giro, 4 che, 5 lain

        if (mListCash != null && mListCash.size() != 0) {
            for (int i = 0; i < mListCash.size(); i++) {
                double kurangBayar = 0;
                double paid = 0;
                paid = paid + mListCash.get(i).getAmountPaid();
                if (mListTransfer != null && mListTransfer.size() != 0) {
                    for (CollectionDetail collection : mListTransfer) {
                        paid = paid + collection.getMaterialList().get(i).getAmountPaid();
                    }
                }

                if (mListGiro != null && mListGiro.size() != 0) {
                    for (CollectionDetail collection : mListGiro) {
                        paid = paid + collection.getMaterialList().get(i).getAmountPaid();
                    }
                }

                if (mListCheque != null && mListCheque.size() != 0) {
                    for (CollectionDetail collection : mListCheque) {
                        paid = paid + collection.getMaterialList().get(i).getAmountPaid();
                    }
                }

                if (mListLain != null && mListLain.size() != 0) {
                    paid = paid + mListLain.get(i).getAmountPaid();
                }
                kurangBayar = mListCash.get(i).getPrice() - paid;
                mListCash.get(i).setSisa(kurangBayar);
            }
        }
    }

    public void setKurangBayar(int pos) {
//        type : 1 cash, 2 tf, 3 giro, 4 che, 5 lain
        double kurangBayar = 0;
        double paid = 0;

        if (mListTransfer != null && mListTransfer.size() != 0) {
            for (CollectionDetail collection : mListTransfer) {
                paid = paid + collection.getMaterialList().get(pos).getAmountPaid();
            }
        }

        if (mListGiro != null && mListGiro.size() != 0) {
            for (CollectionDetail collection : mListGiro) {
                paid = paid + collection.getMaterialList().get(pos).getAmountPaid();
            }
        }

        if (mListCheque != null && mListCheque.size() != 0) {
            for (CollectionDetail collection : mListCheque) {
                paid = paid + collection.getMaterialList().get(pos).getAmountPaid();
            }
        }

        if (mListLain != null && mListLain.size() != 0) {
            paid = paid + mListLain.get(pos).getAmountPaid();
        }

        if (mListCash != null && mListCash.size() != 0) {
            paid = paid + mListCash.get(pos).getAmountPaid();

            kurangBayar = mListCash.get(pos).getPrice() - paid;
            mListCash.get(pos).setSisa(kurangBayar);
        }
//
//        switch (type){
//            case 1:
////                mAdapterCash.notifyItemChanged(pos);
//                mAdapterTransfer.updateKurangBayar(pos);
//                mAdapterGiro.updateKurangBayar(pos);
//                mAdapterCheque.updateKurangBayar(pos);
//                mAdapterLain.notifyItemChanged(pos);
//                break;
//            case 2:
//                mAdapterCash.notifyItemChanged(pos);
////                mAdapterTransfer.updateKurangBayar(pos);
//                mAdapterGiro.updateKurangBayar(pos);
//                mAdapterCheque.updateKurangBayar(pos);
//                mAdapterLain.notifyItemChanged(pos);
//                break;
//            case 3:
//                mAdapterCash.notifyItemChanged(pos);
//                mAdapterTransfer.updateKurangBayar(pos);
////                mAdapterGiro.updateKurangBayar(pos);
//                mAdapterCheque.updateKurangBayar(pos);
//                mAdapterLain.notifyItemChanged(pos);
//                break;
//            case 4:
//                mAdapterCash.notifyItemChanged(pos);
//                mAdapterTransfer.updateKurangBayar(pos);
//                mAdapterGiro.updateKurangBayar(pos);
////                mAdapterCheque.updateKurangBayar(pos);
//                mAdapterLain.notifyItemChanged(pos);
//                break;
//            case 5:
//                mAdapterCash.notifyItemChanged(pos);
//                mAdapterTransfer.updateKurangBayar(pos);
//                mAdapterGiro.updateKurangBayar(pos);
//                mAdapterCheque.updateKurangBayar(pos);
////                mAdapterLain.notifyItemChanged(pos);
//                break;
//        }
    }

    public double getSisaPrice(int pos, int type, int idHeader) {
//        type : 1 cash, 2 tf, 3 giro, 4 che, 5 lain
        double kurangBayar = 0;
        double paid = 0;
        CollectionDetail collection;

        if (type != 1) {
            if (mListCash != null && mListCash.size() != 0) {
                paid = paid + mListCash.get(pos).getAmountPaid();
            }
        }

        if (type == 2) {
            if (mListTransfer != null && mListTransfer.size() != 0) {
                for (int i = 0; i < mListTransfer.size(); i++) {
                    collection = mListTransfer.get(i);
                    if (i != idHeader) {
                        paid = paid + collection.getMaterialList().get(pos).getAmountPaid();
                    }
                }
            }
        } else {
            if (mListTransfer != null && mListTransfer.size() != 0) {
                for (int i = 0; i < mListTransfer.size(); i++) {
                    collection = mListTransfer.get(i);
                    paid = paid + collection.getMaterialList().get(pos).getAmountPaid();
                }
            }
        }

        if (type == 3) {
            if (mListGiro != null && mListGiro.size() != 0) {
                for (int i = 0; i < mListGiro.size(); i++) {
                    collection = mListGiro.get(i);
                    if (i != idHeader) {
                        paid = paid + collection.getMaterialList().get(pos).getAmountPaid();
                    }
                }
            }
        } else {
            if (mListGiro != null && mListGiro.size() != 0) {
                for (int i = 0; i < mListGiro.size(); i++) {
                    collection = mListGiro.get(i);
                    paid = paid + collection.getMaterialList().get(pos).getAmountPaid();
                }
            }
        }

        if (type == 4) {
            if (mListCheque != null && mListCheque.size() != 0) {
                for (int i = 0; i < mListCheque.size(); i++) {
                    collection = mListCheque.get(i);
                    if (i != idHeader) {
                        paid = paid + collection.getMaterialList().get(pos).getAmountPaid();
                    }
                }
            }
        } else {
            if (mListCheque != null && mListCheque.size() != 0) {
                for (int i = 0; i < mListCheque.size(); i++) {
                    collection = mListCheque.get(i);
                    paid = paid + collection.getMaterialList().get(pos).getAmountPaid();
                }
            }
        }

        if (type != 5) {
            if (mListLain != null && mListLain.size() != 0) {
                paid = paid + mListLain.get(pos).getAmountPaid();
            }
        }

        if (mListCash != null && mListCash.size() != 0) {
            kurangBayar = mListCash.get(pos).getPrice() - paid;
//            mListCash.get(pos).setSisa(kurangBayar);
        }

        return kurangBayar;
    }

    public double getKurangBayar(int pos) {
        double kurangBayar = 0;
        if (mListCash != null && mListCash.size() != 0) {
            Material mat = mListCash.get(pos);
            kurangBayar = mat.getSisa();
        }
        return kurangBayar;
    }

    public double calculateLeftCash(double qty, int pos) {
        double totalPaid = 0;
        for (int i = 0; i < mListCash.size(); i++) {
            Material mat = mListCash.get(i);
//            if (mat.isChecked()) {
            if (i == pos) {
                totalPaid = totalPaid + qty;
            } else {
                totalPaid = totalPaid + mat.getAmountPaid();
            }
//            }
        }
        leftCash = totalPaymentCash - totalPaid;
        return leftCash;
    }

    public double getTotalAmountCash() {
        return totalPaymentCash;
    }

    public double getTotalAmountLain() {
        return totalPaymentLain;
    }

    public void setLeftLain() {
        double totalPaid = 0;
        for (Material mat : mListLain) {
//            if (mat.isChecked()) {
            totalPaid = totalPaid + mat.getAmountPaid();
//            }
        }

        leftLain = totalPaymentLain - totalPaid;
        txtLeftLain.setText("Rp." + format.format(leftLain));
    }

    public double calculateLeftLain(double qty, int pos) {
        double totalPaid = 0;
        for (int i = 0; i < mListLain.size(); i++) {
            Material mat = mListLain.get(i);
//            if (mat.isChecked()) {
            if (i == pos) {
                totalPaid = totalPaid + qty;
            } else {
                totalPaid = totalPaid + mat.getAmountPaid();
            }
//            }
        }
        leftLain = totalPaymentLain - totalPaid;
        return leftLain;
    }

    public void dialogConfirm() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog(this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDialog = dialog.findViewById(R.id.txtDialog);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        txtTitle.setText("Collection");
        txtDialog.setText("Apakah outlet ini tidak jadi melakukan pembayaran?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(CollectionFormActivity.this, OrderActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        switch (colLFrom) {
            case 2:
                intent = new Intent(this, CollectionVisitActivity.class);
                startActivity(intent);
                break;
            case 3:
                dialogConfirm();
                break;
            case 1:
            default:
                intent = new Intent(this, CollectionActivity.class);
                startActivity(intent);
                break;
        }
    }

    //print
    private void print() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_print);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        RecyclerView mConnectorView = dialog.findViewById(R.id.list);
        SwipeRefreshLayout mSwipeLayout = findViewById(R.id.swipe_container);
        mConnectorView.setLayoutManager(new LinearLayoutManager(this));
        mConnectorView.setHasFixedSize(true);


        dialog.show();
    }
}
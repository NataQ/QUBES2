package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CollectionChequeNewAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionGiroNewAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionInvoiceCashAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionInvoiceLainAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionTransferNewAdapter;
import id.co.qualitas.qubes.adapter.aspp.SpinnerInvoiceAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.RecyclerViewMaxHeight;
import id.co.qualitas.qubes.model.CollectionDetail;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.printer.AbstractConnector;
import id.co.qualitas.qubes.printer.ConnectorAdapter;

public class CollectionFormActivityNew extends BaseActivity {
    double totalPaymentCash, totalPaymentLain, leftCash, leftLain;
    private Button btnSubmit, btnAddCash, btnAddTransfer, btnAddGiro, btnAddCheque, btnAddlain;
    private RecyclerView recyclerViewCash, recyclerViewTransfer, recyclerViewGiro, recyclerViewCheque, recyclerViewLain;
    private TextView edtPaymentCash, edtPaymentLain;
    private TextView txtLeftCash, txtLeftLain;
    private TextView txtInvNo, txtDate, txtAmount, txtOrderNo;
    private LinearLayout llOrder, llInvoice;
    private TextView txtCash, txtTransfer, txtGiro, txtCheq, txtLain;
    private LinearLayout buttonCash, buttonTransfer, buttonCheq, buttonGiro, buttonLain;
    private LinearLayout llCash, llTransfer, llGiro, llCheque, llLain;
    private List<CollectionDetail> mListTransfer, mListGiro, mListCheque;
    private List<Invoice> mListCash, mListLain, mListMaster;
    private WSMessage logResult;
    private Invoice header;
    private CollectionInvoiceCashAdapter mAdapterCash;
    private CollectionTransferNewAdapter mAdapterTransfer;
    private CollectionGiroNewAdapter mAdapterGiro;
    private CollectionChequeNewAdapter mAdapterCheque;
    private CollectionInvoiceLainAdapter mAdapterLain;
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
    RecyclerViewMaxHeight rv;
    private CardView cvUnCheckAll, cvCheckedAll;
    private List<Invoice> listFilteredSpinner, listAdded, listMasterInvoice;
    boolean checkedAll = false;
    private LinearLayoutManager linearLayoutManMaterial;
    private SpinnerInvoiceAdapter invoiceAdapter;
    private String searchInv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_collection_create_payment);

        initialize();

        btnSubmit.setOnClickListener(v -> {
            setToast("Submit");
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CollectionFormActivityNew.this);
        });
    }

    private void setLainView() {
        mAdapterLain = new CollectionInvoiceLainAdapter(this, mListLain, header -> {

        });
        recyclerViewLain.setAdapter(mAdapterLain);

        buttonLain.setOnClickListener(v -> {
            setSelectView(5);
        });

        edtPaymentLain.setOnClickListener(view -> {
            openDialogTotalAmount(2);
        });

        btnAddlain.setOnClickListener(v -> {
            addInvoice(2);
        });
    }

    private void setCashView() {
        mAdapterCash = new CollectionInvoiceCashAdapter(this, mListCash, header -> {

        });
        recyclerViewCash.setAdapter(mAdapterCash);

        buttonCash.setOnClickListener(v -> {
            setSelectView(1);
        });

        edtPaymentCash.setOnClickListener(v -> {
            openDialogTotalAmount(1);
        });

        btnAddCash.setOnClickListener(v -> {
            if (totalPaymentCash != 0) {
                addInvoice(1);
            } else {
                setToast("Masukkan total Payment");
            }
        });
    }

    private void addInvoice(int type) {
        Dialog dialog = new Dialog(CollectionFormActivityNew.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner_product);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        cvUnCheckAll = dialog.findViewById(R.id.cvUnCheckAll);
        cvCheckedAll = dialog.findViewById(R.id.cvCheckedAll);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        EditText editText = dialog.findViewById(R.id.edit_text);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);

        rv = dialog.findViewById(R.id.rv);
        linearLayoutManMaterial = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManMaterial);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        btnSearch.setVisibility(View.GONE);
        txtTitle.setText("Search Invoice");

        listAdded = new ArrayList<>();
        listMasterInvoice = new ArrayList<>();
        listMasterInvoice = database.getAllInvoiceCustomerCollection("", "");
        listAdded = getFilteredInvoice(listMasterInvoice, type);
        invoiceAdapter = new SpinnerInvoiceAdapter(CollectionFormActivityNew.this, listAdded, (nameItem, adapterPosition) -> {
        });
        rv.setAdapter(invoiceAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                invoiceAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cvCheckedAll.setOnClickListener(v -> {
            if (listFilteredSpinner == null) listFilteredSpinner = new ArrayList<>();
            checkedAll = false;
            if (!listFilteredSpinner.isEmpty()) {
                for (Invoice mat : listFilteredSpinner) mat.setCheckedInvoice(checkedAll);
            } else {
                for (Invoice mat : listAdded) mat.setCheckedInvoice(checkedAll);
            }
            invoiceAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.VISIBLE);
            cvCheckedAll.setVisibility(View.GONE);
        });

        cvUnCheckAll.setOnClickListener(v -> {
            if (listFilteredSpinner == null) listFilteredSpinner = new ArrayList<>();
            checkedAll = true;
            if (!listFilteredSpinner.isEmpty()) {
                for (Invoice mat : listFilteredSpinner) mat.setCheckedInvoice(checkedAll);
            } else {
                for (Invoice mat : listAdded) mat.setCheckedInvoice(checkedAll);
            }
            invoiceAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.GONE);
            cvCheckedAll.setVisibility(View.VISIBLE);
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnSave.setOnClickListener(v -> {
            List<Invoice> addList = new ArrayList<>();
            for (Invoice mat : listAdded) {
                if (mat.isCheckedInvoice()) addList.add(mat);
            }
            addNewInvoice(addList, type);
            dialog.dismiss();
        });
    }

    private List<Invoice> getFilteredInvoice(List<Invoice> listMasterInvoice, int type) {
        List<Invoice> filteredInvoice = new ArrayList<>();
        if (type == 1) {
            if (mListCash == null) {
                mListCash = new ArrayList<>();
            }
            for (Invoice detail : listMasterInvoice) {
                boolean exist = false;
                for (Invoice detail2 : mListCash) {
                    if (detail.getNo_invoice().equals(detail2.getNo_invoice())) exist = true;
                }
                if (!exist) filteredInvoice.add(detail);
            }
        } else {
            if (mListLain == null) {
                mListLain = new ArrayList<>();
            }
            for (Invoice detail : listMasterInvoice) {
                boolean exist = false;
                for (Invoice detail2 : mListLain) {
                    if (detail.getNo_invoice().equals(detail2.getNo_invoice())) exist = true;
                }
                if (!exist) filteredInvoice.add(detail);
            }
        }
        return filteredInvoice;
    }

    private void addNewInvoice(List<Invoice> addedList, int type) {
        if (type == 1) {
            if (mListCash == null) {
                mListCash = new ArrayList<>();
            }
            for (Invoice detail : mListCash) {
                for (Invoice detail2 : addedList) {
                    if (detail.getNo_invoice().equals(detail2.getNo_invoice())) {
                        addedList.remove(detail2);
                    }
                }
            }
            mListCash.addAll(addedList);
            mAdapterCash.setData(mListCash);
        } else {
            if (mListLain == null) {
                mListLain = new ArrayList<>();
            }
            for (Invoice detail : mListLain) {
                for (Invoice detail2 : addedList) {
                    if (detail.getNo_invoice().equals(detail2.getNo_invoice())) {
                        addedList.remove(detail2);
                    }
                }
            }
            mListLain.addAll(addedList);
            mAdapterLain.setData(mListLain);
        }

//        new CountDownTimer(1000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                progress.show();
//                if (type == 1) {
//                    mAdapterCash.notifyDataSetChanged();
//                } else {
//                    mAdapterLain.notifyDataSetChanged();
//                }
//            }
//
//            public void onFinish() {
//                progress.dismiss();
//                if (type == 1) {
//                    recyclerViewCash.smoothScrollToPosition(recyclerViewCash.getAdapter().getItemCount() - 1);
//                } else {
//                    recyclerViewLain.smoothScrollToPosition(recyclerViewLain.getAdapter().getItemCount() - 1);
//                }
//            }
//        }.start();
    }

    public void setCheckedAll() {
        int checked = 0;
        for (Invoice mat : listAdded) {
            if (mat.isCheckedInvoice()) {
                checked++;
            }
        }
        if (checked == listAdded.size()) {
            checkedAll = true;
            invoiceAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.GONE);
            cvCheckedAll.setVisibility(View.VISIBLE);
        } else {
            checkedAll = false;
            invoiceAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.VISIBLE);
            cvCheckedAll.setVisibility(View.GONE);
        }
    }

    public void setFilteredData(List<Invoice> mFilteredList) {
        listFilteredSpinner = new ArrayList<>();
        listFilteredSpinner.addAll(mFilteredList);

        int checked = 0;
        for (Invoice mat : listFilteredSpinner) {
            if (mat.isCheckedInvoice()) {
                checked++;
            }
        }
        if (checked == listFilteredSpinner.size()) {
            checkedAll = true;
            invoiceAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.GONE);
            cvCheckedAll.setVisibility(View.VISIBLE);
        } else {
            checkedAll = false;
            invoiceAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.VISIBLE);
            cvCheckedAll.setVisibility(View.GONE);
        }
    }

    private void setTransferView() {
        buttonTransfer.setOnClickListener(v -> {
            setSelectView(2);
        });

        mAdapterTransfer = new CollectionTransferNewAdapter(this, mListTransfer, header -> {

        });
        recyclerViewTransfer.setAdapter(mAdapterTransfer);

        btnAddTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListTransfer == null) mListTransfer = new ArrayList<>();
                CollectionDetail colLTf = new CollectionDetail();
                colLTf.setTgl(null);
                colLTf.setTotalPayment(0);
                colLTf.setLeft(0);
                colLTf.setInvoiceList(new ArrayList<>());
                mListTransfer.add(colLTf);

                mAdapterTransfer.setData(mListTransfer);
//                new CountDownTimer(1000, 1000) {
//
//                    public void onTick(long millisUntilFinished) {
//                        progress.show();
//
//                    }
//
//                    public void onFinish() {
//                        progress.dismiss();
//                        recyclerViewTransfer.smoothScrollToPosition(recyclerViewTransfer.getAdapter().getItemCount() - 1);
//                    }
//                }.start();
            }
        });
    }

    private void setGiroView() {
        buttonGiro.setOnClickListener(v -> {
            setSelectView(3);
        });

        mAdapterGiro = new CollectionGiroNewAdapter(this, mListGiro, header -> {

        });
        recyclerViewGiro.setAdapter(mAdapterGiro);

        btnAddGiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListGiro == null) mListGiro = new ArrayList<>();
                CollectionDetail colLTf = new CollectionDetail();
                colLTf.setTgl(null);
                colLTf.setTglCair(null);
                colLTf.setIdBankCust(null);
                colLTf.setBankCust(null);
                colLTf.setIdBankASPP(null);
                colLTf.setBankNameASPP(null);
                colLTf.setTotalPayment(0);
                colLTf.setLeft(0);
                colLTf.setInvoiceList(new ArrayList<>());
                mListGiro.add(colLTf);
                mAdapterGiro.setData(mListGiro);
//                new CountDownTimer(1000, 1000) {
//                    public void onTick(long millisUntilFinished) {
//                        progress.show();
//
//
//                        mAdapterGiro.notifyDataSetChanged();
//                    }
//
//                    public void onFinish() {
//                        progress.dismiss();
//                        recyclerViewGiro.smoothScrollToPosition(recyclerViewGiro.getAdapter().getItemCount() - 1);
//                    }
//                }.start();
            }
        });
    }

    private void setChequeView() {
        buttonCheq.setOnClickListener(v -> {
            setSelectView(4);
        });

        mAdapterCheque = new CollectionChequeNewAdapter(this, mListCheque, header -> {

        });
        recyclerViewCheque.setAdapter(mAdapterCheque);

        btnAddCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListCheque == null) mListCheque = new ArrayList<>();
                CollectionDetail colLTf = new CollectionDetail();
                colLTf.setTgl(null);
                colLTf.setTglCair(null);
                colLTf.setIdBankCust(null);
                colLTf.setBankCust(null);
                colLTf.setIdBankASPP(null);
                colLTf.setBankNameASPP(null);
                colLTf.setTotalPayment(0);
                colLTf.setLeft(0);
                colLTf.setMaterialList(new ArrayList<>());
                mListCheque.add(colLTf);
                mAdapterCheque.setData(mListCheque);
//                new CountDownTimer(1000, 1000) {
//                    public void onTick(long millisUntilFinished) {
//                        progress.show();
//
//
//                        mAdapterCheque.notifyDataSetChanged();
//                    }
//
//                    public void onFinish() {
//                        progress.dismiss();
//                        recyclerViewCheque.smoothScrollToPosition(recyclerViewCheque.getAdapter().getItemCount() - 1);
//                    }
//                }.start();
            }
        });
    }


    private void initData() {
        setCashView();
        setTransferView();
        setGiroView();
        setChequeView();
        setLainView();
    }

    private void setSelectView(int posTab) {
        switch (posTab) {
            case 1:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.VISIBLE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                break;
            case 2:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.VISIBLE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                break;
            case 3:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.VISIBLE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                break;
            case 4:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.VISIBLE);
                llLain.setVisibility(View.GONE);
                break;
            case 5:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        recyclerViewCash = findViewById(R.id.recyclerViewCash);
        recyclerViewCash.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCash.setHasFixedSize(true);
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
        edtPaymentCash = findViewById(R.id.edtPaymentCash);
        txtLeftCash = findViewById(R.id.txtLeftCash);
        btnAddCash = findViewById(R.id.btnAddCash);
        btnAddlain = findViewById(R.id.btnAddlain);
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
        llLain = findViewById(R.id.llLain);
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

    private void openDialogTotalAmount(int typePayment) {
        LayoutInflater inflater = LayoutInflater.from(CollectionFormActivityNew.this);
        final Dialog dialog = new Dialog(CollectionFormActivityNew.this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_amount_total, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        TextView txtTypePayment = dialog.findViewById(R.id.txtTypePayment);
        EditText edtTotalPayment = dialog.findViewById(R.id.edtTotalPayment);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        if (typePayment == 1) {
            txtTypePayment.setText("Cash");
            edtTotalPayment.setText(totalPaymentCash != 0 ? Helper.setDotCurrencyAmount(totalPaymentCash) : null);
        } else {
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
                    edtPaymentCash.setText("Rp." + format.format(qty));
                    totalPaymentCash = qty;
                    mListCash = new ArrayList<>();
                    mAdapterCash.setData(mListCash);
                    setLeftCash();
                } else {
                    edtPaymentLain.setText("Rp." + format.format(qty));
                    totalPaymentLain = qty;
                    mListLain = new ArrayList<>();
                    mAdapterLain.setData(mListLain);
                    setLeftLain();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void setLeftCash() {
        double totalPaid = 0;
        for (Invoice detail : mListCash) {
            totalPaid = totalPaid + detail.getTotalPayment();
        }
        leftCash = totalPaymentCash - totalPaid;
        txtLeftCash.setText("Rp." + format.format(leftCash));
    }

    public void setLeftLain() {
        double totalPaid = 0;
        for (Invoice detail : mListLain) {
            totalPaid = totalPaid + detail.getTotalPayment();
        }
        leftLain = totalPaymentLain - totalPaid;
        txtLeftLain.setText("Rp." + format.format(leftLain));
    }

    public double getKurangBayar(Invoice invoice, Material material, int type) {
        double kurangBayar = 0, paidAmount = 0;

        if (Helper.isNotEmptyOrNull(mListCash)) {
            Invoice inv = mListCash.stream()
                    .filter(i -> i.getNo_invoice().equals(invoice.getNo_invoice()))
                    .findFirst()
                    .orElse(null);

            Material mat = inv.getMaterialList().stream()
                    .filter(i -> i.getId().equals(material.getId()))
                    .findFirst()
                    .orElse(null);

            paidAmount = paidAmount + mat.getAmountPaid();
        }

        if (Helper.isNotEmptyOrNull(mListLain)) {
            Invoice inv = mListLain.stream()
                    .filter(i -> i.getNo_invoice().equals(invoice.getNo_invoice()))
                    .findFirst()
                    .orElse(null);

            Material mat = inv.getMaterialList().stream()
                    .filter(i -> i.getId().equals(material.getId()))
                    .findFirst()
                    .orElse(null);

            paidAmount = paidAmount + mat.getAmountPaid();
        }

        if (Helper.isNotEmptyOrNull(mListTransfer)) {
            for (int i = 0; i < mListTransfer.size(); i++) {
                CollectionDetail detail = mListTransfer.get(i);
                Invoice inv = detail.getInvoiceList().stream()
                        .filter(j -> j.getNo_invoice().equals(invoice.getNo_invoice()))
                        .findFirst()
                        .orElse(null);

                Material mat = inv.getMaterialList().stream()
                        .filter(j -> j.getId().equals(material.getId()))
                        .findFirst()
                        .orElse(null);

                paidAmount = paidAmount + mat.getAmountPaid();
            }
        }

        if (Helper.isNotEmptyOrNull(mListGiro)) {
            for (int i = 0; i < mListGiro.size(); i++) {
                CollectionDetail detail = mListGiro.get(i);
                Invoice inv = detail.getInvoiceList().stream()
                        .filter(j -> j.getNo_invoice().equals(invoice.getNo_invoice()))
                        .findFirst()
                        .orElse(null);

                Material mat = inv.getMaterialList().stream()
                        .filter(j -> j.getId().equals(material.getId()))
                        .findFirst()
                        .orElse(null);

                paidAmount = paidAmount + mat.getAmountPaid();
            }
        }

        if (Helper.isNotEmptyOrNull(mListCheque)) {
            for (int i = 0; i < mListCheque.size(); i++) {
                CollectionDetail detail = mListCheque.get(i);
                Invoice inv = detail.getInvoiceList().stream()
                        .filter(j -> j.getNo_invoice().equals(invoice.getNo_invoice()))
                        .findFirst()
                        .orElse(null);

                Material mat = inv.getMaterialList().stream()
                        .filter(j -> j.getId().equals(material.getId()))
                        .findFirst()
                        .orElse(null);

                paidAmount = paidAmount + mat.getAmountPaid();
            }
        }
        kurangBayar = material.getNett() - paidAmount;
        return kurangBayar;
    }

    public double getTotalAmount(int type) {
        switch (type) {
            case 1:
                return totalPaymentCash;
            case 2:
                return totalPaymentLain;
            default:
                return 0;
        }
    }
}
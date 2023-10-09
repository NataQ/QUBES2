package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CollectionCashAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionChequeAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionGiroAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionKreditAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionLainAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionTransferAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.CollectionCheque;
import id.co.qualitas.qubes.model.CollectionGiro;
import id.co.qualitas.qubes.model.CollectionTransfer;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.printer.AbstractConnector;
import id.co.qualitas.qubes.printer.ConnectorActivity;
import id.co.qualitas.qubes.printer.ConnectorAdapter;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class CollectionFormActivity extends BaseActivity {
    double totalPaymentCash, totalPaymentLain, leftCash, leftLain;
    private Button btnSubmit, btnAddTransfer, btnAddGiro, btnAddCheque;
    private RecyclerView recyclerViewCash, recyclerViewTransfer, recyclerViewGiro, recyclerViewCheque, recyclerViewLain, recyclerViewKredit;
    private EditText edtPaymentCash, edtPaymentLain;
    private TextView txtLeftCash, txtLeftLain;
    private TextView txtInvNo, txtDate, txtAmount, txtOrderNo;
    private LinearLayout llOrder, llInvoice;
    private TextView txtCash, txtTransfer, txtGiro, txtCheq, txtLain, txtKredit;
    private LinearLayout buttonCash, buttonTransfer, buttonCheq, buttonGiro, buttonLain, buttonKredit;
    private LinearLayout llCash, llTransfer, llGiro, llCheque, llLain, llKredit;
    private List<CollectionTransfer> mListTransfer;
    private List<CollectionGiro> mListGiro;
    private List<CollectionCheque> mListCheque;
    private List<Material> mListCash, mListLain, mListKredit, mListMaster;
    private Invoice header;
    private CollectionKreditAdapter mAdapterKredit;
    private CollectionCashAdapter mAdapterCash;
    private CollectionTransferAdapter mAdapterTransfer;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_collection_form);

        initialize();

        btnSubmit.setOnClickListener(v -> {
            if (colLFrom == 3) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH);
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN);
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_BLUETOOTH_CONNECT);
                    }
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_BLUETOOTH_SCAN);
                    }

                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                } else {
                    intent = new Intent(CollectionFormActivity.this, ConnectorActivity.class);
                    startActivity(intent);
                }
            } else {
                validate();
                onBackPressed();
            }
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CollectionFormActivity.this);
        });
    }

    private void validate() {
        List<Material> cashList = new ArrayList<>();
        List<Material> tfList = new ArrayList<>();
        List<Material> giroList = new ArrayList<>();
        List<Material> chequeList = new ArrayList<>();
        List<Material> lainList = new ArrayList<>();

        for (Material material: mListCash){
            if(material.isChecked() && material.getAmountPaid() != 0){
                cashList.add(material);
            }
        }

        for(CollectionTransfer collection : mListTransfer) {
            for (Material material : collection.getMaterialList()) {
                if (material.isChecked() && material.getAmountPaid() != 0) {
                    tfList.add(material);
                }
            }
            collection.setCheckedMaterialList(tfList);
        }

        for(CollectionGiro collection : mListGiro) {
            for (Material material : collection.getMaterialList()) {
                if (material.isChecked() && material.getAmountPaid() != 0) {
                    giroList.add(material);
                }
            }
            collection.setCheckedMaterialList(giroList);
        }

        for(CollectionCheque collection : mListCheque) {
            for (Material material : collection.getMaterialList()) {
                if (material.isChecked() && material.getAmountPaid() != 0) {
                    chequeList.add(material);
                }
            }
            collection.setCheckedMaterialList(chequeList);
        }

        for (Material material: mListLain){
            if(material.isChecked() && material.getAmountPaid() != 0){
                lainList.add(material);
            }
        }
    }

    private void setCashView() {
        mAdapterCash = new CollectionCashAdapter(this, mListCash, header -> {

        });
        recyclerViewCash.setAdapter(mAdapterCash);

        buttonCash.setOnClickListener(v -> {
            setSelectView(1);
        });

        edtPaymentCash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Helper.setDotCurrency(edtPaymentCash, this, s);
                if (!s.toString().equals("") && !s.toString().equals("-")) {
                    double qty = Double.parseDouble(s.toString().replace(",", ""));
                    if (qty < 0) {
                        Toast.makeText(CollectionFormActivity.this, "Tidak boleh kurang dari 0", Toast.LENGTH_SHORT).show();
                    } else {
                        totalPaymentCash = qty;
                    }
                } else {
                    totalPaymentCash = 0;
                }
                setLeftCash();
            }
        });
    }

    private void setKreditView() {
        mAdapterKredit = new CollectionKreditAdapter(this, mListKredit, header -> {

        });
        recyclerViewKredit.setAdapter(mAdapterKredit);

        buttonKredit.setOnClickListener(v -> {
            setSelectView(6);
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
                            try {
                                cloneMat = (Material) p.clone();
                                cloneMat.setAmountPaid(0);
                                cloneMat.setChecked(false);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            newList.add(cloneMat);
                        }

                        CollectionTransfer colLTf = new CollectionTransfer();
                        colLTf.setTglTransfer(null);
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
                            try {
                                cloneMat = (Material) p.clone();
                                cloneMat.setAmountPaid(0);
                                cloneMat.setChecked(false);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            newList.add(cloneMat);
                        }

                        CollectionGiro colLTf = new CollectionGiro();
                        colLTf.setTglGiro(null);
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
                            try {
                                cloneMat = (Material) p.clone();
                                cloneMat.setAmountPaid(0);
                                cloneMat.setChecked(false);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            newList.add(cloneMat);
                        }

                        CollectionCheque colLTf = new CollectionCheque();
                        colLTf.setTglCheque(null);
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

        edtPaymentLain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Helper.setDotCurrency(edtPaymentLain, this, s);
                if (!s.toString().equals("") && !s.toString().equals("-")) {
                    double qty = Double.parseDouble(s.toString().replace(",", ""));
                    if (qty < 0) {
                        Toast.makeText(CollectionFormActivity.this, "Tidak boleh kurang dari 0", Toast.LENGTH_SHORT).show();
                    } else {
                        totalPaymentLain = qty;
                    }
                } else {
                    totalPaymentLain = 0;
                }
                setLeftLain();
            }
        });
    }

    private void initData() {
        if (SessionManagerQubes.getCollectionHeader() == null) {
            onBackPressed();
            setToast("Gagal ambil data. Silahkan coba lagi");
        } else {
            header = SessionManagerQubes.getCollectionHeader();
            colLFrom = SessionManagerQubes.getCollectionSource();

            mListTransfer = new ArrayList<>();
            mListGiro = new ArrayList<>();
            mListCheque = new ArrayList<>();

            if (colLFrom == 3) {
                llOrder.setVisibility(View.VISIBLE);
                llInvoice.setVisibility(View.GONE);
                buttonKredit.setVisibility(View.VISIBLE);
            } else {
                llOrder.setVisibility(View.GONE);
                llInvoice.setVisibility(View.VISIBLE);
                buttonKredit.setVisibility(View.GONE);
            }

            mListMaster = database.getAllInvoiceDetail(header.getIdHeader());
            mListCash = database.getAllInvoiceDetail(header.getIdHeader());
            mListLain = database.getAllInvoiceDetail(header.getIdHeader());
            mListKredit = database.getAllInvoiceDetail(header.getIdHeader());

            txtInvNo.setText(Helper.isEmpty(header.getInvoiceNo(), "-"));
            txtAmount.setText("Rp." + format.format(header.getAmount()));
            if (!Helper.isNullOrEmpty(header.getInvoiceDate())) {
                String requestDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, header.getInvoiceDate());
                txtDate.setText(requestDate);
            } else {
                txtDate.setText("-");
            }

            setCashView();
            setTransferView();
            setGiroView();
            setChequeView();
            setLainView();
            setKreditView();

//            Material clone = null;
//            try  {
//                clone = (Material) super.clone();
//                //Copy new date object to cloned method
//                clone.setDob((Date) this.getDob().clone());
//            }
//            catch (CloneNotSupportedException e)  {
//                throw new RuntimeException(e);
//            }
        }
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
        db = new DatabaseHelper(this);
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
            if (mat.isChecked()) {
                totalPaid = totalPaid + mat.getAmountPaid();
            }
        }

        leftCash = totalPaymentCash - totalPaid;
        txtLeftCash.setText("Rp." + format.format(leftCash));
    }

    public double calculateLeftCash(double qty, int pos) {
        double totalPaid = 0;
        for (int i = 0; i < mListCash.size(); i++) {
            Material mat = mListCash.get(i);
            if (mat.isChecked()) {
                if (i == pos) {
                    totalPaid = totalPaid + qty;
                } else {
                    totalPaid = totalPaid + mat.getAmountPaid();
                }
            }
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
            if (mat.isChecked()) {
                totalPaid = totalPaid + mat.getAmountPaid();
            }
        }

        leftLain = totalPaymentLain - totalPaid;
        txtLeftLain.setText("Rp." + format.format(leftLain));
    }

    public double calculateLeftLain(double qty, int pos) {
        double totalPaid = 0;
        for (int i = 0; i < mListLain.size(); i++) {
            Material mat = mListLain.get(i);
            if (mat.isChecked()) {
                if (i == pos) {
                    totalPaid = totalPaid + qty;
                } else {
                    totalPaid = totalPaid + mat.getAmountPaid();
                }
            }
        }
        leftLain = totalPaymentLain - totalPaid;
        return leftLain;
    }
}
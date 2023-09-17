package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CollectionCashAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionChequeAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionGiroAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionKreditAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionLainAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionTransferAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAdapter;
import id.co.qualitas.qubes.adapter.aspp.StockRequestAddAdapter;
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
import id.co.qualitas.qubes.printer.BluetoothLeConnector;
import id.co.qualitas.qubes.printer.BluetoothSppConnector;
import id.co.qualitas.qubes.printer.ConnectorActivity;
import id.co.qualitas.qubes.printer.ConnectorAdapter;
import id.co.qualitas.qubes.printer.NetworkConnector;
import id.co.qualitas.qubes.printer.UsbDeviceConnector;

public class CollectionFormActivity extends BaseActivity {
    private ImageView imgBack;
    private Button btnSubmit, btnAddTransfer, btnAddGiro, btnAddCheque;
    private RecyclerView recyclerViewCash, recyclerViewTransfer, recyclerViewGiro, recyclerViewCheque, recyclerViewLain, recyclerViewKredit;
    private TextView txtPaymentCash, txtLeftCash, txtAmount;
    private TextView txtPaymentLain, txtLeftLain;
    private TextView txtInvNo, txtDate, txtOrderNo;
    private LinearLayout llOrder, llInvoice;
    private TextView txtCash, txtTransfer, txtGiro, txtCheq, txtLain, txtKredit;
    private LinearLayout buttonCash, buttonTransfer, buttonCheq, buttonGiro, buttonLain, buttonKredit;
    private LinearLayout llCash, llTransfer, llGiro, llCheque, llLain, llKredit;
    private List<CollectionTransfer> mListTransfer;
    private List<CollectionGiro> mListGiro;
    private List<CollectionCheque> mListCheque;
    private List<Material> mListCash, mListLain, mListKredit;
    private Invoice collectionHeader;
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

        init();
        initialize();
        initData();
        setCashView();
        setTransferView();
        setGiroView();
        setChequeView();
        setLainView();
        setKreditView();

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
                onBackPressed();
            }
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CollectionFormActivity.this);
        });

        buttonCash.setOnClickListener(v -> {
            setSelectView(1);
        });

        buttonTransfer.setOnClickListener(v -> {
            setSelectView(2);
        });

        buttonGiro.setOnClickListener(v -> {
            setSelectView(3);
        });

        buttonCheq.setOnClickListener(v -> {
            setSelectView(4);
        });

        buttonLain.setOnClickListener(v -> {
            setSelectView(5);
        });

        buttonKredit.setOnClickListener(v -> {
            setSelectView(6);
        });
    }

    private void setCashView() {
        mListCash = new ArrayList<>();
        mListCash.addAll(initDataMaterial());

        mAdapterCash = new CollectionCashAdapter(this, mListCash, header -> {

        });
        recyclerViewCash.setAdapter(mAdapterCash);
    }

    private void setKreditView() {
        mListKredit = new ArrayList<>();
        mListKredit.addAll(initDataMaterial());

        mAdapterKredit = new CollectionKreditAdapter(this, mListKredit, header -> {

        });
        recyclerViewKredit.setAdapter(mAdapterKredit);
    }

    private void setTransferView() {
        mAdapterTransfer = new CollectionTransferAdapter(this, mListTransfer, header -> {

        });
        recyclerViewTransfer.setAdapter(mAdapterTransfer);

        btnAddTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionTransfer detail = new CollectionTransfer(null, initDataMaterial());
                mListTransfer.add(detail);

                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        progress.show();
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
        mAdapterGiro = new CollectionGiroAdapter(this, mListGiro, header -> {

        });
        recyclerViewGiro.setAdapter(mAdapterGiro);

        btnAddGiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionGiro detail = new CollectionGiro(null, initDataMaterial());
                mListGiro.add(detail);

                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        progress.show();
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
        mAdapterCheque = new CollectionChequeAdapter(this, mListCheque, header -> {

        });
        recyclerViewCheque.setAdapter(mAdapterCheque);

        btnAddCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionCheque detail = new CollectionCheque(null, initDataMaterial());
                mListCheque.add(detail);

                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        progress.show();
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
        mListLain = new ArrayList<>();
        mListLain.addAll(initDataMaterial());

        mAdapterLain = new CollectionLainAdapter(this, mListLain, header -> {

        });
        recyclerViewLain.setAdapter(mAdapterLain);
    }

    private List<Material> initDataMaterial() {
        List<Material> mList = new ArrayList<>();
        mList.add(new Material("11001", "Kratingdaeng", "1,000,000"));
        mList.add(new Material("11030", "Redbull", "2,000,000"));
        mList.add(new Material("31020", "You C1000 Vitamin Orange", "8,900,000"));
        return mList;
    }

    private void initData() {
        mListTransfer = new ArrayList<>();
        mListGiro = new ArrayList<>();
        mListCheque = new ArrayList<>();

        colLFrom = Helper.getItemParam(Constants.COLLECTION_FROM) != null ? (int) Helper.getItemParam(Constants.COLLECTION_FROM) : 0;

        if (colLFrom == 3) {
            llOrder.setVisibility(View.VISIBLE);
            llInvoice.setVisibility(View.GONE);
            buttonKredit.setVisibility(View.VISIBLE);
        } else {
            llOrder.setVisibility(View.GONE);
            llInvoice.setVisibility(View.VISIBLE);
            buttonKredit.setVisibility(View.GONE);
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
        collectionHeader = (Invoice) Helper.getItemParam(Constants.COLLECTION_HEADER);

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
        txtPaymentCash = findViewById(R.id.txtPaymentCash);
        txtLeftCash = findViewById(R.id.txtLeftCash);
        btnAddTransfer = findViewById(R.id.btnAddTransfer);
        btnAddGiro = findViewById(R.id.btnAddGiro);
        btnAddCheque = findViewById(R.id.btnAddCheque);
        txtPaymentLain = findViewById(R.id.txtPaymentLain);
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
    }

    //printer
//    public void openDialogSearchPrinter() {
//        LayoutInflater inflater = LayoutInflater.from(CollectionFormActivity.this);
//        final Dialog dialog = new Dialog(CollectionFormActivity.this);
//        View dialogView = inflater.inflate(R.layout.aspp_dialog_connector, null);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(dialogView);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        mSwipeLayout = dialog.findViewById(R.id.swipe_container);
//        mConnectorView = dialog.findViewById(R.id.list);
//        Button btnCancel = dialog.findViewById(R.id.btnCancel);
//
//        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        mConnectorList = new ArrayList<>();
//        mConnectorAdapter = new ConnectorAdapter(CollectionFormActivity.this, mConnectorList, this);
//        mConnectorView.setAdapter(mConnectorAdapter);
//
//        ItemTouchHelper.Callback callback = new ConnectorSwipeHelper(mConnectorAdapter);
//        ItemTouchHelper helper = new ItemTouchHelper(callback);
//        helper.attachToRecyclerView(mConnectorView);
//
//        assert mSwipeLayout != null;
//        mSwipeLayout.setOnRefreshListener(this);
//        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//
//        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        bluetoothFilter.addAction(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mBluetoothReceiver, bluetoothFilter);
//        initConnector();
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }
//
//    public void initConnector() {
//        mConnectorAdapter.clear();
//
//        // Enumerate all network devices.
//        Set<String> hostList = mPreferences.getStringSet(PREF_HOST_LIST, new HashSet<String>());
//        for (String url : hostList) {
//            int delimiter = url.indexOf(":");
//            String host = url.substring(0, delimiter > 0 ? delimiter : url.length());
//            int port = Integer.parseInt(url.substring(delimiter > 0 ? delimiter + 1 : 0));
//            AbstractConnector connector = new NetworkConnector(this, host, port);
//            mConnectorAdapter.addLast(connector);
//            mConnectorView.smoothScrollToPosition(0);
//        }
//
//        // Enumerate USB devices
//        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        if (manager != null) {
//            HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
//
//            for (UsbDevice device : deviceList.values()) {
//                if (manager.hasPermission(device)) {
//                    AbstractConnector connector = new UsbDeviceConnector(this, manager, device);
//                    mConnectorAdapter.addLast(connector);
//                    mConnectorView.smoothScrollToPosition(0);
//                }
//            }
//        }
//
//        // Enumerate Bluetooth devices
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//        if (adapter != null && adapter.isEnabled()) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            Set<BluetoothDevice> boundedDevices = adapter.getBondedDevices();
//
//            for (BluetoothDevice device : boundedDevices) {
//                AbstractConnector connector;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                    if (device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
//                        connector = new BluetoothLeConnector(this, adapter, device);
//                    } else {
//                        connector = new BluetoothSppConnector(this, adapter, device);
//                    }
//                } else {
//                    connector = new BluetoothSppConnector(this, adapter, device);
//                }
//                mConnectorAdapter.addLast(connector);
//                mConnectorView.smoothScrollToPosition(0);
//            }
//        }
//
////        String searchPattern = mSearchView.getText().toString();
////        mConnectorAdapter.applySearchPattern(searchPattern);
////        mConnectorAdapter.notifyDataSetChanged();
//    }
//
//    private void enableBluetooth() {
//        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//
//        if (adapter != null && !adapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }
//    }
}
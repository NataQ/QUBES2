package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.OrderDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.SummaryDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.printer.ConnectorActivity;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class OrderDetailActivity extends BaseActivity {
    private OrderDetailAdapter mAdapter;
    private List<Material> mList;
    private TextView txtOrderNo, txtDate, txtStatus, txtOmzet;
    private LinearLayout llStatus, llNoData;
    private Button btnPrint;
    private Order orderHeader;
    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;
    private static final int REQUEST_LOCATION_PERMISSION = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order_detail);

        initialize();
        initData();

        btnPrint.setOnClickListener(v -> {
            if (orderHeader.getPrintOrder() < database.getMaxPrint()) {
                openDialogConfirmation();
            } else {
                setToast("Sudah mencapai maksimal print");
            }
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(OrderDetailActivity.this);
        });
    }

    private void openDialogConfirmation() {
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
        txtTitle.setText("Print");
        txtDialog.setText("Print again?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                checkBluetooth();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog.show();
    }

    private void checkBluetooth() {
        if (ContextCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN);
        } else if (ContextCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_BLUETOOTH_CONNECT);
            }
        } else if (ContextCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_BLUETOOTH_SCAN);
            }
        } else if (ContextCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            intent = new Intent(OrderDetailActivity.this, ConnectorActivity.class);
            startActivity(intent);
        }
    }

    private void initData() {
        orderHeader = SessionManagerQubes.getOrder();
        if (orderHeader == null) {
            onBackPressed();
        } else {
            if (!Helper.isNullOrEmpty(orderHeader.getOrder_date())) {
                String requestDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, orderHeader.getOrder_date());
                txtDate.setText(requestDate);
            } else {
                txtDate.setText("");
            }
            txtOrderNo.setText(orderHeader.getIdHeader() + " - " + format.format(orderHeader.getId()));
            txtOmzet.setText("Rp. " + format.format(orderHeader.getOmzet()));
//            txtStatus.setText(!Helper.isEmpty(orderHeader.getStatus()) ? orderHeader.getStatus() : "-");

            if (!Helper.isCanvasSales(user)) {
                btnPrint.setVisibility(View.GONE);
            } else {
                btnPrint.setVisibility(View.VISIBLE);
            }

            if (!Helper.isEmpty(orderHeader.getStatus())) {
                switch (orderHeader.getStatus()) {
                    case Constants.STATUS_APPROVE:
                        llStatus.setVisibility(View.VISIBLE);
                        llStatus.setBackgroundTintList(ContextCompat.getColorStateList(OrderDetailActivity.this, R.color.green3_aspp));
                        txtStatus.setText("Approve");
                        txtStatus.setTextColor(ContextCompat.getColor(OrderDetailActivity.this, R.color.green_aspp));
                        break;
                    case Constants.STATUS_REJECTED:
                        txtStatus.setText("Reject");
                        llStatus.setVisibility(View.VISIBLE);
                        llStatus.setBackgroundTintList(ContextCompat.getColorStateList(OrderDetailActivity.this, R.color.red_aspp));
                        txtStatus.setTextColor(ContextCompat.getColor(OrderDetailActivity.this, R.color.red2_aspp));
                        break;
                    case Constants.STATUS_PENDING:
                        txtStatus.setText("Pending");
                        llStatus.setVisibility(View.VISIBLE);
                        llStatus.setBackgroundTintList(ContextCompat.getColorStateList(OrderDetailActivity.this, R.color.yellow3_aspp));
                        txtStatus.setTextColor(ContextCompat.getColor(OrderDetailActivity.this, R.color.yellow_aspp));
                        break;
                    case Constants.STATUS_SYNC_SUCCESS:
                        txtStatus.setText("Sync Success");
                        llStatus.setVisibility(View.VISIBLE);
                        llStatus.setBackgroundTintList(ContextCompat.getColorStateList(OrderDetailActivity.this, R.color.blue8_aspp));
                        txtStatus.setTextColor(ContextCompat.getColor(OrderDetailActivity.this, R.color.aspp_blue9));
                        break;
                    case Constants.STATUS_DRAFT:
                        txtStatus.setText("Draft");
                        llStatus.setVisibility(View.VISIBLE);
                        llStatus.setBackgroundTintList(ContextCompat.getColorStateList(OrderDetailActivity.this, R.color.gray12_aspp));
                        txtStatus.setTextColor(ContextCompat.getColor(OrderDetailActivity.this, R.color.black1_aspp));
                        break;
                    default:
                        llStatus.setVisibility(View.GONE);
                        llStatus.setBackgroundTintList(null);
                        txtStatus.setText("-");
                }
            } else {
                llStatus.setVisibility(View.GONE);
                llStatus.setBackgroundTintList(null);
                txtStatus.setText("-");
            }

            mList = new ArrayList<>();
            mList.addAll(database.getAllDetailOrder(orderHeader));

            mAdapter = new OrderDetailAdapter(this, mList, header -> {
            });

            recyclerView.setAdapter(mAdapter);
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        btnPrint = findViewById(R.id.btnPrint);
        llNoData = findViewById(R.id.llNoData);
        txtOmzet = findViewById(R.id.txtOmzet);
        txtStatus = findViewById(R.id.txtStatus);
        llStatus = findViewById(R.id.llStatus);
        txtDate = findViewById(R.id.txtDate);
        txtOrderNo = findViewById(R.id.txtOrderNo);
        imgLogOut = findViewById(R.id.imgLogOut);
        imgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
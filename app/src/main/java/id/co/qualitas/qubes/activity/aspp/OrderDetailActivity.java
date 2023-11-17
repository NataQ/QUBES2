package id.co.qualitas.qubes.activity.aspp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class OrderDetailActivity extends BaseActivity {
    private OrderDetailAdapter mAdapter;
    private List<Material> mList;
    private TextView txtOrderNo, txtDate,txtStatus, txtOmzet;
    private LinearLayout llStatus, llNoData;
    private Order orderHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order_detail);

        initialize();
        initData();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(OrderDetailActivity.this);
        });
    }

    private void initData() {
        orderHeader = SessionManagerQubes.getOrder();
        if(orderHeader == null){
            onBackPressed();
        }else {
            if (!Helper.isNullOrEmpty(orderHeader.getOrder_date())) {
                String requestDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, orderHeader.getOrder_date());
                txtDate.setText(requestDate);
            } else {
                txtDate.setText("");
            }
            txtOrderNo.setText(format.format(orderHeader.getId()));
            txtOmzet.setText("Rp. " + format.format(orderHeader.getOmzet()));
            txtStatus.setText(!Helper.isEmpty(orderHeader.getStatus()) ? orderHeader.getStatus() : "-");

            if (!Helper.isEmpty(orderHeader.getStatus())) {
                switch (orderHeader.getStatus().toLowerCase()) {
                    case "approve":
                        llStatus.setVisibility(View.VISIBLE);
                        llStatus.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.green3_aspp));
                        txtStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green_aspp));
                        break;
                    case "reject":
                        llStatus.setVisibility(View.VISIBLE);
                        llStatus.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.red_aspp));
                        txtStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red2_aspp));
                        break;
                    case "pending":
                        llStatus.setVisibility(View.VISIBLE);
                        llStatus.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.yellow3_aspp));
                        txtStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow_aspp));
                        break;
                    case "sync success":
                        llStatus.setVisibility(View.VISIBLE);
                        llStatus.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.blue8_aspp));
                        txtStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.aspp_blue9));
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
package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoDctOutletAdapter;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoOutstandingFakturAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;

public class OrderSummaryActivity extends BaseActivity {
    private ImageView imgBack;
    private Button btnPay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order_summary);

        initProgress();
        initialize();
        setData();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnPay.setOnClickListener(v -> {
            Intent intent = new Intent(this, CollectionActivity.class);
            startActivity(intent);
        });
    }

    private void setData() {

    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgBack = findViewById(R.id.imgBack);
        btnPay = findViewById(R.id.btnPay);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
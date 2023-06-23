package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.OrderAdapter;
import id.co.qualitas.qubes.adapter.aspp.StoreCheckAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;

public class OrderActivity extends BaseActivity {
    private OrderAdapter mAdapter;
    private List<Order> mList;
    private MovableFloatingActionButton btnAdd;
    private ImageView imgBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order);

        init();
        initialize();
        initData();

        mAdapter = new OrderAdapter(this, mList, header -> {
            Intent intent = new Intent(this, OrderAddActivity.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(mAdapter);

        btnAdd.setOnClickListener(y -> {
            Intent intent = new Intent(this, OrderAddActivity.class);
            startActivity(intent);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Order("ORDER000001", "1,000,000", "0WJ42 - SARI SARI (TK)", "SO0000001", "Draft", "1 June 2023"));
        mList.add(new Order("ORDER000002", "2,000,000", "0WJ42 - SARI SARI (TK)", "SO0000002", "Draft", "2 June 2023"));
        mList.add(new Order("ORDER000003", "3,000,000", "0WJ42 - SARI SARI (TK)", "SO0000003", "Draft", "3 June 2023"));
        mList.add(new Order("ORDER000004", "4,000,000", "0WJ42 - SARI SARI (TK)", "SO0000004", "Draft", "4 June 2023"));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgBack = findViewById(R.id.imgBack);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
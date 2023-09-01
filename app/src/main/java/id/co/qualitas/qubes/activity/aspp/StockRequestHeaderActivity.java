package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.StockRequestHeaderAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;

public class StockRequestHeaderActivity extends BaseActivity {
    private StockRequestHeaderAdapter mAdapter;
    private List<StockRequest> mList;
    private Button btnAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_stock_request_header);

        init();
        initialize();
        initData();

        mAdapter = new StockRequestHeaderAdapter(this, mList, header -> {
            Intent intent = new Intent(this, StockRequestDetailActivity.class);
            startActivity(intent);
        });
        
        recyclerView.setAdapter(mAdapter);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, StockRequestAddActivity.class);
            startActivity(intent);
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(StockRequestHeaderActivity.this);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new StockRequest("1 May 2023", "2023050101000010001", "1 May 2023", "Approve"));
        mList.add(new StockRequest("2 May 2023", "2023050201000010001", "2 May 2023", "Pending"));
        mList.add(new StockRequest("3 May 2023", "2023050301000010001", "3 May 2023", "Rejected"));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
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
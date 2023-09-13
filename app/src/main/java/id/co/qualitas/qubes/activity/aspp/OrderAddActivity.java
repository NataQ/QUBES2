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
import id.co.qualitas.qubes.adapter.aspp.OrderAddAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;

public class OrderAddActivity extends BaseActivity {
    private OrderAddAdapter mAdapter;
    private List<Material> mList;
    private Button btnAdd, btnNext, btnGetDiscount;
    private TextView txtDate, txtOmzet;
    private ImageView imgBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order_add);

        init();
        initialize();
        initData();

        mAdapter = new OrderAddAdapter(this, mList, header -> {

        });

        recyclerView.setAdapter(mAdapter);

        btnAdd.setOnClickListener(v -> {
            addNew();
        });

        btnNext.setOnClickListener(v -> {
            onBackPressed();
            Helper.setItemParam(Constants.COLLECTION_FROM, 3);
            Intent intent = new Intent(this, CollectionFormActivity.class);
            startActivity(intent);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(OrderAddActivity.this);
        });
    }

    private void addNew() {
        Material detail = new Material("", "", "", "");
        mList.add(detail);
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                progress.show();
                mAdapter.notifyDataSetChanged();
            }

            public void onFinish() {
                progress.dismiss();
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        }.start();
    }

    private void initData() {
        mList = new ArrayList<>();
        txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_1));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtOmzet = findViewById(R.id.txtOmzet);
        txtDate = findViewById(R.id.txtDate);
        btnGetDiscount = findViewById(R.id.btnGetDiscount);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnNext = findViewById(R.id.btnNext);
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
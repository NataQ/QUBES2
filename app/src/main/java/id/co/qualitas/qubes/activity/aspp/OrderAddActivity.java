package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;

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
    private MovableFloatingActionButton btnAdd;
    private Button btnNext, btnGetDiscount;
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
            Intent intent = new Intent(this, OrderSummaryActivity.class);
            startActivity(intent);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
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
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        btnGetDiscount = findViewById(R.id.btnGetDiscount);
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

    public void delete(Material detail, int pos) {
        mList.remove(pos);
        mAdapter.notifyItemRemoved(pos);
    }
}
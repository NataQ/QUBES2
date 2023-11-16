package id.co.qualitas.qubes.activity.aspp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

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
    private List<Material> mList, mListExtra;
    private ImageView imgBack;
    private Order header;

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
        header = SessionManagerQubes.getOrder();
        if(header == null){
            onBackPressed();
        }else {
            mList = new ArrayList<>();
            mList.addAll(database.getAllDetailOrder(header));

            mAdapter = new OrderDetailAdapter(this, mList, header -> {
            });

            recyclerView.setAdapter(mAdapter);
        }
header nya belum
                diskon listnya masih 1 doank
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

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
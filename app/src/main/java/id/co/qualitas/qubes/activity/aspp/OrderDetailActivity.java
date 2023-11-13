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
import id.co.qualitas.qubes.model.User;

public class OrderDetailActivity extends BaseActivity {
    private OrderDetailAdapter mAdapter;
    private List<Material> mList, mListExtra;
    private ImageView imgBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order_detail);

        initProgress();
        initialize();
        initData();

        mAdapter = new OrderDetailAdapter(this, mList, header -> {
        });

        recyclerView.setAdapter(mAdapter);

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(OrderDetailActivity.this);
        });
    }

    private void initData() {
        mList = new ArrayList<>();
//        mList.add(new Material("11_KTD R", "11008_KRATINGDAENG LUAR PULAU - MT", 1, 1000, "BTL", initDataExtra()));
//        mList.add(new Material("11_KTD R", "11007_KRATINGDAENG - MT", 1, 2000, "BTL", initDataExtra()));
//        mList.add(new Material("11_KTD R", "11006_KRATINGDAENG - LAIN-LAIN", 1, 3000, "BTL", initDataExtra()));
//        mList.add(new Material("11_KTD R", "11005_KRATINGDAENG LUAR PULAU", 1, 4000, "BTL", initDataExtra()));
    }

    private List<Material> initDataExtra() {
        mListExtra = new ArrayList<>();
//        mListExtra.add(new Material("11_KTD R", "11007_KRATINGDAENG - MT", 1, 2000, "BTL"));
//        mListExtra.add(new Material("11_KTD R", "11005_KRATINGDAENG LUAR PULAU", 1, 4000, "BTL"));
        return mListExtra;
    }

    private void initialize() {
        db = new DatabaseHelper(this);
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
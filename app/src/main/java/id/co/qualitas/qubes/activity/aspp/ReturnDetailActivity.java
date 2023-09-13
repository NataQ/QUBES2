package id.co.qualitas.qubes.activity.aspp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.ReturnDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.SummaryDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;

public class ReturnDetailActivity extends BaseActivity {
    private ReturnDetailAdapter mAdapter;
    private List<Material> mList;
    private ImageView imgBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_return_detail);

        init();
        initialize();
        initData();

        mAdapter = new ReturnDetailAdapter(this, mList, header -> {
        });

        recyclerView.setAdapter(mAdapter);

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Material("11_KTD R", "11008_KRATINGDAENG LUAR PULAU - MT", 1, "1,000", "BTL"));
        mList.add(new Material("11_KTD R", "11007_KRATINGDAENG - MT", 1, "2,000", "BTL"));
        mList.add(new Material("11_KTD R", "11006_KRATINGDAENG - LAIN-LAIN", 1, "3,000", "BTL"));
        mList.add(new Material("11_KTD R", "11005_KRATINGDAENG LUAR PULAU", 1, "4,000", "BTL"));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

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
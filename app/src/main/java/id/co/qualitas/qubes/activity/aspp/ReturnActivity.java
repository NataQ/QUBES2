package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.ReturnAdapter;
import id.co.qualitas.qubes.adapter.aspp.ReturnAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.User;

public class ReturnActivity extends BaseActivity {
    private ReturnAdapter mAdapter;
    private List<Return> mList;
    private MovableFloatingActionButton btnAdd;
    private ImageView imgBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_return);

        init();
        initialize();
        initData();

        mAdapter = new ReturnAdapter(this, mList, header -> {
            Intent intent = new Intent(this, ReturnDetailActivity.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(mAdapter);

        btnAdd.setOnClickListener(y -> {
            Intent intent = new Intent(this, ReturnAddActivity.class);
            startActivity(intent);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Return("RE00001", "1 June 2023"));
        mList.add(new Return("RE00002", "2 June 2023"));
        mList.add(new Return("RE00003", "3 June 2023"));
        mList.add(new Return("RE00004", "4 June 2023"));
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
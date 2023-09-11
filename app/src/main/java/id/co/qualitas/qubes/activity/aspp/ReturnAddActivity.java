package id.co.qualitas.qubes.activity.aspp;

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
import id.co.qualitas.qubes.adapter.aspp.ReturnAddAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;

public class ReturnAddActivity extends BaseActivity {
    private ReturnAddAdapter mAdapter;
    private List<Material> mList;
    private Button btnAdd, btnSave;
    private TextView txtReturnDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_return_add);

        init();
        initialize();
        initData();

        mAdapter = new ReturnAddAdapter(this, mList, header -> {
        });

        recyclerView.setAdapter(mAdapter);

        btnAdd.setOnClickListener(v -> {
            addNew();
        });

        btnSave.setOnClickListener(v -> {
            onBackPressed();
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(ReturnAddActivity.this);
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
        txtReturnDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_4));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtReturnDate = findViewById(R.id.txtReturnDate);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSave = findViewById(R.id.btnSave);
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
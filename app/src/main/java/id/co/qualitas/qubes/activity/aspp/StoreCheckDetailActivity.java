package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.SpinnerProductStoreCheckAdapter;
import id.co.qualitas.qubes.adapter.aspp.StoreCheckAdapter;
import id.co.qualitas.qubes.adapter.aspp.StoreCheckDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class StoreCheckDetailActivity extends BaseActivity {
    private StoreCheckDetailAdapter mAdapter;
    private List<Material> mList;
    private Button btnAdd;
    private TextView txtDate;
    private String today;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_store_check);

        initialize();
        initData();

        mAdapter = new StoreCheckDetailAdapter(this, mList, header -> {

        });
        recyclerView.setAdapter(mAdapter);

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(StoreCheckDetailActivity.this);
        });

    }

    private void initData() {
        today = Helper.getTodayDate(Constants.DATE_FORMAT_3);

        mList = new ArrayList<>();
        mList.addAll(database.getAllStoreCheck(SessionManagerQubes.getOutletHeader().getId()));
        if (mList != null && mList.size() != 0) {
            today = mList.get(0).getDate();
            txtDate.setText(Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, today));
        } else {
            txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        }

        btnAdd.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgLogOut = findViewById(R.id.imgLogOut);
        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgBack);
        txtDate = findViewById(R.id.txtDate);
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
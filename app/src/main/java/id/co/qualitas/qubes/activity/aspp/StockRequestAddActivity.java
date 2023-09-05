package id.co.qualitas.qubes.activity.aspp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.StockRequestAddAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;

public class StockRequestAddActivity extends BaseActivity {
    private StockRequestAddAdapter mAdapter;
    private List<Material> mList;
    private Button btnAdd;
    private TextView txtDate;
    Date fromDate;
    String fromDateString, paramFromDate;
    Calendar todayDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_stock_request_add);

        init();
        initialize();
        initData();

        mAdapter = new StockRequestAddAdapter(this, mList, header -> {

        });
        recyclerView.setAdapter(mAdapter);

        btnAdd.setOnClickListener(y -> {
            addNew();
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnSave.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(StockRequestAddActivity.this);
        });

        setDate();
    }

    private void setDate() {
        fromDate = Helper.getTodayDate();
        paramFromDate = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(fromDate);
        txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                todayDate = Calendar.getInstance();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(fromDate);
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int date = calendar.get(Calendar.DATE);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DATE, dayOfMonth);

                        fromDate = calendar.getTime();
                        fromDateString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(calendar.getTime());
                        paramFromDate = new SimpleDateFormat(Constants.DATE_FORMAT_3).format(calendar.getTime());
                        txtDate.setText(fromDateString);
                        txtDate.setError(null);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(StockRequestAddActivity.this, R.style.DialogTheme, dateSetListener, year, month, date);
                dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getDatePicker().setLayoutParams(params);
                dialog.show();
            }
        });
    }

    private void initData() {
        mList = new ArrayList<>();
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

    private void initialize() {
        db = new DatabaseHelper(this);
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
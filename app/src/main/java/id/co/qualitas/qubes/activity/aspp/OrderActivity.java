package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.OrderAdapter;
import id.co.qualitas.qubes.adapter.aspp.StoreCheckAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class OrderActivity extends BaseActivity {
    private OrderAdapter mAdapter;
    private List<Order> mList;
    private Button btnAdd;
    private Customer outletHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order);

        initialize();

        btnAdd.setOnClickListener(y -> {
            SessionManagerQubes.clearOrderSession();
            Intent intent = new Intent(this, OrderAddActivity.class);
            startActivity(intent);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(OrderActivity.this);
        });

        swipeLayout.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void setAdapter() {
        mAdapter = new OrderAdapter(this, mList, header -> {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtNoData = findViewById(R.id.txtNoData);
        imgLogOut = findViewById(R.id.imgLogOut);
        imgBack = findViewById(R.id.imgBack);
        btnAdd = findViewById(R.id.btnAdd);
        progressCircle = findViewById(R.id.progressCircle);
        swipeLayout = findViewById(R.id.swipeLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFirstDataOffline();
    }

    private void getFirstDataOffline() {
        outletHeader = SessionManagerQubes.getOutletHeader();
        requestData();
    }

    private void requestData() {
        recyclerView.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        new RequestUrl().execute();
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllOrder(outletHeader);
        setAdapter();
    }

    private class RequestUrl extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                mList = new ArrayList<>();
                getData();
                return true;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("Order", ex.getMessage());
                }
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressCircle.setVisibility(View.GONE);
            if (Helper.isNotEmptyOrNull(mList)) {
                recyclerView.setVisibility(View.VISIBLE);
                txtNoData.setVisibility(View.GONE);
                mAdapter.setData(mList);
            } else {
                txtNoData.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DailySalesmanActivity.class);
        startActivity(intent);
    }
}
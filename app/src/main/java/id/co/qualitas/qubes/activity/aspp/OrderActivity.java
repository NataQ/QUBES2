package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.OrderAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;
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
            validasi kalau uda ke sync gak bisa save lagi?
            if (!header.isStatusPaid()) {
                dialogConfirm(header);
            } else {
                SessionManagerQubes.setOrder(header);
                Intent intent = new Intent(this, OrderDetailActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = findViewById(R.id.llNoData);
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
        setAdapter();
        if (Helper.isEmptyOrNull(mList)) requestData();
    }

    private void requestData() {
        llNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        new RequestUrl().execute();
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllOrder(outletHeader);
    }

    private class RequestUrl extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
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
            if (Helper.isEmptyOrNull(mList)) {
                recyclerView.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
            } else {
                mAdapter.setData(mList);
                recyclerView.setVisibility(View.VISIBLE);
                llNoData.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(this, DailySalesmanActivity.class);
        startActivity(intent);
    }

    public void dialogConfirm(Order header) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog(this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDialog = dialog.findViewById(R.id.txtDialog);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        txtTitle.setText("Collection");
        txtDialog.setText("Apakah outlet ini akan melakukan pembayaran?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SessionManagerQubes.setOrder(header);
                SessionManagerQubes.setCollectionSource(3);
                Intent intent = new Intent(OrderActivity.this, CollectionFormActivity.class);
                startActivity(intent);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SessionManagerQubes.setOrder(header);
                Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }
}
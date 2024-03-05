package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.InvoiceDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.StockRequestDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class InvoiceDetailActivity extends BaseActivity {
    private InvoiceDetailAdapter mAdapter;
    private TextView txtInvoiceDate, txtInvoiceNo, txtTglJatuhTempo,
            txtCustomer, txtInvoiceTotal, txtInvoicePaid, txtNett;
    private Invoice header;
    private List<Material> mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_invoice_detail);

        initialize();

        imgLogOut.setOnClickListener(v -> {
            logOut(InvoiceDetailActivity.this);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

//        swipeLayout.setColorSchemeResources(R.color.blue_aspp,
//                R.color.green_aspp,
//                R.color.yellow_krang,
//                R.color.red_krang);
//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                setAdapter();
//                swipeLayout.setRefreshing(false);
//            }
//        });
    }

    private void initData() {
        header = SessionManagerQubes.getCollectionHeader();
        if (header == null) {
            onBackPressed();
            setToast(getString(R.string.failedGetData));
        } else {
            setFormatSeparator();
            String idCustomer = Helper.isEmpty(header.getId_customer(), "");
            String nameCustomer = Helper.isEmpty(header.getNama(), "");
            txtCustomer.setText(idCustomer + "-" + nameCustomer);
            txtInvoiceNo.setText(Helper.isEmpty(header.getNo_invoice(), "-"));
            txtInvoiceTotal.setText("Rp." + format.format(header.getAmount()));
            txtInvoicePaid.setText("Rp." + format.format(header.getTotal_paid()));
            txtNett.setText("Rp." + format.format(header.getNett()));

            if (!Helper.isNullOrEmpty(header.getInvoice_date())) {
                String requestDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, header.getInvoice_date());
                txtInvoiceDate.setText(requestDate);
            } else {
                txtInvoiceDate.setText("-");
            }

            if (!Helper.isNullOrEmpty(header.getTanggal_jatuh_tempo())) {
                String tglKirim = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, header.getTanggal_jatuh_tempo());
                txtTglJatuhTempo.setText(tglKirim);
            } else {
                txtTglJatuhTempo.setText("-");
            }

            mList = database.getAllInvoiceDetail(header);
            mAdapter = new InvoiceDetailAdapter(this, mList, header -> {
            });
            recyclerView.setAdapter(mAdapter);
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = findViewById(R.id.llNoData);
        txtCustomer = findViewById(R.id.txtCustomer);
        txtTglJatuhTempo = findViewById(R.id.txtTglJatuhTempo);
        txtInvoiceNo = findViewById(R.id.txtInvoiceNo);
        txtInvoiceDate = findViewById(R.id.txtInvoiceDate);
        txtInvoiceTotal = findViewById(R.id.txtInvoiceTotal);
        txtNett = findViewById(R.id.txtNett);
        txtInvoicePaid = findViewById(R.id.txtInvoicePaid);
        swipeLayout = findViewById(R.id.swipeLayout);
        progressCircle = findViewById(R.id.progressCircle);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
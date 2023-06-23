package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.InvoiceVerificationAdapter;
import id.co.qualitas.qubes.adapter.aspp.InvoiceVerificationAddAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.User;

public class InvoiceVerificationAddActivity extends BaseActivity {
    private InvoiceVerificationAddAdapter mAdapter;
    private List<Invoice> mList;
    private Button btnSubmit;
    private EditText edtSearch;
    private RelativeLayout layoutCheck;
    private ImageView imgChecked;
    private MovableFloatingActionButton btnSync;
    private boolean checkAll = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_invoice_verification_add);

        init();
        initialize();
        initData();

        mAdapter = new InvoiceVerificationAddAdapter(this, mList, header -> {
        });

        recyclerView.setAdapter(mAdapter);

        btnSync.setOnClickListener(v -> {
            mAdapter.notifyDataSetChanged();
        });

        btnSubmit.setOnClickListener(v -> {
            onBackPressed();
        });

        layoutCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheck();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setCheck() {
        for (Invoice data : mList) {
            if (checkAll) {
                data.setChecked(false);
            } else {
                data.setChecked(true);
            }
        }
        mAdapter.notifyDataSetChanged();
        setCheckAll();
    }

    public void setCheckAll() {
        int sizeCheck = 0, sizeVisible = 0;
        for (Invoice data : mList) {
            if (data.isChecked()) {
                sizeCheck++;
            }
        }
        if (sizeCheck == mList.size()) {
            imgChecked.setVisibility(View.VISIBLE);
            checkAll = true;
        } else {
            imgChecked.setVisibility(View.GONE);
            checkAll = false;
        }
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Invoice("DKA495486", "TOKO SIDIK HALIM", "0GV43", 884736, 0, "1 June 2023", "Route"));
        mList.add(new Invoice("DKA496933", "TOKO SIDIK HALIM", "0GV43", 39294, 0, "2 June 2023", "Route"));
        mList.add(new Invoice("DKA492540", "SARI SARI (TK)", "0WJ42", 14024448, 0, "3 June 2023", "Non Route"));
        mList.add(new Invoice("DKA402541", "TOKO SIDIK HALIM", "0WJ42", 9363600, 0, "4 June 2023", "Non Route"));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgChecked = findViewById(R.id.imgChecked);
        layoutCheck = findViewById(R.id.layoutCheck);
        edtSearch = findViewById(R.id.edtSearch);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSync = findViewById(R.id.btnSync);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
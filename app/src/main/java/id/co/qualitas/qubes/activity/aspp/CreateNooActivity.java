package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.StockRequestHeaderAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;

public class CreateNooActivity extends BaseActivity {
    private ImageView imgBack;
    private Button btnSave;
    private AutoCompleteTextView txtSuku;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_create_noo);

        init();
        initialize();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CreateNooActivity.this);
        });

        btnSave.setOnClickListener(v -> {
            onBackPressed();
        });
        setDropDown();
    }

    private void setDropDown() {
        List<String> suku = new ArrayList<>();
        suku.add("Tiong Hua");
        suku.add("Indonesia");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CreateNooActivity.this, android.R.layout.simple_dropdown_item_1line, suku);
        txtSuku.setAdapter(arrayAdapter);
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtSuku = findViewById(R.id.txtSuku);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSave = findViewById(R.id.btnSave);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
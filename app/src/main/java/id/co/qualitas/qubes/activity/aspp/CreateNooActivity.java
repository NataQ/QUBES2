package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
    private Spinner spnSuku, spnStatusToko, spnStatusNpwp;
    private LinearLayout llKTP, llNPWP, llOutlet;

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
        suku.add("--");
        suku.add("Tiong Hua");
        suku.add("Indonesia");

        List<String> statusToko = new ArrayList<>();
        statusToko.add("--");
        statusToko.add("Milik Sendiri");
        statusToko.add("Sewa");

        List<String> statusNPWP = new ArrayList<>();
        statusNPWP.add("--");
        statusNPWP.add("PKP");
        statusNPWP.add("Non PKP");

        setSpinnerData(suku, spnSuku);
        setSpinnerData(statusToko, spnStatusToko);
        setSpinnerData(statusNPWP, spnStatusNpwp);
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        spnStatusNpwp = findViewById(R.id.spnStatusNpwp);
        spnSuku = findViewById(R.id.spnSuku);
        spnStatusToko = findViewById(R.id.spnStatusToko);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSave = findViewById(R.id.btnSave);
        llKTP = findViewById(R.id.llKTP);
        llNPWP = findViewById(R.id.llNPWP);
        llOutlet = findViewById(R.id.llOutlet);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
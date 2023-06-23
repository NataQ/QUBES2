package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoDctOutletAdapter;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoOutstandingFakturAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;

public class CustomerInfoActivity extends BaseActivity {
    private ImageView imgBack;
    private RecyclerView rvOutstandingFaktur, rvDctOutlet;
    private CustomerInfoOutstandingFakturAdapter fakturAdapter;
    private CustomerInfoDctOutletAdapter dctOutletAdapter;
    private List<Material> fakturList;
    private List<Material> dctOutletList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_customer_info);

        init();
        initialize();
        setData();

        fakturAdapter = new CustomerInfoOutstandingFakturAdapter(this, fakturList, header -> {
        });

        rvOutstandingFaktur.setAdapter(fakturAdapter);

        dctOutletAdapter = new CustomerInfoDctOutletAdapter(this, dctOutletList, header -> {
        });

        rvDctOutlet.setAdapter(dctOutletAdapter);

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setData() {
        fakturList = new ArrayList<>();
        fakturList.add(new Material("Drink", 1));
        fakturList.add(new Material("Redbull", 0));
        fakturList.add(new Material("Drink UC", 1));
        fakturList.add(new Material("Battery", 1));

        dctOutletList = new ArrayList<>();
        dctOutletList.add(new Material("Kratingdaeng", 1));
        dctOutletList.add(new Material("Redbull", 0));
        dctOutletList.add(new Material("Vitamin", 0));
        dctOutletList.add(new Material("Water", 0));
        dctOutletList.add(new Material("Bat CZ", 0));
        dctOutletList.add(new Material("bat ALK", 0));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgBack = findViewById(R.id.imgBack);
        rvOutstandingFaktur = findViewById(R.id.rvOutstandingFaktur);
        rvOutstandingFaktur.setLayoutManager(new LinearLayoutManager(this));
        rvOutstandingFaktur.setHasFixedSize(true);
        rvDctOutlet = findViewById(R.id.rvDctOutlet);
        rvDctOutlet.setLayoutManager(new LinearLayoutManager(this));
        rvDctOutlet.setHasFixedSize(true);

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
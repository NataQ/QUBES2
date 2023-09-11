package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoDctOutletAdapter;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoOutstandingFakturAdapter;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoPromoAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.User;

public class DailySalesmanActivity extends BaseActivity {
    private TextView txtOutlet, txtTypeOutlet;
    private TextView txtNamaPemilik, txtPhone, txtSisaKreditLimit, txtTotalTagihan, txtKTP, txtNPWP;
    private Chronometer timerValue;
    private Button btnCheckOut;
    private LinearLayout llPause, llStoreCheck, llOrder, llCollection, llReturn;
    private LinearLayout llKTP, llNPWP, llOutlet;
    private ImageView imgKTP, imgNPWP, imgOutlet;
    private RecyclerView rvPromo, rvOutstandingFaktur, rvDCTOutlet;

    private CustomerInfoPromoAdapter promoAdapter;
    private CustomerInfoOutstandingFakturAdapter fakturAdapter;
    private CustomerInfoDctOutletAdapter dctOutletAdapter;

    private List<Material> fakturList;
    private List<Material> dctOutletList;
    private List<Promotion> promoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_daily_salesman);

        init();
        initialize();
        setData();
        setView();

        btnCheckOut.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(this);

            alertDialog.setContentView(R.layout.aspp_dialog_reason);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);

            List<String> groupList = new ArrayList<>();
            groupList.add("01 - Stock Masih Cukup");
            groupList.add("02 - Beli dari luar");
            groupList.add("03 - Tukar Faktur");
            groupList.add("04 - Toko Tutup");
            groupList.add("05 - Harga Tidak Cocok");

            txtTitle.setText("Reason Not Buy");

            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(this, groupList, (nameItem, adapterPosition) -> {
                alertDialog.dismiss();
            });

            LinearLayoutManager mManager = new LinearLayoutManager(this);
            listView.setLayoutManager(mManager);
            listView.setHasFixedSize(true);
            listView.setNestedScrollingEnabled(false);
            listView.setAdapter(spinnerAdapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    spinnerAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        });

        llPause.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(this);

            alertDialog.setContentView(R.layout.aspp_dialog_reason);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);

            List<String> groupList = new ArrayList<>();
            groupList.add("P1 - Toko Ramai");
            groupList.add("P2 - Toko Tutup");
            groupList.add("P3 - Toko Pindah");

            txtTitle.setText("Reason Pause");

            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(this, groupList, (nameItem, adapterPosition) -> {
//                spnBankTransfer.setText(nameItem);
                alertDialog.dismiss();
            });

            LinearLayoutManager mManager = new LinearLayoutManager(this);
            listView.setLayoutManager(mManager);
            listView.setHasFixedSize(true);
            listView.setNestedScrollingEnabled(false);
            listView.setAdapter(spinnerAdapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    spinnerAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        });

        llStoreCheck.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoreCheckActivity.class);
            startActivity(intent);
        });

        llOrder.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
        });

        llCollection.setOnClickListener(v -> {
            Intent intent = new Intent(this, CollectionVisitActivity.class);
            startActivity(intent);
        });

        llReturn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReturnAddActivity.class);
            startActivity(intent);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(DailySalesmanActivity.this);
        });
    }

    private void setView() {
        fakturAdapter = new CustomerInfoOutstandingFakturAdapter(this, fakturList, header -> {
        });

        rvOutstandingFaktur.setAdapter(fakturAdapter);

        dctOutletAdapter = new CustomerInfoDctOutletAdapter(this, dctOutletList, header -> {
        });

        rvDCTOutlet.setAdapter(dctOutletAdapter);

        promoAdapter = new CustomerInfoPromoAdapter(this, promoList, header -> {
        });

        rvPromo.setAdapter(promoAdapter);
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
        dctOutletList.add(new Material("Vitamin", 2));
        dctOutletList.add(new Material("Water", 0));
        dctOutletList.add(new Material("Bat CZ", 1));
        dctOutletList.add(new Material("Bat ALK", 0));

        promoList = new ArrayList<>();
        promoList.add(new Promotion("Beli 4 Kratingdaeng get discount 10%"));
        promoList.add(new Promotion("Beli kratingdaeng bisa nonton bola di Madrid"));
        promoList.add(new Promotion("Dapatkan voucher Buy 1 Get 1 untuk variant Kratingdaeng Bull"));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        rvDCTOutlet = findViewById(R.id.rvDCTOutlet);
        rvDCTOutlet.setLayoutManager(new LinearLayoutManager(this));
        rvDCTOutlet.setHasFixedSize(true);
        rvOutstandingFaktur = findViewById(R.id.rvOutstandingFaktur);
        rvOutstandingFaktur.setLayoutManager(new LinearLayoutManager(this));
        rvOutstandingFaktur.setHasFixedSize(true);
        rvPromo = findViewById(R.id.rvPromo);
        rvPromo.setLayoutManager(new LinearLayoutManager(this));
        rvPromo.setHasFixedSize(true);
        imgOutlet = findViewById(R.id.imgOutlet);
        llOutlet = findViewById(R.id.llOutlet);
        imgNPWP = findViewById(R.id.imgNPWP);
        llNPWP = findViewById(R.id.llNPWP);
        imgKTP = findViewById(R.id.imgKTP);
        llKTP = findViewById(R.id.llKTP);
        txtNPWP = findViewById(R.id.txtNPWP);
        txtKTP = findViewById(R.id.txtKTP);
        txtTotalTagihan = findViewById(R.id.txtTotalTagihan);
        txtSisaKreditLimit = findViewById(R.id.txtSisaKreditLimit);
        txtPhone = findViewById(R.id.txtPhone);
        txtNamaPemilik = findViewById(R.id.txtNamaPemilik);
        timerValue = findViewById(R.id.timerValue);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        txtTypeOutlet = findViewById(R.id.txtTypeOutlet);
        txtOutlet = findViewById(R.id.txtOutlet);
        llPause = findViewById(R.id.llPause);
        imgBack = findViewById(R.id.imgBack);
        llStoreCheck = findViewById(R.id.llStoreCheck);
        llOrder = findViewById(R.id.llOrder);
        llCollection = findViewById(R.id.llCollection);
        llReturn = findViewById(R.id.llReturn);
        imgLogOut = findViewById(R.id.imgLogOut);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
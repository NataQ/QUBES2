package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.User;

public class DailySalesmanActivity extends BaseActivity {
    private ImageView imgBack, imgPause, imgStop;
    private LinearLayout llCustomerInfo, llStoreCheck, llOrder, llCollection, llReturn, llReasonNotVisit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_daily_salesman);

        init();
        initialize();

        imgStop.setOnClickListener(v -> {
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

        imgPause.setOnClickListener(v -> {
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

        llCustomerInfo.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerInfoActivity.class);
            startActivity(intent);
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

        llReasonNotVisit.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(this);

            alertDialog.setContentView(R.layout.aspp_dialog_reason);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);

            List<String> groupList = new ArrayList<>();
            groupList.add("N1 - Waktu Habis");
            groupList.add("N2 - Pindah");
            groupList.add("N3 - Banjir");
            groupList.add("N4 - Tidak Ketemu");
            groupList.add("N5 - Tutup");

            txtTitle.setText("Reason Not Visit");

            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(this, groupList, (nameItem, adapterPosition) -> {
                alertDialog.dismiss();

                Intent intent = new Intent(this, InstructionPhotoActivity.class);
                startActivity(intent);
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

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgPause = findViewById(R.id.imgPause);
        imgStop = findViewById(R.id.imgStop);
        imgBack = findViewById(R.id.imgBack);
        llReasonNotVisit = findViewById(R.id.llReasonNotVisit);
        llCustomerInfo = findViewById(R.id.llCustomerInfo);
        llStoreCheck = findViewById(R.id.llStoreCheck);
        llOrder = findViewById(R.id.llOrder);
        llCollection = findViewById(R.id.llCollection);
        llReturn = findViewById(R.id.llReturn);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
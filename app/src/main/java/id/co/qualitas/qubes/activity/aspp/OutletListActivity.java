package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAdapter;
import id.co.qualitas.qubes.adapter.aspp.OutletListAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.User;

public class OutletListActivity extends BaseActivity {
    private OutletListAdapter mAdapterVisit, mAdapterNoo;
    private List<Customer> mList, mListNoo;
    private MovableFloatingActionButton btnAddVisit, btnAddNoo;
    private EditText edtSearchVisit, edtSearchNoo;
    private LinearLayout llVisitTab, llNooTab, llVisit, llNoo;
    private View tabVisit, tabNoo;
    private RecyclerView recyclerViewVisit, recyclerViewNoo;
    private boolean tabVisitUI = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_outlet_list);

        init();
        initialize();
        initData();

        llVisitTab.setOnClickListener(v -> {
            tabVisitUI = true;
            tabVisit.setVisibility(View.VISIBLE);
            llVisit.setVisibility(View.VISIBLE);
            tabNoo.setVisibility(View.GONE);
            llNoo.setVisibility(View.GONE);
        });

        llNooTab.setOnClickListener(v -> {
            tabVisitUI = false;
            tabVisit.setVisibility(View.GONE);
            llVisit.setVisibility(View.GONE);
            tabNoo.setVisibility(View.VISIBLE);
            llNoo.setVisibility(View.VISIBLE);
        });

        setViewVisit();
        setViewNoo();
    }

    private void setViewVisit() {
        mAdapterVisit = new OutletListAdapter(this, mList, header -> {
            Intent intent = new Intent(this, DailySalesmanActivity.class);
            startActivity(intent);
        });

        recyclerViewVisit.setAdapter(mAdapterVisit);

        edtSearchVisit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapterVisit != null) {
                    mAdapterVisit.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnAddVisit.setOnClickListener(v -> {
            openDialogAdd();
        });
    }

    private void setViewNoo() {
        mAdapterNoo = new OutletListAdapter(this, mListNoo, header -> {
            Intent intent = new Intent(this, DailySalesmanActivity.class);
            startActivity(intent);
        });

        recyclerViewNoo.setAdapter(mAdapterNoo);

        edtSearchNoo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapterNoo != null) {
                    mAdapterNoo.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnAddNoo.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateNooActivity.class);
            startActivity(intent);
        });
    }

    private void openDialogAdd() {
        Dialog alertDialog = new Dialog(this);

        alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        EditText editText = alertDialog.findViewById(R.id.edit_text);
        RecyclerView listView = alertDialog.findViewById(R.id.list_view);

        List<String> groupList = new ArrayList<>();
        groupList.add("OBC44 - YANTO (TK)\nJL. KOMODORE");
        groupList.add("0C258 - SUSU IBU (WR)\nJL. RAYA BOGOR");
        groupList.add("0AQ13 - TOSIN (WR)\nJL. JATIWARINGIN");
        groupList.add("0AM92 - ES TIGA (TK)\nJL. BINA MARGA");

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
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Customer("BO", "Black Owl", "Golf Island Beach Theme Park, Jl. Pantai Indah Kapuk No.77, Kamal Muara, DKI Jakarta 14470", true, new LatLng(-6.090263984566263, 106.74593288657607)));
        mList.add(new Customer("PCP", "Pantjoran Chinatown PIK", "Unnamed Road, 14460", false, new LatLng(-6.09047339393416, 106.74535959301855)));
        mList.add(new Customer("CMP", "Central Market PIK", "Golf Island, Kawasan Pantai Maju, Jl, Jl. Boulevard Raya, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14470", true, new LatLng(-6.09102018270127, 106.74661148098058)));

        mListNoo = new ArrayList<>();
        mListNoo.add(new Customer("CHGI", "Cluster Harmony, Golf Island", "WP6W+7JR, Pantai Indah Kapuk St, Kamal Muara, Penjaringan, North Jakarta City, Jakarta 14460", false, new LatLng(-6.089065696336256, 106.74676357552187)));
        mListNoo.add(new Customer("MSGIP", "Monsieur Spoon Golf Island PIK", "Urban Farm, Unit 5, Kawasan Pantai Maju Jl. The Golf Island Boulevard, Kel, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14460", true, new LatLng(-6.09032214182743, 106.74191982249332)));
        mListNoo.add(new Customer("KMPP", "K3 Mart PIK Pantjoran", "Golf Island, Ruko Blok D No.02A, Kamal Muara, Jkt Utara, Daerah Khusus Ibukota Jakarta 11447", false, new LatLng(-6.088542162422348, 106.74239952686823)));

    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        edtSearchNoo = findViewById(R.id.edtSearchNoo);
        edtSearchVisit = findViewById(R.id.edtSearchVisit);
        llVisit = findViewById(R.id.llVisit);
        llNoo = findViewById(R.id.llNoo);
        llVisitTab = findViewById(R.id.llVisitTab);
        llNooTab = findViewById(R.id.llNooTab);
        tabVisit = findViewById(R.id.tabVisit);
        tabNoo = findViewById(R.id.tabNoo);
        btnAddVisit = findViewById(R.id.btnAddVisit);
        btnAddNoo = findViewById(R.id.btnAddNoo);

        recyclerViewVisit = findViewById(R.id.recyclerViewVisit);
        recyclerViewVisit.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVisit.setHasFixedSize(true);

        recyclerViewNoo = findViewById(R.id.recyclerViewNoo);
        recyclerViewNoo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNoo.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
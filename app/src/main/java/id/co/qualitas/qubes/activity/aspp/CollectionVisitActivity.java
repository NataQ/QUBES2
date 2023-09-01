package id.co.qualitas.qubes.activity.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CollectionVisitAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.User;

public class CollectionVisitActivity extends BaseActivity {
    private CollectionVisitAdapter mAdapter;
    private List<Invoice> mList;
    private ImageView imgBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_collection_visit);

        init();
        initialize();
        initData();

        mAdapter = new CollectionVisitAdapter(this, mList, header -> {
            Intent intent = new Intent(this, CollectionFormActivity.class);
            startActivity(intent);
        });

        recyclerView.setAdapter(mAdapter);

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Invoice("DKA495486", "TOKO SIDIK HALIM", "0GV43", 884736, 0, "1 June 2023", true));
        mList.add(new Invoice("DKA496933", "TOKO SIDIK HALIM", "0GV43", 39294, 0, "2 June 2023", false));
        mList.add(new Invoice("DKA402541", "TOKO SIDIK HALIM", "0WJ42", 9363600, 0, "4 June 2023", true));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
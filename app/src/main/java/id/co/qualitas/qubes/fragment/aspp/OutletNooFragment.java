package id.co.qualitas.qubes.fragment.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CreateNooActivity;
import id.co.qualitas.qubes.activity.aspp.DailySalesmanActivity;
import id.co.qualitas.qubes.adapter.aspp.OutletNooAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.User;

public class OutletNooFragment extends BaseFragment {
    private MovableFloatingActionButton btnAdd;
    private EditText edtSearch;
    private OutletNooAdapter mAdapter;
    private List<Customer> mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_outlet_noo, container, false);

        getActivity().setTitle(getString(R.string.visit));

        init();
        initFragment();
        initialize();
        initData();

        mAdapter = new OutletNooAdapter(this, mList, header -> {
            Intent intent = new Intent(getActivity(), DailySalesmanActivity.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(mAdapter);

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

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateNooActivity.class);
            startActivity(intent);
        });

        return rootView;
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Customer("CHGI", "Cluster Harmony, Golf Island", "WP6W+7JR, Pantai Indah Kapuk St, Kamal Muara, Penjaringan, North Jakarta City, Jakarta 14460", false, new LatLng(-6.089065696336256, 106.74676357552187)));
        mList.add(new Customer("MSGIP", "Monsieur Spoon Golf Island PIK", "Urban Farm, Unit 5, Kawasan Pantai Maju Jl. The Golf Island Boulevard, Kel, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14460", true, new LatLng(-6.09032214182743, 106.74191982249332)));
        mList.add(new Customer("KMPP", "K3 Mart PIK Pantjoran", "Golf Island, Ruko Blok D No.02A, Kamal Muara, Jkt Utara, Daerah Khusus Ibukota Jakarta 11447", false, new LatLng(-6.088542162422348, 106.74239952686823)));

    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        btnAdd = rootView.findViewById(R.id.btnAdd);
        edtSearch = rootView.findViewById(R.id.edtSearch);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }
}
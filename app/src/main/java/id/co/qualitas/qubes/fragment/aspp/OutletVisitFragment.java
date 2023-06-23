package id.co.qualitas.qubes.fragment.aspp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.DailySalesmanActivity;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAdapter;
import id.co.qualitas.qubes.adapter.aspp.OutletVisitAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.User;

public class OutletVisitFragment extends BaseFragment {
    private MovableFloatingActionButton btnAdd;
    private EditText edtSearch;
    private OutletVisitAdapter mAdapter;
    private List<Customer> mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_outlet_visit, container, false);

        getActivity().setTitle(getString(R.string.visit));

        init();
        initFragment();
        initialize();
        initData();

        mAdapter = new OutletVisitAdapter(this, mList, header -> {
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
            openDialogAdd();
        });

        return rootView;
    }

    private void openDialogAdd() {
        Dialog alertDialog = new Dialog(getActivity());

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

        FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(getActivity(), groupList, (nameItem, adapterPosition) -> {
            alertDialog.dismiss();
        });

        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
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
package id.co.qualitas.qubes.fragment.aspp;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.adapter.aspp.RouteCustomerAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.RouteCustomer;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.utils.Utils;

public class RouteCustomerFragment extends BaseFragment {
    private static final int LOCATION_PERMISSION_REQUEST = 7;
    private RouteCustomerAdapter mAdapter;
    private List<RouteCustomer> mList, mListFiltered;
    private ArrayAdapter<String> spn1Adapter, spn2Adapter, spnRouteCustAdapter;
    private Button btnCoverage;
    private EditText edtSearch;
    private Spinner spinnerRouteCustomer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_route_customer, container, false);

        getActivity().setTitle(getString(R.string.routeCustomer));

        init();
        initFragment();
        initialize();
        initData();

        btnCoverage.setOnClickListener(v -> {
            ((NewMainActivity) getActivity()).changePage(23);
//            Intent intent = new Intent(getActivity(), CoverageActivity.class);
//            startActivity(intent);
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

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    setEnabled(false);
                    ((NewMainActivity) getActivity()).changePage(1);
                }
            }
        });

        return rootView;
    }

    public void moveDirection(RouteCustomer headerCustomer) {
        if (Utils.isGPSOn(getContext())) {
            Helper.setItemParam(Constants.ROUTE_CUSTOMER_HEADER, headerCustomer);
            ((NewMainActivity) getActivity()).changePage(24);
        } else {
            Utils.turnOnGPS(getActivity());
        }
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new RouteCustomer("0EM44", "ECA", "JL. RAYA HALIM NO.8 SAMPING TK. MELLY KEBUN PALA KP. MAKASSAR", "5.41 KM", -6.090263984566263, 106.74593288657607, true));
        mList.add(new RouteCustomer("0DP90", "BAGUS CAR WASH (CAHAYA MADURA)", "JL. SQUADRON 26 HALIM PERDANAKUSUMA KEBON PALA MAKASSAR", "5.80 KM", -6.09047339393416, 106.74535959301855, false));
        mList.add(new RouteCustomer("0GT55", "TOKO LOLITA (PAK YUSUF)", "JL. MESJID AL MUNIR NO. 7 DEPAN PUSKESMAS MAKASSAR 0", "5.91 KM", -6.089065696336256, 106.74676357552187, true));
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        spinnerRouteCustomer = rootView.findViewById(R.id.spinnerRouteCustomer);
        edtSearch = rootView.findViewById(R.id.edtSearch);
        btnCoverage = rootView.findViewById(R.id.btnCoverage);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setHasFixedSize(true);

        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("All");
        listSpinner.add("Route");

        spnRouteCustAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, listSpinner);
        spnRouteCustAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRouteCustomer.setAdapter(spnRouteCustAdapter);
        spinnerRouteCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                edtSearch.setText(null);
                if (position == 0) {
                    filterData(true);
                } else {
                    filterData(false);
                }
                setDataFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void filterData(boolean all) {
        mListFiltered = new ArrayList<>();

        if (all) {
            mListFiltered.addAll(mList);
        } else {
            for (RouteCustomer routeCustomer : mList) {
                if (routeCustomer.isRoute()) {
                    mListFiltered.add(routeCustomer);
                }
            }
        }
    }

    private void setDataFilter() {
        mAdapter = new RouteCustomerAdapter(this, mListFiltered);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "2");

//        try {
//            if (getView() != null) {
//                getView().setFocusableInTouchMode(true);
//            }
//        } catch (NullPointerException ignored) {
//        }
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
//            }
//        });
    }

    public void openDialogDirections(RouteCustomer detail) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialogview = inflater.inflate(R.layout.aspp_dialog_directions, null);
        alertDialog = new Dialog(getActivity());
        alertDialog.setContentView(dialogview);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Spinner spn1 = dialogview.findViewById(R.id.spn1);
        Spinner spn2 = dialogview.findViewById(R.id.spn2);

        ImageView imgClose = dialogview.findViewById(R.id.imgClose);
        MaterialButton btnDirections = dialogview.findViewById(R.id.btnDirections);

        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("Your Location");
        for (RouteCustomer routeCustomer : mList) {
            listSpinner.add(routeCustomer.getNameCustomer());
        }

        spn1Adapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_item, listSpinner);
        spn1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn1.setAdapter(spn1Adapter);

        spn2Adapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_item, listSpinner);
        spn2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn2.setAdapter(spn2Adapter);

        for (int i = 0; i < listSpinner.size(); i++) {
            String name = listSpinner.get(i);
            if (detail.getNameCustomer().contains(name)) {
                spn2.setSelection(i);
                break;
            }
        }

        imgClose.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        btnDirections.setOnClickListener(v -> {
            alertDialog.dismiss();
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + -6.090263984566263 + "," + 106.74593288657607 + "&daddr=" + -6.088542162422348 + "," + 106.74239952686823));
                startActivity(intent);
            } catch (ActivityNotFoundException ane) {
                Toast.makeText(getActivity(), "Please Install Google Maps ", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.getMessage();
            }
        });

        alertDialog.show();
    }
}
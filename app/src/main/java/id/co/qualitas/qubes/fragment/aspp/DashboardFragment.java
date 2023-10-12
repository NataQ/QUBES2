package id.co.qualitas.qubes.fragment.aspp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.User;

public class DashboardFragment extends BaseFragment {
    TextView txtName, txtDriver, txtTodayDate, txtRoute, txtJabatanArea, txtCoverageArea;
    TextView txtAsset, txtAssetRoute, txtAssetNonRute;
    TextView txtECS, txtAT;
    TextView txtCallRute, txtCallNonRute, txtTotalCall, txtNonVisit;
    TextView txtTotalInvoiceAmount, txtPaymentAmount, txtOutstandingAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_dashboard, container, false);
        getActivity().setTitle(getString(R.string.navmenu1));
        initialize();
        return rootView;
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        txtName = rootView.findViewById(R.id.txtName);
        txtDriver = rootView.findViewById(R.id.txtDriver);
        txtJabatanArea = rootView.findViewById(R.id.txtJabatanArea);
        txtRoute = rootView.findViewById(R.id.txtRoute);
        txtTodayDate = rootView.findViewById(R.id.txtTodayDate);
        txtCoverageArea = rootView.findViewById(R.id.txtCoverageArea);
        txtAsset = rootView.findViewById(R.id.txtAsset);
        txtAssetRoute = rootView.findViewById(R.id.txtAssetRoute);
        txtAssetNonRute = rootView.findViewById(R.id.txtAssetNonRute);
        txtECS = rootView.findViewById(R.id.txtECS);
        txtAT = rootView.findViewById(R.id.txtAT);
        txtCallRute = rootView.findViewById(R.id.txtCallRute);
        txtCallNonRute = rootView.findViewById(R.id.txtCallNonRute);
        txtTotalCall = rootView.findViewById(R.id.txtTotalCall);
        txtNonVisit = rootView.findViewById(R.id.txtNonVisit);
        txtTotalInvoiceAmount = rootView.findViewById(R.id.txtTotalInvoiceAmount);
        txtPaymentAmount = rootView.findViewById(R.id.txtPaymentAmount);
        txtOutstandingAmount = rootView.findViewById(R.id.txtOutstandingAmount);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
//            }
//        });
    }

    private void setData() {
        setFormatSeparator();
        txtTodayDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        txtRoute.setText(Helper.getTodayRoute());
        txtName.setText(Helper.isEmpty(user.getFull_name(), "-"));
        txtDriver.setText(Helper.isEmpty(user.getDriver_name(), "-"));
        txtJabatanArea.setText("");

        txtAsset.setText(format.format(database.getCountRouteCustomer(true)));//all
        txtAssetRoute.setText(format.format(database.getCountRouteCustomer(false)));//route
        txtAssetNonRute.setText(format.format(user.getMax_visit()));//global parameter add non route


    }
}
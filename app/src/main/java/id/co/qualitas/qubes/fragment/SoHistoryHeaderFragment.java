package id.co.qualitas.qubes.fragment;

import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.adapter.SalesOrderHistoryAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitOrderHeader;

public class SoHistoryHeaderFragment extends BaseFragment {
    private List<VisitOrderHeader> listOrderHeader;
    private TextView txtDate;

    @Override
    public void onResume() {
        super.onResume();

        if (listOrderHeader != null && !listOrderHeader.isEmpty()) {
            setData();
        } else {
            txtEmpty.setVisibility(View.GONE);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sales_order_history_header, container, false);

//        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.navmenu6b));
//        ((MainActivityDrawer) getActivity()).enableBackToolbar(true);

        initFragment();
        initialize();

        if (user != null) {
            if (user.getCurrentDate() != null) {
                txtDate.setText(Helper.validateResponseEmpty(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_12, user.getCurrentDate())));
            }else{
                txtDate.setText("-");
            }
        }
        return rootView;
    }

    private void initialize() {
        listOrderHeader = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recycler_view_sales_order_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        txtEmpty = rootView.findViewById(R.id.txtViewEmpty);
        progressBar = rootView.findViewById(R.id.progressBar);
        txtDate = rootView.findViewById(R.id.txtDate);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        try {
            listOrderHeader = db.getAllListOrderHeader();
        } catch (SQLiteException ignored) {

        }
    }

    private void setData() {
        recyclerView.setVisibility(View.VISIBLE);
        SalesOrderHistoryAdapter mAdapter = new SalesOrderHistoryAdapter(listOrderHeader, this);
        recyclerView.setAdapter(mAdapter);
    }

    public void goToOrder() {
        ((NewMainActivity) getActivity()).changePage(13);
    }
}

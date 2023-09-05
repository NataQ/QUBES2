package id.co.qualitas.qubes.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.adapter.OrderSummaryDetailAdapterV2;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;

public class OrderSummaryDetail2Fragment extends BaseFragment {
    private List<VisitOrderDetailResponse> orderSummaryDetailList = new ArrayList<>();
    private OrderSummaryDetailAdapterV2 mAdapter;
    private TextView totalItems, txtDate, txtOutlet;
    BigDecimal totalPriceNett = BigDecimal.ZERO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_order_summary_detail, container, false);

        initialize();
        initFragment();
        setData();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((NewMainActivity) getActivity()).changePage(14);
                        return true;
                    }
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewMainActivity) getActivity()).changePage(14);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "15");
    }

    private void setData() {
        orderSummaryDetailList = new ArrayList<>();
        if (Helper.getItemParam(Constants.VISIT_ORDER_DETAIL) != null) {
            txtViewEmpty.setVisibility(View.GONE);
            orderSummaryDetailList = (List<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);

            if (orderSummaryDetailList != null && !orderSummaryDetailList.isEmpty()) {
                Collections.sort(orderSummaryDetailList, new Comparator<VisitOrderDetailResponse>() {
                    @Override
                    public int compare(VisitOrderDetailResponse s1, VisitOrderDetailResponse s2) {
                        return Helper.ltrim(Helper.validateResponseEmpty(s1.getMaterialName())).compareToIgnoreCase(Helper.ltrim(Helper.validateResponseEmpty(s2.getMaterialName())));
                    }
                });

                linearLayoutList.setVisibility(View.VISIBLE);
                linearLayoutList.removeAllViews();
                mAdapter = new OrderSummaryDetailAdapterV2(OrderSummaryDetail2Fragment.this, orderSummaryDetailList, db);

                for (int i = 0; i < mAdapter.getCount(); i++) {
                    View item = mAdapter.getView(i, null, null);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 0, 0, 15);
                    linearLayoutList.addView(item, layoutParams);
                }

                setDataHeader();
            } else {
                txtViewEmpty.setVisibility(View.VISIBLE);
            }
        } else {
            txtViewEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void setDataHeader() {
        if (outletResponse != null) {
            if (getCurrentDate() != null) {
                txtDate.setText(Helper.changeDateFormatDefa(Constants.DATE_TYPE_1, Constants.DATE_TYPE_12, getCurrentDate()));
            }

            if(outletResponse.getVisitDate() == null){
                outletResponse.setVisitDate(Helper.changeDateFormatDefa(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, getCurrentDate()));
            }

            if (outletResponse.getIdOutlet() != null) {
                txtOutlet.setText(Helper.validateResponseEmpty(outletResponse.getIdOutlet()).concat(Constants.STRIP)
                        .concat(db.getOutletName(outletResponse.getIdOutlet())));}
        }
    }

    private void initialize() {
        imgBack = rootView.findViewById(R.id.imgBack);
        linearLayoutList = rootView.findViewById(R.id.linearLayoutList);
        txtViewEmpty = rootView.findViewById(R.id.txtViewEmpty);
        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);
        txtDate = rootView.findViewById(R.id.txtDate);
        txtOutlet = rootView.findViewById(R.id.txtOutlet);
    }
}

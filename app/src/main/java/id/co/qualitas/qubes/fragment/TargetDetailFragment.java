package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.math.BigDecimal;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.TargetResponse;
import id.co.qualitas.qubes.model.TargetSummaryRequest;

public class TargetDetailFragment extends BaseFragment {
    private TargetResponse targetDetail = new TargetResponse();
    private TextView txtIdOutlet, txtOutletName, txtDivProd, txtViewTarget, txtTarget, txtActual, txtInitialTarget;
    private LinearLayout rowInitialTarget;
    static Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_target_detail, container, false);

//        ((MainActivityDrawer)getActivity()).setTitle(getString(R.string.navmenu2z));
//        ((MainActivityDrawer)getActivity()).enableBackToolbar(true);

        init();
        initialize();

        setData();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        ((NewMainActivity) getActivity()).changePage(1);
                        getFragmentManager().popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((NewMainActivity) getActivity()).changePage(1);
                getFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());

        targetDetail = (TargetResponse) Helper.getItemParam(Constants.TARGET_ITEM_DETAIL);

        imgBack = rootView.findViewById(R.id.imgBack);
        txtIdOutlet = rootView.findViewById(R.id.idOutlet);
        txtOutletName = rootView.findViewById(R.id.outletName);
        txtDivProd = rootView.findViewById(R.id.divProd);
        txtViewTarget = rootView.findViewById(R.id.txtTarget);
        txtTarget = rootView.findViewById(R.id.target);
        txtInitialTarget = rootView.findViewById(R.id.initialTarget);
        txtActual = rootView.findViewById(R.id.actual);

        rowInitialTarget = rootView.findViewById(R.id.row3b);
    }

    private void setData() {
        if(targetDetail != null){
            txtIdOutlet.setText(Helper.validateResponseEmpty(targetDetail.getIdOutlet()));
            txtOutletName.setText(Helper.validateResponseEmpty(targetDetail.getOutletName()));
            txtDivProd.setText(Helper.validateResponseEmpty(targetDetail.getMatClass()));
            if(targetDetail.getTarget() != null){
                txtTarget.setText(Helper.toRupiahFormat2(String.valueOf(targetDetail.getTarget().setScale(2, BigDecimal.ROUND_HALF_UP))));
            }
            if(targetDetail.getActual() != null){
                txtActual.setText(Helper.toRupiahFormat2(String.valueOf(targetDetail.getActual().setScale(2, BigDecimal.ROUND_HALF_UP))));
            }
            if(targetDetail.getTargetAwal() != null){
                txtInitialTarget.setText(Helper.toRupiahFormat2(String.valueOf(targetDetail.getTargetAwal().setScale(2, BigDecimal.ROUND_HALF_UP))));
            }
        }

        if(Helper.getItemParam(Constants.TARGET_SUMMARY_REQUEST) != null){
            if(((TargetSummaryRequest)Helper.getItemParam(Constants.TARGET_SUMMARY_REQUEST)).getPeriod().equals(Constants.MONTH)){
                rowInitialTarget.setVisibility(View.GONE);
            }else{
                rowInitialTarget.setVisibility(View.VISIBLE);
                txtViewTarget.setText("Current Target");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "22");
    }
}
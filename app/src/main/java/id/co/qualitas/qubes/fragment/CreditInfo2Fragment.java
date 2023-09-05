package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.RupiahTextView;
import id.co.qualitas.qubes.model.OutletResponse;

public class CreditInfo2Fragment extends BaseFragment {
    private RupiahTextView valueCreditLimit, valueCreditAvailable, valueSpPending, valueOverdue;
    private OutletResponse outletDetail;
    private String idOutlet, outletName = Constants.EMPTY_STRING;

    private TextView txtOutlet, txtDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_credit_info, container, false);

        initialize();
        initFragment();
        setDataHeader();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewMainActivity) getActivity()).changePage(8);
            }
        });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((NewMainActivity) getActivity()).changePage(8);
                        return true;
                    }
                }
                return false;
            }
        });

        if (idOutlet != null) {
            outletDetail = new OutletResponse();
            outletDetail = db.getDetailOutlet(idOutlet);
            setDataCreditInfo();
        }
        return rootView;
    }

    private void initialize() {
        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);

        if (outletResponse != null) {
            if (outletResponse.getIdOutlet() != null) {
                idOutlet = outletResponse.getIdOutlet();
            }
            if (outletResponse.getOutletName() != null) {
                outletName = outletResponse.getOutletName();
            }
        }
        imgBack = rootView.findViewById(R.id.imgBack);
        txtOutlet = rootView.findViewById(R.id.customerName);
        txtDate = rootView.findViewById(R.id.layDate);
        valueCreditLimit = rootView.findViewById(R.id.valueCreditLimit);
        valueOverdue = rootView.findViewById(R.id.valueOverdue);
        valueSpPending = rootView.findViewById(R.id.valueSpPending);
        valueCreditAvailable = rootView.findViewById(R.id.valueCreditAvailable);
    }

    private void setDataHeader() {
        if (outletResponse != null) {
            txtDate.setText(outletResponse.getVisitDate() != null
                    ? Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_12, outletResponse.getVisitDate())
                    : getResources().getString(R.string.emptyText));

            txtOutlet.setText(Helper.validateResponseEmpty(outletResponse.getOutletName()));
        }
    }

    private void setDataCreditInfo() {
        if (outletDetail != null) {
            if (outletDetail.getCredit_limit() != null) {
                valueCreditLimit.setText(String.valueOf(outletDetail.getCredit_limit()));
            }

            if (outletDetail.getCredit_exposure() != null) {
                valueSpPending.setText(String.valueOf(outletDetail.getCredit_exposure()));
            }

            if (outletDetail.getOverdue() != null) {
                valueOverdue.setText(String.valueOf(outletDetail.getOverdue()));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "9");
    }
}
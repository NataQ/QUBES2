package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.RupiahTextView;
import id.co.qualitas.qubes.model.OutletResponse;

public class CreditInfoFragment extends BaseFragment {

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
        rootView = inflater.inflate(R.layout.fragment_credit_info_n, container, false);

//        ((MainActivityDrawer)getActivity()).setTitle(getString(R.string.navmenu5a));
//        ((MainActivityDrawer)getActivity()).enableBackToolbar(true);

        initialize();
        initFragment();
        setDataHeader();
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
        txtOutlet = rootView.findViewById(R.id.outletAndDate);
//        txtDate = rootView.findViewById(R.id.layDate);
        valueCreditLimit = rootView.findViewById(R.id.valueCreditLimit);
        valueOverdue = rootView.findViewById(R.id.valueOverdue);
        valueSpPending = rootView.findViewById(R.id.valueSpPending);
    }

    private void setDataHeader() {

        if (outletResponse != null) {
            txtOutlet.setText(Helper.validateResponseEmpty(outletResponse.getOutletName())
                    .concat(Constants.PIPE)
                    .concat(
                    outletResponse.getVisitDate() != null?Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_12, outletResponse.getVisitDate()):getResources().getString(R.string.emptyText))
            );
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

}
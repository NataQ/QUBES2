package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;

public class SummaryFragment extends BaseFragment {
    protected Fragment fragment;
    protected FragmentManager fragmentManager;
    protected FragmentTransaction ft;
    protected FragmentManager fm;
    private LinearLayout llSR, llSOH;
    private TextView txtSummaryReport, txtSOH;
    private View lineSummaryReport, lineSOH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_summary, container, false);

//        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.navmenu6));
//        ((MainActivityDrawer)getActivity()).enableBackToolbar(false);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((NewMainActivity) getActivity()).changePage(1);
                        return true;
                    }
                }
                return false;
            }
        });

        initialize();

        setFirstFragment();

        llSR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineSummaryReport.setVisibility(View.VISIBLE);
                lineSOH.setVisibility(View.INVISIBLE);
                txtSummaryReport.setTextColor(ContextCompat.getColor(getContext(), R.color.new_blue));
                txtSOH.setTextColor(ContextCompat.getColor(getContext(), R.color.caldroid_gray));

                Helper.removeItemParam(Constants.SUMMARY_TYPE);
                fragment = new SummaryReportMonthlyFragment();
                setContent(fragment);
            }
        });

        llSOH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineSummaryReport.setVisibility(View.INVISIBLE);
                lineSOH.setVisibility(View.VISIBLE);
                txtSummaryReport.setTextColor(ContextCompat.getColor(getContext(), R.color.caldroid_gray));
                txtSOH.setTextColor(ContextCompat.getColor(getContext(), R.color.new_blue));

                fragment = new SoHistoryHeaderFragment();
                setContent(fragment);
            }
        });
        return rootView;
    }

    private void initialize() {
        txtSummaryReport = rootView.findViewById(R.id.txtSummaryReport);
        txtSOH = rootView.findViewById(R.id.txtSOH);
        lineSummaryReport = rootView.findViewById(R.id.lineSummaryReport);
        lineSOH = rootView.findViewById(R.id.lineSOH);
        llSR = rootView.findViewById(R.id.llSR);
        llSOH = rootView.findViewById(R.id.llSOH);
    }

    public void setContent(Fragment fragment) {
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.main_container_summary, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE,"4");
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button
//                    setContentToHome();
                    return true;
                }
                return false;
            }
        });
    }

    private void setFirstFragment() {
        lineSummaryReport.setVisibility(View.VISIBLE);
        lineSOH.setVisibility(View.INVISIBLE);
        txtSummaryReport.setTextColor(ContextCompat.getColor(getContext(), R.color.new_blue));
        txtSOH.setTextColor(ContextCompat.getColor(getContext(), R.color.caldroid_gray));

        Helper.removeItemParam(Constants.SUMMARY_TYPE);
        fragment = new SummaryReportMonthlyFragment();
        setContent(fragment);
    }
}
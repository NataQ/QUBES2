package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
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

public class TargetFragment extends BaseFragment {
    protected Fragment fragment;
    protected FragmentManager fragmentManager;
    protected FragmentTransaction ft;
    protected FragmentManager fm;

    private LinearLayout llST, llObject;
    private TextView txtSalesTarget, txtObject;
    private View lineSalesTarget, lineObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.new_fragment_target, container, false);

        initialize();

        setFirstFragment();

        llST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineSalesTarget.setVisibility(View.VISIBLE);
                lineObject.setVisibility(View.INVISIBLE);
                txtSalesTarget.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_blue));
                txtObject.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));

                fragment = new CustomerTargetFragment();
                setContent(fragment);
            }
        });

        llObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineSalesTarget.setVisibility(View.INVISIBLE);
                lineObject.setVisibility(View.VISIBLE);
                txtSalesTarget.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));
                txtObject.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_blue));

                fragment = new ObjectTargetFragmentv2();
                setContent(fragment);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setFirstFragment() {
        lineSalesTarget.setVisibility(View.VISIBLE);
        lineObject.setVisibility(View.INVISIBLE);
        txtSalesTarget.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_blue));
        txtObject.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));

        fragment = new CustomerTargetFragment();
        setContent(fragment);
    }

    private void initialize() {
        llST = rootView.findViewById(R.id.llST);
        txtSalesTarget = rootView.findViewById(R.id.txtSalesTarget);
        lineSalesTarget = rootView.findViewById(R.id.lineSalesTarget);
        llObject = rootView.findViewById(R.id.llObject);
        txtObject = rootView.findViewById(R.id.txtObject);
        lineObject = rootView.findViewById(R.id.lineObject);
    }

    public void setContent(Fragment fragment) {
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.main_container_target, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}
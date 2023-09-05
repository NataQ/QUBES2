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


public class Order2Fragment extends BaseFragment {
    protected Fragment fragment;
    protected FragmentManager fragmentManager;
    protected FragmentTransaction ft;
    protected FragmentManager fm;
    private LinearLayout llCO, llTO;
    private TextView txtCanvasOrder, txtTakingOrder;
    private View lineCanvasOrder, lineTakingOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_order, container, false);

        initialize();

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

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewMainActivity) getActivity()).changePage(8);
            }
        });

        Helper.setItemParam(Constants.VIEW_STORE_CHECK, Constants.DONE_VIEW_STORE_CHECK);

        setFirstFragment();

        llCO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineCanvasOrder.setVisibility(View.VISIBLE);
                lineTakingOrder.setVisibility(View.INVISIBLE);
                txtCanvasOrder.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_blue));
                txtTakingOrder.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));

                Helper.removeItemParam(Constants.VISIT_ORDER_HEADER);
                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL_SAVE);
                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
                Helper.setItemParam(Constants.ORDER_TYPE, Constants.ORDER_CANVAS_TYPE);

                fragment = new OrderHeaderFragment();
                setContent(fragment);
            }
        });

        llTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineCanvasOrder.setVisibility(View.INVISIBLE);
                lineTakingOrder.setVisibility(View.VISIBLE);
                txtCanvasOrder.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));
                txtTakingOrder.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_blue));

                Helper.removeItemParam(Constants.VISIT_ORDER_HEADER);
                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL_SAVE);
                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
                Helper.setItemParam(Constants.ORDER_TYPE, Constants.ORDER_TAKING_TYPE);

                fragment = new OrderHeaderFragment();
                setContent(fragment);
            }
        });
        
        return rootView;
    }

    private void setFirstFragment() {
        lineCanvasOrder.setVisibility(View.INVISIBLE);
        lineTakingOrder.setVisibility(View.VISIBLE);
        txtCanvasOrder.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));
        txtTakingOrder.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_blue));

        Helper.removeItemParam(Constants.VISIT_ORDER_HEADER);
        Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL_SAVE);
        Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
        Helper.setItemParam(Constants.ORDER_TYPE, Constants.ORDER_TAKING_TYPE);

        fragment = new OrderHeaderFragment();
        setContent(fragment);
    }

    private void initialize() {
        imgBack = rootView.findViewById(R.id.imgBack);
        txtCanvasOrder = rootView.findViewById(R.id.txtCanvasOrder);
        txtTakingOrder = rootView.findViewById(R.id.txtTakingOrder);
        lineCanvasOrder = rootView.findViewById(R.id.lineCanvasOrder);
        lineTakingOrder = rootView.findViewById(R.id.lineTakingOrder);
        llCO = rootView.findViewById(R.id.llCO);
        llTO = rootView.findViewById(R.id.llTO);
    }

    public void setContent(Fragment fragment) {
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.main_container_order, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onResume() {
        Helper.setItemParam(Constants.CURRENTPAGE, "11");
        super.onResume();
    }
}
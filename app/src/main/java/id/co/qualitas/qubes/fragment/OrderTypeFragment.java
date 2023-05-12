package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.MainActivityDrawer;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;


public class OrderTypeFragment extends BaseFragment {
    protected Fragment fragment;
    protected FragmentManager fragmentManager;
    protected FragmentTransaction ft;
    protected FragmentManager fm;
    private TextView txtCanvasOrder,txtTakingOrder;
    private LinearLayout llCanvasOrder, llTakingOrder, linearCanvasOrder, linearTakingOrder;
    private ImageView triangleCanvasOrder,triangleTakingOrder,imgCanvasOrder,imgTakingOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_type, container, false);

        ((MainActivityDrawer)getActivity()).setTitle(getString(R.string.navmenu5c));
        initialize();

        Helper.setItemParam(Constants.VIEW_STORE_CHECK, Constants.DONE_VIEW_STORE_CHECK);
        llCanvasOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triangleCanvasOrder.setVisibility(View.VISIBLE);
                imgCanvasOrder.getLayoutParams().height = 85;
                imgCanvasOrder.getLayoutParams().width = 85;
                linearCanvasOrder.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                txtCanvasOrder.setTextSize(36);
                txtCanvasOrder.setTextColor(getResources().getColor(R.color.white));
                Helper.removeItemParam(Constants.VISIT_ORDER_HEADER);
                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL_SAVE);
                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
                Helper.setItemParam(Constants.ORDER_TYPE, Constants.ORDER_CANVAS_TYPE);
                fragment = new OrderHeaderFragment();
                setContent(fragment);
            }
        });

        llTakingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triangleTakingOrder.setVisibility(View.VISIBLE);
                imgTakingOrder.getLayoutParams().height = 85;
                imgTakingOrder.getLayoutParams().width = 85;
                linearTakingOrder.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                txtTakingOrder.setTextColor(getResources().getColor(R.color.white));
                txtTakingOrder.setTextSize(36);
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

    private void initialize() {
        txtCanvasOrder = rootView.findViewById(R.id.canvasOTxt);
        txtTakingOrder = rootView.findViewById(R.id.takingOTxt);
        llCanvasOrder = rootView.findViewById(R.id.llCanvasOrder);
        llTakingOrder = rootView.findViewById(R.id.llTakingOrder);
        linearCanvasOrder = rootView.findViewById(R.id.linearCOrder);
        linearTakingOrder = rootView.findViewById(R.id.linearTOrder);
        triangleCanvasOrder = rootView.findViewById(R.id.canvasOTriangle);
        triangleTakingOrder = rootView.findViewById(R.id.takingOrderTriangle);
        imgCanvasOrder = rootView.findViewById(R.id.canvasOIcon);
        imgTakingOrder = rootView.findViewById(R.id.takingOrderIcon);
    }

}
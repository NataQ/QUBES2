package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.OrderHeaderAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;

public class OrderHeaderFragment extends BaseFragment {
    private RecyclerView recyclerViewOrder;
    private OutletResponse outletResponse;
    private ArrayList<VisitOrderHeader> visitOrderHeaderArrayList = new ArrayList<>();
    private FloatingActionButton btnNewOrder;
    private String idOutlet, namaOutlet, orderType;
    private TextView txtOutlet, txtDate;
    //txt1, txt2, txt3;
    public static final OrderHeaderFragment INSTANCE = new OrderHeaderFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_header, container, false);

        getActivity().setTitle("Order Header");

        initialize();
        initFragment();
        initProgress();

        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.removeItemParam(Constants.ACCESS_POINT);
                Helper.removeItemParam(Constants.ORDER_HEADER_SELECTED);
                Helper.removeItemParam(Constants.VISIT_ORDER_HEADER);
                Helper.removeItemParam(Constants.ALL_ONE_TIME_DISCOUNT_DETAIL);

                ((MainActivity) getActivity()).changePage(12);
//                Intent intent = new Intent(getActivity(), CreateOrder2Fragment.class);
//                startActivity(intent);
//                fragment = new OrderSoldFragment();
//                setContent(fragment);
            }
        });


        return rootView;
    }

    public static OrderHeaderFragment getInstance() {
        return INSTANCE;
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        Helper.removeItemParam(Constants.ACCESS_POINT);

        recyclerViewOrder = rootView.findViewById(R.id.recycler_view);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewOrder.setItemAnimator(new DefaultItemAnimator());

        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);

        progressBar = rootView.findViewById(R.id.progressBar);
        btnNewOrder = rootView.findViewById(R.id.btnNewOrder);

        txtOutlet = rootView.findViewById(R.id.txtOutlet);
        txtDate = rootView.findViewById(R.id.txtDate);

//        txt1 = rootView.findViewById(R.id.txt1);
//        txt2 = rootView.findViewById(R.id.txt2);
//        txt3 = rootView.findViewById(R.id.txt3);
//
//        txt2.setText("Total Amount");
//        txt3.setText("Status");

        if (outletResponse != null) {
            if (outletResponse.getIdOutlet() != null) {
                idOutlet = outletResponse.getIdOutlet();
            }

            if (outletResponse.getOutletName() != null) {
                namaOutlet = outletResponse.getOutletName();
            }

            txtOutlet.setText(Helper.validateResponseEmpty(idOutlet).concat(Constants.STRIP)
                    .concat(Helper.validateResponseEmpty(namaOutlet)));
            if (outletResponse.getVisitDate() != null) {
                txtDate.setText(Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_12, outletResponse.getVisitDate()));
            }
        }

        if (Helper.getItemParam(Constants.ORDER_TYPE) != null) {
            orderType = Helper.getItemParam(Constants.ORDER_TYPE).toString();
//            if (orderType.equals(Constants.ORDER_CANVAS_TYPE)) {
//                txt1.setText("No. CO");
//            } else {
//                txt1.setText("No. TO");
//            }
        }
    }


    private void setData(ArrayList<VisitOrderHeader> visitOrderHeaders) {
        recyclerViewOrder.removeAllViews();
        OrderHeaderAdapter mAdapter = new OrderHeaderAdapter(visitOrderHeaders, this, orderType);
        recyclerViewOrder.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (String.valueOf(Helper.getItemParam(Constants.KEEP_BACK)).equals(Constants.TRUE)) {
            Helper.removeItemParam(Constants.KEEP_BACK);
        } else {
            getData();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem save = menu.findItem(R.id.action_save);
        final MenuItem filter = menu.findItem(R.id.action_filter);
        final MenuItem search = menu.findItem(R.id.action_search);
        final MenuItem edit = menu.findItem(R.id.action_edit);
        final MenuItem setting = menu.findItem(R.id.action_settings);
        final MenuItem next = menu.findItem(R.id.action_next);

        save.setVisible(false);
        filter.setVisible(false);
        edit.setVisible(false);
        setting.setVisible(false);
        search.setVisible(false);
        next.setVisible(false);
    }

    public void getData() {
        if (idOutlet != null) {
            visitOrderHeaderArrayList = db.getListOrderHeader(idOutlet, orderType);
            if (visitOrderHeaderArrayList != null && !visitOrderHeaderArrayList.isEmpty()) {
                setData(visitOrderHeaderArrayList);
            }
        }
    }
}
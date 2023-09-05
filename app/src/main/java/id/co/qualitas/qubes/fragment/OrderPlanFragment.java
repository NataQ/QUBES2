package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.adapter.OrderPlanAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OrderPlanHeader;

public class OrderPlanFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    private FloatingActionButton btnAdd;
    private ArrayList<OrderPlanHeader> orderPlanList;
    OrderPlanAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_order_plan, container, false);
        getActivity().setTitle(getString(R.string.navmenu3));
//        ((MainActivityDrawer) getActivity()).enableBackToolbar(false);

        init();
        initialize();
        removeFlag();

        getData();

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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewMainActivity) getActivity()).changePage(20);
//                Fragment fr = new OrderPlanDetailFragmentV2();
//                setContent(fr);
            }
        });

        return rootView;
    }

    private void getData() {
        orderPlanList = new ArrayList<>();
        orderPlanList = db.getAllOrderPlanDate();
        if (orderPlanList != null && !orderPlanList.isEmpty()) {
            for (OrderPlanHeader orderPlan : orderPlanList) {
                BigDecimal totalPlanInDay = BigDecimal.ZERO;
                if (orderPlan.getOutletDate() != null) {
                    ArrayList<OrderPlanHeader> listContent = db.getListOrderPlanContentByDate(orderPlan.getOutletDate());

                    if (listContent != null && !listContent.isEmpty()) {
                        for (OrderPlanHeader child : listContent) {
                            if (child.getIdOutlet() != null) {
                                child.setOutletName(db.getOutletName(child.getIdOutlet()));
                            }
                            try {
                                totalPlanInDay = totalPlanInDay.add(new BigDecimal(child.getPlan()));
                            } catch (NumberFormatException ignored) {

                            }
                        }
                        orderPlan.setTotalPrice(String.valueOf(totalPlanInDay));

                        Collections.sort(listContent, new Comparator<OrderPlanHeader>() {
                            @Override
                            public int compare(OrderPlanHeader s1, OrderPlanHeader s2) {
                                return Helper.ltrim(Helper.validateResponseEmpty(s1.getOutletName())).compareToIgnoreCase(Helper.ltrim(Helper.validateResponseEmpty(s2.getOutletName())));
                            }
                        });
                        orderPlan.setContent(listContent);
                    }
                }
            }

            setListData(orderPlanList);

            isEmptyData(false);
        } else {
            isEmptyData(true);
        }
    }

    private void isEmptyData(boolean b) {
        if (!b) {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setListData(ArrayList<OrderPlanHeader> mData) {
        mAdapter = new OrderPlanAdapter(mData, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());

        recyclerView = rootView.findViewById(R.id.recycler_view);
        txtEmpty = rootView.findViewById(R.id.txtEmpty);
        btnAdd = rootView.findViewById(R.id.btnAdd);
    }

    private void removeFlag() {
        Helper.removeItemParam(Constants.TARGET_ORDER_PLAN);
        Helper.removeItemParam(Constants.AMOUNT_PLAN);
        Helper.removeItemParam(Constants.IS_TODAY);

        Helper.removeItemParam(Constants.ORDER_PLAN_SELECTED);
        Helper.removeItemParam(Constants.ORDER_PLAN_DETAIL_HEADER);
        Helper.removeItemParam(Constants.GET_DETAIL_ORDER_PLAN);
        Helper.removeItemParam(Constants.ORDER_PLAN_DETAIL_SAVE);
    }

    public void deleteOrderPlan() {
        OrderPlanHeader orderPlanHeader = (OrderPlanHeader) Helper.getItemParam(Constants.ORDER_PLAN_SELECTED);
        if (orderPlanHeader != null) {
            orderPlanHeader.setStatusOrder(getString(R.string.STATUS_FALSE));
            orderPlanHeader.setDate(orderPlanHeader.getOutletDate());
            db.updateStatusOrderPlanNew(orderPlanHeader);

            orderPlanHeader.setDeleted(getResources().getString(R.string.STATUS_TRUE));
            db.updateOrderPlanDeletedById(orderPlanHeader);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "2");
        getData();

        try {
            if(getView() != null){
                getView().setFocusableInTouchMode(true);
            }
        } catch (NullPointerException ignored) {
        }
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(true);

        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.tanggal_hint));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            if (orderPlanList != null && !orderPlanList.isEmpty()) {
                setListData(orderPlanList);
            }
            return false;
        }

        final ArrayList<OrderPlanHeader> filteredList = new ArrayList<>();

        for (OrderPlanHeader header : orderPlanList) {
            final String text = Helper.validateResponseEmpty(header.getOutletDate()).toLowerCase();
            if (text.contains(newText)) {
                filteredList.add(header);
            }
        }
        setListData(filteredList);
        return false;
    }
}
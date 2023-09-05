package id.co.qualitas.qubes.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.adapter.ReturnSalesAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.ReturnResponse;
import id.co.qualitas.qubes.model.User;

public class Return2Fragment extends BaseFragment {
    private ArrayList<ReturnResponse> returnHeaderList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ReturnSalesAdapter mAdapter;
    private FloatingActionButton btnAddNew;
    private DatabaseHelper db;
    private TextView outletName, errData, txtDate;
    private MessageResponse messageResponse;
    private ReturnResponse returnRequestDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_return, container, false);

//        ((MainActivityDrawer) getActivity()).enableBackToolbar(true);
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

        setDataHeader();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewMainActivity) getActivity()).changePage(8);
            }
        });

        Helper.removeItemParam(Constants.RETURN_DETAIL);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewMainActivity) getActivity()).changePage(18);
//                Intent intent = new Intent(Return2Fragment.this, CreateReturn2Fragment.class);
//                startActivity(intent);
//                fragment = new CreateNewReturnFragment();
//                setContent(fragment);
            }
        });

        return rootView;
    }

    private void setDataHeader() {
        if (outletResponse != null) {
            outletName.setText(Helper.validateResponseEmpty(outletResponse.getOutletName()));
            if (outletResponse.getVisitDate() != null) {
                txtDate.setText(Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_12, outletResponse.getVisitDate()));
            }
        }
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);
        imgBack = rootView.findViewById(R.id.imgBack);
        btnAddNew = rootView.findViewById(R.id.btnAddNew);
        mRecyclerView = rootView.findViewById(R.id.recycler_view_return_header);
        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);
        outletName = rootView.findViewById(R.id.outletName);
        txtDate = rootView.findViewById(R.id.txtDate);
        errData = rootView.findViewById(R.id.errData);
        progressBar = rootView.findViewById(R.id.progressBar);
        returnHeaderList = db.getAllListReturnHeaders(outletResponse.getIdOutlet());
    }

    private void setData(List<ReturnResponse> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (list.size() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            errData.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            mAdapter = new ReturnSalesAdapter(list, Return2Fragment.this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            mRecyclerView.setVisibility(View.GONE);
            errData.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void deleteReturn(String id, int pos) {
        db.deleteReturN(id);
        db.deleteReturnDetailById(id);

        returnHeaderList = db.getAllListReturnHeaders(outletResponse.getIdOutlet());
        setData(returnHeaderList);
    }

    @Override
    public void onResume() {
        super.onResume();

        initialize();
        Helper.setItemParam(Constants.CURRENTPAGE, "17");

        returnHeaderList = new ArrayList<>();
        returnHeaderList = db.getAllListReturnHeaders(outletResponse.getIdOutlet());
        setData(returnHeaderList);
    }
}
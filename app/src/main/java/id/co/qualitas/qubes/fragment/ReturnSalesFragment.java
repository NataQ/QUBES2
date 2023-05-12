package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.adapter.ReturnSalesAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.ReturnResponse;
import id.co.qualitas.qubes.model.User;

public class ReturnSalesFragment extends BaseFragment {
    private ArrayList<ReturnResponse> returnHeaderList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ReturnSalesAdapter mAdapter;
    private FloatingActionButton btnAdd;
    private ImageView icAdd;
    private LinearLayout linearCreate;
    private ConstraintLayout linearList;
    private DatabaseHelper db;
    private TextView outletName, errData, txtDate;
    private MessageResponse messageResponse;
    private ReturnResponse returnRequestDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_return, container, false);
        getActivity().setTitle(getString(R.string.navmenu5d));
//        ((MainActivityDrawer) getActivity()).enableBackToolbar(true);
        initialize();

        setDataHeader();

        Helper.removeItemParam(Constants.RETURN_DETAIL);

        icAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new CreateNewReturnFragment();
                setContent(fragment);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new CreateNewReturnFragment();
                setContent(fragment);
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
        mRecyclerView = rootView.findViewById(R.id.recycler_view_return_header);
        icAdd = rootView.findViewById(R.id.add_return);
        linearCreate = rootView.findViewById(R.id.linear_create);
        linearList = rootView.findViewById(R.id.returnHeaderList);
        btnAdd = rootView.findViewById(R.id.btnAddNew);
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
            linearList.setVisibility(View.VISIBLE);
            linearCreate.setVisibility(View.GONE);


//            mAdapter = new ReturnSalesAdapter(list, this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            linearList.setVisibility(View.GONE);
            linearCreate.setVisibility(View.VISIBLE);
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

        returnHeaderList = new ArrayList<>();
        returnHeaderList = db.getAllListReturnHeaders(outletResponse.getIdOutlet());
        setData(returnHeaderList);
    }
}
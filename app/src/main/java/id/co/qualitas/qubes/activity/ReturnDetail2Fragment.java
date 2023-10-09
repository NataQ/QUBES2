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

import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.ReturnDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.ReturnResponse;

public class ReturnDetail2Fragment extends BaseFragment {
    private ArrayList<Return> returnDetail = new ArrayList<>();
    private ReturnResponse returnHeader = new ReturnResponse();
    private RecyclerView mRecyclerView;
    private ReturnDetailAdapter mAdapter;
//    private Button btnBack;
    private TextView txtHeader, outletName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_return_detail, container, false);

        initialize();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((MainActivity) getActivity()).changePage(17);
                        return true;
                    }
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changePage(17);
            }
        });

        setParam();
        return rootView;
    }

    private void initialize() {
        initProgress();
        db = new DatabaseHelper(getContext());
//        btnBack = rootView.findViewById(R.id.btnBack);
        imgBack = rootView.findViewById(R.id.imgBack);
        txtHeader = rootView.findViewById(R.id.txtHeader);
        returnHeader = (ReturnResponse) Helper.getItemParam(Constants.RETURN_DETAIL_CHOOSE);
        mRecyclerView = rootView.findViewById(R.id.recycler_view_return_detail);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });

        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);
        outletName = rootView.findViewById(R.id.outletName);
    }

    private void setParam() {
        txtHeader.setText(getResources().getString(R.string.noBrb) + " " + Helper.validateResponseEmpty(returnHeader.getNoBrb()) + " | " +
                getResources().getString(R.string.noReturRep) + " " + Helper.validateResponseEmpty(returnHeader.getNoRr()));

        if (returnHeader != null) {
            if (returnHeader.getId() != null) {
                returnDetail = db.getListReturnDetail(returnHeader.getId());
                if (returnDetail != null && !returnDetail.isEmpty()) {
                    for (int i = 0; i < returnDetail.size(); i++) {
                        if (returnDetail.get(i).getMaterialName() == null) {
                            if (returnDetail.get(i).getMaterialId() != null) {
                                String materialName = db.getMaterialName(returnDetail.get(i).getMaterialId());
                                if (materialName != null) {
                                    returnDetail.get(i).setMaterialName(materialName);
                                }
                            }
                        }
                    }
                }
            }
        }

        setData(returnDetail);
    }

    private void setData(ArrayList<Return> list) {
        mAdapter = new ReturnDetailAdapter(list, db);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "19");
        if (outletResponse != null) {
            if (outletResponse.getOutletName() != null) {
                outletName.setText(outletResponse.getOutletName());
            }
        }
    }
}
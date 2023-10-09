package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.MainActivityDrawer;
import id.co.qualitas.qubes.adapter.ReturnDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.ReturnResponse;

public class ReturnSalesDetailFragment extends BaseFragment {
    private ArrayList<Return> returnDetail = new ArrayList<>();
    private ReturnResponse returnHeader = new ReturnResponse();
    private RecyclerView mRecyclerView;
    private ReturnDetailAdapter mAdapter;
//    private Button btnBack;
    private TextView txtHeader, outletName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_return_detail, container, false);
        ((MainActivityDrawer)getActivity()).setTitle(getString(R.string.navmenu5dx));
        ((MainActivityDrawer)getActivity()).enableBackToolbar(true);

        initialize();
        setParam();


        return rootView;
    }

    private void initialize() {
        initProgress();
        db = new DatabaseHelper(getContext());
//        btnBack = rootView.findViewById(R.id.btnBack);
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
        txtHeader.setText(getActivity().getResources().getString(R.string.noBrb) + " " + Helper.validateResponseEmpty(returnHeader.getNoBrb()) + " | " +
                getActivity().getResources().getString(R.string.noReturRep) + " " + Helper.validateResponseEmpty(returnHeader.getNoRr()));

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (outletResponse != null) {
            if (outletResponse.getOutletName() != null) {
                outletName.setText(outletResponse.getOutletName());
            }
        }
    }
}
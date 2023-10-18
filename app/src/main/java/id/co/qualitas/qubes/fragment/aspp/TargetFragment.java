package id.co.qualitas.qubes.fragment.aspp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.aspp.TargetAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Target;
import id.co.qualitas.qubes.model.User;

public class TargetFragment extends BaseFragment {
    private TargetAdapter mAdapter;
    private List<Target> mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_target, container, false);

        getActivity().setTitle(getString(R.string.target));

        initProgress();
        initFragment();
        initialize();
        initData();

        mAdapter = new TargetAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    setEnabled(false);
//                    ((MainActivity) getActivity()).changePage(1);
                    ((MainActivity) getActivity()).backPress();
                }
            }
        });

        return rootView;
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Target("KTD R",300,78,1130,865));
        mList.add(new Target("REDBULL",204,13,196,5));
        mList.add(new Target("YCV",147,72,1760,955));
        mList.add(new Target("YCW",130,61,760,470));
        mList.add(new Target("BAT CZ",15,4,600,12));
        mList.add(new Target("BAT ALK",10,2,86,6));
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setHasFixedSize(true);
    }
}
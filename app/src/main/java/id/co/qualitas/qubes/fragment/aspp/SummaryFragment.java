package id.co.qualitas.qubes.fragment.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.activity.aspp.SummaryDetailActivity;
import id.co.qualitas.qubes.adapter.aspp.SummaryAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;

public class SummaryFragment extends BaseFragment {
    private SummaryAdapter mAdapter;
    private List<Order> mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_summary, container, false);

        getActivity().setTitle(getString(R.string.summary));

        init();
        initialize();
        initData();

        mAdapter = new SummaryAdapter(SummaryFragment.this, mList, header -> {
            Intent intent = new Intent(getActivity(), SummaryDetailActivity.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(mAdapter);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    setEnabled(false);
                    ((NewMainActivity) getActivity()).changePage(1);
                }
            }
        });

        return rootView;
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Order("ORDER000001", "1,000,000", "0WJ42 - SARI SARI (TK)", "SO0000001", "approve", "1 June 2023"));
        mList.add(new Order("ORDER000002", "2,000,000", "0WJ42 - SARI SARI (TK)", "-", "reject", "2 June 2023"));
        mList.add(new Order("ORDER000003", "3,000,000", "0WJ42 - SARI SARI (TK)", "SO0000003", "pending", "3 June 2023"));
        mList.add(new Order("ORDER000004", "4,000,000", "0WJ42 - SARI SARI (TK)", "-", "sync success", "4 June 2023"));
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "4");
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }
}
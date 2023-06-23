package id.co.qualitas.qubes.fragment.aspp;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.adapter.aspp.PromotionAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.User;

public class PromotionFragment extends BaseFragment {

    private PromotionAdapter mAdapter;
    private List<Promotion> promotionList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_promotion, container, false);

        getActivity().setTitle(getString(R.string.promotion));

        init();
        initFragment();
        initialize();
        initData();

        mAdapter = new PromotionAdapter(this, promotionList);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private void initData() {
        promotionList = new ArrayList<>();
        promotionList.add(new Promotion("Buy 7 Get 2 Red Bull Minuman Energi 250ml", "01 June 2023 - 30 June 2023"));
        promotionList.add(new Promotion("Buy 9 Get 2 Kratingdaeng Regular Minuman Energi 150ml", "01 June 2023 - 30 June 2023"));
        promotionList.add(new Promotion("Buy 2 Get 1 Kratingdaeng Regular Minuman Energi 150ml", "01 June 2023 - 30 June 2023"));
        promotionList.add(new Promotion("Buy 2 Get 1 Red Bull Minuman Energi 250ml", "01 June 2023 - 30 June 2023"));
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setHasFixedSize(true);


    }

    @Override
    public void onResume() {
        super.onResume();

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
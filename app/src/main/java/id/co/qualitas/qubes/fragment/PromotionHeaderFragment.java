package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.adapter.PromotionHeaderAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.User;

public class PromotionHeaderFragment extends BaseFragment {
    private ArrayList<OutletResponse> outletList = new ArrayList<>();
    private PromotionHeaderAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView curDate, errData;
    private ProgressBar spinner;
    private EditText searchBar;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_promotion_header, container, false);

        initialize();

        setDataHeader();

        setData(outletList);

        filter();

        return rootView;
    }

    private void filter() {
        searchBar = rootView.findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                query = query.toString().toLowerCase();
                final ArrayList<OutletResponse> filteredList = new ArrayList<>();

                for (int i = 0; i < outletList.size(); i++) {
                    final String text = outletList.get(i).getOutletName().toLowerCase() +
                            " " + outletList.get(i).getIdOutlet().toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(outletList.get(i));
                    }
                }
                setData(filteredList);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setDataHeader() {
        curDate.setText(Helper.validateResponseEmpty(user.getDateTimeNow()));
    }

    public void setData(ArrayList<OutletResponse> outletList) {

        mAdapter = new PromotionHeaderAdapter(outletList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanCount(2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        mRecyclerView = rootView.findViewById(R.id.recycler_view_retail_promotion_list);
        spinner = rootView.findViewById(R.id.progressBar);
        curDate = rootView.findViewById(R.id.curDate);
        errData = rootView.findViewById(R.id.errData);
        outletList = db.getListOutletResponse();
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    setContentToHome();
                    return true;
                }
                return false;
            }
        });
    }
}
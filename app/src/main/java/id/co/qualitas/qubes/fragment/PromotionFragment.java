package id.co.qualitas.qubes.fragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.adapter.PromotionAdapter;
import id.co.qualitas.qubes.adapter.PromotionFreeGoodsAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.PromotionFilter;
import id.co.qualitas.qubes.model.PromotionFreeGoods;
import id.co.qualitas.qubes.model.User;

public class PromotionFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    private static ArrayList<Discount> promotionResponseArraylist = new ArrayList<>();
    private static ArrayList<Discount> searchResults = new ArrayList<>();
    private static ArrayList<PromotionFreeGoods> searchFgResults = new ArrayList<>();
    private static ArrayList<PromotionFreeGoods> promotionFGResponseArraylist = new ArrayList<>();
    static ArrayList<Discount> filteredList = new ArrayList<>();
    static ArrayList<PromotionFreeGoods> filteredFGList = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    private static RecyclerView recyclerViewPromotion;
    private static RecyclerView recyclerViewPromotionFG;
    private TextView errData;
    private TextView errDataFG;
    private ProgressBar spinner, spinnerFG;
    //    private TabHost tabHost;
    private Typeface face;

    private CardView btnFilter;

    private static final String TAG1 = Constants.PROMOTION_TYPE1;
    private static final String TAG2 = Constants.PROMOTION_TYPE2;

    private PromotionFreeGoods[] promotionFreeGoods;

    private int index = 0, type = 0;

    private PromotionAdapter mAdapter;
    private PromotionFreeGoodsAdapter nAdapter;

    private TextView loadMore, loadMore2;
    private LinearLayoutManager layoutManager;

    private int visibleItemCount, totalItemCount, pastVisiblesItems;
    private boolean loading;
    private RelativeLayout layoutDiscount, layoutFreeGoods;
    private LinearLayout llDiscount, llFreeGoods;
    private TextView txtDiscount, txtFreeGoods;
    private View lineDiscount, lineFreeGoods;
    boolean searchVisible = false;
    private ImageView imgSearch;
    private EditText edtTxtSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_promotion, container, false);

        initialize();
        initProgress();

        getDataAll();

        functionTab();

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchVisible) {
                    searchVisible = false;
                    edtTxtSearch.setVisibility(View.GONE);
                } else {
                    searchVisible = true;
                    edtTxtSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        edtTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence newText, int i, int i1, int i2) {
                if (type == 0) {
                    doSearch(newText.toString().toLowerCase(Locale.getDefault()));
                } else {
                    doSearchFg(newText.toString().toLowerCase(Locale.getDefault()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return rootView;
    }

    private void functionTab() {
        llDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 0;
                lineDiscount.setVisibility(View.VISIBLE);
                lineFreeGoods.setVisibility(View.INVISIBLE);
                txtDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_blue));
                txtFreeGoods.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));

                PARAM = 1;
                new RequestUrl().execute();
                spinner.setVisibility(View.VISIBLE);
                loadMore.setVisibility(View.GONE);
                recyclerViewPromotion.setVisibility(View.GONE);
                layoutDiscount.setVisibility(View.VISIBLE);
                layoutFreeGoods.setVisibility(View.GONE);
            }
        });

        llFreeGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 1;

                lineDiscount.setVisibility(View.INVISIBLE);
                lineFreeGoods.setVisibility(View.VISIBLE);
                txtDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));
                txtFreeGoods.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_blue));

                PARAM = 2;
                new RequestUrl().execute();
                spinnerFG.setVisibility(View.VISIBLE);
                loadMore2.setVisibility(View.GONE);
                recyclerViewPromotionFG.setVisibility(View.GONE);
                layoutDiscount.setVisibility(View.GONE);
                layoutFreeGoods.setVisibility(View.VISIBLE);
            }
        });

    }

    private void getDataAll() {
        type = 0;
        lineDiscount.setVisibility(View.VISIBLE);
        lineFreeGoods.setVisibility(View.INVISIBLE);
        txtDiscount.setTextColor(ContextCompat.getColor(getActivity(), R.color.new_blue));
        txtFreeGoods.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));

        PARAM = 1;
        new RequestUrl().execute();
        spinner.setVisibility(View.VISIBLE);
        loadMore.setVisibility(View.GONE);
        recyclerViewPromotion.setVisibility(View.GONE);

    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        btnFilter = rootView.findViewById(R.id.btnFilter);
        layoutDiscount = rootView.findViewById(R.id.layoutDiscount);
        layoutFreeGoods = rootView.findViewById(R.id.layoutFreeGoods);

        imgSearch = rootView.findViewById(R.id.imgSearch);
        edtTxtSearch = rootView.findViewById(R.id.edtTxtSearch);

        llDiscount = rootView.findViewById(R.id.llDiscount);
        txtDiscount = rootView.findViewById(R.id.txtDiscount);
        lineDiscount = rootView.findViewById(R.id.lineDiscount);

        llFreeGoods = rootView.findViewById(R.id.llFreeGoods);
        txtFreeGoods = rootView.findViewById(R.id.txtFreeGoods);
        lineFreeGoods = rootView.findViewById(R.id.lineFreeGoods);

        spinner = rootView.findViewById(R.id.progressBar);
        errData = rootView.findViewById(R.id.errData);

        spinnerFG = rootView.findViewById(R.id.progressBar2);
        errDataFG = rootView.findViewById(R.id.errData2);

        recyclerViewPromotion = rootView.findViewById(R.id.recycler_view_promotion);
        recyclerViewPromotionFG = rootView.findViewById(R.id.recycler_view_promotion2);

        loadMore = rootView.findViewById(R.id.txtLoadMore);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData(promotionResponseArraylist);
            }
        });

        loadMore2 = rootView.findViewById(R.id.txtLoadMore2);
        loadMore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataFg(promotionFGResponseArraylist);
            }
        });

        setData();
        setDataFG();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preventDupeDialog();
                Helper.setItemParam(Constants.ACTIVE_MENU, getResources().getString(R.string.navmenu4));
                Helper.setItemParam(Constants.PROMOTION_TYPE, type == 0 ? Constants.PROMOTION_TYPE1 : Constants.PROMOTION_TYPE2);
                openDialog(DIALOG_FILTER);
            }
        });
    }

    private void setData() {
        spinner.setVisibility(View.GONE);
        errData.setVisibility(View.GONE);
        loadMore.setVisibility(View.VISIBLE);

        mAdapter = new PromotionAdapter(searchResults, this);
        recyclerViewPromotion.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewPromotion.setLayoutManager(layoutManager);
        recyclerViewPromotion.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPromotion.setAdapter(mAdapter);

        recyclerViewPromotion.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (index == 0) {
                    itemCount();
                }
            }
        });
    }

    private void setDataFG() {
        spinnerFG.setVisibility(View.GONE);
        errDataFG.setVisibility(View.GONE);
        loadMore2.setVisibility(View.VISIBLE);


        nAdapter = new PromotionFreeGoodsAdapter(searchFgResults, this);
        recyclerViewPromotionFG.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewPromotionFG.setLayoutManager(layoutManager);
        recyclerViewPromotionFG.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPromotionFG.setAdapter(nAdapter);

        recyclerViewPromotionFG.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (index == 0) {
                    itemCount();
                }
            }
        });
    }

    public void itemCount() {
        visibleItemCount = layoutManager.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
//                index = totalItemCount;
                insertData(promotionResponseArraylist);
            }
        }
    }

    public void insertData(List<Discount> item) {
        int temp = 10;
        if (index == 0) {
            mAdapter.notifyDataSetChanged();
            mAdapter.clearData();
            if (promotionResponseArraylist.size() - searchResults.size() < 10) {
                temp = promotionResponseArraylist.size() - searchResults.size();
            }
            for (int i = 0; i < temp; i++) {
                searchResults.add(item.get(i));
            }
            index = temp;
        } else {
            if (promotionResponseArraylist.size() - searchResults.size() < 10) {
                temp = promotionResponseArraylist.size() - searchResults.size();
            }
            for (int i = index; i < index + temp; i++) {
                Discount dt = null;
                searchResults.add(item.get(i));
            }
            index = index + temp;
        }
        if (searchResults.size() == promotionResponseArraylist.size()) {
            loadMore.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
        mAdapter.updateData(searchResults);
//        edtSearch.setText("");
        recyclerViewPromotion.setVisibility(View.VISIBLE);
        recyclerViewPromotionFG.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        errData.setVisibility(View.GONE);
        loadMore.setVisibility(View.VISIBLE);
    }

    public void insertDataFg(List<PromotionFreeGoods> item) {
        int temp = 10;
        if (index == 0) {
            nAdapter.notifyDataSetChanged();
            nAdapter.clearData();
            if (promotionFGResponseArraylist.size() - searchFgResults.size() < 10) {
                temp = promotionFGResponseArraylist.size() - searchFgResults.size();
            }
            for (int i = 0; i < temp; i++) {
                searchFgResults.add(item.get(i));
            }
            index = temp;
        } else {
            if (promotionFGResponseArraylist.size() - searchFgResults.size() < 10) {
                temp = promotionFGResponseArraylist.size() - searchFgResults.size();
            }
            for (int i = index; i < index + temp; i++) {
                searchFgResults.add(item.get(i));
            }
            index = index + temp;
        }
        if (searchFgResults.size() == promotionFGResponseArraylist.size()) {
            loadMore2.setVisibility(View.GONE);
        }
        nAdapter.notifyDataSetChanged();
        nAdapter.updateData(searchFgResults);
//        edtSearch.setText("");
        recyclerViewPromotion.setVisibility(View.GONE);
        recyclerViewPromotionFG.setVisibility(View.VISIBLE);
        spinnerFG.setVisibility(View.GONE);
        errDataFG.setVisibility(View.GONE);
        loadMore2.setVisibility(View.VISIBLE);
//        doSearch(edtSearch.getText().toString().toLowerCase(Locale.getDefault()));
    }

    @SuppressLint("StaticFieldLeak")
    private class RequestUrl extends AsyncTask<Void, Void, Discount[]> {

        @Override
        protected Discount[] doInBackground(Void... voids) {
            try {
                String URL_GET_PROMOTION;
                if (PARAM == 1) {
                    URL_GET_PROMOTION = Constants.API_GET_LIST_PROMOTION;

                    String idSalesman = Constants.QUESTION.concat(Constants.SALESMAN_ID.concat(Constants.EQUALS)) +
                            user.getIdEmployee();

                    final String url = Constants.URL.concat(URL_GET_PROMOTION).concat(idSalesman);

                    return (Discount[]) Helper.getWebservice(url, Discount[].class);
                } else {
                    URL_GET_PROMOTION = Constants.API_GET_LIST_FREE_GOODS_PROMOTION;

                    String idSalesman = Constants.QUESTION.concat(Constants.SALESMAN_ID.concat(Constants.EQUALS)) +
                            user.getIdEmployee();

                    final String url = Constants.URL.concat(URL_GET_PROMOTION).concat(idSalesman);

                    promotionFreeGoods = (PromotionFreeGoods[]) Helper.getWebservice(url, PromotionFreeGoods[].class);

                    return null;
                }
            } catch (Exception ex) {

                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Discount[] promotionResponses) {
            index = 0;
            if (PARAM == 1) {
                if (promotionResponses != null) {
                    promotionResponseArraylist = new ArrayList<>();

                    Collections.addAll(promotionResponseArraylist, promotionResponses);

                    insertData(promotionResponseArraylist);

                } else {
                    spinner.setVisibility(View.GONE);
                    errData.setVisibility(View.VISIBLE);
                }
            } else {
                if (promotionFreeGoods != null) {
                    promotionFGResponseArraylist = new ArrayList<>();

                    Collections.addAll(promotionFGResponseArraylist, promotionFreeGoods);

//                    setDataPromotionFG(promotionFGResponseArraylist);
                    insertDataFg(promotionFGResponseArraylist);

//                    spinnerFG.setVisibility(View.GONE);
//                    errDataFG.setVisibility(View.GONE);
                } else {
                    spinnerFG.setVisibility(View.GONE);
                    errDataFG.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setDataPromotion(ArrayList<Discount> mData) {
        mAdapter = new PromotionAdapter(mData, this);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewPromotion.setLayoutManager(layoutManager);
        recyclerViewPromotion.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPromotion.setAdapter(mAdapter);
    }

    private void setDataPromotionFG(ArrayList<PromotionFreeGoods> mData) {
        nAdapter = new PromotionFreeGoodsAdapter(mData, this);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewPromotionFG.setLayoutManager(layoutManager);
        recyclerViewPromotionFG.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPromotionFG.setAdapter(nAdapter);

        progress.dismiss();
    }

    @Override
    public void onResume() {
        if (Helper.getItemParam(Constants.PROMOTION_ITEM_FILTER) != null) {
            if (Helper.getItemParam(Constants.PROMOTION_TYPE) != null) {
                if (Helper.getItemParam(Constants.PROMOTION_TYPE).equals(Constants.PROMOTION_TYPE1)) {
                    filter((PromotionFilter) Helper.getItemParam(Constants.PROMOTION_ITEM_FILTER));
                } else if (Helper.getItemParam(Constants.PROMOTION_TYPE).equals(Constants.PROMOTION_TYPE2)) {
                    filterFg((PromotionFilter) Helper.getItemParam(Constants.PROMOTION_ITEM_FILTER));
                }
            }
        }

        super.onResume();
    }

    public void filter(PromotionFilter data) {
        filteredList = new ArrayList<>();
        filteredFGList = new ArrayList<>();
        int outletFiltered = 0, productFiltered = 0;
        if (data.getOutlet() == null && data.getProduct() == null && data.getValidFrom() == null && data.getValidTo() == null) {
            setDataPromotion(promotionResponseArraylist);
        } else {
            if (data.getOutlet() != null) {
                filterPromotionByOutlet(data, promotionResponseArraylist);
                outletFiltered = 1;
            }

            if (data.getProduct() != null) {
                if (filteredList.isEmpty()) {
                    if (outletFiltered != 1) {
                        filterPromotionByProduct(data, promotionResponseArraylist);
                    }
                } else {
                    filterPromotionByProduct(data, filteredList);
                }
                productFiltered = 1;
            }

            if (data.getValidFrom() != null || data.getValidTo() != null) {
                if (filteredList.isEmpty()) {
                    if (productFiltered != 1) {
                        filterPromotionByDate(data, promotionResponseArraylist);
                    }
                } else {
                    filterPromotionByDate(data, filteredList);
                }
            }

            setDataPromotion(filteredList);
        }
    }

    public void filterFg(PromotionFilter data) {
        filteredList = new ArrayList<>();
        filteredFGList = new ArrayList<>();
        int outletFiltered = 0, productFiltered = 0;
        if (data.getOutlet() == null && data.getProduct() == null && data.getValidFrom() == null && data.getValidTo() == null) {
            setDataPromotionFG(promotionFGResponseArraylist);
        } else {
            if (data.getOutlet() != null) {
                filterPromotionFGByOutlet(data, promotionFGResponseArraylist);
                outletFiltered = 1;
            }

            if (data.getProduct() != null) {
                if (filteredFGList.isEmpty()) {
                    if (outletFiltered != 1) {
                        filterPromotionFGByProduct(data, promotionFGResponseArraylist);
                    }
                } else {
                    filterPromotionFGByProduct(data, filteredFGList);
                }
                productFiltered = 1;
            }

            if (data.getValidFrom() != null || data.getValidTo() != null) {
                if (filteredFGList.isEmpty()) {
                    if (productFiltered != 1) {
                        filterPromotionFGByDate(data, promotionFGResponseArraylist);
                    }
                } else {
                    filterPromotionFGByDate(data, filteredFGList);
                }
            }

            setDataPromotionFG(filteredFGList);
        }
    }

    private static void filterPromotionByOutlet(PromotionFilter data, List<Discount> mData) {
        if (mData != null && !mData.isEmpty()) {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getCustomer() != null) {
                    if (data.getOutlet().contains(mData.get(i).getCustomer())) {
                        filteredList.add(mData.get(i));
                    }
                }
            }
        }
    }

    private static void filterPromotionFGByOutlet(PromotionFilter data, List<PromotionFreeGoods> mData) {
        if (mData != null && !mData.isEmpty()) {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getCustomer() != null) {
                    if (data.getOutlet().contains(mData.get(i).getCustomer())) {
                        filteredFGList.add(mData.get(i));
                    }
                }
            }
        }
    }

    private static void filterPromotionByProduct(PromotionFilter data, List<Discount> mData) {
        filteredList = new ArrayList<>();
        for (Discount disc : mData) {
            if (data != null && disc != null) {
                if (data.getProduct() != null && disc.getIdMaterialGroup3() != null) {
                    if (data.getProduct().contains(disc.getIdMaterialGroup3())) {
                        filteredList.add(disc);
                    }
                }
            }
        }
    }

    private static void filterPromotionFGByProduct(PromotionFilter data, List<PromotionFreeGoods> mData) {
        filteredFGList = new ArrayList<>();
        for (PromotionFreeGoods disc : mData) {
            if (data != null && disc != null) {
                if (data.getProduct() != null && disc.getProduct() != null) {
                    if (disc.getProduct().contains(data.getProduct())) {
                        filteredFGList.add(disc);
                    }
                }
            }
        }
    }

    private static void filterPromotionByDate(PromotionFilter data, List<Discount> mData) {

        if (data.getValidFrom() != null && data.getValidTo() != null) {
            Date inputDateFrom = Helper.convertStringtoDate(Constants.DATE_TYPE_15, data.getValidFrom());
            Date inputDateTo = Helper.convertStringtoDate(Constants.DATE_TYPE_15, data.getValidTo());
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getValid_from() != null && mData.get(i).getValid_to() != null) {
                    Date validDateFrom = Helper.convertStringtoDate(Constants.DATE_TYPE_15, mData.get(i).getValid_from());
                    Calendar c = Calendar.getInstance();
                    c.setTime(validDateFrom);
                    c.add(Calendar.DATE, -1);
                    validDateFrom = c.getTime();

                    Date validDateTo = Helper.convertStringtoDate(Constants.DATE_TYPE_15, mData.get(i).getValid_to());
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(validDateTo);
                    c2.add(Calendar.DATE, 1);
                    validDateTo = c.getTime();

                    if (inputDateFrom.after(validDateFrom) &&
                            inputDateTo.before(validDateTo)) {
                        filteredList.add(mData.get(i));
                    }
                }
            }
        } else if (data.getValidFrom() != null) {
            Date inputDateFrom = Helper.convertStringtoDate(Constants.DATE_TYPE_15, data.getValidFrom());
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getValid_from() != null) {
                    Date validDateFrom = Helper.convertStringtoDate(Constants.DATE_TYPE_15, mData.get(i).getValid_from());
                    Calendar c = Calendar.getInstance();
                    c.setTime(validDateFrom);
                    c.add(Calendar.DATE, -1);
                    validDateFrom = c.getTime();
                    if (inputDateFrom.after(validDateFrom)) {
                        filteredList.add(mData.get(i));
                    }
                }
            }
        } else if (data.getValidTo() != null) {
            Date inputDateTo = Helper.convertStringtoDate(Constants.DATE_TYPE_15, data.getValidTo());
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getValid_to() != null) {
                    Date validDateTo = Helper.convertStringtoDate(Constants.DATE_TYPE_15, mData.get(i).getValid_to());
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(validDateTo);
                    c2.add(Calendar.DATE, 1);
                    validDateTo = c2.getTime();
                    if (inputDateTo.before(validDateTo)) {
                        filteredList.add(mData.get(i));
                    }
                }
            }
        }
    }

    private static void filterPromotionFGByDate(PromotionFilter data, List<PromotionFreeGoods> mData) {

        if (data.getValidFrom() != null && data.getValidTo() != null) {
            Date inputDateFrom = Helper.convertStringtoDate(Constants.DATE_TYPE_15, data.getValidFrom());
            Date inputDateTo = Helper.convertStringtoDate(Constants.DATE_TYPE_15, data.getValidTo());
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getValidFrom() != null && mData.get(i).getValidTo() != null) {

                    try {
                        Calendar c = Calendar.getInstance();
                        Date validDateFrom = Helper.convertStringtoDateUS(Constants.DATE_TYPE_17, mData.get(i).getValidFrom());
                        c.setTime(validDateFrom);
                        c.add(Calendar.DATE, -1);
                        validDateFrom = c.getTime();

                        Calendar c2 = Calendar.getInstance();
                        Date validDateTo = Helper.convertStringtoDateUS(Constants.DATE_TYPE_17, mData.get(i).getValidTo());
                        c2.setTime(validDateTo);
                        c2.add(Calendar.DATE, 1);
                        validDateTo = c2.getTime();

                        if (inputDateFrom.after(validDateFrom) &&
                                inputDateTo.before(validDateTo)) {
                            filteredFGList.add(mData.get(i));
                        }
                    } catch (NullPointerException ignored) {

                    }

                }
            }
        } else if (data.getValidFrom() != null) {
            Date inputDateFrom = Helper.convertStringtoDate(Constants.DATE_TYPE_15, data.getValidFrom());
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getValidFrom() != null) {
                    try {
                        Calendar c = Calendar.getInstance();
                        Date validDateFrom = Helper.convertStringtoDateUS(Constants.DATE_TYPE_17, mData.get(i).getValidFrom());
                        c.setTime(validDateFrom);
                        c.add(Calendar.DATE, -1);
                        validDateFrom = c.getTime();
                        if (inputDateFrom.after(validDateFrom)) {
                            filteredFGList.add(mData.get(i));
                        }
                    } catch (NullPointerException ignored) {

                    }
                }
            }
        } else if (data.getValidTo() != null) {
            Date inputDateTo = Helper.convertStringtoDate(Constants.DATE_TYPE_15, data.getValidTo());
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getValidTo() != null) {
                    try {
                        Calendar c2 = Calendar.getInstance();
                        Date validDateTo = Helper.convertStringtoDateUS(Constants.DATE_TYPE_17, mData.get(i).getValidTo());
                        c2.setTime(validDateTo);
                        c2.add(Calendar.DATE, 1);
                        validDateTo = c2.getTime();
                        if (inputDateTo.before(validDateTo)) {
                            filteredFGList.add(mData.get(i));
                        }
                    } catch (NullPointerException ignored) {

                    }
                }
            }
        }
    }

    private void doSearch(String s) {
        mAdapter.filter(s);
    }

    private void doSearchFg(String s) {
        nAdapter.filter(s);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem filter = menu.findItem(R.id.action_filter);
        final MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(true);
        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.toolbarSearchHintForPromotion));
        filter.setVisible(true);

        filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                preventDupeDialog();
                Helper.setItemParam(Constants.ACTIVE_MENU, getResources().getString(R.string.navmenu4));
                Helper.setItemParam(Constants.PROMOTION_TYPE, type == 0 ? Constants.PROMOTION_TYPE1 : Constants.PROMOTION_TYPE2);
                openDialog(DIALOG_FILTER);

                return false;
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        if (tabHost != null) {
//            if (tabHost.getCurrentTabTag() != null) {
//                if (tabHost.getCurrentTabTag().equals(Constants.PROMOTION_TYPE1)) {
//                    doSearch(newText.toLowerCase(Locale.getDefault()));
//                } else {
//                    doSearchFg(newText.toLowerCase(Locale.getDefault()));
//                }
//
//            }
//        }

        if (type == 0) {
            doSearch(newText.toLowerCase(Locale.getDefault()));
        } else {
            doSearchFg(newText.toLowerCase(Locale.getDefault()));
        }

        return false;
    }
}
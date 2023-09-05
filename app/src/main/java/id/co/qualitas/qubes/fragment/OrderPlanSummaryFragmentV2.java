package id.co.qualitas.qubes.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.adapter.OrderPlanSummaryAdapterV2;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.MaterialResponse;
import id.co.qualitas.qubes.model.OrderPlanDetailRequest;
import id.co.qualitas.qubes.model.OrderPlanHeader;
import id.co.qualitas.qubes.model.TargetResponse;
import id.co.qualitas.qubes.model.TargetSummaryRequest;
import id.co.qualitas.qubes.model.UnitOfMeasure;

public class OrderPlanSummaryFragmentV2 extends BaseFragment implements SearchView.OnQueryTextListener {
    private List<MaterialResponse> listMaterial, listMaterialNew, filteredList, listUnfilteredMaterial;
    private List<OrderPlanHeader> listOrderPlan;
    private ArrayList<UnitOfMeasure> listUom;
    private OrderPlanSummaryAdapterV2 mAdapter;

    private OrderPlanHeader orderPlanHeader;

    private EditText edtIdOrderPlan, edtPlan, edtOrderDate;
//    private TextView txtTargetMonth, txtTargetCall, txtSisa, txtAchiev, txtLine,
//            txtMaterial, txtQtyUomN;

    private CardView cvDetail;

    private TextView txtTargetMonth, txtTargetCall, txtSisa, txtAchiev, txtLine,
            txtMaterial, txtQtyUomN,
            txtViewStockAwal, txtViewLastOrder, txtViewLastCheck, txtViewSuggestion,
            txtStockAwal, txtLastOrder, txtLastCheck, txtSuggestion, txtTryAgain;

    public CardView getCvDetail() {
        return cvDetail;
    }

    private ConstraintLayout topContent;

    public ConstraintLayout getTopContent() {
        return topContent;
    }

    private ConstraintLayout toolbarList;

    private ArrayList<MaterialResponse> materialResponseArrayList = new ArrayList<>();
    private ArrayList<MaterialResponse> materialResponseArrayListNew = new ArrayList<>();
    private ArrayList<OrderPlanDetailRequest> orderPlanDetailRequests = new ArrayList<>();
    private ArrayList<OrderPlanDetailRequest> orderPlanDetailRequestsNew;
    private ArrayList<OrderPlanHeader> orderPlanHeaderArrayList = new ArrayList<>();

    private String outletCode, outletName, date;

    private Spinner edtCustomer;
    private EditText edtOutlet;

    private TextInputLayout layOutlet;
    private ArrayList<OrderPlanHeader> orderPlanList = new ArrayList<>();
    private LinearLayout linearTryAgain;
    private CardView layoutTarget;

    private ImageView imgSearch;
    private Button btnNext;
    private EditText edtTxtSearch;
    boolean searchVisible = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_plan_detail_v2_new, container, false);
//        ((MainActivityDrawer) getActivity()).setTitle(getString(R.string.navmenu3az));
//        ((MainActivityDrawer) getActivity()).enableBackToolbar(true);

        initialize();
        init();
        initFragment();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (Helper.getItemParam(Constants.IS_TODAY) != null) {
                            ((NewMainActivity) getActivity()).changePage(2);
                        } else {
                            ((NewMainActivity) getActivity()).changePage(20);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.getItemParam(Constants.IS_TODAY) != null) {
                    ((NewMainActivity) getActivity()).changePage(2);
                } else {
                    ((NewMainActivity) getActivity()).changePage(20);
                }
            }
        });

        if (Helper.getItemParam(Constants.IS_TODAY) != null) {
            btnNext.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }

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

        btnNext.setText("Save");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preventDupeDialog();
                orderPlanDetailRequestsNew = new ArrayList<>();
                listMaterialNew = new ArrayList<>();

                if (listMaterial != null) {
                    if (listMaterial.size() != 0) {
                        for (int i = 0; i < listMaterial.size(); i++) {
                            if (!listMaterial.get(i).getNewQty1().equals(BigDecimal.ZERO)) {
                                if (listMaterial.get(i).getNewQty2() == null) {
                                    listMaterial.get(i).setNewQty2(BigDecimal.ZERO);
                                }
                                if (listMaterial.get(i).getPrice() == null) {
                                    listMaterial.get(i).setPrice(BigDecimal.ZERO);
                                }
                                listMaterialNew.add(listMaterial.get(i));
                            }
                        }
                    }
                }

                PARAM_DIALOG_ALERT = 1;
                Helper.setItemParam(Constants.ORDER_PLAN, listMaterialNew);
                openDialog(DIALOG_ALERT_CONFIRM);
            }
        });

        edtTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence newText, int i, int i1, int i2) {
                if (newText == null || newText.toString().trim().isEmpty()) {
                    if (listMaterial != null && !listMaterial.isEmpty()) {
                        setDataList(listMaterial);
                    }
                }
                final ArrayList<MaterialResponse> filteredList = new ArrayList<>();

                for (int j = 0; j < listMaterial.size(); j++) {
                    final String text = Helper.validateResponseEmpty(listMaterial.get(j).getIdMaterial()).toLowerCase() +
                            " " + Helper.validateResponseEmpty(listMaterial.get(j).getMaterialName()).toLowerCase();
                    if (text.contains(newText)) {
                        filteredList.add(listMaterial.get(j));
                    }
                }
                setDataList(filteredList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setData();

        return rootView;
    }

    private void setData() {
        setDataHeader();

        setDataList(listMaterial);
    }

    private void setDataList(List<MaterialResponse> mData) {
        mAdapter = new OrderPlanSummaryAdapterV2(mData, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void setDataHeader() {
        if (orderPlanHeader != null) {
            if (orderPlanHeader.getId() != null) {
                edtIdOrderPlan.setText(orderPlanHeader.getId());
            }
            if (orderPlanHeader.getOutletDate() != null) {
                edtOrderDate.setText(orderPlanHeader.getOutletDate());
            }

            if (orderPlanHeader.getOutletName() != null) {
                edtOutlet.setText(orderPlanHeader.getIdOutlet().concat(" - ").concat(orderPlanHeader.getOutletName()));
            }

            if (Helper.getItemParam(Constants.AMOUNT_PLAN) != null) {
                edtPlan.setText(String.valueOf(Helper.getItemParam(Constants.AMOUNT_PLAN)));
            }

            if (orderPlanHeader.getTargetMonth() != null) {
                txtTargetMonth.setText(Helper.toRupiahFormat(orderPlanHeader.getTargetMonth()));
            }

            if (orderPlanHeader.getTargetCall() != null) {
                txtTargetCall.setText(Helper.toRupiahFormat(orderPlanHeader.getTargetCall()));
            }

            if (orderPlanHeader.getAchiev() != null) {
                txtAchiev.setText(Helper.toRupiahFormat(orderPlanHeader.getAchiev()));
            }
        }
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        imgSearch = rootView.findViewById(R.id.imgSearch);
        btnNext = rootView.findViewById(R.id.btnNext);
        edtTxtSearch = rootView.findViewById(R.id.edtTxtSearch);
        txtTitle = rootView.findViewById(R.id.txtTitle);
        imgBack = rootView.findViewById(R.id.imgBack);
        txtTryAgain = rootView.findViewById(R.id.txtTryAgain);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        orderPlanHeader = (OrderPlanHeader) Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_HEADER);

        edtIdOrderPlan = rootView.findViewById(R.id.edtIdOp);
//        edtCustomer = rootView.findViewById(R.id.edtCustomer);
//        edtCustomer.setVisibility(View.INVISIBLE);
        edtOutlet = rootView.findViewById(R.id.edtOutlet);
        edtOrderDate = rootView.findViewById(R.id.edtOrderDate);
        edtPlan = rootView.findViewById(R.id.edtPlan);

        txtTargetMonth = rootView.findViewById(R.id.id);
        txtTargetCall = rootView.findViewById(R.id.txtCall);
        txtSisa = rootView.findViewById(R.id.txtSisa);
        txtAchiev = rootView.findViewById(R.id.plan);

        txtLine = rootView.findViewById(R.id.txtLine);
        txtMaterial = rootView.findViewById(R.id.txtMaterial);
        txtQtyUomN = rootView.findViewById(R.id.txtQtyUomN);

        cvDetail = rootView.findViewById(R.id.view_op_line_detail);
        topContent = rootView.findViewById(R.id.topContent);
        toolbarList = rootView.findViewById(R.id.toolbar_list);
        progressBar = rootView.findViewById(R.id.progressBar);

        if (Helper.getItemParam(Constants.IS_TODAY) != null) {
            if (orderPlanHeader != null) {
                if (orderPlanHeader.getIdOutlet() != null && orderPlanHeader.getOutletDate() != null) {
                    listMaterial = db.getAllOrderPlanByOutletInDate(orderPlanHeader.getIdOutlet(), orderPlanHeader.getOutletDate());

                    listUom = new ArrayList<>();
                    if (listMaterial != null && !listMaterial.isEmpty()) {
                        for (MaterialResponse material : listMaterial) {
                            if (material.getIdMaterial() != null) {
                                material.setMaterialName(db.getMaterialName(material.getIdMaterial()));

                                listUom = db.getListUomByIdMat(material.getIdMaterial(), Constants.IS_ORDER);
                                material.setListUomName(listUom);
                            }
                        }

                    }
                }
            }

        } else {
            listMaterial = (List<MaterialResponse>) Helper.getItemParam(Constants.ORDER_PLAN);
        }

        Collections.sort(listMaterial, new Comparator<MaterialResponse>() {
            @Override
            public int compare(MaterialResponse s1, MaterialResponse s2) {
                return Helper.ltrim(Helper.validateResponseEmpty(s1.getMaterialName())).compareToIgnoreCase(Helper.validateResponseEmpty(Helper.ltrim(s2.getMaterialName())));
            }
        });


//        layOutlet = rootView.findViewById(R.id.layOutlet);
//        layOutlet.setVisibility(View.VISIBLE);

        edtIdOrderPlan.setEnabled(false);
        edtPlan.setEnabled(false);
        edtOrderDate.setEnabled(false);

        txtViewStockAwal = rootView.findViewById(R.id.txtViewStockAwal);
        txtViewLastOrder = rootView.findViewById(R.id.txtViewLastOrder);

        txtViewLastCheck = rootView.findViewById(R.id.txtViewLastCheck);
        txtViewSuggestion = rootView.findViewById(R.id.txtViewSuggestion);

        txtStockAwal = rootView.findViewById(R.id.txtStockAwal);
        txtLastOrder = rootView.findViewById(R.id.txtLastOrder);
        txtLastCheck = rootView.findViewById(R.id.txtStoreCheck);
        txtSuggestion = rootView.findViewById(R.id.txtSuggestion);

        txtViewLastOrder.setVisibility(View.VISIBLE);
        txtViewLastCheck.setVisibility(View.GONE);
        txtViewSuggestion.setVisibility(View.GONE);

        txtStockAwal.setVisibility(View.GONE);
        txtLastOrder.setVisibility(View.GONE);
        txtLastCheck.setVisibility(View.GONE);
        txtSuggestion.setVisibility(View.GONE);

        txtViewStockAwal.setText("Stock tersedia");


        linearTryAgain = rootView.findViewById(R.id.linearTryAgain);
        layoutTarget = rootView.findViewById(R.id.layoutTarget);
    }


    public void viewDetailData(MaterialResponse material, int pos) {
        if (pos != -1) {
            String qty1 = Constants.EMPTY_STRING, qty2 = Constants.EMPTY_STRING, uom1 = Constants.EMPTY_STRING, uom2 = Constants.EMPTY_STRING;

            if (material.getNewQty1() != null) {
                qty1 = String.valueOf(material.getNewQty1());
            }
            if (material.getNewQty2() != null) {
                qty2 = String.valueOf(material.getNewQty2());
            }
            if (material.getNewUom1() != null) {
                uom1 = String.valueOf(material.getNewUom1());
            }
            if (material.getNewUom2() != null) {
                uom2 = String.valueOf(material.getNewUom2());
            }

//            topContent.setVisibility(View.GONE);
            cvDetail.setVisibility(View.VISIBLE);
            txtLine.setText(String.valueOf(material.getIndex()));
            if (material.getIdMaterial() != null && material.getMaterialName() != null) {
                txtMaterial.setText(material.getIdMaterial().concat(Constants.SPACE).concat(material.getMaterialName()));
            }
            txtQtyUomN.setText(qty1.concat(uom1
                    .concat(Constants.SPACE)
                    .concat(qty2.concat(uom2))));
            if (material.getStockQty1() != null && material.getStockQty2() != null && !material.getStockQty2().equals(BigDecimal.ZERO)) {
                /*Stock awal*/
                txtViewLastOrder.setText(material.getStockQty1().toString()
                        .concat(Helper.validateResponseEmpty(material.getStockUom1()))
                        .concat(Constants.SPACE)
                        .concat(material.getStockQty2().toString())
                        .concat(Helper.validateResponseEmpty(material.getStockUom2())));
            } else if (material.getStockQty1() != null) {
                txtViewLastOrder.setText(material.getStockQty1().toString()
                        .concat(Helper.validateResponseEmpty(material.getStockUom1())));
            }
        } else {
            cvDetail.setVisibility(View.GONE);
//            topContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem save = menu.findItem(R.id.action_save);
        final MenuItem search = menu.findItem(R.id.action_search);

        search.setVisible(true);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search..");

        if (Helper.getItemParam(Constants.IS_TODAY) != null) {
            save.setVisible(false);
        } else {
            save.setVisible(true);
        }

        save.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                preventDupeDialog();
                orderPlanDetailRequestsNew = new ArrayList<>();
                listMaterialNew = new ArrayList<>();

                if (listMaterial != null) {
                    if (listMaterial.size() != 0) {
                        for (int i = 0; i < listMaterial.size(); i++) {
                            if (!listMaterial.get(i).getNewQty1().equals(BigDecimal.ZERO)) {
                                if (listMaterial.get(i).getNewQty2() == null) {
                                    listMaterial.get(i).setNewQty2(BigDecimal.ZERO);
                                }
                                if (listMaterial.get(i).getPrice() == null) {
                                    listMaterial.get(i).setPrice(BigDecimal.ZERO);
                                }
                                listMaterialNew.add(listMaterial.get(i));
                            }
                        }
                    }
                }

                PARAM_DIALOG_ALERT = 1;
                Helper.setItemParam(Constants.ORDER_PLAN, listMaterialNew);
                openDialog(DIALOG_ALERT_CONFIRM);
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
        if (newText == null || newText.trim().isEmpty()) {
            if (listMaterial != null && !listMaterial.isEmpty()) {
                setDataList(listMaterial);
            }
            return false;
        }

        final ArrayList<MaterialResponse> filteredList = new ArrayList<>();

        for (int i = 0; i < listMaterial.size(); i++) {
            final String text = Helper.validateResponseEmpty(listMaterial.get(i).getIdMaterial()).toLowerCase() +
                    " " + Helper.validateResponseEmpty(listMaterial.get(i).getMaterialName()).toLowerCase();
            if (text.contains(newText)) {
                filteredList.add(listMaterial.get(i));
            }
        }
        setDataList(filteredList);
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        txtTitle.setText("Summary");
        Helper.setItemParam(Constants.CURRENTPAGE, "21");

        ArrayList<TargetResponse> targetList = new ArrayList<>();
        targetList = (ArrayList<TargetResponse>) Helper.getItemParam(Constants.TARGET_ORDER_PLAN);

        if (targetList != null) {
            if (targetList.get(0).getTargetMonth() != null) {
                txtTargetMonth.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getTargetMonth())));
            }

            if (targetList.get(0).getTargetCall() != null) {
                txtTargetCall.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getTargetCall())));
            }

            if (targetList.get(0).getGap() != null) {
                if (targetList.get(0).getGap().contains(" ")) {
                    txtSisa.setText(String.valueOf(targetList.get(0).getGap()));
                } else {
                    txtSisa.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getGap())));
                }
            }

            if (targetList.get(0).getActual() != null) {
                txtAchiev.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getTargetAchiev())));
            }
        }
//        else{
//            PARAM = 1;
//            new RequestUrl().execute();
//        }
    }

    private class RequestUrl extends AsyncTask<Void, Void, TargetResponse[]> {

        @Override
        protected TargetResponse[] doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    TargetSummaryRequest targetSummaryRequest = new TargetSummaryRequest();
                    if (orderPlanHeader != null) {
                        if (orderPlanHeader.getOutletName() != null) {
                            targetSummaryRequest.setIdOutlet(db.getOutletCode(orderPlanHeader.getOutletName()));
                        }
                    }
                    if (user != null) {
                        if (user.getIdEmployee() != null) {
                            targetSummaryRequest.setIdEmployee(user.getIdEmployee());
                        }
                    }

                    if (dayF != null) {
                        targetSummaryRequest.setDate(dayF);
                    } else {
                        if (edtOrderDate != null) {
                            targetSummaryRequest.setDate(edtOrderDate.getText().toString());
                        }
                    }
                    url = Constants.URL.concat(Constants.API_GET_TARGET_FOR_ORDER_PLAN);
                    return (TargetResponse[]) Helper.postWebserviceWithBody(url, TargetResponse[].class, targetSummaryRequest);
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("Target", ex.getMessage());
                }
                return null;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            if (PARAM == 1) {
                progressBar.setVisibility(View.VISIBLE);
                layoutTarget.setVisibility(View.INVISIBLE);
                linearTryAgain.setVisibility(View.INVISIBLE);
            } else {
                progress.show();
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(TargetResponse[] targetResponses) {

            if (PARAM == 1) {
                if (targetResponses != null) {
                    linearTryAgain.setVisibility(View.INVISIBLE);
                    layoutTarget.setVisibility(View.VISIBLE);
                    ArrayList<TargetResponse> targetList = new ArrayList<>();
                    Collections.addAll(targetList, targetResponses);
                    if (targetList.get(0).getTargetMonth() != null) {
                        txtTargetMonth.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getTargetMonth())));
                    } else {
                        txtTargetMonth.setText("0");
                    }

                    if (targetList.get(0).getTargetCall() != null) {
                        txtTargetCall.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getTargetCall())));
                    } else {
                        txtTargetCall.setText("0");
                    }

                    if (targetList.get(0).getGap() != null) {
                        if (targetList.get(0).getGap().contains(" ")) {
                            txtSisa.setText(String.valueOf(targetList.get(0).getGap()));
                        } else {
                            txtSisa.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getGap())));
                        }
                    } else {
                        txtSisa.setText("0");
                    }

                    if (targetList.get(0).getActual() != null) {
                        txtAchiev.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getActual())));
                    } else {
                        txtAchiev.setText("0");
                    }

                    Helper.setItemParam(Constants.TARGET_ORDER_PLAN, targetList);
                }
//                else {
//                    linearTryAgain.setVisibility(View.VISIBLE);
//                    layoutTarget.setVisibility(View.INVISIBLE);
//
//                    txtTryAgain.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            PARAM = 1;
//                            new RequestUrl().execute();
//                        }
//                    });
//                }
            }


            progressBar.setVisibility(View.GONE);
        }
    }
}


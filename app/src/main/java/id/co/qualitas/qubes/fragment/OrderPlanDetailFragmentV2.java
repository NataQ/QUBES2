package id.co.qualitas.qubes.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.adapter.OrderPlanDetailAdapterV2;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.GetPriceRequest;
import id.co.qualitas.qubes.model.GetPriceResponse;
import id.co.qualitas.qubes.model.MaterialResponse;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.OrderPlanHeader;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.TargetResponse;
import id.co.qualitas.qubes.model.TargetSummaryRequest;
import id.co.qualitas.qubes.model.ToPrice;
import id.co.qualitas.qubes.model.UnitOfMeasure;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;

public class OrderPlanDetailFragmentV2 extends BaseFragment {
    private List<MaterialResponse> listMaterial, listMaterialNew, listMaterialTemp, listMaterialShow;
    private List<MaterialResponse> filteredList = new ArrayList<>();
    private OrderPlanDetailAdapterV2 mAdapter;

    private static OrderPlanHeader orderPlanHeader;

    private EditText edtIdOrderPlan;
    private EditText edtPlan;
    private EditText edtOrderDate;
    private NestedScrollView scroll;
    private boolean searchData = false;

    public static EditText getEdtOutlet() {
        return edtOutlet;
    }

    @SuppressLint("StaticFieldLeak")
    private static EditText edtOutlet;
    private TextView txtTargetMonth;
    private TextView txtTargetCall;
    private TextView txtSisa;
    private TextView txtAchiev;
    private TextView txtLine;
    private TextView txtMaterial;
    private TextView txtQtyUomN;
    private TextView txtStockAwal;
    private TextView txtLastOrder;
    private TextView txtTryAgain;
    protected int index = 0;

    private CardView cvDetail;

    private ConstraintLayout topContent;

    public ConstraintLayout getTopContent() {
        return topContent;
    }

    private int valid = 0;

    private LinearLayout linearTryAgain;
    private CardView layoutTarget;
    private GetPriceResponse getPriceResponse;

    private int uomid1 = 0, uomid2 = 0;

    private VisitOrderDetailResponse requestDetail;
    private MessageResponse lastOrder;

    public static OrderPlanHeader getOrderPlanHeader() {
        return orderPlanHeader;
    }

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

//        getActivity().setTitle(getString(R.string.navmenu3a));
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
                        ((NewMainActivity) getActivity()).changePage(2);
                        return true;
                    }
                }
                return false;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean error;

                if (listMaterial != null) {
                    if (edtOutlet.getText() != null || !edtOutlet.getText().toString().equals(Constants.EMPTY_STRING)) {
                        for (int i = 0; i < listMaterial.size(); i++) {
                            error = validateMaterial(listMaterial.get(i));
                            if (!error) {
                                valid++;
                            }
                        }
                        if (valid > 0) {
                            listMaterialNew = new ArrayList<>();
                            listMaterialTemp = new ArrayList<>();
                            if (listMaterial != null && listMaterial.size() > 0) {
                                for (int i = 0; i < listMaterial.size(); i++) {
                                    if (listMaterial.get(i).getNewQty1() != null) {
                                        if ((!listMaterial.get(i).getNewQty1().equals(getResources().getString(R.string.space).trim())
                                                && listMaterial.get(i).getNewUom1() != null
                                                && !listMaterial.get(i).getNewQty1().equals(BigDecimal.ZERO))
                                        ) {
                                            listMaterialNew.add(listMaterial.get(i));
                                        }

                                        if ((!listMaterial.get(i).getNewQty1().equals(getResources().getString(R.string.space).trim())
                                                && listMaterial.get(i).getNewUom1() != null
                                                && !listMaterial.get(i).getNewQty1().equals(BigDecimal.ZERO)) || (listMaterial.get(i).getId() != null && listMaterial.get(i).isDeleted())
                                        ) {
                                            listMaterialTemp.add(listMaterial.get(i));
                                        }
                                    }
                                }

                                Helper.setItemParam(Constants.ORDER_PLAN, listMaterialNew);
                                Helper.setItemParam(Constants.ORDER_PLAN_TEMP, listMaterialTemp);
                                Helper.setItemParam(Constants.ORDER_PLAN_DETAIL_SAVE, listMaterial);
                                Helper.setItemParam(Constants.GET_DETAIL_ORDER_PLAN, Constants.ONE);
                                if (edtOutlet.getText() != null) {
                                    try {
                                        orderPlanHeader.setIdOutlet(edtOutlet.getText().toString().split(" - ")[0]);
                                        orderPlanHeader.setOutletName(edtOutlet.getText().toString().split(" - ")[1]);
                                        Helper.setItemParam(Constants.ORDER_PLAN_DETAIL_HEADER, orderPlanHeader);
                                    } catch (IndexOutOfBoundsException ignored) {

                                    }
                                } else {
                                    Toast.makeText(getContext(), R.string.warning_belum_pilih_outlet, Toast.LENGTH_SHORT).show();
                                }

                                Helper.setItemParam(Constants.AMOUNT_PLAN, edtPlan.getText().toString());

                                ((NewMainActivity) getActivity()).changePage(21);
//                                fragment = new OrderPlanSummaryFragmentV2();
//                                setContent(fragment);
                            } else {
                                Toast.makeText(getContext(), getResources().getString(R.string.empty), Toast.LENGTH_SHORT).show();
                            }
                            valid = 0;
                        } else {
                            snackBar(rootView, R.string.msg_error_field);
                        }
                    } else {
                        snackBar(rootView, R.string.msg_error_field_customer);
                    }
                }
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchVisible) {
                    searchVisible = false;
//                    searchData = false;
                    edtTxtSearch.setVisibility(View.GONE);
                    cvDetail.setVisibility(View.GONE);//??harusnya visible
                } else {
                    searchVisible = true;
                    edtTxtSearch.setVisibility(View.VISIBLE);
                    cvDetail.setVisibility(View.GONE);
                }
            }
        });

        edtTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence newText, int i, int i1, int i2) {
                searchData = true;
                if (newText == null || newText.toString().trim().isEmpty()) {
                    searchData = false;
                    if (listMaterial != null && !listMaterial.isEmpty()) {
                        listMaterialShow = new ArrayList<>();
                        listMaterialShow.addAll(getOtherData(listMaterial, true));//search kosong
//                        setMaterialData(listMaterial);//search kosong
                        sort(listMaterialShow);
                        setMaterialData(listMaterialShow);//search kosong
                    }
                } else {
                    filteredList = new ArrayList<>();
                    for (MaterialResponse material : listMaterial) {
                        final String text = (Helper.validateResponseEmpty(material.getIdMaterial())
                                .concat(getResources().getString(R.string.space))
                                .concat(Helper.validateResponseEmpty(material.getMaterialName()))).toLowerCase();

                        if (text.contains(newText)) {
                            filteredList.add(material);
                        }
                    }
                    cvDetail.setVisibility(View.GONE);
                    listMaterialShow = new ArrayList<>();
                    listMaterialShow.addAll(getOtherData(filteredList, true));//search isi
//                setMaterialData(filteredList);//search isi
                    sort(filteredList);
                    setMaterialData(listMaterialShow);//search isi
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewMainActivity) getActivity()).changePage(2);
            }
        });

        functionSelectDate();

        edtOutlet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loadDataTarget();
            }
        });

        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startAsyncTaskInParallel(RequestUrl task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }

    public void loadDataTarget() {
        PARAM = 1;
//        new RequestUrl().execute();
        startAsyncTaskInParallel(new RequestUrl());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void functionSelectDate() {
        Date nextDate = Helper.convertStringtoDate(Constants.DATE_TYPE_1, getCurrentDate());
        final Calendar c = Calendar.getInstance();
        c.setTime(nextDate);
        c.add(Calendar.DATE, 1);
        nextDate = c.getTime();

        final Date[] date = new Date[1];
        edtOrderDate.setInputType(InputType.TYPE_NULL);
        edtOrderDate.requestFocus();
        edtOrderDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        final Calendar tanggal = Calendar.getInstance();
                        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                                getContext(),
                                new DatePickerDialog.OnDateSetListener() {

                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,
                                                          int dayOfMonth) {

                                        tanggal.set(year, monthOfYear, dayOfMonth);
                                        dateString[0] = Helper
                                                .convertDateToStringNew(Constants.DATE_TYPE_1, tanggal.getTime());
                                        edtOrderDate.setText(dateString[0]);
                                        date[0] = Helper.convertStringtoDate(Constants.DATE_TYPE_1, dateString[0]);
                                    }

                                }, tanggal.get(Calendar.YEAR),
                                tanggal.get(Calendar.MONTH),
                                tanggal.get(Calendar.DAY_OF_MONTH));

                        fromDatePickerDialog.getDatePicker().setCalendarViewShown(false);
                        fromDatePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                        fromDatePickerDialog.show();
                        fromDatePickerDialog
                                .setButton(DialogInterface.BUTTON_POSITIVE, "Search", new DialogInterface
                                        .OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == DialogInterface.BUTTON_POSITIVE) {
                                            // Do Stuff
                                            DatePicker datePicker = fromDatePickerDialog.getDatePicker();
                                            String day = String.valueOf(datePicker.getDayOfMonth());
                                            String month = Helper.getMonthForInt(datePicker.getMonth());
                                            String year = String.valueOf(datePicker.getYear());
                                            dayF = year.concat("-").concat(String.format("%02d", datePicker.getMonth() + 1)).concat("-").concat(String.format("%02d", Integer.parseInt(day)));
                                            edtOrderDate.setText(dayF);

                                            List<OutletResponse> listOutlet = db.getListAvailableOutletByDate(dayF);

                                            if (listOutlet.isEmpty()) {
                                                txtTargetMonth.setText(Constants.ZERO);
                                                txtTargetCall.setText(Constants.ZERO);
                                                txtSisa.setText(Constants.ZERO);
                                                txtAchiev.setText(Constants.ZERO);
                                            }
                                            Helper.setItemParam(Constants.LIST_SELECTED_CUSTOMER_ORDER_PLAN, listOutlet);
                                            Helper.setItemParam(Constants.PARAM_FOR_DIALOG, Constants.SELECTED_CUSTOMER_ORDER_PLAN);
                                            openDialog(DIALOG_ADD_NEW_VISIT);

                                            if (orderPlanHeader == null) {
                                                orderPlanHeader = new OrderPlanHeader();
                                            }
                                            orderPlanHeader.setOutletDate(dayF);
                                        }
                                    }
                                });
                        break;
                }
                return true;
            }
        });
    }

    private Boolean validateMaterial(MaterialResponse listMaterial) {
        Boolean results = false;


        if (listMaterial.getNewQty1() == null || String.valueOf(listMaterial.getNewQty1()).trim().equals(Constants.EMPTY_STRING)) {
            results = true;
        }
        return results;
    }

    @SuppressLint("SetTextI18n")
    public void viewDetailData(MaterialResponse material, int pos) {
        if (pos != -1) {
            String qty1 = Constants.EMPTY_STRING, qty2 = Constants.EMPTY_STRING, uom1 = Constants.EMPTY_STRING, uom2 = Constants.EMPTY_STRING;

            if (material.getNewQty1() != null && material.getNewUom1() != null) {
                qty1 = String.valueOf(material.getNewQty1());
                uom1 = String.valueOf(material.getNewUom1());
            }
            if (material.getNewQty2() != null) {
                if (!material.getNewQty2().equals(BigDecimal.ZERO)) {
                    qty2 = String.valueOf(material.getNewQty2());
                }
            }
            if (material.getNewUom2() != null) {
                uom2 = String.valueOf(material.getNewUom2());
            }

            requestDetail = new VisitOrderDetailResponse();
            requestDetail.setIdMaterial(material.getIdMaterial());
            PARAM = 3;
//            new RequestUrl().execute();
            startAsyncTaskInParallel(new RequestUrl());
//            topContent.setVisibility(View.GONE);
            cvDetail.setVisibility(View.GONE);//??harusnya visible
            txtLine.setText(String.valueOf(material.getIndex()));
            if (material.getIdMaterial() != null && material.getMaterialName() != null) {
                txtMaterial.setText(material.getIdMaterial().concat(Constants.SPACE).concat(material.getMaterialName()));
            }
            txtQtyUomN.setText(qty1.concat(uom1
                    .concat(Constants.SPACE)
                    .concat(qty2.concat(uom2))));
            if (material.getStockQty1() != null && material.getStockQty2() != null && !material.getStockQty2().equals(BigDecimal.ZERO)) {
                /*Stock awal*/
                txtStockAwal.setText(material.getStockQty1().toString()
                        .concat(Helper.validateResponseEmpty(material.getStockUom1()))
                        .concat(Constants.SPACE)
                        .concat(material.getStockQty2().toString())
                        .concat(Helper.validateResponseEmpty(material.getStockUom2())));
            } else if (material.getStockQty1() != null) {
                txtStockAwal.setText(material.getStockQty1().toString()
                        .concat(Helper.validateResponseEmpty(material.getStockUom1())));
            }
        } else {
            cvDetail.setVisibility(View.GONE);
//            topContent.setVisibility(View.VISIBLE);
        }
    }

    private void setDataHeader() {
        if (orderPlanHeader.getOutletDate() != null) {
            edtOrderDate.setText(orderPlanHeader.getOutletDate());
        }

        if (orderPlanHeader.getIdOutlet() != null && orderPlanHeader.getOutletName() != null) {
            edtOutlet.setText(orderPlanHeader.getIdOutlet().concat(" - ").concat(orderPlanHeader.getOutletName()));
        }

        if (orderPlanHeader.getId() != null) {
            edtIdOrderPlan.setText(orderPlanHeader.getId());
        }

        if (orderPlanHeader.getPlan() != null) {
            edtPlan.setText(Helper.toRupiahFormat(orderPlanHeader.getPlan()));
        }

        if (orderPlanHeader.getTargetMonth() != null) {
            txtTargetMonth.setText(Helper.toRupiahFormat2(orderPlanHeader.getTargetMonth()));
        }

        if (orderPlanHeader.getTargetCall() != null) {
            txtTargetCall.setText(Helper.toRupiahFormat(orderPlanHeader.getTargetCall()));
        }

        if (orderPlanHeader.getAchiev() != null) {
            txtAchiev.setText(Helper.toRupiahFormat(orderPlanHeader.getAchiev()));
        }
    }

    private void initialize() {
        scroll = rootView.findViewById(R.id.scroll);
        imgSearch = rootView.findViewById(R.id.imgSearch);
        btnNext = rootView.findViewById(R.id.btnNext);
        edtTxtSearch = rootView.findViewById(R.id.edtTxtSearch);
        txtTitle = rootView.findViewById(R.id.txtTitle);
        imgBack = rootView.findViewById(R.id.imgBack);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        edtIdOrderPlan = rootView.findViewById(R.id.edtIdOp);
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

        TextView txtViewStockAwal = rootView.findViewById(R.id.txtViewStockAwal);
        TextView txtViewLastOrder = rootView.findViewById(R.id.txtViewLastOrder);
        TextView txtViewLastCheck = rootView.findViewById(R.id.txtViewLastCheck);
        TextView txtViewSuggestion = rootView.findViewById(R.id.txtViewSuggestion);

        txtStockAwal = rootView.findViewById(R.id.txtStockAwal);
        txtLastOrder = rootView.findViewById(R.id.txtLastOrder);
        TextView txtLastCheck = rootView.findViewById(R.id.txtStoreCheck);
        TextView txtSuggestion = rootView.findViewById(R.id.txtSuggestion);

        txtViewStockAwal.setVisibility(View.VISIBLE);
        txtViewLastOrder.setVisibility(View.VISIBLE);
        txtViewLastCheck.setVisibility(View.GONE);
        txtViewSuggestion.setVisibility(View.GONE);

        txtStockAwal.setVisibility(View.VISIBLE);
        txtLastOrder.setVisibility(View.VISIBLE);
        txtLastCheck.setVisibility(View.GONE);
        txtSuggestion.setVisibility(View.GONE);

        progressBar = rootView.findViewById(R.id.progressBar);
        linearTryAgain = rootView.findViewById(R.id.linearTryAgain);
        layoutTarget = rootView.findViewById(R.id.layoutTarget);
        txtTryAgain = rootView.findViewById(R.id.txtTryAgain);
    }

    private void getData() {
        orderPlanHeader = new OrderPlanHeader();
        if (Helper.getItemParam(Constants.GET_DETAIL_ORDER_PLAN) != null && Helper.getItemParam(Constants.FLAG_DO_ORDER_PLAN_DB) == null) {
            /*PARAM yang dilempar ke summary && param yang dari list header tidak ada*/
            if (Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_HEADER) != null) {
                orderPlanHeader = (OrderPlanHeader) Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_HEADER); /*header yang dilempar ke summary*/
            }

            if (orderPlanHeader != null) {
                setDataHeader();
                editHeaderMode(true);
            }

            if (Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_SAVE) != null) {/*list yang dilempar ke summary*/
                listMaterial = new ArrayList<>();
                listMaterial = (ArrayList<MaterialResponse>) Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_SAVE);
                if (listMaterial != null && !listMaterial.isEmpty()) {
                    sort(listMaterial);
                    listMaterialShow = new ArrayList();
                    listMaterialShow.addAll(getOtherData(listMaterial, true));////helper order plan awal, first get data
//                    setMaterialData(listMaterial);//helper order plan awal
//                    setMaterialData(listMaterialShow);//helper order plan awal
                }
            }
        } else {
            if (Helper.getItemParam(Constants.ORDER_PLAN_SELECTED) != null) {/*dari halaman header order plan yang uda kebuat*/
                orderPlanHeader = (OrderPlanHeader) Helper.getItemParam(Constants.ORDER_PLAN_SELECTED);

                editHeaderMode(false);
            } else {/*baru buat order plan*/
                if (Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_HEADER) != null) {
                    orderPlanHeader = (OrderPlanHeader) Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_HEADER); /*header yang dilempar ke summary*/
                }
                editHeaderMode(true);
            }

            if (orderPlanHeader != null) {
                setDataHeader();
            }

            listMaterial = new ArrayList<>();
            try {
                listMaterial = db.getAllMaterialOrder();
            } catch (SQLiteException ignored) {

            }

            ArrayList<UnitOfMeasure> listUom;
            if (listMaterial != null && !listMaterial.isEmpty()) {
                for (MaterialResponse material : listMaterial) {
                    listUom = db.getListUomByIdMat(material.getIdMaterial(), Constants.IS_ORDER);
                    material.setListUomName(listUom);
                }
            }

            if (Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_SAVE) != null) {/*list yang dilempar ke summary*/
                listMaterial = new ArrayList<>();
                listMaterial = (ArrayList<MaterialResponse>) Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_SAVE);
                if (listMaterial != null && !listMaterial.isEmpty()) {
                    sort(listMaterial);
                    listMaterialShow = new ArrayList();
                    listMaterialShow.addAll(getOtherData(listMaterial, true));//db order plan ke summary, first get data
//                    setMaterialData(listMaterialShow);//db order plan ke summary
//                    setMaterialData(listMaterial);//db order plan ke summary
                }
            } else {/*list yang dari halaman depan*/
                mappingListMaterialFromDb();
                sort(listMaterial);
                listMaterialShow = new ArrayList();
                listMaterialShow.addAll(getOtherData(listMaterial, true));//db order plan ke summary, first get data
//                setMaterialData(listMaterial);//db order plan ke summary
//                setMaterialData(listMaterialShow);//db order plan ke summary
            }
        }

        setMaterialData(listMaterialShow);
        functionLoad();

//        calculateAmountPlan(listMaterial);

//        if(Helper.getItemParam(Constants.SELECTED_OUTLET_ORDER_PLAN) != null){
//            orderPlanHeader.setIdOutlet(((OutletResponse)Helper.getItemParam(Constants.SELECTED_OUTLET_ORDER_PLAN)).getIdOutlet());
//        }
//        if(orderPlanHeader.getIdOutlet() != null){
//            loadDataTarget();
//        }
    }

    private List<MaterialResponse> getOtherData(List<MaterialResponse> mData, boolean first) {
        int count = 0;
        List<MaterialResponse> arrayList = new ArrayList();

        if (first) {
            index = 0;
        } else {
            index = Integer.parseInt((listMaterialShow.get(listMaterialShow.size() - 1).getIndex()));
        }

        for (int i = index; i < mData.size(); i++) {
            if (count < Constants.LIMIT_ITEM_LIST) {
                arrayList.add(mData.get(i));
                count++;
            } else {
                break;
            }
        }
        return arrayList;
    }

    class loadingSync extends AsyncTask<Void, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected Boolean doInBackground(Void... voids) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getData();
//                    return null;
                }
            });

            return true;
        }

        // can use UI thread here
        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        }
    }

    private void editHeaderMode(boolean b) {
        try {
            if (b) {
                edtOrderDate.setEnabled(true);
            } else {
                edtOrderDate.setEnabled(false);
            }
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onPause() {
        Helper.setItemParam(Constants.ORDER_PLAN_DETAIL_SAVE, listMaterial);
        super.onPause();
    }

    private void mappingListMaterialFromDb() {
        if (orderPlanHeader.getIdOutlet() != null && orderPlanHeader.getOutletDate() != null) {
            ArrayList<OrderPlanHeader> orderPlanList = db.getAllOrderPlanByOutlet(orderPlanHeader.getIdOutlet(), orderPlanHeader.getOutletDate());

            if (listMaterial != null) {
                for (int i = 0; i < listMaterial.size(); i++) {
                    if (orderPlanList != null) {
                        for (int j = 0; j < orderPlanList.size(); j++) {
                            if (listMaterial.get(i).getIdMaterial().equals(orderPlanList.get(j).getIdMaterial())) {
                                if (orderPlanHeader.getId() != null) {
                                    listMaterial.get(i).setId(orderPlanHeader.getId());
                                }

                                if (orderPlanList.get(j).getQty1() != null) {
                                    try {
                                        listMaterial.get(i).setNewQty1(new BigDecimal(orderPlanList.get(j).getQty1()));
                                    } catch (NumberFormatException ignored) {

                                    }
                                }
                                if (orderPlanList.get(j).getUom1() != null) {
                                    listMaterial.get(i).setNewUom1(orderPlanList.get(j).getUom1());
                                }
                                if (orderPlanList.get(j).getQty2() != null) {
                                    try {
                                        listMaterial.get(i).setNewQty2(new BigDecimal(orderPlanList.get(j).getQty2()));
                                    } catch (NumberFormatException ignored) {

                                    }
                                }
                                if (orderPlanList.get(j).getUom2() != null) {
                                    listMaterial.get(i).setNewUom2(orderPlanList.get(j).getUom2());
                                }
                                if (orderPlanList.get(j).getPrice() != null) {
                                    listMaterial.get(i).setPrice(orderPlanList.get(j).getPrice());
                                }

                            }
                        }
                    }
                }
            }

        }
    }

    private void functionLoad() {
        try {
            scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    View view = (View) scroll.getChildAt(scroll.getChildCount() - 1);
                    int diff = (view.getBottom() - (scroll.getHeight() + scroll.getScrollY()));
                    if (diff == 0) {
                        try {
                            try {
                                if (!searchData) {
                                    listMaterialShow.addAll(getOtherData(listMaterial, false));//load data, no search
                                } else {
                                    listMaterialShow.addAll(getOtherData(filteredList, false));//load data, with search
                                }
                                mAdapter.notifyDataSetChanged();
                            } catch (NumberFormatException e) {

                            }
                        } catch (IndexOutOfBoundsException ignored) {

                        }
                    }
                }
            });
        } catch (NullPointerException ignored) {

        }
    }

    private void setMaterialData(List<MaterialResponse> mData) {
//        sort(mData);
        mAdapter = new OrderPlanDetailAdapterV2(listMaterialShow, OrderPlanDetailFragmentV2.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter.notifyDataSetChanged();

//        calculateAmountPlan(listMaterial);
        calculateAmountPlan(mData);
    }

    private void sort(List<MaterialResponse> data) {
        Collections.sort(data, new Comparator<MaterialResponse>() {

            @Override
            public int compare(MaterialResponse o1, MaterialResponse o2) {
                if (o1.getNewQty1() == null) {
                    return (o2.getNewQty1() == null) ? 0 : -1;
                }
                if (o2.getNewQty1() == null) {
                    return 1;
                }
                return o2.getNewQty1().compareTo(o1.getNewQty1());
            }
        });

        Collections.reverse(data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem search = menu.findItem(R.id.action_search);
        final MenuItem next = menu.findItem(R.id.action_next);

        search.setVisible(true);
        next.setVisible(true);
        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.toolbarSearchHint));
        search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
//                topContent.setVisibility(View.VISIBLE);
                cvDetail.setVisibility(View.GONE);
                return false;
            }
        });

        next.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Boolean error;

                if (listMaterial != null) {
                    if (edtOutlet.getText() != null || !edtOutlet.getText().toString().equals(Constants.EMPTY_STRING)) {
                        for (int i = 0; i < listMaterial.size(); i++) {
                            error = validateMaterial(listMaterial.get(i));
                            if (!error) {
                                valid++;
                            }
                        }
                        if (valid > 0) {
                            listMaterialNew = new ArrayList<>();
                            listMaterialTemp = new ArrayList<>();
                            if (listMaterial != null && listMaterial.size() > 0) {
                                for (int i = 0; i < listMaterial.size(); i++) {
                                    if (listMaterial.get(i).getNewQty1() != null) {
                                        if ((!listMaterial.get(i).getNewQty1().equals(getResources().getString(R.string.space).trim())
                                                && listMaterial.get(i).getNewUom1() != null
                                                && !listMaterial.get(i).getNewQty1().equals(BigDecimal.ZERO))
                                        ) {
                                            listMaterialNew.add(listMaterial.get(i));
                                        }

                                        if ((!listMaterial.get(i).getNewQty1().equals(getResources().getString(R.string.space).trim())
                                                && listMaterial.get(i).getNewUom1() != null
                                                && !listMaterial.get(i).getNewQty1().equals(BigDecimal.ZERO)) || (listMaterial.get(i).getId() != null && listMaterial.get(i).isDeleted())
                                        ) {
                                            listMaterialTemp.add(listMaterial.get(i));
                                        }
                                    }
                                }

                                Helper.setItemParam(Constants.ORDER_PLAN, listMaterialNew);
                                Helper.setItemParam(Constants.ORDER_PLAN_TEMP, listMaterialTemp);
                                Helper.setItemParam(Constants.ORDER_PLAN_DETAIL_SAVE, listMaterial);
                                Helper.setItemParam(Constants.GET_DETAIL_ORDER_PLAN, Constants.ONE);
                                if (edtOutlet.getText() != null) {
                                    try {
                                        orderPlanHeader.setIdOutlet(edtOutlet.getText().toString().
                                                split(" - ")[0]);
                                        orderPlanHeader.setOutletName(edtOutlet.getText().toString().
                                                split(" - ")[1]);
                                        Helper.setItemParam(Constants.ORDER_PLAN_DETAIL_HEADER, orderPlanHeader);
                                    } catch (IndexOutOfBoundsException ignored) {

                                    }
                                } else {
                                    Toast.makeText(getContext(), R.string.warning_belum_pilih_outlet, Toast.LENGTH_SHORT).show();
                                }

                                Helper.setItemParam(Constants.AMOUNT_PLAN, edtPlan.getText().toString());

                                ((NewMainActivity) getActivity()).changePage(21);
//                                fragment = new OrderPlanSummaryFragmentV2();
//                                setContent(fragment);
                            } else {
                                Toast.makeText(getContext(), getResources().getString(R.string.empty),
                                        Toast.LENGTH_SHORT).show();
                            }
                            valid = 0;
                        } else {
                            snackBar(rootView, R.string.msg_error_field);
                        }
                    } else {
                        snackBar(rootView, R.string.msg_error_field_customer);
                    }


                }
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
                setMaterialData(listMaterial);//search gak ke pake
            }
            return false;
        }

        final ArrayList<MaterialResponse> filteredList = new ArrayList<>();
        for (MaterialResponse material : listMaterial) {
            final String text = (Helper.validateResponseEmpty(material.getIdMaterial())
                    .concat(getResources().getString(R.string.space))
                    .concat(Helper.validateResponseEmpty(material.getMaterialName()))).toLowerCase();

            if (text.contains(newText)) {
                filteredList.add(material);
            }
        }
        cvDetail.setVisibility(View.GONE);
//        topContent.setVisibility(View.GONE);
        setMaterialData(filteredList);//search gak ke pake
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private class RequestUrl extends AsyncTask<Void, Void, TargetResponse[]> {

        @Override
        protected TargetResponse[] doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    TargetSummaryRequest targetSummaryRequest = new TargetSummaryRequest();
                    if (orderPlanHeader != null) {
                        if (orderPlanHeader.getIdOutlet() == null) {
                            if (orderPlanHeader.getOutletName() != null) {
                                targetSummaryRequest.setIdOutlet(db.getOutletCode(orderPlanHeader.getOutletName()));
                            }
                        } else {
                            targetSummaryRequest.setIdOutlet(orderPlanHeader.getIdOutlet());
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
                } else if (PARAM == 2) {

                    GetPriceRequest getPriceRequest = new GetPriceRequest();
                    getPriceRequest.setIdMaterial(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getIdMaterial());
                    if (edtOutlet.getText() != null) {
//                        getPriceRequest.setIdOutlet(db.getOutletCode(edtOutlet.getText().toString()));
                        try {
                            getPriceRequest.setIdOutlet(edtOutlet.getText().toString().
                                    split(" - ")[0]);
                        } catch (IndexOutOfBoundsException e) {

                        }
                    }
                    getPriceRequest.setQty1(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getNewQty1());

                    if (((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName() != null &&
                            !((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName().isEmpty()) {
                        for (int i = 0; i < ((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName().size(); i++) {
                            if (((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName().get(i).getId() != null) {
                                if (((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName().get(i).getUomName()
                                        .equals(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getNewUom1())) {
                                    getPriceRequest.setUom1(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName().get(i).getId());
                                    uomid1 = 1;
                                    break;
                                }
                            }
                        }
                    }

                    if (uomid1 == 0) {
                        getPriceRequest.setUom1(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getNewUom1());
                    } else {
                        uomid1 = 0;
                    }
                    if (((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName() != null &&
                            !((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName().isEmpty()) {
                        for (int i = 0; i < ((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName().size(); i++) {
                            if (((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName().get(i).getId() != null) {
                                if (((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName().get(i).getUomName()
                                        .equals(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getNewUom2())) {
                                    getPriceRequest.setUom2(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getListUomName().get(i).getId());
                                    uomid1 = 1;
                                    break;
                                }
                            }
                        }
                    }
                    if (uomid2 == 0) {
                        getPriceRequest.setUom2(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getNewUom2());
                    } else {
                        uomid2 = 0;
                    }

                    if (((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getNewQty2() != null) {
                        getPriceRequest.setQty2(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getNewQty2());
                    } else {
                        getPriceRequest.setQty2(BigDecimal.ZERO);
                        getPriceRequest.setUom2(null);
                    }

                    final String url = Constants.URL.concat(Constants.API_GET_ORDER_PLAN_PRICING);
                    getPriceResponse = (GetPriceResponse) Helper.postWebserviceWithBody(url, GetPriceResponse.class, getPriceRequest);
                    return null;
                } else {
                    String URL_GET = Constants.API_GET_LAST_ORDER;

                    try {
                        requestDetail.setIdOutlet(db.getOutletCode(edtOutlet.getText().toString()));
                    } catch (NullPointerException ignored) {

                    }
                    requestDetail.setIdSalesman(user.getIdEmployee());

                    final String url = Constants.URL.concat(URL_GET);
                    lastOrder = (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, requestDetail);
                    return null;
                }
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            if (PARAM == 1) {
                progressBar.setVisibility(View.VISIBLE);
                layoutTarget.setVisibility(View.GONE);
                linearTryAgain.setVisibility(View.GONE);
            } else {
                progress.show();
            }
            super.onPreExecute();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(TargetResponse[] targetResponses) {

            if (PARAM == 1) {
                if (targetResponses != null) {
                    linearTryAgain.setVisibility(View.GONE);
                    layoutTarget.setVisibility(View.VISIBLE);
                    ArrayList<TargetResponse> targetList = new ArrayList<>();
                    Collections.addAll(targetList, targetResponses);
                    if (targetList.get(0).getTargetMonth() != null) {
                        txtTargetMonth.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getTargetMonth())));
                    } else {
                        txtTargetMonth.setText(Constants.ZERO);
                    }

                    if (targetList.get(0).getTargetCall() != null) {
                        txtTargetCall.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getTargetCall())));
                    } else {
                        txtTargetCall.setText(Constants.ZERO);
                    }

                    if (targetList.get(0).getGap() != null) {
                        if (targetList.get(0).getGap().contains(" ")) {
                            txtSisa.setText(String.valueOf(targetList.get(0).getGap()));
                        } else {
                            txtSisa.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getGap())));
                        }
                    } else {
                        txtSisa.setText(Constants.ZERO);
                    }

                    if (targetList.get(0).getActual() != null) {
                        txtAchiev.setText(Helper.toRupiahFormat(String.valueOf(targetList.get(0).getActual())));
                    } else {
                        txtAchiev.setText(Constants.ZERO);
                    }

                    Helper.setItemParam(Constants.TARGET_ORDER_PLAN, targetList);
                } else {
                    linearTryAgain.setVisibility(View.VISIBLE);
                    layoutTarget.setVisibility(View.GONE);

                    txtTryAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PARAM = 1;
//                            new RequestUrl().execute();

                            startAsyncTaskInParallel(new RequestUrl());
                        }
                    });
                }
            } else if (PARAM == 2) {
                if (getPriceResponse != null) {
                    ArrayList<ToPrice> listToPrice = new ArrayList<>();

                    if (getPriceResponse.getListToPrice() != null) {
                        Collections.addAll(listToPrice, getPriceResponse.getListToPrice());
                    }

                    /*New*/
                    if (listMaterial != null && !listMaterial.isEmpty()) {
                        for (int i = 0; i < listMaterial.size(); i++) {
                            if (listMaterial.get(i).getIdMaterial().equals(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getIdMaterial())) {

                                if (!listToPrice.isEmpty()) {
                                    for (int j = 0; j < listToPrice.size(); j++) {
                                        if (j == listToPrice.size() - 1) {
                                            listMaterial.get(i).setPrice(listToPrice.get(j).getAmount());

                                        }
                                    }
                                } else {
                                    listMaterial.get(i).setPrice(new BigDecimal(0));
                                }

                            }
                        }
                    }

                    mAdapter.notifyItemChanged(((MaterialResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL)).getPos());
                    calculateAmountPlan(listMaterial);

                }
                progress.dismiss();
            } else {
                if (lastOrder != null) {
                    if (lastOrder.getIdMessage() == 1) {
                        if (lastOrder.getResult() != null) {
                            if (lastOrder.getResult().getLastQty1() != null && lastOrder.getResult().getLastUom1() != null) {
                                txtLastOrder.setText(lastOrder.getResult().getLastQty1().toString().concat(Constants.SPACE).concat(lastOrder.getResult().getLastUom1()));
                            }
                        }
                    }
                }
                progress.dismiss();
            }


            progressBar.setVisibility(View.GONE);
        }
    }

    public void requestPriceN() {
        if (isNetworkAvailable()) {
            PARAM = 2;
            startAsyncTaskInParallel(new RequestUrl());
//            new RequestUrl().execute();
        }

    }

    public void calculateAmountPlan(List<MaterialResponse> listMaterial) {
        BigDecimal totalPlan = BigDecimal.ZERO;

        if (listMaterial != null && !listMaterial.isEmpty()) {
            for (MaterialResponse material : listMaterial) {
                totalPlan = totalPlan.add(material.getPrice() != null ? material.getPrice() : BigDecimal.ZERO);
            }
        }

        edtPlan.setText(Helper.toRupiahFormat2(String.valueOf(totalPlan)));
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
//        txtTitle.setText(getString(R.string.navmenu3a));
        Helper.setItemParam(Constants.CURRENTPAGE, "20");
        new loadingSync().execute();
//        getData();

//        if (getView() != null) {
//            getView().setFocusableInTouchMode(true);
//            getView().requestFocus();
//            getView().setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
//                }
//            });
//        }
    }
}
package id.co.qualitas.qubes.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.adapter.VisitOrderDetailAdapterV2;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.FreeGoods;
import id.co.qualitas.qubes.model.GetPriceRequest;
import id.co.qualitas.qubes.model.GetPriceResponse;
import id.co.qualitas.qubes.model.JenisJualandTop;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.ToPrice;
import id.co.qualitas.qubes.model.Uom;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;
import id.co.qualitas.qubes.model.VisitOrderRequest;

public class OrderFragment extends BaseFragment implements SearchView.OnQueryTextListener {

    private ArrayList<VisitOrderDetailResponse> visitOrderDetailResponseList, visitOrderDetailResponseListNew = new ArrayList<>();
    private ArrayList<ToPrice> listToPrice = new ArrayList<>();
    private ArrayList<ToPrice> listOneTimeDiscount = new ArrayList<>();
    private ArrayList<Uom> listUom = new ArrayList<>();
    private ArrayList<JenisJualandTop> listJenisJual = new ArrayList<>();
    private ArrayList<ArrayList<ToPrice>> allListToPrice = new ArrayList<>();

    private VisitOrderDetailResponse storeCheck = new VisitOrderDetailResponse();
    private VisitOrderDetailResponse visitOrderDetailNew = new VisitOrderDetailResponse();
    private GetPriceResponse getPriceResponse;
    private VisitOrderDetailResponse orderDetail;

    public VisitOrderHeader getSavedHeader() {
        return savedHeader;
    }

    private VisitOrderHeader savedHeader;
    private OutletResponse outletResponse;
    private VisitOrderDetailResponse orderPlanMaterial = new VisitOrderDetailResponse();

    private VisitOrderDetailAdapterV2 mAdapter;

    private Button btnNext, btnCancel, btnGetPrice;
    private Spinner spinnerClassification;
    private int flag = 0;

    public ScrollView getScroll() {
        return scroll;
    }

    private ScrollView scroll;

    private String errorMsg;
    private String idMaterial;
    private int invalid = 0;

    private FreeGoods[] freeGoods;
    private List<ToPrice> listAllOneTimeDiscount;

    private ArrayList<FreeGoods> listFreeGoods = new ArrayList<>();

    private VisitOrderDetailResponse priceRequest = new VisitOrderDetailResponse();

    private GetPriceRequest getPriceRequest = new GetPriceRequest();

    private boolean animationStarted = false;

    private int uomid1 = 0, uomid2 = 0;

    private EditText txtOutlet, txtDate, txtTotal;

    private FloatingActionButton btnNewMaterial;
//    private Button btnNewMaterial;

    private ConstraintLayout topContent;
    private CardView cvDetail;

    private com.rey.material.widget.Button btnSpecimen;

    private VisitOrderDetailResponse requestDetail;

    private MessageResponse lastOrder;

    private String qty1 = Constants.EMPTY_STRING, qty2 = Constants.EMPTY_STRING, uom1 = Constants.EMPTY_STRING, uom2 = Constants.EMPTY_STRING;

    private TextView txtTargetMonth, txtTargetCall, txtSisa, txtAchiev, txtLine,
            txtMaterial, txtQtyUomN,
            txtViewStockAwal, txtViewLastOrder, txtViewLastCheck, txtViewSuggestion,
            txtStockAwal, txtLastOrder, txtLastCheck, txtSuggestion;

    private String lastOrder1 = Constants.EMPTY_STRING, lastOrder2 = Constants.EMPTY_STRING;

    private VisitOrderHeader header;

    private List<VisitOrderHeader> listOrderHeader;

    private EditText edtId;

    private BigDecimal resultAfterConv = BigDecimal.ZERO;

    private CheckBox cbFixedPrice;

    private int wait = 0;

    private long mLastClickTime = 0;

    private TextView viewDetailZero;
    private HorizontalScrollView viewDetailSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void enableButtonAdd(boolean bool) {
        btnNewMaterial.setEnabled(bool);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, container, false);

//        ((MainActivityDrawer) getActivity()).setTitle(getString(R.string.navmenu5c));
//        ((MainActivityDrawer) getActivity()).enableBackToolbar(true);

        initProgress();
        initialize();
        scrollViewHacks();

        setDataHeader();

        btnNewMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress != null){
                    if(progress.isShowing()){
                        return;
                    }
                }

                if (alertDialog != null) {
                    if (alertDialog.isShowing()) return;
                }

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();

                Helper.removeItemParam(Constants.STORE_CHECK_DETAIL);
                Helper.removeItemParam(Constants.RETURN_DETAIL);
                Helper.setItemParam(Constants.VISIT_ORDER_DETAIL, visitOrderDetailResponseList);

                openDialog(DIALOG_ADD_MATERIAL);
            }
        });

        btnSpecimen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.removeItemParam(Constants.OUTLET_CODE);
                try {
                    Helper.setItemParam(Constants.OUTLET_CODE, outletResponse.getIdOutlet());
                } catch (NullPointerException ignored) {

                }
                Fragment fr = new ImageDetailFragment();
                setContent(fr);
            }
        });


        if (savedHeader != null) {
            if (savedHeader.getId() != null) {
                if (savedHeader.getId().contains(Constants.KEY_TRUE_ID_TO)) {
                    cbFixedPrice.setEnabled(false);
                }

                if (db.checkHargaJadi(savedHeader.getId())) {
                    cbFixedPrice.setChecked(true);
                } else {
                    cbFixedPrice.setChecked(false);
                }
            }
        }

        cbFixedPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    header.setHargaJadi(true);
                } else {
                    header.setHargaJadi(false);
                }
            }
        });



        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void scrollViewHacks() {
        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scroll.setFocusable(true);
        scroll.setFocusableInTouchMode(true);
        scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.requestFocusFromTouch();
//                scroll.clearFocus();
                return false;
            }

        });

    }

    private void setDataHeader() {
        if (outletResponse != null) {
            txtOutlet.setText(Helper.validateResponseEmpty(outletResponse.getIdOutlet()).concat(Constants.STRIP)
                    .concat(Helper.validateResponseEmpty(outletResponse.getOutletName())));
            if (user.getCurrentDate() != null) {
                txtDate.setText(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
            }
        }
    }

    public void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        linearLayoutList = rootView.findViewById(R.id.linearLayoutList);
        btnNewMaterial = rootView.findViewById(R.id.btnAdd);
        txtEmpty = rootView.findViewById(R.id.txtViewEmpty);
        progressBar = rootView.findViewById(R.id.progressBar);
        savedHeader = (VisitOrderHeader) Helper.getItemParam(Constants.ORDER_HEADER_SELECTED);

        scroll = rootView.findViewById(R.id.r3);
        btnGetPrice = rootView.findViewById(R.id.btnGetPrice);
        visitOrderDetailResponseList = (ArrayList<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);

        txtOutlet = rootView.findViewById(R.id.edtOutlet);
        txtDate = rootView.findViewById(R.id.edtOrderDate);
        txtTotal = rootView.findViewById(R.id.edtTotal);

        txtLine = rootView.findViewById(R.id.txtLine);
        txtMaterial = rootView.findViewById(R.id.txtMaterial);
        txtQtyUomN = rootView.findViewById(R.id.txtQtyUomN);

        cvDetail = rootView.findViewById(R.id.view_op_line_detail);
        topContent = rootView.findViewById(R.id.topContent);

        btnSpecimen = rootView.findViewById(R.id.btnSpecimen);

        txtViewStockAwal = rootView.findViewById(R.id.txtViewStockAwal);
        txtViewLastOrder = rootView.findViewById(R.id.txtViewLastOrder);
        txtViewLastCheck = rootView.findViewById(R.id.txtViewLastCheck);
        txtViewSuggestion = rootView.findViewById(R.id.txtViewSuggestion);

        txtStockAwal = rootView.findViewById(R.id.txtStockAwal);
        txtLastOrder = rootView.findViewById(R.id.txtLastOrder);
        txtLastCheck = rootView.findViewById(R.id.txtStoreCheck);
        txtSuggestion = rootView.findViewById(R.id.txtSuggestion);

        if (Helper.getItemParam(Constants.ACCESS_POINT) != null) {
            if (Helper.getItemParam(Constants.ACCESS_POINT).equals(getResources().getString(R.string.navmenu6b))) {
                if (((VisitOrderHeader) Helper.getItemParam(Constants.SO_HEADER_SELECTED)).getIdOutlet() != null &&
                        ((VisitOrderHeader) Helper.getItemParam(Constants.SO_HEADER_SELECTED)).getId() != null) {
                    header = db.getOrderHeaderById(((VisitOrderHeader) Helper.getItemParam(Constants.SO_HEADER_SELECTED)).getId());
                    savedHeader = db.getOrderHeaderById(((VisitOrderHeader) Helper.getItemParam(Constants.SO_HEADER_SELECTED)).getId());
                    Helper.setItemParam(Constants.ORDER_HEADER_SELECTED, savedHeader);
                }

                if (header.getIdOutlet() != null) {
                    outletResponse = new OutletResponse();
                    outletResponse.setIdOutlet(header.getIdOutlet());
                    outletResponse.setOutletName(db.getOutletName(header.getIdOutlet()));
                }


            } else {
                header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
                savedHeader = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);

            }
        } else {
            header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
            if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                if (((VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER)).getId() != null) {
                    savedHeader = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
                }
            }
            outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);

        }

        if (Helper.getItemParam(Constants.VISIT_ORDER_DETAIL) == null) {
            getData();
        }

        listOrderHeader = db.getAllListOrderHeader();


        edtId = rootView.findViewById(R.id.edtIdOp);

        if (savedHeader != null) {
            if (savedHeader.getId() != null) {
                edtId.setText(savedHeader.getId());
            }
        }

        cbFixedPrice = rootView.findViewById(R.id.cbFixedPrice);

        viewDetailZero = rootView.findViewById(R.id.view_detail_zero);
        viewDetailSelected = rootView.findViewById(R.id.view_detail_selected);
    }

    public void setDateFilter(List<VisitOrderDetailResponse> filteredList) {
        linearLayoutList.removeAllViews();
        if (Helper.getItemParam(Constants.ORDER_TYPE) != null) {
//            mAdapter = new VisitOrderDetailAdapterV2(this, filteredList, Helper.getItemParam(Constants.ORDER_TYPE).toString());
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View item = mAdapter.getView(i, null, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 15);
            linearLayoutList.addView(item, layoutParams);
        }

//        View item = mAdapter.getView(mAdapter.getCount(), null, null);
//        linearLayoutList.addView(item);

        mAdapter.notifyDataSetChanged();
    }

    public void deleteDetail(int position) {
        showDetail(false);
        if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
            visitOrderDetailResponseList.remove(position);
            setMaterialData(visitOrderDetailResponseList);
            calculateTotal(visitOrderDetailResponseList);
        }

    }

    private class RequestUrl extends AsyncTask<Void, Void, VisitOrderDetailResponse[]> {

        @Override
        protected VisitOrderDetailResponse[] doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_GET = Constants.API_GET_ORDER_DETAIL;

                    String idOutlet =
                            Constants.QUESTION.concat(Constants.OUTLET_CODE.concat(Constants.EQUALS)) +
                                    outletResponse.getIdOutlet();

                    final String url = Constants.URL.concat(URL_GET).concat(idOutlet);
                    return (VisitOrderDetailResponse[]) Helper
                            .getWebservice(url, VisitOrderDetailResponse[].class);
                } else if (PARAM == 2) {
                    //new
                    String URL_GET = Constants.API_GET_ORDER_DETAIL_NEW;

                    String idOutlet = Constants.QUESTION.concat(Constants.OUTLET_CODE.concat(Constants.EQUALS)) + outletResponse.getIdOutlet();

                    String idMaterialString = Constants.AND.concat(Constants.ID_MATERIAL.concat(Constants.EQUALS)) + idMaterial;

                    String idSalesman = Constants.AND.concat(Constants.SALESMAN_ID).concat(Constants.EQUALS) + user.getIdEmployee();

                    final String url = Constants.URL.concat(URL_GET).concat(idOutlet).concat(idMaterialString).concat(idSalesman);
                    visitOrderDetailNew = (VisitOrderDetailResponse) Helper.getWebservice(url, VisitOrderDetailResponse.class);
                    return null;
                } else if (PARAM == 3) {//new
                    String URL_GET = Constants.API_GET_ORDER_DETAIL_2;

                    String idOutlet = Constants.QUESTION.concat(Constants.OUTLET_CODE.concat(Constants.EQUALS)) + outletResponse.getIdOutlet();

                    String idSalesman = Constants.AND.concat(Constants.SALESMAN_ID.concat(Constants.EQUALS)) + user.getIdEmployee();

                    final String url = Constants.URL.concat(URL_GET).concat(idOutlet).concat(idSalesman);
                    return (VisitOrderDetailResponse[]) Helper
                            .getWebservice(url, VisitOrderDetailResponse[].class);
                } else if (PARAM == 4) {//new
                    String URL_GET_PRICING = Constants.API_GET_PRICING;

                    VisitOrderDetailResponse priceRequest = (VisitOrderDetailResponse) Helper.getItemParam(Constants.PRICE_REQ);

                    ArrayList<VisitOrderDetailResponse> requestArrayList = new ArrayList<>();
                    requestArrayList.add(priceRequest);

                    final String url = Constants.URL.concat(URL_GET_PRICING);
                    return (VisitOrderDetailResponse[]) Helper
                            .postWebserviceWithBody(url, VisitOrderDetailResponse[].class, requestArrayList);
                } else if (PARAM == 5) {
                    String URL_GET = Constants.API_GET_LIST_TO_DETAIL_ID;

                    String idTo = Constants.QUESTION.concat(Constants.ID_TO.concat(Constants.EQUALS)).concat(savedHeader.getId());

                    String idOutlet = Constants.AND.concat(Constants.OUTLET_CODE.concat(Constants.EQUALS)) + outletResponse.getIdOutlet();

                    String idSalesman = Constants.AND.concat(Constants.SALESMAN_ID.concat(Constants.EQUALS)) + user.getIdEmployee();

                    final String url = Constants.URL.concat(URL_GET).concat(idTo).concat(idOutlet).concat(idSalesman);
                    return (VisitOrderDetailResponse[]) Helper.getWebservice(url, VisitOrderDetailResponse[].class);
                } else if (PARAM == 6) {
                    String URL_GET = Constants.API_GET_PRICING;

                    mappingRequestDataPricing();

                    wait = 1;

                    final String url = Constants.URL.concat(URL_GET);
                    getPriceResponse = (GetPriceResponse) Helper.postWebserviceWithBody(url, GetPriceResponse.class, getPriceRequest);
                    return null;
                } else if (PARAM == 7) {
                    String URL_GET = Constants.API_GET_FREE_GOODS;

                    VisitOrderRequest order = new VisitOrderRequest();

                    VisitOrderHeader header = new VisitOrderHeader();
                    header.setIdOutlet(outletResponse.getIdOutlet());
                    order.setOrderHeader(header);
                    order.setOrderDetail(visitOrderDetailResponseListNew);
                    listAllOneTimeDiscount = new ArrayList<>();

                    final String url = Constants.URL.concat(URL_GET);
                    freeGoods = (FreeGoods[]) Helper.postWebserviceWithBody(url, FreeGoods[].class, order);
                    return null;
                } else {
                    String URL_GET = Constants.API_GET_LAST_ORDER;

                    requestDetail.setIdOutlet(outletResponse.getIdOutlet());
                    requestDetail.setIdSalesman(user.getIdEmployee());

                    final String url = Constants.URL.concat(URL_GET);
                    lastOrder = (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, requestDetail);
                    return null;
                }
            } catch (Exception ex) {
                //connection = true;
                if (ex.getMessage() != null) {
                    Log.e("Get data", ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }

        @Override
        protected void onPostExecute(VisitOrderDetailResponse[] visitOrderDetailResponse) {


            if (PARAM == 1) {
                if (visitOrderDetailResponse != null) {
                    visitOrderDetailResponseList = new ArrayList<>();
                    Collections.addAll(visitOrderDetailResponseList, visitOrderDetailResponse);
                    txtEmpty.setVisibility(View.GONE);
                    Helper.setItemParam(Constants.SIZE_VISIT_ORDER_DETAIL, visitOrderDetailResponseList.size());
                    setMaterialData(visitOrderDetailResponseList);
                } else {
                    txtEmpty.setVisibility(View.VISIBLE);
                }
            } else if (PARAM == 2) {
                if (visitOrderDetailNew != null) {
                    visitOrderDetailResponseList = new ArrayList<>();
                    if (Helper.getItemParam(Constants.VISIT_ORDER_DETAIL) != null) {
                        visitOrderDetailResponseList = (ArrayList<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);
                        Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
                        visitOrderDetailResponseList.add(visitOrderDetailNew);
                    } else {
                        visitOrderDetailResponseList.add(visitOrderDetailNew);
                    }
                    setMaterialData(visitOrderDetailResponseList);
                } else {
                    Toast.makeText(getContext(), R.string.errorData, Toast.LENGTH_LONG).show();
                    setMaterialData(visitOrderDetailResponseList);
                }
            } else if (PARAM == 3) {
                if (visitOrderDetailResponse != null) {
                    visitOrderDetailResponseList = new ArrayList<>();
                    Collections.addAll(visitOrderDetailResponseList, visitOrderDetailResponse);
                    if (visitOrderDetailResponseList.size() != 0) {
                        txtEmpty.setVisibility(View.GONE);
                        Helper.setItemParam(Constants.SIZE_VISIT_ORDER_DETAIL,
                                visitOrderDetailResponseList.size());
                        setMaterialData(visitOrderDetailResponseList);
                    } else {
                        txtEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    txtEmpty.setVisibility(View.VISIBLE);
                }
            } else if (PARAM == 4) {
                if (visitOrderDetailResponse != null) {
                    visitOrderDetailResponseList = new ArrayList<>();
                    Collections.addAll(visitOrderDetailResponseList, visitOrderDetailResponse);
                    txtEmpty.setVisibility(View.GONE);
                    Helper.setItemParam(Constants.SIZE_VISIT_ORDER_DETAIL, visitOrderDetailResponseList.size());
                    setMaterialData(visitOrderDetailResponseList);
                } else {
                    txtEmpty.setVisibility(View.VISIBLE);
                }
            } else if (PARAM == 5) {
                if (visitOrderDetailResponse != null) {
                    visitOrderDetailResponseList = new ArrayList<>();

                    Collections.addAll(visitOrderDetailResponseList, visitOrderDetailResponse);
                    if (visitOrderDetailResponseList.size() != 0) {
                        txtEmpty.setVisibility(View.GONE);
                        Helper.setItemParam(Constants.SIZE_VISIT_ORDER_DETAIL,
                                visitOrderDetailResponseList.size());
                        setMaterialData(visitOrderDetailResponseList);
                    } else {
                        txtEmpty.setVisibility(View.VISIBLE);
                    }

                } else {
                    txtEmpty.setVisibility(View.VISIBLE);
                }
            } else if (PARAM == 6) {
                if (getPriceResponse != null) {
                    listToPrice = new ArrayList<>();
                    listOneTimeDiscount = new ArrayList<>();

                    if (getPriceResponse.getListToPrice() != null) {
                        Collections.addAll(listToPrice, getPriceResponse.getListToPrice());
                    }

                    if (getPriceResponse.getListToPrice() != null) {
                        Collections.addAll(listOneTimeDiscount, getPriceResponse.getListOneTimeDiscount());
                    }

                    Helper.removeItemParam(Constants.PRICE_DETAIL);
                    Helper.removeItemParam(Constants.ONE_TIME_DISCOUNT_DETAIL);
                    Helper.setItemParam(Constants.PRICE_DETAIL, listToPrice);

                    Helper.setItemParam(Constants.ONE_TIME_DISCOUNT_DETAIL, listOneTimeDiscount);

                    /*New*/
                    if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
                        for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
                            if (visitOrderDetailResponseList.get(i).getIdMaterial().equals(orderDetail.getIdMaterial())) {

                                visitOrderDetailResponseList.get(i).setId_price(getPriceResponse.getIdToPrice());

                                int flagMwst = 1;
                                if (listToPrice != null && !listToPrice.isEmpty()) {
                                    visitOrderDetailResponseList.get(i).setListToPrice(listToPrice);
                                    visitOrderDetailResponseList.get(i).setPriceBfr(listToPrice.get(0).getAmount());
                                    for (int j = 0; j < listToPrice.size(); j++) {
                                        if (j == listToPrice.size() - 1) {
                                            visitOrderDetailResponseList.get(i).setPrice(listToPrice.get(j).getAmount());

                                            for (int k = 0; k < listToPrice.size(); k++) {
                                                if (listToPrice.get(k).getId_cond_type() != null) {
                                                    if (listToPrice.get(k).getId_cond_type().equals(getString(R.string.cond_type_disc))) {
                                                        if (visitOrderDetailResponseList.get(i).getPrice() != null
                                                                && listToPrice.get(j).getAmount() != null
                                                                && listToPrice.get(k).getDisc_value() != null) {
                                                            /*Total- pajak - Harga Awal */

                                                            BigDecimal finalDisc =
                                                                    listToPrice.get(j).getAmount()
                                                                            .subtract(listToPrice.get(k).getAmount())
                                                                            .subtract(listToPrice.get(0).getAmount());

                                                            visitOrderDetailResponseList.get(i).setDisc(finalDisc.setScale(2, BigDecimal.ROUND_HALF_UP));

                                                            visitOrderDetailResponseList.get(i).setTax(listToPrice.get(k).getDiscValueString());

                                                            flagMwst = 1;
                                                            break;
                                                        }
                                                    } else {
                                                        flagMwst = 0;
                                                    }
                                                }
                                            }

                                            if (flagMwst == 0) {
                                                if (visitOrderDetailResponseList.get(i).getPrice() != null
                                                        && listToPrice.get(j).getAmount() != null) {
                                                    /*Total- pajak - Harga Awal */
                                                    BigDecimal tempDisc =
                                                            visitOrderDetailResponseList.get(i).getPrice()
                                                                    .subtract(listToPrice.get(j).getAmount().multiply(BigDecimal.ZERO))
                                                                    .setScale(2, BigDecimal.ROUND_HALF_UP);

                                                    BigDecimal finalDisc =
                                                            listToPrice.get(j).getAmount()
                                                                    .subtract(BigDecimal.ZERO)
                                                                    .subtract(listToPrice.get(0).getAmount());

                                                    visitOrderDetailResponseList.get(i).setDisc(finalDisc.setScale(2, BigDecimal.ROUND_HALF_UP));

                                                    visitOrderDetailResponseList.get(i).setTax(Constants.ZERO);
                                                }
                                                flagMwst = 1;
                                            }
                                        }
                                    }
                                } else {
                                    visitOrderDetailResponseList.get(i).setPrice(new BigDecimal(0));
                                }

                                visitOrderDetailResponseList.get(i).setListOneTimeDiscount(listOneTimeDiscount);

                                if (getPriceResponse.getIdToPrice() != null) {
                                    visitOrderDetailResponseList.get(i).setIdToPrice(getPriceResponse.getIdToPrice());
                                }
                            }
                        }
                    }

                    setMaterialData(visitOrderDetailResponseList);

                    if (Helper.getItemParam(Constants.VIEW_PRICE_DETAIL) != null) {
                        Helper.removeItemParam(Constants.VIEW_PRICE_DETAIL);
                        Helper.setItemParam(Constants.VISIT_ORDER_DETAIL, visitOrderDetailResponseList);


                        if (Helper.getItemParam(Constants.LIST_ALL_TO_PRICE) != null) {
                            allListToPrice = (ArrayList<ArrayList<ToPrice>>) Helper.getItemParam(Constants.LIST_ALL_TO_PRICE);
                        } else {
                            allListToPrice.add(listToPrice);
                        }

                        openDialog(22);

                    } else {
                        allListToPrice = new ArrayList<>();
                        allListToPrice.add(listToPrice);
                    }

                    calculateTotal(visitOrderDetailResponseList);
                }

                wait = 0;
                progress.dismiss();
            } else if (PARAM == 7) {
                if (wait == 0) {
                    progress.dismiss();
                    if (freeGoods != null) {
                        listFreeGoods = new ArrayList<>();
                        Collections.addAll(listFreeGoods, freeGoods);
                        Helper.setItemParam(Constants.LIST_FREE_GOODS, listFreeGoods);
                    }
                    fragment = new OrderSummaryFragmentN();
                    setContent(fragment);
                }
            } else {
                if (lastOrder != null) {
                    lastOrder1 = getResources().getString(R.string.nullData);
                    if (lastOrder.getIdMessage() == 1) {
                        if (lastOrder.getResult() != null) {
                            if (lastOrder.getResult().getLastQty1() != null && lastOrder.getResult().getLastUom1() != null) {
                                lastOrder1 = lastOrder.getResult().getLastQty1().toString().concat(Constants.SPACE).concat(lastOrder.getResult().getLastUom1());
                            }
                        }

                        txtLastOrder.setText(lastOrder1);

                    } else {
                        txtLastOrder.setText(lastOrder1);
                    }
                }
            }
            progress.dismiss();
        }
    }

    private void mappingRequestDataPricing() {
        orderDetail = (VisitOrderDetailResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL);

        getPriceRequest = new GetPriceRequest();
        getPriceRequest.setIdMaterial(orderDetail.getIdMaterial());
        getPriceRequest.setIdOutlet(outletResponse.getIdOutlet());
        getPriceRequest.setQty1(orderDetail.getQty1());
        if (orderDetail.getListUom() != null && !orderDetail.getListUom().isEmpty()) {
            for (int i = 0; i < orderDetail.getListUom().size(); i++) {
                if (orderDetail.getListUom().get(i).getId() != null) {
                    if (orderDetail.getListUom().get(i).getUomName().equals(orderDetail.getUom1())) {
                        getPriceRequest.setUom1(orderDetail.getListUom().get(i).getId());
                        uomid1 = 1;
                        break;
                    }
                }
            }
        }

        if (uomid1 == 0) {
            getPriceRequest.setUom1(orderDetail.getUom1());
        } else {
            uomid1 = 0;
        }
        if (orderDetail.getListUom() != null && !orderDetail.getListUom().isEmpty()) {
            for (int i = 0; i < orderDetail.getListUom().size(); i++) {
                if (orderDetail.getListUom().get(i).getId() != null) {
                    if (orderDetail.getListUom().get(i).getUomName().equals(orderDetail.getUom2())) {
                        getPriceRequest.setUom2(orderDetail.getListUom().get(i).getId());
                        uomid2 = 1;
                        break;
                    }
                }
            }
        }
        if (uomid2 == 0) {
            getPriceRequest.setUom2(orderDetail.getUom2());
        } else {
            uomid2 = 0;
        }

        if (orderDetail.getQty2() != null) {
            getPriceRequest.setQty2(orderDetail.getQty2());
        } else {
            getPriceRequest.setQty2(BigDecimal.ZERO);
            getPriceRequest.setUom2(null);
        }

        if (orderDetail.getJenisJual() != null) {
            getPriceRequest.setJenisJual(orderDetail.getJenisJual());
        }

        getPriceRequest.setTop_sap(orderDetail.getTop_sap());

        if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
            for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
                if (visitOrderDetailResponseList.get(i).getIdMaterial() != null && orderDetail.getIdMaterial() != null) {
                    if (visitOrderDetailResponseList.get(i).getIdMaterial().equals(orderDetail.getIdMaterial())) {
                        if (visitOrderDetailResponseList.get(i).getIdToPrice() != null) {//dsini
                            getPriceRequest.setIdToPrice(visitOrderDetailResponseList.get(i).getIdToPrice());
                        }
                    }
                }
            }
        }
    }


    private void setMaterialData(ArrayList<VisitOrderDetailResponse> mData) {
        linearLayoutList.setVisibility(View.VISIBLE);
        linearLayoutList.removeAllViews();

        listUom = new ArrayList<>();
        listJenisJual = new ArrayList<>();

        if (mData != null && !mData.isEmpty()) {
            for (VisitOrderDetailResponse material : mData) {
                if (material.getMaterialName() == null) {
                    if (material.getIdMaterial() != null) {
                        material.setMaterialName(db.getMaterialName(material.getIdMaterial()));
                    }
                }

                if (material.getJenisJual() == null) {
                    if (material.getTop() != null) {
                        material.setJenisJual(db.getIdJj(outletResponse.getIdOutlet(), material.getTop()));
                    }
                }

                if (material.getTop() != null) {
                    material.setTop_sap(db.getTopSap(material.getTop()));
                }

                listUom = db.getListUomByIdMat(material.getIdMaterial(), Constants.IS_ORDER);
                material.setListUom(listUom);

                if (Helper.getItemParam(Constants.ORDER_TYPE) != null) {
                    listJenisJual = db.getJenisJual(outletResponse.getIdOutlet(), material.getIdMaterial(), String.valueOf(Helper.getItemParam(Constants.ORDER_TYPE)));
                } else if (Helper.getItemParam(Constants.SO_HEADER_SELECTED) != null) {
                    if (((VisitOrderHeader) Helper.getItemParam(Constants.SO_HEADER_SELECTED)).getOrderType() != null) {
                        listJenisJual = db.getJenisJual(outletResponse.getIdOutlet(), material.getIdMaterial(), ((VisitOrderHeader) Helper.getItemParam(Constants.SO_HEADER_SELECTED)).getOrderType());
                    }
                }


                material.setJenisJualandTop(listJenisJual);


                if (Helper.getItemParam(Constants.ORDER_TYPE) != null) {
                    if (Helper.getItemParam(Constants.ORDER_TYPE).equals(Constants.ORDER_TAKING_TYPE)) {
                        material.setStockQty1(db.getAvailableStock(material.getIdMaterial(), Constants.ORDER_TAKING_TYPE).getStockQty1());
                        material.setStockUom1(db.getAvailableStock(material.getIdMaterial(), Constants.ORDER_TAKING_TYPE).getStockUom1());
                    } else {
                        material.setStockQty1(db.getAvailableStock(material.getIdMaterial(), Constants.ORDER_CANVAS_TYPE).getStockQty1());
                        material.setStockUom1(db.getAvailableStock(material.getIdMaterial(), Constants.ORDER_CANVAS_TYPE).getStockUom1());
                    }
                }

                //tambahan
                if (outletResponse != null) {
                    if (outletResponse.getIdOutlet() != null && material.getIdMaterial() != null) {
                        VisitOrderDetailResponse orderPlan = db.getCurOrderPlanBy(outletResponse.getIdOutlet(), material.getIdMaterial());
                        material.setOrderPlanQty1(orderPlan.getOrderPlanQty1());
                        material.setOrderPlanUom1(orderPlan.getOrderPlanUom1());
                        material.setOrderPlanQty2(orderPlan.getOrderPlanQty2());
                        material.setOrderPlanUom2(orderPlan.getOrderPlanUom2());
                    }
                }

                /*current*/
                storeCheck = db.getMaterialStoreCheck(outletResponse.getIdOutlet(), material.getIdMaterial());

                if (storeCheck != null) {
                    if (storeCheck.getStockQty2() != null && storeCheck.getStockUom2() != null) {
                        material.setLastCheckQty2(storeCheck.getStockQty2());
                        material.setLastCheckUom2(storeCheck.getStockUom2());
                        if (storeCheck.getStockQty1() != null && storeCheck.getStockUom1() != null) {
                            material.setLastCheckQty1(storeCheck.getStockQty1());
                            material.setLastCheckUom1(storeCheck.getStockUom1());
                            txtLastCheck.setText(String.valueOf(
                                    storeCheck.getStockQty1().add(
                                            storeCheck.getStockQty2().multiply(db.getConversionBy(material.getIdMaterial(), storeCheck.getStockUom2()))
                                                    .divide(db.getConversionBy(material.getIdMaterial(), storeCheck.getStockUom1()), RoundingMode.HALF_UP)
                                    )).concat(Constants.SPACE).concat(db.getUomName(storeCheck.getStockUom1())));
                        }
                    } else if (storeCheck.getStockQty1() != null && storeCheck.getStockUom1() != null) {
                        txtLastCheck.setText(String.valueOf(storeCheck.getStockQty1()).concat(Constants.SPACE).concat(db.getUomName(storeCheck.getStockUom1())));
                    } else {
                        txtLastCheck.setText(getResources().getString(R.string.nullData));
                    }
                }

                if (listUom != null && !listUom.isEmpty()) {
                    for (int j = 0; j < listUom.size(); j++) {
                        if (listUom.get(j).getId() != null && listUom.get(j).getUomName() != null) {
                            if (orderPlanMaterial.getUom1() != null) {
                                if (orderPlanMaterial.getUom1().equals(listUom.get(j).getId())) {
                                    orderPlanMaterial.setUom1(listUom.get(j).getUomName());
                                }
                            }
                            if (orderPlanMaterial.getUom2() != null) {
                                if (orderPlanMaterial.getUom2().equals(listUom.get(j).getId())) {
                                    orderPlanMaterial.setUom2(listUom.get(j).getUomName());
                                }
                            }
                        }
                    }
                }
            }

            //TODO: 4102019 CEK DISINI
            if (Helper.getItemParam(Constants.ADD_MATERIAL) != null) {
                if (listJenisJual != null && !listJenisJual.isEmpty() && mData.size() > 1) {
                    for (JenisJualandTop jjt : mData.get(mData.size() - 1).getJenisJualandTop()) {
                        if (jjt.getJenisJual().equals(mData.get(mData.size() - 2).getJenisJual())) {
                            mData.get(mData.size() - 1).setJenisJual(mData.get(mData.size() - 2).getJenisJual());
                            mData.get(mData.size() - 1).setTop_sap(mData.get(mData.size() - 2).getTop_sap());
                            mData.get(mData.size() - 1).setTop(mData.get(mData.size() - 2).getTop());
                            break;
                        } else {
                            mData.get(mData.size() - 1).setJenisJual(Constants.STRIP.trim());
                        }
                    }
                }
            }
        }

        if (Helper.getItemParam(Constants.ORDER_TYPE) != null) {
//            mAdapter = new VisitOrderDetailAdapterV2(this, mData, Helper.getItemParam(Constants.ORDER_TYPE).toString());
        } else {
//            mAdapter = new VisitOrderDetailAdapterV2(this, mData);
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View item = mAdapter.getView(i, null, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 15);
            linearLayoutList.addView(item, layoutParams);
        }


        assert mData != null;
        checkEmptyText(mData);

        progress.dismiss();
    }

    private void checkEmptyText(ArrayList<VisitOrderDetailResponse> mData) {
        if (mData.size() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onResume() {
        super.onResume();
        if (progress != null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }

        if (savedHeader != null) {
            if (savedHeader.getId() != null) {
                if (savedHeader.getId().contains(Constants.KEY_TRUE_ID_TO)) {

                    btnNewMaterial.setVisibility(View.GONE);
                } else {
                    btnNewMaterial.setVisibility(View.VISIBLE);
                }
            } else {
                btnNewMaterial.setVisibility(View.VISIBLE);
            }
        }

        if (visitOrderDetailResponseList != null) {
            if (visitOrderDetailResponseList.isEmpty()) {
                showDetail(false);
            }
        } else {
            showDetail(false);
        }

        getData();
//
//        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
//
//                    back();
//
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    private void getData() {
//        database = new DatabaseHelper(rootView.getContext());
//        if (Helper.getItemParam(Constants.GET_DETAIL_VISIT) != null) {//udah pernah isi
//            if (Helper.getItemParam(Constants.ADD_MATERIAL) != null) {//mau add materiall
//                Material material = (Material) Helper.getItemParam(Constants.ADD_MATERIAL);
//                idMaterial = material.getMaterialCode();
//                Helper.removeItemParam(Constants.GET_DETAIL_VISIT);
//                visitOrderDetailResponseList = new ArrayList<>();
//                visitOrderDetailResponseList = (ArrayList<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);
//
//                if (visitOrderDetailResponseList != null) {
//                    if (visitOrderDetailResponseList.size() != 0) {
//                        for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
//                            if (visitOrderDetailResponseList.get(i).getIdMaterial().equals(idMaterial)) {
//                                setMaterialData(visitOrderDetailResponseList);
//                                break;
//                            } else {//addMaterial
//                                if (Helper.getItemParam(Constants.ADD_MATERIAL) != null) {
//                                    VisitOrderDetailResponse visitOrderDetailResponse = new VisitOrderDetailResponse();
//                                    if (material.getMaterialCode() != null) {
//                                        visitOrderDetailResponse.setIdMaterial(material.getMaterialCode());
//                                    }
//                                    if (material.getDesc() != null) {
//                                        visitOrderDetailResponse.setMaterialName(material.getDesc());
//                                    }
//
//                                    if (Helper.getItemParam(Constants.VISIT_ORDER_DETAIL) != null) {
//                                        visitOrderDetailResponseList = new ArrayList<>();
//                                        visitOrderDetailResponseList = (ArrayList<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);
//                                        Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
//                                        visitOrderDetailResponseList.add(visitOrderDetailResponse);
//                                    } else {
//                                        visitOrderDetailResponseList.add(visitOrderDetailResponse);
//                                    }
//                                    setMaterialData(visitOrderDetailResponseList);
//
//                                    Helper.removeItemParam(Constants.ADD_MATERIAL);
//
//                                    scroll.post(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
////                                            return null;
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    } else {
//                        if (Helper.getItemParam(Constants.ADD_MATERIAL) != null) {// addmaterial
//                            VisitOrderDetailResponse visitOrderDetailResponse = new VisitOrderDetailResponse();
//
//                            if (material.getDesc() != null) {
//                                visitOrderDetailResponse.setMaterialName(material.getDesc());
//                            }
//                            if (material.getMaterialId() != null) {
//                                visitOrderDetailResponse.setIdMaterial(material.getMaterialId());
//                            }
//                            if (Helper.getItemParam(Constants.VISIT_ORDER_DETAIL) != null) {
//                                visitOrderDetailResponseList = new ArrayList<>();
//                                visitOrderDetailResponseList = (ArrayList<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);
//                                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
//                                visitOrderDetailResponseList.add(visitOrderDetailResponse);
//                            } else {
//                                visitOrderDetailResponseList.add(visitOrderDetailResponse);
//                            }
//                            setMaterialData(visitOrderDetailResponseList);
//
//                            Helper.removeItemParam(Constants.ADD_MATERIAL);
//                        }
//                    }
//                } else {
//                    if (Helper.getItemParam(Constants.ADD_MATERIAL) != null) {// addmaterial
//                        VisitOrderDetailResponse visitOrderDetailResponse = new VisitOrderDetailResponse();
//
//                        if (material.getMaterialCode() != null) {
//                            visitOrderDetailResponse.setIdMaterial(material.getMaterialCode());
//                        }
//
//                        if (material.getDesc() != null) {
//                            visitOrderDetailResponse.setMaterialName(material.getDesc());
//                        }
//                        if (Helper.getItemParam(Constants.VISIT_ORDER_DETAIL) != null) {
//                            visitOrderDetailResponseList = new ArrayList<>();
//                            visitOrderDetailResponseList = (ArrayList<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);
//                            Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
//                            visitOrderDetailResponseList.add(visitOrderDetailResponse);
//                        } else {
//                            visitOrderDetailResponseList = new ArrayList<>();
//                            visitOrderDetailResponseList.add(visitOrderDetailResponse);
//                        }
//                        setMaterialData(visitOrderDetailResponseList);
//
//                        Helper.removeItemParam(Constants.ADD_MATERIAL);
//                    }
//                }
//            } else {
//                //dari summary
//                if (Helper.getItemParam(Constants.VISIT_ORDER_DETAIL) != null) {
//                    visitOrderDetailResponseList = new ArrayList<>();
//                    visitOrderDetailResponseList = (ArrayList<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);
//                    if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
//                        progress.show();//new
//                        setMaterialData(visitOrderDetailResponseList);
//                    }
//                }
//            }
//        } else {
//            if (Helper.getItemParam(Constants.VISIT_ORDER_DETAIL) != null) {
//                setMaterialData((ArrayList<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL));
//            } else {
//                if (savedHeader != null) {//manggil ini pas kalo masuk ke halaman lewat detail list yang uda kesimpen
//                    visitOrderDetailResponseList = new ArrayList<>();
//                    if (savedHeader.getId() != null) {
//                        visitOrderDetailResponseList = db.getListOrderDetail(savedHeader.getId());
//
//                        if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
//                            for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
//
//                                listToPrice = new ArrayList<>();
//                                if (visitOrderDetailResponseList.get(i).getId_price() != null) {
//                                    listToPrice = db.getToPricewithIdPrice(visitOrderDetailResponseList.get(i).getId_price());
//                                }
//
//                                int flagMwst = 1;
//                                if (listToPrice != null && !listToPrice.isEmpty()) {
//                                    visitOrderDetailResponseList.get(i).setListToPrice(listToPrice);
//                                    visitOrderDetailResponseList.get(i).setPriceBfr(listToPrice.get(0).getAmount());
//                                    for (int j = 0; j < listToPrice.size(); j++) {
//                                        if (j == listToPrice.size() - 1) {
//                                            visitOrderDetailResponseList.get(i).setPrice(listToPrice.get(j).getAmount());
//
//                                            for (int k = 0; k < listToPrice.size(); k++) {
//                                                if (listToPrice.get(k).getId_cond_type() != null) {
//                                                    if (listToPrice.get(k).getId_cond_type().equals(getString(R.string.cond_type_disc))) {
//                                                        if (visitOrderDetailResponseList.get(i).getPrice() != null
//                                                                && listToPrice.get(j).getAmount() != null
//                                                                && listToPrice.get(k).getDiscValueString() != null) {
//                                                            /*Total- pajak - Harga Awal */
//
//                                                            BigDecimal finalDisc =
//                                                                    listToPrice.get(j).getAmount()
//                                                                            .subtract(listToPrice.get(k).getAmount())
//                                                                            .subtract(listToPrice.get(0).getAmount());
//
//                                                            visitOrderDetailResponseList.get(i).setDisc(finalDisc.setScale(2, BigDecimal.ROUND_HALF_UP));
//
//                                                            visitOrderDetailResponseList.get(i).setTax(listToPrice.get(k).getDiscValueString());
//                                                            flagMwst = 1;
//                                                            break;
//                                                        }
//                                                    } else {
//                                                        flagMwst = 0;
//                                                    }
//
//                                                }
//                                            }
//
//                                            if (flagMwst == 0) {
//                                                if (visitOrderDetailResponseList.get(i).getPrice() != null
//                                                        && listToPrice.get(j).getAmount() != null) {
//                                                    /*Total- pajak - Harga Awal */
//                                                    BigDecimal tempDisc =
//                                                            visitOrderDetailResponseList.get(i).getPrice()
//                                                                    .subtract(listToPrice.get(j).getAmount().multiply(BigDecimal.ZERO))
//                                                                    .setScale(2, BigDecimal.ROUND_HALF_UP);
//
//
//                                                    visitOrderDetailResponseList.get(i).setDisc(BigDecimal.ZERO);
//
//                                                    visitOrderDetailResponseList.get(i).setTax(Constants.ZERO);
//                                                }
//                                                flagMwst = 1;
//                                            }
//                                        }
//                                    }
//                                } else {
//                                    visitOrderDetailResponseList.get(i).setPrice(new BigDecimal(0));
//                                }
//
//                            }
//                        }
//
//                        setMaterialData(visitOrderDetailResponseList);
//                    } else {//pertama kali manggil ini
//                        visitOrderDetailResponseList = new ArrayList<>();
//                        if (savedHeader != null) {
//                            if (savedHeader.getId() != null) {
//                                visitOrderDetailResponseList = db.getListOrderDetail(savedHeader.getId());
//                                if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
//
//                                    setMaterialData(visitOrderDetailResponseList);
//                                }
//                            }
//                        }
//                    }
//
//
//                } else {//pertama kali manggil ini
//                    visitOrderDetailResponseList = new ArrayList<>();
//                    if (savedHeader != null) {
//                        if (savedHeader.getId() != null) {
//                            visitOrderDetailResponseList = db.getListOrderDetail(savedHeader.getId());
//                            if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
//
//                                setMaterialData(visitOrderDetailResponseList);
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
//                calculateTotal(visitOrderDetailResponseList);
//            }
//        }
    }

    @Override
    public void onPause() {
        Helper.setItemParam(Constants.VISIT_ORDER_DETAIL, visitOrderDetailResponseList);

        super.onPause();
    }

    public void requestPrice() {
        if (savedHeader != null) {
            if (savedHeader.getId().contains(Constants.KEY_TRUE_ID_TO)) {
                orderDetail = (VisitOrderDetailResponse) Helper.getItemParam(Constants.MATERIAL_DETAIL);

                if (orderDetail.getId_price() != null) {
                    preventDupeDialog();
                    listToPrice = db.getToPricewithIdPrice(orderDetail.getId_price());
                    Helper.removeItemParam(Constants.PRICE_DETAIL);
                    Helper.setItemParam(Constants.PRICE_DETAIL, listToPrice);
                    openDialog(22);
                } else {
                    Toast.makeText(getContext(), R.string.missing_id, Toast.LENGTH_SHORT).show();
                }
            } else {
                PARAM = 6;
//                new RequestUrl().execute();
                new RequestUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                progress.show();
            }
        } else {
            PARAM = 6;
//            new RequestUrl().execute();
            new RequestUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            progress.show();
        }
    }

    public void requestPriceN(VisitOrderDetailResponse order) {
        if (isNetworkAvailable()) {

            Helper.setItemParam(Constants.MATERIAL_DETAIL, order);
            PARAM = 6;

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        new RequestUrl().execute().get();
                        new RequestUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 200);
//            new RequestUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private boolean validateOrder(VisitOrderDetailResponse visitOrderDetailResponse) {
        boolean results = false;
        if (visitOrderDetailResponse.getQty1() == null ||
                String.valueOf(visitOrderDetailResponse.getQty1()).trim().equals("0") ||
                visitOrderDetailResponse.getJenisJual() == null ||
                visitOrderDetailResponse.getJenisJual().trim().equals("-") ||
                visitOrderDetailResponse.getUom1() == null) {
            results = true;
        }
        return results;
    }

    private String validateOrder2(VisitOrderDetailResponse visitOrderDetailResponse) {
        String results = null;
        if (visitOrderDetailResponse.getQty1() == null || String.valueOf(visitOrderDetailResponse.getQty1()).trim().equals("0")) {
            results = Constants.errorQTY;
        } else if (visitOrderDetailResponse.getJenisJual() == null || visitOrderDetailResponse.getJenisJual().trim().equals("-")) {
            results = Constants.errorJenisJual;
        } else if (visitOrderDetailResponse.getUom1() == null) {
            results = Constants.errorUom;
        }
        return results;
    }

    public void viewDetailData(VisitOrderDetailResponse material, int pos) {
        if (pos != -1) {
            if (material.getLastOrderQty1() == null && material.getLastOrderUom1() == null) {
                requestDetail = new VisitOrderDetailResponse();
                requestDetail.setIdMaterial(material.getIdMaterial());

                PARAM = 8;
//                new RequestUrl().execute();
                new RequestUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                setDetailMaterial(material, pos);
            }
        } else {
            showDetail(false);
        }
    }

    public void showDetail(Boolean b) {
        if (b) {
            cvDetail.setVisibility(View.VISIBLE);
        } else {
            //disini
            cvDetail.setVisibility(View.GONE);
        }
    }

    private void setDetailMaterial(VisitOrderDetailResponse material, int pos) {
        cvDetail.setVisibility(View.VISIBLE);
        txtLine.setText(String.valueOf(pos + 1));
        txtMaterial.setText(material.getIdMaterial().concat(Constants.SPACE).concat(material.getMaterialName()));
        txtQtyUomN.setText(qty1.concat(uom1
                .concat(Constants.SPACE)
                .concat(qty2.concat(uom2))));

        if (material.getStockQty1() != null && material.getStockUom1() != null) {
            txtStockAwal.setText(String.valueOf(material.getStockQty1())
                    .concat(db.getUomName(material.getStockUom1())));
        }

        //rumus saran order : order plan – last check

        String orderPlanUom = Constants.EMPTY_STRING, orderPlanUom2 = Constants.EMPTY_STRING;
        String lastCheckUom = Constants.EMPTY_STRING, lastCheckUom2 = Constants.EMPTY_STRING;
        BigDecimal orderPlanQty = BigDecimal.ZERO, orderPlanQty2 = BigDecimal.ZERO, lastCheckQty = BigDecimal.ZERO, lastCheckQty2 = BigDecimal.ZERO;

        if (material.getOrderPlanQty1() != null && material.getOrderPlanUom1() != null) {
            orderPlanQty = material.getOrderPlanQty1().setScale(2, BigDecimal.ROUND_HALF_UP);
            orderPlanUom = material.getOrderPlanUom1();


            if (material.getOrderPlanQty2() != null && material.getOrderPlanUom2() != null) {
                orderPlanQty2 = material.getOrderPlanQty2().setScale(2, BigDecimal.ROUND_HALF_UP);
                orderPlanUom2 = material.getOrderPlanUom2();

                if (orderPlanQty != null && orderPlanUom != null) {
                    if (!orderPlanQty.equals(BigDecimal.ZERO) && !orderPlanUom.equals(Constants.EMPTY_STRING)) {
                        //ini
                        orderPlanQty = orderPlanQty.add(Helper.convertQtyUom(orderPlanQty2,
                                checkAmountConv(db.getConversionBy(material.getIdMaterial(), orderPlanUom2)),
                                checkAmountConv(db.getConversionBy(material.getIdMaterial(), orderPlanUom))));


                        if (orderPlanQty.compareTo(BigDecimal.ZERO) == -1) {
                            orderPlanQty = BigDecimal.ZERO;
                        }
                    }
                }
            }

            if (material.getLastCheckQty1() != null && material.getLastCheckUom1() != null) {
                lastCheckQty = material.getLastCheckQty1().setScale(2, BigDecimal.ROUND_HALF_UP);
                lastCheckUom = material.getLastCheckUom1();
            }

            if (material.getLastCheckQty2() != null && material.getLastCheckUom2() != null) {
                lastCheckQty2 = material.getLastCheckQty2().setScale(2, BigDecimal.ROUND_HALF_UP);
                lastCheckUom2 = material.getLastCheckUom2();

                if (lastCheckQty != null && lastCheckUom != null) {
                    if (!lastCheckQty.equals(BigDecimal.ZERO) && !lastCheckUom.equals(Constants.EMPTY_STRING)) {
                        lastCheckQty = lastCheckQty.add(Helper.convertQtyUom(lastCheckQty2,
                                checkAmountConv(db.getConversionBy(material.getIdMaterial(), lastCheckUom2)),
                                checkAmountConv(db.getConversionBy(material.getIdMaterial(), lastCheckUom))));
                    }
                }
            }

            /*Tambah*/
            if (lastCheckUom != null && orderPlanUom != null) {
                if (!lastCheckUom.equals(orderPlanUom)) {
                    lastCheckQty = Helper.convertQtyUom(lastCheckQty,
                            checkAmountConv(db.getConversionBy(material.getIdMaterial(), lastCheckUom)),
                            checkAmountConv(db.getConversionBy(material.getIdMaterial(), orderPlanUom)));
                }
            }


            if (lastCheckQty.compareTo(BigDecimal.ZERO) == -1) {
                lastCheckQty = BigDecimal.ZERO;
            }

            String suggesionQty = "-";
            if (orderPlanQty != null) {
                if ((orderPlanQty.subtract(lastCheckQty)).compareTo(BigDecimal.ZERO) == -1) {
                    suggesionQty = "0";
                } else {
                    suggesionQty = String.valueOf(orderPlanQty.subtract(lastCheckQty).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
            txtSuggestion.setText(suggesionQty.concat(Constants.SPACE).concat(orderPlanUom));

        } else {
            txtSuggestion.setText(getResources().getString(R.string.nullData));
        }
        storeCheck = db.getMaterialStoreCheck(outletResponse.getIdOutlet(), material.getIdMaterial());

        if (storeCheck != null) {
            if (storeCheck.getStockQty2() != null && storeCheck.getStockUom2() != null) {
                if (storeCheck.getStockQty1() != null && storeCheck.getStockUom1() != null) {
                    txtLastCheck.setText(String.valueOf(
                            storeCheck.getStockQty1().add(
                                    storeCheck.getStockQty2().multiply(db.getConversionBy(material.getIdMaterial(), storeCheck.getStockUom2()))
                                            .divide(db.getConversionBy(material.getIdMaterial(), storeCheck.getStockUom1()))
                            )).concat(Constants.SPACE).concat(db.getUomName(storeCheck.getStockUom1())));
                }
            } else if (storeCheck.getStockQty1() != null && storeCheck.getStockUom1() != null) {
                txtLastCheck.setText(String.valueOf(storeCheck.getStockQty1()).concat(Constants.SPACE).concat(db.getUomName(storeCheck.getStockUom1())));
            } else {
                txtLastCheck.setText(getResources().getString(R.string.nullData));
            }
        }

    }

    private BigDecimal checkAmountConv(BigDecimal input) {
        if (input.equals(BigDecimal.ZERO)) {
            input = BigDecimal.ONE;
        }

        return input;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem save = menu.findItem(R.id.action_save);
        final MenuItem filter = menu.findItem(R.id.action_filter);
        final MenuItem search = menu.findItem(R.id.action_search);
        final MenuItem edit = menu.findItem(R.id.action_edit);
        final MenuItem setting = menu.findItem(R.id.action_settings);
        final MenuItem next = menu.findItem(R.id.action_next);
        final MenuItem attachment = menu.findItem(R.id.action_attachment);

        attachment.setVisible(true);
        search.setVisible(true);
        next.setVisible(true);

        attachment.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                flagAttachment = 1;
                if (savedHeader != null) {
                    if (savedHeader.getId() != null) {
                        Helper.setItemParam(Constants.ATTACHMENT, db.getAttachmentSaved(savedHeader.getId()));
                        openDialog(DIALOG_SPECIMEN);
                    }
                } else {
                    Toast.makeText(getContext(), R.string.no_attachment, Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search..");
        search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                //disini
                cvDetail.setVisibility(View.GONE);
                return false;
            }
        });

        next.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                boolean error;
                int flagPrice = 1;

                if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
                    for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
                        error = validateOrder(visitOrderDetailResponseList.get(i));
                        errorMsg = validateOrder2(visitOrderDetailResponseList.get(i));
                        if (error) {
                            invalid++;
                        }
                    }
                }

                if (invalid > 0) {
                    Snackbar snack = Snackbar.make(rootView, errorMsg, Snackbar.LENGTH_LONG);
                    snack.show();
                    invalid = 0;
                } else {
                    visitOrderDetailResponseListNew = new ArrayList<>();
                    if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
                        for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
                            VisitOrderDetailResponse orderDetail = visitOrderDetailResponseList.get(i);

                            if (visitOrderDetailResponseList.get(i).getPrice() != null) {
                                if (visitOrderDetailResponseList.get(i).getPrice().equals(BigDecimal.ZERO)) {
                                    flagPrice = 0;
                                }
                            } else {
                                flagPrice = 0;
                            }

                            if (orderDetail.getListUom() != null && !orderDetail.getListUom().isEmpty()) {
                                for (int j = 0; j < orderDetail.getListUom().size(); j++) {
                                    if (orderDetail.getListUom().get(j).getId() != null) {
                                        if (orderDetail.getListUom().get(j).getUomName().equals(orderDetail.getUom1())) {
                                            orderDetail.setUom1(orderDetail.getListUom().get(j).getId());
                                            uomid1 = 1;
                                            break;
                                        }
                                    }
                                }
                            }

                            if (uomid1 == 0) {
                                orderDetail.setUom1(orderDetail.getUom1());
                            } else {
                                uomid1 = 0;
                            }
                            if (orderDetail.getListUom() != null && !orderDetail.getListUom().isEmpty()) {
                                for (int j = 0; j < orderDetail.getListUom().size(); j++) {
                                    if (orderDetail.getListUom().get(j).getId() != null) {
                                        if (orderDetail.getListUom().get(j).getUomName().equals(orderDetail.getUom2())) {
                                            orderDetail.setUom2(orderDetail.getListUom().get(j).getId());
                                            uomid2 = 1;
                                            break;
                                        }
                                    }
                                }
                            }

                            if (uomid2 == 0) {
                                orderDetail.setUom2(orderDetail.getUom2());
                            } else {
                                uomid2 = 0;
                            }

                            if (!orderDetail.getQty1().equals(BigDecimal.ZERO) || !orderDetail.getQty2().equals(BigDecimal.ZERO)) {

                                visitOrderDetailResponseListNew.add(orderDetail);
                            }

                        }

                        //new, ubah param yg ke tiga
                        Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
                        Helper.removeItemParam(Constants.LIST_FREE_GOODS);

                        Helper.setItemParam(Constants.GET_DETAIL_VISIT, "1");
                        Helper.setItemParam(Constants.VISIT_ORDER_DETAIL_SAVE, visitOrderDetailResponseListNew);
                        Helper.setItemParam(Constants.VISIT_ORDER_DETAIL, visitOrderDetailResponseList);

                        int blmdpetprice = 0;
                        if (savedHeader != null) {
                            if (savedHeader.getId() != null) {
                                if (listOrderHeader != null && !listOrderHeader.isEmpty()) {
                                    for (int i = 0; i < listOrderHeader.size(); i++) {
                                        if (listOrderHeader.get(i).getId().equals(savedHeader.getId())) {
                                            if (listOrderHeader.get(i).getStatusPrice() != null) {
                                                if (listOrderHeader.get(i).getStatusPrice().equals(getResources().getString(R.string.status_price_unavailable))) {
                                                    blmdpetprice = 1;
                                                }
                                            }
                                        }
                                    }
                                    if (blmdpetprice == 1) {
                                        PARAM = 7;
                                        new RequestUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        progress.show();
                                        blmdpetprice = 0;
                                    } else {
                                        if (savedHeader.getId().contains(Constants.KEY_TRUE_ID_TO)) {//ini dipanggil klo cuma view yang uda di save
                                            listFreeGoods = new ArrayList<>();
                                            listFreeGoods = db.getFreeGoodsByIdFG(savedHeader.getId());

                                            for(FreeGoods fg : listFreeGoods){
                                                fg.setListOptionFreeGoods(new ArrayList[]{db.getOptionFreeGoodsById(savedHeader.getId())});
                                            }

                                            Helper.setItemParam(Constants.LIST_FREE_GOODS, listFreeGoods);

                                            fragment = new OrderSummaryFragmentN();
                                            setContent(fragment);
                                        } else {
                                            if (wait == 0) {
                                                PARAM = 7;
                                                new RequestUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                progress.show();
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (wait == 0) {
                                PARAM = 7;
//                                new RequestUrl().execute();
                                new RequestUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                progress.show();
                            }
                        }


                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.empty), Toast.LENGTH_SHORT).show();
                    }

                    if (flagPrice == 0) {
                        header.setStatusPrice(getString(R.string.status_price_unavailable));
                    } else {
                        header.setStatusPrice(getString(R.string.status_price_available));
                    }

                    Helper.setItemParam(Constants.VISIT_ORDER_HEADER, header);
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
            if (visitOrderDetailResponseList.size() != 0) {
                setMaterialData(visitOrderDetailResponseList);
            }
            return false;
        }

        final List<VisitOrderDetailResponse> filteredList = new ArrayList<>();

        for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
            final String text = Helper.validateResponseEmpty(visitOrderDetailResponseList.get(i).getMaterialName()).
                    concat(Constants.SPACE).concat(Helper.validateResponseEmpty(visitOrderDetailResponseList.get(i).getIdMaterial()));
            if (text.toLowerCase().contains(newText)) {
                filteredList.add(visitOrderDetailResponseList.get(i));
            }
        }
        //disini
        cvDetail.setVisibility(View.GONE);
        setDateFilter(filteredList);
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void calculateTotal(List<VisitOrderDetailResponse> list) {
        BigDecimal total = BigDecimal.ZERO;
        for (VisitOrderDetailResponse data : list) {
            if (data.getPrice() != null) {
                total = total.add(data.getPrice());
            }
        }
        txtTotal.setText(Helper.toRupiahFormat(String.valueOf(total)));
    }

}
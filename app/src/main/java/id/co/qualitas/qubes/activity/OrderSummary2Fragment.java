package id.co.qualitas.qubes.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.adapter.FreeGoodsNAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.FreeGoods;
import id.co.qualitas.qubes.model.OptionFreeGoods;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;

public class OrderSummary2Fragment extends BaseFragment implements Spinner.OnItemSelectedListener {
    private TextView txtDate, txtOutlet;
    private ImageView imgDetail, imgAttachment;
    private Button btnNext;
    private static TextView txtViewTotalHna, txtViewDisc, txtViewTotal, txtViewPotongan, txtViewPpn, txtAmountPpn;
    private TextView txtViewTotalItems;
    private TextView txtViewTotalOrderPlan;
    private TextView txtNoticeFG;
    private EditText edtTxtDescRequest;
    private Button btnViewDetails;
    private CheckBox cbCash;

    private static List<VisitOrderDetailResponse> visitOrderDetailList = new ArrayList<>();
    public static ArrayList<FreeGoods> listFreeGoods;
    private ArrayList<OptionFreeGoods>[] list;
    private ArrayList<OptionFreeGoods> listSelected;
    public static List<String> listSplit = new ArrayList<>();
    private ArrayList<ArrayList<OptionFreeGoods>> mData = new ArrayList<>();
    ArrayList<VisitOrderHeader> orderHeaderList = new ArrayList<>();

    public static String[] selectedFreeGoods, selectedDiskon;
    private String total;
    private String idTo = Constants.EMPTY_STRING;
    private int totalOrderPlan = 0;

    private OutletResponse outletResponse;
    private RecyclerView recyclerView;
    private FreeGoodsNAdapter mAdapter;

    public static BigDecimal price = BigDecimal.ZERO;
    public static BigDecimal disc = BigDecimal.ZERO;
    public static BigDecimal tax = BigDecimal.ZERO;
    public static BigDecimal potongan = BigDecimal.ZERO;
    public static BigDecimal totalHnaAfter = BigDecimal.ZERO;
    public static BigDecimal amountPpn = BigDecimal.ZERO;
    public static BigDecimal amount = BigDecimal.ZERO;
    public static BigDecimal amountSelected = BigDecimal.ZERO;

    public static String choosen = Constants.EMPTY_STRING;
    private static String ppn = Constants.EMPTY_STRING;
    private static String res = Constants.EMPTY_STRING;

    private VisitOrderHeader savedHeader;

    private ArrayList<FreeGoods> listSyncedFG = new ArrayList<>();
    private ArrayList<OptionFreeGoods> listSyncedOptionFG = new ArrayList<>();
    private ArrayList<OptionFreeGoods> listSyncedInsideOptionFreeGoods = new ArrayList<>();
    private ArrayList<ArrayList<OptionFreeGoods>> arrayListOptionFG = new ArrayList<>();
    private LinearLayout rowRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_order_summary, container, false);

        initFragment();
        initialize();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((NewMainActivity) getActivity()).changePage(13);
                        return true;
                    }
                }
                return false;
            }
        });
        
        getSavedFreeGoods();

        setSummaryData();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewMainActivity) getActivity()).changePage(13);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preventDupeDialog();
                VisitOrderHeader header = new VisitOrderHeader();
                int flag_unselected = 0;
                if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                    header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);

                    if (cbCash.isChecked()) {
                        header.setRequestDisc(1);
                        header.setRequest_disc(true);
                    } else {
                        header.setRequestDisc(0);
                        header.setRequest_disc(false);
                    }

                    if (!Helper.isEmpty(edtTxtDescRequest)) {
                        header.setDescriptionDisc(edtTxtDescRequest.getText().toString().trim());
                    } else {
                        header.setDescriptionDisc(null);
                    }

                    if (totalHnaAfter != null && amountPpn != null) {
                        header.setTotalAmount(totalHnaAfter.add(amountPpn));
                    }

                    if (listFreeGoods != null && !listFreeGoods.isEmpty()) {
                        ArrayList<ArrayList<OptionFreeGoods>> allSelectedFG = new ArrayList<ArrayList<OptionFreeGoods>>();
                        for (FreeGoods freeGoods : listFreeGoods) {
                            if (freeGoods.getListOptionFreeGoods() != null) {
                                mData = new ArrayList<>(Arrays.asList(freeGoods.getListOptionFreeGoods()));
                            }

                            if (mData != null && !mData.isEmpty()) {
                                for (ArrayList<OptionFreeGoods> listOptionFreeGoods : mData) {
                                    if (listOptionFreeGoods != null && !listOptionFreeGoods.isEmpty()) {
                                        allSelectedFG.add(listOptionFreeGoods);
                                    }
                                }
                            }
                        }
                        header.setListFreeGoods(allSelectedFG);
                    }

                    Helper.setItemParam(Constants.VISIT_ORDER_HEADER, header);
                }

                openDialog(DIALOG_SIGNATURE);
            }
        });

        imgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewMainActivity) getActivity()).changePage(15);
//                Intent intent = new Intent(OrderSummary2Fragment.this, OrderSummaryDetail2Fragment.class);
//                startActivity(intent);
//                fragment = new OrderSummaryDetailFragment();
//                setContent(fragment);
            }
        });

        imgAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagAttachment = 1;
                if (savedHeader != null) {
                    if (savedHeader.getId() != null) {
                        Helper.setItemParam(Constants.ATTACHMENT, db.getAttachmentSaved(savedHeader.getId()));
                        openDialog(DIALOG_SPECIMEN);
                    }
                } else {
                    Toast.makeText(getContext(), "Tidak ada lampiran", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cbCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtTxtDescRequest.setEnabled(true);
                } else {
                    edtTxtDescRequest.setText(null);
                    edtTxtDescRequest.setEnabled(false);
                }
            }
        });

        edtTxtDescRequest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkRequestDiscSelected();
            }
        });

        return rootView;
    }

    private void setSummaryData() {
        setDataHeader();

        checkFreeGoodsIsEmpty();

        checkRequestDiscSelected();
    }

    private void setDataHeader() {
        if (outletResponse != null) {
            if (getCurrentDate() != null) {
                txtDate.setText(Helper.changeDateFormatDefa(Constants.DATE_TYPE_1, Constants.DATE_TYPE_12, getCurrentDate()));
            }

            if(outletResponse.getVisitDate() == null){
                outletResponse.setVisitDate(Helper.changeDateFormatDefa(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, getCurrentDate()));
            }

            if (outletResponse.getIdOutlet() != null) {
                txtOutlet.setText(Helper.validateResponseEmpty(outletResponse.getIdOutlet()).concat(Constants.STRIP)
                        .concat(db.getOutletName(outletResponse.getIdOutlet())));
                txtViewTotalOrderPlan.setText(String.valueOf(Double.valueOf(db.totalAmountPlanBy(outletResponse.getIdOutlet(), outletResponse.getVisitDate())).longValue()));
            }
        }
    }


    private void checkFreeGoodsIsEmpty() {
        if (savedHeader == null) {
            if (listFreeGoods != null && !listFreeGoods.isEmpty()) {
                setData(listFreeGoods);
                txtNoticeFG.setVisibility(View.GONE);
            } else {
                txtNoticeFG.setVisibility(View.VISIBLE);
            }
        }
    }

    private void checkRequestDiscSelected() {
        if (cbCash.isChecked()) {
            edtTxtDescRequest.setEnabled(true);
        } else {
            edtTxtDescRequest.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "14");
        visitOrderDetailList = (List<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL_SAVE);
        if (visitOrderDetailList != null) {
            if (savedHeader != null) {
                if (savedHeader.getId() != null) {
                    if (savedHeader.getId().contains(Constants.KEY_TRUE_ID_TO)) {
                        btnNext.setVisibility(View.GONE);
                    } else {
                        btnNext.setVisibility(View.VISIBLE);
                    }
                } else {
                    btnNext.setVisibility(View.VISIBLE);
                }
            }

            price = BigDecimal.ZERO;
            disc = BigDecimal.ZERO;

            for (int i = 0; i < visitOrderDetailList.size(); i++) {
                if (visitOrderDetailList.get(i).getPriceBfr() != null) {
                    price = price.add(visitOrderDetailList.get(i).getPriceBfr());
                }
                if (visitOrderDetailList.get(i).getDisc() != null) {
                    disc = disc.add(visitOrderDetailList.get(i).getDisc());
                }
                if (visitOrderDetailList.get(i).getOrderPlanUom1() != null) {
                    if (!visitOrderDetailList.get(i).getOrderPlanUom1().equals("-")) {
                        totalOrderPlan++;
                    }
                }
            }

            txtViewTotalHna.setText(Helper.toRupiahFormat2(String.valueOf(price.setScale(2, RoundingMode.HALF_UP))));
            txtViewDisc.setText(Helper.toRupiahFormat2(String.valueOf(disc)));

            txtViewTotalItems.setText(String.valueOf(visitOrderDetailList.size()));
            total = Helper.getTotalPrice(String.valueOf(price), String.valueOf(disc), getString(R.string.zero));

            txtViewTotal.setText(Helper.toRupiahFormat2(total));

            if (savedHeader != null) {
                if (savedHeader.isRequest_disc()) {
                    cbCash.setChecked(true);
                    edtTxtDescRequest.setEnabled(true);
                } else {
                    cbCash.setChecked(false);
                    edtTxtDescRequest.setEnabled(false);
                }

                if (savedHeader.getDescriptionDisc() != null) {
                    edtTxtDescRequest.setText(savedHeader.getDescriptionDisc());
                }
            }

//            if (outletResponse != null) {
//                if (outletResponse.getStatusCheckIn() != null) {
//                    if (outletResponse.getStatusCheckIn().equals(Constants.FINISHED)) {
//                        cbCash.setEnabled(false);
//                        edtTxtDescRequest.setEnabled(false);
//                    }
//                }
//            }


            calculate();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void initialize() {
        savedHeader = (VisitOrderHeader) Helper.getItemParam(Constants.ORDER_HEADER_SELECTED);
        listFreeGoods = (ArrayList<FreeGoods>) Helper.getItemParam(Constants.LIST_FREE_GOODS);

        imgBack = rootView.findViewById(R.id.imgBack);
        btnNext = rootView.findViewById(R.id.btnNext);
        imgDetail = rootView.findViewById(R.id.imgDetail);
        imgAttachment = rootView.findViewById(R.id.imgAttachment);
        txtNoticeFG = rootView.findViewById(R.id.txtNoticeFG);
        edtTxtDescRequest = rootView.findViewById(R.id.edtTxtDescRequest);
        txtDate = rootView.findViewById(R.id.txtDate);
        txtOutlet = rootView.findViewById(R.id.txtOutlet);
        txtViewTotalHna = rootView.findViewById(R.id.txtViewTotalHna);
        txtViewTotalItems = rootView.findViewById(R.id.txtViewTotalItems);
        txtViewTotalOrderPlan = rootView.findViewById(R.id.txtViewTotalOrderPlan);
        cbCash = rootView.findViewById(R.id.cbCash);
        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        txtViewDisc = rootView.findViewById(R.id.txtViewDisc);
        txtViewPotongan = rootView.findViewById(R.id.potongan);
        txtViewPpn = rootView.findViewById(R.id.txtPpn);
        txtAmountPpn = rootView.findViewById(R.id.ppn);
        txtViewTotal = rootView.findViewById(R.id.txtViewTotal);
        rowRequest = rootView.findViewById(R.id.rowRequest);

    }

    private void getSavedFreeGoods() {
        if (savedHeader != null) {
            if (savedHeader.getId() != null) {
                if (savedHeader.getId().contains(Constants.KEY_TRUE_ID_TO)) {

                    cbCash.setEnabled(false);
                    edtTxtDescRequest.setEnabled(false);

                    listSyncedFG = new ArrayList<>();
                    arrayListOptionFG = new ArrayList<>();
                    listSyncedOptionFG = new ArrayList<>();

                    listSyncedOptionFG = db.getOptionFreeGoodsByIdGroupedBy(savedHeader.getId());

                    if (listSyncedOptionFG != null && !listSyncedOptionFG.isEmpty()) {

                        listSyncedFG = new ArrayList<>();
                        for (int i = 0; i < listSyncedOptionFG.size(); i++) {
                            FreeGoods fg = new FreeGoods();

                            if (listSyncedOptionFG.get(i).getJenisJual() != null) {
                                fg.setJenisJual(listSyncedOptionFG.get(i).getJenisJual());
                            }

                            if (listSyncedOptionFG.get(i).getKlasifikasi() != null) {
                                fg.setKlasifikasi(listSyncedOptionFG.get(i).getKlasifikasi());
                            }

                            if (listSyncedOptionFG.get(i).getTop() != null) {
                                fg.setTopF(listSyncedOptionFG.get(i).getTop());
                            }

                            if (fg.getJenisJual() != null && fg.getKlasifikasi() != null && fg.getTopF() != null) {
                                listSyncedInsideOptionFreeGoods = db.getOptionFreeGoodsBy4Id(savedHeader.getId(), fg.getJenisJual(), fg.getKlasifikasi(), fg.getTopF());
                                arrayListOptionFG.add(listSyncedInsideOptionFreeGoods);
                                fg.setArraylistOptionFG(arrayListOptionFG);
                            }

                            listSyncedFG.add(fg);

                        }
                        setData(listSyncedFG);
                    }
                } else {
                    if (listFreeGoods != null && !listFreeGoods.isEmpty()) {
                        setData(listFreeGoods);
                        txtNoticeFG.setVisibility(View.GONE);
                    } else {
                        txtNoticeFG.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private void setData(final ArrayList<FreeGoods> list) {
        mAdapter = new FreeGoodsNAdapter(OrderSummary2Fragment.this, list, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    public void calculate() {

        visitOrderDetailList = (List<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL_SAVE);
//        Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL_SAVE);

        totalHnaAfter = price;
        potongan = BigDecimal.ZERO;
        amountPpn = BigDecimal.ZERO;
        amount = BigDecimal.ZERO;
        amountSelected = BigDecimal.ZERO;
        ArrayList<ArrayList<OptionFreeGoods>> mData = new ArrayList<>();

        if (listFreeGoods != null && !listFreeGoods.isEmpty()) {
            for (FreeGoods freeGoods : listFreeGoods) {
                if (freeGoods.getListOptionFreeGoods() != null) {
                    mData = new ArrayList<>(Arrays.asList(freeGoods.getListOptionFreeGoods()));

                    for (ArrayList<OptionFreeGoods> listOptionFG : mData) {
                        for (OptionFreeGoods optionFG : listOptionFG) {
                            if (optionFG.getId_material() != null) {
                                if (optionFG.getId_material().toLowerCase().contains(String.valueOf(Constants.DISKON).toLowerCase())) {
                                        potongan = potongan.add(optionFG.getAmount()==null?BigDecimal.ZERO: optionFG.getAmount().
                                                add(optionFG.getAmountP()==null?BigDecimal.ZERO: optionFG.getAmountP()));
                                }
                            }
                        }
                    }
                }
            }

            Helper.validateAmountLessThanZero(potongan);
        }

        totalHnaAfter = totalHnaAfter.add(disc).subtract(potongan);
        Helper.validateAmountLessThanZero(totalHnaAfter);

        if (visitOrderDetailList != null && !visitOrderDetailList.isEmpty()) {
            if (visitOrderDetailList.get(0).getTax() != null) {
                if (visitOrderDetailList.get(0).getTax().contains(getString(R.string.percent))){
                    ppn = String.valueOf(new BigDecimal(visitOrderDetailList.get(0).getTax().replace(getString(R.string.percent), Constants.EMPTY_STRING))
                            .divide(new BigDecimal(getString(R.string.percent_amount))));
                }

                if (ppn.equals("")) {
                    ppn = "0";
                }

                amountPpn = ((totalHnaAfter).multiply(new BigDecimal(ppn))).setScale(2, BigDecimal.ROUND_HALF_UP);

                try {
                    txtViewPpn.setText(getString(R.string.PPN).concat(getString(R.string.op_brackets)).concat(visitOrderDetailList.get(0).getTax()).concat(getString(R.string.cl_brackets)));
                }catch(NullPointerException ignored){

                }
            }
        }

        txtAmountPpn.setText(Helper.toRupiahFormat2(String.valueOf(amountPpn)));
        txtViewPotongan.setText(Helper.toRupiahFormat2(String.valueOf(potongan.setScale(2, BigDecimal.ROUND_HALF_UP))));
        txtViewTotal.setText(Helper.toRupiahFormat2(String.valueOf((totalHnaAfter.add(amountPpn)).setScale(2, BigDecimal.ROUND_HALF_UP))));
    }

    private static void calculateAmountDiskon(String selected) {
        selectedDiskon = selected.split(Constants.SPACE);
        if (selectedDiskon[1].contains(Constants.COMA)) {
            amount = new BigDecimal(selectedDiskon[1].replace(Constants.COMA, Constants.EMPTY_STRING));
        } else if (selectedDiskon[1].contains(Constants.DOT)) {
            amount = new BigDecimal(selectedDiskon[1].replace(Constants.DOT, Constants.EMPTY_STRING));
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem next = menu.findItem(R.id.action_next);
        final MenuItem detail = menu.findItem(R.id.action_detail);
        final MenuItem attachment = menu.findItem(R.id.action_attachment);

        next.setVisible(true);
        detail.setVisible(true);
        attachment.setVisible(true);

        if (savedHeader != null) {
            if (savedHeader.getId() != null) {
                if (savedHeader.getId().contains(Constants.KEY_TRUE_ID_TO)) {
                    next.setVisible(false);
                } else {
                    next.setVisible(true);
                }
            } else {
                next.setVisible(true);
            }
        }

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
                    Toast.makeText(getContext(), "Tidak ada lampiran", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        detail.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
//                fragment = new OrderSummaryDetailFragment();
//                setContent(fragment);
                return false;
            }
        });

        next.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                preventDupeDialog();
                VisitOrderHeader header = new VisitOrderHeader();
                int flag_unselected = 0;
                if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                    header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);

                    if (cbCash.isChecked()) {
                        header.setRequestDisc(1);
                        header.setRequest_disc(true);
                    } else {
                        header.setRequestDisc(0);
                        header.setRequest_disc(false);
                    }

                    if (!Helper.isEmpty(edtTxtDescRequest)) {
                        header.setDescriptionDisc(edtTxtDescRequest.getText().toString().trim());
                    } else {
                        header.setDescriptionDisc(null);
                    }

                    if (totalHnaAfter != null && amountPpn != null) {
                        header.setTotalAmount(totalHnaAfter.add(amountPpn));
                    }

                    if (listFreeGoods != null && !listFreeGoods.isEmpty()) {
                        ArrayList<ArrayList<OptionFreeGoods>> allSelectedFG = new ArrayList<ArrayList<OptionFreeGoods>>();
                        for (FreeGoods freeGoods : listFreeGoods) {
                            if (freeGoods.getListOptionFreeGoods() != null) {
                                mData = new ArrayList<>(Arrays.asList(freeGoods.getListOptionFreeGoods()));
                            }

                            if (mData != null && !mData.isEmpty()) {
                                for (ArrayList<OptionFreeGoods> listOptionFreeGoods : mData) {
                                    if (listOptionFreeGoods != null && !listOptionFreeGoods.isEmpty()) {
                                        allSelectedFG.add(listOptionFreeGoods);
                                    }
                                }
                            }
                        }
                        header.setListFreeGoods(allSelectedFG);
                    }

                    Helper.setItemParam(Constants.VISIT_ORDER_HEADER, header);
                }

                openDialog(DIALOG_SIGNATURE);
                return false;
            }
        });
    }


}
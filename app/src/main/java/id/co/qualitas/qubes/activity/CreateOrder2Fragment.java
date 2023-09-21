package id.co.qualitas.qubes.activity;

import android.app.DatePickerDialog;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Partner;
import id.co.qualitas.qubes.model.SalesOffice;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitOrderHeader;
import id.co.qualitas.qubes.model.VisitOrderHeaderDropDownResponse;

//import butterknife.OnTextChanged;

public class CreateOrder2Fragment extends BaseFragment {
    private Button btnNext;
    private TextView customerName, txtDate, edtTxtSoldTo, txtTitle;
    private AutoCompleteTextView edtTxtShipTo, edtSalesOffice;
    private EditText edtTxtTglPO, edtTxtNoPO, edtTxtEndDatePO;
    private String dateString;
    private Spinner spinnerShipTo, spinnerSalesOffice;
    private String[] itemsClassification;
    private VisitOrderHeader savedHeader;
    private ArrayList<String> listOutletId;
    private ArrayList<String> listOutlet;
    private ArrayList<String> listPartner;
    private ArrayList<SalesOffice> salesOfficeByIdSalesman;
    private ArrayList<OutletResponse> outletResponseArrayList = new ArrayList<>();
    private ArrayList<Partner> assignPartnerList = new ArrayList<>();
    private ArrayList<SalesOffice> salesOfficeList = new ArrayList<>();
    private ArrayList<VisitOrderHeaderDropDownResponse> visitOrderHeaderDropDownResponseArrayList = new ArrayList<>();
    private String tglPO, endTglPO, salesId;
    private OutletResponse outletResponse, outletDetail;
    private ArrayList<String> listSalesOffice;
    private User user;
    private VisitOrderHeader orderHeader = new VisitOrderHeader();
    private String outletSoldShip = "";
    private String idPlantSalesman;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_create_order, container, false);
        init();
        initialize();

        initFragment();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((MainActivity) getActivity()).changePage(11);
                        return true;
                    }
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changePage(12);
            }
        });
        setAdapterDataShipTo();
        setAdapterDataSalesOffice();

        getData();

        setAutoComplete();
        checkEmptyNoPo();
        validateEditBox();


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "12");
    }

    private void validateEditBox() {
        edtTxtNoPO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkEmptyNoPo();
            }
        });


        edtTxtTglPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtTxtTglPO.isEnabled()) {
                    Toast.makeText(getContext(), R.string.input_no_po, Toast.LENGTH_LONG).show();
                }
            }
        });

        edtTxtEndDatePO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtTxtEndDatePO.isEnabled()) {
                    Toast.makeText(getContext(), R.string.input_ed_po, Toast.LENGTH_LONG).show();
                }
            }
        });

        edtTxtEndDatePO.setInputType(InputType.TYPE_NULL);
        edtTxtEndDatePO.requestFocus();
        edtTxtEndDatePO.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        final Calendar tanggalPenagihan = Calendar.getInstance();
                        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                                getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        tanggalPenagihan.set(year, monthOfYear, dayOfMonth);
                                        endTglPO = Helper.convertDateToStringNew(Constants.DATE_TYPE_2, tanggalPenagihan.getTime());
                                        dateString = Helper.convertDateToStringNew(Constants.DATE_TYPE_1, tanggalPenagihan.getTime());
                                        edtTxtEndDatePO.setText(dateString);
                                        edtTxtEndDatePO.setError(null);
                                    }
                                }, tanggalPenagihan.get(Calendar.YEAR), tanggalPenagihan.get(Calendar.MONTH), tanggalPenagihan.get(Calendar.DAY_OF_MONTH));
                        fromDatePickerDialog.getDatePicker().setCalendarViewShown(false);
                        fromDatePickerDialog.show();
                        break;
                }
                return true;
            }
        });

        edtTxtTglPO.setInputType(InputType.TYPE_NULL);
        edtTxtTglPO.requestFocus();
        edtTxtTglPO.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        final Calendar tanggalPenagihan = Calendar.getInstance();
                        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                                getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        tanggalPenagihan.set(year, monthOfYear, dayOfMonth);
                                        tglPO = Helper.convertDateToStringNew(Constants.DATE_TYPE_2, tanggalPenagihan.getTime());
                                        dateString = Helper.convertDateToStringNew(Constants.DATE_TYPE_1, tanggalPenagihan.getTime());
                                        edtTxtTglPO.setText(dateString);
                                        edtTxtTglPO.setError(null);
                                    }
                                }, tanggalPenagihan.get(Calendar.YEAR), tanggalPenagihan.get(Calendar.MONTH), tanggalPenagihan.get(Calendar.DAY_OF_MONTH));
                        fromDatePickerDialog.getDatePicker().setCalendarViewShown(false);
                        fromDatePickerDialog.show();
                        break;
                }
                return true;
            }
        });
    }

    private void setAdapterDataSalesOffice() {
        if (attendances != null) {
            if (attendances.get(0).getIdPlant() != null) {
                salesOfficeList = new ArrayList<>();
//                salesOfficeList = db.getSalesOfficeByIdPlant(attendances.get(0).getIdPlant());
                salesOfficeList = db.getAllSalesOffice();
            }
        }

        if (salesOfficeList != null && !salesOfficeList.isEmpty()) {
            listSalesOffice = new ArrayList<>();
            for (int i = 0; i < salesOfficeList.size(); i++) {
                if (salesOfficeList.get(i).getId() != null && salesOfficeList.get(i).getName() != null) {
                    listSalesOffice.add(i, salesOfficeList.get(i).getId() + getString(R.string.connectorIdWithName) + salesOfficeList.get(i).getName());
                }
            }
            String[] listSales = new String[listSalesOffice.size()];
            listSales = listSalesOffice.toArray(listSales);
            setSpinnerAdapter3(listSales, spinnerSalesOffice);
        }
    }

    private void setAdapterDataShipTo() {
        assignPartnerList = db.getListPartner(outletResponse.getIdOutlet());
        if (assignPartnerList != null && !assignPartnerList.isEmpty()) {
            listPartner = new ArrayList<>();
            for (int i = 0; i < assignPartnerList.size(); i++) {
                if (assignPartnerList.get(i).getId_partner() != null && assignPartnerList.get(i).getNamePartner() != null) {
                    listPartner.add(i, assignPartnerList.get(i).getId_partner() + getString(R.string.connectorIdWithName) + assignPartnerList.get(i).getNamePartner());
                }
            }

            String[] listPartnerArray = new String[listPartner.size()];
            listPartnerArray = listPartner.toArray(listPartnerArray);
            setSpinnerAdapter3(listPartnerArray, spinnerShipTo);
        }
    }

    private void getData() {
        if (!Helper.getItemParam(Constants.ORDER_TYPE).equals(Constants.ORDER_CANVAS_TYPE)) {
            if (outletDetail != null) {
                if (outletDetail.getSegment() != null) {
                    if (outletDetail.getSegment().equals(Constants.KEY_NO_SEGMENT_MANDATORY)) {
                        txtTitle.setText((getString(R.string.navmenu5c)).concat("(MTKA)"));
                    }
                }
            }
        }

        if (user != null) {
            if (user.getId_sales_office() != null) {
                SalesOffice salesOffice = db.getSalesOfficeByIdSalesOffice(user.getId_sales_office());

                if (salesOffice != null
                        && (salesOfficeList != null && !salesOfficeList.isEmpty())) {
//                    for (int i = 0; i < salesOfficeList.size(); i++) {
//                        if (salesOffice.getId().equals(salesOfficeList.get(i).getId())) {
                    try {
                        int spinnerPosition = getSpinnerAdapter().getPosition(salesOffice.getId().concat(getString(R.string.connectorIdWithName)).concat(salesOffice.getName()));
                        spinnerSalesOffice.setSelection(spinnerPosition);
                    } catch (NullPointerException ignored) {

                    }

//                        }
//                    }
                }
            }
        }

        if (outletResponse.getIdOutlet() != null && outletResponse.getOutletName() != null) {
            customerName.setText(outletResponse.getIdOutlet() + getString(R.string.connectorIdWithName) + outletResponse.getOutletName());
            edtTxtSoldTo.setText(outletResponse.getIdOutlet() + getString(R.string.connectorIdWithName) + outletResponse.getOutletName());
        }

        if (outletResponse != null) {
            if (outletResponse.getVisitDate() != null) {
                txtDate.setText(Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_12, outletResponse.getVisitDate()));
            }
        }

        if (savedHeader != null) {
            setDataSaved();
        } else if (orderHeader != null) {
            setDataFromDb();
        }
    }

    private void checkEmptyNoPo() {
        if (edtTxtNoPO.getText().toString().matches("") || edtTxtNoPO.getText().toString().isEmpty()) {
            setEnabledPO(false);
        } else {
            setEnabledPO(true);
        }
    }

    private void setEnabledPO(boolean b) {
        edtTxtEndDatePO.setEnabled(b);
        edtTxtTglPO.setEnabled(b);
        edtTxtTglPO.setBackground(getResources().getDrawable(R.drawable.editbox));
        edtTxtEndDatePO.setBackground(getResources().getDrawable(R.drawable.editbox));

        if (!b) {
            edtTxtEndDatePO.setText(null);
            edtTxtTglPO.setText(null);
            edtTxtTglPO.setBackground(getResources().getDrawable(R.drawable.editbox_disable));
            edtTxtEndDatePO.setBackground(getResources().getDrawable(R.drawable.editbox_disable));
        }
    }

    private void setDataFromDb() {
        if (orderHeader.getShipTo() != null) {
            outletSoldShip = db.getPartnerName(orderHeader.getShipTo());
        }

        if (orderHeader.getNoPo() != null) {
            edtTxtNoPO.setText(orderHeader.getNoPo());
        }

        if (orderHeader.getTglPoString() != null) {
            edtTxtTglPO.setText(orderHeader.getTglPoString());
        }

        if (orderHeader.getEdPoString() != null) {
            edtTxtEndDatePO.setText(orderHeader.getEdPoString());
        }


        if (orderHeader.getSalesOffice() != null) {
            setAdapterDataSalesOffice();
            if (listSalesOffice != null) {
                for (int i = 0; i < listSalesOffice.size(); i++) {
                    if (listSalesOffice.get(i).contains(orderHeader.getSalesOffice())) {
                        int spinnerPosition = getSpinnerAdapter().getPosition(listSalesOffice.get(i));
                        spinnerSalesOffice.setSelection(spinnerPosition);
                    }
                }
            }
        }

        if (orderHeader.getShipTo() != null) {
            setAdapterDataShipTo();
            if (listPartner != null) {
                for (int i = 0; i < listPartner.size(); i++) {
                    if (listPartner.get(i).contains(orderHeader.getShipTo())) {
                        int spinnerPosition = getSpinnerAdapter().getPosition(listPartner.get(i));
                        spinnerShipTo.setSelection(spinnerPosition);
                    }
                }
            }
        }
    }

    private void setDataSaved() {
        if (savedHeader != null) {
            if (savedHeader.getNoPo() != null) {
                edtTxtNoPO.setText(savedHeader.getNoPo());
            }
            if (savedHeader.getTglPoString() != null) {
                edtTxtTglPO.setText(savedHeader.getTglPoString());
            }
            if (savedHeader.getEdPoString() != null) {
                edtTxtEndDatePO.setText(savedHeader.getEdPoString());
            }

            if (savedHeader.getShipTo() != null) {
                setAdapterDataShipTo();
                if (listPartner != null) {
                    for (int i = 0; i < listPartner.size(); i++) {
                        if (listPartner.get(i).contains(savedHeader.getShipTo())) {
                            int spinnerPosition = getSpinnerAdapter().getPosition(listPartner.get(i));
                            spinnerShipTo.setSelection(spinnerPosition);
                        }
                    }
                }
            }
            if (savedHeader.getSalesOffice() != null) {
                setAdapterDataSalesOffice();
                if (listSalesOffice != null) {
                    for (int i = 0; i < listSalesOffice.size(); i++) {
                        if (listSalesOffice.get(i).contains(savedHeader.getSalesOffice())) {
                            int spinnerPosition = getSpinnerAdapter().getPosition(listSalesOffice.get(i));
                            spinnerSalesOffice.setSelection(spinnerPosition);
                        }
                    }
                }
            }

            Helper.removeItemParam(Constants.VISIT_ORDER_HEADER);
        }
    }

    private void disableAllComponents(Boolean b) {
        if (b) {
            spinnerShipTo.setEnabled(false);
            edtTxtTglPO.setEnabled(false);
            edtTxtNoPO.setEnabled(false);
            edtTxtEndDatePO.setEnabled(false);
            spinnerSalesOffice.setEnabled(false);
        } else {
            spinnerShipTo.setEnabled(true);
            edtTxtTglPO.setEnabled(true);
            edtTxtNoPO.setEnabled(true);
            edtTxtEndDatePO.setEnabled(true);
            spinnerSalesOffice.setEnabled(true);
        }
    }

    private void setAutoComplete() {
        listOutlet = new ArrayList<>();
        listOutletId = new ArrayList<>();
        ArrayList<OutletResponse> outletList;

        outletList = db.getAllOutlet();

        for (int i = 0; i < outletList.size(); i++) {
            listOutlet.add(i, outletList.get(i).getOutletName());
            listOutletId.add(i, outletList.get(i).getIdOutlet());
        }
    }

    private void checkValidation() {
        int checkSegment = 0;

        String noPo = edtTxtNoPO.getText().toString().trim();
        String tglPo = edtTxtTglPO.getText().toString().trim();
        String edPo = edtTxtEndDatePO.getText().toString().trim();

        if (spinnerShipTo.getSelectedItem() != null) {
            if (!Helper.getItemParam(Constants.ORDER_TYPE).equals(Constants.ORDER_CANVAS_TYPE)) {
                if (outletDetail.getSegment() != null) {
                    if (outletDetail.getSegment().equals(Constants.KEY_NO_SEGMENT_MANDATORY)) {
                        if (noPo.equals(Constants.EMPTY_STRING) || tglPo.equals(Constants.EMPTY_STRING) || edPo.equals(Constants.EMPTY_STRING)) {
                            checkSegment = 0;
                        } else {
                            checkSegment = 1;
                        }
                    } else {
                        checkSegment = 1;
                    }
                }
            } else {
                checkSegment = 1;
            }
        } else {
            checkSegment = 2;
        }


        if (checkSegment == 1) {
            VisitOrderHeader visitOrderHeader = new VisitOrderHeader();

            if (!edtTxtTglPO.getText().toString().trim().equals("")) {
                if (orderHeader != null) {
                    tglPO = edtTxtTglPO.getText().toString().trim();
                } else {
                    tglPO = Helper.changeDateFormatDefa(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, edtTxtTglPO.getText().toString().trim());
                }
            }


            if (!edtTxtEndDatePO.getText().toString().trim().equals("")) {
                if (orderHeader != null) {
                    endTglPO = edtTxtEndDatePO.getText().toString().trim();
                } else {
                    endTglPO = Helper.changeDateFormatDefa(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, edtTxtEndDatePO.getText().toString().trim());
                }
            }

            visitOrderHeader.setTglPoString(tglPO);
            visitOrderHeader.setEdPoString(endTglPO);
            visitOrderHeader.setNoPo(edtTxtNoPO.getText().toString().trim());
            if (spinnerSalesOffice.getSelectedItem() != null) {
                visitOrderHeader.setSalesOffice(spinnerSalesOffice.getSelectedItem().toString().split(getResources().getString(R.string.connectorIdWithName))[0].trim());
                visitOrderHeader.setSalesOfficeName(spinnerSalesOffice.getSelectedItem().toString().split(getResources().getString(R.string.connectorIdWithName))[1].trim());
            }
            if (outletResponse != null) {
                if (outletResponse.getIdOutlet() != null) {
                    visitOrderHeader.setSoldTo(outletResponse.getIdOutlet());
                }
            }

            try {
                if (spinnerShipTo.getSelectedItem().toString().contains(getResources().getString(R.string.connectorIdWithName))) {
                    visitOrderHeader.setShipTo(spinnerShipTo.getSelectedItem().toString().split(getResources().getString(R.string.connectorIdWithName))[0].trim());
                }
            } catch (NullPointerException ignored) {

            }
            visitOrderHeader.setIdEmployee(user.getIdEmployee());
            visitOrderHeader.setIdOutlet(outletResponse.getIdOutlet());
            visitOrderHeader.setStatusString(getString(R.string.status_on_progress));
            visitOrderHeader.setStatusPrice(getString(R.string.status_price_unavailable));

            if (orderHeader != null) {
                if (orderHeader.getId() != null) {
                    visitOrderHeader.setId(orderHeader.getId());
                }
            }

            Helper.setItemParam(Constants.VISIT_ORDER_HEADER, visitOrderHeader);
            Helper.removeItemParam(Constants.GET_DETAIL_VISIT);
            Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
            Helper.removeItemParam(Constants.FROM_SALES_ORDER);

            ((MainActivity) getActivity()).changePage(13);
//            Intent intent = new Intent(CreateOrder2Fragment.this, CreateOrderDetail2Fragment.class);
//            startActivity(intent);
//            fragment = new OrderFragment();
//            setContent(fragment);
        } else if (checkSegment == 0) {
            Toast.makeText(getContext(), R.string.mtka_mandatory_notice, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.no_partner_notice, Toast.LENGTH_LONG).show();
        }
    }

//    private void setContentWithTag(Fragment fragment){
//        final Activity activity = getActivity();
//        //Close keyBoard in transition
//        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(
//                Context.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.nav_contentframe, fragment, "OrderFragment");
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft.addToBackStack(null);
//        ft.commit();
//    }

    public void initialize() {
        db = new DatabaseHelper(getContext());
        btnNext = rootView.findViewById(R.id.btnNext);
        imgBack = rootView.findViewById(R.id.imgBack);
        txtTitle = rootView.findViewById(R.id.txtTitle);
        customerName = rootView.findViewById(R.id.txtOutlet);
        txtDate = rootView.findViewById(R.id.txtDate);
        edtTxtSoldTo = rootView.findViewById(R.id.edtTxtSoldTo);
        edtTxtTglPO = rootView.findViewById(R.id.edtTxtTglPO);
        edtTxtNoPO = rootView.findViewById(R.id.edtTxtNoPO);
        edtTxtEndDatePO = rootView.findViewById(R.id.edtTxtEndDatePO);

        spinnerShipTo = rootView.findViewById(R.id.spinnerShipTo);
        spinnerSalesOffice = rootView.findViewById(R.id.spinnerSales);
        savedHeader = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        outletDetail = db.getDetailOutlet(outletResponse.getIdOutlet());
        orderHeader = (VisitOrderHeader) Helper.getItemParam(Constants.ORDER_HEADER_SELECTED);
        if (orderHeader != null) {
            if (orderHeader.getId() != null) {
                if (orderHeader.getId().contains(Constants.KEY_TRUE_ID_TO)) {
                    disableAllComponents(true);
                } else {
                    disableAllComponents(false);
                }
            }

        }

        idPlantSalesman = (String) Helper.getItemParam(Constants.ID_PLANT_SALESMAN);
    }

    //    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem next = menu.findItem(R.id.action_next);

        next.setVisible(true);

        next.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                checkValidation();

                return false;
            }
        });
    }
}

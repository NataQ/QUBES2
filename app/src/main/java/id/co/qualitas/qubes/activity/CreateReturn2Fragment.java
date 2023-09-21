package id.co.qualitas.qubes.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.CreateNewReturnAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.ReturnHeader;
import id.co.qualitas.qubes.model.ReturnRequest;
import id.co.qualitas.qubes.model.ReturnResponse;
import id.co.qualitas.qubes.model.UnitOfMeasure;
import id.co.qualitas.qubes.model.User;

public class CreateReturn2Fragment extends BaseFragment {
    private Button btnSave;
    private Button btnAdd;
    private ArrayList<Return> returnList = new ArrayList<>();
    private ArrayList<String> listSavedBrb = new ArrayList<>();
    private List<ReturnRequest> returnHeaderList = new ArrayList<>();
    private ArrayList<UnitOfMeasure> listUom = new ArrayList<>();
    private LinearLayout mLinearLayout;
    private Return returns;
    private ReturnHeader returnHeader = null;
    private ReturnResponse returnResponse = null;
    private ReturnRequest returnRequest;
    private DatabaseHelper db;
    private EditText noBrb, noRetur, tglBRB, tglRR;
    private DatePickerDialog dateBRBPickerDialog, dateRRPickerDialog;
    private Calendar date;
    private CreateNewReturnAdapter mAdapter;
    private Material material;

    public ArrayList<Reason> getReasonArrayList() {
        return reasonArrayList;
    }

    private ArrayList<Reason> reasonArrayList = new ArrayList<>();

    public ArrayList<Reason> getReasonGoodList() {
        return reasonGoodList;
    }

    public ArrayList<Reason> getReasonBadList() {
        return reasonBadList;
    }

    private ArrayList<Reason> reasonGoodList = new ArrayList<>();
    private ArrayList<Reason> reasonBadList = new ArrayList<>();
    private List<String> selectedReason;
    private RadioGroup rgStock;
    private RadioButton rbGood, rbBad;
    private User user;
    private int errorBrb = 0;
//    private TextView customerName;
    private ImageView imgSave;
    View parentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_create_return, container, false);

        initialize();
        init();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((MainActivity) getActivity()).changePage(17);
                        return true;
                    }
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changePage(17);
            }
        });

//        if(outletResponse != null){
//            customerName.setText(Helper.validateResponseEmpty(outletResponse.getOutletName()));
//        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.removeItemParam(Constants.STORE_CHECK_DETAIL);
                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
                Helper.setItemParam(Constants.RETURN_DETAIL, returnList);
                openDialog(DIALOG_ADD_MATERIAL);
            }
        });

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogview;
                final Dialog alertDialog = new Dialog(getContext());
                dialogview = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(dialogview);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btnCancel = alertDialog.findViewById(R.id.btnCancel);
                Button btnSave = alertDialog.findViewById(R.id.btnSave);
                TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);

                txtDialog.setText(getResources().getString(R.string.saveReturnOrNot));

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (noBrb.getText().toString().equals(Constants.EMPTY_STRING)) {
                            snackBar(rootView, R.string.errorNoBRB);
                        } else if (tglBRB.getText().toString().equals(Constants.EMPTY_STRING)) {
                            snackBar(rootView, R.string.errorTglBRB);
                        } else if (returnList == null || returnList.size() == 0) {
                            snackBar(rootView, R.string.errorMaterialReturn);
                        } else {
                            String error = null;
                            for (int i = 0; i < returnList.size(); i++) {
                                error = validateMaterial(returnList.get(i));
                                returnList.get(i).setError(error);
                                if (error != null) {
                                    break;
                                }
                            }
                            if (error == null) {
                                returnResponse = new ReturnResponse();
                                returnResponse.setId("");
                                returnResponse.setNoBrb(noBrb.getText().toString().trim());
                                returnResponse.setTanggalBrb(Helper.changeDateFormat(Constants.DATE_TYPE_12, Constants.DATE_TYPE_2, tglBRB.getText().toString()));
                                if (!noRetur.getText().toString().equals(Constants.EMPTY_STRING)) {
                                    returnResponse.setTanggalRr(Helper.changeDateFormat(Constants.DATE_TYPE_12, Constants.DATE_TYPE_2, tglRR.getText().toString()));
                                }
                                returnResponse.setNoRr(noRetur.getText() != null ? noRetur.getText().toString().trim() : null);

                                returnRequest = new ReturnRequest();
                                returnRequest.setIdHeader(Constants.ID_RETURN.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime()))));
                                returnRequest.setIdEmployee(user.getIdEmployee());
                                returnRequest.setIdOutlet(outletResponse.getIdOutlet());
                                returnRequest.setNoBrb(noBrb.getText().toString());
                                returnRequest.setTanggalBrbString(Helper.changeDateFormat(Constants.DATE_TYPE_12, Constants.DATE_TYPE_2, tglBRB.getText().toString()));
                                if (!noRetur.getText().toString().equals(Constants.EMPTY_STRING)) {
                                    returnRequest.setTanggalRrString(Helper.changeDateFormat(Constants.DATE_TYPE_12, Constants.DATE_TYPE_2, tglRR.getText().toString()));
                                }
                                returnRequest.setNoRr(noRetur.getText().toString());
                                returnRequest.setReturnDetail(returnList);

                                if (returnList != null && !returnList.isEmpty()) {
                                    if (listSavedBrb != null && !listSavedBrb.isEmpty()) {
                                        for (int i = 0; i < listSavedBrb.size(); i++) {
                                            if (returnRequest.getNoBrb().equals(listSavedBrb.get(i))) {
                                                errorBrb++;
                                            }
                                        }
                                    }

                                    if (errorBrb == 0) {

                                        returnRequest.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                        db.addReturnHeader(returnRequest);

                                        for (int i = 0; i < returnList.size(); i++) {
                                            returnList.get(i).setIdReturn(returnRequest.getIdHeader());
                                            db.addReturnDetail(returnList.get(i));
                                        }
                                        ((MainActivity) getActivity()).changePage(17);
                                    } else {
                                        errorBrb = 0;
                                        snackBar(rootView, R.string.usedNoBrb);
                                    }
                                }
                            } else {
                                setDataErr();
                            }
                        }

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        returnHeader = new ReturnHeader();

        //datepicker dialog
        tglBRB.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        date = Calendar.getInstance();
                        dateBRBPickerDialog = new DatePickerDialog(getContext(),
                                new DatePickerDialog.OnDateSetListener() {

                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,
                                                          int dayOfMonth) {
                                        //toast validasi
                                        date.set(year, monthOfYear, dayOfMonth);
                                        String dateString1 = Helper.convertDateToString(Constants.DATE_TYPE_12, date.getTime());
                                        tglBRB.setText(dateString1);
                                    }

                                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
//                        dateBRBPickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                        dateBRBPickerDialog.getDatePicker().setCalendarViewShown(false);
                        dateBRBPickerDialog.show();
                        break;
                }
                return true;
            }
        });

        tglRR.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        date = Calendar.getInstance();
                        dateRRPickerDialog = new DatePickerDialog(
                                getContext(),
                                new DatePickerDialog.OnDateSetListener() {

                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,
                                                          int dayOfMonth) {
                                        //toast validasi
                                        date.set(year, monthOfYear, dayOfMonth);
                                        String dateString1 = Helper.convertDateToString(Constants.DATE_TYPE_12, date.getTime());
                                        tglRR.setText(dateString1);
                                    }

                                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
                        dateRRPickerDialog.getDatePicker().setCalendarViewShown(false);
                        dateRRPickerDialog.show();
                        break;
                }
                return true;
            }
        });

        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem save = menu.findItem(R.id.action_save);

        save.setVisible(true);

        save.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogview;
                final Dialog alertDialog = new Dialog(getContext());
                dialogview = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(dialogview);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btnCancel = alertDialog.findViewById(R.id.btnCancel);
                Button btnSave = alertDialog.findViewById(R.id.btnSave);
                TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);

                txtDialog.setText(getResources().getString(R.string.saveReturnOrNot));

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (noBrb.getText().toString().equals(Constants.EMPTY_STRING)) {
                            snackBar(rootView, R.string.errorNoBRB);
                        } else if (tglBRB.getText().toString().equals(Constants.EMPTY_STRING)) {
                            snackBar(rootView, R.string.errorTglBRB);
                        } else if (returnList == null || returnList.size() == 0) {
                            snackBar(rootView, R.string.errorMaterialReturn);
                        } else {
                            String error = null;
                            for (int i = 0; i < returnList.size(); i++) {
                                error = validateMaterial(returnList.get(i));
                                returnList.get(i).setError(error);
                                if (error != null) {
                                    break;
                                }
                            }
                            if (error == null) {
                                returnResponse = new ReturnResponse();
                                returnResponse.setId("");
                                returnResponse.setNoBrb(noBrb.getText().toString().trim());
                                returnResponse.setTanggalBrb(Helper.changeDateFormat(Constants.DATE_TYPE_12, Constants.DATE_TYPE_2, tglBRB.getText().toString()));
                                if (!noRetur.getText().toString().equals(Constants.EMPTY_STRING)) {
                                    returnResponse.setTanggalRr(Helper.changeDateFormat(Constants.DATE_TYPE_12, Constants.DATE_TYPE_2, tglRR.getText().toString()));
                                }
                                returnResponse.setNoRr(noRetur.getText() != null ? noRetur.getText().toString().trim() : null);

                                returnRequest = new ReturnRequest();
                                returnRequest.setIdHeader(Constants.ID_RETURN.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime()))));
                                returnRequest.setIdEmployee(user.getIdEmployee());
                                returnRequest.setIdOutlet(outletResponse.getIdOutlet());
                                returnRequest.setNoBrb(noBrb.getText().toString());
                                returnRequest.setTanggalBrbString(Helper.changeDateFormat(Constants.DATE_TYPE_12, Constants.DATE_TYPE_2, tglBRB.getText().toString()));
                                if (!noRetur.getText().toString().equals(Constants.EMPTY_STRING)) {
                                    returnRequest.setTanggalRrString(Helper.changeDateFormat(Constants.DATE_TYPE_12, Constants.DATE_TYPE_2, tglRR.getText().toString()));
                                }
                                returnRequest.setNoRr(noRetur.getText().toString());
                                returnRequest.setReturnDetail(returnList);

                                if (returnList != null && !returnList.isEmpty()) {
                                    if (listSavedBrb != null && !listSavedBrb.isEmpty()) {
                                        for (int i = 0; i < listSavedBrb.size(); i++) {
                                            if (returnRequest.getNoBrb().equals(listSavedBrb.get(i))) {
                                                errorBrb++;
                                            }
                                        }
                                    }

                                    if (errorBrb == 0) {

                                        returnRequest.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                        db.addReturnHeader(returnRequest);

                                        for (int i = 0; i < returnList.size(); i++) {
                                            returnList.get(i).setIdReturn(returnRequest.getIdHeader());
                                            db.addReturnDetail(returnList.get(i));
                                        }
                                        ((MainActivity) getActivity()).changePage(17);
                                    } else {
                                        errorBrb = 0;
                                        snackBar(rootView, R.string.usedNoBrb);
                                    }
                                }
                            } else {
                                setDataErr();
                            }
                        }

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

                return false;
            }
        });
    }

    private String validateMaterial(Return listReturn) {
        String results = null;
        if (listReturn.getQty1() == null || String.valueOf(listReturn.getQty1()).trim().equals(Constants.EMPTY_STRING)) {
            results = Constants.errorQTY;
        } else if (listReturn.getBatch() == null || listReturn.getBatch().trim().equals(Constants.EMPTY_STRING)) {
            results = Constants.errorBatch;
        } else if (listReturn.getExpiredDate() == null || listReturn.getExpiredDate().trim().equals(Constants.EMPTY_STRING)) {
            results = Constants.errorExpiredDate;
        } else if (listReturn.getReason() == null || listReturn.getReason().trim().equals(Constants.EMPTY_STRING) || listReturn.getReason().equals(Constants.REASON)) {
            results = Constants.errorReason;
        }
        return results;
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        parentLayout = rootView.findViewById(android.R.id.content);
        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);
        imgBack = rootView.findViewById(R.id.imgBack);
        imgSave = rootView.findViewById(R.id.imgSave);
        noBrb = rootView.findViewById(R.id.noBrb);
        noRetur = rootView.findViewById(R.id.noReturRep);
        tglBRB = rootView.findViewById(R.id.dateBRB);
        tglRR = rootView.findViewById(R.id.dateRR);
        mLinearLayout = rootView.findViewById(R.id.list_view_add_material);
        btnAdd = rootView.findViewById(R.id.btnAddMaterial);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnSave = rootView.findViewById(R.id.btnSave);
        returnHeaderList = db.getAllListReturnHeader(outletResponse.getIdOutlet());
//        try {
            reasonArrayList = db.getAllReason();
//        }catch (SQLiteException e){
//
//        }
//        customerName = rootView.findViewById(R.id.customerName);

        if (reasonArrayList != null && !reasonArrayList.isEmpty()) {
            for (int i = 0; i < reasonArrayList.size(); i++) {
                if (reasonArrayList.get(i).getType().equals(Constants.COND_RETURN_GOOD)) {
                    reasonGoodList.add(reasonArrayList.get(i));
                }
                if (reasonArrayList.get(i).getType().equals(Constants.COND_RETURN_BAD)) {
                    reasonBadList.add(reasonArrayList.get(i));
                }
            }
        }
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        listSavedBrb = db.getListBRB();
    }

    public void setData() {
        mLinearLayout.removeAllViews();
        returns = new Return();
//        if(outletResponse != null){
//            customerName.setText(Helper.validateResponseEmpty(outletResponse.getOutletName()));
//        }
        if (material != null) {
            returns.setIdMaterial(Helper.validateResponseEmpty(material.getMaterialCode()));
            returns.setMaterialName(Helper.validateResponseEmpty(db.getMaterialName(material.getMaterialCode())));

            if (material.getMaterialCode() != null) {
                listUom = db.getListUomByIdMat(material.getMaterialCode(), Constants.IS_RETURN);
                returns.setListUomName(listUom);
            }


            if (returnList != null && !returnList.isEmpty()) {
                for (int i = 0; i < returnList.size(); i++) {
                    if (returnList.get(i).getIdMaterial().equals(returns.getIdMaterial())) {
                        returnList.remove(i);
                    }
                }
            }
            returnList.add(returns);

            Collections.sort(returnList, new Comparator<Return>() {
                @Override
                public int compare(Return s1, Return s2) {
                    return Helper.ltrim(Helper.validateResponseEmpty(s1.getMaterialName())).compareToIgnoreCase(Helper.ltrim(Helper.validateResponseEmpty(s2.getMaterialName())));
                }
            });

            mAdapter = new CreateNewReturnAdapter(this, returnList);

            for (int i = 0; i < mAdapter.getCount(); i++) {
                View item = mAdapter.getView(i, null, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 15);
                mLinearLayout.addView(item, layoutParams);
            }
            Helper.removeItemParam(Constants.ADD_MATERIAL);
            progress.dismiss();
        }
    }

    private void setDataNew(ArrayList<Return> returnList) {
        mLinearLayout.removeAllViews();

        listUom = new ArrayList<>();

        if (returnList != null && !returnList.isEmpty()) {
            for (int i = 0; i < returnList.size(); i++) {
                Return material = returnList.get(i);

                listUom = db.getListUomByIdMat(material.getIdMaterial(), Constants.IS_RETURN);
                material.setListUomName(listUom);

                selectedReason = new ArrayList<>();

                if (reasonArrayList != null && !reasonArrayList.isEmpty()) {
                    for (int a = 0; a < reasonArrayList.size(); a++) {
                        selectedReason.add(reasonArrayList.get(a).getDesc());
                    }

                    selectedReason.add(selectedReason.size(), Constants.REASON);
//                    selectedReason.add("aaaaaa");
//                    selectedReason.add("bbbbb");
//                    selectedReason.add("cccccc");
                    String[] listReason = new String[selectedReason.size()];
                    listReason = selectedReason.toArray(listReason);
                    material.setItemsReason(listReason);
                }
            }
        }

        mAdapter = new CreateNewReturnAdapter(this, returnList);

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View item = mAdapter.getView(i, null, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 15);
            mLinearLayout.addView(item, layoutParams);
        }

        progress.dismiss();
    }

    public void setDataErr() {
        mLinearLayout.removeAllViews();
        returns = new Return();

        mAdapter = new CreateNewReturnAdapter(this, returnList);

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View item = mAdapter.getView(i, null, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 15);
            mLinearLayout.addView(item, layoutParams);
        }
        Helper.removeItemParam(Constants.ADD_MATERIAL);
        progress.dismiss();
    }


    public void setDataRet(Return returns) {
        mLinearLayout.removeAllViews();
        returns.setMaterialName(Helper.validateResponseEmpty(db.getMaterialName(returns.getIdMaterial())));
//        if (!isNetworkAvailable()) {
        if (returns.getIdMaterial() != null) {
            listUom = db.getListUomByIdMat(returns.getIdMaterial(), Constants.IS_RETURN);
            returns.setListUomName(listUom);
        }
//        }

        returnList.add(returns);
        mAdapter = new CreateNewReturnAdapter(this, returnList);

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View item = mAdapter.getView(i, null, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 15);
            mLinearLayout.addView(item, layoutParams);
        }
        progress.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "18");

        if (Helper.getItemParam(Constants.ADD_MATERIAL) != null) {
            material = (Material) Helper.getItemParam(Constants.ADD_MATERIAL);

            Return addedMat = new Return();
            addedMat.setIdMaterial(Helper.validateResponseEmpty(material.getMaterialCode()));
            addedMat.setMaterialName(Helper.validateResponseEmpty(db.getMaterialName(material.getMaterialCode())));

            if (Helper.getItemParam(Constants.RETURN_DETAIL) != null) {
                returnList = new ArrayList<>();
                returnList = (ArrayList<Return>) Helper.getItemParam(Constants.RETURN_DETAIL);
                Helper.removeItemParam(Constants.RETURN_DETAIL);
                returnList.add(addedMat);
            } else {
                returnList = new ArrayList<>();
                returnList.add(addedMat);
            }

            setDataNew(returnList);

            Helper.removeItemParam(Constants.ADD_MATERIAL);
        }else if(Helper.getItemParam(Constants.RETURN_DETAIL) != null){
            setDataNew((ArrayList<Return>) Helper.getItemParam(Constants.RETURN_DETAIL));
        }
    }

    public void deleteMaterial(int position) {
        returnList.remove(position);
        setDataNew(returnList);
    }

    @Override
    public void onPause() {
        Helper.setItemParam(Constants.RETURN_DETAIL, returnList);

        super.onPause();
    }
}
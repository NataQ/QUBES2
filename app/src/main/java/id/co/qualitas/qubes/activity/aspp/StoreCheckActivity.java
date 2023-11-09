package id.co.qualitas.qubes.activity.aspp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.SpinnerProductStockRequestAdapter;
import id.co.qualitas.qubes.adapter.aspp.SpinnerProductStoreCheckAdapter;
import id.co.qualitas.qubes.adapter.aspp.StockRequestAddAdapter;
import id.co.qualitas.qubes.adapter.aspp.StoreCheckAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.StoreCheck;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class StoreCheckActivity extends BaseActivity {
    private StoreCheckAdapter mAdapter;
    private List<Material> mList;
    private Button btnAdd;
    private TextView txtDate;
    Date fromDate;
    String fromDateString, paramFromDate;
    Calendar todayDate;
    private List<Material> listSpinner, listFilteredSpinner;
    private CardView cvUnCheckAll, cvCheckedAll;
    boolean checkedAll = false;
    private SpinnerProductStoreCheckAdapter spinnerAdapter;
    private String today;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_store_check);

        initialize();
        initData();

        mAdapter = new StoreCheckAdapter(this, mList, header -> {

        });
        recyclerView.setAdapter(mAdapter);

        btnAdd.setOnClickListener(y -> {
            addProduct();
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnSave.setOnClickListener(v -> {
            validateData();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(StoreCheckActivity.this);
        });

//        setDate();
    }

    private void validateData() {
        int param = 0;

        if (Helper.isNotEmptyOrNull(mList)) {
            for (Material material : mList) {
                if (material.getQty() == 0) {
                    param++;
                }

                if (Helper.isNullOrEmpty(material.getUom())) {
                    param++;
                }
            }
        } else {
            param++;
        }

        if (param == 0) {
            progress.show();
            new RequestUrl().execute();//1
        } else {
            setToast(getString(R.string.emptyMaterial) + "\n" + "dan semua field sudah terisi");
        }
    }

    private class RequestUrl extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Map header = new HashMap();
                header.put("id_customer", SessionManagerQubes.getOutletHeader().getId());
                header.put("date", today);
                header.put("username", user.getUsername());
                database.deleteStoreCheck(header);

                for (Material material : mList) {
                    database.addStoreCheck(material, header);
                }
                return true;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("storeCheck", ex.getMessage());
                }
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progress.dismiss();
            if (result) {
                setToast("Save Success");
                onBackPressed();
            } else {
                setToast("Save Failed");
            }
        }
    }

//    private void setDate() {
//        fromDate = Helper.getTodayDate();
//        paramFromDate = new SimpleDateFormat(Constants.DATE_FORMAT_3).format(fromDate);
//
//        txtDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideKeyboard();
//                todayDate = Calendar.getInstance();
//                final Calendar calendar = Calendar.getInstance();
//                calendar.setTime(fromDate);
//                final int year = calendar.get(Calendar.YEAR);
//                final int month = calendar.get(Calendar.MONTH);
//                final int date = calendar.get(Calendar.DATE);
//
//                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        calendar.set(Calendar.YEAR, year);
//                        calendar.set(Calendar.MONTH, month);
//                        calendar.set(Calendar.DATE, dayOfMonth);
//
//                        fromDate = calendar.getTime();
//                        fromDateString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(calendar.getTime());
//                        paramFromDate = new SimpleDateFormat(Constants.DATE_FORMAT_3).format(calendar.getTime());
//                        txtDate.setText(fromDateString);
//                        txtDate.setError(null);
//                    }
//                };
//                DatePickerDialog dialog = new DatePickerDialog(StoreCheckActivity.this, dateSetListener, year, month, date);
//                dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
//                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                dialog.getDatePicker().setLayoutParams(params);
//                dialog.show();
//            }
//        });
//    }

    private void initData() {
        today = Helper.getTodayDate(Constants.DATE_FORMAT_3);
        mList = new ArrayList<>();
        mList.addAll(database.getAllStoreCheck(SessionManagerQubes.getOutletHeader().getId()));
        if (mList != null && mList.size() != 0) {
            today = mList.get(0).getDate();
            txtDate.setText(Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, today));
        } else {
            txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        }
    }

    private void addNew(List<Material> addedList) {
        mList.addAll(addedList);

        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                progress.show();
                mAdapter.notifyDataSetChanged();
            }

            public void onFinish() {
                progress.dismiss();
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        }.start();
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgLogOut = findViewById(R.id.imgLogOut);
        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgBack);
        txtDate = findViewById(R.id.txtDate);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void addProduct() {
        Dialog dialog = new Dialog(StoreCheckActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner_product);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        cvUnCheckAll = dialog.findViewById(R.id.cvUnCheckAll);
        cvCheckedAll = dialog.findViewById(R.id.cvCheckedAll);
        EditText editText = dialog.findViewById(R.id.edit_text);
        RecyclerView rv = dialog.findViewById(R.id.rv);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        listSpinner = new ArrayList<>();
        listSpinner.addAll(initDataMaterial());

        spinnerAdapter = new SpinnerProductStoreCheckAdapter(StoreCheckActivity.this, listSpinner, (nameItem, adapterPosition) -> {
        });

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(spinnerAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spinnerAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cvCheckedAll.setOnClickListener(v -> {
            if (listFilteredSpinner == null) {
                listFilteredSpinner = new ArrayList<>();
            }
            checkedAll = false;
            if (!listFilteredSpinner.isEmpty()) {
                for (Material mat : listFilteredSpinner) {
                    mat.setChecked(checkedAll);
                }
            } else {
                for (Material mat : listSpinner) {
                    mat.setChecked(checkedAll);
                }
            }
            spinnerAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.VISIBLE);
            cvCheckedAll.setVisibility(View.GONE);
        });

        cvUnCheckAll.setOnClickListener(v -> {
            if (listFilteredSpinner == null) {
                listFilteredSpinner = new ArrayList<>();
            }
            checkedAll = true;
            if (!listFilteredSpinner.isEmpty()) {
                for (Material mat : listFilteredSpinner) {
                    mat.setChecked(checkedAll);
                }
            } else {
                for (Material mat : listSpinner) {
                    mat.setChecked(checkedAll);
                }
            }
            spinnerAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.GONE);
            cvCheckedAll.setVisibility(View.VISIBLE);
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnSave.setOnClickListener(v -> {
            List<Material> addList = new ArrayList<>();
            for (Material mat : listSpinner) {
                if (mat.isChecked()) {
                    addList.add(mat);
                }
            }
            addNew(addList);
            dialog.dismiss();
        });
    }

    public void setCheckedAll() {
        int checked = 0;
        for (Material mat : listSpinner) {
            if (mat.isChecked()) {
                checked++;
            }
        }
        if (checked == listSpinner.size()) {
            checkedAll = true;
            spinnerAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.GONE);
            cvCheckedAll.setVisibility(View.VISIBLE);
        } else {
            checkedAll = false;
            spinnerAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.VISIBLE);
            cvCheckedAll.setVisibility(View.GONE);
        }
    }

    public void setFilteredData(List<Material> mFilteredList) {
        listFilteredSpinner = new ArrayList<>();
        listFilteredSpinner.addAll(mFilteredList);

        int checked = 0;
        for (Material mat : listFilteredSpinner) {
            if (mat.isChecked()) {
                checked++;
            }
        }
        if (checked == listFilteredSpinner.size()) {
            checkedAll = true;
            spinnerAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.GONE);
            cvCheckedAll.setVisibility(View.VISIBLE);
        } else {
            checkedAll = false;
            spinnerAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.VISIBLE);
            cvCheckedAll.setVisibility(View.GONE);
        }
    }

    private List<Material> initDataMaterial() {
        List<Material> listSpinner = new ArrayList<>();
        List<Material> listMat = new ArrayList<>();
        listMat.addAll(database.getAllMasterMaterial());

        for (Material param : listMat) {
            int exist = 0;
            for (Material param1 : mList) {
                if (param.getId().equals(param1.getId())) {
                    exist++;
                }
            }
            if (exist == 0) {
                listSpinner.add(param);
            }
        }

        return listSpinner;
    }

    public void delete(int pos) {
        mList.remove(pos);
        mAdapter.notifyItemRemoved(pos);
    }
}
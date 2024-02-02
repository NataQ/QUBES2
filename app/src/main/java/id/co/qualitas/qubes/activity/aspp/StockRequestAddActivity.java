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
import android.widget.LinearLayout;
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
import java.util.Locale;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.SpinnerProductStockRequestAdapter;
import id.co.qualitas.qubes.adapter.aspp.StockRequestAddAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.helper.RecyclerViewMaxHeight;
import id.co.qualitas.qubes.listener.OnLoadMoreListener;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;

public class StockRequestAddActivity extends BaseActivity {
    private StockRequestAddAdapter mAdapter;
    private List<Material> mList;
    private List<Material> listSpinner, listFilteredSpinner;
    private CardView cvUnCheckAll, cvCheckedAll;
    boolean checkedAll = false;
    private SpinnerProductStockRequestAdapter spinnerAdapter;
    private Button btnAdd;
    private TextView txtDate;
    Date fromDate;
    String fromDateString, paramFromDate;
    Calendar todayDate;
    private StockRequest headerRequest;
    private boolean saveDataSuccess = false;
    private WSMessage logResult;
    int offset;
    LinearLayout loadingDataBottom;
    private String searchMat;
    RecyclerViewMaxHeight rv;
    private LinearLayoutManager linearLayoutManMaterial;
    private boolean loading = true;
    protected int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_stock_request_add);

        initialize();
        initData();

        mAdapter = new StockRequestAddAdapter(this, mList, header -> {

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
            logOut(StockRequestAddActivity.this);
        });

        setDate();
    }

    private void validateData() {
        int param = 0, paramMat = 0;

        if (Helper.isEmpty(txtDate)) {
            param++;
            txtDate.setError(getString(R.string.emptyField));
        } else {
            txtDate.setError(null);
        }

        if (mList.isEmpty() || mList == null) {
            param++;
            setToast(getString(R.string.emptyMaterial));
        }

        for (Material mat : mList) {
            if (mat.getQty() == 0 || mat.getUom().equals("-")) {
                paramMat++;
                break;
            }
        }
        if (paramMat > 0) {
            setToast("Qty dan Uom tidak boleh kosong");
        }

        param = param + paramMat;

        if (param == 0) {
            PARAM = 1;
            progress.show();
            new RequestUrl().execute();//1
        }
    }

    private void prepareData() {
        headerRequest = new StockRequest();
        String date = Helper.changeDateFormat(Constants.DATE_FORMAT_5, Constants.DATE_FORMAT_3, txtDate.getText().toString().trim());
        headerRequest.setReq_date(date);
        headerRequest.setId_mobile(Constants.ID_RS_MOBILE.concat(user.getUsername()).concat(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
        headerRequest.setId_salesman(user.getUsername());
        headerRequest.setStatus(Constants.STATUS_DRAFT);
        headerRequest.setEnabled(1);
        headerRequest.setIsSync(0);
        headerRequest.setMaterialList(mList);
    }

    private void saveDataToDatabase() {
        int idHeader = database.addStockRequestHeader(headerRequest, user.getUsername());

        for (Material param : mList) {
            database.addStockRequestDetail(param, String.valueOf(idHeader), user.getUsername());
        }
    }

    private void setDate() {
        fromDate = Helper.getTodayDate();
        paramFromDate = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(fromDate);
        txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                openDateDialog();
            }
        });
    }

    private void openDateDialog() {
        todayDate = Calendar.getInstance();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DATE);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, dayOfMonth);

                fromDate = calendar.getTime();
                fromDateString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(calendar.getTime());
                paramFromDate = new SimpleDateFormat(Constants.DATE_FORMAT_3).format(calendar.getTime());
                txtDate.setText(fromDateString);
                txtDate.setError(null);
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(StockRequestAddActivity.this, dateSetListener, year, month, date);
        dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
        dialog.show();
    }

    private void initData() {
        mList = new ArrayList<>();
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

    private void addProduct() {
        searchMat = null;
        offset = 0;
        Dialog dialog = new Dialog(StockRequestAddActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner_product);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        cvUnCheckAll = dialog.findViewById(R.id.cvUnCheckAll);
        cvCheckedAll = dialog.findViewById(R.id.cvCheckedAll);
        loadingDataBottom = dialog.findViewById(R.id.loadingDataBottom);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        EditText editText = dialog.findViewById(R.id.edit_text);

        rv = dialog.findViewById(R.id.rv);
        linearLayoutManMaterial = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManMaterial);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        listSpinner = new ArrayList<>();
        listSpinner = initDataMaterial(searchMat);
        spinnerAdapter = new SpinnerProductStockRequestAdapter(StockRequestAddActivity.this, listSpinner, (nameItem, adapterPosition) -> {
        });
        rv.setAdapter(spinnerAdapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0) {
                    itemCount();
                } else {
                    if (dy > 0) //check for scroll down
                    {
                        itemCount();
                    } else {
                        loadingDataBottom.setVisibility(View.GONE);
                        loading = true;
                    }
                }
            }
        });

        btnSearch.setOnClickListener(v -> {
            if (!Helper.isEmptyEditText(editText)) {
                searchMat = editText.getText().toString().trim();
                offset = 0;
                listSpinner = initDataMaterial(searchMat);
                spinnerAdapter = new SpinnerProductStockRequestAdapter(StockRequestAddActivity.this, listSpinner, (nameItem, adapterPosition) -> {
                });
                rv.setAdapter(spinnerAdapter);
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

    public void itemCount() {
        visibleItemCount = linearLayoutManMaterial.getChildCount();
        totalItemCount = linearLayoutManMaterial.getItemCount();
        pastVisiblesItems = linearLayoutManMaterial.findFirstVisibleItemPosition();

        if (loading) {
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                loading = false;
                offset = totalItemCount;
                loadingDataBottom.setVisibility(View.VISIBLE);
                listSpinner.addAll(initDataMaterial(searchMat));
                spinnerAdapter.notifyDataSetChanged();
                loadingDataBottom.setVisibility(View.GONE);
            }
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = findViewById(R.id.llNoData);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgBack);
        txtDate = findViewById(R.id.txtDate);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private List<Material> initDataMaterial(String searchString) {
        List<Material> listSpinner = new ArrayList<>();
        List<Material> listMat = new ArrayList<>();
        listMat.addAll(database.getAllMasterMaterial(offset, searchString, user.getSales_category()));

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

    @Override
    public void onResume() {
        super.onResume();
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

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {
        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    prepareData();
                    String URL_ = Constants.API_STOCK_REQUEST_INSERT;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    Map request = new HashMap();
                    request.put("header", headerRequest);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, request);
                    return null;
                } else {
                    saveDataToDatabase();
                    saveDataSuccess = true;
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("stockRequestAdd", ex.getMessage());
                }
                if (PARAM == 2) {
                    saveDataSuccess = false;
                } else {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    logResult.setMessage("Add Stock Request error: " + ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage e) {
            if (PARAM == 1) {
                if (logResult.getIdMessage() == 1) {
                    String message = "Add Stock Request : " + logResult.getMessage();
                    logResult.setMessage(message);
                }
                database.addLog(logResult);
                if (logResult.getIdMessage() == 1) {
                    PARAM = 2;
                    new RequestUrl().execute();//2
                } else {
                    progress.dismiss();
                    setToast(logResult.getMessage());
                }
            } else {
                progress.dismiss();
                if (saveDataSuccess) {
                    setToast("Stock Request Success");
                    onBackPressed();
                } else {
                    setToast("Save to database failed");
                }
            }
        }
    }
}
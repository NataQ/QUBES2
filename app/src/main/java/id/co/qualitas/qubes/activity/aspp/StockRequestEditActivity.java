package id.co.qualitas.qubes.activity.aspp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import id.co.qualitas.qubes.adapter.aspp.SpinnerProductStockRequestEditAdapter;
import id.co.qualitas.qubes.adapter.aspp.StockRequestAddAdapter;
import id.co.qualitas.qubes.adapter.aspp.StockRequestEditAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.helper.RecyclerViewMaxHeight;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class StockRequestEditActivity extends BaseActivity {
    private StockRequestEditAdapter mAdapter;
    private List<Material> mList;
    private List<Material> listSpinner, listFilteredSpinner;
    private CardView cvUnCheckAll, cvCheckedAll;
    boolean checkedAll = false;
    private SpinnerProductStockRequestEditAdapter spinnerAdapter;
    private Button btnAdd, btnApprove;
    private StockRequest headerRequest;
    private boolean saveDataSuccess = false;
    private TextView txtDate;
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
        setContentView(R.layout.aspp_activity_stock_request_edit);

        initialize();

        btnAdd.setOnClickListener(y -> {
            addProduct();
        });

        btnApprove.setOnClickListener(y -> {
            openDialogConfirm();
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnSave.setOnClickListener(v -> {
            validateData();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(StockRequestEditActivity.this);
        });
    }

    private void validateData() {
        int param = 0, paramMat = 0;

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

    public void openDialogConfirm() {
        LayoutInflater inflater = LayoutInflater.from(StockRequestEditActivity.this);
        final Dialog dialog = new Dialog(StockRequestEditActivity.this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDialog = dialog.findViewById(R.id.txtDialog);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        txtTitle.setText("Approve");
        txtDialog.setText("Are you sure want to approve?");

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                progress.show();
                PARAM = 3;
                new RequestUrl().execute();//1
            }
        });

        dialog.show();
    }

    private void prepareData() {
        headerRequest.setMaterialList(mList);
    }

    private void saveDataToDatabase() {
        database.deleteRequestStockDetail(headerRequest.getIdHeader());

        for (Material param : mList) {
            database.addStockRequestDetail(param, headerRequest.getIdHeader(), user.getUsername());
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

    private void addProduct() {
        searchMat = null;
        offset = 0;
        Dialog dialog = new Dialog(StockRequestEditActivity.this);

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
        spinnerAdapter = new SpinnerProductStockRequestEditAdapter(StockRequestEditActivity.this, listSpinner, (nameItem, adapterPosition) -> {
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
                spinnerAdapter = new SpinnerProductStockRequestEditAdapter(StockRequestEditActivity.this, listSpinner, (nameItem, adapterPosition) -> {
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
        userRoleList = user.getRoleList();

        llNoData = findViewById(R.id.llNoData);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgBack);
        btnApprove = findViewById(R.id.btnApprove);
        btnAdd = findViewById(R.id.btnAdd);
        txtDate = findViewById(R.id.txtDate);
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
        headerRequest = SessionManagerQubes.getStockRequestHeader();
        if (headerRequest == null) {
            onBackPressed();
            setToast(getString(R.string.failedGetData));
        } else {
            String date = "";
            if (headerRequest.getReq_date() != null) {
                date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, headerRequest.getReq_date());
            }
            txtDate.setText(date);
            mList = new ArrayList<>();
            mList.addAll(database.getAllStockRequestDetail(headerRequest.getIdHeader()));

            mAdapter = new StockRequestEditAdapter(this, mList, header -> {

            });
            recyclerView.setAdapter(mAdapter);

            if (Helper.findRole(userRoleList, "MOBILE_REQUEST_STOCK_UPDATE_STATUS")) {
                btnApprove.setVisibility(View.VISIBLE);
            } else {
                btnApprove.setVisibility(View.VISIBLE);
            }

            if (Helper.findRole(userRoleList, "MOBILE_REQUEST_STOCK_UPDATE")) {
                btnSave.setVisibility(View.VISIBLE);
            } else {
                btnSave.setVisibility(View.VISIBLE);
            }
        }
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
                    String URL_ = Constants.API_STOCK_REQUEST_UPDATE;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    Map request = new HashMap();
                    request.put("header", headerRequest);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, request);
                    return null;
                } else if (PARAM == 2) {
                    saveDataToDatabase();
                    saveDataSuccess = true;
                    return null;
                } else {
                    prepareData();
                    String URL_ = Constants.API_STOCK_REQUEST_UPDATE_STATUS;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    Map request = new HashMap();
                    headerRequest.setStatus(Constants.STATUS_PENDING);
                    request.put("header", headerRequest);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, request);
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("stockRequestUpdate", ex.getMessage());
                }
                if (PARAM == 2) {
                    saveDataSuccess = false;
                } else {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    logResult.setMessage("Update Stock Request error: " + ex.getMessage());
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
                    String message = "Update Stock Request : " + logResult.getMessage();
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
            } else if (PARAM == 2) {
                progress.dismiss();
                if (saveDataSuccess) {
                    setToast("Update Stock Request Success");
                    onBackPressed();
                } else {
                    setToast("Save to database failed");
                }
            } else {
                progress.dismiss();
                if (logResult.getIdMessage() == 1) {
                    String message = "Update Stock Request : " + logResult.getMessage();
                    database.updateStatusRequestStock(headerRequest);
                    logResult.setMessage(message);
                    setToast(logResult.getMessage());
                    onBackPressed();
                } else {
                    setToast(logResult.getMessage());
                }
                database.addLog(logResult);
            }
        }
    }
}
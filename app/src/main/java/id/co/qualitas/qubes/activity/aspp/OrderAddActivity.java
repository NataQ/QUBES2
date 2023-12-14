package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.OrderAddAdapter;
import id.co.qualitas.qubes.adapter.aspp.SpinnerProductOrderAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.printer.ConnectorActivity;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class OrderAddActivity extends BaseActivity {
    private OrderAddAdapter mAdapter;
    private List<Material> mList;
    private Button btnAdd, btnNext, btnGetDiscount;
    private TextView txtDate, txtOmzet, txtTglKirim;
    private List<Material> listSpinner, listFilteredSpinner;
    private CardView cvUnCheckAll, cvCheckedAll;
    boolean checkedAll = false;
    private SpinnerProductOrderAdapter spinnerAdapter;
    private Customer outletHeader;
    private boolean saveDiscount = false, saveOrder = false, payNow = false;
    private WSMessage wsMessage;
    boolean getDiscount = false;
    private StockRequest stockHeader;
    String ketLK = "", ketDB;
    private boolean overLK = false, doubleBon = false;
    Calendar todayDate;
    Date fromDate;
    String fromDateString, paramFromDate;
    Order headerSave = new Order();
    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;
    private static final int REQUEST_LOCATION_PERMISSION = 5;

    ArrayList<String> permissionsList;
    String[] permissionsStr = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN
    };
    int permissionsCount = 0;

    ActivityResultLauncher<String[]> permissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            permissionsList = new ArrayList<>();
                            permissionsCount = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (shouldShowRequestPermissionRationale(permissionsStr[i])) {
                                    permissionsList.add(permissionsStr[i]);
                                } else if (!hasPermission(OrderAddActivity.this, permissionsStr[i])) {
                                    permissionsCount++;
                                }
                            }
                            if (permissionsList.size() > 0) {
                                //Some permissions are denied and can be asked again.
                                askForPermissions(permissionsList);
                            } else if (permissionsCount > 0) {
                                //Show alert dialog
                                showPermissionDialog();
                            } else {
                                dialogConfirm();
                            }
                        }
                    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order_add);

        initialize();
        permissionsList = new ArrayList<>();
        permissionsList.addAll(Arrays.asList(permissionsStr));

        btnGetDiscount.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                if (checkMaterial() == 0) {
                    getDiscount = true;
                    progress.show();
                    PARAM = 1;
                    new RequestUrl().execute();//1
                } else {
                    setToast("Pastikan data sudah benar dan tidak ada yang kosong");
                }

            } else {
                setToast("Anda tidak memiliki jaringan internet");
            }
        });

        btnAdd.setOnClickListener(v -> {
            addProduct();
        });

        btnNext.setOnClickListener(v -> {
            if (checkDiscount()) {
                if (checkOmzet()) {
                    if (!Helper.isEmpty(user.getType_sales())) {
                        overLK = checkOverLk();
                        doubleBon = checkDoubleBon();
                        if (overLK || doubleBon) {
                            if (user.getType_sales().equals("CO")) {
                                setToast(ketLK + ketDB);
                            } else {
                                dialogConfirm();
                            }
                        } else {
                            dialogConfirm();
//                            askForPermissions(permissionsList);
                        }
                    } else {
                        dialogConfirm();
                    }
                } else {
                    setToast("Pastikan data sudah benar dan tidak ada yang kosong");
                }
            } else {
                setToast("Silahkan dapatkan diskon. ");
            }
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(OrderAddActivity.this);
        });
    }

    private void setDate() {
        fromDate = Helper.getTodayDate();
        paramFromDate = new SimpleDateFormat(Constants.DATE_FORMAT_1).format(fromDate);
        txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_1));
        txtTglKirim.setOnClickListener(new View.OnClickListener() {
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
                fromDateString = new SimpleDateFormat(Constants.DATE_FORMAT_1).format(calendar.getTime());
                paramFromDate = new SimpleDateFormat(Constants.DATE_FORMAT_3).format(calendar.getTime());
                txtTglKirim.setText(fromDateString);
                txtTglKirim.setError(null);
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(OrderAddActivity.this, dateSetListener, year, month, date);
//        dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
        dialog.show();
    }

    private boolean checkDiscount() {
        boolean result = false;
        if (isNetworkAvailable()) {
            if (getDiscount) {
                result = true;
            } else {
                result = false;
            }
        } else {
            result = true;
        }
        return result;
    }

    private boolean checkOverLk() {
        boolean result = false;
        ketLK = "";
        if (!Helper.isEmpty(txtOmzet)) {
            String omzet = txtOmzet.getText().toString().replace("Rp.", "").replace(".", "");
            if (omzet.equals("0")) {
                result = false;
            } else {
                double total = Double.parseDouble(omzet);
                double LK = database.getLKCustomer(outletHeader);

                if (LK < total) {
                    result = true;
                    ketLK = ketLK + "Over LK\n";
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    private boolean checkOmzet() {
        int empty = 0;
        if (!Helper.isEmpty(txtOmzet)) {
            String omzet = txtOmzet.getText().toString().replace("Rp.", "").replace(".", "");
            if (omzet.equals("0")) {
                empty++;
            }
        } else {
            empty++;
        }
        if (txtTglKirim.getText().toString().equals("")) {
            empty++;
        }

        empty = empty + checkMaterial();

        return empty == 0;
    }

    private int checkMaterial() {
        int empty = 0;
        if (Helper.isNotEmptyOrNull(mList)) {
            for (Material material : mList) {
                if (material.getQty() == 0 &&  material.getUom().equals("-")) {
                    empty++;
                    break;
                }
                if (Helper.isNotEmptyOrNull(material.getExtraItem())) {
                    for (Material extra : material.getExtraItem()) {
                        if (extra.getQty() == 0 && material.getUom().equals("-")) {
                            empty++;
                            break;
                        }
                    }
                }
            }
        } else {
            empty++;
        }
        return empty;
    }

    private boolean checkDoubleBon() {
        ketDB = "";
        boolean isMaxBon = false;
        for (Material material : mList) {
            double limitBon = database.getLimitBon(material.getId(), outletHeader.getId());
            if (limitBon != 0) {
                isMaxBon = true;
                ketDB = ketDB + "Double Bon : " + material.getNama() + "\n";
                break;
            }
        }
        return isMaxBon;
    }

    private void saveOrderSession() {
        progress.show();
        PARAM = 3;
        new RequestUrl().execute();//3
    }

    private void setAdapter() {
        mAdapter = new OrderAddAdapter(this, mList, outletHeader, header -> {

        });

        recyclerView.setAdapter(mAdapter);
    }

    private void addNew(List<Material> addedList) {
        mList.addAll(addedList);
        removeOmzet();
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

    private void initData() {
        outletHeader = SessionManagerQubes.getOutletHeader();
        stockHeader = database.getAllStockMaterial();
        mList = new ArrayList<>();
        setDate();
        txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_1));
        setAdapter();
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtTglKirim = findViewById(R.id.txtTglKirim);
        txtOmzet = findViewById(R.id.txtOmzet);
        txtDate = findViewById(R.id.txtDate);
        btnGetDiscount = findViewById(R.id.btnGetDiscount);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnNext = findViewById(R.id.btnNext);
        imgBack = findViewById(R.id.imgBack);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void addProduct() {
        Dialog dialog = new Dialog(OrderAddActivity.this);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner_product);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        cvUnCheckAll = dialog.findViewById(R.id.cvUnCheckAll);
        cvCheckedAll = dialog.findViewById(R.id.cvCheckedAll);
        RelativeLayout checkbox = dialog.findViewById(R.id.checkbox);
        EditText editText = dialog.findViewById(R.id.edit_text);
        RecyclerView rv = dialog.findViewById(R.id.rv);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        if (Helper.isNotEmptyOrNull(mList)) {
            checkbox.setVisibility(View.VISIBLE);
        } else {
            checkbox.setVisibility(View.GONE);
        }

        listSpinner = new ArrayList<>();
        listSpinner.addAll(initDataMaterial());

        spinnerAdapter = new SpinnerProductOrderAdapter(OrderAddActivity.this, listSpinner, (nameItem, adapterPosition) -> {
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
            String idMatGroup = null;
            for (Material mat : listSpinner) {
                if (mat.isChecked()) {
                    boolean avaiable = false;
                    if (user.getType_sales().equals("CO")) {
//                        Material materialStock = database.getStockMaterial(new HashMap());
                        for (Material materialStock : stockHeader.getMaterialList()) {
                            if (mat.getId().equals(materialStock.getId())) {
                                if (materialStock.getQty() != 0) {
                                    avaiable = true;
                                }
                                break;
                            }
                        }
                    } else {
                        avaiable = true;
                    }
                    if (avaiable) {
//                        addList.add(mat);
//                        if (Helper.isNullOrEmpty(mat.getUomSisa())) {
//                            Map req = new HashMap();
//                            req.put("udf5", outletHeader.getUdf_5());
//                            req.put("productId", mat.getId_product_group());
//                            req.put("matId", mat.getId());
//                            Material matDetail = database.getPriceMaterial(req);
                        if (idMatGroup != null) {
                            if (idMatGroup.equals(mat.getId_material_group())) {
                                addList.add(mat);
                            } else {
                                setToast("Harus product yang grup nya sama");
                            }
                        } else {
                            idMatGroup = mat.getId_material_group();
                            addList.add(mat);
                        }
//                        } else {
//                            addList.add(mat);
//                        }
                    } else {
                        setToast("Tidak ada stock untuk material ini");
                    }
                }
            }
            if (Helper.isNotEmptyOrNull(addList)) addNew(addList);
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
        Map req = new HashMap();
        req.put("udf_5", outletHeader.getUdf_5());
        if (Helper.isNotEmptyOrNull(mList)) {
            req.put("price_list_code", mList.get(0).getPriceListCode());
            req.put("material_group_id", mList.get(0).getId_material_group());
        } else {
            req.put("price_list_code", null);
            req.put("material_group_id", null);
        }
        listMat.addAll(database.getAllMasterMaterialByCustomer(req));
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    public void calculateOmzet() {
        double omzet = 0;
        for (Material material : mList) {
            omzet = omzet + (material.getPrice() - material.getTotalDiscount());
        }

        txtOmzet.setText("Rp. " + format.format(omzet));
    }

    public void removeOmzet() {
        txtOmzet.setText("0");
        getDiscount = false;
    }

    public void resizeView() {
        recyclerView.swapAdapter(mAdapter, true);
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {
        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    Map request = new HashMap();
                    request.put("id_customer", outletHeader.getId());
                    request.put("tipe_outlet", outletHeader.getType_customer());
                    request.put("id", null);
                    request.put("order_date", Helper.getTodayDate(Constants.DATE_FORMAT_3));

                    List<Map> listBarang = new ArrayList<>();
                    for (Material material : mList) {
                        if (material.getQty() != 0) {
                            Map tempBarang = new HashMap<>();
                            tempBarang.put("id", material.getId());
                            tempBarang.put("harga", material.getPrice());
                            tempBarang.put("qty", material.getQty());
                            tempBarang.put("satuan", material.getUom());
                            listBarang.add(tempBarang);
                        }
                    }
                    request.put("listDetail", listBarang);
                    String URL_ = Constants.API_GET_DISCOUNT_ORDER;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, request);
                } else if (PARAM == 2) {
                    Map resultMap = (Map) wsMessage.getResult();
                    List<Map> barangList = new ArrayList<>();
                    barangList = (List<Map>) resultMap.get("barang");
                    for (Map barangMap : barangList) {
                        String kodeBarang = barangMap.get("kodeBarang").toString();

                        //diskon
                        Map<String, String> map = (Map<String, String>) barangMap.get("diskon");
                        double totalDisc = 0;
                        List<Discount> discList = new ArrayList<>();
                        for (Map.Entry<String, String> pair : map.entrySet()) {
                            Discount disc = new Discount();
                            disc.setKeydiskon(pair.getKey());
                            disc.setValuediskon(pair.getValue());
                            totalDisc = totalDisc + Double.parseDouble(pair.getValue());
                            discList.add(disc);
                        }
                        //diskon

                        Discount extra = Helper.ObjectToGSON(barangMap.get("extra"), Discount.class);

                        for (Material material : mList) {
                            if (material.getId().equals(kodeBarang)) {
//                                material.setDiscount(discount);
                                material.setExtraDiscount(extra);
                                material.setTotalDiscount(totalDisc);
                                material.setDiskonList(discList);
                            }
                        }
                    }
                    saveDiscount = true;
                    return null;
                } else {
                    headerSave = new Order();
                    headerSave.setId_customer(outletHeader.getId());
                    headerSave.setOrder_date(Helper.getTodayDate(Constants.DATE_FORMAT_3));
                    double omzet = 0;
                    try {
                        omzet = Double.parseDouble(txtOmzet.getText().toString().replace("Rp. ", "").replace(".", ""));
                    } catch (Exception e) {
                        omzet = 0;
                    }
                    String date = Helper.changeDateFormat(Constants.DATE_FORMAT_1, Constants.DATE_FORMAT_3, txtTglKirim.getText().toString().trim());
                    headerSave.setTanggal_kirim(date);
                    headerSave.setOmzet(omzet);
                    headerSave.setIdStockHeaderBE(stockHeader.getId());
                    headerSave.setIdStockHeaderDb(stockHeader.getId_mobile());
                    headerSave.setStatus(Constants.STATUS_DRAFT);
                    headerSave.setIsSync(0);
                    headerSave.setTop(Helper.isNotEmptyOrNull(mList) ? mList.get(0).getTop() : null);
                    headerSave.setMaterialList(mList);
                    headerSave.setDiscount(getDiscount);
                    headerSave.setType_customer(outletHeader.getType_customer());
                    headerSave.setStatusPaid(false);
                    headerSave.setOrder_type(user.getType_sales().equals("CO") ? Constants.ORDER_CANVAS_TYPE : Constants.ORDER_TAKING_TYPE);
                    headerSave.setIdHeader(Constants.ID_OP_MOBILE.concat(user.getUsername()).concat(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
                    saveOrder = database.addOrder(headerSave, user);
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("order", ex.getMessage());
                }
                if (PARAM == 2) {
                    saveDiscount = false;
                }
                if (PARAM == 3) {
                    saveOrder = false;
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage result) {
            if (PARAM == 1) {
                if (result != null) {
                    wsMessage = result;
                    PARAM = 2;
                    new RequestUrl().execute();//2
                } else {
                    progress.dismiss();
                    setToast("Failed get discount");
                    calculateOmzet();
                }
            } else if (PARAM == 2) {
                progress.dismiss();
                if (saveDiscount) {
                    mAdapter.notifyDataSetChanged();
                } else {
                    setToast("Failed save discount");
                }
                calculateOmzet();
            } else {
                progress.dismiss();
                if (saveOrder) {
                    setToast("Save order Success");
                    SessionManagerQubes.setOrder(headerSave);
                    if (payNow) {
                        SessionManagerQubes.setAlreadyPrint(false);
                        SessionManagerQubes.setCollectionSource(3);
                        Intent intent = new Intent(OrderAddActivity.this, CollectionFormActivity.class);
                        startActivity(intent);
                    } else {
                        if (user.getType_sales().equals("CO")) {
                            if (ContextCompat.checkSelfPermission(OrderAddActivity.this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(OrderAddActivity.this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH);
                            } else if (ContextCompat.checkSelfPermission(OrderAddActivity.this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(OrderAddActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN);
                            } else if (ContextCompat.checkSelfPermission(OrderAddActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    ActivityCompat.requestPermissions(OrderAddActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_BLUETOOTH_CONNECT);
                                }
                            } else if (ContextCompat.checkSelfPermission(OrderAddActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    ActivityCompat.requestPermissions(OrderAddActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_BLUETOOTH_SCAN);
                                }
                            } else if (ContextCompat.checkSelfPermission(OrderAddActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(OrderAddActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                            } else {
                                intent = new Intent(OrderAddActivity.this, ConnectorActivity.class);
                                startActivity(intent);
                            }
                        }else{
                            Intent intent = new Intent(OrderAddActivity.this, OrderActivity.class);
                            startActivity(intent);
                        }
                    }
                } else {
                    setToast("Failed save order");
                }
            }
        }
    }

    public void dialogKredit() {
        LayoutInflater inflater = LayoutInflater.from(OrderAddActivity.this);
        final Dialog dialog = new Dialog(OrderAddActivity.this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_bayar_kredit, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        Button btnKredit = dialog.findViewById(R.id.btnKredit);
        Button btnBayar = dialog.findViewById(R.id.btnBayar);
        btnKredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payNow = false;
                saveOrderSession();//tidak bayar
                dialog.dismiss();
            }
        });

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payNow = true;
                dialog.dismiss();
                saveOrderSession();//bayar sekarang
            }
        });

        dialog.show();
    }

    public void dialogConfirm() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog(this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDialog = dialog.findViewById(R.id.txtDialog);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        txtTitle.setText("Order");
        if (!Helper.isEmpty(user.getType_sales())) {
            if (user.getType_sales().equals("CO")) {
                txtDialog.setText("Anda yakin sudah selesai order?");
            } else {
                String text = "";
                if (overLK) text = text + "Order ini melebihi limit customer.";
                if (doubleBon)
                    text = text + "\nOrder ini memiliki double bon.";
                text = text + "\nAnda yakin ingin menyimpan order ini?";
                txtDialog.setText(text);
            }
        } else {
            String text = null;
            if (overLK) text = text + "Order ini melebihi limit customer.";
            if (doubleBon)
                text = text + "\nOrder ini memiliki double bon.";
            text = text + "\nAnda yakin ingin menyimpan order ini?";
            txtDialog.setText(text);
        }

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
                if (!Helper.isEmpty(user.getType_sales())) {
                    if (user.getType_sales().equals("CO")) {
                        dialogKredit();
                    } else {
                        saveOrderSession();//dialog confirm
                    }
                } else {
                    saveOrderSession();//dialog confirm
                }
            }
        });

        dialog.show();
    }

    private boolean hasPermission(Context context, String permissionStr) {
        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED;
    }

    private void askForPermissions(ArrayList<String> permissionsList) {
        String[] newPermissionStr = new String[permissionsList.size()];
        for (int i = 0; i < newPermissionStr.length; i++) {
            newPermissionStr[i] = permissionsList.get(i);
        }
        if (newPermissionStr.length > 0) {
            permissionsLauncher.launch(newPermissionStr);
        } else {
            /* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
            which will lead them to app details page to enable permissions from there. */
            showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission required")
                .setMessage("Some permissions are needed to be allowed to use this app without any problems.")
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }
}
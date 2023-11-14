package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class OrderAddActivity extends BaseActivity {
    private OrderAddAdapter mAdapter;
    private List<Material> mList;
    private Button btnAdd, btnNext, btnGetDiscount;
    private TextView txtDate, txtOmzet;
    private List<Material> listSpinner, listFilteredSpinner;
    private CardView cvUnCheckAll, cvCheckedAll;
    boolean checkedAll = false;
    private SpinnerProductOrderAdapter spinnerAdapter;
    private Customer outletHeader;
    private boolean saveDiscount = false, saveOrder = false, payNow = false;
    private WSMessage wsMessage;
    boolean getDiscount = false;
    private List<Material> fakturList;
    private StockRequest stockHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order_add);

        initialize();

        btnGetDiscount.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                getDiscount = true;
                progress.show();
                PARAM = 1;
                new RequestUrl().execute();//1
            } else {
                setToast("Anda tidak memiliki jaringan internet");
            }
        });

        btnAdd.setOnClickListener(v -> {
            addProduct();
        });

        btnNext.setOnClickListener(v -> {
            if (checkDiscount()) {
                dialogConfirm();
            } else {
                setToast("Silahkan ambil diskon lagi sebelum save order");
            }
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(OrderAddActivity.this);
        });
    }

    private boolean validateCO() {
        int validate = 0;
        //tim sr/gt/on premise:
        //-> 2 bon produk existing (KTD-R)
        //-> 2 bon produk existing (Vitamin n Water)
        //-> 1 bon produk RBG
        //-> 1 bon produk batterai

        //tim sr batterai
        //-> 2 bon produk batterai
//        if(mList){
//
//        }

        return validate == 0;
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
        fakturList = database.getOutstandingFaktur(outletHeader.getId());
        mList = new ArrayList<>();
        txtDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_1));
        setAdapter();
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        stockHeader = database.getLastStockRequest();

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
                    for (Material materialStock : stockHeader.getMaterialList()) {
                        if (mat.getId().equals(materialStock.getId())) {
                            if (materialStock.getQtySisa() != 0) {
                                avaiable = true;
                            }
                            break;
                        }
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
            addNew(addList);
            dialog.dismiss();
        });
    }

    public boolean checkStock(Material material) {
        boolean stockReady = false;
        for (Material mat : stockHeader.getMaterialList()) {
            if (mat.getId().equals(material.getId())) {
                Material stock = database.getQtySmallUom(mat);
                Material order = database.getQtySmallUom(material);
                if (stock.getQty() > order.getQty()) {
                    stockReady = true;
                } else {
                    stockReady = false;
                }
                break;
            }
        }
        return stockReady;
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
            omzet = omzet + material.getPrice() + material.getTotalDiscount();
        }

        txtOmzet.setText("Rp. " + format.format(omzet));
    }

    public void removeOmzet() {
        txtOmzet.setText("0");
    }

    public void resizeView() {
        recyclerView.swapAdapter(mAdapter,true);
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
                        Map tempBarang = new HashMap<>();
                        tempBarang.put("id", material.getId());
                        tempBarang.put("harga", material.getPrice());
                        tempBarang.put("qty", material.getQty());
                        tempBarang.put("satuan", material.getUom());
                        listBarang.add(tempBarang);
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
                    Order header = new Order();
                    header.setId_customer(outletHeader.getId());
                    header.setOrder_date(Helper.getTodayDate(Constants.DATE_FORMAT_3));
                    double omzet = 0;
                    try {
                        omzet = Double.parseDouble(txtOmzet.getText().toString().replace("Rp. ", "").replace(".", ""));
                    } catch (Exception e) {
                        omzet = 0;
                    }
                    header.setOmzet(omzet);
                    header.setStatus("Pending");
                    header.setIsSync(0);
                    header.setMaterialList(mList);

                    database.addOrder(header, user);
                    SessionManagerQubes.setOrder(header);

                    saveOrder = true;
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
                }
            } else if (PARAM == 2) {
                progress.dismiss();
                if (saveDiscount) {
                    mAdapter.notifyDataSetChanged();
                    calculateOmzet();
                } else {
                    setToast("Failed save discount");
                }
            } else {
                progress.dismiss();
                if (saveOrder) {
                    setToast("Save order Success");
                    if (payNow) {
                        SessionManagerQubes.setCollectionSource(3);
                        Intent intent = new Intent(OrderAddActivity.this, CollectionFormActivity.class);
                        startActivity(intent);
                    } else {
                        onBackPressed();
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
                saveOrderSession();//bayar sekarang
                dialog.dismiss();
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
        txtDialog.setText("Anda yakin sudah selesai order?");

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
                        if (validateCO()) {
                            dialogKredit();
                        }
                    } else {
                        saveOrderSession();
                    }
                } else {
                    saveOrderSession();
                }
            }
        });

        dialog.show();
    }
}
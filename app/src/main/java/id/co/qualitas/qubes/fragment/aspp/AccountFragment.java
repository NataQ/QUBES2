package id.co.qualitas.qubes.fragment.aspp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.BuildConfig;
import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.aspp.LogAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Bank;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.CustomerType;
import id.co.qualitas.qubes.model.DaerahTingkat;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.GroupMaxBon;
import id.co.qualitas.qubes.model.LogModel;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.Parameter;
import id.co.qualitas.qubes.model.Price;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.Uom;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class AccountFragment extends BaseFragment {
    private CardView llUploadDB, llSync, llLog, llChangePassword, llUpdateRegisterID, llLogOut;
    private LogAdapter mAdapter;
    private boolean saveDataSuccess = false, setDataSyncSuccess = false;
    private WSMessage messageResponse, logResult;
    private ProgressDialog progressDialog;
    private List<Customer> nooList = new ArrayList<>();
    private List<VisitSalesman> visitSalesmanList = new ArrayList<>();
    private List<Map> storeCheckList = new ArrayList<>(), returnList = new ArrayList<>();
    private List<CollectionHeader> collectionList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>(), orderEmptyDiscount = new ArrayList<>();
    private List<Map> photoList = new ArrayList<>();
    private int sizeData = 0;
    private File pathDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_account, container, false);
        initialize();
        initProgress();
        initFragment();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    setEnabled(false);
                    ((MainActivity) getActivity()).backPress();
                }
            }
        });

        llLogOut.setOnClickListener(v -> {
            logOut(getActivity());
        });

        llUpdateRegisterID.setOnClickListener(v -> {
            try {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }
                                String refreshedToken = task.getResult();
                                if (refreshedToken != null) {
                                    Helper.setItemParam(Constants.REGIISTERID, refreshedToken);
                                    user.setRegis_id(refreshedToken);
                                    SessionManagerQubes.setUserProfile(user);

                                    progress.show();
                                    PARAM = 30;
                                    new RequestUrl().execute();
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        llUploadDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    pathDB = Helper.exportDB(getActivity(), user.getUsername());
                    progress.show();
                    PARAM = 29;
                    new RequestUrl().execute();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage() != null ? e.getMessage() : "Failed export DB", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogChangePassword();
            }
        });

        llSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View dialogview;
                final Dialog alertDialog = new Dialog(getActivity());
                dialogview = inflater.inflate(R.layout.custom_dialog_sync_menu, null);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(dialogview);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
                alertDialog.setCanceledOnTouchOutside(false);

                LinearLayout linearReason = alertDialog.findViewById(R.id.linearReason);
                LinearLayout linearBank = alertDialog.findViewById(R.id.linearBank);
                LinearLayout linearDaerahTingkat = alertDialog.findViewById(R.id.linearDaerahTingkat);
                LinearLayout linearParameter = alertDialog.findViewById(R.id.linearParameter);
                LinearLayout linearMaterialPrice = alertDialog.findViewById(R.id.linearMaterialPrice);
                LinearLayout linearCustomer = alertDialog.findViewById(R.id.linearCustomer);
                LinearLayout linearCollection = alertDialog.findViewById(R.id.linearCollection);
                LinearLayout linearNOO = alertDialog.findViewById(R.id.linearNOO);
                LinearLayout linearVisit = alertDialog.findViewById(R.id.linearVisit);
                LinearLayout linearStoreCheck = alertDialog.findViewById(R.id.linearStoreCheck);
                LinearLayout linearOrder = alertDialog.findViewById(R.id.linearOrder);
                LinearLayout linearPhoto = alertDialog.findViewById(R.id.linearPhoto);
                LinearLayout linearReturn = alertDialog.findViewById(R.id.linearReturn);
                LinearLayout linearDiscount = alertDialog.findViewById(R.id.linearDiscount);
                Button btnCancel = alertDialog.findViewById(R.id.btnCancel);

                linearReason.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 1;
                    new RequestUrl().execute();//1
                });

                linearBank.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 3;
                    new RequestUrl().execute();//3
                });

                linearDaerahTingkat.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 5;
                    new RequestUrl().execute();//5
                });

                linearMaterialPrice.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 7;
                    new RequestUrl().execute();//7
                });

                linearParameter.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 9;
                    new RequestUrl().execute();//9
                });

                linearCustomer.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 11;
                    new RequestUrl().execute();//11
                });

                //transaksi
                linearNOO.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 13;
                    new RequestUrl().execute();//13
                });

                linearVisit.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 15;
                    new RequestUrl().execute();//15
                });

                linearStoreCheck.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 17;
                    new RequestUrl().execute();//17
                });

                linearCollection.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 19;
                    new RequestUrl().execute();//19
                });

                linearOrder.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 21;
                    new RequestUrl().execute();//21
                });

                linearReturn.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 23;
                    new RequestUrl().execute();//23
                });

                linearPhoto.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 25;
                    new RequestUrl().execute();//25
                });

                linearDiscount.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 27;
                    new RequestUrl().execute();//25
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        llLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View dialogview;
                final Dialog alertDialog = new Dialog(getActivity());
                dialogview = inflater.inflate(R.layout.custom_dialog_log, null);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(dialogview);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                RecyclerView recyclerView = alertDialog.findViewById(R.id.recyclerView);
                TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);
                Button btnCancel = alertDialog.findViewById(R.id.btnCancel);

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setHasFixedSize(true);

                List<LogModel> mList = new ArrayList<>();
                mList.addAll(database.getAllLog());

                mAdapter = new LogAdapter(AccountFragment.this, mList, header -> {
                });
                recyclerView.setAdapter(mAdapter);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        return rootView;
    }

    private void openDialogChangePassword() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogview;
        final Dialog alertDialog = new Dialog(getActivity());
        dialogview = inflater.inflate(R.layout.aspp_dialog_change_password, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(dialogview);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        alertDialog.setCanceledOnTouchOutside(false);


        Button btnCancel = alertDialog.findViewById(R.id.btnCancel);
        Button btnSave = alertDialog.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void initialize() {
        database = new Database(getContext());
        llLogOut = rootView.findViewById(R.id.llLogOut);
        llUpdateRegisterID = rootView.findViewById(R.id.llUpdateRegisterID);
        llUploadDB = rootView.findViewById(R.id.llUploadDB);
        llSync = rootView.findViewById(R.id.llSync);
        llLog = rootView.findViewById(R.id.llLog);
        llChangePassword = rootView.findViewById(R.id.llChangePassword);
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "5");
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_ = Constants.API_GET_MASTER_REASON;
                    User param = new User();
                    param.setUserLogin(user.getUserLogin());
                    param.setUsername(user.getUsername());
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else if (PARAM == 2) {
                    List<Reason> reasonList = new ArrayList<>();
                    Reason[] paramArray = Helper.ObjectToGSON(logResult.getResult(), Reason[].class);
                    if (paramArray != null) {
                        Collections.addAll(reasonList, paramArray);
                        database.deleteMasterReason();
                    }
                    for (Reason reason : reasonList) {
                        database.addMasterReason(reason, user.getUsername());
                    }
                    saveDataSuccess = true;
                    return null;
                } else if (PARAM == 3) {
                    String URL_ = Constants.API_GET_MASTER_BANK;
                    User param = new User();
                    param.setUserLogin(user.getUserLogin());
                    param.setUsername(user.getUsername());
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else if (PARAM == 4) {
                    List<Bank> bankList = new ArrayList<>();
                    Bank[] paramArray1 = Helper.ObjectToGSON(logResult.getResult(), Bank[].class);
                    if (paramArray1 != null) {
                        Collections.addAll(bankList, paramArray1);
                        database.deleteMasterBank();
                    }
                    for (Bank param : bankList) {
                        database.addMasterBank(param, user.getUsername());
                    }
                    saveDataSuccess = true;
                    return null;
                } else if (PARAM == 5) {
                    String URL_ = Constants.API_GET_MASTER_DAERAH_TINGKAT;
                    User param = new User();
                    param.setUserLogin(user.getUserLogin());
                    param.setUsername(user.getUsername());
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else if (PARAM == 6) {
                    List<DaerahTingkat> daerahTingkatList = new ArrayList<>();
                    DaerahTingkat[] paramArray3 = Helper.ObjectToGSON(logResult.getResult(), DaerahTingkat[].class);
                    if (paramArray3 != null) {
                        Collections.addAll(daerahTingkatList, paramArray3);
                        database.deleteMasterDaerahTingkat();
                    }
                    for (DaerahTingkat param : daerahTingkatList) {
                        database.addMasterDaerahTingkat(param, user.getUsername());
                    }
                    saveDataSuccess = true;
                    return null;
                } else if (PARAM == 7) {
                    String URL_ = Constants.API_GET_MASTER_MATERIAL_PRICE;
                    User param = new User();
                    param.setUserLogin(user.getUserLogin());
                    param.setUsername(user.getUsername());
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else if (PARAM == 8) {
                    Map response = (Map) logResult.getResult();
                    List<Material> materialList = new ArrayList<>();
                    Material[] paramArray4 = Helper.ObjectToGSON(response.get("listMaterial"), Material[].class);
                    if (paramArray4 != null) {
                        Collections.addAll(materialList, paramArray4);
                        database.deleteMasterMaterial();
                    }
                    for (Material param : materialList) {
                        database.addMasterMaterial(param, user.getUsername());
                    }

                    List<Uom> uomList = new ArrayList<>();
                    Uom[] paramArray5 = Helper.ObjectToGSON(response.get("listUom"), Uom[].class);
                    if (paramArray5 != null) {
                        Collections.addAll(uomList, paramArray5);
                        database.deleteMasterUom();
                    }
                    for (Uom param : uomList) {
                        database.addMasterUom(param, user.getUsername());
                    }

                    List<GroupMaxBon> listGroupSalesMaxBon = new ArrayList<>();
                    GroupMaxBon[] paramArray6 = Helper.ObjectToGSON(response.get("listGroupSalesMaxBon"), GroupMaxBon[].class);
                    if (paramArray6 != null) {
                        Collections.addAll(listGroupSalesMaxBon, paramArray6);
                        database.deleteMasterGroupSalesMaxBon();
                    }
                    for (GroupMaxBon param : listGroupSalesMaxBon) {
                        database.addMasterGroupSalesMaxBon(param, user.getUsername());
                    }

                    List<Price> listPrice = new ArrayList<>();
                    Price[] paramArray7 = Helper.ObjectToGSON(response.get("listPrice"), Price[].class);
                    if (paramArray7 != null) {
                        Collections.addAll(listPrice, paramArray7);
                        database.deleteMasterPrice();
                    }
                    for (Price param : listPrice) {
                        database.addMasterPrice(param, user.getUsername());
                    }

//                    List<PriceCode> priceList = new ArrayList<>();
//                    PriceCode[] paramArray6 = Helper.ObjectToGSON(response.get("listPriceCode"), PriceCode[].class);
//                    if (paramArray6 != null) {
//                        Collections.addAll(priceList, paramArray6);
//                        database.deleteMasterPriceCode();
//                    }
//                    for (PriceCode param : priceList) {
//                        database.addMasterPriceCode(param, user.getUsername());
//                    }
//
//                    List<SalesPriceHeader> salesPriceHeaderList = new ArrayList<>();
//                    SalesPriceHeader[] paramArray7 = Helper.ObjectToGSON(response.get("listSalesPriceHeader"), SalesPriceHeader[].class);
//                    if (paramArray7 != null) {
//                        Collections.addAll(salesPriceHeaderList, paramArray7);
//                        database.deleteMasterSalesPriceHeader();
//                    }
//                    for (SalesPriceHeader param : salesPriceHeaderList) {
//                        database.addMasterSalesPriceHeader(param, user.getUsername());
//                    }
//
//                    List<SalesPriceDetail> salesPriceDetailList = new ArrayList<>();
//                    SalesPriceDetail[] paramArray8 = Helper.ObjectToGSON(response.get("listSalesPriceDetail"), SalesPriceDetail[].class);
//                    if (paramArray8 != null) {
//                        Collections.addAll(salesPriceDetailList, paramArray8);
//                        database.deleteMasterSalesPriceDetail();
//                    }
//                    for (SalesPriceDetail param : salesPriceDetailList) {
//                        database.addMasterSalesPriceDetail(param, user.getUsername());
//                    }
//
//                    List<Material> minOrderList = new ArrayList<>();
//                    Material[] paramArray11 = Helper.ObjectToGSON(response.get("listMinimalOrder"), Material[].class);
//                    Collections.addAll(minOrderList, paramArray11);
//                    database.deleteMasterMinimalOrder();
//                    for (Material param : minOrderList) {
//                        database.addLimitBon(param, user.getUserLogin());
//                    }
//
//                    List<GroupMaxBon> groupMaxBonList = new ArrayList<>();
//                    GroupMaxBon[] paramArray12 = Helper.ObjectToGSON(response.get("listMaxBonLimit"), GroupMaxBon[].class);
//                    Collections.addAll(groupMaxBonList, paramArray12);
//                    database.deleteMasterMaxBonLimits();
//                    for (GroupMaxBon param : groupMaxBonList) {
//                        database.addMasterMaxBonLimits(param, user.getUserLogin());
//                    }

                    saveDataSuccess = true;
                    return null;
                } else if (PARAM == 9) {
                    String URL_ = Constants.API_GET_MASTER_PARAMETER;
                    User param = new User();
                    param.setUserLogin(user.getUserLogin());
                    param.setUsername(user.getUsername());
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else if (PARAM == 10) {
                    Map response = (Map) logResult.getResult();
                    List<Parameter> parameterList = new ArrayList<>();
                    Parameter[] paramArray9 = Helper.ObjectToGSON(response.get("parameter"), Parameter[].class);
                    if (paramArray9 != null) {
                        Collections.addAll(parameterList, paramArray9);
                        database.deleteMasterParameter();
                    }
                    for (Parameter param : parameterList) {
                        database.addMasterParameter(param, user.getUsername());
                    }

                    if (response.get("max_visit") != null) {
                        user.setMax_visit(Integer.parseInt(response.get("max_visit").toString()));
                    }

                    SessionManagerQubes.setUserProfile(user);
                    saveDataSuccess = true;
                    return null;
                } else if (PARAM == 11) {
                    String URL_ = Constants.API_GET_MASTER_CUSTOMER;
                    User param = new User();
                    param.setUserLogin(user.getUserLogin());
                    param.setUsername(user.getUsername());
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else if (PARAM == 12) {
                    Map response = (Map) logResult.getResult();
                    List<CustomerType> cusTypeList = new ArrayList<>();
                    CustomerType[] paramArray10 = Helper.ObjectToGSON(response.get("listCustomerType"), CustomerType[].class);
                    if (paramArray10 != null) {
                        Collections.addAll(cusTypeList, paramArray10);
                        database.deleteMasterCustomerType();
                    }
                    for (CustomerType param : cusTypeList) {
                        database.addMasterCustomerType(param, user.getUsername());
                    }

//                    if (response.get("visit") != null) {
//                        StartVisit startDay = Helper.ObjectToGSON(response.get("visit"), StartVisit.class);
//                        database.addStartVisit(startDay);
//                        SessionManagerQubes.setStartDay(startDay);
////                        LinkedTreeMap startDay = (LinkedTreeMap) response.get("visit");
////                        double statusVisit = startDay.get("statusVisit") != null ? (double) startDay.get("statusVisit") : 0;
////                        double idVisit = startDay.get("idVisit") != null ? (double) startDay.get("idVisit") :0;
////                        SessionManagerQubes.setStartDay((int) statusVisit);
////                        SessionManagerQubes.setIdVisit((int) idVisit);
//                    } else {
//                        SessionManagerQubes.setStartDay(null);
////                        SessionManagerQubes.setIdVisit(0);
//                    }

                    Customer[] paramArray = Helper.ObjectToGSON(response.get("listCustomerSalesman"), Customer[].class);
                    List<Customer> mList = new ArrayList<>();
                    if (paramArray != null) {
                        Collections.addAll(mList, paramArray);
                        database.deleteMasterCustomerSalesman();
//                        database.deleteMasterNonRouteCustomerPromotion();
//                        database.deleteMasterNonRouteCustomerDct();
                    }

                    for (Customer param : mList) {
//                        List<Promotion> arrayList = new ArrayList<>();
//                        Promotion[] matArray = Helper.ObjectToGSON(param.getPromoList(), Promotion[].class);
//                        if (matArray != null) {
//                            Collections.addAll(arrayList, matArray);
//                        }
//                        param.setPromoList(arrayList);
//
//                        List<Material> arrayDctList = new ArrayList<>();
//                        Material[] dctArray = Helper.ObjectToGSON(param.getDctList(), Material[].class);
//                        if (dctArray != null) {
//                            Collections.addAll(arrayDctList, dctArray);
//                        }
//                        param.setDctList(arrayDctList);
                        int idHeader = database.addCustomerSalesman(param, user.getUsername());
//                        for (Promotion mat : arrayList) {
//                            database.addNonRouteCustomerPromotion(mat, String.valueOf(idHeader), user.getUsername());
//                        }
//
//                        for (Material mat : arrayDctList) {
//                            database.addNonRouteCustomerDct(mat, String.valueOf(idHeader), user.getUsername(), param.getId());
//                        }
                    }

                    saveDataSuccess = true;
                    return null;
                } else if (PARAM == 13) {
                    nooList = new ArrayList<>();
                    nooList = database.getAllNoo();
                    if (nooList == null) {
                        setDataSyncSuccess = false;
                        logResult = new WSMessage();
                        logResult.setIdMessage(0);
                        logResult.setResult(null);
                        String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline data noo failed: " + exMess);
                    } else {
                        setDataSyncSuccess = true;
                    }
                    return null;
                } else if (PARAM == 15) {
                    visitSalesmanList = new ArrayList<>();
                    visitSalesmanList = database.getAllVisitSalesman();
                    if (visitSalesmanList == null) {
                        setDataSyncSuccess = false;
                        logResult = new WSMessage();
                        logResult.setIdMessage(0);
                        logResult.setResult(null);
                        String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline data visit failed: " + exMess);
                    } else {
                        setDataSyncSuccess = true;
                    }
                    return null;
                } else if (PARAM == 17) {
                    List<Customer> customerList = database.getAllCustomerCheckOut();
                    if (customerList != null) {
                        if (!customerList.isEmpty()) {
                            storeCheckList = new ArrayList<>();
//                            List<Material> mList = new ArrayList<>();
//                            Map header = new HashMap();
                            for (Customer customer : customerList) {
//                                mList = new ArrayList<>();
                                storeCheckList.addAll(database.getAllStoreCheckDate(customer.getId(), user.getUsername()));
                                if (storeCheckList != null) {
                                    setDataSyncSuccess = true;
                                } else {
                                    setDataSyncSuccess = false;
                                }
//                                mList = database.getAllStoreCheckCheckOut(customer.getId());
//                                if (mList != null) {
//                                    if (!mList.isEmpty()) {
//                                        header = new HashMap();
//                                        header.put("id_mobile", mList.get(0).getIdheader());
//                                        header.put("date", mList.get(0).getDate());
//                                        header.put("id_salesman", user.getUsername());
//                                        header.put("id_customer", mList.get(0).getId_customer());
//                                        header.put("listData", mList);
//                                        storeCheckList.add(header);
//                                        setDataSyncSuccess = true;
//                                    } else {
//                                        setDataSyncSuccess = true;
//                                    }
//                                } else {
//                                    setDataSyncSuccess = false;
//                                }
                            }

                            if (!setDataSyncSuccess) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(0);
                                logResult.setResult(null);
                                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                                logResult.setMessage("Set offline store check failed: " + exMess);
                            }
                        } else {
                            setDataSyncSuccess = true;
                            returnList = new ArrayList<>();
                        }
                    } else {
                        setDataSyncSuccess = false;
                        logResult = new WSMessage();
                        logResult.setIdMessage(0);
                        logResult.setResult(null);
                        String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline store check failed: " + exMess);
                    }
                    return null;
                } else if (PARAM == 19) {
                    List<Customer> customerList = database.getAllCustomerCheckOut();
                    if (customerList != null) {
                        if (!customerList.isEmpty()) {
                            collectionList = new ArrayList<>();
                            List<CollectionHeader> mList = new ArrayList<>();
                            for (Customer customer : customerList) {
                                mList = new ArrayList<>();
                                mList = database.getAllCollectionHeader(customer.getId());
                                if (mList != null) {
                                    if (!mList.isEmpty()) {
                                        collectionList.addAll(mList);
                                        setDataSyncSuccess = true;
                                    } else {
                                        setDataSyncSuccess = true;
                                    }
                                } else {
                                    setDataSyncSuccess = false;
                                }
                            }
                            if (!setDataSyncSuccess) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(0);
                                logResult.setResult(null);
                                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                                logResult.setMessage("Set offline collection failed: " + exMess);
                            }
                        } else {
                            setDataSyncSuccess = true;
                            collectionList = new ArrayList<>();
                        }
                    } else {
                        setDataSyncSuccess = false;
                        logResult = new WSMessage();
                        logResult.setIdMessage(0);
                        logResult.setResult(null);
                        String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline collection failed: " + exMess);
                    }
                    return null;
                } else if (PARAM == 21) {
                    List<Customer> customerList = database.getAllCustomerCheckOut();
                    if (customerList != null) {
                        if (!customerList.isEmpty()) {
                            orderList = new ArrayList<>();
                            List<Order> mList = new ArrayList<>();
                            for (Customer customer : customerList) {
                                mList = new ArrayList<>();
                                mList = database.getAllOrderHeader(customer.getId(), user.getUsername());
                                if (mList != null) {
                                    if (!mList.isEmpty()) {
                                        orderList.addAll(mList);
                                        setDataSyncSuccess = true;
                                    } else {
                                        setDataSyncSuccess = true;
                                    }
                                } else {
                                    setDataSyncSuccess = false;
                                }
                            }

                            if (!setDataSyncSuccess) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(0);
                                logResult.setResult(null);
                                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                                logResult.setMessage("Set offline order failed: " + exMess);
                            }
                        } else {
                            setDataSyncSuccess = true;
                            orderList = new ArrayList<>();
                        }
                    } else {
                        setDataSyncSuccess = false;
                        logResult = new WSMessage();
                        logResult.setIdMessage(0);
                        logResult.setResult(null);
                        String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline order failed: " + exMess);
                    }
                    return null;
                } else if (PARAM == 23) {
                    List<Customer> customerList = database.getAllCustomerCheckOut();
                    if (customerList != null) {
                        if (!customerList.isEmpty()) {
                            returnList = new ArrayList<>();
//                            List<Material> mList = new ArrayList<>();
//                            Map header = new HashMap();
                            for (Customer customer : customerList) {
                                returnList.addAll(database.getAllReturnDate(customer.getId(), user.getUsername()));
                                if (returnList != null) {
                                    setDataSyncSuccess = true;
                                } else {
                                    setDataSyncSuccess = false;
                                }
//                                mList = new ArrayList<>();
//                                mList = database.getAllReturnCheckOut(customer.getId());
//                                if (mList != null) {
//                                    if (!mList.isEmpty()) {
//                                        header = new HashMap();
//                                        header.put("id_mobile", mList.get(0).getIdheader());
//                                        header.put("date", mList.get(0).getDate());
//                                        header.put("id_salesman", user.getUsername());
//                                        header.put("id_customer", mList.get(0).getId_customer());
//                                        header.put("listData", mList);
//                                        returnList.add(header);
//                                        setDataSyncSuccess = true;
//                                    } else {
//                                        setDataSyncSuccess = true;
//                                    }
//                                } else {
//                                    setDataSyncSuccess = false;
//                                }
                            }
                            if (!setDataSyncSuccess) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(0);
                                logResult.setResult(null);
                                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                                logResult.setMessage("Set offline return failed: " + exMess);
                            }
                        } else {
                            setDataSyncSuccess = true;
                            returnList = new ArrayList<>();
                        }
                    } else {
                        setDataSyncSuccess = false;
                        logResult = new WSMessage();
                        logResult.setIdMessage(0);
                        logResult.setResult(null);
                        String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline return failed: " + exMess);
                    }
                    return null;
                } else if (PARAM == 25) {
                    List<Customer> customerList = database.getAllCustomerCheckOut();
                    if (customerList != null) {
                        if (!customerList.isEmpty()) {
                            photoList = new ArrayList<>();
                            List<Map> mList = new ArrayList<>();
                            for (Customer customer : customerList) {
                                mList = new ArrayList<>();
                                mList = database.getAllPhoto(customer.getId());
                                if (mList != null) {
                                    if (!mList.isEmpty()) {
                                        photoList.addAll(mList);
                                        setDataSyncSuccess = true;
                                    } else {
                                        setDataSyncSuccess = true;
                                    }
                                } else {
                                    setDataSyncSuccess = false;
                                }
                            }
                            if (!setDataSyncSuccess) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(0);
                                logResult.setResult(null);
                                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                                logResult.setMessage("Set offline data photo failed: " + exMess);
                            }
                        } else {
                            setDataSyncSuccess = true;
                            photoList = new ArrayList<>();
                        }
                    } else {
                        setDataSyncSuccess = false;
                        logResult = new WSMessage();
                        logResult.setIdMessage(0);
                        logResult.setResult(null);
                        String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline data photo failed: " + exMess);
                    }
                    return null;
                } else if (PARAM == 29) {
                    Map req = new HashMap();
                    req.put("username", user.getUsername());
                    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                    if (pathDB != null) {
                        map.add("database", new FileSystemResource(pathDB.getPath()));
                    } else {
                        map.add("database", "");
                    }
                    String json = new Gson().toJson(req);
                    map.add("data", json);

                    String URL_ = Constants.API_SEND_DATABASE_LOCAL;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBodyMultiPart(url, WSMessage.class, map);
                    return null;
                } else {
                    String URL_ = Constants.API_UPDATE_REGISTER_ID;
                    User param = new User();
                    param.setUserLogin(user.getUserLogin());
                    param.setUsername(user.getUsername());
                    param.setRegis_id(user.getRegis_id());
                    param.setApk_version(BuildConfig.VERSION_NAME);
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, param);
                    return null;
                }
            } catch (
                    Exception ex) {
                //connection = true;
                if (ex.getMessage() != null) {
                    Log.e("syncMaster", ex.getMessage());
                }
                if (PARAM == 2 || PARAM == 4 || PARAM == 6 || PARAM == 8 || PARAM == 10 || PARAM == 12) {
                    saveDataSuccess = false;
                } else {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                    switch (PARAM) {
                        case 1:
                            logResult.setMessage("Get data reason failed: " + exMess);
                            break;
                        case 3:
                            logResult.setMessage("Get data bank failed: " + exMess);
                            break;
                        case 5:
                            logResult.setMessage("Get data daerah tingkat failed: " + exMess);
                            break;
                        case 7:
                            logResult.setMessage("Get data material failed: " + exMess);
                            break;
                        case 9:
                            logResult.setMessage("Get data parameter failed: " + exMess);
                            break;
                        case 11:
                            logResult.setMessage("Get data customer failed: " + exMess);
                            break;
                        case 13:
                            logResult.setMessage("Set offline data noo failed: " + exMess);
                            break;
                        case 15:
                            logResult.setMessage("Set offline data visit failed: " + exMess);
                            break;
                        case 17:
                            logResult.setMessage("Set offline store check failed: " + exMess);
                            break;
                        case 19:
                            logResult.setMessage("Set offline collection failed: " + exMess);
                            break;
                        case 21:
                            logResult.setMessage("Set offline order failed: " + exMess);
                            break;
                        case 23:
                            logResult.setMessage("Set offline return failed: " + exMess);
                            break;
                        case 25:
                            logResult.setMessage("Set offline data photo failed: " + exMess);
                            break;
                        case 26:
                            logResult.setMessage("Set order failed: " + exMess);
                            break;
                        case 29:
                            logResult.setMessage("Send database offline failed: " + exMess);
                            break;
                        case 30:
                            logResult.setMessage("Update register id failed: " + exMess);
                            break;
                    }
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
        protected void onPostExecute(WSMessage r) {
            if (PARAM == 1) {
                if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                    String message = "Sync data reason sukses";
                    logResult.setMessage(message);
                    database.addLog(logResult);

                    PARAM = 2;
                    new RequestUrl().execute();
                } else {
                    progress.dismiss();
                    setToast("Gagal mengambil data reason");
                }
            } else if (PARAM == 2) {
                progress.dismiss();
                if (saveDataSuccess) {
                    setToast("Sync data reason sukses");
                } else {
                    setToast("Gagal menyimpan data reason");
                }
            } else if (PARAM == 3) {
                if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                    String message = "Sync data bank sukses";
                    logResult.setMessage(message);
                    database.addLog(logResult);

                    PARAM = 4;
                    new RequestUrl().execute();
                } else {
                    progress.dismiss();
                    setToast("Gagal mengambil data bank");
                }
            } else if (PARAM == 4) {
                progress.dismiss();
                if (saveDataSuccess) {
                    setToast("Sync data bank sukses");
                } else {
                    setToast("Gagal menyimpan data bank");
                }
            } else if (PARAM == 5) {
                if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                    String message = "Sync data daerah tingkat sukses";
                    logResult.setMessage(message);
                    database.addLog(logResult);

                    PARAM = 6;
                    new RequestUrl().execute();
                } else {
                    progress.dismiss();
                    setToast("Gagal mengambil data daerah tingkat");
                }
            } else if (PARAM == 6) {
                progress.dismiss();
                if (saveDataSuccess) {
                    setToast("Sync data daerah tingkat sukses");
                } else {
                    setToast("Gagal menyimpan data daerah tingkat");
                }
            } else if (PARAM == 7) {
                if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                    String message = "Sync data material sukses";
                    logResult.setMessage(message);
                    database.addLog(logResult);

                    PARAM = 8;
                    new RequestUrl().execute();
                } else {
                    progress.dismiss();
                    setToast("Gagal mengambil data material");
                }
            } else if (PARAM == 8) {
                progress.dismiss();
                if (saveDataSuccess) {
                    setToast("Sync data material sukses");
                } else {
                    setToast("Gagal menyimpan data material");
                }
            } else if (PARAM == 9) {
                if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                    String message = "Sync data parameter sukses";
                    logResult.setMessage(message);
                    database.addLog(logResult);

                    PARAM = 10;
                    new RequestUrl().execute();
                } else {
                    progress.dismiss();
                    setToast("Gagal mengambil data parameter");
                }
            } else if (PARAM == 10) {
                progress.dismiss();
                if (saveDataSuccess) {
                    setToast("Sync data parameter sukses");
                } else {
                    setToast("Gagal menyimpan data parameter");
                }
            } else if (PARAM == 11) {
                if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                    String message = "Sync data customer sukses";
                    logResult.setMessage(message);
                    database.addLog(logResult);

                    PARAM = 12;
                    new RequestUrl().execute();
                } else {
                    progress.dismiss();
                    setToast("Gagal mengambil data customer");
                }
            } else if (PARAM == 12) {
                progress.dismiss();
                if (saveDataSuccess) {
                    setToast("Sync data customer sukses");
                } else {
                    setToast("Gagal menyimpan data customer");
                }
            } else if (PARAM == 13) {
                progress.dismiss();
                if (setDataSyncSuccess) {
                    if (Helper.isNotEmptyOrNull(nooList)) {
                        sizeData = nooList.size();
                        PARAM = 14;
                        new RequestUrlTransaction().execute();//14
                    } else {
                        setToast("Tidak ada data atau Semua data sudah di sync");
                    }
                } else {
                    database.addLog(logResult);
                    setToast("Gagal menyiapkan data noo");
                }
            } else if (PARAM == 15) {
                progress.dismiss();
                if (setDataSyncSuccess) {
                    if (Helper.isNotEmptyOrNull(visitSalesmanList)) {
                        sizeData = visitSalesmanList.size();
                        PARAM = 16;
                        new RequestUrlTransaction().execute();//16
                    } else {
                        setToast("Tidak ada data atau Semua data sudah di sync");
                    }
                } else {
                    database.addLog(logResult);
                    setToast("Gagal menyiapkan data visit");
                }
            } else if (PARAM == 17) {
                progress.dismiss();
                if (setDataSyncSuccess) {
                    if (Helper.isNotEmptyOrNull(storeCheckList)) {
                        sizeData = storeCheckList.size();
                        PARAM = 18;
                        new RequestUrlTransaction().execute();//18
                    } else {
                        setToast("Tidak ada data atau Semua data sudah di sync");
                    }
                } else {
                    database.addLog(logResult);
                    setToast("Gagal menyiapkan data store check");
                }
            } else if (PARAM == 19) {
                progress.dismiss();
                if (setDataSyncSuccess) {
                    if (Helper.isNotEmptyOrNull(collectionList)) {
                        sizeData = collectionList.size();
                        PARAM = 20;
                        new RequestUrlTransaction().execute();//20
                    } else {
                        setToast("Tidak ada data atau Semua data sudah di sync");
                    }
                } else {
                    database.addLog(logResult);
                    setToast("Gagal menyiapkan data collection");
                }
            } else if (PARAM == 21) {
                progress.dismiss();
                if (setDataSyncSuccess) {
                    if (Helper.isNotEmptyOrNull(orderList)) {
                        sizeData = orderList.size();
                        PARAM = 22;
                        new RequestUrlTransaction().execute();//22
                    } else {
                        setToast("Tidak ada data atau Semua data sudah di sync");
                    }
                } else {
                    database.addLog(logResult);
                    setToast("Gagal menyiapkan data order");
                }
            } else if (PARAM == 23) {
                progress.dismiss();
                if (setDataSyncSuccess) {
                    if (Helper.isNotEmptyOrNull(returnList)) {
                        sizeData = returnList.size();
                        PARAM = 24;
                        new RequestUrlTransaction().execute();//24
                    } else {
                        setToast("Tidak ada data atau Semua data sudah di sync");
                    }
                } else {
                    database.addLog(logResult);
                    setToast("Gagal menyiapkan data return");
                }
            } else if (PARAM == 25) {
                progress.dismiss();
                if (setDataSyncSuccess) {
                    if (Helper.isNotEmptyOrNull(photoList)) {
                        sizeData = photoList.size();
                        PARAM = 26;
                        new RequestUrlTransaction().execute();//26
                    } else {
                        setToast("Tidak ada data atau Semua data sudah di sync");
                    }
                } else {
                    database.addLog(logResult);
                    setToast("Gagal menyiapkan data photo");
                }
            } else if (PARAM == 27) {
                progress.dismiss();
                if (setDataSyncSuccess) {
                    if (Helper.isNotEmptyOrNull(orderEmptyDiscount)) {
                        sizeData = orderEmptyDiscount.size();
                        PARAM = 28;
                        new RequestUrlTransaction().execute();//14
                    } else {
                        setToast("Tidak ada data atau Semua data sudah ada diskon");
                    }
                } else {
                    database.addLog(logResult);
                    setToast("Gagal menyiapkan data order");
                }
            } else if (PARAM == 29) {
                progress.dismiss();
                if (logResult.getIdMessage() == 1) {
                    String message = "Send database offline : " + logResult.getMessage();
                    logResult.setMessage(message);
                    setToast(logResult.getMessage());
                } else {
                    setToast(logResult.getMessage());
                }
                database.addLog(logResult);
            } else if (PARAM == 30) {
                progress.dismiss();
                if (logResult.getIdMessage() == 1) {
                    String message = "Send update register id : " + logResult.getMessage();
                    logResult.setMessage(message);
                    setToast(logResult.getMessage());
                } else {
                    setToast(logResult.getMessage());
                }
                database.addLog(logResult);
            }
        }

        private class RequestUrlTransaction extends AsyncTask<Void, Integer, List<WSMessage>> {

            @Override
            protected List<WSMessage> doInBackground(Void... voids) {
                try {
                    List<WSMessage> listWSMsg = new ArrayList<>();
                    String URL_ = null, url = null;
                    Map req = new HashMap();
                    int counter = 0;

                    if (PARAM == 14) {
                        URL_ = Constants.API_SYNC_CUSTOMER_NOO;
                        url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                        req = new HashMap();
                        List<Customer> noo = new ArrayList<>();
                        for (Customer data : nooList) {
                            counter++;
                            req = new HashMap();
                            noo = new ArrayList<>();
                            noo.add(data);
                            req.put("listData", noo);
                            if (progressDialog != null) {
                                progressDialog.setMessage("Mengirim data noo offline...");
                            }
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Noo " + data.getId() + " success");
                                database.updateSyncNoo(data);
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                        }
                    } else if (PARAM == 16) {
                        URL_ = Constants.API_SYNC_VISIT;
                        url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                        req = new HashMap();
                        List<VisitSalesman> noo = new ArrayList<>();
                        for (VisitSalesman data : visitSalesmanList) {
                            counter++;
                            req = new HashMap();
                            noo = new ArrayList<>();
                            noo.add(data);
                            req.put("listData", noo);
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Visit Salesman " + data.getIdHeader() + " success");
                                database.updateSyncVisitSalesman(data);
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                        }
                    } else if (PARAM == 18) {
                        URL_ = Constants.API_SYNC_STORE_CHECK;
                        url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                        for (Map data : storeCheckList) {
                            counter++;
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Store Check " + data.get("id_mobile").toString() + " success");
                                database.updateSyncStoreCheck(data);
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                        }
                    } else if (PARAM == 20) {
                        URL_ = Constants.API_SYNC_COLLECTION;
                        url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                        for (CollectionHeader data : collectionList) {
                            counter++;
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Collection " + data.getIdHeader() + " success");
                                database.updateSyncCollectionHeader(data);
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                        }
                    } else if (PARAM == 22) {
                        URL_ = Constants.API_SYNC_ORDER;
                        url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                        for (Order data : orderList) {
                            counter++;
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Order " + data.getIdHeader() + " success");
                                database.updateSyncOrderHeader(data);
                            }
                            database.addLog(logResult);
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                        }
                    } else if (PARAM == 24) {
                        URL_ = Constants.API_SYNC_RETURN;
                        url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                        for (Map data : returnList) {
                            counter++;
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Return " + data.get("id_customer").toString() + " success");
                                database.addLog(logResult);
                                database.updateSyncReturn(data);
                            }

                            publishProgress(counter);
                            listWSMsg.add(logResult);
                        }
                    } else if (PARAM == 26) {
                        URL_ = Constants.API_SYNC_ONE_PHOTO;
                        url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);

                        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                        Map requestData = new HashMap();
                        for (Map data : photoList) {
                            map = new LinkedMultiValueMap<String, Object>();
                            if (data.get("photo") != null) {
                                map.add("photo", new FileSystemResource(data.get("photo").toString()));
                            } else {
                                map.add("photo", "");
                            }
                            requestData = new HashMap();
                            requestData.put("typePhoto", data.get("typePhoto"));
                            requestData.put("photoName", data.get("photoName"));
                            requestData.put("id", user.getUsername());
                            requestData.put("customerId", data.get("customerId"));
                            requestData.put("idDB", data.get("idDB"));
                            String json = new Gson().toJson(requestData);
                            map.add("data", json);

                            counter++;
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBodyMultiPart(url, WSMessage.class, map);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Photo " + data.get("customerId").toString() + " success");
                                database.addLog(logResult);
                                database.updateSyncPhoto(data);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                        }
                    } else if (PARAM == 28) {
                        req = new HashMap();
                        List<Order> mList = new ArrayList<>();
                        url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_GET_DISCOUNT_ORDER);
                        Map request = new HashMap();
                        for (Order data : orderEmptyDiscount) {
                            counter++;
                            request = new HashMap();
                            request.put("id_customer", data.getId_customer());
                            request.put("tipe_outlet", data.getOrder_type());
                            request.put("id", data.getIdHeader());
                            request.put("order_date", data.getOrder_date());
                            List<Map> listBarang = new ArrayList<>();
                            double omzet = 0;
                            Map tempBarang = new HashMap<>();
                            for (Material material : data.getMaterialList()) {
                                if (material.getQty() != 0) {
                                    tempBarang = new HashMap<>();
                                    tempBarang.put("id", material.getId());
                                    tempBarang.put("harga", material.getPrice());
                                    tempBarang.put("qty", material.getQty());
                                    tempBarang.put("satuan", material.getUom());
                                    listBarang.add(tempBarang);
                                }
                            }
                            request.put("listDetail", listBarang);
                            if (progressDialog != null) {
                                progressDialog.setMessage("Mengirim data order...");
                            }
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                            if (logResult != null) {
                                if (logResult.getIdMessage() == 1) {
                                    logResult.setMessage("Sync order discount " + data.getId() + " success");
                                    database.addLog(logResult);
                                    Map resultMap = (Map) logResult.getResult();
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
                                        for (Material material : data.getMaterialList()) {
                                            if (material.getId().equals(kodeBarang)) {
                                                material.setExtraDiscount(extra);
                                                material.setTotalDiscount(totalDisc);
                                                material.setDiskonList(discList);
                                            }
                                            omzet = 0;
                                            omzet = omzet + (material.getPrice() - material.getTotalDiscount());
                                        }
                                        data.setOmzet(omzet);
                                        database.updateOrderDiscount(data, user.getUsername());
                                    }
                                }
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                        }
                    }
                    return listWSMsg;
                } catch (Exception ex) {
                    if (ex.getMessage() != null) {
                        Log.e("Sync  offline", ex.getMessage());
                    }
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                    switch (PARAM) {
                        case 14:
                            logResult.setMessage("Sync noo failed : " + exMess);
                            break;
                        case 16:
                            logResult.setMessage("Sync visit salesman failed : " + exMess);
                            break;
                        case 18:
                            logResult.setMessage("Sync store check failed : " + exMess);
                            break;
                        case 20:
                            logResult.setMessage("Sync collection failed : " + exMess);
                            break;
                        case 22:
                            logResult.setMessage("Sync order failed : " + exMess);
                            break;
                        case 24:
                            logResult.setMessage("Sync return failed : " + exMess);
                            break;
                        case 26:
                            logResult.setMessage("Sync photo failed : " + exMess);
                            break;
                        case 28:
                            logResult.setMessage("Get discount failed : " + exMess);
                            break;
                    }
                    database.addLog(logResult);
                    return null;
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(sizeData);
                progressDialog.setMessage(getString(R.string.progress_checkout));
                progressDialog.show();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressDialog.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(List<WSMessage> listResult) {
                if (listResult != null) {
                    int error = 0;
                    for (WSMessage msg : listResult) {
                        if (msg.getIdMessage() == 0) {
                            error++;
                        }
                    }
                    if (listResult.size() == sizeData) {//ganti sizeData
                        if (error == 0) {
                            setToast("Sukses mengirim data " + String.valueOf(listResult.size()));
                            if (PARAM == 26) {
                                Helper.deleteFolder(getDirLoc(getActivity()).getPath());
                            }
                        } else {
                            setToast("Gagal mengirim data : " + String.valueOf(error));
                        }
                    } else {
                        setToast("Gagal mengirim data : " + String.valueOf(error));
                    }
                } else {
                    setToast("Gagal mengirim data");
                }
                progressDialog.dismiss();
            }
        }
    }
}
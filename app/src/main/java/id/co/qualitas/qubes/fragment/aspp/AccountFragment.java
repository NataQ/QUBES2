package id.co.qualitas.qubes.fragment.aspp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.LoginActivity;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.aspp.LogAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.CalendarUtils;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.Bank;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.CustomerType;
import id.co.qualitas.qubes.model.DaerahTingkat;
import id.co.qualitas.qubes.model.FreeGoods;
import id.co.qualitas.qubes.model.JenisJualandTop;
import id.co.qualitas.qubes.model.LastLog;
import id.co.qualitas.qubes.model.LogModel;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.MaterialResponse;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.OffDate;
import id.co.qualitas.qubes.model.OfflineLoginData;
import id.co.qualitas.qubes.model.OptionFreeGoods;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.OrderPlanHeader;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Parameter;
import id.co.qualitas.qubes.model.PriceCode;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.ReturnRequest;
import id.co.qualitas.qubes.model.ReturnResponse;
import id.co.qualitas.qubes.model.SalesPriceDetail;
import id.co.qualitas.qubes.model.SalesPriceHeader;
import id.co.qualitas.qubes.model.StoreCheck;
import id.co.qualitas.qubes.model.ToPrice;
import id.co.qualitas.qubes.model.Uom;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;
import id.co.qualitas.qubes.model.VisitOrderRequest;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManager;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class AccountFragment extends BaseFragment {
    private CardView llUploadDB, llSync, llLog, llChangePassword;
    private LogAdapter mAdapter;
    private boolean saveDataSuccess = false;
    private WSMessage messageResponse, logResult;
    private ProgressDialog progressDialog;
    private int sizeData = 0;

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

        llUploadDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Upload DB", Toast.LENGTH_SHORT).show();
            }
        });

        llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((MainActivity) getActivity()).changePage(7);
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
                    PARAM = 12;
                    new RequestUrlTransaction().execute();//12
                });

                linearVisit.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 13;
                    new RequestUrlTransaction().execute();//13
                });

                linearStoreCheck.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 14;
                    new RequestUrlTransaction().execute();//14
                });

                linearCollection.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 15;
                    new RequestUrlTransaction().execute();//15
                });

                linearOrder.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 16;
                    new RequestUrlTransaction().execute();//16
                });

                linearReturn.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 17;
                    new RequestUrlTransaction().execute();//17
                });

                linearPhoto.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    progress.show();
                    PARAM = 18;
                    new RequestUrlTransaction().execute();//18
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

                    List<PriceCode> priceList = new ArrayList<>();
                    PriceCode[] paramArray6 = Helper.ObjectToGSON(response.get("listPriceCode"), PriceCode[].class);
                    if (paramArray6 != null) {
                        Collections.addAll(priceList, paramArray6);
                        database.deleteMasterPriceCode();
                    }
                    for (PriceCode param : priceList) {
                        database.addMasterPriceCode(param, user.getUsername());
                    }

                    List<SalesPriceHeader> salesPriceHeaderList = new ArrayList<>();
                    SalesPriceHeader[] paramArray7 = Helper.ObjectToGSON(response.get("listSalesPriceHeader"), SalesPriceHeader[].class);
                    if (paramArray7 != null) {
                        Collections.addAll(salesPriceHeaderList, paramArray7);
                        database.deleteMasterSalesPriceHeader();
                    }
                    for (SalesPriceHeader param : salesPriceHeaderList) {
                        database.addMasterSalesPriceHeader(param, user.getUsername());
                    }

                    List<SalesPriceDetail> salesPriceDetailList = new ArrayList<>();
                    SalesPriceDetail[] paramArray8 = Helper.ObjectToGSON(response.get("listSalesPriceDetail"), SalesPriceDetail[].class);
                    if (paramArray8 != null) {
                        Collections.addAll(salesPriceDetailList, paramArray8);
                        database.deleteMasterSalesPriceDetail();
                    }
                    for (SalesPriceDetail param : salesPriceDetailList) {
                        database.addMasterSalesPriceDetail(param, user.getUsername());
                    }

                    List<Material> minOrderList = new ArrayList<>();
                    Material[] paramArray11 = Helper.ObjectToGSON(response.get("listMinimalOrder"), Material[].class);
                    Collections.addAll(minOrderList, paramArray11);
                    database.deleteMasterMinimalOrder();
                    for (Material param : minOrderList) {
                        database.addLimitBon(param, user.getUserLogin());
                    }

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
                } else {
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

                    if (response.get("visit") != null) {
                        LinkedTreeMap startDay = (LinkedTreeMap) response.get("visit");
                        double id = (double) startDay.get("id");
                        SessionManagerQubes.setStartDay((int) id);
                    } else {
                        SessionManagerQubes.setStartDay(0);
                    }

                    Customer[] paramArray = Helper.ObjectToGSON(response.get("listCustomerSalesman"), Customer[].class);
                    List<Customer> mList = new ArrayList<>();
                    if (paramArray != null) {
                        Collections.addAll(mList, paramArray);
                        database.deleteMasterNonRouteCustomer();
                        database.deleteMasterNonRouteCustomerPromotion();
                        database.deleteMasterNonRouteCustomerDct();
                    }

                    for (Customer param : mList) {
                        List<Promotion> arrayList = new ArrayList<>();
                        Promotion[] matArray = Helper.ObjectToGSON(param.getPromoList(), Promotion[].class);
                        if (matArray != null) {
                            Collections.addAll(arrayList, matArray);
                        }
                        param.setPromoList(arrayList);

                        List<Material> arrayDctList = new ArrayList<>();
                        Material[] dctArray = Helper.ObjectToGSON(param.getDctList(), Material[].class);
                        if (dctArray != null) {
                            Collections.addAll(arrayDctList, dctArray);
                        }
                        param.setDctList(arrayDctList);
                        int idHeader = database.addNonRouteCustomer(param, user.getUsername());
                        for (Promotion mat : arrayList) {
                            database.addNonRouteCustomerPromotion(mat, String.valueOf(idHeader), user.getUsername());
                        }

                        for (Material mat : arrayDctList) {
                            database.addNonRouteCustomerDct(mat, String.valueOf(idHeader), user.getUsername(), param.getId());
                        }
                    }

                    saveDataSuccess = true;
                    return null;
                }
            } catch (Exception ex) {
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
                    if (PARAM == 1) {
                        logResult.setMessage("Get data reason error: " + exMess);
                    } else if (PARAM == 3) {
                        logResult.setMessage("Get data bank error: " + exMess);
                    } else if (PARAM == 5) {
                        logResult.setMessage("Get data daerah tingkat error: " + exMess);
                    } else if (PARAM == 7) {
                        logResult.setMessage("Get data material error: " + exMess);
                    } else if (PARAM == 9) {
                        logResult.setMessage("Get data parameter error: " + exMess);
                    } else if (PARAM == 11) {
                        logResult.setMessage("Get data customer error: " + exMess);
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
            }


        }
    }

    private class RequestUrlTransaction extends AsyncTask<Void, Integer, List<WSMessage>> {

        @Override
        protected List<WSMessage> doInBackground(Void... voids) {
            try {
                List<WSMessage> listWSMsg = new ArrayList<>();
                int counter = 0;
                if (PARAM == 12) {
                    List<Customer> mList = setDataCustomerNoo();
                    String URL_ = Constants.API_SYNC_CUSTOMER_NOO;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    Map req = new HashMap();
                    sizeData = mList.size();
                    for (Customer data : mList) {
                        counter++;
                        req = new HashMap();
                        req.put("listData", new ArrayList<>().add(data));
                        logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                        if (logResult.getIdMessage() == 1) {
                            logResult = new WSMessage();
                            logResult.setIdMessage(1);
                            logResult.setMessage("Sync Noo " + data.getId() + " success");
                            database.updateSyncNoo(data);
                        }
                        database.addLog(logResult);
                        publishProgress(counter);
                        listWSMsg.add(logResult);
                    }
                } else if (PARAM == 13) {
                    List<VisitSalesman> mList = setDataVisit();
                    String URL_ = Constants.API_SYNC_VISIT;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    Map req = new HashMap();
                    sizeData = mList.size();
                    for (VisitSalesman data : mList) {
                        counter++;
                        req = new HashMap();
                        req.put("listData", new ArrayList<>().add(data));
                        logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                        if (logResult.getIdMessage() == 1) {
                            logResult = new WSMessage();
                            logResult.setIdMessage(1);
                            logResult.setMessage("Sync Visit Salesman " + data.getIdHeader() + " success");
                            database.updateSyncVisitSalesman(data);
                        }
                        database.addLog(logResult);
                        publishProgress(counter);
                        listWSMsg.add(logResult);
                    }
                } else if (PARAM == 14) {
                    List<Map> mList = setDataStoreCheck();
                    String URL_ = Constants.API_SYNC_STORE_CHECK;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    sizeData = mList.size();
                    for (Map data : mList) {
                        counter++;
                        logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                        if (logResult.getIdMessage() == 1) {
                            logResult = new WSMessage();
                            logResult.setIdMessage(1);
                            logResult.setMessage("Sync Store Check " + data.get("id_mobile").toString() + " success");
                            database.updateSyncStoreCheck(data);
                        }
                        database.addLog(logResult);
                        publishProgress(counter);
                        listWSMsg.add(logResult);
                    }
                } else if (PARAM == 15) {
                    List<CollectionHeader> mList = setDataCollection();
                    String URL_ = Constants.API_SYNC_COLLECTION;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    sizeData = mList.size();
                    for (CollectionHeader data : mList) {
                        counter++;
                        logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                        if (logResult.getIdMessage() == 1) {
                            logResult = new WSMessage();
                            logResult.setIdMessage(1);
                            logResult.setMessage("Sync Collection " + data.getIdHeader() + " success");
                            database.updateSyncCollectionHeader(data);
                        }
                        database.addLog(logResult);
                        publishProgress(counter);
                        listWSMsg.add(logResult);
                    }
                } else if (PARAM == 16) {
                    List<Order> mList = setDataOrder();
                    String URL_ = Constants.API_SYNC_ORDER;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    sizeData = mList.size();
                    for (Order data : mList) {
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
                } else if (PARAM == 17) {
                    List<Map> mList = setDataReturn();
                    String URL_ = Constants.API_SYNC_RETURN;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    sizeData = mList.size();
                    for (Map data : mList) {
                        counter++;
                        logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                        if (logResult.getIdMessage() == 1) {
                            logResult = new WSMessage();
                            logResult.setIdMessage(1);
                            logResult.setMessage("Sync Return " + data.get("id_customer").toString() + " success");
                            database.updateSyncStoreCheck(data);
                        }
                        database.addLog(logResult);
                        publishProgress(counter);
                        listWSMsg.add(logResult);
                    }
                } else if (PARAM == 18) {
                    List<VisitSalesman> mList = setDataPhoto();
                    String URL_ = Constants.API_SYNC_PHOTO;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    Map req = new HashMap();
                    sizeData = mList.size();

                    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                    if (uriBerangkat != null) {
                        map.add("kmAwalPhoto", new FileSystemResource(uriBerangkat.getPath()));
                    } else {
                        map.add("kmAwalPhoto", "");
                    }

                    for (VisitSalesman data : mList) {
                        counter++;
                        req = new HashMap();
                        req.put("listData", new ArrayList<>().add(data));
                        logResult = (WSMessage) NetworkHelper.postWebserviceWithBodyMultiPart(url, WSMessage.class, req);
                        if (logResult.getIdMessage() == 1) {
                            logResult = new WSMessage();
                            logResult.setIdMessage(1);
                            logResult.setMessage("Sync Photo " + data.getIdHeader() + " success");
                            database.updateSyncVisitSalesman(data);
                        }
                        database.addLog(logResult);
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
                    case 12:
                        logResult.setMessage("Sync noo data failed : " + exMess);
                        break;
                    case 13:
                        logResult.setMessage("Sync visit salesman data failed : " + exMess);
                        break;
                    case 14:
                        logResult.setMessage("Sync store check data failed : " + exMess);
                        break;
                    case 15:
                        logResult.setMessage("Sync collection data failed : " + exMess);
                        break;
                    case 16:
                        logResult.setMessage("Sync order data failed : " + exMess);
                        break;
                    case 17:
                        logResult.setMessage("Sync return data failed : " + exMess);
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
                if (listResult.size() == sizeData) {
                    if (error == 0) {
                        setToast("Sukses mengirim data");
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

    private List<Customer> setDataCustomerNoo() {
        List<Customer> mList = new ArrayList<>();
        mList.addAll(database.getAllNoo());
        return mList;
    }

    private List<VisitSalesman> setDataVisit() {
        List<VisitSalesman> mList = new ArrayList<>();
        mList.addAll(database.getAllVisitSalesman());
        return mList;
    }

    private List<Map> setDataStoreCheck() {
        List<Map> headerList = new ArrayList<>();
        List<Customer> customerList = database.getAllCustomerCheckOut();
        List<Material> mList = new ArrayList<>();
        Map header = new HashMap();
        for (Customer customer : customerList) {
            mList.addAll(database.getAllStoreCheckCheckOut(customer.getId()));
            if (mList.size() != 0) {
                header = new HashMap();
                header.put("id_mobile", mList.get(0).getIdheader());
                header.put("date", mList.get(0).getDate());
                header.put("id_salesman", user.getUsername());
                header.put("id_customer", mList.get(0).getId_customer());
                header.put("listData", mList);
                headerList.add(header);
            }
        }
        return headerList;
    }

    private List<Map> setDataReturn() {
        List<Map> headerList = new ArrayList<>();
        List<Customer> customerList = database.getAllCustomerCheckOut();
        List<Material> mList = new ArrayList<>();
        Map header = new HashMap();
        for (Customer customer : customerList) {
            mList.addAll(database.getAllReturnCheckOut(customer.getId()));
            if (mList.size() != 0) {
                header = new HashMap();
                header.put("id_mobile", mList.get(0).getIdheader());
                header.put("date", mList.get(0).getDate());
                header.put("id_salesman", user.getUsername());
                header.put("id_customer", mList.get(0).getId_customer());
                header.put("listData", mList);
                headerList.add(header);
            }
        }
        return headerList;
    }

    private List<CollectionHeader> setDataCollection() {
        List<Customer> customerList = database.getAllCustomerCheckOut();
        List<CollectionHeader> mListHeader = new ArrayList<>();
        for (Customer customer : customerList) {
            mListHeader.addAll(database.getAllCollectionHeader(customer.getId()));
        }
        return mListHeader;
    }

    private List<Order> setDataOrder() {
        List<Customer> customerList = database.getAllCustomerCheckOut();
        List<Order> mListHeader = new ArrayList<>();
        for (Customer customer : customerList) {
            mListHeader.addAll(database.getAllOrderHeader(customer.getId(), user.getUsername()));
        }
        return mListHeader;
    }

    private List<Order> setDataPhoto() {
        List<Customer> customerList = database.getAllCustomerCheckOut();
        List<Order> mListHeader = new ArrayList<>();
        for (Customer customer : customerList) {
            mListHeader.addAll(database.getAllOrderHeader(customer.getId(), user.getUsername()));
        }
        return mListHeader;
    }
}
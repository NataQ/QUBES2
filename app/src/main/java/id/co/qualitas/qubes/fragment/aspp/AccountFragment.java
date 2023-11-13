package id.co.qualitas.qubes.fragment.aspp;

import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import id.co.qualitas.qubes.model.OrderPlanHeader;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Parameter;
import id.co.qualitas.qubes.model.PriceCode;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.ReturnRequest;
import id.co.qualitas.qubes.model.ReturnResponse;
import id.co.qualitas.qubes.model.SalesPriceDetail;
import id.co.qualitas.qubes.model.SalesPriceHeader;
import id.co.qualitas.qubes.model.ToPrice;
import id.co.qualitas.qubes.model.Uom;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;
import id.co.qualitas.qubes.model.VisitOrderRequest;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManager;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class AccountFragment extends BaseFragment {
    private CardView llUploadDB, llSync, llLog, llChangePassword;
    private LogAdapter mAdapter;
    private boolean saveDataSuccess = false;
    private WSMessage messageResponse, logResult;

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

                LinearLayout linSyncAll = alertDialog.findViewById(R.id.linearSyncAll);
                LinearLayout linSyncMasterData = alertDialog.findViewById(R.id.linearMasterData);
                LinearLayout linearCollection = alertDialog.findViewById(R.id.linearCollection);
                LinearLayout linSyncVisit = alertDialog.findViewById(R.id.linearVisit);
                LinearLayout linSyncStoreCheck = alertDialog.findViewById(R.id.linearStoreCheck);
                LinearLayout linSyncOrder = alertDialog.findViewById(R.id.linearOrder);
                LinearLayout linSyncReturn = alertDialog.findViewById(R.id.linearReturn);
                Button btnCancel = alertDialog.findViewById(R.id.btnCancel);

                linSyncAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM = 4;
                        new RequestUrl().execute();
                        alertDialog.dismiss();
                    }
                });
                linSyncMasterData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM = 1;
                        new RequestUrl().execute();
                        alertDialog.dismiss();
                    }
                });

                linearCollection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });
                linSyncVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });
                linSyncStoreCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });
                linSyncOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                    }
                });
                linSyncReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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
                    String URL_ = Constants.API_MASTER_DATA_GET;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
                    return null;
                } else {
                    Map response = (Map) messageResponse.getResult();

                    List<Reason> reasonList = new ArrayList<>();
                    Reason[] paramArray = Helper.ObjectToGSON(response.get("listReason"), Reason[].class);
                    if (paramArray != null) {
                        Collections.addAll(reasonList, paramArray);
                        database.deleteMasterReason();
                    }
                    for (Reason reason : reasonList) {
                        database.addMasterReason(reason, user.getUsername());
                    }

                    List<Bank> bankList = new ArrayList<>();
                    Bank[] paramArray1 = Helper.ObjectToGSON(response.get("listBank"), Bank[].class);
                    if (paramArray1 != null) {
                        Collections.addAll(bankList, paramArray1);
                        database.deleteMasterBank();
                    }
                    for (Bank param : bankList) {
                        database.addMasterBank(param, user.getUsername());
                    }

                    List<DaerahTingkat> daerahTingkatList = new ArrayList<>();
                    DaerahTingkat[] paramArray3 = Helper.ObjectToGSON(response.get("listDaerahTingkat"), DaerahTingkat[].class);
                    if (paramArray3 != null) {
                        Collections.addAll(daerahTingkatList, paramArray3);
                        database.deleteMasterDaerahTingkat();
                    }
                    for (DaerahTingkat param : daerahTingkatList) {
                        database.addMasterDaerahTingkat(param, user.getUsername());
                    }

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

                    List<Parameter> parameterList = new ArrayList<>();
                    Parameter[] paramArray9 = Helper.ObjectToGSON(response.get("parameter"), Parameter[].class);
                    if (paramArray9 != null) {
                        Collections.addAll(parameterList, paramArray9);
                        database.deleteMasterParameter();
                    }
                    for (Parameter param : parameterList) {
                        database.addMasterParameter(param, user.getUsername());
                    }

                    List<CustomerType> cusTypeList = new ArrayList<>();
                    CustomerType[] paramArray10 = Helper.ObjectToGSON(response.get("listCustomerType"), CustomerType[].class);
                    if (paramArray10 != null) {
                        Collections.addAll(cusTypeList, paramArray10);
                        database.deleteMasterCustomerType();
                    }
                    for (CustomerType param : cusTypeList) {
                        database.addMasterCustomerType(param, user.getUsername());
                    }

                    if (response.get("max_visit") != null) {
                        user.setMax_visit((Integer) response.get("max_visit"));
//                        double maxVisit = (double) response.get("max_visit");
//                        Parameter parameter = new Parameter();
//                        parameter.setKey("MAX_VISIT");
//                        parameter.setValue(String.valueOf(maxVisit));
//                        database.deleteParameterByKey("MAX_VISIT");
//                        database.addMasterParameter(parameter, userResponse.getUsername());
                    }

                    SessionManagerQubes.setUserProfile(user);

                    saveDataSuccess = true;
                    return null;
                }
            } catch (Exception ex) {
                //connection = true;
                if (ex.getMessage() != null) {
                    Log.e("syncMaster", ex.getMessage());
                }
                if (PARAM == 2) {
                    saveDataSuccess = false;
                } else {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                    logResult.setMessage("Sync Master Data error: " + exMess);
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
        protected void onPostExecute(WSMessage wsMessage) {
            if (PARAM == 1) {
                if (wsMessage != null) {
                    messageResponse = wsMessage;
                    PARAM = 2;
                    new RequestUrl().execute();
                } else {
                    progress.dismiss();
                    setToast(getString(R.string.failedGetData));
                }
            } else {
                progress.dismiss();
                if (saveDataSuccess) {
                    setToast("Sync Data Success");
                } else {
                    setToast(getString(R.string.failedSaveData));
                }
            }
        }
    }
}
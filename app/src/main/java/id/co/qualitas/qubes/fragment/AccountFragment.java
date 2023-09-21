package id.co.qualitas.qubes.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.LoginActivity;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.CalendarUtils;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.FreeGoods;
import id.co.qualitas.qubes.model.JenisJualandTop;
import id.co.qualitas.qubes.model.LastLog;
import id.co.qualitas.qubes.model.MaterialResponse;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.OffDate;
import id.co.qualitas.qubes.model.OfflineLoginData;
import id.co.qualitas.qubes.model.OptionFreeGoods;
import id.co.qualitas.qubes.model.OrderPlanHeader;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.ReturnRequest;
import id.co.qualitas.qubes.model.ReturnResponse;
import id.co.qualitas.qubes.model.ToPrice;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;
import id.co.qualitas.qubes.model.VisitOrderRequest;
import id.co.qualitas.qubes.session.SessionManager;

public class AccountFragment extends BaseFragment {
    private CardView llProfile, llSync, llLog, llChangePassword;
    private Button btnLogout;
    private Intent intent;
    private int PARAM_VISIT = 0;
    private static final String SYNC_SUCCESS = "Sync succeed";
    private static final String SYNC_FAILED = "Sync failed";
    private String MSG_SYNC_ALL = "";
    private OfflineLoginData offlineData = new OfflineLoginData();
    private boolean saveOrder = false;
    private ArrayList<OrderPlanHeader> listUpdateOrderPlan = new ArrayList<>();
    private ArrayList<OrderPlanHeader> listInsertOrderPlan = new ArrayList<>();
    private ArrayList<OrderPlanHeader> listDeleteOrderPlan = new ArrayList<>();

    private ArrayList<OutletResponse> listSaveNewVisit = new ArrayList<>();
    protected ArrayList<OutletResponse> listDeleteNewVisit = new ArrayList<>();
    private ArrayList<OutletResponse> listSaveVisitSalesman = new ArrayList<>();
    private ArrayList<OutletResponse> listUpdateVisitSalesman = new ArrayList<>();

    private ArrayList<ReturnRequest> listReturnForSync = new ArrayList<>();

    private ArrayList<MaterialResponse> listStoreCheck = new ArrayList<>();
    private ArrayList<MaterialResponse> listUpdateSC = new ArrayList<>();
    private ArrayList<MaterialResponse> listInsertSC = new ArrayList<>();
    private ArrayList<MaterialResponse> listDeleteSC = new ArrayList<>();

    private ArrayList<VisitOrderRequest> listSaveOrder = new ArrayList<>();
    private ArrayList<VisitOrderRequest> listTempPendingOrder = new ArrayList<>();
    private ArrayList<ToPrice> listTempPendingToPrice = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account, container, false);
        initialize();
        init();
        initFragment();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((MainActivity) getActivity()).changePage(1);
                        return true;
                    }
                }
                return false;
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changePage(6);
            }
        });

        llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changePage(7);
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
                        PARAM = 6;
                        new RequestUrl().execute();
                        alertDialog.dismiss();
                    }
                });

                linearCollection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM = 7;
                        new RequestUrl().execute();
                        alertDialog.dismiss();
                    }
                });
                linSyncVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM = 8;
                        new RequestUrl().execute();
                        alertDialog.dismiss();
                    }
                });
                linSyncStoreCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM_VISIT = 9;
                        PARAM = 8;
                        new RequestUrl().execute();
                        alertDialog.dismiss();
                    }
                });
                linSyncOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM_VISIT = 10;
                        PARAM = 8;
                        new RequestUrl().execute();
                        alertDialog.dismiss();
                    }
                });
                linSyncReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM_VISIT = 11;
                        PARAM = 8;
                        new RequestUrl().execute();
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

//                TextView txtLastSync = alertDialog.findViewById(R.id.txtLastSync);
//                TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);
//                Button btnHide = alertDialog.findViewById(R.id.btnHide);
//
//                LastLog lLog = (LastLog) Helper.getItemParam(Constants.ERROR_LOG);
//                txtDialog.setText("Empty");
//                if (lLog != null) {
//                    txtLastSync.setText(Helper.validateResponseEmpty(lLog.getLastSync()));
//                    txtDialog.setText(Helper.validateResponseEmpty(lLog.getLastMsg()));
//                }
//
//                btnHide.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.dismiss();
//                    }
//                });

                alertDialog.show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView;
                final Dialog alertDialog = new Dialog(getContext());
                dialogView = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(dialogView);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btnCancel = alertDialog.findViewById(R.id.btnCancel);
                Button btnSave = alertDialog.findViewById(R.id.btnSave);
                TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);

                txtDialog.setText(getResources().getString(R.string.textDialogLogout));
                btnSave.setText(getResources().getString(R.string.yes));
                btnCancel.setText(getResources().getString(R.string.no));

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PARAM = 12;
                        new RequestUrl().execute();
                        progress.show();

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        return rootView;
    }

    private void initialize() {
        btnLogout = rootView.findViewById(R.id.btnLogout);
        llProfile = rootView.findViewById(R.id.llProfile);
        llSync = rootView.findViewById(R.id.llSync);
        llLog = rootView.findViewById(R.id.llLog);
        llChangePassword = rootView.findViewById(R.id.llChangePassword);
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "5");
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    // handle back button
////                    setContentToHome();
//                    Helper.setItemParam(Constants.CURRENTPAGE, "1");
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    private class RequestUrl extends AsyncTask<Void, Void, MessageResponse> {

        @Override
        protected MessageResponse doInBackground(Void... voids) {
            try {
                if (PARAM == 4) {
                    final String url = Constants.URL.concat(Constants.API_SYNC_OFFLINE);

                    OfflineLoginData offlineData = new OfflineLoginData();
                    offlineData.setIdSalesman(user.getIdEmployee());

                    getListSyncOrderPlan();
                    offlineData.setListSaveOrderPlan(listInsertOrderPlan);//1
                    offlineData.setListUpdateOrderPlan(listUpdateOrderPlan);
                    offlineData.setListDeleteOrderPlan(listDeleteOrderPlan);

                    getListSyncVisitPlan();
                    offlineData.setListSaveNewVisit(listSaveNewVisit);//2
                    offlineData.setListDeleteNewVisit(listDeleteNewVisit);
                    offlineData.setListSaveVisitSalesman(listSaveVisitSalesman);//3
                    offlineData.setListUpdateVisitSalesman(listUpdateVisitSalesman);

                    getListSyncStoreCheck();
                    offlineData.setListSaveStoreCheck(listInsertSC);//4
                    offlineData.setListUpdateStoreCheck(listUpdateSC);
                    offlineData.setListDeleteStoreCheck(listDeleteSC);

                    getListSyncOrder();
                    offlineData.setListSaveOrder(listSaveOrder);//5

                    getListSyncReturn();
                    offlineData.setListSaveReturn(listReturnForSync);

                    return (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, offlineData);
                } else if (PARAM == 5) {
                    String URL_GET_OFFLINE_DATA = Constants.API_GET_OFFLINE_LOGIN;

//                    final String url = Constants.URL.concat(URL_GET_OFFLINE_DATA).
//                            concat(Constants.QUESTION).concat(Constants.ID_EMPLOYEE).concat(Constants.EQUALS).concat(user.getIdEmployee());

                    final String url = Constants.URL.concat(URL_GET_OFFLINE_DATA).
                            concat(Constants.QUESTION).concat(Constants.ID_EMPLOYEE).concat(Constants.EQUALS).concat(user.getIdEmployee())
                            .concat(Constants.AND).concat(Constants.VERSION).concat(Constants.EQUALS).concat(Constants.CURRENT_VERSION);

                    offlineData = (OfflineLoginData) Helper.getWebservice(url, OfflineLoginData.class);
                    return null;
                } else if (PARAM == 6) {
                    final String url = Constants.URL.concat(Constants.API_SYNC_MASTER_DATA).concat(Constants.QUESTION)
                            .concat(Constants.ID_EMPLOYEE).concat(Constants.EQUALS).concat(user.getIdEmployee());

                    offlineData = (OfflineLoginData) Helper.getWebservice(url, OfflineLoginData.class);
                    return null;
                }
                if (PARAM == 7) {//OrderPlan
                    //OrderPlan
                    getListSyncOrderPlan();

                    offlineData.setIdSalesman(user.getIdEmployee());
                    offlineData.setListSaveOrderPlan(listInsertOrderPlan);//1
                    offlineData.setListUpdateOrderPlan(listUpdateOrderPlan);
                    offlineData.setListDeleteOrderPlan(listDeleteOrderPlan);

                    final String url = Constants.URL.concat(Constants.API_SYNC_ORDER_PLAN);

                    return (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, offlineData);
                } else if (PARAM == 8) {/*visit*/
                    getListSyncVisitPlan();

                    offlineData.setIdSalesman(user.getIdEmployee());
                    offlineData.setListSaveNewVisit(listSaveNewVisit);//2
                    offlineData.setListDeleteNewVisit(listDeleteNewVisit);
                    offlineData.setListSaveVisitSalesman(listSaveVisitSalesman);//3
                    offlineData.setListUpdateVisitSalesman(listUpdateVisitSalesman);

                    final String url = Constants.URL.concat(Constants.API_SYNC_VISIT);
//
//                    String infoKiki = user.getIdEmployee() + " \n"
//                            + "New Visit sebanyak " + listSaveNewVisit.size() + "\n"
//                            + "Delete Visit sebanyak " + listDeleteNewVisit.size() + "\n"
//                            + "Save Visit Salesman sebanyak " + listDeleteNewVisit.size() + "\n"
//                            + "Delete Visit sebanyak " + listSaveVisitSalesman.size() + "\n"
//                            + "Delete Visit sebanyak " + listUpdateVisitSalesman.size() + "\n"
//                            ;
//                    Helper.setItemParam(Constants.INFO_KIKI, infoKiki);

                    return (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, offlineData);
                } else if (PARAM == 9) {/*StoreCheck*/
                    getListSyncStoreCheck();
                    offlineData.setIdSalesman(user.getIdEmployee());
                    offlineData.setListSaveStoreCheck(listInsertSC);//4
                    offlineData.setListUpdateStoreCheck(listUpdateSC);
                    offlineData.setListDeleteStoreCheck(listDeleteSC);


                    final String url = Constants.URL.concat(Constants.API_SYNC_STORE_CHECK);

                    return (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, offlineData);
                } else if (PARAM == 10) {//Order
                    //hari ini
                    getListSyncOrder();
                    offlineData.setIdSalesman(user.getIdEmployee());
                    offlineData.setListSaveOrder(listSaveOrder);//5


                    final String url = Constants.URL.concat(Constants.API_SYNC_ORDER);

                    return (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, offlineData);
                } else if (PARAM == 11) {/*Return*/
                    getListSyncReturn();
                    offlineData.setIdSalesman(user.getIdEmployee());
                    offlineData.setListSaveReturn(listReturnForSync);

                    final String url = Constants.URL.concat(Constants.API_SYNC_RETURN);

                    return (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, offlineData);
                } else {
                    String URL_GET_HOME_LIST = Constants.API_LOGOUT.concat(Constants.QUESTION).concat(Constants.USERNAME.concat(Constants.EQUALS))
                            + user.getNik().concat(getString(R.string.clientId));
                    final String url = Constants.URL.concat(URL_GET_HOME_LIST);
                    return (MessageResponse) Helper.getWebservice(url, MessageResponse.class);
                }
            } catch (Exception ex) {
                //connection = true;
                if (ex.getMessage() != null) {
                    Log.e("UpdateStat", ex.getMessage());
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
        protected void onPostExecute(MessageResponse msg) {

            if (PARAM == 4) {
                if (msg != null) {
                    if (msg.getMessage() != null) {
                        LastLog lastLog = new LastLog();
                        Date curDate = SecureDate.getInstance().getDate();
                        if (curDate != null) {
                            lastLog.setLastSync(Helper.convertDateToStringNew(Constants.DATE_TYPE_16, curDate));
                        }
                        lastLog.setLastMsg(Helper.validateResponseEmpty(msg.getMessage()));
//                        Helper.setItemParam(Constants.ERROR_LOG, lastLog);
                    }

                    if (msg.getIdMessage() == 1) {
//                        Toast.makeText(getActivity(), msg.getMessage(), Toast.LENGTH_LONG).show();
                        MSG_SYNC_ALL = SYNC_SUCCESS;
                        saveOrder = msg.getResult().isSaveOrder();
                    } else {
                        MSG_SYNC_ALL = SYNC_FAILED;
//                        Toast.makeText(getActivity(), msg.getMessage(), Toast.LENGTH_LONG).show();

                        saveOrder = msg.getResult().isSaveOrder();

                        List<String> listNoBrbFailed = msg.getResult().getListNoBrbFailed();
                        List<String> listNoBrb = db.getListBRB();

                        if (listNoBrb != null && !listNoBrb.isEmpty()) {
                            for (int i = 0; i < listNoBrb.size(); i++) {
                                for (String noBrbFailed : listNoBrbFailed) {
                                    if (listNoBrb.get(i).equals(noBrbFailed)) {
                                        listNoBrb.remove(i);
                                    }
                                }
                            }
                        }
                    }
                    PARAM = 5;
                    new RequestUrl().execute();
                } else {
                    Toast.makeText(getActivity(), "Sync error", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }

            } else if (PARAM == 5) {
                progress.dismiss();
                if (offlineData != null) {
                    progress.setTitle("Save offline data...");
                    progress.show();
                    new databaseFunction().run();
                    MSG_SYNC_ALL = SYNC_SUCCESS;
                } else {
                    MSG_SYNC_ALL = SYNC_FAILED;
                }
            } else if (PARAM == 6) {
                if (offlineData != null) {
                    /*Table Material*/
                    if (offlineData.getListMaterial() != null && !offlineData.getListMaterial().isEmpty()) {
                        db.addMaterialN(offlineData.getListMaterial());
                    }

                    /*Table UOM*/
                    if (offlineData.getListUOm() != null && !offlineData.getListUOm().isEmpty()) {
                        db.addUOMN(offlineData.getListUOm());
                    }

                    if (offlineData.getListMasterUom() != null && !offlineData.getListMasterUom().isEmpty()) {
                        db.addMasterUomN(offlineData.getListMasterUom());
                    }

                    /*Table Outlet*/
                    db.deleteOutlet();
                    if (offlineData.getListOutlet() != null || !offlineData.getListOutlet().isEmpty()) {
                        Collections.sort(offlineData.getListOutlet(), new Comparator<OutletResponse>() {
                            @Override
                            public int compare(OutletResponse s1, OutletResponse s2) {
                                return Helper.ltrim(Helper.validateResponseEmpty(s1.getOutletName())).compareToIgnoreCase(Helper.ltrim(Helper.validateResponseEmpty(s2.getOutletName())));
                            }
                        });

                        for (OutletResponse outlet : offlineData.getListOutlet()) {
                            outlet.setEnabled(true);
                            db.addOutletNew(outlet);
                        }
                    }

                    /*Table TOP*/
                    db.deleteTOP();
                    if (offlineData.getListTop() != null || !offlineData.getListTop().isEmpty()) {
                        db.addTOPN(offlineData.getListTop());
                    }
                    /*Table Jenis Jual*/
                    db.deleteJenisJual();
                    if (offlineData.getListJenisJual() != null && !offlineData.getListJenisJual().isEmpty()) {
                        for (JenisJualandTop jjt : offlineData.getListJenisJual()) {
                            db.addJenisJual(jjt);
                        }
                    }

                    /*Table Sales Office*/
                    if (offlineData.getListSalesOffice() != null || !offlineData.getListSalesOffice().isEmpty()) {
                        db.addSalesOfficeN(offlineData.getListSalesOffice());
                    }

                    if (offlineData.getListPartner() != null && !offlineData.getListPartner().isEmpty()) {
                        db.addPartnerN(offlineData.getListPartner());
                    }

                    db.deleteReason();
                    if (offlineData.getListReasonReturn() != null && !offlineData.getListReasonReturn().isEmpty()) {
                        for (int i = 0; i < offlineData.getListReasonReturn().size(); i++) {
                            db.addReason(offlineData.getListReasonReturn().get(i));
                        }
                    }

                    /*FreeGoods*/
                    db.addFreeGoods(offlineData.getListFreeGoods());

                    if (offlineData.getDatetimeNow() != null) {
                        Date curDate = Helper.convertStringtoDate(Constants.DATE_TYPE_16, offlineData.getDatetimeNow());
                        Long elapse = SystemClock.elapsedRealtime();

                        OffDate offDate = new OffDate();
                        offDate.setCurDate(offlineData.getDatetimeNow());
                        offDate.setElapseTime(elapse);

                        SecureDate.getInstance().initServerDate(curDate);
                        db.deleteAttendance();

                        user.setDateTimeNow(offlineData.getDatetimeNow());
                        user.setmElapsedRealTime(SystemClock.elapsedRealtime());
                        db.addAttendance(user);

                        if (offlineData.getAdditionalInfo() != null) {
                            if (offlineData.getAdditionalInfo().getIdPlant() != null) {
                                db.updateIdPlantUser(user.getIdEmployee(), offlineData.getAdditionalInfo().getIdPlant());
                            }
                        }

                        String ex = Helper.objectToString(offDate);
                        new SessionManager(getActivity()).createDateSession(ex);

                    }

                    /*VisitPlan*/
                    if (offlineData.getListVisitPlan() != null && !offlineData.getListVisitPlan().isEmpty()) {
                        db.deleteVisitPlan();
                        db.addVisitPlanN(offlineData.getListVisitPlan());
                    }
                    progress.dismiss();
                    Toast.makeText(getActivity(), SYNC_SUCCESS, Toast.LENGTH_LONG).show();
                } else {
                    progress.dismiss();
                    Toast.makeText(getActivity(), SYNC_FAILED, Toast.LENGTH_LONG).show();
                }
            } else if (PARAM == 7) {
                progress.dismiss();
                if (msg != null) {
                    if (msg.getIdMessage() == 1) {
                        if (msg.getResult() != null) {
                            if (msg.getResult().getListOrderPlan() != null && !msg.getResult().getListOrderPlan().isEmpty()) {

                                db.deleteOrderPlan();
                                db.addOrderPlanN(msg.getResult().getListOrderPlan());

                                Toast.makeText(getActivity(), SYNC_SUCCESS, Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), SYNC_FAILED, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Null Message", Toast.LENGTH_LONG).show();
                }
            } else if (PARAM == 8) {
                progress.dismiss();
                if (msg != null) {
                    if (msg.getIdMessage() == 1) {
                        if (msg.getResult() != null) {
                            if (msg.getResult().getListVisitPlan() != null && !msg.getResult().getListVisitPlan().isEmpty()) {

                                db.deleteVisitPlan();
                                db.addVisitPlanN(msg.getResult().getListVisitPlan());

                                switch (PARAM_VISIT) {
                                    case 9:
                                        PARAM = 9;
                                        new RequestUrl().execute();
                                        break;
                                    case 10:
                                        PARAM = 10;
                                        new RequestUrl().execute();
                                        break;
                                    case 11:
                                        PARAM = 11;
                                        new RequestUrl().execute();
                                        break;
                                    default:
                                        Toast.makeText(getActivity(), "Sync Visit Success", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "Sync Visit Failed", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (PARAM == 9) {
                progress.dismiss();
                if (msg != null) {
                    if (msg.getIdMessage() == 1) {
                        if (msg.getResult() != null) {
                            if (msg.getResult().getListStore() != null && !msg.getResult().getListStore().isEmpty()) {

                                db.deleteStoreCheck();
                                db.addStoreCheckN(msg.getResult().getListStore());

                                Toast.makeText(getActivity(), SYNC_SUCCESS, Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), SYNC_FAILED, Toast.LENGTH_LONG).show();
                    }
                }
            } else if (PARAM == 10) {/*Order*/
                if (msg != null) {
                    if (msg.getIdMessage() == 1) {
                        if (msg.getResult() != null) {
                            /**/
                            db.deleteToHeader();
                            if (msg.getResult().getListOrderHeader() != null && !msg.getResult().getListOrderHeader().isEmpty()) {
                                for (VisitOrderHeader data : msg.getResult().getListOrderHeader()) {
                                    if (data.getSalesOffice() != null) {
                                        data.setSalesOfficeName(db.getSalesOfficeName(data.getSalesOffice()));
                                    }
                                    if (data.getSignature() != null) {
                                        data.setSignatureString(data.getSignature());
                                    }
                                    if (data.getPhoto() != null) {
                                        data.setPhotoString(data.getPhoto());
                                    }
                                    db.addOrderHeader(data);
                                }
                            }

                            db.deleteOrderDetail();
                            if (msg.getResult().getListOrderDetail() != null && !msg.getResult().getListOrderDetail().isEmpty()) {
                                for (VisitOrderDetailResponse data : msg.getResult().getListOrderDetail()) {
                                    db.addOrderDetail(data);
                                }
                            }


                            if (msg.getResult().getListFreegoods() != null && !msg.getResult().getListFreegoods().isEmpty()) {

                                db.addFreeGoods(msg.getResult().getListFreegoods());
                            }

                            /*set list Order yang masih kepending untuk di sync*/
                            if (listTempPendingOrder != null && !listTempPendingOrder.isEmpty()) {
                                for (VisitOrderRequest data : listTempPendingOrder) {

                                    if (data.getOrderHeader() != null) {
                                        db.addOrderHeader(data.getOrderHeader());
                                    }
                                    if (data.getOrderDetail() != null && !data.getOrderDetail().isEmpty()) {
                                        for (VisitOrderDetailResponse childData : data.getOrderDetail()) {
                                            db.addOrderDetail(data.getOrderHeader().getId(), childData);
                                        }
                                    }

                                    if (data.getListFreeGoods() != null) {
                                        ArrayList<FreeGoods> mData = new ArrayList<>(Arrays.asList(data.getListFreeGoods()));
                                        if (!mData.isEmpty()) {
                                            for (FreeGoods freeGoods : mData) {
                                                if (freeGoods != null) {
                                                    if (freeGoods.getListOptionFreeGoods() != null) {
                                                        ArrayList<ArrayList<OptionFreeGoods>> mDataOFG = new ArrayList<>(Arrays.asList(freeGoods.getListOptionFreeGoods()));
                                                        db.addFreeGoodsWODrop(data.getOrderHeader().getId(), mDataOFG);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                            if (msg.getResult().getListToPrice() != null && !msg.getResult().getListToPrice().isEmpty()) {
                                db.deleteToPrice();
                                for (ToPrice data : msg.getResult().getListToPrice()) {
                                    db.addToPrice(data);
                                }
                            }

                            if (listTempPendingToPrice != null && !listTempPendingToPrice.isEmpty()) {
                                for (ToPrice price : listTempPendingToPrice) {
                                    if (price != null) {
                                        db.addToPrice(price);
                                    }
                                }
                            }
                        }
                        progress.dismiss();
                        Toast.makeText(getActivity(), SYNC_SUCCESS, Toast.LENGTH_LONG).show();
                    } else {
                        progress.dismiss();
                        Toast.makeText(getActivity(), SYNC_FAILED, Toast.LENGTH_LONG).show();
                        if (msg.getResult() != null) {
                            if (msg.getResult().getListIdOrderFailed() != null) {
                                for (String idOrderFailed : msg.getResult().getListIdOrderFailed()) {
                                    String idFailed = Helper.splitIdFailed(idOrderFailed, 0);
                                }
                            }
                        }
                    }
                }
            } else if (PARAM == 11) {
                progress.dismiss();
                if (msg != null) {
                    if (msg.getIdMessage() == 1) {
                        /*Return*/
                        if (msg.getResult().getListReturnHeader() != null && !msg.getResult().getListReturnHeader().isEmpty()) {
                            db.deleteReturnHeader();
                            for (ReturnResponse data : msg.getResult().getListReturnHeader()) {
                                db.addReturnHeader(data);
                            }
                        }


                        if (msg.getResult().getListReturnDetail() != null && !msg.getResult().getListReturnDetail().isEmpty()) {
                            db.deleteReturnDetail();
                            for (Return data : msg.getResult().getListReturnDetail()) {
                                if (data.getReason() != null) {
                                    String idReason = data.getReason();
                                    data.setReason(db.getReasonById(idReason).getDesc());
                                    data.setCategory(db.getReasonById(idReason).getType());
                                }
                                db.addReturnDetail(data);
                            }
                        }

                        Toast.makeText(getActivity(), SYNC_SUCCESS, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), SYNC_FAILED, Toast.LENGTH_LONG).show();
                    }
                }
//                dismissAct();
            } else {
                progress.dismiss();
                if (msg != null) {
                    if (msg.getIdMessage() == 1) {
                        /*newest6Nov*/
                        db.deleteAttendance();

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), msg.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.errCon), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getListSyncOrderPlan() {
        ArrayList<OrderPlanHeader> listOrderPlan = db.getListOrderPlanHeader();

        if (listOrderPlan != null && !listOrderPlan.isEmpty()) {
            listInsertOrderPlan = new ArrayList<>();
            listUpdateOrderPlan = new ArrayList<>();
            listDeleteOrderPlan = new ArrayList<>();

            for (OrderPlanHeader orderPlan : listOrderPlan) {
                if (user != null) {
                    if (user.getIdEmployee() != null) {
                        orderPlan.setIdSalesman(user.getIdEmployee());
                    }
                }

                if (orderPlan.getQty2() != null) {
                    if (orderPlan.getQty2().equals("0") || orderPlan.getQty2().equals("0.0")) {
                        orderPlan.setQty2(null);
                        orderPlan.setUom2(null);
                    }
                } else {
                    orderPlan.setUom2(null);
                }

                if (orderPlan.getId() != null) {
                    if (orderPlan.getId().contains(Constants.ID_TRULY_OP)) {
                        if (orderPlan.getDeleted() != null) {
                            if (orderPlan.getDeleted().equals(getResources().getString(R.string.STATUS_TRUE))) {
                                listDeleteOrderPlan.add(orderPlan);
                            } else {
                                listUpdateOrderPlan.add(orderPlan);
                            }
                        } else {
                            listUpdateOrderPlan.add(orderPlan);
                        }
                    } else {
                        listInsertOrderPlan.add(orderPlan);
                    }
                }
            }
        }
    }

    class databaseFunction implements Runnable {

        // Runs the code for this task
        public void run() {
            if (offlineData.getListMaterial() != null && !offlineData.getListMaterial().isEmpty()) {
                db.addMaterialN(offlineData.getListMaterial());
            }

            /*Table UOM*/
            if (offlineData.getListUOm() != null && !offlineData.getListUOm().isEmpty()) {
                db.addUOMN(offlineData.getListUOm());
            }

            if (offlineData.getListMasterUom() != null && !offlineData.getListMasterUom().isEmpty()) {
                db.addMasterUomN(offlineData.getListMasterUom());
            }

            /*Table Outlet*/
            db.deleteOutlet();
            if (offlineData.getListOutlet() != null && !offlineData.getListOutlet().isEmpty()) {
                Collections.sort(offlineData.getListOutlet(), new Comparator<OutletResponse>() {
                    @Override
                    public int compare(OutletResponse s1, OutletResponse s2) {
                        return Helper.ltrim(Helper.validateResponseEmpty(s1.getOutletName())).compareToIgnoreCase(Helper.ltrim(Helper.validateResponseEmpty(s2.getOutletName())));
                    }
                });

                for (OutletResponse outlet : offlineData.getListOutlet()) {
                    outlet.setEnabled(true);
                    db.addOutletNew(outlet);
                }
            }

            /*Table TOP*/
            db.deleteTOP();
            if (offlineData.getListTop() != null && !offlineData.getListTop().isEmpty()) {
                db.addTOPN(offlineData.getListTop());
            }
            /*Table Jenis Jual*/
            db.deleteJenisJual();
            if (offlineData.getListJenisJual() != null && !offlineData.getListJenisJual().isEmpty()) {
                for (JenisJualandTop jjt : offlineData.getListJenisJual()) {
                    db.addJenisJual(jjt);
                }
            }

            /*Table Sales Office*/
            if (offlineData.getListSalesOffice() != null && !offlineData.getListSalesOffice().isEmpty()) {
                db.addSalesOfficeN(offlineData.getListSalesOffice());
            }

            if (offlineData.getListPartner() != null && !offlineData.getListPartner().isEmpty()) {
                db.addPartnerN(offlineData.getListPartner());
            }


            db.deleteOrderPlan();
            if (offlineData.getListOrderPlan() != null && !offlineData.getListOrderPlan().isEmpty()) {
                db.addOrderPlanN(offlineData.getListOrderPlan());
            }

            if (listDeleteOrderPlan != null && !listDeleteOrderPlan.isEmpty()) {
                for (int i = 0; i < listDeleteOrderPlan.size(); i++) {
                    listDeleteOrderPlan.get(i).setStatusOrder(getString(R.string.STATUS_FALSE));
                    db.updateStatusOrderPlanNew(listDeleteOrderPlan.get(i));

                    listDeleteOrderPlan.get(i).setDeleted(getResources().getString(R.string.STATUS_TRUE_SYNCED));
                    db.updateOrderPlanDeletedById(listDeleteOrderPlan.get(i));
                }
            }


            db.deleteReason();
            if (offlineData.getListReasonReturn() != null && !offlineData.getListReasonReturn().isEmpty()) {
                for (Reason reason : offlineData.getListReasonReturn()) {
                    db.addReason(reason);
                }
            }

            /*FreeGoods*/
            db.addFreeGoods(offlineData.getListFreeGoods());

            if (offlineData.getDatetimeNow() != null) {
                Date curDate = Helper.convertStringtoDate(Constants.DATE_TYPE_16, offlineData.getDatetimeNow());
                Long elapse = SystemClock.elapsedRealtime();

                OffDate offDate = new OffDate();
                offDate.setCurDate(offlineData.getDatetimeNow());
                offDate.setElapseTime(elapse);

                SecureDate.getInstance().initServerDate(curDate);
                db.deleteAttendance();

                user.setDateTimeNow(offlineData.getDatetimeNow());
                user.setmElapsedRealTime(SystemClock.elapsedRealtime());
                db.addAttendance(user);

                if (offlineData.getAdditionalInfo() != null) {
                    if (offlineData.getAdditionalInfo().getIdPlant() != null) {
                        db.updateIdPlantUser(user.getIdEmployee(), offlineData.getAdditionalInfo().getIdPlant());
                    }
                }

                String ex = Helper.objectToString(offDate);
                new SessionManager(getContext()).createDateSession(ex);

            }

            /*VisitPlan*/
            if (offlineData.getListVisitPlan() != null && !offlineData.getListVisitPlan().isEmpty()) {
                db.deleteVisitPlan();
                db.addVisitPlanN(offlineData.getListVisitPlan());
                for (OutletResponse visitPlan : offlineData.getListVisitPlan()) {
                    if (visitPlan.getVisitDate().equals(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()))) {

                        /*StoreCheck*/
                        if (offlineData.getListStore() != null && !offlineData.getListStore().isEmpty()) {
                            for (MaterialResponse storeCheck : offlineData.getListStore()) {
                                if (storeCheck.getId_mobile() != null) {
                                    //TODO terakhir cek disini
                                    if (listStoreCheck != null && !listStoreCheck.isEmpty()) {
                                        for (MaterialResponse materialStoreCheck : listStoreCheck) {
                                            if (materialStoreCheck.getId_store_check() != null) {
                                                if (materialStoreCheck.getId_store_check().equals(storeCheck.getId_mobile())) {
                                                    db.updateStoreCheck(storeCheck);
                                                }
                                            }
                                        }
                                    } else {
                                        db.addStoreCheck(storeCheck);
                                    }
                                } else {
                                    db.addStoreCheck(storeCheck);
                                }
                            }
                        }


                        /*Order*/
                        if (saveOrder) {
                            /*Test dulu*/
                            db.deleteToHeader();
                            if (offlineData.getListOrderHeader() != null && !offlineData.getListOrderHeader().isEmpty()) {
                                for (VisitOrderHeader data : offlineData.getListOrderHeader()) {
                                    if (data.getSalesOffice() != null) {
                                        data.setSalesOfficeName(db.getSalesOfficeName(data.getSalesOffice()));
                                    }
                                    if (data.getSignature() != null) {
                                        data.setSignatureString(data.getSignature());
                                    }
                                    if (data.getPhoto() != null) {
                                        data.setPhotoString(data.getPhoto());
                                    }
                                    db.addOrderHeader(data);
                                }
                            }

                            db.deleteOrderDetail();
                            if (offlineData.getListOrderDetail() != null && !offlineData.getListOrderDetail().isEmpty()) {
                                for (VisitOrderDetailResponse data : offlineData.getListOrderDetail()) {
                                    db.addOrderDetail(data);
                                }
                            }

                            /*set list Order yang masih kepending untuk di sync*/
                            if (listTempPendingOrder != null && !listTempPendingOrder.isEmpty()) {
                                for (VisitOrderRequest data : listTempPendingOrder) {
                                    if (data.getOrderHeader() != null) {
                                        db.addOrderHeader(data.getOrderHeader());
                                    }

                                    if (data.getOrderDetail() != null && !data.getOrderDetail().isEmpty()) {
                                        for (VisitOrderDetailResponse childData : data.getOrderDetail()) {
                                            db.addOrderDetail(data.getOrderHeader().getId(), childData);
                                        }
                                    }

                                    if (data.getListFreeGoods() != null) {

                                        ArrayList<FreeGoods> mData = new ArrayList<>(Arrays.asList(data.getListFreeGoods()));
                                        if (!mData.isEmpty()) {
                                            for (FreeGoods freeGoods : mData) {
                                                if (freeGoods != null) {
                                                    if (freeGoods.getListOptionFreeGoods() != null) {
                                                        ArrayList<ArrayList<OptionFreeGoods>> mDataOFG = new ArrayList<>(Arrays.asList(freeGoods.getListOptionFreeGoods()));
                                                        db.addFreeGoodsWODrop(data.getOrderHeader().getId(), mDataOFG);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            db.deleteToPrice();
                            if (offlineData.getListToPrice() != null && !offlineData.getListToPrice().isEmpty()) {
                                for (ToPrice data : offlineData.getListToPrice()) {
                                    db.addToPrice(data);
                                }
                            }
                        }

                        if (listTempPendingToPrice != null && !listTempPendingToPrice.isEmpty()) {
                            for (ToPrice price : listTempPendingToPrice) {
                                if (price != null) {
                                    db.addToPrice(price);
                                }
                            }
                        }
                        /*Return*/
                        db.deleteReturnHeader();
                        for (ReturnResponse data : offlineData.getListReturnHeader()) {
                            db.addReturnHeader(data);
                        }

                        db.deleteReturnDetail();
                        for (Return data : offlineData.getListReturnDetail()) {
                            if (data.getReason() != null) {
                                String idReason = data.getReason();
                                data.setReason(db.getReasonById(idReason).getDesc());
                                data.setCategory(db.getReasonById(idReason).getType());
                            }
                            db.addReturnDetail(data);
                        }
                    }
                }
            }

            progress.dismiss();
            Toast.makeText(getActivity(), MSG_SYNC_ALL, Toast.LENGTH_LONG).show();
//            dismissAct();
//            return null;
        }
    }

    private void getListSyncVisitPlan() {
        if (user != null) {
            if (user.getCurrentDate() != null) {
                ArrayList<OutletResponse> listVisitPlan = db.getAllVisitPlan(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));

                listSaveNewVisit = new ArrayList<>();
                listUpdateVisitSalesman = new ArrayList<>();
                listSaveVisitSalesman = new ArrayList<>();

                if (listVisitPlan != null && !listVisitPlan.isEmpty()) {
                    for (OutletResponse visitPlan : listVisitPlan) {
                        if (visitPlan.getId().contains(Constants.ID_VP_MOBILE)) {
                            listSaveNewVisit.add(visitPlan);
                        }

                        if (visitPlan.getCheckInTime() != null || visitPlan.getPause_time() != null ||
                                visitPlan.getContinue_time() != null || visitPlan.getCheck_out_time() != null) {

                            if (visitPlan.getCheckInTime() != null) {
                                visitPlan.setCheckInString(CalendarUtils.ConvertMilliSecondsToFormattedDate(visitPlan.getCheckInTime()));
                            }
                            if (visitPlan.getPause_time() != null) {
                                visitPlan.setPauseString(CalendarUtils.ConvertMilliSecondsToFormattedDate(visitPlan.getPause_time()));
                            }
                            if (visitPlan.getContinue_time() != null) {
                                visitPlan.setContinueString(CalendarUtils.ConvertMilliSecondsToFormattedDate(visitPlan.getContinue_time()));
                            }
                            if (visitPlan.getCheck_out_time() != null) {
                                visitPlan.setCheckOutString(CalendarUtils.ConvertMilliSecondsToFormattedDate(visitPlan.getCheck_out_time()));
                            }

                            if (visitPlan.getCheckInTime() != null) {
                                if (visitPlan.getIdVisitSalesman() != null) {
                                    listUpdateVisitSalesman.add(visitPlan);
                                } else {
                                    listSaveVisitSalesman.add(visitPlan);
                                }
                            }
                        }
                    }
                }
            }
        }

        listDeleteNewVisit = new ArrayList<>();
        if (Helper.getItemParam(Constants.LIST_DELETE_NEW_VISIT) != null) {
            listDeleteNewVisit = (ArrayList<OutletResponse>) Helper.getItemParam(Constants.LIST_DELETE_NEW_VISIT);
            Helper.removeItemParam(Constants.LIST_DELETE_NEW_VISIT);
        }
    }

    private void getListSyncStoreCheck() {
        listStoreCheck = db.getListStoreCheck();
        listInsertSC = new ArrayList<>();
        listUpdateSC = new ArrayList<>();
        listDeleteSC = new ArrayList<>();
        if (listStoreCheck != null && !listStoreCheck.isEmpty()) {
            for (MaterialResponse storeCheck : listStoreCheck) {
                if (storeCheck.getQty2() != null) {
                    if (storeCheck.getQty2().equals("0") || storeCheck.getQty2().equals("0.0")) {
                        storeCheck.setQty2(null);
                        storeCheck.setUom2(null);
                    }
                } else {
                    storeCheck.setUom2(null);
                }

                if (storeCheck.getId_store_check() != null) {
                    if (storeCheck.getId_store_check().contains(Constants.ID_ORI_STORE_CHECK)) {
                        if (storeCheck.isDeleted() != null) {
                            if (storeCheck.isDeleted()) {
                                listDeleteSC.add(storeCheck);
                            } else {
                                listUpdateSC.add(storeCheck);
                            }
                        } else {
                            listUpdateSC.add(storeCheck);
                        }
                    } else {
                        listInsertSC.add(storeCheck);
                    }
                } else {
                    listInsertSC.add(storeCheck);
                }
            }
        }
    }

    private void getListSyncOrder() {
        listSaveOrder = new ArrayList<>();
        listTempPendingOrder = new ArrayList<>();
        listTempPendingToPrice = new ArrayList<>();
        ArrayList<VisitOrderHeader> listOrderHeader;
        ArrayList<VisitOrderDetailResponse> listOrderDetail;
        ArrayList<ToPrice> listOneTimeDisc = new ArrayList<>();
        ArrayList<FreeGoods> listFreeGoods;
        ArrayList<OptionFreeGoods> listSelectionFreeGoods;
        ToPrice data;
        FreeGoods[] fgArray = new FreeGoods[0];
        listOrderHeader = db.getAllOrderHeader();

        if (listOrderHeader != null && !listOrderHeader.isEmpty()) {
            for (VisitOrderHeader orderHeader : listOrderHeader) {
                fgArray = new FreeGoods[0];
                listOneTimeDisc = new ArrayList<>();
                if (orderHeader.getId() != null) {
                    listOrderDetail = db.getListOrderDetail(orderHeader.getId());
                    for (VisitOrderDetailResponse temp : listOrderDetail) {
                        if (temp.getId_price() != null) {
                            data = db.getOneTimeDiscByIdPrice(temp.getId_price());
                            if (data.getTable_name() != null) {
                                listOneTimeDisc.add(data);
                            }
                        }
                    }

                    listFreeGoods = db.getFreeGoodsByIdFG(orderHeader.getId());

                    if (listFreeGoods != null && !listFreeGoods.isEmpty()) {
                        for (FreeGoods freeGoods : listFreeGoods) {
                            if (orderHeader.getId() != null &&
                                    freeGoods.getId() != null) {
                                listSelectionFreeGoods = db.getOptionFreeGoodsByIdFG(orderHeader.getId(), freeGoods.getId());
                                freeGoods.setListSelectedOptionFreeGoods(listSelectionFreeGoods);
                            }
                        }
//                        fgArray = new FreeGoods[0];
                        if (!listFreeGoods.isEmpty()) {
                            fgArray = listFreeGoods.toArray(new FreeGoods[listFreeGoods.size()]);
                        }
                    }

                    VisitOrderRequest order = new VisitOrderRequest();
                    order.setOrderHeader(orderHeader);
                    order.setOrderDetail(listOrderDetail);
                    order.setListOneTimeDiscount(listOneTimeDisc);
                    order.setListFreeGoods(fgArray);

                    if (orderHeader.getId() != null && orderHeader.getStatusPrice() != null) {
                        if (!orderHeader.getId().contains(Constants.KEY_TRUE_ID_TO)
                                && orderHeader.getStatusPrice().equals(getResources().getString(R.string.status_price_available))) {
                            listSaveOrder.add(order);
                        } else {
                            listTempPendingOrder.add(order);
                            if (order.getOrderDetail() != null && !order.getOrderDetail().isEmpty()) {
                                for (VisitOrderDetailResponse orderDetail : order.getOrderDetail()) {
                                    if (orderDetail.getId_price() != null) {
                                        try {
                                            listTempPendingToPrice.addAll(db.getToPricewithIdPrice(orderDetail.getId_price()));
                                        } catch (NullPointerException ignored) {

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void getListSyncReturn() {
        ArrayList<ReturnRequest> listReturnHeader = db.getAllListReturnHeader();
        if (listReturnHeader != null && !listReturnHeader.isEmpty()) {
            for (ReturnRequest returnR : listReturnHeader) {
                if (returnR.getIdHeader() != null) {
                    ArrayList<Return> listSaveReturn = db.getListReturnDetail(returnR.getIdHeader());
                    if (listSaveReturn != null && !listSaveReturn.isEmpty()) {
                        for (Return savedReturn : listSaveReturn) {
                            if (savedReturn.getReason() != null) {
                                savedReturn.setReason(db.getIdReason(savedReturn.getReason()));
                            }
                        }
                        returnR.setReturnDetail(listSaveReturn);
                    }
                }
            }
        }

        listReturnForSync = new ArrayList<>();
        if (listReturnHeader != null && !listReturnHeader.isEmpty()) {
            for (ReturnRequest returnHeader : listReturnHeader) {
                if (returnHeader.getIdHeader() != null) {
                    if (!returnHeader.getIdHeader().contains(Constants.ID_TRULY_RETURN)) {
                        listReturnForSync.add(returnHeader);
                    }
                } else {
                    listReturnForSync.add(returnHeader);
                }
            }
        }
    }
}
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
import id.co.qualitas.qubes.model.WSMessage;
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
        initProgress();
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
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if(PARAM == 1){
                    return null;
                }
                return null;
            } catch (Exception ex) {
                //connection = true;
                if (ex.getMessage() != null) {
                    Log.e("Sync", ex.getMessage());
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
        protected void onPostExecute(WSMessage msg) {
        }
    }
}
package id.co.qualitas.qubes.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.database.SecondDatabaseHelper;
import id.co.qualitas.qubes.fragment.CalendarFragment;
import id.co.qualitas.qubes.fragment.FragmentDrawer;
import id.co.qualitas.qubes.fragment.ImageDetailFragment;
import id.co.qualitas.qubes.fragment.OrderPlanFragment;
import id.co.qualitas.qubes.fragment.ProfileFragment;
import id.co.qualitas.qubes.fragment.PromotionFragment;
import id.co.qualitas.qubes.fragment.RetailOutletFragment;
import id.co.qualitas.qubes.fragment.SummaryFragment;
import id.co.qualitas.qubes.fragment.TargetFragment;
import id.co.qualitas.qubes.helper.CalendarUtils;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.FreeGoods;
import id.co.qualitas.qubes.model.GCMResponse;
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
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;
import id.co.qualitas.qubes.model.VisitOrderRequest;
import id.co.qualitas.qubes.session.SessionManager;

public class MainActivityDrawer extends BaseActivity implements FragmentDrawer.FragmentDrawerListener {
    @SuppressLint("StaticFieldLeak")
    static Toolbar mToolbar;

    public DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;
    Handler mHandler;
    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private boolean mUserLearnedDrawer;
    public static int mCurrentSelectedPosition;
    private User user = new User();
    TextView userNumber, name, appVersion;
    ImageView tempImgView;
    private RelativeLayout trigger;

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

    private OfflineLoginData offlineData = new OfflineLoginData();

    private boolean saveOrder = false;

    // used to store app title
    private CharSequence mTitle;

    private Button btnLogout;

    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;

    private int PARAM_VISIT = 0;

    private static final String SYNC_SUCCESS = "Sync succeed";
    private static final String SYNC_FAILED = "Sync failed";

    private String MSG_SYNC_ALL = "";

    private SecondDatabaseHelper sdb;
    public int positionNow = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_slide);
        mToolbar = findViewById(R.id.tool_bar);
        initialize();
        initProgress();

        setData();
        initToolbar();

        setSession();

        //setting drawer
        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER));
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        setUpNavDrawer();

        mTitle = getTitle();

        displayView(0);

        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        try {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = LayoutInflater.from(MainActivityDrawer.this);
                    View dialogview;
                    final Dialog alertDialog = new Dialog(MainActivityDrawer.this);
                    dialogview = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.setContentView(dialogview);
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

                            new RequestUrl2().execute();

                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();

                }
            });
        } catch (RuntimeException ignored) {

        }

        SharedPreferences permissionStatus = getSharedPreferences(getString(R.string.permission_status), MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(permissionsRequired[0])
                        || shouldShowRequestPermissionRationale(permissionsRequired[1])
                        || shouldShowRequestPermissionRationale(permissionsRequired[2])
                        || shouldShowRequestPermissionRationale(permissionsRequired[3])) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityDrawer.this);
                    builder.setTitle(R.string.need_multiple_permissions);
                    builder.setCancelable(false);
                    builder.setMessage(R.string.need_gallery_and_location_permission);
                    builder.setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Toast.makeText(getBaseContext(), R.string.unable_to_get_permission, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityDrawer.this);
                    builder.setTitle(R.string.need_multiple_permissions);
                    builder.setCancelable(false);
                    builder.setMessage(R.string.need_gallery_and_location_permission);
                    builder.setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getBaseContext(), R.string.go_to_permisson_to_grant_gallery_and_location, Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Toast.makeText(getBaseContext(), R.string.unable_to_get_permission, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    builder.show();
                } else {
                    //just request the permission
                    requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }

                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0], true);
                editor.commit();
            } else {
                //You already have the permission, just go ahead.
                proceedAfterPermission();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(permissionsRequired[0])
                        || shouldShowRequestPermissionRationale(permissionsRequired[1])
                        || shouldShowRequestPermissionRationale(permissionsRequired[2])
                        || shouldShowRequestPermissionRationale(permissionsRequired[3])) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityDrawer.this);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setCancelable(false);
                    builder.setMessage("This app needs Gallery and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.show();
                } else {
                    finish();
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                    //Got Permission
                    proceedAfterPermission();
                }
            }
        }
    }

    private void proceedAfterPermission() {
//        Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                    //Got Permission
                    proceedAfterPermission();
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RequestUrl2 extends AsyncTask<Void, Void, MessageResponse> {

        @Override
        protected MessageResponse doInBackground(Void... voids) {
            try {
                String URL_GET_HOME_LIST = Constants.API_LOGOUT.concat(Constants.QUESTION).concat(Constants.USERNAME.concat(Constants.EQUALS))
                        + user.getNik().concat(getString(R.string.clientId));

                final String url = Constants.URL.concat(URL_GET_HOME_LIST);
                return (MessageResponse) Helper.getWebservice(url, MessageResponse.class);

            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("LoginActivity", ex.getMessage());
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
        protected void onPostExecute(MessageResponse messageResponse) {
            progress.dismiss();
            if (messageResponse != null) {
                if (messageResponse.getIdMessage() == 1) {

                    SessionManager sessionManager = new SessionManager(MainActivityDrawer.this);
                    sessionManager.logoutUser();

                    db.deleteOrderPlan();
                    db.deleteMaterial();

                    /*newest6Nov*/
                    db.deleteAttendance();

                    Intent intent = new Intent(MainActivityDrawer.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivityDrawer.this, messageResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivityDrawer.this, getString(R.string.errCon), Toast.LENGTH_LONG).show();
            }
        }

    }

    public void initialize() {
        db = new DatabaseHelper(getApplicationContext());
        mHandler = new Handler();

        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        tempImgView = findViewById(R.id.imgMenu);
        userNumber = findViewById(R.id.txtUserNumber);
        name = findViewById(R.id.txtName);
        trigger = findViewById(R.id.trigger);
        appVersion = findViewById(R.id.appVersion);

        btnLogout = findViewById(R.id.btnLogout);

        setMenuDrawer();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
    }

    public void setMenuDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        FragmentDrawer drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
        drawerFragment.setDrawerListener(this);
    }

    public void displayView(int position) {
        Fragment fragment = null;
        if (position == 0) {
            positionNow = position;
            setTitle(getString(R.string.navmenu1));
            fragment = new CalendarFragment();
            setContent(fragment);
        } else if (position == 1) {
            positionNow = position;
            Helper.setItemParam(Constants.ACTIVE_MENU, getResources().getString(R.string.navmenu2));
            setTitle(getString(R.string.navmenu2));
            fragment = new TargetFragment();
            setContent(fragment);
        } else if (position == 2) {
            positionNow = position;
            if (user != null) {
                if (user.getReason() == 2) {
                    setTitle(getString(R.string.navmenu3));
                    fragment = new OrderPlanFragment();
                    setContent(fragment);
                }
            }
        } else if (position == 3) {
            positionNow = position;
            Helper.setItemParam(Constants.ACTIVE_MENU, getResources().getString(R.string.navmenu4));
            Helper.removeItemParam(Constants.PROMOTION_ITEM_FILTER);
            setTitle(getString(R.string.navmenu4));
            fragment = new PromotionFragment();
            setContent(fragment);
        } else if (position == 4) {
            positionNow = position;
            setTitle(getString(R.string.navmenu5));
            fragment = new RetailOutletFragment();
            setContent(fragment);
        } else if (position == 5) {
            positionNow = position;
            setTitle(getString(R.string.navmenu6));
            fragment = new SummaryFragment();
            setContent(fragment);
        } else if (position == 6) {
            positionNow = position;
            setTitle(getString(R.string.navmenu7));
            fragment = new ProfileFragment();
            setContent(fragment);
        } else if (position == 7) {

            LayoutInflater inflater = LayoutInflater.from(MainActivityDrawer.this);
            View dialogview;
            final Dialog alertDialog = new Dialog(MainActivityDrawer.this);
            dialogview = inflater.inflate(R.layout.custom_dialog_sync_menu, null);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(dialogview);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        } else if (position == 8) {
            LayoutInflater inflater = LayoutInflater.from(MainActivityDrawer.this);
            View dialogview;
            final Dialog alertDialog = new Dialog(MainActivityDrawer.this);
            dialogview = inflater.inflate(R.layout.custom_dialog_log, null);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(dialogview);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//            TextView txtLastSync = alertDialog.findViewById(R.id.txtLastSync);
//            TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);
//            Button btnHide = alertDialog.findViewById(R.id.btnHide);

//            LastLog lLog = (LastLog) Helper.getItemParam(Constants.ERROR_LOG);
//            txtDialog.setText("Empty");
//            if (lLog != null) {
//                txtLastSync.setText(Helper.validateResponseEmpty(lLog.getLastSync()));
//                txtDialog.setText(Helper.validateResponseEmpty(lLog.getLastMsg()));
//            }

//            btnHide.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    alertDialog.dismiss();
//                }
//            });

            alertDialog.show();
        } else if (position == 9) {
            LayoutInflater inflater = LayoutInflater.from(MainActivityDrawer.this);
            View dialogview;
            final Dialog alertDialog = new Dialog(MainActivityDrawer.this);
            dialogview = inflater.inflate(R.layout.custom_dialog_reason, null);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(dialogview);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final EditText edtInput = alertDialog.findViewById(R.id.edtReason);
            TextView desc = alertDialog.findViewById(R.id.textView);
            Button btnNeg = alertDialog.findViewById(R.id.btnCancel);
            Button btnPos = alertDialog.findViewById(R.id.btnSave);
            btnNeg.setText("Hide");
            btnPos.setText("Save");
            edtInput.setHint("Input the new IP here..");
            desc.setText(getResources().getString(R.string.ipAddress));

            if (Helper.getItemParam(Constants.URL) != null) {
                String urltemp = Helper.getItemParam(Constants.URL).toString();
                String temp[] = urltemp.split("/");
                edtInput.setText(temp[2]);
            }


            btnPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ip = edtInput.getText().toString().trim();
                    if (ip.isEmpty() || ip == null) {
                        Snackbar.make(view, R.string.pleasefillipaddress, Snackbar.LENGTH_LONG).show();
                    } else {

                        LayoutInflater inflater = LayoutInflater.from((view.getRootView().getContext()));
                        final Dialog alertDialog = new Dialog((view.getRootView().getContext()));
                        View dialogview = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.setContentView(dialogview);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Button btnNo = (Button) dialogview.findViewById(R.id.btnCancel);
                        Button btnYes = (Button) dialogview.findViewById(R.id.btnSave);
                        TextView textDialog = (TextView) dialogview.findViewById(R.id.txtDialog);
                        textDialog.setText(R.string.changeipmessage);
                        btnNo.setText("No");
                        btnYes.setText("Yes");

                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Constants.URL = edtInput.getText().toString();
                                new SessionManager(getApplicationContext()).createUrlSession(Constants.URL);
                                Helper.setItemParam(Constants.URL, Constants.URL);
                                Toast.makeText(getApplicationContext(), R.string.ipaddress_has_been_changed, Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                }
            });

            btnNeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        }

        if (fragment != null) {
            intentFragment(fragment);
        }
    }

    public void intentFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_contentframe, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void setSession() {
        SessionManager session = new SessionManager(this);
        if (session.isUrlEmpty()) {
            Map<String, String> urlSession = session.getUrl();
            Constants.URL = urlSession.get(Constants.KEY_URL);
            Helper.setItemParam(Constants.URL, Constants.URL);
        } else {
            Helper.setItemParam(Constants.URL, Constants.URL);
        }
    }

    private void setData() {
        if (user != null) {
            name.setText(user.getFullName());
            userNumber.setText(user.getNik());
        }
        appVersion.setText(Constants.APP_VERSION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getFragmentManager();
        return true;
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue3)));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    public void enableBackToolbar(boolean enable) {

        if (enable) {
            mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
            Fragment fragmentInFrame = getSupportFragmentManager()
                    .findFragmentById(R.id.nav_contentframe);
            if (fragmentInFrame instanceof ImageDetailFragment) {
                mToolbar.setBackgroundColor(getResources().getColor(R.color.black));
            } else {
                mToolbar.setBackgroundColor(getResources().getColor(R.color.blue3));
            }
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_black_24dp));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    public void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mUserLearnedDrawer = true;
            saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, getResources().getString(R.string.STATUS_TRUE));
        }
    }

    public void setContent(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_contentframe, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, "false");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();

        db = new DatabaseHelper(getApplicationContext());
        GCMResponse gcm = db.getGCM();
        if (gcm.getDesc() != null) {
            String username = null;
            String[] parts = gcm.getUsername().split("[|]");
            username = parts[0];
            if (username.equals(user.getNik())) {
                //untuk hapus notification jika sudah masuk menu
                NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                assert notificationManager != null;
                notificationManager.cancel(1);
                db.deleteFCM();
                Intent intent = new Intent(MainActivityDrawer.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                assert notificationManager != null;
                notificationManager.cancel(1);
                db.deleteFCM();
            }
        }

        user = (User) Helper.getItemParam(Constants.USER_DETAIL);


        setData();

    }

    @Override
    protected void onPause() {
        if (user != null) {
            Helper.setItemParam(Constants.USER_DETAIL, user);
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
//            OrderFragment orderFragment = (OrderFragment) getSupportFragmentManager().findFragmentByTag("OrderFragment");
//            if (orderFragment != null && orderFragment.isVisible()) {
//                orderFragment.back();
//            } else {
            super.onBackPressed();
//            }
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
        FragmentDrawer.updateSelected(position);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    public static void setToolbarTransparant() {
        mToolbar.setVisibility(View.GONE);
    }

    @SuppressLint("StaticFieldLeak")
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
                } else {/*Return*/
                    getListSyncReturn();
                    offlineData.setIdSalesman(user.getIdEmployee());
                    offlineData.setListSaveReturn(listReturnForSync);

                    final String url = Constants.URL.concat(Constants.API_SYNC_RETURN);

                    return (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, offlineData);
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
//                        Toast.makeText(MainActivityDrawer.this, msg.getMessage(), Toast.LENGTH_LONG).show();
                        MSG_SYNC_ALL = SYNC_SUCCESS;
                        saveOrder = msg.getResult().isSaveOrder();
                    } else {
                        MSG_SYNC_ALL = SYNC_FAILED;
//                        Toast.makeText(MainActivityDrawer.this, msg.getMessage(), Toast.LENGTH_LONG).show();

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
                    Toast.makeText(getApplicationContext(), "Sync error", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }

            } else if (PARAM == 5) {
                if (offlineData != null) {
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
                        new SessionManager(getApplicationContext()).createDateSession(ex);

                    }

                    /*VisitPlan*/
                    if (offlineData.getListVisitPlan() != null && !offlineData.getListVisitPlan().isEmpty()) {
                        db.deleteVisitPlan();
                        db.addVisitPlanN(offlineData.getListVisitPlan());
                    }
                    Toast.makeText(MainActivityDrawer.this, SYNC_SUCCESS, Toast.LENGTH_LONG).show();
                    dismissAct();
                } else {
                    Toast.makeText(MainActivityDrawer.this, SYNC_FAILED, Toast.LENGTH_LONG).show();
                }
            } else if (PARAM == 7) {
                if (msg != null) {
                    if (msg.getIdMessage() == 1) {
                        if (msg.getResult() != null) {
                            if (msg.getResult().getListOrderPlan() != null && !msg.getResult().getListOrderPlan().isEmpty()) {

                                db.deleteOrderPlan();
                                db.addOrderPlanN(msg.getResult().getListOrderPlan());

                                Toast.makeText(MainActivityDrawer.this, SYNC_SUCCESS, Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(MainActivityDrawer.this, SYNC_FAILED, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivityDrawer.this, "Null Message", Toast.LENGTH_LONG).show();
                }
                dismissAct();
            } else if (PARAM == 8) {
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
                                        Toast.makeText(MainActivityDrawer.this, "SYNC VISIT SUCCESS", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }
                    } else {
                        Toast.makeText(MainActivityDrawer.this, "SYNC VISIT FAILED", Toast.LENGTH_LONG).show();
                    }
                }
                dismissAct();
            } else if (PARAM == 9) {
                if (msg != null) {
                    if (msg.getIdMessage() == 1) {
                        if (msg.getResult() != null) {
                            if (msg.getResult().getListStore() != null && !msg.getResult().getListStore().isEmpty()) {

                                db.deleteStoreCheck();
                                db.addStoreCheckN(msg.getResult().getListStore());

                                Toast.makeText(MainActivityDrawer.this, SYNC_SUCCESS, Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(MainActivityDrawer.this, SYNC_FAILED, Toast.LENGTH_LONG).show();
                    }
                }
                dismissAct();
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
                        Toast.makeText(MainActivityDrawer.this, msg.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivityDrawer.this, msg.getMessage(), Toast.LENGTH_LONG).show();


                        if (msg.getResult() != null) {
                            if (msg.getResult().getListIdOrderFailed() != null) {
                                for (String idOrderFailed : msg.getResult().getListIdOrderFailed()) {
                                    String idFailed = Helper.splitIdFailed(idOrderFailed, 0);
                                    //TODO: update idFailed di table to dengan splitIdFailed pos 1
                                }
                            }
                        }
                    }
                }
                dismissAct();
            } else {
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
                                    data.setReason(db.getReasonById(idReason).getDescription());
                                    data.setCategory(db.getReasonById(idReason).getType());
                                }
                                db.addReturnDetail(data);
                            }
                        }

                        Toast.makeText(MainActivityDrawer.this, SYNC_SUCCESS, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivityDrawer.this, SYNC_FAILED, Toast.LENGTH_LONG).show();
                    }
                }
                dismissAct();
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

    @SuppressWarnings("unchecked")
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
                new SessionManager(getApplicationContext()).createDateSession(ex);

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
                                data.setReason(db.getReasonById(idReason).getDescription());
                                data.setCategory(db.getReasonById(idReason).getType());
                            }
                            db.addReturnDetail(data);
                        }
                    }
                }
            }

            Toast.makeText(MainActivityDrawer.this, MSG_SYNC_ALL, Toast.LENGTH_LONG).show();
            dismissAct();


//            return null;
        }
    }

    private void dismissAct() {

        if (positionNow == 0) {

            Fragment fragment = new CalendarFragment();
            setContent(fragment);
        } else {

            try {
                getSupportFragmentManager().findFragmentById(R.id.nav_contentframe).onResume();
            } catch (Exception ignored) {
            }
        }

        progress.dismiss();
    }
}
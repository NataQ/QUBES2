package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.activity.CreateOrder2Fragment;
import id.co.qualitas.qubes.activity.CreateOrderDetail2Fragment;
import id.co.qualitas.qubes.activity.CreateReturn2Fragment;
import id.co.qualitas.qubes.activity.ImageDetail2Fragment;
import id.co.qualitas.qubes.activity.OrderSummary2Fragment;
import id.co.qualitas.qubes.activity.OrderSummaryDetail2Fragment;
import id.co.qualitas.qubes.activity.Return2Fragment;
import id.co.qualitas.qubes.activity.ReturnDetail2Fragment;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.ChangePasswordFragment;
import id.co.qualitas.qubes.fragment.CreditInfo2Fragment;
import id.co.qualitas.qubes.fragment.aspp.DirectionFragment;
import id.co.qualitas.qubes.fragment.aspp.HomeFragment;
import id.co.qualitas.qubes.fragment.Order2Fragment;
import id.co.qualitas.qubes.fragment.OrderPlanDetailFragmentV2;
import id.co.qualitas.qubes.fragment.OrderPlanSummaryFragmentV2;
import id.co.qualitas.qubes.fragment.Profile2Fragment;
import id.co.qualitas.qubes.fragment.StoreCheckFragment;
import id.co.qualitas.qubes.fragment.TargetDetailFragment;
import id.co.qualitas.qubes.fragment.Timer2Fragment;
import id.co.qualitas.qubes.fragment.aspp.AccountFragment;
import id.co.qualitas.qubes.fragment.aspp.ActivityFragment;
import id.co.qualitas.qubes.fragment.aspp.CoverageFragment;
import id.co.qualitas.qubes.fragment.aspp.RouteCustomerFragment;
import id.co.qualitas.qubes.fragment.aspp.SummaryFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.session.SessionManager;

public class NewMainActivity extends BaseActivity {
    String currentpage;
    private String page = "0";
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private ImageView imgLogOut;
    private boolean sentToSettings = false;
    // Tracks the bound state of the service.
//    private boolean mBound = false;
//    // The BroadcastReceiver used to listen from broadcasts from the service.
//    private MyReceiver myReceiver;
//    private static LocationUpdatesService mServiceFusedLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.new_activity_main);
        initialize();
        setSession();
//        myReceiver = new MyReceiver();

        SharedPreferences permissionStatus = getSharedPreferences(getString(R.string.permission_status), MODE_PRIVATE);

        imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut(NewMainActivity.this);
            }
        });
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewMainActivity.this);
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

    public void setSession() {
        SessionManager session = new SessionManager(this);
        if (session.isUrlEmpty()) {
            Map<String, String> urlSession = session.getUrl();
            Constants.IP = urlSession.get(Constants.KEY_URL);
            Constants.URL = Constants.IP;
            Helper.setItemParam(Constants.URL, Constants.URL);
        } else {
            Constants.IP = Constants.URL;
            Constants.URL = Constants.IP;
            Helper.setItemParam(Constants.URL, Constants.URL);
        }
    }

    private void initialize() {
        imgLogOut = findViewById(R.id.imgLogOut);
        bottomNavigationView = findViewById(R.id.navigation);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                currentpage = (String) Helper.getItemParam(Constants.CURRENTPAGE);
                if (currentpage == null) {
                    currentpage = "0";
                }
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (!currentpage.equals("1")) {
                            Helper.setItemParam(Constants.CURRENTPAGE, "1");
                            fragment = new HomeFragment();
                            setContent(fragment);
                        }
                        return true;
                    case R.id.navigation_route_customer:
                        if (!currentpage.equals("2")) {
                            Helper.setItemParam(Constants.CURRENTPAGE, "2");
                            fragment = new RouteCustomerFragment();
                            setContent(fragment);
                        }
                        return true;
                    case R.id.navigation_activity:
                        if (!currentpage.equals("3")) {
                            Helper.setItemParam(Constants.CURRENTPAGE, "3");
                            fragment = new ActivityFragment();
                            setContent(fragment);
                        }
                        return true;
                    case R.id.navigation_summary:
                        if (!currentpage.equals("4")) {
                            Helper.setItemParam(Constants.CURRENTPAGE, "4");
                            fragment = new SummaryFragment();
                            setContent(fragment);
                        }
                        return true;
                    case R.id.navigation_account:
                        if (!currentpage.equals("5")) {
                            Helper.setItemParam(Constants.CURRENTPAGE, "5");
                            fragment = new AccountFragment();
                            setContent(fragment);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    public void setContent(Fragment fragment) {
        fm = this.getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void changePage(int page) {
        currentpage = (String) Helper.getItemParam(Constants.CURRENTPAGE);
        if (currentpage == null) {
            currentpage = "0";
        }
        switch (page) {
            case 1:
                if (!currentpage.equals("1")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "1");
                    fragment = new HomeFragment();
                    setContent(fragment);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                }
                break;
            case 2:
                if (!currentpage.equals("2")) {
//                    if (getMenu(124) || getMenu(127)) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "2");
//                    fragment = new OrderPlanFragment();
                    fragment = new RouteCustomerFragment();
                    setContent(fragment);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_route_customer);
//                    } else {
//                        setToast("You don't have access to this menu");
//                    }
                }
                break;
            case 3:
                if (!currentpage.equals("3")) {
//                    if (getMenu(88) || getMenu(92)) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "3");
//                    fragment = new NewVisitHomeFragment();
                    fragment = new ActivityFragment();
                    setContent(fragment);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_activity);
//                    }
                }
                break;
            case 4:
                if (!currentpage.equals("4")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "4");
                    fragment = new SummaryFragment();
                    setContent(fragment);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_summary);
                }
                break;
            case 5:
                if (!currentpage.equals("5")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "5");
                    fragment = new AccountFragment();
                    setContent(fragment);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_account);
                }
                break;
            case 6:
                if (!currentpage.equals("6")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "6");
                    fragment = new Profile2Fragment();
                    setContent(fragment);
                }
                break;
            case 7:
                if (!currentpage.equals("7")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "7");
                    fragment = new ChangePasswordFragment();
                    setContent(fragment);
                }
                break;
            case 8:
                if (!currentpage.equals("8")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "8");
                    fragment = new Timer2Fragment();
                    setContent(fragment);
                }
                break;
            case 9:
                if (!currentpage.equals("9")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "9");
                    fragment = new CreditInfo2Fragment();
                    setContent(fragment);
                }
                break;
            case 10:
                if (!currentpage.equals("10")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "10");
                    fragment = new StoreCheckFragment();
                    setContent(fragment);
                }
                break;
            case 11:
                if (!currentpage.equals("11")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "11");
                    fragment = new Order2Fragment();
                    setContent(fragment);
                }
                break;
            case 12:
                if (!currentpage.equals("12")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "12");
                    fragment = new CreateOrder2Fragment();
                    setContent(fragment);
                }
                break;
            case 13:
                if (!currentpage.equals("13")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "13");
                    fragment = new CreateOrderDetail2Fragment();
                    setContent(fragment);
                }
                break;
            case 14:
                if (!currentpage.equals("14")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "14");
                    fragment = new OrderSummary2Fragment();
                    setContent(fragment);
                }
                break;
            case 15:
                if (!currentpage.equals("15")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "15");
                    fragment = new OrderSummaryDetail2Fragment();
                    setContent(fragment);
                }
                break;
            case 16:
                if (!currentpage.equals("16")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "16");
                    fragment = new ImageDetail2Fragment();
                    setContent(fragment);
                }
                break;
            case 17:
                if (!currentpage.equals("17")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "17");
                    fragment = new Return2Fragment();
                    setContent(fragment);
                }
                break;
            case 18:
                if (!currentpage.equals("18")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "18");
                    fragment = new CreateReturn2Fragment();
                    setContent(fragment);
                }
                break;
            case 19:
                if (!currentpage.equals("19")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "19");
                    fragment = new ReturnDetail2Fragment();
                    setContent(fragment);
                }
                break;
            case 20:
                if (!currentpage.equals("20")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "20");
                    fragment = new OrderPlanDetailFragmentV2();
                    setContent(fragment);
                }
                break;
            case 21:
                if (!currentpage.equals("21")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "21");
                    fragment = new OrderPlanSummaryFragmentV2();
                    setContent(fragment);
                }
                break;
            case 22:
                if (!currentpage.equals("22")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "22");
                    fragment = new TargetDetailFragment();
                    setContent(fragment);
                }
                break;
            case 23:
                if (!currentpage.equals("23")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "23");
                    fragment = new CoverageFragment();
                    setContent(fragment);
                }
                break;
            case 24:
                if (!currentpage.equals("24")) {
                    Helper.setItemParam(Constants.CURRENTPAGE, "24");
                    fragment = new DirectionFragment();
                    setContent(fragment);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
//        registerReceiver(myReceiver, new IntentFilter(MyFirebaseMessagingService2.ACTION_BROADCAST));
        setPage();
    }

    private void setPage() {
        currentpage = (String) Helper.getItemParam(Constants.CURRENTPAGE);
        if (currentpage != null) {//&& session != null
            if (currentpage.equals(0)) {
                currentpage = "1";
            }
            changePage(Integer.parseInt(currentpage));
        } else {
            changePage(1);
        }
    }

    public void backPress() {
        if (doubleBackToExitPressedOnce) {
            Helper.removeItemParam(Constants.CURRENTPAGE);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        setToast("Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
//                return null;
            }
        }, 2000);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("page_helper", (String) Helper.getItemParam(Constants.CURRENTPAGE));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Helper.setItemParam(Constants.CURRENTPAGE, savedInstanceState.getString("page_helper"));
        init();
        setPage();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (!checkPermissions(false)) {
//            requestPermissions(false);
//        }
//
//        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
//
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        //enhancement tracking
//        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

//    private class MyReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String actionTracking = intent.getStringExtra(MyFirebaseMessagingService2.ACTION_TRACKING);
//            if (actionTracking.equals("start_tracking")) {
//                mServiceFusedLocation.requestLocationUpdates();
//            } else if (actionTracking.equals("stop_tracking")) {
//                mServiceFusedLocation.removeLocationUpdates();
//            }
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        //enhancement tracking
//        if (mBound) {
//            // Unbind from the service. This signals to the service that this activity is no longer
//            // in the foreground, and the service can respond by promoting itself to a foreground
//            // service.
//            unbindService(mServiceConnection);
//            mBound = false;
//        }
//        super.onStop();
//    }
//
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
//            mServiceFusedLocation = binder.getService();
//            mBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mServiceFusedLocation = null;
//            mBound = false;
//        }
//    };
//
//    @Override
//    protected void onPause() {
//        //enhancement tracking
//        unregisterReceiver(myReceiver);
//        super.onPause();
//    }
}

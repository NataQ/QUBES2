package id.co.qualitas.qubes.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.LoginActivity;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.database.SecondDatabaseHelper;
import id.co.qualitas.qubes.fragment.TimerFragment;
import id.co.qualitas.qubes.helper.GPSTracker;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.CheckInOutRequest;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.MaterialResponse;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.OfflineLoginData;
import id.co.qualitas.qubes.model.OrderPlanHeader;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;
import id.co.qualitas.qubes.model.VisitOrderRequest;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManager;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

//import android.support.multidex.MultiDex;

public class BaseActivity extends AppCompatActivity {
    protected WorkManager workManager;
    protected WorkRequest workRequest;
    protected LinearLayout llNoData;
    protected ProgressBar progressCircle;
    protected Context context;
    protected ProgressDialog progress;
    protected DatabaseHelper db;
    protected Database database;
    protected SessionManagerQubes sessionManagerQubes;
    protected SecondDatabaseHelper sdb;
    protected int PARAM = 0, FLAG = 0;
    protected SessionManager session;
    public BottomNavigationView bottomNavigationView;
    public boolean doubleBackToExitPressedOnce = false;
    protected SwipeRefreshLayout swipeLayout;
    protected TextView txtNoData, txtTitle;
    protected Intent intent;
    protected Fragment fragment;
    protected FragmentManager fragmentManager;
    protected FragmentTransaction ft;
    protected FragmentManager fm;
    protected GPSTracker gpsTracker;
    protected RecyclerView recyclerView;
    protected int PARAM_DIALOG_ALERT = 0;
    protected static String PARAM_STATUS_OUTLET = Constants.UNCHECKED_IN;
    protected ArrayList<User> attendances = new ArrayList<>();
    private Date curDate = new Date();
    protected User user;
    private OfflineLoginData offlineData;
    protected String idEmployee = Constants.EMPTY_STRING;
    private View dialogview;
    private LayoutInflater inflater;
    public static Dialog alertDialog;
    public int flagCode = 0, flagName = 0;
    protected Button btnSave, btnCancel, btnCheckIn, btnYes, btnNo, btnReset, btnBack;
    protected AutoCompleteTextView edtMaterialName, edtMaterialCode;
    public CheckInOutRequest checkInRequest, checkOutRequest, pauseRequest;
    protected ImageView imgBack, imgLogOut;
    protected TextView txtEmpty, txtDialog;
    protected EditText edtKlasifikasi, edtTxtComment;
    private ArrayList<String> listMaterialName, listMaterialCode;
    public ArrayList<OrderPlanHeader> orderPlanHeaderArrayList;
    protected int PARAM_MENU_SYNC = 0;
    private int[] go = {0};
    protected int flagAttachment = 0;
    private ArrayAdapter adapter, codeAdapter;
    OutletResponse outletResponse;
    protected ProgressBar progressBar;
    protected ArrayAdapter<String> spinnerAdapter;
    protected ArrayAdapter<Reason> spinnerReasonAdapter;
    protected LinearLayout linearLayoutList;
    protected VisitOrderHeader visitOrderHeader, orderHeaderDetail;
    private TextView txtImagePathAttachment;
    private ImageView imgAttachment, imgSignatureSaved;
    protected ImageView imgCameraPhoto, imgGalleryPhoto, imgCancelPhoto, imgAttachmentReturn, imgAttachedCost;
    protected Bitmap bitmapCamera, bitmapGallery, bitmap;
    private int PARAM_PHOTO = 0;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;
    protected TextView txtViewEmpty;
    private SignaturePad mSignaturePad;
    private ArrayList<VisitOrderHeader> orderHeaderList;
    private Bitmap bitmapPhoto, compressedBitmapGallery;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

    public ArrayAdapter<String> getSpinnerAdapter() {
        return spinnerAdapter;
    }

    public ArrayAdapter<Reason> getSpinnerReasonAdapter() {
        return spinnerReasonAdapter;
    }

    final protected static int DIALOG_REASON = 1;
    final protected static int DIALOG_ATTACH_PHOTO = 3;
    final protected static int DIALOG_SIGNATURE = 5;
    final protected static int DIALOG_ADD_MATERIAL = 11;
    final protected static int DIALOG_REASON_PAUSE = 14;
    final protected static int DIALOG_SPECIMEN = 19;
    final protected static int DIALOG_ALERT_CONFIRM = 20;

    public static Dialog getAlertDialog() {
        return alertDialog;
    }

    public ProgressDialog getProgress() {
        return progress;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new Database(getApplicationContext());
        Helper.trustSSL();
        initProgress();
        initBase();
        setFormatSeparator();
    }

    public void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }

    public void initBase() {
        if (SessionManagerQubes.getUserProfile() != null) {
            Helper.setItemParam(Constants.USER_DETAIL, SessionManagerQubes.getUserProfile());
            user = (User) Helper.getItemParam(Constants.USER_DETAIL);
            if (SessionManagerQubes.getUrl() != null) {
                Helper.setItemParam(Constants.URL, SessionManagerQubes.getUrl());
            }
            if (user == null) {
                setToast("Session telah habis. Silahkan login ulang.");
                clearAllSession();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                if (SessionManagerQubes.getToken() != null) {
                    Helper.setItemParam(Constants.TOKEN, SessionManagerQubes.getToken());
                }

                if (SessionManagerQubes.getUrl() != null) {
                    String ipAddress = SessionManagerQubes.getUrl();
                    Constants.URL = ipAddress;
                    Helper.setItemParam(Constants.URL, Constants.URL);
                }
            }
        } else {
            setToast("Session telah habis. Silahkan login ulang.");
            clearAllSession();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String currentDateTime = df.format(new Date());

        return currentDateTime;
    }

    public void showDialogInformation(Activity activity, String msg) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        final Dialog dialog = new Dialog(activity);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_information, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        txtMsg.setText(msg);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public String getOrderType(Customer outletHeader, User user) {
        String orderType;
        //outletHeader.getOrder_type() != null ? (outletHeader.getOrder_type().equals("CO") ? "Canvas Order" : "Taking Order") :
        orderType = user.getType_sales().equals("CO") ? "Canvas Order" : "Taking Order";
        return orderType;
    }

    public void logOut(Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        final Dialog dialog = new Dialog(activity);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_logout, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);
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
                if (database.getCountOfflineData() == 0) {
                    progress.show();
                    new requestLogOut().execute();
                } else {
                    setToast("Pastikan semua data offline sudah di sync");
                }
            }
        });

        dialog.show();
    }

    private class requestLogOut extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                String URL_ = Constants.API_LOG_OUT;
                final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                return (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, user);
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("logOut", ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage wsMessage) {
            progress.dismiss();
            if (wsMessage != null) {
                if (wsMessage.getIdMessage() == 1) {
                    clearAllSession();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    setToast(wsMessage.getMessage());
                }
            } else {
                setToast(getString(R.string.failedGetData));
            }
        }
    }

    private void clearAllSession() {
        SessionManagerQubes.clearAllSession();
        database.deleteAllDatabase();

        Helper.removeItemParam(Constants.FROM_VISIT);
        Helper.removeItemParam(Constants.CURRENTPAGE);
    }

    public void snackBar(View rootView, int message) {
        Snackbar snack = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    public void setSpinnerData(List<String> items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
//                text.setTextColor(getResources().getColor(R.color.grey));
                return view;
            }

        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        if (spinnerAdapter != null) {
            spinner.setAdapter(spinnerAdapter);
        }
    }

    public void setSpinnerAdapter2(String[] items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.grey));
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        spinner.setAdapter(spinnerAdapter);
    }

    public void setCustomSpinner(String[] items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView text = v.findViewById(R.id.text1);
                    text.setText("");
                    text.setHint(getItem(getCount()));//"Hint to be displayed"
                    text.setTextColor(getResources().getColor(R.color.grey));
                }

                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.grey));
                return view;

            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };
        //dasdas
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (items != null) {
            spinnerAdapter.addAll(items);
        }
        if (spinnerAdapter != null) {
            if (spinnerAdapter.getCount() > 0) {
                spinner.setAdapter(spinnerAdapter);
                spinner.setSelection(spinnerAdapter.getCount());
            }
        }
    }

    public void setCustomSpinnerTransparent(String[] items, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_login) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                TextView text = (TextView) v.findViewById(R.id.text1);
                text.setTextSize(getResources().getDimension(R.dimen.superExtraSmallFont));
                if (position == getCount()) {
                    text.setText("");
                    text.setHint(getItem(getCount()));//"Hint to be displayed"
                    text.setTextColor(getResources().getColor(R.color.white));
                } else {
                    text.setTextColor(getResources().getColor(R.color.white));
                }

                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.white));
                return view;

            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };
        //dasdas
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(items);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
    }

    public void setSpinnerAdapter(String[] items, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.white));
                return view;
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(items);
        spinner.setAdapter(adapter);
    }

    public void underlineTextview(TextView textView, String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }

    public void setCustomSpinnerLogin(String[] items, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                TextView text = (TextView) v.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.white));
                text.setGravity(Gravity.CENTER_HORIZONTAL);
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.white));
                text.setGravity(Gravity.CENTER_HORIZONTAL);
                return view;

            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };
        //dasdas
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (items != null) {
            adapter.addAll(items);
        }
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
    }

    public void setCustomSpinnerLoginB(String[] items, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                TextView text = (TextView) v.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.black));
                text.setGravity(Gravity.CENTER_HORIZONTAL);
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.darkGray));
                text.setGravity(Gravity.CENTER_HORIZONTAL);
                return view;

            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };
        //dasdas
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (items != null) {
            adapter.addAll(items);
        }
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
    }

    public void initProgress() {
        progress = new ProgressDialog(this);
        progress.setMessage(Constants.STR_WAIT);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
    }

    protected void setToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        int width = size.x;
        int height = size.y;

        return width;
    }

    public int getScreenOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
//                    Log.e(TAG, "Unknown screen orientation. Defaulting to " +
//                            "portrait.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
//                    Log.e(TAG, "Unknown screen orientation. Defaulting to " +
//                            "landscape.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progress != null && progress.isShowing()) {
            progress.cancel();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
//        MultiDex.install(this);
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

    public void initActivity() {
        database = new Database(getApplicationContext());

//        db = new DatabaseHelper(getApplicationContext());
//        sdb = new SecondDatabaseHelper(getApplicationContext());

//        attendances = db.getAttendance();
//        int ATZchecked = 0;
//        int ATchecked = 0;
//        try {
//            ATZchecked = Settings.Global.getInt(getApplicationContext().getContentResolver(), Settings.Global.AUTO_TIME_ZONE);
//            ATchecked = Settings.Global.getInt(getApplicationContext().getContentResolver(), Settings.Global.AUTO_TIME);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        SecureDate.getInstance().initAttend(attendances, ATZchecked, ATchecked);
//        curDate = SecureDate.getInstance().getDate();
//        if (curDate == null) {
//            PARAM = 5;
//            new RequestUrl().execute();
//        }
//        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
//        if (user != null) {
//            if (user.getIdEmployee() != null) {
//                idEmployee = user.getIdEmployee();
//            }
//        }
    }

    private void initDialog(int resource) {
        dialogview = inflater.inflate(resource, null);
        alertDialog.setContentView(dialogview);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @SuppressLint("InflateParams")
    public void openDialog(int id) {
        initActivity();
        inflater = LayoutInflater.from(this);
        alertDialog = new Dialog(this);
        switch (id) {
            case DIALOG_REASON_PAUSE:
                initDialog(R.layout.custom_dialog_reason_pause);

                btnSave = alertDialog.findViewById(R.id.btnSave);
                btnCancel = alertDialog.findViewById(R.id.btnCancel);
                final EditText edtReasonPause = alertDialog.findViewById(R.id.edtReason);

                checkInRequest = (CheckInOutRequest) Helper.getItemParam(Constants.CHECK_IN_REQUEST);
                user = (User) Helper.getItemParam(Constants.USER_DETAIL);

                pauseRequest = new CheckInOutRequest();
                pauseRequest.setIdOutlet(checkInRequest.getIdOutlet());
                pauseRequest.setIdEmployee(user.getIdEmployee());

                btnSave.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (edtReasonPause.getText().toString().isEmpty()) {
                            edtReasonPause.setError(getResources().getString(R.string.pleaseFillReason));
                        } else {
                            OutletResponse outletResponse = new OutletResponse();
                            outletResponse.setIdOutlet(checkInRequest.getIdOutlet());
                            outletResponse.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                            outletResponse.setPause_time(String.valueOf(curDate.getTime()));
                            outletResponse.setPauseReason(edtReasonPause.getText().toString());
                            outletResponse.setContinue_time(null);
                            outletResponse.setTimer(String.valueOf(SystemClock.elapsedRealtime() - TimerFragment.getTimerValue().getBase()));
                            db.updateVisitPlan(outletResponse);

                            PARAM_STATUS_OUTLET = Constants.PAUSE;

                            hideSoftKeyboard(edtReasonPause);
                            onResume();

                            alertDialog.dismiss();
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        hideSoftKeyboard(edtReasonPause);

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_SPECIMEN:
                initDialog(R.layout.custom_dialog_spesimen);

                btnBack = alertDialog.findViewById(R.id.btnBack);
                ImageView imgSpesimen = alertDialog.findViewById(R.id.imgSpesimen);
                if (flagAttachment == 1) {
                    if (Helper.getItemParam(Constants.ATTACHMENT) != null) {
                        if (!Helper.getItemParam(Constants.ATTACHMENT).equals(Constants.NO)) {
                            byte[] decodedString = Base64.decode(String.valueOf(Helper.getItemParam(Constants.ATTACHMENT)), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgSpesimen.setImageBitmap(decodedByte);
                        }
                    }
                    flagAttachment = 0;
                } else {
                    String specimen = (String) Helper.getItemParam(Constants.SPECIMEN);
//                    String specimen = getResources().getString(R.string.testGambar);
                    if (specimen != null) {
                        byte[] decodedString = Base64.decode(specimen, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgSpesimen.setImageBitmap(decodedByte);
                    }
                }
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_ALERT_CONFIRM:
                initDialog(R.layout.custom_dialog_alert_delete);

                btnCancel = alertDialog.findViewById(R.id.btnCancel);
                btnSave = alertDialog.findViewById(R.id.btnSave);
                txtDialog = alertDialog.findViewById(R.id.txtDialog);

                btnSave.setText(getResources().getString(R.string.yes));
                btnCancel.setText(getResources().getString(R.string.no));

                if (PARAM_DIALOG_ALERT == 1) {
                    txtDialog.setText("Save?");
                } else if (PARAM_DIALOG_ALERT == 2) {
                    txtDialog.setText("Apakah anda ingin checkout?");
                    btnSave.setText(getApplicationContext().getResources().getString(R.string.check_out));
                } else if (PARAM_DIALOG_ALERT == 3) {
                    txtDialog.setText(getResources().getString(R.string.syncornot));
                }

                checkOutRequest = (CheckInOutRequest) Helper.getItemParam(Constants.REQUEST);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress.dismiss();
                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (PARAM_DIALOG_ALERT == 1) {/*Save OrderPlan*/
//                            progress.show();
                            List<MaterialResponse> listMaterial, listMaterialForSave;
                            OutletResponse outletResponse;
                            OrderPlanHeader orderPlanHeader;
                            orderPlanHeaderArrayList = db.getAllOrderPlanHeader();

                            if (Helper.getItemParam(Constants.ORDER_PLAN) == null) {
                                listMaterial = new ArrayList<MaterialResponse>();
                                listMaterialForSave = new ArrayList<MaterialResponse>();
                            } else {
                                listMaterial = (List<MaterialResponse>) Helper.getItemParam(Constants.ORDER_PLAN);
                                listMaterialForSave = (List<MaterialResponse>) Helper.getItemParam(Constants.ORDER_PLAN_TEMP);
                            }
                            if (Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_HEADER) == null) {
                                orderPlanHeader = new OrderPlanHeader();
                            } else {
                                orderPlanHeader = (OrderPlanHeader) Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_HEADER);
                            }

                            if (orderPlanHeader.getIdOutlet() != null) {
                                if (Helper.getItemParam(Constants.FLAG_DO_ORDER_PLAN_DB) != null) {
                                    if (Helper.getItemParam(Constants.AMOUNT_PLAN) != null) {
                                        orderPlanHeader.setPlan(String.valueOf(Helper.getItemParam(Constants.AMOUNT_PLAN)));
                                    }

                                    if (Helper.getItemParam(Constants.FLAG_DO_ORDER_PLAN_DB).equals(getString(R.string.FLAG_UPDATE_ORDER_PLAN))) {
                                        if (listMaterialForSave != null && !listMaterialForSave.isEmpty()) {
                                            for (int i = 0; i < listMaterialForSave.size(); i++) {
                                                orderPlanHeader.setIdMaterial(listMaterialForSave.get(i).getIdMaterial());
                                                orderPlanHeader.setQty1(String.valueOf(listMaterialForSave.get(i).getNewQty1()));
                                                orderPlanHeader.setUom1(listMaterialForSave.get(i).getNewUom1());
                                                orderPlanHeader.setQty2(String.valueOf(listMaterialForSave.get(i).getNewQty2()));
                                                orderPlanHeader.setUom2(listMaterialForSave.get(i).getNewUom2());
                                                orderPlanHeader.setPrice(listMaterialForSave.get(i).getPrice());

                                                if (listMaterialForSave.get(i).getId() != null) {
                                                    orderPlanHeader.setId(listMaterialForSave.get(i).getId());

                                                    if (listMaterialForSave.get(i).isDeleted()) {
                                                        orderPlanHeader.setDeleted(getResources().getString(R.string.STATUS_TRUE));
                                                    } else {
                                                        orderPlanHeader.setDeleted(getResources().getString(R.string.STATUS_FALSE));
                                                    }

                                                    db.updateOrderPlan(orderPlanHeader);
                                                } else {
                                                    orderPlanHeader.setId(Constants.ID_OP_MOBILE.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime()))));
                                                    orderPlanHeader.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                                    db.addOrderPlan(orderPlanHeader);
                                                }
                                            }
                                        }

                                        orderPlanHeader.setStatusOrder("true");
                                        orderPlanHeader.setDate(orderPlanHeader.getOutletDate());

                                        db.updateStatusOrderPlanNew(orderPlanHeader);

                                        Helper.removeItemParam(Constants.FLAG_DO_ORDER_PLAN_DB);
                                    }
                                } else {/*Save*/
                                    if (listMaterial != null && !listMaterial.isEmpty()) {
                                        for (int i = 0; i < listMaterial.size(); i++) {
                                            orderPlanHeader.setId(Constants.ID_OP_MOBILE.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime()))));
                                            orderPlanHeader.setIdMaterial(listMaterial.get(i).getIdMaterial());
                                            orderPlanHeader.setQty1(String.valueOf(listMaterial.get(i).getNewQty1()));
                                            orderPlanHeader.setUom1(listMaterial.get(i).getNewUom1());
                                            orderPlanHeader.setQty2(String.valueOf(listMaterial.get(i).getNewQty2()));
                                            orderPlanHeader.setUom2(listMaterial.get(i).getNewUom2());
                                            orderPlanHeader.setPrice(listMaterial.get(i).getPrice());
                                            orderPlanHeader.setDeleted("false");
                                            orderPlanHeader.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));


                                            db.addOrderPlan(orderPlanHeader);
                                        }

                                        orderPlanHeader.setStatusOrder("true");
                                        orderPlanHeader.setDate(orderPlanHeader.getOutletDate());

                                        db.updateStatusOrderPlanNew(orderPlanHeader);

                                        Helper.removeItemParam(Constants.GET_DETAIL_ORDER_PLAN);
                                        Helper.removeItemParam(Constants.ORDER_PLAN_DETAIL_SAVE);
                                    }
                                }

                                Helper.setItemParam(Constants.CURRENTPAGE, "2");
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
//                                fragment = new OrderPlanFragment(); //ubah ke activity dulu
//                                setContent(fragment);
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.missingData, Toast.LENGTH_SHORT).show();
                            }
                        } else if (PARAM_DIALOG_ALERT == 2) {
                            OutletResponse outletResponse = new OutletResponse();
                            outletResponse.setIdOutlet(checkOutRequest.getIdOutlet());
                            outletResponse.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                            outletResponse.setLat_check_out(Double.valueOf(checkOutRequest.getLatCheckOut()));
                            outletResponse.setLong_check_out(Double.valueOf(checkOutRequest.getLongCheckOut()));
                            outletResponse.setCheck_out_time(String.valueOf(curDate.getTime()));
                            outletResponse.setTimer(checkOutRequest.getTimer());

                            outletResponse.setReason(checkOutRequest.getReason());
                            db.updateVisitPlan(outletResponse);

                            PARAM_STATUS_OUTLET = Constants.FINISHED;
                            onResume();
                        } else if (PARAM_DIALOG_ALERT == 3) {
//                            requestUrlSync();//belum ada rquest url yg sync nya
                        }

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_SIGNATURE:
                initDialog(R.layout.custom_dialog_signature_and_comment);

                btnSave = alertDialog.findViewById(R.id.btnSave);
                btnReset = alertDialog.findViewById(R.id.btnReset);
                mSignaturePad = alertDialog.findViewById(R.id.signature_pad);
                edtTxtComment = alertDialog.findViewById(R.id.edtTxtComment);
                imgSignatureSaved = alertDialog.findViewById(R.id.imgSignatureSaved);

                visitOrderHeader = (VisitOrderHeader) Helper.getItemParam(Constants.ORDER_HEADER_SELECTED);
                OutletResponse outlet = new OutletResponse();
                outlet = (OutletResponse) Helper.getItemParam(Constants.OUTLET);

                if (outlet != null) {
                    if (outlet.getStatusCheckIn() != null) {
                        if (outlet.getStatusCheckIn().equals(Constants.FINISHED)) {
                            edtTxtComment.setEnabled(false);
                        }
                    }
                }

                if (visitOrderHeader != null) {
                    if (visitOrderHeader.getSignatureString() != null) {
                        byte[] decodedString = Base64.decode(visitOrderHeader.getSignatureString(), Base64.DEFAULT);
                        final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgSignatureSaved.setVisibility(View.VISIBLE);
                        imgSignatureSaved.setImageBitmap(decodedByte);

                        mSignaturePad.setVisibility(View.GONE);
                    } else {
                        mSignaturePad.setVisibility(View.VISIBLE);
                        imgSignatureSaved.setVisibility(View.GONE);
                    }

                    if (visitOrderHeader.getComment() != null) {
                        edtTxtComment.setText(visitOrderHeader.getComment());
                    }
                } else {
                    mSignaturePad.setVisibility(View.VISIBLE);
                    imgSignatureSaved.setVisibility(View.GONE);
                }


                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventDupeDialog();
                        //getSignatureBitmap() => background white
                        //getTransparentSignatureBitmap() => background transparant
                        VisitOrderHeader header = new VisitOrderHeader();
                        Bitmap bitmapSignature;
                        if (imgSignatureSaved.getVisibility() == View.GONE) {
                            bitmapSignature = mSignaturePad.getSignatureBitmap();
                        } else if (visitOrderHeader != null) {
                            if (visitOrderHeader.getSignatureString() != null) {
                                byte[] decodedString = Base64.decode(visitOrderHeader.getSignatureString(), Base64.DEFAULT);
                                final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                bitmapSignature = decodedByte;
                            } else {
                                bitmapSignature = mSignaturePad.getSignatureBitmap();
                            }
                        } else {
                            bitmapSignature = mSignaturePad.getSignatureBitmap();
                        }

                        if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                            header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
                            header.setComment(edtTxtComment.getText().toString().trim());
                            header.setSignatureString(getEncodedImage(bitmapSignature));
                            Helper.setItemParam(Constants.VISIT_ORDER_HEADER, header);

                        }
                        alertDialog.dismiss();
                        Helper.dialogCase = "S";
                        openDialog(DIALOG_ATTACH_PHOTO);
                    }
                });

                btnReset.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        imgSignatureSaved.setVisibility(View.GONE);
                        mSignaturePad.setVisibility(View.VISIBLE);
                        mSignaturePad.clear();
                    }
                });
                alertDialog.show();
                break;
            case DIALOG_ATTACH_PHOTO:
                initProgress();
                db = new DatabaseHelper(getApplicationContext());
                initDialog(R.layout.custom_dialog_photo_new);

                btnSave = alertDialog.findViewById(R.id.btnSave);
                btnCancel = alertDialog.findViewById(R.id.btnCancel);
                imgCameraPhoto = alertDialog.findViewById(R.id.imgCameraPhoto);
                imgGalleryPhoto = alertDialog.findViewById(R.id.imgGalleryPhoto);
                imgCancelPhoto = alertDialog.findViewById(R.id.imgCancelPhoto);
                txtImagePathAttachment = alertDialog.findViewById(R.id.txtImagePathAttachment);
                imgAttachment = alertDialog.findViewById(R.id.imgAttachment);
                visitOrderHeader = (VisitOrderHeader) Helper.getItemParam(Constants.ORDER_HEADER_SELECTED);
                user = (User) Helper.getItemParam(Constants.USER_DETAIL);
                final OutletResponse outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);

                if (visitOrderHeader != null) {
                    if (visitOrderHeader.getPhotoString() != null) {
                        byte[] decodedString = Base64.decode(visitOrderHeader.getPhotoString(), Base64.DEFAULT);
                        final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgAttachment.setImageBitmap(decodedByte);
                    }
                }

                imgCancelPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bitmapCamera != null) {
                            bitmap = bitmapCamera;
                        } else if (bitmapGallery != null) {
                            bitmap = bitmapGallery;
                        }
                        if (bitmap != null) {
                            txtImagePathAttachment.setText(R.string.attach_here);
                            imgAttachment.setImageResource(R.drawable.ic_attach);
                            bitmap = null;
                        } else {
                            txtImagePathAttachment.setText(R.string.attach_here);
                            imgAttachment.setImageResource(R.drawable.ic_attach);
                            bitmap = null;
                        }
                    }
                });

                imgCameraPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bitmapCamera != null) {
                            bitmap = bitmapCamera;
                        } else if (bitmapGallery != null) {
                            bitmap = bitmapGallery;
                        }
                        if (bitmap != null) {
                            txtImagePathAttachment.setText(R.string.attach_here);
                            imgAttachment.setImageResource(R.drawable.ic_attach);
                            bitmap = null;
                        } else {
                            txtImagePathAttachment.setText(R.string.attach_here);
                            imgAttachment.setImageResource(R.drawable.ic_attach);
                            bitmap = null;
                        }
                        PARAM_PHOTO = 3;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                });

                imgGalleryPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bitmap = null;
                        bitmapCamera = null;
                        bitmapGallery = null;

                        PARAM_PHOTO = 3;
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }
                });

                if (Helper.dialogCase.equals("S")) {
                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Helper.dialogCase = "Ph";
                            VisitOrderHeader header = new VisitOrderHeader();

                            if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                                header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
                                if (bitmapPhoto != null) {
                                    compressedBitmapGallery = scaleDown(bitmapPhoto, 600, true);
                                }
                                if (visitOrderHeader != null) {
                                    if (visitOrderHeader.getPhotoString() != null) {
                                        if (getEncodedImage(compressedBitmapGallery).equals("")) {
                                            header.setPhotoString(visitOrderHeader.getPhotoString());
                                        } else if (!getEncodedImage(compressedBitmapGallery).equals("")) {
                                            header.setPhotoString(getEncodedImage(compressedBitmapGallery));
                                        }
                                    } else {
                                        if (bitmapPhoto != null) {
                                            header.setPhotoString(getEncodedImage(compressedBitmapGallery));
                                        }
                                    }

                                } else {
                                    header.setPhotoString(null);
                                    if (bitmapPhoto != null) {
                                        header.setPhotoString(getEncodedImage(compressedBitmapGallery));
                                    }
                                }
                                Helper.setItemParam(Constants.VISIT_ORDER_HEADER, header);
                            }

                            orderHeaderDetail = (VisitOrderHeader) Helper.getItemParam(Constants.ORDER_HEADER_SELECTED);

                            progress.show();
                            header = new VisitOrderHeader();
                            if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                                header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
                            }
                            user = (User) Helper.getItemParam(Constants.USER_DETAIL);
                            if (user.getReason() == 0) {
                                header.setIdEmployee("KT");
                            }
                            if (Helper.getItemParam(Constants.ORDER_TYPE) != null) {
                                header.setOrderType(Helper.getItemParam(Constants.ORDER_TYPE).toString());
                            }
                            List<VisitOrderDetailResponse> visitOrderDetailResponseList = new ArrayList<>();
                            if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                                visitOrderDetailResponseList = (List<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);
                            }

                            VisitOrderRequest visitOrderRequest = new VisitOrderRequest();
                            visitOrderRequest.setOrderDetail(visitOrderDetailResponseList);
                            visitOrderRequest.setOrderHeader(header);

                            if (outletResponse != null) {
                                if (outletResponse.getIdOutlet() != null && Helper.getItemParam(Constants.ORDER_TYPE) != null) {
                                    orderHeaderList = db.getListOrderHeader(outletResponse.getIdOutlet(), Helper.getItemParam(Constants.ORDER_TYPE).toString());
                                }
                            }
                            if (orderHeaderDetail == null) {
                                String idTo = Constants.EMPTY_STRING;
                                if (Helper.getItemParam(Constants.ORDER_TYPE).equals(Constants.ORDER_CANVAS_TYPE)) {
                                    idTo = Constants.ID_SAVE_OC.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
                                } else {
                                    idTo = Constants.ID_SAVE_OT.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
                                }

                                header.setId(idTo);
                                header.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                db.addOrderHeader(header);
                                if (header.getListFreeGoods() != null && !header.getListFreeGoods().isEmpty()) {
                                    db.addFreeGoodsWODrop(idTo, header.getListFreeGoods());
                                }

                                if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
                                    for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
                                        db.addOrderDetail(idTo, visitOrderDetailResponseList.get(i));
                                        if (visitOrderDetailResponseList.get(i).getListToPrice() != null && !visitOrderDetailResponseList.get(i).getListToPrice().isEmpty()) {
                                            for (int j = 0; j < visitOrderDetailResponseList.get(i).getListToPrice().size(); j++) {
                                                db.addToPrice(visitOrderDetailResponseList.get(i).getListToPrice().get(j));
                                            }
                                        }
                                    }
                                }
                            } else {
                                db.updateOrderHeader(header);
                                db.deleteOrderDetailBy(orderHeaderDetail.getId());
                                if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
                                    for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
                                        db.addOrderDetail(orderHeaderDetail.getId(), visitOrderDetailResponseList.get(i));
                                        if (visitOrderDetailResponseList.get(i).getListToPrice() != null && !visitOrderDetailResponseList.get(i).getListToPrice().isEmpty()) {
                                            for (int j = 0; j < visitOrderDetailResponseList.get(i).getListToPrice().size(); j++) {
                                                db.addToPrice(visitOrderDetailResponseList.get(i).getListToPrice().get(j));
                                            }
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < 3; i++) {
                                onBackPressed();
                            }
                            progress.dismiss();
                            alertDialog.dismiss();
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                } else {
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
                }

                alertDialog.show();
                break;
            case DIALOG_REASON:
                initDialog(R.layout.custom_dialog_reason);

                btnSave = alertDialog.findViewById(R.id.btnSave);
                btnCancel = alertDialog.findViewById(R.id.btnCancel);
                final EditText edtReason = alertDialog.findViewById(R.id.edtReason);

                btnSave.setText(getApplicationContext().getResources().getString(R.string.check_out));

                checkOutRequest = (CheckInOutRequest) Helper.getItemParam(Constants.REQUEST);
                user = (User) Helper.getItemParam(Constants.USER_DETAIL);

                btnSave.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        checkOutRequest.setReason(edtReason.getText().toString());
                        if (edtReason.getText().toString().isEmpty()) {
                            edtReason.setError(getResources().getString(R.string.pleaseFillReason));
                        } else {
                            OutletResponse outletResponse = new OutletResponse();
                            outletResponse.setIdOutlet(checkOutRequest.getIdOutlet());
                            outletResponse.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                            outletResponse.setLat_check_out(Double.valueOf(checkOutRequest.getLatCheckOut()));
                            outletResponse.setLong_check_out(Double.valueOf(checkOutRequest.getLongCheckOut()));
                            outletResponse.setCheck_out_time(String.valueOf(curDate.getTime()));
                            outletResponse.setTimer(checkOutRequest.getTimer());
                            outletResponse.setReason(checkOutRequest.getReason());
                            db.updateVisitPlan(outletResponse);

                            Helper.setItemParam(Constants.CURRENTPAGE, "3");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

//                            fragment = new RetailOutletFragment(); pindah halaman ke retail outlet??
//                            setContent(fragment);

                            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edtReason.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            alertDialog.dismiss();
                        }
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edtReason.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_ADD_MATERIAL:
                initDialog(R.layout.custom_dialog_add_material);

                ImageView btnCancel = alertDialog.findViewById(R.id.btnCancel);
                btnSave = alertDialog.findViewById(R.id.btnSave);
                edtMaterialName = dialogview.findViewById(R.id.materialName);
                edtMaterialCode = dialogview.findViewById(R.id.materialCode);
                edtKlasifikasi = dialogview.findViewById(R.id.klasifikasi);

                new LoadMaterial().execute();

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftKeyboard(edtMaterialName);
                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listMaterialName.contains(edtMaterialName.getText().toString())) {
                            Material material = new Material();
                            material.setMaterialCode(edtMaterialCode.getText().toString());
                            material.setId(edtMaterialCode.getText().toString());
                            material.setDesc(edtMaterialName.getText().toString());
                            material.setKlasifikasi(edtKlasifikasi.getText().toString());

                            MaterialResponse materialResponse = new MaterialResponse();
                            materialResponse.setIdMaterial(edtMaterialCode.getText().toString());
                            materialResponse.setMaterialName(edtMaterialName.getText().toString());

                            Helper.setItemParam(Constants.ADD_MATERIAL, material);
                            Helper.setItemParam(Constants.ADD_MATERIAL_STORE, materialResponse);
                            Helper.setItemParam(Constants.GET_DETAIL_VISIT, "1");

                            hideSoftKeyboard(edtMaterialName);
                            alertDialog.dismiss();
                            onResume();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.warningXMaterial, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
        }
    }

    private class LoadMaterial extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                ArrayList<MaterialResponse> materialResponseList = (ArrayList<MaterialResponse>) Helper.getItemParam(Constants.STORE_CHECK_DETAIL);
                ArrayList<Return> returnList = (ArrayList<Return>) Helper.getItemParam(Constants.RETURN_DETAIL);
                List<VisitOrderDetailResponse> visitOrderDetailResponseList = (List<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);

                go = new int[]{0};

                listMaterialName = new ArrayList<>();
                listMaterialCode = new ArrayList<>();
                ArrayList<Material> listMaterialNew = new ArrayList<>();

                if (visitOrderDetailResponseList != null) {

                    listMaterialNew = db.getMasterMaterialNameCodeForOrder();
                    for (Material data : listMaterialNew) {
                        listMaterialName.add(data.getMaterialCode());
                        listMaterialCode.add(String.valueOf(data.getId()));
                    }

                    if (!listMaterialName.isEmpty() && !listMaterialCode.isEmpty()) {
                        for (VisitOrderDetailResponse order : visitOrderDetailResponseList) {
                            for (int j = 0; j < listMaterialName.size(); j++) {
                                if (order.getMaterialName().equals(listMaterialName.get(j))) {
                                    listMaterialName.remove(j);
                                }
                            }

                            for (int i = 0; i < listMaterialCode.size(); i++) {
                                if (order.getIdMaterial().equals(listMaterialCode.get(i))) {
                                    listMaterialCode.remove(i);
                                }
                            }
                        }
                    }
                } else if (materialResponseList != null) {
                    listMaterialNew = db.getMasterMaterialNameCodeForOrder();
                    for (Material data : listMaterialNew) {
                        listMaterialName.add(data.getMaterialCode());
                        listMaterialCode.add(String.valueOf(data.getId()));
                    }

                    if (!listMaterialName.isEmpty() && !listMaterialCode.isEmpty()) {
                        for (MaterialResponse material : materialResponseList) {
                            for (int j = 0; j < listMaterialName.size(); j++) {
                                if (material.getMaterialName().equals(listMaterialName.get(j))) {
                                    listMaterialName.remove(j);
                                }
                            }

                            for (int i = 0; i < listMaterialCode.size(); i++) {
                                if (material.getIdMaterial().equals(listMaterialCode.get(i))) {
                                    listMaterialCode.remove(i);
                                }
                            }
                        }
                    }
                } else {
                    listMaterialNew = db.getMasterMaterialNameCodeForOrder();
                    for (Material data : listMaterialNew) {
                        listMaterialName.add(data.getMaterialCode());
                        listMaterialCode.add(String.valueOf(data.getId()));
                    }
                }

                adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listMaterialName);
                codeAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listMaterialCode);

            } catch (Exception ex) {
                return null;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*32019 beta update*/
            initProgress();
            progress.show();

            /**/
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            progress.dismiss();
            if (msg != null) {
                if (msg) {
                    EdtMaterialListener edtMatListener = new EdtMaterialListener();
                    EdtMaterialCodeListener edtMatCodeListener = new EdtMaterialCodeListener();

                    EdtMatNameWatcher edtMatNameWatcher = new EdtMatNameWatcher();
                    EdtMatCodeWatcher edtMatCodeWatcher = new EdtMatCodeWatcher();

                    edtMaterialName.setOnItemClickListener(edtMatListener);
                    edtMaterialCode.setOnItemClickListener(edtMatCodeListener);

                    edtMaterialName.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            flagName = 1;
                            flagCode = 0;
                            return false;
                        }
                    });

                    edtMaterialCode.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            flagCode = 1;
                            flagName = 0;
                            return false;
                        }
                    });
                    edtMaterialName.addTextChangedListener(edtMatNameWatcher);
                    edtMaterialCode.addTextChangedListener(edtMatCodeWatcher);

                    alertDialog.show();
                }
            }

        }
    }

    private class EdtMatNameWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (flagName == 1) {
                edtMaterialCode.setText(null);
                edtKlasifikasi.setText(null);

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private class EdtMatCodeWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (flagCode == 1) {
                edtMaterialName.setText(null);
                edtKlasifikasi.setText(null);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class EdtMaterialListener implements AdapterView.OnItemClickListener {
        MaterialResponse materialResponse;

        EdtMaterialListener() {
            edtMaterialName.setAdapter(adapter);
            edtMaterialName.setThreshold(1);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
            String selection = (String) parent.getItemAtPosition(position);
            try {
                materialResponse = db.getMasterMaterialByName(selection);
                edtMaterialCode.setText(materialResponse.getIdMaterial());
                edtKlasifikasi.setText(materialResponse.getIdMaterialClass());
            } catch (SQLiteException e) {

            }
        }
    }

    private class EdtMaterialCodeListener implements AdapterView.OnItemClickListener {
        MaterialResponse materialResponse;

        EdtMaterialCodeListener() {
            edtMaterialCode.setAdapter(codeAdapter);
            edtMaterialCode.setThreshold(1);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
            String selection = (String) parent.getItemAtPosition(position);
            materialResponse = db.getMasterMaterialByCode(selection);
            edtMaterialName.setText(materialResponse.getMaterialName());
            edtKlasifikasi.setText(materialResponse.getIdMaterialClass());
        }
    }

    private class RequestUrl extends AsyncTask<Void, Void, MessageResponse> {

        @Override
        protected MessageResponse doInBackground(Void... voids) {
            try {
                if (PARAM == 5) {
                    String URL_GET_CURRENT_TIME = Constants.API_GET_OFFLINE_LOGIN.concat(Constants.QUESTION)
                            .concat(Constants.VERSION).concat(Constants.EQUALS).concat(Constants.CURRENT_VERSION);
                    final String url = Constants.URL.concat(URL_GET_CURRENT_TIME);
                    offlineData = (OfflineLoginData) Helper.getWebservice(url, OfflineLoginData.class);
                    return null;
                }
                return null;
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
        }

        @Override
        protected void onPostExecute(MessageResponse msg) {
            if (offlineData != null) {
                if (offlineData.getDatetimeNow() != null) {
                    SecureDate.getInstance().initServerDate(Helper.convertStringtoDate(Constants.DATE_TYPE_16, offlineData.getDatetimeNow()));
                    curDate = SecureDate.getInstance().getDate();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT);
            }
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }

    public void preventDupeDialog() {
        if (getAlertDialog() != null) {
            if (getAlertDialog().isShowing()) {
                return;
            }
        }
    }

    public void setSpinnerAdapter3(String[] items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        spinner.setAdapter(spinnerAdapter);
    }

    public String getEncodedImage(Bitmap bm) {
        String encodedImage = "";
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        }
        return encodedImage;
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void setAutoCompleteAdapter(List<String> items, AutoCompleteTextView spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
//                text.setTextSize(7);
//                text.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        spinner.setAdapter(spinnerAdapter);
    }

    public void setAutoCompleteAdapterReason(List<Reason> items, AutoCompleteTextView spinner) {
        spinnerReasonAdapter = new ArrayAdapter<Reason>(getApplicationContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                return view;
            }
        };

        spinnerReasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReasonAdapter.addAll(items);
        spinner.setAdapter(spinnerReasonAdapter);
    }

    public byte[] getByteArrayFromUriGallery(Uri uri) {
        InputStream iStream = null;
        byte[] inputData = null;
        try {
            iStream = getContentResolver().openInputStream(uri);
            inputData = getBytesGallery(iStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputData;
    }

    public byte[] getBytesGallery(InputStream inputStream) throws IOException {
        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteBuffer);
        bmp = Bitmap.createScaledBitmap(bmp, 600, 250, true);
        return encodeTobase64(bmp);
    }

    public byte[] encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] b = baos.toByteArray();
//        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return b;
    }

    public File getDirLoc(Context applicationContext) {
        String PDF_FOLDER_NAME = "/Qubes/";
        File directory = null;
        //if there is no SD card, create new directory objects to make directory on device
        if (Environment.getExternalStorageState() == null) {
            //create new file directory object
            directory = new File(Environment.getDataDirectory() + PDF_FOLDER_NAME);
            // if no directory exists, create new directory
            if (!directory.exists()) {
                directory.mkdir();
            }

            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            try {
                int version = Build.VERSION.SDK_INT;
                if (version >= 30) {
                    directory = new File(applicationContext.getFilesDir() + PDF_FOLDER_NAME);
                } else {
                    directory = new File(Environment.getExternalStorageDirectory() + PDF_FOLDER_NAME);
                }
                // results
                if (!directory.exists()) {
                    directory.mkdir();
                }
            } catch (Exception ex) {
                setToast(ex.getMessage());
            }
        }// end of SD card checking

        return directory;
    }
}

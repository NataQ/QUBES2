package id.co.qualitas.qubes.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.database.SecondDatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.ChangePasswordRequest;
import id.co.qualitas.qubes.model.JenisJualandTop;
import id.co.qualitas.qubes.model.LoginResponse;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.OffDate;
import id.co.qualitas.qubes.model.OfflineLoginData;
import id.co.qualitas.qubes.model.OrderPlanHeader;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.ReturnResponse;
import id.co.qualitas.qubes.model.ToPrice;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;
import id.co.qualitas.qubes.session.SessionManager;

public class LoginActivity extends BaseActivity {
    private ImageView imgShowPassword;
    private Button login;
    private EditText input_username, input_password;
    private TextView subsLogin, ipChanger, txtQubes;

    private String txtUsername, txtPassword;

    private User user;
    private OfflineLoginData offlineData;
    private MessageResponse messageResponse;
    private String registerID;

    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;

    private ProgressDialog pDialog;

    private SecondDatabaseHelper sdb;

    private long startTime;
    boolean showPassword = false;

    //bluetooth
    public static final int PERMISSION_BLUETOOTH = 1;

    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        initialize();

        SharedPreferences permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                            dialog.cancel();
                            Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setCancelable(false);
                    builder.setMessage("This app needs Camera Gallery and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getBaseContext(), "Go to Permissions to Grant Gallery and Location", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
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
                editor.apply();
            } else {
                //You already have the permission, just go ahead.
                proceedAfterPermission();
            }
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(input_username);
                hideSoftKeyboard(input_password);
                login();
            }
        });

        subsLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginPenggantiActivity.class);
                startActivity(intent);
            }
        });

        ipChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showPassword) {
                    //show password
                    input_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowPassword.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_not_look_password));
                    showPassword = true;
                } else {
                    // hide password
                    input_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowPassword.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_look_password));
                    showPassword = false;
                }
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    private void initialize() {
        txtQubes = findViewById(R.id.txtQubes);
        login = findViewById(R.id.btnSave);
        imgShowPassword = findViewById(R.id.imgShowPassword);
        input_username = findViewById(R.id.edtUsername);
        input_password = findViewById(R.id.edtPassword);
        subsLogin = findViewById(R.id.loginPengganti);
        ipChanger = findViewById(R.id.ipChanger);
        db = new DatabaseHelper(getApplicationContext());

        if (Helper.getItemParam(Constants.REGIISTERID) == null) {
            try {
                String refreshedToken = FirebaseMessaging.getInstance().getToken().toString();
                Helper.setItemParam(Constants.REGIISTERID, refreshedToken);
                registerID = refreshedToken;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), getString(R.string.getTokenError), Toast.LENGTH_LONG).show();
            }
        } else {
            registerID = Helper.getItemParam(Constants.REGIISTERID).toString();
        }
    }

    private void login() {
        txtUsername = input_username.getText().toString().trim();
        txtPassword = input_password.getText().toString().trim();

        if (txtUsername.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pleaseFillUsername), Toast.LENGTH_SHORT).show();
        } else if (txtPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pleaseFillUsername), Toast.LENGTH_SHORT).show();
        } else {
            startTime = System.nanoTime();

            System.out.println("mulai " + String.valueOf(startTime));
//            PARAM = 4;
            PARAM = 1;
            new RequestUrl().execute();//4
            progress.show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RequestUrl extends AsyncTask<Void, Void, LoginResponse> {

        @Override
        protected LoginResponse doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_LOGIN = Constants.OAUTH_TOKEN_PATH;
                    String pwd = Constants.AND.concat(Constants.TXTPASSWORD).concat(Constants.EQUALS) + txtPassword;
                    String username = Constants.USERNAME.concat(Constants.EQUALS) + txtUsername.concat(Constants.PIPE_).concat(Constants.CLIENT_ID);
                    String grantType = Constants.GRANT_TYPE.concat(Constants.EQUALS) + Constants.TXTPASSWORD + Constants.AND;

//                    final String url = Constants.URL.concat(URL_LOGIN);
                    final String content = grantType.concat(username).concat(pwd);
                    final String url = Constants.URL.concat(URL_LOGIN).concat("?").concat(content);

                    return (LoginResponse) Helper.postWebserviceLogin(url, LoginResponse.class, null);
                } else if (PARAM == 2) {
                    String URL_GET_DETAIL = Constants.API_GET_DETAIL_USER;
                    String username = Constants.QUESTION.concat(Constants.USERNAME.concat(Constants.EQUALS)) + txtUsername.concat(Constants.PIPE_).concat(Constants.CLIENT_ID);

                    final String url = Constants.URL.concat(URL_GET_DETAIL).concat(username);

                    user = (User) Helper.getWebservice(url, User.class);
                    return null;
                } else if (PARAM == 3) {
                    String URL_GET_OFFLINE_DATA = Constants.API_GET_OFFLINE_LOGIN;

//                    final String url = Constants.URL.concat(URL_GET_OFFLINE_DATA).
//                            concat(Constants.QUESTION).concat(Constants.ID_EMPLOYEE).concat(Constants.EQUALS).concat(user.getIdEmployee());

                    final String url = Constants.URL.concat(URL_GET_OFFLINE_DATA).
                            concat(Constants.QUESTION).concat(Constants.ID_EMPLOYEE).concat(Constants.EQUALS).concat(user.getIdEmployee())
                            .concat(Constants.AND).concat(Constants.VERSION).concat(Constants.EQUALS).concat(Constants.CURRENT_VERSION);
                    //TODO disini

                    offlineData = (OfflineLoginData) Helper.getWebservice(url, OfflineLoginData.class);
                    return null;
                } else if (PARAM == 4) {
                    final String url = Constants.URL.concat(Constants.API_CHECKING_USER);

                    ChangePasswordRequest loginRequest = new ChangePasswordRequest();
                    loginRequest.setUsername(txtUsername + getString(R.string.clientId));
                    loginRequest.setRegisId(registerID);
                    loginRequest.setPassword(txtPassword);

                    messageResponse = (MessageResponse) Helper.postWebserviceWithBodyWithoutHeaders(url, MessageResponse.class, loginRequest);//post
                    return null;
                } else {
                    String URL_GET_DETAIL = Constants.API_UPDATE_REGIS;

                    final String url = Constants.URL.concat(URL_GET_DETAIL);

                    ChangePasswordRequest loginRequest = new ChangePasswordRequest();
                    loginRequest.setUsername(txtUsername + getString(R.string.clientId));
                    loginRequest.setRegisId(registerID);

                    user = (User) Helper.postWebserviceWithBody(url, User.class, loginRequest);//post
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e(Constants.LOGIN_ACTIVITY, ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(LoginResponse logins) {
            if (PARAM == 1) {
                if (logins != null) {
                    if (String.valueOf(logins.getError()).equals(Constants.INVALID_GRANT)) {
                        Toast.makeText(getApplicationContext(), getString(R.string.wrongUser), Toast.LENGTH_SHORT).show();
                    } else {
                        Helper.setItemParam(Constants.LOGIN, logins);
                        Helper.setItemParam(Constants.TOKEN, logins.getAccess_token());

                        PARAM = 5;
                        new RequestUrl().execute();//5
                    }
                } else {
                    progress.dismiss();
                    if (Helper.getItemParam(Constants.ERROR_LOGIN) != null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.wrongUser), Toast.LENGTH_SHORT).show();
                        Helper.removeItemParam(Constants.ERROR_LOGIN);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.errCon), Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (PARAM == 2) {
                if (user != null) {
                    Helper.removeItemParam(Constants.SUPERVISOR_DETAIL);
                    Helper.setItemParam(Constants.USER_DETAIL, user);
//                    String ex = Helper.objectToString(user);
//                    new SessionManager(getApplicationContext()).createLoginSession(ex);

                    if(!db.isLogin(txtUsername)){
                        PARAM = 3;
                        new RequestUrl().execute();//3
                    }else{
                        Intent intent = new Intent(LoginActivity.this, NewMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.errorData), Toast.LENGTH_SHORT).show();
                }
            } else if (PARAM == 3) {
                if (offlineData != null) {
                    if (offlineData.getAdditionalInfo() != null) {
                        if (offlineData.getAdditionalInfo().getIdPlant() != null) {
                            user.setIdPlant(offlineData.getAdditionalInfo().getIdPlant());
                        }
                    }

                    /*Table Material*/
                    db.addMaterialN(offlineData.getListMaterial());

                    /*Table UOM*/
                    db.addUOMN(offlineData.getListUOm());

                    db.addMasterUomN(offlineData.getListMasterUom());

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

                    /*Table Visit Plan*/
                    db.addVisitPlanN(offlineData.getListVisitPlan());

                    /*Table Order Plan*/
                    db.deleteOrderPlan();

                    db.addOrderPlanN(offlineData.getListOrderPlan());

                    if (offlineData.getListOrderPlan() != null && !offlineData.getListOrderPlan().isEmpty()) {
                        for (OrderPlanHeader orderPlan : offlineData.getListOrderPlan()) {
                            orderPlan.setStatusOrder(getString(R.string.STATUS_TRUE));
                            db.updateStatusOrderPlanNew(orderPlan);
                        }
                    }

                    /*Table TOP*/
                    db.addTOPN(offlineData.getListTop());

                    /*Table Jenis Jual*/
                    db.deleteJenisJual();
                    if (offlineData.getListJenisJual() != null && !offlineData.getListJenisJual().isEmpty()) {
                        for (JenisJualandTop jenisJualandTop : offlineData.getListJenisJual()) {
                            db.addJenisJual(jenisJualandTop);
                        }
                    }

                    /*Table Sales Office*/
                    db.addSalesOfficeN(offlineData.getListSalesOffice());

                    db.addPartnerN(offlineData.getListPartner());

                    db.addStoreCheckN(offlineData.getListStore());

                    db.deleteToHeader();
                    if (offlineData.getListOrderHeader() != null && !offlineData.getListOrderHeader().isEmpty()) {
                        for (VisitOrderHeader orderHeader : offlineData.getListOrderHeader()) {
                            if (orderHeader.getSalesOffice() != null) {
                                orderHeader.setSalesOfficeName(db.getSalesOfficeName(orderHeader.getSalesOffice()));
                            }


                            if (orderHeader.getSignature() != null) {
                                orderHeader.setSignatureString(orderHeader.getSignature());
                            }
                            if (orderHeader.getPhoto() != null) {
                                orderHeader.setPhotoString(orderHeader.getPhoto());
                            }
                            db.addOrderHeader(orderHeader);
                        }
                    }

                    db.deleteOrderDetail();
                    if (offlineData.getListOrderDetail() != null && !offlineData.getListOrderDetail().isEmpty()) {
                        for (VisitOrderDetailResponse orderDetail : offlineData.getListOrderDetail()) {
                            db.addOrderDetail(orderDetail);
                        }
                    }

                    db.deleteToPrice();
                    if (offlineData.getListToPrice() != null && !offlineData.getListToPrice().isEmpty()) {
                        for (ToPrice toPrice : offlineData.getListToPrice()) {
                            db.addToPrice(toPrice);
                        }
                    }

                    //TODO: TEST
                    db.deleteReason();
                    if (offlineData.getListReasonReturn() != null && !offlineData.getListReasonReturn().isEmpty()) {
                        for (Reason reason : offlineData.getListReasonReturn()) {
                            db.addReason(reason);
                        }
                    }

                    db.deleteReturnHeader();
                    if (offlineData.getListReturnHeader() != null && !offlineData.getListReturnHeader().isEmpty()) {
                        for (ReturnResponse returnHeader : offlineData.getListReturnHeader()) {
                            db.addReturnHeader(returnHeader);
                        }
                    }

                    db.deleteReturnDetail();
                    if (offlineData.getListReturnDetail() != null && !offlineData.getListReturnDetail().isEmpty()) {
                        for (Return returnDetail : offlineData.getListReturnDetail()) {
                            if (returnDetail.getReason() != null) {
                                String idReason = returnDetail.getReason();
                                returnDetail.setReason(db.getReasonById(idReason).getDesc());
                                returnDetail.setCategory(db.getReasonById(idReason).getType());
                            }
                            db.addReturnDetail(returnDetail);
                        }
                    }

                /*FreeGoods*/
                    db.addFreeGoods(offlineData.getListFreeGoods());

                    if (offlineData.getDatetimeNow() != null) {
                        Date curDate = Helper.convertStringtoDateUS(Constants.DATE_TYPE_16, offlineData.getDatetimeNow());
                        Long elapse = SystemClock.elapsedRealtime();

                        OffDate offDate = new OffDate();
                        offDate.setCurDate(offlineData.getDatetimeNow());
                        offDate.setElapseTime(elapse);

                        try {
                            SecureDate.getInstance().initServerDate(curDate);
                        }catch (ExceptionInInitializerError e){
                            runOnUiThread(new Runnable(){
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Init Server Date Error",Toast.LENGTH_LONG).show();
//                                    return null;
                                }
                            });
                        }
                        db.deleteAttendance();

                        user.setDateTimeNow(offlineData.getDatetimeNow());
                        user.setmElapsedRealTime(SystemClock.elapsedRealtime());
                        db.addAttendance(user);

                        String ex = Helper.objectToString(offDate);
                        new SessionManager(getApplicationContext()).createDateSession(ex);

                    }

                    Helper.setItemParam(Constants.CURDATE, offlineData.getDatetimeNow());
                    Intent intent = new Intent(LoginActivity.this, MainActivityDrawer.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                progress.dismiss();
            } else if (PARAM == 4) {
                if (messageResponse != null) {
                    if (messageResponse.getIdMessage() == 0) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.wrongUser), Toast.LENGTH_SHORT).show();
                    } else if (messageResponse.getIdMessage() == 1) {
                        PARAM = 1;
                        new RequestUrl().execute();//1
                    } else if (messageResponse.getIdMessage() == 2) {
                        progress.dismiss();
                        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
                        View dialogview = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                        final Dialog alertDialog = new Dialog(LoginActivity.this);
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.setContentView(dialogview);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.setCanceledOnTouchOutside(true);
                        Button btnCancel = (Button) alertDialog.findViewById(R.id.btnCancel);
                        Button btnSave = (Button) alertDialog.findViewById(R.id.btnSave);
                        TextView txtDialog = (TextView) alertDialog.findViewById(R.id.txtDialog);

                        txtDialog.setText(getResources().getString(R.string.otherLogIn));
                        btnCancel.setText(Constants.NO);
                        btnSave.setText(Constants.YES);

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progress.show();
                                PARAM = 1;
                                new RequestUrl().execute();//1
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.show();
                    }
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.errCon), Toast.LENGTH_SHORT).show();
                }
            } else {
                PARAM = 2;
                new RequestUrl().execute();//2
            }
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing.
        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
        View dialogview;
        final Dialog alertDialog = new Dialog(LoginActivity.this);
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

        txtDialog.setText(getResources().getString(R.string.alertExit));
        btnSave.setText("Ya");
        btnCancel.setText("Tidak");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}

package id.co.qualitas.qubes.activity.aspp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.BuildConfig;
import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.activity.SettingActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Bank;
import id.co.qualitas.qubes.model.LoginResponse;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.printer.ConnectorActivity;
import id.co.qualitas.qubes.session.SessionManagerQubes;

//https://github.com/DantSu/ESCPOS-ThermalPrinter-Android
//https://stackoverflow.com/questions/48496035/how-to-connect-to-a-bluetooth-printer
//https://github.com/sanxy/BluetoothPrinter

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private TextView txtSettings, txtVersion;
    private String registerID;
    boolean showPassword = false;
    private ImageView imgShowPassword;
    private EditText edtPassword, edtUsername;

    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
    private String userId, password;
    private LoginResponse loginResponse;
    private WSMessage messageResponse;
    private boolean setData = false;
    protected Database database;
    protected ProgressDialog progress;
    protected int PARAM = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aspp_activity_login);

        database = new Database(getApplicationContext());
        Helper.trustSSL();
        initProgress();
        deleteHelper();
        initialize();

        txtVersion.setText("Version " + String.valueOf(BuildConfig.VERSION_NAME));

        txtVersion.setOnClickListener(v -> {
        });

        txtSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialogSetting();
                Intent intent = new Intent(getApplicationContext(), ConnectorActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(v -> {
            userId = edtUsername.getText().toString().trim().toLowerCase();
            password = edtPassword.getText().toString().trim();

            if (userId.isEmpty()) {
                edtUsername.setError(getString(R.string.pleaseFillUsername));
            } else if (password.isEmpty()) {
                edtPassword.setError(getString(R.string.pleaseFillPassword));
            } else {
                PARAM = 1;
                new RequestUrl().execute();
                progress.show();
            }
        });

        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showPassword) {
                    //show password
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowPassword.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_not_look_password));
                    showPassword = true;
                } else {
                    // hide password
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowPassword.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_look_password));
                    showPassword = false;
                }
            }
        });
    }

    private void setReason(String username) {
        List<Reason> mList = new ArrayList<>();
        mList.add(new Reason("1", "Toko Tutup", "Not Visit", false, true));
        mList.add(new Reason("2", "Toko Kebanjiran", "Not Visit", false, true));
        mList.add(new Reason("3", "Waktu Tidak Cukup", "Not Visit", false, false));
        mList.add(new Reason("4", "Other", "Not Visit", false, true));
        mList.add(new Reason("5", "Toko Ramai", "Pause", false, true));
        mList.add(new Reason("6", "Pemilik Sedang Keluar", "Pause", false, false));
        mList.add(new Reason("7", "Pemilik Sibuk", "Pause", false, true));
        mList.add(new Reason("8", "Other", "Pause", true, false));
        mList.add(new Reason("9", "Barang Masih Banyak", "Not Buy", false, true));
        mList.add(new Reason("10", "Barang Expired", "Return", false, true));
        mList.add(new Reason("11", "Toko Bangkrut", "Return", false, true));

        for (Reason reason : mList) {
            database.addMasterReason(reason, username);
        }
    }

    private void setBank(String username) {
        List<Bank> mList = new ArrayList<>();
        mList.add(new Bank("BCA", "Bank Central Asia", "Bank Customer", "", false));
        mList.add(new Bank("Mandiri", "Mandiri", "Bank Customer", "", false));
        mList.add(new Bank("BRI", "BRI", "Bank Customer", "", false));
        mList.add(new Bank("100RP008", "BCA", "Bank ASPP", "0700006609387", false));
        mList.add(new Bank("100RP014", "MADIRI", "Bank ASPP", "0013020008", false));
        mList.add(new Bank("201RP014", "BCA ASEMKA", "Bank ASPP", "0013037601", false));
        mList.add(new Bank("202RP014", "BCA ASEMKA", "Bank ASPP", "0017288788", false));

        for (Bank param : mList) {
            database.addMasterBank(param, username);
        }
    }

    private void showDialogSetting() {
        BottomSheetDialog dialog = new BottomSheetDialog(LoginActivity.this, R.style.SheetDialog);
        dialog.setContentView(R.layout.aspp_dialog_bottom_setting);

        LinearLayout layoutChangeIP = dialog.findViewById(R.id.layoutChangeIP);
        LinearLayout layoutRestoreDB = dialog.findViewById(R.id.layoutRestoreDB);

        layoutChangeIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        layoutRestoreDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                setToast("Restore DB");
            }
        });

        dialog.show();
    }

    private void initialize() {
        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);
        imgShowPassword = findViewById(R.id.imgShowPassword);
        txtVersion = findViewById(R.id.txtVersion);
        login = findViewById(R.id.btnLogIn);
        txtSettings = findViewById(R.id.txtSettings);

        if (Helper.getItemParam(Constants.REGIISTERID) == null) {
            try {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                String refreshedToken = task.getResult();
//                                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                                Helper.setItemParam(Constants.REGIISTERID, refreshedToken);
                                registerID = refreshedToken;

                                // Log and toast
//                                String msg = getString(R.string.msg_token_fmt, token);
//                                Log.d(TAG, msg);
//                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), getString(R.string.getTokenError), Toast.LENGTH_LONG).show();
            }
        } else {
            registerID = Helper.getItemParam(Constants.REGIISTERID).toString();
        }
    }

    private void deleteHelper() {
        Helper.removeItemParam(Constants.CURRENTPAGE);
        Helper.removeItemParam(Constants.USER_DETAIL);
        Helper.removeItemParam(Constants.ROUTE_CUSTOMER_HEADER);
    }

    public String generateUniqueIdentifier() throws Exception {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private class RequestUrl extends AsyncTask<Void, Void, LoginResponse> {

        @Override
        protected LoginResponse doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_LOGIN = Constants.OAUTH_TOKEN_PATH;
                    String grantType = Constants.GRANT_TYPE + "&";
                    String email1 = Constants.USERNAME.concat("=") + userId + "&";
                    String pwd = Constants.PASSWORD.concat("=") + password;
                    final String content = grantType.concat(email1).concat(pwd);
                    final String url = Constants.URL.concat(URL_LOGIN).concat(Constants.QUESTION).concat(content);
                    return (LoginResponse) NetworkHelper.postWebserviceLogin(url, LoginResponse.class, null);
                } else if (PARAM == 2) {
                    String URL_ = Constants.API_GET_USER_DETAIL;
                    User param = new User();
                    param.setUsername(userId);
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    messageResponse = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, param);
                    return null;
                } else {
                    String imei = null;
                    try {
                        imei = generateUniqueIdentifier();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LoginResponse response = new LoginResponse();
                    response = Helper.ObjectToGSON(messageResponse.getResult(), LoginResponse.class);

                    User userResponse = Helper.ObjectToGSON(response.getUser(), User.class);
                    userResponse.setUserLogin(userId);
                    userResponse.setImei(imei);
                    userResponse.setToken(loginResponse.getAccess_token());
                    userResponse.setRegis_id(registerID);
                    userResponse.setRadius(response.getRadius());

                    List<Reason> reasonList = new ArrayList<>();
                    Reason[] paramArray = Helper.ObjectToGSON(response.getListReason(), Reason[].class);
                    Collections.addAll(reasonList, paramArray);

                    for (Reason reason : reasonList) {
                        database.addMasterReason(reason, userId);
                    }

                    List<Bank> bankList = new ArrayList<>();
                    Bank[] paramArray1 = Helper.ObjectToGSON(response.getListBank(), Bank[].class);
                    Collections.addAll(bankList, paramArray1);

                    for (Bank param : bankList) {
                        database.addMasterBank(param, userId);
                    }

                    SessionManagerQubes.setUserProfile(userResponse);
                    SessionManagerQubes.setImei(imei);
                    setData = true;
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("LoginActivity", ex.getMessage());
                }
                if (PARAM == 3) {
                    setData = false;
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
            if (Helper.getItemParam(Constants.NO_CONNECTION) != null) {
                progress.dismiss();
                showDialogInformation(LoginActivity.this, getString(R.string.noConnection));
            } else {
                if (PARAM == 1) {
                    if (logins != null) {
                        if (String.valueOf(logins.getError()).equals("invalid_grant")) {
                            progress.dismiss();
                            setToast(getString(R.string.wrongUser));
                        } else {
                            loginResponse = logins;
                            Helper.setItemParam(Constants.TOKEN, logins.getAccess_token());
                            PARAM = 2;
                            new RequestUrl().execute();
                        }
                    } else {
                        progress.dismiss();
                        if (Helper.getItemParam(Constants.ERROR_LOGIN) != null) {
                            setToast(getString(R.string.wrongUser));
                            Helper.removeItemParam(Constants.ERROR_LOGIN);
                        } else {
                            setToast(getString(R.string.errCon));
                        }
                    }
                } else if (PARAM == 2) {
                    if (messageResponse != null) {
                        PARAM = 3;
                        new RequestUrl().execute();
                    } else {
                        progress.dismiss();
                        setToast(getString(R.string.failedGetData));
                    }
                } else {
                    progress.dismiss();
                    if (setData) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        setToast(getString(R.string.failedGetData));
                    }
                }
            }
        }
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
}

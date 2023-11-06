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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.qualitas.qubes.BuildConfig;
import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.SettingActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Bank;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.CustomerType;
import id.co.qualitas.qubes.model.DaerahTingkat;
import id.co.qualitas.qubes.model.DepoRegion;
import id.co.qualitas.qubes.model.LoginResponse;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Parameter;
import id.co.qualitas.qubes.model.PriceCode;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.SalesPriceDetail;
import id.co.qualitas.qubes.model.SalesPriceHeader;
import id.co.qualitas.qubes.model.Uom;
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
    private boolean saveDataSuccess = false;
    protected Database database;
    protected ProgressDialog progress;
    protected int PARAM = 0;
    private User userResponse;

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
                showDialogSetting();
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
//                setDataDummyUser();
                progress.show();
                PARAM = 1;
                new RequestUrl().execute();
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
    }

    private void setDataDummyUser() {
        String jsonFileString = NetworkHelper.getJsonFromAssets(getApplicationContext(), "detailUser.json");
        Gson gson = new Gson();
        Type resultType = new TypeToken<WSMessage>(){}.getType();

        WSMessage result = gson.fromJson(jsonFileString, resultType);
        if (result != null) {
            LoginResponse response = Helper.ObjectToGSON(result.getResult(), LoginResponse.class);
            userResponse = Helper.ObjectToGSON(response.getUserDetail(), User.class);
            userResponse.setUserLogin(userId);
            userResponse.setImei(Helper.getImei(getApplicationContext()));
//            userResponse.setToken(loginResponse.getAccess_token());
            userResponse.setRegis_id(registerID);

            List<DepoRegion> arrayList = new ArrayList<>();
            DepoRegion[] paramArray = Helper.ObjectToGSON(response.getDepoRegion(), DepoRegion[].class);
            Collections.addAll(arrayList, paramArray);
            userResponse.setDepoRegionList(arrayList);
            SessionManagerQubes.setUserProfile(userResponse);
            SessionManagerQubes.setImei(Helper.getImei(getApplicationContext()));
            setDataDummyMaster();
        }else{
            setToast("failed");
        }
    }

    private void setDataDummyMaster() {
        String jsonFileString = NetworkHelper.getJsonFromAssets(getApplicationContext(), "masterData.json");
        Gson gson = new Gson();
        Type resultType = new TypeToken<WSMessage>(){}.getType();

        WSMessage result = gson.fromJson(jsonFileString, resultType);
        if (result != null) {
            Map response = (Map) result.getResult();

            List<Reason> reasonList = new ArrayList<>();
            Reason[] paramArray = Helper.ObjectToGSON(response.get("listReason"), Reason[].class);
            Collections.addAll(reasonList, paramArray);
            database.deleteMasterReason();
            for (Reason reason : reasonList) {
                database.addMasterReason(reason, userId);
            }

            List<Bank> bankList = new ArrayList<>();
            Bank[] paramArray1 = Helper.ObjectToGSON(response.get("listBank"), Bank[].class);
            Collections.addAll(bankList, paramArray1);
            database.deleteMasterBank();
            for (Bank param : bankList) {
                database.addMasterBank(param, userId);
            }

            List<DaerahTingkat> daerahTingkatList = new ArrayList<>();
            DaerahTingkat[] paramArray3 = Helper.ObjectToGSON(response.get("listDaerahTingkat"), DaerahTingkat[].class);
            Collections.addAll(daerahTingkatList, paramArray3);
            database.deleteMasterDaerahTingkat();
            for (DaerahTingkat param : daerahTingkatList) {
                database.addMasterDaerahTingkat(param, userId);
            }

            List<Material> materialList = new ArrayList<>();
            Material[] paramArray4 = Helper.ObjectToGSON(response.get("listMaterial"), Material[].class);
            Collections.addAll(materialList, paramArray4);
            database.deleteMasterMaterial();
            for (Material param : materialList) {
                database.addMasterMaterial(param, userId);
            }

            List<Uom> uomList = new ArrayList<>();
            Uom[] paramArray5 = Helper.ObjectToGSON(response.get("listUom"), Uom[].class);
            Collections.addAll(uomList, paramArray5);
            database.deleteMasterUom();
            for (Uom param : uomList) {
                database.addMasterUom(param, userId);
            }

            List<PriceCode> priceList = new ArrayList<>();
            PriceCode[] paramArray6 = Helper.ObjectToGSON(response.get("listPriceCode"), PriceCode[].class);
            Collections.addAll(priceList, paramArray6);
            database.deleteMasterPriceCode();
            for (PriceCode param : priceList) {
                database.addMasterPriceCode(param, userId);
            }

            List<SalesPriceHeader> salesPriceHeaderList = new ArrayList<>();
            SalesPriceHeader[] paramArray7 = Helper.ObjectToGSON(response.get("listSalesPriceHeader"), SalesPriceHeader[].class);
            Collections.addAll(salesPriceHeaderList, paramArray7);
            database.deleteMasterSalesPriceHeader();
            for (SalesPriceHeader param : salesPriceHeaderList) {
                database.addMasterSalesPriceHeader(param, userId);
            }

            List<SalesPriceDetail> salesPriceDetailList = new ArrayList<>();
            SalesPriceDetail[] paramArray8 = Helper.ObjectToGSON(response.get("listSalesPriceDetail"), SalesPriceDetail[].class);
            Collections.addAll(salesPriceDetailList, paramArray8);
            database.deleteMasterSalesPriceDetail();
            for (SalesPriceDetail param : salesPriceDetailList) {
                database.addMasterSalesPriceDetail(param, userId);
            }

            List<Parameter> parameterList = new ArrayList<>();
            Parameter[] paramArray9 = Helper.ObjectToGSON(response.get("parameter"), Parameter[].class);
            Collections.addAll(parameterList, paramArray9);
            database.deleteMasterParameter();
            for (Parameter param : parameterList) {
                database.addMasterParameter(param, userId);
            }

            List<CustomerType> cusTypeList = new ArrayList<>();
            CustomerType[] paramArray10 = Helper.ObjectToGSON(response.get("listCustomerType"), CustomerType[].class);
            Collections.addAll(cusTypeList, paramArray10);
            database.deleteMasterCustomerType();
            for (CustomerType param : cusTypeList) {
                database.addMasterCustomerType(param, userId);
            }

            List<Material> minOrderList = new ArrayList<>();
            Material[] paramArray11 = Helper.ObjectToGSON(response.get("listMinimalOrder"), Material[].class);
            Collections.addAll(minOrderList, paramArray11);
            database.deleteMasterMinimalOrder();
            for (Material param : minOrderList) {
                database.addMinimalOrder(param, userId);
            }

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            setToast("failed");
        }
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
                    String URL_ = Constants.API_USER_DETAIL_GET;
                    User param = new User();
                    param.setUsername(userId);
                    param.setImei(Helper.getImei(getApplicationContext()));
                    param.setRegis_id(registerID);
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    messageResponse = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, param);
                    return null;
                } else if (PARAM == 3) {
                    LoginResponse response = Helper.ObjectToGSON(messageResponse.getResult(), LoginResponse.class);
                    if (response.getUserDetail() != null) {
                        userResponse = Helper.ObjectToGSON(response.getUserDetail(), User.class);
                        userResponse.setUserLogin(userId);
                        userResponse.setImei(Helper.getImei(getApplicationContext()));
                        userResponse.setToken(loginResponse.getAccess_token());
                        userResponse.setRegis_id(registerID);

                        List<DepoRegion> arrayList = new ArrayList<>();
                        DepoRegion[] paramArray = Helper.ObjectToGSON(response.getDepoRegion(), DepoRegion[].class);
                        Collections.addAll(arrayList, paramArray);
                        userResponse.setDepoRegionList(arrayList);
                        SessionManagerQubes.setUserProfile(userResponse);
                        SessionManagerQubes.setImei(Helper.getImei(getApplicationContext()));
                        saveDataSuccess = true;
                    } else {
                        saveDataSuccess = false;
                    }
                    return null;
                } else if (PARAM == 4) {
                    String URL_ = Constants.API_MASTER_DATA_GET;
                    User param = new User();
                    param.setUsername(userId);
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    messageResponse = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, userResponse);
                    return null;
                } else {
                    Map response = (Map) messageResponse.getResult();

                    List<Reason> reasonList = new ArrayList<>();
                    Reason[] paramArray = Helper.ObjectToGSON(response.get("listReason"), Reason[].class);
                    Collections.addAll(reasonList, paramArray);
                    database.deleteMasterReason();
                    for (Reason reason : reasonList) {
                        database.addMasterReason(reason, userId);
                    }

                    List<Bank> bankList = new ArrayList<>();
                    Bank[] paramArray1 = Helper.ObjectToGSON(response.get("listBank"), Bank[].class);
                    Collections.addAll(bankList, paramArray1);
                    database.deleteMasterBank();
                    for (Bank param : bankList) {
                        database.addMasterBank(param, userId);
                    }

                    List<DaerahTingkat> daerahTingkatList = new ArrayList<>();
                    DaerahTingkat[] paramArray3 = Helper.ObjectToGSON(response.get("listDaerahTingkat"), DaerahTingkat[].class);
                    Collections.addAll(daerahTingkatList, paramArray3);
                    database.deleteMasterDaerahTingkat();
                    for (DaerahTingkat param : daerahTingkatList) {
                        database.addMasterDaerahTingkat(param, userId);
                    }

                    List<Material> materialList = new ArrayList<>();
                    Material[] paramArray4 = Helper.ObjectToGSON(response.get("listMaterial"), Material[].class);
                    Collections.addAll(materialList, paramArray4);
                    database.deleteMasterMaterial();
                    for (Material param : materialList) {
                        database.addMasterMaterial(param, userId);
                    }

                    List<Uom> uomList = new ArrayList<>();
                    Uom[] paramArray5 = Helper.ObjectToGSON(response.get("listUom"), Uom[].class);
                    Collections.addAll(uomList, paramArray5);
                    database.deleteMasterUom();
                    for (Uom param : uomList) {
                        database.addMasterUom(param, userId);
                    }

                    List<PriceCode> priceList = new ArrayList<>();
                    PriceCode[] paramArray6 = Helper.ObjectToGSON(response.get("listPriceCode"), PriceCode[].class);
                    Collections.addAll(priceList, paramArray6);
                    database.deleteMasterPriceCode();
                    for (PriceCode param : priceList) {
                        database.addMasterPriceCode(param, userId);
                    }

                    List<SalesPriceHeader> salesPriceHeaderList = new ArrayList<>();
                    SalesPriceHeader[] paramArray7 = Helper.ObjectToGSON(response.get("listSalesPriceHeader"), SalesPriceHeader[].class);
                    Collections.addAll(salesPriceHeaderList, paramArray7);
                    database.deleteMasterSalesPriceHeader();
                    for (SalesPriceHeader param : salesPriceHeaderList) {
                        database.addMasterSalesPriceHeader(param, userId);
                    }

                    List<SalesPriceDetail> salesPriceDetailList = new ArrayList<>();
                    SalesPriceDetail[] paramArray8 = Helper.ObjectToGSON(response.get("listSalesPriceDetail"), SalesPriceDetail[].class);
                    Collections.addAll(salesPriceDetailList, paramArray8);
                    database.deleteMasterSalesPriceDetail();
                    for (SalesPriceDetail param : salesPriceDetailList) {
                        database.addMasterSalesPriceDetail(param, userId);
                    }

                    List<Parameter> parameterList = new ArrayList<>();
                    Parameter[] paramArray9 = Helper.ObjectToGSON(response.get("parameter"), Parameter[].class);
                    Collections.addAll(parameterList, paramArray9);
                    database.deleteMasterParameter();
                    for (Parameter param : parameterList) {
                        database.addMasterParameter(param, userId);
                    }

                    List<CustomerType> cusTypeList = new ArrayList<>();
                    CustomerType[] paramArray10 = Helper.ObjectToGSON(response.get("listCustomerType"), CustomerType[].class);
                    Collections.addAll(cusTypeList, paramArray10);
                    database.deleteMasterCustomerType();
                    for (CustomerType param : cusTypeList) {
                        database.addMasterCustomerType(param, userId);
                    }

                    SessionManagerQubes.setUserProfile(userResponse);

                    saveDataSuccess = true;
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("LoginActivity", ex.getMessage());
                }
                if (PARAM == 3 || PARAM == 5) {
                    saveDataSuccess = false;
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
                            SessionManagerQubes.setToken(logins.getAccess_token());
                            PARAM = 2;
                            new RequestUrl().execute();//2
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
                        if (messageResponse.getIdMessage() == 1) {
                            PARAM = 3;
                            new RequestUrl().execute();//f75c38b1ea8e2416, 3
                        } else {
                            progress.dismiss();
                            setToast(getString(R.string.failedGetData));
                        }
                    } else {
                        progress.dismiss();
                        setToast(getString(R.string.failedGetData));
                    }
                } else if (PARAM == 3) {
                    if (saveDataSuccess) {
                        saveDataSuccess = false;
                        PARAM = 4;
                        new RequestUrl().execute();//4
                    } else {
                        progress.dismiss();
                        setToast(getString(R.string.failedSaveData));
                    }
                } else if (PARAM == 4) {
                    if (messageResponse != null) {
                        PARAM = 5;
                        new RequestUrl().execute();//5
                    } else {
                        progress.dismiss();
                        setToast(getString(R.string.failedGetData));
                    }
                } else {
                    progress.dismiss();
                    if (saveDataSuccess) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        setToast(getString(R.string.failedSaveData));
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

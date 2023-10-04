package id.co.qualitas.qubes.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Date;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.ChangePasswordRequest;
import id.co.qualitas.qubes.model.LoginPenggantiRequest;
import id.co.qualitas.qubes.model.LoginResponse;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.OffDate;
import id.co.qualitas.qubes.model.OfflineLoginData;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManager;

public class LoginPenggantiActivity extends BaseActivity {
    Button login;
    String txtSalesman, txtSupervisor, txtPassword;
    EditText nikSalesman, nikSupervisor, inputPassword;
    private TextView loginUser, ipChanger;
    private String[] itemReason = {"Sakit", "Cuti", "Reason"};
    private User user;
    LoginPenggantiRequest loginPenggantiRequest;
    private MessageResponse messageResponse;
    private Spinner reason;
    private OfflineLoginData offlineData;
    private OffDate offDate;
    private String registerID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pengganti);

        initProgress();
        initialize();

        ipChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initialize(){
        db = new DatabaseHelper(getApplicationContext());
        login = findViewById(R.id.btnSave);
        loginUser = findViewById(R.id.loginUser);
        nikSalesman = findViewById(R.id.nikSalesman);
        nikSupervisor = findViewById(R.id.nikSupervisor);
        inputPassword = findViewById(R.id.password);
        ipChanger = findViewById(R.id.ipChanger);

        reason = findViewById(R.id.spinnerReason);

//        if(getScreenOrientation() == 0){
//            setCustomSpinnerLogin(itemReason, reason);
//        }else if(getScreenOrientation() == 1){
            setCustomSpinnerLoginB(itemReason, reason);
//        }

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
        txtSalesman = nikSalesman.getText().toString().trim();
        txtSupervisor = nikSupervisor.getText().toString().trim();
        txtPassword = inputPassword.getText().toString().trim();

        if (txtSalesman.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pleaseFillUsername), Toast.LENGTH_SHORT).show();
        }else if (txtSupervisor.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pleaseFillSupervisor), Toast.LENGTH_SHORT).show();
        } else if (txtPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pleaseFillUsername), Toast.LENGTH_SHORT).show();
        } else if(reason.getSelectedItemPosition() == 2){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pleaseChoose), Toast.LENGTH_SHORT).show();
        }else {
            PARAM = 1;
            new RequestUrl().execute();
            progress.show();
        }
    }

    private class RequestUrl extends AsyncTask<Void, Void, LoginResponse> {

        @Override
        protected LoginResponse doInBackground(Void... voids) {
            try {
                if(PARAM == 1){
                    String URL_GET_REPLACEMENT_CHECK = Constants.API_GET_REPLACEMENT_CHECK;

                    loginPenggantiRequest = new LoginPenggantiRequest();
                    loginPenggantiRequest.setSalesman(txtSalesman);
                    loginPenggantiRequest.setSupervisor(txtSupervisor);
                    loginPenggantiRequest.setIdClient(Constants.CLIENT_ID);

                    final String url = Constants.URL.concat(URL_GET_REPLACEMENT_CHECK);
                    messageResponse = (MessageResponse) Helper.postWebserviceWithBodyWithoutHeaders(url, MessageResponse.class, loginPenggantiRequest);
                    return null;
                }else if (PARAM == 2) {
                    String URL_LOGIN = Constants.OAUTH_TOKEN_PATH;

                    String pwd = Constants.AND.concat(Constants.TXTPASSWORD).concat(Constants.EQUALS) + txtPassword;

                    String username = Constants.USERNAME.concat(Constants.EQUALS) + txtSupervisor.concat(Constants.PIPE_).concat(Constants.CLIENT_ID);

                    String grantType = Constants.GRANT_TYPE.concat(Constants.EQUALS) + Constants.TXTPASSWORD + Constants.AND;

                    final String url = Constants.URL.concat(URL_LOGIN);
                    final String content = grantType.concat(username).concat(pwd);
                    return (LoginResponse) Helper.postWebserviceLogin(url, LoginResponse.class, content);
                } else if (PARAM == 3){
                    String URL_GET_DETAIL = Constants.API_GET_DETAIL_USER;

                    String username = Constants.QUESTION.concat(Constants.USERNAME.concat(Constants.EQUALS)) + txtSalesman.concat(Constants.PIPE_).concat(Constants.CLIENT_ID);
                    final String url = Constants.URL.concat(URL_GET_DETAIL).concat(username);
                    user = (User) Helper.getWebservice(url, User.class);
                    return null;
                }else if (PARAM == 4){
                    String URL_GET_OFFLINE_DATA = Constants.API_GET_OFFLINE_LOGIN;

//                    final String url = Constants.URL.concat(URL_GET_OFFLINE_DATA).
//                            concat(Constants.QUESTION).concat(Constants.ID_EMPLOYEE).concat(Constants.EQUALS).concat(user.getIdEmployee());

                    final String url = Constants.URL.concat(URL_GET_OFFLINE_DATA).
                            concat(Constants.QUESTION).concat(Constants.ID_EMPLOYEE).concat(Constants.EQUALS).concat(user.getIdEmployee())
                            .concat(Constants.AND).concat(Constants.VERSION).concat(Constants.EQUALS).concat(Constants.CURRENT_VERSION);

                    offlineData = (OfflineLoginData) Helper.getWebservice(url, OfflineLoginData.class);
                    return null;
                }else if (PARAM == 5) {
                    final String url = Constants.URL.concat(Constants.API_CHECKING_USER);

                    ChangePasswordRequest loginRequest = new ChangePasswordRequest();
                    loginRequest.setUsername(txtSalesman + getString(R.string.clientId));
                    loginRequest.setSupervisor(txtSupervisor + getString(R.string.clientId));
                    loginRequest.setRegisId(registerID);
                    loginRequest.setPassword(txtPassword);

                    messageResponse = (MessageResponse) Helper.postWebserviceWithBodyWithoutHeaders(url, MessageResponse.class, loginRequest);//post
                    return null;
                } else {
                    String URL_GET_DETAIL = Constants.API_UPDATE_REGIS;

                    final String url = Constants.URL.concat(URL_GET_DETAIL);

                    ChangePasswordRequest loginRequest = new ChangePasswordRequest();
                    loginRequest.setUsername(txtSalesman + getString(R.string.clientId));
                    loginRequest.setRegisId(registerID);

                    user = (User) Helper.postWebserviceWithBody(url, User.class, loginRequest);//post
                    return null;
                }
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
        }

        @Override
        protected void onPostExecute(LoginResponse logins) {
            progress.dismiss();
            if(PARAM == 1){
                if (messageResponse != null) {
                    if (messageResponse.getIdMessage() == 0) {
                        Toast.makeText(getApplicationContext(), messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        user = new User();
                        user.setSupervisorNik(messageResponse.getResult().getSupervisorNik());
                        user.setSupervisorName(messageResponse.getResult().getSupervisorName());
                        user.setSupervisorPosition(messageResponse.getResult().getSupervisorPosition());

                        Helper.setItemParam(Constants.SUPERVISOR_DETAIL, user);

                        PARAM = 5;
                        new RequestUrl().execute();
                        progress.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }else if (PARAM == 2) {
                if (logins != null) {
                    if (String.valueOf(logins.getError()).equals(Constants.INVALID_GRANT)) {

                        Toast.makeText(getApplicationContext(), getString(R.string.wrongUser), Toast.LENGTH_SHORT).show();
                    } else {
                        Helper.setItemParam(Constants.LOGIN, logins);
                        Helper.setItemParam(Constants.TOKEN, logins.getAccess_token());

                        PARAM = 6;
                        new RequestUrl().execute();
                        progress.show();
                    }
                } else {
                    if (Helper.getItemParam(Constants.ERROR_LOGIN) != null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.wrongUser), Toast.LENGTH_SHORT).show();
                        Helper.removeItemParam(Constants.ERROR_LOGIN);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.errCon), Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (PARAM == 3) {
                if (user != null) {

                    user.setSupervisorNik(((User)Helper.getItemParam(Constants.SUPERVISOR_DETAIL)).getSupervisorNik());
                    user.setSupervisorName(((User)Helper.getItemParam(Constants.SUPERVISOR_DETAIL)).getSupervisorName());
                    user.setSupervisorPosition(((User)Helper.getItemParam(Constants.SUPERVISOR_DETAIL)).getSupervisorPosition());
                    user.setReason(reason.getSelectedItemPosition());

                    Helper.setItemParam(Constants.USER_DETAIL, user);

                    if(!db.isLogin(txtSupervisor)){
                        PARAM = 4;
                        new RequestUrl().execute();
                        progress.show();
                    }else{
                        Intent intent = new Intent(LoginPenggantiActivity.this, MainActivityDrawer.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.errorData), Toast.LENGTH_SHORT).show();
                }
            }else if(PARAM == 4){
                if (offlineData != null) {
                    if(offlineData.getAdditionalInfo() != null){
                        if(offlineData.getAdditionalInfo().getIdPlant() != null){
                            user.setIdPlant(offlineData.getAdditionalInfo().getIdPlant());
                        }
                    }
                   /*Table Material*/
                    db.deleteMaterial();
                    db.addMaterialN(offlineData.getListMaterial());

                    /*Table UOM*/
                    db.deleteUOM();
                    db.addUOMN(offlineData.getListUOm());
                    /*Table Outlet*/
                    db.deleteOutlet();
                    if (offlineData.getListOutlet() != null && !offlineData.getListOutlet().isEmpty()) {
                            for (OutletResponse outlet : offlineData.getListOutlet()) {
                                outlet.setEnabled(true);
                                db.addOutletNew(outlet);
                            }
                    }
                    /*Table Visit Plan*/
                    db.deleteVisitPlan();
                    db.addVisitPlanN(offlineData.getListVisitPlan());

                    /*Table Order Plan*/
                    db.deleteOrderPlan();
                    db.addOrderPlanN(offlineData.getListOrderPlan());

                    /*Table TOP*/
                    db.addTOPN(offlineData.getListTop());

                    /*Table Jenis Jual*/
                    db.deleteJenisJual();
                    if (offlineData.getListJenisJual() != null && !offlineData.getListJenisJual().isEmpty()) {
                        for (int i = 0; i < offlineData.getListJenisJual().size(); i++) {
                            db.addJenisJual(offlineData.getListJenisJual().get(i));
                        }
                    }

                    /*Table Sales Office*/
                    db.addSalesOfficeN(offlineData.getListSalesOffice());

                    db.addPartnerN(offlineData.getListPartner());

                    db.addStoreCheckN(offlineData.getListStore());

                    db.deleteToHeader();
                    if (offlineData.getListOrderHeader() != null && !offlineData.getListOrderHeader().isEmpty()) {
                        for (int i = 0; i < offlineData.getListOrderHeader().size(); i++) {
                            if (offlineData.getListOrderHeader().get(i).getSalesOffice() != null) {
                                offlineData.getListOrderHeader().get(i).setSalesOfficeName(db.getSalesOfficeName(offlineData.getListOrderHeader().get(i).getSalesOffice()));
                            }


                            if(offlineData.getListOrderHeader().get(i).getSignature() != null){
                                offlineData.getListOrderHeader().get(i).setSignatureString(offlineData.getListOrderHeader().get(i).getSignature());
                            }
                            if(offlineData.getListOrderHeader().get(i).getPhoto() != null){
                                offlineData.getListOrderHeader().get(i).setPhotoString(offlineData.getListOrderHeader().get(i).getPhoto());
                            }
                            db.addOrderHeader(offlineData.getListOrderHeader().get(i));
                        }
                    }

                    db.deleteOrderDetail();
                    if (offlineData.getListOrderDetail() != null && !offlineData.getListOrderDetail().isEmpty()) {
                        for (int i = 0; i < offlineData.getListOrderDetail().size(); i++) {
                            db.addOrderDetail(offlineData.getListOrderDetail().get(i));
                        }
                    }

                    db.deleteToPrice();
                    if (offlineData.getListToPrice() != null && !offlineData.getListToPrice().isEmpty()) {
                        for (int i = 0; i < offlineData.getListToPrice().size(); i++) {
                            db.addToPrice(offlineData.getListToPrice().get(i));
                        }
                    }

                    db.deleteReason();
                    if (offlineData.getListReasonReturn() != null && !offlineData.getListReasonReturn().isEmpty()) {
                        for (int i = 0; i < offlineData.getListReasonReturn().size(); i++) {
                            db.addReason(offlineData.getListReasonReturn().get(i));
                        }
                    }

                    db.deleteReturnHeader();
                    if (offlineData.getListReturnHeader() != null && !offlineData.getListReturnHeader().isEmpty()) {
                        for (int i = 0; i < offlineData.getListReturnHeader().size(); i++) {
                            db.addReturnHeader(offlineData.getListReturnHeader().get(i));
                        }
                    }

                    db.deleteReturnDetail();
                    if (offlineData.getListReturnDetail() != null && !offlineData.getListReturnDetail().isEmpty()) {
                        for (Return returnDetail:offlineData.getListReturnDetail()) {
                            if(returnDetail.getReason() != null){
                                String idReason = returnDetail.getReason();
                                returnDetail.setReason(db.getReasonById(idReason).getDesc());
                                returnDetail.setCategory(db.getReasonById(idReason).getType());
                            }
                            db.addReturnDetail(returnDetail);
                        }
                    }

                /*FreeGoods*/
                    db.addFreeGoods(offlineData.getListFreeGoods());

                    if(offlineData.getDatetimeNow() != null){
                        Date curDate = Helper.convertStringtoDate(Constants.DATE_TYPE_16, offlineData.getDatetimeNow());
                        Long elapse = SystemClock.elapsedRealtime();
                        offDate = new OffDate();
                        offDate.setCurDate(offlineData.getDatetimeNow());
                        offDate.setElapseTime(elapse);

                        SecureDate.getInstance().initServerDate(curDate);
                        db.deleteAttendance();

                        user.setDateTimeNow(offlineData.getDatetimeNow());
                        user.setmElapsedRealTime(SystemClock.elapsedRealtime());
                        if(user.getSupervisorNik() != null){
                            user.setNik(user.getSupervisorNik());
                        }
                        db.addAttendance(user);

                        String ex = Helper.objectToString(offDate);
                        new SessionManager(getApplicationContext()).createDateSession(ex);
                    }

                    Helper.setItemParam(Constants.CURDATE, offlineData.getDatetimeNow());
                    Intent intent = new Intent(LoginPenggantiActivity.this, MainActivityDrawer.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else if (PARAM == 5) {
                if (messageResponse != null) {
                    if (messageResponse.getIdMessage() == 0) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.wrongUser), Toast.LENGTH_SHORT).show();
                    } else if (messageResponse.getIdMessage() == 1) {
                        PARAM = 2;
                        new RequestUrl().execute();
                    } else if (messageResponse.getIdMessage() == 2) {
                        progress.dismiss();
                        LayoutInflater inflater = LayoutInflater.from(LoginPenggantiActivity.this);
                        View dialogview = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                        final Dialog alertDialog = new Dialog(LoginPenggantiActivity.this);
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
                                PARAM = 2;
                                new RequestUrl().execute();
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
                PARAM = 3;
                new RequestUrl().execute();
            }
        }
    }
}

package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import id.co.qualitas.qubes.BuildConfig;
import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.activity.NewMainActivity;
import id.co.qualitas.qubes.activity.SettingActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;

public class LoginActivity extends BaseActivity {
    private Button login;
    private TextView ipChanger, txtQubes, txtQubesVersion;
    private String registerID;
    boolean showPassword = false;
    private ImageView imgShowPassword;
    private EditText input_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_login);

        init();
        initialize();

        txtQubesVersion.setText("Version " + String.valueOf(BuildConfig.VERSION_NAME));

        ipChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSetting();
            }
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
        imgShowPassword = findViewById(R.id.imgShowPassword);
        txtQubesVersion = findViewById(R.id.txtQubesVersion);
        txtQubes = findViewById(R.id.txtQubes);
        login = findViewById(R.id.btnSave);
        ipChanger = findViewById(R.id.ipChanger);
        db = new DatabaseHelper(getApplicationContext());

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

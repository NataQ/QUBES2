package id.co.qualitas.qubes.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.session.SessionManager;


public class SettingActivity extends AppCompatActivity {
    private EditText edtIp;
    private String ip;
    private TextView appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        edtIp = (EditText) findViewById(R.id.edtIP);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        appVersion = findViewById(R.id.appVersion);

        appVersion.setText(Constants.APP_VERSION);

        if(Helper.getItemParam(Constants.URL) != null){
            String urltemp = Helper.getItemParam(Constants.URL).toString();
            String temp[] = urltemp.split("/");
            edtIp.setText(temp[2]);
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ip = edtIp.getText().toString().trim();
                if (ip.isEmpty() || ip == null) {
                    Snackbar.make(view, R.string.pleasefillipaddress, Snackbar.LENGTH_SHORT).show();
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

                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Constants.IP = edtIp.getText().toString();
                            Constants.URL = "http://" + Constants.IP + "/";
                            new SessionManager(
                                    getApplicationContext())
                                    .createUrlSession(Constants.URL);
                            Helper.setItemParam(
                                    Constants.URL,
                                    Constants.URL);
                            Toast.makeText(getApplicationContext(),
                                    R.string.ipaddress_has_been_changed,
                                    Toast.LENGTH_LONG).show();
                            onBackPressed();
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
    }

}

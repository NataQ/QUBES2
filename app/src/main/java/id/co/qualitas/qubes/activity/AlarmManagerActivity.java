package id.co.qualitas.qubes.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.ServiceConnection;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
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
import id.co.qualitas.qubes.services.NotiWorker;
import id.co.qualitas.qubes.session.SessionManager;

public class AlarmManagerActivity extends BaseActivity {
    private static final String TAG = AlarmManagerActivity.class.getSimpleName();

    private Button buttonStart, buttonStop;
    private TextView textViewthreadCount;
    int count = 0;

    WorkManager workManager;
    private WorkRequest workRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);
        Log.i(getString(R.string.service_demo_tag), "MainActivity thread id: " + Thread.currentThread().getId());

        buttonStart = findViewById(R.id.buttonThreadStarter);
        buttonStop = findViewById(R.id.buttonStopthread);
        textViewthreadCount = findViewById(R.id.textViewthreadCount);

        workManager = WorkManager.getInstance(getApplicationContext());

        buttonStart.setOnClickListener(v -> {
//            new RequestUrl().execute();
            workRequest = new PeriodicWorkRequest.Builder(NotiWorker.class, 15, TimeUnit.MINUTES).build();
            workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, (PeriodicWorkRequest) workRequest);
        });

        buttonStop.setOnClickListener(v -> {
            workManager.cancelAllWork();
//            workManager.cancelWorkById(workRequest.getId());
        });

    }

    private class RequestUrl extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String url = "http://192.168.1.8:9191/api/v1/doubleLogin/syncData";
                User user = new User();
                user.setUsername("mobile");
                return (Boolean) Helper.postWebserviceWithBody(url, Boolean.class, user);//post
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e(Constants.LOGIN_ACTIVITY, ex.getMessage());
                }
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean logins) {
            setToast(logins ? "true" : "false");
        }
    }
}

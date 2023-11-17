package id.co.qualitas.qubes.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManager;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class NotiWorker extends Worker {
    Context context;
    WorkerParameters workerParams;

    private boolean mIsRandomGeneratorOn;
    private int mRandomNumber;

    public NotiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.workerParams = workerParams;
        mIsRandomGeneratorOn = true;
    }

    private void startRandomNumberGenerator() {
        int i = 0;
        while (i < 5 && !isStopped()) {
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = i + 8;
                    Log.e(context.getString(R.string.service_demo_tag), "Thread id: " + Thread.currentThread().getId() + ", Random Number: " + mRandomNumber);
                    i++;
                }
            } catch (InterruptedException e) {
                Log.i(context.getString(R.string.service_demo_tag), "Thread Interrupted");
            }
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        WSMessage result = new WSMessage();
        String url = null;
        if (!isStopped())
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
            setSession();
        url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_OFFLINE_DATA);
        Map req = new HashMap();
        req.put("request", "mobile " + Helper.getTodayDate(Constants.DATE_FORMAT_2));

        try {
            result = (WSMessage) Helper.postWebserviceWithBody(url, WSMessage.class, req);//post
        } catch (Exception e) {
            result.setIdMessage(0);
            result.setMessage("Sync offline data : " + e.getMessage());
        }
//                }
//            }).start();
        new Database(getApplicationContext()).addLog(result);
        if (result.getIdMessage() != 0) {
            Log.e("worker", "success");
            return Result.success();
        } else {
            Log.e("worker", "failed");
            return Result.failure();
        }
    }

    public void setSession() {
        if (SessionManagerQubes.getUrl() != null) {
            String ipAddress = SessionManagerQubes.getUrl();
            Constants.URL = ipAddress;
            Helper.setItemParam(Constants.URL, Constants.URL);
        }
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.e("worker", "Worker has been canceled");
    }
}
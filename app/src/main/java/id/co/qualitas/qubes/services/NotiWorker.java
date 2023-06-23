package id.co.qualitas.qubes.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Random;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.User;
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
        final boolean[] result = {false};
        String url = null;
        if (!isStopped())
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
            url = "http://192.168.1.8:9191/api/v1/doubleLogin/syncData";
        User user = new User();
        user.setUsername("mobile " + Helper.getTodayDate(Constants.DATE_FORMAT_2));
        try {
            result[0] = (Boolean) Helper.postWebserviceWithBody(url, Boolean.class, user);//post
        } catch (Exception e) {
            result[0] = false;
        }
//                }
//            }).start();
        if (result[0]) {
            Log.e("worker", "success");
            return Result.success();
        } else {
            Log.e("worker", "failed");
            return Result.failure();
        }

//        startRandomNumberGenerator();
//        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.e("worker", "Worker has been canceled");
    }
}
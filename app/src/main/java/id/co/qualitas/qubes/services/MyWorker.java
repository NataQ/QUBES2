package id.co.qualitas.qubes.services;

import android.content.Context;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.GCMResponse;

public class MyWorker extends Worker {

    private static final String TAG = "MyWorker";
    private static final String PACKAGE_NAME = "id.co.qualitas.qubes";
    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";
    public static final String ACTION_TRACKING = PACKAGE_NAME + ".broadcast";
    private GCMResponse gcmResponse;

    public MyWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Performing long running task in scheduled job");
        gcmResponse = (GCMResponse) Helper.getItemParam(Constants.FCM);
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(ACTION_TRACKING, gcmResponse.getDesc());
//        intent.putExtra(ACTION_TRACKING, "start_tracking");
        getApplicationContext().sendBroadcast(intent);
        return Result.success();
    }
}

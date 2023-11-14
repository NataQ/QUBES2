package id.co.qualitas.qubes.services;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.RetailOutletFragment;
import id.co.qualitas.qubes.helper.GPSTracker;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.GPSModel;
import id.co.qualitas.qubes.model.LiveTracking;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManager;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;
import id.co.qualitas.qubes.utils.UtilsLocation;


public class LocationForegroundService extends Service {
    private static final int NOTIFICATION_ID = 12345678;
    private static final String CHANNEL_ID = "channel_01";

    private Handler mServiceHandler;
    private NotificationManager mNotificationManager;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location mLocation;
    private LiveTracking liveTracking;
    private Boolean result = false;
    private Handler handler;
    private Runnable runHandler;

    private static final String TAG = LocationForegroundService.class.getSimpleName();
    static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;//1000 => 1s

    /*
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals("stop")) {
//                removeLocationUpdates(intent);
                removeLocationUpdates(intent);
//                Log.i(TAG, "Removing location updates");
//                try {
//                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//                    UtilsLocation.setRequestingLocationUpdates(this, false);
//                    stopSelf();
//                } catch (SecurityException unlikely) {
//                    UtilsLocation.setRequestingLocationUpdates(this, true);
//                    Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
//                }
            }
        } else {
            setLocation();
            startForegroundService();
//            handler = new Handler();
//            runHandler = new Runnable() {
//                @Override
//                public void run() {
//                    getCurrentLocation();
//                    handler.postDelayed(this, 10000);
//                }
//            };
//            handler.postDelayed(runHandler, 10000);

//            new Thread(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            while (true) {
//                                Log.e("Service", "Service is running..." + Helper.getTodayDate(Constants.DATE_FORMAT_2));
//                                try {
//                                    Thread.sleep(2000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//            ).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void getCurrentLocation() {
        /* kalau pakai gps tracker, app nya closed/foreground gak jalan
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        if (gpsTracker.canGetLocation()) {
            Log.e("Service", "Lat : " + String.valueOf(gpsTracker.getLatitude()) + "Long : " + String.valueOf(gpsTracker.getLongitude()));
        } else {
        }*/
//        mFusedLocationClient.getLastLocation().
    }

    public void startForegroundService() {
        Log.i(TAG, "Requesting location updates");
        UtilsLocation.setRequestingLocationUpdates(this, true);

        try {
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, mServiceHandler.getLooper());
        } catch (SecurityException unlikely) {
            UtilsLocation.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }

        startForeground(NOTIFICATION_ID, getNotification());
    }

    /*
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates(Intent intent) {
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            UtilsLocation.setRequestingLocationUpdates(this, false);
        } catch (SecurityException unlikely) {
            UtilsLocation.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }

        stopForeground(true);
        stopSelf();
        stopService(intent);
    }

    private void setLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_IN_MILLISECONDS)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setMaxUpdateDelayMillis(UPDATE_INTERVAL_IN_MILLISECONDS)
                .build();

        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.e(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name;
            name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    private void onNewLocation(Location location) {
        Log.e(TAG, "New location: " + location);

        mLocation = location;

        // Notify anyone listening for broadcasts about the new location.
//        Intent intent = new Intent(ACTION_BROADCAST);
//        intent.putExtra(EXTRA_LOCATION, location);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        // Update notification content if running as a foreground service.
        //try comment
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }

        if (location != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    liveTracking = new LiveTracking(location.getLatitude(), location.getLongitude());

                    Log.e("Service", "Lat:" + String.valueOf(location.getLatitude()) + " Long : " + String.valueOf(location.getLongitude()));
                    setSession();
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_UPDATE_LOCATION_SALESMAN);
                    Map req = new HashMap();
                    req.put("latitude", location.getLatitude());
                    req.put("longitude", location.getLongitude());
                    req.put("username", SessionManagerQubes.getUserProfile() != null ? SessionManagerQubes.getUserProfile().getUsername() : null);
                    try {
                        Helper.postWebserviceWithBody(url, null, req);//post
                    } catch (Exception e) {
                        result = false;
                    }

//                    try {
//                        WSMessage result = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, liveTracking);
//                    } catch (Exception e) {
//
//                    }
                }
            }).start();
        }
    }

    public void setSession() {
        if (SessionManagerQubes.getUrl() != null) {
            String ipAddress = SessionManagerQubes.getUrl();
            Constants.URL = ipAddress;
            Helper.setItemParam(Constants.URL, Constants.URL);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification getNotification() {
//        Intent intent = new Intent(this, LocationUpdatesService.class);

        Intent notificationIntent = new Intent(this, LocationForegroundService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_qubes_new)
                        .setSound(null)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true);
        return notificationBuilder.build();
    }

    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mServiceHandler.removeCallbacksAndMessages(null);
    }
}

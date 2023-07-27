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
import android.content.res.Configuration;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.SplashScreenActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.LiveTracking;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManager;
import id.co.qualitas.qubes.utils.UtilsLocation;

public class LocationUpdatesService extends Service {
    //https://github.com/gonpuga/LocationUpdatesForegroundService/blob/master/app/src/main/java/com/example/locationupdatesforegroundservice/LocationUpdatesService.java
    //https://stackoverflow.com/questions/25375407/is-it-bad-to-put-service-binding-at-onstart
    //https://stackoverflow.com/questions/10184159/android-service-mboundservice-null-in-onresume
    //https://github.com/droidbyme/Location/blob/master/app/src/main/java/com/coders/location/MainActivity.java
    //https://proandroiddev.com/when-your-app-makes-android-foreground-services-misbehave-8dbcc57dd99c
    //https://www.youtube.com/watch?v=bA7v1Ubjlzw
    //https://github.com/ibrahimgiki/location-updates-java/blob/master/app/src/main/java/com/trickyworld/locationupdates/MainActivity.java

    private static final String PACKAGE_NAME = "id.co.qualitas.qubes";

    private static final String TAG = LocationUpdatesService.class.getSimpleName();

    /*
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";

//    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    //    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = new LocalBinder();

    /*
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /*
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /*
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;

    /*
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private NotificationManager mNotificationManager;

    /*
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /*
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /*
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    /*
     * The current location.
     */
    private Location mLocation;
    private LiveTracking liveTracking;
    private Boolean result = false;

    public LocationUpdatesService() {
    }

    @Override
    public void onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        createLocationRequest();
        getLastLocation();

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        // Tells the system to not try to recreate the service after it has been killed.=> START_NOT_STICKY
        return START_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && UtilsLocation.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");
            //try comment
            startForeground(NOTIFICATION_ID, getNotification());
            //try comment
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    /*
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    @SuppressLint("MissingPermission")
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        UtilsLocation.setRequestingLocationUpdates(this, true);
        startService(new Intent(this, LocationUpdatesService.class));
//        HandlerThread handlerThread = new HandlerThread("tracking");
//        handlerThread.start();
        try {
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, mServiceHandler.getLooper());
        } catch (SecurityException unlikely) {
            UtilsLocation.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    public void startForegroundService() {
        Log.i(TAG, "Requesting location updates");
        UtilsLocation.setRequestingLocationUpdates(this, true);
//        startService(new Intent(this, LocationUpdatesService.class));
////        HandlerThread handlerThread = new HandlerThread("tracking");
////        handlerThread.start();
        try {
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, mServiceHandler.getLooper());
        } catch (SecurityException unlikely) {
            UtilsLocation.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }

//        if (!mChangingConfiguration && UtilsLocation.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");
            //try comment
            startForeground(NOTIFICATION_ID, getNotification());
//            try comment
//        }
        Log.i(TAG, "Starting foreground service");
        //try comment

    }

    /*
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            UtilsLocation.setRequestingLocationUpdates(this, false);
            stopForeground(true);
            stopSelf();
        } catch (SecurityException unlikely) {
            UtilsLocation.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }

//        stopForeground(true);
        mChangingConfiguration = false;
    }

    /*
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
//        Intent intent = new Intent(this, LocationUpdatesService.class);

        Intent notificationIntent = new Intent(this, LocationUpdatesService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        CharSequence text = UtilsLocation.getLocationText(mLocation);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_qubes_new)
                        .setSound(null)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true);
        return notificationBuilder.build();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    private void onNewLocation(Location location) {
        Log.i(TAG, "New location: " + location);

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
                    setSession();
                    final String url = Constants.URL.concat(Constants.API_SYNC_DATA);
                    User user = new User();
                    user.setUsername("mobile " + Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    try {
                        result = (Boolean) Helper.postWebserviceWithBody(url, Boolean.class, user);//post
                    } catch (Exception e) {
                        result = false;
                    }
//                    try {
//                        MessageResponse result = (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, liveTracking);
//                    } catch (Exception e) {
//                    }
                }
            }).start();
        }
    }

    public void setSession() {
        SessionManager session = new SessionManager(this);
        if (session.isUrlEmpty()) {
            Map<String, String> urlSession = session.getUrl();
            Constants.IP = urlSession.get(Constants.KEY_URL);
            Constants.URL = Constants.IP;
            Helper.setItemParam(Constants.URL, Constants.URL);
        } else {
            Constants.IP = Constants.URL;
            Constants.URL = Constants.IP;
            Helper.setItemParam(Constants.URL, Constants.URL);
        }
    }

    /*
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setFastestInterval(30000/2);
//        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS / 2);
        mLocationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

    }

    /*
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocationUpdatesService getService() {
            return LocationUpdatesService.this;
        }

    }

    /*
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
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
}
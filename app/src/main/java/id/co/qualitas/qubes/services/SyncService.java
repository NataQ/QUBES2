//package id.co.qualitas.qubes.services;
//
//import android.annotation.SuppressLint;
//import android.app.ActivityManager;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.location.Location;
//import android.os.AsyncTask;
//import android.os.Binder;
//import android.os.Build;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.IBinder;
//import android.os.Looper;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//
//import id.co.qualitas.qubes.R;
//import id.co.qualitas.qubes.activity.SplashScreenActivity;
//import id.co.qualitas.qubes.constants.Constants;
//import id.co.qualitas.qubes.helper.Helper;
//import id.co.qualitas.qubes.model.MessageResponse;
//
//public class SyncService extends Service {
//    private static final String PACKAGE_NAME = "id.co.qualitas.podv2";
//
//    private static final String TAG = SyncService.class.getSimpleName();
//
//    /*
//     * The name of the channel for notifications.
//     */
//    private static final String CHANNEL_ID = "channel_01";
//
//    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";
//
//    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
//    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
//            ".started_from_notification";
//
//    private final IBinder mBinder = new LocalBinder();
//
//    /*
//     * The identifier for the notification displayed for the foreground service.
//     */
//    private static final int NOTIFICATION_ID = 12345678;
//
//    /*
//     * Used to check whether the bound activity has really gone away and not unbound as part of an
//     * orientation change. We create a foreground service notification only if the former takes
//     * place.
//     */
//    private boolean mChangingConfiguration = false;
//
//    private NotificationManager mNotificationManager;
//
//    private Handler mServiceHandler;
//
//    public SyncService() {
//    }
//
//    @Override
//    public void onCreate() {
////        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
////        mLocationCallback = new LocationCallback() {
////            @Override
////            public void onLocationResult(LocationResult locationResult) {
////                super.onLocationResult(locationResult);
////                onNewLocation(locationResult.getLastLocation());
////            }
////        };
////        createLocationRequest();
////        getLastLocation();
//
//        HandlerThread handlerThread = new HandlerThread(TAG);
//        handlerThread.start();
//        mServiceHandler = new Handler(handlerThread.getLooper());
//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        // Android O requires a Notification Channel.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name;
//            name = getString(R.string.app_name);
//            // Create the channel for the notification
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
//
//            // Set the Notification Channel for the Notification Manager.
//            mNotificationManager.createNotificationChannel(mChannel);
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG, "Service started");
//        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false);
//
//        // We got here because the user decided to remove location updates from the notification.
//        if (startedFromNotification) {
//            removeLocationUpdates();
//            stopSelf();
//        }
//        // Tells the system to not try to recreate the service after it has been killed.
//        return START_NOT_STICKY;
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mChangingConfiguration = true;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // Called when a client (MainActivity in case of this sample) comes to the foreground
//        // and binds with this service. The service should cease to be a foreground service
//        // when that happens.
//        Log.i(TAG, "in onBind()");
//        stopForeground(true);
//        mChangingConfiguration = false;
//        return mBinder;
//    }
//
//    @Override
//    public void onRebind(Intent intent) {
//        // Called when a client (MainActivity in case of this sample) returns to the foreground
//        // and binds once again with this service. The service should cease to be a foreground
//        // service when that happens.
//        Log.i(TAG, "in onRebind()");
//        stopForeground(true);
//        mChangingConfiguration = false;
//        super.onRebind(intent);
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        Log.i(TAG, "Last client unbound from service");
//
//        // Called when the last client (MainActivity in case of this sample) unbinds from this
//        // service. If this method is called due to a configuration change in MainActivity, we
//        // do nothing. Otherwise, we make this service a foreground service.
//        if (!mChangingConfiguration && UtilsLocation.requestingLocationUpdates(this)) {
//            Log.i(TAG, "Starting foreground service");
//            //try comment
//            startForeground(NOTIFICATION_ID, getNotification());
//            //try comment
//        }
//        return true; // Ensures onRebind() is called when a client re-binds.
//    }
//
//    @Override
//    public void onDestroy() {
//        mServiceHandler.removeCallbacksAndMessages(null);
//    }
//
//    /*
//     * Makes a request for location updates. Note that in this sample we merely log the
//     * {@link SecurityException}.
//     */
//    @SuppressLint("MissingPermission")
//    public void requestLocationUpdates() {
//        Log.i(TAG, "Requesting location updates");
//        UtilsLocation.setRequestingLocationUpdates(this, true);
//        startService(new Intent(this, LocationUpdatesService.class));
//        try {
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//        } catch (SecurityException unlikely) {
//            UtilsLocation.setRequestingLocationUpdates(this, false);
//            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
//        }
//    }
//
//    /*
//     * Removes location updates. Note that in this sample we merely log the
//     * {@link SecurityException}.
//     */
//    public void removeLocationUpdates() {
//        Log.i(TAG, "Removing location updates");
//        try {
//            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//            UtilsLocation.setRequestingLocationUpdates(this, false);
//            stopSelf();
//        } catch (SecurityException unlikely) {
//            UtilsLocation.setRequestingLocationUpdates(this, true);
//            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
//        }
//    }
//
//    /*
//     * Returns the {@link NotificationCompat} used as part of the foreground service.
//     */
//    private Notification getNotification() {
//        Intent intent = new Intent(this, SyncService.class);
//
//        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
//        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);//bwt kasih tau stop service
//
//        // The PendingIntent that leads to a call to onStartCommand() in this service.
//        PendingIntent servicePendingIntent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            servicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_MUTABLE);
//        } else {
//            servicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
//
//        // The PendingIntent to launch activity.
//        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, SplashScreenActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
////                .addAction(R.drawable.ic_launch, getString(R.string.launch_activity), activityPendingIntent)
////                .addAction(R.drawable.ic_cancel, getString(R.string.remove_location_updates), servicePendingIntent)
////                .setContentText(text)
////                .setContentTitle(UtilsLocation.getLocationTitle(this))
//                .setSound(null)
//                .setOngoing(true)
//                .setPriority(Notification.PRIORITY_HIGH)
//                .setSmallIcon(R.drawable.ic_qubes_new)
////                .setTicker(text)
//                .setNotificationSilent()
//                .setWhen(System.currentTimeMillis());
//
//        // Set the Channel ID for Android O.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder.setChannelId(CHANNEL_ID); // Channel ID
//        }
//
//        return builder.build();
//    }
//
//    private void onNewLocation(Location location) {
//
//        // Notify anyone listening for broadcasts about the new location.
//        Intent intent = new Intent(ACTION_BROADCAST);
//        intent.putExtra(EXTRA_LOCATION, location);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//
//        // Update notification content if running as a foreground service.
//        //try comment
//        if (serviceIsRunningInForeground(this)) {
//            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
//        }
//
//        if (location != null) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    final String url = Constants.URL.concat("api/v1/doubleLogin/syncData");
//                    try {
//                        boolean result = (boolean) Helper.postWebserviceWithBodyWithoutHeaders(url, boolean.class, "mobile");//post
//                    } catch (Exception e) {
//
//                    }
//                }
//            }).start();
//
//        }
//    }
//
//    /*
//     * Class used for the client Binder.  Since this service runs in the same process as its
//     * clients, we don't need to deal with IPC.
//     */
//    public class LocalBinder extends Binder {
//        public SyncService getService() {
//            return SyncService.this;
//        }
//
//    }
//
//    /*
//     * Returns true if this is a foreground service.
//     *
//     * @param context The {@link Context}.
//     */
//    public boolean serviceIsRunningInForeground(Context context) {
//        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (getClass().getName().equals(service.service.getClassName())) {
//                if (service.foreground) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//}
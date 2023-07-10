package id.co.qualitas.qubes.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.SplashScreenActivity;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.GCMResponse;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 * <p>
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 * <p>
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */

public class MyFirebaseMessagingService2 extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static final String PACKAGE_NAME = "id.co.qualitas.qubes";
    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";
    public static final String ACTION_TRACKING = PACKAGE_NAME + ".broadcast";

    //https://stackoverflow.com/questions/41383238/firebase-notifications-when-app-is-closed

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map map = remoteMessage.getData();

            if (map != null) {
                Bundle data = new Bundle();
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    data.putString(entry.getKey(), entry.getValue());
                }

                GCMResponse gcmResponse = new GCMResponse();
                gcmResponse.setDesc(!Helper.isEmpty(data.get("body")) ? data.get("body").toString() : null);
                gcmResponse.setContentTitle(!Helper.isEmpty(data.get("title")) ? data.get("title").toString() : null);
                gcmResponse.setImageUrl(!Helper.isEmpty(data.get("imageUrl")) ? data.get("imageUrl").toString() : null);
                sendNotification(gcmResponse);

                if (!Helper.isEmpty(gcmResponse.getDesc())) {
                    Intent intent = new Intent(ACTION_BROADCAST);
                    intent.putExtra(ACTION_TRACKING, gcmResponse.getDesc());
                    this.sendBroadcast(intent);
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            RemoteMessage.Notification notif = remoteMessage.getNotification();

            if (notif != null) {
                GCMResponse gcmResponse = new GCMResponse();
                gcmResponse.setDesc(!Helper.isEmpty(notif.getBody()) ? notif.getBody() : null);
                gcmResponse.setContentTitle(!Helper.isEmpty(notif.getTitle()) ? notif.getTitle() : null);
                gcmResponse.setImageUrl(!Helper.isEmpty(notif.getImageUrl()) ? notif.getImageUrl().toString() : null);
                sendNotification(gcmResponse);

                if (!Helper.isEmpty(gcmResponse.getDesc())) {
                    Intent intent = new Intent(ACTION_BROADCAST);
                    intent.putExtra(ACTION_TRACKING, gcmResponse.getDesc());
                    this.sendBroadcast(intent);
                }
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param response FCM message body received.
     */
    private void sendNotification(GCMResponse response) {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_qubes_new)
                        .setContentTitle(response.getContentTitle())
                        .setContentText(response.getDesc())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        if (response.getImageUrl() != null) {
            Bitmap bitmap = getBitmapfromUrl(String.valueOf(response.getImageUrl()));
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null)
            ).setLargeIcon(bitmap);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }
}

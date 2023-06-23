package id.co.qualitas.qubes.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.SplashScreenActivity;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.model.GCMResponse;


/**
 * Created by Natalia on 6/18/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private DatabaseHelper db;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        initialize();

        String from = remoteMessage.getFrom();
        Map map = remoteMessage.getData();
        Bundle data = new Bundle();
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            data.putString(entry.getKey(), entry.getValue());
        }
        GCMResponse gcmResponse = new GCMResponse();
        gcmResponse.setMessage(data.getString("body"));
        gcmResponse.setContentTitle(data.getString("title"));
        gcmResponse.setUsername(data.getString("username"));
//        if (session.isLoggedIn()) {
        sendNotification(gcmResponse);
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void initialize() {
        db = new DatabaseHelper(getApplicationContext());
    }
    // [END receive_message]


    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendNotification(GCMResponse gcmResponse) {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;

        //notifikasi selain reminder
        //kalau lagi buka aplikasi
        if (componentInfo.getPackageName().equalsIgnoreCase("id.co.qualitas.qubes")) {
//            Helper.setItemParam(Constants.NOTIFICATION, gcmResponse);
            if (gcmResponse != null) {
                //jika lagi buka aplikasi, maka ga usah kirim notif, langsung aja kirim pesan di login
//                Helper.removeItemParam(Constants.OUTLET_CLICKED);

//                new Thread(db).execute();
//                db.deleteFCM();
                /*newest6Nov*/

                //harusnya gak d comment cmn biar gak double login aja
//                Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
//                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent2);

            }
        } else {
            //kalau lagi nggak buka aplikasi , muncul notifikasi
            if (gcmResponse != null) {
                db.deleteFCM();
                db.addFCM(gcmResponse);

                //intent ke login dari notifikasi
//                        Intent intent = new Intent(this, LoginActivity.class);
                Intent intent = new Intent(this, SplashScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_qubes)
                        .setContentTitle("QUBES")
                        .setContentText(gcmResponse.getMessage())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setTicker(gcmResponse.getMessage())//tulisan yang ada di paling atas (yang jalan)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }
        }
    }
}

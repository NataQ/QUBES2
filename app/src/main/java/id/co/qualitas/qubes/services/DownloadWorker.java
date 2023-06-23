//package id.co.qualitas.qubes.services;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.os.Build;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//import androidx.work.Data;
//import androidx.work.ForegroundInfo;
//import androidx.work.WorkManager;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import id.co.qualitas.qubes.R;
//
//public class DownloadWorker extends Worker {
//    private static final String KEY_INPUT_URL = "KEY_INPUT_URL";
//    private static final String KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME";
//
//    private NotificationManager notificationManager;
//
//    public DownloadWorker(
//            @NonNull Context context,
//            @NonNull WorkerParameters parameters) {
//        super(context, parameters);
//        notificationManager = (NotificationManager)
//                context.getSystemService(NOTIFICATION_SERVICE);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//        Data inputData = getInputData();
//        String inputUrl = inputData.getString(KEY_INPUT_URL);
//        String outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME);
//        // Mark the Worker as important
//        String progress = "Starting Download";
//        setForegroundAsync(createForegroundInfo(progress));
//        download(inputUrl, outputFile);
//        return Result.success();
//    }
//
//    private void download(String inputUrl, String outputFile) {
//        // Downloads a file and updates bytes read
//        // Calls setForegroundAsync(createForegroundInfo(myProgress))
//        // periodically when it needs to update the ongoing Notification.
//    }
//
//    @NonNull
//    private ForegroundInfo createForegroundInfo(@NonNull String progress) {
//        // Build a notification using bytesRead and contentLength
//
//        Context context = getApplicationContext();
//        String id = context.getString(R.string.channelId);
//        String title = context.getString(R.string.title_home);
//        String cancel = context.getString(R.string.title_activity_maps);
//        // This PendingIntent can be used to cancel the worker
//        PendingIntent intent = WorkManager.getInstance(context).createCancelPendingIntent(getId());
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createChannel();
//        }
//
//        Notification notification = new NotificationCompat.Builder(context, id)
//                .setContentTitle(title)
//                .setTicker(title)
//                .setSmallIcon(R.drawable.ic_qubes_new)
//                .setOngoing(true)
//                // Add the cancel action to the notification which can
//                // be used to cancel the worker
//                .addAction(android.R.drawable.ic_delete, cancel, intent)
//                .build();
//
//        return new ForegroundInfo(notificationId, notification);
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private void createChannel() {
//        // Create a Notification channel
//    }
//}

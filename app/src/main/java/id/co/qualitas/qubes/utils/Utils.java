package id.co.qualitas.qubes.utils;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.SplashScreenActivity;

public class Utils {
    private static final Gson gson = new Gson();
    private static Toast toaster;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final int NOTIFICATION_ID = 22;
    private static final String CHANNEL_ID = "notify";
    private static final String CHANNEL_NAME = "workmanager-reminder";

    @SuppressLint("ShowToast")
    public static void init(Context context) {
        toaster = Toast.makeText(context, null, Toast.LENGTH_LONG);
    }
    public static void sendNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel1.enableVibration(true);
            channel1.enableLights(true);
            channel1.setLightColor(R.color.colorPrimary);
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            notificationManager.createNotificationChannel(channel1);
        }

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setContentTitle("WorkManager Sample")
//                .setContentText("WorkManager Started")
//                .setAutoCancel(true)
//                .setSmallIcon(R.drawable.ic_qubes_new);

//        notificationManager.notify(1, builder.build());

//        Intent intent = new Intent(this, LocationUpdatesService.class);
//        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);//bwt kasih tau stop service
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            servicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_MUTABLE);
//        } else {
//            servicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, SplashScreenActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .addAction(R.drawable.ic_qubes_new, "lauch activity", activityPendingIntent)
//                .addAction(R.drawable.ic_cancel, getString(R.string.remove_location_updates), servicePendingIntent)
                .setContentTitle("WorkManager Sample")
                .setContentText("WorkManager Started")
//                .setContentTitle(UtilsLocation.getLocationTitle(this))
                .setSound(null)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_qubes_new)
//                .setTicker(text)
                .setNotificationSilent()
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        notificationManager.notify(1, builder.build());
    }

    public static void showToast(Context context, String message) {//spy gbs di spam
        toaster = Toast.makeText(context, null, Toast.LENGTH_LONG);
        toaster.setText(message);
        toaster.show();
    }

    public static int getLocationMode(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (TextUtils.isEmpty(locationProviders)) {
                locationMode = Settings.Secure.LOCATION_MODE_OFF;
            } else if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                locationMode = Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
            } else if (locationProviders.contains(LocationManager.GPS_PROVIDER)) {
                locationMode = Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
            } else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                locationMode = Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
            }

        }

        return locationMode;
    }

    public static void turnOnGPS(Activity activity) {
        Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivity(gpsOptionsIntent);
    }

    public static Integer[] getDeviceSize(Activity mActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return new Integer[]{height, width};
    }

    public interface OnItemClickListener {
        void OnItemClicked(int position, String id);
    }

    public static String convertSecondToTime(Integer durationSeconds) {
        return String.format("%02d:%02d:%02d", durationSeconds / 3600,
                (durationSeconds % 3600) / 60, (durationSeconds % 60));
    }

    public static String calculateTimeElapsed(Long timestamp) {
        Long elapsed = System.currentTimeMillis() - timestamp;
        Long second = elapsed / 1000L;
        Long minutes = second / 60L;
        Long hour = minutes / 60L;
        Long days = hour / 24L;

        return days > 0 ? days.toString() + " d" : (hour > 0 ? hour.toString() + " h" : minutes.toString() + " m");
    }

    public static void openDialer(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    public static String convertHmsTimer(Long millis) {
        return String.format(Locale.US, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public static boolean isGPSOn(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static String getCurrentAddress(Context context, Double lat, Double lng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            //            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
            /*String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();*/
            return addresses.get(0).getAddressLine(0); //+ "\n" + city + "\n" + state;
        } catch (Exception e) {
            return null;
        }
    }

    public static String objectToJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T jsonToObject(String json, Class<T> model) {
        return gson.fromJson(json, model);
    }

    public static String getCurrentDate() {
        return Calendar.getInstance().getTime().toString();
    }

    public static String timestampToDate(String toModel, Long timestamp) {
        return new SimpleDateFormat(toModel, Locale.GERMAN).format(new Date(timestamp));
    }

    public static String datetimeCalculate(String model1, String model2, String datetime1, String datetime2) {
        SimpleDateFormat format1 = new SimpleDateFormat(model1, Locale.GERMAN);
        SimpleDateFormat format2 = new SimpleDateFormat(model2, Locale.GERMAN);

        try {
            Date date1 = format1.parse(datetime1);
            Date date2 = format2.parse(datetime2);
            long difTemp = date1.getTime() - date2.getTime();
            if (difTemp < 0L) {
                return null;
            }
            long diff = Math.abs(difTemp);
            long sec = diff / 1000;
            long min = sec / 60;
            long hour = min / 60;
            Long day = hour / 24;
            Long month = day / 30;
            Long year = month / 12;
            if (day <= 30) {
                return day.intValue() + " Days";
            } else {
                if (month <= 12) {
                    return month.intValue() + " Months";
                } else {
                    return year.intValue() + " Years";
                }
            }
//            return new SimpleDateFormat(toModel,Locale.GERMAN).format(new Date(diff));
//            return to.convert(diff, TimeUnit.MILLISECONDS);
//            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
//                return null;
            }
        };
        t.start();
        return t;
    }

    public Long datetimeToTimestamp(String model, String datetime) {
        SimpleDateFormat format1 = new SimpleDateFormat(model, Locale.GERMAN);

        try {
            Date date1 = format1.parse(datetime);
            return date1.getTime();
        } catch (Exception e) {
            return 0L;
        }
    }

    public static void showDatePicker(final EditText editText, final OnDateSet onDateSet) {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                editText.setText(sdf.format(myCalendar.getTime()));

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                onDateSet.onSet(sdf2.format(myCalendar.getTime()));
            }

        };

        DatePickerDialog dpd = new DatePickerDialog(editText.getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        DatePicker dp = dpd.getDatePicker();
        dp.setMinDate(System.currentTimeMillis());
        dp.setMaxDate(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(90));//3 Bulan

        dpd.show();
    }

    public static void showTimePicker(final EditText editText, final OnTimeSet onTimeSet) {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(editText.getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
                        editText.setText(sdf.format(c.getTime()));
                        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.US);
                        onTimeSet.onSet(sdf2.format(c.getTime()));
                    }
                }, mHour, mMinute, true);//false untuk format 12 jam
        timePickerDialog.show();
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Activity mActivity) {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public interface OnDateSet {
        void onSet(String dbDate);
    }

    public interface OnTimeSet {
        void onSet(String dbTime);
    }

    public interface OnBackDialogPressed {
        void onBackPressed();
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String compressImageUri(Context context, String imageUri) {

        String filePath = getRealPathFromURI(context, imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
//        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError e) {
            try {
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (Exception ex) {
                Log.e("test", ex.getMessage());
            }
        }

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = filePath;
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private static String getRealPathFromURI(Context context, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static File getDirLocPDF(Context applicationContext) {
        File directory = null;
        directory = new File(String.valueOf(applicationContext.getFilesDir()));

        directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // if no directory exists, create new directory
        File newDir = new File(directory + "/QUBES");
        if (!newDir.exists()) {
            newDir.mkdir();
        }

        return newDir;
    }

    public static Uri cropPhoto(Activity activity, Uri photoUri) {
        try {
            Uri outputUri = Uri.fromFile(createImageFile(activity.getApplicationContext()));

            UCrop.Options opt = new UCrop.Options();
            opt.setCompressionQuality(25);
            opt.setCompressionFormat(Bitmap.CompressFormat.PNG);
            opt.setLogoColor(ContextCompat.getColor(activity, R.color.white));
            opt.setToolbarColor(ContextCompat.getColor(activity, R.color.white));
            opt.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
            opt.setActiveControlsWidgetColor(ContextCompat.getColor(activity, R.color.white));

            UCrop.of(photoUri, outputUri)
//                    .withAspectRatio(16, 9)//16:9 default
                    .withOptions(opt)
                    .withMaxResultSize(600, 250)
                    .start(activity);
            return outputUri;
        } catch (IOException e) {
            Toast.makeText(activity.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static File createImageFile(Context context) throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMAN).format(new Date());
        String imageFileName = "crop_";
        File storageDir = getDirLoc(context);
        File image = File.createTempFile(
                imageFileName,   //prefix
                ".jpg",   //suffix
                storageDir  //directory
                ///data/user/0/id.co.qualitas.oem/files/storage/emulated/0/Pictures/JPEG_20201125_121251_-1693854433.jpg
        );
        return image;
//        return new File(Environment.getExternalStorageDirectory(), imageFileName);
    }

    public static File getDirLoc(Context applicationContext) {
        String PDF_FOLDER_NAME = "/KTC/";
        File directory = null;
        //if there is no SD card, create new directory objects to make directory on device
        if (Environment.getExternalStorageState() == null) {
            //create new file directory object
            directory = new File(Environment.getDataDirectory() + PDF_FOLDER_NAME);
            // if no directory exists, create new directory
            if (!directory.exists()) {
                directory.mkdir();
            }

            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            try {
                int version = Build.VERSION.SDK_INT;
                if (version >= 30) {
                    directory = new File(applicationContext.getFilesDir() + PDF_FOLDER_NAME);
                } else {
                    directory = new File(Environment.getExternalStorageDirectory() + PDF_FOLDER_NAME);
                }
                // results
                if (!directory.exists()) {
                    directory.mkdir();
                }
            } catch (Exception ex) {
                Toast.makeText(applicationContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }// end of SD card checking

        return directory;
    }
}

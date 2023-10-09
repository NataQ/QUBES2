package id.co.qualitas.qubes.activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.LoginActivity;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.LoginResponse;
import id.co.qualitas.qubes.model.OffDate;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManager;
import id.co.qualitas.qubes.session.SessionManagerQubes;


public class SplashScreenActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 0;
    private String mUSer, mProfile, mDate;
    private LoginResponse loginResponse;
    private User user;
    private List<String> permissionsNeeded = new ArrayList<>();
    private List<String> permissionsList = new ArrayList<>();
    private PackageInfo packageInfo = null;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private OffDate offlineDat;
    TextView txtApp, txtDetail;
    LinearLayout llText;
    ImageView image;

    private User attendance = new User();

    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isLocationPermissionGranted = false;
    private boolean isReadPermissionGranted = false;
    //    private boolean isWritePermissionGranted = false;
    private boolean isCameraPermissionGranted = false;
    private boolean isNotificationPermissionGranted = false;
    private Intent intent;

//    private final ActivityResultLauncher<String> requestPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                if (isGranted) {
//                    Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "FCM can't post notifications without POST_NOTIFICATIONS permission", Toast.LENGTH_LONG).show();
//                }
//            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        initialize();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW));
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("TAG", "Key: " + key + " Value: " + value);
            }
        }

        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.get(Manifest.permission.CAMERA) != null) {
                    isCameraPermissionGranted = result.get(Manifest.permission.CAMERA);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (result.get(Manifest.permission.READ_MEDIA_IMAGES) != null) {
                        isReadPermissionGranted = result.get(Manifest.permission.READ_MEDIA_IMAGES);
                    }
                } else {
                    if (result.get(Manifest.permission.READ_EXTERNAL_STORAGE) != null) {
                        isReadPermissionGranted = result.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }

                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null) {
                    isLocationPermissionGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                }

                if (result.get(Manifest.permission.POST_NOTIFICATIONS) != null) {
                    isNotificationPermissionGranted = result.get(Manifest.permission.POST_NOTIFICATIONS);
                }

                if (isCameraPermissionGranted && isReadPermissionGranted && isLocationPermissionGranted && isNotificationPermissionGranted) {
                    if (!Helper.isGPSOn(SplashScreenActivity.this)) {
                        setToast("Please turn on GPS");
                        Helper.turnOnGPS(SplashScreenActivity.this);
                    } else {
                        setData();
                    }
                } else {
                    setToast("Please allow all permissions");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageViewAnimatedChange(getApplicationContext(), image, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_ilustrasi2));
    }

    private void requestPermission() {
        isCameraPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//        isReadPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isReadPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            isReadPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        isLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isNotificationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        } else {
            isNotificationPermissionGranted = true;
        }

        List<String> permissionRequest = new ArrayList<>();
        if (!isCameraPermissionGranted) permissionRequest.add(Manifest.permission.CAMERA);
        if (!isReadPermissionGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionRequest.add(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        if (!isLocationPermissionGranted)
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (!isNotificationPermissionGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionRequest.add(Manifest.permission.POST_NOTIFICATIONS);
        }
        if (!permissionRequest.isEmpty()) {
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }

        if (isCameraPermissionGranted && isReadPermissionGranted && isLocationPermissionGranted && isNotificationPermissionGranted) {
            if (!Helper.isGPSOn(SplashScreenActivity.this)) {
                setToast("Please turn on GPS");
                Helper.turnOnGPS(SplashScreenActivity.this);
            } else {
                setData();
            }
        }

//        // This is only necessary for API Level > 33 (TIRAMISU)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
//                    PackageManager.PERMISSION_GRANTED) {
//                // FCM SDK (and your app) can post notifications.
//            } else {
//                // Directly ask for the permission
//                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
//            }
//        }
    }

    private void setToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }

    public void ImageViewAnimatedChange(Context c, final ImageView v, final Drawable new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.fade_in);
        final Animation anim_in2 = AnimationUtils.loadAnimation(c, R.anim.fade_in);

        v.startAnimation(anim_in);
        anim_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.VISIBLE);

                Drawable currentDrawable = v.getDrawable();
                if (currentDrawable == null) {
                    v.setImageDrawable(new_image);
                    return;
                }

                TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{
                        currentDrawable,
                        new_image
                });
                v.setImageDrawable(transitionDrawable);
                transitionDrawable.setCrossFadeEnabled(true);
                transitionDrawable.startTransition(1000);

//                v.setImageDrawable(new_image);

                llText.startAnimation(anim_in2);
                anim_in2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        llText.setVisibility(View.VISIBLE);
                        requestPermission();
                    }
                });
            }
        });
    }

    private void setData() {
//        try {
//            packageInfo = SplashScreenActivity.this.getPackageManager().getPackageInfo(SplashScreenActivity.this.getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        int permissionCheck = ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
//
//        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, Manifest.permission.READ_CONTACTS)) {
//            } else {
//                ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_STORAGE);
//            }
//        }

//        if (loginResponse != null) {
//            Helper.setItemParam(Constants.LOGIN, loginResponse);
//            Helper.setItemParam(Constants.TOKEN, loginResponse.getAccess_token());
//        }
//
//        if (session.isUrlEmpty()) {
//            Map<String, String> urlSession = session.getUrl();
//            Constants.IP = urlSession.get(Constants.KEY_URL);
//        } else {
//            Constants.IP = Constants.URL;
//        }
//        Constants.URL = Constants.IP;
//        Helper.setItemParam(Constants.URL, Constants.URL);

//        if (offlineDat != null) {
//            SecureDate.getInstance().initServerDate(Helper.convertStringtoDate(Constants.DATE_TYPE_16, offlineDat.getCurDate()), offlineDat.getElapseTime());
//        }

        new CountDownTimer(Constants.LONG_1000, Constants.LONG_100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (SessionManagerQubes.getUserProfile() == null) {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }.start();
    }

    private void initialize() {
        txtApp = findViewById(R.id.txtApp);
        txtDetail = findViewById(R.id.txtDetail);
        llText = findViewById(R.id.llText);
        image = findViewById(R.id.imageView1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

}
package id.co.qualitas.qubes.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.LoginResponse;
import id.co.qualitas.qubes.model.OffDate;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManager;


public class SplashScreenActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();

        initialize();

        ImageViewAnimatedChange(getApplicationContext(), image, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_ilustrasi2));

//        Button start = findViewById(R.id.start);
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ImageViewAnimatedChange(getApplicationContext(), image, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_ilustrasi2));
//            }
//        });
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
                        setData();
                    }
                });
            }
        });
    }

    private void setData() {
        try {
            packageInfo = SplashScreenActivity.this.getPackageManager().getPackageInfo(SplashScreenActivity.this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        if (loginResponse != null) {
            Helper.setItemParam(Constants.LOGIN, loginResponse);
            Helper.setItemParam(Constants.TOKEN, loginResponse.getAccess_token());
        }

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

        if (user != null) {
            Helper.setItemParam(Constants.USER_DETAIL, user);
            Helper.setItemParam(Constants.SUPERVISOR_DETAIL, user);
        }

        if (offlineDat != null) {
            SecureDate.getInstance().initServerDate(Helper.convertStringtoDate(Constants.DATE_TYPE_16, offlineDat.getCurDate()), offlineDat.getElapseTime());
        }

        new CountDownTimer(Constants.LONG_1000, Constants.LONG_100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
        session = new SessionManager(this);
        db = new DatabaseHelper(getApplicationContext());

        Map<String, String> dateSession = session.getDate();
        mDate = dateSession.get(Constants.KEY_DATE);

        offlineDat = (OffDate) Helper.stringToObject(mDate);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
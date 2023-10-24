package id.co.qualitas.qubes.activity.aspp;

import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.fixed;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.back;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.front;
import static io.fotoapparat.parameter.selector.Selectors.firstAvailable;
import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.aspp.ConfirmationDialogFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;

public class CameraActivity extends BaseActivity implements View.OnClickListener {
    private Uri croppedUri;
    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };
    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };
    private CameraView mCameraView;
    private ImageView fab;
    private Toolbar toolbar;
    private Fotoapparat fotoapparatFront, fotoapparatBack, fotoapparatCurrent;
    private boolean isCameraStarted = false;
    private boolean isCameraFrontFacing = false;
    private TextView toolbarTitle;
    private String currentPhotoPath;
    private boolean notDelivered = false;
    public static final int CAMERA_PERM_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_camera);
        init();
        initProgress();
        initCamera();
    }

    public void init() {
        toolbar = findViewById(R.id.toolbar);
        mCameraView = findViewById(R.id.camera);
        fab = findViewById(R.id.take_picture);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initCamera() {
        fotoapparatFront =
                Fotoapparat
                        .with(this)
                        .into(mCameraView)           // view which will draw the camera preview
                        .previewScaleType(ScaleType.CENTER_CROP)  // we want the preview to fill the view
                        .photoSize(biggestSize())// we want to have the biggest photo possible
                        .lensPosition(front())       // we want back camera
                        .focusMode(firstAvailable(  // (optional) use the first focus mode which is supported by device
                                continuousFocus(),
                                autoFocus(),        // in case if continuous focus is not available on device, auto focus will be used
                                fixed()             // if even auto focus is not available - fixed focus mode will be used
                        )).build();

        fotoapparatBack =
                Fotoapparat
                        .with(this)
                        .into(mCameraView)
                        .previewScaleType(ScaleType.CENTER_CROP)
                        .photoSize(biggestSize())
                        .lensPosition(back())
                        .focusMode(firstAvailable(
                                continuousFocus(),
                                autoFocus(),
                                fixed()
                        )).build();

        if (isCameraFrontFacing) {
            fotoapparatCurrent = fotoapparatFront;
        } else {
            fotoapparatCurrent = fotoapparatBack;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //permission don't allow
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, CAMERA_PERM_CODE);
        } else {
            startCamera();
        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
//                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                ConfirmationDialogFragment
//                        .newInstance(R.string.camera_permission_confirmation,
//                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                Constants.REQUEST_CAMERA_CODE,
//                                R.string.camera_and_storage_permission_not_granted)
//                        .show(getSupportFragmentManager(), Constants.FRAGMENT_DIALOG);
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CAMERA_CODE);
//            }
//        } else {
//            startCamera();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCamera();
    }

    private void startCamera() {
        if (!isCameraStarted) {
            isCameraStarted = true;
            fotoapparatCurrent.start();
        }
    }

    private void stopCamera() {
        if (isCameraStarted) {
            isCameraStarted = false;
            fotoapparatCurrent.stop();
        }
    }

    private void switchCamera() {
        stopCamera();
        if (isCameraFrontFacing) {
            fotoapparatCurrent = fotoapparatBack;
            isCameraFrontFacing = false;
        } else {
            fotoapparatCurrent = fotoapparatFront;
            isCameraFrontFacing = true;
        }
        startCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            onBackPressed();
            Toast.makeText(this, R.string.camera_and_storage_permission_not_granted, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_flash:
                if (mCameraView != null) {
                }
                return true;

            case R.id.switch_camera:
                if (mCameraView != null) {
                    switchCamera();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void takePicture() {
        PhotoResult photoResult = fotoapparatCurrent.takePicture();
        progress.show();
//        File file = createBitmapFile();
        File file = null;

        try {
            file = createImageFile();
            File finalFile = file;
            photoResult
                    .saveToFile(file)
                    .whenAvailable(aVoid -> {
                        progress.dismiss();
                        croppedUri = Utils.cropPhoto(CameraActivity.this, Uri.fromFile(finalFile));
                    });
            Helper.removeImage(getApplicationContext(), file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            setToast(e.getMessage() != null ? e.getMessage() : "Failed to create image");
        }
    }

    private void gotoConfirmPhoto(Uri uri) {
        Intent resultIntent = null;
//        ImageType imageType = SessionManagerQubes.getImageType() != null ? SessionManagerQubes.getImageType() : new ImageType();
        ImageType imageType = Helper.getItemParam(Constants.IMAGE_TYPE) != null ? (ImageType) Helper.getItemParam(Constants.IMAGE_TYPE) : new ImageType();
        switch (imageType.getPosImage()){
            case 1:
            case 2:
            case 3:
                resultIntent = new Intent(CameraActivity.this, CreateNooActivity.class);
                break;
            case 4:
            case 5:
            case 6:
                resultIntent = new Intent(CameraActivity.this, VisitActivity.class);
                break;
            case 7:
                resultIntent = new Intent(CameraActivity.this, ReturnAddActivity.class);
                break;
            case 8:
            case 9:
            case 10:
                resultIntent = new Intent(CameraActivity.this, DailySalesmanActivity.class);
                break;
        }
//        if (imageType.getPosImage() > 3) {
//            resultIntent = new Intent(CameraActivity.this, VisitActivity.class);
//        } else {
//            resultIntent = new Intent(CameraActivity.this, CreateNooActivity.class);
//        }

        if (uri != null) {
            resultIntent.putExtra(Constants.OUTPUT_CAMERA, uri);
        }
        startActivity(resultIntent);
        finish();
    }

    private File createImageFile() throws IOException {
        String imageFileName = "";
//        ImageType imageType = SessionManagerQubes.getImageType() != null ? SessionManagerQubes.getImageType() : new ImageType();
        ImageType imageType = Helper.getItemParam(Constants.IMAGE_TYPE) != null ? (ImageType) Helper.getItemParam(Constants.IMAGE_TYPE) : new ImageType();
        switch (imageType.getPosImage()) {
            case 1:
                imageFileName = "KTP" + Helper.getTodayDate(Constants.DATE_TYPE_18);
                break;
            case 2:
                imageFileName = "NPWP" + Helper.getTodayDate(Constants.DATE_TYPE_18);
                break;
            case 3:
                imageFileName = "Outlet" + Helper.getTodayDate(Constants.DATE_TYPE_18);
                break;
            case 4:
                imageFileName = "KM_Awal" + Helper.getTodayDate(Constants.DATE_TYPE_18);
                break;
            case 5:
                imageFileName = "KM_Akhir" + Helper.getTodayDate(Constants.DATE_TYPE_18);
                break;
            case 6:
                imageFileName = "Completed" + Helper.getTodayDate(Constants.DATE_TYPE_18);
                break;
            case 7:
                imageFileName = "Return" + Helper.getTodayDate(Constants.DATE_TYPE_18);
                break;
            case 8:
                imageFileName = "KTP" + Helper.getTodayDate(Constants.DATE_TYPE_18);
                break;
            case 9:
                imageFileName = "NPWP" + Helper.getTodayDate(Constants.DATE_TYPE_18);
                break;
            case 10:
                imageFileName = "Outlet" + Helper.getTodayDate(Constants.DATE_TYPE_18);
                break;
        }

        File storageDir = getDirLoc(getApplicationContext());
        File image = File.createTempFile(
                imageFileName,   //prefix
                ".jpg",   //suffix
                storageDir  //directory
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(fab)) {
            takePicture();
        }
//        else if (v.equals(btnSkip)) {
//            gotoConfirmPhoto(null);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UCrop.REQUEST_CROP:
                    gotoConfirmPhoto(croppedUri);
                    break;
            }
        } else {
            switch (requestCode) {
                case UCrop.REQUEST_CROP:
                    break;
            }
        }
    }

}

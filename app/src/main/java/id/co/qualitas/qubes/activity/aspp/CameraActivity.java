//package id.co.qualitas.qubes.activity.aspp;
//
//import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
//import static io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus;
//import static io.fotoapparat.parameter.selector.FocusModeSelectors.fixed;
//import static io.fotoapparat.parameter.selector.LensPositionSelectors.back;
//import static io.fotoapparat.parameter.selector.LensPositionSelectors.front;
//import static io.fotoapparat.parameter.selector.Selectors.firstAvailable;
//import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.yalantis.ucrop.UCrop;
//
//import java.io.File;
//import java.io.IOException;
//
//import id.co.qualitas.podv2.R;
//import id.co.qualitas.podv2.activity.BaseActivity;
//import id.co.qualitas.podv2.constants.Constants;
//import id.co.qualitas.podv2.fragment.ConfirmationDialogFragment;
//import id.co.qualitas.podv2.helper.Helper;
//import id.co.qualitas.podv2.session.SessionManager;
//import id.co.qualitas.podv2.utils.Utils;
//import io.fotoapparat.Fotoapparat;
//import io.fotoapparat.parameter.ScaleType;
//import io.fotoapparat.result.PhotoResult;
//import io.fotoapparat.view.CameraView;
//
//public class CameraActivity extends BaseActivity implements View.OnClickListener {
//
//    private Uri croppedUri;
//
//    private static final int[] FLASH_ICONS = {
//            R.drawable.ic_flash_auto,
//            R.drawable.ic_flash_off,
//            R.drawable.ic_flash_on,
//    };
//
//    private static final int[] FLASH_TITLES = {
//            R.string.flash_auto,
//            R.string.flash_off,
//            R.string.flash_on,
//    };
//
//    private CameraView mCameraView;
//    private ImageView fab;
//    private Toolbar toolbar;
//
//    private Button btnSkip;
//
//    private Fotoapparat fotoapparatFront, fotoapparatBack, fotoapparatCurrent;
//    private boolean isCameraStarted = false;
//    private boolean isCameraFrontFacing = false;
//    private TextView toolbarTitle;
//    private String currentPhotoPath;
//    private boolean notDelivered = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//
//        init();
//        initCamera();
//    }
//
//    private void init() {
//        toolbar = findViewById(R.id.toolbar);
//        btnSkip = findViewById(R.id.btnSkip);
//        toolbarTitle = findViewById(R.id.toolbar_title2);
//        mCameraView = findViewById(R.id.camera);
//        fab = findViewById(R.id.take_picture);
//        setSupportActionBar(toolbar);
//
//        btnSkip.setOnClickListener(this);
//        toolbarTitle.setText("");
//        fab.setOnClickListener(this);
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
//    }
//
//    private void initCamera() {
//        fotoapparatFront =
//                Fotoapparat
//                        .with(this)
//                        .into(mCameraView)           // view which will draw the camera preview
//                        .previewScaleType(ScaleType.CENTER_CROP)  // we want the preview to fill the view
//                        .photoSize(biggestSize())// we want to have the biggest photo possible
//                        .lensPosition(front())       // we want back camera
//                        .focusMode(firstAvailable(  // (optional) use the first focus mode which is supported by device
//                                continuousFocus(),
//                                autoFocus(),        // in case if continuous focus is not available on device, auto focus will be used
//                                fixed()             // if even auto focus is not available - fixed focus mode will be used
//                        )).build();
//
//        fotoapparatBack =
//                Fotoapparat
//                        .with(this)
//                        .into(mCameraView)
//                        .previewScaleType(ScaleType.CENTER_CROP)
//                        .photoSize(biggestSize())
//                        .lensPosition(back())
//                        .focusMode(firstAvailable(
//                                continuousFocus(),
//                                autoFocus(),
//                                fixed()
//                        )).build();
//
//        if (isCameraFrontFacing) {
//            fotoapparatCurrent = fotoapparatFront;
//        } else {
//            fotoapparatCurrent = fotoapparatBack;
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //permission don't allow
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
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        Constants.REQUEST_CAMERA_CODE);
//            }
//        } else {
//            startCamera();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        stopCamera();
//    }
//
//    private void startCamera() {
//        if (!isCameraStarted) {
//            isCameraStarted = true;
//            fotoapparatCurrent.start();
//        }
//    }
//
//    private void stopCamera() {
//        if (isCameraStarted) {
//            isCameraStarted = false;
//            fotoapparatCurrent.stop();
//        }
//    }
//
//    private void switchCamera() {
//        stopCamera();
//        if (isCameraFrontFacing) {
//            fotoapparatCurrent = fotoapparatBack;
//            isCameraFrontFacing = false;
//        } else {
//            fotoapparatCurrent = fotoapparatFront;
//            isCameraFrontFacing = true;
//        }
//        startCamera();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case Constants.REQUEST_CAMERA_CODE:
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
//                    onBackPressed();
//                    Toast.makeText(this, R.string.camera_and_storage_permission_not_granted, Toast.LENGTH_SHORT).show();
//                } else {
//                    startCamera();
//                }
//                break;
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.camera_options, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.switch_flash:
//                if (mCameraView != null) {
//                }
//                return true;
//
//            case R.id.switch_camera:
//                if (mCameraView != null) {
//                    switchCamera();
//                }
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void takePicture() {
//        PhotoResult photoResult = fotoapparatCurrent.takePicture();
//        getProgressDialogDefault().show();
////        File file = createBitmapFile();
//        File file = null;
//        try {
//            file = createImageFile();
//            File finalFile = file;
//            photoResult
//                    .saveToFile(file)
//                    .whenAvailable(aVoid -> {
//                        getProgressDialogDefault().dismiss();
//                        croppedUri = Utils.cropPhoto(Camera3PLActivity.this, Uri.fromFile(finalFile));
//                    });
//            Helper.removeImage(getApplicationContext(), file.getPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//            Utils.showToast(e.getMessage() != null ? e.getMessage() : "Failed to create image");
//        }
//    }
//
//    private void gotoConfirmPhoto(Uri uri) {
//        Intent resultIntent = new Intent(Camera3PLActivity.this, InstructionPhoto3PLActivity.class);
//
//        if (uri != null) {
//            resultIntent.putExtra(Constants.OUTPUT_CAMERA, uri);
//        }
//        startActivity(resultIntent);
//        finish();
//    }
//
//    private File createImageFile() throws IOException {
//        String imageFileName = "";
//        String doNo = SessionManager.getDoHeader() != null ? SessionManager.getDoHeader().getDoNumber() : "null";
//        String shipmentNo = SessionManager.getSelectedCustomerMobile() != null ? SessionManager.getSelectedCustomerMobile().getShipToNumber() : "null";
//
//        imageFileName = imageFileName + "_" + shipmentNo + "_" + doNo;
//
//        File storageDir = getDirLoc(getApplicationContext());
//        File image = File.createTempFile(
//                imageFileName,   //prefix
//                ".jpg",   //suffix
//                storageDir  //directory
//        );
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.equals(fab)) {
//            takePicture();
//        } else if (v.equals(btnSkip)) {
//            gotoConfirmPhoto(null);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case UCrop.REQUEST_CROP:
//                    gotoConfirmPhoto(croppedUri);
//                    break;
//            }
//        } else {
//            switch (requestCode) {
//                case UCrop.REQUEST_CROP:
//                    break;
//            }
//        }
//    }
//
//}

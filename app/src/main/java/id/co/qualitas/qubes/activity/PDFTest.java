package id.co.qualitas.qubes.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;
import java.util.HashMap;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.utils.LashPdfUtils;
import id.co.qualitas.qubes.utils.Utils;

public class PDFTest extends BaseActivity {

    private LashPdfUtils pdfUtils;
    private File pdfFile;
    private Boolean success = false;
    private PDFView pdfView;
    private TextView txtPath;
    //EXPORT N IMPORT
    private static final int PERMISSION_STORAGE_END = 222;
    private static final int PERMISSION_READ_STORAGE_CODE = 2000;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_pdf);
        pdfView = findViewById(R.id.pdfView);
        txtPath = findViewById(R.id.path);
        pdfUtils = LashPdfUtils.getInstance(PDFTest.this);

        new AsyncTaskGeneratePDF().execute();

        txtPath.setOnClickListener(v -> {
//            if (checkPermission()) {
                new AsyncTaskGeneratePDF().execute();
//                exportDB(getApplicationContext());
//            } else {
//                setToast(getString(R.string.pleaseEnablePermission));
//                requestPermission();
//            }
        });
    }

    private class AsyncTaskGeneratePDF extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                pdfFile = new File(Utils.getDirLocPDF(getApplicationContext()) + "/lash.pdf");
//                if (pdfFile.getParentFile().exists()) {
                success = pdfUtils.createPDF(pdfFile);
//                } else {
//                    setToast(pdfFile.getAbsolutePath() + " doesn't exists");
//                }
                return success;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result == null) {
                Utils.showToast("generate pdf failed, please try again");
            } else {
                if (result) {
                    loadPdfViewer(pdfView, pdfFile);
                    txtPath.setText(pdfFile.getAbsolutePath());
                } else {
                    setToast("Can't show pdf A4. Please try again later..");
                }
            }
        }
    }

    private void loadPdfViewer(PDFView pdfView, File pdfFile) {
        pdfView.fromFile(pdfFile).enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false).enableDoubletap(true).defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
                .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                .password(null).scrollHandle(null).enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0).autoSpacing(true) // add dynamic spacing to fit each page on its own on the screen
                .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
//                .pageSnap(false) // snap pages to screen boundaries
//                .pageFling(false) // make a fling change only a single page like ViewPager
                .swipeHorizontal(true).pageSnap(true).autoSpacing(true).pageFling(true).onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                }).nightMode(false) // toggle night mode
                .load();
    }

    //EXPORT N IMPORT SQLITE
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            boolean check = Environment.isExternalStorageManager() && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            return check;
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
        }
    }

    private void requestPermission11() {
        ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    new AsyncTaskGeneratePDF().execute();//first
                } else {
                    setToast(getString(R.string.pleaseEnablePermission));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    requestPermission11();
                } else {
                    setToast("Allow permission for storage access!");
                }
            }
        }
    }

}

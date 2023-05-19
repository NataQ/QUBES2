package id.co.qualitas.qubes.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_pdf);
        pdfView = findViewById(R.id.pdfView);
        txtPath = findViewById(R.id.path);
        pdfUtils = LashPdfUtils.getInstance(PDFTest.this);
        new AsyncTaskGeneratePDF().execute();//first
    }

    private class AsyncTaskGeneratePDF extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                pdfFile = new File(Utils.getDirLocPDF(getApplicationContext()) + "/text.pdf");
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

}

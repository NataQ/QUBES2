package id.co.qualitas.qubes.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.MainActivityDrawer;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;

public class ImageDetailFragment extends BaseFragment {
    private id.co.qualitas.qubes.helper.TouchImageView img;
    private String specimen;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_image_zoom, container, false);


        getActivity().setTitle(getString(R.string.navmenu11));
        ((MainActivityDrawer)getActivity()).enableBackToolbar(true);
        init();

        initialize();

        loadImage();

        return rootView;
    }

    private void loadImage() {
        new Thread().execute();
    }

    private Bitmap getImage() {
        String urls = Helper.getItemParam(Constants.URL).toString() + Constants.API_GET_IMAGE + "?id=" + Helper.getItemParam(Constants.OUTLET_CODE);
        URL url = null;
        try {
            url = new URL(urls);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmp;
    }

    private void initialize() {
        img = rootView.findViewById(R.id.myZoomageView);
    }

    private class Thread extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {

            return getImage();
        }

        @Override
        protected void onPreExecute() {
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bm) {
            if (bm != null) {
                img.setImageBitmap(bm);
            }

            progress.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
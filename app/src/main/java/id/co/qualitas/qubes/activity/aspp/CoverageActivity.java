package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.model.Customer;

public class CoverageActivity extends BaseActivity {
    private MapView mMapView;
    private IMapController mapController;
    private static final String TAG = "CoverageActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private BoundingBox boundingBox;
    private List<GeoPoint> geoPointList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handle permissions first, before mMapView is created. not depicted here
        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the mMapView has a writable location for the mMapView cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the mMapView
        setContentView(R.layout.aspp_fragment_coverage);

        mMapView = findViewById(R.id.mapView);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mMapView.setMultiTouchControls(true);
        mMapView.getController().setZoom(15.0);

        //icon here aja
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), mMapView);
//        mLocationOverlay.enableMyLocation();
        mMapView.getOverlays().add(mLocationOverlay);

        //icon compass
        mCompassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), mMapView);
        mCompassOverlay.enableCompass();
        mMapView.getOverlays().add(mCompassOverlay);

//        mRotationGestureOverlay = new RotationGestureOverlay(getApplicationContext(), mMapView);
//        mRotationGestureOverlay.setEnabled(true);
//        mMapView.setMultiTouchControls(true);
//        mMapView.getOverlays().add(this.mRotationGestureOverlay);

        //your items
        List<Customer> custList = new ArrayList<>();
        custList.add(new Customer("BO", "Black Owl", "Golf Island Beach Theme Park, Jl. Pantai Indah Kapuk No.77, Kamal Muara, DKI Jakarta 14470", true, -6.090263984566263, 106.74593288657607));
        custList.add(new Customer("PCP", "Pantjoran Chinatown PIK", "Unnamed Road, 14460", false, -6.09047339393416, 106.74535959301855));
        custList.add(new Customer("CMP", "Central Market PIK", "Golf Island, Kawasan Pantai Maju, Jl, Jl. Boulevard Raya, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14470", true, -6.09102018270127, 106.74661148098058));
        custList.add(new Customer("CHGI", "Cluster Harmony, Golf Island", "WP6W+7JR, Pantai Indah Kapuk St, Kamal Muara, Penjaringan, North Jakarta City, Jakarta 14460", false, -6.089065696336256, 106.74676357552187));
        custList.add(new Customer("MSGIP", "Monsieur Spoon Golf Island PIK", "Urban Farm, Unit 5, Kawasan Pantai Maju Jl. The Golf Island Boulevard, Kel, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14460", true, -6.09032214182743, 106.74191982249332));
        custList.add(new Customer("KMPP", "K3 Mart PIK Pantjoran", "Golf Island, Ruko Blok D No.02A, Kamal Muara, Jkt Utara, Daerah Khusus Ibukota Jakarta 11447", false, -6.088542162422348, 106.74239952686823));

//        zoomToBounds(custList);

        ArrayList<OverlayItem> items = setOverLayItems(custList);

        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, getApplicationContext());
        mOverlay.setFocusItemsOnTap(true);
        mOverlay.setOnFocusChangeListener(new ItemizedOverlay.OnFocusChangeListener() {
            @Override
            public void onFocusChanged(ItemizedOverlay<?> overlay, OverlayItem newFocus) {
                mOverlay.unSetFocusedItem();
                mOverlay.setFocusedItem(newFocus);
            }
        });

        mMapView.getOverlays().add(mOverlay);
        mMapView.getController().setCenter(computeCentroid(custList));

//        boundingBox = new BoundingBox();
//        boundingBox.fromGeoPointsSafe(geoPointList);
//        mMapView.zoomToBoundingBox(boundingBox, true);

    }


    private Bitmap getMarkerBitmapFromView(Customer cust) {
        //HERE YOU CAN ADD YOUR CUSTOM VIEW
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);

        //IN THIS EXAMPLE WE ARE TAKING TEXTVIEW BUT YOU CAN ALSO TAKE ANY KIND OF VIEW LIKE IMAGEVIEW, BUTTON ETC.
        TextView txt_name = customMarkerView.findViewById(R.id.txt_name);
        TextView txt_add = customMarkerView.findViewById(R.id.txt_add);
        ImageView imgStore = customMarkerView.findViewById(R.id.imgStore);

        txt_name.setText(cust.getId() + " - " + cust.getNama());
//        txt_add.setText(cust.getAddress());
        if (cust.isRoute()) {
            imgStore.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.marker_blue));
        } else {
            imgStore.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.marker_red));
        }

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null) drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private GeoPoint computeCentroid(List<Customer> points) {
        double latitude = 0;
        double longitude = 0;
        int n = points.size();

        for (Customer point : points) {
            latitude += point.getLatitude();
            longitude += point.getLongitude();
        }

        return new GeoPoint(latitude / n, longitude / n);
    }

    private ArrayList<OverlayItem> setOverLayItems(List<Customer> customers) {
        Drawable markerRed = ContextCompat.getDrawable(getApplicationContext(), R.drawable.marker_red);
        Drawable markerBlue = ContextCompat.getDrawable(getApplicationContext(), R.drawable.marker_blue);
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

        geoPointList = new ArrayList<>();

        for (Customer cust : customers) {
            OverlayItem ov = new OverlayItem(cust.getId() + "-" + cust.getNama(), cust.getAddress(), new GeoPoint(cust.getLatitude(), cust.getLongitude()));
//            if (cust.isRoute()) {
//
//            } else {
//                ov.setMarker(markerRed);
//            }
            ov.setMarker(new BitmapDrawable(getResources(), getMarkerBitmapFromView(cust)));
            items.add(ov);

            geoPointList.add(new GeoPoint(cust.getLatitude(), cust.getLongitude()));
        }
        return items;
    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        if (mMapView != null)
            mMapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        if (mMapView != null)
            mMapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }
}

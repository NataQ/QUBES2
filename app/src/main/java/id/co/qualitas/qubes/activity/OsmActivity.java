package id.co.qualitas.qubes.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;

public class OsmActivity extends AppCompatActivity {
    private MapView mMapView;
    private IMapController mapController;
    private static final String TAG = "OsmActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;

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
        setContentView(R.layout.activity_maps);

        mMapView = findViewById(R.id.mapView);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        mapController = mMapView.getController();
        mapController.setZoom(18);

        //icon here aja
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), mMapView);
        mLocationOverlay.enableMyLocation();
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
        List<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(new GeoPoint(-6.088578550359071, 106.69715529826526));
        geoPoints.add(new GeoPoint(-6.089667657056153, 106.69914254522651));
        geoPoints.add(new GeoPoint(-6.087210800359574, 106.69752915396195));
        geoPoints.add(new GeoPoint(-6.089007607038804, 106.69676394516692));
        geoPoints.add(new GeoPoint(-6.089640154498307, 106.69651502050023));
        geoPoints.add(new GeoPoint(-6.0899335116841, 106.69844187205418));

        ArrayList<OverlayItem> items = setOverLayItems(geoPoints);

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

//        GeoPoint startPoint = new GeoPoint(-6.088578550359071, 106.69715529826526);
//        mapController.setCenter(startPoint);
    }

    private ArrayList<OverlayItem> setOverLayItems(List<GeoPoint> geoPoints) {
        Drawable marker = ContextCompat.getDrawable(getApplicationContext(), R.drawable.marker_red);
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

        for (GeoPoint geoPo : geoPoints) {
            OverlayItem ov = new OverlayItem("Home", "Description", geoPo);
            ov.setMarker(marker);
            items.add(ov);
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

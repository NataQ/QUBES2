package id.co.qualitas.qubes.fragment.aspp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

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
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.User;

public class CoverageFragment extends BaseFragment implements LocationListener {
    private MapView mMapView;
    private ImageView imgBack;
    private Button btnZoom, btCenterMap;
    private IMapController mapController;
    private static final String TAG = "CoverageActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private BoundingBox boundingBox;
    private List<GeoPoint> geoPointList;
    private List<Customer> custList;
    private LocationManager lm;
    private Location currentLocation = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_coverage, container, false);
        getActivity().setTitle(getString(R.string.coverage));
        Context ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0l, 0f, this);

        initProgress();
        initFragment();
        initialize();
        setMap();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changePage(2);
            }
        });

        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setZoom();
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    setEnabled(false);
//                    requireActivity().onBackPressed();
                    ((MainActivity) getActivity()).changePage(2);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    private void setMap() {
        mapController = mMapView.getController();
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mMapView.setMultiTouchControls(true);
        mapController.setZoom(Constants.ZOOM_LEVEL);

//        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mMapView);
//        mMapView.getOverlays().add(mLocationOverlay);
//        mLocationOverlay.enableMyLocation();

        //icon compass
        mCompassOverlay = new CompassOverlay(getContext(), new InternalCompassOrientationProvider(getContext()), mMapView);
        mCompassOverlay.enableCompass();
        mMapView.getOverlays().add(mCompassOverlay);

        //your items
        custList = new ArrayList<>();
        custList.add(new Customer("BO", "Black Owl", "Golf Island Beach Theme Park, Jl. Pantai Indah Kapuk No.77, Kamal Muara, DKI Jakarta 14470", true, -6.090263984566263, 106.74593288657607));
        custList.add(new Customer("PCP", "Pantjoran Chinatown PIK", "Unnamed Road, 14460", false, -6.09047339393416, 106.74535959301855));
        custList.add(new Customer("CMP", "Central Market PIK", "Golf Island, Kawasan Pantai Maju, Jl, Jl. Boulevard Raya, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14470", true, -6.09102018270127, 106.74661148098058));
        custList.add(new Customer("CHGI", "Cluster Harmony, Golf Island", "WP6W+7JR, Pantai Indah Kapuk St, Kamal Muara, Penjaringan, North Jakarta City, Jakarta 14460", false, -6.089065696336256, 106.74676357552187));
        custList.add(new Customer("MSGIP", "Monsieur Spoon Golf Island PIK", "Urban Farm, Unit 5, Kawasan Pantai Maju Jl. The Golf Island Boulevard, Kel, Kamal Muara, Kec. Penjaringan, Daerah Khusus Ibukota Jakarta 14460", true, -6.09032214182743, 106.74191982249332));
        custList.add(new Customer("KMPP", "K3 Mart PIK Pantjoran", "Golf Island, Ruko Blok D No.02A, Kamal Muara, Jkt Utara, Daerah Khusus Ibukota Jakarta 11447", false, -6.088542162422348, 106.74239952686823));

//        zoomToBounds(custList);

        ArrayList<OverlayItem> items = Helper.setOverLayItems(custList, getActivity());

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
                }, getContext());
        mOverlay.setFocusItemsOnTap(true);
        mOverlay.setOnFocusChangeListener(new ItemizedOverlay.OnFocusChangeListener() {
            @Override
            public void onFocusChanged(ItemizedOverlay<?> overlay, OverlayItem newFocus) {
                mOverlay.unSetFocusedItem();
                mOverlay.setFocusedItem(newFocus);
            }
        });

        mMapView.getOverlays().add(mOverlay);
        mapController.setCenter(Helper.computeCentroid(custList));

//        double minLat = Integer.MAX_VALUE;
//        double maxLat = Integer.MIN_VALUE;
//        double minLong = Integer.MAX_VALUE;
//        double maxLong = Integer.MIN_VALUE;
//        for (OverlayItem item : items) {
//            GeoPoint point = (GeoPoint) item.getPoint();
//            if (point.getLatitude() < minLat)
//                minLat = point.getLatitude();
//            if (point.getLatitude() > maxLat)
//                maxLat = point.getLatitude();
//            if (point.getLongitude() < minLong)
//                minLong = point.getLongitude();
//            if (point.getLongitude() > maxLong)
//                maxLong = point.getLongitude();
//        }
//
//        boundingBox = new BoundingBox(maxLat, maxLong, minLat, minLong);
//        mMapView.zoomToBoundingBox(boundingBox, true);
//        mMapView.invalidate();
    }

    private void setZoom() {
        if (!geoPointList.isEmpty()) {
            double north = -90;
            double south = 90;
            double west = 180;
            double east = -180;

            for (GeoPoint position : geoPointList) {
                north = Math.max(position.getLatitude(), north);
                south = Math.min(position.getLatitude(), south);

                west = Math.min(position.getLongitude(), west);
                east = Math.max(position.getLongitude(), east);
            }

            Log.d(TAG, String.format("North %f, south %f, west %f, east %f", north, south, west, east));
            BoundingBox boundingBox = new BoundingBox(north, west, south, east);
            mMapView.zoomToBoundingBox(boundingBox, false);
            mapController.setCenter(Helper.computeCentroid(custList));
            mMapView.invalidate();
        }
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        imgBack = rootView.findViewById(R.id.imgBack);
        btCenterMap = rootView.findViewById(R.id.btCenterMap);
        btnZoom = rootView.findViewById(R.id.btnZoom);
        mMapView = rootView.findViewById(R.id.mapView);
    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        Helper.setItemParam(Constants.CURRENTPAGE, "23");
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
            if (getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    getContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
package id.co.qualitas.qubes.fragment.aspp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.aspp.AutoCompleteRouteAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.GPSTracker;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class DirectionFragment extends BaseFragment {
    private MapView mMapView;
    ArrayList<OverlayItem> items;
    ItemizedOverlayWithFocus<OverlayItem> mOverlay;
    private OverlayItem ov, ov1;
    private IMapController mapController;
    private static final String TAG = "DirectionActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private BoundingBox boundingBox;
    //    private List<GeoPoint> geoPointList;
    private List<Customer> custList;
    private AutoCompleteTextView edtStartPoint, edtEndPoint;
    private ImageView imgCurrentStartingPoint, imgCurrentEndPoint, imgBack;
    private TextView txtStore, txtAddress, txtRoute;
    private Button btnMaps;
    private AutoCompleteRouteAdapter startPointAdapter, endPointAdapter;
    private Customer startPointCustomer, currLocation, endPointCustomer;

    final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionLauncher;
    private Customer routeCustHeader;
    Customer currentLoc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_direction, container, false);
        getActivity().setTitle(getString(R.string.directions));
        Context ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        initialize();
        setData();

        startPointAdapter = new AutoCompleteRouteAdapter(getContext(), custList);
        edtStartPoint.setAdapter(startPointAdapter);
        edtStartPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //this is the way to find selected object/item
                startPointCustomer = (Customer) adapterView.getItemAtPosition(pos);
                if (startPointCustomer != null && endPointCustomer != null)
                    setMap(startPointCustomer, endPointCustomer);
            }
        });

        endPointAdapter = new AutoCompleteRouteAdapter(getContext(), custList);
        edtEndPoint.setAdapter(endPointAdapter);
        edtEndPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //this is the way to find selected object/item
                endPointCustomer = (Customer) adapterView.getItemAtPosition(pos);
                if (startPointCustomer != null && endPointCustomer != null)
                    setMap(startPointCustomer, endPointCustomer);
            }
        });

        btnMaps.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + startPointCustomer.getLatitude() + "," + startPointCustomer.getLongitude()
                                + "&daddr=" + endPointCustomer.getLatitude() + "," + endPointCustomer.getLongitude()));
                startActivity(intent);
            } catch (ActivityNotFoundException ane) {
                Toast.makeText(getActivity(), "Please Install Google Maps ", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.getMessage();
            }
        });

        imgCurrentStartingPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
                edtStartPoint.setText("Current Location");
                startPointCustomer = new Customer("", "Current Location", currentLoc.getAddress(), false, currentLoc.getLatitude(), currentLoc.getLongitude());
                if (startPointCustomer != null && endPointCustomer != null)
                    setMap(startPointCustomer, endPointCustomer);
            }
        });

        imgCurrentEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
                edtEndPoint.setText("Current Location");
                endPointCustomer = new Customer("", "Current Location", currentLoc.getAddress(), false, currentLoc.getLatitude(), currentLoc.getLongitude());
                if (startPointCustomer != null && endPointCustomer != null)
                    setMap(startPointCustomer, endPointCustomer);
            }
        });

        multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, isGranted -> {
            if (isGranted.containsValue(false)) {
                multiplePermissionLauncher.launch(PERMISSIONS);
            } else {
                gpsOn();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changePage(2);
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

    private void setData() {
        if (SessionManagerQubes.getRouteCustomerHeader() != null) {
            routeCustHeader = SessionManagerQubes.getRouteCustomerHeader();
            custList = new ArrayList<>();
            custList = database.getAllCustomerVisit(null, false);

            txtStore.setText(routeCustHeader.getId() + " - " + routeCustHeader.getNama());
            txtAddress.setText(routeCustHeader.getAddress());
            edtEndPoint.setText(routeCustHeader.getNama());
            txtRoute.setText(routeCustHeader.getRute());

            getMyLocation();

            edtStartPoint.setText("Current Location");
            startPointCustomer = new Customer("", "Current Location", currentLoc.getAddress(), false, currentLoc.getLatitude(), currentLoc.getLongitude());
            endPointCustomer = new Customer(routeCustHeader.getId(), routeCustHeader.getNama(), routeCustHeader.getAddress(), true, routeCustHeader.getLatitude(), routeCustHeader.getLongitude());
            if (startPointCustomer != null && endPointCustomer != null)
                setMap(startPointCustomer, endPointCustomer);
        } else {
            ((MainActivity) getActivity()).changePage(2);
        }


    }

    private void setMap(Customer startPointCustomer, Customer endPointCustomer) {
//        zoomToBounds(custList)

        if (mOverlay != null) {
            try {
                mOverlay.removeAllItems();
                mMapView.getOverlays().add(mOverlay);
                mMapView.invalidate();
            } catch (Exception e) {
            }
        }

        items = new ArrayList<>();
        ov = new OverlayItem(startPointCustomer.getId() + "" + startPointCustomer.getNama(), startPointCustomer.getAddress(), new GeoPoint(startPointCustomer.getLatitude(), startPointCustomer.getLongitude()));
        ov.setMarker(new BitmapDrawable(getResources(), getMarkerBitmapFromView(startPointCustomer, true)));
        items.add(ov);

        ov1 = new OverlayItem(endPointCustomer.getId() + "" + endPointCustomer.getNama(), endPointCustomer.getAddress(), new GeoPoint(endPointCustomer.getLatitude(), endPointCustomer.getLongitude()));
        ov1.setMarker(new BitmapDrawable(getResources(), getMarkerBitmapFromView(endPointCustomer, false)));
        items.add(ov1);

        //the overlay
        mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
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
        mMapView.getController().setCenter(computeCentroid(startPointCustomer, endPointCustomer));

//        boundingBox = new BoundingBox();
//        boundingBox.fromGeoPointsSafe(geoPointList);
//        mMapView.zoomToBoundingBox(boundingBox, true, 100);
    }

    private void initialize() {
        gpsTracker = new GPSTracker(getActivity());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        mMapView = rootView.findViewById(R.id.mapView);
        btnMaps = rootView.findViewById(R.id.btnMaps);
        edtStartPoint = rootView.findViewById(R.id.edtStartPoint);
        edtEndPoint = rootView.findViewById(R.id.edtEndPoint);
        txtStore = rootView.findViewById(R.id.txtStore);
        txtAddress = rootView.findViewById(R.id.txtAddress);
        txtRoute = rootView.findViewById(R.id.txtRoute);
        imgCurrentStartingPoint = rootView.findViewById(R.id.imgCurrentStartingPoint);
        imgCurrentEndPoint = rootView.findViewById(R.id.imgCurrentEndPoint);
        imgBack = rootView.findViewById(R.id.imgBack);

        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mMapView.setMultiTouchControls(true);
        mMapView.getController().setZoom(Constants.ZOOM_LEVEL);

        //icon here aja
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), mMapView);
//        mMapView.getOverlays().add(mLocationOverlay);
        mLocationOverlay.disableMyLocation();

        //icon compass
        mCompassOverlay = new CompassOverlay(getContext(), new InternalCompassOrientationProvider(getContext()), mMapView);
        mCompassOverlay.enableCompass();
        mMapView.getOverlays().add(mCompassOverlay);
    }

    private Bitmap getMarkerBitmapFromView(Customer cust, boolean start) {
        //HERE YOU CAN ADD YOUR CUSTOM VIEW
        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);

        //IN THIS EXAMPLE WE ARE TAKING TEXTVIEW BUT YOU CAN ALSO TAKE ANY KIND OF VIEW LIKE IMAGEVIEW, BUTTON ETC.
        TextView txt_name = customMarkerView.findViewById(R.id.txt_name);
        TextView txt_add = customMarkerView.findViewById(R.id.txt_add);
        ImageView imgStore = customMarkerView.findViewById(R.id.imgStore);

        txt_name.setText(cust.getId() + " - " + cust.getNama());
//        txt_add.setText(cust.getAddress());
        if (start) {
            imgStore.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_marker_blue));
        } else {
            imgStore.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_marker_red));
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

    private GeoPoint computeCentroid(Customer startPoint, Customer endPoint) {
        double latitude = 0;
        double longitude = 0;

        latitude += startPoint.getLatitude();
        latitude += endPoint.getLatitude();
        longitude += startPoint.getLongitude();
        longitude += endPoint.getLongitude();

        return new GeoPoint(latitude / 2, longitude / 2);
    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        Helper.setItemParam(Constants.CURRENTPAGE, "24");
        checkPermissionGPS();
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

    public void checkPermissionGPS() {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            gpsOn();
        }
    }

    public void gpsOn() {
        if (Utils.isGPSOn(getContext())) {
            getMyLocation();
//            getCurrentLocation();
        } else {
            Utils.turnOnGPS(getActivity());
        }
    }

    private void getMyLocation() {
        if (gpsTracker.canGetLocation()) {
            String address = Utils.getCurrentAddress(getContext(), gpsTracker.getLatitude(), gpsTracker.getLongitude());
            currentLoc = new Customer("", "Current Location", address, false, gpsTracker.getLatitude(), gpsTracker.getLongitude());
        } else {
            currentLoc = new Customer("", "Current Location", "", false, 0, 0);
        }
//        custList.add(0, currentLoc);
    }

    public boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    public void requestPermissions() {
        multiplePermissionLauncher.launch(PERMISSIONS);
    }

}
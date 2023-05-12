package id.co.qualitas.qubes.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.NewMainActivity;
import id.co.qualitas.qubes.adapter.OutletAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.GPSTracker;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.GPSModel;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.User;

public class RetailOutletFragment extends BaseFragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapView mMapView;
    private View rootView;
    private GPSTracker gpsTracker;
    private LatLng currentLocation, startLocation;
    private Marker ml;
    private Circle mCircle;
    private TextView errData;
    private ProgressBar spinner;
    private String outletName = Constants.EMPTY_STRING;
    private ArrayList<OutletResponse> outletResponseArrayList = new ArrayList<>();
    private boolean checkIns = false;

    private FloatingActionButton btnNewVisits;

    private TextView txtTotalCustomer;
    Bundle savedInstanceState;
    private static final int REQUEST_LOCATION_PERMISSION = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_outlet_home, container, false);
//        getActivity().setTitle(getString(R.string.navmenu5));
//        ((MainActivityDrawer) getActivity()).enableBackToolbar(false);

        initialize();
        initFragment();
        init();
        mMapView.onCreate(savedInstanceState);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((NewMainActivity) getActivity()).changePage(3);
                        return true;
                    }
                }
                return false;
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            checkGPS();
        }

        btnNewVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preventDupeDialog();
                Helper.setItemParam(Constants.OUTLET_LIST_DETAIL, outletResponseArrayList);
                openDialog(DIALOG_ADD_NEW_VISIT);
            }
        });

        return rootView;
    }

    private void checkGPS() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            Toast.makeText(getActivity(), "Please enable your gps", Toast.LENGTH_SHORT).show();
                            ((NewMainActivity) getActivity()).changePage(1);
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
//            getData();

//            if (mMap != null) {
                initMap();
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0) {
                    boolean finelocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean coarselocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (finelocation && coarselocation) {
                        checkGPS();
                    } else {
                        Toast.makeText(getActivity(), "Please enable your gps", Toast.LENGTH_SHORT).show();
                        //show error msg
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void initMap() {
        if (isGPSEnabled(getContext())) {
            mMapView.onResume();
        }

        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            googleMap.getUiSettings().setAllGesturesEnabled(true);

            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean gps = getMyLocation();

            if (gps && currentLocation != null) {
                CameraPosition cameraPosition1 = new CameraPosition.Builder().target(currentLocation).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition1));
                Helper.setItemParam(Constants.MARKER, "true");
                MarkerOptions myLoc = new MarkerOptions()
                        .position(currentLocation)
//                        .position(googleMap.getCameraPosition().target)
                        .icon(BitmapDescriptorFactory.fromBitmap(drawTextToBitmap(rootView.getContext())))
                        .anchor(0.5f, 1);
                ml = googleMap.addMarker(myLoc);
                startLocation = new LatLng(-6.9027117, 107.6037877);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(15.0f).build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.moveCamera(cameraUpdate);
                mMap = googleMap;
            } else {
                gpsTracker.showSettingsAlert();
            }
        } else {
            mMap = null;
        }
    }

    public Bitmap drawTextToBitmap(Context gContext) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(rootView.getResources(), R.drawable.grey_circle);//grey_circle
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(ContextCompat.getColor(getContext(), R.color.new_blue));
        // text size in pixels
        paint.setTextSize((int) (10 * scale));//14
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds("You", 0, "You".length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;

        canvas.drawText("You", x, y, paint);
        Bitmap bitmapBack;
        if (Helper.getItemParam(Constants.MARKER) != null) {
            bitmapBack = BitmapFactory.decodeResource(rootView.getResources(), R.drawable.marker_blue);
            Helper.removeItemParam(Constants.MARKER);
        } else {
            bitmapBack = BitmapFactory.decodeResource(rootView.getResources(), R.drawable.marker_red);
        }
        bitmapBack = bitmapBack.copy(Bitmap.Config.ARGB_8888, true);

        return mergeToPin(bitmapBack, bitmap);
    }

    public static Bitmap mergeToPin(Bitmap back, Bitmap front) {
        Bitmap result = Bitmap.createBitmap(back.getWidth(), back.getHeight(), back.getConfig());
        Canvas canvas = new Canvas(result);
        int widthBack = back.getWidth();
        int widthFront = front.getWidth();
        float move = (widthBack - widthFront) / 2;
        canvas.drawBitmap(back, 0f, 0f, null);
        canvas.drawBitmap(front, move, move, null);
        return result;
    }

    @SuppressLint("SetTextI18n")
    public void setData(ArrayList<OutletResponse> listRetailOulets) {
        OutletAdapter mAdapter = new OutletAdapter(listRetailOulets, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initialize() {
        db = new DatabaseHelper(rootView.getContext());

        mMapView = rootView.findViewById(R.id.map);

        mRecyclerView = rootView.findViewById(R.id.today_retail_recycle_view);
        mRecyclerView.setVisibility(View.VISIBLE);
        gpsTracker = new GPSTracker(getActivity());
        btnNewVisits = rootView.findViewById(R.id.btnNewVisits);
        spinner = rootView.findViewById(R.id.progressBar);
        errData = rootView.findViewById(R.id.noticeErrData);
        txtTotalCustomer = rootView.findViewById(R.id.txtTotalCustomer);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
    }

    private boolean getMyLocation() {
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            currentLocation = new LatLng(latitude, longitude);

            drawMarkerWithCircle(currentLocation);
            return true;
        } else {
            return false;
        }
    }

    public boolean isGPSEnabled(Context mContext) {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void drawMarkerWithCircle(LatLng position) {
        double radiusInMeters = 500.0;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        if (mMap != null) {
            mCircle = mMap.addCircle(circleOptions);
        }

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        if (mMap != null) {
            ml = mMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
        mMapView.onResume();
    }

    @SuppressWarnings("unchecked")
    public void deleteVisit(String id, String idOutlet) {
        errData.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        OutletResponse delete = new OutletResponse();
        delete.setIdOutlet(idOutlet);
        delete.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
        delete.setDeleted(true);
        db.updateDeletedVisitPlan(delete);

        OutletResponse outlet = new OutletResponse();
        outlet.setId(id);
        ArrayList<OutletResponse> listDeleteNewVisit = new ArrayList<>();
        if (Helper.getItemParam(Constants.LIST_DELETE_NEW_VISIT) != null) {
            listDeleteNewVisit = (ArrayList<OutletResponse>) Helper.getItemParam(Constants.LIST_DELETE_NEW_VISIT);
            Helper.removeItemParam(Constants.LIST_DELETE_NEW_VISIT);
        }
        listDeleteNewVisit.add(outlet);
        Helper.setItemParam(Constants.LIST_DELETE_NEW_VISIT, listDeleteNewVisit);

        setVisitData();
    }

    private void getData() {
        if (Helper.getItemParam(Constants.GET_DETAIL_NEW_VISIT) != null) {
            Helper.removeItemParam(Constants.GET_DETAIL_NEW_VISIT);
            OutletResponse outletChoose = (OutletResponse) Helper.getItemParam(Constants.OUTLET);

            if (outletChoose != null) {
                if (outletChoose.getIdOutlet() != null) {
                    //TODO: check lagi
                    outletChoose.setId(Constants.ID_VP_MOBILE.concat(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
                    outletChoose.setEnabled(false);
                    outletChoose.setStatusCheckIn(Constants.UNCHECKED_IN);
                    if (user != null) {
                        outletChoose.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                        db.addVisitPlan(outletChoose);

                        setVisitData();
                    }
                }
            }

        } else {
            setVisitData();
        }
    }

    @SuppressLint("SetTextI18n")
    public void setVisitData() {
        errData.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        if (user != null) {
            if (user.getCurrentDate() != null) {
                outletResponseArrayList = new ArrayList<>();
                outletResponseArrayList = db.getAllVisitPlan(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));

                if (outletResponseArrayList != null && !outletResponseArrayList.isEmpty()) {
                    for (OutletResponse outlet : outletResponseArrayList) {

                        try {
                            if (outlet.getIdOutlet() != null || !outlet.getIdOutlet().equals("null")) {
                                outletName = db.getOutletName(outlet.getIdOutlet());
                            }
                            outlet.setOutletName(outletName);
                            if (outlet.getCheck_out_time() != null) {
                                outlet.setStatusCheckIn(Constants.FINISHED);
                            } else if (outlet.getContinue_time() != null) {
                                if (outlet.getPause_time() != null) {
                                    if (Long.parseLong(outlet.getPause_time()) > Long.parseLong(outlet.getContinue_time())) {
                                        outlet.setStatusCheckIn(Constants.PAUSE);
                                    } else {
                                        outlet.setStatusCheckIn(Constants.CHECK_IN);
                                    }
                                } else {
                                    outlet.setStatusCheckIn(Constants.CHECK_IN);
                                }
                            } else if (outlet.getPause_time() != null) {
                                outlet.setStatusCheckIn(Constants.PAUSE);
                            } else if (outlet.getCheckInTime() != null) {
                                outlet.setStatusCheckIn(Constants.CHECK_IN);
                            } else {
                                outlet.setStatusCheckIn(Constants.UNCHECKED_IN);
                            }
                        } catch (NullPointerException ignored) {

                        }
                    }
                    Helper.setItemParam(Constants.OUTLET_LIST_TODAY, outletResponseArrayList);
                    setData(outletResponseArrayList);
                }
                setTotalOutlet(outletResponseArrayList);
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private void setTotalOutlet(ArrayList<OutletResponse> list) {
        txtTotalCustomer.setText("Total Customer: " + String.valueOf(list != null ? list.size() : 0));
    }

    public void changeFragment() {
        ((NewMainActivity) getActivity()).changePage(8);
    }

    @SuppressLint("StaticFieldLeak")
    private class Thread extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                if (gpsTracker.canGetLocation()) {
                    GPSModel gpsModel = new GPSModel();
                    gpsModel.setLatitude(String.valueOf(gpsTracker.getLatitude()));
                    gpsModel.setLongitude(String.valueOf(gpsTracker.getLongitude()));
                    gpsModel.setCountry(gpsTracker.getLocality(getContext()));
                    gpsModel.setPostalCode(gpsTracker.getPostalCode(getContext()));
                    gpsModel.setAddressLine(gpsTracker.getAddressLine(getContext()));
                    Helper.setItemParam(Constants.GPS_DATA, gpsModel);
                } else {
                    progress.dismiss();
                }

            } catch (Exception ex) {
                return null;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            progress.dismiss();
            if (msg != null) {
                if (msg) {
                    if (checkIns) {
                        openDialog(DIALOG_CHECK_IN);
                    }
                }
            } else {
                gpsTracker.showSettingsAlert();
            }
        }
    }

    public void getLocation() {
        checkIns = true;
        gpsTracker = new GPSTracker(getActivity());
        new Thread().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(true);
        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.toolbarSearchHint));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return super.onQueryTextSubmit(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            if (outletResponseArrayList != null && !outletResponseArrayList.isEmpty()) {
                setData(outletResponseArrayList);
                setTotalOutlet(outletResponseArrayList);
            }
            return false;
        }

        final ArrayList<OutletResponse> filteredList = new ArrayList<>();

        for (OutletResponse outlet : outletResponseArrayList) {
            final String text = Helper.validateResponseEmpty(outlet.getOutletName()).toLowerCase() +
                    " " + Helper.validateResponseEmpty(outlet.getIdOutlet()).toLowerCase();
            if (text.contains(newText)) {
                filteredList.add(outlet);
            }
        }
        setData(filteredList);
        setTotalOutlet(filteredList);
        return super.onQueryTextChange(newText);
    }
}
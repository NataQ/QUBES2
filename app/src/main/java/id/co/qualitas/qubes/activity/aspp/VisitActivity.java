package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.se.omapi.Session;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.apache.commons.io.FileUtils;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.AlarmManagerActivity;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAllReasonAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerCustomerAdapter;
import id.co.qualitas.qubes.adapter.aspp.NooListAdapter;
import id.co.qualitas.qubes.adapter.aspp.ReasonNotVisitAdapter;
import id.co.qualitas.qubes.adapter.aspp.VisitListAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.aspp.AccountFragment;
import id.co.qualitas.qubes.helper.AddressResultReceiver;
import id.co.qualitas.qubes.helper.FetchAddressIntentService;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.interfaces.CallbackOnResult;
import id.co.qualitas.qubes.interfaces.LocationRequestCallback;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.OfflineData;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.StartVisit;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.services.NotiWorker;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.LashPdfUtils;
import id.co.qualitas.qubes.utils.Utils;

public class VisitActivity extends BaseActivity {
    private VisitListAdapter mAdapterVisit;
    private NooListAdapter mAdapterNoo;
    private List<Customer> mList, mListNonRoute;
    private List<Customer> mListNoo;
    private MovableFloatingActionButton btnAddVisit, btnAddNoo;
    private Button btnEndDay, btnStartDay, btnStartVisit, btnEndVisit;
    private TextView txtVisit, txtVisitLine, txtNOO, txtNOOLine;
    private EditText edtSearchVisit, edtSearchNoo;
    private LinearLayout llVisit, llNoo;
    private RelativeLayout rlInap, rlDaily;
    private RecyclerView recyclerViewVisit, recyclerViewNoo;
    private boolean isLocationPermissionGranted = false, swipeRefresh = false;
    private LocationManager lm;
    private Customer outletClicked;
    private LashPdfUtils pdfUtils;
    private File pdfFile;
    private Boolean success = false;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private final static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private SwipeRefreshLayout swipeLayoutNoo, swipeLayoutVisit;
    private LinearLayout llNoDataVisit, llNoDataNoo;
    private ProgressBar progressCircleNoo, progressCircleVisit;
    private WSMessage resultWsMessage;
    private WSMessage logResult;
    private boolean saveDataSuccess = false;
    private boolean outRadius = false;
    public static final int CAMERA_PERM_CODE = 102;
    private ImageType imageType;
    private Uri uriBerangkat, uriPulang, uriSelesai;
    private Map startDay, endDay;
    private String kmAwal, kmAkhir;
    private VisitSalesman visitSalesman;
    private boolean fromNoo = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private LocationRequestCallback<String, Location> addressCallback;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    //    private Location currentLocation;
    private Map currentLocation;
    private boolean endVisit = false;
    private List<VisitSalesman> listVisitSalesman;
    private static final String TAG = VisitActivity.class.getSimpleName();
    private boolean setDataSyncSuccess = false;
    private ProgressDialog progressDialog;
    private List<Customer> nooList = new ArrayList<>();
    private List<OfflineData> offlineData = new ArrayList<>();
    private List<VisitSalesman> visitSalesmanList = new ArrayList<>();
    private List<Map> storeCheckList = new ArrayList<>(), returnList = new ArrayList<>();
    private List<CollectionHeader> collectionList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();
    private List<Map> photoList = new ArrayList<>();
    private int sizeData = 0;
    private StartVisit startVisit;
    private String resultOffline = null;
    private boolean salesInap = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_visit);

        //set Map
        Configuration.getInstance().load(VisitActivity.this, PreferenceManager.getDefaultSharedPreferences(VisitActivity.this));
        workManager = WorkManager.getInstance(getApplicationContext());
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(2000)
                .setMaxUpdateDelayMillis(2000)
                .build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
            }
        };
        mResultReceiver = new AddressResultReceiver(new Handler());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

        initialize();
//        initData();
        setViewVisit();
        setViewNoo();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            SessionManagerQubes.clearStartDaySession();
            logOut(VisitActivity.this);
        });
    }

    private void setViewVisit() {
        txtVisit.setOnClickListener(v -> {
            txtVisit.setTextColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtNOO.setTextColor(ContextCompat.getColor(this, R.color.gray8_aspp));
            txtVisitLine.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtNOOLine.setBackgroundColor(ContextCompat.getColor(this, R.color.space_transparent));

            llVisit.setVisibility(View.VISIBLE);
            llNoo.setVisibility(View.GONE);
        });

        btnStartVisit.setOnClickListener(v -> {
            if (swipeRefresh) {
                startDayVisit();//start visit
            } else {
                setToast("Silahkan refresh data customer");
            }
        });

        btnStartDay.setOnClickListener(v -> {
            startVisit.setStartDay(true);
            SessionManagerQubes.setStartDay(startVisit);
        });

        btnEndVisit.setOnClickListener(v -> {
            if (user.getRute_inap() == 1 && !startVisit.isEndDay()) {
                setToast("Silahkan end day terlebih dahulu sebelum end visit");
            } else {
                if (checkPermission()) {
                    endTodayVisit();//end visit
                } else {
                    setToast(getString(R.string.pleaseEnablePermission));
                    requestPermission();
                }
            }
        });

        btnEndDay.setOnClickListener(v -> {
            startVisit.setEndDay(true);
            SessionManagerQubes.setStartDay(startVisit);
            progress.show();
            PARAM = 6;
            new RequestUrl().execute();//rute inap
        });

        btnAddVisit.setOnClickListener(v -> {
            openDialogAdd();
        });

        swipeLayoutVisit.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh = true;
                if (database.getCountOfflineData() == 0) {
                    if (startVisit != null) {
                        if (startVisit.getStart_time() == null || startVisit.getEnd_time() != null) {
                            requestData();//swipe visit
                        } else {
                            if (user.getRute_inap() == 1) {
                                if (startVisit.isStartDay()) {
                                    setToast("Sudah start visit");
                                } else {
                                    requestData();//swipe visit
                                }
                            } else {
                                setToast("Sudah start visit");
                            }
                        }
                    } else {
                        requestData();//swipe visit
                    }
                } else {
                    setToast("Pastikan semua data offline sudah di sync");
                }
                swipeLayoutVisit.setRefreshing(false);
            }
        });

        edtSearchVisit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapterVisit != null) {
                    mAdapterVisit.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnAddVisit.setOnClickListener(v -> {
            if (database.getCountOfflineData() == 0) {
                if (startVisit != null) {
                    if (startVisit.getStart_time() != null && startVisit.getEnd_time() == null) {
                        openDialogAdd();
                    } else {
                        setToast("Please start visit first");
                    }
                } else {
                    setToast("Please start visit first");
                }
            } else {
                setToast("Pastikan semua data offline sudah di sync");
            }

//            if (SessionManagerQubes.getStartDay() == 1) {
//                openDialogAdd();
//            } else {
//                setToast("Please start visit first");
//            }
        });
    }

    private void startDayVisit() {
        boolean invoice = database.getCountInvoiceToday() != 0;
        boolean verifIncoice = database.getCountInvoiceToday() == database.getCountInvoiceVerifToday();
        boolean stockRequest = user.getType_sales().equals("CO") ? database.getCountStockRequestToday() != 0 : true;
        if (stockRequest) {
            if (invoice) {
                if (verifIncoice) {
                    openDialogStartVisit();
                } else {
                    setToast("Pastikan invoice sudah di verifikasi");
                }
            } else {
                openDialogConfirmInvoice();
            }
        } else {
            setToast("Pastikan sudah input stock request dan sudah di verifikasi");
        }
    }

    private void openDialogConfirmInvoice() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog(this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDialog = dialog.findViewById(R.id.txtDialog);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        txtTitle.setText("Invoice Tidak ada");
        txtDialog.setText("Anda tidak memiliki invoice untuk di tagihkan. Anda yakin ingin memulai visit?");

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openDialogStartVisit();
            }
        });

        dialog.show();
    }

    private void endTodayVisit() {
        if (getCountCheckInPauseOutlet()) {
            List<VisitSalesman> tempList = new ArrayList<>();
            SessionManagerQubes.setVisitSalesmanReason(tempList);
            endVisit = true;
            checkLocationPermission();//end visit
//            openDialogReasonNotVisit();//end visit
        } else {
            setToast("Selesaikan customer yang sedang check in atau pause");
        }
    }

    private void setAdapterVisit() {
        mAdapterVisit = new VisitListAdapter(this, mList, header -> {
            header.setNoo(false);
            outletClicked = header;
            fromNoo = false;
            if (startVisit != null) {
                if (startVisit.getStart_time() != null) {
                    if (header.getStatus() == Constants.CHECK_IN_VISIT || header.getStatus() == Constants.PAUSE_VISIT) {
                        SessionManagerQubes.setOutletHeader(outletClicked);
                        Intent intent = new Intent(VisitActivity.this, DailySalesmanActivity.class);
                        startActivity(intent);
                    } else {
                        endVisit = false;
                        if (checkOutletStatus()) {
                            if (header.isRoute()) {
                                moveCheckInDaily(header);
                            } else {
                                if (checkNonRouteCheckIn()) {
                                    moveCheckInDaily(header);
                                } else {
                                    setToast("Anda sudah mencapai max visit non route");
                                }
                            }
                        } else {
                            setToast("Selesaikan customer yang sedang check in");
                        }
                    }
                } else {
                    setToast("Please start visit first");
                }
            } else {
                setToast("Please start visit first");
            }
        });

        recyclerViewVisit.setAdapter(mAdapterVisit);
    }

    private boolean checkOutletStatus() {
        int statusCheckIn = 0;
        int statusCheckInVisit = database.getCountCheckInVisit();
        int statusCheckInNoo = database.getCountCheckInNoo();
        statusCheckIn = statusCheckInVisit + statusCheckInNoo;
//        for (Customer customer : mList) {
//            //0-> no status, 1 -> check in, 2 -> pause, 3-> check out
//            if (customer.getStatus() == 1) {
//                statusCheckIn++;
//            }
//        }
//
//        for (Customer customer : mListNoo) {
//            //0-> no status, 1 -> check in, 2 -> pause, 3-> check out
//            if (customer.getStatus() == 1) {
//                statusCheckIn++;
//            }
//        }
        return (statusCheckIn == 0);
    }

    private boolean getCountCheckInPauseOutlet() {
        int statusCheckIn = 0;
        int statusCheckInVisit = database.getCountCheckInPauseVisit();
        int statusCheckInNoo = database.getCountCheckInPauseNoo();
        statusCheckIn = statusCheckInVisit + statusCheckInNoo;
        return (statusCheckIn == 0);
    }

    private List<VisitSalesman> getAllCustomerNotVisit(Map currentLocation) {
        List<VisitSalesman> customerList = new ArrayList<>();
        customerList.addAll(database.getAllCheckInPauseVisit(currentLocation));
        customerList.addAll(database.getAllCheckInPauseNoo(currentLocation));
        return customerList;
    }

    private boolean checkNonRouteCheckIn() {
        int nonRouteCust = database.getCountCheckInVisitNonRoute();
        int nonRouteMax = user.getMax_visit();
        if (nonRouteCust < nonRouteMax) {
            return true;
        } else {
            return false;
        }
    }

    private void checkLocationPermission() {
        List<String> permissionRequest = new ArrayList<>();
        isLocationPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!isLocationPermissionGranted)
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (isLocationPermissionGranted) {
            if (!Helper.isGPSOn(VisitActivity.this)) {
                setToast("Please turn on GPS");
                Helper.turnOnGPS(VisitActivity.this);
            } else {
                getLocationGPS();//from checkLocationPermission
            }
        }
    }

    private void openDialogMapCheckIn() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_map_check_in);
        TextView txtAddress = dialog.findViewById(R.id.txtAddress);
        Button btnCheckIn = dialog.findViewById(R.id.btnCheckIn);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        MapView mapView = dialog.findViewById(R.id.mapView);
        ImageView btCenterMap = dialog.findViewById(R.id.btCenterMap);

        if (currentLocation != null) {
            txtAddress.setText(currentLocation.get("address") != null
                    ? currentLocation.get("address").toString()
                    : "Lokasi tidak di temukan");
        } else {
            txtAddress.setText("Lokasi tidak di temukan");
        }

        btnCheckIn.setOnClickListener(v -> {
            if (currentLocation != null) {
                dialog.dismiss();
                Location locCustomer = new Location(LocationManager.GPS_PROVIDER);
                locCustomer.setLatitude(outletClicked.getLatitude());
                locCustomer.setLongitude(outletClicked.getLongitude());

                Location currLoc = new Location(LocationManager.GPS_PROVIDER);
                currLoc.setLatitude(currentLocation.get("latitude") != null ? (Double) currentLocation.get("latitude") : 0);
                currLoc.setLongitude(currentLocation.get("longitude") != null ? (Double) currentLocation.get("longitude") : 0);
                outRadius = Helper.checkRadius(currLoc, locCustomer);

                visitSalesman = new VisitSalesman();
                visitSalesman.setIdHeader(Constants.ID_VS_MOBILE.concat(user.getUsername()).concat(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
                visitSalesman.setStatus(Constants.CHECK_IN_VISIT);
                visitSalesman.setCheckInTime(Helper.getTodayDate(Constants.DATE_FORMAT_2));
                visitSalesman.setLatCheckIn(currentLocation.get("latitude") != null ? (Double) currentLocation.get("latitude") : 0);
                visitSalesman.setLongCheckIn(currentLocation.get("longitude") != null ? (Double) currentLocation.get("longitude") : 0);
                visitSalesman.setInside(outRadius);
                visitSalesman.setIdSalesman(user.getUsername());
                visitSalesman.setCustomerId(outletClicked.getId());
                visitSalesman.setDate(Helper.getTodayDate(Constants.DATE_FORMAT_3));
                visitSalesman.setSync(0);

                if (outRadius) {
                    openDialogOutRadius();
                } else {
                    moveVisitSalesman();
                }
            } else {
                setToast("Can't get your location.. Please try again..");
            }
        });

        btnNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        IMapController mapController = mapView.getController();
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mapView.setMultiTouchControls(true);
        mapController.setZoom(Constants.ZOOM_LEVEL);

        //icon compass
        CompassOverlay mCompassOverlay = new CompassOverlay(VisitActivity.this, new InternalCompassOrientationProvider(VisitActivity.this), mapView);
        mCompassOverlay.enableCompass();
        mapView.getOverlays().add(mCompassOverlay);

        final List<Customer>[] custList = new List[]{new ArrayList<>()};
        custList[0].add(new Customer(outletClicked.getId(), outletClicked.getNama(), outletClicked.getAddress(), true, outletClicked.getLatitude(), outletClicked.getLongitude()));
        if (currentLocation != null) {
            GeoPoint myPosition = new GeoPoint((Double) currentLocation.get("latitude"), (Double) currentLocation.get("longitude"));
            custList[0].add(new Customer("", "You", "", false, (Double) currentLocation.get("latitude"), (Double) currentLocation.get("longitude")));
            mapView.getController().animateTo(myPosition);
        }

        final ArrayList<OverlayItem>[] items = new ArrayList[]{Helper.setOverLayItems(custList[0], VisitActivity.this)};

        //the overlay
        final ItemizedOverlayWithFocus<OverlayItem>[][] mOverlay = new ItemizedOverlayWithFocus[][]{new ItemizedOverlayWithFocus[]{new ItemizedOverlayWithFocus<OverlayItem>(items[0], new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                //do something
                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                return false;
            }
        }, VisitActivity.this)}};
        mOverlay[0][0].setFocusItemsOnTap(true);
        mOverlay[0][0].setOnFocusChangeListener(new ItemizedOverlay.OnFocusChangeListener() {
            @Override
            public void onFocusChanged(ItemizedOverlay<?> overlay, OverlayItem newFocus) {
                mOverlay[0][0].unSetFocusedItem();
                mOverlay[0][0].setFocusedItem(newFocus);
            }
        });

        mapView.getOverlays().add(mOverlay[0][0]);
        mapController.setCenter(Helper.computeCentroid(custList[0]));

        btCenterMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLocation != null) {
                    GeoPoint myPosition = new GeoPoint((Double) currentLocation.get("latitude"), (Double) currentLocation.get("longitude"));
                    mapView.getController().animateTo(myPosition);

                    custList[0].clear();
                    custList[0].add(new Customer(outletClicked.getId(), outletClicked.getNama(), outletClicked.getAddress(), true, outletClicked.getLatitude(), outletClicked.getLongitude()));
                    custList[0].add(new Customer("", "You", "", false, (Double) currentLocation.get("latitude"), (Double) currentLocation.get("longitude")));

                    items[0] = Helper.setOverLayItems(custList[0], VisitActivity.this);
                    mOverlay[0] = new ItemizedOverlayWithFocus[]{new ItemizedOverlayWithFocus<OverlayItem>(items[0], new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                        @Override
                        public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                            //do something
                            return true;
                        }

                        @Override
                        public boolean onItemLongPress(final int index, final OverlayItem item) {
                            return false;
                        }
                    }, VisitActivity.this)};
                    mapView.getOverlays().add(mOverlay[0][0]);
                    mapView.invalidate();
                } else {
                    setToast("Can't get your location.. Please try again..");
                }
            }
        });

        dialog.show();
    }

    private void openDialogOutRadius() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_out_radius);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            moveVisitSalesman();
        });

        btnNo.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void moveVisitSalesman() {
        outletClicked.setStatus(Constants.CHECK_IN_VISIT);
        database.addVisitSalesman(visitSalesman, user.getUsername());
        if (fromNoo) {
            database.updateStatusOutletNoo(outletClicked, user.getUsername());
        } else {
            database.updateStatusOutletVisit(outletClicked, user.getUsername());
        }
        SessionManagerQubes.setOutletHeader(outletClicked);

        Intent intent = new Intent(VisitActivity.this, DailySalesmanActivity.class);
        startActivity(intent);
    }

    private void setViewNoo() {
        txtNOO.setOnClickListener(v -> {
            txtVisit.setTextColor(ContextCompat.getColor(this, R.color.gray8_aspp));
            txtNOO.setTextColor(ContextCompat.getColor(this, R.color.blue_aspp));
            txtVisitLine.setBackgroundColor(ContextCompat.getColor(this, R.color.space_transparent));
            txtNOOLine.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_aspp));

            llVisit.setVisibility(View.GONE);
            llNoo.setVisibility(View.VISIBLE);
        });

        btnAddNoo.setOnClickListener(v -> {
//            if (database.getCountOfflineData() == 0) {
            if (startVisit != null) {
                if (startVisit.getStart_time() != null && startVisit.getEnd_time() == null) {
                    SessionManagerQubes.clearCustomerNooSession();
                    Helper.removeItemParam(Constants.IMAGE_TYPE);
                    Intent intent = new Intent(VisitActivity.this, CreateNooActivity.class);
                    startActivity(intent);
                } else {
                    setToast("Please start visit first");
                }
            } else {
                setToast("Please start visit first");
            }
//            } else {
//                setToast("Pastikan semua data offline sudah di sync");
//            }
//            if (SessionManagerQubes.getStartDay() == 1) {
//
//            } else {
//                setToast("Please start visit first");
//            }
        });

        swipeLayoutNoo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh = true;
                if (database.getCountOfflineData() == 0) {
                    if (startVisit != null) {
                        if (startVisit.getStart_time() == null || startVisit.getEnd_time() != null) {
                            requestData();//swipe noo
                        } else {
                            if (user.getRute_inap() == 1) {
                                if (startVisit.isStartDay()) {
                                    setToast("Sudah start visit");
                                } else {
                                    requestData();//swipe noo rute inap
                                }
                            } else {
                                setToast("Sudah start visit");
                            }
                        }
                    } else {
                        requestData();//swipe noo
                    }
                } else {
                    setToast("Pastikan semua data offline sudah di sync");
                }
                swipeLayoutNoo.setRefreshing(false);
            }
        });

        edtSearchNoo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapterNoo != null) {
                    mAdapterNoo.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setAdapterNoo() {
        mAdapterNoo = new NooListAdapter(this, mListNoo, header -> {
            header.setNoo(true);
            outletClicked = header;
            fromNoo = true;
            if (startVisit != null) {
                if (startVisit.getStart_time() != null) {
                    if (header.getStatus() == Constants.CHECK_IN_VISIT || header.getStatus() == Constants.PAUSE_VISIT) {
                        SessionManagerQubes.setOutletHeader(outletClicked);
                        Intent intent = new Intent(VisitActivity.this, DailySalesmanActivity.class);
                        startActivity(intent);
                    } else {
                        endVisit = false;
                        if (checkOutletStatus()) {
                            if (header.isRoute()) {
                                moveCheckInDaily(header);
                            } else {
                                if (checkNonRouteCheckIn()) {
                                    moveCheckInDaily(header);
                                } else {
                                    setToast("Anda sudah mencapai max visit non route");
                                }
                            }
                        } else {
                            setToast("Selesaikan customer yang sedang check in");
                        }
                    }
                } else {
                    setToast("Please start visit first");
                }
            } else {
                setToast("Please start visit first");
            }
        });

        recyclerViewNoo.setAdapter(mAdapterNoo);
    }

    private void moveCheckInDaily(Customer header) {
        if (header.getStatus() == 0) {//belum check in
            checkLocationPermission();//from adapter
        } else {
            SessionManagerQubes.setOutletHeader(outletClicked);
            Intent intent = new Intent(VisitActivity.this, DailySalesmanActivity.class);
            startActivity(intent);
        }
    }

    private void openDialogReasonNotVisit() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_reason_not_visit);

        Button btnApply = dialog.findViewById(R.id.btnApply);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Spinner spnReasonAll = dialog.findViewById(R.id.spnReasonAll);
        EditText edtTxtOther = dialog.findViewById(R.id.edtTxtOther);
        ImageView imgDelete = dialog.findViewById(R.id.imgDelete);
        ImageView img = dialog.findViewById(R.id.img);
        RelativeLayout llPhoto = dialog.findViewById(R.id.llPhoto);
        LinearLayout layoutCamera = dialog.findViewById(R.id.layoutCamera);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        List<Reason> reasonList = new ArrayList<>();
        reasonList.addAll(database.getAllReason(Constants.REASON_TYPE_NOT_VISIT));
        final String[] descReason = new String[1];
        final Reason[] reasonChoose = {null};
        final int[] posReason = {0};

        List<VisitSalesman> listCust = new ArrayList<>();

        ReasonNotVisitAdapter mAdapter = new ReasonNotVisitAdapter(this, listCust, header -> {
        });
        recyclerView.setAdapter(mAdapter);

        FilteredSpinnerAllReasonAdapter spinnerAdapter = new FilteredSpinnerAllReasonAdapter(this, reasonList);
        spnReasonAll.setAdapter(spinnerAdapter);

        if (Helper.isNotEmptyOrNull(SessionManagerQubes.getVisitSalesmanReason())) {
            listCust.addAll(SessionManagerQubes.getVisitSalesmanReason());
            if (imageType != null) {
                if (!Helper.isNullOrEmpty(imageType.getPosReason())) {
                    int posR = Integer.parseInt(imageType.getPosReason());
                    try {
                        listCust.get(posR).setPhotoNotVisitReason(imageType.getPhotoReason());
                    } catch (Exception e) {

                    }
                    mAdapter.notifyItemChanged(posR);
                } else {
                    llPhoto.setVisibility(View.VISIBLE);
                    Utils.loadImageFit(getApplicationContext(), imageType.getPhotoReason(), img);
                    for (VisitSalesman cust : listCust) {
                        cust.setPhotoNotVisitReason(imageType.getPhotoReason());
                    }
//                    mAdapter.setData(listCust);
                }
            } else {
                mAdapter.setData(listCust);
            }
        } else {
            listCust.addAll(getAllCustomerNotVisit(currentLocation));
            mAdapter.setData(listCust);
        }

        imgDelete.setOnClickListener(v -> {
            llPhoto.setVisibility(View.GONE);
            for (VisitSalesman cust : listCust) {
                cust.setPhotoNotVisitReason(null);
            }
        });

        spnReasonAll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reasonChoose[0] = reasonList.get(i);
                posReason[0] = i;

                if (reasonChoose[0].getIs_photo() == 1) {
                    layoutCamera.setVisibility(View.VISIBLE);
                } else {
                    layoutCamera.setVisibility(View.GONE);
                }

                if (reasonChoose[0].getIs_freetext() == 1) {
                    edtTxtOther.setVisibility(View.VISIBLE);
                } else {
                    edtTxtOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtTxtOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    descReason[0] = s.toString();

                }
            }
        });

        btnApply.setOnClickListener(v -> {
            for (VisitSalesman vs : listCust) {
                vs.setDescNotVisitReason(descReason[0]);
                vs.setIdNotVisitReason(String.valueOf(reasonChoose[0].getId()));
                vs.setNameNotVisitReason(reasonChoose[0].getDescription());
                vs.setPosReason(posReason[0]);
            }
            mAdapter.notifyDataSetChanged();
        });

        layoutCamera.setOnClickListener(v -> {
            SessionManagerQubes.setVisitSalesmanReason(listCust);
            openCamera(null);
        });

        btnSave.setOnClickListener(v -> {
            for (VisitSalesman vs : listCust) {
                vs.setDescNotVisitReason(descReason[0]);
                vs.setIdNotVisitReason(String.valueOf(reasonChoose[0].getId()));
                vs.setNameNotVisitReason(reasonChoose[0].getDescription());
                vs.setPosReason(posReason[0]);
            }
            int param = 0;
            if (Helper.isNotEmptyOrNull(listCust)) {
                for (VisitSalesman vs : listCust) {
                    if (!Helper.isNullOrEmpty(vs.getIdNotVisitReason())) {
                        Reason reason = database.getDetailReasonById(Constants.REASON_TYPE_NOT_VISIT, vs.getIdNotVisitReason());
                        if (reason.getIs_freetext() == 1) {
                            if (Helper.isNullOrEmpty(vs.getDescNotVisitReason())) {
                                param++;
                            }
                        }

                        if (reason.getIs_photo() == 1) {
                            if (Helper.isNullOrEmpty(vs.getPhotoNotVisitReason())) {
                                param++;
                            }
                        }
                    } else {
                        param++;
                    }
                }
            }

            if (param == 0) {
                dialog.dismiss();
                listVisitSalesman = new ArrayList<>();
                listVisitSalesman.addAll(listCust);
                PARAM = 5;
                progress.show();
                new RequestUrl().execute();//5
            } else {
                setToast("Pastikan semua field susah di isi");
            }
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void validateVisitSalesman() {
        int nonRouteMax = user.getMax_visit();

        for (VisitSalesman vs : listVisitSalesman) {
            if (vs.isNoo()) {
                saveVisitSalesman(vs);//dari noo
            } else {
                if (vs.isRoute()) {
                    //rute hari ini
                    saveVisitSalesman(vs);//rute hari ini
                } else {
                    //non rute
                    int nonRouteCust = database.getCountCheckInVisitNonRoute();
                    if (nonRouteCust < nonRouteMax) {
                        saveVisitSalesman(vs);//non rute
                    }
                }
            }
        }

    }

    private void saveVisitSalesman(VisitSalesman vs) {
        if (currentLocation == null) {
            currentLocation = new HashMap();
            currentLocation.put("latitude", null);
            currentLocation.put("longitude", null);
        }

        Location locCustomer = new Location(LocationManager.GPS_PROVIDER);
        locCustomer.setLatitude(vs.getLatCheckIn());
        locCustomer.setLongitude(vs.getLongCheckIn());
        Location currLoc = new Location(LocationManager.GPS_PROVIDER);
        currLoc.setLatitude(vs.getLatCheckOut());
        currLoc.setLongitude(vs.getLongCheckOut());
        outRadius = Helper.checkRadius(currLoc, locCustomer);

        vs.setIdSalesman(user.getUsername());
        vs.setDate(Helper.getTodayDate(Constants.DATE_FORMAT_3));
        vs.setStatus(Constants.CHECK_OUT_VISIT);
        vs.setInside(outRadius);
        vs.setIdVisit(String.valueOf(startVisit.getId()));
        vs.setInsideCheckOut(outRadius);
        vs.setIdHeader(Constants.ID_VS_MOBILE.concat(user.getUsername()).concat(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
        database.addVisitSalesmanAll(vs);

        if (vs.getPhotoNotVisitReason() != null) {
            Map req = new HashMap();
            req.put("photo", vs.getPhotoNotVisitReason());
            req.put("typePhoto", "not_visit");
            req.put("idDB", vs.getIdHeader());
            req.put("customerID", vs.getCustomerId());
            req.put("username", user.getUsername());
            database.addPhoto(req);
        }

        Customer cus = new Customer();
        cus.setIdHeader(vs.getIdHeader());
        cus.setId(vs.getCustomerId());
        cus.setStatus(Constants.CHECK_OUT_VISIT);
        if (vs.isNoo()) {
            database.updateStatusOutletNoo(cus, user.getUsername());
        } else {
            database.updateStatusOutletVisit(cus, user.getUsername());
        }
    }

    private void openDialogEndVisit() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_end_visit);
        Button btnEnd = dialog.findViewById(R.id.btnEnd);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        EditText txtKmAkhir = dialog.findViewById(R.id.txtKmAkhir);
        LinearLayout llImgSelesai = dialog.findViewById(R.id.llImgSelesai);
        LinearLayout llImgPulang = dialog.findViewById(R.id.llImgPulang);
        ImageView imgSelesai = dialog.findViewById(R.id.imgSelesai);
        ImageView imgAddSelesai = dialog.findViewById(R.id.imgAddSelesai);
        ImageView imgPulang = dialog.findViewById(R.id.imgPulang);
        ImageView imgAddPulang = dialog.findViewById(R.id.imgAddPulang);

        if (imageType != null) {
            if (imageType.getPhotoSelesai() != null) {
                Utils.loadImageFit(VisitActivity.this, imageType.getPhotoSelesai(), imgSelesai);
                imgAddSelesai.setVisibility(View.GONE);
            } else {
                imgAddSelesai.setVisibility(View.VISIBLE);
            }
        } else {
            imgAddSelesai.setVisibility(View.VISIBLE);
        }

        if (imageType != null) {
            if (imageType.getPhotoAkhir() != null) {
                Utils.loadImageFit(VisitActivity.this, imageType.getPhotoAkhir(), imgPulang);
                imgAddPulang.setVisibility(View.GONE);
            } else {
                imgAddPulang.setVisibility(View.VISIBLE);
            }
        } else {
            imgAddSelesai.setVisibility(View.VISIBLE);
        }

        txtKmAkhir.setText(imageType.getKmAkhir());

        llImgPulang.setOnClickListener(v -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setKmAkhir(txtKmAkhir.getText().toString().trim());
            imageType.setPhotoAkhir(uriPulang != null ? uriPulang.getPath() : imageType.getPhotoAkhir());
            imageType.setPhotoSelesai(uriSelesai != null ? uriSelesai.getPath() : imageType.getPhotoSelesai());
            imageType.setPosImage(5);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            askPermissionCamera();
        });

        llImgSelesai.setOnClickListener(v -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPosImage(6);
            imageType.setKmAkhir(txtKmAkhir.getText().toString().trim());
            imageType.setPhotoAkhir(uriPulang != null ? uriPulang.getPath() : imageType.getPhotoAkhir());
            imageType.setPhotoSelesai(uriSelesai != null ? uriSelesai.getPath() : imageType.getPhotoSelesai());
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            askPermissionCamera();
        });

        btnEnd.setOnClickListener(v -> {
            if (imageType == null) {
                imageType = new ImageType();
            }

            if (Helper.isEmptyEditText(txtKmAkhir)) {
                txtKmAkhir.setError(getString(R.string.emptyField));
            } else if (imageType.getPhotoAkhir() == null) {
                setToast("Harus Foto KM Akhir");
            } else if (imageType.getPhotoSelesai() == null) {
                setToast("Harus Foto Selesai");
            } else {
                kmAkhir = txtKmAkhir.getText().toString().trim();
                PARAM = 4;
                new RequestUrl().execute();//4
                progress.show();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openDialogStartVisit() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog(this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_start_visit, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(500, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5

        Button btnStart = dialog.findViewById(R.id.btnStart);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        EditText txtKmAwal = dialog.findViewById(R.id.txtKmAwal);
        ImageView imgAdd = dialog.findViewById(R.id.imgAdd);
        ImageView imgBerangkat = dialog.findViewById(R.id.imgBerangkat);
        LinearLayout llImgBerangkat = dialog.findViewById(R.id.llImgBerangkat);

        if (uriBerangkat != null) {
            Utils.loadImageFit(VisitActivity.this, uriBerangkat.getPath(), imgBerangkat);
//            imgBerangkat.setImageURI(uriBerangkat);
            imgAdd.setVisibility(View.GONE);
        } else {
            imgAdd.setVisibility(View.VISIBLE);
        }

        txtKmAwal.setText(imageType.getKmAwal());

        llImgBerangkat.setOnClickListener(v -> {
            ImageType imageType = new ImageType();
            imageType.setKmAwal(txtKmAwal.getText().toString().trim());
            imageType.setPosImage(4);
            imageType.setPhotoKmAwal(uriBerangkat != null ? uriBerangkat.getPath() : null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
//            SessionManagerQubes.setImageType(imageType);
            askPermissionCamera();
//            Intent camera = new Intent(this, Camera3PLActivity.class);//3
//            startActivityForResult(camera, Constants.REQUEST_CAMERA_CODE);
        });

        btnStart.setOnClickListener(v -> {
            if (Helper.isEmptyEditText(txtKmAwal)) {
                txtKmAwal.setError(getString(R.string.emptyField));
            } else if (uriBerangkat == null) {
                setToast("Foto KM Awal");
            } else {
                kmAwal = txtKmAwal.getText().toString().trim();
                PARAM = 3;
                new RequestUrl().execute();//3
                progress.show();
//                startDayDummy();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    public void askPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, CAMERA_PERM_CODE);
        } else {
            Helper.takePhoto(VisitActivity.this);
        }
    }

    private void openDialogAdd() {
        Dialog alertDialog = new Dialog(this);

        alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        EditText editText = alertDialog.findViewById(R.id.edit_text);
        RecyclerView listView = alertDialog.findViewById(R.id.list_view);

        List<Customer> groupList = new ArrayList<>();
        groupList.addAll(database.getAllNonRouteCustomer());

        FilteredSpinnerCustomerAdapter spinnerAdapter = new FilteredSpinnerCustomerAdapter(this, groupList, (header, adapterPosition) -> {
            int idHeader = database.addCustomer(header, user.getUsername());
            List<Promotion> promoList = database.getPromotionNonRouteByIdCustomer(header.getId());
            for (Promotion promo : promoList) {
                database.addCustomerPromotion(promo, String.valueOf(idHeader), user.getUsername());
            }

            List<Material> dctList = database.getDctNonRouteByIdCustomer(header.getId());
            for (Material mat : dctList) {
                database.addCustomerDct(mat, String.valueOf(idHeader), user.getUsername());
            }

//            database.deleteMasterNonRouteCustomerById(header.getIdHeader());
//            database.deleteMasterNonRouteCustomerPromotionById(header.getIdHeader());
//            database.deleteMasterNonRouteCustomerDctById(header.getIdHeader());
            getData();
            setAdapterVisit();
            alertDialog.dismiss();
        });

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mManager);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        listView.setAdapter(spinnerAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spinnerAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        pdfUtils = LashPdfUtils.getInstance(VisitActivity.this);

        llNoDataVisit = findViewById(R.id.llNoDataVisit);
        llNoDataNoo = findViewById(R.id.llNoDataNoo);
        progressCircleNoo = findViewById(R.id.progressCircleNoo);
        progressCircleVisit = findViewById(R.id.progressCircleVisit);
        txtNOOLine = findViewById(R.id.txtNOOLine);
        txtNOO = findViewById(R.id.txtNOO);
        txtVisit = findViewById(R.id.txtVisit);
        txtVisitLine = findViewById(R.id.txtVisitLine);
        rlDaily = findViewById(R.id.rlDaily);
        rlInap = findViewById(R.id.rlInap);
        btnEndVisit = findViewById(R.id.btnEndVisit);
        btnStartVisit = findViewById(R.id.btnStartVisit);
        btnEndDay = findViewById(R.id.btnEndDay);
        btnStartDay = findViewById(R.id.btnStartDay);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        edtSearchNoo = findViewById(R.id.edtSearchNoo);
        edtSearchVisit = findViewById(R.id.edtSearchVisit);
        llVisit = findViewById(R.id.llVisit);
        llNoo = findViewById(R.id.llNoo);
        btnAddVisit = findViewById(R.id.btnAddVisit);
        btnAddNoo = findViewById(R.id.btnAddNoo);

        recyclerViewVisit = findViewById(R.id.recyclerViewVisit);
        recyclerViewVisit.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVisit.setHasFixedSize(true);

        recyclerViewNoo = findViewById(R.id.recyclerViewNoo);
        recyclerViewNoo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNoo.setHasFixedSize(true);

        swipeLayoutNoo = findViewById(R.id.swipeLayoutNoo);
        swipeLayoutNoo.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);

        swipeLayoutVisit = findViewById(R.id.swipeLayoutVisit);
        swipeLayoutVisit.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFirstDataOffline();
        if (Helper.getItemParam(Constants.IMAGE_TYPE) != null) {
            imageType = new ImageType();
            imageType = (ImageType) Helper.getItemParam(Constants.IMAGE_TYPE);
        } else {
            imageType = new ImageType();
        }

        if (getIntent().getExtras() != null) {
            Uri uri = (Uri) getIntent().getExtras().get(Constants.OUTPUT_CAMERA);
            getIntent().removeExtra(Constants.OUTPUT_CAMERA);

            switch (imageType.getPosImage()) {
                case 4:
                    uriBerangkat = uri;
                    openDialogStartVisit();//on resume
                    break;
                case 5:
                    uriPulang = uri;
                    imageType.setPhotoAkhirString(Utils.encodeImageBase64(VisitActivity.this, uriPulang));
                    imageType.setPhotoAkhir(uriPulang.getPath());
                    openDialogEndVisit();//on resume
                    break;
                case 6:
                    uriSelesai = uri;
                    imageType.setPhotoSelesaiString(Utils.encodeImageBase64(VisitActivity.this, uriSelesai));
                    imageType.setPhotoSelesai(uriSelesai.getPath());
                    openDialogEndVisit();//on resume
                    break;
                case 13:
                    imageType.setPhotoReason(uri.getPath());
                    openDialogReasonNotVisit();//on resume
                    break;
            }
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            boolean check =
                    Environment.isExternalStorageManager();
//                            && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//                            && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            return check;
        } else {
            if (
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        }
    }

    private void requestPermission() {
        //  if(!Environment.isExternalStorageManager()){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
                } else {
                    setToast("Allow permission for storage access!");
                }
            }
        }
    }

    public void openCamera(String pos) {
        ImageType imageType = new ImageType();
        imageType.setPosImage(13);
        imageType.setPosReason(pos);
        Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
        askPermissionCamera();
    }

    private class AsyncTaskGeneratePDF extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String nameLash = user.getUsername() + Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_TYPE_7, SessionManagerQubes.getStartDay().getDate());
                pdfFile = new File(Utils.getDirLocPDF(getApplicationContext()) + "/" + nameLash + ".pdf");
                List<Map> lashList = new ArrayList<>();
                lashList = database.getDatalash();
                success = pdfUtils.createPDF(pdfFile, lashList, nameLash);
                return success;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (result == null) {
                setToast("Gagal membuat pdf.. Silahkan coba lagi..");
            } else {
                if (result) {
                    setToast("Downloaded to " + pdfFile.getAbsolutePath());
                } else {
                    setToast("Gagal membuat pdf.. Silahkan coba lagi");
                }

                if (user.getRute_inap() != 1) {
                    //bukan rute inap
                    if (user.getType_sales().equals("TO")) {
                        new RequestUrlSync().execute();
                    } else {
                        SessionManagerQubes.setStockRequestHeader(database.getLastStockRequest());
                        Helper.setItemParam(Constants.FROM_STOCK_REQUEST, 0);
                        Intent intent = new Intent(VisitActivity.this, UnloadingActivity.class);
                        startActivity(intent);
                    }
                } else {
                    btnStartDay.setVisibility(View.VISIBLE);
                    btnEndDay.setVisibility(View.GONE);
                }
            }
        }
    }

    private void getFirstDataOffline() {
        rlInap.setVisibility(View.GONE);
        rlDaily.setVisibility(View.GONE);
        startVisit = SessionManagerQubes.getStartDay();
        getData();
        setAdapterVisit();
        setAdapterNoo();

        if (mList == null || mList.isEmpty()) {
            requestData();//getFirstDataOffline
        }

        validateButton();
    }

    private void requestData() {
        progressCircleVisit.setVisibility(View.VISIBLE);
        progressCircleNoo.setVisibility(View.VISIBLE);
        recyclerViewVisit.setVisibility(View.GONE);
        recyclerViewNoo.setVisibility(View.GONE);
        llNoDataVisit.setVisibility(View.GONE);
        llNoDataNoo.setVisibility(View.GONE);
        PARAM = 1;
        new RequestUrl().execute();//1
//        setDataDummy();
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllCustomerVisit();

        mListNoo = new ArrayList<>();
        mListNoo = database.getAllCustomerNoo();
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    List<Customer> custList = database.getTodayCustomer();
                    if (custList.size() != 0) {
                        database.deleteCustomer();
                        database.deleteCustomerDct();
                        database.deleteCustomerPromotion();
                        database.deleteVisitSalesman();
                        database.deletePhoto();
                        database.deleteNoo();
                        SessionManagerQubes.clearStartDaySession();//remove start visit
                        startVisit = new StartVisit();
                        for (Customer customer : custList) {
                            int idHeader = database.addCustomer(customer, user.getUsername());
                            List<Promotion> promoList = database.getPromotionNonRouteByIdCustomer(customer.getId());
                            for (Promotion promo : promoList) {
                                database.addCustomerPromotion(promo, String.valueOf(idHeader), user.getUsername());
                            }

                            List<Material> dctList = database.getDctNonRouteByIdCustomer(customer.getId());
                            for (Material mat : dctList) {
                                database.addCustomerDct(mat, String.valueOf(idHeader), user.getUsername());
                            }
                        }
                    }
                    getData();
                    saveDataSuccess = true;
                    return null;
                } else if (PARAM == 2) {
                    Map req = new HashMap();
                    req.put("username", user.getUsername());
                    String URL_ = Constants.API_GET_START_VISIT;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                    return null;
                } else if (PARAM == 3) {
                    startDay = new HashMap();
                    startDay.put("kmAwal", kmAwal);
                    startDay.put("username", user.getUsername());
                    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                    if (uriBerangkat != null) {
                        map.add("kmAwalPhoto", new FileSystemResource(uriBerangkat.getPath()));
                    } else {
                        map.add("kmAwalPhoto", "");
                    }
                    String json = new Gson().toJson(startDay);
                    map.add("data", json);

                    String URL_ = Constants.API_GET_START_DAY_MULTI_PART;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBodyMultiPart(url, WSMessage.class, map);
                    return null;
                } else if (PARAM == 4) {
                    endDay = new HashMap();
                    endDay.put("idVisit", startVisit.getId());
                    endDay.put("kmAkhir", kmAkhir);
                    endDay.put("username", user.getUsername());

                    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                    if (imageType.getPhotoAkhir() != null) {
                        File file = FileUtils.getFile(String.valueOf(this), imageType.getPhotoAkhir());
                        map.add("photoKmAkhir", new FileSystemResource(imageType.getPhotoAkhir()));
                    } else {
                        map.add("photoKmAkhir", "");
                    }
                    if (imageType.getPhotoSelesai() != null) {
                        map.add("photoCompleted", new FileSystemResource(imageType.getPhotoSelesai()));
                    } else {
                        map.add("photoCompleted", "");
                    }
                    String json = new Gson().toJson(endDay);
                    map.add("data", json);

                    String URL_ = Constants.API_GET_END_DAY_MULTI_PART;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBodyMultiPart(url, WSMessage.class, map);
                    return null;
                } else if (PARAM == 5) {
                    validateVisitSalesman();
                    getData();
                    saveDataSuccess = true;
                    return null;
                } else {
                    setDataOffline();
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("Customer", ex.getMessage());
                }
                if (PARAM == 2 || PARAM == 5) {
                    saveDataSuccess = false;
                } else {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                    switch (PARAM) {
                        case 1:
                            logResult.setMessage("Today Customer error: " + exMess);
                            break;
                        case 3:
                            logResult.setMessage("Start Visit error: " + exMess);
                            break;
                        case 4:
                            logResult.setMessage("End Visit error: " + exMess);
                            break;
                    }
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(WSMessage result) {
            if (PARAM == 1) {
                if (saveDataSuccess) {
                    boolean start = false;
                    if (startVisit != null) {
                        if (startVisit.getStart_time() != null && startVisit.getEnd_time() == null) {
                            start = true;
                        } else {
                            start = false;
                        }
                    } else {
                        start = false;
                    }

                    if (start) {
                        progressCircleVisit.setVisibility(View.GONE);
                        progressCircleNoo.setVisibility(View.GONE);
                        mAdapterVisit.setData(mList);
                        mAdapterNoo.setData(mListNoo);
                        workRequest = new PeriodicWorkRequest.Builder(NotiWorker.class, 15, TimeUnit.MINUTES).build();
                        workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, (PeriodicWorkRequest) workRequest);
                        validateButton();//after get data
                        if (Helper.isEmptyOrNull(mList)) {
                            recyclerViewVisit.setVisibility(View.GONE);
                            llNoDataVisit.setVisibility(View.VISIBLE);
                        } else {
                            recyclerViewVisit.setVisibility(View.VISIBLE);
                            llNoDataVisit.setVisibility(View.GONE);
                        }

                        if (Helper.isEmptyOrNull(mListNoo)) {
                            recyclerViewNoo.setVisibility(View.GONE);
                            llNoDataNoo.setVisibility(View.VISIBLE);
                        } else {
                            recyclerViewNoo.setVisibility(View.VISIBLE);
                            llNoDataNoo.setVisibility(View.GONE);
                        }
                    } else {
                        if (mList.size() != 0) {
                            PARAM = 2;
                            new RequestUrl().execute();//2
                        } else {
                            progressCircleVisit.setVisibility(View.GONE);
                            progressCircleNoo.setVisibility(View.GONE);
                            mAdapterVisit.setData(mList);
                            mAdapterNoo.setData(mListNoo);
                            validateButton();//after get data
                            if (Helper.isEmptyOrNull(mList)) {
                                recyclerViewVisit.setVisibility(View.GONE);
                                llNoDataVisit.setVisibility(View.VISIBLE);
                            } else {
                                recyclerViewVisit.setVisibility(View.VISIBLE);
                                llNoDataVisit.setVisibility(View.GONE);
                            }

                            if (Helper.isEmptyOrNull(mListNoo)) {
                                recyclerViewNoo.setVisibility(View.GONE);
                                llNoDataNoo.setVisibility(View.VISIBLE);
                            } else {
                                recyclerViewNoo.setVisibility(View.VISIBLE);
                                llNoDataNoo.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    setToast(getString(R.string.failedSaveData));
                }
            } else if (PARAM == 2) {
                progressCircleVisit.setVisibility(View.GONE);
                progressCircleNoo.setVisibility(View.GONE);
                mAdapterVisit.setData(mList);
                mAdapterNoo.setData(mListNoo);
                if (logResult != null) {
                    if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                        StartVisit mData = Helper.ObjectToGSON(logResult.getResult(), StartVisit.class);
                        SessionManagerQubes.setStartDay(mData);
                        startVisit = SessionManagerQubes.getStartDay();
                    }
                }

                if (startVisit != null) {
                    if (startVisit.getStart_time() != null && startVisit.getEnd_time() == null) {
                        workRequest = new PeriodicWorkRequest.Builder(NotiWorker.class, 15, TimeUnit.MINUTES).build();
                        workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, (PeriodicWorkRequest) workRequest);
                    }
                }

                validateButton();//after get data
                if (Helper.isEmptyOrNull(mList)) {
                    recyclerViewVisit.setVisibility(View.GONE);
                    llNoDataVisit.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewVisit.setVisibility(View.VISIBLE);
                    llNoDataVisit.setVisibility(View.GONE);
                }

                if (Helper.isEmptyOrNull(mListNoo)) {
                    recyclerViewNoo.setVisibility(View.GONE);
                    llNoDataNoo.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewNoo.setVisibility(View.VISIBLE);
                    llNoDataNoo.setVisibility(View.GONE);
                }
            } else if (PARAM == 3) {
                progress.dismiss();
                if (logResult.getIdMessage() == 1) {
                    String message = "Start Visit : " + logResult.getMessage();
                    logResult.setMessage(message);
                    database.addLog(logResult);
                    setToast(logResult.getMessage());
                    startVisit = Helper.ObjectToGSON(logResult.getResult(), StartVisit.class);
                    if (startVisit == null) {
                        startVisit = new StartVisit();
                    }
                    startVisit.setStart_time(Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    startVisit.setKm_awal(kmAwal);
                    startVisit.setPhoto_km_awal(uriBerangkat.getPath());
                    SessionManagerQubes.setStartDay(startVisit);

                    workRequest = new PeriodicWorkRequest.Builder(NotiWorker.class, 15, TimeUnit.MINUTES).build();
                    workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, (PeriodicWorkRequest) workRequest);
                    validateButton();//start
                } else {
                    database.addLog(logResult);
                    setToast(logResult.getMessage());
                    openDialogStartVisit();
                }
            } else if (PARAM == 4) {
                progress.dismiss();
                if (logResult.getIdMessage() == 1) {
                    String message = "End Visit : " + logResult.getMessage();
                    logResult.setMessage(message);
                    database.addLog(logResult);

                    startVisit.setEnd_time(Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    startVisit.setKm_akhir(kmAkhir);
                    startVisit.setPhoto_km_akhir(imageType.getPhotoAkhir());
                    startVisit.setPhoto_complete(imageType.getPhotoSelesai());
                    SessionManagerQubes.setStartDay(startVisit);

                    validateButton();//end
                    if (user.getRute_inap() == 1 && user.getType_sales().equals("CO")) {
                        //rute inap dan dia co
                        SessionManagerQubes.setStockRequestHeader(database.getLastStockRequest());
                        Helper.setItemParam(Constants.FROM_STOCK_REQUEST, 0);
                        Intent intent = new Intent(VisitActivity.this, UnloadingActivity.class);
                        startActivity(intent);
                    } else {
                        progress.show();
                        PARAM = 6;
                        new RequestUrl().execute();//6
                    }
                } else {
                    database.addLog(logResult);
                    setToast(logResult.getMessage());
                    openDialogEndVisit();//if failed end viist
                }
            } else if (PARAM == 5) {
                progress.dismiss();
                if (saveDataSuccess) {
                    setAdapterVisit();
                    setAdapterNoo();
//                    if (user.getType_sales().equals("CO")) {
//                        SessionManagerQubes.setStockRequestHeader(database.getLastStockRequest());
//                        Helper.setItemParam(Constants.FROM_STOCK_REQUEST, 0);
//                        Intent intent = new Intent(VisitActivity.this, UnloadingActivity.class);
//                        startActivity(intent);
//                    } else {
                    openDialogEndVisit();//after save daily salesman
//                    }
                } else {
                    setToast("Gagal menyimpan data");
                }
            } else if (PARAM == 6) {
                progress.show();
                new AsyncTaskGeneratePDF().execute();//bukan sales inap
                //dia rute inap dan lagi end day

            }
        }
    }

    private void setDataOffline() {
        offlineData = new ArrayList<>();
        resultOffline = null;
        try {
            //noo
            nooList = new ArrayList<>();
            nooList = database.getAllNoo();
            if (nooList == null) {
                logResult = new WSMessage();
                logResult.setIdMessage(0);
                logResult.setResult(null);
                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                logResult.setMessage("Set offline data noo failed: " + exMess);
            } else {
                nooList = new ArrayList<>();
            }
            OfflineData offData = new OfflineData();
            offData.setNooList(nooList);
            offlineData.add(0, offData);

            //visit
            visitSalesmanList = new ArrayList<>();
            visitSalesmanList = database.getAllVisitSalesman();
            if (visitSalesmanList == null) {
                logResult = new WSMessage();
                logResult.setIdMessage(0);
                logResult.setResult(null);
                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                logResult.setMessage("Set offline data visit failed: " + exMess);
            } else {
                visitSalesmanList = new ArrayList<>();
            }
            offData = new OfflineData();
            offData.setVisitSalesmanList(visitSalesmanList);
            offlineData.add(1, offData);

            List<Customer> customerList = database.getAllCustomerCheckOut();

            if (customerList != null) {
                if (!customerList.isEmpty()) {
                    Map headerStoreCheck = new HashMap();
                    storeCheckList = new ArrayList<>();
                    List<Material> storeCheck = new ArrayList<>();
                    collectionList = new ArrayList<>();
                    List<CollectionHeader> collection = new ArrayList<>();
                    orderList = new ArrayList<>();
                    List<Order> order = new ArrayList<>();
                    Map headerReturn = new HashMap();
                    returnList = new ArrayList<>();
                    List<Material> returnO = new ArrayList<>();
                    photoList = new ArrayList<>();
                    List<Map> photos = new ArrayList<>();

                    for (Customer customer : customerList) {
                        //store check
                        storeCheck = database.getAllStoreCheckCheckOut(customer.getId());
                        if (storeCheck != null) {
                            if (!storeCheck.isEmpty()) {
                                headerStoreCheck = new HashMap();
                                headerStoreCheck.put("id_mobile", storeCheck.get(0).getIdheader());
                                headerStoreCheck.put("date", storeCheck.get(0).getDate());
                                headerStoreCheck.put("id_salesman", user.getUsername());
                                headerStoreCheck.put("id_customer", storeCheck.get(0).getId_customer());
                                headerStoreCheck.put("listData", storeCheck);
                                storeCheckList.add(headerStoreCheck);
                                setDataSyncSuccess = true;
                            } else {
                                setDataSyncSuccess = true;
                            }
                        } else {
                            setDataSyncSuccess = false;
                        }

                        //collection
                        collection = database.getAllCollectionHeader(customer.getId());
                        if (collection != null) {
                            if (!collection.isEmpty()) {
                                collectionList.addAll(collection);
                                setDataSyncSuccess = true;
                            } else {
                                setDataSyncSuccess = true;
                            }
                        } else {
                            setDataSyncSuccess = false;
                        }

                        //order
                        order = database.getAllOrderHeader(customer.getId(), user.getUsername());
                        if (order != null) {
                            if (!order.isEmpty()) {
                                orderList.addAll(order);
                                setDataSyncSuccess = true;
                            } else {
                                setDataSyncSuccess = true;
                            }
                        } else {
                            setDataSyncSuccess = false;
                        }

                        //return
                        returnO = database.getAllReturnCheckOut(customer.getId());
                        if (returnO != null) {
                            if (!returnO.isEmpty()) {
                                headerReturn = new HashMap();
                                headerReturn.put("id_mobile", returnO.get(0).getIdheader());
                                headerReturn.put("date", returnO.get(0).getDate());
                                headerReturn.put("id_salesman", user.getUsername());
                                headerReturn.put("id_customer", returnO.get(0).getId_customer());
                                headerReturn.put("listData", returnO);
                                returnList.add(headerReturn);
                                setDataSyncSuccess = true;
                            } else {
                                setDataSyncSuccess = true;
                            }
                        } else {
                            setDataSyncSuccess = false;
                        }

                        //photo
                        photos = database.getAllPhoto(customer.getId());
                        if (photos != null) {
                            if (!photos.isEmpty()) {
                                photoList.addAll(photos);
                                setDataSyncSuccess = true;
                            } else {
                                setDataSyncSuccess = true;
                            }
                        } else {
                            setDataSyncSuccess = false;
                        }
                    }

                    if (Helper.isNotEmptyOrNull(storeCheckList)) {
                        storeCheckList = new ArrayList<>();
                    }
                    if (Helper.isNotEmptyOrNull(collectionList)) {
                        collectionList = new ArrayList<>();
                    }
                    if (Helper.isNotEmptyOrNull(orderList)) {
                        orderList = new ArrayList<>();
                    }
                    if (Helper.isNotEmptyOrNull(returnList)) {
                        returnList = new ArrayList<>();
                    }
                    if (Helper.isNotEmptyOrNull(photoList)) {
                        photoList = new ArrayList<>();
                    }
                    offData = new OfflineData();
                    offData.setStoreCheckList(storeCheckList);
                    offlineData.add(2, offData);

                    offData = new OfflineData();
                    offData.setCollectionList(collectionList);
                    offlineData.add(3, offData);

                    offData = new OfflineData();
                    offData.setOrderList(orderList);
                    offlineData.add(4, offData);

                    offData = new OfflineData();
                    offData.setReturnList(returnList);
                    offlineData.add(5, offData);

                    offData = new OfflineData();
                    offData.setPhotoList(photoList);
                    offlineData.add(6, offData);

                    if (!setDataSyncSuccess) {
                        logResult = new WSMessage();
                        logResult.setIdMessage(0);
                        logResult.setResult(null);
                        String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline customer failed: " + exMess);
                    }
                } else {
                    setDataSyncSuccess = true;
                    if (Helper.isNotEmptyOrNull(storeCheckList)) {
                        storeCheckList = new ArrayList<>();
                    }
                    if (Helper.isNotEmptyOrNull(collectionList)) {
                        collectionList = new ArrayList<>();
                    }
                    if (Helper.isNotEmptyOrNull(orderList)) {
                        orderList = new ArrayList<>();
                    }
                    if (Helper.isNotEmptyOrNull(returnList)) {
                        returnList = new ArrayList<>();
                    }
                    if (Helper.isNotEmptyOrNull(photoList)) {
                        photoList = new ArrayList<>();
                    }

                    offData = new OfflineData();
                    offData.setStoreCheckList(storeCheckList);
                    offlineData.add(2, offData);

                    offData = new OfflineData();
                    offData.setCollectionList(collectionList);
                    offlineData.add(3, offData);

                    offData = new OfflineData();
                    offData.setOrderList(orderList);
                    offlineData.add(4, offData);

                    offData = new OfflineData();
                    offData.setReturnList(returnList);
                    offlineData.add(5, offData);

                    offData = new OfflineData();
                    offData.setPhotoList(photoList);
                    offlineData.add(6, offData);
                }
            } else {
                logResult = new WSMessage();
                logResult.setIdMessage(0);
                logResult.setResult(null);
                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                logResult.setMessage("Set offline customer failed: " + exMess);

                if (Helper.isNotEmptyOrNull(storeCheckList)) {
                    storeCheckList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(collectionList)) {
                    collectionList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(orderList)) {
                    orderList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(returnList)) {
                    returnList = new ArrayList<>();
                }
                if (Helper.isNotEmptyOrNull(photoList)) {
                    photoList = new ArrayList<>();
                }

                offData = new OfflineData();
                offData.setStoreCheckList(storeCheckList);
                offlineData.add(2, offData);

                offData = new OfflineData();
                offData.setCollectionList(collectionList);
                offlineData.add(3, offData);

                offData = new OfflineData();
                offData.setOrderList(orderList);
                offlineData.add(4, offData);

                offData = new OfflineData();
                offData.setReturnList(returnList);
                offlineData.add(5, offData);

                offData = new OfflineData();
                offData.setPhotoList(photoList);
                offlineData.add(6, offData);
            }
        } catch (Exception e) {
            resultOffline = e.getMessage() != null ? e.getMessage() : "Failed";
        }
    }

    private class RequestUrlSync extends AsyncTask<Void, Integer, List<WSMessage>> {

        @Override
        protected List<WSMessage> doInBackground(Void... voids) {
            try {
                List<WSMessage> listWSMsg = new ArrayList<>();
                String URL_ = null, url = null;
                Map req = new HashMap();
                int counter = 0;
                for (int i = 0; i < offlineData.size(); i++) {
                    switch (i) {
                        case 0:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_CUSTOMER_NOO);
                            req = new HashMap();
                            req.put("listData", offlineData.get(i).getNooList());
                            if (progressDialog != null) {
                                progressDialog.setMessage("Mengirim data noo offline...");
                            }
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Noo success");
                                for (Customer data : offlineData.get(i).getNooList()) {
                                    database.updateSyncNoo(data);
                                }
                            }
                            database.addLog(logResult);
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 1:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_VISIT);
                            counter++;
                            req = new HashMap();
                            req.put("listData", offlineData.get(i).getVisitSalesmanList());
                            logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                            if (logResult.getIdMessage() == 1) {
                                logResult = new WSMessage();
                                logResult.setIdMessage(1);
                                logResult.setMessage("Sync Visit Salesman success");
                                for (VisitSalesman data : offlineData.get(i).getVisitSalesmanList()) {
                                    database.updateSyncVisitSalesman(data);
                                }
                            }
                            database.addLog(logResult);
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 2:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_STORE_CHECK);
                            counter++;
                            for (Map data : offlineData.get(i).getStoreCheckList()) {
                                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                                if (logResult.getIdMessage() == 1) {
                                    logResult = new WSMessage();
                                    logResult.setIdMessage(1);
                                    logResult.setMessage("Sync Store Check " + data.get("id_mobile").toString() + " success");
                                    database.updateSyncStoreCheck(data);
                                }
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 3:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_COLLECTION);
                            counter++;
                            for (CollectionHeader data : offlineData.get(i).getCollectionList()) {
                                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                                if (logResult.getIdMessage() == 1) {
                                    logResult = new WSMessage();
                                    logResult.setIdMessage(1);
                                    logResult.setMessage("Sync Collection " + data.getIdHeader() + " success");
                                    database.updateSyncCollectionHeader(data);
                                }
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 4:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_ORDER);
                            counter++;
                            for (Order data : offlineData.get(i).getOrderList()) {
                                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                                if (logResult.getIdMessage() == 1) {
                                    logResult = new WSMessage();
                                    logResult.setIdMessage(1);
                                    logResult.setMessage("Sync Order " + data.getIdHeader() + " success");
                                    database.updateSyncOrderHeader(data);
                                }
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 5:
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_RETURN);
                            counter++;
                            for (Map data : offlineData.get(i).getReturnList()) {
                                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                                if (logResult.getIdMessage() == 1) {
                                    logResult = new WSMessage();
                                    logResult.setIdMessage(1);
                                    logResult.setMessage("Sync Return " + data.get("id_customer").toString() + " success");
                                    database.updateSyncReturn(data);
                                }
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                        case 6:
                            counter++;
                            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_ONE_PHOTO);
                            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                            Map requestData = new HashMap();
                            for (Map data : offlineData.get(i).getPhotoList()) {
                                map = new LinkedMultiValueMap<String, Object>();
                                if (data.get("photo") != null) {
                                    map.add("photo", new FileSystemResource(data.get("photo").toString()));
                                } else {
                                    map.add("photo", "");
                                }
                                requestData = new HashMap();
                                requestData.put("typePhoto", data.get("typePhoto"));
                                requestData.put("id", user.getUsername());
                                requestData.put("customerId", data.get("customerId"));
                                requestData.put("idDB", data.get("idDB"));
                                String json = new Gson().toJson(requestData);
                                map.add("data", json);

                                logResult = (WSMessage) NetworkHelper.postWebserviceWithBodyMultiPart(url, WSMessage.class, map);
                                if (logResult.getIdMessage() == 1) {
                                    logResult = new WSMessage();
                                    logResult.setIdMessage(1);
                                    logResult.setMessage("Sync Photo " + data.get("customerId").toString() + " success");
                                    database.updateSyncPhoto(data);
                                }
                                database.addLog(logResult);
                            }
                            publishProgress(counter);
                            listWSMsg.add(logResult);
                            break;
                    }
                }
                return listWSMsg;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("Sync  offline", ex.getMessage());
                }
                logResult = new WSMessage();
                logResult.setIdMessage(0);
                String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                logResult.setMessage("Sync offline data failed : " + exMess);
                database.addLog(logResult);
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VisitActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(offlineData.size());
            progressDialog.setMessage(getString(R.string.progress_checkout));
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(List<WSMessage> listResult) {
            if (listResult != null) {
                int error = 0;
                for (WSMessage msg : listResult) {
                    if (msg.getIdMessage() == 0) {
                        error++;
                    }
                }
                if (listResult.size() == offlineData.size()) {//ganti sizeData
                    if (error == 0) {
                        setToast("Sukses mengirim data " + String.valueOf(listResult.size()));
                    } else {
                        setToast("Gagal mengirim data : " + String.valueOf(error));
                    }
                } else {
                    setToast("Gagal mengirim data : " + String.valueOf(error));
                }
            } else {
                setToast("Gagal mengirim data");
            }
            progressDialog.dismiss();

            if (user.getRute_inap() == 1) {

            }
        }
    }

    private void validateButton() {
        if (user.getRute_inap() == 1) {
            rlInap.setVisibility(View.VISIBLE);
            rlDaily.setVisibility(View.GONE);
        } else {
            rlInap.setVisibility(View.GONE);
            rlDaily.setVisibility(View.VISIBLE);
        }

        int status = 0;
        if (startVisit != null) {
            if (startVisit.getStart_time() != null && startVisit.getEnd_time() == null) {
                status = 1;
            } else if (startVisit.getStart_time() != null && startVisit.getEnd_time() != null) {
                status = 2;
            }
        } else {
            status = 0;
        }

        switch (status) {
            case 0:
                btnStartVisit.setVisibility(View.VISIBLE);
                btnStartDay.setVisibility(View.VISIBLE);
                btnEndVisit.setVisibility(View.GONE);
                btnEndDay.setVisibility(View.GONE);
                btnAddVisit.setVisibility(View.GONE);
                btnAddNoo.setVisibility(View.GONE);
                workManager.cancelAllWork();
                break;
            case 1:
                btnStartVisit.setVisibility(View.GONE);
                btnStartDay.setVisibility(View.GONE);
                btnEndVisit.setVisibility(View.VISIBLE);
                btnEndDay.setVisibility(View.VISIBLE);
                btnAddVisit.setVisibility(View.VISIBLE);
                btnAddNoo.setVisibility(View.VISIBLE);
                break;
            case 2:
                btnStartVisit.setVisibility(View.GONE);
                btnStartDay.setVisibility(View.GONE);
                btnEndVisit.setVisibility(View.GONE);
                btnEndDay.setVisibility(View.GONE);
                btnAddVisit.setVisibility(View.GONE);
                btnAddNoo.setVisibility(View.GONE);
                workManager.cancelAllWork();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Helper.takePhoto(VisitActivity.this);
        } else if (requestCode == PERMISSION_REQUEST_CODE && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            endTodayVisit();//end day/visit
        } else if (requestCode == Constants.LOCATION_PERMISSION_REQUEST
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            if (Utils.isGPSOn(this)) {
                getLocationGPS();//from onRequestPermissionsResult
            } else {
                Utils.turnOnGPS(this);
            }
        } else {
            setToast(getString(R.string.pleaseEnablePermission));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Helper.setItemParam(Constants.FROM_VISIT, "1");
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getLocationGPS() {
        getAddressWithPermission((result, location) -> {
            Utils.backgroundTask(progress,
//                    () -> Utils.getCurrentAddress(CreateNooActivity.this, location.getLatitude(), location.getLongitude()),
                    () -> Utils.getCurrentAddressFull(VisitActivity.this, location.getLatitude(), location.getLongitude()),
                    new CallbackOnResult<Address>() {
                        @Override
                        public void onFinish(Address result) {
                            if (location != null) {
                                currentLocation = new HashMap();
                                currentLocation.put("latitude", location.getLatitude());
                                currentLocation.put("longitude", location.getLongitude());
                                currentLocation.put("address", result.getAddressLine(0));
                                if (endVisit) {
                                    if (database.getCountNotVisit() == 0) {
                                        openDialogEndVisit();//get location
                                    } else {
                                        openDialogReasonNotVisit();//end visit
                                    }

                                } else {
                                    openDialogMapCheckIn();
                                }
                            } else {
                                setToast("Lokasi tidak di temukan");
                            }
                        }

                        @Override
                        public void onFailed() {
                            setToast("Lokasi tidak di temukan");
                        }
                    });
        });
    }

    public void getAddressWithPermission(LocationRequestCallback<String, Location> callbackOnResult) {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            mResultReceiver.setCallback(callbackOnResult);
            this.addressCallback = callbackOnResult;
            getAddress();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getAddress() {
        mFusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        mLastLocation = location;
                        mResultReceiver.setLastLocation(mLastLocation);
                        if (!Geocoder.isPresent()) {
                            if (addressCallback != null) {
                                addressCallback.onFinish("Geocoder not available", location);
                            }
                            return;
                        }
                        startIntentService();
                    } else {
                        addressCallback.onFinish(null, null);
                        return;
                    }
                })
                .addOnFailureListener(this, e -> {
                    addressCallback.onFinish(null, null);
                });
    }

    private void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    public boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_REQUEST);
    }
}
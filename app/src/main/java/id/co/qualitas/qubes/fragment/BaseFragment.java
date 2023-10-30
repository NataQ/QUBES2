package id.co.qualitas.qubes.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.LoginActivity;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.AddNewOutletListAdapter;
import id.co.qualitas.qubes.adapter.ShowPriceAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.database.SecondDatabaseHelper;
import id.co.qualitas.qubes.helper.CalendarUtils;
import id.co.qualitas.qubes.helper.GPSTracker;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.CheckInOutRequest;
import id.co.qualitas.qubes.model.GPSModel;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.MaterialResponse;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.OfflineLoginData;
import id.co.qualitas.qubes.model.OrderPlanHeader;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.PromotionFilter;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.SummaryRequest;
import id.co.qualitas.qubes.model.TargetSummaryRequest;
import id.co.qualitas.qubes.model.ToPrice;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;
import id.co.qualitas.qubes.model.VisitOrderRequest;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class BaseFragment extends Fragment implements SearchView.OnQueryTextListener {
    public int flagCode = 0, flagName = 0;
    protected Context context;
    protected String url;
    private TextView txtImagePathCost;
    private ImageView imgClose;
    private Button btnSubmit;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    protected SwipeRefreshLayout swipeLayout;
    protected ProgressBar progressCircle;

    public View getRootView() {
        return rootView;
    }

    protected View rootView;

    public ProgressDialog getProgress() {
        return progress;
    }

    protected ImageView imgBack;
    protected ProgressDialog progress;
    protected ProgressBar progressBar;
    protected String result = "";
    protected LinearLayout linearLayoutList;
    protected Button btnSave, btnCancel, btnCheckIn, btnYes, btnNo, btnReset, btnBack;
    final protected static int DIALOG_REASON = 1;
    final protected static int DIALOG_SYNC_MENU = 2;
    final protected static int DIALOG_ATTACH_PHOTO = 3;
    final protected static int DIALOG_CHECK_IN = 4;
    final protected static int DIALOG_SIGNATURE = 5;
    final protected static int DIALOG_FEEDBACK = 6;
    final protected static int DIALOG_EDIT_ITEM = 7;
    final protected static int DIALOG_ADD_RETURN = 8;
    final protected static int DIALOG_ADD_RETURN_SALES = 10;
    final protected static int DIALOG_ADD_MATERIAL = 11;
    final protected static int DIALOG_CREATE_NEW_ORDER_PLAN = 12;
    final protected static int DIALOG_COLLECT_PAYMENT = 13;
    final protected static int DIALOG_REASON_PAUSE = 14;
    final protected static int DIALOG_ACHIEVEMENT = 15;
    final protected static int DIALOG_ROUNDING_CHOICE = 16;
    final protected static int DIALOG_ROUNDING = 17;
    final protected static int DIALOG_OFFSET = 18;
    final protected static int DIALOG_SPECIMEN = 19;
    final protected static int DIALOG_ALERT_CONFIRM = 20;
    final protected static int DIALOG_ADD_NEW_VISIT = 21;
    final protected static int DIALOG_VIEW_PRICE = 22;
    final protected static int DIALOG_FILTER = 23;

    protected static Database database;
    protected TextView txtViewEmpty, txtTitle;
    private SignaturePad mSignaturePad;

    protected ImageView imgCameraPhoto, imgGalleryPhoto, imgCancelPhoto, imgAttachmentReturn, imgAttachedCost;
    protected TextView txtImagePath;
    protected EditText edtTxtMaterialCode, edtTxtQty, edtTxtDesc, edtTxtBatch, edtTxtDeliveryNumber, edtTxtMaterialName, edtTextAmountToPay, editAmountPaid, edtKlasifikasi, edtTxtComment;
    protected Bitmap bitmapCamera, bitmapGallery, bitmap;
    protected AutoCompleteTextView edtMaterialName, edtMaterialCode;

    private int PARAM_PHOTO = 0;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;

    protected Fragment fragment = null;

    private TextView txtImagePathAttachment;
    private ImageView imgAttachment, imgSignatureSaved;
    protected TextView txtEmpty, txtDialog;

    protected RecyclerView mRecyclerView, mRecyclerView2;
    protected LinearLayoutManager mLayoutManager, mLayoutManager2;
    protected DatabaseHelper db;
    protected SecondDatabaseHelper sdb;

    public ArrayAdapter<String> getSpinnerAdapter() {
        return spinnerAdapter;
    }

    protected ArrayAdapter<String> spinnerAdapter;

    protected ArrayAdapter<String> spinnerAdapterOrder;
    public ArrayList<OutletResponse> outletLists, visitList = new ArrayList<>();
    //new
    public ArrayList<OrderPlanHeader> orderPlanHeaderArrayList;

    public CheckInOutRequest checkInRequest, checkOutRequest, pauseRequest;
    protected OutletResponse outletResponse;
    protected RecyclerView recyclerView;
    private Bitmap bitmapPhoto, compressedBitmapGallery;
    protected int PARAM = 0;
    protected int PARAM_DIALOG_ALERT = 0;
    private MessageResponse messageResponseFinish, messageResponsePause, messageResponseSave;
    protected User user;
    public static Dialog alertDialog;
    final String[] dateString = new String[1];
    protected String dayF;
    protected VisitOrderHeader visitOrderHeader, orderHeaderDetail;

    protected String idEmployee = Constants.EMPTY_STRING;

    private ArrayList<VisitOrderHeader> orderHeaderList;
    protected ArrayList<User> attendances = new ArrayList<>();

    protected GPSTracker gpsTracker;
    private Date curDate = new Date();
    private OfflineLoginData offlineData;

    private Spinner spn1, spn2, spn3;
    private CheckBox cb1, cb2, cb3;

    private LinearLayout linearRow1, linearRow2, linearRow3, linearValidDate;
    private View div1, div2, div3;

    private TextView txtRow1;
    private TextView txtRow2;

    private EditText edtValidFrom, edtValidTo;

    private boolean spinnerOutlet = false, spinnerDivProd = false;

    protected PromotionFilter promotionFilterRequest = new PromotionFilter();
    protected SummaryRequest summaryRequest = new SummaryRequest();

    private ArrayList<String> listMaterialName, listMaterialCode;
    private ArrayAdapter adapter, codeAdapter;
    private int[] go = {0};

    private ArrayAdapter<String> monthAdapter, outletAdapter, divProdAdapter;

    public void setPARAM_STATUS_OUTLET(String PARAM_STATUS_OUTLET) {
        this.PARAM_STATUS_OUTLET = PARAM_STATUS_OUTLET;
    }

    protected static String PARAM_STATUS_OUTLET = Constants.UNCHECKED_IN;

    public static Dialog getAlertDialog() {
        return alertDialog;
    }

    private List<String> idOutletList;

    public ArrayList<OutletResponse> tempAvailableOutlet;

    protected int flagAttachment = 0;

    protected int flagEdtReason = 0;

    private List<String> divProdList;

    protected int PARAM_MENU_SYNC = 0;

    private ArrayList<OrderPlanHeader> listUpdateOrderPlan = new ArrayList<>();
    private ArrayList<OrderPlanHeader> listInsertOrderPlan = new ArrayList<>();
    private ArrayList<OrderPlanHeader> listDeleteOrderPlan = new ArrayList<>();
    private ArrayList<OrderPlanHeader> listOrderPlan = new ArrayList<>();

    private ArrayList<OutletResponse> listVisitPlan = new ArrayList<>();
    private ArrayList<OutletResponse> listSaveNewVisit = new ArrayList<>();
    protected ArrayList<OutletResponse> listDeleteNewVisit = new ArrayList<>();
    private ArrayList<OutletResponse> listSaveVisitSalesman = new ArrayList<>();
    private ArrayList<OutletResponse> listUpdateVisitSalesman = new ArrayList<>();

    private MessageResponse msg;

    public View dialogview;
    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initFragment();
        Helper.trustSSL();
        initProgress();
        setFormatSeparator();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void initFragment() {
        database = new Database(getContext());
        if (SessionManagerQubes.getUserProfile() != null) {
            Helper.setItemParam(Constants.USER_DETAIL, SessionManagerQubes.getUserProfile());
            user = (User) Helper.getItemParam(Constants.USER_DETAIL);
            if (user == null) {
                setToast("Session telah habis. Silahkan login ulang.");
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            if (SessionManagerQubes.getUrl() != null) {
                Helper.setItemParam(Constants.URL, SessionManagerQubes.getUrl());
            }
        } else {
            setToast("Session telah habis. Silahkan login ulang.");
            clearAllSession();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        /*db = new DatabaseHelper(getContext());
        sdb = new SecondDatabaseHelper(getContext());

        attendances = db.getAttendance();
        int ATZchecked = 0;
        int ATchecked = 0;
        try {
            ATZchecked = Settings.Global.getInt(getContext().getContentResolver(), Settings.Global.AUTO_TIME_ZONE);
            ATchecked = Settings.Global.getInt(getContext().getContentResolver(), Settings.Global.AUTO_TIME);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        SecureDate.getInstance().initAttend(attendances, ATZchecked, ATchecked);
        curDate = SecureDate.getInstance().getDate();
        if (curDate == null) {
            PARAM = 5;
            new RequestUrl().execute();
        }
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        if (user != null) {
            if (user.getIdEmployee() != null) {
                idEmployee = user.getIdEmployee();
            }
        }*/
    }

    public void initProgress() {
        progress = new ProgressDialog(getActivity());
        progress.setMessage(Constants.STR_WAIT);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
    }

    private void clearAllSession() {
        SessionManagerQubes.clearLoginSession();
        SessionManagerQubes.clearStockRequestHeaderSession();
        SessionManagerQubes.clearStartDaySession();
        SessionManagerQubes.clearCollectionHistorySession();
        SessionManagerQubes.clearImageTypeSession();
        SessionManagerQubes.clearReturnSession();
        SessionManagerQubes.clearCustomerNooSession();
        SessionManagerQubes.clearOutletHeaderSession();
        SessionManagerQubes.clearCollectionHeaderSession();
        SessionManagerQubes.clearRouteCustomerHeaderSession();

        database.deleteStockRequestHeader();
        database.deleteStockRequestDetail();
        database.deleteInvoiceHeader();
        database.deleteInvoiceDetail();
        database.deleteMasterNonRouteCustomer();
        database.deleteMasterNonRouteCustomerPromotion();
        database.deleteCustomer();
        database.deleteCustomerPromotion();
        database.deleteNoo();
        database.deleteMasterBank();
        database.deleteMasterReason();
        database.deleteMasterMaterial();
        database.deleteMasterUom();
        database.deleteMasterCustomerType();
        database.deleteMasterDaerahTingkat();
        database.deleteMasterPriceCode();
        database.deleteMasterSalesPriceHeader();
        database.deleteMasterSalesPriceDetail();
        database.deleteMasterParameter();

        Helper.removeItemParam(Constants.FROM_VISIT);
        Helper.removeItemParam(Constants.CURRENTPAGE);
    }

    public void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }

    @SuppressLint("InflateParams")
    public void openDialog(int id) {
        initFragment();
        inflater = LayoutInflater.from(getActivity());
        alertDialog = new Dialog(getActivity());
        switch (id) {
            case DIALOG_REASON:
                initDialog(R.layout.custom_dialog_reason);

                btnSave = alertDialog.findViewById(R.id.btnSave);
                btnCancel = alertDialog.findViewById(R.id.btnCancel);
                final EditText edtReason = alertDialog.findViewById(R.id.edtReason);

                btnSave.setText(getContext().getResources().getString(R.string.check_out));

                checkOutRequest = (CheckInOutRequest) Helper.getItemParam(Constants.REQUEST);
                user = (User) Helper.getItemParam(Constants.USER_DETAIL);

                btnSave.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        checkOutRequest.setReason(edtReason.getText().toString());
                        if (edtReason.getText().toString().isEmpty()) {
                            edtReason.setError(getResources().getString(R.string.pleaseFillReason));
                        } else {
                            OutletResponse outletResponse = new OutletResponse();
                            outletResponse.setIdOutlet(checkOutRequest.getIdOutlet());
                            outletResponse.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                            outletResponse.setLat_check_out(Double.valueOf(checkOutRequest.getLatCheckOut()));
                            outletResponse.setLong_check_out(Double.valueOf(checkOutRequest.getLongCheckOut()));
                            outletResponse.setCheck_out_time(String.valueOf(curDate.getTime()));
                            outletResponse.setTimer(checkOutRequest.getTimer());
                            outletResponse.setReason(checkOutRequest.getReason());
                            db.updateVisitPlan(outletResponse);

                            ((MainActivity) getActivity()).changePage(3);
//                            fragment = new RetailOutletFragment();
//                            setContent(fragment);

                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(edtReason.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            alertDialog.dismiss();
                        }
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edtReason.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_SYNC_MENU:
                initDialog(R.layout.custom_dialog_sync_menu);

                LinearLayout linSyncAll = alertDialog.findViewById(R.id.linearSyncAll);
                LinearLayout linSyncMasterData = alertDialog.findViewById(R.id.linearMasterData);
                LinearLayout linearCollection = alertDialog.findViewById(R.id.linearCollection);
                LinearLayout linSyncVisit = alertDialog.findViewById(R.id.linearVisit);
                LinearLayout linSyncStoreCheck = alertDialog.findViewById(R.id.linearStoreCheck);
                LinearLayout linSyncOrder = alertDialog.findViewById(R.id.linearOrder);
                LinearLayout linSyncReturn = alertDialog.findViewById(R.id.linearReturn);
                btnCancel = alertDialog.findViewById(R.id.btnCancel);

                PARAM_DIALOG_ALERT = 3;

                linSyncAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM_MENU_SYNC = 1;
                        openDialog(DIALOG_ALERT_CONFIRM);
                    }
                });


                linSyncMasterData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM_MENU_SYNC = 1;
                        openDialog(DIALOG_ALERT_CONFIRM);
                    }
                });

                linearCollection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM_MENU_SYNC = 2;
                        openDialog(DIALOG_ALERT_CONFIRM);
                    }
                });

                linSyncVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM_MENU_SYNC = 3;
                        openDialog(DIALOG_ALERT_CONFIRM);
                    }
                });

                linSyncStoreCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM_MENU_SYNC = 4;
                        openDialog(DIALOG_ALERT_CONFIRM);
                    }
                });

                linSyncOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM_MENU_SYNC = 5;
                        openDialog(DIALOG_ALERT_CONFIRM);
                    }
                });

                linSyncReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PARAM_MENU_SYNC = 6;
                        openDialog(DIALOG_ALERT_CONFIRM);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_ATTACH_PHOTO:
                initProgress();
                db = new DatabaseHelper(getContext());
                initDialog(R.layout.custom_dialog_photo_new);

                btnSave = alertDialog.findViewById(R.id.btnSave);
                btnCancel = alertDialog.findViewById(R.id.btnCancel);
                imgCameraPhoto = alertDialog.findViewById(R.id.imgCameraPhoto);
                imgGalleryPhoto = alertDialog.findViewById(R.id.imgGalleryPhoto);
                imgCancelPhoto = alertDialog.findViewById(R.id.imgCancelPhoto);
                txtImagePathAttachment = alertDialog.findViewById(R.id.txtImagePathAttachment);
                imgAttachment = alertDialog.findViewById(R.id.imgAttachment);
                visitOrderHeader = (VisitOrderHeader) Helper.getItemParam(Constants.ORDER_HEADER_SELECTED);
                user = (User) Helper.getItemParam(Constants.USER_DETAIL);
                final OutletResponse outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);

                if (visitOrderHeader != null) {
                    if (visitOrderHeader.getPhotoString() != null) {
                        byte[] decodedString = Base64.decode(visitOrderHeader.getPhotoString(), Base64.DEFAULT);
                        final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgAttachment.setImageBitmap(decodedByte);
                    }
                }

                imgCancelPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bitmapCamera != null) {
                            bitmap = bitmapCamera;
                        } else if (bitmapGallery != null) {
                            bitmap = bitmapGallery;
                        }
                        if (bitmap != null) {
                            txtImagePathAttachment.setText(R.string.attach_here);
                            imgAttachment.setImageResource(R.drawable.ic_attach);
                            bitmap = null;
                        } else {
                            txtImagePathAttachment.setText(R.string.attach_here);
                            imgAttachment.setImageResource(R.drawable.ic_attach);
                            bitmap = null;
                        }
                    }
                });

                imgCameraPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bitmapCamera != null) {
                            bitmap = bitmapCamera;
                        } else if (bitmapGallery != null) {
                            bitmap = bitmapGallery;
                        }
                        if (bitmap != null) {
                            txtImagePathAttachment.setText(R.string.attach_here);
                            imgAttachment.setImageResource(R.drawable.ic_attach);
                            bitmap = null;
                        } else {
                            txtImagePathAttachment.setText(R.string.attach_here);
                            imgAttachment.setImageResource(R.drawable.ic_attach);
                            bitmap = null;
                        }
                        PARAM_PHOTO = 3;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                });

                imgGalleryPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bitmap = null;
                        bitmapCamera = null;
                        bitmapGallery = null;

                        PARAM_PHOTO = 3;
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }
                });

                if (Helper.dialogCase.equals("S")) {
                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Helper.dialogCase = "Ph";
                            VisitOrderHeader header = new VisitOrderHeader();

                            if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                                header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
                                if (bitmapPhoto != null) {
                                    compressedBitmapGallery = scaleDown(bitmapPhoto, 600, true);
                                }
                                if (visitOrderHeader != null) {
                                    if (visitOrderHeader.getPhotoString() != null) {
                                        if (getEncodedImage(compressedBitmapGallery).equals("")) {
                                            header.setPhotoString(visitOrderHeader.getPhotoString());
                                        } else if (!getEncodedImage(compressedBitmapGallery).equals("")) {
                                            header.setPhotoString(getEncodedImage(compressedBitmapGallery));
                                        }
                                    } else {
                                        if (bitmapPhoto != null) {
                                            header.setPhotoString(getEncodedImage(compressedBitmapGallery));
                                        }
                                    }

                                } else {
                                    header.setPhotoString(null);
                                    if (bitmapPhoto != null) {
                                        header.setPhotoString(getEncodedImage(compressedBitmapGallery));
                                    }
                                }
                                Helper.setItemParam(Constants.VISIT_ORDER_HEADER, header);
                            }

                            orderHeaderDetail = (VisitOrderHeader) Helper.getItemParam(Constants.ORDER_HEADER_SELECTED);

                            progress.show();
                            header = new VisitOrderHeader();
                            if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                                header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
                            }
                            user = (User) Helper.getItemParam(Constants.USER_DETAIL);
                            if (user.getReason() == 0) {
                                header.setIdEmployee("KT");
                            }
                            if (Helper.getItemParam(Constants.ORDER_TYPE) != null) {
                                header.setOrderType(Helper.getItemParam(Constants.ORDER_TYPE).toString());
                            }
                            List<VisitOrderDetailResponse> visitOrderDetailResponseList = new ArrayList<>();
                            if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                                visitOrderDetailResponseList = (List<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);
                            }

                            VisitOrderRequest visitOrderRequest = new VisitOrderRequest();
                            visitOrderRequest.setOrderDetail(visitOrderDetailResponseList);
                            visitOrderRequest.setOrderHeader(header);

                            if (outletResponse != null) {
                                if (outletResponse.getIdOutlet() != null && Helper.getItemParam(Constants.ORDER_TYPE) != null) {
                                    orderHeaderList = db.getListOrderHeader(outletResponse.getIdOutlet(), Helper.getItemParam(Constants.ORDER_TYPE).toString());
                                }
                            }
                            if (orderHeaderDetail == null) {
                                String idTo = Constants.EMPTY_STRING;
                                if (Helper.getItemParam(Constants.ORDER_TYPE).equals(Constants.ORDER_CANVAS_TYPE)) {
                                    idTo = Constants.ID_SAVE_OC.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
                                } else {
                                    idTo = Constants.ID_SAVE_OT.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
                                }

                                header.setId(idTo);
                                header.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                db.addOrderHeader(header);
                                if (header.getListFreeGoods() != null && !header.getListFreeGoods().isEmpty()) {
                                    db.addFreeGoodsWODrop(idTo, header.getListFreeGoods());
                                }

                                if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
                                    for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
                                        db.addOrderDetail(idTo, visitOrderDetailResponseList.get(i));
                                        if (visitOrderDetailResponseList.get(i).getListToPrice() != null && !visitOrderDetailResponseList.get(i).getListToPrice().isEmpty()) {
                                            for (int j = 0; j < visitOrderDetailResponseList.get(i).getListToPrice().size(); j++) {
                                                db.addToPrice(visitOrderDetailResponseList.get(i).getListToPrice().get(j));
                                            }
                                        }
                                    }
                                }
                            } else {
                                db.updateOrderHeader(header);
                                db.deleteOrderDetailBy(orderHeaderDetail.getId());
                                if (visitOrderDetailResponseList != null && !visitOrderDetailResponseList.isEmpty()) {
                                    for (int i = 0; i < visitOrderDetailResponseList.size(); i++) {
                                        db.addOrderDetail(orderHeaderDetail.getId(), visitOrderDetailResponseList.get(i));
                                        if (visitOrderDetailResponseList.get(i).getListToPrice() != null && !visitOrderDetailResponseList.get(i).getListToPrice().isEmpty()) {
                                            for (int j = 0; j < visitOrderDetailResponseList.get(i).getListToPrice().size(); j++) {
                                                db.addToPrice(visitOrderDetailResponseList.get(i).getListToPrice().get(j));
                                            }
                                        }
                                    }
                                }
                            }

                            if (Helper.getItemParam(Constants.FROM_SALES_ORDER) != null) {
                                ((MainActivity) getActivity()).changePage(4);
                            } else {
                                ((MainActivity) getActivity()).changePage(11);
                            }


//                            for (int i = 0; i < 3; i++) {
//                                getActivity().onBackPressed();//???
//                            }
                            progress.dismiss();
                            alertDialog.dismiss();
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                } else {
                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }

                alertDialog.show();
                break;
            case DIALOG_CHECK_IN:
                initDialog(R.layout.custom_dialog_check_in);

                checkInRequest = (CheckInOutRequest) Helper.getItemParam(Constants.CHECK_IN_REQUEST);

                TextView outletName = alertDialog.findViewById(R.id.namaPerusahaan);
                TextView outletAddress = alertDialog.findViewById(R.id.alamatPerusahaan);
                TextView outletPostal = alertDialog.findViewById(R.id.kota);

                if (checkInRequest != null) {
                    if (checkInRequest.getOutletName() != null) {
                        outletName.setText(checkInRequest.getOutletName());

                        if (Helper.getItemParam(Constants.GPS_DATA) != null) {
                            outletAddress.setText(Helper.validateResponseEmpty(((GPSModel) Helper.getItemParam(Constants.GPS_DATA)).getAddressLine()));
                            outletPostal.setText(Helper.validateResponseEmpty(((GPSModel) Helper.getItemParam(Constants.GPS_DATA)).getPostalCode()));
                        }
                    }
                }

                btnCheckIn = alertDialog.findViewById(R.id.btnCheckIn);

                btnCheckIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OutletResponse outletResponse = new OutletResponse();
                        outletResponse.setIdOutlet(checkInRequest.getIdOutlet());
                        if (user != null) {
                            outletResponse.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                        }
                        outletResponse.setCheckInTime(String.valueOf(curDate.getTime()));
                        outletResponse.setTimer("0");
                        outletResponse.setLat_check_in(Double.valueOf(checkInRequest.getLatCheckIn()));
                        outletResponse.setLong_check_in(Double.valueOf(checkInRequest.getLongCheckIn()));
                        db.updateVisitPlan(outletResponse);
                        PARAM_STATUS_OUTLET = Constants.CHECK_IN;

                        onResume();
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        startActivity(intent);
//                        fragment = new TimerFragment();
//                        setContent(fragment);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_SIGNATURE:
                initDialog(R.layout.custom_dialog_signature_and_comment);

                btnSave = alertDialog.findViewById(R.id.btnSave);
                btnReset = alertDialog.findViewById(R.id.btnReset);
                mSignaturePad = alertDialog.findViewById(R.id.signature_pad);
                edtTxtComment = alertDialog.findViewById(R.id.edtTxtComment);
                imgSignatureSaved = alertDialog.findViewById(R.id.imgSignatureSaved);

                visitOrderHeader = (VisitOrderHeader) Helper.getItemParam(Constants.ORDER_HEADER_SELECTED);
                OutletResponse outlet = new OutletResponse();
                outlet = (OutletResponse) Helper.getItemParam(Constants.OUTLET);

                if (outlet != null) {
                    if (outlet.getStatusCheckIn() != null) {
                        if (outlet.getStatusCheckIn().equals(Constants.FINISHED)) {
                            edtTxtComment.setEnabled(false);
                        }
                    }
                }

                if (visitOrderHeader != null) {
                    if (visitOrderHeader.getSignatureString() != null) {
                        byte[] decodedString = Base64.decode(visitOrderHeader.getSignatureString(), Base64.DEFAULT);
                        final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgSignatureSaved.setVisibility(View.VISIBLE);
                        imgSignatureSaved.setImageBitmap(decodedByte);

                        mSignaturePad.setVisibility(View.GONE);
                    } else {
                        mSignaturePad.setVisibility(View.VISIBLE);
                        imgSignatureSaved.setVisibility(View.GONE);
                    }

                    if (visitOrderHeader.getComment() != null) {
                        edtTxtComment.setText(visitOrderHeader.getComment());
                    }
                } else {
                    mSignaturePad.setVisibility(View.VISIBLE);
                    imgSignatureSaved.setVisibility(View.GONE);
                }

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preventDupeDialog();
                        //getSignatureBitmap() => background white
                        //getTransparentSignatureBitmap() => background transparant
                        VisitOrderHeader header = new VisitOrderHeader();
                        Bitmap bitmapSignature;
                        if (imgSignatureSaved.getVisibility() == View.GONE) {
                            bitmapSignature = mSignaturePad.getSignatureBitmap();
                        } else if (visitOrderHeader != null) {
                            if (visitOrderHeader.getSignatureString() != null) {
                                byte[] decodedString = Base64.decode(visitOrderHeader.getSignatureString(), Base64.DEFAULT);
                                final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                bitmapSignature = decodedByte;
                            } else {
                                bitmapSignature = mSignaturePad.getSignatureBitmap();
                            }
                        } else {
                            bitmapSignature = mSignaturePad.getSignatureBitmap();
                        }

                        if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                            header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
                            header.setComment(edtTxtComment.getText().toString().trim());
                            header.setSignatureString(getEncodedImage(bitmapSignature));
                            Helper.setItemParam(Constants.VISIT_ORDER_HEADER, header);

                        }
                        alertDialog.dismiss();
                        Helper.dialogCase = "S";
                        openDialog(DIALOG_ATTACH_PHOTO);
                    }
                });

                btnReset.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        imgSignatureSaved.setVisibility(View.GONE);
                        mSignaturePad.setVisibility(View.VISIBLE);
                        mSignaturePad.clear();
                    }
                });
                alertDialog.show();
                break;
            case DIALOG_EDIT_ITEM:
                Toast.makeText(getContext(), "DIALOG EDIT ITEM", Toast.LENGTH_SHORT).show();
//                initDialog(R.layout.custom_dialog_edit_item);
//
//                btnSave = alertDialog.findViewById(R.id.btnSave);
//                btnCancel = alertDialog.findViewById(R.id.btnCancel);
//                btnSave.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//                btnCancel.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//                alertDialog.show();
                break;
            case DIALOG_FEEDBACK:
                Toast.makeText(getContext(), "DIALOG FEEDBACK", Toast.LENGTH_SHORT).show();
//                initDialog(R.layout.custom_dialog_feedback);
//
//                btnYes = alertDialog.findViewById(R.id.btnYes);
//                btnNo = alertDialog.findViewById(R.id.btnNo);
//                if (Helper.dialogCase.equals("Ph")) {
//                    btnYes.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            DatabaseHelper db = new DatabaseHelper(getActivity());
//                            StandardDeliveryOrder std = new StandardDeliveryOrder();
//                            String timeStamp = new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime());
//                            String date = new SimpleDateFormat("yymmdd").format(Calendar.getInstance().getTime());
//                            std.setCheck_out_date(date);
//                            std.setCheck_out_time(timeStamp);
//                            std.setStatus("CO");
//                            std.setCustomer_no(Helper.customerNo);
//                            db.checkOut(std);
//                            alertDialog.dismiss();
//                            ((MainActivity) getActivity()).changePage(3);
////                            fragment = new RetailOutletFragment();
////                            setContent(fragment);
//                            Helper.removeItemParam(Constants.CHECK_IN);
//                            // openDialog(DIALOG_SIGNATURE);
//                        }
//                    });
//                    btnNo.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            alertDialog.dismiss();
//                        }
//                    });
//
//                }
//                if (Helper.dialogCase.equals("D")) {
//                    btnYes.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            Helper.totalItem = 0;
//                            alertDialog.dismiss();
//                        }
//                    });
//                    btnNo.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            alertDialog.dismiss();
//                        }
//                    });
//
//                } else if (Helper.dialogCase.equals("P")) {
//                    btnYes.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            StandardDeliveryOrder std = new StandardDeliveryOrder();
//                            std.setStatus("I");
//                            std.setCustomer_no(Helper.customerNo);
//                            database.pass(std);
//                            alertDialog.dismiss();
//                            ((MainActivity) getActivity()).changePage(3);
////                            fragment = new RetailOutletFragment();
////                            setContent(fragment);
//                        }
//                    });
//
//                    btnNo.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            alertDialog.dismiss();
//                        }
//                    });
//
//                }
//                alertDialog.show();
                break;
            case DIALOG_ADD_RETURN:
                Toast.makeText(getContext(), "DIALOG ADD RETURN", Toast.LENGTH_SHORT).show();
//                initDialog(R.layout.custom_d"ialog_add_return);
//
//                btnCancel = alertDialog.findViewById(R.id.btnCancel);
//                btnSave = alertDialog.findViewById(R.id.btnSave);
//
//                btnCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                btnSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Helper.setItemParam(Constants.FLAG_CREATE, "a");
//                        alertDialog.dismiss();
//                    }
//                });
//                alertDialog.show();"
                break;
            case DIALOG_ADD_RETURN_SALES:
                Toast.makeText(getContext(), "DIALOG ADD RETURN SALES", Toast.LENGTH_SHORT).show();
//                initDialog(R.layout.custom_dialog_add_return_sales);
//                alertDialog.show();
                break;
            case DIALOG_ADD_MATERIAL:
                initDialog(R.layout.custom_dialog_add_material);

                imgClose = alertDialog.findViewById(R.id.btnCancel);
                btnSave = alertDialog.findViewById(R.id.btnSave);
                edtMaterialName = dialogview.findViewById(R.id.materialName);
                edtMaterialCode = dialogview.findViewById(R.id.materialCode);
                edtKlasifikasi = dialogview.findViewById(R.id.klasifikasi);

                new LoadMaterial().execute();

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftKeyboard(edtMaterialName);
                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listMaterialName.contains(edtMaterialName.getText().toString())) {
                            Material material = new Material();
                            material.setMaterialCode(edtMaterialCode.getText().toString());
//                            material.setMaterialid(edtMaterialCode.getText().toString());
                            material.setDesc(edtMaterialName.getText().toString());
                            material.setKlasifikasi(edtKlasifikasi.getText().toString());

                            MaterialResponse materialResponse = new MaterialResponse();
                            materialResponse.setIdMaterial(edtMaterialCode.getText().toString());
                            materialResponse.setMaterialName(edtMaterialName.getText().toString());

                            Helper.setItemParam(Constants.ADD_MATERIAL, material);
                            Helper.setItemParam(Constants.ADD_MATERIAL_STORE, materialResponse);
                            Helper.setItemParam(Constants.GET_DETAIL_VISIT, "1");

                            hideSoftKeyboard(edtMaterialName);
                            alertDialog.dismiss();
                            onResume();
                        } else {
                            Toast.makeText(getContext(), R.string.warningXMaterial, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case DIALOG_CREATE_NEW_ORDER_PLAN:
                Toast.makeText(getContext(), "DIALOG CREATE NEW ORDER PLAN", Toast.LENGTH_SHORT).show();
                break;
            case DIALOG_COLLECT_PAYMENT:
                Toast.makeText(getContext(), "DIALOG COLLECT PAYMENT", Toast.LENGTH_SHORT).show();
                break;

            case DIALOG_REASON_PAUSE:
                initDialog(R.layout.custom_dialog_reason_pause);

                btnSave = alertDialog.findViewById(R.id.btnSave);
                btnCancel = alertDialog.findViewById(R.id.btnCancel);
                final EditText edtReasonPause = alertDialog.findViewById(R.id.edtReason);

                checkInRequest = (CheckInOutRequest) Helper.getItemParam(Constants.CHECK_IN_REQUEST);
                user = (User) Helper.getItemParam(Constants.USER_DETAIL);

                pauseRequest = new CheckInOutRequest();
                pauseRequest.setIdOutlet(checkInRequest.getIdOutlet());
                pauseRequest.setIdEmployee(user.getIdEmployee());

                btnSave.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (edtReasonPause.getText().toString().isEmpty()) {
                            edtReasonPause.setError(getResources().getString(R.string.pleaseFillReason));
                        } else {
                            OutletResponse outletResponse = new OutletResponse();
                            outletResponse.setIdOutlet(checkInRequest.getIdOutlet());
                            outletResponse.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                            outletResponse.setPause_time(String.valueOf(curDate.getTime()));
                            outletResponse.setPauseReason(edtReasonPause.getText().toString());
                            outletResponse.setContinue_time(null);
                            outletResponse.setTimer(String.valueOf(SystemClock.elapsedRealtime() - Timer2Fragment.getTimerValue().getBase()));
                            db.updateVisitPlan(outletResponse);

                            PARAM_STATUS_OUTLET = Constants.PAUSE;

                            hideSoftKeyboard(edtReasonPause);
                            onResume();

                            alertDialog.dismiss();
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        hideSoftKeyboard(edtReasonPause);

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_ACHIEVEMENT:
                initProgress();
                initDialog(R.layout.custom_dialog_achievement);

                btnSave = alertDialog.findViewById(R.id.btnSave);
                imgClose = alertDialog.findViewById(R.id.btnCancel);

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        progress.show();
                        PARAM = 4;
                        new RequestUrl().execute();
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_ROUNDING_CHOICE:
                Toast.makeText(getContext(), "DIALOG ROUNDING CHOICE", Toast.LENGTH_SHORT).show();
                break;
            case DIALOG_ROUNDING:
                Toast.makeText(getContext(), "DIALOG ROUNDING", Toast.LENGTH_SHORT).show();
                break;
            case DIALOG_OFFSET:
                Toast.makeText(getContext(), "DIALOG OFFSET", Toast.LENGTH_SHORT).show();
                break;
            case DIALOG_SPECIMEN:
                initDialog(R.layout.custom_dialog_spesimen);

                btnBack = alertDialog.findViewById(R.id.btnBack);
                ImageView imgSpesimen = alertDialog.findViewById(R.id.imgSpesimen);
                if (flagAttachment == 1) {
                    if (Helper.getItemParam(Constants.ATTACHMENT) != null) {
                        if (!Helper.getItemParam(Constants.ATTACHMENT).equals(Constants.NO)) {
                            byte[] decodedString = Base64.decode(String.valueOf(Helper.getItemParam(Constants.ATTACHMENT)), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgSpesimen.setImageBitmap(decodedByte);
                        }
                    }
                    flagAttachment = 0;
                } else {
                    String specimen = (String) Helper.getItemParam(Constants.SPECIMEN);
//                    String specimen = getResources().getString(R.string.testGambar);

                    if (specimen != null) {
                        byte[] decodedString = Base64.decode(specimen, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgSpesimen.setImageBitmap(decodedByte);
                    }
                }
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_ALERT_CONFIRM:
                initDialog(R.layout.custom_dialog_alert_delete);

                btnCancel = alertDialog.findViewById(R.id.btnCancel);
                btnSave = alertDialog.findViewById(R.id.btnSave);
                txtDialog = alertDialog.findViewById(R.id.txtDialog);

                btnSave.setText(getResources().getString(R.string.yes));
                btnCancel.setText(getResources().getString(R.string.no));

                if (PARAM_DIALOG_ALERT == 1) {
                    txtDialog.setText("Save?");
                } else if (PARAM_DIALOG_ALERT == 2) {
                    txtDialog.setText("Apakah anda ingin checkout?");
                    btnSave.setText(getContext().getResources().getString(R.string.check_out));
                } else if (PARAM_DIALOG_ALERT == 3) {
                    txtDialog.setText(getResources().getString(R.string.syncornot));
                }

                checkOutRequest = (CheckInOutRequest) Helper.getItemParam(Constants.REQUEST);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress.dismiss();
                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (PARAM_DIALOG_ALERT == 1) {/*Save OrderPlan*/
//                            progress.show();
                            List<MaterialResponse> listMaterial, listMaterialForSave;
                            OutletResponse outletResponse;
                            OrderPlanHeader orderPlanHeader;
                            orderPlanHeaderArrayList = db.getAllOrderPlanHeader();

                            if (Helper.getItemParam(Constants.ORDER_PLAN) == null) {
                                listMaterial = new ArrayList<MaterialResponse>();
                                listMaterialForSave = new ArrayList<MaterialResponse>();
                            } else {
                                listMaterial = (List<MaterialResponse>) Helper.getItemParam(Constants.ORDER_PLAN);
                                listMaterialForSave = (List<MaterialResponse>) Helper.getItemParam(Constants.ORDER_PLAN_TEMP);
                            }
                            if (Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_HEADER) == null) {
                                orderPlanHeader = new OrderPlanHeader();
                            } else {
                                orderPlanHeader = (OrderPlanHeader) Helper.getItemParam(Constants.ORDER_PLAN_DETAIL_HEADER);
                            }

                            if (orderPlanHeader.getIdOutlet() != null) {
                                if (Helper.getItemParam(Constants.FLAG_DO_ORDER_PLAN_DB) != null) {
                                    if (Helper.getItemParam(Constants.AMOUNT_PLAN) != null) {
                                        orderPlanHeader.setPlan(String.valueOf(Helper.getItemParam(Constants.AMOUNT_PLAN)));
                                    }

                                    if (Helper.getItemParam(Constants.FLAG_DO_ORDER_PLAN_DB).equals(getString(R.string.FLAG_UPDATE_ORDER_PLAN))) {
                                        if (listMaterialForSave != null && !listMaterialForSave.isEmpty()) {
                                            for (int i = 0; i < listMaterialForSave.size(); i++) {
                                                orderPlanHeader.setIdMaterial(listMaterialForSave.get(i).getIdMaterial());
                                                orderPlanHeader.setQty1(String.valueOf(listMaterialForSave.get(i).getNewQty1()));
                                                orderPlanHeader.setUom1(listMaterialForSave.get(i).getNewUom1());
                                                orderPlanHeader.setQty2(String.valueOf(listMaterialForSave.get(i).getNewQty2()));
                                                orderPlanHeader.setUom2(listMaterialForSave.get(i).getNewUom2());
                                                orderPlanHeader.setPrice(listMaterialForSave.get(i).getPrice());

                                                if (listMaterialForSave.get(i).getId() != null) {
                                                    orderPlanHeader.setId(listMaterialForSave.get(i).getId());

                                                    if (listMaterialForSave.get(i).isDeleted()) {
                                                        orderPlanHeader.setDeleted(getResources().getString(R.string.STATUS_TRUE));
                                                    } else {
                                                        orderPlanHeader.setDeleted(getResources().getString(R.string.STATUS_FALSE));
                                                    }

                                                    db.updateOrderPlan(orderPlanHeader);
                                                } else {
                                                    orderPlanHeader.setId(Constants.ID_OP_MOBILE.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime()))));
                                                    orderPlanHeader.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                                    db.addOrderPlan(orderPlanHeader);
                                                }
                                            }
                                        }

                                        orderPlanHeader.setStatusOrder("true");
                                        orderPlanHeader.setDate(orderPlanHeader.getOutletDate());

                                        db.updateStatusOrderPlanNew(orderPlanHeader);
                                        Helper.removeItemParam(Constants.FLAG_DO_ORDER_PLAN_DB);
                                    }
                                } else {/*Save*/
                                    if (listMaterial != null && !listMaterial.isEmpty()) {
                                        for (int i = 0; i < listMaterial.size(); i++) {
                                            orderPlanHeader.setId(Constants.ID_OP_MOBILE.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime()))));
                                            orderPlanHeader.setIdMaterial(listMaterial.get(i).getIdMaterial());
                                            orderPlanHeader.setQty1(String.valueOf(listMaterial.get(i).getNewQty1()));
                                            orderPlanHeader.setUom1(listMaterial.get(i).getNewUom1());
                                            orderPlanHeader.setQty2(String.valueOf(listMaterial.get(i).getNewQty2()));
                                            orderPlanHeader.setUom2(listMaterial.get(i).getNewUom2());
                                            orderPlanHeader.setPrice(listMaterial.get(i).getPrice());
                                            orderPlanHeader.setDeleted("false");
                                            orderPlanHeader.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                            db.addOrderPlan(orderPlanHeader);
                                        }

                                        orderPlanHeader.setStatusOrder("true");
                                        orderPlanHeader.setDate(orderPlanHeader.getOutletDate());

                                        db.updateStatusOrderPlanNew(orderPlanHeader);

                                        Helper.removeItemParam(Constants.GET_DETAIL_ORDER_PLAN);
                                        Helper.removeItemParam(Constants.ORDER_PLAN_DETAIL_SAVE);
                                    }
                                }

//                                progress.dismiss();
                                ((MainActivity) getActivity()).changePage(2);
//                                fragment = new OrderPlanFragment();
//                                setContent(fragment);
                            } else {
                                Toast.makeText(getContext(), R.string.missingData, Toast.LENGTH_SHORT).show();
                            }
                        } else if (PARAM_DIALOG_ALERT == 2) {
                            OutletResponse outletResponse = new OutletResponse();
                            outletResponse.setIdOutlet(checkOutRequest.getIdOutlet());
                            outletResponse.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                            outletResponse.setLat_check_out(Double.valueOf(checkOutRequest.getLatCheckOut()));
                            outletResponse.setLong_check_out(Double.valueOf(checkOutRequest.getLongCheckOut()));
                            outletResponse.setCheck_out_time(String.valueOf(curDate.getTime()));
                            outletResponse.setTimer(checkOutRequest.getTimer());

                            outletResponse.setReason(checkOutRequest.getReason());
                            db.updateVisitPlan(outletResponse);

                            PARAM_STATUS_OUTLET = Constants.FINISHED;
                            onResume();

                        } else if (PARAM_DIALOG_ALERT == 3) {
                            requestUrlSync();
                        }

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_ADD_NEW_VISIT:
//                dialogview = inflater.inflate(R.layout.custom_dialog_add_new_visit, null);
//                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                alertDialog.setContentView(dialogview);
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                initDialog(R.layout.custom_dialog_add_new_visit);
//                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                if (String.valueOf(Helper.getItemParam(Constants.PARAM_FOR_DIALOG)).equals(Constants.SELECTED_CUSTOMER_ORDER_PLAN)) {
//                    Helper.removeItemParam(Constants.PARAM_FOR_DIALOG);

                    outletLists = (ArrayList<OutletResponse>) Helper.getItemParam(Constants.LIST_SELECTED_CUSTOMER_ORDER_PLAN);

                    Helper.removeItemParam(Constants.LIST_SELECTED_CUSTOMER_ORDER_PLAN);
                } else {
                    user = (User) Helper.getItemParam(Constants.USER_DETAIL);

                    visitList = new ArrayList<>();
                    outletLists = new ArrayList<>();

                    visitList = (ArrayList<OutletResponse>) Helper.getItemParam(Constants.OUTLET_LIST_DETAIL);
                    outletLists = db.getAllOutlet();

                    if (outletLists != null && !outletLists.isEmpty()) {
                        tempAvailableOutlet = new ArrayList<>(outletLists);
                        for (int i = 0; i < outletLists.size(); i++) {
                            if (visitList != null && !visitList.isEmpty()) {
                                for (int j = 0; j < visitList.size(); j++) {
                                    try {
                                        if (outletLists.get(i).getIdOutlet() != null && visitList.get(j).getIdOutlet() != null) {
                                            if (outletLists.get(i).getIdOutlet().equals(visitList.get(j).getIdOutlet())) {
                                                outletLists.remove(i);
                                                i = -1;
                                                break;
                                            }
                                        }
                                    } catch (IndexOutOfBoundsException ignored) {
                                    }

                                }
                            }
                        }
                    } else {
                        outletLists = new ArrayList<>();
                    }
                }

                final RecyclerView recyclerViewOutlet2 = dialogview.findViewById(R.id.recycler_view_outlet);
                AddNewOutletListAdapter mAdapter2;
                if (String.valueOf(Helper.getItemParam(Constants.PARAM_FOR_DIALOG)).equals(Constants.SELECTED_CUSTOMER_ORDER_PLAN)) {
                    OrderPlanDetailFragmentV2 fr = new OrderPlanDetailFragmentV2();
                    mAdapter2 = new AddNewOutletListAdapter(outletLists, getActivity().getApplicationContext());
                } else {
                    mAdapter2 = new AddNewOutletListAdapter(outletLists, getActivity().getApplicationContext());
                }
                recyclerViewOutlet2.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewOutlet2.setItemAnimator(new DefaultItemAnimator());
                recyclerViewOutlet2.setAdapter(mAdapter2);
                mAdapter2.notifyDataSetChanged();

                imgClose = dialogview.findViewById(R.id.imgClose);
                btnSubmit = dialogview.findViewById(R.id.btnSubmit);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (outletLists.size() != 0) {
                            if (outletLists.get(0).getPosClicked() != null) {
                                int posClicked = outletLists.get(0).getPosClicked();
                                if (String.valueOf(Helper.getItemParam(Constants.PARAM_FOR_DIALOG)).equals(Constants.SELECTED_CUSTOMER_ORDER_PLAN)) {
                                    Helper.setItemParam(Constants.SELECTED_OUTLET_ORDER_PLAN, outletLists.get(posClicked));

                                    OrderPlanDetailFragmentV2.getEdtOutlet().setText(outletLists.get(posClicked).getIdOutlet().concat(" - ").concat(outletLists.get(posClicked).getOutletName()));
                                    OrderPlanDetailFragmentV2.getEdtOutlet().setEnabled(false);
                                    OrderPlanDetailFragmentV2.getOrderPlanHeader().setIdOutlet(outletLists.get(posClicked).getIdOutlet());
                                } else {
                                    Helper.setItemParam(Constants.OUTLET, outletLists.get(posClicked));
                                    Helper.setItemParam(Constants.GET_DETAIL_NEW_VISIT, "1");
                                    onResume();
//                                Fragment fragment = new RetailOutletFragment();
//                                setContent(fragment);
                                }
                                BaseFragment.getAlertDialog().dismiss();
                            } else {
                                Toast.makeText(getContext(), "Please choose one outlet", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Please choose one outlet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.removeItemParam(Constants.PARAM_FOR_DIALOG);
                        alertDialog.dismiss();
                    }
                });

                EditText searchBar2 = dialogview.findViewById(R.id.searchBar);
                searchBar2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence query, int start, int before, int count) {
                        query = query.toString().toLowerCase();
                        final ArrayList<OutletResponse> filteredList = new ArrayList<>();

                        if (outletLists != null && !outletLists.isEmpty()) {
                            for (OutletResponse outlet : outletLists) {
                                final String text = outlet.getOutletName().toLowerCase() + getResources().getString(R.string.connectorIdWithName) + outlet.getIdOutlet().toLowerCase();
                                if (text.contains(query)) {
                                    filteredList.add(outlet);
                                }
                            }
                        }

                        recyclerViewOutlet2.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewOutlet2.setItemAnimator(new DefaultItemAnimator());
                        AddNewOutletListAdapter nAdapter = new AddNewOutletListAdapter(filteredList, getActivity().getApplicationContext());
                        recyclerViewOutlet2.setAdapter(nAdapter);
                        nAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                alertDialog.show();
                break;
            case DIALOG_VIEW_PRICE:
                initDialog(R.layout.custom_dialog_price);

                ArrayList<ToPrice> listToPrice;
                listToPrice = (ArrayList<ToPrice>) Helper.getItemParam(Constants.PRICE_DETAIL);
                MaterialResponse materialResponse = (MaterialResponse) Helper.getItemParam(Constants.MATERIAL_ORDER_PLAN_DETAIL);
                final ArrayList<MaterialResponse> orderPlanList = (ArrayList<MaterialResponse>) Helper.getItemParam(Constants.ORDER_PLAN);

                final RecyclerView recyclerViewPrice = dialogview.findViewById(R.id.recycler_view_price);
                if (listToPrice != null && !listToPrice.isEmpty()) {
                    ShowPriceAdapter nAdapter = new ShowPriceAdapter(listToPrice, this);
                    recyclerViewPrice.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
                    recyclerViewPrice.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewPrice.setAdapter(nAdapter);
                    nAdapter.notifyDataSetChanged();

                    if (materialResponse != null && orderPlanList != null) {
                        for (int i = 0; i < orderPlanList.size(); i++) {
                            if (materialResponse.getIdMaterial().equals(orderPlanList.get(i).getIdMaterial())) {
                                orderPlanList.get(i).setPrice(listToPrice.get(0).getAmount());
                            }
                        }
                        Helper.setItemParam(Constants.ORDER_PLAN, orderPlanList);
                    }
                }

                btnCancel = dialogview.findViewById(R.id.btnCancel);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
                break;
            case DIALOG_FILTER:
                initDialog(R.layout.custom_dialog_filter);

                linearRow1 = dialogview.findViewById(R.id.linearRow1);
                linearRow2 = dialogview.findViewById(R.id.linearRow2);
                linearRow3 = dialogview.findViewById(R.id.linearRow3);
                linearValidDate = dialogview.findViewById(R.id.linearValidDate);

                div1 = dialogview.findViewById(R.id.div1);
                div2 = dialogview.findViewById(R.id.div2);
                div3 = dialogview.findViewById(R.id.div3);

                cb1 = dialogview.findViewById(R.id.cb1);
                cb2 = dialogview.findViewById(R.id.cb2);
                cb3 = dialogview.findViewById(R.id.cb3);

                spn1 = dialogview.findViewById(R.id.spn1);
                spn2 = dialogview.findViewById(R.id.spn2);
                spn3 = dialogview.findViewById(R.id.spn3);

                txtRow1 = dialogview.findViewById(R.id.txtRow1);
                txtRow2 = dialogview.findViewById(R.id.txtRow2);
                TextView txtRow3 = dialogview.findViewById(R.id.txtRow3);

                edtValidFrom = dialogview.findViewById(R.id.edtValidFrom);
                edtValidTo = dialogview.findViewById(R.id.edtValidTo);

                imgClose = dialogview.findViewById(R.id.imgClose);
                btnYes = dialogview.findViewById(R.id.btnApply);

//                if(Helper.getItemParam(Constants.ACTIVE_MENU) != null) {
                if (Helper.getItemParam(Constants.ACTIVE_MENU).equals(getResources().getString(R.string.navmenu2))) {
                    functionSpinnerForTarget();
                } else if (Helper.getItemParam(Constants.ACTIVE_MENU).toString().equalsIgnoreCase(getResources().getString(R.string.navmenu4))) {
                    functionSpinnerForPromotion();
                } else if (Helper.getItemParam(Constants.ACTIVE_MENU).toString().equalsIgnoreCase(getResources().getString(R.string.navmenu6a))) {
                    addItemsOnDialogFilterSummary();
                    functionSpinnerForSummaryReport();
                }
//                }

                alertDialog.show();
                break;
        }
    }

    public void initDialog(int resource) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialogview = inflater.inflate(resource, null);
        alertDialog.setContentView(dialogview);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
//        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void requestUrlSync() {
        if (PARAM_MENU_SYNC == 1) {
            PARAM = 6;
            new RequestUrl().execute();
            progress.show();
        } else if (PARAM_MENU_SYNC == 2) {
            PARAM = 7;
            new RequestUrl().execute();
            progress.show();
        } else if (PARAM_MENU_SYNC == 3) {
            PARAM = 8;
            new RequestUrl().execute();
            progress.show();
        } else if (PARAM_MENU_SYNC == 4) {
            PARAM = 9;
            new RequestUrl().execute();
            progress.show();
        } else if (PARAM_MENU_SYNC == 5) {
            PARAM = 10;
            new RequestUrl().execute();
            progress.show();
        } else if (PARAM_MENU_SYNC == 6) {
            PARAM = 11;
            new RequestUrl().execute();
            progress.show();
        }
    }

    private void functionSpinnerForSummaryReport() {
        if (Helper.getItemParam(Constants.SUMMARY_TYPE) != null) {
            if (Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER) != null) {
                if (((SummaryRequest) Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER)).getMatClass() != null) {
                    divProdList = new ArrayList<>();
                    if (Helper.getItemParam(Constants.ITEM_DIV_PROD) != null) {
                        divProdList = (List<String>) Helper.getItemParam(Constants.ITEM_DIV_PROD);
                    }
                    if (divProdList != null && !divProdList.isEmpty()) {
                        divProdAdapter = new ArrayAdapter<String>(this.getContext(),
                                R.layout.spinner_item, divProdList);
                        divProdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn3.setAdapter(divProdAdapter);

                        for (String divProd : divProdList) {
                            if (divProd != null) {
                                int spinnerPosition = divProdAdapter.getPosition(((SummaryRequest) Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER)).getMatClass());
                                spn3.setSelection(spinnerPosition);
                                break;
                            }
                        }
                    }
                }

                if (((SummaryRequest) Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER)).getDateFrom() != null) {
                    if (!((SummaryRequest) Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER)).getDateFrom().equals(Constants.EMPTY_STRING)) {
                        edtValidFrom.setText(Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_1,
                                ((SummaryRequest) Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER)).getDateFrom()));
                    }
                }

                if (((SummaryRequest) Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER)).getDateTo() != null) {
                    if (!((SummaryRequest) Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER)).getDateTo().equals(Constants.EMPTY_STRING)) {
                        edtValidTo.setText(Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_1,
                                ((SummaryRequest) Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER)).getDateTo()));

                    }
                }
            }

            if (Helper.getItemParam(Constants.SUMMARY_TYPE).equals(Constants.DAY)) {
                linearRow1.setVisibility(View.GONE);
                linearRow2.setVisibility(View.GONE);
                linearRow3.setVisibility(View.VISIBLE);
                linearValidDate.setVisibility(View.VISIBLE);
                div1.setVisibility(View.GONE);
                div2.setVisibility(View.GONE);
                div3.setVisibility(View.VISIBLE);

                edtValidFrom.setText(user.getCurrentDate());
                edtValidTo.setText(user.getCurrentDate());


                summaryRequest = new SummaryRequest();


                functionDatePromotion(edtValidFrom);
                functionDatePromotion(edtValidTo);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!cb3.isChecked()) {
                            summaryRequest.setMatClass(Constants.EMPTY_STRING);
                        } else {
                            if (spn3.getSelectedItem() != null) {
                                summaryRequest.setMatClass(spn3.getSelectedItem().toString());
                            } else {
                                summaryRequest.setMatClass(Constants.EMPTY_STRING);
                            }
                        }

                        try {
                            if (edtValidFrom.getText() != null) {
                                summaryRequest.setDateFrom(Helper.changeDateFormat(Constants.DATE_TYPE_15, Constants.DATE_TYPE_2, edtValidFrom.getText().toString()));
                            }
                        } catch (NullPointerException e) {

                        }

                        try {
                            if (edtValidTo.getText() != null) {
                                summaryRequest.setDateTo(Helper.changeDateFormat(Constants.DATE_TYPE_15, Constants.DATE_TYPE_2, edtValidTo.getText().toString()));
                            }
                        } catch (NullPointerException e) {

                        }


                        Helper.setItemParam(Constants.SUMMARY_ITEM_FILTER, summaryRequest);

                        onResume();

                        alertDialog.dismiss();
                    }
                });

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            } else {
                linearRow1.setVisibility(View.GONE);
                linearRow2.setVisibility(View.GONE);
                linearRow3.setVisibility(View.VISIBLE);
                linearValidDate.setVisibility(View.GONE);
                div1.setVisibility(View.GONE);
                div2.setVisibility(View.GONE);
                div3.setVisibility(View.VISIBLE);

                summaryRequest = new SummaryRequest();

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!cb3.isChecked()) {
                            summaryRequest.setMatClass(Constants.EMPTY_STRING);
                        } else {
                            if (spn3.getSelectedItem() != null) {
                                summaryRequest.setMatClass(spn3.getSelectedItem().toString());
                            } else {
                                summaryRequest.setMatClass(Constants.EMPTY_STRING);
                            }
                        }

                        Helper.setItemParam(Constants.SUMMARY_ITEM_FILTER, summaryRequest);

                        onResume();

                        alertDialog.dismiss();
                    }
                });

                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        }
    }

    private void functionSpinnerForPromotion() {

        linearRow3.setVisibility(View.GONE);
        div3.setVisibility(View.GONE);
        linearValidDate.setVisibility(View.VISIBLE);

        txtRow1.setText("Outlet");
        txtRow2.setText("Product");

        cb1.setEnabled(true);
        cb2.setEnabled(true);
        cb1.setChecked(false);
        cb2.setChecked(false);

        if (Helper.getItemParam(Constants.PROMOTION_ITEM_FILTER) != null) {
            if (((PromotionFilter) Helper.getItemParam(Constants.PROMOTION_ITEM_FILTER)).getOutlet() != null) {
                cb1.setChecked(true);
            }
            if (((PromotionFilter) Helper.getItemParam(Constants.PROMOTION_ITEM_FILTER)).getProduct() != null) {
                cb2.setChecked(true);
            }
        }

        promotionFilterRequest = new PromotionFilter();

        addItemsOnDialogFilterPromotion();

        functionDatePromotion(edtValidFrom);
        functionDatePromotion(edtValidTo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cb1.isChecked()) {
                    promotionFilterRequest.setOutlet(null);
                }
                if (!cb2.isChecked()) {
                    promotionFilterRequest.setProduct(null);
                }

                Helper.setItemParam(Constants.PROMOTION_ITEM_FILTER, promotionFilterRequest);
                onResume();

                alertDialog.dismiss();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void addItemsOnDialogFilterPromotion() {

        ArrayAdapter<String> outletAdapter, divProdAdapter;

        List<String> outletList = new ArrayList<String>();
        ArrayList<OutletResponse> listOutletName = db.getAllOutletName();
        if (listOutletName != null && !listOutletName.isEmpty()) {
            for (int i = 0; i < listOutletName.size(); i++) {
                outletList.add(listOutletName.get(i).getIdOutlet().concat(" - ").concat(listOutletName.get(i).getOutletName()));
            }
        }

        outletAdapter = new ArrayAdapter<String>(this.getContext(),
                R.layout.spinner_item, outletList);
        outletAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn1.setAdapter(outletAdapter);

        if (Helper.getItemParam(Constants.PROMOTION_ITEM_FILTER) != null) {
            if (((PromotionFilter) Helper.getItemParam(Constants.PROMOTION_ITEM_FILTER)).getOutlet() != null) {
                for (String outlet : outletList) {
                    if (outlet.contains(((PromotionFilter) Helper.getItemParam(Constants.PROMOTION_ITEM_FILTER)).getOutlet())) {
                        int spinnerPosition = outletAdapter.getPosition(outlet);
                        spn1.setSelection(spinnerPosition);
                    }
                }
            }
        }

        List<String> divProdList = db.getAllClassification();


        divProdAdapter = new ArrayAdapter<String>(this.getContext(),
                R.layout.spinner_item, divProdList);
        divProdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn2.setAdapter(divProdAdapter);
        spn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                spinnerDivProd = true;
                return false;
            }
        });

        spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                promotionFilterRequest.setProduct(spn2.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        }

        spn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                spinnerOutlet = true;
                return false;
            }
        });

        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                promotionFilterRequest.setOutlet(spn1.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void addItemsOnDialogFilterSummary() {

        ArrayAdapter<String> divProdAdapter;

        divProdList = (List<String>) Helper.getItemParam(Constants.ITEM_DIV_PROD);
        if (divProdList != null) {
            divProdAdapter = new ArrayAdapter<>(this.getContext(),
                    R.layout.spinner_item, divProdList);
            divProdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn3.setAdapter(divProdAdapter);
        }

        if (Helper.getItemParam(Constants.TARGET_SUMMARY_REQUEST) != null) {
            TargetSummaryRequest savedFilter = (TargetSummaryRequest) Helper.getItemParam(Constants.TARGET_SUMMARY_REQUEST);

            if (savedFilter != null) {
                //set checkbox
                if (savedFilter.getMatClass() != null) {
                    cb3.setChecked(true);
                } else {
                    cb3.setChecked(false);
                }

                //set spinner

                if (divProdList != null) {
                    divProdAdapter = new ArrayAdapter<String>(this.getContext(),
                            R.layout.spinner_item, divProdList);
                    divProdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn3.setAdapter(divProdAdapter);

                    if (savedFilter.getMatClass() != null) {
                        int spinnerPosition = divProdAdapter.getPosition(savedFilter.getMatClass());
                        spn3.setSelection(spinnerPosition);
                    }
                }
            }

        } else {
            cb3.setChecked(true);
        }

        spn3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cb3.setChecked(true);
                return false;
            }
        });

    }

    private void functionDatePromotion(final EditText edt) {
        final Date[] date = new Date[1];
        edt.setInputType(InputType.TYPE_NULL);
        edt.requestFocus();
        edt.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        final Calendar tanggal = Calendar.getInstance();
                        String dateF;
                        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                                getContext(),
                                new DatePickerDialog.OnDateSetListener() {

                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,
                                                          int dayOfMonth) {

                                        tanggal.set(year, monthOfYear, dayOfMonth);
                                        dateString[0] = Helper
                                                .convertDateToStringNew(Constants.DATE_TYPE_1, tanggal.getTime());
                                        edt.setText(dateString[0]);
                                        date[0] = Helper.convertStringtoDate(Constants.DATE_TYPE_1, dateString[0]);
                                    }

                                }, tanggal.get(Calendar.YEAR),
                                tanggal.get(Calendar.MONTH),
                                tanggal.get(Calendar.DAY_OF_MONTH));

                        fromDatePickerDialog.getDatePicker().setCalendarViewShown(false);
                        if (edt == edtValidTo) {
                            fromDatePickerDialog.getDatePicker().setMinDate(Helper.convertDateToLong(edtValidFrom.getText().toString(), Constants.DATE_TYPE_1));
                        }
                        fromDatePickerDialog.show();
                        fromDatePickerDialog
                                .setButton(DialogInterface.BUTTON_POSITIVE, "Search", new DialogInterface
                                        .OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == DialogInterface.BUTTON_POSITIVE) {
                                            // Do Stuff
                                            DatePicker datePicker = fromDatePickerDialog.getDatePicker();
                                            String day = String.valueOf(datePicker.getDayOfMonth());
                                            String month = Helper.getMonthForInt(datePicker.getMonth());
                                            String year = String.valueOf(datePicker.getYear());
//                                            dayF = day + " " + month + " " + year;
                                            dayF = String.format("%02d", Integer.parseInt(day)).concat("/").concat(String.format("%02d", datePicker.getMonth() + 1)).concat("/").concat(year);

                                            edt.setText(dayF);

                                            if (edt == edtValidFrom) {
                                                if (Helper.getItemParam(Constants.ACTIVE_MENU).toString().equalsIgnoreCase(getResources().getString(R.string.navmenu4))) {
                                                    promotionFilterRequest.setValidFrom(dayF);
                                                } else if (Helper.getItemParam(Constants.ACTIVE_MENU).toString().equalsIgnoreCase(getResources().getString(R.string.navmenu6a))) {
                                                    summaryRequest.setDateFrom(Helper.changeDateFormat(Constants.DATE_TYPE_15, Constants.DATE_TYPE_2, dayF));
                                                }
                                                edtValidTo.setEnabled(true);
                                            } else if (edt == edtValidTo) {
                                                if (Helper.getItemParam(Constants.ACTIVE_MENU).toString().equalsIgnoreCase(getResources().getString(R.string.navmenu4))) {
                                                    promotionFilterRequest.setValidTo(dayF);
                                                } else if (Helper.getItemParam(Constants.ACTIVE_MENU).toString().equalsIgnoreCase(getResources().getString(R.string.navmenu6a))) {
                                                    summaryRequest.setDateTo(Helper.changeDateFormat(Constants.DATE_TYPE_15, Constants.DATE_TYPE_2, dayF));
                                                }
                                            }
                                        }
                                    }
                                });
                        break;
                }
                return true;
            }
        });
    }

    private void functionSpinnerForTarget() {

        cb1.setEnabled(false);
        cb2.setEnabled(false);
        cb1.setChecked(true);
        cb2.setChecked(true);

        if (Helper.getItemParam(Constants.TARGET_TYPE) != null) {
            if (Helper.getItemParam(Constants.TARGET_TYPE).equals(Constants.OBJECT_TARGET)) {
                if (Helper.getItemParam(Constants.SELECTED_TAB) != null) {
                    if (!Helper.getItemParam(Constants.SELECTED_TAB).equals("0")) {
                        linearRow1.setVisibility(View.GONE);
                        div1.setVisibility(View.GONE);
                        linearRow3.setVisibility(View.VISIBLE);
                        div3.setVisibility(View.VISIBLE);
                    } else {
                        linearRow1.setVisibility(View.GONE);
                        linearRow3.setVisibility(View.GONE);
                        div3.setVisibility(View.GONE);
                    }
                }
            } else {
                if (Helper.getItemParam(Constants.SELECTED_TAB) != null) {
//                    if (Helper.getItemParam(Constants.SELECTED_TAB).equals(getString(R.string.customer))) {
                    if ((int) Helper.getItemParam(Constants.SELECTED_TAB) == 1) {
                        linearRow2.setVisibility(View.VISIBLE);
                        div2.setVisibility(View.VISIBLE);
                    } else {
                        linearRow2.setVisibility(View.GONE);
                        div2.setVisibility(View.GONE);
                    }
                }
            }
        }

        addItemsOnDialogFilter();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TargetSummaryRequest targetSummaryRequest = new TargetSummaryRequest();
                if (cb1.isChecked()) {
                    targetSummaryRequest.setPeriod(spn1.getSelectedItem().toString());
                }

                if (cb2.isChecked()) {
                    if (spn2.getSelectedItem() != null) {
                        if (spn2.getSelectedItem().toString().equals(getString(R.string.allCaps))) {
                            targetSummaryRequest.setIdOutlet(Constants.EMPTY_STRING);
                        } else {
                            targetSummaryRequest.setIdOutlet(idOutletList.get(spn2.getSelectedItemPosition() - 1));
                        }
                    } else {
                        targetSummaryRequest.setIdOutlet(Constants.EMPTY_STRING);
                    }
                } else {
                    targetSummaryRequest.setMatClass(Constants.EMPTY_STRING);
                }

                if (cb3.isChecked()) {
                    if (spn3.getSelectedItem() != null) {
                        if (spn3.getSelectedItem().toString().equals(getString(R.string.allCaps))) {
                            targetSummaryRequest.setMatClass(Constants.EMPTY_STRING);
                        } else {
                            targetSummaryRequest.setMatClass(spn3.getSelectedItem().toString());
                        }
                    } else {
                        targetSummaryRequest.setMatClass(Constants.EMPTY_STRING);
                    }
                } else {
                    targetSummaryRequest.setMatClass(Constants.EMPTY_STRING);
                }

                Helper.setItemParam(Constants.TARGET_SUMMARY_REQUEST, targetSummaryRequest);

                onResume();

                alertDialog.dismiss();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void addItemsOnDialogFilter() {
        List<String> periodList = new ArrayList<String>();
        periodList.add(Constants.MONTH);
        periodList.add(Constants.DAY);
        monthAdapter = new ArrayAdapter<String>(this.getContext(),
                R.layout.spinner_item, periodList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn1.setAdapter(monthAdapter);

        List<String> outletList = (List<String>) Helper.getItemParam(Constants.ITEM_CUSTOMER_NAME);
        idOutletList = (List<String>) Helper.getItemParam(Constants.ITEM_CUSTOMER_ID);
        if (outletList != null) {
            outletAdapter = new ArrayAdapter<String>(this.getContext(),
                    R.layout.spinner_item, outletList);
            outletAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn2.setAdapter(outletAdapter);
        }

        List<String> divProdList = (List<String>) Helper.getItemParam(Constants.ITEM_DIV_PROD);
        if (divProdList != null) {
            divProdAdapter = new ArrayAdapter<String>(this.getContext(),
                    R.layout.spinner_item, divProdList);
            divProdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn3.setAdapter(divProdAdapter);
        }

        if (Helper.getItemParam(Constants.TARGET_SUMMARY_REQUEST) != null) {
            TargetSummaryRequest savedFilter = (TargetSummaryRequest) Helper.getItemParam(Constants.TARGET_SUMMARY_REQUEST);

            if (savedFilter != null) {
                //set checkbox
                cb1.setChecked(true);
                cb2.setChecked(true);
                if (savedFilter.getMatClass() != null) {
                    cb3.setChecked(true);
                } else {
                    cb3.setChecked(false);
                }

                //set spinner
                if (savedFilter.getPeriod() != null) {
                    int spinnerPosition = monthAdapter.getPosition(savedFilter.getPeriod());
                    spn1.setSelection(spinnerPosition);
                }

                if (outletList != null) {
                    outletAdapter = new ArrayAdapter<>(this.getContext(),
                            R.layout.spinner_item, outletList);
                    outletAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn2.setAdapter(outletAdapter);

                    if (savedFilter.getIdOutlet() != null) {
                        if (!idOutletList.isEmpty()) {
                            for (int i = 0; i < idOutletList.size(); i++) {
                                if (savedFilter.getIdOutlet().contains(idOutletList.get(i))) {
                                    int spinnerPosition = outletAdapter.getPosition(outletList.get(i + 1));
                                    spn2.setSelection(spinnerPosition);
                                }
                            }
                        }
                    }
                }

                if (divProdList != null) {
                    divProdAdapter = new ArrayAdapter<>(this.getContext(),
                            R.layout.spinner_item, divProdList);
                    divProdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn3.setAdapter(divProdAdapter);

                    if (savedFilter.getMatClass() != null) {
                        int spinnerPosition = divProdAdapter.getPosition(savedFilter.getMatClass());
                        spn3.setSelection(spinnerPosition);
                    }
                }
            }

        } else {
            cb1.setChecked(true);
            cb2.setChecked(true);
            cb3.setChecked(true);
        }

        spn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cb1.setChecked(true);
                return false;
            }
        });

        spn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cb2.setChecked(true);
                return false;
            }
        });

        spn3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cb3.setChecked(true);
                return false;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    public void setContent(Fragment fragment) {
        final Activity activity = getActivity();
        //Close keyBoard in transition
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_contentframe, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String destinationImage = destination.getAbsolutePath();
        bitmapCamera = thumbnail;

        if (PARAM_PHOTO == 1) {
            txtImagePath.setText(destinationImage);
            imgAttachmentReturn.setImageBitmap(bitmapCamera);
        } else if (PARAM_PHOTO == 2) {
            txtImagePathCost.setText(destinationImage);
            imgAttachedCost.setImageBitmap(bitmapCamera);
        } else if (PARAM_PHOTO == 3) {
            txtImagePathAttachment.setText(destinationImage);
            imgAttachment.setImageBitmap(bitmapCamera);
        }
        bitmapPhoto = bitmapCamera;
    }

    private void onSelectFromGalleryResult(Intent data) {
        final InputStream imageStream;
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(selectedImage));
        if (type == null) {
            type = MimeTypeMap.getFileExtensionFromUrl(selectedImage.toString());
        }
        if (type.equals("jpeg") || type.equals("png") || type.equals("jpg")) {
            Cursor cursor = cR.query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String imagePath = picturePath;


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            bitmapGallery = BitmapFactory.decodeFile(picturePath, options);

            try {
                imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmapGallery = BitmapFactory.decodeStream(imageStream, null, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmapGallery.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (PARAM_PHOTO == 1) {
                txtImagePath.setText(imagePath);
                imgAttachmentReturn.setImageBitmap(bitmapGallery);
            } else if (PARAM_PHOTO == 2) {
                txtImagePathCost.setText(imagePath);
                imgAttachedCost.setImageBitmap(bitmapGallery);
            } else if (PARAM_PHOTO == 3) {
                txtImagePathAttachment.setText(imagePath);
                imgAttachment.setImageBitmap(bitmapGallery);
            }
            bitmapPhoto = bitmapGallery;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(Constants.ERROR_ATTACH);
            builder.setNegativeButton("OK", null);
            builder.show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public interface OnRecyclerViewItemClickListener {

        void onRecyclerViewItemClicked(int position, int id);
    }

    public String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String currentDateTime = df.format(new Date());

        return currentDateTime;
    }

    public String getCurrentDateTo(String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        String currentDateTime = df.format(new Date());

        return currentDateTime;
    }

    public void setCustomSpinner(String[] items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView text = v.findViewById(R.id.text1);
                    text.setText("");
                    text.setHint(getItem(getCount()));//"Hint to be displayed"
                    text.setTextColor(getResources().getColor(R.color.grey));
                }

                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.grey));
                return view;

            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };
        //dasdas
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (items != null) {
            spinnerAdapter.addAll(items);
        }
        if (spinnerAdapter != null) {
            if (spinnerAdapter.getCount() > 0) {
                spinner.setAdapter(spinnerAdapter);
                spinner.setSelection(spinnerAdapter.getCount());
            }
        }
    }

    public void setCustomSpinner2(String[] items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    TextView text = v.findViewById(R.id.text1);
                    text.setText("");
//                    text.setHint(getItem(getCount()));//"Hint to be displayed"
                    text.setTextColor(getResources().getColor(R.color.grey));
                }

                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.grey));
                return view;

            }

            @Override
            public int getCount() {
                return super.getCount(); // you dont display last item. It is used as hint.
            }

        };
        //dasdas
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (items != null) {
            spinnerAdapter.addAll(items);
        }
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinnerAdapter.getCount());
    }

    public void setSpinner(String[] items, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.grey));
                return view;

            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(items);
        spinner.setAdapter(adapter);
    }

    public void setSpinnerAdapter(String[] items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_adapter) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.white));
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        spinner.setAdapter(spinnerAdapter);
    }

    public void setSpinnerAdapter2(String[] items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.grey));
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        spinner.setAdapter(spinnerAdapter);
    }

    public void setAutoCompleteAdapter(String[] items, AutoCompleteTextView spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        spinner.setAdapter(spinnerAdapter);
    }

    public void setSpinnerAdapter3(String[] items, AutoCompleteTextView spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        spinner.setAdapter(spinnerAdapter);
    }

    public void setSpinnerAdapter3(String[] items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setSpinnerAdapter3(List<String> items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        spinner.setAdapter(spinnerAdapter);
    }

    public void notifySpinnerAdapterDataChanged() {
        spinnerAdapter.notifyDataSetChanged();
    }

    public void underlineTextview(TextView textView, String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }

    public void snackBar(View rootView, int message) {
        Snackbar snack = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private class RequestUrl extends AsyncTask<Void, Void, MessageResponse> {

        @Override
        protected MessageResponse doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    String URL_VISIT_CHECK_IN = Constants.API_VISIT_CHECK_IN;

                    final String url = Constants.URL.concat(URL_VISIT_CHECK_IN);

                    checkInRequest = (CheckInOutRequest) Helper.getItemParam(Constants.CHECK_IN_REQUEST);

                    return (MessageResponse) Helper
                            .postWebserviceWithBody(url, MessageResponse.class, checkInRequest);
                } else if (PARAM == 2) {
                    String URL_VISIT_FINISH = Constants.API_VISIT_FINISH;

                    final String url = Constants.URL.concat(URL_VISIT_FINISH);

                    messageResponseFinish = (MessageResponse) Helper
                            .postWebserviceWithBody(url, MessageResponse.class, checkOutRequest);
                    return null;
                } else if (PARAM == 3) {
                    String URL_VISIT_PAUSE = Constants.API_VISIT_PAUSE;

                    final String url = Constants.URL.concat(URL_VISIT_PAUSE);

                    messageResponsePause = (MessageResponse) Helper
                            .postWebserviceWithBody(url, MessageResponse.class, pauseRequest);
                    return null;
                } else if (PARAM == 4) {
                    String URL_GET = Constants.API_SAVE_ORDER_VISIT;

                    final String url = Constants.URL.concat(URL_GET);

                    VisitOrderHeader header = new VisitOrderHeader();
                    if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                        header = (VisitOrderHeader) Helper.getItemParam(Constants.VISIT_ORDER_HEADER);
                    }
                    user = (User) Helper.getItemParam(Constants.USER_DETAIL);
                    if (user.getReason() == 0) {
                        header.setIdEmployee("KT");
                    }
                    header.setOrderType(Helper.getItemParam(Constants.ORDER_TYPE).toString());
                    List<VisitOrderDetailResponse> visitOrderDetailResponseList = new ArrayList<>();
                    if (Helper.getItemParam(Constants.VISIT_ORDER_HEADER) != null) {
                        visitOrderDetailResponseList = (List<VisitOrderDetailResponse>) Helper
                                .getItemParam(Constants.VISIT_ORDER_DETAIL_SAVE);
                    }

                    VisitOrderHeader savedHeader = new VisitOrderHeader();
                    savedHeader = (VisitOrderHeader) Helper.getItemParam(Constants.ORDER_HEADER_SELECTED);

                    if (savedHeader != null) {
                        header.setId(savedHeader.getId());
                    }

                    ArrayList<ToPrice> listOneTimeDiscount = (ArrayList<ToPrice>) Helper.getItemParam(Constants.ALL_ONE_TIME_DISCOUNT_DETAIL);

                    VisitOrderRequest visitOrderRequest = new VisitOrderRequest();
                    visitOrderRequest.setOrderDetail(visitOrderDetailResponseList);
                    visitOrderRequest.setOrderHeader(header);
                    visitOrderRequest.setListOneTimeDiscount(listOneTimeDiscount);

                    messageResponseSave = (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, visitOrderRequest);
                    return null;
                } else if (PARAM == 5) {
                    String URL_GET_CURRENT_TIME = Constants.API_GET_OFFLINE_LOGIN.
                            concat(Constants.QUESTION).concat(Constants.VERSION).concat(Constants.EQUALS).concat(Constants.CURRENT_VERSION);

                    final String url = Constants.URL.concat(URL_GET_CURRENT_TIME);

                    offlineData = (OfflineLoginData) Helper.getWebservice(url, OfflineLoginData.class);
                    return null;
                } else if (PARAM == 6) {
                    String URL_SYNC_MASTER_DATA = Constants.API_SYNC_MASTER_DATA;

                    final String url = Constants.URL.concat(URL_SYNC_MASTER_DATA);
                    OfflineLoginData offlineData = new OfflineLoginData();
                    offlineData.setIdSalesman(user.getIdEmployee());

                    offlineData = (OfflineLoginData) Helper.getWebservice(url, OfflineLoginData.class);
                    return null;
                } else if (PARAM == 7) {
                    //OrderPlan
                    listOrderPlan = db.getListOrderPlanHeader();

                    if (listOrderPlan != null && !listOrderPlan.isEmpty()) {
                        listUpdateOrderPlan = new ArrayList<>();
                        listInsertOrderPlan = new ArrayList<>();
                        listDeleteOrderPlan = new ArrayList<>();

                        if (listOrderPlan != null && !listOrderPlan.isEmpty()) {
                            for (OrderPlanHeader orderPlan : listOrderPlan) {
                                if (user != null) {
                                    if (user.getIdEmployee() != null) {
                                        orderPlan.setIdSalesman(user.getIdEmployee());
                                    }
                                }
                                if (orderPlan.getQty2() != null) {
                                    if (orderPlan.getQty2().equals("0") || orderPlan.getQty2().equals("0.0")) {
                                        orderPlan.setQty2(null);
                                        orderPlan.setUom2(null);
                                    }
                                } else {
                                    orderPlan.setUom2(null);
                                }

                                if (orderPlan.getId() != null) {
                                    if (orderPlan.getId().contains(Constants.ID_TRULY_OP)) {
                                        if (orderPlan.getDeleted() != null) {
                                            if (orderPlan.getDeleted().equals(getResources().getString(R.string.STATUS_TRUE))) {
                                                listDeleteOrderPlan.add(orderPlan);
                                            } else {
                                                listUpdateOrderPlan.add(orderPlan);
                                            }
                                        } else {
                                            listUpdateOrderPlan.add(orderPlan);
                                        }
                                    } else {
                                        listInsertOrderPlan.add(orderPlan);
                                    }
                                }

                            }
                        }
                    }

                    offlineData.setListSaveOrderPlan(listInsertOrderPlan);//1
                    offlineData.setListUpdateOrderPlan(listUpdateOrderPlan);
                    offlineData.setListDeleteOrderPlan(listDeleteOrderPlan);

                    msg = (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, offlineData);
                    return null;
                } else if (PARAM == 8) {
                    if (user != null) {
                        if (user.getCurrentDate() != null) {
                            listVisitPlan = db.getAllVisitPlan(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));

                            listSaveNewVisit = new ArrayList<>();
                            if (listVisitPlan != null && !listVisitPlan.isEmpty()) {
                                for (int i = 0; i < listVisitPlan.size(); i++) {
                                    /*New Visit*/
                                    if (listVisitPlan.get(i).getId().contains(Constants.ID_VP_MOBILE)) {
                                        listSaveNewVisit.add(listVisitPlan.get(i));
                                    }
                                }
                            }
                        }
                    }

                    if (listVisitPlan != null && !listVisitPlan.isEmpty()) {
                        listUpdateVisitSalesman = new ArrayList<>();
                        listSaveVisitSalesman = new ArrayList<>();
                        for (int i = 0; i < listVisitPlan.size(); i++) {
                            if (listVisitPlan.get(i).getCheckInTime() != null || listVisitPlan.get(i).getPause_time() != null ||
                                    listVisitPlan.get(i).getContinue_time() != null || listVisitPlan.get(i).getCheck_out_time() != null) {

                                if (listVisitPlan.get(i).getCheckInTime() != null) {
                                    listVisitPlan.get(i).setCheckInString(CalendarUtils.ConvertMilliSecondsToFormattedDate(listVisitPlan.get(i).getCheckInTime()));
                                }
                                if (listVisitPlan.get(i).getPause_time() != null) {
                                    listVisitPlan.get(i).setPauseString(CalendarUtils.ConvertMilliSecondsToFormattedDate(listVisitPlan.get(i).getPause_time()));
                                }
                                if (listVisitPlan.get(i).getContinue_time() != null) {
                                    listVisitPlan.get(i).setContinueString(CalendarUtils.ConvertMilliSecondsToFormattedDate(listVisitPlan.get(i).getContinue_time()));
                                }
                                if (listVisitPlan.get(i).getCheck_out_time() != null) {
                                    listVisitPlan.get(i).setCheckOutString(CalendarUtils.ConvertMilliSecondsToFormattedDate(listVisitPlan.get(i).getCheck_out_time()));
                                }

                                if (listVisitPlan.get(i).getCheckInTime() != null) {
                                    if (listVisitPlan.get(i).getIdVisitSalesman() != null) {
                                        listUpdateVisitSalesman.add(listVisitPlan.get(i));
                                    } else {
                                        listSaveVisitSalesman.add(listVisitPlan.get(i));
                                    }
                                }

                            }

                        }
                    }

                    listDeleteNewVisit = new ArrayList<OutletResponse>();
                    if (Helper.getItemParam(Constants.LIST_DELETE_NEW_VISIT) != null) {
                        listDeleteNewVisit = (ArrayList<OutletResponse>) Helper.getItemParam(Constants.LIST_DELETE_NEW_VISIT);
                        Helper.removeItemParam(Constants.LIST_DELETE_NEW_VISIT);
                    }


                    offlineData.setListSaveNewVisit(listSaveNewVisit);//2
                    offlineData.setListDeleteNewVisit(listDeleteNewVisit);
                    offlineData.setListSaveVisitSalesman(listSaveVisitSalesman);//3
                    offlineData.setListUpdateVisitSalesman(listUpdateVisitSalesman);

                    msg = (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, offlineData);
                    return null;
                }
                return null;
            } catch (Exception ex) {
                //connection = true;
                if (ex.getMessage() != null) {
                    Log.e("UpdateStat", ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(MessageResponse msg) {
            if (PARAM == 1) {
                if (msg != null) {
                    if (msg.getIdMessage() == 0) {
                        Toast.makeText(getContext(), msg.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), msg.getMessage(), Toast.LENGTH_SHORT).show();

                        checkInRequest = (CheckInOutRequest) Helper.getItemParam(Constants.CHECK_IN_REQUEST);

                        outletResponse = new OutletResponse();
                        outletResponse.setIdOutlet(checkInRequest.getIdOutlet());
                        outletResponse.setStatusCheckIn("true");

                        db.updateStatusCheckIn(outletResponse);
                        ((MainActivity) getActivity()).changePage(8);
//                        fragment = new TimerFragment();
//                        setContent(fragment);
                    }
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            } else if (PARAM == 2) {//finish
                if (messageResponseFinish != null) {
                    if (messageResponseFinish.getIdMessage() == 0) {
                        Toast.makeText(getContext(), messageResponseFinish.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getContext(), messageResponseFinish.getMessage(), Toast.LENGTH_SHORT)
                                .show();

                        TimerFragment.getTimerValue().stop();
                        TimerFragment.getTimerValue().setText(Constants.DEFAULT_TIMER);

                        Helper.lastTimePause = TimerFragment.getTimerValue().getText().toString();
                        Helper.tempMinute = Integer.parseInt(Helper.mm);
                        Helper.tempSecond = Integer.parseInt(Helper.ss);
                        Helper.valTime = 1;
                        Helper.resume = true;

                        Helper.removeItemParam(Constants.CHECK_IN);
                        ((MainActivity) getActivity()).changePage(3);
//                        fragment = new RetailOutletFragment();
//                        setContent(fragment);
                    }
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.serverError),
                            Toast.LENGTH_SHORT).show();
                }
            } else if (PARAM == 3) {//PAUSE
                if (messageResponsePause != null) {
                    if (messageResponsePause.getIdMessage() == 0) {
                        Toast.makeText(getContext(), messageResponsePause.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getContext(), messageResponsePause.getMessage(), Toast.LENGTH_SHORT)
                                .show();

                        if (Helper.getItemParam(Constants.PAUSE) != null) {
                            Helper.setItemParam(Constants.PLAY, "1");
                            Helper.removeItemParam(Constants.PAUSE);
                        } else {
                            Helper.setItemParam(Constants.PAUSE, "1");
                            Helper.removeItemParam(Constants.PLAY);
                        }

                        TimerFragment.setPlayPause();
                        Helper.removeItemParam(Constants.CHECK_IN);
                        ((MainActivity) getActivity()).changePage(3);
//                        fragment = new RetailOutletFragment();
//                        setContent(fragment);
                    }
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.serverError),
                            Toast.LENGTH_SHORT).show();
                }
            } else if (PARAM == 4) {
                progress.dismiss();
                if (messageResponseSave != null) {
                    if (messageResponseSave.getIdMessage() == 1) {
                        Toast.makeText(getContext(), messageResponseSave.getMessage(), Toast.LENGTH_LONG).show();
                        ((MainActivity) getActivity()).changePage(8);
//                        fragment = new TimerFragment();
//                        setContent(fragment);
                    } else {
                        Toast.makeText(getContext(), messageResponseSave.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            } else if (PARAM == 5) {
                if (offlineData != null) {
                    if (offlineData.getDatetimeNow() != null) {
                        SecureDate.getInstance().initServerDate(Helper.convertStringtoDate(Constants.DATE_TYPE_16, offlineData.getDatetimeNow()));
                        curDate = SecureDate.getInstance().getDate();
                    }
                } else {
                    Toast.makeText(getContext(), "Please connect to internet", Toast.LENGTH_SHORT);
                }
            } else if (PARAM == 8) {
                if (msg != null) {

                }
            }
        }
    }

    public String getEncodedImage(Bitmap bm) {
        String encodedImage = "";
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        }
        return encodedImage;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setSpinnerAdapterWhite(List<String> items, Spinner spinner) {
        spinnerAdapterOrder = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_adapter) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.white));
                return view;
            }

        };

        spinnerAdapterOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapterOrder.addAll(items);
        spinner.setAdapter(spinnerAdapterOrder);
    }

    public void setAutoCompleteAdapter(List<String> items, AutoCompleteTextView spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
//                text.setTextSize(7);
//                text.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        spinner.setAdapter(spinnerAdapter);
    }

    public void setSpinnerGrey(List<String> items, Spinner spinner) {
        spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                text.setTextColor(getResources().getColor(R.color.grey));
                return view;
            }

        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.addAll(items);
        if (spinnerAdapter != null) {
            spinner.setAdapter(spinnerAdapter);
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public void setContentToHome() {
        ((MainActivity) getActivity()).changePage(1);
//        fragment = new CalendarFragment();
//        setContent(fragment);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class LoadMaterial extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                ArrayList<MaterialResponse> materialResponseList = (ArrayList<MaterialResponse>) Helper.getItemParam(Constants.STORE_CHECK_DETAIL);
                ArrayList<Return> returnList = (ArrayList<Return>) Helper.getItemParam(Constants.RETURN_DETAIL);
                List<VisitOrderDetailResponse> visitOrderDetailResponseList = (List<VisitOrderDetailResponse>) Helper.getItemParam(Constants.VISIT_ORDER_DETAIL);

                go = new int[]{0};

                listMaterialName = new ArrayList<>();
                listMaterialCode = new ArrayList<>();
                ArrayList<Material> listMaterialNew = new ArrayList<>();

                if (visitOrderDetailResponseList != null) {

                    listMaterialNew = db.getMasterMaterialNameCodeForOrder();
                    for (Material data : listMaterialNew) {
                        listMaterialName.add(data.getMaterialCode());
//                        listMaterialCode.add(data.getMaterialid());
                    }

                    if (!listMaterialName.isEmpty() && !listMaterialCode.isEmpty()) {
                        for (VisitOrderDetailResponse order : visitOrderDetailResponseList) {
                            for (int j = 0; j < listMaterialName.size(); j++) {
                                if (order.getMaterialName()
                                        .equals(listMaterialName.get(j))) {
                                    listMaterialName.remove(j);
                                }
                            }

                            for (int i = 0; i < listMaterialCode.size(); i++) {
                                if (order.getIdMaterial().equals(listMaterialCode.get(i))) {
                                    listMaterialCode.remove(i);
                                }
                            }
                        }
                    }
                } else if (materialResponseList != null) {
                    listMaterialNew = db.getMasterMaterialNameCodeForOrder();
                    for (Material data : listMaterialNew) {
                        listMaterialName.add(data.getMaterialCode());
//                        listMaterialCode.add(data.getMaterialid());
                    }

                    if (!listMaterialName.isEmpty() && !listMaterialCode.isEmpty()) {
                        for (MaterialResponse material : materialResponseList) {
                            for (int j = 0; j < listMaterialName.size(); j++) {
                                if (material.getMaterialName().equals(listMaterialName.get(j))) {
                                    listMaterialName.remove(j);
                                }
                            }

                            for (int i = 0; i < listMaterialCode.size(); i++) {
                                if (material.getIdMaterial().equals(listMaterialCode.get(i))) {
                                    listMaterialCode.remove(i);
                                }
                            }
                        }
                    }
                } else {
                    listMaterialNew = db.getMasterMaterialNameCodeForOrder();
                    for (Material data : listMaterialNew) {
                        listMaterialName.add(data.getMaterialCode());
//                        listMaterialCode.add(data.getMaterialid());
                    }
                }

                adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listMaterialName);
                codeAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listMaterialCode);

            } catch (Exception ex) {
                return null;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*32019 beta update*/
            initProgress();
            progress.show();

            /**/
        }

        @Override
        protected void onPostExecute(Boolean msg) {

            progress.dismiss();
            if (msg != null) {
                if (msg) {

                    EdtMaterialListener edtMatListener = new EdtMaterialListener();
                    EdtMaterialCodeListener edtMatCodeListener = new EdtMaterialCodeListener();

                    EdtMatNameWatcher edtMatNameWatcher = new EdtMatNameWatcher();
                    EdtMatCodeWatcher edtMatCodeWatcher = new EdtMatCodeWatcher();

                    edtMaterialName.setOnItemClickListener(edtMatListener);
                    edtMaterialCode.setOnItemClickListener(edtMatCodeListener);

                    edtMaterialName.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            flagName = 1;
                            flagCode = 0;
                            return false;
                        }
                    });

                    edtMaterialCode.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            flagCode = 1;
                            flagName = 0;
                            return false;
                        }
                    });
                    edtMaterialName.addTextChangedListener(edtMatNameWatcher);
                    edtMaterialCode.addTextChangedListener(edtMatCodeWatcher);

                    alertDialog.show();
                }
            }

        }
    }

    private class EdtMaterialListener implements AdapterView.OnItemClickListener {
        MaterialResponse materialResponse;

        EdtMaterialListener() {
            edtMaterialName.setAdapter(adapter);
            edtMaterialName.setThreshold(1);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
            String selection = (String) parent.getItemAtPosition(position);
            try {
                materialResponse = db.getMasterMaterialByName(selection);
                edtMaterialCode.setText(materialResponse.getIdMaterial());
                edtKlasifikasi.setText(materialResponse.getIdMaterialClass());
            } catch (SQLiteException e) {

            }
        }
    }

    private class EdtMaterialCodeListener implements AdapterView.OnItemClickListener {
        MaterialResponse materialResponse;

        EdtMaterialCodeListener() {
            edtMaterialCode.setAdapter(codeAdapter);
            edtMaterialCode.setThreshold(1);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
            String selection = (String) parent.getItemAtPosition(position);
            materialResponse = db.getMasterMaterialByCode(selection);
            edtMaterialName.setText(materialResponse.getMaterialName());
            edtKlasifikasi.setText(materialResponse.getIdMaterialClass());
        }
    }

    private class EdtMatNameWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (flagName == 1) {

                edtMaterialCode.setText(null);
                edtKlasifikasi.setText(null);

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private class EdtMatCodeWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (flagCode == 1) {
                edtMaterialName.setText(null);
                edtKlasifikasi.setText(null);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


    public void preventDupeDialog() {
        if (getAlertDialog() != null) {
            if (getAlertDialog().isShowing()) {
                return;
            }
        }
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected class MyValueFormatter extends IndexAxisValueFormatter {

        public MyValueFormatter() {
            otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator(',');
            format = new DecimalFormat("#,###,###,###,###,###.##", otherSymbols);
            format.setDecimalSeparatorAlwaysShown(false);
        }

        @Override
        public String getFormattedValue(float value) {
            // write your logic here
            return format.format(value); // e.g. append a dollar-sign
        }
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    protected void setToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

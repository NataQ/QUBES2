package id.co.qualitas.qubes.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.CalendarUtils;
import id.co.qualitas.qubes.helper.GPSTracker;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.CheckInOutRequest;
import id.co.qualitas.qubes.model.GPSModel;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitOrderHeader;

public class Timer2Fragment extends BaseFragment {
    private TextView clientName, desc, dateValue, txtOrder, txtReturn, txtStoreCheck;
    private LinearLayout llCreditInfo, llStoreCheck, llOrder, llReturn;

    private CheckInOutRequest resumeRequest;
    private CheckInOutRequest checkInOutRequest;
    private User user;

    static int h;
    static int m;
    static int s;
    @SuppressLint("StaticFieldLeak")
    static Chronometer timerValue;

    public static Chronometer getTimerValue() {
        return timerValue;
    }

    private static ImageView btnPause;
    private static OutletResponse outletResponse;
    public String url, checkInTime, pauseTime, curTime, continueTime;
    public String idOutlet, outletName, idEmployee = Constants.EMPTY_STRING;

    private ImageView icReturn, icOrder, icStoreCheck;
    private ImageView action_pause, action_continue, action_finish;

    private ArrayList<VisitOrderHeader> listOrder;
    private String timeDuration;
    private int checkLocation = 0;
    private Date curDate = new Date();
    private Date dCheckIn = null, dCurrent = null, dResume = null;

    public MenuItem save;

    public MenuItem filter;
    public MenuItem search;
    public MenuItem edit;
    public MenuItem setting;
    public MenuItem next;
    public static MenuItem pause;
    public static MenuItem cont;
    public static MenuItem stop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_timer, container, false);

        initialize();
        init();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((MainActivity) getActivity()).changePage(3);
                        return true;
                    }
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changePage(3);
            }
        });

        setTodayDate();

        setHeaderData();

        llCreditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changePage(9);
            }
        });

        llStoreCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.removeItemParam(Constants.GET_DETAIL_VISIT);
                Helper.removeItemParam(Constants.STORE_CHECK_DETAIL);
                ((MainActivity) getActivity()).changePage(10);
//                fragment = new StockOpnameBfrFragment();
//                setContent(fragment);
            }
        });

        llOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.removeItemParam(Constants.CURRENTPAGE);
                ((MainActivity) getActivity()).changePage(11);
//                Intent intent = new Intent(Timer2Fragment.this, Order2Fragment.class);
//                startActivity(intent);
//                fragment = new OrderTypeFragment();
//                setContent(fragment);
            }
        });

        llReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.removeItemParam(Constants.CURRENTPAGE);
                Helper.removeItemParam(Constants.RETURN_DETAIL);
                ((MainActivity) getActivity()).changePage(17);
//                Intent intent = new Intent(Timer2Fragment.this, Return2Fragment.class);
//                startActivity(intent);
//                fragment = new ReturnSalesFragment();
//                setContent(fragment);
            }
        });

        action_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preventDupeDialog();
                getLocation(1);
            }
        });

        action_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preventDupeDialog();
                ArrayList<OutletResponse> outletResponseArrayList = (ArrayList<OutletResponse>) Helper.getItemParam(Constants.OUTLET_LIST_TODAY);
                if (outletResponseArrayList != null && !outletResponseArrayList.isEmpty()) {
                    int flag_checked_in = 0;
                    String outlet = Constants.EMPTY_STRING;

                    for (int i = 0; i < outletResponseArrayList.size(); i++) {
                        if (outletResponseArrayList.get(i).getStatusCheckIn() != null) {
                            if (outletResponseArrayList.get(i).getStatusCheckIn().equals(Constants.CHECK_IN)) {
                                if (!outletResponseArrayList.get(i).getIdOutlet().equals(outletResponse.getIdOutlet())) {
                                    flag_checked_in = 1;
                                    outlet = outletResponseArrayList.get(i).getOutletName();
                                }
                            }
                        }
                    }
                    if (flag_checked_in == 1) {
                        Toast.makeText(getActivity(), getString(R.string.string_notice_checked_in).concat(outlet), Toast.LENGTH_SHORT).show();
                        flag_checked_in = 0;
                    } else {
                        PARAM_STATUS_OUTLET = Constants.RESUME;

                        resumeRequest = new CheckInOutRequest();
                        resumeRequest.setIdOutlet(idOutlet);
                        resumeRequest.setIdEmployee(idEmployee);

                        OutletResponse outletResponse = new OutletResponse();
                        outletResponse.setIdOutlet(idOutlet);
                        outletResponse.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));

                        outletResponse.setContinue_time(String.valueOf(curDate.getTime()));
                        db.updateVisitPlan(outletResponse);

                        onResume();
                    }
                }
            }
        });

        action_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preventDupeDialog();
                getLocation(2);
            }
        });

        return rootView;
    }

    private void setTodayDate() {
        if (user != null) {
            if (user.getCurrentDate() != null) {
                dateValue.setText(Helper.validateResponseEmpty(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_12, user.getCurrentDate())));
            }
        }
    }

    private void setHeaderData() {
        clientName.setText(Helper.validateResponseEmpty(idOutlet).concat(" - ").concat(Helper.validateResponseEmpty(outletName)));

        desc.setText(Helper.validateResponseEmpty(outletResponse.getStreet1()));
    }

    public static void setPlayPause() {
        if (Helper.getItemParam(Constants.PAUSE) != null) {
            timerValue.stop();

            OutletResponse pause = new OutletResponse();
            pause.setIdOutlet(outletResponse.getIdOutlet());
            pause.setTimer(String.valueOf(timerValue.getBase()));

            btnPause.setImageDrawable(btnPause.getResources().getDrawable(R.drawable.ic_play));
        } else if (Helper.getItemParam(Constants.PLAY) != null) {
            if (Helper.valTime == 1) {
                Helper.tempTime = SystemClock.elapsedRealtime();
                Helper.valTime = 2;
            }

            formatResumeTime();
            timerValue.start();
            Helper.resume = false;
            btnPause.setImageDrawable(btnPause.getResources().getDrawable(R.drawable.pause));
        }
    }

    private void initialize() {
        db = new DatabaseHelper(getActivity());
        curDate = SecureDate.getInstance().getDate();

        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);
        if (outletResponse.getIdOutlet() != null && outletResponse.getVisitDate() != null) {
            timeDuration = db.getTimer(outletResponse.getIdOutlet(), outletResponse.getVisitDate());
            checkInOutRequest = db.getOutletTimeDetail(outletResponse.getIdOutlet(), outletResponse.getVisitDate());
            if (PARAM_STATUS_OUTLET.equals(Constants.PAUSE)) {
                checkInOutRequest.setContinueTime(null);
            }
        }
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        if (user != null) {
            idEmployee = user.getIdEmployee();
        }

        if (outletResponse != null) {
            if (outletResponse.getIdOutlet() != null) {
                idOutlet = outletResponse.getIdOutlet();

                listOrder = new ArrayList<>();
                listOrder = db.getListOrderHeader(idOutlet, Constants.ALL_TYPE);
            }

            if (outletResponse.getOutletName() != null) {
                outletName = outletResponse.getOutletName();
            }

        }

        imgBack = rootView.findViewById(R.id.imgBack);
        action_pause = rootView.findViewById(R.id.action_pause);
        action_continue = rootView.findViewById(R.id.action_continue);
        action_finish = rootView.findViewById(R.id.action_finish);

        txtOrder = rootView.findViewById(R.id.txtOrder);
        txtReturn = rootView.findViewById(R.id.txtReturn);
        dateValue = rootView.findViewById(R.id.dateValue);
        clientName = rootView.findViewById(R.id.namaClient);
        desc = rootView.findViewById(R.id.keterangan);
        llCreditInfo = rootView.findViewById(R.id.llCreditInfo);
        llStoreCheck = rootView.findViewById(R.id.llStoreCheck);
        llOrder = rootView.findViewById(R.id.llOrder);
        llReturn = rootView.findViewById(R.id.llReturn);
        timerValue = rootView.findViewById(R.id.timerValue);
        icReturn = rootView.findViewById(R.id.iconReturn);
        icOrder = rootView.findViewById(R.id.iconOrder);
        icStoreCheck = rootView.findViewById(R.id.iconStoreCheck);
        txtStoreCheck = rootView.findViewById(R.id.txtStoreCheck);
        btnPause = rootView.findViewById(R.id.btnPause);
    }

    private static void formatTime() {
        long time = SystemClock.elapsedRealtime() - Helper.tempTime;
        h = (int) (time / 3600000);
        m = (int) (time - h * 3600000) / 60000;
        s = (int) (time - h * 3600000 - m * 60000) / 1000;
        Helper.mm = m < 10 ? "0" + m : m + "";
        Helper.ss = s < 10 ? "0" + s : s + "";
    }

    private static void formatResumeTime() {
        timerValue.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChronometerTick(Chronometer cArg) {
                formatTime();
                int finalMinute = Integer.parseInt(Helper.mm) + Helper.tempMinute;
                int finalSecond = Integer.parseInt(Helper.ss) + Helper.tempSecond;

                if (finalSecond >= 60) {
                    finalMinute += 1;
                    finalSecond -= 60;
                }

                String fnMinute = finalMinute < 10 ? "0" + finalMinute : finalMinute + "";
                String fnSecond = finalSecond < 10 ? "0" + finalSecond : finalSecond + "";
                Helper.mm = fnMinute;
                Helper.ss = fnSecond;
                cArg.setText(fnMinute + ":" + fnSecond);
            }
        });
    }

    private void playTimerBy(long time) {
        timerValue.setBase(time);
        if (!PARAM_STATUS_OUTLET.equals(Constants.PAUSE)
                && !PARAM_STATUS_OUTLET.equals(Constants.FINISHED)) {
            timerValue.start();
        } else {
            timerValue.stop();
        }
    }

    public void getLocation(int check) {
//        check = 1 => pause
//        check = 2 => finish
        checkLocation = check;
        gpsTracker = new GPSTracker(getActivity());
        new Thread().execute();
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
                    gpsModel.setCountry(gpsTracker.getLocality(getActivity()));
                    gpsModel.setPostalCode(gpsTracker.getPostalCode(getActivity()));
                    gpsModel.setAddressLine(gpsTracker.getAddressLine(getActivity()));
                    Helper.setItemParam(Constants.GPS_DATA, gpsModel);
                } else {
                    progress.dismiss();
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    //spinner.setVisibility(View.VISIBLE);
//                    gpsTracker.showSettingsAlert();
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
            if (msg != null) {
                if (msg) {
                    checkInOutRequest = new CheckInOutRequest();
                    checkInOutRequest.setIdOutlet(outletResponse.getIdOutlet());
                    checkInOutRequest.setIdEmployee(user.getIdEmployee());
                    checkInOutRequest.setLatCheckIn(String.valueOf(gpsTracker.getLatitude()));
                    checkInOutRequest.setLatCheckOut(String.valueOf(gpsTracker.getLatitude()));
                    checkInOutRequest.setLongCheckIn(String.valueOf(gpsTracker.getLongitude()));
                    checkInOutRequest.setLongCheckOut(String.valueOf(gpsTracker.getLongitude()));
                    checkInOutRequest.setTimer(String.valueOf(SystemClock.elapsedRealtime() - timerValue.getBase()));
                    Helper.setItemParam(Constants.REQUEST, checkInOutRequest);

                    if (checkLocation == 1) {
                        openDialog(DIALOG_REASON_PAUSE);
                    } else {
                        if (listOrder != null && !listOrder.isEmpty()) {
                            PARAM_DIALOG_ALERT = 2;
                            openDialog(DIALOG_ALERT_CONFIRM);
                        } else {
                            openDialog(DIALOG_REASON);
                        }
                    }
                }
            } else {
                gpsTracker.showSettingsAlert();
            }
            progress.dismiss();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "8");
//        progress.dismiss();
        initialize();
        setButton();
        enableMenu();
        setTimerValue();
    }

    private void setButton() {
        switch (PARAM_STATUS_OUTLET) {
            case Constants.UNCHECKED_IN:
                action_pause.setVisibility(View.VISIBLE);
                action_continue.setVisibility(View.GONE);
                action_finish.setVisibility(View.VISIBLE);
                break;
            case Constants.CHECK_IN:
                action_pause.setVisibility(View.VISIBLE);
                action_continue.setVisibility(View.GONE);
                action_finish.setVisibility(View.VISIBLE);
                break;
            case Constants.PAUSE:
                action_pause.setVisibility(View.GONE);
                action_continue.setVisibility(View.VISIBLE);
                action_finish.setVisibility(View.VISIBLE);
                break;
            case Constants.RESUME:
                action_pause.setVisibility(View.VISIBLE);
                action_continue.setVisibility(View.GONE);
                action_finish.setVisibility(View.VISIBLE);
                break;
            case Constants.FINISHED:
                action_pause.setVisibility(View.GONE);
                action_continue.setVisibility(View.GONE);
                action_finish.setVisibility(View.GONE);
                break;
        }
    }

    private void enableMenu() {
        if (outletResponse.getIdOutlet() != null) {
            if (db.sizeStoreCheckBy(outletResponse.getIdOutlet()) > 0 ||
                    db.sizeOrderHeader(outletResponse.getIdOutlet()) > 0) {
                Helper.setItemParam(Constants.VIEW_STORE_CHECK, Constants.DONE_VIEW_STORE_CHECK);
            }
        }

        if (String.valueOf(Helper.getItemParam(Constants.VIEW_STORE_CHECK)).equals(Constants.DONE_VIEW_STORE_CHECK)) {
            Helper.removeItemParam(Constants.VIEW_STORE_CHECK);
            llOrder.setEnabled(true);
            llReturn.setEnabled(true);
            txtOrder.setTextColor(getResources().getColor(R.color.text_color));
            txtReturn.setTextColor(getResources().getColor(R.color.text_color));
        } else {
            llOrder.setEnabled(false);
            llReturn.setEnabled(false);
            txtOrder.setTextColor(getResources().getColor(R.color.off_grey));
            txtReturn.setTextColor(getResources().getColor(R.color.off_grey));
            icReturn.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey2), android.graphics.PorterDuff.Mode.SRC_IN);
            icOrder.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey2), android.graphics.PorterDuff.Mode.SRC_IN);
            icStoreCheck.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey2), android.graphics.PorterDuff.Mode.SRC_IN);
//            icReturn.setImageDrawable(getResources().getDrawable(R.drawable.returnabu1));
//            icOrder.setImageDrawable(getResources().getDrawable(R.drawable.orderabu1));
//            icStoreCheck.setImageDrawable(getResources().getDrawable(R.drawable.stockopnameabu1));
        }

        switch (PARAM_STATUS_OUTLET) {
            case Constants.PAUSE:
                llStoreCheck.setEnabled(false);
                llOrder.setEnabled(false);
                llReturn.setEnabled(false);
                txtOrder.setTextColor(getResources().getColor(R.color.off_grey));
                txtReturn.setTextColor(getResources().getColor(R.color.off_grey));
                icReturn.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey2), android.graphics.PorterDuff.Mode.SRC_IN);
                icOrder.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey2), android.graphics.PorterDuff.Mode.SRC_IN);
                icStoreCheck.setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey2), android.graphics.PorterDuff.Mode.SRC_IN);
//                icReturn.setImageDrawable(getResources().getDrawable(R.drawable.returnabu1));
//                icOrder.setImageDrawable(getResources().getDrawable(R.drawable.orderabu1));
//                icStoreCheck.setImageDrawable(getResources().getDrawable(R.drawable.stockopnameabu1));
                txtStoreCheck.setTextColor(getResources().getColor(R.color.off_grey));
                break;
            case Constants.FINISHED:
                llStoreCheck.setEnabled(true);
                llOrder.setEnabled(true);
                llReturn.setEnabled(true);
                txtOrder.setTextColor(getResources().getColor(R.color.text_color));
                txtReturn.setTextColor(getResources().getColor(R.color.text_color));
                txtStoreCheck.setTextColor(getResources().getColor(R.color.text_color));

                icReturn.setColorFilter(ContextCompat.getColor(getActivity(), R.color.new_pink), android.graphics.PorterDuff.Mode.SRC_IN);
                icOrder.setColorFilter(ContextCompat.getColor(getActivity(), R.color.new_pink), android.graphics.PorterDuff.Mode.SRC_IN);
                icStoreCheck.setColorFilter(ContextCompat.getColor(getActivity(), R.color.new_pink), android.graphics.PorterDuff.Mode.SRC_IN);

//                icReturn.setImageDrawable(getResources().getDrawable(R.drawable.returnabu));
//                icOrder.setImageDrawable(getResources().getDrawable(R.drawable.orderabu));
//                icStoreCheck.setImageDrawable(getResources().getDrawable(R.drawable.stockopnameabun));
                break;
            default:
                llStoreCheck.setEnabled(true);
                icStoreCheck.setColorFilter(ContextCompat.getColor(getActivity(), R.color.new_pink), android.graphics.PorterDuff.Mode.SRC_IN);
//                icStoreCheck.setImageDrawable(getResources().getDrawable(R.drawable.stockopnameabun));
                txtStoreCheck.setTextColor(getResources().getColor(R.color.text_color));
                if (String.valueOf(Helper.getItemParam(Constants.VIEW_STORE_CHECK)).equals(Constants.DONE_VIEW_STORE_CHECK)) {
                    llOrder.setEnabled(true);
                    llReturn.setEnabled(true);
                    txtOrder.setTextColor(getResources().getColor(R.color.text_color));
                    txtReturn.setTextColor(getResources().getColor(R.color.text_color));
                    txtStoreCheck.setTextColor(getResources().getColor(R.color.text_color));

                    icReturn.setColorFilter(ContextCompat.getColor(getActivity(), R.color.new_pink), android.graphics.PorterDuff.Mode.SRC_IN);
                    icOrder.setColorFilter(ContextCompat.getColor(getActivity(), R.color.new_pink), android.graphics.PorterDuff.Mode.SRC_IN);
//                    icReturn.setImageDrawable(getResources().getDrawable(R.drawable.returnabu));
//                    icOrder.setImageDrawable(getResources().getDrawable(R.drawable.orderabu));
                }
                break;
        }
    }

    private void setTimerValue() {
        /*Timer*/
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_TYPE_6);

        if (checkInOutRequest.getCheckInTime() != null) {
            try {
                String date = CalendarUtils.ConvertMilliSecondsToFormattedDate(checkInOutRequest.getCheckInTime());
                checkInTime = getTimeFromDate(date);
            } catch (Exception ignored) {

            }
        }

        if (curDate != null) {
            String currentDate = Helper.convertDateToString(Constants.DATE_TYPE_5, curDate);
            curTime = getTimeFromDate(currentDate);
        } else {
            curTime = null;
            dCurrent = null;
        }

        if (checkInOutRequest.getPauseTime() != null) {
            String pauseDate = CalendarUtils.ConvertMilliSecondsToFormattedDate(checkInOutRequest.getPauseTime());
            pauseTime = getTimeFromDate(pauseDate);
        } else {
            pauseTime = null;
        }
        if (checkInOutRequest.getContinueTime() != null) {
            String continueDate = CalendarUtils.ConvertMilliSecondsToFormattedDate(checkInOutRequest.getContinueTime());
            continueTime = getTimeFromDate(continueDate);
        } else {
            continueTime = null;
            dResume = null;
        }

        try {
            if (checkInTime != null) {
                dCheckIn = format.parse(checkInTime);
            }

            if (curTime != null) {
                dCurrent = format.parse(curTime);
            }
            if (continueTime != null) {
                dResume = format.parse(continueTime);
            }
        } catch (ParseException ignored) {
        }

        //in milliseconds
        long elapseTimeNow = dCheckIn != null ? dCurrent.getTime() - dCheckIn.getTime() : 0;

        switch (PARAM_STATUS_OUTLET) {
            case Constants.CHECK_IN:
                playTimerBy(SystemClock.elapsedRealtime() - elapseTimeNow);
                break;
            case Constants.PAUSE:
                if (timeDuration != null) {
                    try {
                        playTimerBy(SystemClock.elapsedRealtime()
                                - (Long.parseLong(timeDuration) + (dResume != null ? dCurrent.getTime() - dResume.getTime() : 0)));
                    } catch (NumberFormatException ignored) {

                    }

                }
                break;
            case Constants.RESUME:
                if (timeDuration != null) {
                    try {
                        playTimerBy(SystemClock.elapsedRealtime()
                                - (Long.parseLong(timeDuration) + (dResume != null ? dCurrent.getTime() - dResume.getTime() : 0)));
                    } catch (NumberFormatException ignored) {

                    }

                }
                break;
            case Constants.FINISHED:
                if (timeDuration != null) {
                    try {
                        playTimerBy(SystemClock.elapsedRealtime()
                                - Long.parseLong(timeDuration));
                    } catch (NumberFormatException ignored) {

                    }

                }
                break;
            default:
                timerValue.start();
                break;
        }

//        invalidateOptionsMenu();
    }

    private String getTimeFromDate(String date) {
        String[] part = date.split(Constants.SPACE);
        return part[1];
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Helper.removeItemParam(Constants.VIEW_STORE_CHECK);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        save = menu.findItem(R.id.action_save);
        filter = menu.findItem(R.id.action_filter);
        search = menu.findItem(R.id.action_search);
        edit = menu.findItem(R.id.action_edit);
        setting = menu.findItem(R.id.action_settings);
        next = menu.findItem(R.id.action_next);

        pause = menu.findItem(R.id.action_pause);
        cont = menu.findItem(R.id.action_continue);
        stop = menu.findItem(R.id.action_finish);

        switch (PARAM_STATUS_OUTLET) {
            case Constants.UNCHECKED_IN:
                pause.setVisible(true);
                cont.setVisible(false);
                stop.setVisible(true);
                break;
            case Constants.CHECK_IN:
                pause.setVisible(true);
                cont.setVisible(false);
                stop.setVisible(true);
                break;
            case Constants.PAUSE:
                pause.setVisible(false);
                cont.setVisible(true);
                stop.setVisible(true);
                break;
            case Constants.RESUME:
                pause.setVisible(true);
                cont.setVisible(false);
                stop.setVisible(true);
                break;
            case Constants.FINISHED:
                pause.setVisible(false);
                cont.setVisible(false);
                stop.setVisible(false);
                break;
        }

        pause.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                preventDupeDialog();
                getLocation(1);
                return false;
            }
        });

        cont.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                preventDupeDialog();
                ArrayList<OutletResponse> outletResponseArrayList = (ArrayList<OutletResponse>) Helper.getItemParam(Constants.OUTLET_LIST_TODAY);
                if (outletResponseArrayList != null && !outletResponseArrayList.isEmpty()) {
                    int flag_checked_in = 0;
                    String outlet = Constants.EMPTY_STRING;

                    for (int i = 0; i < outletResponseArrayList.size(); i++) {
                        if (outletResponseArrayList.get(i).getStatusCheckIn() != null) {
                            if (outletResponseArrayList.get(i).getStatusCheckIn().equals(Constants.CHECK_IN)) {
                                if (!outletResponseArrayList.get(i).getIdOutlet().equals(outletResponse.getIdOutlet())) {
                                    flag_checked_in = 1;
                                    outlet = outletResponseArrayList.get(i).getOutletName();
                                }
                            }
                        }
                    }
                    if (flag_checked_in == 1) {
                        Toast.makeText(getActivity(), getString(R.string.string_notice_checked_in).concat(outlet), Toast.LENGTH_SHORT).show();
                        flag_checked_in = 0;
                    } else {
                        PARAM_STATUS_OUTLET = Constants.RESUME;

                        resumeRequest = new CheckInOutRequest();
                        resumeRequest.setIdOutlet(idOutlet);
                        resumeRequest.setIdEmployee(idEmployee);

                        OutletResponse outletResponse = new OutletResponse();
                        outletResponse.setIdOutlet(idOutlet);
                        outletResponse.setVisitDate(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));

                        outletResponse.setContinue_time(String.valueOf(curDate.getTime()));
                        db.updateVisitPlan(outletResponse);

                        onResume();
                    }
                }

                return false;
            }
        });

        stop.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                preventDupeDialog();
                getLocation(2);
                return false;
            }
        });
    }
}




package id.co.qualitas.qubes.activity.aspp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoDctOutletAdapter;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoOutstandingFakturAdapter;
import id.co.qualitas.qubes.adapter.aspp.CustomerInfoPromoAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAdapter;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerReasonAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.TimerFragment;
import id.co.qualitas.qubes.helper.CalendarUtils;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class DailySalesmanActivity extends BaseActivity {
    private TextView txtOutlet, txtTypeOutlet, txtStatus;
    private TextView txtNamaPemilik, txtPhone, txtSisaKreditLimit, txtTotalTagihan, txtKTP, txtNPWP;
    private Button btnCheckOut;
    private LinearLayout llPause, llStoreCheck, llOrder, llCollection, llReturn;
    private RelativeLayout llKTP, llNPWP, llOutlet;
    private ImageView imgKTP, imgNPWP, imgOutlet, imgPause;
    private RecyclerView rvPromo, rvOutstandingFaktur, rvDCTOutlet;
    private CustomerInfoPromoAdapter promoAdapter;
    private CustomerInfoOutstandingFakturAdapter fakturAdapter;
    private CustomerInfoDctOutletAdapter dctOutletAdapter;
    private List<Material> fakturList;
    private List<Material> dctOutletList;
    private List<Promotion> promoList;

    static int h;
    static int m;
    static int s;
    @SuppressLint("StaticFieldLeak")
    static Chronometer timerValue;
    //how to save chronometer => checkInOutRequest.setTimer(String.valueOf(SystemClock.elapsedRealtime() - timerValue.getBase()));
    private Date curDate = new Date();
    private Date dCheckIn = null, dCurrent = null, dResume = null;
    public String checkInTime, pauseTime, curTime, continueTime, timeDuration;
    public boolean pause = false;
    private Customer outletHeader;

    public static Chronometer getTimerValue() {
        return timerValue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_daily_salesman);

        initialize();
        setData();
        setView();

        btnCheckOut.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(this);

            alertDialog.setContentView(R.layout.aspp_dialog_reason);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);

            List<String> groupList = new ArrayList<>();
            groupList.add("01 - Stock Masih Cukup");
            groupList.add("02 - Beli dari luar");
            groupList.add("03 - Tukar Faktur");
            groupList.add("04 - Toko Tutup");
            groupList.add("05 - Harga Tidak Cocok");

            txtTitle.setText("Reason Not Buy");

            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(this, groupList, (nameItem, adapterPosition) -> {
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
        });

        llPause.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(this);

            alertDialog.setContentView(R.layout.aspp_dialog_reason);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);

//            List<String> groupList = new ArrayList<>();
//            groupList.add("P1 - Toko Ramai");
//            groupList.add("P2 - Toko Tutup");
//            groupList.add("P3 - Toko Pindah");

            List<Reason> reasonList = new ArrayList<>();
            reasonList.addAll(database.getAllReason("Pause"));

            txtTitle.setText("Reason Pause");

            FilteredSpinnerReasonAdapter spinnerAdapter = new FilteredSpinnerReasonAdapter(this, reasonList, (nameItem, adapterPosition) -> {
//                spnBankTransfer.setText(nameItem);
                outletHeader.setStatus(Constants.PAUSE_VISIT);
                SessionManagerQubes.setOutletHeader(outletHeader);

//                if (Helper.getItemParam(Constants.PAUSE) != null) {
//                    Helper.setItemParam(Constants.PLAY, "1");
//                    Helper.removeItemParam(Constants.PAUSE);
//                } else {
//                    Helper.setItemParam(Constants.PAUSE, "1");
//                    Helper.removeItemParam(Constants.PLAY);
//                }
                if (pause) {
                    resumeTimer();
                } else {
                    pauseTimer();
                }
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
        });

        llStoreCheck.setOnClickListener(v -> {
            Intent intent = new Intent(this, StoreCheckActivity.class);
            startActivity(intent);
        });

        llOrder.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
        });

        llCollection.setOnClickListener(v -> {
            Intent intent = new Intent(this, CollectionVisitActivity.class);
            startActivity(intent);
        });

        llReturn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReturnAddActivity.class);
            startActivity(intent);
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(DailySalesmanActivity.this);
        });
    }

    private void setView() {
        fakturAdapter = new CustomerInfoOutstandingFakturAdapter(this, fakturList, header -> {
        });

        rvOutstandingFaktur.setAdapter(fakturAdapter);

        dctOutletAdapter = new CustomerInfoDctOutletAdapter(this, dctOutletList, header -> {
        });

        rvDCTOutlet.setAdapter(dctOutletAdapter);

        promoAdapter = new CustomerInfoPromoAdapter(this, promoList, header -> {
        });

        rvPromo.setAdapter(promoAdapter);
    }

    private void setData() {
        if (SessionManagerQubes.getOutletHeader() != null) {
            outletHeader = SessionManagerQubes.getOutletHeader();
        } else {
            setToast("Gagal mengambil data");
            onBackPressed();
        }

        txtNPWP.setText(Helper.isEmpty(outletHeader.getNo_npwp(), ""));
        txtKTP.setText(Helper.isEmpty(outletHeader.getNik(), ""));
        txtSisaKreditLimit.setText(format.format(outletHeader.getLimit_kredit()));
        txtPhone.setText(Helper.isEmpty(outletHeader.getNo_tlp(), ""));
        txtNamaPemilik.setText(Helper.isEmpty(outletHeader.getNama_pemilik(), ""));
        txtOutlet.setText(Helper.isEmpty(outletHeader.getNama(), ""));
        String idTypeCust = Helper.isEmpty(outletHeader.getType_customer(), "");
        String nameTypeCust = Helper.isEmpty(outletHeader.getName_type_customer(), "");
        txtTypeOutlet.setText(idTypeCust + " - " + nameTypeCust);

        fakturList = new ArrayList<>();
        fakturList.add(new Material("Drink", 1));
        fakturList.add(new Material("Redbull", 0));
        fakturList.add(new Material("Drink UC", 1));
        fakturList.add(new Material("Battery", 1));

        dctOutletList = new ArrayList<>();
        dctOutletList.add(new Material("Kratingdaeng", 1));
        dctOutletList.add(new Material("Redbull", 0));
        dctOutletList.add(new Material("Vitamin", 2));
        dctOutletList.add(new Material("Water", 0));
        dctOutletList.add(new Material("Bat CZ", 1));
        dctOutletList.add(new Material("Bat ALK", 0));

        promoList = new ArrayList<>();
        promoList.addAll(database.getPromotionRouteByIdCustomer(outletHeader.getId()));

//        if (PARAM_STATUS_OUTLET.equals(Constants.PAUSE)) {
//            checkInOutRequest.setContinueTime(null);
//        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        rvDCTOutlet = findViewById(R.id.rvDCTOutlet);
        rvDCTOutlet.setLayoutManager(new LinearLayoutManager(this));
        rvDCTOutlet.setHasFixedSize(true);
        rvOutstandingFaktur = findViewById(R.id.rvOutstandingFaktur);
        rvOutstandingFaktur.setLayoutManager(new LinearLayoutManager(this));
        rvOutstandingFaktur.setHasFixedSize(true);
        rvPromo = findViewById(R.id.rvPromo);
        rvPromo.setLayoutManager(new LinearLayoutManager(this));
        rvPromo.setHasFixedSize(true);
        imgOutlet = findViewById(R.id.imgOutlet);
        llOutlet = findViewById(R.id.llOutlet);
        imgNPWP = findViewById(R.id.imgNPWP);
        llNPWP = findViewById(R.id.llNPWP);
        imgKTP = findViewById(R.id.imgKTP);
        llKTP = findViewById(R.id.llKTP);
        txtNPWP = findViewById(R.id.txtNPWP);
        txtKTP = findViewById(R.id.txtKTP);
        txtTotalTagihan = findViewById(R.id.txtTotalTagihan);
        txtSisaKreditLimit = findViewById(R.id.txtSisaKreditLimit);
        txtPhone = findViewById(R.id.txtPhone);
        txtNamaPemilik = findViewById(R.id.txtNamaPemilik);
        timerValue = findViewById(R.id.timerValue);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        txtTypeOutlet = findViewById(R.id.txtTypeOutlet);
        txtOutlet = findViewById(R.id.txtOutlet);
        llPause = findViewById(R.id.llPause);
        imgBack = findViewById(R.id.imgBack);
        llStoreCheck = findViewById(R.id.llStoreCheck);
        llOrder = findViewById(R.id.llOrder);
        llCollection = findViewById(R.id.llCollection);
        llReturn = findViewById(R.id.llReturn);
        txtStatus = findViewById(R.id.txtStatus);
        imgPause = findViewById(R.id.imgPause);
        imgLogOut = findViewById(R.id.imgLogOut);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTimerValue();
    }

    private void pauseTimer() {
        timerValue.stop();
//            OutletResponse pause = new OutletResponse();
//            pause.setIdOutlet(outletResponse.getIdOutlet());
//            pause.setTimer(String.valueOf(timerValue.getBase()));

        txtStatus.setText("Resume");
        imgPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.aspp_ic_play_visit));
    }

    private void resumeTimer() {
        if (Helper.valTime == 1) {
            Helper.tempTime = SystemClock.elapsedRealtime();
            Helper.valTime = 2;
        }
        formatResumeTime();
        timerValue.start();
        Helper.resume = false;

        txtStatus.setText("Pause");
        imgPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_visit));
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

    private void setTimerValue() {
        /*Timer*/
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_TYPE_6);

        if (curDate != null) {
            String currentDate = Helper.convertDateToString(Constants.DATE_TYPE_5, curDate);
            curTime = getTimeFromDate(currentDate);
        } else {
            curTime = null;
            dCurrent = null;
        }

//        if (checkInOutRequest.getCheckInTime() != null) {
//            try {
//                String date = CalendarUtils.ConvertMilliSecondsToFormattedDate(checkInOutRequest.getCheckInTime());
//                checkInTime = getTimeFromDate(date);
//            } catch (Exception ignored) {
//                checkInTime = null;
//            }
//        } else {
        checkInTime = null;
//        }

//        if (checkInOutRequest.getPauseTime() != null) {
//            try {
//                String pauseDate = CalendarUtils.ConvertMilliSecondsToFormattedDate(checkInOutRequest.getPauseTime());
//                pauseTime = getTimeFromDate(pauseDate);
//            } catch (Exception ignored) {
//                pauseTime = null;
//            }
//        } else {
        pauseTime = null;
//        }

//        if (checkInOutRequest.getContinueTime() != null) {
//            try {
//                String continueDate = CalendarUtils.ConvertMilliSecondsToFormattedDate(checkInOutRequest.getContinueTime());
//                continueTime = getTimeFromDate(continueDate);
//            } catch (Exception ignored) {
//                continueTime = null;
//                dResume = null;
//            }
//        } else {
        continueTime = null;
        dResume = null;
//        }

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
            case Constants.RESUME:
                if (timeDuration != null) {
                    try {
                        playTimerBy(SystemClock.elapsedRealtime() - (Long.parseLong(timeDuration) + (dResume != null ? dCurrent.getTime() - dResume.getTime() : 0)));
                    } catch (NumberFormatException ignored) {
                    }
                }
                break;
            case Constants.FINISHED:
                if (timeDuration != null) {
                    try {
                        playTimerBy(SystemClock.elapsedRealtime() - Long.parseLong(timeDuration));
                    } catch (NumberFormatException ignored) {
                    }
                }
                break;
            default:
                timerValue.start();
                break;
        }
    }

    private String getTimeFromDate(String date) {
        String[] part = date.split(Constants.SPACE);
        return part[1];
    }
}
package id.co.qualitas.qubes.activity.aspp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.FilteredSpinnerAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.User;

public class CollectionFormActivity extends BaseActivity {
    private ImageView imgBack;
    private Button btnSubmit;
    private TextView spnBankTransfer, spnFromBankGiro, spnBankNameGiro, spnBankCheque,
            txtDueDate, txtDateGiro, txtTanggalCheque, txtDateTransfer;
    private LinearLayout buttonCashSelect, buttonCash;
    private LinearLayout buttonTransferSelect, buttonTransfer;
    private LinearLayout buttonCheqSelect, buttonCheq;
    private LinearLayout buttonGiroSelect, buttonGiro;
    private LinearLayout llCash, llTransfer, llGiro, llCheque;
    private RelativeLayout rlCash, rlTransfer, rlGiro, rlCheque;
    Date todayDate;
    String chooseDateString, todayString;
    Calendar todayCalendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_collection_form);

        init();
        initialize();
        initData();
        setSpinner();
        setDateDialog();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        rlCash.setOnClickListener(v -> {
            setSelectView(1);
        });

        rlTransfer.setOnClickListener(v -> {
            setSelectView(2);
        });

        rlGiro.setOnClickListener(v -> {
            setSelectView(3);
        });

        rlCheque.setOnClickListener(v -> {
            setSelectView(4);
        });
    }

    private void setDateDialog() {
        todayDate = Helper.getTodayDate();
        todayString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(todayDate);

        txtDateTransfer.setText(todayString);
        txtTanggalCheque.setText(todayString);
        txtDueDate.setText(todayString);
        txtDateGiro.setText(todayString);

        txtDateTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                todayCalendar = Calendar.getInstance();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(todayDate);
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int date = calendar.get(Calendar.DATE);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DATE, dayOfMonth);

                        chooseDateString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(calendar.getTime());
                        txtDateTransfer.setText(chooseDateString);
                        txtDateTransfer.setError(null);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(CollectionFormActivity.this, R.style.DialogTheme, dateSetListener, year, month, date);
                dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
                dialog.show();
            }
        });

        txtTanggalCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                todayCalendar = Calendar.getInstance();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(todayDate);
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int date = calendar.get(Calendar.DATE);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DATE, dayOfMonth);

                        chooseDateString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(calendar.getTime());
                        txtTanggalCheque.setText(chooseDateString);
                        txtTanggalCheque.setError(null);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(CollectionFormActivity.this, R.style.DialogTheme, dateSetListener, year, month, date);
                dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
                dialog.show();
            }
        });

        txtDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                todayCalendar = Calendar.getInstance();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(todayDate);
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int date = calendar.get(Calendar.DATE);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DATE, dayOfMonth);

                        chooseDateString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(calendar.getTime());
                        txtDueDate.setText(chooseDateString);
                        txtDateTransfer.setError(null);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(CollectionFormActivity.this, R.style.DialogTheme, dateSetListener, year, month, date);
                dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
                dialog.show();
            }
        });

        txtDateGiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                todayCalendar = Calendar.getInstance();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(todayDate);
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int date = calendar.get(Calendar.DATE);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DATE, dayOfMonth);

                        chooseDateString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(calendar.getTime());
                        txtDateGiro.setText(chooseDateString);
                        txtDateGiro.setError(null);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(CollectionFormActivity.this, R.style.DialogTheme, dateSetListener, year, month, date);
                dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
                dialog.show();
            }
        });
    }

    private void setSpinner() {
        spnBankTransfer.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(this);

            alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);

            List<String> groupList = new ArrayList<>();
            groupList.add("Y001 - BANK BCA");
            groupList.add("Y002 - BANK MANDIRI");
            groupList.add("Y003 - BANK MANDIRI SYARIAH");
            groupList.add("Y004 - BANK BNI");
            groupList.add("Y005 - BANK BNI SYARIAH");

            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(this, groupList, (nameItem, adapterPosition) -> {
                spnBankTransfer.setText(nameItem);
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

        spnFromBankGiro.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(this);

            alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);

            List<String> groupList = new ArrayList<>();
            groupList.add("Y001 - BANK BCA");
            groupList.add("Y002 - BANK MANDIRI");
            groupList.add("Y003 - BANK MANDIRI SYARIAH");
            groupList.add("Y004 - BANK BNI");
            groupList.add("Y005 - BANK BNI SYARIAH");

            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(this, groupList, (nameItem, adapterPosition) -> {
                spnFromBankGiro.setText(nameItem);
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

        spnBankCheque.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(this);

            alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);

            List<String> groupList = new ArrayList<>();
            groupList.add("Y001 - BANK BCA");
            groupList.add("Y002 - BANK MANDIRI");
            groupList.add("Y003 - BANK MANDIRI SYARIAH");
            groupList.add("Y004 - BANK BNI");
            groupList.add("Y005 - BANK BNI SYARIAH");

            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(this, groupList, (nameItem, adapterPosition) -> {
                spnBankCheque.setText(nameItem);
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

        spnBankNameGiro.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(this);

            alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);

            List<String> groupList = new ArrayList<>();
            groupList.add("BCA GATSU 0012345678");
            groupList.add("BCA DAAN MOGOT 987654332");
            groupList.add("BCA BOGOR 567893322");
            groupList.add("BCA MEDAN 1234565432");
            groupList.add("BCA PLUIT 7654345678");

            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(this, groupList, (nameItem, adapterPosition) -> {
                spnBankNameGiro.setText(nameItem);
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
    }

    private void setSelectView(int posTab) {
        switch (posTab) {
            case 1:
                buttonCashSelect.setVisibility(View.VISIBLE);
                buttonCash.setVisibility(View.GONE);
                buttonTransferSelect.setVisibility(View.GONE);
                buttonTransfer.setVisibility(View.VISIBLE);
                buttonCheqSelect.setVisibility(View.GONE);
                buttonCheq.setVisibility(View.VISIBLE);
                buttonGiroSelect.setVisibility(View.GONE);
                buttonGiro.setVisibility(View.VISIBLE);

                llCash.setVisibility(View.VISIBLE);
                llTransfer.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                break;
            case 2:
                buttonCashSelect.setVisibility(View.GONE);
                buttonCash.setVisibility(View.VISIBLE);
                buttonTransferSelect.setVisibility(View.VISIBLE);
                buttonTransfer.setVisibility(View.GONE);
                buttonCheqSelect.setVisibility(View.GONE);
                buttonCheq.setVisibility(View.VISIBLE);
                buttonGiroSelect.setVisibility(View.GONE);
                buttonGiro.setVisibility(View.VISIBLE);

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.VISIBLE);
                llCheque.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                break;
            case 3:
                buttonCashSelect.setVisibility(View.GONE);
                buttonCash.setVisibility(View.VISIBLE);
                buttonTransferSelect.setVisibility(View.GONE);
                buttonTransfer.setVisibility(View.VISIBLE);
                buttonCheqSelect.setVisibility(View.VISIBLE);
                buttonCheq.setVisibility(View.GONE);
                buttonGiroSelect.setVisibility(View.GONE);
                buttonGiro.setVisibility(View.VISIBLE);

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llCheque.setVisibility(View.VISIBLE);
                llGiro.setVisibility(View.GONE);
                break;
            case 4:
                buttonCashSelect.setVisibility(View.GONE);
                buttonCash.setVisibility(View.VISIBLE);
                buttonTransferSelect.setVisibility(View.GONE);
                buttonTransfer.setVisibility(View.VISIBLE);
                buttonCheqSelect.setVisibility(View.GONE);
                buttonCheq.setVisibility(View.VISIBLE);
                buttonGiroSelect.setVisibility(View.VISIBLE);
                buttonGiro.setVisibility(View.GONE);

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llGiro.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initData() {

    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        spnBankTransfer = findViewById(R.id.spnBankTransfer);
        spnFromBankGiro = findViewById(R.id.spnFromBankGiro);
        spnBankCheque = findViewById(R.id.spnBankCheque);
        spnBankNameGiro = findViewById(R.id.spnBankNameGiro);
        txtDueDate = findViewById(R.id.txtDueDate);
        txtTanggalCheque = findViewById(R.id.txtTanggalCheque);
        txtDateTransfer = findViewById(R.id.txtDateTransfer);
        txtDateGiro = findViewById(R.id.txtDateGiro);
        imgBack = findViewById(R.id.imgBack);
        buttonCash = findViewById(R.id.buttonCash);
        buttonCashSelect = findViewById(R.id.buttonCashSelect);
        buttonTransferSelect = findViewById(R.id.buttonTransferSelect);
        buttonTransfer = findViewById(R.id.buttonTransfer);
        buttonCheqSelect = findViewById(R.id.buttonCheqSelect);
        buttonCheq = findViewById(R.id.buttonCheq);
        buttonGiroSelect = findViewById(R.id.buttonGiroSelect);
        buttonGiro = findViewById(R.id.buttonGiro);
        rlCash = findViewById(R.id.rlCash);
        llCash = findViewById(R.id.llCash);
        llTransfer = findViewById(R.id.llTransfer);
        rlTransfer = findViewById(R.id.rlTransfer);
        llGiro = findViewById(R.id.llGiro);
        rlGiro = findViewById(R.id.rlGiro);
        llCheque = findViewById(R.id.llCheque);
        rlCheque = findViewById(R.id.rlCheque);
        buttonGiro = findViewById(R.id.buttonGiro);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
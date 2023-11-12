package id.co.qualitas.qubes.fragment.aspp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.activity.aspp.SummaryDetailActivity;
import id.co.qualitas.qubes.adapter.aspp.SummaryAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class SummaryFragment extends BaseFragment {
    private LinearLayout llFilter;
    private EditText edtSearch;
    private LinearLayout llNoData;
    private SummaryAdapter mAdapter;
    private List<Order> mList;
    private WSMessage resultWsMessage, logResult;
    private boolean saveDataSuccess = false;
    private Spinner spinnerStatus;
    private ArrayAdapter<String> statusAdapter;
    private String selectedStatus, dateStartString = null, dateEndString = null;
    private Date dateFrom, dateTo;
    private Map filter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_summary, container, false);
        getActivity().setTitle(getString(R.string.summary));

        initProgress();
        initFragment();
        initialize();

        swipeLayout.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
                swipeLayout.setRefreshing(false);
            }
        });

        llFilter.setOnClickListener(v -> {
            openDialogFilter();
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    setEnabled(false);
//                    ((MainActivity) getActivity()).changePage(1);
                    ((MainActivity) getActivity()).backPress();
                }
            }
        });

        return rootView;
    }

    private void openDialogFilter() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final Dialog alertDialog = new Dialog(getContext());
        View dialog = inflater.inflate(R.layout.aspp_dialog_filter_summary, null);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(dialog);
        alertDialog.setCanceledOnTouchOutside(false);

        spinnerStatus = dialog.findViewById(R.id.spinnerStatus);
        EditText edtFrom = dialog.findViewById(R.id.edtFrom);
        EditText edtTo = dialog.findViewById(R.id.edtTo);
        Button btnFilter = dialog.findViewById(R.id.btnFilter);

        edtFrom.setText(Helper.todayDateAddDate(Constants.DATE_TYPE_1, -7));
        edtTo.setText(Helper.todayDateAddDate(Constants.DATE_TYPE_1, 0));

        List<String> statusList = new ArrayList<>();
        statusList.add("All");
        statusList.add("Approve");
        statusList.add("Reject");
        statusList.add("Pending");
        statusList.add("Sync Success");

        statusAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                return view;
            }
        };
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusAdapter.addAll(statusList);
        spinnerStatus.setAdapter(statusAdapter);
        int spinnerPositionSuku = statusAdapter.getPosition(selectedStatus);
        spinnerStatus.setSelection(spinnerPositionSuku);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStatus = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if (dateStartString != null) {
            edtFrom.setText(dateStartString);
        }

        if (dateEndString != null) {
            edtTo.setText(dateEndString);
        }

        edtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar dateStartCalendar = Calendar.getInstance();
                if (dateFrom != null) {
                    dateStartCalendar.setTime(dateFrom);
                }
                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateStartCalendar.set(year, monthOfYear, dayOfMonth);
                                dateFrom = dateStartCalendar.getTime();
                                dateStartString = Helper.convertDateToStringNew(Constants.DATE_TYPE_1, dateFrom);
                                edtFrom.setText(dateStartString);
                                edtFrom.setError(null);
                            }
                        }, dateStartCalendar.get(Calendar.YEAR), dateStartCalendar.get(Calendar.MONTH), dateStartCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.getDatePicker().setCalendarViewShown(false);
                fromDatePickerDialog.setTitle("From");
                fromDatePickerDialog.show();
            }
        });

        edtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar dateEndCalendar = Calendar.getInstance();
                if (dateTo != null) {
                    dateEndCalendar.setTime(dateTo);
                }
                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateEndCalendar.set(year, monthOfYear, dayOfMonth);
                                dateTo = dateEndCalendar.getTime();
                                dateEndString = Helper.convertDateToStringNew(Constants.DATE_TYPE_1, dateTo);
                                edtTo.setText(dateEndString);
                                edtTo.setError(null);
                            }
                        }, dateEndCalendar.get(Calendar.YEAR), dateEndCalendar.get(Calendar.MONTH), dateEndCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.getDatePicker().setCalendarViewShown(false);
                fromDatePickerDialog.setTitle("To");
                fromDatePickerDialog.show();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                requestData();
            }
        });

        alertDialog.show();
    }

    private void setAdapter() {
        mAdapter = new SummaryAdapter(SummaryFragment.this, mList, header -> {
            SessionManagerQubes.setOrder(header);
            Intent intent = new Intent(getActivity(), SummaryDetailActivity.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void initialize() {
        database = new Database(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        llNoData = rootView.findViewById(R.id.llNoData);
        edtSearch = rootView.findViewById(R.id.edtSearch);
        llFilter = rootView.findViewById(R.id.llFilter);
        progressCircle = rootView.findViewById(R.id.progressCircle);
        swipeLayout = rootView.findViewById(R.id.swipeLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFirstDataOffline();
        Helper.setItemParam(Constants.CURRENTPAGE, "4");
    }

    private void getFirstDataOffline() {
        getData();
        setAdapter();

        if (mList == null || mList.isEmpty()) {
            requestData();
        }
    }

    private void requestData() {
        llNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        PARAM = 1;
        new RequestUrl().execute();
    }

    private void getData() {
        mList = new ArrayList<>();
    }

    private class RequestUrl extends AsyncTask<Void, Void, WSMessage> {

        @Override
        protected WSMessage doInBackground(Void... voids) {
            try {
                if (PARAM == 1) {
                    filter = new HashMap();
                    filter.put("username", user.getUsername());
                    filter.put("fromString", dateStartString != null ? Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_FORMAT_3, dateStartString) : Helper.todayDateAddDate(Constants.DATE_FORMAT_3, -7));
                    filter.put("toString", dateEndString != null ? Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_FORMAT_3, dateEndString) : Helper.todayDateAddDate(Constants.DATE_FORMAT_3, 0));
                    filter.put("status", selectedStatus != null ? (selectedStatus.equals("All") ? null : selectedStatus) : null);
                    String URL_ = Constants.API_GET_SUMMARY_HEADER;
                    final String url = Constants.URL.concat(Constants.API_PREFIX).concat(URL_);
                    logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, filter);
                    return null;
                } else {
                    mList = new ArrayList<>();
                    Order[] paramArray = Helper.ObjectToGSON(resultWsMessage.getResult(), Order[].class);
                    if (paramArray != null) {
                        Collections.addAll(mList, paramArray);
                    }
                    saveDataSuccess = true;
                    return null;
                }
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("summaryheader", ex.getMessage());
                }
                if (PARAM == 2) {
                    saveDataSuccess = false;
                } else {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : ex.getMessage();
                    logResult.setMessage("Summary Header error: " + exMess);
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
                if (logResult.getIdMessage() == 1) {
                    String message = "Summary Header : " + logResult.getMessage();
                    logResult.setMessage(message);
                }
                database.addLog(logResult);
                if (logResult.getIdMessage() == 1 && logResult.getResult() != null) {
                    resultWsMessage = logResult;
                    PARAM = 2;
                    new RequestUrl().execute();
                } else {
                    progressCircle.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                }
            } else {
                progressCircle.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (saveDataSuccess) {
                    mAdapter.setData(mList);
                }
                if (Helper.isEmptyOrNull(mList)) {
                    recyclerView.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }
            }
        }
    }
}
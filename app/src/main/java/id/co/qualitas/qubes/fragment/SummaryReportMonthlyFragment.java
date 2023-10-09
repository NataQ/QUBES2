package id.co.qualitas.qubes.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.XYMarkerView;
import id.co.qualitas.qubes.model.SpinnerTargetResponse;
import id.co.qualitas.qubes.model.SummaryRequest;
import id.co.qualitas.qubes.model.TargetResponse;
import id.co.qualitas.qubes.model.User;

public class SummaryReportMonthlyFragment extends BaseFragment {
    private TextView totalReturn, targetSales, actualSales, balanceSales, targetCall, actualCall, balanceCall, targetEC, actualEC, balanceEC, targetEA, actualEA, balanceEA;
    private TextView totalReturn2, targetSales2, actualSales2, balanceSales2, targetCall2, actualCall2, balanceCall2, targetEC2, actualEC2,
            balanceEC2, targetEA2, actualEA2, balanceEA2,
            txtTryAgain, txtTryAgain2,
            txtBalanceSalesMonthly, txtBalanceCallMonthly, txtBalanceEAMonthly, txtBalanceECMonthly,
            txtBalanceSalesDaily, txtBalanceCallDaily, txtBalanceEADaily, txtBalanceECDaily;
    private CardView btnFilter;
    private BarChart chartMonthlySales, chartMonthlyCall, chartMonthlyEC, chartMonthlyEA;
    private BarChart chartDailySales, chartDailyCall, chartDailyEC, chartDailyEA;
    private String url;
    private ProgressBar spinner, spinner2;

    private User user = new User();
    private SummaryRequest summaryRequest = new SummaryRequest();
    private LinearLayout linearTryAgain1, linearTryAgain2;
    private RelativeLayout linearSummaryMonthly, linearSummaryDaily;

    private ArrayList<TargetResponse> list;
    private SpinnerTargetResponse spinnerResponse;

    private List<String> nameCustList, codeCustList, divProdList;
    private int type = 0;
    private Button btnMonthly, btnDaily;
    private RelativeLayout layoutMonthly, layoutDaily;

    float barWidth;
    float barSpace;
    float groupSpace;
    private float targetSalesFloat, targetCallFloat, targetECFloat, targetEAFloat;
    private float actualSalesFloat, actualCallFloat, actualECFloat, actualEAFloat;
    private String balanceSalesString, balanceCallString, balanceECString, balanceEAString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_summary_report_tab_2, container, false);

        initProgress();
        initialize();

        btnMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 0;
                setTypeLayout();
                setDataRequest();
            }
        });

        btnDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 1;
                setTypeLayout();
                setDataRequest();
            }
        });

        txtTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PARAM = 1;
                requestUrl();
            }
        });

        txtTryAgain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PARAM = 2;
                requestUrl();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preventDupeDialog();
                Helper.setItemParam(Constants.ACTIVE_MENU, getResources().getString(R.string.navmenu6a));
//                Helper.setItemParam(Constants.SUMMARY_TYPE, tabHost.getCurrentTabTag());
                Helper.setItemParam(Constants.SUMMARY_TYPE, type == 0 ? Constants.MONTH : Constants.DAY);
                openDialog(DIALOG_FILTER);
            }
        });

        return rootView;
    }

    private void setDataRequest() {
        if (type == 0) {//monthly
            PARAM = 1;
            spinner.setVisibility(View.VISIBLE);
            linearSummaryMonthly.setVisibility(View.GONE);
        } else {//daily
            linearSummaryDaily.setVisibility(View.GONE);
            if (summaryRequest.getDateFrom() != null) {
                if (summaryRequest.getDateFrom().equals(Constants.EMPTY_STRING)) {
                    summaryRequest.setDateFrom(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                }
            }
            if (summaryRequest.getDateTo() != null) {
                if (summaryRequest.getDateTo().equals(Constants.EMPTY_STRING)) {
                    summaryRequest.setDateTo(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                }
            }

            PARAM = 2;
            spinner2.setVisibility(View.VISIBLE);
        }
        new RequestUrl().execute();

    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtBalanceSalesMonthly = rootView.findViewById(R.id.txtBalanceSalesMonthly);
        txtBalanceCallMonthly = rootView.findViewById(R.id.txtBalanceCallMonthly);
        txtBalanceEAMonthly = rootView.findViewById(R.id.txtBalanceEAMonthly);
        txtBalanceECMonthly = rootView.findViewById(R.id.txtBalanceECMonthly);

        txtBalanceSalesDaily = rootView.findViewById(R.id.txtBalanceSalesDaily);
        txtBalanceCallDaily = rootView.findViewById(R.id.txtBalanceCallDaily);
        txtBalanceEADaily = rootView.findViewById(R.id.txtBalanceEADaily);
        txtBalanceECDaily = rootView.findViewById(R.id.txtBalanceECDaily);

        btnMonthly = rootView.findViewById(R.id.btnMonthly);
        btnDaily = rootView.findViewById(R.id.btnDaily);
        layoutMonthly = rootView.findViewById(R.id.layoutMonthly);
        layoutDaily = rootView.findViewById(R.id.layoutDaily);

        totalReturn = rootView.findViewById(R.id.txtTotalReturn);
        spinner = rootView.findViewById(R.id.progressBar);
        linearTryAgain1 = rootView.findViewById(R.id.linearTryAgain);
        linearTryAgain2 = rootView.findViewById(R.id.linearTryAgain2);
        linearSummaryMonthly = rootView.findViewById(R.id.linearSummaryMonthly);
        linearSummaryDaily = rootView.findViewById(R.id.linearSummaryDaily);
        targetSales = rootView.findViewById(R.id.txtTargetSales);
        targetCall = rootView.findViewById(R.id.txtTargetCall);
        targetEC = rootView.findViewById(R.id.txtTargetEc);
        targetEA = rootView.findViewById(R.id.txtTargetEA);
        actualSales = rootView.findViewById(R.id.txtActualSales);
        actualCall = rootView.findViewById(R.id.txtActualCall);
        actualEC = rootView.findViewById(R.id.txtActualEc);
        actualEA = rootView.findViewById(R.id.txtActualEA);
        balanceSales = rootView.findViewById(R.id.txtBalanceSales);
        balanceCall = rootView.findViewById(R.id.txtBalanceCall);
        balanceEC = rootView.findViewById(R.id.txtBalanceEc);
        balanceEA = rootView.findViewById(R.id.txtBalanceEA);
        totalReturn2 = rootView.findViewById(R.id.txtTotalReturn2);
        spinner2 = rootView.findViewById(R.id.progressBar2);
        totalReturn2 = rootView.findViewById(R.id.txtTotalReturn2);
        targetSales2 = rootView.findViewById(R.id.txtTargetSales2);
        targetCall2 = rootView.findViewById(R.id.txtTargetCall2);
        targetEC2 = rootView.findViewById(R.id.txtTargetEc2);
        targetEA2 = rootView.findViewById(R.id.txtTargetEA2);
        actualSales2 = rootView.findViewById(R.id.txtActualSales2);
        actualCall2 = rootView.findViewById(R.id.txtActualCall2);
        actualEC2 = rootView.findViewById(R.id.txtActualEc2);
        actualEA2 = rootView.findViewById(R.id.txtActualEA2);
        balanceSales2 = rootView.findViewById(R.id.txtBalanceSales2);
        balanceCall2 = rootView.findViewById(R.id.txtBalanceCall2);
        balanceEC2 = rootView.findViewById(R.id.txtBalanceEc2);
        balanceEA2 = rootView.findViewById(R.id.txtBalanceEA2);
        txtTryAgain = rootView.findViewById(R.id.txtTryAgain);
        txtTryAgain2 = rootView.findViewById(R.id.txtTryAgain2);

        btnFilter = rootView.findViewById(R.id.btnFilter);

        chartMonthlySales = rootView.findViewById(R.id.chartMonthlySales);
        chartMonthlyCall = rootView.findViewById(R.id.chartMonthlyCall);
        chartMonthlyEC = rootView.findViewById(R.id.chartMonthlyEC);
        chartMonthlyEA = rootView.findViewById(R.id.chartMonthlyEA);

        chartDailySales = rootView.findViewById(R.id.chartDailySales);
        chartDailyCall = rootView.findViewById(R.id.chartDailyCall);
        chartDailyEC = rootView.findViewById(R.id.chartDailyEC);
        chartDailyEA = rootView.findViewById(R.id.chartDailyEA);
    }

//    private void functionTab() {
//
//        tabHost.getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                tabHost.setCurrentTab(0);
//
//                TextView tv = tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
//                tv.setTextColor(getResources().getColor(R.color.white));
//
//                PARAM = 1;
//                new RequestUrl().execute();
//                spinner.setVisibility(View.VISIBLE);
//            }
//        });
//
//
//        tabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tabHost.setCurrentTab(1);
//
//                TextView tv = tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
//                tv.setTextColor(getResources().getColor(R.color.white));
//
//                if (summaryRequest.getDateFrom() != null) {
//                    if (summaryRequest.getDateFrom().equals(Constants.EMPTY_STRING)) {
//                        summaryRequest.setDateFrom(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
//                    }
//                }
//                if (summaryRequest.getDateTo() != null) {
//                    if (summaryRequest.getDateTo().equals(Constants.EMPTY_STRING)) {
//                        summaryRequest.setDateTo(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
//                    }
//                }
//
//                PARAM = 2;
//                new RequestUrl().execute();
//                spinner2.setVisibility(View.VISIBLE);
//            }
//        });
//    }

    private class RequestUrl extends AsyncTask<Void, Void, TargetResponse[]> {

        @Override
        protected TargetResponse[] doInBackground(Void... voids) {
            try {
                Calendar cal = Helper.todayDate();
                int month = cal.get(Calendar.MONTH) + 1;
                int year = cal.get(Calendar.YEAR);

                String idEmployee = Constants.QUESTION.concat(Constants.EMPLOYEE_ID.concat(Constants.EQUALS)) + user.getIdEmployee();
                String bulan = Constants.MONTH.concat(Constants.EQUALS).concat(String.valueOf(month));
                String tahun = Constants.YEAR.concat(Constants.EQUALS).concat(String.valueOf(year));
                if (summaryRequest.getMatClass() != null) {
                    if (summaryRequest.getMatClass().equals("ALL")) {
                        summaryRequest.setMatClass("");
                    }
                }
                if (PARAM == 1) {
                    url = Constants.URL.concat(Constants.API_GET_TARGET_FOR_SUMMARY_MONTHLY);
                } else if (PARAM == 2) {
                    url = Constants.URL.concat(Constants.API_GET_TARGET_FOR_SUMMARY_DAILY);
                } else {
                    url = Constants.URL.concat(Constants.API_GET_LIST_SPINNER).concat(idEmployee).concat(Constants.AND).concat(bulan).concat(Constants.AND).concat(tahun);
                    spinnerResponse = (SpinnerTargetResponse) Helper.getWebservice(url, SpinnerTargetResponse.class);
                    return null;
                }
                return (TargetResponse[]) Helper.postWebserviceWithBody(url, TargetResponse[].class, summaryRequest);
            } catch (Exception ex) {
                //connection = true;
                if (ex.getMessage() != null) {
                    Log.e("Summary", ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(TargetResponse[] response) {
            linearSummaryMonthly.setVisibility(View.VISIBLE);
            linearSummaryDaily.setVisibility(View.VISIBLE);

            if (PARAM == 1) {
                if (response != null) {
                    list = new ArrayList<>();
                    Collections.addAll(list, response);

//                    setDataReport(targetSales, targetCall, targetEC, targetEA,
//                            actualSales, actualCall, actualEC, actualEA,
//                            balanceSales, balanceCall, balanceEC, balanceEA, totalReturn);
                    setDataReport(1, totalReturn);
                } else {
                    linearTryAgain1.setVisibility(View.VISIBLE);
                    linearSummaryMonthly.setVisibility(View.GONE);
                }

                spinner.setVisibility(View.GONE);
                spinner2.setVisibility(View.GONE);
                linearTryAgain1.setVisibility(View.GONE);
                linearTryAgain2.setVisibility(View.GONE);
            } else if (PARAM == 2) {
                if (response != null) {
                    list = new ArrayList<>();
                    Collections.addAll(list, response);
                    setDataReport(2, totalReturn2);
//                    setDataReport(targetSales2, targetCall2, targetEC2, targetEA2,
//                            actualSales2, actualCall2, actualEC2, actualEA2,
//                            balanceSales2, balanceCall2, balanceEC2, balanceEA2, totalReturn2);
                } else {
                    linearTryAgain2.setVisibility(View.VISIBLE);
                    linearSummaryDaily.setVisibility(View.GONE);
                }

                spinner.setVisibility(View.GONE);
                spinner2.setVisibility(View.GONE);
                linearTryAgain1.setVisibility(View.GONE);
                linearTryAgain2.setVisibility(View.GONE);
            } else {
                if (spinnerResponse != null) {
                    if (spinnerResponse.getListCustomer() != null && spinnerResponse.getListCustomer().size() != 0) {
                        nameCustList = new ArrayList<>();
                        codeCustList = new ArrayList<>();
                        nameCustList.add(0, getString(R.string.allCaps));

                        for (int i = 0; i < spinnerResponse.getListCustomer().size(); i++) {
                            nameCustList.add(spinnerResponse.getListCustomer().get(i).getName());
                            codeCustList.add(spinnerResponse.getListCustomer().get(i).getIdOutlet());
                        }
                        Helper.setItemParam(Constants.ITEM_CUSTOMER_NAME, nameCustList);
                        Helper.setItemParam(Constants.ITEM_CUSTOMER_ID, codeCustList);
                    }

                    if (spinnerResponse.getMatClass() != null && spinnerResponse.getMatClass().size() != 0) {
                        divProdList = new ArrayList<>();
                        divProdList.add(0, getString(R.string.allCaps));

                        for (int i = 0; i < spinnerResponse.getMatClass().size(); i++) {
                            divProdList.add(i + 1, spinnerResponse.getMatClass().get(i));
                        }
                        Helper.setItemParam(Constants.ITEM_DIV_PROD, divProdList);
                    }
                    progress.dismiss();


                    PARAM = 1;
                    new RequestUrl().execute();

                }
            }


        }
    }

    //    private void setDataReport(TextView targetSales, TextView targetCall, TextView targetEC, TextView targetEA,
//                               TextView actualSales, TextView actualCall, TextView actualEC, TextView actualEA,
//                               TextView balanceSales, TextView balanceCall, TextView balanceEC, TextView balanceEA, TextView totalReturn) {
    private void setDataReport(int typeChart, TextView totalReturn) {
        if (list.get(0).getTargetSales() != null) {
//            targetSales.setText(Helper.toRupiahFormat(
//                    String.valueOf(new BigDecimal(list.get(0).getTargetSales()).setScale(0, BigDecimal.ROUND_HALF_UP))));
            targetSalesFloat = (new BigDecimal(list.get(0).getTargetSales()).setScale(0, BigDecimal.ROUND_HALF_UP)).floatValue();
        } else {
            targetSalesFloat = 0;
//            targetSales.setText(Constants.ZERO);
        }
        if (list.get(0).getTargetCall() != null) {
//            targetCall.setText(Helper.toRupiahFormat(String.valueOf(list.get(0).getTargetCall())));
            targetCallFloat = list.get(0).getTargetCall().floatValue();
        } else {
            targetCallFloat = 0;
//            targetCall.setText(Constants.ZERO);
        }
        if (list.get(0).getTargetEc() != null) {
//            targetEC.setText(Helper.toRupiahFormat(list.get(0).getTargetEc()));
            targetECFloat = Float.parseFloat(list.get(0).getTargetEc());
        } else {
            targetECFloat = 0;
//            targetEC.setText(Constants.ZERO);
        }
        if (list.get(0).getTargetEa() != null) {
//            targetEA.setText(Helper.toRupiahFormat(list.get(0).getTargetEa()));
            targetEAFloat = Float.parseFloat(list.get(0).getTargetEa());
        } else {
            targetEAFloat = 0;
//            targetEA.setText(Constants.ZERO);
        }

        if (list.get(0).getActualSales() != null) {
//            actualSales.setText(Helper.toRupiahFormat(
//                    String.valueOf(new BigDecimal(list.get(0).getActualSales()).setScale(0, BigDecimal.ROUND_HALF_UP))));
            actualSalesFloat = (new BigDecimal(list.get(0).getActualSales()).setScale(0, BigDecimal.ROUND_HALF_UP)).floatValue();
        } else {
            actualSalesFloat = 0;
//            actualSales.setText(Constants.ZERO);
        }

        if (list.get(0).getActualCall() != null) {
//            actualCall.setText(list.get(0).getActualCall());
            actualCallFloat = Float.parseFloat(list.get(0).getActualCall());
        } else {
            actualCallFloat = 0;
//            actualCall.setText(Constants.ZERO);
        }

        if (list.get(0).getActualEc() != null) {
//            actualEC.setText(list.get(0).getActualEc());
            actualECFloat = Float.parseFloat(list.get(0).getActualEc());
        } else {
            actualECFloat = 0;
//            actualEC.setText(Constants.ZERO);
        }

        if (list.get(0).getActualEa() != null) {
//            actualEA.setText(list.get(0).getActualEa());
            actualEAFloat = Float.parseFloat(list.get(0).getActualEa());
        } else {
            actualEAFloat = 0;
//            actualEA.setText(Constants.ZERO);
        }

        if (list.get(0).getGapSales() != null) {
            if (list.get(0).getGapSales().contains(Constants.SPACE)) {
                balanceSales.setText(list.get(0).getGapSales().split(Constants.SPACE)[0]
                        .concat(Constants.SPACE)
                        .concat(Helper.toRupiahFormat(
                                String.valueOf(
                                        new BigDecimal(list.get(0).getGapSales().split(Constants.SPACE)[1]).setScale(0, BigDecimal.ROUND_HALF_UP)))));
            } else {
                balanceSales.setText(Helper.toRupiahFormat(
                        String.valueOf(new BigDecimal(list.get(0).getGapSales()).setScale(0, BigDecimal.ROUND_HALF_UP))));
            }
        } else {
            balanceSales.setText(Constants.ZERO);
        }

        if (list.get(0).getGapCall() != null) {
            if (list.get(0).getGapCall().contains(Constants.SPACE)) {
                balanceCall.setText(list.get(0).getGapCall().split(Constants.SPACE)[0]
                        .concat(Constants.SPACE)
                        .concat(Helper.toRupiahFormat(
                                String.valueOf(
                                        new BigDecimal(list.get(0).getGapCall().split(Constants.SPACE)[1]).setScale(0, BigDecimal.ROUND_HALF_UP)))));
                balanceSalesString = list.get(0).getGapCall().split(Constants.SPACE)[0]
                        .concat(Constants.SPACE)
                        .concat(Helper.toRupiahFormat(
                                String.valueOf(
                                        new BigDecimal(list.get(0).getGapCall().split(Constants.SPACE)[1]).setScale(0, BigDecimal.ROUND_HALF_UP))));
            } else if (list.get(0).getGapCall().contains(Constants.COMPLETED)) {
                balanceCall.setText(list.get(0).getGapCall());
                balanceSalesString = list.get(0).getGapCall();
            } else {
                try {
                    balanceCall.setText(Helper.toRupiahFormat(
                            String.valueOf(new BigDecimal(list.get(0).getGapCall()).setScale(0, BigDecimal.ROUND_HALF_UP))));
                    balanceSalesString = Helper.toRupiahFormat(
                            String.valueOf(new BigDecimal(list.get(0).getGapCall()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                } catch (NumberFormatException ignored) {
                }
            }
        } else {
            balanceCall.setText(Constants.ZERO);
            balanceSalesString = Constants.ZERO;
        }

        if (list.get(0).getGapEc() != null) {
            if (list.get(0).getGapEc().contains(Constants.SPACE)) {
                balanceEC.setText(list.get(0).getGapEc().split(Constants.SPACE)[0]
                        .concat(Constants.SPACE)
                        .concat(Helper.toRupiahFormat(
                                String.valueOf(
                                        new BigDecimal(list.get(0).getGapEc().split(Constants.SPACE)[1]).setScale(0, BigDecimal.ROUND_HALF_UP)))));
                balanceECString = list.get(0).getGapEc().split(Constants.SPACE)[0]
                        .concat(Constants.SPACE)
                        .concat(Helper.toRupiahFormat(
                                String.valueOf(
                                        new BigDecimal(list.get(0).getGapEc().split(Constants.SPACE)[1]).setScale(0, BigDecimal.ROUND_HALF_UP))));
                ;
            } else if (list.get(0).getGapEc().contains(Constants.COMPLETED)) {
                balanceEC.setText(list.get(0).getGapEc());
                balanceECString = list.get(0).getGapEc();
            } else {
                try {
                    balanceEC.setText(Helper.toRupiahFormat(
                            String.valueOf(new BigDecimal(list.get(0).getGapEc()).setScale(0, BigDecimal.ROUND_HALF_UP))));
                    balanceECString = Helper.toRupiahFormat(
                            String.valueOf(new BigDecimal(list.get(0).getGapEc()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                    ;
                } catch (NumberFormatException ignored) {
                }
            }
        } else {
            balanceEC.setText(Constants.ZERO);
            balanceECString = Constants.ZERO;
        }

        if (list.get(0).getGapEa() != null) {
            if (list.get(0).getGapEa().contains(Constants.SPACE)) {
                balanceEA.setText(list.get(0).getGapEa().split(Constants.SPACE)[0]
                        .concat(Constants.SPACE)
                        .concat(Helper.toRupiahFormat(
                                String.valueOf(
                                        new BigDecimal(list.get(0).getGapEa().split(Constants.SPACE)[1]).setScale(0, BigDecimal.ROUND_HALF_UP)))));
                balanceEAString = list.get(0).getGapEa().split(Constants.SPACE)[0]
                        .concat(Constants.SPACE)
                        .concat(Helper.toRupiahFormat(
                                String.valueOf(
                                        new BigDecimal(list.get(0).getGapEa().split(Constants.SPACE)[1]).setScale(0, BigDecimal.ROUND_HALF_UP))));
            } else if (list.get(0).getGapEa().contains(Constants.COMPLETED)) {
                balanceEA.setText(list.get(0).getGapEa());
                balanceEAString = list.get(0).getGapEa();
            } else {
                try {
                    balanceEA.setText(Helper.toRupiahFormat(
                            String.valueOf(new BigDecimal(list.get(0).getGapEa()).setScale(0, BigDecimal.ROUND_HALF_UP))));
                    balanceEAString = Helper.toRupiahFormat(
                            String.valueOf(new BigDecimal(list.get(0).getGapEa()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                } catch (NumberFormatException ignored) {
                }
            }
        } else {
            balanceEA.setText(Constants.ZERO);
            balanceEAString = Constants.ZERO;
        }

        try {
            totalReturn.setText(String.valueOf(db.sizeReturnHeader()));
        } catch (NullPointerException ignored) {

        }
        if(typeChart == 1) {
            setDataSales(txtBalanceSalesMonthly, "Sales", balanceSalesString, chartMonthlySales);
            setDataSales(txtBalanceCallMonthly, "Call", balanceCallString, chartMonthlyCall);
            setDataSales(txtBalanceEAMonthly, "EA", balanceEAString, chartMonthlyEA);
            setDataSales(txtBalanceECMonthly, "EC", balanceECString, chartMonthlyEC);
        }else{
            setDataSales(txtBalanceSalesDaily, "Sales", balanceSalesString, chartDailySales);
            setDataSales(txtBalanceCallDaily, "Call", balanceCallString, chartDailyCall);
            setDataSales(txtBalanceEADaily, "EA", balanceEAString, chartDailyEA);
            setDataSales(txtBalanceECDaily, "EC", balanceECString, chartDailyEC);
        }
    }

    private void setDataSales(TextView title, String type, String balance, BarChart chart) {
        title.setText(type + " (Balance/GAP : " + (balance != null ?balance : "-") + ")");
        setFormatSeparator();

        List<BarEntry> entries = new ArrayList<>();
        final String[] finalQuarters = {"Target", "Actual"};
        final String[] finalQuarters2;
        String label = "";

        float target1, actual1;

        switch (type){
            case "Sales":
                target1 = targetSalesFloat;
                actual1 = actualSalesFloat;
                break;
            case "Call":
                target1 = targetCallFloat;
                actual1 = actualCallFloat;
                break;
            case "EA":
                target1 = targetEAFloat;
                actual1 = actualEAFloat;
                break;
            case "EC":
                target1 = targetECFloat;
                actual1 = actualECFloat;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        entries.add(new BarEntry(1f, target1));
        entries.add(new BarEntry(2f, actual1));

        finalQuarters2 = new String[]{"Target (" + format.format(target1) + ")", "Actual (" + format.format(actual1) + ")"};


        BarDataSet set = new BarDataSet(entries, label);
        set.setColors(new int[]{R.color.new_blue, R.color.new_pink}, getContext());
        set.setValueFormatter(new MyValueFormatter());

        BarData data = new BarData(set);

        IndexAxisValueFormatter formatter1 = new IndexAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                int val = (int) (value) - 1;
                if (val >= 0) {
                    if (finalQuarters.length > val) {
                        return finalQuarters[val];
                    } else return "";
                } else {
                    return "";
                }
            }
        };

        IndexAxisValueFormatter formatter2 = new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int val = (int) (value) - 1;
                if (val >= 0) {
                    if (finalQuarters2.length > val) {
                        return finalQuarters2[val];
                    } else return "";
                } else {
                    return "";
                }
            }
        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(finalQuarters.length, false);
        xAxis.setValueFormatter(formatter1);
        xAxis.setDrawGridLines(true);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XYMarkerView mv;
        mv = new XYMarkerView(getContext(), formatter2);

        mv.setChartView(chart); // For bounds control
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setPinchZoom(true);
        chart.animateY(3000, Easing.EaseOutBack);
        chart.setMarker(mv); // Set the marker to the chart
        chart.invalidate(); // refresh
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER) != null) {
            summaryRequest = (SummaryRequest) Helper.getItemParam(Constants.SUMMARY_ITEM_FILTER);
            summaryRequest.setIdEmployee(user.getIdEmployee());
            summaryRequest.setIdOutlet(Constants.EMPTY_STRING);
        } else {
            summaryRequest = new SummaryRequest();
            summaryRequest.setIdEmployee(user.getIdEmployee());
            summaryRequest.setIdOutlet(Constants.EMPTY_STRING);
            summaryRequest.setDateFrom(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
            summaryRequest.setDateTo(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
            summaryRequest.setMatClass(Constants.EMPTY_STRING);
        }

        if (Helper.getItemParam(Constants.SUMMARY_TYPE) != null) {
            if (Helper.getItemParam(Constants.SUMMARY_TYPE).equals(Constants.MONTH)) {
                type = 0;
                setTypeLayout();
//                tabHost.setCurrentTab(0);

                summaryRequest.setDateFrom(Constants.EMPTY_STRING);
                summaryRequest.setDateTo(Constants.EMPTY_STRING);

                PARAM = 1;
                requestUrl();
            } else {
                type = 1;
                setTypeLayout();
//                tabHost.setCurrentTab(1);
                PARAM = 2;
                requestUrl();
            }
        } else {
            PARAM = 3;
            requestUrl();
            progress.show();
        }
    }

    private void requestUrl() {
        new RequestUrl().execute();
        spinner.setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem filter = menu.findItem(R.id.action_filter);
        filter.setVisible(true);

        filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                preventDupeDialog();
                Helper.setItemParam(Constants.ACTIVE_MENU, getResources().getString(R.string.navmenu6a));
//                Helper.setItemParam(Constants.SUMMARY_TYPE, tabHost.getCurrentTabTag());
                Helper.setItemParam(Constants.SUMMARY_TYPE, type == 0 ? Constants.MONTH : Constants.DAY);
                openDialog(DIALOG_FILTER);

                return false;
            }
        });
    }

    @Override
    public void onPause() {
//        Helper.setItemParam(Constants.SUMMARY_TYPE, tabHost.getCurrentTabTag());
        Helper.setItemParam(Constants.SUMMARY_TYPE, type == 0 ? Constants.MONTH : Constants.DAY);
        super.onPause();
    }

    private void setTypeLayout() {
        switch (type) {
            case 0:
                btnMonthly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_target_selected));
                btnMonthly.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                btnDaily.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_target_unselected));
                btnDaily.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));
                layoutMonthly.setVisibility(View.VISIBLE);
                layoutDaily.setVisibility(View.GONE);
                break;
            case 1:
                btnMonthly.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_target_unselected));
                btnMonthly.setTextColor(ContextCompat.getColor(getActivity(), R.color.caldroid_gray));
                btnDaily.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_target_selected));
                btnDaily.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                layoutMonthly.setVisibility(View.GONE);
                layoutDaily.setVisibility(View.VISIBLE);
                break;
        }
    }
}
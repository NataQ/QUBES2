package id.co.qualitas.qubes.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.adapter.NewOutletPAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitDateResponse;

public class CalendarFragment extends BaseFragment {
    private TextView userName;
    private TextView userNik;
    private TextView userDesc;
    private TextView supervisorNik;
    private TextView supervisorName;
    private TextView supervisorPosition;
    private TextView totalOutlet;

    private ArrayList<OutletResponse> outletResponseArrayList = new ArrayList<>();
    private ArrayList<OutletResponse> loadedList = new ArrayList<>();
    private ArrayList<VisitDateResponse> visitCalendar = new ArrayList<>();

    private User user = new User();
    private RecyclerView recycleOutlet;
    private CaldroidCustomFragment caldroidFragment;
    private Calendar cal;
    private ColorDrawable outletColor;
    private Drawable outletDrawable;

    private LinearLayout calendar, supervisorDetail;
    private LinearLayout linearProgress;

    private String clientName = "";
    private int totalSelected = 0;
    private int monthNow = 0;
    private int lastVisibleItem;
    private int PARAM_LOADING = 0;

    protected Handler handler;

    private NewOutletPAdapter nAdapter;
    boolean searchVisible = false;
    private ImageView imgSearch;
    private EditText edtTxtSearch;
    private NestedScrollView scroll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

           getActivity().setTitle(getString(R.string.navmenu1));
//        ((MainActivityDrawer) getActivity()).enableBackToolbar(false);

        init();
        initFragment();
        initialize();

        selectFirstMenu();
        setDataMatrix();

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchVisible) {
                    searchVisible = false;
                    edtTxtSearch.setVisibility(View.GONE);
                    setDataPortrait(outletResponseArrayList);
                } else {
                    searchVisible = true;
                    edtTxtSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        edtTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence newText, int i, int i1, int i2) {
                if (newText == null || newText.toString().trim().isEmpty()) {
                    if (outletResponseArrayList != null && !outletResponseArrayList.isEmpty()) {
                        setDataPortrait(outletResponseArrayList);
                    }
                }

                final ArrayList<OutletResponse> filteredList = new ArrayList<>();

                if(outletResponseArrayList != null && !outletResponseArrayList.isEmpty()){
                    for (OutletResponse outlet : outletResponseArrayList) {
                        final String text = Helper.validateResponseEmpty(outlet.getOutletName()).toLowerCase().concat(getString(R.string.space)).concat(Helper.validateResponseEmpty(outlet.getIdOutlet()).toLowerCase());
                        if (text.contains(newText)) {
                            filteredList.add(outlet);
                        }
                    }
                }
                setDataPortrait(filteredList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setCalendar(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));

        functionLoad();

        recycleOutlet.removeAllViews();
        getData(db.getTopIndexOutlet());

        functionOnItemClickAdapter(outletResponseArrayList);

        setTotalOutlet(totalSelected);

        return rootView;
    }

    private void selectFirstMenu() {
//        FragmentDrawer.updateSelected(0);
    }

    private void functionLoad() {
        try {
            scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    View view = (View) scroll.getChildAt(scroll.getChildCount() - 1);

                    int diff = (view.getBottom() - (scroll.getHeight() + scroll.getScrollY()));

                    if (diff == 0) {
//                        lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                        if (PARAM_LOADING == 0) {
//                            if (lastVisibleItem == outletResponseArrayList.size() - 1) {

                                try {
                                    try{
                                        fetchData(String.valueOf(Integer.parseInt(outletResponseArrayList.get(outletResponseArrayList.size() - 1).getIndex()) + 1));
                                    }catch (NumberFormatException e){

                                    }
                                } catch (IndexOutOfBoundsException ignored) {

                                }
//                            }
                        }
                    }
                }
            });

//            recycleOutlet.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    try {
//                        lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//
//                        if (PARAM_LOADING == 0) {
//                            if (lastVisibleItem == outletResponseArrayList.size() - 1) {
//
//                                try {
//                                    try{
//                                        fetchData(String.valueOf(Integer.parseInt(outletResponseArrayList.get(outletResponseArrayList.size() - 1).getIndex()) + 1));
//                                    }catch (NumberFormatException e){
//
//                                    }
//                                } catch (IndexOutOfBoundsException ignored) {
//
//                                }
//                            }
//                        }
//
//                    } catch (NullPointerException ignored) {
//
//                    }
//                }
//
//            });
        } catch (NullPointerException ignored) {

        }
    }

    private void fetchData(String lastIndex) {
        try {
            getData(lastIndex);
        } catch (NullPointerException ignored) {

        }
    }

    private void getData(String lastIndex) {
        new LoadData(lastIndex).execute();
    }

    private void setTotalOutlet(int total) {
        totalOutlet.setText(getResources().getString(R.string.totalOutlet).concat(String.valueOf(total)));
        totalSelected = 0;
    }

    private void setDataMatrix() {
        if (user != null) {
            if (user.getSupervisorNik() != null) {
                setDataSupervisor();
            } else {
                supervisorDetail.setVisibility(View.GONE);
                setDataUser();
            }
        }
    }

    private void setDataUser() {
        userName.setText(user.getFullName());
        userNik.setText(user.getNik());
        userDesc.setText(user.getPosition());
    }

    private void setDataSupervisor() {
        supervisorNik.setText(user.getSupervisorNik());
        supervisorName.setText(user.getSupervisorName());
        supervisorPosition.setText(user.getSupervisorPosition());
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClicked(int position, int id);
    }

    private void initCalendar(String clientName) {
        clearPrevOutlet(visitCalendar);

        visitCalendar = new ArrayList<>();
        visitCalendar = db.getVisitByClient(clientName);

        this.clientName = clientName;

        if (!visitCalendar.isEmpty()) {
            String dateString = Helper
                    .parseDateString(visitCalendar.get(0).getVisitDate(), Constants.DATE_TYPE_2,
                            Constants.DATE_TYPE_12);
            String temp[] = dateString.split(Constants.DATE_SPLIT_SYMBOL);
            setCalendar(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
        } else {
            setCalendar(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        }
    }

    public void initializeCalendar(int month, int year) {
        caldroidFragment = new CaldroidCustomFragment();
        Bundle args = new Bundle();
        args.putInt(CaldroidCustomFragment.THEME_RESOURCE, R.style.CaldroidDefaultCustom);
        args.putInt(CaldroidCustomFragment.MONTH, month);
        args.putInt(CaldroidCustomFragment.YEAR, year);
        caldroidFragment.setArguments(args);
        colorCalendar();

        if (calendar != null) {
            FragmentTransaction t = getChildFragmentManager().beginTransaction();
            t.replace(R.id.calendar1, caldroidFragment);
            t.commit();
        }
    }

    private void setCalendar(final int month, int year) {
        initializeCalendar(month, year);

        setCalendarColor(visitCalendar);

        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                List<Date> listDate = new ArrayList<Date>();

                String temp = Helper.convertDateToString(Constants.DATE_TYPE_2, date);

                outletResponseArrayList = db.getAllVisitPlan(temp);
                if (outletResponseArrayList != null && !outletResponseArrayList.isEmpty()) {
                    setDataPortrait(outletResponseArrayList, true);
                    totalSelected = outletResponseArrayList.size();
                } else {
                    outletResponseArrayList = db.getLoadOutletData(db.getTopIndexOutlet());
                    setDataPortrait(outletResponseArrayList, false);
                }

                cal.setTime(date);
                setCalendar(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));

                caldroidFragment.clearBackgroundDrawableForDates(listDate);

                for (Date cur : listDate) {
                    if (monthNow == cur.getMonth() + 1) {
                        caldroidFragment.setTextColorForDate(R.color.grey, cur);
                    }
                }

                clientName = Constants.EMPTY_STRING;
                clearPrevOutlet(visitCalendar);
                caldroidFragment.setBackgroundDrawableForDate(outletDrawable, date);
                caldroidFragment.setTextColorForDate(R.color.new_pink, date);

                caldroidFragment.refreshView();

                setTotalOutlet(totalSelected);
                sortOutletThatHovered();

            }

            @Override
            public void onChangeMonth(int month, int year) {
                super.onChangeMonth(month, year);

                visitCalendar = db.getVisitByClient(clientName);
                clearPrevOutlet(visitCalendar);
                monthNow = month;

                ArrayList<VisitDateResponse> newVisitCalendar = new ArrayList<VisitDateResponse>();
                for (VisitDateResponse visitDate : visitCalendar) {
                    String monthVisit = visitDate.getVisitDate().substring(5, 7);
                    int realMonth = Integer.parseInt(monthVisit);
                    if (month == realMonth) {
                        newVisitCalendar.add(visitDate);
                    }

                }
                setCalendarColor(newVisitCalendar);

            }
        });
    }

    private void sortOutletThatHovered() {
        Collections.sort(outletResponseArrayList, new Comparator<OutletResponse>() {

            @Override
            public int compare(OutletResponse o1, OutletResponse o2) {

                boolean b1 = o1.isColor();
                boolean b2 = o2.isColor();

                if (b1 != b2) {

                    if (b1) {
                        return -1;
                    }

                    return 1;
                }
                return 0;

            }
        });
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        scroll = rootView.findViewById(R.id.scroll);
        userName = rootView.findViewById(R.id.userName);
        userNik = rootView.findViewById(R.id.userNik);
        userDesc = rootView.findViewById(R.id.userDesc);

        imgSearch = rootView.findViewById(R.id.imgSearch);
        edtTxtSearch = rootView.findViewById(R.id.edtTxtSearch);

        linearProgress = rootView.findViewById(R.id.linearProgress);

        recycleOutlet = rootView.findViewById(R.id.recycleOutlet);

        cal = Calendar.getInstance();
        calendar = rootView.findViewById(R.id.calendar1);

        supervisorNik = rootView.findViewById(R.id.supervisorNik);
        supervisorName = rootView.findViewById(R.id.supervisorName);
        supervisorPosition = rootView.findViewById(R.id.supervisorPosition);

        supervisorDetail = rootView.findViewById(R.id.linearSupervisorDetail);
        totalOutlet = rootView.findViewById(R.id.totalOutlet);
        handler = new Handler();

    }

    private Date parseDate(int x, ArrayList<VisitDateResponse> visitCalendar) {
        String dateString = Helper
                .parseDateString(visitCalendar.get(x).getVisitDate(), Constants.DATE_TYPE_2,
                        Constants.DATE_TYPE_12);
        String temp[] = dateString.split(Constants.DATE_SPLIT_SYMBOL);
        int day = Integer.parseInt(temp[0]);
        int month = Integer.parseInt(temp[1]);
        int year = Integer.parseInt(temp[2]);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        return cal.getTime();
    }

    private void setCalendarColor(ArrayList<VisitDateResponse> visitCalendar) {
        clearPrevOutlet(this.visitCalendar);
        visitCalendar = db.getVisitByClient(clientName);

        ArrayList<VisitDateResponse> newVisitCalendar = new ArrayList<VisitDateResponse>();
        for (VisitDateResponse dateResponse : visitCalendar) {
            String monthVisit = dateResponse.getVisitDate().substring(5, 7);
            int realMonth = Integer.parseInt(monthVisit);
            if (monthNow == realMonth) {
                newVisitCalendar.add(dateResponse);
            }
        }

        clearPrevOutlet(visitCalendar);

        HashMap selectedDatesBackground = new HashMap();
        HashMap selectedDatesText = new HashMap();
        // Put elements to the map
        for (int i = 0; i < newVisitCalendar.size(); i++) {
            selectedDatesBackground.put(parseDate(i, newVisitCalendar), outletDrawable);
            selectedDatesText.put(parseDate(i, newVisitCalendar), R.color.new_pink);
        }
        caldroidFragment.setBackgroundDrawableForDates(selectedDatesBackground);
        caldroidFragment.setTextColorForDates(selectedDatesText);

        caldroidFragment.refreshView();
    }

    private void clearPrevOutlet(ArrayList<VisitDateResponse> visitCalendar) {
        for (int x = 0; x < visitCalendar.size(); x++) {
            Date selectedDate = parseDate(x, visitCalendar);

            String monthVisit = visitCalendar.get(x).getVisitDate().substring(5, 7);
            int realMonth = Integer.parseInt(monthVisit);

            if (monthNow == realMonth) {
                caldroidFragment.clearBackgroundDrawableForDate(selectedDate);
                caldroidFragment.setTextColorForDate(R.color.white, selectedDate);
            } else {
                caldroidFragment.clearBackgroundDrawableForDate(selectedDate);
                caldroidFragment.setTextColorForDate(R.color.grey, selectedDate);
            }
        }
    }

    private void colorCalendar() {
        outletColor = new ColorDrawable(getResources().getColor(R.color.white));
        outletDrawable = getResources().getDrawable(R.drawable.textview_rounded_white);
    }


    private void setLayoutManager() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        recycleOutlet.setLayoutManager(mLayoutManager);
        recycleOutlet.setNestedScrollingEnabled(false);
        recycleOutlet.setItemAnimator(new DefaultItemAnimator());
        recycleOutlet.setAdapter(nAdapter);
    }

    private void setDataPortrait(final ArrayList<OutletResponse> mData) {
        nAdapter = new NewOutletPAdapter(mData, CalendarFragment.this);
        setLayoutManager();
        functionOnItemClickAdapter(mData);
    }

    private void setDataPortrait(final ArrayList<OutletResponse> mData, boolean selected) {
        nAdapter = new NewOutletPAdapter(mData, CalendarFragment.this, selected);
        setLayoutManager();
        functionOnItemClickAdapter(mData);
    }

    private void setDataPortrait(final ArrayList<OutletResponse> mData, int pos) {
        nAdapter = new NewOutletPAdapter(mData, CalendarFragment.this, pos);
        setLayoutManager();
        functionOnItemClickAdapter(mData);
    }

    private void functionOnItemClickAdapter(final ArrayList<OutletResponse> mData) {
        try {
            nAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onRecyclerViewItemClicked(int position, int id) {
                    setTotalOutlet(1);
                    setDataPortrait(mData, position);
                    initCalendar(mData.get(position).getOutletName());

                }
            });
        } catch (NullPointerException ignored) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem search = menu.findItem(R.id.action_search);

        search.setVisible(true);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.toolbarSearchHint));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            if (outletResponseArrayList != null && !outletResponseArrayList.isEmpty()) {
                setDataPortrait(outletResponseArrayList);
            }
            return false;
        }

        final ArrayList<OutletResponse> filteredList = new ArrayList<>();

        if(outletResponseArrayList != null && !outletResponseArrayList.isEmpty()){
            for (OutletResponse outlet : outletResponseArrayList) {
                final String text = Helper.validateResponseEmpty(outlet.getOutletName()).toLowerCase().concat(getString(R.string.space)).concat(Helper.validateResponseEmpty(outlet.getIdOutlet()).toLowerCase());
                if (text.contains(newText)) {
                    filteredList.add(outlet);
                }
            }
        }
        setDataPortrait(filteredList);
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadData extends AsyncTask<Void, Void, Boolean> {
        String curIndex = null;

        LoadData(String lastIndex) {
            curIndex = lastIndex;
            loadedList = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                loadedList = db.getLoadOutletData(curIndex);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            PARAM_LOADING = 1;
            linearProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            if (msg != null) {
                if (msg) {
                    if (loadedList != null && !loadedList.isEmpty()) {
                        if (!outletResponseArrayList.contains(loadedList)) {
                            outletResponseArrayList.addAll(loadedList);
                        }
//                        if (nAdapter != null) {
                            setDataPortrait(outletResponseArrayList);//kalau pakai notifyDataSetChanged gak muncul
//                            nAdapter.notifyDataSetChanged();
//                        } else {
//                            setDataPortrait(outletResponseArrayList);
//                        }
                    }
                } else {
                    Toast.makeText(getContext(), R.string.failedLoaded, Toast.LENGTH_SHORT).show();
                }
            }

            PARAM_LOADING = 0;
            linearProgress.setVisibility(View.GONE);
        }
    }
}
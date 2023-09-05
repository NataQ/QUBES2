package id.co.qualitas.qubes.adapter.aspp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CollectionFormActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.CollectionCheque;
import id.co.qualitas.qubes.model.CollectionGiro;

public class CollectionChequeAdapter extends RecyclerView.Adapter<CollectionChequeAdapter.Holder> implements Filterable {
    private List<CollectionCheque> mList;
    private List<CollectionCheque> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionFormActivity mContext;
    private OnAdapterListener onAdapterListener;
    private boolean visible = false;
    Date todayDate;
    String chooseDateString, todayString;
    Calendar todayCalendar;
    private CollectionChequePaymentAdapter mAdapter;

    public CollectionChequeAdapter(CollectionFormActivity mContext, List<CollectionCheque> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.onAdapterListener = onAdapterListener;
    }

    public void setData(List<CollectionCheque> mDataSet) {
        this.mList = mDataSet;
        this.mFilteredList = mDataSet;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mList;
                } else {
                    List<CollectionCheque> filteredList = new ArrayList<>();
                    for (CollectionCheque row : mList) {

                        /*filter by name*/
                        if (row.getNoCheque().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<CollectionCheque>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLeft, txtTglCheque, txtPrice, txtTglCair, spnBankName, spnBankCust;
        EditText txtPayment, edtNoCheque;
        RecyclerView recyclerView;
        ImageView imgView;
        LinearLayout llPayment, layout;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            spnBankCust = itemView.findViewById(R.id.spnBankCust);
            spnBankName = itemView.findViewById(R.id.spnBankName);
            txtTglCair = itemView.findViewById(R.id.txtTglCair);
            layout = itemView.findViewById(R.id.layout);
            llPayment = itemView.findViewById(R.id.llPayment);
            imgView = itemView.findViewById(R.id.imgView);
            edtNoCheque = itemView.findViewById(R.id.edtNoCheque);
            txtLeft = itemView.findViewById(R.id.txtLeft);
            txtTglCheque = itemView.findViewById(R.id.txtTglCheque);
            txtPayment = itemView.findViewById(R.id.txtPayment);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setHasFixedSize(true);
            this.onAdapterListener = onAdapterListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAdapterListener.onAdapterClick(mFilteredList.get(getAdapterPosition()));
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_row_view_coll_cheque, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        CollectionCheque detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        todayDate = Helper.getTodayDate();
        todayString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(todayDate);

        holder.txtTglCheque.setText(Helper.getTodayDate(Constants.DATE_FORMAT_4));
        holder.txtTglCair.setText(Helper.getTodayDate(Constants.DATE_FORMAT_4));

        holder.txtTglCheque.setOnClickListener(v -> {
            mContext.hideKeyboard();
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
                    holder.txtTglCheque.setText(chooseDateString);
                    holder.txtTglCheque.setError(null);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(mContext, R.style.DialogTheme, dateSetListener, year, month, date);
            dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
            dialog.show();
        });

        holder.txtTglCair.setOnClickListener(v -> {
            mContext.hideKeyboard();
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
                    holder.txtTglCair.setText(chooseDateString);
                    holder.txtTglCair.setError(null);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(mContext, R.style.DialogTheme, dateSetListener, year, month, date);
            dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
            dialog.show();
        });

        holder.spnBankName.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(mContext);

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

            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(mContext, groupList, (nameItem, adapterPosition) -> {
                holder.spnBankName.setText(nameItem);
                alertDialog.dismiss();
            });

            LinearLayoutManager mManager = new LinearLayoutManager(mContext);
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

        holder.spnBankCust.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(mContext);

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

            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(mContext, groupList, (nameItem, adapterPosition) -> {
                holder.spnBankCust.setText(nameItem);
                alertDialog.dismiss();
            });

            LinearLayoutManager mManager = new LinearLayoutManager(mContext);
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

        mAdapter = new CollectionChequePaymentAdapter(mContext, detail.getMaterialList(), header -> {

        });
        holder.recyclerView.setAdapter(mAdapter);

        holder.layout.setOnClickListener(v -> {
            if (visible) {
                visible = false;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_up));
                holder.llPayment.setVisibility(View.VISIBLE);
            } else {
                visible = true;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_down_aspp));
                holder.llPayment.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(CollectionCheque detail);
    }
}

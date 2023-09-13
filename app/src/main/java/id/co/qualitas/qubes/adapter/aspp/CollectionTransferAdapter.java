package id.co.qualitas.qubes.adapter.aspp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import id.co.qualitas.qubes.model.CollectionTransfer;
import id.co.qualitas.qubes.model.Material;

public class CollectionTransferAdapter extends RecyclerView.Adapter<CollectionTransferAdapter.Holder> implements Filterable {
    private List<CollectionTransfer> mList;
    private List<CollectionTransfer> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionFormActivity mContext;
    private OnAdapterListener onAdapterListener;
    boolean visible = false;
    Date todayDate;
    String chooseDateString, todayString;
    Calendar todayCalendar;
    private CollectionTransferPaymentAdapter mAdapter;

    public CollectionTransferAdapter(CollectionFormActivity mContext, List<CollectionTransfer> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<CollectionTransfer> mDataSet) {
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
                    List<CollectionTransfer> filteredList = new ArrayList<>();
                    for (CollectionTransfer row : mList) {

                        /*filter by name*/
                        if (row.getTglTransfer().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<CollectionTransfer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLeft, txtTglTransfer, txtPrice;
        LinearLayout llPayment, layout;
        ImageView imgView;
        EditText txtPayment;
        RecyclerView recyclerView;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            llPayment = itemView.findViewById(R.id.llPayment);
            imgView = itemView.findViewById(R.id.imgView);
            txtLeft = itemView.findViewById(R.id.txtLeft);
            txtTglTransfer = itemView.findViewById(R.id.txtTglTransfer);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_coll_trarnsfer, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        CollectionTransfer detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        todayDate = Helper.getTodayDate();
        todayString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(todayDate);

        holder.txtTglTransfer.setText(Helper.getTodayDate(Constants.DATE_FORMAT_4));

        holder.txtTglTransfer.setOnClickListener(v -> {
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
                    holder.txtTglTransfer.setText(chooseDateString);
                    holder.txtTglTransfer.setError(null);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(mContext, dateSetListener, year, month, date);
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            dialog.getDatePicker().setLayoutParams(params);
//            dialog.getWindow().setLayout(100,200);
            dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
            dialog.show();
        });

        mAdapter = new CollectionTransferPaymentAdapter(mContext, detail.getMaterialList(), header -> {

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
        void onAdapterClick(CollectionTransfer detail);
    }
}
package id.co.qualitas.qubes.adapter.aspp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private List<Material> materialList = new ArrayList<>();
    private Holder dataObjectHolder;
    double totalPayment, left;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

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
        LinearLayout llPayment, layout, llDelete;
        CardView card_view;
        ImageView imgView;
        EditText edtPayment;
        RecyclerView recyclerView;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            llDelete = itemView.findViewById(R.id.llDelete);
            card_view = itemView.findViewById(R.id.card_view);
            layout = itemView.findViewById(R.id.layout);
            llPayment = itemView.findViewById(R.id.llPayment);
            imgView = itemView.findViewById(R.id.imgView);
            txtLeft = itemView.findViewById(R.id.txtLeft);
            txtTglTransfer = itemView.findViewById(R.id.txtTglTransfer);
            edtPayment = itemView.findViewById(R.id.edtPayment);
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
        dataObjectHolder = new Holder(itemView, onAdapterListener);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        setFormatSeparator();
        CollectionTransfer detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        materialList = detail.getMaterialList();
        todayDate = Helper.getTodayDate();
        todayString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(todayDate);

        if (!Helper.isNullOrEmpty(detail.getTglTransfer())) {
            String date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_4, detail.getTglTransfer());
            holder.txtTglTransfer.setText(date);
        } else {
            holder.txtTglTransfer.setText(null);
        }
        holder.edtPayment.setText(Helper.setDotCurrencyAmount(detail.getTotalPayment()));

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

                    chooseDateString = new SimpleDateFormat(Constants.DATE_FORMAT_4).format(calendar.getTime());
                    String tglTf = new SimpleDateFormat(Constants.DATE_FORMAT_3).format(calendar.getTime());
                    detail.setTglTransfer(tglTf);
                    holder.txtTglTransfer.setText(chooseDateString);
                    holder.txtTglTransfer.setError(null);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(mContext, dateSetListener, year, month, date);
//            dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
            dialog.show();
        });

        holder.edtPayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Helper.setDotCurrency(holder.edtPayment, this, s);
                if (!s.toString().equals("") && !s.toString().equals("-")) {
                    double qty = Double.parseDouble(s.toString().replace(",", ""));
                    if (qty < 0) {
                        Toast.makeText(mContext, "Tidak boleh kurang dari 0", Toast.LENGTH_SHORT).show();
                        holder.edtPayment.setText(s.toString().substring(0, s.toString().length() - 1));
                    } else {
                        totalPayment = qty;
                        detail.setTotalPayment(totalPayment);
                    }
                } else {
                    totalPayment = 0;
                    detail.setTotalPayment(0);
                }
                setLeft();
            }
        });

        mAdapter = new CollectionTransferPaymentAdapter(mContext, CollectionTransferAdapter.this, materialList, header -> {

        });
        holder.recyclerView.setAdapter(mAdapter);

        holder.llDelete.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogView);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView txtTitle = dialog.findViewById(R.id.txtTitle);
            TextView txtDialog = dialog.findViewById(R.id.txtDialog);
            Button btnNo = dialog.findViewById(R.id.btnNo);
            Button btnYes = dialog.findViewById(R.id.btnYes);
            txtTitle.setText("Hapus");
            txtDialog.setText("Anda yakin?");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFilteredList.remove(holder.getAbsoluteAdapterPosition());
                    notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                    dialog.dismiss();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        holder.card_view.setOnClickListener(v -> {
            if (visible) {
                visible = false;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_down_aspp));
                holder.llPayment.setVisibility(View.GONE);
            } else {
                visible = true;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_up));
                holder.llPayment.setVisibility(View.VISIBLE);
            }
        });
    }

    public double getTotalAmount() {
        return totalPayment;
    }

    public void setLeft() {
        double totalPaid = 0;
        for (Material mat : materialList) {
            if (mat.isChecked()) {
                totalPaid = totalPaid + mat.getAmountPaid();
            }
        }

        left = totalPayment - totalPaid;
        dataObjectHolder.txtLeft.setText("Rp." + format.format(left));
    }

    public double calculateLeft(double qty, int pos) {
        double totalPaid = 0;
        for (int i = 0; i < materialList.size(); i++) {
            Material mat = materialList.get(i);
            if (mat.isChecked()) {
                if (i == pos) {
                    totalPaid = totalPaid + qty;
                } else {
                    totalPaid = totalPaid + mat.getAmountPaid();
                }
            }
        }
        left = totalPayment - totalPaid;
        return left;
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(CollectionTransfer detail);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}

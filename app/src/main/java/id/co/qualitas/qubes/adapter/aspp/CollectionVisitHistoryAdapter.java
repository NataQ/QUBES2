package id.co.qualitas.qubes.adapter.aspp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CollectionVisitActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Customer;

public class CollectionVisitHistoryAdapter extends RecyclerView.Adapter<CollectionVisitHistoryAdapter.Holder> implements Filterable {
    private List<CollectionHeader> mList;
    private List<CollectionHeader> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionVisitActivity mContext;
    private OnAdapterListener onAdapterListener;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private Dialog alertDialog;
    private View dialogview;
    private LayoutInflater inflater;
    private Customer outletHeader;

    public CollectionVisitHistoryAdapter(CollectionVisitActivity mContext, Customer outletHeader, List<CollectionHeader> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.outletHeader = outletHeader;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.onAdapterListener = onAdapterListener;
    }

    public void setData(List<CollectionHeader> mDataSet, Customer outletHeader) {
        this.mList = mDataSet;
        this.mFilteredList = mDataSet;
        this.outletHeader = outletHeader;
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
                    List<CollectionHeader> filteredList = new ArrayList<>();
                    for (CollectionHeader row : mList) {

                        /*filter by name*/
                        if (row.getInvoiceNo().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<CollectionHeader>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtAmount, txtPaid, txtInvoiceNo, txtInvoiceDate;
        LinearLayout llDelete, llHeader;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            llHeader = itemView.findViewById(R.id.llHeader);
            llDelete = itemView.findViewById(R.id.llDelete);
            txtInvoiceNo = itemView.findViewById(R.id.txtInvoiceNo);
            txtInvoiceDate = itemView.findViewById(R.id.txtInvoiceDate);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtPaid = itemView.findViewById(R.id.txtPaid);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_collection_visit_history, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        setFormatSeparator();
        CollectionHeader detail = mFilteredList.get(position);

        if (!Helper.isNullOrEmpty(detail.getInvoiceDate())) {
            String date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, detail.getInvoiceDate());
            holder.txtInvoiceDate.setText(date);
        }
        holder.txtInvoiceNo.setText(Helper.isEmpty(detail.getInvoiceNo(), "-"));
        holder.txtAmount.setText("Rp. " + format.format(detail.getInvoiceTotal()));
        holder.txtPaid.setText("Rp. " + format.format(detail.getTotalPaid()));

        if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
            if (detail.isDeleted()) {
                holder.llDelete.setVisibility(View.GONE);
            } else {
                holder.llDelete.setVisibility(View.VISIBLE);
            }
        } else {
            holder.llDelete.setVisibility(View.GONE);
        }

        if (detail.isDeleted()) {
            holder.llHeader.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_aspp));
        } else {
            holder.llHeader.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }

        holder.llDelete.setOnClickListener(v -> {
            inflater = LayoutInflater.from(mContext);
            alertDialog = new Dialog(mContext);
            initDialog(R.layout.aspp_dialog_confirmation);
            TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
            TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);
            Button btnNo = alertDialog.findViewById(R.id.btnNo);
            Button btnYes = alertDialog.findViewById(R.id.btnYes);
            txtTitle.setText("Delete Payment");
            txtDialog.setText("Are you sure want to delete this payment?");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
//                        if (detail.getTypePayment().equals("invoice")) {
                        new Database(mContext).deletePayment(detail);
//                        new Database(mContext).deleteAllCollection(detail);
//                        } else {
//                            new Database(mContext).updateOrderAmount(detail);
//                        }
//                        mFilteredList.remove(holder.getAbsoluteAdapterPosition());
//                        notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                        mContext.refreshData();
                        Toast.makeText(mContext, "Success remove item", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Failed remove item", Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        });
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(CollectionHeader CollectionHeader);
    }

    private void initDialog(int resource) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialogview = inflater.inflate(resource, null);
        alertDialog.setContentView(dialogview);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);//back di hp
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
//        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}

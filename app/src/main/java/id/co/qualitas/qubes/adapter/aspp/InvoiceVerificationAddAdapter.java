package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.InvoiceVerificationActivity;
import id.co.qualitas.qubes.activity.aspp.InvoiceVerificationAddActivity;
import id.co.qualitas.qubes.model.Invoice;

public class InvoiceVerificationAddAdapter extends RecyclerView.Adapter<InvoiceVerificationAddAdapter.Holder> implements Filterable {
    private List<Invoice> mList;
    private List<Invoice> mFilteredList;
    private LayoutInflater mInflater;
    private InvoiceVerificationAddActivity mContext;
    private OnAdapterListener onAdapterListener;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

    public InvoiceVerificationAddAdapter(InvoiceVerificationAddActivity mContext, List<Invoice> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<Invoice> mDataSet) {
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
                    List<Invoice> filteredList = new ArrayList<>();
                    for (Invoice row : mList) {

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
                mFilteredList = (ArrayList<Invoice>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtInvoiceNo, txtCustName, txtAmount, txtDate, txtRoute;
        RelativeLayout layoutCheck;
        ImageView imgChecked;
        View viewRoute;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtInvoiceNo = itemView.findViewById(R.id.txtInvoiceNo);
            txtCustName = itemView.findViewById(R.id.txtCustName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
            layoutCheck = itemView.findViewById(R.id.layoutCheck);
            imgChecked = itemView.findViewById(R.id.imgChecked);
            txtRoute = itemView.findViewById(R.id.txtRoute);
            viewRoute = itemView.findViewById(R.id.viewRoute);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_invoice_verification_add, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        setFormatSeparator();
        Invoice detail = mFilteredList.get(position);
        holder.txtCustName.setText(detail.getCustomerID() + " - " + detail.getCustomerName());
        holder.txtInvoiceNo.setText(detail.getInvoiceNo());
        holder.txtAmount.setText(format.format(detail.getAmount()) + " (" + format.format(detail.getPaid()) + ")");
        holder.txtDate.setText(detail.getDate());
        holder.txtRoute.setText(detail.getRoute());

        if (detail.isChecked()) {
            holder.imgChecked.setVisibility(View.VISIBLE);
        } else {
            holder.imgChecked.setVisibility(View.GONE);
        }

        switch (detail.getRoute().toLowerCase()) {
            case "non route":
                holder.viewRoute.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
                holder.txtRoute.setTextColor(ContextCompat.getColor(mContext, R.color.green));
                break;
            case "route":
                holder.viewRoute.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_krang));
                holder.txtRoute.setTextColor(ContextCompat.getColor(mContext, R.color.red_krang));
                break;
        }

        holder.layoutCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail.isChecked()) {
                    holder.imgChecked.setVisibility(View.GONE);
                    detail.setChecked(false);
                } else {
                    holder.imgChecked.setVisibility(View.VISIBLE);
                    detail.setChecked(true);
                }
                mContext.setCheckAll();
            }
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
        void onAdapterClick(Invoice Invoice);
    }
}

package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CollectionVisitActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.CollectionHeader;

public class CollectionVisitHistoryAdapter extends RecyclerView.Adapter<CollectionVisitHistoryAdapter.Holder> implements Filterable {
    private List<CollectionHeader> mList;
    private List<CollectionHeader> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionVisitActivity mContext;
    private OnAdapterListener onAdapterListener;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

    public CollectionVisitHistoryAdapter(CollectionVisitActivity mContext, List<CollectionHeader> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<CollectionHeader> mDataSet) {
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
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
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
}

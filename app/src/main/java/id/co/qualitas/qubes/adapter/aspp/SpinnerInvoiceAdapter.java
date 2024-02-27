package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CollectionFormActivityNew;
import id.co.qualitas.qubes.listener.OnLoadMoreListener;
import id.co.qualitas.qubes.model.Invoice;

public class SpinnerInvoiceAdapter extends RecyclerView.Adapter<SpinnerInvoiceAdapter.Holder> implements Filterable {
    private List<Invoice> mList;
    private List<Invoice> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionFormActivityNew mContext;
    private OnAdapterListener onAdapterListener;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading = false;


    public SpinnerInvoiceAdapter(CollectionFormActivityNew mContext, List<Invoice> mList, OnAdapterListener onAdapterListener) {
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
                        if (row.getNo_invoice().toLowerCase().contains(charString.toLowerCase())) {
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
                mContext.setFilteredData(mFilteredList);
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout llHeader;
        TextView text;
        CardView cvUncheck, cvCheck;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            cvUncheck = itemView.findViewById(R.id.cvUncheck);
            text = itemView.findViewById(R.id.text);
            llHeader = itemView.findViewById(R.id.llHeader);
            cvCheck = itemView.findViewById(R.id.cvCheck);
            this.onAdapterListener = onAdapterListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAdapterListener.onAdapterClick(mFilteredList.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_spinner_filtered_item_product, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Invoice detail = mFilteredList.get(position);
        holder.text.setText(detail.getNo_invoice());
        if (detail.isCheckedInvoice()) {
            holder.cvUncheck.setVisibility(View.GONE);
            holder.cvCheck.setVisibility(View.VISIBLE);
        } else {
            holder.cvUncheck.setVisibility(View.VISIBLE);
            holder.cvCheck.setVisibility(View.GONE);
        }

        holder.llHeader.setOnClickListener(v -> {
            if (detail.isCheckedInvoice()) {
                detail.setCheckedInvoice(false);
                holder.cvUncheck.setVisibility(View.VISIBLE);
                holder.cvCheck.setVisibility(View.GONE);
            } else {
                detail.setCheckedInvoice(true);
                holder.cvUncheck.setVisibility(View.GONE);
                holder.cvCheck.setVisibility(View.VISIBLE);
            }
            mContext.setCheckedAll();
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Invoice Invoice, int adapterPosition);
    }
}

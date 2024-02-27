package id.co.qualitas.qubes.adapter.aspp;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CollectionFormActivityNew;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Invoice;

public class CollectionInvoiceChequeAdapter extends RecyclerView.Adapter<CollectionInvoiceChequeAdapter.Holder> implements Filterable {
    private List<Invoice> mList;
    private List<Invoice> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionFormActivityNew mContext;
    private OnAdapterListener onAdapterListener;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    protected boolean checked = true;
    private CollectionItemChequeAdapter mAdapter;
    boolean visible = false;
    CollectionChequeNewAdapter headerAdapter;
    int idHeader;

    public CollectionInvoiceChequeAdapter(CollectionFormActivityNew mContext, CollectionChequeNewAdapter headerAdapter, int idHeader, List<Invoice> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.headerAdapter = headerAdapter;
        this.mContext = mContext;
        this.idHeader = idHeader;
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
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNoFaktur, txtTglJatuhTempo, txtAmountInv, txtTotalPaid, txtLeftAmount;
        LinearLayout layout, llItem;
        ImageView imgView;
        RecyclerView recyclerView;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtNoFaktur = itemView.findViewById(R.id.txtNoFaktur);
            txtTglJatuhTempo = itemView.findViewById(R.id.txtTglJatuhTempo);
            txtLeftAmount = itemView.findViewById(R.id.txtLeftAmount);
            txtAmountInv = itemView.findViewById(R.id.txtAmountInv);
            txtTotalPaid = itemView.findViewById(R.id.txtTotalPaid);
            layout = itemView.findViewById(R.id.layout);
            llItem = itemView.findViewById(R.id.llItem);
            imgView = itemView.findViewById(R.id.imgView);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_coll_invoice, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        setFormatSeparator();
        Invoice detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());

        holder.layout.setOnClickListener(v -> {
            if (visible) {
                visible = false;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_down_aspp));
                holder.llItem.setVisibility(View.GONE);
            } else {
                visible = true;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_up));
                holder.llItem.setVisibility(View.VISIBLE);
            }
        });

        holder.txtNoFaktur.setText(Helper.isEmpty(detail.getNo_invoice(), ""));
        if (!Helper.isNullOrEmpty(detail.getTanggal_jatuh_tempo())) {
            String dueDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_1, detail.getTanggal_jatuh_tempo());
            holder.txtTglJatuhTempo.setText(dueDate);
        }
        holder.txtAmountInv.setText("Rp." + format.format(detail.getAmount()));
        holder.txtLeftAmount.setText("Rp." + format.format(0));
        holder.txtTotalPaid.setText("Rp." + format.format(0));

        mAdapter = new CollectionItemChequeAdapter(mContext, headerAdapter, CollectionInvoiceChequeAdapter.this, idHeader, holder.getAbsoluteAdapterPosition(), mFilteredList.get(holder.getAbsoluteAdapterPosition()).getMaterialList(), header -> {

        });
        holder.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Invoice Invoice);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}

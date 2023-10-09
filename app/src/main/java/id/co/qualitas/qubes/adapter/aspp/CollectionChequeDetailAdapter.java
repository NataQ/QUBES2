package id.co.qualitas.qubes.adapter.aspp;

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

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CollectionDetailActivity;
import id.co.qualitas.qubes.model.CollectionCheque;

public class CollectionChequeDetailAdapter extends RecyclerView.Adapter<CollectionChequeDetailAdapter.Holder> implements Filterable {
    private List<CollectionCheque> mList;
    private List<CollectionCheque> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionDetailActivity mContext;
    private OnAdapterListener onAdapterListener;
    private boolean visible = false;
    private CollectionPaymentDetailAdapter mAdapter;

    public CollectionChequeDetailAdapter(CollectionDetailActivity mContext, List<CollectionCheque> mList, OnAdapterListener onAdapterListener) {
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
        TextView txtTglCheque, txtTglCair, txtBankName, txtBankCust, txtNoCheque;
        RecyclerView recyclerView;
        ImageView imgView;
        LinearLayout llPayment, layout;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtNoCheque = itemView.findViewById(R.id.txtNoCheque);
            txtBankCust = itemView.findViewById(R.id.txtBankCust);
            txtBankName = itemView.findViewById(R.id.txtBankName);
            txtTglCair = itemView.findViewById(R.id.txtTglCair);
            layout = itemView.findViewById(R.id.layout);
            llPayment = itemView.findViewById(R.id.llPayment);
            imgView = itemView.findViewById(R.id.imgView);
            txtTglCheque = itemView.findViewById(R.id.txtTglCheque);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_coll_cheque_detail, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        CollectionCheque detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        holder.txtTglCheque.setText(detail.getTglCheque());
        holder.txtTglCair.setText(detail.getTglCair());
        holder.txtBankName.setText(detail.getIdBankASPP() + " - " + detail.getBankNameASPP());
        holder.txtBankCust.setText(detail.getIdBankCust() + " - " + detail.getBankCust());
        holder.txtNoCheque.setText(detail.getNoCheque());

        mAdapter = new CollectionPaymentDetailAdapter(mContext, detail.getMaterialList(), header -> {

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

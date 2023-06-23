package id.co.qualitas.qubes.adapter.aspp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.OrderActivity;
import id.co.qualitas.qubes.activity.aspp.StoreCheckActivity;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> implements Filterable {
    private List<Order> mList;
    private List<Order> mFilteredList;
    private LayoutInflater mInflater;
    private OrderActivity mContext;
    private OnAdapterListener onAdapterListener;

    public OrderAdapter(OrderActivity mContext, List<Order> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<Order> mDataSet) {
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
                    List<Order> filteredList = new ArrayList<>();
                    for (Order row : mList) {

                        /*filter by name*/
                        if (row.getTxtOrderCode().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<Order>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtOrderNo, txtOmzet, txtSoNo, txtStatus;
        Button btnDetail, btnPrint, btnDelete;
        View viewStatus;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            viewStatus = itemView.findViewById(R.id.viewStatus);
            txtOrderNo = itemView.findViewById(R.id.txtOrderNo);
            txtOmzet = itemView.findViewById(R.id.txtOmzet);
            txtSoNo = itemView.findViewById(R.id.txtSoNo);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            btnPrint = itemView.findViewById(R.id.btnPrint);
            btnDelete = itemView.findViewById(R.id.btnDelete);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_order, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Order detail = mFilteredList.get(position);
        holder.txtOrderNo.setText(detail.getTxtOrderCode());
        holder.txtOmzet.setText(detail.getTxtPriceNett());
        holder.txtSoNo.setText(detail.getSoNo());
        holder.txtStatus.setText(detail.getStatus());
        switch (detail.getStatus().toLowerCase()) {
            case "approve":
                holder.viewStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
                holder.txtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
                break;
            case "draft":
                holder.viewStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow_krang));
                holder.txtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.yellow_krang));
                break;
            case "rejected":
                holder.viewStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_krang));
                holder.txtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red_krang));
                break;
        }

        holder.btnDetail.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Order Order);
    }
}

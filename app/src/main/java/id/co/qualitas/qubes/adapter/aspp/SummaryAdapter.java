package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.aspp.SummaryFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.Holder> implements Filterable {
    private List<Order> mList;
    private List<Order> mFilteredList;
    private LayoutInflater mInflater;
    private SummaryFragment mContext;
    private OnAdapterListener onAdapterListener;

    public SummaryAdapter(SummaryFragment mContext, List<Order> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext.getContext());
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
        TextView txtOrderNo, txtDate, txtOmzet, txtOutlet, txtInvoiceNo, txtStatus;
        LinearLayout llStatus;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            llStatus = itemView.findViewById(R.id.llStatus);
            txtOrderNo = itemView.findViewById(R.id.txtOrderNo);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtOutlet = itemView.findViewById(R.id.txtOutlet);
            txtInvoiceNo = itemView.findViewById(R.id.txtInvoiceNo);
            txtOmzet = itemView.findViewById(R.id.txtOmzet);
            txtStatus = itemView.findViewById(R.id.txtStatus);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_summary, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Order detail = mFilteredList.get(position);
        holder.txtOrderNo.setText(!Helper.isEmpty(detail.getTxtOrderCode()) ? detail.getTxtOrderCode() : "-");
        holder.txtOmzet.setText(!Helper.isEmpty(detail.getTxtPriceNett()) ? detail.getTxtPriceNett() : "-");
        holder.txtInvoiceNo.setText(!Helper.isEmpty(detail.getSoNo()) ? detail.getSoNo() : "-");
        holder.txtDate.setText(!Helper.isEmpty(detail.getDate()) ? detail.getDate() : "-");
        holder.txtOutlet.setText(!Helper.isEmpty(detail.getOutlet()) ? detail.getOutlet() : "-");
        holder.txtStatus.setText(!Helper.isEmpty(detail.getStatus()) ? detail.getStatus() : "-");

        if (!Helper.isEmpty(detail.getStatus())) {
            switch (detail.getStatus().toLowerCase()) {
                case "approve":
                    holder.llStatus.setVisibility(View.VISIBLE);
                    holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext.getContext(), R.color.green3_aspp));
                    holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.green_aspp));
                    break;
                case "reject":
                    holder.llStatus.setVisibility(View.VISIBLE);
                    holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext.getContext(), R.color.red_aspp));
                    holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.red2_aspp));
                    break;
                case "pending":
                    holder.llStatus.setVisibility(View.VISIBLE);
                    holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext.getContext(), R.color.yellow3_aspp));
                    holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.yellow_aspp));
                    break;
                case "sync success":
                    holder.llStatus.setVisibility(View.VISIBLE);
                    holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext.getContext(), R.color.blue8_aspp));
                    holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.blue9_aspp));
                    break;
                default:
                    holder.llStatus.setVisibility(View.GONE);
                    holder.llStatus.setBackgroundTintList(null);
                    holder.txtStatus.setText("-");
            }
        } else {
            holder.llStatus.setVisibility(View.GONE);
            holder.llStatus.setBackgroundTintList(null);
            holder.txtStatus.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Order Order);
    }
}

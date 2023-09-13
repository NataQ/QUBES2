package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.VisitActivity;
import id.co.qualitas.qubes.model.Customer;

public class NooListAdapter extends RecyclerView.Adapter<NooListAdapter.Holder> implements Filterable {
    private List<Customer> mList;
    private List<Customer> mFilteredList;
    private LayoutInflater mInflater;
    private VisitActivity mContext;
    private OnAdapterListener onAdapterListener;

    public NooListAdapter(VisitActivity mContext, List<Customer> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<Customer> mDataSet) {
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
                    List<Customer> filteredList = new ArrayList<>();
                    for (Customer row : mList) {

                        /*filter by name*/
                        if (row.getNameCustomer().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtOutlet, txtAddress, txtNo;
        ImageView imgStatus;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtOutlet = itemView.findViewById(R.id.txtOutlet);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtNo = itemView.findViewById(R.id.txtNo);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_noo, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Customer detail = mFilteredList.get(position);
        holder.txtNo.setText(String.valueOf(position + 1));
        holder.txtAddress.setText(detail.getAddress());
        holder.txtOutlet.setText(detail.getIdCustomer() + " - " + detail.getNameCustomer());

        if (position == 1) {
            holder.imgStatus.setVisibility(View.VISIBLE);
        } else {
            holder.imgStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Customer Customer);
    }
}
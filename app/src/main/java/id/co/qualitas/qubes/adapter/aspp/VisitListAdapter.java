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

public class VisitListAdapter extends RecyclerView.Adapter<VisitListAdapter.Holder> implements Filterable {
    private List<Customer> mList;
    private List<Customer> mFilteredList;
    private LayoutInflater mInflater;
    private VisitActivity mContext;
    private OnAdapterListener onAdapterListener;

    public VisitListAdapter(VisitActivity mContext, List<Customer> mList, OnAdapterListener onAdapterListener) {
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
        TextView txtOutlet, txtAddress, txtLabelRoute, txtNo;
        ImageView imgStatus, imgStatus2;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtOutlet = itemView.findViewById(R.id.txtOutlet);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            imgStatus2 = itemView.findViewById(R.id.imgStatus2);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtLabelRoute = itemView.findViewById(R.id.txtLabelRoute);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_visit, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Customer detail = mFilteredList.get(position);
        holder.txtNo.setText(String.valueOf(position + 1));
        holder.txtAddress.setText(detail.getAddress());
        holder.txtOutlet.setText(detail.getIdCustomer() + " - " + detail.getNameCustomer());

        if (position == 1) {
            holder.imgStatus2.setVisibility(View.VISIBLE);
        } else {
            holder.imgStatus2.setVisibility(View.GONE);
        }

        if (detail.isRoute()) {
            holder.txtLabelRoute.setText("Route");
            holder.txtLabelRoute.setBackground(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.bg_label_route));
            holder.txtLabelRoute.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.green_aspp));
        } else {
            holder.txtLabelRoute.setText("Non Route");
            holder.txtLabelRoute.setBackground(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.bg_label_non_route));
            holder.txtLabelRoute.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.red2_aspp));
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
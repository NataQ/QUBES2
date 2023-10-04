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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.VisitActivity;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.CustomerNoo;

public class NooListAdapter extends RecyclerView.Adapter<NooListAdapter.Holder> implements Filterable {
    private List<CustomerNoo> mList;
    private List<CustomerNoo> mFilteredList;
    private LayoutInflater mInflater;
    private VisitActivity mContext;
    private OnAdapterListener onAdapterListener;

    public NooListAdapter(VisitActivity mContext, List<CustomerNoo> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<CustomerNoo> mDataSet) {
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
                    List<CustomerNoo> filteredList = new ArrayList<>();
                    for (CustomerNoo row : mList) {

                        /*filter by name*/
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<CustomerNoo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtOutlet, txtAddress, txtNo, txtStatus;
        ImageView imgStatus;
        LinearLayout llStatus;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtOutlet = itemView.findViewById(R.id.txtOutlet);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            llStatus = itemView.findViewById(R.id.llStatus);
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
        CustomerNoo detail = mFilteredList.get(position);
        holder.txtNo.setText(String.valueOf(position + 1));
        holder.txtAddress.setText(detail.getAddress());
        holder.txtOutlet.setText(detail.getName());

        switch (detail.getStatus()) {
            case 0:
                holder.llStatus.setVisibility(View.GONE);
                break;
            case 1:
                //checked in
                holder.llStatus.setVisibility(View.VISIBLE);
                holder.imgStatus.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.ic_status_checked_in));
                holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.yellow_aspp));
                holder.txtStatus.setText("Checked In");
                break;
            case 2:
                //pause
                holder.llStatus.setVisibility(View.VISIBLE);
                holder.imgStatus.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.ic_status_pause));
                holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.red));
                holder.txtStatus.setText("Pause");
                break;
            case 3:
                //completed
                holder.llStatus.setVisibility(View.VISIBLE);
                holder.imgStatus.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.ic_status_completed));
                holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.aspp_blue9));
                holder.txtStatus.setText("Completed");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(CustomerNoo Customer);
    }
}

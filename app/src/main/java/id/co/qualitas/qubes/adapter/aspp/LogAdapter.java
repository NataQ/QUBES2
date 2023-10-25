package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.aspp.AccountFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.LogModel;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.Holder> implements Filterable {
    private List<LogModel> mList;
    private List<LogModel> mFilteredList;
    private LayoutInflater mInflater;
    private AccountFragment mContext;
    private OnAdapterListener onAdapterListener;

    public LogAdapter(AccountFragment mContext, List<LogModel> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<LogModel> mDataSet) {
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
                    List<LogModel> filteredList = new ArrayList<>();
                    for (LogModel row : mList) {

                        /*filter by name*/
                        if (row.getDescLog().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<LogModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtDate, txtTime, txtDesc;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtTime = itemView.findViewById(R.id.txtTime);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_log, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        LogModel detail = mFilteredList.get(position);
        holder.txtDate.setText(!Helper.isEmpty(detail.getDateLog()) ? detail.getDateLog() : "-");
        holder.txtTime.setText(!Helper.isEmpty(detail.getTimeLog()) ? detail.getTimeLog() : "-");
        holder.txtDesc.setText(!Helper.isEmpty(detail.getDescLog()) ? detail.getDescLog() : "-");
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(LogModel LogModel);
    }
}

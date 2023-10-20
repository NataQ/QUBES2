package id.co.qualitas.qubes.adapter.aspp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.rpc.Help;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.CustomerType;

public class FilteredSpinnerTypePriceAdapter extends RecyclerView.Adapter<FilteredSpinnerTypePriceAdapter.Holder> implements Filterable {
    private List<CustomerType> mList;
    private List<CustomerType> mFilteredList;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnAdapterListener onAdapterListener;
    private int type;

    public FilteredSpinnerTypePriceAdapter(Context mContext, int type, List<CustomerType> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.type = type;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.onAdapterListener = onAdapterListener;
    }

    public void setData(List<CustomerType> mDataSet) {
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
                    List<CustomerType> filteredList = new ArrayList<>();
                    for (CustomerType row : mList) {

                        /*filter by name*/
                        if (type == 1) {
                            if (row.getId().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        } else {
                            if (row.getType_price().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
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
                mFilteredList = (ArrayList<CustomerType>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text1;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
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
        View itemView = mInflater.inflate(R.layout.aspp_spinner_filtered_item, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        CustomerType detail = mFilteredList.get(position);
        if (type == 1) {
            String id = Helper.isEmpty(detail.getId(), "");
            String name = Helper.isEmpty(detail.getName(), "");
            holder.text1.setText(id + " - " + name);
        } else {
            String id = Helper.isEmpty(detail.getType_price(), "");
            String name = Helper.isEmpty(detail.getName(), "");
            holder.text1.setText(id + " - " + name);
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(CustomerType String, int adapterPosition);
    }
}

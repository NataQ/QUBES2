package id.co.qualitas.qubes.adapter.aspp;

import android.content.Context;
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
import id.co.qualitas.qubes.model.Bank;

public class SpinnerBankAdapter extends RecyclerView.Adapter<SpinnerBankAdapter.Holder> implements Filterable {
    private List<Bank> mList;
    private List<Bank> mFilteredList;
    private LayoutInflater mInflater;
    private boolean showNoRek;
    private Context mContext;
    private OnAdapterListener onAdapterListener;

    public SpinnerBankAdapter(Context mContext, List<Bank> mList, boolean showNoRek, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.showNoRek = showNoRek;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.onAdapterListener = onAdapterListener;
    }

    public void setData(List<Bank> mDataSet) {
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
                    List<Bank> filteredList = new ArrayList<>();
                    for (Bank row : mList) {

                        /*filter by name*/
                        if (row.getId().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<Bank>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text1, txtNoRek;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            txtNoRek = itemView.findViewById(R.id.txtNoRek);
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
        View itemView = mInflater.inflate(R.layout.aspp_spinner_filtered_item_bank, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Bank detail = mFilteredList.get(position);
        holder.text1.setText(detail.getId() + " - " + detail.getName());
        if (showNoRek) {
            holder.txtNoRek.setVisibility(View.VISIBLE);
            holder.txtNoRek.setText(detail.getNo_rek());
        } else {
            holder.txtNoRek.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Bank String, int adapterPosition);
    }
}

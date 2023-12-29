package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.OrderAddActivity;
import id.co.qualitas.qubes.model.Material;

public class SpinnerProductOrderAdapter extends RecyclerView.Adapter<SpinnerProductOrderAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private LayoutInflater mInflater;
    private OrderAddActivity mContext;
    private OnAdapterListener onAdapterListener;

    public SpinnerProductOrderAdapter(OrderAddActivity mContext, List<Material> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<Material> mDataSet) {
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
                    List<Material> filteredList = new ArrayList<>();
                    for (Material row : mList) {
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) || String.valueOf(row.getId()).contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<Material>) filterResults.values;
                mContext.setFilteredData(mFilteredList);
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout llHeader;
        RelativeLayout checkBox;
        TextView text, textStock;
        CardView cvUncheck, cvCheck;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            cvUncheck = itemView.findViewById(R.id.cvUncheck);
            textStock = itemView.findViewById(R.id.textStock);
            text = itemView.findViewById(R.id.text);
            llHeader = itemView.findViewById(R.id.llHeader);
            cvCheck = itemView.findViewById(R.id.cvCheck);
            textStock.setVisibility(View.VISIBLE);
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
        View itemView = mInflater.inflate(R.layout.aspp_spinner_filtered_item_product, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Material detail = mFilteredList.get(position);
        holder.text.setText(detail.getId() + " - " + detail.getNama() + " (" + detail.getId_material_group() + " - " + detail.getMaterial_group_name() + ")");
        holder.textStock.setText("Stock : " );

        if (detail.isChecked()) {
            holder.cvUncheck.setVisibility(View.GONE);
            holder.cvCheck.setVisibility(View.VISIBLE);
        } else {
            holder.cvUncheck.setVisibility(View.VISIBLE);
            holder.cvCheck.setVisibility(View.GONE);
        }

        holder.llHeader.setOnClickListener(v -> {
            if (detail.isChecked()) {
                detail.setChecked(false);
                holder.cvUncheck.setVisibility(View.VISIBLE);
                holder.cvCheck.setVisibility(View.GONE);
            } else {
                detail.setChecked(true);
                holder.cvUncheck.setVisibility(View.GONE);
                holder.cvCheck.setVisibility(View.VISIBLE);
            }
            mContext.setCheckedAll();
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Material material, int adapterPosition);
    }
}

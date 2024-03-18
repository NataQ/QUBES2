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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.OrderDetailActivity;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.Material;

public class OrderDetailExtraAdapter extends RecyclerView.Adapter<OrderDetailExtraAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private int positionHeader;
    private LayoutInflater mInflater;
    private OrderDetailActivity mContext;
    private OnAdapterListener onAdapterListener;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private boolean isExpand = false;
    private List<Discount> mListDiskon;
    private OrderDetailDiscountAdapter mAdapterDiscount;

    public OrderDetailExtraAdapter(int positionHeader, OrderDetailActivity mContext, List<Material> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.positionHeader = positionHeader;
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

                        /*filter by name*/
                        if (row.getMaterialCode().toLowerCase().contains(charString.toLowerCase())) {
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
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtProduct, txtQty, txtPrice, txtUom, txtNo, txtTotal;
        TextView txtTotalDiscount;
        LinearLayout llDiscountAll;
        ImageView imgView;
        RecyclerView rvDiscount;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtNo = itemView.findViewById(R.id.txtNo);
            imgView = itemView.findViewById(R.id.imgView);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtUom = itemView.findViewById(R.id.txtUom);
            txtTotalDiscount = itemView.findViewById(R.id.txtTotalDiscount);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQty = itemView.findViewById(R.id.txtQty);
            llDiscountAll = itemView.findViewById(R.id.llDiscountAll);
            rvDiscount = itemView.findViewById(R.id.rvDiscount);
            rvDiscount.setLayoutManager(new LinearLayoutManager(mContext));
            rvDiscount.setHasFixedSize(true);

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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_summary_extra, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Material detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        setFormatSeparator();
        mListDiskon = new ArrayList<>();
        if (Helper.isNotEmptyOrNull(detail.getDiskonList())) {
            mListDiskon.addAll(detail.getDiskonList());
        }

        holder.txtNo.setText(format.format(positionHeader + 1) + "." + format.format(holder.getAbsoluteAdapterPosition() + 1));
        holder.txtProduct.setText(Helper.isEmpty(detail.getNama(), ""));
        holder.txtQty.setText(format.format(detail.getQty()));
        holder.txtUom.setText(Helper.isEmpty(detail.getUom(), ""));
        holder.txtPrice.setText("Rp. " + format.format(detail.getPrice()));
        holder.txtTotalDiscount.setText("Rp. " + format.format(detail.getTotalDiscount()));
        holder.txtTotal.setText("Rp. " + format.format(detail.getTotal()));

        if (Helper.isNotEmptyOrNull(detail.getDiskonList())) {
            holder.llDiscountAll.setVisibility(View.VISIBLE);
            mAdapterDiscount = new OrderDetailDiscountAdapter(mContext, mListDiskon, header -> {
            });
            holder.rvDiscount.setAdapter(mAdapterDiscount);
        } else {
            holder.llDiscountAll.setVisibility(View.GONE);
        }

        holder.txtTotalDiscount.setOnClickListener(v -> {
            if (!isExpand) {
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.ic_drop_up));
                holder.rvDiscount.setVisibility(View.VISIBLE);
                isExpand = true;
            } else {
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.ic_drop_down_aspp));
                holder.rvDiscount.setVisibility(View.GONE);
                isExpand = false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Material Material);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}


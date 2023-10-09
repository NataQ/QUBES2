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
import id.co.qualitas.qubes.activity.aspp.StockRequestListActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.StockRequest;

public class StockRequestHeaderAdapter extends RecyclerView.Adapter<StockRequestHeaderAdapter.Holder> implements Filterable {
    private List<StockRequest> mList;
    private List<StockRequest> mFilteredList;
    private LayoutInflater mInflater;
    private StockRequestListActivity mContext;
    private OnAdapterListener onAdapterListener;

    public StockRequestHeaderAdapter(StockRequestListActivity mContext, List<StockRequest> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<StockRequest> mDataSet) {
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
                    List<StockRequest> filteredList = new ArrayList<>();
                    for (StockRequest row : mList) {

                        /*filter by name*/
                        if (row.getNodoc().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<StockRequest>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTanggal, txtNoDoc, txtTglKirim, txtStatus, txtSuratJalan;
        LinearLayout llStatus;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            llStatus = itemView.findViewById(R.id.llStatus);
            txtTanggal = itemView.findViewById(R.id.txtTanggal);
            txtNoDoc = itemView.findViewById(R.id.txtNoDoc);
            txtTglKirim = itemView.findViewById(R.id.txtTglKirim);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtSuratJalan = itemView.findViewById(R.id.txtSuratJalan);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_stock_request_header, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        StockRequest detail = mFilteredList.get(position);
        holder.txtNoDoc.setText(Helper.isEmpty(detail.getNodoc(), ""));
        holder.txtSuratJalan.setText(Helper.isEmpty(detail.getNosuratjalan(), ""));
        if (!Helper.isNullOrEmpty(detail.getReqdate())) {
            String requestDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, detail.getReqdate());
            holder.txtTanggal.setText(requestDate);
        }
        if (!Helper.isNullOrEmpty(detail.getTanggalkirim())) {
            String tglKirim = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, detail.getTanggalkirim());
            holder.txtTglKirim.setText(tglKirim);
        }
        holder.txtStatus.setText(detail.getStatus());

        if (!Helper.isEmpty(detail.getStatus())) {
            switch (detail.getStatus().toLowerCase()) {
                case Constants.STATUS_PENDING:
                    holder.llStatus.setVisibility(View.VISIBLE);
                    holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext.getApplicationContext(), R.color.yellow2_aspp));
                    holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.yellow_aspp));
                    break;
                case Constants.STATUS_REJECTED:
                    holder.llStatus.setVisibility(View.VISIBLE);
                    holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext.getApplicationContext(), R.color.red_aspp));
                    holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.red2_aspp));
                    break;
                case Constants.STATUS_APPROVE:
                    holder.llStatus.setVisibility(View.VISIBLE);
                    holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext.getApplicationContext(), R.color.green2_aspp));
                    holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.green_aspp));
                    break;
                default:
                    holder.llStatus.setVisibility(View.VISIBLE);
                    holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext.getApplicationContext(), R.color.gray12_aspp));
                    holder.txtStatus.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.gray8_aspp));
                    holder.txtStatus.setText(detail.getStatus());
                    break;
            }
        } else {
            holder.llStatus.setVisibility(View.GONE);
            holder.llStatus.setBackground(null);
            holder.txtStatus.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(StockRequest stockRequest);
    }
}

package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.aspp.TargetFragment;
import id.co.qualitas.qubes.model.Target;

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.Holder> implements Filterable {
    private List<Target> mList;
    private List<Target> mFilteredList;
    private LayoutInflater mInflater;
    private TargetFragment mContext;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

    public TargetAdapter(TargetFragment mContext, List<Target> mList) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext.getContext());
    }

    public void setData(List<Target> mDataSet) {
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
                    List<Target> filteredList = new ArrayList<>();
                    for (Target row : mList) {

                        /*filter by name*/
                        if (row.getMaterial_group_name().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<Target>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTgtAt, txtGroupMat, txtAt, txtTotalPercentageAT, txtTotalAT, txtTgtOms, txtOms, txtTotalPercentageOMS, txtTotalOMS;

        public Holder(View itemView) {
            super(itemView);
            txtTotalOMS = itemView.findViewById(R.id.txtTotalOMS);
            txtTotalPercentageOMS = itemView.findViewById(R.id.txtTotalPercentageOMS);
            txtOms = itemView.findViewById(R.id.txtOms);
            txtTgtOms = itemView.findViewById(R.id.txtTgtOms);
            txtTotalAT = itemView.findViewById(R.id.txtTotalAT);
            txtTotalPercentageAT = itemView.findViewById(R.id.txtTotalPercentageAT);
            txtAt = itemView.findViewById(R.id.txtAt);
            txtGroupMat = itemView.findViewById(R.id.txtGroupMat);
            txtTgtAt = itemView.findViewById(R.id.txtTgtAt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            onClickListener.onClick(getAdapterPosition());
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_row_view_target, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        setFormatSeparator();
        Target detail = mFilteredList.get(position);
        holder.txtGroupMat.setText(detail.getMaterial_group_name());
        holder.txtTgtAt.setText(format.format(detail.getTgt_at()));
        holder.txtAt.setText(format.format(detail.getAt()));
        holder.txtTgtOms.setText(format.format(detail.getTgt_oms()));
        holder.txtOms.setText(format.format(detail.getOms()));

        double totalPercentAT = ((double) detail.getAt() / detail.getTgt_at()) * 100;
        double totalPercentOMS = ((double) detail.getOms() / detail.getTgt_oms()) * 100;
        double totalAT = detail.getAt() - detail.getTgt_at();
        double totalOMS = detail.getOms() - detail.getTgt_oms();

        holder.txtTotalPercentageAT.setText(format.format(Math.round(totalPercentAT)) + "%");
        holder.txtTotalPercentageOMS.setText(format.format(Math.round(totalPercentOMS)) + "%");
        holder.txtTotalAT.setText(format.format(totalAT));
        holder.txtTotalOMS.setText(format.format(totalOMS));
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnClickListener {
        void onClick(int pos);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}

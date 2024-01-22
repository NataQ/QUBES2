package id.co.qualitas.qubes.adapter.aspp;

import android.graphics.Typeface;
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
import id.co.qualitas.qubes.model.TargetPekan;

public class TargetPekanAdapter extends RecyclerView.Adapter<TargetPekanAdapter.Holder> implements Filterable {
    private List<TargetPekan> mList;
    private List<TargetPekan> mFilteredList;
    private LayoutInflater mInflater;
    private TargetFragment mContext;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

    public TargetPekanAdapter(TargetFragment mContext, List<TargetPekan> mList) {
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

    public void setData(List<TargetPekan> mDataSet) {
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
                    List<TargetPekan> filteredList = new ArrayList<>();
                    for (TargetPekan row : mList) {

                        /*filter by name*/
                        if (row.getPekan().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<TargetPekan>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtPekan, txtAtTgt, txtAtReal, txtAtPercent, txtAtMinus,
                txtOmzTgt, txtOmzReal, txtOmzPercent, txtOmzMinus;

        public Holder(View itemView) {
            super(itemView);
            txtOmzMinus = itemView.findViewById(R.id.txtOmzMinus);
            txtOmzPercent = itemView.findViewById(R.id.txtOmzPercent);
            txtOmzReal = itemView.findViewById(R.id.txtOmzReal);
            txtOmzTgt = itemView.findViewById(R.id.txtOmzTgt);
            txtAtMinus = itemView.findViewById(R.id.txtAtMinus);
            txtAtPercent = itemView.findViewById(R.id.txtAtPercent);
            txtAtReal = itemView.findViewById(R.id.txtAtReal);
            txtAtTgt = itemView.findViewById(R.id.txtAtTgt);
            txtPekan = itemView.findViewById(R.id.txtPekan);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            onClickListener.onClick(getAdapterPosition());
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_row_view_target_detail, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        setFormatSeparator();
        TargetPekan detail = mFilteredList.get(position);
        if (detail.getPekan().toLowerCase().equals("total")) {
            holder.txtPekan.setTypeface(null, Typeface.BOLD);
            holder.txtAtTgt.setTypeface(null, Typeface.BOLD);
            holder.txtAtReal.setTypeface(null, Typeface.BOLD);
            holder.txtAtPercent.setTypeface(null, Typeface.BOLD);
            holder.txtAtMinus.setTypeface(null, Typeface.BOLD);
            holder.txtOmzTgt.setTypeface(null, Typeface.BOLD);
            holder.txtOmzReal.setTypeface(null, Typeface.BOLD);
            holder.txtOmzPercent.setTypeface(null, Typeface.BOLD);
            holder.txtOmzMinus.setTypeface(null, Typeface.BOLD);
        } else {
            holder.txtPekan.setTypeface(null, Typeface.NORMAL);
            holder.txtAtTgt.setTypeface(null, Typeface.NORMAL);
            holder.txtAtReal.setTypeface(null, Typeface.NORMAL);
            holder.txtAtPercent.setTypeface(null, Typeface.NORMAL);
            holder.txtAtMinus.setTypeface(null, Typeface.NORMAL);
            holder.txtOmzTgt.setTypeface(null, Typeface.NORMAL);
            holder.txtOmzReal.setTypeface(null, Typeface.NORMAL);
            holder.txtOmzPercent.setTypeface(null, Typeface.NORMAL);
            holder.txtOmzMinus.setTypeface(null, Typeface.NORMAL);
        }
        holder.txtPekan.setText(detail.getPekan());
        holder.txtAtTgt.setText(format.format(detail.getAt_tgt()));
        holder.txtAtReal.setText(format.format(detail.getAt_real()));
        holder.txtAtPercent.setText(format.format(Math.round(detail.getAt_percent())) + " %");
        holder.txtAtMinus.setText(format.format(detail.getAt_minus()));
        holder.txtOmzTgt.setText(format.format(detail.getOmz_tgt()));
        holder.txtOmzReal.setText(format.format(detail.getOmz_real()));
        holder.txtOmzPercent.setText(format.format(Math.round(detail.getOmz_percent())) + " %");
        holder.txtOmzMinus.setText(format.format(detail.getOmz_minus()));
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

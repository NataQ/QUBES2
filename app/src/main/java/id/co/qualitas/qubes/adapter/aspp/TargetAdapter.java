package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
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

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.Holder> implements Filterable {
    private List<Target> mList;
    private List<Target> mFilteredList;
    private LayoutInflater mInflater;
    private TargetFragment mContext;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private TargetPekanAdapter mAdapter;

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
                        if (row.getName_material_group().toLowerCase().contains(charString.toLowerCase())) {
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
        TextView txtProduct;
        RecyclerView recyclerView;

        public Holder(View itemView) {
            super(itemView);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext.getContext()));
            recyclerView.setHasFixedSize(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            onClickListener.onClick(getAdapterPosition());
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_row_view_target_header, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        setFormatSeparator();
        Target detail = mFilteredList.get(position);
        holder.txtProduct.setText(detail.getName_material_group());
        mAdapter = new TargetPekanAdapter(mContext, detail.getPekan_list());
        holder.recyclerView.setAdapter(mAdapter);
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

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
import id.co.qualitas.qubes.model.DaerahTingkat;

public class FilteredSpinnerDaerahTingkatAdapter extends RecyclerView.Adapter<FilteredSpinnerDaerahTingkatAdapter.Holder> implements Filterable {
    private List<DaerahTingkat> mList;
    private List<DaerahTingkat> mFilteredList;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnAdapterListener onAdapterListener;
    int typeDaerah = 0;

    public FilteredSpinnerDaerahTingkatAdapter(Context mContext, int typeDaerah, List<DaerahTingkat> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.typeDaerah = typeDaerah;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.onAdapterListener = onAdapterListener;
    }

    public void setData(List<DaerahTingkat> mDataSet) {
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
                    List<DaerahTingkat> filteredList = new ArrayList<>();
                    for (DaerahTingkat row : mList) {

                        /*filter by name*/
                        switch (typeDaerah) {
                            case 1:
                                int search = Integer.parseInt(charString.toLowerCase());
                                if (row.getKode_pos() == search) {
                                    filteredList.add(row);
                                }
                                break;
                            case 2:
                                if (row.getKode_kelurahan().toLowerCase().contains(charString.toLowerCase()) ||
                                        row.getNama_kelurahan().toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;
                            case 3:
                                if (row.getKode_kecamatan().toLowerCase().contains(charString.toLowerCase()) ||
                                        row.getNama_kecamatan().toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;
                            case 4:
                                if (row.getKode_kabupaten().toLowerCase().contains(charString.toLowerCase()) ||
                                        row.getNama_kabupaten().toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;
                            case 5:
                                if (row.getKode_provinsi().toLowerCase().contains(charString.toLowerCase()) ||
                                        row.getNama_provinsi().toLowerCase().contains(charString.toLowerCase())) {
                                    filteredList.add(row);
                                }
                                break;
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
                mFilteredList = (ArrayList<DaerahTingkat>) filterResults.values;
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
        DaerahTingkat detail = mFilteredList.get(position);
        switch (typeDaerah) {
            case 1:
                holder.text1.setText(String.valueOf(detail.getKode_pos()));
                break;
            case 2:
                String id = Helper.isEmpty(detail.getKode_kelurahan(), "");
                String name = Helper.isEmpty(detail.getNama_kelurahan(), "");
                holder.text1.setText(id + " - " + name);
                break;
            case 3:
                String id1 = Helper.isEmpty(detail.getKode_kecamatan(), "");
                String name1 = Helper.isEmpty(detail.getNama_kecamatan(), "");
                holder.text1.setText(id1 + " - " + name1);
                break;
            case 4:
                String id2 = Helper.isEmpty(detail.getKode_kabupaten(), "");
                String name2 = Helper.isEmpty(detail.getNama_kabupaten(), "");
                holder.text1.setText(id2 + " - " + name2);
                break;
            case 5:
                String id3 = Helper.isEmpty(detail.getKode_provinsi(), "");
                String name3 = Helper.isEmpty(detail.getNama_provinsi(), "");
                holder.text1.setText(id3 + " - " + name3);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(DaerahTingkat daerahTingkat, int adapterPosition);
    }
}

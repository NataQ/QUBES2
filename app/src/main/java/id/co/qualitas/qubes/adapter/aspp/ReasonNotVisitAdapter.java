package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.VisitActivity;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Reason;

public class ReasonNotVisitAdapter extends RecyclerView.Adapter<ReasonNotVisitAdapter.Holder> implements Filterable {
    private List<Customer> mList;
    private List<Customer> mFilteredList;
    private LayoutInflater mInflater;
    private VisitActivity mContext;
    private OnAdapterListener onAdapterListener;

    public ReasonNotVisitAdapter(VisitActivity mContext, List<Customer> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<Customer> mDataSet) {
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
                    List<Customer> filteredList = new ArrayList<>();
                    for (Customer row : mList) {

                        /*filter by name*/
                        if (row.getNameCustomer().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtOutlet, txtAddress;
        ImageView imgAddReason, imgReason;
        EditText edtTxtOther;
        LinearLayout llImg;
        AutoCompleteTextView txtReason;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            llImg = itemView.findViewById(R.id.llImg);
            txtOutlet = itemView.findViewById(R.id.txtOutlet);
            edtTxtOther = itemView.findViewById(R.id.edtTxtOther);
            txtReason = itemView.findViewById(R.id.txtReason);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            imgAddReason = itemView.findViewById(R.id.imgAddReason);
            imgReason = itemView.findViewById(R.id.imgReason);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_reason_not_visit, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Customer detail = mFilteredList.get(position);
        holder.txtAddress.setText(detail.getAddress());
        holder.txtOutlet.setText(detail.getIdCustomer() + " - " + detail.getNameCustomer());
        List<Reason> reasonList = new ArrayList<>();
        reasonList.add(new Reason("N1", "Waktu Habis", false, false));
        reasonList.add(new Reason("N2", "Pindah", true, false));
        reasonList.add(new Reason("N3", "Banjir", false, true));
        reasonList.add(new Reason("N4", "Tidak Ketemu", true, true));
        reasonList.add(new Reason("N5", "Tutup", false, true));
        reasonList.add(new Reason("N6", "Other", true, false));

        final ArrayAdapter<Reason> arrayAdapter = new ArrayAdapter<Reason>(mContext, android.R.layout.simple_dropdown_item_1line, reasonList);
        holder.txtReason.setAdapter(arrayAdapter);

        holder.txtReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                holder.txtReason.showDropDown();
            }
        });

        holder.txtReason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Reason temp = arrayAdapter.getItem(i);
                if (temp.isPhoto()) {
                    holder.llImg.setVisibility(View.VISIBLE);
                } else {
                    holder.llImg.setVisibility(View.GONE);
                }

                if (temp.isFreeText()) {
                    holder.edtTxtOther.setVisibility(View.VISIBLE);
                } else {
                    holder.edtTxtOther.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Customer Customer);
    }
}

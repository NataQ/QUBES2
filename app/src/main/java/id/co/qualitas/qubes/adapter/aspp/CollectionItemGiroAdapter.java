package id.co.qualitas.qubes.adapter.aspp;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.C;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CollectionFormActivityNew;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;

public class CollectionItemGiroAdapter extends RecyclerView.Adapter<CollectionItemGiroAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionFormActivityNew mContext;
    private OnAdapterListener onAdapterListener;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    protected boolean checked = true;
    CollectionGiroNewAdapter headerAdapter;
    CollectionInvoiceGiroAdapter invoiceAdapter;
    int idHeader, invoicePosition;

    public CollectionItemGiroAdapter(CollectionFormActivityNew mContext, CollectionGiroNewAdapter headerAdapter, CollectionInvoiceGiroAdapter invoiceAdapter, int idHeader, int invoicePosition, List<Material> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.headerAdapter = headerAdapter;
        this.invoiceAdapter = invoiceAdapter;
        this.idHeader = idHeader;
        this.invoicePosition = invoicePosition;
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
        TextView txtNo, txtProduct, txtPrice, txtPaid;
        EditText edtPaid;
        CheckBox cb;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtNo = itemView.findViewById(R.id.txtNo);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            edtPaid = itemView.findViewById(R.id.edtPaid);
            txtPrice = itemView.findViewById(R.id.txtPrice);
//            txtLeft = itemView.findViewById(R.id.txtLeft);
            txtPaid = itemView.findViewById(R.id.txtPaid);
            cb = itemView.findViewById(R.id.cb);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_coll_payment, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        setFormatSeparator();
        Material detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());

        holder.txtNo.setText(format.format(holder.getAbsoluteAdapterPosition() + 1) + ".");
        holder.txtProduct.setText(Helper.isEmpty(detail.getNama(), ""));
        holder.txtPrice.setText("Rp." + format.format(detail.getPrice()));
        holder.edtPaid.setText(detail.getAmountPaid() != 0 ? Helper.setDotCurrencyAmount(detail.getAmountPaid()) : null);
        holder.txtPaid.setText(detail.getAmountPaid() != 0 ? Helper.setDotCurrencyAmount(detail.getAmountPaid()) : null);

        holder.txtPaid.setOnClickListener(view -> {
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

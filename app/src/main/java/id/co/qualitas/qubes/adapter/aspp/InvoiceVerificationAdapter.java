package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.InvoiceVerificationActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Invoice;

public class InvoiceVerificationAdapter extends RecyclerView.Adapter<InvoiceVerificationAdapter.Holder> implements Filterable {
    private List<Invoice> mList;
    private List<Invoice> mFilteredList;
    private LayoutInflater mInflater;
    private InvoiceVerificationActivity mContext;
    private OnAdapterListener onAdapterListener;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

    public InvoiceVerificationAdapter(InvoiceVerificationActivity mContext, List<Invoice> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<Invoice> mDataSet) {
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
                    List<Invoice> filteredList = new ArrayList<>();
                    for (Invoice row : mList) {

                        /*filter by name*/
                        if (row.getNo_invoice().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<Invoice>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtInvoiceNo, txtCustomer, txtAmount, txtPaid, txtInvoiceDate, txtDueDate, txtNett, txtLabelRoute;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtLabelRoute = itemView.findViewById(R.id.txtLabelRoute);
            txtNett = itemView.findViewById(R.id.txtNett);
            txtInvoiceDate = itemView.findViewById(R.id.txtInvoiceDate);
            txtDueDate = itemView.findViewById(R.id.txtDueDate);
            txtInvoiceNo = itemView.findViewById(R.id.txtInvoiceNo);
            txtCustomer = itemView.findViewById(R.id.txtCustomer);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtPaid = itemView.findViewById(R.id.txtPaid);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_invoice_verification, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        setFormatSeparator();
        Invoice detail = mFilteredList.get(position);

        String idCust = Helper.isEmpty(detail.getNo_invoice(), "");
        String nameCust = Helper.isEmpty(detail.getNama(), "");

        if (!Helper.isNullOrEmpty(detail.getInvoice_date())) {
            String invDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, detail.getInvoice_date());
            holder.txtInvoiceDate.setText(invDate);
        }

        if (!Helper.isNullOrEmpty(detail.getTanggal_jatuh_tempo())) {
            String dueDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, detail.getTanggal_jatuh_tempo());
            holder.txtDueDate.setText(dueDate);
        }
        holder.txtCustomer.setText(idCust + " - " + nameCust);
        holder.txtInvoiceNo.setText(Helper.isEmpty(detail.getNo_invoice(), "-"));
        holder.txtAmount.setText("Rp. " + format.format(detail.getAmount()));
        holder.txtNett.setText("Rp. " + format.format(detail.getNett()));
        holder.txtPaid.setText("Rp. " + format.format(detail.getTotal_paid()));

        if (detail.getIs_route() == 1) {
            holder.txtLabelRoute.setText("Route");
            holder.txtLabelRoute.setBackground(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.bg_label_route));
            holder.txtLabelRoute.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.green_aspp));
        } else {
            holder.txtLabelRoute.setText("Non Route");
            holder.txtLabelRoute.setBackground(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.bg_label_non_route));
            holder.txtLabelRoute.setTextColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.red2_aspp));
        }
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Invoice Invoice);
    }
}

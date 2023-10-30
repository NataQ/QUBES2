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
import id.co.qualitas.qubes.activity.aspp.CollectionDetailActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.CollectionDetail;
import id.co.qualitas.qubes.model.CollectionDetail;

public class CollectionChequeDetailAdapter extends RecyclerView.Adapter<CollectionChequeDetailAdapter.Holder> implements Filterable {
    private List<CollectionDetail> mList;
    private List<CollectionDetail> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionDetailActivity mContext;
    private OnAdapterListener onAdapterListener;
    private boolean visible = false;
    private CollectionPaymentDetailAdapter mAdapter;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

    public CollectionChequeDetailAdapter(CollectionDetailActivity mContext, List<CollectionDetail> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<CollectionDetail> mDataSet) {
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
                    List<CollectionDetail> filteredList = new ArrayList<>();
                    for (CollectionDetail row : mList) {

                        /*filter by name*/
                        if (row.getNo().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<CollectionDetail>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTglCheque, txtTglCair, txtBankName, txtBankCust, txtNoCheque, txtPayment, txtLeft;
        RecyclerView recyclerView;
        ImageView imgView;
        LinearLayout llPayment, layout;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtLeft = itemView.findViewById(R.id.txtLeft);
            txtPayment = itemView.findViewById(R.id.txtPayment);
            txtNoCheque = itemView.findViewById(R.id.txtNoCheque);
            txtBankCust = itemView.findViewById(R.id.txtBankCust);
            txtBankName = itemView.findViewById(R.id.txtBankName);
            txtTglCair = itemView.findViewById(R.id.txtTglCair);
            layout = itemView.findViewById(R.id.layout);
            llPayment = itemView.findViewById(R.id.llPayment);
            imgView = itemView.findViewById(R.id.imgView);
            txtTglCheque = itemView.findViewById(R.id.txtTglCheque);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setHasFixedSize(true);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_coll_cheque_detail, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        setFormatSeparator();

        CollectionDetail detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        String idBankCust = Helper.isEmpty(detail.getIdBankCust(), "");
        String nameBankCust = Helper.isEmpty(detail.getBankCust(), "");

        String idBank = Helper.isEmpty(detail.getIdBankASPP(), "");
        String nameBank = Helper.isEmpty(detail.getBankNameASPP(), "");
        if (!Helper.isNullOrEmpty(detail.getTgl())) {
            String date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_4, detail.getTgl());
            holder.txtTglCheque.setText(date);
        } else {
            holder.txtTglCheque.setText(null);
        }

        if (!Helper.isNullOrEmpty(detail.getTglCair())) {
            String date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_4, detail.getTglCair());
            holder.txtTglCair.setText(date);
        } else {
            holder.txtTglCair.setText(null);
        }

        holder.txtBankCust.setText(!idBankCust.equals("") && !nameBankCust.equals("") ? idBankCust + " - " + nameBankCust : null);
        holder.txtBankName.setText(!idBank.equals("") && !nameBank.equals("") ? idBank + " - " + nameBank : null);

        holder.txtNoCheque.setText(Helper.isEmpty(detail.getNo(), ""));
        holder.txtPayment.setText("Rp. " + format.format(detail.getTotalPayment()));
        holder.txtLeft.setText("Rp. " + format.format(detail.getLeft()));

        mAdapter = new CollectionPaymentDetailAdapter(mContext, detail.getMaterialList(), header -> {

        });
        holder.recyclerView.setAdapter(mAdapter);

        holder.layout.setOnClickListener(v -> {
            if (visible) {
                visible = false;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_up));
                holder.llPayment.setVisibility(View.VISIBLE);
            } else {
                visible = true;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_down_aspp));
                holder.llPayment.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(CollectionDetail detail);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}

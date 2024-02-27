package id.co.qualitas.qubes.adapter.aspp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CollectionFormActivity;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;

public class CollectionTransferPaymentAdapter extends RecyclerView.Adapter<CollectionTransferPaymentAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionFormActivity mContext;
    private OnAdapterListener onAdapterListener;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    protected CollectionTransferAdapter headerAdapter;
    protected boolean checked = true;
    protected int idHeader;

    public CollectionTransferPaymentAdapter(CollectionFormActivity mContext, CollectionTransferAdapter headerAdapter, int idHeader, List<Material> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.idHeader = idHeader;
        this.headerAdapter = headerAdapter;
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
        TextView txtNo, txtProduct, txtPrice, txtPaid; //txtLeft
        EditText edtPaid;
        CheckBox cb;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtNo = itemView.findViewById(R.id.txtNo);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            edtPaid = itemView.findViewById(R.id.edtPaid);
//            txtLeft = itemView.findViewById(R.id.txtLeft);
            txtPrice = itemView.findViewById(R.id.txtPrice);
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
//        holder.txtLeft.setText("Rp." + format.format(mContext.getKurangBayar(holder.getAbsoluteAdapterPosition())));
        holder.edtPaid.setText(detail.getAmountPaid() != 0 ? Helper.setDotCurrencyAmount(detail.getAmountPaid()) : null);
        holder.txtPaid.setText(detail.getAmountPaid() != 0 ? Helper.setDotCurrencyAmount(detail.getAmountPaid()) : null);

        holder.txtPaid.setOnClickListener(view -> {
            if (headerAdapter.getTotalAmount(idHeader) > 0) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                final Dialog dialog = new Dialog(mContext);
                View dialogView = inflater.inflate(R.layout.aspp_dialog_amount_paid, null);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(dialogView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
                TextView txtMaterial = dialog.findViewById(R.id.txtMaterial);
                TextView txtPrice = dialog.findViewById(R.id.txtPrice);
                TextView txtLeft = dialog.findViewById(R.id.txtLeft);
                EditText edtPaid = dialog.findViewById(R.id.edtPaid);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnSave = dialog.findViewById(R.id.btnSave);

                txtMaterial.setText(Helper.isEmpty(detail.getNama(), ""));
                txtPrice.setText("Rp." + format.format(detail.getPrice()));
                txtLeft.setText("Rp." + format.format(mContext.getKurangBayar(holder.getAbsoluteAdapterPosition())));
                edtPaid.setText(detail.getAmountPaid() != 0 ? Helper.setDotCurrencyAmount(detail.getAmountPaid()) : null);

                edtPaid.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Helper.setDotCurrency(edtPaid, this, s);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Helper.isEmptyEditText(edtPaid)) {
                            double qty = Double.parseDouble(edtPaid.getText().toString().replace(",", ""));
                            if (qty > mContext.getSisaPrice(holder.getAbsoluteAdapterPosition(), 2, idHeader)) {
                                Toast.makeText(mContext, "Tidak boleh melebihi sisa harga barang", Toast.LENGTH_SHORT).show();
                            } else if (headerAdapter.calculateLeft(qty, holder.getAbsoluteAdapterPosition()) < 0) {
                                Toast.makeText(mContext, "Saldo tidak cukup", Toast.LENGTH_SHORT).show();
                            } else if (qty > headerAdapter.getTotalAmount(idHeader)) {
                                Toast.makeText(mContext, "Tidak boleh melebihi total amount", Toast.LENGTH_SHORT).show();
                            } else if (qty > detail.getPrice()) {
                                Toast.makeText(mContext, "Tidak boleh melebihi harga barang", Toast.LENGTH_SHORT).show();
                            } else {
                                holder.txtPaid.setText(Helper.setDotCurrencyAmount(qty));
                                detail.setAmountPaid(qty);
                                mContext.setKurangBayar(holder.getAbsoluteAdapterPosition());
//                                holder.txtLeft.setText("Rp." + format.format(mContext.getKurangBayar(holder.getAbsoluteAdapterPosition())));
                                headerAdapter.setLeft(idHeader);
//                                mContext.updateLeft(2, idHeader);
                                dialog.dismiss();
                            }
                        } else {
                            dialog.dismiss();
//                            holder.txtPaid.setError(mContext.getString(R.string.emptyField));
                        }
                    }
                });
                dialog.show();
            } else {
                Toast.makeText(mContext, "Masukkan total payment", Toast.LENGTH_SHORT).show();
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
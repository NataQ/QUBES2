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
import id.co.qualitas.qubes.activity.aspp.CollectionFormActivityNew;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;

public class CollectionItemTransferAdapter extends RecyclerView.Adapter<CollectionItemTransferAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private Invoice invoiceHeader;
    private LayoutInflater mInflater;
    private CollectionFormActivityNew mContext;
    private OnAdapterListener onAdapterListener;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    protected boolean checked = true;
    CollectionTransferNewAdapter transferAdapter;
    CollectionInvoiceTransferAdapter invoiceAdapter;
    int transferPosition, invoicePosition;
    private double left;

    public CollectionItemTransferAdapter(CollectionFormActivityNew mContext, CollectionTransferNewAdapter transferAdapter, CollectionInvoiceTransferAdapter invoiceAdapter,
                                         int transferPosition, int invoicePosition, Invoice invoiceHeader, OnAdapterListener onAdapterListener) {
        if (Helper.isNotEmptyOrNull(invoiceHeader.getMaterialList())) {
            this.mList = invoiceHeader.getMaterialList();
            this.mFilteredList = invoiceHeader.getMaterialList();
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.transferAdapter = transferAdapter;
        this.invoiceHeader = invoiceHeader;
        this.invoiceAdapter = invoiceAdapter;
        this.invoicePosition = invoicePosition;
        this.transferPosition = transferPosition;
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
        TextView txtNo, txtProduct, txtPrice, txtPaid, txtLeft;
        EditText edtPaid;
        CheckBox cb;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtNo = itemView.findViewById(R.id.txtNo);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            edtPaid = itemView.findViewById(R.id.edtPaid);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtPaid = itemView.findViewById(R.id.txtPaid);
            cb = itemView.findViewById(R.id.cb);
            txtLeft = itemView.findViewById(R.id.txtLeft);
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
        itemStateArray.put(holder.getAbsoluteAdapterPosition(), mFilteredList.get(holder.getAbsoluteAdapterPosition()).isChecked());
        if (!itemStateArray.get(holder.getAbsoluteAdapterPosition(), false)) {
            holder.cb.setChecked(false);
        } else {
            holder.cb.setChecked(true);
        }

        holder.txtNo.setText(format.format(holder.getAbsoluteAdapterPosition() + 1) + ".");
        holder.txtProduct.setText(Helper.isEmpty(detail.getNama(), ""));
        holder.txtPrice.setText("Rp." + format.format(detail.getPrice()));
        holder.edtPaid.setText(detail.getAmountPaid() != 0 ? Helper.setDotCurrencyAmount(detail.getAmountPaid()) : null);
        holder.txtPaid.setText(detail.getAmountPaid() != 0 ? Helper.setDotCurrencyAmount(detail.getAmountPaid()) : null);
        left = mContext.getKurangBayarMaterial(invoiceHeader, mFilteredList.get(holder.getAbsoluteAdapterPosition()));
        holder.txtLeft.setText("Rp." + format.format(left));

        holder.txtPaid.setOnClickListener(view -> {
//            if (invoiceHeader.getTotalPayment() > 0) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            final Dialog dialog = new Dialog(mContext);
            View dialogView = inflater.inflate(R.layout.aspp_dialog_amount_paid, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogView);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
            TextView txtMaterial = dialog.findViewById(R.id.txtMaterial);
            TextView txtLeft = dialog.findViewById(R.id.txtLeft);
            TextView txtPrice = dialog.findViewById(R.id.txtPrice);
            EditText edtPaid = dialog.findViewById(R.id.edtPaid);
            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            Button btnSave = dialog.findViewById(R.id.btnSave);
            left = mContext.getKurangBayarMaterial(invoiceHeader, mFilteredList.get(holder.getAbsoluteAdapterPosition()));

            txtMaterial.setText(Helper.isEmpty(mFilteredList.get(holder.getAbsoluteAdapterPosition()).getNama(), ""));
            txtPrice.setText("Rp." + format.format(mFilteredList.get(holder.getAbsoluteAdapterPosition()).getPrice()));
            txtLeft.setText("Rp." + format.format(left));
            edtPaid.setText(mFilteredList.get(holder.getAbsoluteAdapterPosition()).getAmountPaid() != 0
                    ? Helper.setDotCurrencyAmount(mFilteredList.get(holder.getAbsoluteAdapterPosition()).getAmountPaid()) : null);

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
                        double kurangBayar = left + mFilteredList.get(holder.getAbsoluteAdapterPosition()).getAmountPaid();
                        if (qty > kurangBayar) {
                            Toast.makeText(mContext, "Tidak boleh melebihi sisa harga barang", Toast.LENGTH_SHORT).show();
//                            } else if (qty > invoiceAdapter.calculateLeftWoPos(invoiceHeader, mFilteredList.get(holder.getAbsoluteAdapterPosition()).getId())) {
                        } else if (qty > transferAdapter.getTotalAmount(mFilteredList.get(holder.getAbsoluteAdapterPosition()), transferPosition)) {
                            Toast.makeText(mContext, "Saldo tidak cukup", Toast.LENGTH_SHORT).show();
                        } else if (qty > mFilteredList.get(holder.getAbsoluteAdapterPosition()).getNett()) {
                            Toast.makeText(mContext, "Tidak boleh melebihi harga barang", Toast.LENGTH_SHORT).show();
                        } else {
                            holder.txtPaid.setText(Helper.setDotCurrencyAmount(qty));
                            mFilteredList.get(holder.getAbsoluteAdapterPosition()).setAmountPaid(qty);
//                                invoiceAdapter.setLeftInvoice(invoicePosition, invoiceHeader);
                            mContext.notifyAdapter(1);
//                            transferAdapter.notifyItemChanged(transferPosition);
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
//            } else {
//                Toast.makeText(mContext, "Masukkan total payment", Toast.LENGTH_SHORT).show();
//            }
        });

        holder.cb.setOnClickListener(v -> {
            if (!itemStateArray.get(holder.getAbsoluteAdapterPosition(), false)) {
//                if (invoiceHeader.getTotalPayment() > 0) {
                double sisaTotalAmount = transferAdapter.getTotalAmount(mFilteredList.get(holder.getAbsoluteAdapterPosition()), transferPosition);
                double kurangBayarMaterial = mContext.getKurangBayarMaterial(invoiceHeader, mFilteredList.get(holder.getAbsoluteAdapterPosition()));
                kurangBayarMaterial = kurangBayarMaterial + mFilteredList.get(holder.getAbsoluteAdapterPosition()).getAmountPaid();
                if (kurangBayarMaterial > 0) {
                    if (sisaTotalAmount > kurangBayarMaterial || sisaTotalAmount == kurangBayarMaterial) {
                        mFilteredList.get(holder.getAbsoluteAdapterPosition()).setAmountPaid(kurangBayarMaterial);
                    } else if (sisaTotalAmount < kurangBayarMaterial) {
                        mFilteredList.get(holder.getAbsoluteAdapterPosition()).setAmountPaid(sisaTotalAmount);
                    }
                    holder.cb.setChecked(true);
                    itemStateArray.put(holder.getAbsoluteAdapterPosition(), true);
                    mFilteredList.get(holder.getAbsoluteAdapterPosition()).setChecked(true);
                }
//                } else {
//                    Toast.makeText(mContext, "Masukkan total payment", Toast.LENGTH_SHORT).show();
//                }
            } else {
                holder.cb.setChecked(false);
                itemStateArray.put(holder.getAbsoluteAdapterPosition(), false);
                mFilteredList.get(holder.getAbsoluteAdapterPosition()).setChecked(false);
                mFilteredList.get(holder.getAbsoluteAdapterPosition()).setAmountPaid(0);
            }

            invoiceAdapter.setCheckedAll(invoicePosition);
            mContext.notifyAdapter(1);
//            transferAdapter.notifyItemChanged(transferPosition);
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

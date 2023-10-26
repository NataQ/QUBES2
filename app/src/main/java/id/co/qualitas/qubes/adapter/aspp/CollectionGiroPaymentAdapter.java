package id.co.qualitas.qubes.adapter.aspp;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
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

public class CollectionGiroPaymentAdapter extends RecyclerView.Adapter<CollectionGiroPaymentAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionFormActivity mContext;
    private OnAdapterListener onAdapterListener;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    protected CollectionGiroAdapter headerAdapter;
    protected boolean checked = true;

    public CollectionGiroPaymentAdapter(CollectionFormActivity mContext, CollectionGiroAdapter headerAdapter, List<Material> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
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
        TextView txtNo, txtProduct, txtPrice, txtLeft;
        EditText edtPaid;
        CheckBox cb;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtNo = itemView.findViewById(R.id.txtNo);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            edtPaid = itemView.findViewById(R.id.edtPaid);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtLeft = itemView.findViewById(R.id.txtLeft);
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
        holder.txtLeft.setText("Rp." + format.format(mContext.getKurangBayar(holder.getAbsoluteAdapterPosition())));
        holder.edtPaid.setText(Helper.setDotCurrencyAmount(detail.getAmountPaid()));

//        if (!itemStateArray.get(holder.getAbsoluteAdapterPosition(), false)) {
//            holder.cb.setChecked(false);
//            holder.edtPaid.setEnabled(false);
//            holder.edtPaid.setBackground(ContextCompat.getDrawable(mContext, R.drawable.editbox_disable));
//        } else {
//            holder.cb.setChecked(true);
//            holder.edtPaid.setEnabled(true);
//            holder.edtPaid.setBackground(ContextCompat.getDrawable(mContext, R.drawable.editbox));
//        }
//
//        if (detail.isChecked()) {
//            holder.cb.setChecked(true);
//            holder.edtPaid.setEnabled(true);
//            holder.edtPaid.setBackground(ContextCompat.getDrawable(mContext, R.drawable.editbox));
//        } else {
//            holder.cb.setChecked(false);
//            holder.edtPaid.setEnabled(false);
//            holder.edtPaid.setBackground(ContextCompat.getDrawable(mContext, R.drawable.editbox_disable));
//        }
//
//        holder.cb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (headerAdapter.getTotalAmount() != 0) {
//                    if (!itemStateArray.get(holder.getAbsoluteAdapterPosition(), false)) {
//                        holder.cb.setChecked(true);
//                        itemStateArray.put(holder.getAbsoluteAdapterPosition(), true);
//                        detail.setChecked(true);
//                        holder.edtPaid.setEnabled(true);
//                        holder.edtPaid.setBackground(ContextCompat.getDrawable(mContext, R.drawable.editbox));
//                    } else {
//                        holder.cb.setChecked(false);
//                        itemStateArray.put(holder.getAbsoluteAdapterPosition(), false);
//                        detail.setChecked(false);
//                        detail.setAmountPaid(0);
//                        holder.edtPaid.setText(null);
//                        holder.edtPaid.setEnabled(false);
//                        holder.edtPaid.setBackground(ContextCompat.getDrawable(mContext, R.drawable.editbox_disable));
//                        headerAdapter.setLeft();
//                    }
//                } else {
//                    Toast.makeText(mContext, "Masukkan total payment", Toast.LENGTH_SHORT).show();
//                    holder.cb.setChecked(false);
//                    itemStateArray.put(holder.getAbsoluteAdapterPosition(), false);
//                    detail.setChecked(false);
//                    detail.setAmountPaid(0);
//                    holder.edtPaid.setText(null);
//                    holder.edtPaid.setEnabled(false);
//                    holder.edtPaid.setBackground(ContextCompat.getDrawable(mContext, R.drawable.editbox_disable));
//                    headerAdapter.setLeft();
//                }
//            }
//        });

        holder.edtPaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (checked) {
                    Helper.setDotCurrency(holder.edtPaid, this, s);
                    if (!s.toString().equals("") && !s.toString().equals("-")) {
                        double qty = Double.parseDouble(s.toString().replace(",", ""));
                        if (qty > 0 && headerAdapter.getTotalAmount() > 0) {
                            if (mContext.getKurangBayar(holder.getAbsoluteAdapterPosition()) == 0) {
                                checked = false;
                                Toast.makeText(mContext, "Material ini sudah lunas", Toast.LENGTH_SHORT).show();
                                String qtyString = s.toString().replace(",", "");
                                double qtyR = Double.parseDouble(qtyString.substring(0, qtyString.length() - 1));
                                holder.edtPaid.setText(Helper.setDotCurrencyAmount(qtyR));
                                holder.edtPaid.setSelection(holder.edtPaid.getText().length());
                            } else if (headerAdapter.calculateLeft(qty, holder.getAbsoluteAdapterPosition()) < 0) {
                                checked = false;
                                Toast.makeText(mContext, "Saldo tidak cukup", Toast.LENGTH_SHORT).show();
                                String qtyString = s.toString().replace(",", "");
                                double qtyR = Double.parseDouble(qtyString.substring(0, qtyString.length() - 1));
                                holder.edtPaid.setText(Helper.setDotCurrencyAmount(qtyR));
                                holder.edtPaid.setSelection(holder.edtPaid.getText().length());
                            } else if (qty < 0) {
                                checked = false;
                                Toast.makeText(mContext, "Tidak boleh kurang dari 0", Toast.LENGTH_SHORT).show();
                                String qtyString = s.toString().replace(",", "");
                                double qtyR = Double.parseDouble(qtyString.substring(0, qtyString.length() - 1));
                                holder.edtPaid.setText(Helper.setDotCurrencyAmount(qtyR));
                                holder.edtPaid.setSelection(holder.edtPaid.getText().length());
                            } else if (qty > headerAdapter.getTotalAmount()) {
                                checked = false;
                                Toast.makeText(mContext, "Tidak boleh melebihi total amount", Toast.LENGTH_SHORT).show();
                                String qtyString = s.toString().replace(",", "");
                                double qtyR = Double.parseDouble(qtyString.substring(0, qtyString.length() - 1));
                                holder.edtPaid.setText(Helper.setDotCurrencyAmount(qtyR));
                                holder.edtPaid.setSelection(holder.edtPaid.getText().length());
                            } else if (qty > detail.getPrice()) {
                                checked = false;
                                Toast.makeText(mContext, "Tidak boleh melebihi harga barang", Toast.LENGTH_SHORT).show();
                                String qtyString = s.toString().replace(",", "");
                                double qtyR = Double.parseDouble(qtyString.substring(0, qtyString.length() - 1));
                                holder.edtPaid.setText(Helper.setDotCurrencyAmount(qtyR));
                                holder.edtPaid.setSelection(holder.edtPaid.getText().length());
                            } else {
                                detail.setAmountPaid(qty);
                                mContext.setKurangBayar(holder.getAbsoluteAdapterPosition(), 3);
                                holder.txtLeft.setText("Rp." + format.format(mContext.getKurangBayar(holder.getAbsoluteAdapterPosition())));
                            }
                        } else {
                            detail.setAmountPaid(0);
                            holder.edtPaid.setText(null);
                            Toast.makeText(mContext, "Masukkan total payment", Toast.LENGTH_SHORT).show();
                            mContext.setKurangBayar(holder.getAbsoluteAdapterPosition(), 3);
                            holder.txtLeft.setText("Rp." + format.format(mContext.getKurangBayar(holder.getAbsoluteAdapterPosition())));
                        }
                    } else {
                        detail.setAmountPaid(0);
                        mContext.setKurangBayar(holder.getAbsoluteAdapterPosition(), 3);
                        holder.txtLeft.setText("Rp." + format.format(mContext.getKurangBayar(holder.getAbsoluteAdapterPosition())));
                    }
                    headerAdapter.setLeft();
                } else {
                    checked = true;
                }
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

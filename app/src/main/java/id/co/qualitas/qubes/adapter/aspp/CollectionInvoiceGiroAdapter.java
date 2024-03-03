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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CollectionFormActivityNew;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.CollectionDetail;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;

public class CollectionInvoiceGiroAdapter extends RecyclerView.Adapter<CollectionInvoiceGiroAdapter.Holder> implements Filterable {
    private List<Invoice> mList;
    private List<Invoice> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionFormActivityNew mContext;
    private OnAdapterListener onAdapterListener;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    protected boolean checked = false;
    private CollectionItemGiroAdapter mAdapter;
    boolean visible = false;
    CollectionGiroNewAdapter giroAdapter;
    private Holder dataObjectHolder;
    int giroPosition;
    CollectionDetail collectionDetail;

    public CollectionInvoiceGiroAdapter(CollectionFormActivityNew mContext, CollectionGiroNewAdapter giroAdapter,
                                        int giroPosition, CollectionDetail collectionDetail, OnAdapterListener onAdapterListener) {
        if (Helper.isNotEmptyOrNull(collectionDetail.getInvoiceList())) {
            this.mList = collectionDetail.getInvoiceList();
            this.mFilteredList = collectionDetail.getInvoiceList();
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.collectionDetail = collectionDetail;
        this.giroAdapter = giroAdapter;
        this.mContext = mContext;
        this.giroPosition = giroPosition;
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
        TextView txtNoFaktur, txtTglJatuhTempo, txtTotalInv, txtTotalPaid, txtLeftAmount;
        TextView txtOutstanding, txtAmountInvoice;
        LinearLayout layout, llItem, llDelete;
        ImageView imgView;
        CheckBox cbAll;
        RecyclerView recyclerView;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtAmountInvoice = itemView.findViewById(R.id.txtAmountInvoice);
            txtOutstanding = itemView.findViewById(R.id.txtOutstanding);
            cbAll = itemView.findViewById(R.id.cbAll);
            txtNoFaktur = itemView.findViewById(R.id.txtNoFaktur);
            txtTglJatuhTempo = itemView.findViewById(R.id.txtTglJatuhTempo);
            txtLeftAmount = itemView.findViewById(R.id.txtLeftAmount);
            txtTotalInv = itemView.findViewById(R.id.txtTotalInv);
            txtTotalPaid = itemView.findViewById(R.id.txtTotalPaid);
            layout = itemView.findViewById(R.id.layout);
            llItem = itemView.findViewById(R.id.llItem);
            llDelete = itemView.findViewById(R.id.llDelete);
            imgView = itemView.findViewById(R.id.imgView);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_coll_invoice, parent, false);
        dataObjectHolder = new Holder(itemView, onAdapterListener);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        setFormatSeparator();
        Invoice detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());

        if (!itemStateArray.get(holder.getAbsoluteAdapterPosition(), false)) {
            holder.cbAll.setChecked(false);
        } else {
            holder.cbAll.setChecked(true);
        }

        if (mFilteredList.get(holder.getAbsoluteAdapterPosition()).isOpen()) {
            visible = true;
            holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_up));
            holder.llItem.setVisibility(View.VISIBLE);
            mFilteredList.get(holder.getAbsoluteAdapterPosition()).setOpen(true);
        } else {
            visible = false;
            holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_down_aspp));
            mFilteredList.get(holder.getAbsoluteAdapterPosition()).setOpen(false);
            holder.llItem.setVisibility(View.GONE);
        }

        holder.layout.setOnClickListener(v -> {
            if (visible) {
                visible = false;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_down_aspp));
                mFilteredList.get(holder.getAbsoluteAdapterPosition()).setOpen(false);
                holder.llItem.setVisibility(View.GONE);
            } else {
                visible = true;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_up));
                holder.llItem.setVisibility(View.VISIBLE);
                mFilteredList.get(holder.getAbsoluteAdapterPosition()).setOpen(true);
            }
        });

        holder.txtNoFaktur.setText(Helper.isEmpty(detail.getNo_invoice(), ""));
        if (!Helper.isNullOrEmpty(detail.getTanggal_jatuh_tempo())) {
            String dueDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_1, detail.getTanggal_jatuh_tempo());
            holder.txtTglJatuhTempo.setText(dueDate);
        }
        holder.txtTotalInv.setText("Rp." + format.format(detail.getAmount()));
        holder.txtAmountInvoice.setText("Rp." + format.format(detail.getNett()));
        holder.txtOutstanding.setText("Rp." + format.format(mContext.getOutstandingInvoice(mFilteredList.get(holder.getAbsoluteAdapterPosition()))));
//        holder.txtLeftAmount.setText("Rp." + format.format(calculateLeft(mFilteredList.get(holder.getAbsoluteAdapterPosition()))));
//        holder.txtTotalPaid.setText("Rp." + format.format(detail.getTotalPayment()));

        mAdapter = new CollectionItemGiroAdapter(mContext, giroAdapter, CollectionInvoiceGiroAdapter.this, giroPosition,
                holder.getAbsoluteAdapterPosition(), mFilteredList.get(holder.getAbsoluteAdapterPosition()), header -> {
        });
        holder.recyclerView.setAdapter(mAdapter);

        holder.llDelete.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogView);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView txtTitle = dialog.findViewById(R.id.txtTitle);
            TextView txtDialog = dialog.findViewById(R.id.txtDialog);
            Button btnNo = dialog.findViewById(R.id.btnNo);
            Button btnYes = dialog.findViewById(R.id.btnYes);
            txtTitle.setText("Hapus");
            txtDialog.setText("Anda yakin?");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFilteredList.remove(holder.getAbsoluteAdapterPosition());
//                    notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                    mContext.notifyAdapter(2);
                    dialog.dismiss();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

//        holder.txtTotalPaid.setOnClickListener(v -> {
//            if (collectionDetail.getTotalPayment() > 0) {
//                openDialogTotalPaid(holder.getAbsoluteAdapterPosition());
//            } else {
//                Toast.makeText(mContext, "Masukkan total payment", Toast.LENGTH_SHORT).show();
//            }
//        });

        holder.cbAll.setOnClickListener(v -> {
            if (!itemStateArray.get(holder.getAbsoluteAdapterPosition(), false)) {
                holder.cbAll.setChecked(true);
                itemStateArray.put(holder.getAbsoluteAdapterPosition(), true);
                setCheckedMaterial(holder.getAbsoluteAdapterPosition(), true);
            } else {
                holder.cbAll.setChecked(false);
                itemStateArray.put(holder.getAbsoluteAdapterPosition(), false);
                setCheckedMaterial(holder.getAbsoluteAdapterPosition(), false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Invoice Invoice);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }

//    public double calculateLeft(Invoice invoice) {
//        double left = 0, totalPaid = 0;
//        for (Material detail2 : invoice.getMaterialList()) {
//            totalPaid = totalPaid + detail2.getAmountPaid();
//        }
//        left = invoice.getTotalPayment() - totalPaid;
//        return left;
//    }
//
//    public double calculateLeftWoPos(Invoice invoice, String idMat) {
//        double left = 0, totalPaid = 0;
//        for (Material detail2 : invoice.getMaterialList()) {
//            if (!detail2.getId().equals(idMat)) {
//                totalPaid = totalPaid + detail2.getAmountPaid();
//            }
//        }
//        left = invoice.getTotalPayment() - totalPaid;
//        return left;
//    }

    private void setCheckedMaterial(int absoluteAdapterPosition, boolean checked) {
        double totalPaymentGiro = giroAdapter.getSisaTotalAmountExInvoice(absoluteAdapterPosition, mFilteredList.get(absoluteAdapterPosition).getNo_invoice());

        for (int i = 0; i < mFilteredList.get(absoluteAdapterPosition).getMaterialList().size(); i++) {
            Material detail = mFilteredList.get(absoluteAdapterPosition).getMaterialList().get(i);
            if (checked) {
                if (totalPaymentGiro > 0) {
                    double kurangBayarMaterial = mContext.getKurangBayarMaterial(mFilteredList.get(absoluteAdapterPosition), detail);
                    if (kurangBayarMaterial > 0) {
                        if (totalPaymentGiro > kurangBayarMaterial || totalPaymentGiro == kurangBayarMaterial) {
                            detail.setChecked(true);
                            detail.setAmountPaid(kurangBayarMaterial);
                            totalPaymentGiro = totalPaymentGiro - kurangBayarMaterial;
                        } else if (totalPaymentGiro < kurangBayarMaterial) {
                            detail.setChecked(true);
                            detail.setAmountPaid(totalPaymentGiro);
                            totalPaymentGiro = totalPaymentGiro - totalPaymentGiro;
                        }
                    }
                }
            } else {
                detail.setChecked(false);
                detail.setAmountPaid(0);
            }
        }
        mContext.notifyAdapter(2);
//        notifyItemChanged(absoluteAdapterPosition);
    }

//    private void openDialogTotalPaid(int pos) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        final Dialog dialog = new Dialog(mContext);
//        View dialogView = inflater.inflate(R.layout.aspp_dialog_amount_total, null);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(dialogView);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
//        TextView txtTypePayment = dialog.findViewById(R.id.txtTypePayment);
//        EditText edtTotalPayment = dialog.findViewById(R.id.edtTotalPayment);
//        Button btnCancel = dialog.findViewById(R.id.btnCancel);
//        Button btnSave = dialog.findViewById(R.id.btnSave);
//
//        txtTypePayment.setText("Giro");
//        edtTotalPayment.setText(mFilteredList.get(pos).getTotalPayment() != 0 ? Helper.setDotCurrencyAmount(mFilteredList.get(pos).getTotalPayment()) : null);
//
//        edtTotalPayment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Helper.setDotCurrency(edtTotalPayment, this, s);
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                double totalPayment = collectionDetail.getTotalPayment();
//                double qty = Double.parseDouble(edtTotalPayment.getText().toString().replace(",", ""));
//                if (qty < totalPayment || qty == totalPayment) {
//                    mFilteredList.get(pos).setTotalPayment(qty);
//                    for (Material detail : mFilteredList.get(pos).getMaterialList()) {
//                        detail.setChecked(false);
//                        detail.setAmountPaid(0);
//                    }
//                    giroAdapter.notifyItemChanged(giroPosition);
//                    dialog.dismiss();
//                } else {
//                    Toast.makeText(mContext, "Melebihi total payment giro", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        dialog.show();
//    }

    public void setCheckedAll(int idHeader) {
        int checked = 0;
        for (Material detail : mFilteredList.get(idHeader).getMaterialList()) {
            if (detail.isChecked()) checked++;
        }
        if (checked == mFilteredList.get(idHeader).getMaterialList().size()) {
            dataObjectHolder.cbAll.setChecked(true);
        } else {
            dataObjectHolder.cbAll.setChecked(false);
        }
    }

//    public double getTotalAmount(int transferPosition) {
//        return mFilteredList.get(transferPosition).getTotalPayment();
//    }
//
//    public void setLeftInvoice(int invoicePosition, Invoice invoice) {
//        double left = calculateLeft(invoice);
//        mFilteredList.get(invoicePosition).setLeft(left);
//        notifyItemChanged(invoicePosition);
//    }
}

package id.co.qualitas.qubes.adapter.aspp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.OrderAddActivity;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class OrderAddExtraAdapter extends RecyclerView.Adapter<OrderAddExtraAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private LayoutInflater mInflater;
    private OrderAddActivity mContext;
    private OnAdapterListener onAdapterListener;
    private LayoutInflater inflater;
    private Dialog alertDialog;
    private View dialogview;
    private int posHeader;
    private boolean isExpand = false;
    private List<Discount> mListDiskon;
    private List<Material> listSpinner, listFilteredSpinner;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private ArrayAdapter<String> uomAdapter;
    private Holder dataObjectHolder;
    private Material stockItem, itemOrder;
    private OrderAddAdapter headerAdapter;

    public OrderAddExtraAdapter(OrderAddActivity mContext, OrderAddAdapter headerAdapter, List<Material> mList, int posHeader, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.headerAdapter = headerAdapter;
        this.posHeader = posHeader;
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
        LinearLayout llDelete, llDiscountAll;
        LinearLayout llDiscountQty, llDiscountValue, llDiscountKelipatan;
        ImageView imgView;
        RecyclerView rvDiscount;
        AutoCompleteTextView autoCompleteUom;
        TextView txtNo, txtPrice, txtTotalDiscount, txtTotal;
        TextView txtDiscountQty, txtDiscountValue, txtDiscountKelipatan;
        EditText edtProduct, edtQty;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            llDelete = itemView.findViewById(R.id.llDelete);
            txtNo = itemView.findViewById(R.id.txtNo);
            edtProduct = itemView.findViewById(R.id.edtProduct);
            edtQty = itemView.findViewById(R.id.edtQty);
            autoCompleteUom = itemView.findViewById(R.id.autoCompleteUom);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtTotalDiscount = itemView.findViewById(R.id.txtTotalDiscount);
            imgView = itemView.findViewById(R.id.imgView);
            rvDiscount = itemView.findViewById(R.id.rvDiscount);
            rvDiscount.setLayoutManager(new LinearLayoutManager(mContext));
            rvDiscount.setHasFixedSize(true);
            llDiscountAll = itemView.findViewById(R.id.llDiscountAll);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_order_add_extra, parent, false);
        dataObjectHolder = new Holder(itemView, onAdapterListener);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        Material detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        setFormatSeparator();

        holder.txtNo.setText(format.format(posHeader + 1) + "." + format.format(holder.getAbsoluteAdapterPosition() + 1));
        holder.edtQty.clearFocus();
        holder.edtQty.setText(Helper.setDotCurrencyAmount(detail.getQty()));
        String productName = !Helper.isNullOrEmpty(detail.getNama()) ? detail.getNama() : null;
        String productId = String.valueOf(detail.getId());
        holder.edtProduct.setText(productId + " - " + productName);

        holder.imgView.setOnClickListener(v -> {
            if (!isExpand) {
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.ic_drop_up));
                holder.rvDiscount.setVisibility(View.VISIBLE);
                isExpand = true;
            } else {
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.ic_drop_down_aspp));
                holder.rvDiscount.setVisibility(View.GONE);
                isExpand = false;
            }
        });

        //uom
        List<String> listSpinner = new Database(mContext).getUom(detail.getId());
        if (listSpinner == null || listSpinner.size() == 0) {
            listSpinner.add("-");
        }
        uomAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                return view;
            }
        };
        uomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uomAdapter.addAll(listSpinner);
        holder.autoCompleteUom.setAdapter(uomAdapter);
        holder.autoCompleteUom.setText(detail.getUom(), false);
        holder.autoCompleteUom.setOnItemClickListener((adapterView, view, i, l) -> {
            String selected = listSpinner.get(i).toString();
            detail.setUom(selected);
            if (!Helper.isEmptyEditText(holder.edtQty) && !Helper.isNullOrEmpty(detail.getUom())) {
                if (Helper.isCanvasSales(SessionManagerQubes.getUserProfile())) {
                    Map req = new HashMap();
                    req.put("id_material", productId);
                    req.put("uom", detail.getUom());
                    stockItem = headerAdapter.getAllStockExtra(detail, holder.getAbsoluteAdapterPosition());
                    itemOrder = new Database(mContext).getQtySmallUom(detail);
                    if (itemOrder.getQty() > stockItem.getQty()) {
                        String ket = "Stock item ini: " + format.format(stockItem.getQty()) + " " + stockItem.getUom();
                        Toast.makeText(mContext, ket, Toast.LENGTH_SHORT).show();
                        holder.edtQty.clearFocus();
                        holder.edtQty.setText("0");
                        mFilteredList.get(holder.getAbsoluteAdapterPosition()).setQty(0);
                        mFilteredList.get(holder.getAbsoluteAdapterPosition()).setPrice(0);
                    }
                }
            }
        });
        //uom

        holder.edtQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.edtQty.isFocused()) {
                    Helper.setDotCurrency(holder.edtQty, this, s);
                    if (!s.toString().equals("") && !s.toString().equals("-")) {
                        double qty = Double.parseDouble(s.toString().replace(",", ""));
                        if (!Helper.isNullOrEmpty(detail.getUom())) {
                            if (Helper.isCanvasSales(SessionManagerQubes.getUserProfile())) {
                                Map req = new HashMap();
                                req.put("id_material", productId);
                                req.put("uom", detail.getUom());
                                stockItem = headerAdapter.getAllStockExtra(detail, holder.getAbsoluteAdapterPosition());
                                Material mat = new Material();
                                mat.setQty(qty);
                                mat.setId(productId);
                                mat.setUom(detail.getUom());
                                itemOrder = new Database(mContext).getQtySmallUom(mat);
                                if (itemOrder.getQty() > stockItem.getQty()) {
                                    String ket = "Stock item ini: " + format.format(stockItem.getQty()) + " " + stockItem.getUom();
                                    Toast.makeText(mContext, ket, Toast.LENGTH_SHORT).show();
                                    holder.edtQty.clearFocus();
                                    holder.edtQty.setText("0");
                                } else {
                                    mFilteredList.get(holder.getAbsoluteAdapterPosition()).setQty(qty);
                                    mFilteredList.get(holder.getAbsoluteAdapterPosition()).setPrice(new Database(mContext).getPrice(mFilteredList.get(holder.getAbsoluteAdapterPosition())));
                                }
                            } else {
                                mFilteredList.get(holder.getAbsoluteAdapterPosition()).setQty(qty);
                                mFilteredList.get(holder.getAbsoluteAdapterPosition()).setPrice(new Database(mContext).getPrice(mFilteredList.get(holder.getAbsoluteAdapterPosition())));
                            }
                        } else {
                            mFilteredList.get(holder.getAbsoluteAdapterPosition()).setQty(qty);
                            mFilteredList.get(holder.getAbsoluteAdapterPosition()).setPrice(new Database(mContext).getPrice(mFilteredList.get(holder.getAbsoluteAdapterPosition())));
                        }
                    }
//                else {
//                    mFilteredList.get(holder.getAbsoluteAdapterPosition()).setQty(0);
//                }
                }
            }
        });

        holder.llDelete.setOnClickListener(v -> {
            inflater = LayoutInflater.from(mContext);
            alertDialog = new Dialog(mContext);
            initDialog(R.layout.aspp_dialog_confirmation);
            TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
            TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);
            Button btnNo = alertDialog.findViewById(R.id.btnNo);
            Button btnYes = alertDialog.findViewById(R.id.btnYes);
            txtTitle.setText("Delete Extra");
            txtDialog.setText("Are you sure want to delete the item?");
            btnYes.setOnClickListener(view -> {
                mFilteredList.remove(holder.getAbsoluteAdapterPosition());
//                    notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                notifyDataSetChanged();
                alertDialog.dismiss();
            });
            btnNo.setOnClickListener(view -> alertDialog.dismiss());
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Material Material);
    }

    private void initDialog(int resource) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialogview = inflater.inflate(resource, null);
        alertDialog.setContentView(dialogview);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);//back di hp
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
//        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}

package id.co.qualitas.qubes.adapter.aspp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
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
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;

public class OrderAddAdapter extends RecyclerView.Adapter<OrderAddAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private LayoutInflater mInflater;
    private OrderAddActivity mContext;
    private OnAdapterListener onAdapterListener;
    private OrderAddExtraAdapter mAdapter;
    private OrderDiscountAdapter mAdapterDiscount;
    private ProgressDialog progress;
    private LayoutInflater inflater;
    private Dialog alertDialog;
    private View dialogview;
    private boolean isExpand = false;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    protected Customer outletHeader;
    private Holder dataObjectHolder;
    private ArrayAdapter<String> uomAdapter;
    private List<Material> mListExtra;
    private List<Discount> mListDiskon;
    private List<Material> listSpinner, listFilteredSpinner;
    boolean checkedAll = false;
    private Material minimalOrder;

    public OrderAddAdapter(OrderAddActivity mContext, List<Material> mList, Customer outletHeader, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.outletHeader = outletHeader;
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
        LinearLayout llAddExtraItem, llDelete, llDiscountAll;
        RecyclerView rvExtra, rvDiscount;
        ImageView imgView;
        AutoCompleteTextView autoCompleteUom;
        TextView txtNo, txtPrice, txtTotalDiscount, txtTotal;
        EditText edtProduct, edtQty;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            llDiscountAll = itemView.findViewById(R.id.llDiscountAll);
            rvExtra = itemView.findViewById(R.id.rvExtra);
            rvExtra.setLayoutManager(new LinearLayoutManager(mContext));
            rvExtra.setHasFixedSize(true);
            rvDiscount = itemView.findViewById(R.id.rvDiscount);
            rvDiscount.setLayoutManager(new LinearLayoutManager(mContext));
            rvDiscount.setHasFixedSize(true);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            llAddExtraItem = itemView.findViewById(R.id.llAddExtraItem);
            llDelete = itemView.findViewById(R.id.llDelete);
            txtNo = itemView.findViewById(R.id.txtNo);
            edtProduct = itemView.findViewById(R.id.edtProduct);
            edtQty = itemView.findViewById(R.id.edtQty);
            autoCompleteUom = itemView.findViewById(R.id.autoCompleteUom);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtTotalDiscount = itemView.findViewById(R.id.txtTotalDiscount);
            imgView = itemView.findViewById(R.id.imgView);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_order_add, parent, false);
        dataObjectHolder = new Holder(itemView, onAdapterListener);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        Material detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        setFormatSeparator();
        setProgress();
        mListExtra = new ArrayList<>();
        mListDiskon = new ArrayList<>();
        if (Helper.isNotEmptyOrNull(detail.getExtraItem())) {
            mListExtra.addAll(detail.getExtraItem());
        }
        if (Helper.isNotEmptyOrNull(detail.getDiskonList())) {
            mListDiskon.addAll(detail.getDiskonList());
        }
        holder.txtNo.setText(format.format(holder.getAbsoluteAdapterPosition() + 1) + ".");
        String productName = !Helper.isNullOrEmpty(detail.getNama()) ? detail.getNama() : null;
        String productId = String.valueOf(detail.getId());
        holder.edtProduct.setText(productId + " - " + productName);
        if (detail.getQty() == 0 && !Helper.isEmpty(detail.getUom())) {
            Map req = new HashMap();
            req.put("id", productId);
            req.put("uom", detail.getUom());
            minimalOrder = new Database(mContext).getMinimalOrder(req);
            holder.edtQty.setText(Helper.setDotCurrencyAmount(minimalOrder.getQty()));
        } else {
            holder.edtQty.setText(Helper.setDotCurrencyAmount(detail.getQty()));
        }
        holder.txtPrice.setText("Rp. " + format.format(detail.getPrice()));
        holder.txtTotalDiscount.setText("Rp. " + format.format(detail.getTotalDiscount()));

        if (Helper.isNotEmptyOrNull(detail.getDiskonList())) {
            holder.llDiscountAll.setVisibility(View.VISIBLE);
            mAdapterDiscount = new OrderDiscountAdapter(mContext, mListDiskon, header -> {
            });
            holder.rvDiscount.setAdapter(mAdapterDiscount);
        } else {
            holder.llDiscountAll.setVisibility(View.GONE);
        }

        List<String> listSpinner = new Database(mContext).getUom(detail.getId());
        if (listSpinner == null || listSpinner.size() == 0) {
            listSpinner.add("-");
        }

        mAdapter = new OrderAddExtraAdapter(mContext, mListExtra, holder.getAbsoluteAdapterPosition(), header -> {
        });
        holder.rvExtra.setAdapter(mAdapter);

        //uom
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
            Map req = new HashMap();
            req.put("id", productId);
            req.put("uom", detail.getUom());
            minimalOrder = new Database(mContext).getMinimalOrder(req);

            if (detail.getQty() < minimalOrder.getQty()) {
                holder.edtQty.setText(Helper.setDotCurrencyAmount(minimalOrder.getQty()));
                detail.setQty(minimalOrder.getQty());
                Toast.makeText(mContext, "minimal order : " + format.format(minimalOrder.getQty()), Toast.LENGTH_SHORT).show();
            } else {
                detail.setPrice(new Database(mContext).getPrice(detail));
                holder.txtPrice.setText("Rp. " + format.format(detail.getPrice()));
                mContext.calculateOmzet();
                calculateTotal(holder.getAbsoluteAdapterPosition());
            }
        });
        //uom

        holder.llAddExtraItem.setOnClickListener(v -> {
            addExtraItem(holder.getAbsoluteAdapterPosition());
//            addNew(holder.rvExtra, holder.getAbsoluteAdapterPosition());
        });

        holder.llDelete.setOnClickListener(v -> {
            inflater = LayoutInflater.from(mContext);
            alertDialog = new Dialog(mContext);
            initDialog(R.layout.aspp_dialog_confirmation);
            TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
            TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);
            Button btnNo = alertDialog.findViewById(R.id.btnNo);
            Button btnYes = alertDialog.findViewById(R.id.btnYes);
            txtTitle.setText("Delete");
            txtDialog.setText("Are you sure want to delete the item?");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFilteredList.remove(holder.getAbsoluteAdapterPosition());
                    notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                    alertDialog.dismiss();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        });

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

        holder.edtQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Helper.setDotCurrency(holder.edtQty, this, s);
                if (!s.toString().equals("") && !s.toString().equals("-")) {
                    int qty = Integer.parseInt(s.toString().replace(",", ""));
                    if (!Helper.isNullOrEmpty(detail.getUom())) {
                        Map req = new HashMap();
                        req.put("id", productId);
                        req.put("uom", detail.getUom());
                        minimalOrder = new Database(mContext).getMinimalOrder(req);

                        if (detail.getQty() < minimalOrder.getQty()) {
                            holder.edtQty.setText(Helper.setDotCurrencyAmount(minimalOrder.getQty()));
                            detail.setQty(minimalOrder.getQty());
                            Toast.makeText(mContext, "minimal order : " + format.format(minimalOrder.getQty()), Toast.LENGTH_SHORT).show();
                        } else {
                            detail.setQty(qty);
                        }
                    }
                } else {
                    detail.setQty(0);
                }
                if (!Helper.isNullOrEmpty(detail.getUom())) {
                    detail.setPrice(new Database(mContext).getPrice(detail));
                    holder.txtPrice.setText("Rp. " + format.format(detail.getPrice()));
                    mContext.calculateOmzet();
                    calculateTotal(holder.getAbsoluteAdapterPosition());
                }
            }
        });
    }

    private void calculateTotal(int pos) {
        double priceTotal = 0;
        priceTotal = mFilteredList.get(pos).getPrice() + mFilteredList.get(pos).getTotalDiscount();
        dataObjectHolder.txtTotal.setText("Rp. " + format.format(priceTotal));
        //tambah diskon?
    }

    private void addExtraItem(int pos) {
        Dialog dialog = new Dialog(mContext);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner_product);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        CardView cvUnCheckAll = dialog.findViewById(R.id.cvUnCheckAll);
        CardView cvCheckedAll = dialog.findViewById(R.id.cvCheckedAll);
        RelativeLayout checkbox = dialog.findViewById(R.id.checkbox);
        EditText editText = dialog.findViewById(R.id.edit_text);
        RecyclerView rv = dialog.findViewById(R.id.rv);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        if (Helper.isNotEmptyOrNull(mList)) {
            checkbox.setVisibility(View.VISIBLE);
        } else {
            checkbox.setVisibility(View.GONE);
        }

        listSpinner = new ArrayList<>();
        listSpinner.addAll(initDataMaterial());

        SpinnerProductOrderAdapter spinnerAdapter = new SpinnerProductOrderAdapter(mContext, listSpinner, (nameItem, adapterPosition) -> {
        });

        rv.setLayoutManager(new LinearLayoutManager(mContext));
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(spinnerAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spinnerAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cvCheckedAll.setOnClickListener(v -> {
            if (listFilteredSpinner == null) {
                listFilteredSpinner = new ArrayList<>();
            }
            checkedAll = false;
            if (!listFilteredSpinner.isEmpty()) {
                for (Material mat : listFilteredSpinner) {
                    mat.setChecked(checkedAll);
                }
            } else {
                for (Material mat : listSpinner) {
                    mat.setChecked(checkedAll);
                }
            }
            spinnerAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.VISIBLE);
            cvCheckedAll.setVisibility(View.GONE);
        });

        cvUnCheckAll.setOnClickListener(v -> {
            if (listFilteredSpinner == null) {
                listFilteredSpinner = new ArrayList<>();
            }
            checkedAll = true;
            if (!listFilteredSpinner.isEmpty()) {
                for (Material mat : listFilteredSpinner) {
                    mat.setChecked(checkedAll);
                }
            } else {
                for (Material mat : listSpinner) {
                    mat.setChecked(checkedAll);
                }
            }
            spinnerAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.GONE);
            cvCheckedAll.setVisibility(View.VISIBLE);
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnSave.setOnClickListener(v -> {
            List<Material> addList = new ArrayList<>();
            for (Material mat : listSpinner) {
                if (mat.isChecked()) {
                    if (Helper.isNullOrEmpty(mat.getUomSisa())) {
                        Map req = new HashMap();
                        req.put("udf5", outletHeader.getUdf_5());
                        req.put("productId", mat.getId_product_group());
                        req.put("matId", mat.getId());
                        Material matDetail = new Database(mContext).getPriceMaterial(req);
                        addList.add(matDetail);
                    } else {
                        addList.add(mat);
                    }
                }
            }
            addNewExtra(addList, pos);
            dialog.dismiss();
        });
    }

    private void addNewExtra(List<Material> addedList, int pos) {
        mListExtra.addAll(addedList);
        if (mAdapter == null) {
            mAdapter = new OrderAddExtraAdapter(mContext, mListExtra, pos, header -> {
            });
            dataObjectHolder.rvExtra.setAdapter(mAdapter);
        }
        if (mListExtra.size() == 1) {
            mFilteredList.get(pos).setExtraItem(mListExtra);
            mAdapter.setData(mListExtra);
        } else {
            new CountDownTimer(1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    progress.show();
                    int sizeList = mListExtra.size();
                    mAdapter.notifyItemInserted(sizeList);
                }

                public void onFinish() {
                    progress.dismiss();
                    dataObjectHolder.rvExtra.smoothScrollToPosition(dataObjectHolder.rvExtra.getAdapter().getItemCount() - 1);
                }
            }.start();
        }
    }

    private List<Material> initDataMaterial() {
        //perlu d tany extra item itu bebas, atau sesuai sama top nya
        List<Material> listSpinner = new ArrayList<>();
        List<Material> listMat = new ArrayList<>();
        listMat.addAll(new Database(mContext).getAllMasterMaterial());
        for (Material param : listMat) {
            int exist = 0;
            for (Material param1 : mListExtra) {
                if (param.getId().equals(param1.getId())) {
                    exist++;
                }
            }
            if (exist == 0) {
                listSpinner.add(param);
            }
        }

        return listSpinner;
    }

    private void addNew(RecyclerView rvExtra, int pos) {
        List<Material> mListExtra = mFilteredList.get(pos).getExtraItem();
        if (mListExtra == null) mListExtra = new ArrayList<>();
        Material detail = new Material("", "", "", "");
        mListExtra.add(detail);
        mAdapter = new OrderAddExtraAdapter(mContext, mListExtra, pos, header -> {
        });
        rvExtra.setAdapter(mAdapter);
        if (mListExtra.size() == 1) {
            mFilteredList.get(pos).setExtraItem(mListExtra);
            mAdapter.setData(mListExtra);
        } else {
            List<Material> finalMListExtra = mListExtra;
            new CountDownTimer(1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    progress.show();
                    int sizeList = finalMListExtra.size();
                    mAdapter.notifyItemInserted(sizeList);
                }

                public void onFinish() {
                    progress.dismiss();
                    rvExtra.smoothScrollToPosition(rvExtra.getAdapter().getItemCount() - 1);
                }
            }.start();
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Material Material);
    }

    public void setProgress() {
        progress = new ProgressDialog(mContext);
        progress.setMessage(Constants.STR_WAIT);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
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

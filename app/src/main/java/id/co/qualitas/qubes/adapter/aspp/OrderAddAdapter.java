package id.co.qualitas.qubes.adapter.aspp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Looper;
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
import androidx.work.Data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.OrderAddActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;

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
    private Material minMaxOrder, stockItem, itemOrder;
    private StockRequest stockHeader;
    private User user;

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
        final Material[] detail = {mFilteredList.get(holder.getAbsoluteAdapterPosition())};
        setFormatSeparator();
        setProgress();
        user = SessionManagerQubes.getUserProfile();
        if (Helper.isCanvasSales(user)) {
            stockHeader = new Database((mContext)).getAllStockMaterial(user);
        }
        mListExtra = new ArrayList<>();
        mListDiskon = new ArrayList<>();
        if (Helper.isNotEmptyOrNull(detail[0].getExtraItem())) {
            mListExtra.addAll(detail[0].getExtraItem());
        }
        if (Helper.isNotEmptyOrNull(detail[0].getDiskonList())) {
            mListDiskon.addAll(detail[0].getDiskonList());
        }
        holder.txtNo.setText(format.format(holder.getAbsoluteAdapterPosition() + 1) + ".");
        String productName = !Helper.isNullOrEmpty(detail[0].getNama()) ? detail[0].getNama() : null;
        String productId = String.valueOf(detail[0].getId());
        holder.edtProduct.setText(productId + " - " + productName);
//        if (detail.getQty() == 0 && !Helper.isEmpty(detail.getUom())) {
//            Map req = new HashMap();
//            req.put("id", productId);
//            req.put("uom", detail.getUom());
//            minMaxOrder = new Database(mContext).getMinimalOrder(req);
//            holder.edtQty.setText(Helper.setDotCurrencyAmount(minMaxOrder.getQty()));
//        } else {
        holder.edtQty.clearFocus();
        holder.edtQty.setText(Helper.setDotCurrencyAmount(detail[0].getQty()));
//        }
        holder.txtPrice.setText("Rp. " + format.format(detail[0].getPrice()));
        holder.txtTotalDiscount.setText("Rp. " + format.format(detail[0].getTotalDiscount()));
        double priceTotal = detail[0].getPrice() - detail[0].getTotalDiscount();
        holder.txtTotal.setText("Rp. " + format.format(priceTotal));

        if (Helper.isNotEmptyOrNull(detail[0].getDiskonList())) {
            holder.llDiscountAll.setVisibility(View.VISIBLE);
            mAdapterDiscount = new OrderDiscountAdapter(mContext, mListDiskon, header -> {
            });
            holder.rvDiscount.setAdapter(mAdapterDiscount);
        } else {
            holder.llDiscountAll.setVisibility(View.GONE);
        }

        List<String> listSpinner = new Database(mContext).getUom(detail[0].getId());
        if (listSpinner == null || listSpinner.size() == 0) {
            listSpinner.add("-");
        }

        mAdapter = new OrderAddExtraAdapter(mContext, OrderAddAdapter.this, mListExtra, holder.getAbsoluteAdapterPosition(), header -> {
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
        holder.autoCompleteUom.setText(detail[0].getUom(), false);
        holder.autoCompleteUom.setOnItemClickListener((adapterView, view, i, l) -> {
            detail[0] = mFilteredList.get(holder.getAbsoluteAdapterPosition());
            String selected = listSpinner.get(i).toString();
            detail[0].setUom(selected);
            if (!Helper.isEmptyEditText(holder.edtQty) && !Helper.isNullOrEmpty(detail[0].getUom())) {
                Map req = new HashMap();
                req.put("id_material", productId);
                req.put("uom", detail[0].getUom());
                req.put("id_customer", outletHeader.getId());
                minMaxOrder = new Database(mContext).getMinimalOrder(req);
                double qty = Double.parseDouble(holder.edtQty.getText().toString().replace(",", ""));
                if (!Helper.isCanvasSales(user)) {
//                    detail.setPrice(new Database(mContext).getPrice(detail));
//                    holder.txtPrice.setText("Rp. " + format.format(detail.getPrice()));
//                    double pt = detail.getPrice() - detail.getTotalDiscount();
//                    holder.txtTotal.setText("Rp. " + format.format(pt));
                    if (qty < minMaxOrder.getQtyMin() || qty > (minMaxOrder.getQtyMax() == 0 ? qty + 1 : minMaxOrder.getQtyMax())) {
                        String ket = "min qty : " + format.format(minMaxOrder.getQtyMin()) + " " + minMaxOrder.getUom() + "\n" + "max qty : " + format.format(minMaxOrder.getQtyMax()) + " " + minMaxOrder.getUom();
                        Toast.makeText(mContext, ket, Toast.LENGTH_SHORT).show();
                        holder.edtQty.clearFocus();
                        holder.edtQty.setText("0");
                        detail[0].setQty(0);
                        detail[0].setPrice(0);
                        detail[0].setDiskonList(null);
                        detail[0].setTotalDiscount(0);
                        holder.txtPrice.setText("0");
                        holder.txtTotal.setText("0");
                        holder.llDiscountAll.setVisibility(View.GONE);
                    } else {
                        detail[0].setPrice(new Database(mContext).getPrice(detail[0]));
                        holder.txtPrice.setText("Rp. " + format.format(detail[0].getPrice()));
                        double pt = detail[0].getPrice() - detail[0].getTotalDiscount();
                        holder.txtTotal.setText("Rp. " + format.format(pt));
                    }
                } else {
                    stockItem = getAllStock(holder.getAbsoluteAdapterPosition());
                    Material mat = new Material();
                    mat.setQty(qty);
                    mat.setId(productId);
                    mat.setUom(detail[0].getUom());
                    itemOrder = new Database(mContext).getQtySmallUom(mat);
                    boolean overMaxQty = minMaxOrder.getQtyMax() == 0 ? true : false;
                    if (qty < minMaxOrder.getQtyMin() || (overMaxQty ? false : (qty > minMaxOrder.getQtyMax()))) {
//                    qty > (minMaxOrder.getQtyMax() == 0 ? qty + 1 : minMaxOrder.getQtyMax())) {
                        String ket = "min qty : " + format.format(minMaxOrder.getQtyMin()) + " " + minMaxOrder.getUom() + "\n" + "max qty : " + format.format(minMaxOrder.getQtyMax()) + " " + minMaxOrder.getUom();
                        Toast.makeText(mContext, ket, Toast.LENGTH_SHORT).show();
                        holder.edtQty.clearFocus();
                        holder.edtQty.setText("0");
                        detail[0].setQty(0);
                        detail[0].setPrice(0);
                        detail[0].setDiskonList(null);
                        detail[0].setTotalDiscount(0);
                        holder.txtPrice.setText("0");
                        holder.txtTotal.setText("0");
                        holder.llDiscountAll.setVisibility(View.GONE);
                    } else if (itemOrder.getQty() > stockItem.getQty()) {
                        String ket = "Stock item ini: " + format.format(stockItem.getQty()) + " " + stockItem.getUom();
                        Toast.makeText(mContext, ket, Toast.LENGTH_SHORT).show();
                        holder.edtQty.clearFocus();
                        holder.edtQty.setText("0");
                        detail[0].setQty(0);
                        detail[0].setPrice(0);
                        detail[0].setDiskonList(null);
                        detail[0].setTotalDiscount(0);
                        holder.txtPrice.setText("0");
                        holder.txtTotal.setText("0");
                        holder.llDiscountAll.setVisibility(View.GONE);
                    } else {
                        detail[0].setPrice(new Database(mContext).getPrice(detail[0]));
                        holder.txtPrice.setText("Rp. " + format.format(detail[0].getPrice()));
                        double pt = detail[0].getPrice() - detail[0].getTotalDiscount();
                        holder.txtTotal.setText("Rp. " + format.format(pt));
                    }
                }
                mContext.removeOmzet();
            }
        });
        //uom

        holder.llAddExtraItem.setOnClickListener(v -> {
            detail[0] = mFilteredList.get(holder.getAbsoluteAdapterPosition());
//            addExtraItem(holder.getAbsoluteAdapterPosition(), detail);
            if (detail[0].getQty() > 0 || !Helper.isNullOrEmpty(detail[0].getUom())) {
                Dialog dialog = new Dialog(mContext);

                dialog.setContentView(R.layout.aspp_dialog_searchable_spinner_product);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                Button btnSearch = dialog.findViewById(R.id.btnSearch);
                btnSearch.setVisibility(View.GONE);
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

                List<Material> listSpinnerMat = new ArrayList<>();
                listSpinnerMat.addAll(initDataMaterial(pos, detail[0]));

                SpinnerProductOrderAdapter spinnerAdapter = new SpinnerProductOrderAdapter(mContext, listSpinnerMat, user, (nameItem, adapterPosition) -> {
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

                cvCheckedAll.setOnClickListener(v1 -> {
                    if (listFilteredSpinner == null) {
                        listFilteredSpinner = new ArrayList<>();
                    }
                    checkedAll = false;
                    if (!listFilteredSpinner.isEmpty()) {
                        for (Material mat : listFilteredSpinner) {
                            mat.setChecked(checkedAll);
                        }
                    } else {
                        for (Material mat : listSpinnerMat) {
                            mat.setChecked(checkedAll);
                        }
                    }
                    spinnerAdapter.notifyDataSetChanged();
                    cvUnCheckAll.setVisibility(View.VISIBLE);
                    cvCheckedAll.setVisibility(View.GONE);
                });

                cvUnCheckAll.setOnClickListener(v2 -> {
                    if (listFilteredSpinner == null) {
                        listFilteredSpinner = new ArrayList<>();
                    }
                    checkedAll = true;
                    if (!listFilteredSpinner.isEmpty()) {
                        for (Material mat : listFilteredSpinner) {
                            mat.setChecked(checkedAll);
                        }
                    } else {
                        for (Material mat : listSpinnerMat) {
                            mat.setChecked(checkedAll);
                        }
                    }
                    spinnerAdapter.notifyDataSetChanged();
                    cvUnCheckAll.setVisibility(View.GONE);
                    cvCheckedAll.setVisibility(View.VISIBLE);
                });

                btnCancel.setOnClickListener(v4 -> {
                    dialog.dismiss();
                });

                btnSave.setOnClickListener(v5 -> {
                    if(Helper.isNotEmptyOrNull(listSpinnerMat)) {
                        List<Material> addList = new ArrayList<>();
                        for (Material mat : listSpinnerMat) {
                            if (mat.isChecked()) {
                                addList.add(mat);
                            }
                        }
                        mListExtra = new ArrayList<>();
                        if (Helper.isNotEmptyOrNull(mFilteredList.get(pos).getExtraItem())) {
                            mListExtra.addAll(mFilteredList.get(pos).getExtraItem());
                        }
                        mListExtra.addAll(addList);
                        mAdapter = new OrderAddExtraAdapter(mContext, OrderAddAdapter.this, mListExtra, pos, header -> {
                        });
                        holder.rvExtra.setAdapter(mAdapter);
//                    if (mListExtra.size() > 0) {
                        mFilteredList.get(pos).setExtraItem(mListExtra);
                        mAdapter.setData(mListExtra);
//                    } else {
                        new CountDownTimer(1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                progress.show();
                                int sizeList = mListExtra.size();
                                mAdapter.notifyItemInserted(sizeList);
                            }

                            public void onFinish() {
                                progress.dismiss();
                                holder.rvExtra.smoothScrollToPosition(holder.rvExtra.getAdapter().getItemCount() - 1);
                            }
                        }.start();
                    }else{
                        Toast.makeText(mContext, "No data", Toast.LENGTH_SHORT).show();
                    }
//                    }
                    dialog.dismiss();
                });
            } else {
                Toast.makeText(mContext, "Qty dan Uom tidak boleh kosong", Toast.LENGTH_SHORT).show();
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
            txtTitle.setText("Delete");
            txtDialog.setText("Are you sure want to delete the item?");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFilteredList.remove(holder.getAbsoluteAdapterPosition());
                    notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                    mContext.removeOmzet();
                    mContext.resizeView();
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

        holder.txtTotalDiscount.setOnClickListener(v -> {
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

            private Timer timer = new Timer();
            private final long DELAY = 2000; // Milliseconds

            @Override
            public void afterTextChanged(Editable s) {
                detail[0] = mFilteredList.get(holder.getAbsoluteAdapterPosition());
                if (holder.edtQty.isFocused()) {
                    Helper.setDotCurrency(holder.edtQty, this, s);
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    if (!s.toString().equals("") && !s.toString().equals("-")) {
                                        double qty = Double.parseDouble(s.toString().replace(",", ""));
                                        if (!Helper.isNullOrEmpty(detail[0].getUom())) {
                                            Map req = new HashMap();
                                            req.put("id_material", productId);
                                            req.put("uom", detail[0].getUom());
                                            req.put("id_customer", outletHeader.getId());
                                            minMaxOrder = new Database(mContext).getMinimalOrder(req);
                                            if (!Helper.isCanvasSales(user)) {
//                                            detail.setQty(qty);
//                                            if (!Helper.isNullOrEmpty(detail.getUom())) {
//                                                detail.setPrice(new Database(mContext).getPrice(detail));
//                                                holder.txtPrice.setText("Rp. " + format.format(detail.getPrice()));
//                                                double priceTotal = detail.getPrice() - detail.getTotalDiscount();
//                                                holder.txtTotal.setText("Rp. " + format.format(priceTotal));
//                                            }
                                                boolean overMaxQty = minMaxOrder.getQtyMax() == 0 ? true : false;
//                                                if (qty < minMaxOrder.getQtyMin() || qty > (minMaxOrder.getQtyMax() == 0 ? qty + 1 : minMaxOrder.getQtyMax())) {
                                                if (qty < minMaxOrder.getQtyMin() || (overMaxQty ? false : (qty > minMaxOrder.getQtyMax()))) {
                                                    String ket = "min qty : " + format.format(minMaxOrder.getQtyMin()) + " " + minMaxOrder.getUom() + "\n" + "max qty : " + format.format(minMaxOrder.getQtyMax()) + " " + minMaxOrder.getUom();
                                                    mContext.runOnUiThread(() -> {
                                                        holder.edtQty.clearFocus();
                                                        holder.edtQty.setText("0");
                                                        holder.txtPrice.setText("0");
                                                        holder.txtTotal.setText("0");
                                                        holder.llDiscountAll.setVisibility(View.GONE);
                                                    });
                                                    Toast.makeText(mContext, ket, Toast.LENGTH_SHORT).show();
                                                    detail[0].setQty(0);
                                                    detail[0].setPrice(0);
                                                    detail[0].setDiskonList(null);
                                                    detail[0].setTotalDiscount(0);
                                                } else {
                                                    mContext.runOnUiThread(() -> {
                                                        detail[0].setQty(qty);
                                                        if (!Helper.isNullOrEmpty(detail[0].getUom())) {
                                                            detail[0].setPrice(new Database(mContext).getPrice(detail[0]));
                                                            holder.txtPrice.setText("Rp. " + format.format(detail[0].getPrice()));
                                                            double priceTotal1 = detail[0].getPrice() - detail[0].getTotalDiscount();
                                                            holder.txtTotal.setText("Rp. " + format.format(priceTotal1));
                                                        }
                                                    });
                                                }
                                            } else {
                                                stockItem = getAllStock(holder.getAbsoluteAdapterPosition());
                                                Material mat = new Material();
                                                mat.setQty(qty);
                                                mat.setId(productId);
                                                mat.setUom(detail[0].getUom());
                                                itemOrder = new Database(mContext).getQtySmallUom(mat);
                                                boolean overMaxQty = minMaxOrder.getQtyMax() == 0 ? true : false;
//                                                if (qty < minMaxOrder.getQtyMin() || qty > (minMaxOrder.getQtyMax() == 0 ? qty + 1 : minMaxOrder.getQtyMax())) {
                                                if (qty < minMaxOrder.getQtyMin() || (overMaxQty ? false : (qty > minMaxOrder.getQtyMax()))) {
                                                    String ket = "min qty : " + format.format(minMaxOrder.getQtyMin()) + " " + minMaxOrder.getUom() + "\n" + "max qty : " + format.format(minMaxOrder.getQtyMax()) + " " + minMaxOrder.getUom();
                                                    mContext.runOnUiThread(() -> {
                                                        holder.edtQty.clearFocus();
                                                        holder.edtQty.setText("0");
                                                        holder.txtPrice.setText("0");
                                                        holder.txtTotal.setText("0");
                                                        holder.llDiscountAll.setVisibility(View.GONE);
                                                    });
                                                    Toast.makeText(mContext, ket, Toast.LENGTH_SHORT).show();
                                                    detail[0].setQty(0);
                                                    detail[0].setPrice(0);
                                                    detail[0].setDiskonList(null);
                                                    detail[0].setTotalDiscount(0);
                                                } else if (itemOrder.getQty() > stockItem.getQty()) {
                                                    String ket = "Stock item ini: " + format.format(stockItem.getQty()) + " " + stockItem.getUom();
                                                    mContext.runOnUiThread(() -> {
                                                        holder.edtQty.clearFocus();
                                                        holder.edtQty.setText("0");
                                                        holder.txtPrice.setText("0");
                                                        holder.txtTotal.setText("0");
                                                        holder.llDiscountAll.setVisibility(View.GONE);
                                                    });
                                                    Toast.makeText(mContext, ket, Toast.LENGTH_SHORT).show();
                                                    detail[0].setQty(0);
                                                    detail[0].setPrice(0);
                                                    detail[0].setDiskonList(null);
                                                    detail[0].setTotalDiscount(0);
                                                } else {
                                                    mContext.runOnUiThread(() -> {
                                                        detail[0].setQty(qty);
                                                        if (!Helper.isNullOrEmpty(detail[0].getUom())) {
                                                            detail[0].setPrice(new Database(mContext).getPrice(detail[0]));
                                                            holder.txtPrice.setText("Rp. " + format.format(detail[0].getPrice()));
                                                            double priceTotal12 = detail[0].getPrice() - detail[0].getTotalDiscount();
                                                            holder.txtTotal.setText("Rp. " + format.format(priceTotal12));
                                                        }
                                                    });
                                                }
                                            }
                                        } else {
                                            mContext.runOnUiThread(() -> detail[0].setQty(qty));
                                        }
                                    }
                                }
                            },
                            DELAY
                    );
                    mContext.removeOmzet();
                }
            }
        });
    }

    private Material getAllStock(int pos) {
        Material detail = mFilteredList.get(pos);
        Map req = new HashMap();
        req.put("id_material", detail.getId());
        req.put("uom", detail.getUom());
        Material stock = new Database(mContext).getStockMaterial(req);
        double qtySisa = stock.getQty();
        for (int i = 0; i < mFilteredList.size(); i++) {
            Material matPos = mFilteredList.get(i);
            if (i != pos && matPos.getId().equals(detail.getId())) {
                if (matPos.getId() != null && matPos.getUom() != null) {
                    Material order = new Database(mContext).getQtySmallUom(matPos);
                    qtySisa = qtySisa - order.getQty();
                }
            }

            if (Helper.isNotEmptyOrNull(matPos.getExtraItem())) {
                for (int j = 0; j < matPos.getExtraItem().size(); j++) {
                    Material matExtraPos = matPos.getExtraItem().get(j);
                    if (matExtraPos.getId().equals(detail.getId())) {
                        if (matExtraPos.getId() != null && matExtraPos.getUom() != null) {
                            Material orderExtra = new Database(mContext).getQtySmallUom(matExtraPos);
                            qtySisa = qtySisa - orderExtra.getQty();
                        }
                    }
                }
            }
        }
        stock.setQty(qtySisa);
        return stock;
    }

    public Material getAllStockExtra(Material detail, int pos) {
        Map req = new HashMap();
        req.put("id_material", detail.getId());
        req.put("uom", detail.getUom());
        Material stock = new Database(mContext).getStockMaterial(req);
        double qtySisa = stock.getQty();
        for (int i = 0; i < mFilteredList.size(); i++) {
            Material matPos = mFilteredList.get(i);
            if (matPos.getId().equals(detail.getId())) {
                if (matPos.getId() != null && matPos.getUom() != null) {
                    Material order = new Database(mContext).getQtySmallUom(matPos);
                    qtySisa = qtySisa - order.getQty();
                }
            }

            if (Helper.isNotEmptyOrNull(matPos.getExtraItem())) {
                for (int j = 0; j < matPos.getExtraItem().size(); j++) {
                    Material matExtraPos = matPos.getExtraItem().get(j);
                    if (j != pos && matExtraPos.getId().equals(detail.getId())) {
                        if (matExtraPos.getId() != null && matExtraPos.getUom() != null) {
                            Material orderExtra = new Database(mContext).getQtySmallUom(matExtraPos);
                            qtySisa = qtySisa - orderExtra.getQty();
                        }
                    }
                }
            }
        }
        stock.setQty(qtySisa);
        return stock;
    }

    private List<Material> initDataMaterial(int pos, Material detail) {
        //perlu d tany extra item itu bebas, atau sesuai sama top nya
        List<Material> listSpinner = new ArrayList<>(), listFinalStock = new ArrayList<>();
        ;
        List<Material> listMat = new ArrayList<>();
        Map req = new HashMap();
//        req.put("udf_5", outletHeader.getUdf_5());
        req.put("id_sales_group", user.getId_sales_group());//gt/on
        req.put("id_customer", outletHeader.getId());
        req.put("type_customer", outletHeader.getType_customer());
        req.put("sales_category", user.getSales_category());//rg/bt
        req.put("id_stock_request_header", stockHeader != null ? stockHeader.getIdHeader() : null);
        if (Helper.isNotEmptyOrNull(mList)) {
//            req.put("price_list_code", mList.get(0).getPriceListCode());
//            String top = user.getId_sales_group().equals("ON") ? mList.get(0).getTop_on() : mList.get(0).getTop_gt();
            req.put("top", mList.get(0).getTop());
            req.put("material_group_id", mList.get(0).getId_material_group());
        } else {
//            req.put("price_list_code", null);
            req.put("top", null);
            req.put("material_group_id", null);
        }
        if (!Helper.isCanvasSales(user)) {
            listMat.addAll(new Database(mContext).getTOMaterialExtra(req));
        } else {
            listMat.addAll(new Database(mContext).getCOMaterialExtra(req));
        }
//        listMat.addAll(new Database(mContext).getAllMasterMaterialByCustomer(req));ubah ambil materialnya

        if (Helper.isNotEmptyOrNull(mFilteredList.get(pos).getExtraItem())) {
            for (Material param : listMat) {
                int exist = 0;
                for (Material param1 : mFilteredList.get(pos).getExtraItem()) {
                    if (param.getId().equals(param1.getId())) {
                        exist++;
                    }
                }
                if (exist == 0) {
                    listSpinner.add(param);
                }
            }
        } else {
            listSpinner.addAll(listMat);
        }

        if (Helper.isCanvasSales(user)) {
            //itung stock dari order skrg
            for (Material materialStock : stockHeader.getMaterialList()) {
                double qtyOrder = 0;
                for (Material matOrder : mFilteredList) {
                    if (materialStock.getId().equals(matOrder.getId())) {
                        Material conversionOrder = new Database(mContext).getQtySmallUom(matOrder);
                        qtyOrder = qtyOrder + conversionOrder.getQty();
                    }
                    if (Helper.isNotEmptyOrNull(matOrder.getExtraItem())) {
                        for (Material matExtra : matOrder.getExtraItem()) {
                            if (materialStock.getId().equals(matExtra.getId())) {
                                Material conversionExtra = new Database(mContext).getQtySmallUom(matExtra);
                                qtyOrder = qtyOrder + conversionExtra.getQty();
                            }
                        }
                    }
                }
                Material conversionStock = new Database(mContext).getQtySmallUomSisa(materialStock);
                double qtyStock = conversionStock.getQtySisa();
                materialStock.setQtySisa(qtyStock);
                materialStock.setTotalQtyOrder(qtyOrder);
                materialStock.setUom(conversionStock.getUom());
            }

            listFinalStock = new ArrayList<>();
            for (Material materialMaster : listSpinner) {
                for (Material materialStock : stockHeader.getMaterialList()) {
                    if (materialMaster.getId().equals(materialStock.getId())) {
                        double stock = materialStock.getQtySisa() - materialStock.getTotalQtyOrder();
                        if (stock != 0) {
                            materialMaster.setQtySisa(stock);
                            materialMaster.setUomStock(materialStock.getUom());
                            listFinalStock.add(materialMaster);
                            break;
                        }
                    }
                }
            }
        } else {
            listFinalStock.addAll(listSpinner);
        }

        return listFinalStock;
    }

    private void addNew(RecyclerView rvExtra, int pos) {
        List<Material> mListExtra = mFilteredList.get(pos).getExtraItem();
        if (mListExtra == null) mListExtra = new ArrayList<>();
        Material detail = new Material("", "", "", "");
        mListExtra.add(detail);
        mAdapter = new OrderAddExtraAdapter(mContext, OrderAddAdapter.this, mListExtra, pos, header -> {
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

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
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.OrderAddActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;

public class OrderAddAdapter extends RecyclerView.Adapter<OrderAddAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private LayoutInflater mInflater;
    private OrderAddActivity mContext;
    private OnAdapterListener onAdapterListener;
    private OrderAddExtraAdapter mAdapter;
    private ProgressDialog progress;
    private LayoutInflater inflater;
    private Dialog alertDialog;
    private View dialogview;
    private boolean isExpand = false;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private ArrayAdapter<String> uomAdapter;

    public OrderAddAdapter(OrderAddActivity mContext, List<Material> mList, OnAdapterListener onAdapterListener) {
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
        LinearLayout llAddExtraItem, llDelete, llDiscount;
        LinearLayout llDiscountQty, llDiscountValue, llDiscountKelipatan;
        RecyclerView rvExtra;
        ImageView imgView;
        AutoCompleteTextView autoCompleteUom;
        TextView txtNo, txtPrice, txtTotalDiscount, txtTotal;
        TextView txtDiscountQty, txtDiscountValue, txtDiscountKelipatan;
        EditText edtProduct, edtQty;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            rvExtra = itemView.findViewById(R.id.rvExtra);
            rvExtra.setLayoutManager(new LinearLayoutManager(mContext));
            rvExtra.setHasFixedSize(true);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtDiscountKelipatan = itemView.findViewById(R.id.txtDiscountKelipatan);
            llDiscountKelipatan = itemView.findViewById(R.id.llDiscountKelipatan);
            txtDiscountValue = itemView.findViewById(R.id.txtDiscountValue);
            llDiscountValue = itemView.findViewById(R.id.llDiscountValue);
            txtDiscountQty = itemView.findViewById(R.id.txtDiscountQty);
            llDiscountQty = itemView.findViewById(R.id.llDiscountQty);
            llAddExtraItem = itemView.findViewById(R.id.llAddExtraItem);
            llDelete = itemView.findViewById(R.id.llDelete);
            txtNo = itemView.findViewById(R.id.txtNo);
            edtProduct = itemView.findViewById(R.id.edtProduct);
            edtQty = itemView.findViewById(R.id.edtQty);
            autoCompleteUom = itemView.findViewById(R.id.autoCompleteUom);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtTotalDiscount = itemView.findViewById(R.id.txtTotalDiscount);
            imgView = itemView.findViewById(R.id.imgView);
            llDiscount = itemView.findViewById(R.id.llDiscount);
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
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        Material detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        setFormatSeparator();
        setProgress();
        holder.txtNo.setText(String.valueOf(holder.getAbsoluteAdapterPosition() + 1) + ".");
        String productName = !Helper.isNullOrEmpty(detail.getNama()) ? detail.getNama() : null;
        String productId = String.valueOf(detail.getId());
        holder.edtProduct.setText(productId + " - " + productName);
        holder.edtQty.setText(Helper.setDotCurrencyAmount(detail.getQty()));

        List<String> listSpinner = new Database(mContext).getUom(detail.getId());
        if (listSpinner == null || listSpinner.size() == 0) {
            listSpinner.add("-");
        }

        mAdapter = new OrderAddExtraAdapter(mContext, mFilteredList.get(holder.getAbsoluteAdapterPosition()).getExtraItem(), holder.getAbsoluteAdapterPosition(), header -> {
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
        });
        //uom

        holder.llAddExtraItem.setOnClickListener(v -> {
            addNew(holder.rvExtra, holder.getAbsoluteAdapterPosition());
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
                holder.llDiscount.setVisibility(View.VISIBLE);
                isExpand = true;
            } else {
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(), R.drawable.ic_drop_down_aspp));
                holder.llDiscount.setVisibility(View.GONE);
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
                    detail.setQty(qty);
                } else {
                    detail.setQty(0);
                }
            }
        });
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

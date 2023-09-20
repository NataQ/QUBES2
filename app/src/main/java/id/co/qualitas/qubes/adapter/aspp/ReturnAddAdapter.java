package id.co.qualitas.qubes.adapter.aspp;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.ReturnAddActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Reason;

public class ReturnAddAdapter extends RecyclerView.Adapter<ReturnAddAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private LayoutInflater mInflater;
    private ReturnAddActivity mContext;
    private OnAdapterListener onAdapterListener;
    private ArrayAdapter<String> spn1Adapter, spn2Adapter, spn3Adapter;

    public ReturnAddAdapter(ReturnAddActivity mContext, List<Material> mList, OnAdapterListener onAdapterListener) {
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
        //        TextView spnGroupName, spnProduct, spnReason;
//        EditText edtTxtQtyStock1, edtTxtQtyStock2, edtTxtQtyStock3;
//        Spinner spinnerUom1, spinnerUom2, spinnerUom3;
//        ImageView imgDelete;
        EditText edtProduct, edtQty, edtExpDate;
        AutoCompleteTextView autoCompleteUom, autoCompleteCondition, autoCompleteReason;
        LinearLayout llDelete;
        TextView txtNo;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            edtProduct = itemView.findViewById(R.id.edtProduct);
            edtExpDate = itemView.findViewById(R.id.edtExpDate);
            edtQty = itemView.findViewById(R.id.edtQty);
            txtNo = itemView.findViewById(R.id.txtNo);
            autoCompleteUom = itemView.findViewById(R.id.autoCompleteUom);
            autoCompleteCondition = itemView.findViewById(R.id.autoCompleteCondition);
            autoCompleteReason = itemView.findViewById(R.id.autoCompleteReason);
            llDelete = itemView.findViewById(R.id.llDelete);
            this.onAdapterListener = onAdapterListener;
            itemView.setOnClickListener(this);

//            spnReason.setOnClickListener(v -> {
//                Dialog alertDialog = new Dialog(mContext);
//
//                alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                alertDialog.show();
//
//                EditText editText = alertDialog.findViewById(R.id.edit_text);
//                RecyclerView listView = alertDialog.findViewById(R.id.list_view);
//
//                List<String> groupList = new ArrayList<>();
//                groupList.add("01 - Produk Lama");
//                groupList.add("02 - Produk Rusak");
//
//                FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(mContext, groupList, (nameItem, adapterPosition) -> {
//                    spnReason.setText(nameItem);
//                    alertDialog.dismiss();
//                });
//
//                LinearLayoutManager mManager = new LinearLayoutManager(mContext);
//                listView.setLayoutManager(mManager);
//                listView.setHasFixedSize(true);
//                listView.setNestedScrollingEnabled(false);
//                listView.setAdapter(spinnerAdapter);
//
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        spinnerAdapter.getFilter().filter(s);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//            });
//
//            spnGroupName.setOnClickListener(v -> {
//                Dialog alertDialog = new Dialog(mContext);
//
//                alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                alertDialog.show();
//
//                EditText editText = alertDialog.findViewById(R.id.edit_text);
//                RecyclerView listView = alertDialog.findViewById(R.id.list_view);
//
//                List<String> groupList = new ArrayList<>();
//                groupList.add("11_KTD R");
//                groupList.add("12 - REDBULL");
//                groupList.add("13 - KTD PRO");
//                groupList.add("14 - KTD S");
//                groupList.add("31 - VIT");
//
//                FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(mContext, groupList, (nameItem, adapterPosition) -> {
//                    spnGroupName.setText(nameItem);
//                    alertDialog.dismiss();
//                });
//
//                LinearLayoutManager mManager = new LinearLayoutManager(mContext);
//                listView.setLayoutManager(mManager);
//                listView.setHasFixedSize(true);
//                listView.setNestedScrollingEnabled(false);
//                listView.setAdapter(spinnerAdapter);
//
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        spinnerAdapter.getFilter().filter(s);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//            });
//
//            spnProduct.setOnClickListener(v -> {
//                Dialog alertDialog = new Dialog(mContext);
//
//                alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                alertDialog.show();
//
//                EditText editText = alertDialog.findViewById(R.id.edit_text);
//                RecyclerView listView = alertDialog.findViewById(R.id.list_view);
//
//                List<String> groupList = new ArrayList<>();
//                groupList.add("11008_KRATINGDAENG LUAR PULAU - MT");
//                groupList.add("11007_KRATINGDAENG - MT");
//                groupList.add("11006_KRATINGDAENG - LAIN-LAIN");
//                groupList.add("11005_KRATINGDAENG LUAR PULAU");
//                groupList.add("11001_KRATINGDAENG");
//
//                FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(mContext, groupList, (nameItem, adapterPosition) -> {
//                    spnProduct.setText(nameItem);
//                    alertDialog.dismiss();
//                });
//
//                LinearLayoutManager mManager = new LinearLayoutManager(mContext);
//                listView.setLayoutManager(mManager);
//                listView.setHasFixedSize(true);
//                listView.setNestedScrollingEnabled(false);
//                listView.setAdapter(spinnerAdapter);
//
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        spinnerAdapter.getFilter().filter(s);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//            });
        }

        @Override
        public void onClick(View v) {
            onAdapterListener.onAdapterClick(mFilteredList.get(getAdapterPosition()));
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_row_view_return_add, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        Material detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());

        holder.txtNo.setText(String.valueOf(holder.getAbsoluteAdapterPosition() + 1) + ".");

        List<String> uomList = new ArrayList<>();
        uomList.add("BTL");
        uomList.add("SLOP");
        uomList.add("KRT");

        List<String> conditionList = new ArrayList<>();
        conditionList.add("Good");
        conditionList.add("Bad");

        List<Reason> reasonList = new ArrayList<>();
        reasonList.addAll(Helper.getDataReason());

        String productName = !Helper.isNullOrEmpty(detail.getMaterialCode()) ? detail.getMaterialCode() : null;
        String productId = !Helper.isNullOrEmpty(detail.getIdMaterial()) ? detail.getIdMaterial() : null;
        holder.edtProduct.setText(productId + " - " + productName);

        holder.edtExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.hideKeyboard();
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int date = calendar.get(Calendar.DATE);

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DATE, dayOfMonth);
                        String expDate = new SimpleDateFormat(Constants.DATE_FORMAT_1).format(calendar.getTime());
                        holder.edtExpDate.setText(expDate);
                        holder.edtExpDate.setError(null);
//                        materialArrayListFiltered.get(getAdapterPosition()).setExpDate(expDateBE);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(mContext, dateSetListener, year, month, date);
                dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dialog.show();
            }
        });

//        holder.edtProduct.setOnClickListener(v -> {
//            Dialog alertDialog = new Dialog(mContext);
//
//            alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
//            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            alertDialog.show();
//
//            EditText editText = alertDialog.findViewById(R.id.edit_text);
//            RecyclerView listView = alertDialog.findViewById(R.id.list_view);
//
//            List<String> groupList = new ArrayList<>();
//            groupList.add("11008_KRATINGDAENG LUAR PULAU - MT");
//            groupList.add("11007_KRATINGDAENG - MT");
//            groupList.add("11006_KRATINGDAENG - LAIN-LAIN");
//            groupList.add("11005_KRATINGDAENG LUAR PULAU");
//            groupList.add("11001_KRATINGDAENG");
//
//            FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(mContext, groupList, (nameItem, adapterPosition) -> {
//                holder.edtProduct.setText(nameItem);
//                alertDialog.dismiss();
//            });
//
//            LinearLayoutManager mManager = new LinearLayoutManager(mContext);
//            listView.setLayoutManager(mManager);
//            listView.setHasFixedSize(true);
//            listView.setNestedScrollingEnabled(false);
//            listView.setAdapter(spinnerAdapter);
//
//            editText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    spinnerAdapter.getFilter().filter(s);
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//        });

        mContext.setAutoCompleteAdapter(uomList, holder.autoCompleteUom);
        mContext.setAutoCompleteAdapter(conditionList, holder.autoCompleteCondition);
        mContext.setAutoCompleteAdapterReason(reasonList, holder.autoCompleteReason);

        holder.llDelete.setOnClickListener(v -> {
            mContext.delete(detail, holder.getAbsoluteAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Material Material);
    }
}

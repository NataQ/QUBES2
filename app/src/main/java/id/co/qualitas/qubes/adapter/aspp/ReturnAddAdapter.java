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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.ReturnAddActivity;
import id.co.qualitas.qubes.model.Material;

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
        TextView spnGroupName, spnProduct, spnReason;
        EditText edtTxtQtyStock1, edtTxtQtyStock2, edtTxtQtyStock3;
        Spinner spinnerUom1, spinnerUom2, spinnerUom3;
        ImageView imgDelete;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            spnGroupName = itemView.findViewById(R.id.spnGroupName);
            spnProduct = itemView.findViewById(R.id.spnProduct);
            edtTxtQtyStock1 = itemView.findViewById(R.id.edtTxtQtyStock1);
            edtTxtQtyStock2 = itemView.findViewById(R.id.edtTxtQtyStock2);
            edtTxtQtyStock3 = itemView.findViewById(R.id.edtTxtQtyStock3);
            spinnerUom1 = itemView.findViewById(R.id.spinnerUom1);
            spinnerUom2 = itemView.findViewById(R.id.spinnerUom2);
            spinnerUom3 = itemView.findViewById(R.id.spinnerUom3);
            spnReason = itemView.findViewById(R.id.spnReason);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            this.onAdapterListener = onAdapterListener;
            itemView.setOnClickListener(this);

            spnReason.setOnClickListener(v -> {
                Dialog alertDialog = new Dialog(mContext);

                alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                EditText editText = alertDialog.findViewById(R.id.edit_text);
                RecyclerView listView = alertDialog.findViewById(R.id.list_view);

                List<String> groupList = new ArrayList<>();
                groupList.add("01 - Produk Lama");
                groupList.add("02 - Produk Rusak");

                FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(mContext, groupList, (nameItem, adapterPosition) -> {
                    spnReason.setText(nameItem);
                    alertDialog.dismiss();
                });

                LinearLayoutManager mManager = new LinearLayoutManager(mContext);
                listView.setLayoutManager(mManager);
                listView.setHasFixedSize(true);
                listView.setNestedScrollingEnabled(false);
                listView.setAdapter(spinnerAdapter);

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
            });

            spnGroupName.setOnClickListener(v -> {
                Dialog alertDialog = new Dialog(mContext);

                alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                EditText editText = alertDialog.findViewById(R.id.edit_text);
                RecyclerView listView = alertDialog.findViewById(R.id.list_view);

                List<String> groupList = new ArrayList<>();
                groupList.add("11 - KTD R");
                groupList.add("12 - REDBULL");
                groupList.add("13 - KTD PRO");
                groupList.add("14 - KTD S");
                groupList.add("31 - VIT");

                FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(mContext, groupList, (nameItem, adapterPosition) -> {
                    spnGroupName.setText(nameItem);
                    alertDialog.dismiss();
                });

                LinearLayoutManager mManager = new LinearLayoutManager(mContext);
                listView.setLayoutManager(mManager);
                listView.setHasFixedSize(true);
                listView.setNestedScrollingEnabled(false);
                listView.setAdapter(spinnerAdapter);

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
            });

            spnProduct.setOnClickListener(v -> {
                Dialog alertDialog = new Dialog(mContext);

                alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                EditText editText = alertDialog.findViewById(R.id.edit_text);
                RecyclerView listView = alertDialog.findViewById(R.id.list_view);

                List<String> groupList = new ArrayList<>();
                groupList.add("11008 - KRATINGDAENG LUAR PULAU - MT");
                groupList.add("11007 - KRATINGDAENG - MT");
                groupList.add("11006 - KRATINGDAENG - LAIN-LAIN");
                groupList.add("11005 - KRATINGDAENG LUAR PULAU");
                groupList.add("11001 - KRATINGDAENG");

                FilteredSpinnerAdapter spinnerAdapter = new FilteredSpinnerAdapter(mContext, groupList, (nameItem, adapterPosition) -> {
                    spnProduct.setText(nameItem);
                    alertDialog.dismiss();
                });

                LinearLayoutManager mManager = new LinearLayoutManager(mContext);
                listView.setLayoutManager(mManager);
                listView.setHasFixedSize(true);
                listView.setNestedScrollingEnabled(false);
                listView.setAdapter(spinnerAdapter);

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
            });
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
    public void onBindViewHolder(Holder holder, int position) {
        Material detail = mFilteredList.get(position);

        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("BTL");
        listSpinner.add("SLOP");
        listSpinner.add("KRT");

        spn1Adapter = new ArrayAdapter<>(mContext.getApplicationContext(), R.layout.spinner_item, listSpinner);
        spn1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerUom1.setAdapter(spn1Adapter);

        spn2Adapter = new ArrayAdapter<>(mContext.getApplicationContext(), R.layout.spinner_item, listSpinner);
        spn2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerUom2.setAdapter(spn2Adapter);

        spn3Adapter = new ArrayAdapter<>(mContext.getApplicationContext(), R.layout.spinner_item, listSpinner);
        spn3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerUom3.setAdapter(spn3Adapter);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Material Material);
    }
}

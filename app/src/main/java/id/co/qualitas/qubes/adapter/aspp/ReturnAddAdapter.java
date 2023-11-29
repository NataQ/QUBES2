package id.co.qualitas.qubes.adapter.aspp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.ReturnAddActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.DropDown;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.utils.Utils;

public class ReturnAddAdapter extends RecyclerView.Adapter<ReturnAddAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private LayoutInflater mInflater;
    private ReturnAddActivity mContext;
    private OnAdapterListener onAdapterListener;
    private ArrayAdapter<String> uomAdapter;//conditionAdapter;
    private ArrayAdapter<String> reasonAdapter;
    private SpinnerAllDropDownAdapter conditionAdapter;
    private Reason reasonDetail;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private Dialog alertDialog;
    private View dialogview;
    private LayoutInflater inflater;

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
        EditText edtProduct, edtQty, edtExpDate, edtDescReason;
        AutoCompleteTextView autoCompleteUom, autoCompleteCondition, autoCompleteReason;
        LinearLayout llDelete, llReasonDesc;
        RelativeLayout llPhoto;
        ImageView imgAdd, img;
        TextView txtNo;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            imgAdd = itemView.findViewById(R.id.imgAdd);
            img = itemView.findViewById(R.id.img);
            llPhoto = itemView.findViewById(R.id.llPhoto);
            edtProduct = itemView.findViewById(R.id.edtProduct);
            edtDescReason = itemView.findViewById(R.id.edtDescReason);
            edtExpDate = itemView.findViewById(R.id.edtExpDate);
            edtQty = itemView.findViewById(R.id.edtQty);
            txtNo = itemView.findViewById(R.id.txtNo);
            autoCompleteUom = itemView.findViewById(R.id.autoCompleteUom);
            autoCompleteCondition = itemView.findViewById(R.id.autoCompleteCondition);
            autoCompleteReason = itemView.findViewById(R.id.autoCompleteReason);
            llDelete = itemView.findViewById(R.id.llDelete);
            llReasonDesc = itemView.findViewById(R.id.llReasonDesc);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_return_add, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        setFormatSeparator();
        Material detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());

        holder.txtNo.setText(format.format(holder.getAbsoluteAdapterPosition() + 1) + ".");

        if (detail.getPhotoReason() != null) {
            Utils.loadImageFit(mContext, detail.getPhotoReason(), holder.img);
//            try {
//                mContext.getContentResolver().takePersistableUriPermission(Uri.parse(detail.getPhotoReason()), (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
//                holder.img.setImageURI(Uri.parse(detail.getPhotoReason()));
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            }
            holder.img.setVisibility(View.VISIBLE);
            holder.imgAdd.setVisibility(View.GONE);

        } else {
            holder.img.setVisibility(View.GONE);
            holder.imgAdd.setVisibility(View.VISIBLE);
        }

        List<String> listSpinner = new Database(mContext).getUom(detail.getId());
        if (listSpinner == null || listSpinner.size() == 0) {
            listSpinner.add("-");
        }

        List<DropDown> conditionList = new Database(mContext).getDropDown(Constants.DROP_DOWN_CONDITION_RETURN);
//        List<String> conditionList = new ArrayList<>();
//        conditionList.add("Good");
//        conditionList.add("Bad");

        List<String> reasonList = new ArrayList<>();
        reasonList.addAll(new Database(mContext).getAllStringReason(Constants.REASON_TYPE_RETURN));

        String productName = !Helper.isNullOrEmpty(detail.getNama()) ? detail.getNama() : null;
        String productId = String.valueOf(detail.getId());
        holder.edtProduct.setText(productId + " - " + productName);
        holder.edtQty.setText(Helper.setDotCurrencyAmount(detail.getQty()));
        holder.edtDescReason.setText(Helper.isEmpty(detail.getDescReason(), ""));

        if (!Helper.isNullOrEmpty(detail.getExpiredDate())) {
            String expDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_1, detail.getExpiredDate());
            holder.edtExpDate.setText(expDate);
        } else {
            holder.edtExpDate.setText(null);
        }
        if (!Helper.isNullOrEmpty(detail.getNameReason())) {
            reasonDetail = new Database(mContext).getDetailReason(Constants.REASON_TYPE_RETURN, detail.getNameReason());
            if (reasonDetail.getIs_freetext() == 1) {
                holder.llReasonDesc.setVisibility(View.VISIBLE);
            } else {
                holder.llReasonDesc.setVisibility(View.GONE);
            }

            if (reasonDetail.getIs_photo() == 1) {
                holder.llPhoto.setVisibility(View.VISIBLE);
            } else {
                holder.llPhoto.setVisibility(View.GONE);
            }
        } else {
            holder.llReasonDesc.setVisibility(View.GONE);
            holder.llPhoto.setVisibility(View.GONE);
        }

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
                        String expDate2 = new SimpleDateFormat(Constants.DATE_FORMAT_3).format(calendar.getTime());
                        detail.setExpiredDate(expDate2);
                        holder.edtExpDate.setText(expDate);
                        holder.edtExpDate.setError(null);
//                        materialArrayListFiltered.get(getAdapterPosition()).setExpDate(expDateBE);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(mContext, dateSetListener, year, month, date);
//                dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dialog.show();
            }
        });
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

        conditionAdapter = new SpinnerAllDropDownAdapter(mContext, conditionList);
        holder.autoCompleteCondition.setAdapter(conditionAdapter);
        holder.autoCompleteCondition.setText(detail.getCondition(), false);

//        conditionAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item) {
//            @Override
//            public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text = view.findViewById(R.id.text1);
//                return view;
//            }
//        };
//        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        conditionAdapter.addAll(conditionList);
//        holder.autoCompleteCondition.setAdapter(conditionAdapter);

        holder.autoCompleteCondition.setOnItemClickListener((adapterView, view, i, l) -> {
            String id = conditionList.get(i).getId();
            String selected = conditionList.get(i).toString();
            detail.setCondition(id);
            detail.setCondition_pos(i);
        });

        reasonAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(R.id.text1);
                return view;
            }
        };
        reasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonAdapter.addAll(reasonList);
        holder.autoCompleteReason.setAdapter(reasonAdapter);
        holder.autoCompleteReason.setText(detail.getNameReason(), false);

        holder.autoCompleteUom.setOnItemClickListener((adapterView, view, i, l) -> {
            String selected = listSpinner.get(i).toString();
            detail.setUom(selected);
        });

        holder.autoCompleteReason.setOnItemClickListener((adapterView, view, i, l) -> {
            String selected = reasonList.get(i).toString();
            detail.setNameReason(selected);
            reasonDetail = new Database(mContext).getDetailReason(Constants.REASON_TYPE_RETURN, selected);
            detail.setIdReason(String.valueOf(reasonDetail.getId()));
            detail.setDescReason(null);
            detail.setPhotoReason(null);

            if (reasonDetail.getIs_freetext() == 1) {
                holder.llReasonDesc.setVisibility(View.VISIBLE);
            } else {
                holder.llReasonDesc.setVisibility(View.GONE);
            }

            if (reasonDetail.getIs_photo() == 1) {
                holder.llPhoto.setVisibility(View.VISIBLE);
                Utils.loadImageFit(mContext, detail.getPhotoReason(), holder.img);
                holder.img.setVisibility(View.GONE);
                holder.imgAdd.setVisibility(View.VISIBLE);
            } else {
                holder.llPhoto.setVisibility(View.GONE);
                holder.img.setVisibility(View.VISIBLE);
                holder.imgAdd.setVisibility(View.GONE);
            }
        });

        holder.edtDescReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    detail.setDescReason(s.toString());
                }
            }
        });

        holder.llPhoto.setOnClickListener(v -> {
            mContext.openDialogPhoto(detail, holder.getAbsoluteAdapterPosition());
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
                    double qty = Double.parseDouble(s.toString().replace(",", ""));
                    detail.setQty(qty);
                }
//                else {
//                    detail.setQty(0);
//                }
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
}

package id.co.qualitas.qubes.adapter.aspp;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.VisitActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class ReasonNotVisitAdapter extends RecyclerView.Adapter<ReasonNotVisitAdapter.Holder> implements Filterable {
    private List<VisitSalesman> mList;
    private List<VisitSalesman> mFilteredList;
    private LayoutInflater mInflater;
    private VisitActivity mContext;
    private OnAdapterListener onAdapterListener;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

    public ReasonNotVisitAdapter(VisitActivity mContext, List<VisitSalesman> mList, OnAdapterListener onAdapterListener) {
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

    public void setData(List<VisitSalesman> mDataSet) {
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
                    List<VisitSalesman> filteredList = new ArrayList<>();
                    for (VisitSalesman row : mList) {

                        /*filter by name*/
                        if (row.getCustomerId().toLowerCase().contains(charString.toLowerCase())) {
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
                mFilteredList = (ArrayList<VisitSalesman>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtOutlet, txtAddress, txtNo;
        ImageView imgDelete, imgAdd, img;
        EditText edtTxtOther;
        RelativeLayout llPhoto;
        Spinner spnReason;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtNo = itemView.findViewById(R.id.txtNo);
            llPhoto = itemView.findViewById(R.id.llPhoto);
            txtOutlet = itemView.findViewById(R.id.txtOutlet);
            edtTxtOther = itemView.findViewById(R.id.edtTxtOther);
            spnReason = itemView.findViewById(R.id.spnReason);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            img = itemView.findViewById(R.id.img);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgAdd = itemView.findViewById(R.id.imgAdd);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_reason_not_visit, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        setFormatSeparator();
        VisitSalesman detail = mFilteredList.get(position);
        holder.txtNo.setText(format.format(holder.getAbsoluteAdapterPosition() + 1) + ".");
        holder.txtAddress.setText(Helper.isEmpty(detail.getAddress(), ""));
        holder.txtOutlet.setText(Helper.isEmpty(detail.getCustomerId(), "") + " - " + Helper.isEmpty(detail.getCustomerName(), ""));
        holder.edtTxtOther.setText(Helper.isEmpty(detail.getDescNotVisitReason(), ""));

        List<Reason> reasonList = new ArrayList<>();
        reasonList.addAll(new Database(mContext).getAllReason(Constants.REASON_TYPE_NOT_VISIT));
        Utils.loadImageFit(mContext, detail.getPhotoNotVisitReason(), holder.img);

        if (detail.getPhotoNotVisitReason() != null) {
            holder.imgAdd.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.VISIBLE);
        } else {
            holder.imgAdd.setVisibility(View.VISIBLE);
            holder.imgDelete.setVisibility(View.GONE);
            holder.img.setVisibility(View.GONE);
        }

        holder.imgDelete.setOnClickListener(v -> {
            detail.setPhotoNotVisitReason(null);
            notifyItemChanged(holder.getAbsoluteAdapterPosition());
        });

        FilteredSpinnerAllReasonAdapter spinnerAdapter = new FilteredSpinnerAllReasonAdapter(mContext, reasonList);
        holder.spnReason.setAdapter(spinnerAdapter);
        holder.spnReason.setSelection(detail.getPosReason());

        holder.spnReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Reason temp = reasonList.get(i);
                detail.setIdNotVisitReason(String.valueOf(temp.getId()));
                detail.setNameNotVisitReason(temp.getDescription());

                if (temp.getIs_photo() == 1) {
                    holder.llPhoto.setVisibility(View.VISIBLE);
                } else {
                    holder.llPhoto.setVisibility(View.GONE);
                }

                if (temp.getIs_freetext() == 1) {
                    holder.edtTxtOther.setVisibility(View.VISIBLE);
                } else {
                    holder.edtTxtOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        holder.edtTxtOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    detail.setDescNotVisitReason(s.toString());
                }
            }
        });

        holder.llPhoto.setOnClickListener(v -> {
            SessionManagerQubes.setVisitSalesmanReason(mList);
            mContext.openCamera(String.valueOf(holder.getAbsoluteAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(VisitSalesman VisitSalesman);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}

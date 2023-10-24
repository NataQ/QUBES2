package id.co.qualitas.qubes.adapter.aspp;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.ReturnDetailActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.utils.Utils;

public class ReturnDetailAdapter extends RecyclerView.Adapter<ReturnDetailAdapter.Holder> implements Filterable {
    private List<Material> mList;
    private List<Material> mFilteredList;
    private LayoutInflater mInflater;
    private ReturnDetailActivity mContext;
    private OnAdapterListener onAdapterListener;
    private Reason reasonDetail;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;

    public ReturnDetailAdapter(ReturnDetailActivity mContext, List<Material> mList, OnAdapterListener onAdapterListener) {
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
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase())) {
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
        EditText edtProduct, edtQty, edtUom, edtExpDate, edtCondition, edtReason, edtDescReason;
        RelativeLayout llPhoto;
        ImageView img;
        TextView txtNo;
        TextInputLayout llReasonDesc;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            llReasonDesc = itemView.findViewById(R.id.llReasonDesc);
            txtNo = itemView.findViewById(R.id.txtNo);
            edtProduct = itemView.findViewById(R.id.edtProduct);
            edtQty = itemView.findViewById(R.id.edtQty);
            edtReason = itemView.findViewById(R.id.edtReason);
            edtCondition = itemView.findViewById(R.id.edtCondition);
            edtUom = itemView.findViewById(R.id.edtUom);
            edtExpDate = itemView.findViewById(R.id.edtExpDate);
            edtDescReason = itemView.findViewById(R.id.edtDescReason);
            llPhoto = itemView.findViewById(R.id.llPhoto);
            img = itemView.findViewById(R.id.img);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_return_detail, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        setFormatSeparator();
        Material detail = mFilteredList.get(position);
        holder.txtNo.setText(String.valueOf(holder.getAbsoluteAdapterPosition() + 1) + ".");
        String productName = !Helper.isNullOrEmpty(detail.getNama()) ? detail.getNama() : null;
        String productId = String.valueOf(detail.getId());
        holder.edtProduct.setText(productId + " - " + productName);
        holder.edtQty.setText(format.format(detail.getQty()));
        holder.edtUom.setText(Helper.isEmpty(detail.getUom(), ""));
        holder.edtCondition.setText(Helper.isEmpty(detail.getCondition(), ""));
        holder.edtReason.setText(Helper.isEmpty(detail.getNameReason(), ""));
        Utils.loadImageFit(mContext, detail.getPhotoReason(), holder.img);

        if (!Helper.isNullOrEmpty(detail.getExpiredDate())) {
            String expDate = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_1, detail.getExpiredDate());
            holder.edtExpDate.setText(expDate);
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

        holder.llPhoto.setOnClickListener(v -> {
            mContext.openDialogPhoto(detail, holder.getAbsoluteAdapterPosition());
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


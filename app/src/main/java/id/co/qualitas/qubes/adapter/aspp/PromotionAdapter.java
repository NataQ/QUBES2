package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.colormoon.readmoretextview.ReadMoreTextView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.aspp.PromotionFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.Promotion;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.Holder> implements Filterable {
    private List<Promotion> promotionList;
    private List<Promotion> promotionFilteredList;
    private LayoutInflater mInflater;
    private PromotionFragment mContext;
    private OnAdapterListener onAdapterListener;

    public PromotionAdapter(PromotionFragment mContext, List<Promotion> promotionList, OnAdapterListener onAdapterListener) {
        if (promotionList != null) {
            this.promotionList = promotionList;
            this.promotionFilteredList = promotionList;
        } else {
            this.promotionList = new ArrayList<>();
            this.promotionFilteredList = new ArrayList<>();
        }
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext.getContext());
        this.onAdapterListener = onAdapterListener;
    }

    public void setData(List<Promotion> mDataSet) {
        this.promotionList = mDataSet;
        this.promotionFilteredList = mDataSet;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    promotionFilteredList = promotionList;
                } else {
                    List<Promotion> filteredList = new ArrayList<>();
                    for (Promotion row : promotionList) {

                        /*filter by name*/
                        if (row.getNama_promo().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    promotionFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = promotionFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                promotionFilteredList = (ArrayList<Promotion>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtPeriode, txtTitle, txtDesc, txtNoPromo;
        OnAdapterListener onAdapterListener;
//        ReadMoreTextView txtDesc;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtNoPromo = itemView.findViewById(R.id.txtNoPromo);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtPeriode = itemView.findViewById(R.id.txtPeriode);
            this.onAdapterListener = onAdapterListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAdapterListener.onAdapterClick(promotionFilteredList.get(getAdapterPosition()));
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_row_view_promotion, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Promotion detail = promotionFilteredList.get(position);
        String validFrom = null, validTo = null;
        if (!Helper.isNullOrEmpty(detail.getValid_from())) {
            validFrom = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, detail.getValid_from());
        }

        if (!Helper.isNullOrEmpty(detail.getValid_to())) {
            validTo = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, detail.getValid_to());
        }
        String noPromo = Helper.isEmpty(detail.getNo_promo(), "");
        String namaPromo = Helper.isEmpty(detail.getNama_promo(), "");
        String descPromo = Helper.isEmpty(detail.getKeterangan(), "");

        holder.txtTitle.setText(namaPromo);
        holder.txtDesc.setText(descPromo);
        holder.txtNoPromo.setText(noPromo);
//        holder.txtDesc.setCollapsedText("Read More");
//        holder.txtDesc.setExpandedText("Read Less");
//        holder.txtDesc.setCollapsedTextColor(R.color.blue6_aspp);
//        holder.txtDesc.setExpandedTextColor(R.color.blue6_aspp);
        holder.txtPeriode.setText(validFrom + " - " + validTo);

    }

    @Override
    public int getItemCount() {
        return promotionFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Promotion data);
    }
}

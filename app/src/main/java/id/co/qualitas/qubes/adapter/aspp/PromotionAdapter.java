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

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.aspp.PromotionFragment;
import id.co.qualitas.qubes.model.Promotion;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.Holder> implements Filterable {
    private List<Promotion> promotionList;
    private List<Promotion> promotionFilteredList;
    private LayoutInflater mInflater;
//    private OnClickListener onClickListener;
    private PromotionFragment mContext;

//    public void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    public PromotionAdapter(PromotionFragment mContext, List<Promotion> promotionList) {
        if (promotionList != null) {
            this.promotionList = promotionList;
            this.promotionFilteredList = promotionList;
        } else {
            this.promotionList = new ArrayList<>();
            this.promotionFilteredList = new ArrayList<>();
        }
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext.getContext());
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
        TextView txtPeriode, txtTitle;

        public Holder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPeriode = itemView.findViewById(R.id.txtPeriode);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            onClickListener.onClick(getAdapterPosition());
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_row_view_promotion, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Promotion promotion = promotionFilteredList.get(position);
        holder.txtTitle.setText(promotion.getNama_promo());
        holder.txtPeriode.setText(promotion.getValid_from() + " - " + promotion.getValid_to());
    }

    @Override
    public int getItemCount() {
        return promotionFilteredList.size();
    }

    public interface OnClickListener {
        void onClick(int pos);
    }
}

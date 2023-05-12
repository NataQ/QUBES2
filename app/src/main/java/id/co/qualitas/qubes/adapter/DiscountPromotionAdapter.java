package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.PromotionFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Discount;

public class DiscountPromotionAdapter extends RecyclerView.Adapter<DiscountPromotionAdapter.MyViewHolder> {
    //code, name, contact
    private ArrayList<Discount> discountArrayList;
    private static LayoutInflater inflater = null;
    private PromotionFragment context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView discName, discValue, salesDocTY, scale, minPScale, pmntTerms, perUom, validFromTo;
        public ImageView icStatus;

        public MyViewHolder(View view) {
            super(view);
            discName = (TextView) view.findViewById(R.id.discName);
            discValue = (TextView) view.findViewById(R.id.discValue);
            salesDocTY = (TextView) view.findViewById(R.id.salesDocTY);
//            scale = (TextView) view.findViewById(R.id.scale);
            minPScale = (TextView) view.findViewById(R.id.minPScale);
            pmntTerms = (TextView) view.findViewById(R.id.pmntTerms);
            perUom = (TextView) view.findViewById(R.id.perUom);
            validFromTo = (TextView) view.findViewById(R.id.validFromTo);
            icStatus = (ImageView) view.findViewById(R.id.icStatus);
        }
    }


    public DiscountPromotionAdapter(ArrayList<Discount> discountArrayList, PromotionFragment mcontext) {
        this.discountArrayList = discountArrayList;
        this.context = mcontext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_discount_promotion, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Discount discount = discountArrayList.get(position);
        holder.discName.setText(Helper.validateResponseEmpty(discount.getDiscountName()));
        holder.discValue.setText(Helper.validateResponseEmpty(discount.getValue()));
        holder.salesDocTY.setText(Helper.validateResponseEmpty(discount.getSalesDocTy()));
        holder.scale.setText(Helper.validateResponseEmpty(discount.getScale()));
        holder.minPScale.setText(Helper.validateResponseEmpty(discount.getMinPScale()));
        holder.pmntTerms.setText(Helper.validateResponseEmpty(discount.getPmnsTerms()));
        holder.perUom.setText(Helper.validateResponseEmpty(discount.getPerUom()));
        holder.validFromTo.setText(Helper.validateResponseEmpty(discount.getValidFromTo()));
        if (discount.getStatus() != null) {
            holder.icStatus.setImageResource(R.drawable.ic_timedisc);
        }
    }

    @Override
    public int getItemCount() {
        return discountArrayList.size();
    }

}
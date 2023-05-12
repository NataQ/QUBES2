package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.PromotionFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.PromotionFreeGoods;

public class PromotionFreeGoodsAdapter extends RecyclerView.Adapter<PromotionFreeGoodsAdapter.MyViewHolder> {
    //code, name, contact
    private PromotionFragment context;


    private ArrayList<PromotionFreeGoods> discountList = new ArrayList<>();
    private ArrayList<PromotionFreeGoods> searchFgArrayList = new ArrayList<>();
    private DiscountPromotionAdapter mAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView docNo, kcNo, validFrom, validTo, condition, bonus, salesDocType, type, customer;
        LinearLayout linSalesDocTy, linMinPScale, linPmntTerms, linPerUom, linValidFrTo, linSalesOrg, linDistCh, linPlant, linJenisAcc, linSegmen, linMatGroup;

        public MyViewHolder(View view) {
            super(view);
            docNo = view.findViewById(R.id.docNo);
            kcNo = view.findViewById(R.id.kcNo);
            validFrom = view.findViewById(R.id.validFrom);
            validTo = view.findViewById(R.id.validTo);
            condition = view.findViewById(R.id.condition);
            bonus = view.findViewById(R.id.bonus);
            salesDocType = view.findViewById(R.id.salesDocTy);
            type = view.findViewById(R.id.type);
            customer = view.findViewById(R.id.customer);
        }
    }


    public PromotionFreeGoodsAdapter(ArrayList<PromotionFreeGoods> discountList, PromotionFragment mcontext) {
        this.discountList = discountList;
        this.context = mcontext;

        updateData(discountList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_promotion_free_goods, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        PromotionFreeGoods discount = searchFgArrayList.get(position);
        holder.customer.setText(Helper.validateResponseEmpty(discount.getCustomer()));
        holder.docNo.setText(Helper.validateResponseEmpty(discount.getDoc_no()));

        holder.kcNo.setText(Helper.validateResponseEmpty(discount.getKc_no()));

        holder.validFrom.setText(Helper.validateResponseEmpty(discount.getValidFrom()));


        holder.validTo.setText(Helper.validateResponseEmpty(discount.getValidTo()));

        holder.condition.setText(Helper.validateResponseEmpty(discount.getCondition()));

        holder.bonus.setText(Helper.validateResponseEmpty(discount.getBonus()));

        holder.salesDocType.setText(Helper.validateResponseEmpty(String.valueOf(discount.getSalesDocumentType())));
        holder.type.setText(Helper.validateResponseEmpty(discount.getType()));
    }

    @Override
    public int getItemCount() {
        return searchFgArrayList.size();
    }

    public void clearData(){
        if(this.discountList!=null) {
            this.discountList.clear();
        }

        searchFgArrayList.clear();
        searchFgArrayList.addAll(discountList);
    }

    public void updateData(ArrayList<PromotionFreeGoods> discountList){
        this.discountList = discountList;

        searchFgArrayList.clear();
        searchFgArrayList.addAll(discountList);
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchFgArrayList.clear();
        if (charText.length() == 0) {
            searchFgArrayList.addAll(discountList);
        } else {
            for (PromotionFreeGoods dc : discountList) {
                if (Helper.validateResponseEmpty(dc.getCustomer()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getDoc_no()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getKc_no()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getValidFrom()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getValidTo()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getCondition()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getBonus()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getSalesDocumentType()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getType()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getSalesDocumentType()).toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchFgArrayList.add(dc);
                }

            }

        }
        notifyDataSetChanged();
    }

}
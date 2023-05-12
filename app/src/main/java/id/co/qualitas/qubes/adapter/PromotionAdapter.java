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
import id.co.qualitas.qubes.model.Discount;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.MyViewHolder> {
    //code, name, contact
    private PromotionFragment context;

    private ArrayList<Discount> discountList = new ArrayList<>();
    private ArrayList<Discount> searchArrayList = new ArrayList<>();
    private DiscountPromotionAdapter mAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView discName, prodName, discValue, salesDocTY, scale, minPScale, pmntTerms, perUom, validFromTo, prodId, outletName,
                salesOrganization, distributionChannel, plant, jenisAccount, segmen, materialGroup, noKc, klasifikasi;
        TextView icStatus;
        LinearLayout linSalesDocTy, linMinPScale, linPmntTerms, linPerUom, linValidFrTo, linSalesOrg, linDistCh, linPlant, linJenisAcc, linSegmen, linMatGroup;

        public MyViewHolder(View view) {
            super(view);
            discName = view.findViewById(R.id.discName);
            prodName = view.findViewById(R.id.productName);
            discValue = view.findViewById(R.id.discValue);
            salesDocTY = view.findViewById(R.id.salesDocTY);
            minPScale = view.findViewById(R.id.minPScale);
            pmntTerms = view.findViewById(R.id.pmntTerms);
            perUom = view.findViewById(R.id.perUom);
            validFromTo = view.findViewById(R.id.validFromTo);
            icStatus = view.findViewById(R.id.icStatus);
            prodId = view.findViewById(R.id.productId);
            outletName = view.findViewById(R.id.outletName);
            salesOrganization = view.findViewById(R.id.salesOrganization);
            distributionChannel = view.findViewById(R.id.distChannel);
            jenisAccount = view.findViewById(R.id.jenisAccount);
            segmen = view.findViewById(R.id.segmen);
            materialGroup = view.findViewById(R.id.materialGroup);
            linSalesDocTy = view.findViewById(R.id.linSalesDocTy);
            linPmntTerms = view.findViewById(R.id.linPmntTerms);
            linPerUom = view.findViewById(R.id.linPerUom);
            linValidFrTo = view.findViewById(R.id.linValidFromTo);
            linPlant = view.findViewById(R.id.linPlant);
            linMatGroup = view.findViewById(R.id.linMaterialGroup);
            noKc = view.findViewById(R.id.noKc);
            klasifikasi = view.findViewById(R.id.klasifikasi);
        }
    }


    public PromotionAdapter(ArrayList<Discount> discountList, PromotionFragment mcontext) {
        this.discountList = discountList;
        this.context = mcontext;

        updateData(discountList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_promotions, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Discount discount = searchArrayList.get(position);
        if (discount.getName() != null && discount.getCond_type() != null) {
            holder.discName.setText(Helper.validateResponseEmpty(discount.getName()).concat("(").concat(Helper.validateResponseEmpty(discount.getCond_type())).concat(")"));
        }
        holder.outletName.setText(Helper.validateResponseEmpty(discount.getIdCustomer()).concat(" - ").concat(Helper.validateResponseEmpty(discount.getCustomer())));

        holder.prodName.setText(Helper.validateResponseEmpty(discount.getMaterial()));

        holder.prodId.setText(Helper.validateResponseEmpty(discount.getIdMaterial()));


        holder.discValue.setText(Helper.validateResponseEmpty(discount.getPrice()));

        holder.salesDocTY.setText(Helper.validateResponseEmpty(discount.getSalesDocumentType()));

        holder.minPScale.setText(Helper.validateResponseEmpty(discount.getPers()));

        holder.pmntTerms.setText(Helper.validateResponseEmpty(String.valueOf(discount.getPaymentTerms())).concat(" hari"));
        holder.perUom.setText(Helper.validateResponseEmpty(discount.getUom()));

        holder.validFromTo.setText(Helper.validateResponseEmpty(discount.getValid_from()).concat(" - ").concat(Helper.validateResponseEmpty(discount.getValid_to())));

        if (discount.getCount() != null && !discount.getCount().equals("Status")) {
            holder.icStatus.setText("1 Time Disc");
        }

        holder.klasifikasi.setText(Helper.validateResponseEmpty(discount.getIdMaterialGroup3()).concat(" - ").concat(Helper.validateResponseEmpty(discount.getMaterialGroup3())));
        holder.noKc.setText(Helper.validateResponseEmpty(discount.getKcNumber()));
    }

    @Override
    public int getItemCount() {
        return searchArrayList.size();
    }

    //new
    public void clearData() {
        if (this.discountList != null) {
            this.discountList.clear();
        }

        searchArrayList.clear();
        searchArrayList.addAll(discountList);
    }

    public void updateData(ArrayList<Discount> discountList) {
        this.discountList = discountList;

        searchArrayList.clear();
        searchArrayList.addAll(discountList);
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchArrayList.clear();
        if (charText.length() == 0) {
            searchArrayList.addAll(discountList);
        } else {
            for (Discount dc : discountList) {
                if (Helper.validateResponseEmpty(dc.getName()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getCond_type()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getIdCustomer()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getCustomer()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getDocNumber()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getIdMaterial()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getId_scale()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getKcNumber()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getMaterial()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getSalesDocumentType()).toLowerCase(Locale.getDefault()).contains(charText)
                        || Helper.validateResponseEmpty(dc.getIdPlant()).toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchArrayList.add(dc);
                }

            }

        }
        notifyDataSetChanged();
    }
}
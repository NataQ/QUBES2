package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.OrderSummary2Fragment;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.FreeGoods;
import id.co.qualitas.qubes.model.OptionFreeGoods;

public class FreeGoodsNAdapter extends RecyclerView.Adapter<FreeGoodsNAdapter.MyViewHolder> {
    private DatabaseHelper db;
    private OrderSummary2Fragment context;
    private ArrayList<FreeGoods> listFreeGoods;
    private ArrayList<ArrayList<OptionFreeGoods>> mData = new ArrayList<>();
    private List<String> listOption;
    private List<String> temp;
    private String option = Constants.EMPTY_STRING, begin = Constants.EMPTY_STRING, amounts = Constants.EMPTY_STRING;
    private BigDecimal totalDisc = BigDecimal.ZERO, totalDiscQty = BigDecimal.ZERO;
    private int countDisc = 0;
    private int flag = 0;
    private Boolean spinnerSelected = false;
    private ListFreeGoodsAdapter mAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView klasifikasiNJenisJual;
        RecyclerView mRecyclerView;

        public MyViewHolder(View view) {
            super(view);
            klasifikasiNJenisJual = view.findViewById(R.id.klasifikasiNJenisJual);
            mRecyclerView = view.findViewById(R.id.listFreeGoods);
        }
    }

    public FreeGoodsNAdapter(OrderSummary2Fragment mcontext, ArrayList<FreeGoods> results, DatabaseHelper db) {
        listFreeGoods = results;
        context = mcontext;
        this.db = db;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_free_goods_n, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FreeGoods freeGoods = listFreeGoods.get(position);

        if (freeGoods.getKlasifikasi() != null || freeGoods.getJenisJual() != null) {
            holder.klasifikasiNJenisJual.setText(Helper.validateResponseEmpty(String.valueOf(freeGoods.getKlasifikasi())).concat(Constants.STRIP)
                    .concat(String.valueOf(db.getNamaJenisJual(freeGoods.getJenisJual()))).concat(Constants.STRIP)
                    .concat(Constants.TOP.concat(Constants.SPACE).concat(String.valueOf(freeGoods.getTopF())).concat(Constants.SPACE).concat(Constants.HARI))
            );
        }

        temp = new ArrayList<>();
        listOption = new ArrayList<>();

        if (freeGoods.getListOptionFreeGoods() != null) {
            mData = new ArrayList<>(Arrays.asList(freeGoods.getListOptionFreeGoods()));
        } else if (freeGoods.getArraylistOptionFG() != null) {
            mData = new ArrayList<>(freeGoods.getArraylistOptionFG());
        }

        if (mData != null && !mData.isEmpty()) {
            if (mData.size() > 1) {//multiple data
                flag = 1;
                setListData(position);
            } else {//single data
                flag = 2;
                setListData(position);
            }
        }
        mAdapter = new ListFreeGoodsAdapter(listOption, context);
        holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context.getContext()));
        holder.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.mRecyclerView.setAdapter(mAdapter);
    }

    private void setListData(int position) {
        for (int i = 0; i < mData.size(); i++) {
            countDisc = 0;
            option = Constants.EMPTY_STRING;
            for (int j = 0; j < mData.get(i).size(); j++) {
                if (mData.get(i).get(j).getAmount() != null
                        && mData.get(i).get(j).getUom() != null) {
//                    mData.get(i).get(j).setAmountUom(mData.get(i).get(j).getUom());

                    if (!option.equals(Constants.EMPTY_STRING)) {
                        begin = option + Constants.DOTCOMA + Constants.SPACE;
                    } else {
                        begin = option;
                    }

                    amounts = (String.valueOf((mData.get(i).get(j).getAmount() == null ? BigDecimal.ZERO : mData.get(i).get(j).getAmount()).
                            add(mData.get(i).get(j).getAmountP() == null ? BigDecimal.ZERO : mData.get(i).get(j).getAmountP())))
                            .concat(Constants.SPACE)
                            .concat(mData.get(i).get(j).getAmountUom());

                    if (mData.get(i).get(j).getId_material() != null) {

                        if (mData.get(i).get(j).getId_material().toLowerCase().contains(Constants.DISKON.toLowerCase())) {
                            totalDisc = totalDisc.add((mData.get(i).get(j).getAmount() == null ? BigDecimal.ZERO : mData.get(i).get(j).getAmount()).
                                    add(mData.get(i).get(j).getAmountP() == null ? BigDecimal.ZERO : mData.get(i).get(j).getAmountP()));
                            totalDiscQty = totalDiscQty.add((mData.get(i).get(j).getQty() == null ? BigDecimal.ZERO : mData.get(i).get(j).getQty()).
                                    add(mData.get(i).get(j).getQtyP() == null ? BigDecimal.ZERO : mData.get(i).get(j).getQtyP()));

//                            if (countDisc == 1) {
                            try {
                                option = begin + Constants.DISKON
                                        .concat(Constants.SPACE)
                                        .concat(String.valueOf(
                                                (String.valueOf(mData.get(i).get(j).getUom()).equals(context.getString(R.string.uomPercentFreeGoods)) ?
                                                        totalDiscQty.multiply(new BigDecimal(100)) : totalDiscQty).setScale(3, BigDecimal.ROUND_HALF_UP)))
                                        .concat(String.valueOf(mData.get(i).get(j).getUom()).equals(context.getString(R.string.uomPercentFreeGoods)) ? Constants.EMPTY_STRING :Constants.SPACE )
                                        .concat(String.valueOf(mData.get(i).get(j).getUom()).equals(context.getString(R.string.uomPercentFreeGoods)) ? String.valueOf(Constants.PERCENT) : String.valueOf(mData.get(i).get(j).getUom()))
                                        .concat(Constants.SPACE)
                                        .concat(Helper.toRupiahFormat2(String.valueOf(totalDisc.setScale(0, BigDecimal.ROUND_HALF_UP))))
                                        .concat(Constants.SPACE)
                                        .concat(db.getUomName(mData.get(i).get(j).getAmountUom()));
                            }catch (NullPointerException ignored){

                            }

//                                countDisc = 0;
                            totalDisc = BigDecimal.ZERO;
                            totalDiscQty = BigDecimal.ZERO;

//                            }
//
//                            countDisc++;
                        } else {
                            String materialName;
                            if (mData.get(i).get(j).getMaterialName() != null) {
                                materialName = mData.get(i).get(j).getMaterialName();
                            } else {
                                materialName = db.getMaterialName(mData.get(i).get(j).getId_material());
                            }
                            option = begin + materialName
                                    .concat(Constants.SPACE)
                                    .concat(amounts);

                        }

                    } else if (mData.get(i).get(j).getMaterialName() != null) {
                        option = begin + mData.get(i).get(j).getMaterialName()
                                .concat(Constants.SPACE)
                                .concat(amounts);

                    }
                }
            }
            temp.add(option);
            listOption.add(i, option);
        }
    }

    @Override
    public int getItemCount() {
        return listFreeGoods.size();
    }
}
package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.OrderSummaryDetail2Fragment;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;

public class OrderSummaryDetailAdapterV2 extends BaseAdapter {
    private List<VisitOrderDetailResponse> mData;
    private OrderSummaryDetail2Fragment context;
    private LayoutInflater mInflater;
    private DatabaseHelper db;
    private String qty1 = "", uom1 = "", qty2 = "", uom2 = "";


    public OrderSummaryDetailAdapterV2(OrderSummaryDetail2Fragment fragment, List<VisitOrderDetailResponse> orderSummaryDetail, DatabaseHelper db) {
        this.mData = orderSummaryDetail;
        this.db = db;
        context = fragment;
        mInflater = LayoutInflater.from(fragment.getContext());
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void swap(ArrayList<VisitOrderDetailResponse> datas) {
        if (datas == null || datas.size() == 0)
            return;
        if (mData != null && mData.size() > 0)
            mData.clear();
        mData.addAll(datas);
        notifyDataSetChanged();

    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.row_view_order_summary_detail, null);
            holder = new ViewHolder();
            // Check if no view has focus:

            holder.noOrderSummary = view.findViewById(R.id.txtIndex);
            holder.kodeOrderSummary = view.findViewById(R.id.txtIdMaterial);
            holder.materialName = view.findViewById(R.id.txtMaterialName);
            holder.edtQty = view.findViewById(R.id.edtTxtQty);
            holder.edtPrice = view.findViewById(R.id.edtPrice);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        VisitOrderDetailResponse orderSummaryDetail = mData.get(position);
        holder.noOrderSummary.setText(String.valueOf(position + 1));
        holder.kodeOrderSummary.setText(orderSummaryDetail.getIdMaterial());
        holder.materialName.setText(orderSummaryDetail.getMaterialName());

        if (orderSummaryDetail.getQty1() != null) {
            qty1 = String.valueOf(orderSummaryDetail.getQty1());
        }

        if (orderSummaryDetail.getQty2() != null) {
            qty2 = String.valueOf(orderSummaryDetail.getQty2());
        }

        if (orderSummaryDetail.getUom1() != null) {
            uom1 = String.valueOf(orderSummaryDetail.getUom1());
        }

        if(!qty2.equals("")){
            if (orderSummaryDetail.getUom2() != null) {
                uom2 = String.valueOf(orderSummaryDetail.getUom2());
            }
        }

        holder.edtQty.setText((Helper.toRupiahFormat(qty1)).concat(Constants.SPACE).concat(uom1)
                .concat(Constants.SPACE)
                .concat((Helper.toRupiahFormat(qty2))).concat(Constants.SPACE).concat(uom2));

        if (orderSummaryDetail.getPrice() != null) {
            holder.edtPrice.setText(Helper.toRupiahFormat2(String.valueOf(orderSummaryDetail.getPrice())));
        } else {
            holder.edtPrice.setText(String.valueOf("0"));
        }

        return view;
    }

    static class ViewHolder {
        private TextView noOrderSummary, kodeOrderSummary, discOrderSummary, priceOrderSummary, priceNettOrderSummary, materialName;
        private EditText edtQty, edtPrice;
    }


}

package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.SoHistoryHeaderFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;

public class SalesOrderHistoryAdapter extends RecyclerView.Adapter<SalesOrderHistoryAdapter.MyViewHolder> {

    private List<VisitOrderHeader> salesOrderHistoryList;
    private SoHistoryHeaderFragment mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView toNumberValue, outletValue, statusValue;
        TextView volValue;
        LinearLayout parentView;

        public MyViewHolder(View view) {
            super(view);
            toNumberValue = view.findViewById(R.id.toNumberValue);
            volValue = view.findViewById(R.id.volValue);
            outletValue = view.findViewById(R.id.outletValue);
            statusValue = view.findViewById(R.id.statusValue);
            parentView = view.findViewById(R.id.rr);

        }
    }


    public SalesOrderHistoryAdapter(List<VisitOrderHeader> salesOrderHistoryList, SoHistoryHeaderFragment mContext) {
        this.salesOrderHistoryList = salesOrderHistoryList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_sales_order_history_header, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final VisitOrderHeader salesOrderHistory = salesOrderHistoryList.get(position);
        holder.toNumberValue.setText(Helper.validateResponseEmpty(salesOrderHistory.getId()));
        if(salesOrderHistory.getTotalAmount() != null){
            holder.volValue.setText("Rp. " + Helper.toRupiahFormat2(String.valueOf(salesOrderHistory.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP))));
        }
        holder.outletValue.setText(Helper.validateResponseEmpty(salesOrderHistory.getIdOutlet()).concat(" " + Helper.validateResponseEmpty(salesOrderHistory.getDate_mobile())));
        holder.statusValue.setText(Helper.validateResponseEmpty(salesOrderHistory.getStatusString()));

        if (salesOrderHistory.getStatusPrice() != null) {
            if (salesOrderHistory.getStatusPrice().equals(mContext.getResources().getString(R.string.status_price_unavailable))) {
                holder.parentView.setBackgroundColor(mContext.getResources().getColor(R.color.red1));
            } else {
                holder.parentView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.removeItemParam(Constants.ACCESS_POINT);
                Helper.removeItemParam(Constants.SO_HEADER_SELECTED);
                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
                Helper.removeItemParam(Constants.GET_DETAIL_VISIT);

                Helper.setItemParam(Constants.SO_HEADER_SELECTED, salesOrderHistory);
                if (salesOrderHistory.getOrderType() != null) {
                    Helper.setItemParam(Constants.ORDER_TYPE, salesOrderHistory.getOrderType());
                }

                OutletResponse data = new OutletResponse();
                data.setIdOutlet(salesOrderHistory.getIdOutlet());
                Helper.setItemParam(Constants.OUTLET, data);

                Helper.setItemParam(Constants.ACCESS_POINT, mContext.getResources().getString(R.string.navmenu6b));
                Helper.setItemParam(Constants.FROM_SALES_ORDER, "from sales order");
                mContext.goToOrder();

//                Fragment fr = new OrderFragment();
//                mContext.setContent(fr);
            }
        });
    }

    @Override
    public int getItemCount() {
        return salesOrderHistoryList.size();
    }
}
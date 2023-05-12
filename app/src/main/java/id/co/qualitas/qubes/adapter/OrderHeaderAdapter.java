package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.NewMainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.OrderHeaderFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.VisitOrderHeader;

public class OrderHeaderAdapter extends RecyclerView.Adapter<OrderHeaderAdapter.MyViewHolder> {
    private List<VisitOrderHeader> mData;
    private OrderHeaderFragment mContext;
    private String orderType;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView noTo, statusOrder, txtName;
        private TextView totalAmount;
        private CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            noTo = view.findViewById(R.id.txt1);
            totalAmount = view.findViewById(R.id.txt2);
            statusOrder = view.findViewById(R.id.txt3);
            cardView = view.findViewById(R.id.cardView);
        }
    }


    public OrderHeaderAdapter(ArrayList<VisitOrderHeader> data, OrderHeaderFragment context, String orderType) {
        this.mData = data;
        this.mContext = context;
        this.orderType = orderType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_order_header, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final VisitOrderHeader current = mData.get(position);
        holder.noTo.setText(Helper.validateResponseEmpty(current.getId()));
        holder.statusOrder.setText(Helper.validateResponseEmpty(current.getStatusString()));

        if (orderType.equals(Constants.ORDER_CANVAS_TYPE)) {
            holder.txtName.setText("No. CO");
        } else {
            holder.txtName.setText("No. TO");
        }

        if (current.getTotalAmount() != null) {
            holder.totalAmount.setText("Rp. ".concat(Helper.toRupiahFormat2(String.valueOf(current.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP)))));
        } else {
            holder.totalAmount.setText(Helper.toRupiahFormat2("0"));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!current.getStatusString().equals(Constants.SUCCESS_ORDER_SO)){

                Helper.setItemParam(Constants.ORDER_HEADER_SELECTED, current);
                Helper.removeItemParam(Constants.ALL_ONE_TIME_DISCOUNT_DETAIL);
                Helper.removeItemParam(Constants.VISIT_ORDER_HEADER);
                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);

                ((NewMainActivity) mContext.getActivity()).changePage(12);

//                Fragment fr = new OrderSoldFragment();
//                mContext.setContent(fr);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
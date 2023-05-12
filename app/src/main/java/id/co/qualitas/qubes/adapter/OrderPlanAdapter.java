package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.OrderPlanFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OrderPlanHeader;

public class OrderPlanAdapter extends RecyclerView.Adapter<OrderPlanAdapter.MyViewHolder> {
    //code, name, contact
    private ArrayList<OrderPlanHeader> orderPlanList;
    private OrderPlanFragment mcontext;
    private DatabaseHelper db;
    private OrderPlanHeaderChildV2Adapter mAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDate, txtTotalPrice, txtCount;
        private RecyclerView recyclerView;

        public MyViewHolder(View view) {
            super(view);
            txtDate = view.findViewById(R.id.txtDate);
            txtTotalPrice = view.findViewById(R.id.txtTotalPrice);
            txtCount = view.findViewById(R.id.txtCount);
            recyclerView = view.findViewById(R.id.recycler_view);
            db = new DatabaseHelper(mcontext.getContext());
        }
    }

    public OrderPlanAdapter(ArrayList<OrderPlanHeader> orderPlanList, OrderPlanFragment context) {
        this.orderPlanList = orderPlanList;
        this.mcontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_order_plan_header_par_v2, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final OrderPlanHeader orderPlan = orderPlanList.get(position);

        if(orderPlan.getOutletDate() != null){
            holder.txtDate.setText(String.valueOf(orderPlan.getOutletDate()));
        }
        if(orderPlan.getTotalPrice() != null){
            holder.txtTotalPrice.setText(Helper.toRupiahFormat2(orderPlan.getTotalPrice()));
        }

        if(orderPlan.getContent() != null){
            holder.txtCount.setText(String.valueOf(orderPlan.getContent().size()));

        }
        mAdapter = new OrderPlanHeaderChildV2Adapter(orderPlan.getContent(), mcontext);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mcontext.getContext()));
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(mAdapter);

    }

    @Override
    public int getItemCount() {
        return orderPlanList.size();
    }

}
package id.co.qualitas.qubes.adapter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.NewMainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.OrderPlanFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OrderPlanHeader;

public class OrderPlanHeaderChildV2Adapter extends RecyclerView.Adapter<OrderPlanHeaderChildV2Adapter.MyViewHolder> {
    private List<OrderPlanHeader> orderPlanList;
    private OrderPlanFragment mcontext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOutlet, txtId, txtCall, txtAchiev, txtPlan;
        private LinearLayout ic_del;
        private CardView row;

        public MyViewHolder(View view) {
            super(view);
            txtOutlet = view.findViewById(R.id.txtOutlet);
            txtId = view.findViewById(R.id.id);
//            txtCall = view.findViewById(R.id.txtCall);
//            txtAchiev = view.findViewById(R.id.txtAchiev);
            txtPlan = view.findViewById(R.id.txtPlan);
            ic_del = view.findViewById(R.id.imgDelete);
            row = view.findViewById(R.id.row);
        }
    }

    public OrderPlanHeaderChildV2Adapter(List<OrderPlanHeader> orderPlanList, OrderPlanFragment context) {
        this.orderPlanList = orderPlanList;
        this.mcontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_order_plan_header_child_v2, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final OrderPlanHeader orderPlan = orderPlanList.get(position);
        holder.txtOutlet.setText(Helper.validateResponseEmpty(orderPlan.getOutletName())
                .concat(Constants.SPACE).concat("(")
                .concat(Helper.validateResponseEmpty(orderPlan.getIdOutlet())).concat(")"));

        holder.txtId.setText(Helper.validateResponseEmpty(orderPlan.getId()));
        if(orderPlan.getPlan() != null){
            holder.txtPlan.setText(Helper.toRupiahFormat2(orderPlan.getPlan()));
        }

        if (mcontext.getCurrentDate() != null && orderPlan.getOutletDate() != null) {
            if (Helper.changeDateFormatDefa(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, mcontext.getCurrentDate()).equals(orderPlan.getOutletDate())) {
                holder.ic_del.setVisibility(View.INVISIBLE);
                holder.ic_del.setEnabled(false);
            } else {
                holder.ic_del.setVisibility(View.VISIBLE);
                holder.ic_del.setEnabled(true);
            }
        }

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.removeItemParam(Constants.IS_TODAY);
                Helper.removeItemParam(Constants.GET_DETAIL_ORDER_PLAN);
                Helper.removeItemParam(Constants.ORDER_PLAN_DETAIL_SAVE);
                Helper.removeItemParam(Constants.ORDER_PLAN_TEMP);

                Helper.setItemParam(Constants.ORDER_PLAN_SELECTED, orderPlan);
                Helper.setItemParam(Constants.FLAG_DO_ORDER_PLAN_DB, "1");
                Fragment fragment;
                if (mcontext.getCurrentDate() != null && orderPlan.getOutletDate() != null) {
                    if (Helper.changeDateFormatDefa(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, mcontext.getCurrentDate()).equals(orderPlan.getOutletDate())) {
                        Helper.setItemParam(Constants.IS_TODAY, "Y");
                        Helper.setItemParam(Constants.ORDER_PLAN_DETAIL_HEADER, orderPlan);

                        ((NewMainActivity) mcontext.getActivity()).changePage(21);
//                        fragment = new OrderPlanSummaryFragmentV2();
//                        mcontext.setContent(fragment);
                    } else {
                        ((NewMainActivity) mcontext.getActivity()).changePage(20);
//                        fragment = new OrderPlanDetailFragmentV2();
//                        mcontext.setContent(fragment);
                    }
                } else {
                    ((NewMainActivity) mcontext.getActivity()).changePage(20);
//                    fragment = new OrderPlanDetailFragmentV2();
//                    mcontext.setContent(fragment);
                }

            }
        });

        holder.ic_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.setItemParam(Constants.OUTLET_CODE_CHOOSE, orderPlan.getIdOutlet());
                LayoutInflater inflater = LayoutInflater.from(mcontext.getContext());
                View dialogview;
                final Dialog alertDialog = new Dialog(mcontext.getContext());
                dialogview = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(dialogview);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btnCancel = alertDialog.findViewById(R.id.btnCancel);
                Button btnSave = alertDialog.findViewById(R.id.btnSave);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (orderPlan.getId() != null && orderPlan.getIdOutlet() != null) {
                            Helper.removeItemParam(Constants.FLAG_DO_ORDER_PLAN_DB);

                            Helper.setItemParam(Constants.ORDER_PLAN_SELECTED, orderPlan);
                            mcontext.deleteOrderPlan();
                            orderPlanList.remove(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                            mcontext.onResume();
                        } else {
                            Toast.makeText(mcontext.getContext(), mcontext.getResources().getString(R.string.missingData), Toast.LENGTH_SHORT).show();
                        }

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderPlanList == null ? 0 : orderPlanList.size();
    }
}
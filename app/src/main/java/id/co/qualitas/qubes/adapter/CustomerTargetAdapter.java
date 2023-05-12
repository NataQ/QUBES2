package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.CustomerTargetFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.TargetResponse;

public class CustomerTargetAdapter extends RecyclerView.Adapter<CustomerTargetAdapter.MyViewHolder> {
    //txtKodeCustomer, txtNameCustomer, target
    private boolean month;
    private ArrayList<TargetResponse> customerTargetList;
    private CustomerTargetFragment context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTimeCustomer, txtKodeCustomer, txtNameCustomer, initialTarget, currentTarget, reality, txtViewCodeProductCustomer;
        CardView LLCustomerTarget;

        public MyViewHolder(View view) {
            super(view);
            txtTimeCustomer = view.findViewById(R.id.txtTimeCustomer);
            txtKodeCustomer = view.findViewById(R.id.txtKodeCustomer);
            txtNameCustomer = view.findViewById(R.id.txtNameCustomer);
            txtViewCodeProductCustomer = view.findViewById(R.id.txtViewCodeProductCustomer);
            initialTarget = view.findViewById(R.id.txtViewInitialTargetCustomer);
            currentTarget = view.findViewById(R.id.txtViewCurrentTargetCustomer);
            reality = view.findViewById(R.id.txtActCustomer);
            LLCustomerTarget = view.findViewById(R.id.LLCustomerTarget);
        }
    }

    public CustomerTargetAdapter(ArrayList<TargetResponse> customerTargetList, boolean month, CustomerTargetFragment mContext) {
        this.customerTargetList = customerTargetList;
        this.month = month;
        this.context = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_customer_target, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TargetResponse targetResponse = customerTargetList.get(position);
//        holder.txtTimeCustomer.setId(position);
//        holder.txtKodeCustomer.setId(position);
//        holder.txtNameCustomer.setId(position);
//        holder.txtViewCodeProductCustomer.setId(position);
//        holder.initialTarget.setId(position);
//        holder.currentTarget.setId(position);
//        holder.reality.setId(position);
//        holder.LLCustomerTarget.setId(position);

        if (month) {
            holder.initialTarget.setVisibility(View.INVISIBLE);
            if (targetResponse.getTarget() != null) {
                holder.currentTarget.setText(Helper.toRupiahFormat(String.valueOf(targetResponse.getTarget())));
            }
        } else {
            holder.initialTarget.setVisibility(View.VISIBLE);
        }

        if(targetResponse.getMonth() > 0){
            holder.txtTimeCustomer.setText(Helper.getMonthForInt(targetResponse.getMonth()-1));
        }else{
            holder.txtTimeCustomer.setText(targetResponse.getSalesDate());
        }

        holder.txtKodeCustomer.setText(Helper.validateResponseEmpty(targetResponse.getIdOutlet()));
        holder.txtNameCustomer.setText(Helper.validateResponseEmpty(targetResponse.getOutletName()));
        holder.txtViewCodeProductCustomer.setText(Helper.validateResponseEmpty(targetResponse.getMatClass()));

        if (targetResponse.getTarget() != null) {
            holder.currentTarget.setText(Helper.toRupiahFormat(String.valueOf(targetResponse.getTarget())));
        }

        if (targetResponse.getTargetAwal() != null) {
            holder.initialTarget.setText(Helper.toRupiahFormat(String.valueOf(targetResponse.getTargetAwal())));
        }

        holder.reality.setText(Helper.toRupiahFormat(String.valueOf(targetResponse.getActual())));

        holder.LLCustomerTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.setItemParam(Constants.TARGET_ITEM_DETAIL, targetResponse);
                Helper.setItemParam(Constants.SELECTED_TAB, context.getResources().getString(R.string.customer));
                context.goToTargetDetailFragment();
//                Fragment fragment = new TargetDetailFragment();
//                context.setContent(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerTargetList == null ? 0 : customerTargetList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
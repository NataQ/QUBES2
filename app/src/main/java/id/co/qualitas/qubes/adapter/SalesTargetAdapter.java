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

public class SalesTargetAdapter extends RecyclerView.Adapter<SalesTargetAdapter.MyViewHolder> {
    //code, name, target
    private ArrayList<TargetResponse> salesTargetList;
    private boolean month;
    private CustomerTargetFragment mContext;
    private String produk;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView time, code, name, initialTarget, currentTarget, reality, txtViewCodeProductCustomer;
        CardView LLCustomerTarget;

        public MyViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.txtTimeCustomer);
            code = view.findViewById(R.id.txtKodeCustomer);
            name = view.findViewById(R.id.txtNameCustomer);
            txtViewCodeProductCustomer = view.findViewById(R.id.txtViewCodeProductCustomer);
            initialTarget = view.findViewById(R.id.txtViewInitialTargetCustomer);
            currentTarget = view.findViewById(R.id.txtViewCurrentTargetCustomer);
            reality = view.findViewById(R.id.txtActCustomer);
            LLCustomerTarget = view.findViewById(R.id.LLCustomerTarget);
        }
    }

    public SalesTargetAdapter(ArrayList<TargetResponse> salesTargetList, boolean month, CustomerTargetFragment mContext) {
        this.salesTargetList = salesTargetList;
        this.month = month;
        this.produk = produk;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_sales_target, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TargetResponse targetResponse = salesTargetList.get(position);
        if (month) {
            holder.initialTarget.setVisibility(View.GONE);
        } else {
            holder.initialTarget.setVisibility(View.VISIBLE);
        }

        holder.code.setText(targetResponse.getIdOutlet());
        holder.name.setText(targetResponse.getOutletName());
        holder.txtViewCodeProductCustomer.setText(targetResponse.getMatClass());
        if(targetResponse.getMonth() > 0){
            holder.time.setText(Helper.getMonthForInt(targetResponse.getMonth()-1));
        }else{
            holder.time.setText(targetResponse.getSalesDate());
        }
        if (targetResponse.getTargetAwal() != null) {
            holder.initialTarget.setText(Helper.toRupiahFormat(String.valueOf(targetResponse.getTargetAwal())));
        }
        if (targetResponse.getTarget() != null) {
            holder.currentTarget.setText(Helper.toRupiahFormat(String.valueOf(targetResponse.getTarget())));
        }
        holder.reality.setText(Helper.toRupiahFormat(String.valueOf(targetResponse.getActual())));
        holder.LLCustomerTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.setItemParam(Constants.TARGET_ITEM_DETAIL, targetResponse);
                Helper.setItemParam(Constants.SELECTED_TAB, mContext.getResources().getString(R.string.klproduct));
                mContext.goToTargetDetailFragment();
//                Fragment fragment = new TargetDetailFragment();
//                mContext.setContent(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return salesTargetList.size();
    }
}
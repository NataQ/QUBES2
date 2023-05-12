package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.ToPrice;

public class ShowPriceAdapter extends RecyclerView.Adapter<ShowPriceAdapter.MyViewHolder> {

    private ArrayList<ToPrice> listToPrice = new ArrayList<>();
    private BaseFragment mcontext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView condType;
        public TextView amount;
        public TextView discValue;
        public TextView noKc;
        public TextView txtRpDisc;

        public MyViewHolder(View view) {
            super(view);
            condType = view.findViewById(R.id.condType);
            amount = view.findViewById(R.id.amount);
            discValue = view.findViewById(R.id.discValue);
            noKc = view.findViewById(R.id.noKc);
            txtRpDisc = view.findViewById(R.id.txtRpDisc);
        }
    }


    public ShowPriceAdapter(ArrayList<ToPrice> listToPrice, BaseFragment mcontext) {
        this.listToPrice = listToPrice;
        this.mcontext = mcontext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_price, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ToPrice toPrice = listToPrice.get(position);
        if (toPrice.getCond_type() != null) {
            holder.condType.setText(String.valueOf(toPrice.getCond_type()));
        }
        if (toPrice.getAmount() != null) {
            holder.amount.setText(Helper.toRupiahFormat2(String.valueOf(toPrice.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP))));
        }
        if (toPrice.getDiscValueString() != null) {
            if(!toPrice.getDiscValueString().contains(Constants.PERCENT)){
                holder.txtRpDisc.setVisibility(View.VISIBLE);
                holder.discValue.setText(Helper.toRupiahFormat2(String.valueOf(new BigDecimal(toPrice.getDiscValueString()).setScale(2, BigDecimal.ROUND_HALF_UP))));
            }else{
                holder.txtRpDisc.setVisibility(View.GONE);
                holder.discValue.setText(String.valueOf(new BigDecimal(toPrice.getDiscValueString().split("%")[0]).setScale(2, BigDecimal.ROUND_HALF_UP)).concat("%"));
            }
        }else{
            holder.txtRpDisc.setVisibility(View.INVISIBLE);
        }

        if (toPrice.getKc_no() != null) {
            holder.noKc.setText(String.valueOf(toPrice.getKc_no()));
        }
    }

    @Override
    public int getItemCount() {
        return listToPrice == null ? 0 : listToPrice.size();
    }
}



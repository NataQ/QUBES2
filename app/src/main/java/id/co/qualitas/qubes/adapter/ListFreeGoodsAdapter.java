package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.OrderSummary2Fragment;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.model.OptionFreeGoods;

public class ListFreeGoodsAdapter extends RecyclerView.Adapter<ListFreeGoodsAdapter.MyViewHolder> {
    private DatabaseHelper db;
    private OrderSummary2Fragment context;
    private List<String> listFreeGoods;
    private ArrayList<ArrayList<OptionFreeGoods>> mData = new ArrayList<>();
    private List<String> listOption;
    private List<String> temp;

    private String option = Constants.EMPTY_STRING, begin = Constants.EMPTY_STRING, amounts = Constants.EMPTY_STRING;
    private BigDecimal totalDisc = BigDecimal.ZERO;
    private int countDisc = 0;
    private int flag = 0;
    private Boolean spinnerSelected = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtIndex, txtFreeGoods;

        public MyViewHolder(View view) {
            super(view);
            txtIndex = view.findViewById(R.id.index);
            txtFreeGoods = view.findViewById(R.id.txtFreeGoods);
        }
    }

    public ListFreeGoodsAdapter(List<String> results, OrderSummary2Fragment mcontext) {
        listFreeGoods = results;
        context = mcontext;
        this.db = db;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_list_free_goods, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String txtFreeGoods = listFreeGoods.get(position);
        holder.txtIndex.setId(position);
        holder.txtFreeGoods.setId(position);

        holder.txtIndex.setText(String.valueOf(position + 1).concat(Constants.DOT));
        if (txtFreeGoods != null) {
            holder.txtFreeGoods.setText(txtFreeGoods);
        }

    }

    @Override
    public int getItemCount() {
        return listFreeGoods.size();
    }
}
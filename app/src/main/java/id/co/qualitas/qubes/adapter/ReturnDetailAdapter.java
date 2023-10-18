package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Return;

public class ReturnDetailAdapter extends RecyclerView.Adapter<ReturnDetailAdapter.MyViewHolder> {
    private ArrayList<Return> returnList;
    private DatabaseHelper db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaProduk, kodeProduk, notesDetail, date, qty, qty2, reason;
        public TextView batch, uom1, uom2, categoryReturn, txtNo;

        public MyViewHolder(View view) {
            super(view);
            namaProduk = view.findViewById(R.id.namaProduk);
            kodeProduk = view.findViewById(R.id.kodeProduk);
            date = view.findViewById(R.id.date);
            notesDetail = view.findViewById(R.id.notesDetail);
            qty = view.findViewById(R.id.qty);
            qty2 = view.findViewById(R.id.qty2);
            reason = view.findViewById(R.id.reason);
            batch = view.findViewById(R.id.batch);
            uom1 = view.findViewById(R.id.uom1);
            txtNo = view.findViewById(R.id.txtNo);
            uom2 = view.findViewById(R.id.uom2);
            categoryReturn = view.findViewById(R.id.categoryReturn);
        }
    }


    public ReturnDetailAdapter(ArrayList<Return> returnList, DatabaseHelper db) {
        this.returnList = returnList;
        this.db = db;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_return_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Return aReturn = returnList.get(position);
        holder.txtNo.setText(String.valueOf(position + 1));
        holder.kodeProduk.setText(Helper.validateResponseEmpty(aReturn.getId()));
        holder.namaProduk.setText(Helper.validateResponseEmpty(aReturn.getName()));
        if (aReturn.getExpiredDate() != null) {
            holder.date.setText(Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_12, aReturn.getExpiredDate()));
        }
        holder.notesDetail.setText(Helper.validateResponseEmpty(aReturn.getDescription()));
//        if (aReturn.getQty1() != null && aReturn.getUom1() != null) {
//            holder.qty.setText(Helper.validateResponseEmpty(String.valueOf(aReturn.getQty1())));
//            holder.uom1.setText(Helper.validateResponseEmpty(db.getUomName(aReturn.getUom1())));
//        } else {
//            holder.qty.setText("-");
//        }
        if (aReturn.getQty2() != null && aReturn.getUom2() != null) {
            holder.qty2.setText(Helper.validateResponseEmpty(String.valueOf(aReturn.getQty2())));
            holder.uom2.setText(Helper.validateResponseEmpty(db.getUomName(aReturn.getUom2())));
        } else {
            holder.qty2.setText("-");
        }
        holder.reason.setText(Helper.validateResponseEmpty(aReturn.getReason()));
        holder.batch.setText(Helper.validateResponseEmpty(aReturn.getBatch()));
        holder.categoryReturn.setText(Helper.validateResponseEmpty(aReturn.getCategory()));
    }

    @Override
    public int getItemCount() {
        return returnList.size();
    }
}
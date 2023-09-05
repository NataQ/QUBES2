package id.co.qualitas.qubes.adapter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.NewMainActivity;
import id.co.qualitas.qubes.activity.Return2Fragment;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.ReturnResponse;

public class ReturnSalesAdapter extends RecyclerView.Adapter<ReturnSalesAdapter.MyViewHolder> {
    private List<ReturnResponse> returnHeaderList = new ArrayList<>();
    public Return2Fragment mContext;
    protected Fragment fragment = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView noBrb, noRetur, tglBRB, tglRR;
        LinearLayout imgDelete;
        CardView cardView;
        ImageView imgSyncStatus;

        public MyViewHolder(View view) {
            super(view);
            imgDelete = view.findViewById(R.id.ic_del);
            imgSyncStatus = view.findViewById(R.id.ic_sync_status);
            noBrb = view.findViewById(R.id.noBRB);
            noRetur = view.findViewById(R.id.noReturRep);
            tglBRB = view.findViewById(R.id.tglBRB);
            tglRR = view.findViewById(R.id.tglRR);
            cardView = view.findViewById(R.id.cardView);
        }
    }

    public ReturnSalesAdapter(List<ReturnResponse> returnHeaderList, Return2Fragment mContext) {
        this.returnHeaderList = returnHeaderList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_return_header_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ReturnResponse returnHeader = returnHeaderList.get(position);
        if(returnHeader.getId() != null){
            if(returnHeader.getId().contains(Constants.ID_TRULY_RETURN)){
                holder.imgSyncStatus.setVisibility(View.VISIBLE);
                holder.imgDelete.setVisibility(View.GONE);
            }else{
                holder.imgSyncStatus.setVisibility(View.GONE);
                holder.imgDelete.setVisibility(View.VISIBLE);
            }
        }

        if (returnHeader.getNoBrb() != null) {
            holder.noBrb.setText(returnHeader.getNoBrb());
        }
        if (returnHeader.getNoRr() != null) {
            holder.noRetur.setText(returnHeader.getNoRr());
        }
        if (returnHeader.getTanggalBrb() != null) {
            holder.tglBRB.setText(Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_12, returnHeader.getTanggalBrb()));
        }
        if (returnHeader.getTanggalRr() != null) {
            holder.tglRR.setText(Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_12, returnHeader.getTanggalRr()));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.setItemParam(Constants.RETURN_DETAIL_CHOOSE, returnHeader);
                Helper.removeItemParam(Constants.CURRENTPAGE);
                ((NewMainActivity) mContext.getActivity()).changePage(19);
//                Intent intent = new Intent(mContext, ReturnDetail2Fragment.class);
//                mContext.startActivity(intent);
//                fragment = new ReturnSalesDetailFragment(); ada sebelumnya, blum d ubah ke activity
//                mContext.setContent(fragment);
            }
        });

//        if (returnHeader.getStatusSync() != null) {
//            if (returnHeader.getStatusSync().equals(Constants.NOT_SYNCED)) {
//                holder.imgDelete.setVisibility(View.VISIBLE);
//            } else if (returnHeader.getStatusSync().equals(Constants.SYNCED)) {
//                holder.imgDelete.setVisibility(View.INVISIBLE);
//            }
//        } else {
//            holder.imgDelete.setVisibility(View.VISIBLE);
//        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(mContext.getContext());
                View dialogview;
                final Dialog alertDialog = new Dialog(mContext.getContext());
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
                        mContext.deleteReturn(returnHeader.getId(), position);
                        returnHeaderList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return returnHeaderList.size();
    }
}
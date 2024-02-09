package id.co.qualitas.qubes.adapter.aspp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.OrderActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> implements Filterable {
    private List<Order> mList;
    private List<Order> mFilteredList;
    private LayoutInflater mInflater;
    private OrderActivity mContext;
    private OnAdapterListener onAdapterListener;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    protected Customer outletHeader;
    private Dialog alertDialog;
    private View dialogview;
    private LayoutInflater inflater;

    public OrderAdapter(OrderActivity mContext, List<Order> mList, Customer outletHeader, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.outletHeader = outletHeader;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.onAdapterListener = onAdapterListener;
    }

    public void setData(List<Order> mDataSet) {
        this.mList = mDataSet;
        this.mFilteredList = mDataSet;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mList;
                } else {
                    List<Order> filteredList = new ArrayList<>();
                    for (Order row : mList) {

                        /*filter by name*/
                        if (row.getId_customer().toLowerCase().contains(charString.toLowerCase()) || row.getNama().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Order>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtOrderNo, txtOmzet, txtIdMobile, txtStatus, txtPayment;
        LinearLayout llStatus, llDelete;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            llDelete = itemView.findViewById(R.id.llDelete);
            llStatus = itemView.findViewById(R.id.llStatus);
            txtOrderNo = itemView.findViewById(R.id.txtOrderNo);
            txtOmzet = itemView.findViewById(R.id.txtOmzet);
            txtIdMobile = itemView.findViewById(R.id.txtIdMobile);
            txtPayment = itemView.findViewById(R.id.txtPayment);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            this.onAdapterListener = onAdapterListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAdapterListener.onAdapterClick(mFilteredList.get(getAdapterPosition()));
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_row_view_order, parent, false);
        return new Holder(itemView, onAdapterListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        setFormatSeparator();
        Order detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        holder.txtOrderNo.setText(format.format(detail.getId()));
        holder.txtOmzet.setText("Rp. " + format.format(detail.getOmzet()));
        holder.txtIdMobile.setText(Helper.isEmpty(detail.getIdHeader(), ""));
//        holder.txtStatus.setText(!Helper.isEmpty(detail.getStatus()) ? detail.getStatus() : "-");
        holder.txtPayment.setText(detail.isStatusPaid() ? "Lunas" : "Kredit");
        if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
            if (detail.isDeleted()) {
                holder.llDelete.setVisibility(View.GONE);
            } else {
                holder.llDelete.setVisibility(View.VISIBLE);
            }
        } else {
            holder.llDelete.setVisibility(View.GONE);
        }

        if (detail.isDeleted()) {
            holder.txtStatus.setText("Deleted");
            holder.llDelete.setVisibility(View.GONE);
            holder.llStatus.setVisibility(View.VISIBLE);
            holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.red_aspp));
            holder.txtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red2_aspp));
        } else {
            if (!Helper.isEmpty(detail.getStatus())) {
                switch (detail.getStatus()) {
                    case Constants.STATUS_APPROVE:
                        holder.llStatus.setVisibility(View.VISIBLE);
                        holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.green3_aspp));
                        holder.txtStatus.setText("Approve");
                        holder.txtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green_aspp));
                        break;
                    case Constants.STATUS_REJECTED:
                        holder.txtStatus.setText("Reject");
                        holder.llStatus.setVisibility(View.VISIBLE);
                        holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.red_aspp));
                        holder.txtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red2_aspp));
                        break;
                    case Constants.STATUS_PENDING:
                        holder.txtStatus.setText("Pending");
                        holder.llStatus.setVisibility(View.VISIBLE);
                        holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.yellow3_aspp));
                        holder.txtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.yellow_aspp));
                        break;
                    case Constants.STATUS_SYNC_SUCCESS:
                        holder.txtStatus.setText("Sync Success");
                        holder.llStatus.setVisibility(View.VISIBLE);
                        holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.blue8_aspp));
                        holder.txtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.aspp_blue9));
                        break;
                    case Constants.STATUS_DRAFT:
                        holder.txtStatus.setText("Draft");
                        holder.llStatus.setVisibility(View.VISIBLE);
                        holder.llStatus.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.gray12_aspp));
                        holder.txtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.black1_aspp));
                        break;
                    default:
                        holder.llStatus.setVisibility(View.GONE);
                        holder.llStatus.setBackgroundTintList(null);
                        holder.txtStatus.setText("-");
                }
            } else {
                holder.llStatus.setVisibility(View.GONE);
                holder.llStatus.setBackgroundTintList(null);
                holder.txtStatus.setText("-");
            }
        }

        holder.llDelete.setOnClickListener(v -> {
            inflater = LayoutInflater.from(mContext);
            alertDialog = new Dialog(mContext);
            initDialog(R.layout.aspp_dialog_confirmation);
            TextView txtTitle = alertDialog.findViewById(R.id.txtTitle);
            TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);
            Button btnNo = alertDialog.findViewById(R.id.btnNo);
            Button btnYes = alertDialog.findViewById(R.id.btnYes);
            txtTitle.setText("Delete Order");
            txtDialog.setText("Are you sure want to delete this order?");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
//                        if (detail.getTypePayment().equals("invoice")) {
                        new Database(mContext).deleteOrder(detail);
//                        new Database(mContext).deleteAllCollection(detail);
//                        } else {
//                            new Database(mContext).updateOrderAmount(detail);
//                        }
//                        mFilteredList.remove(holder.getAbsoluteAdapterPosition());
//                        notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                        mContext.requestData();
                        Toast.makeText(mContext, "Success remove item", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Failed remove item", Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(Order Order);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }

    private void initDialog(int resource) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        dialogview = inflater.inflate(resource, null);
        alertDialog.setContentView(dialogview);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);//back di hp
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
//        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}

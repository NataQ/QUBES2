package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.aspp.RouteCustomerFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;

public class RouteCustomerAdapter extends RecyclerView.Adapter<RouteCustomerAdapter.Holder> implements Filterable {
    private List<Customer> dataList;
    private List<Customer> dataFilteredList;
    private LayoutInflater mInflater;
    //    private OnClickListener onClickListener;
    private RouteCustomerFragment mContext;

//    public void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    public RouteCustomerAdapter(RouteCustomerFragment mContext, List<Customer> dataList) {
        if (dataList != null) {
            this.dataList = dataList;
            this.dataFilteredList = dataList;
        } else {
            this.dataList = new ArrayList<>();
            this.dataFilteredList = new ArrayList<>();
        }
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext.getContext());
    }

    public void setData(List<Customer> mDataSet) {
        this.dataList = mDataSet;
        this.dataFilteredList = mDataSet;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataFilteredList = dataList;
                } else {
                    List<Customer> filteredList = new ArrayList<>();
                    for (Customer row : dataList) {

                        /*filter by name*/
                        if (row.getId().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getNama().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    dataFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataFilteredList = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtAddress, txtStore, txtRoute, txtLabelRoute;
        Button btnDirection;

        public Holder(View itemView) {
            super(itemView);
            txtStore = itemView.findViewById(R.id.txtStore);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtLabelRoute = itemView.findViewById(R.id.txtLabelRoute);
            txtRoute = itemView.findViewById(R.id.txtRoute);
            btnDirection = itemView.findViewById(R.id.btnDirection);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            onClickListener.onClick(getAdapterPosition());
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_row_view_route_customer, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Customer detail = dataFilteredList.get(position);
        String mileage = " (" + Helper.isEmpty(detail.getMileage(), "0") + " KM)";
        holder.txtStore.setText(Helper.isEmpty(detail.getId(), "") + " - " + Helper.isEmpty(detail.getNama(), "") + mileage);
        holder.txtAddress.setText(Helper.isEmpty(detail.getAddress(), ""));
        holder.txtRoute.setText(Helper.isEmpty(detail.getRute(), ""));
        if (detail.isRoute()) {
            holder.txtLabelRoute.setText("Route");
            holder.txtLabelRoute.setBackground(ContextCompat.getDrawable(mContext.getContext(), R.drawable.bg_label_route));
            holder.txtLabelRoute.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.green_aspp));
        } else {
            holder.txtLabelRoute.setText("Non Route");
            holder.txtLabelRoute.setBackground(ContextCompat.getDrawable(mContext.getContext(), R.drawable.bg_label_non_route));
            holder.txtLabelRoute.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.red2_aspp));
        }

        holder.btnDirection.setOnClickListener(v -> {
            mContext.moveDirection(detail);
//            mContext.openDialogDirections(detail);
        });
    }

    @Override
    public int getItemCount() {
        return dataFilteredList.size();
    }

    public interface OnClickListener {
        void onClick(int pos);
    }
}

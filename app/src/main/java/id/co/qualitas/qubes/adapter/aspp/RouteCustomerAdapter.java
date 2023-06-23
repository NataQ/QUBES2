package id.co.qualitas.qubes.adapter.aspp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.aspp.RouteCustomerFragment;
import id.co.qualitas.qubes.fragment.aspp.RouteCustomerFragment;
import id.co.qualitas.qubes.model.RouteCustomer;
import id.co.qualitas.qubes.model.RouteCustomer;

public class RouteCustomerAdapter extends RecyclerView.Adapter<RouteCustomerAdapter.Holder> implements Filterable {
    private List<RouteCustomer> dataList;
    private List<RouteCustomer> dataFilteredList;
    private LayoutInflater mInflater;
    //    private OnClickListener onClickListener;
    private RouteCustomerFragment mContext;

//    public void setOnClickListener(OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }

    public RouteCustomerAdapter(RouteCustomerFragment mContext, List<RouteCustomer> dataList) {
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

    public void setData(List<RouteCustomer> mDataSet) {
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
                    List<RouteCustomer> filteredList = new ArrayList<>();
                    for (RouteCustomer row : dataList) {

                        /*filter by name*/
                        if (row.getNameCustomer().toLowerCase().contains(charString.toLowerCase())) {
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
                dataFilteredList = (ArrayList<RouteCustomer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtAddress, txtStore;
        ImageView imgDirections;

        public Holder(View itemView) {
            super(itemView);
            txtStore = itemView.findViewById(R.id.txtStore);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            imgDirections = itemView.findViewById(R.id.imgDirections);
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
        RouteCustomer detail = dataFilteredList.get(position);
        holder.txtStore.setText(detail.getIdCustomer() + " - " + detail.getNameCustomer() + " (" + detail.getMileage() + ")");
        holder.txtAddress.setText(detail.getAddressCustomer());
        holder.imgDirections.setOnClickListener(v -> {
            mContext.openDialogDirections(detail);
            Toast.makeText(mContext.getContext(), "DIREC", Toast.LENGTH_SHORT).show();
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

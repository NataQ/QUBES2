package id.co.qualitas.qubes.adapter.aspp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.model.Customer;

public class AutoCompleteRouteAdapter extends ArrayAdapter<Customer> {
    Context context;
    List<Customer> items, tempItems, suggestions;

    public AutoCompleteRouteAdapter(Context context, List<Customer> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        tempItems = new ArrayList<Customer>(items); // this makes the difference.
        suggestions = new ArrayList<Customer>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.aspp_auto_complete_item, parent, false);
        }
        Customer customer = items.get(position);
        if (customer != null) {
            TextView txtID = (TextView) view.findViewById(R.id.txtID);
            TextView textName = (TextView) view.findViewById(R.id.textName);

            if (txtID != null) txtID.setText(customer.getIdCustomer());
            if (textName != null) textName.setText(customer.getNameCustomer());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Customer) resultValue).getNameCustomer();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Customer Customer : tempItems) {
                    if (Customer.getNameCustomer().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(Customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Customer> filterList = (ArrayList<Customer>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Customer Customer : filterList) {
                    add(Customer);
                    notifyDataSetChanged();
                }
            }
        }
    };
}

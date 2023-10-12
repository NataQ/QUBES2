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
import id.co.qualitas.qubes.model.RouteCustomer;
import id.co.qualitas.qubes.model.RouteCustomer;

public class AutoCompleteRouteAdapter extends ArrayAdapter<RouteCustomer> {
    Context context;
    List<RouteCustomer> items, tempItems, suggestions;

    public AutoCompleteRouteAdapter(Context context, List<RouteCustomer> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        tempItems = new ArrayList<RouteCustomer>(items); // this makes the difference.
        suggestions = new ArrayList<RouteCustomer>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.aspp_auto_complete_item, parent, false);
        }
        RouteCustomer RouteCustomer = items.get(position);
        if (RouteCustomer != null) {
            TextView txtID = (TextView) view.findViewById(R.id.txtID);
            TextView textName = (TextView) view.findViewById(R.id.textName);

            if (txtID != null) txtID.setText(RouteCustomer.getId());
            if (textName != null) textName.setText(RouteCustomer.getNama());
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
            String str = ((RouteCustomer) resultValue).getNama();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (RouteCustomer RouteCustomer : tempItems) {
                    if (RouteCustomer.getNama().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(RouteCustomer);
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
            List<RouteCustomer> filterList = (ArrayList<RouteCustomer>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (RouteCustomer RouteCustomer : filterList) {
                    add(RouteCustomer);
                    notifyDataSetChanged();
                }
            }
        }
    };
}

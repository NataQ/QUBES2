package id.co.qualitas.qubes.adapter.aspp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.model.Reason;

public class FilteredSpinnerAllReasonAdapter extends BaseAdapter {
    Context context;
    List<Reason> mFilteredList;
    LayoutInflater inflter;

    public FilteredSpinnerAllReasonAdapter(Context applicationContext, List<Reason> mFilteredList) {
        this.context = applicationContext;
        this.mFilteredList = mFilteredList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return mFilteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.aspp_spinner_filtered_item, null);
        Reason detail = mFilteredList.get(i);
        TextView text1 = (TextView) view.findViewById(R.id.text1);
        text1.setText(detail.getDescription());
        return view;
    }
}
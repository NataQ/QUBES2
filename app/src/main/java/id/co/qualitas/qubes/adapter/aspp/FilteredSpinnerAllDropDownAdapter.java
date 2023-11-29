package id.co.qualitas.qubes.adapter.aspp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.model.DropDown;
import id.co.qualitas.qubes.model.DropDown;

public class FilteredSpinnerAllDropDownAdapter extends BaseAdapter {
    Context context;
    List<DropDown> mFilteredList;
    LayoutInflater inflater;

    public FilteredSpinnerAllDropDownAdapter(Context applicationContext, List<DropDown> mFilteredList) {
        this.context = applicationContext;
        this.mFilteredList = mFilteredList;
        inflater = (LayoutInflater.from(applicationContext));
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
        view = inflater.inflate(R.layout.aspp_spinner_filtered_item, null);
        DropDown detail = mFilteredList.get(i);
        TextView text1 = (TextView) view.findViewById(R.id.text1);
        text1.setText(detail.getValue());
        return view;
    }
}
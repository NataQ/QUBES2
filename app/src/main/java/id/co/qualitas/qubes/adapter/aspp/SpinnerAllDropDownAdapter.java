package id.co.qualitas.qubes.adapter.aspp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.model.DropDown;
import id.co.qualitas.qubes.model.Material;

public class SpinnerAllDropDownAdapter extends ArrayAdapter<DropDown> {
    Context context;
    List<DropDown> mFilteredList;
    LayoutInflater inflater;
    private OnAdapterListener onAdapterListener;

    public SpinnerAllDropDownAdapter(Context applicationContext, List<DropDown> mFilteredList, OnAdapterListener onAdapterListener) {
        super(applicationContext, 0, mFilteredList);
        this.context = applicationContext;
        this.mFilteredList = mFilteredList;
        this.onAdapterListener = onAdapterListener;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return mFilteredList.size();
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
        onAdapterListener.onAdapterClick(mFilteredList.get(i), i);
        return view;
    }

    public interface OnAdapterListener {
        void onAdapterClick(DropDown dp, int pos);
    }
}
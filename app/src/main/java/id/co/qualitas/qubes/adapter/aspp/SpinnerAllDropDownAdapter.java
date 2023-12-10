package id.co.qualitas.qubes.adapter.aspp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.model.DropDown;
import id.co.qualitas.qubes.model.Material;

public class SpinnerAllDropDownAdapter extends ArrayAdapter<DropDown> {
    Context context;
    List<DropDown> mFilteredList;
    LayoutInflater inflater;

    public SpinnerAllDropDownAdapter(Context applicationContext, List<DropDown> mFilteredList) {
        super(applicationContext, 0, mFilteredList);
        this.context = applicationContext;
        this.mFilteredList = mFilteredList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.aspp_spinner_filtered_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.text1);
        DropDown currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.setText(currentItem.getValue());
        }

        return convertView;
    }

//    @Override
//    public int getCount() {
//        return mFilteredList.size();
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        if(view == null){
//            view = inflater.inflate(R.layout.aspp_spinner_filtered_item, viewGroup, false);
//        }
//
//        DropDown detail = mFilteredList.get(i);
//        TextView text1 = (TextView) view.findViewById(R.id.text1);
//        text1.setText(detail.getValue());
//        onAdapterListener.onAdapterClick(mFilteredList.get(i), i);
//        return view;
//    }
//    public interface OnAdapterListener {
//        void onAdapterClick(DropDown dp, int pos);
//    }
}
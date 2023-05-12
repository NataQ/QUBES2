package id.co.qualitas.qubes.adapter;

import android.annotation.SuppressLint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.CalendarFragment;
import id.co.qualitas.qubes.model.OutletResponse;

public class NewOutletPAdapter extends RecyclerView.Adapter<NewOutletPAdapter.DataObjectHolder> {
    private ArrayList<OutletResponse> mDataset = new ArrayList<>();
    private CalendarFragment mContext;
    private CalendarFragment.OnRecyclerViewItemClickListener listener;
    private SparseBooleanArray selectedItems;
    private boolean curSelected;
    private int curPosition = -1;

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtClient, index, txtIdOutlet;
        ImageView imgArrow;
        ConstraintLayout row;
        CardView parentView;

        DataObjectHolder(View itemView, CalendarFragment context) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtClient = itemView.findViewById(R.id.txtClient);
            index = itemView.findViewById(R.id.index);
            imgArrow = itemView.findViewById(R.id.imgArrow);
            parentView = itemView.findViewById(R.id.item_layout);
            row = itemView.findViewById(R.id.constraint);
            txtIdOutlet = itemView.findViewById(R.id.txtIdOutlet);
            parentView.setSelected(false);

        }

        @Override
        public void onClick(View v) {
            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getPosition());
                parentView.setSelected(false);
            } else {
                selectedItems.put(getAdapterPosition(), true);
                parentView.setSelected(true);
            }
        }


    }

    public void setOnItemClickListener(CalendarFragment.OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public NewOutletPAdapter(ArrayList<OutletResponse> myDataset, CalendarFragment calendarFragment) {
        mDataset = myDataset;
        mContext = calendarFragment;
    }

    public NewOutletPAdapter(ArrayList<OutletResponse> myDataset, CalendarFragment calendarFragment, boolean selected) {
        mDataset = myDataset;
        mContext = calendarFragment;
        curSelected = selected;
    }

    public NewOutletPAdapter(ArrayList<OutletResponse> myDataset, CalendarFragment calendarFragment, int position) {
        mDataset = myDataset;
        mContext = calendarFragment;
        curPosition = position;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_outlet_home, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view, mContext);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.index.setText(String.valueOf(position + 1));
//        holder.txtClient.setText(mDataset.get(position).getStreet1());
        holder.txtClient.setText(mDataset.get(position).getOutletName());
        holder.txtIdOutlet.setText(mDataset.get(position).getIdOutlet());
                //.concat(" - ").concat(mDataset.get(position).getOutletName()));

        if (mDataset.get(position).isColor()) {
            holder.row.setBackgroundColor(mContext.getResources().getColor(R.color.new_blue2));
            holder.txtClient.setTextColor(mContext.getResources().getColor(R.color.new_blue1));
            holder.txtIdOutlet.setTextColor(mContext.getResources().getColor(R.color.grey3));
            holder.imgArrow.setVisibility(View.INVISIBLE);
        } else {
            holder.row.setBackgroundColor(0x00000000);
            holder.txtClient.setTextColor(mContext.getResources().getColor(R.color.new_blue1));
            holder.txtIdOutlet.setTextColor(mContext.getResources().getColor(R.color.grey3));
            holder.imgArrow.setVisibility(View.VISIBLE);
        }

        if(curSelected){
            holder.row.setBackgroundColor(mContext.getResources().getColor(R.color.new_blue2));
            holder.txtClient.setTextColor(mContext.getResources().getColor(R.color.new_blue1));
            holder.txtIdOutlet.setTextColor(mContext.getResources().getColor(R.color.grey3));
            holder.imgArrow.setVisibility(View.INVISIBLE);
        }else{
            holder.row.setBackgroundColor(0x00000000);
            holder.txtClient.setTextColor(mContext.getResources().getColor(R.color.new_blue1));
            holder.txtIdOutlet.setTextColor(mContext.getResources().getColor(R.color.grey3));
            holder.imgArrow.setVisibility(View.VISIBLE);
        }

        if(curPosition > -1){
            if(position == curPosition){
                holder.row.setBackgroundColor(mContext.getResources().getColor(R.color.new_blue2));
                holder.txtClient.setTextColor(mContext.getResources().getColor(R.color.new_blue1));
                holder.txtIdOutlet.setTextColor(mContext.getResources().getColor(R.color.grey3));
                holder.imgArrow.setVisibility(View.INVISIBLE);
            }else{
                holder.row.setBackgroundColor(0x00000000);
                holder.txtClient.setTextColor(mContext.getResources().getColor(R.color.new_blue1));
                holder.txtIdOutlet.setTextColor(mContext.getResources().getColor(R.color.grey3));
                holder.imgArrow.setVisibility(View.VISIBLE);
            }
        }

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.row.setBackgroundColor(mContext.getResources().getColor(R.color.new_blue2));
                listener.onRecyclerViewItemClicked(position, Integer.parseInt(String.valueOf(getItemId(position))));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }



}
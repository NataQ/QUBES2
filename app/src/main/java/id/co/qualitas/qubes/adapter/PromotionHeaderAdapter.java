package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.PromotionFragment;
import id.co.qualitas.qubes.fragment.PromotionHeaderFragment;
import id.co.qualitas.qubes.helper.GPSTracker;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.User;

public class PromotionHeaderAdapter extends RecyclerView.Adapter<PromotionHeaderAdapter.DataObjectHolder> {
    private List<OutletResponse> mDataset;
    private static MyClickListener myClickListener;
    private PromotionHeaderFragment mcontext;
    private GPSTracker gpsTracker;
    private DatabaseHelper db;
    private int wall = 0;

    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final PromotionHeaderFragment mContext;
        TextView txtOutletName, txtOutletCode, txtOutletContact, line;
        RelativeLayout LLOutlets;
        ImageView outletStatus;
        User user = new User();

        public DataObjectHolder(View itemView, PromotionHeaderFragment context) {
            super(itemView);
            mContext = context;
            gpsTracker = new GPSTracker(mcontext.getContext());
            itemView.setOnClickListener(this);
            txtOutletName = (TextView) itemView.findViewById(R.id.txtOutletName);
            txtOutletCode = (TextView) itemView.findViewById(R.id.txtOutetCode);
            txtOutletContact = (TextView) itemView.findViewById(R.id.txtContact);
            LLOutlets = (RelativeLayout) itemView.findViewById(R.id.LLOutlets);
            user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public PromotionHeaderAdapter(ArrayList<OutletResponse> myDataset, PromotionHeaderFragment promotionHeaderFragment) {
        mDataset = myDataset;
        mcontext = promotionHeaderFragment;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_outlet_promotion, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view, mcontext);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final OutletResponse outletResponse = mDataset.get(position);
        db = new DatabaseHelper(mcontext.getContext());

        holder.txtOutletName.setText(outletResponse.getOutletName());
        holder.txtOutletCode.setText(outletResponse.getIdOutlet());
        holder.txtOutletContact.setText(outletResponse.getCustomerName());


        holder.LLOutlets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.setItemParam(Constants.OUTLET, outletResponse);

                Fragment fr = new PromotionFragment();
                mcontext.setContent(fr);
            }

        });
    }

    public void addItem(OutletResponse dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}


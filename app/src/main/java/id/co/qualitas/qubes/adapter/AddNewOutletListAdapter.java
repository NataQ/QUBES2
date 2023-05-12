package id.co.qualitas.qubes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.fragment.OrderPlanDetailFragmentV2;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.VisitDateResponse;

public class AddNewOutletListAdapter extends RecyclerView.Adapter<AddNewOutletListAdapter.MyViewHolder> {

    private List<VisitDateResponse> visitDateResponseList = null;
    private ArrayList<OutletResponse> visitDateResponseArrayList = new ArrayList<>();
    private BaseFragment.OnRecyclerViewItemClickListener listener;
    private Context context;
    private BaseFragment mcontext;
    private OrderPlanDetailFragmentV2 nContext;
    Integer posClicked = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namaOutlet, kodeOutlet;
        public LinearLayout parentView;
        public ImageView imgChecked;

        public MyViewHolder(View view) {
            super(view);
            //name, namecode, purchaseDate, amount, typeInvoice
            namaOutlet = view.findViewById(R.id.namaOutlet);
            imgChecked = view.findViewById(R.id.imgChecked);
            parentView = view.findViewById(R.id.ll);
        }
    }


//    public AddNewOutletListAdapter(ArrayList<OutletResponse> visitDateResponseArrayList, BaseFragment mcontext) {
//        this.visitDateResponseArrayList = visitDateResponseArrayList;
//        this.mcontext = mcontext;
//    }

    public AddNewOutletListAdapter(ArrayList<OutletResponse> visitDateResponseArrayList, Context context) {
        this.visitDateResponseArrayList = visitDateResponseArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_outlet_order_search, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OutletResponse outletResponse = visitDateResponseArrayList.get(position);
        holder.namaOutlet.setText(Helper.validateResponseEmpty(outletResponse.getIdOutlet())
                .concat(Constants.STRIP)
                .concat(Helper.validateResponseEmpty(outletResponse.getOutletName())));
//        holder.kodeOutlet.setText(outletResponse.getIdOutlet());
        if (outletResponse.isClicked()) {
//            if (nContext != null) {
                holder.namaOutlet.setTextColor(ContextCompat.getColor(context, R.color.new_blue));
//            } else if (mcontext != null) {
//                holder.namaOutlet.setTextColor(ContextCompat.getColor(context.getActivity(), R.color.new_blue));
//            }
            holder.imgChecked.setVisibility(View.VISIBLE);
        } else {
//            if (nContext != null) {
                holder.namaOutlet.setTextColor(ContextCompat.getColor(context, R.color.darkGray));
//            } else if (mcontext != null) {
//                holder.namaOutlet.setTextColor(ContextCompat.getColor(mcontext.getActivity(), R.color.darkGray));
//            }
            holder.imgChecked.setVisibility(View.GONE);
        }

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posClicked != null) {
                    visitDateResponseArrayList.get(posClicked).setClicked(false);
                    notifyItemChanged(posClicked);
                }
                posClicked = position;
                visitDateResponseArrayList.get(0).setPosClicked(posClicked);
                outletResponse.setClicked(true);
                notifyItemChanged(posClicked);
            }
        });
    }


    @Override
    public int getItemCount() {
        return visitDateResponseArrayList == null ? 0 : visitDateResponseArrayList.size();
    }

    public void setOnItemClickListener(BaseFragment.OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }
}



package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.TargetResponse;

public class ObjectTargetAdapterv2 extends RecyclerView.Adapter<ObjectTargetAdapterv2.MyViewHolder> {
    private ArrayList<TargetResponse> objectTargetList;
    private String produk, outlet, type;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewKlasifikasi, txtViewMonth, txtViewCode, txtViewName, txtViewTarget, txtViewActual, txtViewGap;
        public TextView txtGap, txtTarget;
        private ConstraintLayout layoutHeader;

        public MyViewHolder(View view) {
            super(view);
            txtViewKlasifikasi = view.findViewById(R.id.txtViewClassification);
            txtViewMonth = view.findViewById(R.id.txtViewMonth);
            txtViewCode = view.findViewById(R.id.txtViewCode);
            txtViewName = view.findViewById(R.id.txtViewName);
            txtViewTarget = view.findViewById(R.id.txtViewTarget);
            txtViewActual = view.findViewById(R.id.txtViewActual);
            txtViewGap = view.findViewById(R.id.txtViewGap);
            layoutHeader = view.findViewById(R.id.layoutHeader);

            txtGap = view.findViewById(R.id.textView32);
            txtTarget = view.findViewById(R.id.textView30);
        }
    }

    public ObjectTargetAdapterv2(ArrayList<TargetResponse> objectTargetList, String produk, String outlet, String type) {
        this.objectTargetList = objectTargetList;
        this.outlet = outlet;
        this.produk = produk;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_object_target_v2, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TargetResponse targetResponse = objectTargetList.get(position);
        if(!produk.equals("0")){
            holder.txtTarget.setVisibility(View.GONE);
            holder.txtGap.setVisibility(View.GONE);
            holder.txtViewTarget.setVisibility(View.GONE);
            holder.txtViewGap.setVisibility(View.GONE);
        }else{
            holder.txtTarget.setVisibility(View.VISIBLE);
            holder.txtGap.setVisibility(View.VISIBLE);
            holder.txtViewTarget.setVisibility(View.VISIBLE);
            holder.txtViewGap.setVisibility(View.VISIBLE);
        }
        holder.txtViewTarget.setText(Helper.toRupiahFormat(String.valueOf(targetResponse.getTarget())));
        holder.txtViewActual.setText(Helper.toRupiahFormat(String.valueOf(targetResponse.getActual())));


        holder.txtViewKlasifikasi.setText(targetResponse.getMatClass());
        holder.txtViewCode.setText(targetResponse.getIdOutlet());
        holder.txtViewName.setText(targetResponse.getOutletName());

        holder.txtViewGap.setText(String.valueOf(targetResponse.getGap()));
    }

    @Override
    public int getItemCount() {
        return objectTargetList.size();
    }
}
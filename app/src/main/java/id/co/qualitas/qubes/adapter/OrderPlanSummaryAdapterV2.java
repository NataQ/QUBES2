package id.co.qualitas.qubes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.OrderPlanSummaryFragmentV2;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.MaterialResponse;

public class OrderPlanSummaryAdapterV2 extends RecyclerView.Adapter<OrderPlanSummaryAdapterV2.MyViewHolder> {
    private List<MaterialResponse> listMaterial;
    private OrderPlanSummaryFragmentV2 mcontext;
    private int selectedIndex;
    private boolean onBind;
    private Boolean selected = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView row;
        private TextView index, txtIdMaterial, txtMaterialName, txtViewTop;
        private EditText edtQty1, edtQty2, edtPrice;
        private Spinner spnUom1, spnUom2;
        private ImageView icDelete, btnGetPrice;
        private Spinner spinnerJenisJual;

        public MyViewHolder(View view) {
            super(view);
            row = view.findViewById(R.id.row);
            index = view.findViewById(R.id.txtIndex);
            txtIdMaterial = view.findViewById(R.id.txtIdMaterial);
            txtMaterialName = view.findViewById(R.id.txtMaterialName);
            edtQty1 = view.findViewById(R.id.edtTxtQty1);
            spnUom1 = view.findViewById(R.id.q1Type);
            edtQty2 = view.findViewById(R.id.edtTxtQty2);
            spnUom2 = view.findViewById(R.id.q2Type);
            edtPrice = view.findViewById(R.id.edtPrice);
//            txtViewTop = view.findViewById(R.id.txtViewTop);
//            txtViewTop.setVisibility(View.GONE);

//            icDelete = view.findViewById(R.id.ic_del);
//            spinnerJenisJual = view.findViewById(R.id.spinnerJenisJual);
//            spinnerJenisJual.setVisibility(View.GONE);
//            btnGetPrice = view.findViewById(R.id.ic_attach);
//            btnGetPrice.setVisibility(View.GONE);
//            icDelete.setVisibility(View.GONE);
        }
    }

    public OrderPlanSummaryAdapterV2(List<MaterialResponse> listMaterial, OrderPlanSummaryFragmentV2 context) {
        this.listMaterial = listMaterial;
        this.mcontext = context;
        selectedIndex = -1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_order_plan_summary_v3_new, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MaterialResponse material = listMaterial.get(holder.getAdapterPosition());

        holder.row.setId(holder.getAdapterPosition());
        holder.index.setId(holder.getAdapterPosition());
        holder.txtIdMaterial.setId(holder.getAdapterPosition());
        holder.txtMaterialName.setId(holder.getAdapterPosition());
        holder.edtQty1.setId(holder.getAdapterPosition());
        holder.spnUom1.setId(holder.getAdapterPosition());
        holder.edtQty2.setId(holder.getAdapterPosition());
        holder.spnUom2.setId(holder.getAdapterPosition());
        holder.edtPrice.setId(holder.getAdapterPosition());

        holder.edtQty1.setEnabled(false);
        holder.spnUom1.setEnabled(false);
        holder.edtQty2.setEnabled(false);
        holder.spnUom2.setEnabled(false);
        holder.edtPrice.setEnabled(false);

        /*Set Data*/
        holder.index.setText(String.valueOf(holder.getAdapterPosition() + 1));
        material.setIndex(String.valueOf(holder.getAdapterPosition() + 1));
        holder.txtIdMaterial.setText(material.getIdMaterial());
        holder.txtMaterialName.setText(material.getMaterialName());
        if (material.getNewQty1() != null) {
            holder.edtQty1.setText(Helper.toRupiahFormat2(String.valueOf(material.getNewQty1())));
        }
        if (material.getNewQty2() != null) {
            holder.edtQty2.setText(Helper.toRupiahFormat2(String.valueOf(material.getNewQty2())));
        }
        if (material.getPrice() != null) {
            holder.edtPrice.setText(Helper.toRupiahFormat2(String.valueOf(material.getPrice())));
        }
        List<String> temp = new ArrayList<>();

        if (material.getListUomName() != null) {
            if (material.getListUomName().size() != 0) {
                for (int i = 0; i < material.getListUomName().size(); i++) {
                    temp.add(material.getListUomName().get(i).getUomName());
                }
                String[] listUom = new String[temp.size()];
                listUom = temp.toArray(listUom);

                mcontext.setSpinnerAdapter3(listUom, holder.spnUom1);
            }
        }

        if (material.getNewUom1() != null) {
            int spinnerPosition = mcontext.getSpinnerAdapter().getPosition(String.valueOf(material.getNewUom1()));
            holder.spnUom1.setSelection(spinnerPosition);
        }
        if (material.getNewQty2() != null && material.getNewUom2() != null) {
            int spinnerPosition = mcontext.getSpinnerAdapter().getPosition(String.valueOf(material.getNewUom2()));
            holder.spnUom2.setSelection(spinnerPosition);
        }

        /*Brain*/
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedIndex == holder.getAdapterPosition()) {
                    setSelectedIndex(-1);
                    mcontext.viewDetailData(material, -1);
                } else {
                    setSelectedIndex(holder.getAdapterPosition());
                    mcontext.viewDetailData(material, holder.getAdapterPosition());
                }
            }
        });

        if (selectedIndex == holder.getAdapterPosition()) {
            holder.row.setSelected(true);
        } else {
            holder.row.setSelected(false);
        }

        holder.spnUom1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String uom1 = holder.spnUom1.getSelectedItem().toString();
//                String uom1 = holder.spnUom1.getText().toString();
                List<String> temp = new ArrayList<>();
                for (int i = 0; i < material.getListUomName().size(); i++) {
                    if (material.getListUomName().get(pos).getConversion() > material.getListUomName().get(i).getConversion()) {
                        temp.add(material.getListUomName().get(i).getUomName());
                    }
                }
                if (temp.size() != 0) {
                    if (material.getNewQty2() != null) {
                        String[] listUom = new String[temp.size()];
                        listUom = temp.toArray(listUom);
                        mcontext.setSpinnerAdapter3(listUom, holder.spnUom2);
                    }
                } else {
                    holder.spnUom2.setAdapter(null);
                    holder.spnUom2.setEnabled(false);
                    holder.edtQty2.setEnabled(false);
                    holder.edtQty2.setText(null);
                }
                material.setNewUom1(uom1);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setSelectedIndex(int ind) {
        selectedIndex = ind;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listMaterial == null ? 0 : listMaterial.size();
    }

}
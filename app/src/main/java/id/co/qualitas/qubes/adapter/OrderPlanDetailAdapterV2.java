package id.co.qualitas.qubes.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.OrderPlanDetailFragmentV2;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.MaterialResponse;

public class OrderPlanDetailAdapterV2 extends RecyclerView.Adapter<OrderPlanDetailAdapterV2.MyViewHolder> {
    private List<MaterialResponse> listMaterial;
    private OrderPlanDetailFragmentV2 mcontext;
    private int selectedIndex;
    private ArrayAdapter<String> adapter;
    List<String> listUom = new ArrayList<>();

    public OrderPlanDetailAdapterV2(List<MaterialResponse> listMaterial, OrderPlanDetailFragmentV2 mcontext) {
        this.listMaterial = listMaterial;
        this.mcontext = mcontext;
        selectedIndex = -1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView row;
        private TextView index, txtIdMaterial, txtMaterialName, txtViewTop;
        private EditText edtQty1, edtQty2, edtPrice;
        //        private Spinner spnUom1, spnUom2;
        private AutoCompleteTextView spnUom1, spnUom2;
        private PriceEditTextListener priceListener;
        private QtyEditTextListener qtyListener1, qtyListener2;
        private UomSpinnerListener uomSpinnerListener1, uomSpinnerListener2;
        private boolean isSelectionSpn1, isSelectionSpn2;
        private int posSelection1, posSelection2;

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

            priceListener = new PriceEditTextListener(edtPrice);
            edtPrice.addTextChangedListener(priceListener);

            qtyListener1 = new QtyEditTextListener(edtQty1, 1);
            qtyListener2 = new QtyEditTextListener(edtQty2, 2);

            edtQty1.addTextChangedListener(qtyListener1);
            edtQty2.addTextChangedListener(qtyListener2);

            row.setOnClickListener(this);

//            mcontext.setSpinnerAdapter3(new ArrayList<String>(), spnUom1);
            mcontext.setAutoCompleteAdapter(new ArrayList<String>(), spnUom1);
//            mcontext.setSpinnerAdapter3(new ArrayList<String>(), spnUom2);
            mcontext.setAutoCompleteAdapter(new ArrayList<String>(), spnUom2);

            uomSpinnerListener1 = new UomSpinnerListener(1);
            spnUom1.setOnItemClickListener(uomSpinnerListener1);
//            spnUom1.setOnItemSelectedListener(uomSpinnerListener1);

            uomSpinnerListener2 = new UomSpinnerListener(2);
//            spnUom2.setOnItemSelectedListener(uomSpinnerListener2);
            spnUom2.setOnItemClickListener(uomSpinnerListener2);

            posSelection1 = -1;
            posSelection2 = -1;
        }

        @Override
        public void onClick(View v) {
            MaterialResponse material = listMaterial.get(getAdapterPosition());
            if (selectedIndex == getAdapterPosition()) {
                setSelectedIndex(-1);
                material.setSelected(false);
                mcontext.viewDetailData(material, -1);
            } else {
                setSelectedIndex(getAdapterPosition());
                material.setSelected(true);
                mcontext.viewDetailData(material, getAdapterPosition());
            }
            notifyDataSetChanged();
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_order_plan_detail_v3_new, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.isSelectionSpn1 = false;
        holder.isSelectionSpn2 = false;

        final MaterialResponse material = listMaterial.get(holder.getBindingAdapterPosition());
        holder.priceListener.updatePosition(holder.getBindingAdapterPosition());
        holder.qtyListener1.updatePosition(holder.getAdapterPosition());
        holder.qtyListener2.updatePosition(holder.getAdapterPosition());
        holder.uomSpinnerListener1.updatePosition(holder.getAdapterPosition(), holder);
        holder.uomSpinnerListener2.updatePosition(holder.getAdapterPosition(), holder);

        holder.index.setText(String.valueOf(holder.getAdapterPosition() + 1) + ".");
        material.setIndex(String.valueOf(holder.getAdapterPosition() + 1));
        holder.txtIdMaterial.setText(material.getIdMaterial());
        holder.txtMaterialName.setText(material.getMaterialName());

        if (material.isSelected() != null) {
            holder.row.setSelected(material.isSelected());
            holder.row.setPressed(material.isSelected());
        }

        if (material.getNewQty1() != null) {
            if (!material.getNewQty1().equals(BigDecimal.ZERO)) {
                holder.edtQty1.setText(String.valueOf(material.getNewQty1()));
            }
        }
        if (material.getNewQty2() != null) {
            if (!material.getNewQty2().equals(BigDecimal.ZERO)) {
                holder.edtQty2.setText(String.valueOf(material.getNewQty2()));
            }
        }
        if (material.getPrice() != null) {
            if (!material.getPrice().equals(BigDecimal.ZERO)) {
                holder.edtPrice.setText(Helper.toRupiahFormat2(String.valueOf(material.getPrice())));
            }
        }

        if (material.getListUomName() != null) {
            List<String> temp = new ArrayList<>();
            if (material.getListUomName().size() != 0) {
                for (int i = 0; i < material.getListUomName().size(); i++) {
                    temp.add(material.getListUomName().get(i).getUomName());
                }
                listUom = new ArrayList<>();
                listUom.addAll(temp);
//                updateSpinner(holder.spnUom1, temp);
                mcontext.setAutoCompleteAdapter(temp, holder.spnUom1);
            }
        }

        if (material.getNewUom1() != null) {
            if (!mcontext.getSpinnerAdapter().isEmpty()) {
                int spinnerPosition = mcontext.getSpinnerAdapter().getPosition(String.valueOf(material.getNewUom1()));
                setSelectedSpn1(spinnerPosition, holder, String.valueOf(material.getNewUom1()));
            } else if (!adapter.isEmpty()) {
                int spinnerPosition = adapter.getPosition(String.valueOf(material.getNewUom1()));
                setSelectedSpn1(spinnerPosition, holder, String.valueOf(material.getNewUom1()));
            }
        }

        if (material.getNewUom2() != null) {
            if (!mcontext.getSpinnerAdapter().isEmpty()) {
                int spinnerPosition = mcontext.getSpinnerAdapter().getPosition(String.valueOf(material.getNewUom2()));
                setSelectedSpn2(spinnerPosition, holder, String.valueOf(material.getNewUom2()));
            } else if (!adapter.isEmpty()) {
//                int spinnerPosition = adapter.getPosition(String.valueOf(material.getNewUom1()));
                int spinnerPosition = adapter.getPosition(String.valueOf(material.getNewUom2()));
//                setSelectedSpn1(spinnerPosition, holder);
                setSelectedSpn2(spinnerPosition, holder, String.valueOf(material.getNewUom2()));
            }
        }

        if (selectedIndex == holder.getAdapterPosition()) {
            holder.row.setSelected(true);
        } else {
            holder.row.setSelected(false);
        }

        holder.edtQty1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    material.setPos(holder.getAdapterPosition());
                    Helper.setItemParam(Constants.MATERIAL_DETAIL, material);
//                    mcontext.requestPriceN();
                    return true;
                }
                return false;
            }
        });

        holder.edtQty2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    material.setPos(holder.getAdapterPosition());
                    Helper.setItemParam(Constants.MATERIAL_DETAIL, material);
//                    mcontext.requestPriceN();
                    return true;
                }
                return false;
            }
        });
        holder.edtQty1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    material.setPos(holder.getAdapterPosition());
                    Helper.setItemParam(Constants.MATERIAL_DETAIL, material);
//                    mcontext.requestPriceN();
                }
                return false;
            }
        });

        holder.edtQty2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    material.setPos(holder.getAdapterPosition());
                    Helper.setItemParam(Constants.MATERIAL_DETAIL, material);
//                    mcontext.requestPriceN();
                }
                return false;
            }
        });

        holder.spnUom1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                holder.isSelectionSpn1 = false;
                return false;
            }
        });

        holder.spnUom2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                holder.isSelectionSpn2 = false;
                return false;
            }
        });
    }

    private void setSelectedIndex(int ind) {
        selectedIndex = ind;
    }

    @Override
    public int getItemCount() {
        return listMaterial.size();
    }

    public int getItemViewType(int position) {
        return position;
    }

    private class PriceEditTextListener implements TextWatcher {
        MaterialResponse materialResponse;
        MyViewHolder holder;
        EditText edt;

        PriceEditTextListener(EditText edt) {
            this.edt = edt;
        }

        void updatePosition(int position) {
            this.materialResponse = listMaterial.get(position);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            Helper.setDotCurrency(edt, this, editable);
            if (editable.length() > 0) {
                materialResponse.setPrice(new BigDecimal(editable.toString().replace(Constants.COMA, Constants.EMPTY_STRING)));
            } else {
                materialResponse.setPrice(new BigDecimal(0));
            }
        }
    }

    private class QtyEditTextListener implements TextWatcher {
        MaterialResponse materialResponse;
        private final int type;
        private EditText edt;

        QtyEditTextListener(EditText edt, int type) {
            this.edt = edt;
            this.type = type;
        }

        void updatePosition(int position) {
            this.materialResponse = listMaterial.get(position);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (type == 1) {
                if (charSequence.length() > 0) {
                    if (charSequence.toString().contains(Constants.COMA)) {
                        materialResponse.setNewQty1(new BigDecimal(charSequence.toString().replace(Constants.COMA, Constants.EMPTY_STRING)));
                    }
                    materialResponse.setDeleted(false);
                } else {
                    materialResponse.setDeleted(true);
                    materialResponse.setNewQty1(new BigDecimal(0));
                }
            } else if (type == 2) {
                if (charSequence.length() > 0) {
                    if (charSequence.toString().contains(Constants.COMA)) {
                        materialResponse.setNewQty2(new BigDecimal(charSequence.toString().replace(Constants.COMA, Constants.EMPTY_STRING)));
                    }
                } else {
                    materialResponse.setNewQty2(new BigDecimal(0));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Helper.setDotCurrency(edt, this, editable);
            if (type == 1) {
                if (editable.length() > 0) {
                    materialResponse.setNewQty1(new BigDecimal(editable.toString().replace(Constants.COMA, Constants.EMPTY_STRING)));
                    materialResponse.setDeleted(false);
                } else {
                    materialResponse.setDeleted(true);
                }
            } else if (type == 2) {
                if (editable.length() > 0) {
                    materialResponse.setNewQty2(new BigDecimal(editable.toString().replace(Constants.COMA, Constants.EMPTY_STRING)));
                    materialResponse.setDeleted(false);
                } else {
                    materialResponse.setDeleted(true);
                }
            }
        }
    }

    private class UomSpinnerListener implements AdapterView.OnItemClickListener {
        private final int type;
        private MaterialResponse materialResponse;
        private MyViewHolder holder;

        UomSpinnerListener(int type) {
            this.type = type;
        }

        private void updatePosition(int posAdapter, MyViewHolder holder) {
            this.materialResponse = listMaterial.get(posAdapter);
            this.holder = holder;
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            if (type == 1) {
                boolean b = holder.isSelectionSpn1;
                holder.isSelectionSpn1 = false;
                holder.isSelectionSpn2 = false;
                if (b || holder.posSelection1 == pos) return;
                holder.posSelection1 = pos;

                String uom1 = holder.spnUom1.getText().toString();
                List<String> temp = new ArrayList<>();

                for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
                    if (materialResponse.getListUomName().get(pos).getConversion() > materialResponse.getListUomName().get(i).getConversion()) {
                        temp.add(materialResponse.getListUomName().get(i).getUomName());
                    }
                }
                if (temp.size() != 0) {
//                    updateSpinner(holder.spnUom2, temp);
                    mcontext.setAutoCompleteAdapter(temp, holder.spnUom2);
                    holder.spnUom2.setEnabled(true);
                    holder.edtQty2.setEnabled(true);
                    holder.spnUom2.setVisibility(View.VISIBLE);
                } else {
                    holder.spnUom2.setVisibility(View.INVISIBLE);
                    holder.spnUom2.setEnabled(false);
                    holder.edtQty2.setEnabled(false);
                    holder.edtQty2.setText(null);
                    materialResponse.setNewQty2(new BigDecimal(0));
                    materialResponse.setNewUom2(null);
                }
                materialResponse.setNewUom1(uom1);

                if (materialResponse.getNewQty1() != null) {
                    if (!materialResponse.getNewQty1().equals(BigDecimal.ZERO)) {
                        Helper.setItemParam(Constants.MATERIAL_DETAIL, materialResponse);
                        mcontext.requestPriceN();
                        mcontext.viewDetailData(materialResponse, pos);
                    }
                }
            } else {
                boolean b = holder.isSelectionSpn2;
                holder.isSelectionSpn1 = false;
                holder.isSelectionSpn2 = false;
                if (b || holder.posSelection2 == pos) return;
                holder.posSelection2 = pos;

                String uom = holder.spnUom2.getText().toString();
                materialResponse.setNewUom2(uom);

                if (materialResponse.getNewQty1() != null) {
                    if (!materialResponse.getNewQty1().equals(BigDecimal.ZERO)) {
                        Helper.setItemParam(Constants.MATERIAL_DETAIL, materialResponse);
                        mcontext.requestPriceN();
                        mcontext.viewDetailData(materialResponse, pos);
                    }
                }
            }
        }
    }

//    private class UomSpinnerListener implements AdapterView.OnItemSelectedListener {
//        private final int type;
//        private MaterialResponse materialResponse;
//        private MyViewHolder holder;
//
//        UomSpinnerListener(int type) {
//            this.type = type;
//        }
//
//        private void updatePosition(int posAdapter, MyViewHolder holder) {
//            this.materialResponse = listMaterial.get(posAdapter);
//            this.holder = holder;
//        }
//
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//            if (type == 1) {
//                boolean b = holder.isSelectionSpn1;
//                holder.isSelectionSpn1 = false;
//                holder.isSelectionSpn2 = false;
//                if (b || holder.posSelection1 == pos) return;
//                holder.posSelection1 = pos;
//
//                String uom1 = holder.spnUom1.getText().toString();
//                List<String> temp = new ArrayList<>();
//
//                for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
//                    if (materialResponse.getListUomName().get(pos).getConversion() > materialResponse.getListUomName().get(i).getConversion()) {
//                        temp.add(materialResponse.getListUomName().get(i).getUomName());
//                    }
//                }
//                if (temp.size() != 0) {
////                    updateSpinner(holder.spnUom2, temp);
//                    mcontext.setAutoCompleteAdapter(temp, holder.spnUom2);
//                    holder.spnUom2.setEnabled(true);
//                    holder.edtQty2.setEnabled(true);
//                    holder.spnUom2.setVisibility(View.VISIBLE);
//                } else {
//                    holder.spnUom2.setVisibility(View.INVISIBLE);
//                    holder.spnUom2.setEnabled(false);
//                    holder.edtQty2.setEnabled(false);
//                    holder.edtQty2.setText(null);
//                    materialResponse.setNewQty2(new BigDecimal(0));
//                    materialResponse.setNewUom2(null);
//                }
//                materialResponse.setNewUom1(uom1);
//
//                if (materialResponse.getNewQty1() != null) {
//                    if (!materialResponse.getNewQty1().equals(BigDecimal.ZERO)) {
//                        Helper.setItemParam(Constants.MATERIAL_DETAIL, materialResponse);
//                        mcontext.requestPriceN();
//                        mcontext.viewDetailData(materialResponse, pos);
//                    }
//                }
//            } else {
//                boolean b = holder.isSelectionSpn2;
//                holder.isSelectionSpn1 = false;
//                holder.isSelectionSpn2 = false;
//                if (b || holder.posSelection2 == pos) return;
//                holder.posSelection2 = pos;
//
//                String uom = holder.spnUom2.getText().toString();
//                materialResponse.setNewUom2(uom);
//
//                if (materialResponse.getNewQty1() != null) {
//                    if (!materialResponse.getNewQty1().equals(BigDecimal.ZERO)) {
//                        Helper.setItemParam(Constants.MATERIAL_DETAIL, materialResponse);
//                        mcontext.requestPriceN();
//                        mcontext.viewDetailData(materialResponse, pos);
//                    }
//                }
//            }
//        }
//
//        public void onNothingSelected(AdapterView<?> parent) {
//        }
//    }

    private void setSelectedSpn1(int spnPos, MyViewHolder holder, String s) {
        holder.isSelectionSpn1 = true;
//        holder.spnUom1.setSelection(spnPos);
        holder.spnUom1.setText(s);
        mcontext.setAutoCompleteAdapter(listUom, holder.spnUom1);
    }

    private void setSelectedSpn2(int spnPos, MyViewHolder holder, String s) {
        holder.isSelectionSpn2 = true;
//        holder.spnUom2.setSelection(spnPos);
        holder.spnUom2.setText(s);
        mcontext.setAutoCompleteAdapter(listUom, holder.spnUom2);
    }

    private void updateSpinner(Spinner spn, List<String> data) {
        adapter = (ArrayAdapter<String>) spn.getAdapter();
        adapter.clear();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }

}

package id.co.qualitas.qubes.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.fragment.StoreCheckFragment;
import id.co.qualitas.qubes.model.MaterialResponse;

public class CreateStoreCheckAdapter extends BaseAdapter {
    private ArrayList<MaterialResponse> materialResponseArrayList;
    private static LayoutInflater inflater = null;
    private StoreCheckFragment mContext;
    private LayoutInflater mInflater;
    private String[] listUom;

    public CreateStoreCheckAdapter(StoreCheckFragment mContext, ArrayList<MaterialResponse> results) {
        materialResponseArrayList = results;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext.getContext());
    }

    public int getCount() {
        return materialResponseArrayList == null ? 0 : materialResponseArrayList.size();
    }

    public Object getItem(int position) {
        return materialResponseArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_view_store_check, null);
            holder = new ViewHolder();
            holder.index = convertView.findViewById(R.id.txtIndex);
            holder.kodeProduk = convertView.findViewById(R.id.kodeProduk);
            holder.namaProduk = convertView.findViewById(R.id.namaProduk);
            holder.qty1 = convertView.findViewById(R.id.qty1);
            holder.qty2 = convertView.findViewById(R.id.qty2);
            holder.q1Type = convertView.findViewById(R.id.q1Type);
            holder.q2Type = convertView.findViewById(R.id.q2Type);
            holder.icDel = convertView.findViewById(R.id.icDel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MaterialResponse materialResponse = materialResponseArrayList.get(position);

        holder.q1Type.setId(position);
        holder.q2Type.setId(position);
        holder.qty1.setId(position);
        holder.qty2.setId(position);

        holder.kodeProduk.setText(materialResponse.getIdMaterial());
        holder.namaProduk.setText(materialResponse.getMaterialName());

        holder.index.setText(String.valueOf(position + 1) + ".");

        holder.q1Type.setOnItemSelectedListener(null);
        holder.q2Type.setOnItemSelectedListener(null);

        List<String> temp = new ArrayList<>();
        if (materialResponse.getListUomName() != null && !materialResponse.getListUomName().isEmpty()) {
            for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
                if (materialResponse.getListUomName().get(i).getUomName() != null) {
                    temp.add(materialResponse.getListUomName().get(i).getUomName());
                }
            }
            listUom = new String[temp.size()];
            listUom = temp.toArray(listUom);
        }

        if (listUom != null) {
            if (listUom.length != 0) {
//                mContext.setSpinnerAdapter3(listUom, holder.q1Type);
                mContext.setAutoCompleteAdapter(listUom, holder.q1Type);
            }

            if (listUom.length == 0) {
                holder.qty1.setEnabled(false);
                holder.q1Type.setEnabled(false);
                holder.qty2.setEnabled(false);
                holder.q2Type.setEnabled(false);
            } else {
                holder.qty1.setEnabled(true);
                holder.q1Type.setEnabled(true);
                holder.qty2.setEnabled(true);
                holder.q2Type.setEnabled(true);
            }
        }


        //new
        if (materialResponse.getUom1() != null) {
            if (materialResponse.getListUomName() != null && !materialResponse.getListUomName().isEmpty()) {
                for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
                    if (materialResponse.getListUomName().get(i).getId() != null) {
                        if (materialResponse.getListUomName().get(i).getId().equals(materialResponse.getUom1())) {
                            int spinnerPosition = mContext.getSpinnerAdapter().getPosition(materialResponse.getListUomName().get(i).getUomName());
                            holder.q1Type.setText(materialResponse.getListUomName().get(i).getUomName());
                            mContext.setAutoCompleteAdapter(listUom, holder.q1Type);
//                            holder.q1Type.setSelection(spinnerPosition);
                            break;
                        }
                    }
                }
            }
        }

        if (materialResponse.getUom2() != null) {
            if (materialResponse.getListUomName() != null && !materialResponse.getListUomName().isEmpty()) {
                for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
                    if (materialResponse.getListUomName().get(i).getId() != null) {
                        if (materialResponse.getListUomName().get(i).getId().equals(materialResponse.getUom2())) {
                            int spinnerPosition = mContext.getSpinnerAdapter().getPosition(materialResponse.getListUomName().get(i).getUomName());
                            holder.q2Type.setText(materialResponse.getListUomName().get(i).getUomName());
                            mContext.setAutoCompleteAdapter(listUom, holder.q2Type);
//                            holder.q2Type.setSelection(spinnerPosition);
                            break;
                        }
                    }
                }
            }
        }

        holder.q1Type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //new
//                String uom1 = holder.q1Type.getSelectedItem().toString();
                String uom1 = holder.q1Type.getText().toString();
                List<String> temp = new ArrayList<>();
                if (materialResponse.getListUomName() != null && !materialResponse.getListUomName().isEmpty()) {
                    for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
                        if (materialResponse.getListUomName().get(pos).getConversion() > materialResponse.getListUomName().get(i).getConversion()) {
                            temp.add(materialResponse.getListUomName().get(i).getUomName());
                        }
                    }
                }


                if (temp.size() != 0) {
                    String[] listUom = new String[temp.size()];
                    listUom = temp.toArray(listUom);
//                    mContext.setSpinnerAdapter3(listUom, holder.q2Type);
                    mContext.setAutoCompleteAdapter(listUom, holder.q2Type);
                    holder.q2Type.setEnabled(true);
                    holder.qty2.setEnabled(true);
                } else {
                    holder.q2Type.setAdapter(null);
                    holder.qty2.setText("");
                    holder.q2Type.setEnabled(false);
                    holder.qty2.setEnabled(false);
                }

                if (materialResponse.getListUomName() != null && !materialResponse.getListUomName().isEmpty()) {
                    for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
                        if (uom1.equals(materialResponse.getListUomName().get(i).getUomName())) {
                            materialResponse.setUom1(materialResponse.getListUomName().get(i).getId());
                        }
                    }
                }
//                materialResponse.setQ1TypePos(holder.q1Type.getSelectedItemPosition());
                materialResponse.setQ1TypePos(pos);
            }
        });

//        holder.q1Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                //new
////                String uom1 = holder.q1Type.getSelectedItem().toString();
//                String uom1 = holder.q1Type.getText().toString();
//                List<String> temp = new ArrayList<>();
//                if (materialResponse.getListUomName() != null && !materialResponse.getListUomName().isEmpty()) {
//                        for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
//                            if (materialResponse.getListUomName().get(pos).getConversion() > materialResponse.getListUomName().get(i).getConversion()) {
//                                temp.add(materialResponse.getListUomName().get(i).getUomName());
//                            }
//                        }
//                    }
//
//
//                if (temp.size() != 0) {
//                    String[] listUom = new String[temp.size()];
//                    listUom = temp.toArray(listUom);
////                    mContext.setSpinnerAdapter3(listUom, holder.q2Type);
//                    mContext.setAutoCompleteAdapter(listUom, holder.q2Type);
//                    holder.q2Type.setEnabled(true);
//                    holder.qty2.setEnabled(true);
//                } else {
//                    holder.q2Type.setAdapter(null);
//                    holder.qty2.setText("");
//                    holder.q2Type.setEnabled(false);
//                    holder.qty2.setEnabled(false);
//                }
//
//                if (materialResponse.getListUomName() != null && !materialResponse.getListUomName().isEmpty()) {
//                    for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
//                        if (uom1.equals(materialResponse.getListUomName().get(i).getUomName())) {
//                            materialResponse.setUom1(materialResponse.getListUomName().get(i).getId());
//                        }
//                    }
//                }
////                materialResponse.setQ1TypePos(holder.q1Type.getSelectedItemPosition());
//                materialResponse.setQ1TypePos(pos);
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        holder.q2Type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String uom2 = holder.q2Type.getText().toString();
                if (uom2.equals("null")) {
                    materialResponse.setUom2(null);
                } else {
                    if (materialResponse.getListUomName() != null && !materialResponse.getListUomName().isEmpty()) {
                        for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
                            if (uom2.equals(materialResponse.getListUomName().get(i).getUomName())) {
                                materialResponse.setUom2(materialResponse.getListUomName().get(i).getId());
                            }
                        }
                    }
                }
                materialResponse.setQ2TypePos(pos);
            }
        });

//        holder.q2Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                String uom2 = holder.q2Type.getSelectedItem().toString();
//                if (uom2.equals("null")) {
//                    materialResponse.setUom2(null);
//                } else {
//                    if (materialResponse.getListUomName() != null && !materialResponse.getListUomName().isEmpty()) {
//                        for (int i = 0; i < materialResponse.getListUomName().size(); i++) {
//                            if (uom2.equals(materialResponse.getListUomName().get(i).getUomName())) {
//                                materialResponse.setUom2(materialResponse.getListUomName().get(i).getId());
//                            }
//                        }
//                    }
//                }
//                materialResponse.setQ2TypePos(holder.q2Type.getSelectedItemPosition());
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        if (materialResponse.getQty1() != null) {
            if (!materialResponse.getQty1().equals("0")) {
                holder.qty1.setText(String.valueOf(materialResponse.getQty1()));
            } else {
                holder.qty1.setText(null);
            }
            materialResponse.setQty1(String.valueOf(materialResponse.getQty1()));
        } else {
            holder.qty1.setText(null);
            materialResponse.setQty1("0");
        }

        if (materialResponse.getQty2() != null) {
            holder.qty2.setText(String.valueOf(materialResponse.getQty2()));
            materialResponse.setQty2(String.valueOf(materialResponse.getQty2()));
        } else {
            holder.qty2.setText(null);
            materialResponse.setQty2("0");
        }

        holder.qty1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    materialResponse.setQty1("0");
                } else {
                    materialResponse.setQty1(s.toString());
                }
            }
        });

        holder.qty2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    materialResponse.setQty2("0");
                } else {
                    materialResponse.setQty2(s.toString());
                }
            }
        });

        holder.icDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.deleteStoreCheck(materialResponse.getId_store_check(), position);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView kodeProduk, namaProduk, index;
        EditText qty1, qty2;
        AutoCompleteTextView q1Type, q2Type;
//        Spinner q1Type, q2Type;
        ImageView icDel;
    }

}
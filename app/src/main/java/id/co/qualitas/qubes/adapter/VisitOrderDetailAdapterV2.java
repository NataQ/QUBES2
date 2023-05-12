package id.co.qualitas.qubes.adapter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.CreateOrderDetail2Fragment;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;

public class VisitOrderDetailAdapterV2 extends BaseAdapter {
    private List<VisitOrderDetailResponse> returnList;
    private CreateOrderDetail2Fragment context;
    private ArrayList<String> itemsJenisJualTop = new ArrayList<>();
    private ArrayList<String> itemsJenisJualTopF = new ArrayList<>();
    private ArrayList<String> itemsJenisJual = new ArrayList<>();
    private ArrayList<String> itemsJenisJualMix = new ArrayList<>();
    private ArrayList<String> itemsJenisJualTopSap = new ArrayList<>();
    private LayoutInflater mInflater;
    private Button btnYes;
    private Button btnNo;
    private Boolean spinnerSelected1 = false, spinnerSelected2 = false, spinnerSelected3 = false;
    private int uomid1 = 0, uomid2 = 0;
    private int selectedIndex;
    private String type;
    private int focusNow = 0;

    public int getFocusNow() {
        return focusNow;
    }

    public VisitOrderDetailAdapterV2(CreateOrderDetail2Fragment fragment, List<VisitOrderDetailResponse> results) {
        returnList = results;
        context = fragment;
        mInflater = LayoutInflater.from(fragment.getContext());
        selectedIndex = -1;
    }

    public VisitOrderDetailAdapterV2(CreateOrderDetail2Fragment fragment, List<VisitOrderDetailResponse> results, String orderType) {
        returnList = results;
        context = fragment;
        mInflater = LayoutInflater.from(fragment.getContext());
        selectedIndex = -1;
        type = orderType;
    }

    public int getCount() {
        return returnList.size();
    }

    public Object getItem(int position) {
        return returnList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.row_view_create_order_detail, null);//row_view_order_plan_detail_v2
            holder = new ViewHolder();

            holder.row = view.findViewById(R.id.row);
            holder.txtIndex = view.findViewById(R.id.txtIndex);
            holder.edtTxtQty1 = view.findViewById(R.id.edtTxtQty1);
            holder.edtTxtQty2 = view.findViewById(R.id.edtTxtQty2);
            holder.q1Type = view.findViewById(R.id.q1Type);
            holder.q2Type = view.findViewById(R.id.q2Type);

            holder.txtOrderName = view.findViewById(R.id.txtMaterialName);
            holder.txtOrderCode = view.findViewById(R.id.txtIdMaterial);
            holder.txtViewTop = view.findViewById(R.id.txtViewTop);

            holder.icDelete = view.findViewById(R.id.ic_del);
//            holder.spinnerJenisJual = view.findViewById(R.id.spinnerJenisJual);
            holder.autoCompleteJenisJual = view.findViewById(R.id.autoCompleteJenisJual);
//            holder.spinnerJenisJual.setVisibility(View.VISIBLE);
            holder.autoCompleteJenisJual.setVisibility(View.VISIBLE);
            holder.btnGetPrice = view.findViewById(R.id.ic_attach);
            holder.btnGetPrice.setVisibility(View.VISIBLE);
            holder.txtTotalPrice = view.findViewById(R.id.edtPrice);
            holder.txtTotalPrice.setEnabled(false);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final VisitOrderDetailResponse order = returnList.get(position);

        if (context.getSavedHeader() != null) {
            if (context.getSavedHeader().getId() != null) {
                if (context.getSavedHeader().getId().contains(Constants.KEY_TRUE_ID_TO)) {
                    holder.edtTxtQty1.setEnabled(false);
//                    holder.edtTxtQty1.setBackground(null);
                    holder.edtTxtQty2.setEnabled(false);
//                    holder.edtTxtQty2.setBackground(null);
                    if (order.getJenisJual() == null) {
//                        holder.spinnerJenisJual.setBackground(null);
                        holder.autoCompleteJenisJual.setBackground(null);
                    }
//                    holder.spinnerJenisJual.setEnabled(false);
                    holder.autoCompleteJenisJual.setEnabled(false);
                    holder.q1Type.setEnabled(false);

                    if (order.getUom2() == null) {
                        holder.q2Type.setVisibility(View.INVISIBLE);
                    } else {
                        holder.q2Type.setVisibility(View.VISIBLE);
                    }
                    holder.q2Type.setEnabled(false);

                    holder.icDelete.setVisibility(View.INVISIBLE);
                }
            }
        }

        holder.q1Type.setTag(position);
        holder.q2Type.setTag(position);

        holder.txtIndex.setText(String.valueOf(position + 1) + ". ");

        holder.txtOrderName.setText(Helper.validateResponseEmpty(order.getMaterialName()));
        holder.txtOrderCode.setText(Helper.validateResponseEmpty(order.getIdMaterial()));

        holder.txtViewTop.setText(String.valueOf(order.getTopf()).concat(" ").concat(context.getString(R.string.hari)));

        if (order.getQty1() != null) {
            holder.edtTxtQty1.setText(Helper.toRupiahFormat(String.valueOf(order.getQty1()), ','));
        }

        if (order.getQty2() != null) {
            holder.edtTxtQty2.setText(Helper.toRupiahFormat(String.valueOf(order.getQty2()), ','));
        }

//        holder.q1Type.setOnItemSelectedListener(null);
        holder.q1Type.setOnItemClickListener(null);
//        holder.q2Type.setOnItemSelectedListener(null);
        holder.q2Type.setOnItemClickListener(null);

        ArrayList<String> itemsUom = new ArrayList<>();
        if (order.getListUom() != null && !order.getListUom().isEmpty()) {
            for (int i = 0; i < order.getListUom().size(); i++) {
                if (order.getListUom().get(i).getUomName() != null) {
                    itemsUom.add(i, order.getListUom().get(i).getUomName());
                }
            }
        }

        String[] listUom = new String[itemsUom.size()];
        listUom = itemsUom.toArray(listUom);

        context.setAutoCompleteAdapter(listUom, holder.q1Type);
        context.setAutoCompleteAdapter(listUom, holder.q2Type);

        if (order.getUom1() != null && !order.getUom1().equals("-")) {
            if (order.getListUom() != null && !order.getListUom().isEmpty()) {
                for (int i = 0; i < order.getListUom().size(); i++) {
                    if (order.getListUom().get(i).getId() != null) {
                        if (order.getListUom().get(i).getUomName().equals(order.getUom1())) {
                            order.setUom1(order.getListUom().get(i).getId());
                            uomid1 = 1;
                            break;
                        }
                    }
                }
            }

            if (uomid1 == 0) {
                order.setUom1(order.getUom1());
            } else {
                uomid1 = 0;
            }

            if (order.getListUom() != null && !order.getListUom().isEmpty()) {
                for (int i = 0; i < order.getListUom().size(); i++) {
                    if (order.getListUom().get(i).getId() != null) {
                        if (order.getListUom().get(i).getId().equals(order.getUom1())) {
                            int spinnerPosition = context.getSpinnerAdapter().getPosition(order.getListUom().get(i).getUomName());
//                            holder.q1Type.setSelection(spinnerPosition);
                            holder.q1Type.setText(order.getListUom().get(i).getUomName());
                            context.setAutoCompleteAdapter(listUom, holder.q1Type);
                            order.setUom1(order.getListUom().get(i).getUomName());
                            break;
                        }
                    }
                }
            }
        }

        if (order.getUom2() != null && !order.getUom2().equals("-")) {
            if (order.getListUom() != null && !order.getListUom().isEmpty()) {
                for (int i = 0; i < order.getListUom().size(); i++) {
                    if (order.getListUom().get(i).getId() != null) {
                        if (order.getListUom().get(i).getUomName().equals(order.getUom2())) {
                            order.setUom2(order.getListUom().get(i).getId());
                            uomid2 = 1;
                            break;
                        }
                    }
                }
            }

            if (uomid2 == 0) {
                order.setUom2(order.getUom2());
            } else {
                uomid2 = 0;
            }

            if (order.getListUom() != null && !order.getListUom().isEmpty()) {
                for (int i = 0; i < order.getListUom().size(); i++) {
                    if (order.getListUom().get(i).getId() != null) {
                        if (order.getListUom().get(i).getId().equals(order.getUom2())) {
                            int spinnerPosition = context.getSpinnerAdapter().getPosition(order.getListUom().get(i).getUomName());
//                            holder.q2Type.setSelection(spinnerPosition);
                            holder.q2Type.setText(order.getListUom().get(i).getUomName());
                            context.setAutoCompleteAdapter(listUom, holder.q2Type);
                            order.setUom2(order.getListUom().get(i).getUomName());
                            break;
                        }
                    }
                }
            }
        }

        holder.autoCompleteJenisJual.setOnItemClickListener(null);
//        holder.spinnerJenisJual.setOnItemSelectedListener(null);

        itemsJenisJual = new ArrayList<>();
        itemsJenisJualMix = new ArrayList<>();
        itemsJenisJualTop = new ArrayList<>();
        itemsJenisJual.add(0, Constants.SELECT_ONE);
        itemsJenisJualMix.add(0, Constants.SELECT_ONE);
        itemsJenisJualTop.add(0, Constants.STRIP.trim());

        if (order.getJenisJualandTop() != null) {
            for (int i = 0; i < order.getJenisJualandTop().size(); i++) {
                itemsJenisJualMix.add(i + 1, order.getJenisJualandTop().get(i).getTop_sap().concat("--").concat(order.getJenisJualandTop().get(i).getJenisJualName()));
                itemsJenisJual.add(i + 1, order.getJenisJualandTop().get(i).getJenisJualName());
                itemsJenisJualTop.add(i + 1, order.getJenisJualandTop().get(i).getTop_sap());
            }
//            context.setSpinnerGrey(itemsJenisJualMix, holder.spinnerJenisJual);
            context.setAutoCompleteAdapter(itemsJenisJualMix, holder.autoCompleteJenisJual);
        }

        if (order.getJenisJual() != null) {
            if (order.getJenisJual().equals(Constants.STRIP.trim())) {
//                holder.spinnerJenisJual.setSelection(0);
//                holder.autoCompleteJenisJual.setSelection(0);
                holder.autoCompleteJenisJual.setText(null);
                context.setAutoCompleteAdapter(itemsJenisJualMix, holder.autoCompleteJenisJual);
            } else {
                if (order.getTop_sap() != null) {
                    if (order.getJenisJualandTop() != null) {
                        for (int i = 0; i < order.getJenisJualandTop().size(); i++) {
                            if (order.getTop_sap().equals(order.getJenisJualandTop().get(i).getTop_sap()) &&
                                    order.getJenisJual().equals(order.getJenisJualandTop().get(i).getJenisJual())) {
//                                holder.spinnerJenisJual.setSelection(i + 1);
                                holder.autoCompleteJenisJual.setText(itemsJenisJualMix.get(i + 1));
                                context.setAutoCompleteAdapter(itemsJenisJualMix, holder.autoCompleteJenisJual);
                            }

                        }
                    }
                }
            }
        }

        holder.edtTxtQty1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    context.getScroll().scrollTo(holder.edtTxtQty1.getWidth(), holder.edtTxtQty1.getHeight()); //rootView is my parentView of the fragment
//                    context.getScroll().scrollTo(context.getRootView().getWidth(), context.getRootView().getHeight()); //rootView is my parentView of the fragment
                }
            }
        });

        holder.edtTxtQty1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = holder.edtTxtQty1.getText().toString().trim();
                if (!s1.trim().equals(Constants.EMPTY_STRING)) {
                    try {
                        if (s1.contains(Constants.COMA)) {
                            order.setQty1(new BigDecimal(s1.replace(Constants.COMA, Constants.EMPTY_STRING)));
                        } else {
                            order.setQty1(new BigDecimal(s1));
                        }
                    } catch (NumberFormatException e) {

                    }
                } else {
                    order.setQty1(BigDecimal.ZERO);
                }

//                if (holder.q1Type.getSelectedItem() != null) {
                if (holder.q1Type.getText() != null) {
//                    String uom1 = holder.q1Type.getSelectedItem().toString();
                    String uom1 = holder.q1Type.getText().toString();
                    if (!holder.edtTxtQty1.getText().toString().trim().equals(context.getResources().getString(R.string.space).trim())) {
                        if (order.getListUom() != null && !order.getListUom().isEmpty()) {
                            for (int i = 0; i < order.getListUom().size(); i++) {
                                if (uom1.equals(order.getListUom().get(i).getUomName())) {
                                    order.setUom1(order.getListUom().get(i).getId());
                                }
                            }
                        }
                    } else {
                        order.setUom1(null);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                holder.txtTotalPrice.setText(null);
                order.setPrice(BigDecimal.ZERO);
                order.setPriceBfr(BigDecimal.ZERO);
                order.setListToPrice(null);
                order.setId_price(null);
//                context.calculateTotal(returnList);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Helper.setDotCurrency(holder.edtTxtQty1, this, s);

                try {
                    if (s.length() > 0) {
                        order.setQty1(new BigDecimal(s.toString().trim().replace(Constants.COMA, Constants.EMPTY_STRING)));
                    }
                } catch (NumberFormatException ignored) {

                }

            }
        });

        if (order.getQty1() == null) {
            order.setQty1(BigDecimal.ZERO);
        }

        holder.edtTxtQty2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                holder.txtTotalPrice.setText(null);
//                order.setPrice(BigDecimal.ZERO);
                String s2 = holder.edtTxtQty2.getText().toString().trim();
                if (!s2.trim().equals(Constants.EMPTY_STRING)) {
                    try {
                        if (s2.contains(Constants.COMA)) {
                            order.setQty2(new BigDecimal(s2.replace(Constants.COMA, Constants.EMPTY_STRING)));
                        } else {
                            order.setQty2(new BigDecimal(s2));
                        }
                    } catch (NumberFormatException e) {

                    }
                } else {
                    order.setQty2(BigDecimal.ZERO);
                    order.setUom2(null);
                }

//                if (holder.q2Type.getSelectedItem() != null) {
                if (holder.q2Type.getText() != null) {
//                    String uom2 = holder.q2Type.getSelectedItem().toString();
                    String uom2 = holder.q2Type.getText().toString();
                    if (uom2 != null) {
                        if (!holder.edtTxtQty2.getText().toString().trim().equals("")) {
                            if (order.getListUom() != null && !order.getListUom().isEmpty()) {
                                for (int i = 0; i < order.getListUom().size(); i++) {
                                    if (uom2.equals(order.getListUom().get(i).getUomName())) {
                                        order.setUom2(order.getListUom().get(i).getId());
                                    }
                                }
                            }
                        } else {
                            order.setUom2(null);
                        }
                    } else {
                        order.setUom2(null);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (holder.edtTxtQty1.getText().equals("0") || holder.edtTxtQty1.getText().equals("")) {
                    if (!s.toString().equals("0") && !s.toString().equals("")) {
                        holder.txtTotalPrice.setText(null);
                        order.setPrice(BigDecimal.ZERO);
                        order.setPriceBfr(BigDecimal.ZERO);
                        order.setListToPrice(null);
                        order.setId_price(null);

//                    context.calculateTotal(returnList);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        holder.txtTotalPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        holder.q1Type.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                spinnerSelected1 = true;
                return false;
            }
        });
        holder.q2Type.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                spinnerSelected2 = true;
                return false;
            }
        });

        holder.autoCompleteJenisJual.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                spinnerSelected3 = true;
                return false;
            }
        });

//        holder.spinnerJenisJual.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                spinnerSelected3 = true;
//                return false;
//            }
//        });

        holder.q1Type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //                String uom1 = holder.q1Type.getSelectedItem().toString();
                String uom1 = holder.q1Type.getText().toString();

                List<String> temp = new ArrayList<>();
                for (int i = 0; i < order.getListUom().size(); i++) {
                    if (order.getListUom().get(pos).getConversion() > order.getListUom().get(i).getConversion()) {
                        temp.add(order.getListUom().get(i).getUomName());
                    }
                }
                if (temp.size() != 0) {
                    String[] listUom = new String[temp.size()];
                    listUom = temp.toArray(listUom);
//                    context.setSpinnerAdapter3(listUom, holder.q2Type);
                    context.setAutoCompleteAdapter(listUom, holder.q2Type);

                    if (order.getUom2() != null) {
                        if (order.getListUom() != null && !order.getListUom().isEmpty()) {
                            for (int i = 0; i < order.getListUom().size(); i++) {
                                if (order.getListUom().get(i).getUomName() != null) {
                                    if (order.getListUom().get(i).getUomName().equals(order.getUom2())) {
                                        int spinnerPosition = context.getSpinnerAdapter().getPosition(order.getListUom().get(i).getUomName());
//                                        holder.q2Type.setSelection(spinnerPosition);
                                        holder.q2Type.setText(order.getListUom().get(i).getUomName());
                                        context.setAutoCompleteAdapter(listUom, holder.q2Type);
                                        order.setUom2(order.getListUom().get(i).getUomName());
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    holder.q2Type.setEnabled(true);
                    if (context.getSavedHeader() != null) {
                        if (context.getSavedHeader().getId() != null) {
                            if (context.getSavedHeader().getId().contains(Constants.KEY_TRUE_ID_TO)) {
                                holder.q2Type.setEnabled(false);
                            }
                        }
                    }

                    holder.edtTxtQty2.setEnabled(true);

                    if (context.getSavedHeader() != null) {
                        if (context.getSavedHeader().getId() != null) {
                            if (context.getSavedHeader().getId().contains(Constants.KEY_TRUE_ID_TO)) {
                                if (order.getUom2() == null) {
                                    holder.q2Type.setVisibility(View.INVISIBLE);
                                } else {
                                    holder.q2Type.setVisibility(View.VISIBLE);
                                }
                                holder.q2Type.setEnabled(false);
                            }
                        }
                    }
                } else {
                    holder.q2Type.setAdapter(null);
                    holder.edtTxtQty2.setText(context.getResources().getString(R.string.space).trim());
                    holder.q2Type.setEnabled(false);
                    holder.edtTxtQty2.setEnabled(false);
                }
//                if (spinnerSelected1) {
                if (!holder.edtTxtQty1.getText().toString().trim().equals("")) {

//                        order.setUom1(getIdUom(order, holder.q1Type.getSelectedItem().toString()));
                    order.setUom1(getIdUom(order, holder.q1Type.getText().toString()));

                    if (!order.getQty1().equals(BigDecimal.ZERO) && order.getJenisJual() != null) {
                        context.requestPriceN(order);
                    }
                } else {
                    order.setUom1(null);
                }

                spinnerSelected1 = false;
//                } else {
////                    if (holder.q1Type.getSelectedItem() != null) {
//                    if (holder.q1Type.getText() != null) {
////                        order.setUom1(getIdUom(order, holder.q1Type.getSelectedItem().toString()));
//                        order.setUom1(getIdUom(order, holder.q1Type.getText().toString()));
//                    }
//                }
            }
        });

        holder.q2Type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
//                if (spinnerSelected2) {
//                    if (holder.q2Type.getSelectedItem() != null) {
                if (holder.q2Type.getText() != null) {
//                        order.setUom2(getIdUom(order, holder.q2Type.getSelectedItem().toString()));
                    order.setUom2(getIdUom(order, holder.q2Type.getText().toString()));
                    if (order.getQty2() != null && order.getJenisJual() != null) {
                        if (!order.getQty2().equals(BigDecimal.ZERO)) {
                            Helper.setItemParam(Constants.UOM2_SELECTED, "Y");
                            context.requestPriceN(order);
                        }
                    }
                } else {
                    order.setUom2(null);
                }

                spinnerSelected2 = false;
//                } else {
//                    if (holder.q2Type.getText() != null) {
////                    if (holder.q2Type.getSelectedItem() != null) {
////                        order.setUom2(getIdUom(order, holder.q2Type.getSelectedItem().toString()));
//                        order.setUom2(getIdUom(order, holder.q2Type.getText().toString()));
//                    }
//                }
            }
        });

        holder.autoCompleteJenisJual.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String top_sap = "-";
                String topf = "0";
                String top = "0";

                itemsJenisJual = new ArrayList<>();
                itemsJenisJualTop = new ArrayList<>();
                itemsJenisJualTopF = new ArrayList<>();
                itemsJenisJualTopSap = new ArrayList<>();
                itemsJenisJual.add(0, Constants.SELECT_ONE);
                itemsJenisJualTop.add(0, "-");
                itemsJenisJualTopF.add(0, "-");
                itemsJenisJualTopSap.add(0, "-");

                if (order.getJenisJualandTop() != null) {
                    for (int i = 0; i < order.getJenisJualandTop().size(); i++) {
                        itemsJenisJual.add(i + 1, order.getJenisJualandTop().get(i).getJenisJual());
                        itemsJenisJualTop.add(i + 1, String.valueOf(order.getJenisJualandTop().get(i).getId()));
                        itemsJenisJualTopF.add(i + 1, String.valueOf(order.getJenisJualandTop().get(i).getTopf()));
                        itemsJenisJualTopSap.add(i + 1, String.valueOf(order.getJenisJualandTop().get(i).getTop_sap()));
                    }
                }

                topf = itemsJenisJualTopF.get(pos);

//                if (spinnerSelected3) {
                if (pos != 0) {
                    Helper.setItemParam(Constants.POSITION_JENIS_JUAL, pos);
                    if (itemsJenisJualTop.size() > 1) {
                        //top sap nya jadi topf
                        top_sap = itemsJenisJualTopSap.get(pos);
                        topf = itemsJenisJualTopF.get(pos);
                        top = itemsJenisJualTop.get(pos);

                        String jenisJual = itemsJenisJual.get(pos);

                        holder.txtViewTop.setText(topf.concat(context.getResources().getString(R.string.space)).concat(view.getResources().getString(R.string.hari)));
                        order.setTop_sap(top_sap);
                        order.setJenisJual(jenisJual);
                        for (int i = 0; i < order.getJenisJualandTop().size(); i++) {
                            if (top.equals(order.getJenisJualandTop().get(i).getId())) {
                                order.setTop(order.getJenisJualandTop().get(i).getId());
                                order.setJenisJual(order.getJenisJualandTop().get(i).getJenisJual());
                            }
                        }
                        order.setPosJenisJual(pos);

                        if (!order.getQty1().equals(BigDecimal.ZERO)) {
                            Helper.setItemParam(Constants.JENIS_JUAL_SELECTED, "Y");
                            context.requestPriceN(order);
                        }
                    }
                } else {
                    holder.txtViewTop.setText(top_sap);
                    order.setTop_sap(null);
                    order.setJenisJual("-");
                    if (Helper.getItemParam(Constants.POSITION_JENIS_JUAL) != null) {
//                            holder.autoCompleteJenisJual.setSelection(pos);
                        holder.autoCompleteJenisJual.setText(itemsJenisJualMix.get(pos));
                        context.setAutoCompleteAdapter(itemsJenisJualMix, holder.autoCompleteJenisJual);
                        Helper.removeItemParam(Constants.POSITION_JENIS_JUAL);
                        returnList.get(position).setPosJenisJual(pos);
                    }

                    if (!order.getQty1().equals(BigDecimal.ZERO)) {
                        Helper.setItemParam(Constants.JENIS_JUAL_SELECTED, "Y");
                        context.requestPriceN(order);
                    }
                }
                spinnerSelected3 = false;
//                }

                if (view != null) {
                    holder.txtViewTop.setText(topf.concat(Constants.SPACE).concat(view.getResources().getString(R.string.hari)));
                }
            }
        });

//        holder.spinnerJenisJual.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
//
//                String top_sap = "-";
//                String topf = "0";
//                String top = "0";
//
//                itemsJenisJual = new ArrayList<>();
//                itemsJenisJualTop = new ArrayList<>();
//                itemsJenisJualTopF = new ArrayList<>();
//                itemsJenisJualTopSap = new ArrayList<>();
//                itemsJenisJual.add(0, Constants.SELECT_ONE);
//                itemsJenisJualTop.add(0, "-");
//                itemsJenisJualTopF.add(0, "-");
//                itemsJenisJualTopSap.add(0, "-");
//
//                if (order.getJenisJualandTop() != null) {
//                    for (int i = 0; i < order.getJenisJualandTop().size(); i++) {
//                        itemsJenisJual.add(i + 1, order.getJenisJualandTop().get(i).getJenisJual());
//                        itemsJenisJualTop.add(i + 1, String.valueOf(order.getJenisJualandTop().get(i).getId()));
//                        itemsJenisJualTopF.add(i + 1, String.valueOf(order.getJenisJualandTop().get(i).getTopf()));
//                        itemsJenisJualTopSap.add(i + 1, String.valueOf(order.getJenisJualandTop().get(i).getTop_sap()));
//                    }
//                }
//
//                topf = itemsJenisJualTopF.get(pos);
//
//                if (spinnerSelected3) {
//                    if (pos != 0) {
//                        Helper.setItemParam(Constants.POSITION_JENIS_JUAL, pos);
//                        if (itemsJenisJualTop.size() > 1) {
//                            //top sap nya jadi topf
//                            top_sap = itemsJenisJualTopSap.get(pos);
//                            topf = itemsJenisJualTopF.get(pos);
//                            top = itemsJenisJualTop.get(pos);
//
//                            String jenisJual = itemsJenisJual.get(pos);
//
//                            holder.txtViewTop.setText(topf.concat(context.getResources().getString(R.string.space)).concat(view.getResources().getString(R.string.hari)));
//                            order.setTop_sap(top_sap);
//                            order.setJenisJual(jenisJual);
//                            for (int i = 0; i < order.getJenisJualandTop().size(); i++) {
//                                if (top.equals(order.getJenisJualandTop().get(i).getId())) {
//                                    order.setTop(order.getJenisJualandTop().get(i).getId());
//                                    order.setJenisJual(order.getJenisJualandTop().get(i).getJenisJual());
//                                }
//                            }
//                            order.setPosJenisJual(pos);
//
//                            if (!order.getQty1().equals(BigDecimal.ZERO)) {
//                                Helper.setItemParam(Constants.JENIS_JUAL_SELECTED, "Y");
//
//                                context.requestPriceN(order);
//                            }
//                        }
//                    } else {
//                        holder.txtViewTop.setText(top_sap);
//                        order.setTop_sap(null);
//                        order.setJenisJual("-");
//                        if (Helper.getItemParam(Constants.POSITION_JENIS_JUAL) != null) {
//                            holder.spinnerJenisJual.setSelection(pos);
//                            Helper.removeItemParam(Constants.POSITION_JENIS_JUAL);
//                            returnList.get(position).setPosJenisJual(pos);
//                        }
//
//                        if (!order.getQty1().equals(BigDecimal.ZERO)) {
//                            Helper.setItemParam(Constants.JENIS_JUAL_SELECTED, "Y");
//
//                            context.requestPriceN(order);
//                        }
//                    }
//                    spinnerSelected3 = false;
//                }
//
//                if (view != null) {
//                    holder.txtViewTop.setText(topf.concat(Constants.SPACE).concat(view.getResources().getString(R.string.hari)));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        holder.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(context.getContext());
                View dialogview;
                final Dialog alertDialog = new Dialog(context.getContext());
                dialogview = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(dialogview);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btnNo = alertDialog.findViewById(R.id.btnCancel);
                btnYes = alertDialog.findViewById(R.id.btnSave);

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.getProgress().show();
                        context.deleteDetail(position);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });

        holder.btnGetPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.preventDupeDialog();
                Helper.setItemParam(Constants.VIEW_PRICE_DETAIL, "Y");
                Helper.setItemParam(Constants.MATERIAL_DETAIL, order);
                context.requestPrice();
            }
        });

        if (order.getPrice() != null) {
            holder.txtTotalPrice.setText(context.getString(R.string.indo_currency).concat(Constants.SPACE).concat(Helper.toRupiahFormat2(String.valueOf(order.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP)))));
        } else {
            holder.txtTotalPrice.setText(null);
        }

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedIndex == position) {
                    setSelectedIndex(-1);
                    context.viewDetailData(order, -1);
                } else {
                    setSelectedIndex(position);
                    context.viewDetailData(order, position);
                }
            }
        });

        if (selectedIndex == position) {
            holder.row.setSelected(true);
        } else {
            holder.row.setSelected(false);
        }


        holder.edtTxtQty1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (order.getJenisJual() != null) {
                        context.requestPriceN(order);
                    }
                    return true;
                }
                return false;
            }
        });


        holder.edtTxtQty2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (order.getJenisJual() != null) {
                        context.requestPriceN(order);
                    }
                    return true;
                }
                return false;
            }
        });

        holder.edtTxtQty1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (order.getJenisJual() != null) {
                        context.requestPriceN(order);
                    }
                }
                return false;
            }
        });

        holder.edtTxtQty2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (order.getJenisJual() != null) {
                        context.requestPriceN(order);
                    }
                }
                return false;
            }
        });

        holder.edtTxtQty1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (order.getJenisJual() != null) {
                        context.requestPriceN(order);
                    }
                } else {
//                    holder.edtTxtQty1.setBackgroundColor(context.getResources().getColor(R.color.blue6));
                }
            }
        });

        holder.edtTxtQty2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (order.getJenisJual() != null) {
                        context.requestPriceN(order);
                    }
                    focusNow = 0;
                } else {
                    focusNow = 1;
//                    holder.edtTxtQty2.setBackgroundColor(context.getResources().getColor(R.color.blue6));
                }
            }
        });


        return view;
    }

    private void addJenisJualItem(int i, VisitOrderDetailResponse order) {
        itemsJenisJual.add(i + 1, order.getJenisJualandTop().get(i).getJenisJualName());
        itemsJenisJualTop.add(i + 1, order.getJenisJualandTop().get(i).getTop_sap());
    }

    private void setSelectedIndex(int ind) {
        selectedIndex = ind;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView txtOrderName, txtOrderCode, txtViewTop, txtIndex;
        ImageView icDelete;
        AutoCompleteTextView q1Type, q2Type;
//        Spinner spinnerJenisJual;
        EditText edtTxtQty1, edtTxtQty2;
        Button btnGetPrice;
        EditText txtTotalPrice;
        CardView row;
        AutoCompleteTextView autoCompleteJenisJual;
    }

    private String getIdUom(VisitOrderDetailResponse order, String input) {
        String output = context.getResources().getString(R.string.space).trim();
        if (input != null) {
            if (order.getListUom() != null && !order.getListUom().isEmpty()) {
                for (int i = 0; i < order.getListUom().size(); i++) {
                    if (input.equals(order.getListUom().get(i).getUomName())) {
                        output = order.getListUom().get(i).getId();
                    }
                }
            }
        }

        return output;
    }

}

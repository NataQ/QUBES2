package id.co.qualitas.qubes.adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.CreateReturn2Fragment;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Return;

public class CreateNewReturnAdapter extends BaseAdapter {
    private List<Return> returnList = new ArrayList<>();
    private CreateReturn2Fragment context;
    private String[] itemsReason;
    private Calendar date;
    private Button btnYes, btnNo;
    private DatePickerDialog expiredDatePickerDialog;
    private LayoutInflater mInflater;
    private Boolean spinnerSelected = false;
    private Boolean spinnerSelected1 = false, spinnerSelected2 = false, spinnerSelected3 = false;
    private String[] listUom;

    public CreateNewReturnAdapter(CreateReturn2Fragment createNewReturnFragment, List<Return> results) {
        returnList = results;
        context = createNewReturnFragment;
        mInflater = LayoutInflater.from(createNewReturnFragment.getContext());
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

    @SuppressLint("ClickableViewAccessibility")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_view_add_material, null);
            holder = new ViewHolder();
//            holder.idRow = convertView.findViewById(R.id.idRow);
            holder.txtNo = convertView.findViewById(R.id.txtNo);
            holder.materialName = convertView.findViewById(R.id.materialName);
            holder.icClose = convertView.findViewById(R.id.ic_close);
            holder.idMaterial = convertView.findViewById(R.id.idMat);
            holder.qty1 = convertView.findViewById(R.id.qty1);
//            holder.qty2 = convertView.findViewById(R.id.qty2);
            holder.uom1 = convertView.findViewById(R.id.uom1);
//            holder.uom2 = convertView.findViewById(R.id.uom2);
            holder.batch = convertView.findViewById(R.id.batch);
            holder.expiredDate = convertView.findViewById(R.id.expiredDate);
            holder.reason = convertView.findViewById(R.id.reason);
            holder.desc = convertView.findViewById(R.id.description);
            holder.txtError = convertView.findViewById(R.id.txtError);
            holder.catReturn = convertView.findViewById(R.id.categoryReturn);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Return aReturn = returnList.get(position);
        holder.reason.setTag(position);
//        holder.idRow.setText(aReturn.getIdRow());
        holder.txtNo.setText(String.valueOf(position + 1) + ".");

        if (aReturn.getError() != null) {
            holder.txtError.setVisibility(View.VISIBLE);
            holder.txtError.setText(aReturn.getError());
        } else {
            holder.txtError.setVisibility(View.GONE);
        }
        holder.idMaterial.setId(position);
        if (returnList.get(position).getId() != null) {
            holder.idMaterial.setText(returnList.get(position).getId());
        }
        if (returnList.get(position).getName() != null) {
            holder.materialName.setText(returnList.get(position).getName());
        }

        if (aReturn.getCategory() != null && !aReturn.getCategory().equals(Constants.NULL)) {
//            if (aReturn.getCategory().equals(Constants.BAD)) {
//                holder.catReturn.setText(Constants.BAD);
//            } else {
//                holder.catReturn.setText(Constants.GOOD);
//            }

            holder.catReturn.setText(aReturn.getCategory());
        }

//        context.setCustomSpinner(aReturn.getItemsReason(), holder.reason);
        context.setAutoCompleteAdapter(aReturn.getItemsReason(), holder.reason);

        holder.idMaterial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                returnList.get(holder.idMaterial.getId()).setId(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.qty1.setId(position);
//        holder.qty2.setId(position);

        holder.qty1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
//                    returnList.get(holder.qty1.getId()).setQty1(new BigDecimal(s.toString().trim()));
                } else {
//                    returnList.get(holder.qty1.getId()).setQty1(BigDecimal.ZERO);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        holder.qty2.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().trim().length() != 0) {
//                    returnList.get(holder.qty2.getId()).setQty2(new BigDecimal(s.toString().trim()));
//                } else {
//                    returnList.get(holder.qty2.getId()).setQty2(BigDecimal.ZERO);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        holder.batch.setId(position);
        if (returnList.get(position).getBatch() != null) {
            holder.batch.setText(returnList.get(position).getBatch());
        }
        holder.batch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                returnList.get(holder.batch.getId()).setBatch(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.expiredDate.setId(position);
        if (returnList.get(position).getExpiredDate() != null) {
            holder.expiredDate.setText(Helper.changeDateFormat(returnList.get(position).getExpiredDate()));
        }
        holder.expiredDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        date = Calendar.getInstance();
                        expiredDatePickerDialog = new DatePickerDialog(
                                context.getContext(),
                                new DatePickerDialog.OnDateSetListener() {

                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,
                                                          int dayOfMonth) {
                                        //toast validasi
                                        date.set(year, monthOfYear, dayOfMonth);
                                        String dateString1 = Helper.convertDateToString(Constants.DATE_TYPE_12, date.getTime());
                                        holder.expiredDate.setText(dateString1);
                                        returnList.get(holder.expiredDate.getId()).setExpiredDate(Helper.changeDateFormat(Constants.DATE_TYPE_12, Constants.DATE_TYPE_2, dateString1));
                                    }

                                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
//                        dateBRBPickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                        expiredDatePickerDialog.getDatePicker().setCalendarViewShown(false);
                        expiredDatePickerDialog.show();
                        break;
                }
                return true;
            }
        });

        holder.desc.setId(position);
        if (returnList.get(position).getDescription() != null) {
            holder.desc.setText(returnList.get(position).getDescription());
        }
        holder.desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                returnList.get(holder.desc.getId()).setDescription(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context.getContext());
                View dialogview;
                final Dialog alertDialog = new Dialog(context.getContext());
                dialogview = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(dialogview);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btnNo = alertDialog.findViewById(R.id.btnCancel);
                btnYes = alertDialog.findViewById(R.id.btnSave);

                btnYes.setText(Constants.YES);
                btnNo.setText(Constants.NO);

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.deleteMaterial(position);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });


        holder.uom1.setOnItemSelectedListener(null);
//        holder.uom2.setOnItemSelectedListener(null);
        holder.reason.setOnItemSelectedListener(null);

        if (aReturn.getItemsReason() != null && aReturn.getItemsReason().length != 0) {
            for (int i = 0; i < aReturn.getItemsReason().length; i++) {
                if (aReturn.getItemsReason()[i].equals(aReturn.getReason())) {
//                    holder.reason.setSelection(i);
                    holder.reason.setText(aReturn.getItemsReason()[i]);
                    context.setAutoCompleteAdapter(aReturn.getItemsReason(), holder.reason);
                }
            }
        }

        //new
        List<String> temp = new ArrayList<>();
        if (aReturn.getListUomName() != null && !aReturn.getListUomName().isEmpty()) {
            for (int i = 0; i < aReturn.getListUomName().size(); i++) {
                if (aReturn.getListUomName().get(i).getUomName() != null) {
                    temp.add(aReturn.getListUomName().get(i).getUomName());
                }
            }
            listUom = new String[temp.size()];
            listUom = temp.toArray(listUom);
//            context.setSpinnerAdapter2(listUom, holder.uom1);
            context.setAutoCompleteAdapter(listUom, holder.uom1);
        }

//        holder.uom2.setEnabled(false);
//        holder.qty2.setEnabled(false);

        holder.uom1.setTag(position);
//        holder.uom2.setTag(position);

        holder.uom1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String uom1 = holder.uom1.getText().toString();

                List<String> temp = new ArrayList<>();
                for (int i = 0; i < aReturn.getListUomName().size(); i++) {
                    if (aReturn.getListUomName().get(pos).getConversion() > aReturn.getListUomName().get(i).getConversion()) {
                        temp.add(aReturn.getListUomName().get(i).getUomName());
                    }
                }
//                if (temp.size() != 0) {
//                    String[] listUom = new String[temp.size()];
//                    listUom = temp.toArray(listUom);
//                    context.setSpinnerAdapter2(listUom, holder.uom2);
//                    holder.uom2.setEnabled(true);
//                    holder.qty2.setEnabled(true);
//                } else {
//                    holder.uom2.setAdapter(null);
//                    holder.qty2.setText("");
//                    holder.uom2.setEnabled(false);
//                    holder.qty2.setEnabled(false);
//                }
                if (aReturn.getListUomName() != null && !aReturn.getListUomName().isEmpty()) {
                    for (int i = 0; i < aReturn.getListUomName().size(); i++) {
                        if (uom1.equals(aReturn.getListUomName().get(i).getUomName())) {
                            aReturn.setUom(aReturn.getListUomName().get(i).getId());
                        }
                    }
                }
            }
        });

//        holder.uom1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//                String uom1 = holder.uom1.getText().toString();
//
//                List<String> temp = new ArrayList<>();
//                for (int i = 0; i < aReturn.getListUomName().size(); i++) {
//                    if (aReturn.getListUomName().get(pos).getConversion() > aReturn.getListUomName().get(i).getConversion()) {
//                        temp.add(aReturn.getListUomName().get(i).getUomName());
//                    }
//                }
////                if (temp.size() != 0) {
////                    String[] listUom = new String[temp.size()];
////                    listUom = temp.toArray(listUom);
////                    context.setSpinnerAdapter2(listUom, holder.uom2);
////                    holder.uom2.setEnabled(true);
////                    holder.qty2.setEnabled(true);
////                } else {
////                    holder.uom2.setAdapter(null);
////                    holder.qty2.setText("");
////                    holder.uom2.setEnabled(false);
////                    holder.qty2.setEnabled(false);
////                }
//                if (aReturn.getListUomName() != null && !aReturn.getListUomName().isEmpty()) {
//                    for (int i = 0; i < aReturn.getListUomName().size(); i++) {
//                        if (uom1.equals(aReturn.getListUomName().get(i).getUomName())) {
//                            aReturn.setUom1(aReturn.getListUomName().get(i).getId());
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        holder.uom2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//
//                if (holder.uom2.getSelectedItem() != null) {
//                    String uom2 = holder.uom2.getSelectedItem().toString();
//
//                    if (aReturn.getListUomName() != null && !aReturn.getListUomName().isEmpty()) {
//                        for (int i = 0; i < aReturn.getListUomName().size(); i++) {
//                            if (uom2.equals(aReturn.getListUomName().get(i).getUomName())) {
//                                aReturn.setUom2(aReturn.getListUomName().get(i).getId());
//                            }
//                        }
//                    }
//                } else {
//                    aReturn.setUom2(null);
//                    aReturn.setQty2(null);
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        holder.reason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                spinnerSelected = true;
                return false;
            }
        });

        holder.reason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                int getPosition = (Integer) adapterView.getTag();
                aReturn.setReason(aReturn.getItemsReason()[i]);

//                if (spinnerSelected) {
                    if (context.getReasonArrayList() != null && !context.getReasonArrayList().isEmpty()) {
                        List<String> temp = new ArrayList<>();
                        for (int a = 0; a < context.getReasonArrayList().size(); a++) {
                            if (aReturn.getItemsReason()[i].equals(context.getReasonArrayList().get(a).getDescription())) {
                                holder.catReturn.setText(context.getReasonArrayList().get(a).getType());
                                aReturn.setCategory(context.getReasonArrayList().get(a).getType());
                            }
                        }
                    }
//                    spinnerSelected = false;
//                }
            }
        });

//        holder.reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                int getPosition = (Integer) adapterView.getTag();
//                returnList.get(getPosition).setReason(aReturn.getItemsReason()[i]);
//
//                if (spinnerSelected) {
//                    if (context.getReasonArrayList() != null && !context.getReasonArrayList().isEmpty()) {
//                        List<String> temp = new ArrayList<>();
//                        for (int a = 0; a < context.getReasonArrayList().size(); a++) {
//                            if (aReturn.getItemsReason()[i].equals(context.getReasonArrayList().get(a).getDesc())) {
//                                holder.catReturn.setText(context.getReasonArrayList().get(a).getType());
//                                aReturn.setCategory(context.getReasonArrayList().get(a).getType());
//                            }
//                        }
//                    }
//
//                    spinnerSelected = false;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        if (aReturn.getItemsReason() != null && aReturn.getItemsReason().length != 0) {
            for (int i = 0; i < aReturn.getItemsReason().length; i++) {
                if (aReturn.getItemsReason()[i].equals(aReturn.getReason())) {
//                    holder.reason.setSelection(i);
                    holder.reason.setText(aReturn.getItemsReason()[i]);
                    context.setAutoCompleteAdapter(aReturn.getItemsReason(), holder.reason);
                }
            }
        }

//        if (returnList.get(position).getQty1() != null) {
//            holder.qty1.setText(String.valueOf(aReturn.getQty1()));
//        }

        if (aReturn.getUom() != null) {
            if (aReturn.getListUomName() != null && !aReturn.getListUomName().isEmpty()) {
                for (int i = 0; i < aReturn.getListUomName().size(); i++) {
                    if (aReturn.getListUomName().get(i).getId() != null) {
                        if (aReturn.getListUomName().get(i).getId().equals(aReturn.getUom())) {
                            int spinnerPosition = context.getSpinnerAdapter().getPosition(aReturn.getListUomName().get(i).getUomName());
//                            holder.uom1.setSelection(spinnerPosition);
                            holder.uom1.setText(aReturn.getListUomName().get(i).getUomName());
                            context.setAutoCompleteAdapter(listUom, holder.uom1);
                        }
                    }
                }
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView idRow, materialName, txtError, catReturn, txtNo;
        EditText idMaterial, qty1, qty2, batch, expiredDate, desc;
//        Spinner uom1, reason;
        AutoCompleteTextView uom1, reason;
        ImageView icClose;
    }
}
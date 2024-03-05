package id.co.qualitas.qubes.adapter.aspp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CollectionFormActivityNew;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.RecyclerViewMaxHeight;
import id.co.qualitas.qubes.model.Bank;
import id.co.qualitas.qubes.model.CollectionDetail;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;

public class CollectionChequeNewAdapter extends RecyclerView.Adapter<CollectionChequeNewAdapter.Holder> implements Filterable {
    private List<CollectionDetail> mList;
    private List<CollectionDetail> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionFormActivityNew mContext;
    private OnAdapterListener onAdapterListener;
    private boolean visible = false;
    Date todayDate;
    String chooseDateString, todayString;
    Calendar todayCalendar;
    private CollectionInvoiceChequeAdapter mAdapter;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private Holder dataObjectHolder;
    double totalPayment, left;
    private Database database;
    RecyclerViewMaxHeight rv;
    private CardView cvUnCheckAll, cvCheckedAll;
    private List<Invoice> listMasterInvoice, listFilteredSpinner, listAdded;
    boolean checkedAll = false;
    private LinearLayoutManager linearLayoutManMaterial;
    private SpinnerInvoiceChequeAdapter invoiceAdapter;
    private String searchInv;

    public CollectionChequeNewAdapter(CollectionFormActivityNew mContext, List<CollectionDetail> mList, OnAdapterListener onAdapterListener) {
        if (mList != null) {
            this.mList = mList;
            this.mFilteredList = mList;
        } else {
            this.mList = new ArrayList<>();
            this.mFilteredList = new ArrayList<>();
        }
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.onAdapterListener = onAdapterListener;
    }

    public void setData(List<CollectionDetail> mDataSet) {
        this.mList = mDataSet;
        this.mFilteredList = mDataSet;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mList;
                } else {
                    List<CollectionDetail> filteredList = new ArrayList<>();
                    for (CollectionDetail row : mList) {

                        /*filter by name*/
                        if (row.getNo().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<CollectionDetail>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLeft, txtTglCheque, txtPrice, txtTglCair, spnBankASPP, spnBankCust;
        TextView edtPayment;
        Button btnAdd;
        EditText edtNoCheque;
        CardView card_view;
        RecyclerView recyclerView;
        ImageView imgView;
        LinearLayout llPayment, layout, llDelete;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            llDelete = itemView.findViewById(R.id.llDelete);
            card_view = itemView.findViewById(R.id.card_view);
            spnBankCust = itemView.findViewById(R.id.spnBankCust);
            spnBankASPP = itemView.findViewById(R.id.spnBankASPP);
            txtTglCair = itemView.findViewById(R.id.txtTglCair);
            layout = itemView.findViewById(R.id.layout);
            llPayment = itemView.findViewById(R.id.llPayment);
            imgView = itemView.findViewById(R.id.imgView);
            edtNoCheque = itemView.findViewById(R.id.edtNoCheque);
            txtLeft = itemView.findViewById(R.id.txtLeft);
            txtTglCheque = itemView.findViewById(R.id.txtTglCheque);
            edtPayment = itemView.findViewById(R.id.edtPayment);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setHasFixedSize(true);
            this.onAdapterListener = onAdapterListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAdapterListener.onAdapterClick(mFilteredList.get(getAdapterPosition()));
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.aspp_row_view_coll_cheque_invoice, parent, false);
        dataObjectHolder = new Holder(itemView, onAdapterListener);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        setFormatSeparator();
        database = new Database(mContext);
        CollectionDetail detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        todayDate = Helper.getTodayDate();
        todayString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(todayDate);
        String idBankCust = Helper.isEmpty(detail.getIdBankCust(), "");
        String nameBankCust = Helper.isEmpty(detail.getBankCust(), "");
        String idBank = Helper.isEmpty(detail.getIdBankASPP(), "");
        String nameBank = Helper.isEmpty(detail.getBankNameASPP(), "");

        if (mFilteredList.get(holder.getAbsoluteAdapterPosition()).isOpen()) {
            visible = true;
            holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_up));
            holder.llPayment.setVisibility(View.VISIBLE);
            mFilteredList.get(holder.getAbsoluteAdapterPosition()).setOpen(true);
        } else {
            visible = false;
            holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_down_aspp));
            mFilteredList.get(holder.getAbsoluteAdapterPosition()).setOpen(false);
            holder.llPayment.setVisibility(View.GONE);
        }

        holder.edtPayment.setText("Rp." + format.format(detail.getTotalPayment()));
        holder.txtLeft.setText("Rp." + format.format(calculateLeft(holder.getAbsoluteAdapterPosition())));
        holder.edtNoCheque.setText(Helper.isEmpty(detail.getNo(), null));

        if (!Helper.isNullOrEmpty(detail.getTgl())) {
            String date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_4, detail.getTgl());
            holder.txtTglCheque.setText(date);
        } else {
            holder.txtTglCheque.setText(null);
        }

        if (!Helper.isNullOrEmpty(detail.getTglCair())) {
            String date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_4, detail.getTglCair());
            holder.txtTglCair.setText(date);
        } else {
            holder.txtTglCair.setText(null);
        }

        holder.spnBankCust.setText(!idBankCust.equals("") && !nameBankCust.equals("") ? idBankCust + " - " + nameBankCust : null);
        holder.spnBankASPP.setText(!idBank.equals("") && !nameBank.equals("") ? idBank + " - " + nameBank : null);

        List<Bank> bankASPPList = database.getAllBank("01");
        List<Bank> bankCustomerList = database.getAllBank("02");

        holder.edtNoCheque.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("") && !s.toString().equals("-")) {
                    detail.setNo(s.toString().trim());
                }
            }
        });

        holder.txtTglCheque.setOnClickListener(v -> {
            mContext.hideKeyboard();
            todayCalendar = Calendar.getInstance();
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(todayDate);
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int date = calendar.get(Calendar.DATE);

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DATE, dayOfMonth);

                    chooseDateString = new SimpleDateFormat(Constants.DATE_FORMAT_4).format(calendar.getTime());
                    String tglTf = new SimpleDateFormat(Constants.DATE_FORMAT_3).format(calendar.getTime());
                    detail.setTgl(tglTf);
                    holder.txtTglCheque.setText(chooseDateString);
                    holder.txtTglCheque.setError(null);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(mContext, dateSetListener, year, month, date);
            dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
            dialog.show();
        });

        holder.txtTglCair.setOnClickListener(v -> {
            mContext.hideKeyboard();
            todayCalendar = Calendar.getInstance();
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(todayDate);
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int date = calendar.get(Calendar.DATE);

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DATE, dayOfMonth);

                    chooseDateString = new SimpleDateFormat(Constants.DATE_FORMAT_4).format(calendar.getTime());
                    String tglTf = new SimpleDateFormat(Constants.DATE_FORMAT_3).format(calendar.getTime());
                    detail.setTglCair(tglTf);
                    holder.txtTglCair.setText(chooseDateString);
                    holder.txtTglCair.setError(null);
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(mContext, dateSetListener, year, month, date);
            dialog.getDatePicker().setMinDate(Helper.getTodayDate().getTime());
            dialog.show();
        });

        holder.spnBankASPP.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(mContext);

            alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);
            Button btnSearch = alertDialog.findViewById(R.id.btnSearch);
            btnSearch.setVisibility(View.GONE);

            SpinnerBankAdapter spinnerAdapter = new SpinnerBankAdapter(mContext, bankASPPList, true, (header, adapterPosition) -> {
                detail.setIdBankASPP(header.getId());
                detail.setBankNameASPP(header.getName());
                holder.spnBankASPP.setText(header.getId() + " - " + header.getName());
                alertDialog.dismiss();
            });

            LinearLayoutManager mManager = new LinearLayoutManager(mContext);
            listView.setLayoutManager(mManager);
            listView.setHasFixedSize(true);
            listView.setNestedScrollingEnabled(false);
            listView.setAdapter(spinnerAdapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    spinnerAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        });

        holder.spnBankCust.setOnClickListener(v -> {
            Dialog alertDialog = new Dialog(mContext);

            alertDialog.setContentView(R.layout.aspp_dialog_searchable_spinner);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            EditText editText = alertDialog.findViewById(R.id.edit_text);
            RecyclerView listView = alertDialog.findViewById(R.id.list_view);
            Button btnSearch = alertDialog.findViewById(R.id.btnSearch);
            btnSearch.setVisibility(View.GONE);

            SpinnerBankAdapter spinnerAdapter = new SpinnerBankAdapter(mContext, bankCustomerList, false, (header, adapterPosition) -> {
                detail.setIdBankCust(header.getId());
                detail.setBankCust(header.getName());
                holder.spnBankCust.setText(header.getId() + " - " + header.getName());
                alertDialog.dismiss();
            });

            LinearLayoutManager mManager = new LinearLayoutManager(mContext);
            listView.setLayoutManager(mManager);
            listView.setHasFixedSize(true);
            listView.setNestedScrollingEnabled(false);
            listView.setAdapter(spinnerAdapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    spinnerAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        });

        mAdapter = new CollectionInvoiceChequeAdapter(mContext, CollectionChequeNewAdapter.this, holder.getAbsoluteAdapterPosition(),
                mFilteredList.get(holder.getAbsoluteAdapterPosition()), header -> {

        });
        holder.recyclerView.setAdapter(mAdapter);

        holder.card_view.setOnClickListener(v -> {
            if (visible) {
                visible = false;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_down_aspp));
                holder.llPayment.setVisibility(View.GONE);
                mFilteredList.get(holder.getAbsoluteAdapterPosition()).setOpen(false);
            } else {
                visible = true;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_up));
                holder.llPayment.setVisibility(View.VISIBLE);
                mFilteredList.get(holder.getAbsoluteAdapterPosition()).setOpen(true);
            }
        });

        holder.edtPayment.setOnClickListener(view -> {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            final Dialog dialog = new Dialog(mContext);
            View dialogView = inflater.inflate(R.layout.aspp_dialog_amount_total, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogView);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
            TextView txtTypePayment = dialog.findViewById(R.id.txtTypePayment);
            EditText edtTotalPayment = dialog.findViewById(R.id.edtTotalPayment);
            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            Button btnSave = dialog.findViewById(R.id.btnSave);

            txtTypePayment.setText("Cheque");
            edtTotalPayment.setText(mFilteredList.get(holder.getAbsoluteAdapterPosition()).getTotalPayment() != 0
                    ? Helper.setDotCurrencyAmount(mFilteredList.get(holder.getAbsoluteAdapterPosition()).getTotalPayment()) : null);

            edtTotalPayment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Helper.setDotCurrency(edtTotalPayment, this, s);
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double qty = Double.parseDouble(edtTotalPayment.getText().toString().replace(",", ""));
                    totalPayment = qty;
                    mFilteredList.get(holder.getAbsoluteAdapterPosition()).setTotalPayment(totalPayment);
                    mFilteredList.get(holder.getAbsoluteAdapterPosition()).setInvoiceList(new ArrayList<>());
//                    notifyItemChanged(holder.getAbsoluteAdapterPosition());
                    mContext.notifyAdapter(3);
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        holder.llDelete.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(dialogView);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView txtTitle = dialog.findViewById(R.id.txtTitle);
            TextView txtDialog = dialog.findViewById(R.id.txtDialog);
            Button btnNo = dialog.findViewById(R.id.btnNo);
            Button btnYes = dialog.findViewById(R.id.btnYes);
            txtTitle.setText("Hapus");
            txtDialog.setText("Anda yakin?");
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFilteredList.remove(holder.getAbsoluteAdapterPosition());
                    mContext.notifyAdapter(3);
//                    notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                    dialog.dismiss();
                }
            });
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        holder.btnAdd.setOnClickListener(v -> {
            if (mFilteredList.get(holder.getAbsoluteAdapterPosition()).getTotalPayment() != 0) {
                addInvoice(holder.getAbsoluteAdapterPosition());
            } else {
                Toast.makeText(mContext, "Masukkan total payment cheque", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public double getTotalAmount(Material mat, int transferPosition) {
        double sisaAmount = calculateLeft(transferPosition);
        double left = sisaAmount + mat.getAmountPaid();
        return left;
    }

    public double calculateLeft(int pos) {
        double totalPaid = 0, left = 0;
        if (Helper.isNotEmptyOrNull(mFilteredList.get(pos).getInvoiceList())) {
            for (Invoice detail : mFilteredList.get(pos).getInvoiceList()) {
                for (Material mat : detail.getMaterialList()) {
                    totalPaid = totalPaid + mat.getAmountPaid();
                }
            }
            left = mFilteredList.get(pos).getTotalPayment() - totalPaid;
        }
        return left;
    }

    public double getSisaTotalAmountExInvoice(int chequePosition, String invoiceNo) {
        double sisaAmount = 0, paidAmount = 0;
        for (Invoice inv : mFilteredList.get(chequePosition).getInvoiceList()) {
            if (!inv.getNo_invoice().equals(invoiceNo)) {
                for (Material mat : inv.getMaterialList()) {
                    paidAmount = paidAmount + mat.getAmountPaid();
                }
            }
        }

        sisaAmount = mFilteredList.get(chequePosition).getTotalPayment() - paidAmount;
        return sisaAmount;
    }

    public double getTotalAmount(int idHeader) {
        double totalAmount = 0;
        totalAmount = mFilteredList.get(idHeader).getTotalPayment();
        return totalAmount;
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface OnAdapterListener {
        void onAdapterClick(CollectionDetail detail);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }

    private void addInvoice(int absoluteAdapterPosition) {
        Dialog dialog = new Dialog(mContext);

        dialog.setContentView(R.layout.aspp_dialog_searchable_spinner_product);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        cvUnCheckAll = dialog.findViewById(R.id.cvUnCheckAll);
        cvCheckedAll = dialog.findViewById(R.id.cvCheckedAll);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        EditText editText = dialog.findViewById(R.id.edit_text);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);

        rv = dialog.findViewById(R.id.rv);
        linearLayoutManMaterial = new LinearLayoutManager(mContext);
        rv.setLayoutManager(linearLayoutManMaterial);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);

        txtTitle.setText("Search Invoice");

        listAdded = new ArrayList<>();
        listMasterInvoice = new ArrayList<>();
        listMasterInvoice = new Database(mContext).getAllInvoiceCustomerCollection(mContext.getCustomer());
        listAdded = getFilteredInvoice(listMasterInvoice, absoluteAdapterPosition);
        invoiceAdapter = new SpinnerInvoiceChequeAdapter(mContext, CollectionChequeNewAdapter.this, listAdded, (nameItem, adapterPosition) -> {
        });
        rv.setAdapter(invoiceAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                invoiceAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cvCheckedAll.setOnClickListener(v -> {
            if (listFilteredSpinner == null) listFilteredSpinner = new ArrayList<>();
            checkedAll = false;
            if (!listFilteredSpinner.isEmpty()) {
                for (Invoice mat : listFilteredSpinner) mat.setCheckedInvoice(checkedAll);
            } else {
                for (Invoice mat : listAdded) mat.setCheckedInvoice(checkedAll);
            }
            invoiceAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.VISIBLE);
            cvCheckedAll.setVisibility(View.GONE);
        });

        cvUnCheckAll.setOnClickListener(v -> {
            if (listFilteredSpinner == null) listFilteredSpinner = new ArrayList<>();
            checkedAll = true;
            if (!listFilteredSpinner.isEmpty()) {
                for (Invoice mat : listFilteredSpinner) mat.setCheckedInvoice(checkedAll);
            } else {
                for (Invoice mat : listAdded) mat.setCheckedInvoice(checkedAll);
            }
            invoiceAdapter.notifyDataSetChanged();
            cvUnCheckAll.setVisibility(View.GONE);
            cvCheckedAll.setVisibility(View.VISIBLE);
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnSave.setOnClickListener(v -> {
            List<Invoice> addList = new ArrayList<>();
            for (Invoice mat : listAdded) {
                if (mat.isCheckedInvoice()) addList.add(mat);
            }
            addNewInvoice(addList, absoluteAdapterPosition);
            dialog.dismiss();
        });
    }

    private List<Invoice> getFilteredInvoice(List<Invoice> listMasterInvoice, int absoluteAdapterPosition) {
        List<Invoice> filteredInvoice = new ArrayList<>();
        if (mFilteredList.get(absoluteAdapterPosition).getInvoiceList() == null) {
            mFilteredList.get(absoluteAdapterPosition).setInvoiceList(new ArrayList<>());
        }
        for (Invoice detail : listMasterInvoice) {
            boolean exist = false;
            for (Invoice detail2 : mFilteredList.get(absoluteAdapterPosition).getInvoiceList()) {
                if (detail.getNo_invoice().equals(detail2.getNo_invoice())) exist = true;
            }
            if (!exist) filteredInvoice.add(detail);
        }
        return filteredInvoice;
    }

    private void addNewInvoice(List<Invoice> addedList, int absoluteAdapterPosition) {
        if (mFilteredList.get(absoluteAdapterPosition).getInvoiceList() == null) {
            mFilteredList.get(absoluteAdapterPosition).setInvoiceList(new ArrayList<>());
        }
        List<Invoice> invoiceList = mFilteredList.get(absoluteAdapterPosition).getInvoiceList();
        for (Invoice detail : invoiceList) {
            for (Invoice detail2 : addedList) {
                if (detail.getNo_invoice().equals(detail2.getNo_invoice())) {
                    addedList.remove(detail2);
                }
            }
        }
        invoiceList.addAll(addedList);
        mFilteredList.get(absoluteAdapterPosition).setInvoiceList(invoiceList);
        notifyDataSetChanged();
    }

    public void setCheckedAll() {
        int checked = 0;
        for (Invoice mat : listAdded) {
            if (mat.isCheckedInvoice()) {
                checked++;
            }
        }
        if (checked == listAdded.size()) {
            checkedAll = true;
            cvUnCheckAll.setVisibility(View.GONE);
            cvCheckedAll.setVisibility(View.VISIBLE);
        } else {
            checkedAll = false;
            cvUnCheckAll.setVisibility(View.VISIBLE);
            cvCheckedAll.setVisibility(View.GONE);
        }
        invoiceAdapter.notifyDataSetChanged();
    }

    public void setFilteredData(List<Invoice> mFilteredList) {
        listFilteredSpinner = new ArrayList<>();
        listFilteredSpinner.addAll(mFilteredList);

        int checked = 0;
        for (Invoice mat : listFilteredSpinner) {
            if (mat.isCheckedInvoice()) {
                checked++;
            }
        }
        if (checked == listFilteredSpinner.size()) {
            checkedAll = true;
            cvUnCheckAll.setVisibility(View.GONE);
            cvCheckedAll.setVisibility(View.VISIBLE);
        } else {
            checkedAll = false;
            cvUnCheckAll.setVisibility(View.VISIBLE);
            cvCheckedAll.setVisibility(View.GONE);
        }
        invoiceAdapter.notifyDataSetChanged();
    }
}

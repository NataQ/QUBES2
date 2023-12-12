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
import id.co.qualitas.qubes.activity.aspp.CollectionFormActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Bank;
import id.co.qualitas.qubes.model.CollectionDetail;
import id.co.qualitas.qubes.model.Material;

public class CollectionGiroAdapter extends RecyclerView.Adapter<CollectionGiroAdapter.Holder> implements Filterable {
    private Database database;
    private List<CollectionDetail> mList;
    private List<CollectionDetail> mFilteredList;
    private LayoutInflater mInflater;
    private CollectionFormActivity mContext;
    private OnAdapterListener onAdapterListener;
    Date todayDate;
    String chooseDateString, todayString;
    Calendar todayCalendar;
    private CollectionGiroPaymentAdapter mAdapter;
    private boolean visible = false;
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private List<Material> materialList = new ArrayList<>();
    private Holder dataObjectHolder;
    double totalPayment, left;

    public CollectionGiroAdapter(CollectionFormActivity mContext, List<CollectionDetail> mList, OnAdapterListener onAdapterListener) {
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

    public void updateKurangBayar(int pos) {
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(pos);
        }
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtLeft, txtTglGiro, txtPrice, txtTglCair, spnBankASPP, spnBankCust;
        EditText edtPayment, edtNoGiro;
        CardView card_view;
        RecyclerView recyclerView;
        ImageView imgView;
        LinearLayout llPayment, layout, llDelete;
        OnAdapterListener onAdapterListener;

        public Holder(View itemView, OnAdapterListener onAdapterListener) {
            super(itemView);
            llDelete = itemView.findViewById(R.id.llDelete);
            card_view = itemView.findViewById(R.id.card_view);
            spnBankCust = itemView.findViewById(R.id.spnBankCust);
            spnBankASPP = itemView.findViewById(R.id.spnBankASPP);
            txtTglCair = itemView.findViewById(R.id.txtTglCair);
            layout = itemView.findViewById(R.id.layout);
            llPayment = itemView.findViewById(R.id.llPayment);
            imgView = itemView.findViewById(R.id.imgView);
            edtNoGiro = itemView.findViewById(R.id.edtNoGiro);
            txtLeft = itemView.findViewById(R.id.txtLeft);
            txtTglGiro = itemView.findViewById(R.id.txtTglGiro);
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
        View itemView = mInflater.inflate(R.layout.aspp_row_view_coll_giro, parent, false);
        dataObjectHolder = new Holder(itemView, onAdapterListener);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        setFormatSeparator();
        database = new Database(mContext);
        CollectionDetail detail = mFilteredList.get(holder.getAbsoluteAdapterPosition());
        materialList = detail.getMaterialList();
        todayDate = Helper.getTodayDate();
        todayString = new SimpleDateFormat(Constants.DATE_FORMAT_5).format(todayDate);
        String idBankCust = Helper.isEmpty(detail.getIdBankCust(), "");
        String nameBankCust = Helper.isEmpty(detail.getBankCust(), "");

        String idBank = Helper.isEmpty(detail.getIdBankASPP(), "");
        String nameBank = Helper.isEmpty(detail.getBankNameASPP(), "");

        if (!Helper.isNullOrEmpty(detail.getTgl())) {
            String date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_4, detail.getTgl());
            holder.txtTglGiro.setText(date);
        } else {
            holder.txtTglGiro.setText(null);
        }

        if (!Helper.isNullOrEmpty(detail.getTglCair())) {
            String date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_4, detail.getTglCair());
            holder.txtTglCair.setText(date);
        } else {
            holder.txtTglCair.setText(null);
        }

        List<Bank> bankASPPList = database.getAllBank("Bank ASPP");
        List<Bank> bankCustomerList = database.getAllBank("Bank Customer");

        holder.edtPayment.setText(Helper.setDotCurrencyAmount(detail.getTotalPayment()));
        holder.spnBankCust.setText(!idBankCust.equals("") && !nameBankCust.equals("") ? idBankCust + " - " + nameBankCust : null);
        holder.spnBankASPP.setText(!idBank.equals("") && !nameBank.equals("") ? idBank + " - " + nameBank : null);

        holder.edtNoGiro.addTextChangedListener(new TextWatcher() {
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

        holder.txtTglGiro.setOnClickListener(v -> {
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
                    holder.txtTglGiro.setText(chooseDateString);
                    holder.txtTglGiro.setError(null);
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

        mAdapter = new CollectionGiroPaymentAdapter(mContext, CollectionGiroAdapter.this, holder.getAbsoluteAdapterPosition(), materialList, header -> {

        });
        holder.recyclerView.setAdapter(mAdapter);

        holder.card_view.setOnClickListener(v -> {
            if (visible) {
                visible = false;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_down_aspp));
                holder.llPayment.setVisibility(View.GONE);
            } else {
                visible = true;
                holder.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_drop_up));
                holder.llPayment.setVisibility(View.VISIBLE);
            }
        });

        holder.edtPayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Helper.setDotCurrency(holder.edtPayment, this, s);
                if (!s.toString().equals("") && !s.toString().equals("-")) {
                    double qty = Double.parseDouble(s.toString().replace(",", ""));
                    if (qty < 0) {
                        Toast.makeText(mContext, "Tidak boleh kurang dari 0", Toast.LENGTH_SHORT).show();
                        holder.edtPayment.setText(s.toString().substring(0, s.toString().length() - 1));
                    } else {
                        totalPayment = qty;
                        detail.setTotalPayment(totalPayment);
                    }
                } else {
                    totalPayment = 0;
                    detail.setTotalPayment(0);
                }
                setLeft(holder.getAbsoluteAdapterPosition());
            }
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
                    notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                    mContext.updateKurangBayarDelete();
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
    }

    public double getTotalAmount(int idHeader) {
        double totalAmount = 0;
        totalAmount = mFilteredList.get(idHeader).getTotalPayment();
        return totalAmount;
    }

    public void setLeft(int pos) {
        double totalPaid = 0;
        for (Material mat : materialList) {
//            if (mat.isChecked()) {
            totalPaid = totalPaid + mat.getAmountPaid();
//            }
        }

        left = totalPayment - totalPaid;
        dataObjectHolder.txtLeft.setText("Rp." + format.format(left));
        mFilteredList.get(pos).setLeft(left);
    }

    public double calculateLeft(double qty, int pos) {
        double totalPaid = 0;
        for (int i = 0; i < materialList.size(); i++) {
            Material mat = materialList.get(i);
//            if (mat.isChecked()) {
            if (i == pos) {
                totalPaid = totalPaid + qty;
            } else {
                totalPaid = totalPaid + mat.getAmountPaid();
            }
//            }
        }
        left = totalPayment - totalPaid;
        return left;
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
}

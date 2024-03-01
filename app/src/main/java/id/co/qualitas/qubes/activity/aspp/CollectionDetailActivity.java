package id.co.qualitas.qubes.activity.aspp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.CollectionChequeDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionGiroDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionPaymentInvoiceDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionPaymentItemDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionTransferDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.CollectionDetail;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class CollectionDetailActivity extends BaseActivity {
    private ImageView imgBack;
    private RecyclerView recyclerViewCash, recyclerViewTransfer, recyclerViewGiro, recyclerViewCheque, recyclerViewLain;
    private TextView txtPaymentLain, txtPaymentCash;
    private TextView txtPaymentNo;
    private TextView txtCash, txtTransfer, txtGiro, txtCheq, txtLain;
    private LinearLayout buttonCash, buttonTransfer, buttonCheq, buttonGiro, buttonLain;
    private LinearLayout llCash, llTransfer, llGiro, llCheque, llLain;
    private List<CollectionDetail> mListTransfer, mListGiro, mListCheque;
    private List<Invoice> mListCash, mListLain;
    private CollectionHeader collectionHeader;
    private CollectionPaymentInvoiceDetailAdapter mAdapterCash, mAdapterLain;
    private CollectionTransferDetailAdapter mAdapterTransfer;
    private CollectionGiroDetailAdapter mAdapterGiro;
    private CollectionChequeDetailAdapter mAdapterCheque;
    private int colLFrom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_collection_detail);

        initialize();
        initData();
        setCashView();
        setTransferView();
        setGiroView();
        setChequeView();
        setLainView();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CollectionDetailActivity.this);
        });

        buttonCash.setOnClickListener(v -> {
            setSelectView(1);
        });

        buttonTransfer.setOnClickListener(v -> {
            setSelectView(2);
        });

        buttonGiro.setOnClickListener(v -> {
            setSelectView(3);
        });

        buttonCheq.setOnClickListener(v -> {
            setSelectView(4);
        });

        buttonLain.setOnClickListener(v -> {
            setSelectView(5);
        });
    }

    private void setCashView() {
        mAdapterCash = new CollectionPaymentInvoiceDetailAdapter(this, mListCash, header -> {

        });
        recyclerViewCash.setAdapter(mAdapterCash);
    }

    private void setTransferView() {
        mAdapterTransfer = new CollectionTransferDetailAdapter(this, mListTransfer, header -> {

        });
        recyclerViewTransfer.setAdapter(mAdapterTransfer);
    }

    private void setGiroView() {
        mAdapterGiro = new CollectionGiroDetailAdapter(this, mListGiro, header -> {

        });
        recyclerViewGiro.setAdapter(mAdapterGiro);
    }

    private void setChequeView() {
        mAdapterCheque = new CollectionChequeDetailAdapter(this, mListCheque, header -> {

        });
        recyclerViewCheque.setAdapter(mAdapterCheque);
    }

    private void setLainView() {
        mAdapterLain = new CollectionPaymentInvoiceDetailAdapter(this, mListLain, header -> {

        });
        recyclerViewLain.setAdapter(mAdapterLain);
    }

    private void initData() {
        mListTransfer = new ArrayList<>();
        mListGiro = new ArrayList<>();
        mListCheque = new ArrayList<>();
        mListCash = new ArrayList<>();
        mListLain = new ArrayList<>();

        if (SessionManagerQubes.getCollectionHistoryHeader() == null) {
            onBackPressed();
            setToast("Gagal ambil data. Silahkan coba lagi");
        } else {
            collectionHeader = SessionManagerQubes.getCollectionHistoryHeader();

            txtPaymentNo.setText(Helper.isEmpty(collectionHeader.getIdHeader(), ""));

            colLFrom = SessionManagerQubes.getCollectionSource();

            mListTransfer.addAll(collectionHeader.getTfList());
            mListGiro.addAll(collectionHeader.getGiroList());
            mListCheque.addAll(collectionHeader.getChequeList());

            if (Helper.isNotEmptyOrNull(collectionHeader.getCashList())) {
                buttonCash.setVisibility(View.VISIBLE);
                mListCash.addAll(collectionHeader.getCashList().get(0).getInvoiceList());
                txtPaymentCash.setText("Rp. " + format.format(collectionHeader.getCashList().get(0).getTotalPayment()));
            } else {
                buttonCash.setVisibility(View.GONE);
                llCash.setVisibility(View.GONE);
            }

            if (Helper.isNotEmptyOrNull(mListTransfer)) {
                buttonTransfer.setVisibility(View.VISIBLE);
                if (Helper.isEmptyOrNull(collectionHeader.getCashList())) {
                    buttonTransfer.callOnClick();
                }
            } else {
                buttonTransfer.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
            }

            if (Helper.isNotEmptyOrNull(mListGiro)) {
                buttonGiro.setVisibility(View.VISIBLE);
                if (Helper.isEmptyOrNull(collectionHeader.getCashList()) && Helper.isEmptyOrNull(mListTransfer)) {
                    buttonGiro.callOnClick();
                }
            } else {
                buttonGiro.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
            }

            if (Helper.isNotEmptyOrNull(mListCheque)) {
                buttonCheq.setVisibility(View.VISIBLE);
                if (Helper.isEmptyOrNull(collectionHeader.getCashList()) && Helper.isEmptyOrNull(mListTransfer) && Helper.isEmptyOrNull(mListGiro)) {
                    buttonCheq.callOnClick();
                }
            } else {
                buttonCheq.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
            }

            if (Helper.isNotEmptyOrNull(collectionHeader.getLainList())) {
                buttonLain.setVisibility(View.VISIBLE);
                mListLain.addAll(collectionHeader.getLainList().get(0).getInvoiceList());
                txtPaymentLain.setText("Rp. " + format.format(collectionHeader.getLainList().get(0).getTotalPayment()));
                if (Helper.isEmptyOrNull(collectionHeader.getCashList()) && Helper.isEmptyOrNull(mListTransfer) && Helper.isEmptyOrNull(mListGiro) && Helper.isEmptyOrNull(mListCheque)) {
                    buttonLain.callOnClick();
                }
            } else {
                buttonLain.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
            }
        }
    }

    private void setSelectView(int posTab) {
        switch (posTab) {
            case 1:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                if (Helper.isNotEmptyOrNull(mListCash)) {
                    llCash.setVisibility(View.VISIBLE);
                }
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                break;
            case 2:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                if (Helper.isNotEmptyOrNull(mListTransfer)) {
                    llTransfer.setVisibility(View.VISIBLE);
                }
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                break;
            case 3:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                if (Helper.isNotEmptyOrNull(mListGiro)) {
                    llGiro.setVisibility(View.VISIBLE);
                }
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                break;
            case 4:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                if (Helper.isNotEmptyOrNull(mListCheque)) {
                    llCheque.setVisibility(View.VISIBLE);
                }
                llLain.setVisibility(View.GONE);
                break;
            case 5:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                if (Helper.isNotEmptyOrNull(mListLain)) {
                    llLain.setVisibility(View.VISIBLE);
                }
                break;
            case 6:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                break;
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        txtPaymentNo = findViewById(R.id.txtPaymentNo);
        txtPaymentLain = findViewById(R.id.txtPaymentLain);
        txtPaymentCash = findViewById(R.id.txtPaymentCash);

        recyclerViewCash = findViewById(R.id.recyclerViewCash);
        recyclerViewCash.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCash.setHasFixedSize(true);
        recyclerViewTransfer = findViewById(R.id.recyclerViewTransfer);
        recyclerViewTransfer.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTransfer.setHasFixedSize(true);
        recyclerViewGiro = findViewById(R.id.recyclerViewGiro);
        recyclerViewGiro.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGiro.setHasFixedSize(true);
        recyclerViewCheque = findViewById(R.id.recyclerViewCheque);
        recyclerViewCheque.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCheque.setHasFixedSize(true);
        recyclerViewLain = findViewById(R.id.recyclerViewLain);
        recyclerViewLain.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLain.setHasFixedSize(true);
        imgLogOut = findViewById(R.id.imgLogOut);
        imgBack = findViewById(R.id.imgBack);
        txtCash = findViewById(R.id.txtCash);
        txtTransfer = findViewById(R.id.txtTransfer);
        txtGiro = findViewById(R.id.txtGiro);
        txtCheq = findViewById(R.id.txtCheq);
        txtLain = findViewById(R.id.txtLain);
        buttonCash = findViewById(R.id.buttonCash);
        buttonTransfer = findViewById(R.id.buttonTransfer);
        buttonCheq = findViewById(R.id.buttonCheq);
        buttonGiro = findViewById(R.id.buttonGiro);
        buttonLain = findViewById(R.id.buttonLain);
        llLain = findViewById(R.id.llLain);
        llCash = findViewById(R.id.llCash);
        llTransfer = findViewById(R.id.llTransfer);
        llGiro = findViewById(R.id.llGiro);
        llCheque = findViewById(R.id.llCheque);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
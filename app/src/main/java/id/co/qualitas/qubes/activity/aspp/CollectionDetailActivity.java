package id.co.qualitas.qubes.activity.aspp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
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
import id.co.qualitas.qubes.adapter.aspp.CollectionCashAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionChequeAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionChequeDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionGiroAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionGiroDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionKreditAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionLainAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionPaymentDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionTransferAdapter;
import id.co.qualitas.qubes.adapter.aspp.CollectionTransferDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.CollectionCheque;
import id.co.qualitas.qubes.model.CollectionGiro;
import id.co.qualitas.qubes.model.CollectionTransfer;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class CollectionDetailActivity extends BaseActivity {
    private ImageView imgBack;
    private RecyclerView recyclerViewCash, recyclerViewTransfer, recyclerViewGiro, recyclerViewCheque, recyclerViewLain, recyclerViewKredit;
    private TextView txtInvNo, txtDate, txtOrderNo, txtAmount;
    private LinearLayout llOrder, llInvoice;
    private TextView txtCash, txtTransfer, txtGiro, txtCheq, txtLain, txtKredit;
    private LinearLayout buttonCash, buttonTransfer, buttonCheq, buttonGiro, buttonLain, buttonKredit;
    private LinearLayout llCash, llTransfer, llGiro, llCheque, llLain, llKredit;
    private List<CollectionTransfer> mListTransfer;
    private List<CollectionGiro> mListGiro;
    private List<CollectionCheque> mListCheque;
    private List<Material> mListCash, mListLain, mListKredit;
    private Invoice collectionHeader;
    private CollectionPaymentDetailAdapter mAdapterCash, mAdapterLain, mAdapterKredit;
    private CollectionTransferDetailAdapter mAdapterTransfer;
    private CollectionGiroDetailAdapter mAdapterGiro;
    private CollectionChequeDetailAdapter mAdapterCheque;
    private int colLFrom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_collection_detail);

        initProgress();
        initialize();
        initData();
        setCashView();
        setTransferView();
        setGiroView();
        setChequeView();
        setLainView();
        setKreditView();

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

        buttonKredit.setOnClickListener(v -> {
            setSelectView(6);
        });
    }

    private void setCashView() {
        mListCash = new ArrayList<>();
        mListCash.addAll(initDataMaterial());

        mAdapterCash = new CollectionPaymentDetailAdapter(this, mListCash, header -> {

        });
        recyclerViewCash.setAdapter(mAdapterCash);
    }

    private void setKreditView() {
        mListKredit = new ArrayList<>();
        mListKredit.addAll(initDataMaterial());

        mAdapterKredit = new CollectionPaymentDetailAdapter(this, mListKredit, header -> {

        });
        recyclerViewKredit.setAdapter(mAdapterKredit);
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
        mListLain = new ArrayList<>();
        mListLain.addAll(initDataMaterial());

        mAdapterLain = new CollectionPaymentDetailAdapter(this, mListLain, header -> {

        });
        recyclerViewLain.setAdapter(mAdapterLain);
    }

    private List<Material> initDataMaterial() {
        List<Material> mList = new ArrayList<>();
        mList.add(new Material("11001", "Kratingdaeng", 1000000, 1000000));
        mList.add(new Material("11030", "Redbull", 2000000, 2000000));
        mList.add(new Material("31020", "You C1000 Vitamin Orange", 890000, 5000000));
        return mList;
    }

    private void initData() {
        mListTransfer = new ArrayList<>();
        mListGiro = new ArrayList<>();
        mListCheque = new ArrayList<>();

        mListTransfer.add(new CollectionTransfer(Helper.getTodayDate(Constants.DATE_FORMAT_4), initDataMaterial()));
        mListTransfer.add(new CollectionTransfer(Helper.getTodayDate(Constants.DATE_FORMAT_4), initDataMaterial()));

        mListGiro.add(new CollectionGiro("000001", "21-04-2023", "17-04-2023", "BM", "Bank MONAS", "BJ", "Bank JAYA", initDataMaterial()));
        mListGiro.add(new CollectionGiro("000002", "27-07-2023", "20-06-2023", "BCA", "Bank Central Asia", "Mandiri", "Bank Mandiri", initDataMaterial()));

        mListCheque.add(new CollectionCheque("000002", "27-07-2023", "20-06-2023", "BCA", "Bank Central Asia", "Mandiri", "Bank Mandiri", initDataMaterial()));
        mListCheque.add(new CollectionCheque("000002", "27-07-2023", "20-06-2023", "BCA", "Bank Central Asia", "Mandiri", "Bank Mandiri", initDataMaterial()));

        if (SessionManagerQubes.getCollectionHeader() == null) {
            onBackPressed();
            setToast("Gagal ambil data. Silahkan coba lagi");
        } else {
            collectionHeader = SessionManagerQubes.getCollectionHeader();

            colLFrom = SessionManagerQubes.getCollectionSource();

            if (colLFrom == 3) {
                llOrder.setVisibility(View.VISIBLE);
                llInvoice.setVisibility(View.GONE);
                buttonKredit.setVisibility(View.VISIBLE);
            } else {
                llOrder.setVisibility(View.GONE);
                llInvoice.setVisibility(View.VISIBLE);
                buttonKredit.setVisibility(View.GONE);
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
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.VISIBLE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.GONE);
                break;
            case 2:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.VISIBLE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.GONE);
                break;
            case 3:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.VISIBLE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.GONE);
                break;
            case 4:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.VISIBLE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.GONE);
                break;
            case 5:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.VISIBLE);
                llKredit.setVisibility(View.GONE);
                break;
            case 6:
                buttonCash.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonTransfer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonGiro.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonCheq.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonLain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type));
                buttonKredit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_type_select));

                txtCash.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtTransfer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtGiro.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtCheq.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtLain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.gray5_aspp));
                txtKredit.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.new_blue));

                llCash.setVisibility(View.GONE);
                llTransfer.setVisibility(View.GONE);
                llGiro.setVisibility(View.GONE);
                llCheque.setVisibility(View.GONE);
                llLain.setVisibility(View.GONE);
                llKredit.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        recyclerViewCash = findViewById(R.id.recyclerViewCash);
        recyclerViewCash.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCash.setHasFixedSize(true);
        recyclerViewKredit = findViewById(R.id.recyclerViewKredit);
        recyclerViewKredit.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewKredit.setHasFixedSize(true);
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
        txtOrderNo = findViewById(R.id.txtOrderNo);
        txtInvNo = findViewById(R.id.txtInvNo);
        txtAmount = findViewById(R.id.txtAmount);
        txtDate = findViewById(R.id.txtDate);
        txtCash = findViewById(R.id.txtCash);
        txtTransfer = findViewById(R.id.txtTransfer);
        txtGiro = findViewById(R.id.txtGiro);
        txtCheq = findViewById(R.id.txtCheq);
        txtLain = findViewById(R.id.txtLain);
        txtKredit = findViewById(R.id.txtKredit);
        buttonCash = findViewById(R.id.buttonCash);
        buttonTransfer = findViewById(R.id.buttonTransfer);
        buttonCheq = findViewById(R.id.buttonCheq);
        buttonGiro = findViewById(R.id.buttonGiro);
        buttonLain = findViewById(R.id.buttonLain);
        buttonKredit = findViewById(R.id.buttonKredit);
        llLain = findViewById(R.id.llLain);
        llKredit = findViewById(R.id.llKredit);
        llCash = findViewById(R.id.llCash);
        llTransfer = findViewById(R.id.llTransfer);
        llGiro = findViewById(R.id.llGiro);
        llCheque = findViewById(R.id.llCheque);
        llOrder = findViewById(R.id.llOrder);
        llInvoice = findViewById(R.id.llInvoice);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
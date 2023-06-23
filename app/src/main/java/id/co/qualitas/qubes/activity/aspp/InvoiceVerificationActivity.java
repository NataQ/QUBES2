package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.InvoiceVerificationAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.User;

public class InvoiceVerificationActivity extends BaseActivity {
    private InvoiceVerificationAdapter mAdapter;
    private List<Invoice> mList;
    private MovableFloatingActionButton btnAdd;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_invoice_verification);

        init();
        initialize();
        initData();

        mAdapter = new InvoiceVerificationAdapter(this, mList, header -> {
        });

        recyclerView.setAdapter(mAdapter);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, InvoiceVerificationAddActivity.class);
            startActivity(intent);
        });

        btnSubmit.setOnClickListener(v -> {
            openDialogSignature();
        });
    }

    private void openDialogSignature() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_signature);
        Button btnClear, btnSubmit;
        btnClear = dialog.findViewById(R.id.btnClear);
        btnSubmit = dialog.findViewById(R.id.btnSubmit);
        final SignaturePad signaturePad = dialog.findViewById(R.id.signature_pad);

        Resources resources = getResources();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final boolean[] cleared = {false};

        btnClear.setOnClickListener(v -> {
            signaturePad.clear();
            cleared[0] = true;
        });

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                try {
                    cleared[0] = false;
                } catch (Exception ignored) {

                }
            }

            @Override
            public void onClear() {
            }
        });

        btnSubmit.setOnClickListener(v -> {
            if (cleared[0]) {
            } else {
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Invoice("DKA495486", "TOKO SIDIK HALIM", "0GV43", 884736, 0, "1 June 2023"));
        mList.add(new Invoice("DKA496933", "TOKO SIDIK HALIM", "0GV43", 39294, 0, "2 June 2023"));
        mList.add(new Invoice("DKA492540", "SARI SARI (TK)", "0WJ42", 14024448, 0, "3 June 2023"));
        mList.add(new Invoice("DKA402541", "TOKO SIDIK HALIM", "0WJ42", 9363600, 0, "4 June 2023"));
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
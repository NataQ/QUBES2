package id.co.qualitas.qubes.activity.aspp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.User;

public class InstructionPhotoActivity extends BaseActivity {
    private ImageView imgBack;
    private Button btnSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_instruction_photo);

        init();
        initialize();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnSave.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgBack = findViewById(R.id.imgBack);
        btnSave = findViewById(R.id.btnSave);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
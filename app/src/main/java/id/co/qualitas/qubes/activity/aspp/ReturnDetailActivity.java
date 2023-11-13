package id.co.qualitas.qubes.activity.aspp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.ReturnAddAdapter;
import id.co.qualitas.qubes.adapter.aspp.ReturnDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.SpinnerProductReturnAdapter;
import id.co.qualitas.qubes.adapter.aspp.SummaryDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class ReturnDetailActivity extends BaseActivity {
    private ReturnDetailAdapter mAdapter;
    private List<Material> mList;
    private Button btnAdd, btnSave;
    private TextView txtReturnDate;
    private String today;
    private Material detailMatPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_return_detail);

        initialize();
        initData();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(ReturnDetailActivity.this);
        });
    }

    private void initData() {
        today = Helper.getTodayDate(Constants.DATE_FORMAT_3);
        mList = new ArrayList<>();
        mList.addAll(database.getAllReturn(SessionManagerQubes.getOutletHeader().getId()));
        if (mList != null && mList.size() != 0) {
            today = mList.get(0).getDate();
            txtReturnDate.setText(Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, today));
        } else {
            txtReturnDate.setText(Helper.getTodayDate(Constants.DATE_FORMAT_5));
        }

        mAdapter = new ReturnDetailAdapter(this, mList, header -> {
        });

        recyclerView.setAdapter(mAdapter);

        if (Helper.isEmptyOrNull(mList)) {
            recyclerView.setVisibility(View.GONE);
            llNoData.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
        }
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = findViewById(R.id.llNoData);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgBack);
        txtReturnDate = findViewById(R.id.txtReturnDate);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void openDialogPhoto(Material detail, int pos) {
        detailMatPhoto = detail;

        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_attach_photo);

        LinearLayout layoutUpload = dialog.findViewById(R.id.layoutUpload);
        LinearLayout layoutGallery = dialog.findViewById(R.id.layoutGallery);
        LinearLayout layoutCamera = dialog.findViewById(R.id.layoutCamera);
        ImageView photo = dialog.findViewById(R.id.photo);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setVisibility(View.GONE);

        if (detailMatPhoto.getPhotoReason() != null) {
            layoutUpload.setVisibility(View.GONE);
            photo.setVisibility(View.VISIBLE);
            Utils.loadImageFit(ReturnDetailActivity.this, detailMatPhoto.getPhotoReason(), photo);
        } else {
            photo.setVisibility(View.GONE);
            layoutUpload.setVisibility(View.VISIBLE);
        }

        layoutGallery.setVisibility(View.GONE);
        layoutCamera.setVisibility(View.GONE);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
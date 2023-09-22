package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.StockRequestHeaderAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.MovableFloatingActionButton;
import id.co.qualitas.qubes.model.Attachment;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;

public class CreateNooActivity extends BaseActivity {
    private Button btnSave;
    private Spinner spnSuku, spnStatusToko, spnStatusNpwp;
    private LinearLayout llKTP, llNPWP, llOutlet;
    private int imageType = 0;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int GALLERY_REQUEST_CODE = 105;
    Attachment attachmentKtp, attachmentNpwp, attachmentOutlet;
    ImageView imgKTP, imgNPWP, imgOutlet;
    ImageView imgAddKTP, imgAddNPWP, imgAddOutlet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_create_noo);

        init();
        initialize();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(CreateNooActivity.this);
        });

        btnSave.setOnClickListener(v -> {
            onBackPressed();
        });
        setDropDown();

        llKTP.setOnClickListener(view -> {
            openDialogPhoto();
        });
    }

    public void openDialogPhoto() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_attach_photo);

        LinearLayout layoutGallery = dialog.findViewById(R.id.layoutGallery);
        LinearLayout layoutCamera = dialog.findViewById(R.id.layoutCamera);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        layoutGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                askPermission();
            }
        });

        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                switch (imageType) {
                    case 1:
//                        saveAllData();//ktp
                        Helper.setItemParam(Constants.IMAGE_TYPE, "ktp");
                        Helper.takePhoto(CreateNooActivity.this);
                        break;
                    case 2:
//                        saveAllData();//npwp click
                        Helper.setItemParam(Constants.IMAGE_TYPE, "npwp");
                        Helper.takePhoto(CreateNooActivity.this);
                        break;
                    case 3:
//                        saveAllData();//click outlet
                        Helper.setItemParam(Constants.IMAGE_TYPE, "outlet");
                        Helper.takePhoto(CreateNooActivity.this);
                        break;
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setDropDown() {
        List<String> suku = new ArrayList<>();
        suku.add("--");
        suku.add("Tiong Hua");
        suku.add("Indonesia");

        List<String> statusToko = new ArrayList<>();
        statusToko.add("--");
        statusToko.add("Milik Sendiri");
        statusToko.add("Sewa");

        List<String> statusNPWP = new ArrayList<>();
        statusNPWP.add("--");
        statusNPWP.add("PKP");
        statusNPWP.add("Non PKP");

        setSpinnerData(suku, spnSuku);
        setSpinnerData(statusToko, spnStatusToko);
        setSpinnerData(statusNPWP, spnStatusNpwp);
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        spnStatusNpwp = findViewById(R.id.spnStatusNpwp);
        spnSuku = findViewById(R.id.spnSuku);
        spnStatusToko = findViewById(R.id.spnStatusToko);
        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);
        btnSave = findViewById(R.id.btnSave);
        llKTP = findViewById(R.id.llKTP);
        llNPWP = findViewById(R.id.llNPWP);
        llOutlet = findViewById(R.id.llOutlet);
        imgKTP = findViewById(R.id.imgKTP);
        imgNPWP = findViewById(R.id.imgNPWP);
        imgOutlet = findViewById(R.id.imgOutlet);
        imgAddKTP = findViewById(R.id.imgAddKTP);
        imgAddNPWP = findViewById(R.id.imgAddNPWP);
        imgAddOutlet = findViewById(R.id.imgAddOutlet);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //foto
    public void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
        } else {
            openGallery();
        }
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            setToast("This permission(s) required");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getData() != null) {
                onSelectFromGalleryResult(data);
            }

        } else {
            setToast("Failed to Get Image");
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImage = data.getData();
        String path = getImageFilePath(selectedImage);
        File pathImg = new File(path);

        byte[] imgByte = getByteArrayFromUriGallery(Uri.fromFile(pathImg));

        switch (imageType) {
            case 1:
                attachmentKtp = new Attachment();
                attachmentKtp.setImgUri(Uri.fromFile(pathImg));
                attachmentKtp.setImg(imgByte);
                attachmentKtp.setFile_description("ktp");
                attachmentKtp.setMime_type(Helper.getMimeType(getApplicationContext(), selectedImage));
                attachmentKtp.setFile_name(pathImg.getName());
                imgKTP.setImageURI(selectedImage);
                imgKTP.setVisibility(View.VISIBLE);
                imgAddKTP.setVisibility(View.GONE);
                break;
            case 2:
                attachmentNpwp = new Attachment();
                attachmentNpwp.setImgUri(Uri.fromFile(pathImg));
                attachmentNpwp.setImg(imgByte);
                attachmentNpwp.setFile_description("npwp");
                attachmentNpwp.setMime_type(Helper.getMimeType(getApplicationContext(), selectedImage));
                attachmentNpwp.setFile_name(pathImg.getName());
                imgNPWP.setImageURI(selectedImage);
                imgNPWP.setVisibility(View.VISIBLE);
                imgAddNPWP.setVisibility(View.GONE);
                break;
            case 3:
                attachmentOutlet = new Attachment();
                attachmentOutlet.setImgUri(Uri.fromFile(pathImg));
                attachmentOutlet.setImg(imgByte);
                attachmentOutlet.setFile_description("outlet");
                attachmentOutlet.setMime_type(Helper.getMimeType(getApplicationContext(), selectedImage));
                attachmentOutlet.setFile_name(pathImg.getName());
                imgOutlet.setImageURI(selectedImage);
                imgOutlet.setVisibility(View.VISIBLE);
                imgAddOutlet.setVisibility(View.GONE);
                break;
        }
    }

    public String getImageFilePath(Uri uri) {
        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            @SuppressLint("Range") String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            cursor.close();
            return imagePath;
        }
        return null;
    }
}
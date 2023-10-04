package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Attachment;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.utils.Utils;

public class CreateNooActivity extends BaseActivity {
    private Button btnSave;
    private Spinner spnSuku, spnStatusToko, spnStatusNpwp;
    private RelativeLayout llKTP, llNPWP, llOutlet;
    private ImageType imageType;
    private int typeImage = 0;
    public static final int GALLERY_PERM_CODE = 101;
    public static final int CAMERA_PERM_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView imgKTP, imgNPWP, imgOutlet;
    ImageView imgAddKTP, imgAddNPWP, imgAddOutlet;
    ImageView imgDeleteKTP, imgDeleteNPWP, imgDeleteOutlet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_create_noo);

        initProgress();
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
            typeImage = 1;
            imageType.setPosImage(typeImage);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            openDialogPhoto();
        });

        llNPWP.setOnClickListener(view -> {
            typeImage = 2;
            imageType.setPosImage(typeImage);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            openDialogPhoto();
        });

        llOutlet.setOnClickListener(view -> {
            typeImage = 3;
            imageType.setPosImage(typeImage);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            openDialogPhoto();
        });

        imgDeleteKTP.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoKTP(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            Utils.loadImageFit(CreateNooActivity.this, null, imgKTP);
            imgAddKTP.setVisibility(View.VISIBLE);
            imgDeleteKTP.setVisibility(View.GONE);
            imgKTP.setVisibility(View.GONE);
        });

        imgDeleteNPWP.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoNPWP(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            Utils.loadImageFit(CreateNooActivity.this, null, imgNPWP);
            imgAddNPWP.setVisibility(View.VISIBLE);
            imgDeleteNPWP.setVisibility(View.GONE);
            imgNPWP.setVisibility(View.GONE);
        });

        imgDeleteOutlet.setOnClickListener(view -> {
            if (imageType == null) {
                imageType = new ImageType();
            }
            imageType.setPhotoOutlet(null);
            Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
            Utils.loadImageFit(CreateNooActivity.this, null, imgOutlet);
            imgAddOutlet.setVisibility(View.VISIBLE);
            imgDeleteOutlet.setVisibility(View.GONE);
            imgOutlet.setVisibility(View.GONE);
        });
    }

    public void openDialogPhoto() {
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

        switch (typeImage) {
            case 1:
                if (imageType.getPhotoKTP() != null) {
                    photo.setImageURI(imageType.getPhotoKTP());
                    photo.setVisibility(View.VISIBLE);
                    layoutUpload.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.GONE);
                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (imageType.getPhotoNPWP() != null) {
                    photo.setImageURI(imageType.getPhotoNPWP());
                    photo.setVisibility(View.VISIBLE);
                    layoutUpload.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.GONE);
                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                if (imageType.getPhotoOutlet() != null) {
                    photo.setImageURI(imageType.getPhotoOutlet());
                    photo.setVisibility(View.VISIBLE);
                    layoutUpload.setVisibility(View.GONE);
                } else {
                    photo.setVisibility(View.GONE);
                    layoutUpload.setVisibility(View.VISIBLE);
                }
                break;
        }

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
                askPermissionCamera();
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
        imgDeleteKTP = findViewById(R.id.imgDeleteKTP);
        imgAddNPWP = findViewById(R.id.imgAddNPWP);
        imgDeleteNPWP = findViewById(R.id.imgDeleteNPWP);
        imgAddOutlet = findViewById(R.id.imgAddOutlet);
        imgDeleteOutlet = findViewById(R.id.imgDeleteOutlet);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Helper.getItemParam(Constants.IMAGE_TYPE) != null) {
            imageType = new ImageType();
            imageType = (ImageType) Helper.getItemParam(Constants.IMAGE_TYPE);
        } else {
            imageType = new ImageType();
        }
        typeImage = imageType.getPosImage();

        if (getIntent().getExtras() != null) {
            Uri uri = (Uri) getIntent().getExtras().get(Constants.OUTPUT_CAMERA);
            getIntent().removeExtra(Constants.OUTPUT_CAMERA);
//            Bitmap myBitmap = null;
//            Uri rotate = null;
//            try {
//                myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                myBitmap = Utils.rotateImageIfRequired(myBitmap, uri);
//                rotate = Utils.getImageUri(CreateNooActivity.this, myBitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            switch (typeImage) {
                case 1:
                    imageType.setPhotoKTP(uri);
                    break;
                case 2:
                    imageType.setPhotoNPWP(uri);
                    break;
                case 3:
                    imageType.setPhotoOutlet(uri);
                    break;
            }
        }

        imgKTP.setImageURI(imageType.getPhotoKTP());
        imgNPWP.setImageURI(imageType.getPhotoNPWP());
        imgOutlet.setImageURI(imageType.getPhotoOutlet());
        Helper.setItemParam(Constants.IMAGE_TYPE, imageType);

//        Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoKTP(), imgKTP);
//        Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoNPWP(), imgNPWP);
//        Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoOutlet(), imgOutlet);
    }

    //foto
    public void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERM_CODE);
            }, GALLERY_PERM_CODE);
        } else {
            openGallery();
        }
    }

    public void askPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, CAMERA_PERM_CODE);
        } else {
            Helper.takePhoto(CreateNooActivity.this);
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
        if (requestCode == GALLERY_PERM_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else if (requestCode == CAMERA_PERM_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Helper.takePhoto(CreateNooActivity.this);
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
//        String path = getImageFilePath(selectedImage);
//        File pathImg = new File(path);
//        byte[] imgByte = getByteArrayFromUriGallery(Uri.fromFile(pathImg));

        switch (typeImage) {
            case 1:
                imageType.setPhotoKTP(selectedImage);
                imgKTP.setImageURI(selectedImage);
//                Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoKTP(), imgKTP);
                imgKTP.setVisibility(View.VISIBLE);
                imgDeleteKTP.setVisibility(View.VISIBLE);
                imgAddKTP.setVisibility(View.GONE);
                break;
            case 2:
                imageType.setPhotoNPWP(selectedImage);
                imgNPWP.setImageURI(selectedImage);
//                Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoNPWP(), imgNPWP);
                imgNPWP.setVisibility(View.VISIBLE);
                imgDeleteNPWP.setVisibility(View.VISIBLE);
                imgAddNPWP.setVisibility(View.GONE);
                break;
            case 3:
                imageType.setPhotoOutlet(selectedImage);
                imgOutlet.setImageURI(selectedImage);
//                Utils.loadImageFit(CreateNooActivity.this, imageType.getPhotoOutlet(), imgOutlet);
                imgOutlet.setVisibility(View.VISIBLE);
                imgDeleteOutlet.setVisibility(View.VISIBLE);
                imgAddOutlet.setVisibility(View.GONE);
                break;
        }

        Helper.setItemParam(Constants.IMAGE_TYPE, imageType);
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
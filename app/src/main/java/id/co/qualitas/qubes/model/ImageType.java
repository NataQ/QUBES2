package id.co.qualitas.qubes.model;

import android.net.Uri;

public class ImageType {
    private Uri photoKTP;
    private Uri photoNPWP;
    private Uri photoOutlet;
    private int posImage;

    public int getPosImage() {
        return posImage;
    }

    public void setPosImage(int posImage) {
        this.posImage = posImage;
    }

    public Uri getPhotoKTP() {
        return photoKTP;
    }

    public void setPhotoKTP(Uri photoKTP) {
        this.photoKTP = photoKTP;
    }

    public Uri getPhotoNPWP() {
        return photoNPWP;
    }

    public void setPhotoNPWP(Uri photoNPWP) {
        this.photoNPWP = photoNPWP;
    }

    public Uri getPhotoOutlet() {
        return photoOutlet;
    }

    public void setPhotoOutlet(Uri photoOutlet) {
        this.photoOutlet = photoOutlet;
    }
}

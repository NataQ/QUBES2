package id.co.qualitas.qubes.model;

import android.net.Uri;

public class ImageType {
    private Uri photoKTP;
    private Uri photoNPWP;
    private Uri photoOutlet;
    private Uri photoAkhir;
    private Uri photoSelesai;
    private Uri photoKmAwal;
    private int posImage;
    private int posMaterial;
    private String kmAwal;
    private String kmAkhir;

    public int getPosMaterial() {
        return posMaterial;
    }

    public void setPosMaterial(int posMaterial) {
        this.posMaterial = posMaterial;
    }

    public Uri getPhotoAkhir() {
        return photoAkhir;
    }

    public void setPhotoAkhir(Uri photoAkhir) {
        this.photoAkhir = photoAkhir;
    }

    public Uri getPhotoSelesai() {
        return photoSelesai;
    }

    public void setPhotoSelesai(Uri photoSelesai) {
        this.photoSelesai = photoSelesai;
    }

    public Uri getPhotoKmAwal() {
        return photoKmAwal;
    }

    public void setPhotoKmAwal(Uri photoKmAwal) {
        this.photoKmAwal = photoKmAwal;
    }

    public String getKmAwal() {
        return kmAwal;
    }

    public void setKmAwal(String kmAwal) {
        this.kmAwal = kmAwal;
    }

    public String getKmAkhir() {
        return kmAkhir;
    }

    public void setKmAkhir(String kmAkhir) {
        this.kmAkhir = kmAkhir;
    }

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

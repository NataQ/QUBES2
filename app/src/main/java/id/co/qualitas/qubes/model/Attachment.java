package id.co.qualitas.qubes.model;

import android.net.Uri;

import java.io.Serializable;

public class Attachment implements Serializable {
    String file_name;
    String file_data;
    String file_description;
    String mime_type;
    byte[] img;
    Uri imgUri;

    String UP_Name;
    String UP_Path;

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_data() {
        return file_data;
    }

    public void setFile_data(String file_data) {
        this.file_data = file_data;
    }

    public String getFile_description() {
        return file_description;
    }

    public void setFile_description(String file_description) {
        this.file_description = file_description;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getUP_Name() {
        return UP_Name;
    }

    public void setUP_Name(String UP_Name) {
        this.UP_Name = UP_Name;
    }

    public String getUP_Path() {
        return UP_Path;
    }

    public void setUP_Path(String UP_Path) {
        this.UP_Path = UP_Path;
    }
}
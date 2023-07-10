package id.co.qualitas.qubes.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class GCMResponse implements Parcelable {
    private String desc;
    private String contentTitle;
    private String username;
    private String imageUrl;

    public GCMResponse() {
    }

    protected GCMResponse(Parcel in) {
        desc = in.readString();
        contentTitle = in.readString();
        username = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<GCMResponse> CREATOR = new Creator<GCMResponse>() {
        @Override
        public GCMResponse createFromParcel(Parcel in) {
            return new GCMResponse(in);
        }

        @Override
        public GCMResponse[] newArray(int size) {
            return new GCMResponse[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(this.desc);
        parcel.writeString(this.contentTitle);
        parcel.writeString(this.username);
        parcel.writeString(this.username);
        parcel.writeString(this.imageUrl);
    }
}

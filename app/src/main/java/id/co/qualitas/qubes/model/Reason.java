package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Reason implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("description")
    private String desc;

    @SerializedName("type")
    private String type;

    private boolean freeText;
    private boolean photo;

    public Reason() {
    }

    public Reason(String id, String desc, boolean freeText, boolean photo) {
        this.id = id;
        this.desc = desc;
        this.freeText = freeText;
        this.photo = photo;
    }

    public boolean isFreeText() {
        return freeText;
    }

    public void setFreeText(boolean freeText) {
        this.freeText = freeText;
    }

    public boolean isPhoto() {
        return photo;
    }

    public void setPhoto(boolean photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return (id + " - " + desc);
        //untuk drop down reason not visit, karena gak pake custom adapter
//        return "Reason{" +
//                "id='" + id + '\'' +
//                ", desc='" + desc + '\'' +
//                '}';
    }
}

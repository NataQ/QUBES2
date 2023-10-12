package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class Reason implements Serializable {
    private int id;
    private String description;
    private String type;//old version
    private String category;
    private int is_freetext;
    private int is_photo;
    private int is_barcode;
    private int is_signature;
    private int is_sync;

    public Reason() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIs_freetext() {
        return is_freetext;
    }

    public void setIs_freetext(int is_freetext) {
        this.is_freetext = is_freetext;
    }

    public int getIs_photo() {
        return is_photo;
    }

    public void setIs_photo(int is_photo) {
        this.is_photo = is_photo;
    }

    public int getIs_barcode() {
        return is_barcode;
    }

    public void setIs_barcode(int is_barcode) {
        this.is_barcode = is_barcode;
    }

    public int getIs_signature() {
        return is_signature;
    }

    public void setIs_signature(int is_signature) {
        this.is_signature = is_signature;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }

    @Override
    public String toString() {
        return (id + " - " + description);
        //untuk drop down reason not visit, karena gak pake custom adapter
//        return "Reason{" +
//                "id='" + id + '\'' +
//                ", desc='" + desc + '\'' +
//                '}';
    }
}

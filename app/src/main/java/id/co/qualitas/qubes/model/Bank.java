package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class Bank implements Serializable {
    private String id;
    private String name;
    private String id_depo;
    private String id_region;
    private String category;
    private String no_rek;
    private int isSync;

    public Bank() {
    }

    public String getId_region() {
        return id_region;
    }

    public void setId_region(String id_region) {
        this.id_region = id_region;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_depo() {
        return id_depo;
    }

    public void setId_depo(String id_depo) {
        this.id_depo = id_depo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNo_rek() {
        return no_rek;
    }

    public void setNo_rek(String no_rek) {
        this.no_rek = no_rek;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }
}

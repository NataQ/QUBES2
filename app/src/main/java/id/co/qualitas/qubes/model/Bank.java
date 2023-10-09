package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bank implements Serializable {
    private String id;
    private String name;
    private String id_depo;
    private String category;
    private String noRekening;
    private boolean isSync;

    public Bank() {
    }

    public Bank(String id, String name, String category, String noRekening, boolean isSync) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.noRekening = noRekening;
        this.isSync = isSync;
    }

    public String getId_depo() {
        return id_depo;
    }

    public void setId_depo(String id_depo) {
        this.id_depo = id_depo;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }
}

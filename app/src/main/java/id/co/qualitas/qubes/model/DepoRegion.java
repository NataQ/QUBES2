package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DepoRegion{
    int id_depo;
    String depo_name;
    int id_region;
    String region_name;

    public int getId_depo() {
        return id_depo;
    }

    public void setId_depo(int id_depo) {
        this.id_depo = id_depo;
    }

    public String getDepo_name() {
        return depo_name;
    }

    public void setDepo_name(String depo_name) {
        this.depo_name = depo_name;
    }

    public int getId_region() {
        return id_region;
    }

    public void setId_region(int id_region) {
        this.id_region = id_region;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }
}

package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Wiliam on 1/9/2018.
 */

public class AdditionalInfo implements Serializable{
    @SerializedName("id_sales_office")
    private String idPlant;

    public String getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(String idPlant) {
        this.idPlant = idPlant;
    }
}

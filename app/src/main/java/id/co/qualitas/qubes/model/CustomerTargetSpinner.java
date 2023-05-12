package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Natalia on 3/22/2017.
 */

public class CustomerTargetSpinner implements Serializable {
    @SerializedName("idOutlet")
    private String idOutlet;

    @SerializedName("name")
    private String name;

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

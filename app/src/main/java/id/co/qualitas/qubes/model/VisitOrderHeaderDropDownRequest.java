package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Foo on 1/24/2017.
 */

public class VisitOrderHeaderDropDownRequest implements Serializable{
    @SerializedName("idOutlet")
    private String idOutlet;

    @SerializedName("idEmployee")
    private String idEmployee;

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }
}

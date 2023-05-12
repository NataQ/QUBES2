package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Foo on 1/24/2017.
 */

public class VisitOrderHeaderDropDownResponse implements Serializable{
    @SerializedName("salesOffice")
    private ArrayList<OutletResponse> salesOffice;

    @SerializedName("klasifikasiMaterial")
    private String[] klasifikasiMaterial;

    @SerializedName("listPartner")
    private ArrayList<OutletResponse> listPartner;

    @SerializedName("spesimen")
    private String spesimen;

    public ArrayList<OutletResponse> getListPartner() {
        return listPartner;
    }

    public void setListPartner(ArrayList<OutletResponse> listPartner) {
        this.listPartner = listPartner;
    }

    public String getSpesimen() {
        return spesimen;
    }

    public void setSpesimen(String spesimen) {
        this.spesimen = spesimen;
    }

    public ArrayList<OutletResponse> getSalesOffice() {
        return salesOffice;
    }

    public void setSalesOffice(ArrayList<OutletResponse> salesOffice) {
        this.salesOffice = salesOffice;
    }

    public String[] getKlasifikasiMaterial() {
        return klasifikasiMaterial;
    }

    public void setKlasifikasiMaterial(String[] klasifikasiMaterial) {
        this.klasifikasiMaterial = klasifikasiMaterial;
    }
}

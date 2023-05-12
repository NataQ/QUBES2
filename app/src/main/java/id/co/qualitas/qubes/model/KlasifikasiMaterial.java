package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Foo on 2/10/2017.
 */

public class KlasifikasiMaterial implements Serializable {
    @SerializedName("MaterialDetail")
    private ArrayList<MaterialResponse> materialResponseArrayList;

    @SerializedName("klasifikasiMaterial")
    private String[] klasifikasiMaterial;

    public ArrayList<MaterialResponse> getMaterialResponseArrayList() {
        return materialResponseArrayList;
    }

    public void setMaterialResponseArrayList(ArrayList<MaterialResponse> materialResponseArrayList) {
        this.materialResponseArrayList = materialResponseArrayList;
    }

    public String[] getKlasifikasiMaterial() {
        return klasifikasiMaterial;
    }

    public void setKlasifikasiMaterial(String[] klasifikasiMaterial) {
        this.klasifikasiMaterial = klasifikasiMaterial;
    }
}

package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CreateStoreCheckRequest implements Serializable {
    @SerializedName("idOutlet")
    private String idOutlet;

    @SerializedName("dateString")
    private String dateString;

    @SerializedName("client")
    private String client;

    @SerializedName("idEmployee")
    private String idEmployee;

    @SerializedName("storeCheckDetail")
    private ArrayList<MaterialResponse> materialResponseArrayList;

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    private String id_store_check;

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getId_store_check() {
        return id_store_check;
    }

    public void setId_store_check(String id_store_check) {
        this.id_store_check = id_store_check;
    }

    public ArrayList<MaterialResponse> getMaterialResponseArrayList() {
        return materialResponseArrayList;
    }

    public void setMaterialResponseArrayList(ArrayList<MaterialResponse> materialResponseArrayList) {
        this.materialResponseArrayList = materialResponseArrayList;
    }
}

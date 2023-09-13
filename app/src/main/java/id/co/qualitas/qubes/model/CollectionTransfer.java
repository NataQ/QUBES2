package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CollectionTransfer implements Serializable {
    public String tglTransfer;
    public String totalPayment;
    public String left;
    public List<Material> materialList;

    public CollectionTransfer() {
    }

    public CollectionTransfer(String tglTransfer, List<Material> materialList) {
        this.tglTransfer = tglTransfer;
        this.materialList = materialList;
    }

    public String getTglTransfer() {
        return tglTransfer;
    }

    public void setTglTransfer(String tglTransfer) {
        this.tglTransfer = tglTransfer;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }
}

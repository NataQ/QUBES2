package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.List;

public class CollectionGiro implements Serializable {
    public String noGiro;
    public String tglCair;
    public String tglGiro;
    public String idBankName;
    public String bankName;
    public String idBankCust;
    public String bankCust;
    public String totalPayment;
    public String left;
    public List<Material> materialList;

    public CollectionGiro() {
    }

    public CollectionGiro(String noGiro, String tglCair, String tglGiro, String idBankName, String bankName, String idBankCust, String bankCust, String totalPayment, String left, List<Material> materialList) {
        this.noGiro = noGiro;
        this.tglCair = tglCair;
        this.tglGiro = tglGiro;
        this.idBankName = idBankName;
        this.bankName = bankName;
        this.idBankCust = idBankCust;
        this.bankCust = bankCust;
        this.totalPayment = totalPayment;
        this.left = left;
        this.materialList = materialList;
    }

    public String getNoGiro() {
        return noGiro;
    }

    public void setNoGiro(String noGiro) {
        this.noGiro = noGiro;
    }

    public String getTglCair() {
        return tglCair;
    }

    public void setTglCair(String tglCair) {
        this.tglCair = tglCair;
    }

    public String getIdBankName() {
        return idBankName;
    }

    public void setIdBankName(String idBankName) {
        this.idBankName = idBankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIdBankCust() {
        return idBankCust;
    }

    public void setIdBankCust(String idBankCust) {
        this.idBankCust = idBankCust;
    }

    public String getBankCust() {
        return bankCust;
    }

    public void setBankCust(String bankCust) {
        this.bankCust = bankCust;
    }

    public String getTglGiro() {
        return tglGiro;
    }

    public void setTglGiro(String tglGiro) {
        this.tglGiro = tglGiro;
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

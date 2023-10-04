package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.List;

public class CollectionGiro implements Serializable {
    public String customerId;
    public String date;
    public double omzet;
    public String noGiro;
    public String tglCair;
    public String tglGiro;
    public String idBankName;
    public String bankName;
    public String idBankCust;
    public String bankCust;
    public double totalPayment;
    public double left;
    public List<Material> materialList;
    private boolean isSync;

    public CollectionGiro() {
    }

    public CollectionGiro(String noGiro, String tglCair, String tglGiro, String idBankName, String bankName, String idBankCust, String bankCust, List<Material> materialList) {
        this.noGiro = noGiro;
        this.tglCair = tglCair;
        this.tglGiro = tglGiro;
        this.idBankName = idBankName;
        this.bankName = bankName;
        this.idBankCust = idBankCust;
        this.bankCust = bankCust;
        this.materialList = materialList;
    }

    public CollectionGiro(String noGiro, List<Material> materialList) {
        this.noGiro = noGiro;
        this.materialList = materialList;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getOmzet() {
        return omzet;
    }

    public void setOmzet(double omzet) {
        this.omzet = omzet;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
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

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }
}

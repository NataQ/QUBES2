package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.List;

public class CollectionCheque implements Serializable {
    public String customerId;
    public String date;
    public double omzet;
    public String noCheque;
    public String tglCair;
    public String tglCheque;
    public String idBankName;
    public String bankName;
    public String idBankCust;
    public String bankCust;
    public double totalPayment;
    public double left;
    public List<Material> materialList;
    private boolean isSync;

    public CollectionCheque() {
    }

    public CollectionCheque(String noCheque, List<Material> materialList) {
        this.noCheque = noCheque;
        this.materialList = materialList;
    }

    public CollectionCheque(String noCheque, String tglCair, String tglCheque, String idBankName, String bankName, String idBankCust, String bankCust, List<Material> materialList) {
        this.noCheque = noCheque;
        this.tglCair = tglCair;
        this.tglCheque = tglCheque;
        this.idBankName = idBankName;
        this.bankName = bankName;
        this.idBankCust = idBankCust;
        this.bankCust = bankCust;
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

    public String getNoCheque() {
        return noCheque;
    }

    public void setNoCheque(String noCheque) {
        this.noCheque = noCheque;
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

    public String getTglCheque() {
        return tglCheque;
    }

    public void setTglCheque(String tglCheque) {
        this.tglCheque = tglCheque;
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

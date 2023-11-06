package id.co.qualitas.qubes.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class CollectionGiro implements Serializable, Cloneable {
    public String customerId;
    public String date;
    public double omzet;
    public String noGiro;
    public String tglCair;
    public String tglGiro;
    public String idBankASPP;
    public String bankNameASPP;
    public String idBankCust;
    public String bankCust;
    public double totalPayment;
    public double left;
    public List<Material> materialList;
    public List<Material> checkedMaterialList;
    private int isSync;

    public CollectionGiro() {
    }

    public CollectionGiro(String noGiro, String tglCair, String tglGiro, String idBankASPP, String bankNameASPP, String idBankCust, String bankCust, List<Material> materialList) {
        this.noGiro = noGiro;
        this.tglCair = tglCair;
        this.tglGiro = tglGiro;
        this.idBankASPP = idBankASPP;
        this.bankNameASPP = bankNameASPP;
        this.idBankCust = idBankCust;
        this.bankCust = bankCust;
        this.materialList = materialList;
    }

    public CollectionGiro(String noGiro, List<Material> materialList) {
        this.noGiro = noGiro;
        this.materialList = materialList;
    }

    public List<Material> getCheckedMaterialList() {
        return checkedMaterialList;
    }

    public void setCheckedMaterialList(List<Material> checkedMaterialList) {
        this.checkedMaterialList = checkedMaterialList;
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

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
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

    public String getIdBankASPP() {
        return idBankASPP;
    }

    public void setIdBankASPP(String idBankASPP) {
        this.idBankASPP = idBankASPP;
    }

    public String getBankNameASPP() {
        return bankNameASPP;
    }

    public void setBankNameASPP(String bankNameASPP) {
        this.bankNameASPP = bankNameASPP;
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

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

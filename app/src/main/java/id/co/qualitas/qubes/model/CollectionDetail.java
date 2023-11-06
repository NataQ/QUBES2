package id.co.qualitas.qubes.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class CollectionDetail implements Serializable, Cloneable {
    public String idDetail;
    public String idHeader;
    public String customerId;
    public String invoiceNo;
    public String status;
    public double omzet;
    public String no;
    public String tglCair;
    public String tgl;
    public String idBankASPP;
    public String bankNameASPP;
    public String idBankCust;
    public String bankCust;
    public double totalPayment;
    public double left;
    public List<Material> materialList;
    public List<Material> checkedMaterialList;
    private int isSync;

    public CollectionDetail() {
    }
    public CollectionDetail(String tgl, List<Material> materialList) {
        this.tgl = tgl;
        this.materialList = materialList;
    }

    public CollectionDetail(String no, String tglCair, String tgl, String idBankASPP, String bankNameASPP, String idBankCust, String bankCust, List<Material> materialList) {
        this.no = no;
        this.tglCair = tglCair;
        this.tgl = tgl;
        this.idBankASPP = idBankASPP;
        this.bankNameASPP = bankNameASPP;
        this.idBankCust = idBankCust;
        this.bankCust = bankCust;
        this.materialList = materialList;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(String idDetail) {
        this.idDetail = idDetail;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getOmzet() {
        return omzet;
    }

    public void setOmzet(double omzet) {
        this.omzet = omzet;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTglCair() {
        return tglCair;
    }

    public void setTglCair(String tglCair) {
        this.tglCair = tglCair;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
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

    public List<Material> getCheckedMaterialList() {
        return checkedMaterialList;
    }

    public void setCheckedMaterialList(List<Material> checkedMaterialList) {
        this.checkedMaterialList = checkedMaterialList;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

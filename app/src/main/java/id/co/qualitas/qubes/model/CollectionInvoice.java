package id.co.qualitas.qubes.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class CollectionInvoice implements Serializable, Cloneable {
    public String idCollectionHeader;
    public String idCollectionDetail;
    public String idHeader;
    public String invoiceNo;
    public double invoiceTotal;
    public double left;
    public double totalPaid;

    public List<Material> materialList;
    public List<Material> checkedMaterialList;
    private int isSync;
    private boolean deleted;

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public String getIdCollectionHeader() {
        return idCollectionHeader;
    }

    public void setIdCollectionHeader(String idCollectionHeader) {
        this.idCollectionHeader = idCollectionHeader;
    }

    public String getIdCollectionDetail() {
        return idCollectionDetail;
    }

    public void setIdCollectionDetail(String idCollectionDetail) {
        this.idCollectionDetail = idCollectionDetail;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(double invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

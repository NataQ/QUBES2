package id.co.qualitas.qubes.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class CollectionHeader implements Serializable, Cloneable {
    public String idHeader;
    public String customerId;
    public String invoiceNo;
    public String invoiceDate;
    public double invoiceTotal;
    public double totalPaid;
    public String status;
    public List<CollectionDetail> detailList;
    public List<CollectionDetail> cashList;
    public List<CollectionDetail> tfList;
    public List<CollectionDetail> giroList;
    public List<CollectionDetail> chequeList;
    public List<CollectionDetail> lainList;
    private int is_sync;

    public List<CollectionDetail> getCashList() {
        return cashList;
    }

    public void setCashList(List<CollectionDetail> cashList) {
        this.cashList = cashList;
    }

    public List<CollectionDetail> getTfList() {
        return tfList;
    }

    public void setTfList(List<CollectionDetail> tfList) {
        this.tfList = tfList;
    }

    public List<CollectionDetail> getGiroList() {
        return giroList;
    }

    public void setGiroList(List<CollectionDetail> giroList) {
        this.giroList = giroList;
    }

    public List<CollectionDetail> getChequeList() {
        return chequeList;
    }

    public void setChequeList(List<CollectionDetail> chequeList) {
        this.chequeList = chequeList;
    }

    public List<CollectionDetail> getLainList() {
        return lainList;
    }

    public void setLainList(List<CollectionDetail> lainList) {
        this.lainList = lainList;
    }

    public CollectionHeader() {
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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CollectionDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<CollectionDetail> detailList) {
        this.detailList = detailList;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
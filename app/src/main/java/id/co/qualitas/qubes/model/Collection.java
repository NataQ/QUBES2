package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.List;

public class Collection implements Serializable {
    private User user;
    private Customer customer;
    private Collection cash;
    private Collection lain;
    private double totalPayment;
    private double totalPaid;
    private double left;
    private List<Invoice> invoiceList;
    private List<CollectionDetail> tfList;
    private List<CollectionDetail> giroList;
    private List<CollectionDetail> cekList;
    private int isSync;

    public Collection() {
    }

    public Collection(double totalPayment, double left,double totalPaid, List<Invoice> invoiceList) {
        this.left = left;
        this.totalPaid = totalPaid;
        this.totalPayment = totalPayment;
        this.invoiceList = invoiceList;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Collection getCash() {
        return cash;
    }

    public void setCash(Collection cash) {
        this.cash = cash;
    }

    public Collection getLain() {
        return lain;
    }

    public void setLain(Collection lain) {
        this.lain = lain;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
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

    public List<CollectionDetail> getCekList() {
        return cekList;
    }

    public void setCekList(List<CollectionDetail> cekList) {
        this.cekList = cekList;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }
}

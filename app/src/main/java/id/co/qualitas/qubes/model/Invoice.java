package id.co.qualitas.qubes.model;

import java.util.List;

public class Invoice {
    private String idHeader;
    private String invoiceNo;
    private String customerName;
    private String customerID;
    private double amount;
    private double paid;
    private double nett;
    private String invoiceDate;
    private String dueDate;
    private String route;
    private String signature;
    private boolean isChecked;
    private boolean isRoute;
    private boolean isSync;
    private boolean isVerification;
    private List<Material> materialList;

    public Invoice() {
    }

    public Invoice(String invoiceNo, String customerName, String customerID, double amount, double paid, String invoiceDate, boolean isRoute) {
        this.invoiceNo = invoiceNo;
        this.customerName = customerName;
        this.customerID = customerID;
        this.amount = amount;
        this.paid = paid;
        this.invoiceDate = invoiceDate;
        this.isRoute = isRoute;
    }

    public Invoice(String invoiceNo, String customerName, String customerID, double amount, double paid, String invoiceDate, boolean isRoute, List<Material> materialList) {
        this.invoiceNo = invoiceNo;
        this.customerName = customerName;
        this.customerID = customerID;
        this.amount = amount;
        this.paid = paid;
        this.invoiceDate = invoiceDate;
        this.isRoute = isRoute;
        this.materialList = materialList;
    }
    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public boolean isVerification() {
        return isVerification;
    }

    public void setVerification(boolean verification) {
        isVerification = verification;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public double getNett() {
        return nett;
    }

    public void setNett(double nett) {
        this.nett = nett;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isRoute() {
        return isRoute;
    }

    public void setRoute(boolean route) {
        isRoute = route;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}

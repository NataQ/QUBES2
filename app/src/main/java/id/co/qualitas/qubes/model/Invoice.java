package id.co.qualitas.qubes.model;

public class Invoice {
    private String invoiceNo;
    private String customerName;
    private String customerID;
    private float amount;
    private float paid;
    private String date;
    private String route;
    private boolean isChecked;

    public Invoice(String invoiceNo, String customerName, String customerID, float amount, float paid, String date, String route) {
        this.invoiceNo = invoiceNo;
        this.customerName = customerName;
        this.customerID = customerID;
        this.amount = amount;
        this.paid = paid;
        this.date = date;
        this.route = route;
    }

    public Invoice(String invoiceNo, String customerName, String customerID, float amount, float paid, String date) {
        this.invoiceNo = invoiceNo;
        this.customerName = customerName;
        this.customerID = customerID;
        this.amount = amount;
        this.paid = paid;
        this.date = date;
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

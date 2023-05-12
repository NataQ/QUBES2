package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 8/12/2016.
 */

public class MoneySettlementDetail {
    private String noTo;
    private String date;
    private String amount;
    private String paid;
    private String left;
    private String status;

    public MoneySettlementDetail(String noTo, String date, String amount, String paid, String left, String status) {
        this.noTo = noTo;
        this.date = date;
        this.amount = amount;
        this.paid=paid;
        this.left = left;
        this.status=status;
    }

    public String getNoTo() {
        return noTo;
    }

    public void setNoTo(String noTo) {
        this.noTo = noTo;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }
}

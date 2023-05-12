package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 8/12/2016.
 */

public class MoneySettlement {
    private String outlet;
    private String date;
    private String amount;
    private String left;
    private int attachment;

    public MoneySettlement(String outlet, String date, String amount, String left, int attachment) {
        this.outlet = outlet;
        this.date = date;
        this.amount = amount;
        this.left = left;
        this.attachment=attachment;
    }

    public int getAttachment() {
        return attachment;
    }

    public void setAttachment(int attachment) {
        this.attachment = attachment;
    }

    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
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

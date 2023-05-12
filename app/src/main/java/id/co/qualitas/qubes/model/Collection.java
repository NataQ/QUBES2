package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 8/22/2016.
 */

public class Collection {
    private String outletName, date, amount, left;
    private int img;

    public Collection(String outletName, String date, String amount, String left, int img) {
        this.amount = amount;
        this.date = date;
        this.img = img;
        this.left = left;
        this.outletName = outletName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}

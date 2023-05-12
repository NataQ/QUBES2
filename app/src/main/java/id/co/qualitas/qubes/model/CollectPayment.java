package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 8/23/2016.
 */

public class CollectPayment {
    private String facNum;
    private String price;
    private int checked;

    public CollectPayment(String facNum, String price) {
        this.facNum = facNum;
        this.price = price;
    }

    public String getFacNum() {
        return facNum;
    }

    public void setFacNum(String facNum) {
        this.facNum = facNum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}

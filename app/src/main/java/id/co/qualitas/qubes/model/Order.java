package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/22/2016.
 */
public class Order {
    private String txtOrderName;
    private String txtOrderCode;
    private String txtPriceBefore;
    private String txtPriceNett;

    public Order(String txtOrderName, String txtOrderCode, String txtPriceBefore, String txtPriceNett) {
        this.txtOrderName = txtOrderName;
        this.txtOrderCode = txtOrderCode;
        this.txtPriceBefore = txtPriceBefore;
        this.txtPriceNett = txtPriceNett;
    }


    public String getTxtOrderName() {
        return txtOrderName;
    }

    public void setTxtOrderName(String txtOrderName) {
        this.txtOrderName = txtOrderName;
    }

    public String getTxtOrderCode() {
        return txtOrderCode;
    }

    public void setTxtOrderCode(String txtOrderCode) {
        this.txtOrderCode = txtOrderCode;
    }

    public String getTxtPriceBefore() {
        return txtPriceBefore;
    }

    public void setTxtPriceBefore(String txtPriceBefore) {
        this.txtPriceBefore = txtPriceBefore;
    }

    public String getTxtPriceNett() {
        return txtPriceNett;
    }

    public void setTxtPriceNett(String txtPriceNett) {
        this.txtPriceNett = txtPriceNett;
    }
}

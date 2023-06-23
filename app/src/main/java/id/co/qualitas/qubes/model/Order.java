package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/22/2016.
 */
public class Order {
    private String txtOrderName;
    private String txtOrderCode;
    private String txtPriceBefore;
    private String txtPriceNett;
    private String status;
    private String date;
    private String soNo;
    private String outlet;

    public Order(String txtOrderCode, String txtPriceNett, String outlet, String soNo, String status, String date) {
        this.txtOrderCode = txtOrderCode;
        this.txtPriceNett = txtPriceNett;
        this.soNo = soNo;
        this.status = status;
        this.date = date;
        this.outlet = outlet;
    }

    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
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

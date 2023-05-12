package id.co.qualitas.qubes.model;

/**
 * Created by Natalia on 6/29/2015.
 */
public class ReturnDeliveryOrder{
    private String customer_no;
    private String customer_name;
    private String material_no;
    private String material_desc;
    private int qty;
    private String uom;
    private String batch;
    private byte[] picture;

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getCustomer_no() {
        return customer_no;
    }

    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMaterial_no() {
        return material_no;
    }

    public void setMaterial_no(String material_no) {
        this.material_no = material_no;
    }

    public String getMaterial_desc() {
        return material_desc;
    }

    public void setMaterial_desc(String material_desc) {
        this.material_desc = material_desc;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}

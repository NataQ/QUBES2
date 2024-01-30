package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class Price implements Serializable {
    private String idMasterPriceDB;
    private String type_customer;
    private String price_list_code;
    private String id_material;
    private String top;
    private double qty;
    private double selling_price;
    private String uom;

    public Price() {
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getIdMasterPriceDB() {
        return idMasterPriceDB;
    }

    public void setIdMasterPriceDB(String idMasterPriceDB) {
        this.idMasterPriceDB = idMasterPriceDB;
    }

    public String getType_customer() {
        return type_customer;
    }

    public void setType_customer(String type_customer) {
        this.type_customer = type_customer;
    }

    public String getPrice_list_code() {
        return price_list_code;
    }

    public void setPrice_list_code(String price_list_code) {
        this.price_list_code = price_list_code;
    }

    public String getId_material() {
        return id_material;
    }

    public void setId_material(String id_material) {
        this.id_material = id_material;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(double selling_price) {
        this.selling_price = selling_price;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
}

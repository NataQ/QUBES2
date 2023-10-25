package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class SalesPriceDetail implements Serializable {
    int idSalesPriceDetailDB;
    String id_material;
    String uom;
    double qty;
    double selling_price;
    String price_list_code;

    public int getIdSalesPriceDetailDB() {
        return idSalesPriceDetailDB;
    }

    public void setIdSalesPriceDetailDB(int idSalesPriceDetailDB) {
        this.idSalesPriceDetailDB = idSalesPriceDetailDB;
    }

    public String getId_material() {
        return id_material;
    }

    public void setId_material(String id_material) {
        this.id_material = id_material;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
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

    public String getPrice_list_code() {
        return price_list_code;
    }

    public void setPrice_list_code(String price_list_code) {
        this.price_list_code = price_list_code;
    }
}

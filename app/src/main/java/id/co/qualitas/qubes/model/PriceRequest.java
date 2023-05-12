package id.co.qualitas.qubes.model;

import java.io.Serializable;

/**
 * Created by WIlliam-QAS on 29/03/2017.
 */

public class PriceRequest implements Serializable {
    private String idMaterial;
    private int qty1;
    private String uom1;
    private int qty2;
    private String uom2;
    private String jenisJual;

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getUom1() {
        return uom1;
    }

    public void setUom1(String uom1) {
        this.uom1 = uom1;
    }

    public int getQty1() {
        return qty1;
    }

    public void setQty1(int qty1) {
        this.qty1 = qty1;
    }

    public int getQty2() {
        return qty2;
    }

    public void setQty2(int qty2) {
        this.qty2 = qty2;
    }

    public String getUom2() {
        return uom2;
    }

    public void setUom2(String uom2) {
        this.uom2 = uom2;
    }

    public String getJenisJual() {
        return jenisJual;
    }

    public void setJenisJual(String jenisJual) {
        this.jenisJual = jenisJual;
    }
}

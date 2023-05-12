package id.co.qualitas.qubes.model;

import java.math.BigDecimal;

/**
 * Created by Wiliam on 8/9/2017.
 */

public class StoreCheck {

    private String id_store_check;
    private String idOutlet;
    private String idMaterial;
    private BigDecimal qty1;
    private String uom1;
    private BigDecimal qty2;
    private String uom2;

    public String getId_store_check() {
        return id_store_check;
    }

    public void setId_store_check(String id_store_check) {
        this.id_store_check = id_store_check;
    }

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public BigDecimal getQty1() {
        return qty1;
    }

    public void setQty1(BigDecimal qty1) {
        this.qty1 = qty1;
    }

    public String getUom1() {
        return uom1;
    }

    public void setUom1(String uom1) {
        this.uom1 = uom1;
    }

    public BigDecimal getQty2() {
        return qty2;
    }

    public void setQty2(BigDecimal qty2) {
        this.qty2 = qty2;
    }

    public String getUom2() {
        return uom2;
    }

    public void setUom2(String uom2) {
        this.uom2 = uom2;
    }
}

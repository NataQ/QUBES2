package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Wiliam on 6/2/2017.
 */

public class GetPriceRequest implements Serializable {
    private String idMaterial;
    private String idOutlet;
    private BigDecimal qty1;
    private String uom1;
    private BigDecimal qty2;
    private String uom2;
    private String jenisJual;
    private String top_sap;
    private String idToPrice;

    public String getIdToPrice() {
        return idToPrice;
    }

    public void setIdToPrice(String idToPrice) {
        this.idToPrice = idToPrice;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
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

    public String getJenisJual() {
        return jenisJual;
    }

    public void setJenisJual(String jenisJual) {
        this.jenisJual = jenisJual;
    }

    public String getTop_sap() {
        return top_sap;
    }

    public void setTop_sap(String top_sap) {
        this.top_sap = top_sap;
    }
}

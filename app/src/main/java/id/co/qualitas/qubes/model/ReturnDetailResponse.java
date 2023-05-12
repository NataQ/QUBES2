package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Foo on 10/31/2016.
 */

public class ReturnDetailResponse implements Serializable {
    private String itemNr;
    private String idReturn;
    private String idMaterial;
    private BigDecimal qty;
    private String uom;
    private BigDecimal qty2;
    private String uom2;
    private String batch;
    private String expiredDate;
    private String materialName;
    private String reason;
    private int idHeader;
    private String description;
    private String noBrb;
    private String noRr;

    public String getItemNr() {
        return itemNr;
    }

    public void setItemNr(String itemNr) {
        this.itemNr = itemNr;
    }

    public BigDecimal getQty2() {
        return qty2;
    }

    public String getUom2() {
        return uom2;
    }

    public String getIdReturn() {
        return idReturn;
    }

    public void setIdReturn(String idReturn) {
        this.idReturn = idReturn;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(int idHeader) {
        this.idHeader = idHeader;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoBrb() {
        return noBrb;
    }

    public void setNoBrb(String noBrb) {
        this.noBrb = noBrb;
    }

    public String getNoRr() {
        return noRr;
    }

    public void setNoRr(String noRr) {
        this.noRr = noRr;
    }

    public void setQty2(BigDecimal qty2) {
        this.qty2 = qty2;
    }

    public void setUom2(String uom2) {
        this.uom2 = uom2;
    }
}

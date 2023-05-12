package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Foo on 10/31/2016.
 */

public class Return {
    private String idReturn;
    private String itemNr;
    private String idRow;
    private String idMaterial;
    private BigDecimal qty;

    private BigDecimal qty2;
    private String uom;

    private String uom2;
    private String batch;
    private String expiredDateString;
    private String materialName;
    private String goodStock;
    private String reason;
    private String idHeader;
    private String description;
    private String error;

    private String noBrb;
    private String noRr;

    @SerializedName("listUomName")
    private ArrayList<UnitOfMeasure> listUomName;

    private String expiredDate;

    private String[] itemsReason;

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getItemsReason() {
        return itemsReason;
    }

    public void setItemsReason(String[] itemsReason) {
        this.itemsReason = itemsReason;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getIdReturn() {
        return idReturn;
    }

    public void setIdReturn(String idReturn) {
        this.idReturn = idReturn;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public String getItemNr() {
        return itemNr;
    }

    public void setItemNr(String itemNr) {
        this.itemNr = itemNr;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getExpiredDateString() {
        return expiredDateString;
    }

    public void setExpiredDateString(String expiredDateString) {
        this.expiredDateString = expiredDateString;
    }

    public ArrayList<UnitOfMeasure> getListUomName() {
        return listUomName;
    }

    public void setListUomName(ArrayList<UnitOfMeasure> listUomName) {
        this.listUomName = listUomName;
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

    public Return() {
    }

    public Return(String idMat,String materialName) {
        this.idMaterial = idMat;
        this.materialName = materialName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Return(String idRow) {
        this.idRow = idRow;
    }

    public String getIdRow() {
        return idRow;
    }

    public void setIdRow(String idRow) {
        this.idRow = idRow;
    }

    public String getIdMat() {
        return idMaterial;
    }

    public void setIdMat(String idMat) {
        this.idMaterial = idMat;
    }

    public BigDecimal getQty1() {
        return qty;
    }

    public void setQty1(BigDecimal qty) {
        this.qty = qty;
    }

    public String getUom1() {
        return uom;
    }

    public void setUom1(String uom) {
        this.uom = uom;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getGoodStock() {
        return goodStock;
    }

    public void setGoodStock(String goodStock) {
        this.goodStock = goodStock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUom2() {
        return uom2;
    }

    public void setUom2(String uom2) {
        this.uom2 = uom2;
    }

    public BigDecimal getQty2() {
        return qty2;
    }

    public void setQty2(BigDecimal qty2) {
        this.qty2 = qty2;
    }

}

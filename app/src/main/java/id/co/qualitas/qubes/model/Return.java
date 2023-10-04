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
    private String materialId;
    private double qty;

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
    private String customerId;
    private String date;
    private String condition;
    private String idReason;
    private String nameReason;
    private String descReason;
    private String photoReason;
    private boolean isSync;

    public Return(String idReturn, String expiredDate) {
        this.idReturn = idReturn;
        this.expiredDate = expiredDate;
    }

    public String getIdReason() {
        return idReason;
    }

    public void setIdReason(String idReason) {
        this.idReason = idReason;
    }

    public String getNameReason() {
        return nameReason;
    }

    public void setNameReason(String nameReason) {
        this.nameReason = nameReason;
    }

    public String getDescReason() {
        return descReason;
    }

    public void setDescReason(String descReason) {
        this.descReason = descReason;
    }

    public String getPhotoReason() {
        return photoReason;
    }

    public void setPhotoReason(String photoReason) {
        this.photoReason = photoReason;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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
        return materialId;
    }

    public void setIdMat(String idMat) {
        this.materialId = idMat;
    }

    public double getQty1() {
        return qty;
    }

    public void setQty1(double qty) {
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

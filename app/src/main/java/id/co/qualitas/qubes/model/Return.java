package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Return {
    private String idReturn;
    private String itemNr;
    private String idRow;
    private String id;
    private double qty;

    private BigDecimal qty2;
    private String uom;

    private String uom2;
    private String batch;
    private String expiredDateString;
    private String name;
    private String goodStock;
    private String reason;
    private String idHeader;
    private String description;
    private String error;

    private String noBrb;
    private String noRr;

    @SerializedName("listUomName")
    private ArrayList<Uom> listUomName;

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
    private int isSync;
    private String id_material_group;
    private String material_group_name;
    private String id_product_group;
    private String name_product_group;

    public Return() {
    }

    public Return(String idReturn, String expiredDate) {
        this.idReturn = idReturn;
        this.expiredDate = expiredDate;
    }

    public String getIdReturn() {
        return idReturn;
    }

    public void setIdReturn(String idReturn) {
        this.idReturn = idReturn;
    }

    public String getItemNr() {
        return itemNr;
    }

    public void setItemNr(String itemNr) {
        this.itemNr = itemNr;
    }

    public String getIdRow() {
        return idRow;
    }

    public void setIdRow(String idRow) {
        this.idRow = idRow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public BigDecimal getQty2() {
        return qty2;
    }

    public void setQty2(BigDecimal qty2) {
        this.qty2 = qty2;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getUom2() {
        return uom2;
    }

    public void setUom2(String uom2) {
        this.uom2 = uom2;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getExpiredDateString() {
        return expiredDateString;
    }

    public void setExpiredDateString(String expiredDateString) {
        this.expiredDateString = expiredDateString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

    public ArrayList<Uom> getListUomName() {
        return listUomName;
    }

    public void setListUomName(ArrayList<Uom> listUomName) {
        this.listUomName = listUomName;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String[] getItemsReason() {
        return itemsReason;
    }

    public void setItemsReason(String[] itemsReason) {
        this.itemsReason = itemsReason;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public String getId_material_group() {
        return id_material_group;
    }

    public void setId_material_group(String id_material_group) {
        this.id_material_group = id_material_group;
    }

    public String getMaterial_group_name() {
        return material_group_name;
    }

    public void setMaterial_group_name(String material_group_name) {
        this.material_group_name = material_group_name;
    }

    public String getId_product_group() {
        return id_product_group;
    }

    public void setId_product_group(String id_product_group) {
        this.id_product_group = id_product_group;
    }

    public String getName_product_group() {
        return name_product_group;
    }

    public void setName_product_group(String name_product_group) {
        this.name_product_group = name_product_group;
    }
}

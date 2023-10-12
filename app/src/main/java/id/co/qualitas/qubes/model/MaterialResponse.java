package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class MaterialResponse implements Serializable {
    private String idOutlet;

    @SerializedName("id")
    private String id;

    @SerializedName("idMaterial")
    private String idMaterial;

    @SerializedName("materialName")
    private String materialName;

    @SerializedName("idMaterialClass")
    private String idMaterialClass;

    @SerializedName("qty1")
    private String qty1;


    @SerializedName("qty2")
    private String qty2;


    @SerializedName("uom1")
    private String uom1;


    @SerializedName("uom2")
    private String uom2;

    @SerializedName("stockQty1")
    private BigDecimal stockQty1;


    @SerializedName("stockQty2")
    private BigDecimal stockQty2;


    @SerializedName("stockUom1")
    private String stockUom1;


    @SerializedName("stockUom2")
    private String stockUom2;

    @SerializedName("stockQty1C")
    private BigDecimal stockQty1C;


    @SerializedName("stockQty2C")
    private BigDecimal stockQty2C;


    @SerializedName("stockUom1C")
    private String stockUom1C;


    @SerializedName("stockUom2C")
    private String stockUom2C;

    @SerializedName("uomName")
    private String uomName;

    @SerializedName("listUomName")
    private ArrayList<Uom> listUomName;

    @SerializedName("qty1StoreCheck")
    private BigDecimal qty1StoreCheck;

    @SerializedName("uom1StoreCheck")
    private String uom1StoreCheck;

    @SerializedName("qty2StoreCheck")
    private BigDecimal qty2StoreCheck;

    @SerializedName("uom2StoreCheck")
    private String uom2StoreCheck;

    @SerializedName("newQty1")
    private BigDecimal newQty1;

    @SerializedName("newUom1")
    private String newUom1;

    @SerializedName("newQty2")
    private BigDecimal newQty2;

    @SerializedName("newUom2")
    private String newUom2;

    private BigDecimal price;

    private int q1TypePos;
    private int q2TypePos;

    private ArrayList<Uom> listUomOrder;
    private ArrayList<Uom> listUomReturn;
    private ArrayList<JenisJualandTop> listTop;

    private String isLastTO;
    private String conversion;

    private String status;

    private String id_store_check;

    private String dateString;

    private boolean color;

    private String index;

    private BigDecimal lastQty1;
    private BigDecimal lastQty2;
    private String lastUom1;
    private String lastUom2;

    private String id_mobile;

    private Boolean selected;

    private Boolean deleted;

    private String date_mobile;
    private int pos;

    public MaterialResponse() {
    }

    public MaterialResponse(String idMaterial, String materialName, String idMaterialClass, BigDecimal stockQty1, BigDecimal stockQty2, String stockUom1, String stockUom2) {
        this.idMaterial = idMaterial;
        this.materialName = materialName;
        this.idMaterialClass = idMaterialClass;
        this.stockQty1 = stockQty1;
        this.stockQty2 = stockQty2;
        this.stockUom1 = stockUom1;
        this.stockUom2 = stockUom2;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getDate_mobile() {
        return date_mobile;
    }

    public void setDate_mobile(String date_mobile) {
        this.date_mobile = date_mobile;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getId_mobile() {
        return id_mobile;
    }

    public void setId_mobile(String id_mobile) {
        this.id_mobile = id_mobile;
    }

    public BigDecimal getLastQty1() {
        return lastQty1;
    }

    public void setLastQty1(BigDecimal lastQty1) {
        this.lastQty1 = lastQty1;
    }

    public BigDecimal getLastQty2() {
        return lastQty2;
    }

    public void setLastQty2(BigDecimal lastQty2) {
        this.lastQty2 = lastQty2;
    }

    public String getLastUom1() {
        return lastUom1;
    }

    public void setLastUom1(String lastUom1) {
        this.lastUom1 = lastUom1;
    }

    public String getLastUom2() {
        return lastUom2;
    }

    public void setLastUom2(String lastUom2) {
        this.lastUom2 = lastUom2;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getId_store_check() {
        return id_store_check;
    }

    public void setId_store_check(String id_store_check) {
        this.id_store_check = id_store_check;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getStockQty1C() {
        return stockQty1C;
    }

    public void setStockQty1C(BigDecimal stockQty1C) {
        this.stockQty1C = stockQty1C;
    }

    public BigDecimal getStockQty2C() {
        return stockQty2C;
    }

    public void setStockQty2C(BigDecimal stockQty2C) {
        this.stockQty2C = stockQty2C;
    }

    public String getStockUom1C() {
        return stockUom1C;
    }

    public void setStockUom1C(String stockUom1C) {
        this.stockUom1C = stockUom1C;
    }

    public String getStockUom2C() {
        return stockUom2C;
    }

    public void setStockUom2C(String stockUom2C) {
        this.stockUom2C = stockUom2C;
    }

    public String getIsLastTO() {
        return isLastTO;
    }

    public void setIsLastTO(String isLastTO) {
        this.isLastTO = isLastTO;
    }

    public ArrayList<JenisJualandTop> getListTop() {
        return listTop;
    }

    public void setListTop(ArrayList<JenisJualandTop> listTop) {
        this.listTop = listTop;
    }

    public ArrayList<Uom> getListUomOrder() {
        return listUomOrder;
    }

    public void setListUomOrder(ArrayList<Uom> listUomOrder) {
        this.listUomOrder = listUomOrder;
    }

    public ArrayList<Uom> getListUomReturn() {
        return listUomReturn;
    }

    public void setListUomReturn(ArrayList<Uom> listUomReturn) {
        this.listUomReturn = listUomReturn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQ1TypePos() {
        return q1TypePos;
    }

    public void setQ1TypePos(int q1TypePos) {
        this.q1TypePos = q1TypePos;
    }

    public int getQ2TypePos() {
        return q2TypePos;
    }

    public void setQ2TypePos(int q2TypePos) {
        this.q2TypePos = q2TypePos;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public ArrayList<Uom> getListUomName() {
        return listUomName;
    }

    public void setListUomName(ArrayList<Uom> listUomName) {
        this.listUomName = listUomName;
    }

    public BigDecimal getNewQty1() {
        return newQty1;
    }

    public void setNewQty1(BigDecimal newQty1) {
        this.newQty1 = newQty1;
    }

    public String getNewUom1() {
        return newUom1;
    }

    public void setNewUom1(String newUom1) {
        this.newUom1 = newUom1;
    }

    public BigDecimal getNewQty2() {
        return newQty2;
    }

    public void setNewQty2(BigDecimal newQty2) {
        this.newQty2 = newQty2;
    }

    public String getNewUom2() {
        return newUom2;
    }

    public void setNewUom2(String newUom2) {
        this.newUom2 = newUom2;
    }


    public String getIdMaterialClass() {
        return idMaterialClass;
    }

    public void setIdMaterialClass(String idMaterialClass) {
        this.idMaterialClass = idMaterialClass;
    }

    public String getQty1() {
        return qty1;
    }

    public void setQty1(String qty1) {
        this.qty1 = qty1;
    }

    public String getQty2() {
        return qty2;
    }

    public void setQty2(String qty2) {
        this.qty2 = qty2;
    }

    public String getUom1() {
        return uom1;
    }

    public void setUom1(String uom1) {
        this.uom1 = uom1;
    }

    public String getUom2() {
        return uom2;
    }

    public void setUom2(String uom2) {
        this.uom2 = uom2;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getStockQty1() {
        return stockQty1;
    }

    public void setStockQty1(BigDecimal stockQty1) {
        this.stockQty1 = stockQty1;
    }

    public BigDecimal getStockQty2() {
        return stockQty2;
    }

    public void setStockQty2(BigDecimal stockQty2) {
        this.stockQty2 = stockQty2;
    }

    public String getStockUom1() {
        return stockUom1;
    }

    public void setStockUom1(String stockUom1) {
        this.stockUom1 = stockUom1;
    }

    public String getStockUom2() {
        return stockUom2;
    }

    public void setStockUom2(String stockUom2) {
        this.stockUom2 = stockUom2;
    }

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public BigDecimal getQty1StoreCheck() {
        return qty1StoreCheck;
    }

    public void setQty1StoreCheck(BigDecimal qty1StoreCheck) {
        this.qty1StoreCheck = qty1StoreCheck;
    }

    public String getUom1StoreCheck() {
        return uom1StoreCheck;
    }

    public void setUom1StoreCheck(String uom1StoreCheck) {
        this.uom1StoreCheck = uom1StoreCheck;
    }

    public BigDecimal getQty2StoreCheck() {
        return qty2StoreCheck;
    }

    public void setQty2StoreCheck(BigDecimal qty2StoreCheck) {
        this.qty2StoreCheck = qty2StoreCheck;
    }

    public String getUom2StoreCheck() {
        return uom2StoreCheck;
    }

    public void setUom2StoreCheck(String uom2StoreCheck) {
        this.uom2StoreCheck = uom2StoreCheck;
    }

    public String getConversion() {
        return conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }
}


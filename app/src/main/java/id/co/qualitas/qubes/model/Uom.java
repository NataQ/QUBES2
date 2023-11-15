package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Uom implements Serializable {
    private String id;

    @SerializedName("uomName")
    private String uomName;

    @SerializedName("price")
    private int price;

//    @SerializedName("discount")
//    private BigDecimal discount;

    @SerializedName("priceNett")
    private int priceNett;

    @SerializedName("priceDiscount")
    private int priceDiscount;

    @SerializedName("discountValue")
    private int discountValue;
    @SerializedName("idMaterial")
    private String idMaterial;

    @SerializedName("is_order")
    private boolean is_order;

    @SerializedName("is_return")
    private boolean is_return;

    //aspp
    String idHeader;
    String id_material;
    String id_uom;
    int conversion;
    double qty_min;
    double qty_max;

    public double getQty_min() {
        return qty_min;
    }

    public void setQty_min(double qty_min) {
        this.qty_min = qty_min;
    }

    public double getQty_max() {
        return qty_max;
    }

    public void setQty_max(double qty_max) {
        this.qty_max = qty_max;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public int getConversion() {
        return conversion;
    }

    public boolean isIs_order() {
        return is_order;
    }

    public boolean isIs_return() {
        return is_return;
    }

    public String getId_material() {
        return id_material;
    }

    public void setId_material(String id_material) {
        this.id_material = id_material;
    }

    public String getId_uom() {
        return id_uom;
    }

    public void setId_uom(String id_uom) {
        this.id_uom = id_uom;
    }

    public void setConversion(int conversion) {
        this.conversion = conversion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public boolean is_order() {
        return is_order;
    }

    public void setIs_order(boolean is_order) {
        this.is_order = is_order;
    }

    public boolean is_return() {
        return is_return;
    }

    public void setIs_return(boolean is_return) {
        this.is_return = is_return;
    }

    public int getPriceNett() {
        return priceNett;
    }

    public void setPriceNett(int priceNett) {
        this.priceNett = priceNett;
    }

    public int getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(int priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

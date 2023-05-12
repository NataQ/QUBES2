package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Foo on 2/2/2017.
 */

public class UnitOfMeasure implements Serializable {
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

    @SerializedName("conversion")
    private double conversion;

    @SerializedName("idMaterial")
    private String idMaterial;

    @SerializedName("is_order")
    private boolean is_order;

    @SerializedName("is_return")
    private boolean is_return;


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

    public double getConversion() {
        return conversion;
    }

    public void setConversion(double conversion) {
        this.conversion = conversion;
    }

//    public int getDiscount() {
//        return discount;
//    }
//
//    public void setDiscount(int discount) {
//        this.discount = discount;
//    }

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

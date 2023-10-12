package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Foo on 4/22/2016.
 */
public class VisitOrderDetailResponse implements Serializable {
    private String idOutlet;
    private String idSalesman;

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(String idSalesman) {
        this.idSalesman = idSalesman;
    }

    @SerializedName("materialName")
    private String materialName;

    @SerializedName("idMaterial")
    private String idMaterial;

    @SerializedName("stockQty1")
    private BigDecimal stockQty1;

    @SerializedName("stockUom1")
    private String stockUom1;

    @SerializedName("stockQty2")
    private BigDecimal stockQty2;

    @SerializedName("stockUom2")
    private String stockUom2;

    @SerializedName("orderPlanQty1")
    private BigDecimal orderPlanQty1;

    @SerializedName("orderPlanUom1")
    private String orderPlanUom1;

    @SerializedName("orderPlanQty2")
    private BigDecimal orderPlanQty2;

    @SerializedName("orderPlanUom2")
    private String orderPlanUom2;

    @SerializedName("toDetailQty1")
    private BigDecimal toDetailQty1;

    @SerializedName("toDetailUom1")
    private String toDetailUom1;

    @SerializedName("toDetailQty2")
    private BigDecimal toDetailQty2;

    @SerializedName("toDetailUom2")
    private String toDetailUom2;

    @SerializedName("saranOrderQty1")
    private BigDecimal saranOrderQty1;

    @SerializedName("saranOrderUom1")
    private String saranOrderUom1;

    @SerializedName("saranOrderQty2")
    private BigDecimal saranOrderQty2;

    @SerializedName("saranOrderUom2")
    private String saranOrderUom2;

    @SerializedName("topf")//hari/tanggal top
    private int topf;

    @SerializedName("top_sap")//hari/tanggal top
    private String top_sap;

    @SerializedName("listUom")
    private List<Uom> listUom;

    @SerializedName("jenisJualandTop")
    private List<JenisJualandTop> jenisJualandTop;

    @SerializedName("qty1")
    private BigDecimal qty1;

    @SerializedName("uom1")
    private String uom1;

    @SerializedName("qty2")
    private BigDecimal qty2;

    @SerializedName("uom2")
    private String uom2;

    //    @SerializedName("price")
    private BigDecimal price;

    @SerializedName("disc")
    private BigDecimal disc;

    @SerializedName("priceNett")
    private String priceNett;

    //save position
    private int posJenisJual;
    private int posQ1Type;
    private int posQ2Type;

    private String top;

    private String jenisJual;

    private BigDecimal totalPrice;

    private Boolean stopLoadQ;
    private Boolean stopLoadQ2;
    private Boolean stopLoadUom;
    private Boolean stopLoadUom2;
    private Boolean stopLoadJ;

    private String idTo;

    private String id_price;

    private String idToPrice;

    private BigDecimal taxValue;

    private String tax;

    private BigDecimal priceBfr;

    private BigDecimal lastQty1;
    private BigDecimal lastQty2;
    private String lastUom1;
    private String lastUom2;

    private BigDecimal lastCheckQty1;
    private String lastCheckUom1;

    private BigDecimal lastCheckQty2;
    private String lastCheckUom2;

    private List<ToPrice> listToPrice;

    public BigDecimal getLastCheckQty2() {
        return lastCheckQty2;
    }

    public void setLastCheckQty2(BigDecimal lastCheckQty2) {
        this.lastCheckQty2 = lastCheckQty2;
    }

    public String getLastCheckUom2() {
        return lastCheckUom2;
    }

    public void setLastCheckUom2(String lastCheckUom2) {
        this.lastCheckUom2 = lastCheckUom2;
    }

    public BigDecimal getLastCheckQty1() {
        return lastCheckQty1;
    }

    public void setLastCheckQty1(BigDecimal lastCheckQty1) {
        this.lastCheckQty1 = lastCheckQty1;
    }

    public String getLastCheckUom1() {
        return lastCheckUom1;
    }

    public void setLastCheckUom1(String lastCheckUom1) {
        this.lastCheckUom1 = lastCheckUom1;
    }

    public List<ToPrice> getListToPrice() {
        return listToPrice;
    }

    public void setListToPrice(List<ToPrice> listToPrice) {
        this.listToPrice = listToPrice;
    }

    public BigDecimal getLastOrderQty1() {
        return lastQty1;
    }

    public void setLastOrderQty1(BigDecimal lastOrderQty1) {
        this.lastQty1 = lastOrderQty1;
    }

    public BigDecimal getLastOrderQty2() {
        return lastQty2;
    }

    public void setLastOrderQty2(BigDecimal lastOrderQty2) {
        this.lastQty2 = lastOrderQty2;
    }

    public String getLastOrderUom1() {
        return lastUom1;
    }

    public void setLastOrderUom1(String lastOrderUom1) {
        this.lastUom1 = lastOrderUom1;
    }

    public String getLastOrderUom2() {
        return lastUom2;
    }

    public void setLastOrderUom2(String lastOrderUom2) {
        this.lastUom2 = lastOrderUom2;
    }

    public BigDecimal getPriceBfr() {
        return priceBfr;
    }

    public void setPriceBfr(BigDecimal priceBfr) {
        this.priceBfr = priceBfr;
    }

    public BigDecimal getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getIdToPrice() {
        return idToPrice;
    }

    public void setIdToPrice(String idToPrice) {
        this.idToPrice = idToPrice;
    }

    private ArrayList<ToPrice> listOneTimeDiscount;

    public String getId_price() {
        return id_price;
    }

    public void setId_price(String id_price) {
        this.id_price = id_price;
    }

    public String getIdTo() {
        return idTo;
    }

    public void setIdTo(String idTo) {
        this.idTo = idTo;
    }

    public Boolean getStopLoadUom() {
        return stopLoadUom;
    }

    public void setStopLoadUom(Boolean stopLoadUom) {
        this.stopLoadUom = stopLoadUom;
    }

    public Boolean getStopLoadUom2() {
        return stopLoadUom2;
    }

    public void setStopLoadUom2(Boolean stopLoadUom2) {
        this.stopLoadUom2 = stopLoadUom2;
    }


    public Boolean getStopLoadQ2() {
        return stopLoadQ2;
    }

    public void setStopLoadQ2(Boolean stopLoadQ2) {
        this.stopLoadQ2 = stopLoadQ2;
    }

    public ArrayList<ToPrice> getListOneTimeDiscount() {
        return listOneTimeDiscount;
    }

    public void setListOneTimeDiscount(ArrayList<ToPrice> listOneTimeDiscount) {
        this.listOneTimeDiscount = listOneTimeDiscount;
    }

    public Boolean getStopLoadQ() {
        return stopLoadQ;
    }

    public void setStopLoadQ(Boolean stopLoadQ) {
        this.stopLoadQ = stopLoadQ;
    }

    public Boolean getStopLoadJ() {
        return stopLoadJ;
    }

    public void setStopLoadJ(Boolean stopLoad) {
        this.stopLoadJ = stopLoad;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
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

    public int getPosJenisJual() {
        return posJenisJual;
    }

    public void setPosJenisJual(int posJenisJual) {
        this.posJenisJual = posJenisJual;
    }

    public int getPosQ1Type() {
        return posQ1Type;
    }

    public void setPosQ1Type(int posQ1Type) {
        this.posQ1Type = posQ1Type;
    }

    public int getPosQ2Type() {
        return posQ2Type;
    }

    public void setPosQ2Type(int posQ2Type) {
        this.posQ2Type = posQ2Type;
    }

    public List<Uom> getListUom() {
        return listUom;
    }

    public void setListUom(List<Uom> listUom) {
        this.listUom = listUom;
    }


    public BigDecimal getQty1() {
        return qty1;
    }

    public void setQty1(BigDecimal qty1) {
        this.qty1 = qty1;
    }

    public BigDecimal getQty2() {
        return qty2;
    }

    public void setQty2(BigDecimal qty2) {
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

    public BigDecimal getDisc() {
        return disc;
    }

    public void setDisc(BigDecimal disc) {
        this.disc = disc;
    }

    public String getPriceNett() {
        return priceNett;
    }

    public void setPriceNett(String priceNett) {
        this.priceNett = priceNett;
    }

    public BigDecimal getSaranOrderQty1() {
        return saranOrderQty1;
    }

    public void setSaranOrderQty1(BigDecimal saranOrderQty1) {
        this.saranOrderQty1 = saranOrderQty1;
    }

    public String getSaranOrderUom1() {
        return saranOrderUom1;
    }

    public void setSaranOrderUom1(String saranOrderUom1) {
        this.saranOrderUom1 = saranOrderUom1;
    }

    public BigDecimal getSaranOrderQty2() {
        return saranOrderQty2;
    }

    public void setSaranOrderQty2(BigDecimal saranOrderQty2) {
        this.saranOrderQty2 = saranOrderQty2;
    }

    public String getSaranOrderUom2() {
        return saranOrderUom2;
    }

    public void setSaranOrderUom2(String saranOrderUom2) {
        this.saranOrderUom2 = saranOrderUom2;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public BigDecimal getStockQty1() {
        return stockQty1;
    }

    public void setStockQty1(BigDecimal stockQty1) {
        this.stockQty1 = stockQty1;
    }

    public String getStockUom1() {
        return stockUom1;
    }

    public void setStockUom1(String stockUom1) {
        this.stockUom1 = stockUom1;
    }

    public BigDecimal getStockQty2() {
        return stockQty2;
    }

    public void setStockQty2(BigDecimal stockQty2) {
        this.stockQty2 = stockQty2;
    }

    public String getStockUom2() {
        return stockUom2;
    }

    public void setStockUom2(String stockUom2) {
        this.stockUom2 = stockUom2;
    }

    public BigDecimal getOrderPlanQty1() {
        return orderPlanQty1;
    }

    public void setOrderPlanQty1(BigDecimal orderPlanQty1) {
        this.orderPlanQty1 = orderPlanQty1;
    }

    public String getOrderPlanUom1() {
        return orderPlanUom1;
    }

    public void setOrderPlanUom1(String orderPlanUom1) {
        this.orderPlanUom1 = orderPlanUom1;
    }

    public BigDecimal getOrderPlanQty2() {
        return orderPlanQty2;
    }

    public void setOrderPlanQty2(BigDecimal orderPlanQty2) {
        this.orderPlanQty2 = orderPlanQty2;
    }

    public String getOrderPlanUom2() {
        return orderPlanUom2;
    }

    public void setOrderPlanUom2(String orderPlanUom2) {
        this.orderPlanUom2 = orderPlanUom2;
    }

    public BigDecimal getToDetailQty1() {
        return toDetailQty1;
    }

    public void setToDetailQty1(BigDecimal toDetailQty1) {
        this.toDetailQty1 = toDetailQty1;
    }

    public String getToDetailUom1() {
        return toDetailUom1;
    }

    public void setToDetailUom1(String toDetailUom1) {
        this.toDetailUom1 = toDetailUom1;
    }

    public BigDecimal getToDetailQty2() {
        return toDetailQty2;
    }

    public void setToDetailQty2(BigDecimal toDetailQty2) {
        this.toDetailQty2 = toDetailQty2;
    }

    public String getToDetailUom2() {
        return toDetailUom2;
    }

    public void setToDetailUom2(String toDetailUom2) {
        this.toDetailUom2 = toDetailUom2;
    }

    public int getTopf() {
        return topf;
    }

    public void setTopf(int topf) {
        this.topf = topf;
    }

    public List<JenisJualandTop> getJenisJualandTop() {
        return jenisJualandTop;
    }

    public void setJenisJualandTop(List<JenisJualandTop> jenisJualandTop) {
        this.jenisJualandTop = jenisJualandTop;
    }
}

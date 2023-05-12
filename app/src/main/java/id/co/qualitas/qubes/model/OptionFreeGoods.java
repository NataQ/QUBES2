package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**em
 * Created by Wiliam on 11/15/2017.
 */

public class OptionFreeGoods implements Serializable{

    private String id;
    private String tableName;
    private String item_nr;
    private String doc_no;
    private String cond_type;
    private String id_material;
    private BigDecimal qty;
    private String uom;
    private boolean one_time_discount;
    private String kc_no;
    private String klasifikasi;
    private String top;
    private String jenisJual;
    private String materialName;
    private BigDecimal amount;
    private String amountUom;

    private BigDecimal tax;

    private String idTo;

    private BigDecimal qtyP;
    private BigDecimal amountP;

    public BigDecimal getQtyP() {
        return qtyP;
    }

    public void setQtyP(BigDecimal qtyP) {
        this.qtyP = qtyP;
    }

    public BigDecimal getAmountP() {
        return amountP;
    }

    public void setAmountP(BigDecimal amountP) {
        this.amountP = amountP;
    }

    public String getIdTo() {
        return idTo;
    }

    public void setIdTo(String idTo) {
        this.idTo = idTo;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getItem_nr() {
        return item_nr;
    }

    public void setItem_nr(String item_nr) {
        this.item_nr = item_nr;
    }

    public String getDoc_no() {
        return doc_no;
    }

    public void setDoc_no(String doc_no) {
        this.doc_no = doc_no;
    }

    public String getCond_type() {
        return cond_type;
    }

    public void setCond_type(String cond_type) {
        this.cond_type = cond_type;
    }

    public String getId_material() {
        return id_material;
    }

    public void setId_material(String id_material) {
        this.id_material = id_material;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountUom() {
        return amountUom;
    }

    public void setAmountUom(String amountUom) {
        this.amountUom = amountUom;
    }

    public boolean isOne_time_discount() {
        return one_time_discount;
    }

    public void setOne_time_discount(boolean one_time_discount) {
        this.one_time_discount = one_time_discount;
    }

    public String getKc_no() {
        return kc_no;
    }

    public void setKc_no(String kc_no) {
        this.kc_no = kc_no;
    }

    public String getKlasifikasi() {
        return klasifikasi;
    }

    public void setKlasifikasi(String klasifikasi) {
        this.klasifikasi = klasifikasi;
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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}

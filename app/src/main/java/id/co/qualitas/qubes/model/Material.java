package id.co.qualitas.qubes.model;

import java.util.List;

/**
 * Created by Natalia on 2/25/2016.
 */
public class Material {
    private String idMaterial;
    private String materialCode;
    private String materialQty;
    private int qty;
    private double amount;
    private String desc;
    private String batch;
    private byte[] attachment;
    private String DeliveryNumber;
    private String klasifikasi;
    private String price;
    private String uom;
    private int totalDiscount;
    private Discount discount;
    private List<Discount> extra;
    private List<Material> extraItem;
    private boolean isChecked;

    public Material() {
    }

    public Material(String idMaterial, String materialCode, int qty, String uom, String price, int totalDiscount) {
        this.idMaterial = idMaterial;
        this.materialCode = materialCode;
        this.qty = qty;
        this.price = price;
        this.uom = uom;
        this.totalDiscount = totalDiscount;
    }

    public Material(String idMaterial, String materialCode, int qty, String uom, String price, int totalDiscount, List<Material> extraItem) {
        this.idMaterial = idMaterial;
        this.materialCode = materialCode;
        this.qty = qty;
        this.price = price;
        this.uom = uom;
        this.totalDiscount = totalDiscount;
        this.extraItem = extraItem;
    }

    public Material(String idMaterial, String materialCode, String price, double amount) {
        this.idMaterial = idMaterial;
        this.materialCode = materialCode;
        this.amount = amount;
        this.price = price;
    }

    public Material(String klasifikasi, String materialCode, int qty, String price, String uom) {
        this.materialCode = materialCode;
        this.qty = qty;
        this.klasifikasi = klasifikasi;
        this.price = price;
        this.uom = uom;
    }

    public Material(String klasifikasi, String materialCode, int qty, String price, String uom, List<Material> extraItem) {
        this.materialCode = materialCode;
        this.qty = qty;
        this.klasifikasi = klasifikasi;
        this.price = price;
        this.uom = uom;
        this.extraItem = extraItem;
    }

    public Material(String materialCode, int qty) {
        this.materialCode = materialCode;
        this.qty = qty;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(int totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Material(String idMaterial, String materialCode, String price) {
        this.idMaterial = idMaterial;
        this.materialCode = materialCode;
        this.price = price;
    }

    public Material(String idMaterial, String materialCode, String materialQty, String uom) {
        this.idMaterial = idMaterial;
        this.materialCode = materialCode;
        this.materialQty = materialQty;
        this.uom = uom;
    }

    public List<Material> getExtraItem() {
        return extraItem;
    }

    public void setExtraItem(List<Material> extraItem) {
        this.extraItem = extraItem;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public List<Discount> getExtra() {
        return extra;
    }

    public void setExtra(List<Discount> extra) {
        this.extra = extra;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMaterialQty() {
        return materialQty;
    }

    public void setMaterialQty(String materialQty) {
        this.materialQty = materialQty;
    }

    public String getDeliveryNumber() {
        return DeliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        DeliveryNumber = deliveryNumber;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getKlasifikasi() {
        return klasifikasi;
    }

    public void setKlasifikasi(String klasifikasi) {
        this.klasifikasi = klasifikasi;
    }
}

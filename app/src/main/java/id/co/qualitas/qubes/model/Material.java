package id.co.qualitas.qubes.model;

import androidx.annotation.NonNull;

import java.util.List;
public class Material implements Cloneable{
    private String idHeader;
    private String materialId;
    private String materialName;
    private String materialCode;
    private String materialQty;
    private int qty;
    private int qtySisa;
    private double amount;
    private double amountPaid;
    private String desc;
    private String batch;
    private byte[] attachment;
    private String DeliveryNumber;
    private String klasifikasi;
    private double price;
    private String uom;
    private String uomSisa;
    private int totalDiscount;
    private double total;
    private Discount discount;
    private List<Discount> extra;
    private List<Material> extraItem;
    private boolean isChecked;
    private boolean isSync;

    public Material() {
    }

    public Material(String materialId, String materialName, int qty, String uom) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.qty = qty;
        this.uom = uom;
    }

    public Material(String materialId, String materialName, int qty, String uom, double price, int totalDiscount) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.qty = qty;
        this.price = price;
        this.uom = uom;
        this.totalDiscount = totalDiscount;
    }

    public Material(String materialId, String materialName, int qty, String uom, double price, int totalDiscount, List<Material> extraItem) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.qty = qty;
        this.price = price;
        this.uom = uom;
        this.totalDiscount = totalDiscount;
        this.extraItem = extraItem;
    }

    public Material(String materialId, String materialName, double price, double amount) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.amount = amount;
        this.price = price;
    }

    public Material(String klasifikasi, String materialName, int qty, double price, String uom) {
        this.materialName = materialName;
        this.qty = qty;
        this.klasifikasi = klasifikasi;
        this.price = price;
        this.uom = uom;
    }

    public Material(String klasifikasi, String materialName, int qty, double price, String uom, List<Material> extraItem) {
        this.materialName = materialName;
        this.qty = qty;
        this.klasifikasi = klasifikasi;
        this.price = price;
        this.uom = uom;
        this.extraItem = extraItem;
    }

    public Material(String materialName, int qty) {
        this.materialName = materialName;
        this.qty = qty;
    }

    public Material(String materialId, String materialName, double price) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.price = price;
    }

    public Material(String materialId, String materialName, String materialQty, String uom) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.materialQty = materialQty;
        this.uom = uom;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public int getQtySisa() {
        return qtySisa;
    }

    public void setQtySisa(int qtySisa) {
        this.qtySisa = qtySisa;
    }

    public String getUomSisa() {
        return uomSisa;
    }

    public void setUomSisa(String uomSisa) {
        this.uomSisa = uomSisa;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();

    }
}

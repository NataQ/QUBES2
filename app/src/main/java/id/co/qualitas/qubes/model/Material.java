package id.co.qualitas.qubes.model;

import androidx.annotation.NonNull;

import java.util.List;
public class Material implements Cloneable{
    private String idHeader;
    private String materialid;
    private String materialname;
    private String materialCode;
    private String materialQty;
    private double qty;
    private double qtySisa;
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
    private double totalDiscount;
    private double total;
    private Discount discount;
    private List<Discount> extra;
    private List<Material> extraItem;
    private boolean isChecked;
    private boolean isSync;
    private String groupname;
    private String loadnumber;
    private int materialgroupid;
    private int itemno;

    public Material() {
    }

    public Material(String materialid, String materialname, double qty, String uom) {
        this.materialid = materialid;
        this.materialname = materialname;
        this.qty = qty;
        this.uom = uom;
    }

    public Material(String materialid, String materialname, double qty, String uom, double price, double totalDiscount) {
        this.materialid = materialid;
        this.materialname = materialname;
        this.qty = qty;
        this.price = price;
        this.uom = uom;
        this.totalDiscount = totalDiscount;
    }

    public Material(String materialid, String materialname, double qty, String uom, double price, double totalDiscount, List<Material> extraItem) {
        this.materialid = materialid;
        this.materialname = materialname;
        this.qty = qty;
        this.price = price;
        this.uom = uom;
        this.totalDiscount = totalDiscount;
        this.extraItem = extraItem;
    }

    public Material(String materialid, String materialname, double price, double amount) {
        this.materialid = materialid;
        this.materialname = materialname;
        this.amount = amount;
        this.price = price;
    }

    public Material(String klasifikasi, String materialname, double qty, double price, String uom) {
        this.materialname = materialname;
        this.qty = qty;
        this.klasifikasi = klasifikasi;
        this.price = price;
        this.uom = uom;
    }

    public Material(String klasifikasi, String materialname, double qty, double price, String uom, List<Material> extraItem) {
        this.materialname = materialname;
        this.qty = qty;
        this.klasifikasi = klasifikasi;
        this.price = price;
        this.uom = uom;
        this.extraItem = extraItem;
    }

    public Material(String materialname, double qty) {
        this.materialname = materialname;
        this.qty = qty;
    }

    public Material(String materialid, String materialname, double price) {
        this.materialid = materialid;
        this.materialname = materialname;
        this.price = price;
    }

    public Material(String materialid, String materialname, String materialQty, String uom) {
        this.materialid = materialid;
        this.materialname = materialname;
        this.materialQty = materialQty;
        this.uom = uom;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getLoadnumber() {
        return loadnumber;
    }

    public void setLoadnumber(String loadnumber) {
        this.loadnumber = loadnumber;
    }

    public int getMaterialgroupid() {
        return materialgroupid;
    }

    public void setMaterialgroupid(int materialgroupid) {
        this.materialgroupid = materialgroupid;
    }

    public int getItemno() {
        return itemno;
    }

    public void setItemno(int itemno) {
        this.itemno = itemno;
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

    public double getQtySisa() {
        return qtySisa;
    }

    public void setQtySisa(double qtySisa) {
        this.qtySisa = qtySisa;
    }

    public String getUomSisa() {
        return uomSisa;
    }

    public void setUomSisa(String uomSisa) {
        this.uomSisa = uomSisa;
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
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

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
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
    public Object clone() throws CloneNotSupportedException {
        return super.clone();

    }
}

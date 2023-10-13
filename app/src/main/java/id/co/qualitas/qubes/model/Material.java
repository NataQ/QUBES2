package id.co.qualitas.qubes.model;

import androidx.annotation.NonNull;

import java.util.List;
public class Material implements Cloneable{
    private String idHeader;//aspp
    private int id;//aspp
    private String nama;//aspp
    private String materialCode;
    private String materialQty;
    //aspp
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
    private int is_sync;
    private String material_sales;
    private String material_group_name;
    private String load_number;
    private String no_invoice;
    private int id_material_group;

    public Material() {
    }

    public Material(String id, String nama, double qty, String uom) {
        this.id = Integer.parseInt(id);
        this.nama = nama;
        this.qty = qty;
        this.uom = uom;
    }

    public Material(String id, String nama, double qty, String uom, double price, double totalDiscount) {
        this.id = Integer.parseInt(id);
        this.nama = nama;
        this.qty = qty;
        this.price = price;
        this.uom = uom;
        this.totalDiscount = totalDiscount;
    }

    public Material(String id, String nama, double qty, String uom, double price, double totalDiscount, List<Material> extraItem) {
        this.id = Integer.parseInt(id);
        this.nama = nama;
        this.qty = qty;
        this.price = price;
        this.uom = uom;
        this.totalDiscount = totalDiscount;
        this.extraItem = extraItem;
    }

    public Material(String id, String nama, double price, double amount) {
        this.id = Integer.parseInt(id);
        this.nama = nama;
        this.amount = amount;
        this.price = price;
    }

    public Material(String klasifikasi, String nama, double qty, double price, String uom) {
        this.nama = nama;
        this.qty = qty;
        this.klasifikasi = klasifikasi;
        this.price = price;
        this.uom = uom;
    }

    public Material(String klasifikasi, String nama, double qty, double price, String uom, List<Material> extraItem) {
        this.nama = nama;
        this.qty = qty;
        this.klasifikasi = klasifikasi;
        this.price = price;
        this.uom = uom;
        this.extraItem = extraItem;
    }

    public Material(String nama, double qty) {
        this.nama = nama;
        this.qty = qty;
    }

    public Material(String id, String nama, double price) {
        this.id = Integer.parseInt(id);
        this.nama = nama;
        this.price = price;
    }

    public Material(String id, String nama, String materialQty, String uom) {
        this.id = Integer.parseInt(id);
        this.nama = nama;
        this.materialQty = materialQty;
        this.uom = uom;
    }

    public String getNo_invoice() {
        return no_invoice;
    }

    public void setNo_invoice(String no_invoice) {
        this.no_invoice = no_invoice;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialQty() {
        return materialQty;
    }

    public void setMaterialQty(String materialQty) {
        this.materialQty = materialQty;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getQtySisa() {
        return qtySisa;
    }

    public void setQtySisa(double qtySisa) {
        this.qtySisa = qtySisa;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
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

    public String getDeliveryNumber() {
        return DeliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        DeliveryNumber = deliveryNumber;
    }

    public String getKlasifikasi() {
        return klasifikasi;
    }

    public void setKlasifikasi(String klasifikasi) {
        this.klasifikasi = klasifikasi;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getUomSisa() {
        return uomSisa;
    }

    public void setUomSisa(String uomSisa) {
        this.uomSisa = uomSisa;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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

    public List<Material> getExtraItem() {
        return extraItem;
    }

    public void setExtraItem(List<Material> extraItem) {
        this.extraItem = extraItem;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }

    public String getMaterial_sales() {
        return material_sales;
    }

    public void setMaterial_sales(String material_sales) {
        this.material_sales = material_sales;
    }

    public String getMaterial_group_name() {
        return material_group_name;
    }

    public void setMaterial_group_name(String material_group_name) {
        this.material_group_name = material_group_name;
    }

    public String getLoad_number() {
        return load_number;
    }

    public void setLoad_number(String load_number) {
        this.load_number = load_number;
    }

    public int getId_material_group() {
        return id_material_group;
    }

    public void setId_material_group(int id_material_group) {
        this.id_material_group = id_material_group;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();

    }
}

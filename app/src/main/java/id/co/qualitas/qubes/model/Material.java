package id.co.qualitas.qubes.model;

import androidx.annotation.NonNull;

import java.util.List;

public class Material implements Cloneable {
    private String idHeader;//aspp
    private String idItem;//aspp
    private String id;//aspp
    private String id_customer;//aspp
    private String nama;//aspp
    private String materialCode;
    private String materialQty;
    //aspp
    private double qty;
    private double target;
    private double qtySisa;
    private double amount;
    private double amountPaid;
    private double nett;
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
    private Discount discount;//from api aspp
    private Discount extraDiscount;//from api aspp

    private List<Discount> extra;
    private List<Material> extraItem;
    private boolean isChecked;
    private int isSync;
    private String material_sales;
    private String load_number;
    private String no_invoice;
    private int id_material_group;
    private String material_group_name;
    private int id_product_group;
    private String name_product_group;
    private String top;
    private String priceListCode;
    private String date;
    //return
    private String condition;
    private String idReason;
    private String nameReason;
    private String descReason;
    private String photoReason;
    private String expiredDate;
    private List<Discount> diskonList;//object
    private double sisa;

    public Material() {
    }

    public Material(String idHeader, String id, String nama, double nett, double price, int id_material_group, String material_group_name, int id_product_group, String name_product_group, double sisa) {
        this.idHeader = idHeader;
        this.id = id;
        this.nama = nama;
        this.nett = nett;
        this.price = price;
        this.id_material_group = id_material_group;
        this.material_group_name = material_group_name;
        this.id_product_group = id_product_group;
        this.name_product_group = name_product_group;
        this.sisa = sisa;
    }

    public Material(Material material) {
        this.idHeader = material.getIdHeader();
        this.id = material.getId();
        this.nama = material.getNama();
        this.nett = material.getNett();
        this.price = material.getPrice();
        this.id_material_group = material.getId_material_group();
        this.material_group_name = material.getMaterial_group_name();
        this.id_product_group = material.getId_product_group();
        this.name_product_group = material.getName_product_group();
        this.sisa = material.getPrice();
    }

    public Material(String id, String nama, String materialQty, String uom) {
        this.id = id;
        this.nama = nama;
        this.materialQty = materialQty;
        this.uom = uom;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public List<Discount> getDiskonList() {
        return diskonList;
    }

    public void setDiskonList(List<Discount> diskonList) {
        this.diskonList = diskonList;
    }

    public Discount getExtraDiscount() {
        return extraDiscount;
    }

    public void setExtraDiscount(Discount extraDiscount) {
        this.extraDiscount = extraDiscount;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getPriceListCode() {
        return priceListCode;
    }

    public void setPriceListCode(String priceListCode) {
        this.priceListCode = priceListCode;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public double getNett() {
        return nett;
    }

    public void setNett(double nett) {
        this.nett = nett;
    }

    public double getSisa() {
        return sisa;
    }

    public void setSisa(double sisa) {
        this.sisa = sisa;
    }

    public String getDate() {
        return date;
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

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public int getId_product_group() {
        return id_product_group;
    }

    public void setId_product_group(int id_product_group) {
        this.id_product_group = id_product_group;
    }

    public String getName_product_group() {
        return name_product_group;
    }

    public void setName_product_group(String name_product_group) {
        this.name_product_group = name_product_group;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
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

//    @NonNull
//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }

    public Material clone() {
        Material newObj = new Material(this.getIdHeader(), this.getId(), this.getNama(), this.getNett(), this.getPrice(), this.getId_material_group(), this.getMaterial_group_name(), this.getId_product_group(), this.getName_product_group(), this.getSisa());
        return newObj;
    }
}

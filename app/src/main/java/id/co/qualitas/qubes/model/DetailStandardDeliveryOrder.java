package id.co.qualitas.qubes.model;

/**
 * Created by Natalia on 2/22/2016.
 */
public class DetailStandardDeliveryOrder {
    private String delivery_no;
    private String material_no;
    private String material_desc;
    private int qty;
    private String uom;
    private String batch;
    private int qtyReturn;
    private String reason;
    private boolean isChecked;
    private int qtyDelivery;
    private String status;
    private int amountToPay;
    private int tempMoney;
    private String description;
    private byte[] photos;
    private String customerName;

    public DetailStandardDeliveryOrder() {
    }

    public DetailStandardDeliveryOrder(String delivery_no, String material_no, String material_desc, int qty, String uom, String batch, int qtyReturn, String reason) {
        this.delivery_no = delivery_no;
        this.material_no = material_no;
        this.material_desc = material_desc;
        this.qty = qty;
        this.uom = uom;
        this.batch = batch;
        this.qtyReturn = qtyReturn;
        this.reason = reason;
    }

    public DetailStandardDeliveryOrder(String delivery_no, String material_no, String material_desc, int qty, String uom, String batch, int qtyReturn, String reason,String status,String description,int amountToPay,int tempMoney,int qtyDelivery) {
        this.delivery_no = delivery_no;
        this.material_no = material_no;
        this.material_desc = material_desc;
        this.qty = qty;
        this.uom = uom;
        this.batch = batch;
        this.qtyReturn = qtyReturn;
        this.reason = reason;
        this.qtyDelivery = qtyDelivery;
        this.status = status;
        this.amountToPay = amountToPay;
        this.tempMoney = tempMoney;
        this.description = description;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getDelivery_no() {
        return delivery_no;
    }

    public void setDelivery_no(String delivery_no) {
        this.delivery_no = delivery_no;
    }

    public String getMaterial_no() {
        return material_no;
    }

    public void setMaterial_no(String material_no) {
        this.material_no = material_no;
    }

    public String getMaterial_desc() {
        return material_desc;
    }

    public void setMaterial_desc(String material_desc) {
        this.material_desc = material_desc;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public int getQtyReturn() {
        return qtyReturn;
    }

    public void setQtyReturn(int qtyReturn) {
        this.qtyReturn = qtyReturn;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getQtyDelivery() {
        return qtyDelivery;
    }

    public void setQtyDelivery(int qtyDelivery) {
        this.qtyDelivery = qtyDelivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(int amountToPay) {
        this.amountToPay = amountToPay;
    }

    public int getTempMoney() {
        return tempMoney;
    }

    public void setTempMoney(int tempMoney) {
        this.tempMoney = tempMoney;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhotos() {
        return photos;
    }

    public void setPhotos(byte[] photos) {
        this.photos = photos;
    }
}

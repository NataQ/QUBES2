package id.co.qualitas.qubes.model;

/**
 * Created by Natalia on 2/25/2016.
 */
public class Material {
    private String idMaterial;
    private String materialCode;
    private int qty;
    private String desc;
    private String batch;
    private byte[] attachment;
    private String DeliveryNumber;
    private String klasifikasi;

    public Material() {
    }

    public Material(String materialCode, int qty, String desc, String batch, byte[] attachment,String klasifikasi) {
        this.materialCode = materialCode;
        this.qty = qty;
        this.desc = desc;
        this.batch = batch;
        this.attachment = attachment;
        this.klasifikasi = klasifikasi;
    }

    public Material(String materialCode, String desc, String klasifikasi) {
        this.materialCode = materialCode;
        this.desc = desc;
        this.klasifikasi = klasifikasi;
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

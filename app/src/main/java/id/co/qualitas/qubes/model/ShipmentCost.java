package id.co.qualitas.qubes.model;

/**
 * Created by Natalia on 2/22/2016.
 */
public class ShipmentCost {
    private String idShipmentCost;
    private String category;
    private String idCategory;
    private String amount;
    private String description;
    private String voucher;
    public String litre;
    private byte[] attachment;

    public ShipmentCost() {
    }

    public String getIdShipmentCost() {
        return idShipmentCost;
    }

    public void setIdShipmentCost(String idShipmentCost) {
        this.idShipmentCost = idShipmentCost;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getLitre() {
        return litre;
    }

    public void setLitre(String litre) {
        this.litre = litre;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }
}

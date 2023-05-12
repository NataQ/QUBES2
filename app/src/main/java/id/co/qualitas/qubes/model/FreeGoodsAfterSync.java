package id.co.qualitas.qubes.model;

/**
 * Created by Wiliam on 1/10/2018.
 */

public class FreeGoodsAfterSync {
    private String idTo;
    private String id_material;
    private String materialName;
    private String amount;
    private String amountUom;
    private String top;
    private String klasifikasi;
    private String jenisJual;

    public String getIdTo() {
        return idTo;
    }

    public void setIdTo(String idTo) {
        this.idTo = idTo;
    }

    public String getId_material() {
        return id_material;
    }

    public void setId_material(String id_material) {
        this.id_material = id_material;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountUom() {
        return amountUom;
    }

    public void setAmountUom(String amountUom) {
        this.amountUom = amountUom;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getKlasifikasi() {
        return klasifikasi;
    }

    public void setKlasifikasi(String klasifikasi) {
        this.klasifikasi = klasifikasi;
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

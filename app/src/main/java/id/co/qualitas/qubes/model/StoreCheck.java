package id.co.qualitas.qubes.model;

/**
 * Created by Wiliam on 8/9/2017.
 */

public class StoreCheck {
    private String idStoreCheck;
    private String date;
    private String customerId;
    private int id;
    private String nama;
    private double qty;
    private String uom;
    private int isSync;
    private String material_group_name;
    private int id_material_group;
    private int id_product_group;
    private String name_product_group;

    public String getIdStoreCheck() {
        return idStoreCheck;
    }

    public void setIdStoreCheck(String idStoreCheck) {
        this.idStoreCheck = idStoreCheck;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public String getMaterial_group_name() {
        return material_group_name;
    }

    public void setMaterial_group_name(String material_group_name) {
        this.material_group_name = material_group_name;
    }

    public int getId_material_group() {
        return id_material_group;
    }

    public void setId_material_group(int id_material_group) {
        this.id_material_group = id_material_group;
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
}

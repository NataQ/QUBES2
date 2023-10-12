package id.co.qualitas.qubes.model;

import java.math.BigDecimal;

/**
 * Created by Wiliam on 8/9/2017.
 */

public class StoreCheck {
    private String idStoreCheck;
    private String date;
    private String customerId;
    private String materialId;
    private String materialName;
    private double qty;
    private String uom;
    private int isSync;

    private String groupname;
    private int materialgroupid;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getMaterialgroupid() {
        return materialgroupid;
    }

    public void setMaterialgroupid(int materialgroupid) {
        this.materialgroupid = materialgroupid;
    }

    public int isSync() {
        return isSync;
    }

    public void setSync(int sync) {
        isSync = sync;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getIdStoreCheck() {
        return idStoreCheck;
    }

    public void setIdStoreCheck(String idStoreCheck) {
        this.idStoreCheck = idStoreCheck;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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
}

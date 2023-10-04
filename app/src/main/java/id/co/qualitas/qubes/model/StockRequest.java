package id.co.qualitas.qubes.model;

import java.util.List;

public class StockRequest {
    private String idHeader;
    private String id;
    private String requestDate;
    private String noDoc;
    private String tanggalKirim;
    private String suratJalan;
    private String status;
    private boolean isUnloading;
    private boolean isSync;
    private boolean enabled;
    private boolean isVerification;
    private String signature;
    private List<Material> materialList;

    public StockRequest() {
    }

    public StockRequest(String requestDate, String noDoc, String tanggalKirim, String suratJalan, String status, boolean isUnloading, boolean isSync, boolean enabled, boolean isVerification, String signature, List<Material> materialList) {
        this.requestDate = requestDate;
        this.noDoc = noDoc;
        this.tanggalKirim = tanggalKirim;
        this.suratJalan = suratJalan;
        this.status = status;
        this.isUnloading = isUnloading;
        this.isSync = isSync;
        this.enabled = enabled;
        this.isVerification = isVerification;
        this.signature = signature;
        this.materialList = materialList;
    }

    public StockRequest(String requestDate, String noDoc, String tanggalKirim, String status) {
        this.requestDate = requestDate;
        this.noDoc = noDoc;
        this.tanggalKirim = tanggalKirim;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }

    public String getSuratJalan() {
        return suratJalan;
    }

    public void setSuratJalan(String suratJalan) {
        this.suratJalan = suratJalan;
    }

    public boolean isUnloading() {
        return isUnloading;
    }

    public void setUnloading(boolean unloading) {
        isUnloading = unloading;
    }

    public boolean isVerification() {
        return isVerification;
    }

    public void setVerification(boolean verification) {
        isVerification = verification;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getNoDoc() {
        return noDoc;
    }

    public void setNoDoc(String noDoc) {
        this.noDoc = noDoc;
    }

    public String getTanggalKirim() {
        return tanggalKirim;
    }

    public void setTanggalKirim(String tanggalKirim) {
        this.tanggalKirim = tanggalKirim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

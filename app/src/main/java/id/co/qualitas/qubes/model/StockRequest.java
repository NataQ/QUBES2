package id.co.qualitas.qubes.model;

import java.util.List;

public class StockRequest {
    private String idHeader;
    private String id;
    private String reqdate;
    private String nodoc;
    private String tanggalkirim;
    private String nosuratjalan;
    private String status;
    private boolean isunloading;
    private boolean isSync;
    private boolean enabled;
    private boolean isverif;
    private String signature;
    private List<Material> materialList;

    public StockRequest() {
    }

    public StockRequest(String reqdate, String nodoc, String tanggalkirim, String nosuratjalan, String status, boolean isunloading, boolean isSync, boolean enabled, boolean isverif, String signature, List<Material> materialList) {
        this.reqdate = reqdate;
        this.nodoc = nodoc;
        this.tanggalkirim = tanggalkirim;
        this.nosuratjalan = nosuratjalan;
        this.status = status;
        this.isunloading = isunloading;
        this.isSync = isSync;
        this.enabled = enabled;
        this.isverif = isverif;
        this.signature = signature;
        this.materialList = materialList;
    }

    public StockRequest(String reqdate, String nodoc, String tanggalkirim, String status) {
        this.reqdate = reqdate;
        this.nodoc = nodoc;
        this.tanggalkirim = tanggalkirim;
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

    public String getNosuratjalan() {
        return nosuratjalan;
    }

    public void setNosuratjalan(String nosuratjalan) {
        this.nosuratjalan = nosuratjalan;
    }

    public boolean isIsunloading() {
        return isunloading;
    }

    public void setIsunloading(boolean isunloading) {
        this.isunloading = isunloading;
    }

    public boolean isIsverif() {
        return isverif;
    }

    public void setIsverif(boolean isverif) {
        this.isverif = isverif;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getNodoc() {
        return nodoc;
    }

    public void setNodoc(String nodoc) {
        this.nodoc = nodoc;
    }

    public String getTanggalkirim() {
        return tanggalkirim;
    }

    public void setTanggalkirim(String tanggalkirim) {
        this.tanggalkirim = tanggalkirim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

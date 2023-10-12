package id.co.qualitas.qubes.model;

import java.util.List;

public class StockRequest {
    private String idHeader;
    private int id;
    private String id_salesman;
    private String req_date;
    private String no_doc;
    private String tanggal_kirim;
    private String no_surat_jalan;
    private String status;
    private int is_unloading;
    private int is_sync;
    private int enabled;
    private int is_verif;
    private String signature;
    private List<Material> materialList;

    public StockRequest() {
    }

    public String getId_salesman() {
        return id_salesman;
    }

    public void setId_salesman(String id_salesman) {
        this.id_salesman = id_salesman;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public int isSync() {
        return is_sync;
    }

    public void setSync(int sync) {
        is_sync = sync;
    }

    public int isEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }

    public String getNo_surat_jalan() {
        return no_surat_jalan;
    }

    public void setNo_surat_jalan(String no_surat_jalan) {
        this.no_surat_jalan = no_surat_jalan;
    }

    public int isIsunloading() {
        return is_unloading;
    }

    public void setIs_unloading(int is_unloading) {
        this.is_unloading = is_unloading;
    }

    public int isIsverif() {
        return is_verif;
    }

    public void setIs_verif(int is_verif) {
        this.is_verif = is_verif;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }

    public String getNo_doc() {
        return no_doc;
    }

    public void setNo_doc(String no_doc) {
        this.no_doc = no_doc;
    }

    public String getTanggal_kirim() {
        return tanggal_kirim;
    }

    public void setTanggal_kirim(String tanggal_kirim) {
        this.tanggal_kirim = tanggal_kirim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

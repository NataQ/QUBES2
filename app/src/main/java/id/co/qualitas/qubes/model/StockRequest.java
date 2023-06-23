package id.co.qualitas.qubes.model;

public class StockRequest {
    private String tanggal;
    private String noDoc;
    private String kirim;
    private String status;

    public StockRequest(String tanggal, String noDoc, String kirim, String status) {
        this.tanggal = tanggal;
        this.noDoc = noDoc;
        this.kirim = kirim;
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNoDoc() {
        return noDoc;
    }

    public void setNoDoc(String noDoc) {
        this.noDoc = noDoc;
    }

    public String getKirim() {
        return kirim;
    }

    public void setKirim(String kirim) {
        this.kirim = kirim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

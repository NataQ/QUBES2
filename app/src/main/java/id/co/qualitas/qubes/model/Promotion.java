package id.co.qualitas.qubes.model;

public class Promotion {
    private String idHeader;
    private int idParent;
    private int id;
    private String idCustomer;
    private String no_promo;
    private String nama_promo;
    private String keterangan;
    private String toleransi;
    private String jenis_promosi;
    private String valid_from;
    private String valid_to;
    private String segmen;
    private int isSync;

    public Promotion() {
    }

    public Promotion(String promotionName) {
        this.nama_promo = promotionName;
    }

    public Promotion(String promotionName, String keterangan) {
        this.nama_promo = promotionName;
        this.keterangan = keterangan;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNo_promo() {
        return no_promo;
    }

    public void setNo_promo(String no_promo) {
        this.no_promo = no_promo;
    }

    public String getNama_promo() {
        return nama_promo;
    }

    public void setNama_promo(String nama_promo) {
        this.nama_promo = nama_promo;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getToleransi() {
        return toleransi;
    }

    public void setToleransi(String toleransi) {
        this.toleransi = toleransi;
    }

    public String getJenis_promosi() {
        return jenis_promosi;
    }

    public void setJenis_promosi(String jenis_promosi) {
        this.jenis_promosi = jenis_promosi;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    public String getSegmen() {
        return segmen;
    }

    public void setSegmen(String segmen) {
        this.segmen = segmen;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }
}

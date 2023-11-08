package id.co.qualitas.qubes.model;

import java.util.List;

public class Order {
    private String status;
    private String top;
    private String order_date;
    private String tanggal_kirim;
    private String soNo;
    private int id;//id from back end
    private String id_customer;
    private String nama;
    private String idHeader;//id sqlite
    private List<Material> materialList;
    private double omzet;
    private int isSync;

    public Order() {
    }

    public String getTanggal_kirim() {
        return tanggal_kirim;
    }

    public void setTanggal_kirim(String tanggal_kirim) {
        this.tanggal_kirim = tanggal_kirim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
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

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public double getOmzet() {
        return omzet;
    }

    public void setOmzet(double omzet) {
        this.omzet = omzet;
    }

    public String getSoNo() {
        return soNo;
    }

    public void setSoNo(String soNo) {
        this.soNo = soNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }
}

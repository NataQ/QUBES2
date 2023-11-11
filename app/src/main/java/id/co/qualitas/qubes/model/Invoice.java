package id.co.qualitas.qubes.model;

import java.util.List;

public class Invoice {
    private String idHeader;
    private String no_invoice;
    private String nama;
    private String date;
    private String id_customer;
    private double amount;
    private double total_paid;
    private double nett;
    private String invoice_date;
    private String tanggal_jatuh_tempo;
    private String route;
    private String status;
    private String signature;
    private boolean isChecked;
    private int is_route;
    private int isSync;
    private int is_verif;
    private List<Material> materialList;

    public Invoice() {
    }

    public Invoice(String no_invoice, String nama, String id_customer, double amount, double total_paid, String invoice_date, boolean is_route) {
        this.no_invoice = no_invoice;
        this.nama = nama;
        this.id_customer = id_customer;
        this.amount = amount;
        this.total_paid = total_paid;
        this.invoice_date = invoice_date;
        this.is_route = is_route ? 1 : 0;
    }

    public Invoice(String no_invoice, String nama, String id_customer, double amount, double total_paid, String invoice_date, boolean is_route, List<Material> materialList) {
        this.no_invoice = no_invoice;
        this.nama = nama;
        this.id_customer = id_customer;
        this.amount = amount;
        this.total_paid = total_paid;
        this.invoice_date = invoice_date;
        this.is_route = is_route ? 1 : 0;
        this.materialList = materialList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public String getNo_invoice() {
        return no_invoice;
    }

    public void setNo_invoice(String no_invoice) {
        this.no_invoice = no_invoice;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotal_paid() {
        return total_paid;
    }

    public void setTotal_paid(double total_paid) {
        this.total_paid = total_paid;
    }

    public double getNett() {
        return nett;
    }

    public void setNett(double nett) {
        this.nett = nett;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getTanggal_jatuh_tempo() {
        return tanggal_jatuh_tempo;
    }

    public void setTanggal_jatuh_tempo(String tanggal_jatuh_tempo) {
        this.tanggal_jatuh_tempo = tanggal_jatuh_tempo;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getIs_route() {
        return is_route;
    }

    public void setIs_route(int is_route) {
        this.is_route = is_route;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public int getIs_verif() {
        return is_verif;
    }

    public void setIs_verif(int is_verif) {
        this.is_verif = is_verif;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
    }
}

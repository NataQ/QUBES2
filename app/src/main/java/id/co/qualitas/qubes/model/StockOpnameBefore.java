package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/28/2016.
 */
public class StockOpnameBefore {
    private String kodeProduk;
    private String namaProduk;
    private String qty1;
    private String q1Type;
    private String qty2;
    private String q2Type;
    private String competitor;
    private String price;
    private String editCompetitor;

    public StockOpnameBefore(String kodeProduk, String namaProduk) {
        this.kodeProduk = kodeProduk;
        this.namaProduk = namaProduk;
    }

    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getQty1() {
        return qty1;
    }

    public void setQty1(String qty1) {
        this.qty1 = qty1;
    }

    public String getQ1Type() {
        return q1Type;
    }

    public void setQ1Type(String q1Type) {
        this.q1Type = q1Type;
    }

    public String getQty2() {
        return qty2;
    }

    public void setQty2(String qty2) {
        this.qty2 = qty2;
    }

    public String getQ2Type() {
        return q2Type;
    }

    public void setQ2Type(String q2Type) {
        this.q2Type = q2Type;
    }

    public String getCompetitor() {
        return competitor;
    }

    public void setCompetitor(String competitor) {
        this.competitor = competitor;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEditCompetitor() {
        return editCompetitor;
    }

    public void setEditCompetitor(String editCompetitor) {
        this.editCompetitor = editCompetitor;
    }
}

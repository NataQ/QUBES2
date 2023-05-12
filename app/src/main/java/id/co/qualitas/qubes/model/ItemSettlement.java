package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 8/12/2016.
 */

public class ItemSettlement {
    private String kodeProduk;
    private String namaProduk;
    private String qty1;
    private String qty2;
    private String notes;
    private String notesDetail;
    private String price;

    public ItemSettlement(String kodeProduk, String namaProduk, String qty1, String qty2, String notes, String notesDetail, String price) {
        this.kodeProduk = kodeProduk;
        this.namaProduk = namaProduk;
        this.qty1 = qty1;
        this.qty2 = qty2;
        this.notes = notes;
        this.notesDetail = notesDetail;
        this.price = price;
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

    public String getQty2() {
        return qty2;
    }

    public void setQty2(String qty2) {
        this.qty2 = qty2;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotesDetail() {
        return notesDetail;
    }

    public void setNotesDetail(String notesDetail) {
        this.notesDetail = notesDetail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

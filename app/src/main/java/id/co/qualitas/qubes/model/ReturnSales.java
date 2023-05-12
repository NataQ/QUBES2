package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/22/2016.
 */
public class ReturnSales {
    private String codeProd;
    private String nameProd;
    private String qtyR1;
    private String q1Type;
    private String qtyR2;
    private String q2Type;
    private String notes;
    private String price;
    private String reason;
    private String idMat;

    public ReturnSales(String codeProd, String nameProd, String qtyR1, String qtyR2, String notes, String price, String q1Type, String q2Type, String reason) {
        this.codeProd = codeProd;
        this.nameProd = nameProd;
        this.qtyR1 = qtyR1;
        this.qtyR2 = qtyR2;
        this.q1Type = q1Type;
        this.q2Type = q2Type;
        this.notes = notes;
        this.price = price;
        this.reason = reason;
    }

    public ReturnSales(String idMat){
        this.idMat = idMat;
    }

    public String getIdMat() {
        return idMat;
    }

    public void setIdMat(String idMat) {
        this.idMat = idMat;
    }

    public String getCodeProd() {
        return codeProd;
    }

    public void setCodeProd(String codeProd) {
        this.codeProd = codeProd;
    }

    public String getNameProd() {
        return nameProd;
    }

    public void setNameProd(String nameProd) {
        this.nameProd = nameProd;
    }

    public String getQtyR1() {
        return qtyR1;
    }

    public void setQtyR1(String qtyR1) {
        this.qtyR1 = qtyR1;
    }

    public String getQtyR2() {
        return qtyR2;
    }

    public void setQtyR2(String qtyR2) {
        this.qtyR2 = qtyR2;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQ1Type() {
        return q1Type;
    }

    public void setQ1Type(String q1Type) {
        this.q1Type = q1Type;
    }

    public String getQ2Type() {
        return q2Type;
    }

    public void setQ2Type(String q2Type) {
        this.q2Type = q2Type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

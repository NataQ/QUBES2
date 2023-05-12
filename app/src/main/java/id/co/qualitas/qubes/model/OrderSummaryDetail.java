package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/29/2016.
 */
public class OrderSummaryDetail {
    private String noOrderSummary;
    private String kodeOrderSummary;
    private String qty1;
    private String qty2;
    private String q1Type;
    private String q2Type;
    private String discOrderSummary;
    private String priceOrderSummary;
    private int priceNettOrderSummary;
    private String materialName;

    public OrderSummaryDetail(String kodeOrderSummary, String qty1, String q1Type, String qty2, String q2Type, String discOrderSummary, String priceOrderSummary, int priceNettOrderSummary, String materialName) {
//        this.noOrderSummary = noOrderSummary;
        this.kodeOrderSummary = kodeOrderSummary;
        this.qty1 = qty1;
        this.qty2 = qty2;
        this.q1Type = q1Type;
        this.q2Type = q2Type;
        this.discOrderSummary = discOrderSummary;
        this.priceOrderSummary = priceOrderSummary;
        this.priceNettOrderSummary=priceNettOrderSummary;
        this.materialName = materialName;
    }

    public String getNoOrderSummary() {
        return noOrderSummary;
    }

    public void setNoOrderSummary(String noOrderSummary) {
        this.noOrderSummary = noOrderSummary;
    }

    public String getKodeOrderSummary() {
        return kodeOrderSummary;
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

    public void setKodeOrderSummary(String kodeOrderSummary) {
        this.kodeOrderSummary = kodeOrderSummary;
    }


    public String getDiscOrderSummary() {
        return discOrderSummary;
    }

    public void setDiscOrderSummary(String discOrderSummary) {
        this.discOrderSummary = discOrderSummary;
    }

    public String getPriceOrderSummary() {
        return priceOrderSummary;
    }

    public void setPriceOrderSummary(String priceOrderSummary) {
        this.priceOrderSummary = priceOrderSummary;
    }

    public int getPriceNettOrderSummary() {
        return priceNettOrderSummary;
    }

    public void setPriceNettOrderSummary(int priceNettOrderSummary) {
        this.priceNettOrderSummary = priceNettOrderSummary;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}

package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/29/2016.
 */
public class SalesOrderHistoryDetail {
    private String noSalesOrder;
    private String noSp;
    private String priceOrder;
    private String status;
    private String nameOrder;
    private String kodeOrder;
    private String q1;
    private String q2;
    private String priceNett;

    public SalesOrderHistoryDetail(String nameOrder, String kodeOrder, String q1, String q2, String priceNett) {
        this.nameOrder = nameOrder;
        this.kodeOrder = kodeOrder;
        this.q1 = q1;
        this.q2=q2;
        this.priceNett=priceNett;
    }

    public String getNoSalesOrder() {
        return noSalesOrder;
    }

    public void setNoSalesOrder(String noSalesOrder) {
        this.noSalesOrder = noSalesOrder;
    }

    public String getNoSp() {
        return noSp;
    }

    public void setNoSp(String noSp) {
        this.noSp = noSp;
    }

    public String getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(String priceOrder) {
        this.priceOrder = priceOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(String nameOrder) {
        this.nameOrder = nameOrder;
    }

    public String getKodeOrder() {
        return kodeOrder;
    }

    public void setKodeOrder(String kodeOrder) {
        this.kodeOrder = kodeOrder;
    }

    public String getQ1() {
        return q1;
    }

    public void setQ1(String q1) {
        this.q1 = q1;
    }

    public String getQ2() {
        return q2;
    }

    public void setQ2(String q2) {
        this.q2 = q2;
    }

    public String getPriceNett() {
        return priceNett;
    }

    public void setPriceNett(String priceNett) {
        this.priceNett = priceNett;
    }
}

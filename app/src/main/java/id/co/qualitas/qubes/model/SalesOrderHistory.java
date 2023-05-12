package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Foo on 4/29/2016.
 */
public class SalesOrderHistory {
    @SerializedName("idTo")
    private String toSalesOrder;

    @SerializedName("idSo")
    private String noSalesOrder;
    private String vol;

    @SerializedName("outletName")
    private String outlet;

    @SerializedName("status")
    private String status;

    public SalesOrderHistory(String toSalesOrder, String noSalesOrder, String vol, String outlet, String status) {
        this.toSalesOrder=toSalesOrder;
        this.noSalesOrder = noSalesOrder;
        this.vol = vol;
        this.outlet = outlet;
        this.status = status;
    }

    public String getNoSalesOrder() {
        return noSalesOrder;
    }

    public void setNoSalesOrder(String noSalesOrder) {
        this.noSalesOrder = noSalesOrder;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToSalesOrder() {
        return toSalesOrder;
    }

    public void setToSalesOrder(String toSalesOrder) {
        this.toSalesOrder = toSalesOrder;
    }
}

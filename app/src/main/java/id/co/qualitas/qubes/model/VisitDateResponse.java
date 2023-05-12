package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Foo on 1/23/2017.
 */

public class VisitDateResponse {
    @SerializedName("outletName")
    private String outletName;

    @SerializedName("visitDate")
    private String visitDate;

    private String idOutlet;

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }
}

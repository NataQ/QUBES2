package id.co.qualitas.qubes.model;

import java.io.Serializable;

/**
 * Created by Foo on 3/17/2017.
 */

public class CreateNewVisitRequest implements Serializable {
    private String idSalesman;
    private String idOutlet;
    private String outletName;
    private String visitDateString;
    private int enabled;

    public String getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(String idSalesman) {
        this.idSalesman = idSalesman;
    }

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

    public String getVisitDateString() {
        return visitDateString;
    }

    public void setVisitDateString(String visitDateString) {
        this.visitDateString = visitDateString;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
}

package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Foo on 1/24/2017.
 */

public class CreditInfoResponse implements Serializable{
    @SerializedName("creditLimit")
    private String creditLimit;

    @SerializedName("creditExposure")
    private String creditExposure;

    @SerializedName("overdue")
    private String overdue;

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getCreditExposure() {
        return creditExposure;
    }

    public void setCreditExposure(String creditExposure) {
        this.creditExposure = creditExposure;
    }

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }
}

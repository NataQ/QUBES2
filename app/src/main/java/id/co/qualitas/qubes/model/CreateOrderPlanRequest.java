package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Foo on 1/26/2017.
 */

public class CreateOrderPlanRequest implements Serializable{
    @SerializedName("idSalesman")
    private String idSalesman;

    @SerializedName("idOutlet")
    private String idOutlet;

    @SerializedName("dateString")
    private String dateString;

    @SerializedName("orderPlanDetail")
    private ArrayList<MaterialResponse> orderPlanDetail;

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

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public ArrayList<MaterialResponse> getOrderPlanDetail() {
        return orderPlanDetail;
    }

    public void setOrderPlanDetail(ArrayList<MaterialResponse> orderPlanDetail) {
        this.orderPlanDetail = orderPlanDetail;
    }
}

package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Foo on 2/23/2017.
 */

public class OrderPlanRequest implements Serializable{
    private String id;
    private String idEmployee;
    private String idOutlet;
    private String dateString;
    private String date;
    private ArrayList<OrderPlanDetailRequest> orderPlanDetail;

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<OrderPlanDetailRequest> getOrderPlanDetail() {
        return orderPlanDetail;
    }

    public void setOrderPlanDetail(ArrayList<OrderPlanDetailRequest> orderPlanDetail) {
        this.orderPlanDetail = orderPlanDetail;
    }
}

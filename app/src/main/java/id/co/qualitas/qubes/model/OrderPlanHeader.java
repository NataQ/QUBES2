package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderPlanHeader implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("date")
    private String date;

    @SerializedName("idOutlet")
    private String idOutlet;

    @SerializedName("outletName")
    private String outletName;

    @SerializedName("customerName")
    private String customerName;

    private String outletDate;

    private BigDecimal target;

    private BigDecimal price;
    private String idMaterial;
    @SerializedName("qty1")
    private String qty1;


    @SerializedName("qty2")
    private String qty2;


    @SerializedName("uom1")
    private String uom1;


    @SerializedName("uom2")
    private String uom2;

    private String idSalesman;

    private String status;

    private String deleted;

    private String totalPrice;

    private String targetMonth;

    private String targetCall;

    private String achiev;

    private String plan;

    private String targetSisa;

    private List<OrderPlanHeader> list;

    private ArrayList<OrderPlanHeader> content;

    private String[] listOutlet;

    private String id_mobile;

    private String statusOrder;

    private String date_mobile;

    public OrderPlanHeader() {
    }

    public OrderPlanHeader(String id, String date, String idOutlet, String idMaterial, String qty1, String qty2, String uom1, String uom2, String status, String targetMonth, String targetCall, String achiev, String plan) {
        this.id = id;
        this.date = date;
        this.idOutlet = idOutlet;
        this.idMaterial = idMaterial;
        this.qty1 = qty1;
        this.qty2 = qty2;
        this.uom1 = uom1;
        this.uom2 = uom2;
        this.status = status;
        this.targetMonth = targetMonth;
        this.targetCall = targetCall;
        this.achiev = achiev;
        this.plan = plan;
    }

    public String getDate_mobile() {
        return date_mobile;
    }

    public void setDate_mobile(String date_mobile) {
        this.date_mobile = date_mobile;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getTargetSisa() {
        return targetSisa;
    }

    public void setTargetSisa(String targetSisa) {
        this.targetSisa = targetSisa;
    }

    public String getId_mobile() {
        return id_mobile;
    }

    public void setId_mobile(String id_mobile) {
        this.id_mobile = id_mobile;
    }

    public ArrayList<OrderPlanHeader> getContent() {
        return content;
    }

    public void setContent(ArrayList<OrderPlanHeader> content) {
        this.content = content;
    }

    public String[] getListOutlet() {
        return listOutlet;
    }

    public void setListOutlet(String[] listOutlet) {
        this.listOutlet = listOutlet;
    }

    public List<OrderPlanHeader> getList() {
        return list;
    }

    public void setList(List<OrderPlanHeader> list) {
        this.list = list;
    }

    public String getTargetMonth() {
        return targetMonth;
    }

    public void setTargetMonth(String targetMonth) {
        this.targetMonth = targetMonth;
    }

    public String getTargetCall() {
        return targetCall;
    }

    public void setTargetCall(String targetCall) {
        this.targetCall = targetCall;
    }

    public String getAchiev() {
        return achiev;
    }

    public void setAchiev(String achiev) {
        this.achiev = achiev;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(String idSalesman) {
        this.idSalesman = idSalesman;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
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

    public String getUom1() {
        return uom1;
    }

    public void setUom1(String uom1) {
        this.uom1 = uom1;
    }

    public String getUom2() {
        return uom2;
    }

    public void setUom2(String uom2) {
        this.uom2 = uom2;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOutletDate() {
        return outletDate;
    }

    public void setOutletDate(String outletDate) {
        this.outletDate = outletDate;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

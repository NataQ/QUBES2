package id.co.qualitas.qubes.model;

import java.util.List;

/**
 * Created by Natalia on 6/29/2015.
 */
public class StandardDeliveryOrder{
    private String delivery_no;
    private String customer_no;
    private String customer_name;
    private String contact_name;
    private String address;
    private String postal_code;
    private Double longitude;
    private Double latitude;
    private List<DetailStandardDeliveryOrder> detailStandardDeliveryOrderList;
    private String check_in_date;
    private String check_in_time;
    private String check_out_date;
    private String check_out_time;
    private String status;
    private String priority;
    private String reason;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public StandardDeliveryOrder() {
    }

    public StandardDeliveryOrder(String delivery_no, String customer_no, String customer_name, String contact_name,
                                 String address, String postal_code, Double latitude, Double longitude,
                                 String check_in_date, String check_in_time, String check_out_date,
                                 String check_out_time, String status, String reason,String priority) {
        this.delivery_no = delivery_no;
        this.customer_no = customer_no;
        this.customer_name = customer_name;
        this.contact_name = contact_name;
        this.address = address;
        this.postal_code = postal_code;
        this.longitude = longitude;
        this.latitude = latitude;
        this.check_in_date = check_in_date;
        this.check_in_time = check_in_time;
        this.check_out_date = check_out_date;
        this.check_out_time = check_out_time;
        this.status = status;
        this.reason=reason;
        this.priority=priority;
    }

    public StandardDeliveryOrder(String customer_name, String customer_no, String contact_name) {
        this.customer_name = customer_name;
        this.customer_no = customer_no;
        this.contact_name = contact_name;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(String check_in_date) {
        this.check_in_date = check_in_date;
    }

    public String getCheck_in_time() {
        return check_in_time;
    }

    public void setCheck_in_time(String check_in_time) {
        this.check_in_time = check_in_time;
    }

    public String getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(String check_out_date) {
        this.check_out_date = check_out_date;
    }

    public String getCheck_out_time() {
        return check_out_time;
    }

    public void setCheck_out_time(String check_out_time) {
        this.check_out_time = check_out_time;
    }

    public List<DetailStandardDeliveryOrder> getDetailStandardDeliveryOrderList() {
        return detailStandardDeliveryOrderList;
    }

    public void setDetailStandardDeliveryOrderList(List<DetailStandardDeliveryOrder> detailStandardDeliveryOrderList) {
        this.detailStandardDeliveryOrderList = detailStandardDeliveryOrderList;
    }

    public String getDelivery_no() {
        return delivery_no;
    }

    public void setDelivery_no(String delivery_no) {
        this.delivery_no = delivery_no;
    }

    public String getCustomer_no() {
        return customer_no;
    }

    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}

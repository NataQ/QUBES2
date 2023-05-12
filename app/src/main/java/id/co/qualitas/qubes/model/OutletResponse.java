package id.co.qualitas.qubes.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Foo on 1/20/2017.
 */

public class OutletResponse implements Serializable, Comparable{
    @SerializedName("id")
    private String id;

    private String idVisitSalesman;

    @SerializedName("idOutlet")
    private String idOutlet;

    @SerializedName("outletName")
    private String outletName;

    @SerializedName("listVisitDate")
    private ArrayList<VisitDateResponse> visitDateList;

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("statusOrder")
    private String statusOrder;

    private String status;

    @SerializedName("statusCheck")
    private String statusCheckIn;

    @SerializedName("enabled")
    private boolean enabled;

    private boolean color;

    private String reason;

    @SerializedName("latCheckIn")
    private Double lat_check_in;

    @SerializedName("longCheckIn")
    private Double long_check_in;

    @SerializedName("latCheckOut")
    private Double lat_check_out;

    @SerializedName("longCheckOut")
    private Double long_check_out;

    private String check_in_time;
    private String pause_time;
    private String continue_time;
    private String check_out_time;
    private String area;

    private String postalCode;

    private String timer;

    private String visitDate;

    @SerializedName("plant")
    private String plant;

    @SerializedName("name")
    private String salesOfficeName;

    private BigDecimal credit_limit;
    private BigDecimal credit_exposure;
    private BigDecimal overdue;

    private String checkInTime;

    private String checkInString;
    private String pauseString;
    private String continueString;
    private String checkOutString;


    private Long checkInLong;
    private String pauseTime;
    private String pauseReason;

    private String statusSync;

    public boolean deleted;

    private String segment;

    private String id_mobile;

    private String index;

    private String street1;

    private String date_mobile;

    public boolean clicked;
    public Integer posClicked;

    public OutletResponse() {
    }

    public OutletResponse(String idOutlet, String outletName, String statusCheckIn) {
        this.idOutlet = idOutlet;
        this.outletName = outletName;
        this.statusCheckIn = statusCheckIn;
    }

    public Integer getPosClicked() {
        return posClicked;
    }

    public void setPosClicked(Integer posClicked) {
        this.posClicked = posClicked;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public String getDate_mobile() {
        return date_mobile;
    }

    public void setDate_mobile(String date_mobile) {
        this.date_mobile = date_mobile;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getId_mobile() {
        return id_mobile;
    }

    public void setId_mobile(String id_mobile) {
        this.id_mobile = id_mobile;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getIdVisitSalesman() {
        return idVisitSalesman;
    }

    public void setIdVisitSalesman(String idVisitSalesman) {
        this.idVisitSalesman = idVisitSalesman;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getStatusSync() {
        return statusSync;
    }

    public void setStatusSync(String statusSync) {
        this.statusSync = statusSync;
    }

    public Long getCheckInLong() {
        return checkInLong;
    }

    public void setCheckInLong(Long checkInLong) {
        this.checkInLong = checkInLong;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(String pauseTime) {
        this.pauseTime = pauseTime;
    }

    public String getPauseReason() {
        return pauseReason;
    }

    public void setPauseReason(String pauseReason) {
        this.pauseReason = pauseReason;
    }

    public BigDecimal getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(BigDecimal credit_limit) {
        this.credit_limit = credit_limit;
    }

    public BigDecimal getCredit_exposure() {
        return credit_exposure;
    }

    public void setCredit_exposure(BigDecimal credit_exposure) {
        this.credit_exposure = credit_exposure;
    }

    public BigDecimal getOverdue() {
        return overdue;
    }

    public void setOverdue(BigDecimal overdue) {
        this.overdue = overdue;
    }

    public Double getLat_check_in() {
        return lat_check_in;
    }

    public void setLat_check_in(Double lat_check_in) {
        this.lat_check_in = lat_check_in;
    }

    public Double getLong_check_in() {
        return long_check_in;
    }

    public void setLong_check_in(Double long_check_in) {
        this.long_check_in = long_check_in;
    }

    public Double getLat_check_out() {
        return lat_check_out;
    }

    public void setLat_check_out(Double lat_check_out) {
        this.lat_check_out = lat_check_out;
    }

    public Double getLong_check_out() {
        return long_check_out;
    }

    public void setLong_check_out(Double long_check_out) {
        this.long_check_out = long_check_out;
    }

    public String getCheck_in_time() {
        return check_in_time;
    }

    public void setCheck_in_time(String check_in_time) {
        this.check_in_time = check_in_time;
    }

    public String getPause_time() {
        return pause_time;
    }

    public void setPause_time(String pause_time) {
        this.pause_time = pause_time;
    }

    public String getContinue_time() {
        return continue_time;
    }

    public void setContinue_time(String continue_time) {
        this.continue_time = continue_time;
    }

    public String getCheck_out_time() {
        return check_out_time;
    }

    public void setCheck_out_time(String check_out_time) {
        this.check_out_time = check_out_time;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getSalesOfficeName() {
        return salesOfficeName;
    }

    public void setSalesOfficeName(String salesOfficeName) {
        this.salesOfficeName = salesOfficeName;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatusCheckIn() {
        return statusCheckIn;
    }

    public void setStatusCheckIn(String statusCheckIn) {
        this.statusCheckIn = statusCheckIn;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public ArrayList<VisitDateResponse> getVisitDateList() {
        return visitDateList;
    }

    public void setVisitDateList(ArrayList<VisitDateResponse> visitDateList) {
        this.visitDateList = visitDateList;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckInString() {
        return checkInString;
    }

    public void setCheckInString(String checkInString) {
        this.checkInString = checkInString;
    }

    public String getPauseString() {
        return pauseString;
    }

    public void setPauseString(String pauseString) {
        this.pauseString = pauseString;
    }

    public String getContinueString() {
        return continueString;
    }

    public void setContinueString(String continueString) {
        this.continueString = continueString;
    }

    public String getCheckOutString() {
        return checkOutString;
    }

    public void setCheckOutString(String checkOutString) {
        this.checkOutString = checkOutString;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        OutletResponse compare = (OutletResponse) o;

        if (compare.isColor() == this.color){
            return 0;
        }
        return 1;
    }
}

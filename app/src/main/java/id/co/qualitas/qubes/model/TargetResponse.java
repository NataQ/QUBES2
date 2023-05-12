package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class TargetResponse implements Serializable {
    private String idOutlet;

    @SerializedName("visitDate")
    private String date;

    @SerializedName("date")
    private String salesDate;

    private String outletName;

    private BigDecimal target;

    private BigDecimal actual;

    private BigDecimal targetAwal;

    private String matClass;

    private BigDecimal targetCall;

    private String gap;

    //Summary
    private String targetEc;
    private String targetEa;
    private String actualCall;
    private String Call;
    private String gapCall;
    private String gapEc;
    private String gapSales;
    private String actualSales;
    private String gapEa;
    private String totalReturn;
    private String actualEc;
    private String actualEa;
    private String targetSales;
    private int month;

    public TargetResponse() {
    }

    public TargetResponse(String idOutlet, String salesDate, String outletName, BigDecimal targetAwal, BigDecimal target, BigDecimal actual, String matClass, int month) {
        this.idOutlet = idOutlet;
        this.salesDate = salesDate;
        this.outletName = outletName;
        this.targetAwal = targetAwal;
        this.target = target;
        this.actual = actual;
        this.matClass = matClass;
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getGapCall() {
        return gapCall;
    }

    public void setGapCall(String gapCall) {
        this.gapCall = gapCall;
    }

    public String getTargetEc() {
        return targetEc;
    }

    public void setTargetEc(String targetEc) {
        this.targetEc = targetEc;
    }

    public String getTargetEa() {
        return targetEa;
    }

    public void setTargetEa(String targetEa) {
        this.targetEa = targetEa;
    }

    public String getActualCall() {
        return actualCall;
    }

    public void setActualCall(String actualCall) {
        this.actualCall = actualCall;
    }

    public String getCall() {
        return Call;
    }

    public void setCall(String call) {
        Call = call;
    }

    public String getGapEc() {
        return gapEc;
    }

    public void setGapEc(String gapEc) {
        this.gapEc = gapEc;
    }

    public String getGapSales() {
        return gapSales;
    }

    public void setGapSales(String gapSales) {
        this.gapSales = gapSales;
    }

    public String getActualSales() {
        return actualSales;
    }

    public void setActualSales(String actualSales) {
        this.actualSales = actualSales;
    }

    public String getGapEa() {
        return gapEa;
    }

    public void setGapEa(String gapEa) {
        this.gapEa = gapEa;
    }

    public String getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(String totalReturn) {
        this.totalReturn = totalReturn;
    }

    public String getActualEc() {
        return actualEc;
    }

    public void setActualEc(String actualEc) {
        this.actualEc = actualEc;
    }

    public String getActualEa() {
        return actualEa;
    }

    public void setActualEa(String actualEa) {
        this.actualEa = actualEa;
    }

    public String getTargetSales() {
        return targetSales;
    }

    public void setTargetSales(String targetSales) {
        this.targetSales = targetSales;
    }

    public String getGap() {
        return gap;
    }

    public void setGap(String gap) {
        this.gap = gap;
    }

    public BigDecimal getTargetMonth() {
        return target;
    }

    public void setTargetMonth(BigDecimal targetMonth) {
        this.target = targetMonth;
    }

    public BigDecimal getTargetCall() {
        return targetCall;
    }

    public void setTargetCall(BigDecimal targetCall) {
        this.targetCall = targetCall;
    }

    public BigDecimal getTargetAchiev() {
        return actual;
    }

    public void setTargetAchiev(BigDecimal targetAchiev) {
        this.actual = targetAchiev;
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

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }

    public BigDecimal getActual() {
        return actual;
    }

    public void setActual(BigDecimal actual) {
        this.actual = actual;
    }

    public BigDecimal getTargetAwal() {
        return targetAwal;
    }

    public void setTargetAwal(BigDecimal targetAwal) {
        this.targetAwal = targetAwal;
    }

    public String getMatClass() {
        return matClass;
    }

    public void setMatClass(String matClass) {
        this.matClass = matClass;
    }
}

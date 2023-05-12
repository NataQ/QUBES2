package id.co.qualitas.qubes.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SummaryResponse {
    //new
    private BigInteger targetEC;
    private BigInteger realityEC;
    private BigInteger gapEc;

    private BigInteger targetC;
    private BigInteger realityC;
    private BigInteger gapC;

    private BigInteger targetEA;
    private BigInteger realityEA;
    private BigInteger gapEA;

    private BigDecimal targetSales;
    private BigDecimal realitySales;
    private BigDecimal gapSales;

    private BigInteger summaryReturn;

    public BigInteger getTargetEC() {
        return targetEC;
    }

    public void setTargetEC(BigInteger targetEC) {
        this.targetEC = targetEC;
    }

    public BigInteger getRealityEC() {
        return realityEC;
    }

    public void setRealityEC(BigInteger realityEC) {
        this.realityEC = realityEC;
    }

    public BigInteger getGapEc() {
        return gapEc;
    }

    public void setGapEc(BigInteger gapEc) {
        this.gapEc = gapEc;
    }

    public BigInteger getTargetC() {
        return targetC;
    }

    public void setTargetC(BigInteger targetC) {
        this.targetC = targetC;
    }

    public BigInteger getRealityC() {
        return realityC;
    }

    public void setRealityC(BigInteger realityC) {
        this.realityC = realityC;
    }

    public BigInteger getGapC() {
        return gapC;
    }

    public void setGapC(BigInteger gapC) {
        this.gapC = gapC;
    }

    public BigInteger getTargetEA() {
        return targetEA;
    }

    public void setTargetEA(BigInteger targetEA) {
        this.targetEA = targetEA;
    }

    public BigInteger getRealityEA() {
        return realityEA;
    }

    public void setRealityEA(BigInteger realityEA) {
        this.realityEA = realityEA;
    }

    public BigInteger getGapEA() {
        return gapEA;
    }

    public void setGapEA(BigInteger gapEA) {
        this.gapEA = gapEA;
    }

    public BigDecimal getTargetSales() {
        return targetSales;
    }

    public void setTargetSales(BigDecimal targetSales) {
        this.targetSales = targetSales;
    }

    public BigDecimal getRealitySales() {
        return realitySales;
    }

    public void setRealitySales(BigDecimal realitySales) {
        this.realitySales = realitySales;
    }

    public BigDecimal getGapSales() {
        return gapSales;
    }

    public void setGapSales(BigDecimal gapSales) {
        this.gapSales = gapSales;
    }

    public BigInteger getSummaryReturn() {
        return summaryReturn;
    }

    public void setSummaryReturn(BigInteger summaryReturn) {
        this.summaryReturn = summaryReturn;
    }

//    public Integer getSalesTarget() {
//        return salesTarget;
//    }
//
//    public void setSalesTarget(Integer salesTarget) {
//        this.salesTarget = salesTarget;
//    }
//
//    public Integer getSalesActual() {
//        return salesActual;
//    }
//
//    public void setSalesActual(Integer salesActual) {
//        this.salesActual = salesActual;
//    }
//
//    public Integer getTotalReturn() {
//        return totalReturn;
//    }
//
//    public void setTotalReturn(Integer totalReturn) {
//        this.totalReturn = totalReturn;
//    }
//
//    public TargetResponse getCallSummary() {
//        return callSummary;
//    }
//
//    public void setCallSummary(TargetResponse callSummary) {
//        this.callSummary = callSummary;
//    }
//
//    public TargetResponse getEcSummary() {
//        return ecSummary;
//    }
//
//    public void setEcSummary(TargetResponse ecSummary) {
//        this.ecSummary = ecSummary;
//    }
//
//    public TargetResponse getEaSummary() {
//        return eaSummary;
//    }
//
//    public void setEaSummary(TargetResponse eaSummary) {
//        this.eaSummary = eaSummary;
//    }
}

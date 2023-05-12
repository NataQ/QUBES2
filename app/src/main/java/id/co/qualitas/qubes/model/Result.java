package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Foo on 3/14/2017.
 */

public class Result implements Serializable {
    @SerializedName("checkInTime")
    String checkInTime;
    @SerializedName("pauseTime")
    String pauseTime;
    @SerializedName("timer")
    String timer;
    @SerializedName("checkStore")
    boolean checkStore;//true kalau ud

    @SerializedName("checkTO")
    boolean checkTO;

    @SerializedName("timeNow")
    String timeNow;

    /*get supervisor detail*/
    @SerializedName("nik")
    String supervisorNik;

    @SerializedName("fullName")
    String supervisorName;

    @SerializedName("position")
    String supervisorPosition;

    /*Last Order*/
    private BigDecimal lastQty1;
    private BigDecimal lastQty2;
    private String lastUom1;
    private String lastUom2;

    public BigDecimal getLastQty1() {
        return lastQty1;
    }

    public void setLastQty1(BigDecimal lastQty1) {
        this.lastQty1 = lastQty1;
    }

    public BigDecimal getLastQty2() {
        return lastQty2;
    }

    public void setLastQty2(BigDecimal lastQty2) {
        this.lastQty2 = lastQty2;
    }

    public String getLastUom1() {
        return lastUom1;
    }

    public void setLastUom1(String lastUom1) {
        this.lastUom1 = lastUom1;
    }

    public String getLastUom2() {
        return lastUom2;
    }

    public void setLastUom2(String lastUom2) {
        this.lastUom2 = lastUom2;
    }

    private boolean saveOrderPlan;
    private boolean updateOrderPlan;
    private boolean deleteOrderPlan;
    private boolean saveStoreCheck;
    private boolean updateStoreCheck;
    private boolean saveOrder;
    private boolean saveReturn;
    private boolean saveNewVisit;
    private boolean deleteNewVisit;
    private boolean saveVisitSalesman;
    private boolean updateVisitSalesman;
    private List<String> listNoBrbFailed;
    private List<String> listIdOrderFailed;
    private List<String> listIdVisitSalesmanFailed;
    private List<String> listIdVisitPlanFailed;
    private List<String> listIdNewVisitFailed;

    /*Sync per Menu*/
    private List<OutletResponse> listVisitPlan;
    private ArrayList<OrderPlanHeader> listOrderPlan;
    private List<MaterialResponse> listStore;
    private List<VisitOrderHeader> listOrderHeader;
    private List<VisitOrderDetailResponse> listOrderDetail;
    private List<ToPrice> listToPrice;
    private ArrayList<OptionFreeGoods> listFreegoods;
    private List<Reason> listReasonReturn;
    private List<ReturnResponse> listReturnHeader;
    private List<Return> listReturnDetail;

    public List<OutletResponse> getListVisitPlan() {
        return listVisitPlan;
    }

    public void setListVisitPlan(List<OutletResponse> listVisitPlan) {
        this.listVisitPlan = listVisitPlan;
    }

    public ArrayList<OrderPlanHeader> getListOrderPlan() {
        return listOrderPlan;
    }

    public void setListOrderPlan(ArrayList<OrderPlanHeader> listOrderPlan) {
        this.listOrderPlan = listOrderPlan;
    }

    public List<MaterialResponse> getListStore() {
        return listStore;
    }

    public void setListStore(List<MaterialResponse> listStore) {
        this.listStore = listStore;
    }

    public List<VisitOrderHeader> getListOrderHeader() {
        return listOrderHeader;
    }

    public void setListOrderHeader(List<VisitOrderHeader> listOrderHeader) {
        this.listOrderHeader = listOrderHeader;
    }

    public List<VisitOrderDetailResponse> getListOrderDetail() {
        return listOrderDetail;
    }

    public void setListOrderDetail(List<VisitOrderDetailResponse> listOrderDetail) {
        this.listOrderDetail = listOrderDetail;
    }

    public List<ToPrice> getListToPrice() {
        return listToPrice;
    }

    public void setListToPrice(List<ToPrice> listToPrice) {
        this.listToPrice = listToPrice;
    }

    public ArrayList<OptionFreeGoods> getListFreegoods() {
        return listFreegoods;
    }

    public void setListFreegoods(ArrayList<OptionFreeGoods> listFreegoods) {
        this.listFreegoods = listFreegoods;
    }

    public List<Reason> getListReasonReturn() {
        return listReasonReturn;
    }

    public void setListReasonReturn(List<Reason> listReasonReturn) {
        this.listReasonReturn = listReasonReturn;
    }

    public List<ReturnResponse> getListReturnHeader() {
        return listReturnHeader;
    }

    public void setListReturnHeader(List<ReturnResponse> listReturnHeader) {
        this.listReturnHeader = listReturnHeader;
    }

    public List<Return> getListReturnDetail() {
        return listReturnDetail;
    }

    public void setListReturnDetail(List<Return> listReturnDetail) {
        this.listReturnDetail = listReturnDetail;
    }

    public List<String> getListIdNewVisitFailed() {
        return listIdNewVisitFailed;
    }

    public void setListIdNewVisitFailed(List<String> listIdNewVisitFailed) {
        this.listIdNewVisitFailed = listIdNewVisitFailed;
    }

    public List<String> getListIdVisitPlanFailed() {
        return listIdVisitPlanFailed;
    }

    public void setListIdVisitPlanFailed(List<String> listIdVisitPlanFailed) {
        this.listIdVisitPlanFailed = listIdVisitPlanFailed;
    }

    public List<String> getListIdVisitSalesmanFailed() {
        return listIdVisitSalesmanFailed;
    }

    public void setListIdVisitSalesmanFailed(List<String> listIdVisitSalesmanFailed) {
        this.listIdVisitSalesmanFailed = listIdVisitSalesmanFailed;
    }

    public List<String> getListNoBrbFailed() {
        return listNoBrbFailed;
    }

    public void setListNoBrbFailed(List<String> listNoBrbFailed) {
        this.listNoBrbFailed = listNoBrbFailed;
    }

    public List<String> getListIdOrderFailed() {
        return listIdOrderFailed;
    }

    public void setListIdOrderFailed(List<String> listIdOrderFailed) {
        this.listIdOrderFailed = listIdOrderFailed;
    }

    public boolean isSaveOrderPlan() {
        return saveOrderPlan;
    }

    public void setSaveOrderPlan(boolean saveOrderPlan) {
        this.saveOrderPlan = saveOrderPlan;
    }

    public boolean isUpdateOrderPlan() {
        return updateOrderPlan;
    }

    public void setUpdateOrderPlan(boolean updateOrderPlan) {
        this.updateOrderPlan = updateOrderPlan;
    }

    public boolean isDeleteOrderPlan() {
        return deleteOrderPlan;
    }

    public void setDeleteOrderPlan(boolean deleteOrderPlan) {
        this.deleteOrderPlan = deleteOrderPlan;
    }

    public boolean isSaveStoreCheck() {
        return saveStoreCheck;
    }

    public void setSaveStoreCheck(boolean saveStoreCheck) {
        this.saveStoreCheck = saveStoreCheck;
    }

    public boolean isUpdateStoreCheck() {
        return updateStoreCheck;
    }

    public void setUpdateStoreCheck(boolean updateStoreCheck) {
        this.updateStoreCheck = updateStoreCheck;
    }

    public boolean isSaveOrder() {
        return saveOrder;
    }

    public void setSaveOrder(boolean saveOrder) {
        this.saveOrder = saveOrder;
    }

    public boolean isSaveReturn() {
        return saveReturn;
    }

    public void setSaveReturn(boolean saveReturn) {
        this.saveReturn = saveReturn;
    }

    public boolean isSaveNewVisit() {
        return saveNewVisit;
    }

    public void setSaveNewVisit(boolean saveNewVisit) {
        this.saveNewVisit = saveNewVisit;
    }

    public boolean isDeleteNewVisit() {
        return deleteNewVisit;
    }

    public void setDeleteNewVisit(boolean deleteNewVisit) {
        this.deleteNewVisit = deleteNewVisit;
    }

    public boolean isSaveVisitSalesman() {
        return saveVisitSalesman;
    }

    public void setSaveVisitSalesman(boolean saveVisitSalesman) {
        this.saveVisitSalesman = saveVisitSalesman;
    }

    public boolean isUpdateVisitSalesman() {
        return updateVisitSalesman;
    }

    public void setUpdateVisitSalesman(boolean updateVisitSalesman) {
        this.updateVisitSalesman = updateVisitSalesman;
    }

    public String getTimeNow() {
        return timeNow;
    }

    public void setTimeNow(String timeNow) {
        this.timeNow = timeNow;
    }

    public boolean isCheckTO() {
        return checkTO;
    }

    public void setCheckTO(boolean checkTO) {
        this.checkTO = checkTO;
    }

    public boolean isCheckStore() {
        return checkStore;
    }

    public void setCheckStore(boolean checkStore) {
        this.checkStore = checkStore;
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

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getSupervisorNik() {
        return supervisorNik;
    }

    public void setSupervisorNik(String supervisorNik) {
        this.supervisorNik = supervisorNik;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorPosition() {
        return supervisorPosition;
    }

    public void setSupervisorPosition(String supervisorPosition) {
        this.supervisorPosition = supervisorPosition;
    }
}

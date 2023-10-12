package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Wiliam on 7/26/2017.
 */

public class OfflineLoginData implements Serializable {

    private String idSalesman;
    private List<MaterialResponse> listMaterial;
    private List<Uom> listUom;
    private List<Uom> listMasterUom;
    private List<OutletResponse> listOutlet;
    private List<JenisJualandTop> listTop;
    private List<JenisJualandTop> listJenisJual;
    private List<SalesOffice> listSalesOffice;
    private List<Partner> listPartner;

    private List<OutletResponse> listVisitPlan;

    private List<OrderPlanHeader> listOrderPlan;

    private List<MaterialResponse> listStore;

    private List<VisitOrderHeader> listOrderHeader;
    private List<VisitOrderDetailResponse> listOrderDetail;
    private List<ToPrice> listToPrice;
    private ArrayList<OptionFreeGoods> listFreegoods;

    private List<Reason> listReasonReturn;
    private List<ReturnResponse> listReturnHeader;
    private List<Return> listReturnDetail;

    private List<OrderPlanHeader> listSaveOrderPlan;
    private List<OrderPlanHeader> listUpdateOrderPlan;
    private List<OrderPlanHeader> listDeleteOrderPlan;
    private List<MaterialResponse> listSaveStoreCheck;
    private List<MaterialResponse> listUpdateStoreCheck;
    private List<MaterialResponse> listDeleteStoreCheck;
    private List<VisitOrderRequest> listSaveOrder;
    private List<ReturnRequest> listSaveReturn;
    private List<OutletResponse> listSaveNewVisit;
    private List<OutletResponse> listDeleteNewVisit;
    private List<OutletResponse> listSaveVisitSalesman;
    private List<OutletResponse> listUpdateVisitSalesman;
    private String datetimeNow;
    private Date offDate;
    private long elapseTime;
    private AdditionalInfo additionalInfo;

    public List<MaterialResponse> getListDeleteStoreCheck() {
        return listDeleteStoreCheck;
    }

    public void setListDeleteStoreCheck(List<MaterialResponse> listDeleteStoreCheck) {
        this.listDeleteStoreCheck = listDeleteStoreCheck;
    }

    public ArrayList<OptionFreeGoods> getListFreeGoods() {
        return listFreegoods;
    }

    public void setListFreeGoods(ArrayList<OptionFreeGoods> listFreeGoods) {
        this.listFreegoods = listFreeGoods;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<Uom> getListMasterUom() {
        return listMasterUom;
    }

    public void setListMasterUom(List<Uom> listMasterUom) {
        this.listMasterUom = listMasterUom;
    }

    public Date getOffDate() {
        return offDate;
    }

    public void setOffDate(Date offDate) {
        this.offDate = offDate;
    }

    public Long getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(Long elapseTime) {
        this.elapseTime = elapseTime;
    }

    public String getDatetimeNow() {
        return datetimeNow;
    }

    public void setDatetimeNow(String datetimeNow) {
        this.datetimeNow = datetimeNow;
    }

    public List<ToPrice> getListToPrice() {
        return listToPrice;
    }

    public void setListToPrice(List<ToPrice> listToPrice) {
        this.listToPrice = listToPrice;
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

    public List<VisitOrderDetailResponse> getListOrderDetail() {
        return listOrderDetail;
    }

    public void setListOrderDetail(List<VisitOrderDetailResponse> listOrderDetail) {
        this.listOrderDetail = listOrderDetail;
    }

    public String getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(String idSalesman) {
        this.idSalesman = idSalesman;
    }

    public List<VisitOrderHeader> getListOrderHeader() {
        return listOrderHeader;
    }

    public void setListOrderHeader(List<VisitOrderHeader> listOrderHeader) {
        this.listOrderHeader = listOrderHeader;
    }

    public List<MaterialResponse> getListStore() {
        return listStore;
    }

    public void setListStore(List<MaterialResponse> listStore) {
        this.listStore = listStore;
    }

    public List<Partner> getListPartner() {
        return listPartner;
    }

    public void setListPartner(List<Partner> listPartner) {
        this.listPartner = listPartner;
    }

    public List<Reason> getListReasonReturn() {
        return listReasonReturn;
    }

    public void setListReasonReturn(List<Reason> listReasonReturn) {
        this.listReasonReturn = listReasonReturn;
    }

    public List<MaterialResponse> getListMaterial() {
        return listMaterial;
    }

    public void setListMaterial(List<MaterialResponse> listMaterial) {
        this.listMaterial = listMaterial;
    }

    public List<Uom> getListUOm() {
        return listUom;
    }

    public void setListUOm(List<Uom> listUOm) {
        this.listUom = listUOm;
    }

    public List<OutletResponse> getListOutlet() {
        return listOutlet;
    }

    public void setListOutlet(List<OutletResponse> listOutlet) {
        this.listOutlet = listOutlet;
    }

    public List<OutletResponse> getListVisitPlan() {
        return listVisitPlan;
    }

    public void setListVisitPlan(List<OutletResponse> listVisitPlan) {
        this.listVisitPlan = listVisitPlan;
    }

    public List<OrderPlanHeader> getListOrderPlan() {
        return listOrderPlan;
    }

    public void setListOrderPlan(List<OrderPlanHeader> listOrderPlan) {
        this.listOrderPlan = listOrderPlan;
    }

    public List<JenisJualandTop> getListTop() {
        return listTop;
    }

    public void setListTop(List<JenisJualandTop> listTop) {
        this.listTop = listTop;
    }

    public List<JenisJualandTop> getListJenisJual() {
        return listJenisJual;
    }

    public void setListJenisJual(List<JenisJualandTop> listJenisJual) {
        this.listJenisJual = listJenisJual;
    }

    public List<SalesOffice> getListSalesOffice() {
        return listSalesOffice;
    }

    public void setListSalesOffice(List<SalesOffice> listSalesOffice) {
        this.listSalesOffice = listSalesOffice;
    }

    public void setListSaveOrderPlan(List<OrderPlanHeader> listSaveOrderPlan) {
        this.listSaveOrderPlan = listSaveOrderPlan;
    }

    public void setListUpdateOrderPlan(List<OrderPlanHeader> listUpdateOrderPlan) {
        this.listUpdateOrderPlan = listUpdateOrderPlan;
    }

    public void setListDeleteOrderPlan(List<OrderPlanHeader> listDeleteOrderPlan) {
        this.listDeleteOrderPlan = listDeleteOrderPlan;
    }

    public void setListSaveStoreCheck(List<MaterialResponse> listSaveStoreCheck) {
        this.listSaveStoreCheck = listSaveStoreCheck;
    }

    public void setListUpdateStoreCheck(List<MaterialResponse> listUpdateStoreCheck) {
        this.listUpdateStoreCheck = listUpdateStoreCheck;
    }

    public void setListSaveOrder(List<VisitOrderRequest> listSaveOrder) {
        this.listSaveOrder = listSaveOrder;
    }

    public void setListSaveReturn(List<ReturnRequest> listSaveReturn) {
        this.listSaveReturn = listSaveReturn;
    }

    public void setListSaveNewVisit(List<OutletResponse> listSaveNewVisit) {
        this.listSaveNewVisit = listSaveNewVisit;
    }

    public void setListDeleteNewVisit(List<OutletResponse> listDeleteNewVisit) {
        this.listDeleteNewVisit = listDeleteNewVisit;
    }

    public void setListSaveVisitSalesman(List<OutletResponse> listSaveVisitSalesman) {
        this.listSaveVisitSalesman = listSaveVisitSalesman;
    }

    public void setListUpdateVisitSalesman(List<OutletResponse> listUpdateVisitSalesman) {
        this.listUpdateVisitSalesman = listUpdateVisitSalesman;
    }
}

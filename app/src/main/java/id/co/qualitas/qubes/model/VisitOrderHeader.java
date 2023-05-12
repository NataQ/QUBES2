package id.co.qualitas.qubes.model;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Foo on 4/22/2016.
 */
public class VisitOrderHeader {
    private String soldTo;
    private String soldToName;
    private String shipTo;
    private String shipToName;
    private String tglPoString;
    private String edPoString;
    private String noPo;
    private String salesOffice;
    private int posSalesOffice;
    private String idEmployee;
    private String idOutlet;
    private String photoString;
    private String signatureString;
    private String comment;
    private int requestDisc;
    private String descriptionDisc;
    private String statusString;
    private String orderType;
    private String id;
    private BigDecimal totalAmount;
    private String photo;
    private String signature;
    private String salesOfficeName;

    private String statusP;

    private String id_mobile;

    private Boolean deleted;

    private String tanggalPo;

    private String edPo;

    private boolean request_disc;

    private Boolean hargaJadi;

    private String date_mobile;

    public String getDate_mobile() {
        return date_mobile;
    }

    public void setDate_mobile(String date_mobile) {
        this.date_mobile = date_mobile;
    }

    public String getStatusP() {
        return statusP;
    }

    public void setStatusP(String statusP) {
        this.statusP = statusP;
    }

    public Boolean getHargaJadi() {
        return hargaJadi;
    }

    public void setHargaJadi(Boolean hargaJadi) {
        this.hargaJadi = hargaJadi;
    }

    public String getTanggalPo() {
        return tanggalPo;
    }

    public void setTanggalPo(String tanggalPo) {
        this.tanggalPo = tanggalPo;
    }

    public String getEdPo() {
        return edPo;
    }

    public void setEdPo(String edPo) {
        this.edPo = edPo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getId_mobile() {
        return id_mobile;
    }

    public void setId_mobile(String id_mobile) {
        this.id_mobile = id_mobile;
    }

    //    private List<OptionFreeGoods> listFreeGoods;
    private ArrayList<ArrayList<OptionFreeGoods>> listFreeGoods;

//    public List<OptionFreeGoods> getListFreeGoods() {
//        return listFreeGoods;
//    }
//
//    public void setListFreeGoods(List<OptionFreeGoods> listFreeGoods) {
//        this.listFreeGoods = listFreeGoods;
//    }


    public ArrayList<ArrayList<OptionFreeGoods>> getListFreeGoods() {
        return listFreeGoods;
    }

    public void setListFreeGoods(ArrayList<ArrayList<OptionFreeGoods>> listFreeGoods) {
        this.listFreeGoods = listFreeGoods;
    }

    public String getStatusPrice() {
        return statusP;
    }

    public void setStatusPrice(String status) {
        this.statusP = status;
    }

    public String getSalesOfficeName() {
        return salesOfficeName;
    }

    public void setSalesOfficeName(String salesOfficeName) {
        this.salesOfficeName = salesOfficeName;
    }


    public boolean isRequest_disc() {
        return request_disc;
    }

    public void setRequest_disc(boolean request_disc) {
        this.request_disc = request_disc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSoldToName() {
        return soldToName;
    }

    public void setSoldToName(String soldToName) {
        this.soldToName = soldToName;
    }

    public String getShipToName() {
        return shipToName;
    }

    public void setShipToName(String shipToName) {
        this.shipToName = shipToName;
    }

    public int getPosSalesOffice() {
        return posSalesOffice;
    }

    public void setPosSalesOffice(int posSalesOffice) {
        this.posSalesOffice = posSalesOffice;
    }

    public String getSoldTo() {
        return soldTo;
    }

    public void setSoldTo(String soldTo) {
        this.soldTo = soldTo;
    }

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }

    public String getNoPo() {
        return noPo;
    }

    public void setNoPo(String noPo) {
        this.noPo = noPo;
    }

    public String getSalesOffice() {
        return salesOffice;
    }

    public void setSalesOffice(String salesOffice) {
        this.salesOffice = salesOffice;
    }

    public String getTglPoString() {
        return tglPoString;
    }

    public void setTglPoString(String tglPoString) {
        this.tglPoString = tglPoString;
    }

    public String getEdPoString() {
        return edPoString;
    }

    public void setEdPoString(String edPoString) {
        this.edPoString = edPoString;
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

    public String getPhotoString() {
        return photoString;
    }

    public void setPhotoString(String photoString) {
        this.photoString = photoString;
    }

    public String getSignatureString() {
        return signatureString;
    }

    public void setSignatureString(String signatureString) {
        this.signatureString = signatureString;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRequestDisc() {
        return requestDisc;
    }

    public void setRequestDisc(int requestDisc) {
        this.requestDisc = requestDisc;
    }

    public String getDescriptionDisc() {
        return descriptionDisc;
    }

    public void setDescriptionDisc(String descriptionDisc) {
        this.descriptionDisc = descriptionDisc;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    
}

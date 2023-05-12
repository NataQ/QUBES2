package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Foo on 11/29/2016.
 */

public class User implements Serializable {
    @SerializedName("client")
    String idClient;
    @SerializedName("idEmployee")
    String idEmployee;
    @SerializedName("nik")
    String nik;
    @SerializedName("fullName")
    String fullName;
    @SerializedName("division")
    String division;
    @SerializedName("position")
    String position;
    @SerializedName("idSupervisor")
    String idSupervisor;
    @SerializedName("username")
    String username;
    @SerializedName("photoString")
    String photoString;
    @SerializedName("idDivision")
    String idDivision;
    @SerializedName("idPosition")
    String idPosition;
    @SerializedName("idSubPosition")
    String idSubPosition;
    @SerializedName("listOutlet")
    String[] listOutlet;
    @SerializedName("idOutlet")
    String idOutlet;

    @SerializedName("listReasonReturn")
    private ArrayList<Reason> reasonArrayList;

    int reason = 2;

    String dateString;

    String supervisorNik;
    String supervisorName;
    String supervisorPosition;

    @SerializedName("currentDate")
    private String currentDate;

    private String idSalesman;

    private String id_sales_office;

    private Long mElapsedRealTime;
    private String dateTimeNow;

    private String idPlant;

    public String getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(String idPlant) {
        this.idPlant = idPlant;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    private AdditionalInfo additionalInfo;

    public String getDateTimeNow() {
        return dateTimeNow;
    }

    public void setDateTimeNow(String dateTimeNow) {
        this.dateTimeNow = dateTimeNow;
    }

    private ArrayList<CreditInfoResponse> listCredit;

    private ArrayList<MaterialResponse> listMaterial;

    public Long getmElapsedRealTime() {
        return mElapsedRealTime;
    }

    public void setmElapsedRealTime(Long mElapsedRealTime) {
        this.mElapsedRealTime = mElapsedRealTime;
    }

    private ArrayList<MaterialResponse> listStoreCheck;

    private ArrayList<VisitOrderDetailResponse> listOrderDetail;

    private ArrayList<VisitOrderHeader> listOrderHeaderCanvas;

    private ArrayList<VisitOrderHeader> listOrderHeaderTaking;

    private VisitOrderHeaderDropDownResponse listSalesAndKlasifikasi;

    private ArrayList<ReturnResponse> listReturnHeader;

    public ArrayList<ReturnResponse> getListReturnHeader() {
        return listReturnHeader;
    }

    public void setListReturnHeader(ArrayList<ReturnResponse> listReturnHeader) {
        this.listReturnHeader = listReturnHeader;
    }

    public ArrayList<VisitOrderHeader> getListOrderHeaderCanvas() {
        return listOrderHeaderCanvas;
    }

    public void setListOrderHeaderCanvas(ArrayList<VisitOrderHeader> listOrderHeaderCanvas) {
        this.listOrderHeaderCanvas = listOrderHeaderCanvas;
    }

    public ArrayList<VisitOrderHeader> getListOrderHeaderTaking() {
        return listOrderHeaderTaking;
    }

    public void setListOrderHeaderTaking(ArrayList<VisitOrderHeader> listOrderHeaderTaking) {
        this.listOrderHeaderTaking = listOrderHeaderTaking;
    }

    public VisitOrderHeaderDropDownResponse getListSalesAndKlasfikasi() {
        return listSalesAndKlasifikasi;
    }

    public void setListSalesAndKlasfikasi(VisitOrderHeaderDropDownResponse listSalesAndKlasfikasi) {
        this.listSalesAndKlasifikasi = listSalesAndKlasfikasi;
    }

    public ArrayList<VisitOrderDetailResponse> getListOrderDetail() {
        return listOrderDetail;
    }

    public void setListOrderDetail(ArrayList<VisitOrderDetailResponse> listOrderDetail) {
        this.listOrderDetail = listOrderDetail;
    }

    public ArrayList<CreditInfoResponse> getListCredit() {
        return listCredit;
    }

    public void setListCredit(ArrayList<CreditInfoResponse> listCredit) {
        this.listCredit = listCredit;
    }

    public ArrayList<MaterialResponse> getListStoreCheck() {
        return listStoreCheck;
    }

    public void setListStoreCheck(ArrayList<MaterialResponse> listStoreCheck) {
        this.listStoreCheck = listStoreCheck;
    }

    public ArrayList<MaterialResponse> getListMaterial() {
        return listMaterial;
    }

    public void setListMaterial(ArrayList<MaterialResponse> listMaterial) {
        this.listMaterial = listMaterial;
    }

    public String getId_sales_office() {
        return id_sales_office;
    }

    public void setId_sales_office(String id_sales_office) {
        this.id_sales_office = id_sales_office;
    }

    public String getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(String idSalesman) {
        this.idSalesman = idSalesman;
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

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIdSupervisor() {
        return idSupervisor;
    }

    public void setIdSupervisor(String idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoString() {
        return photoString;
    }

    public void setPhotoString(String photoString) {
        this.photoString = photoString;
    }

    public String getIdDivision() {
        return idDivision;
    }

    public void setIdDivision(String idDivision) {
        this.idDivision = idDivision;
    }

    public String getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(String idPosition) {
        this.idPosition = idPosition;
    }

    public String getIdSubPosition() {
        return idSubPosition;
    }

    public void setIdSubPosition(String idSubPosition) {
        this.idSubPosition = idSubPosition;
    }

    public String[] getListOutlet() {
        return listOutlet;
    }

    public void setListOutlet(String[] listOutlet) {
        this.listOutlet = listOutlet;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public ArrayList<Reason> getReasonArrayList() {
        return reasonArrayList;
    }

    public void setReasonArrayList(ArrayList<Reason> reasonArrayList) {
        this.reasonArrayList = reasonArrayList;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}

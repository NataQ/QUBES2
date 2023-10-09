package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

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

    //aspp qubes
//    private String username;
//    private List<Group> listGroup;
//    private List<Authority> listAuthority;
    private String id;
    //    private String nik;
    private String fullname;
    private String imei;
    private String regis_id;
    private String tipe;
    private boolean ruteinap;
    private String idsalesgroup;
    private String salesgroupname;
    private String iddriver;
    private String drivername;
    private String idposition;
    private String userLogin;
    private String positionname;
    private String iddepo;
    private String deponame;
    private String idregion;
    private String regionname;
    private String token;
    private float radius;
    private int maxvisit;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIdposition() {
        return idposition;
    }

    public void setIdposition(String idposition) {
        this.idposition = idposition;
    }

    public int getMaxvisit() {
        return maxvisit;
    }

    public void setMaxvisit(int maxvisit) {
        this.maxvisit = maxvisit;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getRegis_id() {
        return regis_id;
    }

    public void setRegis_id(String regis_id) {
        this.regis_id = regis_id;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public boolean isRuteinap() {
        return ruteinap;
    }

    public void setRuteinap(boolean ruteinap) {
        this.ruteinap = ruteinap;
    }

    public String getIdsalesgroup() {
        return idsalesgroup;
    }

    public void setIdsalesgroup(String idsalesgroup) {
        this.idsalesgroup = idsalesgroup;
    }

    public String getSalesgroupname() {
        return salesgroupname;
    }

    public void setSalesgroupname(String salesgroupname) {
        this.salesgroupname = salesgroupname;
    }

    public String getIddriver() {
        return iddriver;
    }

    public void setIddriver(String iddriver) {
        this.iddriver = iddriver;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getPositionname() {
        return positionname;
    }

    public void setPositionname(String positionname) {
        this.positionname = positionname;
    }

    public String getIddepo() {
        return iddepo;
    }

    public void setIddepo(String iddepo) {
        this.iddepo = iddepo;
    }

    public String getDeponame() {
        return deponame;
    }

    public void setDeponame(String deponame) {
        this.deponame = deponame;
    }

    public String getIdregion() {
        return idregion;
    }

    public void setIdregion(String idregion) {
        this.idregion = idregion;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

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

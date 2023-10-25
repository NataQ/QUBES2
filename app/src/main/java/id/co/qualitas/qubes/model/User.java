package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    @SerializedName("client")
    String idClient;
    @SerializedName("idEmployee")
    String idEmployee;
    @SerializedName("fullName")
    String fullName;
    @SerializedName("division")
    String division;
    @SerializedName("position")
    String position;
    @SerializedName("idSupervisor")
    String idSupervisor;
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

    //aspp qubes=========================================================================================
    private String username;
//    private List<Group> listGroup;
//    private List<Authority> listAuthority;
    private String id;
    private String nik;
    private String full_name;
    private String imei;
    private String regis_id;
    private String tipe;
    private int rute_inap;
    private String id_sales_group;
    private String sales_group_name;
    private String id_driver;
    private String driver_name;
    private int id_position;
    private String userLogin;
    private String position_name;
    private String token;
    private String type_sales;
    private float radius;
    private int max_visit;
    private List<DepoRegion> depoRegionList;

    public String getType_sales() {
        return type_sales;
    }

    public void setType_sales(String type_sales) {
        this.type_sales = type_sales;
    }

    public List<DepoRegion> getDepoRegionList() {
        return depoRegionList;
    }

    public void setDepoRegionList(List<DepoRegion> depoRegionList) {
        this.depoRegionList = depoRegionList;
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

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public ArrayList<Reason> getReasonArrayList() {
        return reasonArrayList;
    }

    public void setReasonArrayList(ArrayList<Reason> reasonArrayList) {
        this.reasonArrayList = reasonArrayList;
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

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(String idSalesman) {
        this.idSalesman = idSalesman;
    }

    public String getId_sales_office() {
        return id_sales_office;
    }

    public void setId_sales_office(String id_sales_office) {
        this.id_sales_office = id_sales_office;
    }

    public Long getmElapsedRealTime() {
        return mElapsedRealTime;
    }

    public void setmElapsedRealTime(Long mElapsedRealTime) {
        this.mElapsedRealTime = mElapsedRealTime;
    }

    public String getDateTimeNow() {
        return dateTimeNow;
    }

    public void setDateTimeNow(String dateTimeNow) {
        this.dateTimeNow = dateTimeNow;
    }

    public String getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(String idPlant) {
        this.idPlant = idPlant;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    public int getRute_inap() {
        return rute_inap;
    }

    public void setRute_inap(int rute_inap) {
        this.rute_inap = rute_inap;
    }

    public String getId_sales_group() {
        return id_sales_group;
    }

    public void setId_sales_group(String id_sales_group) {
        this.id_sales_group = id_sales_group;
    }

    public String getSales_group_name() {
        return sales_group_name;
    }

    public void setSales_group_name(String sales_group_name) {
        this.sales_group_name = sales_group_name;
    }

    public String getId_driver() {
        return id_driver;
    }

    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public int getId_position() {
        return id_position;
    }

    public void setId_position(int id_position) {
        this.id_position = id_position;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getMax_visit() {
        return max_visit;
    }

    public void setMax_visit(int max_visit) {
        this.max_visit = max_visit;
    }
}

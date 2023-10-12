package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class RouteCustomer implements Serializable {
    private String idHeader;
    private String id;
    private String nama;
    private String rute;
    private String address;
    private String kode_pos;
    private String kota;
    private String mileage;
    private boolean route;
    private double latitude;
    private double longitude;
    private int isSync;

    public RouteCustomer(String id, String nama, String address, boolean route, double latitude, double longitude) {
        this.id = id;
        this.nama = nama;
        this.address = address;
        this.route = route;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public RouteCustomer(String id, String nama, String address, String mileage, double latitude, double longitude, boolean route) {
        this.id = id;
        this.nama = nama;
        this.address = address;
        this.mileage = mileage;
        this.latitude = latitude;
        this.longitude = longitude;
        this.route = route;
    }

    public RouteCustomer() {
    }

    public String getRute() {
        return rute;
    }

    public void setRute(String rute) {
        this.rute = rute;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public String getKode_pos() {
        return kode_pos;
    }

    public void setKode_pos(String kode_pos) {
        this.kode_pos = kode_pos;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public boolean isRoute() {
        return route;
    }

    public void setRoute(boolean route) {
        this.route = route;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }
}

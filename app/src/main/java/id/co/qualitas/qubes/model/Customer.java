package id.co.qualitas.qubes.model;

import com.google.android.gms.maps.model.LatLng;

public class Customer {
    private String idHeader;
    private String idCustomer;
    private String nameCustomer;
    private String address;
    private String phone;
    private double sisaCreditLimit;
    private double creditLimit;
    private double totalTagihan;
    private String namaPemilik;
    private String noKtp;
    private String noNpwp;
    private int status;
    private boolean route;
    private double latitude;
    private double longitude;
    private LatLng position;
    private String photoKtp;
    private String photoNpwp;
    private String photoOutlet;
    private boolean isSync;

    public Customer() {
    }

    public Customer(String idCustomer, String nameCustomer, String namaPemilik, String address, String phone, double creditLimit, String noKtp, String noNpwp, boolean route, double latitude, double longitude, boolean isSync) {
        this.idCustomer = idCustomer;
        this.namaPemilik = namaPemilik;
        this.nameCustomer = nameCustomer;
        this.address = address;
        this.phone = phone;
        this.creditLimit = creditLimit;
        this.noKtp = noKtp;
        this.noNpwp = noNpwp;
        this.route = route;
        this.latitude = latitude;
        this.latitude = longitude;
        this.isSync = isSync;
    }

    public Customer(String idCustomer, String nameCustomer, String address, boolean route, double latitude, double longitude) {
        this.idCustomer = idCustomer;
        this.nameCustomer = nameCustomer;
        this.address = address;
        this.route = route;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Customer(String idCustomer, String nameCustomer, String address, boolean route, LatLng position) {
        this.idCustomer = idCustomer;
        this.nameCustomer = nameCustomer;
        this.address = address;
        this.route = route;
        this.position = position;
    }
    public Customer(String idCustomer, String nameCustomer, String address, boolean route, LatLng position, int status) {
        this.idCustomer = idCustomer;
        this.nameCustomer = nameCustomer;
        this.address = address;
        this.route = route;
        this.position = position;
        this.status = status;
    }

    public String getPhotoKtp() {
        return photoKtp;
    }

    public void setPhotoKtp(String photoKtp) {
        this.photoKtp = photoKtp;
    }

    public String getPhotoNpwp() {
        return photoNpwp;
    }

    public void setPhotoNpwp(String photoNpwp) {
        this.photoNpwp = photoNpwp;
    }

    public String getPhotoOutlet() {
        return photoOutlet;
    }

    public void setPhotoOutlet(String photoOutlet) {
        this.photoOutlet = photoOutlet;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public boolean isSync() {
        return isSync;
    }
    public void setSync(boolean sync) {
        isSync = sync;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getSisaCreditLimit() {
        return sisaCreditLimit;
    }

    public void setSisaCreditLimit(double sisaCreditLimit) {
        this.sisaCreditLimit = sisaCreditLimit;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getTotalTagihan() {
        return totalTagihan;
    }

    public void setTotalTagihan(double totalTagihan) {
        this.totalTagihan = totalTagihan;
    }

    public String getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(String noKtp) {
        this.noKtp = noKtp;
    }

    public String getNoNpwp() {
        return noNpwp;
    }

    public void setNoNpwp(String noNpwp) {
        this.noNpwp = noNpwp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public boolean isRoute() {
        return route;
    }

    public void setRoute(boolean route) {
        this.route = route;
    }
}

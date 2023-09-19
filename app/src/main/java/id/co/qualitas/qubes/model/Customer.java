package id.co.qualitas.qubes.model;

import com.google.android.gms.maps.model.LatLng;

public class Customer {
    private String idCustomer;
    private String nameCustomer;
    private String address;
    private int status;
    private boolean route;
    private double latitude;
    private double longitude;
    private LatLng position;

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

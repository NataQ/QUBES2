package id.co.qualitas.qubes.model;

import com.google.android.gms.maps.model.LatLng;

public class Customer {
    private String idCustomer;
    private String nameCustomer;
    private String address;
    private boolean route;
    private LatLng position;

    public Customer(String idCustomer, String nameCustomer, String address, boolean route, LatLng position) {
        this.idCustomer = idCustomer;
        this.nameCustomer = nameCustomer;
        this.address = address;
        this.route = route;
        this.position = position;
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

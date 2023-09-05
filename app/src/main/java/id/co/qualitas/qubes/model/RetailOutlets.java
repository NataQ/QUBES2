package id.co.qualitas.qubes.model;

import com.google.android.gms.maps.model.LatLng;

public class RetailOutlets {
    private String namaOutlet;
    private String ownerOutlet;
    private String address;
    private String status;
    private LatLng latlong;
    private String priority;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private double distance;

    public LatLng getLatlong() {
        return latlong;
    }

    public void setLatlong(LatLng latlong) {
        this.latlong = latlong;
    }

    public String getNamaOutlet() {
        return namaOutlet;
    }

    public void setNamaOutlet(String namaOutlet) {
        this.namaOutlet = namaOutlet;
    }

    public String getOwnerOutlet() {
        return ownerOutlet;
    }

    public void setOwnerOutlet(String ownerOutlet) {
        this.ownerOutlet = ownerOutlet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

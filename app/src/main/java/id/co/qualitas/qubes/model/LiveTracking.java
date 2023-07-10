package id.co.qualitas.qubes.model;

public class LiveTracking {
    private double latCurrent;
    private double longCurrent;
    private String id_shipment;
    private String photo;
    private String remark_complete;
    private String driver_name;

    public LiveTracking() {
    }

    public LiveTracking(double latCurrent, double longCurrent) {
        this.latCurrent = latCurrent;
        this.longCurrent = longCurrent;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getRemark_complete() {
        return remark_complete;
    }

    public void setRemark_complete(String remark_complete) {
        this.remark_complete = remark_complete;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId_shipment() {
        return id_shipment;
    }

    public void setId_shipment(String id_shipment) {
        this.id_shipment = id_shipment;
    }

    public double getLatCurrent() {
        return latCurrent;
    }

    public void setLatCurrent(double latCurrent) {
        this.latCurrent = latCurrent;
    }

    public double getLongCurrent() {
        return longCurrent;
    }

    public void setLongCurrent(double longCurrent) {
        this.longCurrent = longCurrent;
    }
}

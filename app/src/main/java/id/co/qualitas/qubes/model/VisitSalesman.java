package id.co.qualitas.qubes.model;

public class VisitSalesman {
    private String customerId;
    private String idSalesman;
    private String date;
    private String checkInTime;
    private String checkOutTime;
    private int status;
    private String resumeTime;
    private String pauseTime;
    private double latCheckIn;
    private double longCheckIn;
    private double latCheckOut;
    private double longCheckOut;
    private boolean inside;
    private boolean insideCheckOut;
    private String idPauseReason;
    private String namePauseReason;
    private String descPauseReason;
    private String photoPauseReason;
    private String idCheckOutReason;
    private String nameCheckOutReason;
    private String descCheckOutReason;
    private String photoCheckOutReason;
    private int isSync;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getIdSalesman() {
        return idSalesman;
    }

    public void setIdSalesman(String idSalesman) {
        this.idSalesman = idSalesman;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResumeTime() {
        return resumeTime;
    }

    public void setResumeTime(String resumeTime) {
        this.resumeTime = resumeTime;
    }

    public String getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(String pauseTime) {
        this.pauseTime = pauseTime;
    }

    public double getLatCheckIn() {
        return latCheckIn;
    }

    public void setLatCheckIn(double latCheckIn) {
        this.latCheckIn = latCheckIn;
    }

    public double getLongCheckIn() {
        return longCheckIn;
    }

    public void setLongCheckIn(double longCheckIn) {
        this.longCheckIn = longCheckIn;
    }

    public double getLatCheckOut() {
        return latCheckOut;
    }

    public void setLatCheckOut(double latCheckOut) {
        this.latCheckOut = latCheckOut;
    }

    public double getLongCheckOut() {
        return longCheckOut;
    }

    public void setLongCheckOut(double longCheckOut) {
        this.longCheckOut = longCheckOut;
    }

    public boolean isInside() {
        return inside;
    }

    public void setInside(boolean inside) {
        this.inside = inside;
    }

    public boolean isInsideCheckOut() {
        return insideCheckOut;
    }

    public void setInsideCheckOut(boolean insideCheckOut) {
        this.insideCheckOut = insideCheckOut;
    }

    public String getIdPauseReason() {
        return idPauseReason;
    }

    public void setIdPauseReason(String idPauseReason) {
        this.idPauseReason = idPauseReason;
    }

    public String getNamePauseReason() {
        return namePauseReason;
    }

    public void setNamePauseReason(String namePauseReason) {
        this.namePauseReason = namePauseReason;
    }

    public String getDescPauseReason() {
        return descPauseReason;
    }

    public void setDescPauseReason(String descPauseReason) {
        this.descPauseReason = descPauseReason;
    }

    public String getPhotoPauseReason() {
        return photoPauseReason;
    }

    public void setPhotoPauseReason(String photoPauseReason) {
        this.photoPauseReason = photoPauseReason;
    }

    public String getIdCheckOutReason() {
        return idCheckOutReason;
    }

    public void setIdCheckOutReason(String idCheckOutReason) {
        this.idCheckOutReason = idCheckOutReason;
    }

    public String getNameCheckOutReason() {
        return nameCheckOutReason;
    }

    public void setNameCheckOutReason(String nameCheckOutReason) {
        this.nameCheckOutReason = nameCheckOutReason;
    }

    public String getDescCheckOutReason() {
        return descCheckOutReason;
    }

    public void setDescCheckOutReason(String descCheckOutReason) {
        this.descCheckOutReason = descCheckOutReason;
    }

    public String getPhotoCheckOutReason() {
        return photoCheckOutReason;
    }

    public void setPhotoCheckOutReason(String photoCheckOutReason) {
        this.photoCheckOutReason = photoCheckOutReason;
    }

    public int isSync() {
        return isSync;
    }

    public void setSync(int sync) {
        isSync = sync;
    }
}

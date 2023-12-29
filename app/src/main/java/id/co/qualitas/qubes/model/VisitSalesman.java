package id.co.qualitas.qubes.model;

public class VisitSalesman {
    private String idHeader;
    private String idVisit;
    private String customerId;
    private String customerName;
    private String address;
    private String idSalesman;
    private String date;
    private String checkInTime;
    private String checkOutTime;
    private int status;
    private String resumeTime;
    private String pauseTime;
    private double totalOrder;

    private double latCheckIn;
    private double longCheckIn;
    private double latCheckOut;
    private double longCheckOut;
    private double latLokasiToko;
    private double longLokasiToko;
    private double longLokasiGudang;
    private double latLokasiGudang;
    private double latLokasiTagihan;
    private double longLokasiTagihan;
    private boolean inside;
    private boolean insideCheckOut;
    private String idPauseReason;
    private String namePauseReason;
    private String descPauseReason;
    private String photoPauseReason;
    private String idNotVisitReason;
    private String nameNotVisitReason;
    private String descNotVisitReason;
    private String photoNotVisitReason;
    private String idNotBuyReason;
    private String nameNotBuyReason;
    private String descNotBuyReason;
    private String photoNotBuyReason;
    private String timer;//new
    private int posReason;
    private int isSync;
    private boolean isNoo;
    private boolean isRoute;

    public double getLatLokasiToko() {
        return latLokasiToko;
    }

    public void setLatLokasiToko(double latLokasiToko) {
        this.latLokasiToko = latLokasiToko;
    }

    public double getLongLokasiToko() {
        return longLokasiToko;
    }

    public void setLongLokasiToko(double longLokasiToko) {
        this.longLokasiToko = longLokasiToko;
    }

    public double getLongLokasiGudang() {
        return longLokasiGudang;
    }

    public void setLongLokasiGudang(double longLokasiGudang) {
        this.longLokasiGudang = longLokasiGudang;
    }

    public double getLatLokasiGudang() {
        return latLokasiGudang;
    }

    public void setLatLokasiGudang(double latLokasiGudang) {
        this.latLokasiGudang = latLokasiGudang;
    }

    public double getLatLokasiTagihan() {
        return latLokasiTagihan;
    }

    public void setLatLokasiTagihan(double latLokasiTagihan) {
        this.latLokasiTagihan = latLokasiTagihan;
    }

    public double getLongLokasiTagihan() {
        return longLokasiTagihan;
    }

    public void setLongLokasiTagihan(double longLokasiTagihan) {
        this.longLokasiTagihan = longLokasiTagihan;
    }

    public double getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(double totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getIdVisit() {
        return idVisit;
    }

    public void setIdVisit(String idVisit) {
        this.idVisit = idVisit;
    }

    public String getIdNotVisitReason() {
        return idNotVisitReason;
    }

    public void setIdNotVisitReason(String idNotVisitReason) {
        this.idNotVisitReason = idNotVisitReason;
    }

    public String getNameNotVisitReason() {
        return nameNotVisitReason;
    }

    public void setNameNotVisitReason(String nameNotVisitReason) {
        this.nameNotVisitReason = nameNotVisitReason;
    }

    public String getDescNotVisitReason() {
        return descNotVisitReason;
    }

    public void setDescNotVisitReason(String descNotVisitReason) {
        this.descNotVisitReason = descNotVisitReason;
    }

    public String getPhotoNotVisitReason() {
        return photoNotVisitReason;
    }

    public void setPhotoNotVisitReason(String photoNotVisitReason) {
        this.photoNotVisitReason = photoNotVisitReason;
    }

    public String getIdNotBuyReason() {
        return idNotBuyReason;
    }

    public void setIdNotBuyReason(String idNotBuyReason) {
        this.idNotBuyReason = idNotBuyReason;
    }

    public String getNameNotBuyReason() {
        return nameNotBuyReason;
    }

    public void setNameNotBuyReason(String nameNotBuyReason) {
        this.nameNotBuyReason = nameNotBuyReason;
    }

    public String getDescNotBuyReason() {
        return descNotBuyReason;
    }

    public void setDescNotBuyReason(String descNotBuyReason) {
        this.descNotBuyReason = descNotBuyReason;
    }

    public String getPhotoNotBuyReason() {
        return photoNotBuyReason;
    }

    public void setPhotoNotBuyReason(String photoNotBuyReason) {
        this.photoNotBuyReason = photoNotBuyReason;
    }

    public boolean isRoute() {
        return isRoute;
    }

    public void setRoute(boolean route) {
        isRoute = route;
    }

    public boolean isNoo() {
        return isNoo;
    }

    public void setNoo(boolean noo) {
        isNoo = noo;
    }

    public int getPosReason() {
        return posReason;
    }

    public void setPosReason(int posReason) {
        this.posReason = posReason;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

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

    public int isSync() {
        return isSync;
    }

    public void setSync(int sync) {
        isSync = sync;
    }
}

package id.co.qualitas.qubes.model;

import java.io.Serializable;

/**
 * Created by Foo on 2/7/2017.
 */

public class CheckInOutRequest implements Serializable {
    private String idOutlet;
    private String outletName;
    private String idEmployee;
    private String latCheckIn;
    private String longCheckIn;
    private String latCheckOut;
    private String longCheckOut;
    private String reason;
    private String pauseReason;
    private String timer;
    private String statusCheckIn;
    private String dateString;

    private String checkInTime;
    private String pauseTime;
    private String continueTime;

    public String getContinueTime() {
        return continueTime;
    }

    public void setContinueTime(String continueTime) {
        this.continueTime = continueTime;
    }

    public String getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(String pauseTime) {
        this.pauseTime = pauseTime;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getStatusCheckIn() {
        return statusCheckIn;
    }

    public void setStatusCheckIn(String statusCheckIn) {
        this.statusCheckIn = statusCheckIn;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getLatCheckIn() {
        return latCheckIn;
    }

    public void setLatCheckIn(String latCheckIn) {
        this.latCheckIn = latCheckIn;
    }

    public String getLongCheckIn() {
        return longCheckIn;
    }

    public void setLongCheckIn(String longCheckIn) {
        this.longCheckIn = longCheckIn;
    }

    public String getLatCheckOut() {
        return latCheckOut;
    }

    public void setLatCheckOut(String latCheckOut) {
        this.latCheckOut = latCheckOut;
    }

    public String getLongCheckOut() {
        return longCheckOut;
    }

    public void setLongCheckOut(String longCheckOut) {
        this.longCheckOut = longCheckOut;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPauseReason() {
        return pauseReason;
    }

    public void setPauseReason(String pauseReason) {
        this.pauseReason = pauseReason;
    }
}

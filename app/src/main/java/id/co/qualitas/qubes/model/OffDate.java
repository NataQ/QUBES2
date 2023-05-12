package id.co.qualitas.qubes.model;

import java.io.Serializable;

/**
 * Created by Wiliam on 10/9/2017.
 */

public class OffDate implements Serializable{
    private String curDate;
    private long elapseTime;

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public long getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(long elapseTime) {
        this.elapseTime = elapseTime;
    }
}

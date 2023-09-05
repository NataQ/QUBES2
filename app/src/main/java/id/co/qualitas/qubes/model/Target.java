package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.List;

public class Target implements Serializable {
    public String groupMat;
    public int tgtAt;
    public int at;
    public int tgtOms;
    public int oms;

    public Target() {
    }

    public Target(String groupMat, int tgtAt, int at, int tgtOms, int oms) {
        this.groupMat = groupMat;
        this.tgtAt = tgtAt;
        this.at = at;
        this.tgtOms = tgtOms;
        this.oms = oms;
    }

    public String getGroupMat() {
        return groupMat;
    }

    public void setGroupMat(String groupMat) {
        this.groupMat = groupMat;
    }

    public int getTgtAt() {
        return tgtAt;
    }

    public void setTgtAt(int tgtAt) {
        this.tgtAt = tgtAt;
    }

    public int getAt() {
        return at;
    }

    public void setAt(int at) {
        this.at = at;
    }

    public int getTgtOms() {
        return tgtOms;
    }

    public void setTgtOms(int tgtOms) {
        this.tgtOms = tgtOms;
    }

    public int getOms() {
        return oms;
    }

    public void setOms(int oms) {
        this.oms = oms;
    }
}

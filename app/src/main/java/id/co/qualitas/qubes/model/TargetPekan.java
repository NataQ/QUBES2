package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.List;

public class TargetPekan implements Serializable {
    public String pekan;
    public double at_tgt;
    public double at_real;
    public double at_percent;
    public double at_minus;
    public double omz_tgt;
    public double omz_real;
    public double omz_percent;
    public double omz_minus;

    public String getPekan() {
        return pekan;
    }

    public void setPekan(String pekan) {
        this.pekan = pekan;
    }

    public double getAt_tgt() {
        return at_tgt;
    }

    public void setAt_tgt(double at_tgt) {
        this.at_tgt = at_tgt;
    }

    public double getAt_real() {
        return at_real;
    }

    public void setAt_real(double at_real) {
        this.at_real = at_real;
    }

    public double getAt_percent() {
        return at_percent;
    }

    public void setAt_percent(double at_percent) {
        this.at_percent = at_percent;
    }

    public double getAt_minus() {
        return at_minus;
    }

    public void setAt_minus(double at_minus) {
        this.at_minus = at_minus;
    }

    public double getOmz_tgt() {
        return omz_tgt;
    }

    public void setOmz_tgt(double omz_tgt) {
        this.omz_tgt = omz_tgt;
    }

    public double getOmz_real() {
        return omz_real;
    }

    public void setOmz_real(double omz_real) {
        this.omz_real = omz_real;
    }

    public double getOmz_percent() {
        return omz_percent;
    }

    public void setOmz_percent(double omz_percent) {
        this.omz_percent = omz_percent;
    }

    public double getOmz_minus() {
        return omz_minus;
    }

    public void setOmz_minus(double omz_minus) {
        this.omz_minus = omz_minus;
    }
}

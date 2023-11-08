package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class Target implements Serializable {
    public String id_material_group;
    public String material_group_name;
    public double tgtat;
    public double at;
    public double tgtoms;
    public double oms;

    public Target() {
    }

    public Target(String material_group_name, double tgtat, double at, double tgtoms, double oms) {
        this.material_group_name = material_group_name;
        this.tgtat = tgtat;
        this.at = at;
        this.tgtoms = tgtoms;
        this.oms = oms;
    }

    public String getMaterial_group_name() {
        return material_group_name;
    }

    public void setMaterial_group_name(String material_group_name) {
        this.material_group_name = material_group_name;
    }

    public double getTgtat() {
        return tgtat;
    }

    public void setTgtat(double tgtat) {
        this.tgtat = tgtat;
    }

    public double getAt() {
        return at;
    }

    public void setAt(double at) {
        this.at = at;
    }

    public double getTgtoms() {
        return tgtoms;
    }

    public void setTgtoms(double tgtoms) {
        this.tgtoms = tgtoms;
    }

    public double getOms() {
        return oms;
    }

    public void setOms(double oms) {
        this.oms = oms;
    }
}

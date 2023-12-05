package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class Target implements Serializable {
    public String id_material_group;
    public String material_group_name;
    public double tgt_at;
    public double at;
    public double tgt_oms;
    public double oms;

    public Target() {
    }

    public Target(String material_group_name, double tgt_at, double at, double tgt_oms, double oms) {
        this.material_group_name = material_group_name;
        this.tgt_at = tgt_at;
        this.at = at;
        this.tgt_oms = tgt_oms;
        this.oms = oms;
    }

    public String getMaterial_group_name() {
        return material_group_name;
    }

    public void setMaterial_group_name(String material_group_name) {
        this.material_group_name = material_group_name;
    }

    public double getTgt_at() {
        return tgt_at;
    }

    public void setTgt_at(double tgt_at) {
        this.tgt_at = tgt_at;
    }

    public double getAt() {
        return at;
    }

    public void setAt(double at) {
        this.at = at;
    }

    public double getTgt_oms() {
        return tgt_oms;
    }

    public void setTgt_oms(double tgt_oms) {
        this.tgt_oms = tgt_oms;
    }

    public double getOms() {
        return oms;
    }

    public void setOms(double oms) {
        this.oms = oms;
    }
}

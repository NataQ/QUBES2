package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class GroupMaxBon implements Serializable {
    private String id_group_max_bon;
    private String name_group_max_bon;
    private String id_group_sales;
    private double limits;
    private String id_material_group;
    private String name_material_group;

    public GroupMaxBon() {
    }

    public String getId_group_max_bon() {
        return id_group_max_bon;
    }

    public void setId_group_max_bon(String id_group_max_bon) {
        this.id_group_max_bon = id_group_max_bon;
    }

    public String getName_group_max_bon() {
        return name_group_max_bon;
    }

    public void setName_group_max_bon(String name_group_max_bon) {
        this.name_group_max_bon = name_group_max_bon;
    }

    public String getId_group_sales() {
        return id_group_sales;
    }

    public void setId_group_sales(String id_group_sales) {
        this.id_group_sales = id_group_sales;
    }

    public double getLimits() {
        return limits;
    }

    public void setLimits(double limits) {
        this.limits = limits;
    }

    public String getId_material_group() {
        return id_material_group;
    }

    public void setId_material_group(String id_material_group) {
        this.id_material_group = id_material_group;
    }

    public String getName_material_group() {
        return name_material_group;
    }

    public void setName_material_group(String name_material_group) {
        this.name_material_group = name_material_group;
    }
}

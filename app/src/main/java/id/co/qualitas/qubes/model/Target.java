package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.List;

public class Target implements Serializable {
    public String id_material_group;
    public String name_material_group;
    public List<TargetPekan> pekan_list;

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

    public List<TargetPekan> getPekan_list() {
        return pekan_list;
    }

    public void setPekan_list(List<TargetPekan> pekan_list) {
        this.pekan_list = pekan_list;
    }
}

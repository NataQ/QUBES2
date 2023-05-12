package id.co.qualitas.qubes.model;

/**
 * Created by Wiliam on 8/9/2017.
 */

public class Partner {
    private String id_outlet;
    private String id_partner;
    private String namePartner;

    public String getNamePartner() {
        return namePartner;
    }

    public void setNamePartner(String namePartner) {
        this.namePartner = namePartner;
    }

    public String getId_outlet() {
        return id_outlet;
    }

    public void setId_outlet(String id_outlet) {
        this.id_outlet = id_outlet;
    }

    public String getId_partner() {
        return id_partner;
    }

    public void setId_partner(String id_partner) {
        this.id_partner = id_partner;
    }
}

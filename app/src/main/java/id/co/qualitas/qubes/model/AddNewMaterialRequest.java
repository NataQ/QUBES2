package id.co.qualitas.qubes.model;

import java.io.Serializable;

/**
 * Created by GRACE on 31/08/2015.
 */
public class AddNewMaterialRequest implements Serializable {
    public String idMaterial;
    public String idOutlet;
    public String jenisJual;

    public String getJenisJual() {
        return jenisJual;
    }

    public void setJenisJual(String jenisJual) {
        this.jenisJual = jenisJual;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }
}

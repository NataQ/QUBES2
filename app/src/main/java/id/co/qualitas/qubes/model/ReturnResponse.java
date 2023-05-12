package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Foo on 2/3/2017.
 */

public class ReturnResponse implements Serializable {
    private String id;
    private String idOutlet;
    private String noBrb;
    private String noRr;
    private String tanggalBrb;
    private String tanggalRr ;
    private String category;
    private ArrayList<Return> listReturnDetail;
    private String statusSync;

    private String id_mobile;

    private Boolean deleted;


    private String date_mobile;

    public String getDate_mobile() {
        return date_mobile;
    }

    public void setDate_mobile(String date_mobile) {
        this.date_mobile = date_mobile;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getId_mobile() {
        return id_mobile;
    }

    public void setId_mobile(String id_mobile) {
        this.id_mobile = id_mobile;
    }

    public String getStatusSync() {
        return statusSync;
    }

    public void setStatusSync(String statusSync) {
        this.statusSync = statusSync;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getNoBrb() {
        return noBrb;
    }

    public void setNoBrb(String noBrb) {
        this.noBrb = noBrb;
    }

    public String getNoRr() {
        return noRr;
    }

    public void setNoRr(String noRr) {
        this.noRr = noRr;
    }

    public String getTanggalBrb() {
        return tanggalBrb;
    }

    public void setTanggalBrb(String tanggalBrb) {
        this.tanggalBrb = tanggalBrb;
    }

    public String getTanggalRr() {
        return tanggalRr;
    }

    public void setTanggalRr(String tanggalRr) {
        this.tanggalRr = tanggalRr;
    }

    public ArrayList<Return> getListReturnDetail() {
        return listReturnDetail;
    }

    public void setListReturnDetail(ArrayList<Return> listReturnDetail) {
        this.listReturnDetail = listReturnDetail;
    }
}

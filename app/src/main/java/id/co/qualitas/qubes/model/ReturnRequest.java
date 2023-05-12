package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ReturnRequest implements Serializable {
    private String id_mobile;
    private String idEmployee;
    private String idOutlet;
    private String noBrb;
    private String noRr;
    private String tanggalBrbString;
    private String tanggalRrString;
    private ArrayList<Return> returnDetail;
    private ArrayList<Return> listSaveReturn;

    private String tanggalBrb;
    private String tanggalRr;

    private String syncStatus;

    private String date_mobile;

    public String getDate_mobile() {
        return date_mobile;
    }

    public void setDate_mobile(String date_mobile) {
        this.date_mobile = date_mobile;
    }

    public String getId_mobile() {
        return id_mobile;
    }

    public void setId_mobile(String id_mobile) {
        this.id_mobile = id_mobile;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
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

    public ArrayList<Return> getListSaveReturn() {
        return listSaveReturn;
    }

    public void setListSaveReturn(ArrayList<Return> listSaveReturn) {
        this.listSaveReturn = listSaveReturn;
    }

    public String getIdHeader() {
        return id_mobile;
    }

    public void setIdHeader(String idHeader) {
        this.id_mobile = idHeader;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
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

    public String getTanggalBrbString() {
        return tanggalBrbString;
    }

    public void setTanggalBrbString(String tanggalBrbString) {
        this.tanggalBrbString = tanggalBrbString;
    }

    public String getTanggalRrString() {
        return tanggalRrString;
    }

    public void setTanggalRrString(String tanggalRrString) {
        this.tanggalRrString = tanggalRrString;
    }

    public ArrayList<Return> getReturnDetail() {
        return returnDetail;
    }

    public void setReturnDetail(ArrayList<Return> returnDetail) {
        this.returnDetail = returnDetail;
    }
}

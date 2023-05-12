package id.co.qualitas.qubes.model;

/**
 * Created by Wiliam on 5/3/2017.
 */

public class SummaryRequest {
    private String idEmployee;
    private String dateFrom;
    private String dateTo;
    private String matClass;
    private String idOutlet;


    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getMatClass() {
        return matClass;
    }

    public void setMatClass(String matClass) {
        this.matClass = matClass;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }
}

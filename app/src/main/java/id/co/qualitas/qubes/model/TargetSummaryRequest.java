package id.co.qualitas.qubes.model;

/**
 * Created by Natalia on 3/22/2017.
 */

public class TargetSummaryRequest {
    String idEmployee;
    String month;
    String year;
    String idOutlet;
    String matClass;
    String type;
    String period;
    String dateFrom;
    String dateTo;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getMatClass() {
        return matClass;
    }

    public void setMatClass(String matClass) {
        this.matClass = matClass;
    }
}

package id.co.qualitas.qubes.model;


public class VisitPlan {

    private String customer_name;
    private String tanggal;

    public VisitPlan(){}

    public VisitPlan(String customer_name,String tanggal){
        this.tanggal = tanggal;
        this.customer_name=customer_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}

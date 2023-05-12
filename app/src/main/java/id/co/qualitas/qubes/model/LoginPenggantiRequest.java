package id.co.qualitas.qubes.model;

import java.io.Serializable;

/**
 * Created by Foo on 3/15/2017.
 */

public class LoginPenggantiRequest implements Serializable {
    private String salesman;
    private String supervisor;
    private String idClient;

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }
}

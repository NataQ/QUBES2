package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Natalia on 3/22/2017.
 */

public class SpinnerTargetResponse implements Serializable {
    @SerializedName("matClass")
    private List<String> matClass;

    @SerializedName("listCustomer")
    private List<CustomerTargetSpinner> listCustomer;

    public List<String> getMatClass() {
        return matClass;
    }

    public void setMatClass(List<String> matClass) {
        this.matClass = matClass;
    }

    public List<CustomerTargetSpinner> getListCustomer() {
        return listCustomer;
    }

    public void setListCustomer(List<CustomerTargetSpinner> listCustomer) {
        this.listCustomer = listCustomer;
    }
}

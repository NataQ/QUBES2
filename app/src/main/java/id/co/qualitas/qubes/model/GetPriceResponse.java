package id.co.qualitas.qubes.model;

import java.io.Serializable;

/**
 * Created by Wiliam on 6/2/2017.
 */

public class GetPriceResponse implements Serializable{
    private ToPrice[] listToPrice;
    private ToPrice[] listOneTimeDiscount;
    private String idToPrice;

    public ToPrice[] getListOneTimeDiscount() {
        return listOneTimeDiscount;
    }

    public void setListOneTimeDiscount(ToPrice[] listOneTimeDiscount) {
        this.listOneTimeDiscount = listOneTimeDiscount;
    }

    public ToPrice[] getListToPrice() {
        return listToPrice;
    }

    public void setListToPrice(ToPrice[] listToPrice) {
        this.listToPrice = listToPrice;
    }

    public String getIdToPrice() {
        return idToPrice;
    }

    public void setIdToPrice(String idToPrice) {
        this.idToPrice = idToPrice;
    }
}

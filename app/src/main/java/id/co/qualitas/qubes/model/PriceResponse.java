package id.co.qualitas.qubes.model;

import java.io.Serializable;

/**
 * Created by WIlliam-QAS on 29/03/2017.
 */

public class PriceResponse implements Serializable {
    private String price;
    private String priceNett;
    private String disc;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceNett() {
        return priceNett;
    }

    public void setPriceNett(String priceNett) {
        this.priceNett = priceNett;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }
}

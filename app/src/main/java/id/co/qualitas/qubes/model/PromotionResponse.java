package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Foo on 1/24/2017.
 */

public class PromotionResponse implements Serializable {
    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    private ArrayList<Discount> discountArrayList;

    public ArrayList<Discount> getDiscountArrayList() {
        return discountArrayList;
    }

    public void setDiscountArrayList(ArrayList<Discount> discountArrayList) {
        this.discountArrayList = discountArrayList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

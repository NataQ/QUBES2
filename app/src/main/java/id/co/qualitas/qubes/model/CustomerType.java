package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerType implements Serializable{
    @SerializedName("id")
    private String id;
    private String type_price;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_price() {
        return type_price;
    }

    public void setType_price(String type_price) {
        this.type_price = type_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

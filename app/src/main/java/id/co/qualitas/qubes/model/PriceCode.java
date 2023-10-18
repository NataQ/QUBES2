package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class PriceCode implements Serializable {
    int idPriceCodeDB;
    int id_product_group;
    String udf_5;
    String udf_5_desc;
    String price_list_code;

    public int getIdPriceCodeDB() {
        return idPriceCodeDB;
    }

    public void setIdPriceCodeDB(int idPriceCodeDB) {
        this.idPriceCodeDB = idPriceCodeDB;
    }

    public int getId_product_group() {
        return id_product_group;
    }

    public void setId_product_group(int id_product_group) {
        this.id_product_group = id_product_group;
    }

    public String getUdf_5() {
        return udf_5;
    }

    public void setUdf_5(String udf_5) {
        this.udf_5 = udf_5;
    }

    public String getUdf_5_desc() {
        return udf_5_desc;
    }

    public void setUdf_5_desc(String udf_5_desc) {
        this.udf_5_desc = udf_5_desc;
    }

    public String getPrice_list_code() {
        return price_list_code;
    }

    public void setPrice_list_code(String price_list_code) {
        this.price_list_code = price_list_code;
    }
}

package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class SalesPriceHeader implements Serializable {
    int idSalesPriceHeaderDB;
    String top;
    String price_list_code;

    public int getIdSalesPriceHeaderDB() {
        return idSalesPriceHeaderDB;
    }

    public void setIdSalesPriceHeaderDB(int idSalesPriceHeaderDB) {
        this.idSalesPriceHeaderDB = idSalesPriceHeaderDB;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getPrice_list_code() {
        return price_list_code;
    }

    public void setPrice_list_code(String price_list_code) {
        this.price_list_code = price_list_code;
    }
}

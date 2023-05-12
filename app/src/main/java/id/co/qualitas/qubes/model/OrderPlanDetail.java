package id.co.qualitas.qubes.model;

public class OrderPlanDetail {
    private String code;
    private String qty1;
    private String qtyLastO1;
    private String qty2;
    private String qtyLastO2;
    private String qtyLastS1;
    private String qtyLastS2;
    private Integer price;
    private String priceNett;
    private String namaProduk;

    public OrderPlanDetail(String code, String qtyLastO1, String qtyLastO2, String qtyLastS1, String qtyLastS2, String qty1, String qty2, Integer price, String namaProduk) {
        this.code = code;
        this.qty1 = qty1;
        this.qty2 = qty2;
        this.qtyLastO1 = qtyLastO1;
        this.qtyLastO2 = qtyLastO2;
        this.qtyLastS1 = qtyLastS1;
        this.qtyLastS2 = qtyLastS2;
        this.price = price;
        this.namaProduk = namaProduk;
    }

    public OrderPlanDetail(String code, String qtyLastO1, String qtyLastO2, String qtyLastS1, String qtyLastS2, Integer price, String namaProduk) {
        this.code = code;
        this.qtyLastO1 = qtyLastO1;
        this.qtyLastO2 = qtyLastO2;
        this.qtyLastS1 = qtyLastS1;
        this.qtyLastS2 = qtyLastS2;
        this.price = price;
        this.namaProduk = namaProduk;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPriceNett() {
        return priceNett;
    }

    public void setPriceNett(String priceNett) {
        this.priceNett = priceNett;
    }

    public String getQty1() {
        return qty1;
    }

    public void setQty1(String qty1) {
        this.qty1 = qty1;
    }

    public String getQty2() {
        return qty2;
    }

    public void setQty2(String qty2) {
        this.qty2 = qty2;
    }

    public String getQtyLastO1() {
        return qtyLastO1;
    }


    public String getQtyLastO2() {
        return qtyLastO2;
    }

    public String getQtyLastS1() {
        return qtyLastS1;
    }


    public String getQtyLastS2() {
        return qtyLastS2;
    }

}

package id.co.qualitas.qubes.model;


public class Product {

    private String customer_name;
    private String material_name;

    public Product(){}

    public Product(String customer_name, String tanggal){
        this.material_name = tanggal;
        this.customer_name=customer_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String tanggal) {
        this.material_name = tanggal;
    }
}

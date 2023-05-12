package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/22/2016.
 */
public class Promise {
    private String dueDate;
    private String purchaseDate;
    private String name;

    public Promise(String dueDate, String purchaseDate, String name) {
        this.dueDate = dueDate;
        this.purchaseDate = purchaseDate;
        this.name = name;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

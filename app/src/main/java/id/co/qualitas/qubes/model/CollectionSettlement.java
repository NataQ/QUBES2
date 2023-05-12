package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 8/12/2016.
 */

public class CollectionSettlement {
    private String name;
    private String nameCode;
    private String purchaseDate;
    private String amount;
    private int typeSettlement;

    public CollectionSettlement(String name, String nameCode, String purchaseDate, String amount, int typeSettlement) {
        this.name = name;
        this.nameCode = nameCode;
        this.purchaseDate = purchaseDate;
        this.amount = amount;
        this.typeSettlement = typeSettlement;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCode() {
        return nameCode;
    }

    public void setNameCode(String nameCode) {
        this.nameCode = nameCode;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getTypeSettlement() {
        return typeSettlement;
    }

    public void setTypeSettlement(int typeSettlement) {
        this.typeSettlement = typeSettlement;
    }
}

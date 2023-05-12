package id.co.qualitas.qubes.model;

/**
 * Created by Wiliam on 1/2/2018.
 */

public class PromotionFreeGoods {
    private String doc_no;
    private String kc_no;
    private String validFrom;
    private String validTo;
    private String condition;
    private String bonus;
    private String SalesDocumentType;
    private String type;
    private String Customer;
    private String product;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getDoc_no() {
        return doc_no;
    }

    public void setDoc_no(String doc_no) {
        this.doc_no = doc_no;
    }

    public String getKc_no() {
        return kc_no;
    }

    public void setKc_no(String kc_no) {
        this.kc_no = kc_no;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getSalesDocumentType() {
        return SalesDocumentType;
    }

    public void setSalesDocumentType(String salesDocumentType) {
        SalesDocumentType = salesDocumentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

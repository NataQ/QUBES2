package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/22/2016.
 */
public class OrderPlan {
    private String idHeader;//id
    private String code;
    private String name;
    private String contact;

    public OrderPlan(String code, String contact, String name) {
        this.code = code;
        this.contact = contact;
        this.name = name;
    }

    public OrderPlan() {
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

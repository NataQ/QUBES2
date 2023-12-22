package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class Role implements Serializable {
    private String group_id;
    private String authority;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}

package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Foo on 12/22/2016.
 */

public class ChangePasswordRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("client")
    private String client;

    @SerializedName("oldPassword")
    private String oldPassword;

    @SerializedName("password")
    private String password;


    @SerializedName("regisId")
    private String regisId;

    private String supervisor;

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getRegisId() {
        return regisId;
    }

    public void setRegisId(String regisId) {
        this.regisId = regisId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LoginResponse implements Serializable {
    @SerializedName("access_token")
    String access_token;
    @SerializedName("token_type")
    String token_type;
    @SerializedName("refresh_token")
    String refresh_token;
    @SerializedName("expires_in")
    int expires_in;
    @SerializedName("scope")
    String scope;
    @SerializedName("error")
    String error;
    @SerializedName("error_description")
    String error_description;

    Object user;
    List<Object> listReason;
    List<Object> listBank;
    float radius;

    public List<Object> getListReason() {
        return listReason;
    }

    public void setListReason(List<Object> listReason) {
        this.listReason = listReason;
    }

    public List<Object> getListBank() {
        return listBank;
    }

    public void setListBank(List<Object> listBank) {
        this.listBank = listBank;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}

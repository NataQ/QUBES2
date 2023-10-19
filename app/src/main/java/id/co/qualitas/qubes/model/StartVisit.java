package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StartVisit implements Serializable {
    private String kmAwal;
    private String kmAkhir;
    private String username;
    private String userLogin;

    public String getKmAwal() {
        return kmAwal;
    }

    public void setKmAwal(String kmAwal) {
        this.kmAwal = kmAwal;
    }

    public String getKmAkhir() {
        return kmAkhir;
    }

    public void setKmAkhir(String kmAkhir) {
        this.kmAkhir = kmAkhir;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}

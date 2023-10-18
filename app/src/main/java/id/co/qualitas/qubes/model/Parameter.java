package id.co.qualitas.qubes.model;

import java.io.Serializable;

public class Parameter implements Serializable {
    int idParameterDB;
    String key;
    String value;
    String description;

    public int getIdParameterDB() {
        return idParameterDB;
    }

    public void setIdParameterDB(int idParameterDB) {
        this.idParameterDB = idParameterDB;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Foo on 12/22/2016.
 */

public class MessageResponse implements Serializable {
    @SerializedName("message")
    String message;

    @SerializedName("idMessage")
    int idMessage;

    @SerializedName("result")
    Result result;

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
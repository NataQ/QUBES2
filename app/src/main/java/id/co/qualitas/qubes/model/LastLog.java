package id.co.qualitas.qubes.model;

/**
 * Created by Wiliam on 1/16/2018.
 */

public class LastLog {
    private String lastSync;
    private String lastMsg;

    public String getLastSync() {
        return lastSync;
    }

    public void setLastSync(String lastSync) {
        this.lastSync = lastSync;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }
}

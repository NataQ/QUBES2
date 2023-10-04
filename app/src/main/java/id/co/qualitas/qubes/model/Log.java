package id.co.qualitas.qubes.model;

public class Log {
    private String descLog;
    private String dateLog;
    private String timeLog;
    private boolean isSync;

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public String getDescLog() {
        return descLog;
    }

    public void setDescLog(String descLog) {
        this.descLog = descLog;
    }

    public String getDateLog() {
        return dateLog;
    }

    public void setDateLog(String dateLog) {
        this.dateLog = dateLog;
    }

    public String getTimeLog() {
        return timeLog;
    }

    public void setTimeLog(String timeLog) {
        this.timeLog = timeLog;
    }
}

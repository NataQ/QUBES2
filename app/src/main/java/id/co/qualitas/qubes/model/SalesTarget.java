package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/25/2016.
 */
public class SalesTarget {
    private String time;
    private String target;
    private String reality;

    public SalesTarget(String time, String target, String reality) {
        this.time = time;
        this.target = target;
        this.reality = reality;
    }

    public String getReality() {
        return reality;
    }

    public void setReality(String reality) {
        this.reality = reality;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

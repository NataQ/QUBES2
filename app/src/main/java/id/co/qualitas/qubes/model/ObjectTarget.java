package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/22/2016.
 */
public class ObjectTarget {
    private String time;
    private String code;
    private String name;
    private String target;
    private String reality;

    public ObjectTarget(String time, String target, String reality) {
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

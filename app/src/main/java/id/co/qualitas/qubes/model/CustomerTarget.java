package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/22/2016.
 */
public class CustomerTarget {
    private String time;
    private String code;
    private String name;
    private String target;
    private String reality;

    public CustomerTarget(String time, String code, String name, String target, String reality) {
        this.time = time;
        this.code = code;
        this.name = name;
        this.target = target;
        this.reality = reality;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

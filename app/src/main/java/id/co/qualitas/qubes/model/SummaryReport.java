package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/28/2016.
 */
public class SummaryReport {
    private String title;
    private String target;
    private String actual;
    private String balance;

    public SummaryReport(String title, String target, String actual, String balance) {
        this.title = title;
        this.target = target;
        this.actual = actual;
        this.balance = balance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}

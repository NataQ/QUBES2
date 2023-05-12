package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 4/28/2016.
 */
public class CreditInfo {
    private String no;
    private String tgl;
    private String jml;

    public CreditInfo(String no, String tgl, String jml) {
        this.no = no;
        this.tgl = tgl;
        this.jml = jml;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getJml() {
        return jml;
    }

    public void setJml(String jml) {
        this.jml = jml;
    }
}

package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 10/31/2016.
 */

public class ReturnHeader {
    private int idHeader;
    private String noBrb;
    private String tglBRB;
    private String noReturRep;
    private String tglRR;
    private String category;

    public ReturnHeader() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(int idHeader) {
        this.idHeader = idHeader;
    }

    public String getNoBrb() {
        return noBrb;
    }

    public void setNoBrb(String noBrb) {
        this.noBrb = noBrb;
    }

    public String getNoReturRep() {
        return noReturRep;
    }

    public void setNoReturRep(String noReturRep) {
        this.noReturRep = noReturRep;
    }

    public String getTglBRB() {
        return tglBRB;
    }

    public void setTglBRB(String tglBRB) {
        this.tglBRB = tglBRB;
    }

    public String getTglRR() {
        return tglRR;
    }

    public void setTglRR(String tglRR) {
        this.tglRR = tglRR;
    }
}

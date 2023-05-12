package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 11/9/2016.
 */

public class Offset {
    private int cbOffset;
    private String offsetType;
    private String amountPaid;

    public Offset(String offsetType) {
        this.offsetType = offsetType;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getOffsetType() {
        return offsetType;
    }

    public void setOffsetType(String offsetType) {
        this.offsetType = offsetType;
    }

    public int getCbOffset() {
        return cbOffset;
    }

    public void setCbOffset(int cbOffset) {
        this.cbOffset = cbOffset;
    }
}

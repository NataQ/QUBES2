package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 10/4/2016.
 */

public class CollectTransfer {
    private String txtNoInvoice;
    private String txtAmount;
    private boolean cbCash;

    public CollectTransfer(String txtNoInvoice, String txtAmount, boolean cbCash) {
        this.txtNoInvoice = txtNoInvoice;
        this.txtAmount = txtAmount;
        this.cbCash = cbCash;
    }

    public String getTxtNoInvoice() {
        return txtNoInvoice;
    }

    public void setTxtNoInvoice(String txtNoInvoice) {
        this.txtNoInvoice = txtNoInvoice;
    }

    public String getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(String txtAmount) {
        this.txtAmount = txtAmount;
    }

    public boolean getCbCash() {
        return cbCash;
    }

    public void setCbCash(boolean cbCash) {
        this.cbCash = cbCash;
    }
}

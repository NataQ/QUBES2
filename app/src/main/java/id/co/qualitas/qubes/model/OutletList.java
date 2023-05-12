package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 8/9/2016.
 */

public class OutletList {
    private String namaOutlet;
    private String kodeOutlet;
    private String outlet;

    public OutletList(String namaOutlet, String kodeOutlet) {
        this.namaOutlet = namaOutlet;
        this.kodeOutlet = kodeOutlet;
    }

    public String getKodeOutlet() {
        return kodeOutlet;
    }

    public void setKodeOutlet(String kodeOutlet) {
        this.kodeOutlet = kodeOutlet;
    }

    public String getNamaOutlet() {
        return namaOutlet;
    }

    public void setNamaOutlet(String namaOutlet) {
        this.namaOutlet = namaOutlet;
    }

    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }
}

package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Natalia on 2/6/2017.
 */
public class JenisJualandTop implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("jenisJual")
    private String jenisJual;

    @SerializedName("topf")
    private int topf;

    @SerializedName("topk")
    private int topk;

    @SerializedName("top_sap")
    private String top_sap;

    @SerializedName("uomPrice")
    private List<Uom> uomPrice;

    @SerializedName("top")
    private String top;

    private String klasifikasi;

    @SerializedName("name")
    private String jenisJualName;

    private String idOutlet;

    public String getIdOutlet() {
        return idOutlet;
    }

    public void setIdOutlet(String idOutlet) {
        this.idOutlet = idOutlet;
    }

    public String getJenisJualName() {
        return jenisJualName;
    }

    public void setJenisJualName(String jenisJualName) {
        this.jenisJualName = jenisJualName;
    }

    public String getKlasifikasi() {
        return klasifikasi;
    }

    public void setKlasifikasi(String klasifikasi) {
        this.klasifikasi = klasifikasi;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getTop_sap() {
        return top_sap;
    }

    public void setTop_sap(String top_sap) {
        this.top_sap = top_sap;
    }

    public List<Uom> getUomPrice() {
        return uomPrice;
    }

    public void setUomPrice(List<Uom> uomPrice) {
        this.uomPrice = uomPrice;
    }

    public String getJenisJual() {
        return jenisJual;
    }

    public void setJenisJual(String jenisJual) {
        this.jenisJual = jenisJual;
    }

    public int getTopf() {
        return topf;
    }

    public void setTopf(int topf) {
        this.topf = topf;
    }

    public int getTopk() {
        return topk;
    }

    public void setTopk(int topk) {
        this.topk = topk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

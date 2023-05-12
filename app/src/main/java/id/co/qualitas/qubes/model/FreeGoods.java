package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**em
 * Created by Wiliam on 11/15/2017.
 */

public class FreeGoods implements Serializable{

    private String jenisJual;
    private String klasifikasi;
    private String topF;
    private BigDecimal tax;
    private ArrayList<OptionFreeGoods>[] listOptionFreeGoods;
    private ArrayList<ArrayList<OptionFreeGoods>> ArraylistOptionFG;
    private ArrayList<OptionFreeGoods> listSelectedOptionFreeGoods;
    private String selectedFreeFoods;
    private int indexSel;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ArrayList<OptionFreeGoods>> getArraylistOptionFG() {
        return ArraylistOptionFG;
    }

    public void setArraylistOptionFG(ArrayList<ArrayList<OptionFreeGoods>> arraylistOptionFG) {
        ArraylistOptionFG = arraylistOptionFG;
    }

    public int getIndexSel() {
        return indexSel;
    }

    public void setIndexSel(int indexSel) {
        this.indexSel = indexSel;
    }

    public String getTopF() {
        return topF;
    }

    public void setTopF(String topF) {
        this.topF = topF;
    }

    public ArrayList<OptionFreeGoods> getListSelectedOptionFreeGoods() {
        return listSelectedOptionFreeGoods;
    }

    public void setListSelectedOptionFreeGoods(ArrayList<OptionFreeGoods> listSelectedOptionFreeGoods) {
        this.listSelectedOptionFreeGoods = listSelectedOptionFreeGoods;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getSelectedFreeFoods() {
        return selectedFreeFoods;
    }

    public void setSelectedFreeFoods(String selectedFreeFoods) {
        this.selectedFreeFoods = selectedFreeFoods;
    }

    public String getJenisJual() {
        return jenisJual;
    }

    public void setJenisJual(String jenisJual) {
        this.jenisJual = jenisJual;
    }

    public String getKlasifikasi() {
        return klasifikasi;
    }

    public void setKlasifikasi(String klasifikasi) {
        this.klasifikasi = klasifikasi;
    }

    public ArrayList<OptionFreeGoods>[] getListOptionFreeGoods() {
        return listOptionFreeGoods;
    }

    public void setListOptionFreeGoods(ArrayList<OptionFreeGoods>[] listOptionFreeGoods) {
        this.listOptionFreeGoods = listOptionFreeGoods;
    }
}
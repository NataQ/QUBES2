package id.co.qualitas.qubes.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Wiliam on 6/2/2017.
 */

public class ToPrice implements Serializable {

    private String cond_type;
    private BigDecimal amount;

    private String id;
    private String item_nr;
    private String table_name;
    private Boolean one_time_discount;
    private int step;
    private BigDecimal disc_value;
    private String doc_no;
    private String kc_no;

    private String discValueString;

    private String id_cond_type;

    public String getId_cond_type() {
        return id_cond_type;
    }

    public void setId_cond_type(String id_cond_type) {
        this.id_cond_type = id_cond_type;
    }

    public String getDiscValueString() {
        return discValueString;
    }

    public void setDiscValueString(String discValueString) {
        this.discValueString = discValueString;
    }

    public String getDoc_no() {
        return doc_no;
    }

    public void setDoc_no(String doc_no) {
        this.doc_no = doc_no;
    }

    public String getKc_no() {
        return kc_no;
    }

    public void setKc_no(String kc_no) {
        this.kc_no = kc_no;
    }

    public BigDecimal getDisc_value() {
        return disc_value;
    }

    public void setDisc_value(BigDecimal disc_value) {
        this.disc_value = disc_value;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_nr() {
        return item_nr;
    }

    public void setItem_nr(String item_nr) {
        this.item_nr = item_nr;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public Boolean getOne_time_discount() {
        return one_time_discount;
    }

    public void setOne_time_discount(Boolean one_time_discount) {
        this.one_time_discount = one_time_discount;
    }

    public String getCond_type() {
        return cond_type;
    }

    public void setCond_type(String cond_type) {
        this.cond_type = cond_type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

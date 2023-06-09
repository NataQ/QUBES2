package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Wiliam on 6/13/2017.
 */

public class Discount implements Serializable {

    private String discountName;
    private String prodName;
    private String prodId;
    private String value;
    private String salesDocTy;
    private String scale;
    private String minPScale; //minimnum pembelian
    private String pmnsTerms;
    private String perUom;
    private String validFromTo;
    private String status;
    private String outletName;

    //discountName a
    private String cond_type;
    private String name;

    //value nominal a
    private String price;

    //per uom a
    private BigDecimal per;
    private String uom;

    //valid from to a
    private String valid_from;
    private String valid_to;

    //scale a
    private String id_scale;

    //count one time discount a
    private String count;

//    //salesOrganization
//    private String salesOrganization;
//
//    //distribution channel
//    private String distributionChannel;

    //plant
    private String idPlant;
    private String plant;

    //jenis account
    private String customerGroup;

    //segmen
    private String classification;

    //sales a
    private String salesGroup;
    private String salesDocumentType;

    //idMaterial a
    private String idMaterial;
    private String material;

    //pmntTerms a
    private int PaymentTerms;

    private String idMaterialGroup3;
    private String materialGroup3;

    //outletName a
    private String idCustomer;
    private String customer;

    private String pers;

    @SerializedName("doc_no")
    private String docNumber;

    @SerializedName("kc_no")
    private String kcNumber;

    private String product;

    public Discount(String name, String cond_type,
                    String idCustomer, String customer,
                    String material, String idMaterial,
                    String price,
                    String salesDocumentType,
                    String pers,
                    String paymentTerms,
                    String uom,
                    String valid_from, String valid_to,
                    String count,
                    String idPlant, String plant,
                    String idMaterialGroup3, String materialGroup3) {
        this.name = name;
        this.cond_type = cond_type;

        this.idCustomer = idCustomer;
        this.customer = customer;

        this.material = material;
        this.idMaterial = idMaterial;

        this.price = price;

        this.salesDocumentType = salesDocumentType;

        this.pers = pers;

        this.pmnsTerms = paymentTerms;

        this.uom = uom;

        this.valid_from = valid_from;
        this.valid_to = valid_to;

        this.count = count;//Status

        this.idPlant = idPlant;
        this.plant = plant;

        this.idMaterialGroup3 = idMaterialGroup3;
        this.materialGroup3 = materialGroup3;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getKcNumber() {
        return kcNumber;
    }

    public void setKcNumber(String kcNumber) {
        this.kcNumber = kcNumber;
    }

    public String getPers() {
        return pers;
    }

    public void setPers(String pers) {
        this.pers = pers;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSalesDocTy() {
        return salesDocTy;
    }

    public void setSalesDocTy(String salesDocTy) {
        this.salesDocTy = salesDocTy;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getMinPScale() {
        return minPScale;
    }

    public void setMinPScale(String minPScale) {
        this.minPScale = minPScale;
    }

    public String getPmnsTerms() {
        return pmnsTerms;
    }

    public void setPmnsTerms(String pmnsTerms) {
        this.pmnsTerms = pmnsTerms;
    }

    public String getPerUom() {
        return perUom;
    }

    public void setPerUom(String perUom) {
        this.perUom = perUom;
    }

    public String getValidFromTo() {
        return validFromTo;
    }

    public void setValidFromTo(String validFromTo) {
        this.validFromTo = validFromTo;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

//new


    public String getCond_type() {
        return cond_type;
    }

    public void setCond_type(String cond_type) {
        this.cond_type = cond_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BigDecimal getPer() {
        return per;
    }

    public void setPer(BigDecimal per) {
        this.per = per;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    public String getId_scale() {
        return id_scale;
    }

    public void setId_scale(String id_scale) {
        this.id_scale = id_scale;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setSalesOrganization(String salesOrganization) {
        salesOrganization = salesOrganization;
    }

    public void setDistributionChannel(String distributionChannel) {
        distributionChannel = distributionChannel;
    }

    public String getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(String idPlant) {
        this.idPlant = idPlant;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        plant = plant;
    }

    public String getSalesGroup() {
        return salesGroup;
    }

    public void setSalesGroup(String salesGroup) {
        salesGroup = salesGroup;
    }

    public String getSalesDocumentType() {
        return salesDocumentType;
    }

    public void setSalesDocumentType(String salesDocumentType) {
        salesDocumentType = salesDocumentType;
    }

    public String getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(String idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        material = material;
    }

    public int getPaymentTerms() {
        return PaymentTerms;
    }

    public void setPaymentTerms(int paymentTerms) {
        PaymentTerms = paymentTerms;
    }

    public String getIdMaterialGroup3() {
        return idMaterialGroup3;
    }

    public void setIdMaterialGroup3(String idMaterialGroup3) {
        this.idMaterialGroup3 = idMaterialGroup3;
    }

    public String getMaterialGroup3() {
        return materialGroup3;
    }

    public void setMaterialGroup3(String materialGroup3) {
        materialGroup3 = materialGroup3;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        customer = customer;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        customerGroup = customerGroup;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        classification = classification;
    }
}

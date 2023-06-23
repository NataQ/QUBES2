package id.co.qualitas.qubes.model;

public class Promotion {
    private String promotionName;
    private String promotionDecs;
    private String promotionPeriode;

    public Promotion(String promotionName, String promotionPeriode) {
        this.promotionName = promotionName;
        this.promotionPeriode = promotionPeriode;
    }

    public String getPromotionPeriode() {
        return promotionPeriode;
    }

    public void setPromotionPeriode(String promotionPeriode) {
        this.promotionPeriode = promotionPeriode;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionDecs() {
        return promotionDecs;
    }

    public void setPromotionDecs(String promotionDecs) {
        this.promotionDecs = promotionDecs;
    }
}

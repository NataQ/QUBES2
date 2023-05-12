package id.co.qualitas.qubes.model;

public class Promotion {
    private String promotionName;
    private String promotionDecs;

    public Promotion(String promotionName, String promotionDecs) {
        this.promotionName = promotionName;
        this.promotionDecs = promotionDecs;
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

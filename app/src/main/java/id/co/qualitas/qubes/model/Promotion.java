package id.co.qualitas.qubes.model;

public class Promotion {
    private String promotionId;
    private String promotionName;
    private String promotionDecs;
    private String promotionPeriode;
    private String promotionValidFrom;
    private String promotionValidTo;
    private boolean isSync;

    public Promotion(String promotionName) {
        this.promotionName = promotionName;
    }

    public Promotion(String promotionName, String promotionPeriode) {
        this.promotionName = promotionName;
        this.promotionPeriode = promotionPeriode;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionValidFrom() {
        return promotionValidFrom;
    }

    public void setPromotionValidFrom(String promotionValidFrom) {
        this.promotionValidFrom = promotionValidFrom;
    }

    public String getPromotionValidTo() {
        return promotionValidTo;
    }

    public void setPromotionValidTo(String promotionValidTo) {
        this.promotionValidTo = promotionValidTo;
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

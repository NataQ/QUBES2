package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by WIlliam-QAS on 04/04/2017.
 */

public class OrderPlanDetailResponse implements Serializable{

  @SerializedName("detail")
  private MaterialResponse[] materialResponses;
  private BigDecimal achievement;
  private BigDecimal targetBulanan;

  public MaterialResponse[] getMaterialResponses() {
    return materialResponses;
  }

  public void setMaterialResponses(
      MaterialResponse[] materialResponses) {
    this.materialResponses = materialResponses;
  }

  public BigDecimal getAchievement() {
    return achievement;
  }

  public void setAchievement(BigDecimal achievement) {
    this.achievement = achievement;
  }

  public BigDecimal getTargetBulanan() {
    return targetBulanan;
  }

  public void setTargetBulanan(BigDecimal targetBulanan) {
    this.targetBulanan = targetBulanan;
  }
}

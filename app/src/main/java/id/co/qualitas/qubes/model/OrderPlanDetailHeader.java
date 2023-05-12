package id.co.qualitas.qubes.model;

/**
 * Created by WIlliam-QAS on 04/04/2017.
 */

public class OrderPlanDetailHeader {
  private String targetBulan;
  private String targetCall;
  private String sisaTargeCall;
  private String plan;
  private String achievement;

  public String getTargetBulan() {
    return targetBulan;
  }

  public void setTargetBulan(String targetBulan) {
    this.targetBulan = targetBulan;
  }

  public String getTargetCall() {
    return targetCall;
  }

  public void setTargetCall(String targetCall) {
    this.targetCall = targetCall;
  }

  public String getSisaTargeCall() {
    return sisaTargeCall;
  }

  public void setSisaTargeCall(String sisaTargeCall) {
    this.sisaTargeCall = sisaTargeCall;
  }

  public String getPlan() {
    return plan;
  }

  public void setPlan(String plan) {
    this.plan = plan;
  }

  public String getAchievement() {
    return achievement;
  }

  public void setAchievement(String achievement) {
    this.achievement = achievement;
  }
}

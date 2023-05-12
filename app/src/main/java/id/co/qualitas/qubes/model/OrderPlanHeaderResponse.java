package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Evan on 5/10/17.
 */

public class OrderPlanHeaderResponse implements Serializable {

    @SerializedName("listOrderPlan")
    private List<OrderPlanHeader> listOrderPlan;

    @SerializedName("totalPrice")
    private BigDecimal totalPrice;

    public List<OrderPlanHeader> getListOrderPlan() {
        return listOrderPlan;
    }

    public void setListOrderPlan(List<OrderPlanHeader> listOrderPlan) {
        this.listOrderPlan = listOrderPlan;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}

package id.co.qualitas.qubes.model;

import java.util.List;

public class VisitOrderRequest {
    private VisitOrderHeader orderHeader;
    private List<VisitOrderDetailResponse> orderDetail;
    private List<ToPrice> listOneTimeDiscount;
//    private ArrayList<List<FreeGoods>> listFreeGoods;
    private FreeGoods[] listFreeGoods;

    public FreeGoods[] getListFreeGoods() {
        return listFreeGoods;
    }

    public void setListFreeGoods(FreeGoods[] listFreeGoods) {
        this.listFreeGoods = listFreeGoods;
    }

    public List<ToPrice> getListOneTimeDiscount() {
        return listOneTimeDiscount;
    }

    public void setListOneTimeDiscount(List<ToPrice> listOneTimeDiscount) {
        this.listOneTimeDiscount = listOneTimeDiscount;
    }

    public VisitOrderHeader getOrderHeader() {
        return orderHeader;
    }

    public void setOrderHeader(VisitOrderHeader orderHeader) {
        this.orderHeader = orderHeader;
    }

    public List<VisitOrderDetailResponse> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<VisitOrderDetailResponse> orderDetail) {
        this.orderDetail = orderDetail;
    }
}

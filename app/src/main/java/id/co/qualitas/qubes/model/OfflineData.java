package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class OfflineData implements Serializable{
    List<VisitSalesman> visitSalesmanList;
    List<Customer> nooList;
    List<CollectionHeader> collectionList;
    List<Order> orderList;
    List<Map> photoList;
    List<Map> storeCheckList;
    List<Map> returnList;

    public List<VisitSalesman> getVisitSalesmanList() {
        return visitSalesmanList;
    }

    public void setVisitSalesmanList(List<VisitSalesman> visitSalesmanList) {
        this.visitSalesmanList = visitSalesmanList;
    }

    public List<Customer> getNooList() {
        return nooList;
    }

    public void setNooList(List<Customer> nooList) {
        this.nooList = nooList;
    }

    public List<CollectionHeader> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<CollectionHeader> collectionList) {
        this.collectionList = collectionList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<Map> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Map> photoList) {
        this.photoList = photoList;
    }

    public List<Map> getStoreCheckList() {
        return storeCheckList;
    }

    public void setStoreCheckList(List<Map> storeCheckList) {
        this.storeCheckList = storeCheckList;
    }

    public List<Map> getReturnList() {
        return returnList;
    }

    public void setReturnList(List<Map> returnList) {
        this.returnList = returnList;
    }
}

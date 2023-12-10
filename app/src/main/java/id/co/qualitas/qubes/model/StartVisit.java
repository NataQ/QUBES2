package id.co.qualitas.qubes.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StartVisit implements Serializable {
    private int id;
    private String id_salesman;
    private String date;
    private String start_time;
    private String end_time;
    private String km_awal;
    private String photo_km_awal;
    private String km_akhir;
    private String photo_km_akhir;
    private String photo_complete;
    private boolean endDay;
    private boolean startDay;

    public boolean isStartDay() {
        return startDay;
    }

    public void setStartDay(boolean startDay) {
        this.startDay = startDay;
    }

    public boolean isEndDay() {
        return endDay;
    }

    public void setEndDay(boolean endDay) {
        this.endDay = endDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_salesman() {
        return id_salesman;
    }

    public void setId_salesman(String id_salesman) {
        this.id_salesman = id_salesman;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getKm_awal() {
        return km_awal;
    }

    public void setKm_awal(String km_awal) {
        this.km_awal = km_awal;
    }

    public String getPhoto_km_awal() {
        return photo_km_awal;
    }

    public void setPhoto_km_awal(String photo_km_awal) {
        this.photo_km_awal = photo_km_awal;
    }

    public String getKm_akhir() {
        return km_akhir;
    }

    public void setKm_akhir(String km_akhir) {
        this.km_akhir = km_akhir;
    }

    public String getPhoto_km_akhir() {
        return photo_km_akhir;
    }

    public void setPhoto_km_akhir(String photo_km_akhir) {
        this.photo_km_akhir = photo_km_akhir;
    }

    public String getPhoto_complete() {
        return photo_complete;
    }

    public void setPhoto_complete(String photo_complete) {
        this.photo_complete = photo_complete;
    }
}

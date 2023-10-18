package id.co.qualitas.qubes.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Customer {
    private String idHeader;
    private String id;
    private String nama;
    private String address;
    private String phone;
    private String udf_5;
    private String udf_5_desc;
    private double sisaCreditLimit;
    private double limit_kredit;
    private double totalTagihan;
    private String nama_pemilik;
    private String nik;
    private String no_npwp;
    private int status;
    private boolean route;
    private double latitude;
    private double longitude;
    private LatLng position;
    private String photoKtp;
    private String photoNpwp;
    private String photoOutlet;
    private String rute;
    private String kode_pos;
    private String kota;
    private String mileage;
    private String type_customer;
    private String name_type_customer;
    private String type_price;
    private int is_sync;
    private List<Promotion> promoList;

    public Customer() {
    }

    public Customer(String id, String nama, String nama_pemilik, String address, String phone, double limit_kredit, String nik, String no_npwp, boolean route, double latitude, double longitude, int isSync) {
        this.id = id;
        this.nama_pemilik = nama_pemilik;
        this.nama = nama;
        this.address = address;
        this.phone = phone;
        this.limit_kredit = limit_kredit;
        this.nik = nik;
        this.no_npwp = no_npwp;
        this.route = route;
        this.latitude = latitude;
        this.latitude = longitude;
        this.is_sync = isSync;
    }

    public Customer(String id, String nama, String address, boolean route, double latitude, double longitude) {
        this.id = id;
        this.nama = nama;
        this.address = address;
        this.route = route;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Customer(String id, String nama, String address, boolean route, LatLng position) {
        this.id = id;
        this.nama = nama;
        this.address = address;
        this.route = route;
        this.position = position;
    }
    public Customer(String id, String nama, String address, boolean route, LatLng position, int status) {
        this.id = id;
        this.nama = nama;
        this.address = address;
        this.route = route;
        this.position = position;
        this.status = status;
    }

    public String getName_type_customer() {
        return name_type_customer;
    }

    public void setName_type_customer(String name_type_customer) {
        this.name_type_customer = name_type_customer;
    }

    public String getRute() {
        return rute;
    }

    public void setRute(String rute) {
        this.rute = rute;
    }

    public String getKode_pos() {
        return kode_pos;
    }

    public void setKode_pos(String kode_pos) {
        this.kode_pos = kode_pos;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getType_customer() {
        return type_customer;
    }

    public void setType_customer(String type_customer) {
        this.type_customer = type_customer;
    }

    public String getType_price() {
        return type_price;
    }

    public void setType_price(String type_price) {
        this.type_price = type_price;
    }

    public List<Promotion> getPromoList() {
        return promoList;
    }

    public void setPromoList(List<Promotion> promoList) {
        this.promoList = promoList;
    }

    public String getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(String idHeader) {
        this.idHeader = idHeader;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUdf_5() {
        return udf_5;
    }

    public void setUdf_5(String udf_5) {
        this.udf_5 = udf_5;
    }

    public String getUdf_5_desc() {
        return udf_5_desc;
    }

    public void setUdf_5_desc(String udf_5_desc) {
        this.udf_5_desc = udf_5_desc;
    }

    public double getSisaCreditLimit() {
        return sisaCreditLimit;
    }

    public void setSisaCreditLimit(double sisaCreditLimit) {
        this.sisaCreditLimit = sisaCreditLimit;
    }

    public double getLimit_kredit() {
        return limit_kredit;
    }

    public void setLimit_kredit(double limit_kredit) {
        this.limit_kredit = limit_kredit;
    }

    public double getTotalTagihan() {
        return totalTagihan;
    }

    public void setTotalTagihan(double totalTagihan) {
        this.totalTagihan = totalTagihan;
    }

    public String getNama_pemilik() {
        return nama_pemilik;
    }

    public void setNama_pemilik(String nama_pemilik) {
        this.nama_pemilik = nama_pemilik;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNo_npwp() {
        return no_npwp;
    }

    public void setNo_npwp(String no_npwp) {
        this.no_npwp = no_npwp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isRoute() {
        return route;
    }

    public void setRoute(boolean route) {
        this.route = route;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getPhotoKtp() {
        return photoKtp;
    }

    public void setPhotoKtp(String photoKtp) {
        this.photoKtp = photoKtp;
    }

    public String getPhotoNpwp() {
        return photoNpwp;
    }

    public void setPhotoNpwp(String photoNpwp) {
        this.photoNpwp = photoNpwp;
    }

    public String getPhotoOutlet() {
        return photoOutlet;
    }

    public void setPhotoOutlet(String photoOutlet) {
        this.photoOutlet = photoOutlet;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }
}

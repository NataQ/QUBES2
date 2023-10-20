package id.co.qualitas.qubes.model;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Customer {
    private String idHeader;
    private String id;
    private String nama;
    private String address;
    private String no_tlp;
    private String udf_5;
    private String udf_5_desc;
    private double latitude;
    private double longitude;
    private int status; //0-> no status, 1 -> check in, 2 -> pause, 3-> check out
    private String kode_pos;
    private String idKelurahan;
    private String kelurahan;
    private String idKecamatan;
    private String kecamatan;
    private String idKota;
    private String kota;
    private String idProvinsi;
    private String provinsi;
    private String no_npwp;
    private String npwp_name;
    private String npwp_address;
    private String status_npwp;
    private String nama_pemilik;
    private String alamat_pemilik;
    private String nik;
    private String status_toko;
    private String location;
    private String jenis_usaha;
    private String lama_usaha;
    private String suku;
    private String type_customer;
    private String name_type_customer;
    private String type_price;
    private double credit_limit;
    private String rute;
    private String photoKtp;
    private String photoNpwp;
    private String photoOutlet;
    private int is_sync;
    private List<Promotion> promoList;
    private boolean route;
    private LatLng position;
    private double limit_kredit;
    private double sisaCreditLimit;
    private double totalTagihan;
    private String mileage;
//    private Uri photoKTPUri;
//    private Uri photoNPWPUri;
//    private Uri photoOutletUri;

    public Customer() {
    }

    public Customer(String id, String nama, String nama_pemilik, String address, String no_tlp, double limit_kredit, String nik, String no_npwp, boolean route, double latitude, double longitude, int isSync) {
        this.id = id;
        this.nama_pemilik = nama_pemilik;
        this.nama = nama;
        this.address = address;
        this.no_tlp = no_tlp;
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

//    public Uri getPhotoKTPUri() {
//        return photoKTPUri;
//    }
//
//    public void setPhotoKTPUri(Uri photoKTPUri) {
//        this.photoKTPUri = photoKTPUri;
//    }
//
//    public Uri getPhotoNPWPUri() {
//        return photoNPWPUri;
//    }
//
//    public void setPhotoNPWPUri(Uri photoNPWPUri) {
//        this.photoNPWPUri = photoNPWPUri;
//    }
//
//    public Uri getPhotoOutletUri() {
//        return photoOutletUri;
//    }
//
//    public void setPhotoOutletUri(Uri photoOutletUri) {
//        this.photoOutletUri = photoOutletUri;
//    }

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

    public String getNo_tlp() {
        return no_tlp;
    }

    public void setNo_tlp(String no_tlp) {
        this.no_tlp = no_tlp;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getKode_pos() {
        return kode_pos;
    }

    public void setKode_pos(String kode_pos) {
        this.kode_pos = kode_pos;
    }

    public String getIdKelurahan() {
        return idKelurahan;
    }

    public void setIdKelurahan(String idKelurahan) {
        this.idKelurahan = idKelurahan;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getIdKecamatan() {
        return idKecamatan;
    }

    public void setIdKecamatan(String idKecamatan) {
        this.idKecamatan = idKecamatan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getIdKota() {
        return idKota;
    }

    public void setIdKota(String idKota) {
        this.idKota = idKota;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getIdProvinsi() {
        return idProvinsi;
    }

    public void setIdProvinsi(String idProvinsi) {
        this.idProvinsi = idProvinsi;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getNo_npwp() {
        return no_npwp;
    }

    public void setNo_npwp(String no_npwp) {
        this.no_npwp = no_npwp;
    }

    public String getNpwp_name() {
        return npwp_name;
    }

    public void setNpwp_name(String npwp_name) {
        this.npwp_name = npwp_name;
    }

    public String getNpwp_address() {
        return npwp_address;
    }

    public void setNpwp_address(String npwp_address) {
        this.npwp_address = npwp_address;
    }

    public String getStatus_npwp() {
        return status_npwp;
    }

    public void setStatus_npwp(String status_npwp) {
        this.status_npwp = status_npwp;
    }

    public String getNama_pemilik() {
        return nama_pemilik;
    }

    public void setNama_pemilik(String nama_pemilik) {
        this.nama_pemilik = nama_pemilik;
    }

    public String getAlamat_pemilik() {
        return alamat_pemilik;
    }

    public void setAlamat_pemilik(String alamat_pemilik) {
        this.alamat_pemilik = alamat_pemilik;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getStatus_toko() {
        return status_toko;
    }

    public void setStatus_toko(String status_toko) {
        this.status_toko = status_toko;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJenis_usaha() {
        return jenis_usaha;
    }

    public void setJenis_usaha(String jenis_usaha) {
        this.jenis_usaha = jenis_usaha;
    }

    public String getLama_usaha() {
        return lama_usaha;
    }

    public void setLama_usaha(String lama_usaha) {
        this.lama_usaha = lama_usaha;
    }

    public String getSuku() {
        return suku;
    }

    public void setSuku(String suku) {
        this.suku = suku;
    }

    public String getType_customer() {
        return type_customer;
    }

    public void setType_customer(String type_customer) {
        this.type_customer = type_customer;
    }

    public String getName_type_customer() {
        return name_type_customer;
    }

    public void setName_type_customer(String name_type_customer) {
        this.name_type_customer = name_type_customer;
    }

    public String getType_price() {
        return type_price;
    }

    public void setType_price(String type_price) {
        this.type_price = type_price;
    }

    public double getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(double credit_limit) {
        this.credit_limit = credit_limit;
    }

    public String getRute() {
        return rute;
    }

    public void setRute(String rute) {
        this.rute = rute;
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

    public List<Promotion> getPromoList() {
        return promoList;
    }

    public void setPromoList(List<Promotion> promoList) {
        this.promoList = promoList;
    }

    public boolean isRoute() {
        return route;
    }

    public void setRoute(boolean route) {
        this.route = route;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public double getLimit_kredit() {
        return limit_kredit;
    }

    public void setLimit_kredit(double limit_kredit) {
        this.limit_kredit = limit_kredit;
    }

    public double getSisaCreditLimit() {
        return sisaCreditLimit;
    }

    public void setSisaCreditLimit(double sisaCreditLimit) {
        this.sisaCreditLimit = sisaCreditLimit;
    }

    public double getTotalTagihan() {
        return totalTagihan;
    }

    public void setTotalTagihan(double totalTagihan) {
        this.totalTagihan = totalTagihan;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }
}

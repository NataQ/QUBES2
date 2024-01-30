package id.co.qualitas.qubes.model;

import java.util.List;

public class Customer {
    private String idHeader;
    private String id;
    private String nama;
    private String address;
    private String no_tlp;
    private String udf_5;
    private String top_khusus;
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
    private String no_npwp1;
    private String no_npwp2;
    private String no_npwp3;
    private String no_npwp4;
    private String npwp_name;
    private String npwp_address;
    private String status_npwp;
    private int status_npwp_pos;
    private String nama_pemilik;
    private String alamat_pemilik;
    private String nik;
    private String nik1;
    private String nik2;
    private String nik3;
    private String nik4;
    private String status_toko;
    private int status_toko_pos;
    private String location;
    private String jenis_usaha;
    private String lama_usaha;
    private String suku;
    private int day_pos;
    private String day;
    private String week;
    private int kelas_outlet_pos;
    private String kelas_outlet;
    private int suku_pos;
    private String type_customer;
    private String name_type_customer;
    private String type_price;
    private String rute;
    private String photoKtp;
    private String photoNpwp;
    private String photoOutlet;
    private int isSync;
    private List<Promotion> promoList;
    private List<Material> dctList;
    private List<GroupMaxBon> maxBonList;
    private List<Uom> dropSizeList;
    private boolean route;
//    private LatLng position;
    private double limit_kredit;
    private double sisaCreditLimit;
    private double totalTagihan;
    private double totalFaktur;
    private boolean noo;
    private String mileage;
    private int is_route;
    private int idReason;
    private String nameReason;
    private String createdDate;
    private int posReason;
    private String order_type;
    private String route_order;
    private boolean w1;
    private boolean w2;
    private boolean w3;
    private boolean w4;
//    private Uri photoKTPUri;
//    private Uri photoNPWPUri;
//    private Uri photoOutletUri;

    public Customer() {
    }

    public Customer(String id, String nama, String address, boolean route, double latitude, double longitude) {
        this.id = id;
        this.nama = nama;
        this.address = address;
        this.route = route;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getIs_route() {
        return is_route;
    }

    public void setIs_route(int is_route) {
        this.is_route = is_route;
    }

    public List<GroupMaxBon> getMaxBonList() {
        return maxBonList;
    }

    public void setMaxBonList(List<GroupMaxBon> maxBonList) {
        this.maxBonList = maxBonList;
    }

    public List<Uom> getDropSizeList() {
        return dropSizeList;
    }

    public void setDropSizeList(List<Uom> dropSizeList) {
        this.dropSizeList = dropSizeList;
    }

    public boolean isW1() {
        return w1;
    }

    public void setW1(boolean w1) {
        this.w1 = w1;
    }

    public boolean isW2() {
        return w2;
    }

    public void setW2(boolean w2) {
        this.w2 = w2;
    }

    public boolean isW3() {
        return w3;
    }

    public void setW3(boolean w3) {
        this.w3 = w3;
    }

    public boolean isW4() {
        return w4;
    }

    public void setW4(boolean w4) {
        this.w4 = w4;
    }

    public String getNo_npwp1() {
        return no_npwp1;
    }

    public void setNo_npwp1(String no_npwp1) {
        this.no_npwp1 = no_npwp1;
    }

    public String getNo_npwp2() {
        return no_npwp2;
    }

    public void setNo_npwp2(String no_npwp2) {
        this.no_npwp2 = no_npwp2;
    }

    public String getNo_npwp3() {
        return no_npwp3;
    }

    public void setNo_npwp3(String no_npwp3) {
        this.no_npwp3 = no_npwp3;
    }

    public String getNo_npwp4() {
        return no_npwp4;
    }

    public void setNo_npwp4(String no_npwp4) {
        this.no_npwp4 = no_npwp4;
    }

    public String getNik1() {
        return nik1;
    }

    public void setNik1(String nik1) {
        this.nik1 = nik1;
    }

    public String getNik2() {
        return nik2;
    }

    public void setNik2(String nik2) {
        this.nik2 = nik2;
    }

    public String getNik3() {
        return nik3;
    }

    public void setNik3(String nik3) {
        this.nik3 = nik3;
    }

    public String getNik4() {
        return nik4;
    }

    public void setNik4(String nik4) {
        this.nik4 = nik4;
    }

    public int getDay_pos() {
        return day_pos;
    }

    public void setDay_pos(int day_pos) {
        this.day_pos = day_pos;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getKelas_outlet_pos() {
        return kelas_outlet_pos;
    }

    public void setKelas_outlet_pos(int kelas_outlet_pos) {
        this.kelas_outlet_pos = kelas_outlet_pos;
    }

    public String getKelas_outlet() {
        return kelas_outlet;
    }

    public void setKelas_outlet(String kelas_outlet) {
        this.kelas_outlet = kelas_outlet;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getRoute_order() {
        return route_order;
    }

    public void setRoute_order(String route_order) {
        this.route_order = route_order;
    }

    public int getStatus_npwp_pos() {
        return status_npwp_pos;
    }

    public void setStatus_npwp_pos(int status_npwp_pos) {
        this.status_npwp_pos = status_npwp_pos;
    }

    public int getStatus_toko_pos() {
        return status_toko_pos;
    }

    public void setStatus_toko_pos(int status_toko_pos) {
        this.status_toko_pos = status_toko_pos;
    }

    public int getSuku_pos() {
        return suku_pos;
    }

    public void setSuku_pos(int suku_pos) {
        this.suku_pos = suku_pos;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getPosReason() {
        return posReason;
    }

    public void setPosReason(int posReason) {
        this.posReason = posReason;
    }

    public int getIdReason() {
        return idReason;
    }

    public void setIdReason(int idReason) {
        this.idReason = idReason;
    }

    public String getNameReason() {
        return nameReason;
    }

    public void setNameReason(String nameReason) {
        this.nameReason = nameReason;
    }

    public List<Material> getDctList() {
        return dctList;
    }

    public void setDctList(List<Material> dctList) {
        this.dctList = dctList;
    }

    public double getTotalFaktur() {
        return totalFaktur;
    }

    public void setTotalFaktur(double totalFaktur) {
        this.totalFaktur = totalFaktur;
    }

    public boolean isNoo() {
        return noo;
    }

    public void setNoo(boolean noo) {
        this.noo = noo;
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

    public String getTop_khusus() {
        return top_khusus;
    }

    public void setTop_khusus(String top_khusus) {
        this.top_khusus = top_khusus;
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

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
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

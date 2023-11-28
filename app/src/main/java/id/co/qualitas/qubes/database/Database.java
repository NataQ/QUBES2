package id.co.qualitas.qubes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Bank;
import id.co.qualitas.qubes.model.CollectionDetail;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.CustomerType;
import id.co.qualitas.qubes.model.DaerahTingkat;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.LogModel;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.Parameter;
import id.co.qualitas.qubes.model.PriceCode;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.SalesPriceDetail;
import id.co.qualitas.qubes.model.SalesPriceHeader;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.StoreCheck;
import id.co.qualitas.qubes.model.Uom;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class Database extends SQLiteOpenHelper {
    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    // static variable
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "Qubes";

    // table name
    private static final String TABLE_STOCK_REQUEST_HEADER = "StockRequestHeader";
    private static final String TABLE_STOCK_REQUEST_DETAIL = "StockRequestDetail";
    private static final String TABLE_INVOICE_HEADER = "InvoiceHeader";
    private static final String TABLE_INVOICE_DETAIL = "InvoiceDetail";
    private static final String TABLE_NOO = "NOO";
    private static final String TABLE_CUSTOMER = "Customer";
    private static final String TABLE_CUSTOMER_PROMOTION = "CustomerPromotion";
    private static final String TABLE_CUSTOMER_DCT = "CustomerDCT";
    private static final String TABLE_VISIT_SALESMAN = "VisitSalesman";
    private static final String TABLE_STORE_CHECK = "StoreCheck";
    private static final String TABLE_ORDER_HEADER = "OrderHeader";
    private static final String TABLE_ORDER_DETAIL = "OrderDetail";
    private static final String TABLE_ORDER_DETAIL_EXTRA = "OrderDetailExtra";
    private static final String TABLE_ORDER_DETAIL_DISCOUNT = "OrderDetailDiscount";
    private static final String TABLE_RETURN = "Return";
    private static final String TABLE_COLLECTION_HEADER = "CollectionHeader";
    private static final String TABLE_COLLECTION_DETAIL = "CollectionDetail";
    private static final String TABLE_COLLECTION_ITEM = "CollectionItem";
    private static final String TABLE_MASTER_REASON = "MasterReason";
    private static final String TABLE_MASTER_PROMOTION = "MasterPromotion";
    private static final String TABLE_MASTER_BANK = "MasterBank";
    private static final String TABLE_MASTER_MATERIAL = "MasterMaterial";
    private static final String TABLE_MASTER_UOM = "MasterUom";
    private static final String TABLE_MASTER_DAERAH_TINGKAT = "MasterDaerahTingkat";
    private static final String TABLE_MASTER_NON_ROUTE_CUSTOMER = "MasterNonRouteCustomer";
    private static final String TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION = "MasterNonRouteCustomerPromotion";
    private static final String TABLE_MASTER_NON_ROUTE_CUSTOMER_DCT = "MasterNonRouteCustomerDct";
    private static final String TABLE_MASTER_PRICE_CODE = "MasterTopPriceCode";
    private static final String TABLE_MASTER_LIMIT_BON = "MasterLimitBon";
    private static final String TABLE_MASTER_SALES_PRICE_HEADER = "MasterSalesPriceHeader";
    private static final String TABLE_MASTER_SALES_PRICE_DETAIL = "MasterSalesPriceDetail";
    private static final String TABLE_MASTER_PARAMETER = "MasterParameter";
    private static final String TABLE_MASTER_CUSTOMER_TYPE = "MasterCustomerType";
    private static final String TABLE_LOG = "Log";
    private static final String TABLE_PHOTO = "Photo";
    private static final String KEY_ID_CUSTOMER_TYPE = "idCustomerType";
    private static final String KEY_ID_TYPE_PRICE = "idTypePrice";
    private static final String KEY_NAME_TYPE_PRICE = "nameTypePrice";
    private static final String KEY_ID_MASTER_NON_ROUTE_CUSTOMER_PROMOTION_DB = "idMasterNonRouteCustomer";

    // column table parameter
    private static final String KEY_ID_PARAMETER_DB = "idParameterDB";
    private static final String KEY_KEY_PARAMETER = "keyParameter";
    private static final String KEY_VALUE = "value";
    private static final String KEY_ID_VISIT = "idVisit";
    private static final String KEY_DESC = "description";
    private static final String KEY_IS_SYNC_PHOTO = "isSyncPhoto";
    private static final String KEY_IS_SYNC_PHOTO_KTP = "isSyncPhotoKTP";
    private static final String KEY_IS_SYNC_PHOTO_OUTLET = "isSyncPhotoOutlet";
    private static final String KEY_IS_SYNC_PHOTO_NPWP = "isSyncPhotoNPWP";
    private static final String KEY_IS_SYNC_PHOTO_REASON_PAUSE = "isSyncPhotoPause";
    private static final String KEY_IS_SYNC_PHOTO_REASON_NOT_VISIT = "isSyncPhotoNotVisit";
    private static final String KEY_IS_SYNC_PHOTO_REASON_NOT_BUY = "isSyncPhotoNotBuy";

    // column table price code
    private static final String KEY_ID_PRICE_CODE_DB = "idPriceCodeDB";
    private static final String KEY_PRICE_LIST_CODE = "priceListCode";
    private static final String KEY_VALID_FROM = "validFrom";
    private static final String KEY_VALID_TO = "validTo";
    private static final String KEY_ID_MINIMAL_ORDER_DB = "idMinimalOrderDb";
    private static final String KEY_BON_LIMIT = "bonLimit";

    //MasterSalesPriceHeader
    private static final String KEY_ID_SALES_PRICE_HEADER_DB = "idSalesPriceHeaderDB";
    private static final String KEY_TOP = "top";

    //MasterSalesPriceDetail
    private static final String KEY_ID_SALES_PRICE_DETAIL_DB = "idSalesPriceDetailDB";
    private static final String SELLING_PRICE = "sellingPrice";

    // column table StockRequestHeader
    private static final String KEY_ID_STOCK_REQUEST_HEADER_DB = "idStockRequestHeaderDB";
    private static final String KEY_ID_STOCK_REQUEST_HEADER_BE = "idStockRequestHeaderBE";
    private static final String KEY_ID_DEPO = "idDepo";
    private static final String KEY_REQUEST_DATE = "requestDate";
    private static final String KEY_ID_SALESMAN = "idSalesman";
    private static final String KEY_NO_DOC = "noDoc";
    private static final String KEY_TANGGAL_KIRIM = "tanggalKirim";
    private static final String KEY_NO_SURAT_JALAN = "noSuratJalan";
    private static final String KEY_SIGN = "signature";
    private static final String KEY_IS_UNLOADING = "isUnloading";
    private static final String KEY_IS_VERIF = "isVerif";
    private static final String KEY_CREATED_BY = "createdBy";
    private static final String KEY_CREATED_DATE = "createdDate";
    private static final String KEY_UPDATED_BY = "updatedBy";
    private static final String KEY_UPDATED_DATE = "updatedDate";
    private static final String KEY_IS_SYNC = "isSync";

    //column table non route customer
    private static final String KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB = "idNonRouteCustomerHeaderDB";

    // column table StockRequestDetail
    private static final String KEY_ID_STOCK_REQUEST_DETAIL_DB = "idStockRequestDetailDB";
    private static final String KEY_MATERIAL_ID = "materialId";
    private static final String KEY_MATERIAL_NAME = "materialName";
    private static final String KEY_MATERIAL_GROUP_ID = "materialGroupId";
    private static final String KEY_MATERIAL_GROUP_NAME = "materialGroupName";
    private static final String KEY_LOAD_NUMBER = "loadNumber";
    private static final String KEY_QTY = "qty";
    private static final String KEY_TARGET = "target";
    private static final String KEY_ID_CUSTOMER_TARGET_DB = "idCustomerTargetDB";
    private static final String KEY_ID_NON_ROUTE_CUSTOMER_TARGET_DB = "idNonRouteCustomerTargetDB";
    private static final String KEY_UOM = "uom";
    private static final String KEY_QTY_SISA = "qtySisa";
    private static final String KEY_UOM_SISA = "uomSisa";

    // column table InvoiceHeader
    private static final String KEY_ID_INVOICE_HEADER_DB = "idInvoiceHeaderDB";
    private static final String KEY_INVOICE_NO = "invoiceNo";
    private static final String KEY_INVOICE_DATE = "invoiceDate";
    private static final String KEY_INVOICE_TOTAL = "invoiceTotal";
    private static final String KEY_DUE_DATE = "dueDate";
    private static final String KEY_PAID = "paid";
    private static final String KEY_TOTAL_PAID = "totalPaid";
    private static final String KEY_NETT = "nett";
    private static final String KEY_CUSTOMER_ID = "customerId";
    private static final String KEY_ID_PHOTO_DB = "idPhotoDB";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_TYPE_PHOTO = "typePhoto";
    private static final String KEY_ID_DB = "idDB";
    private static final String KEY_CUSTOMER_NAME = "customerName";
    private static final String KEY_TYPE_CUSTOMER = "typeCustomer";
    private static final String KEY_TYPE_CUSTOMER_NAME = "typeCustomerName";
    private static final String KEY_TYPE_PRICE = "typePrice";
    private static final String KEY_IS_ROUTE = "isRoute";

    // column table InvoiceDetail
    private static final String KEY_ID_INVOICE_DETAIL_DB = "idInvoiceDetailDB";
    private static final String KEY_PRICE = "price";

    // column table NOO
    private static final String KEY_ID_NOO_DB = "idNooDB";
    private static final String KEY_NAME_NOO = "nameNoo";
    private static final String KEY_ADDRESS_NOO = "addressNoo";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_KODE_POS = "kodePos";
    private static final String KEY_ID_DESA_KELURAHAN = "idDesaKelurahan";
    private static final String KEY_NAME_DESA_KELURAHAN = "nameDesaKelurahan";
    private static final String KEY_ID_KECAMATAN = "idKecamatan";
    private static final String KEY_NAME_KECAMATAN = "nameKecamatan";
    private static final String KEY_ID_KOTA_KABUPATEN = "idKotaKabupaten";
    private static final String KEY_NAME_KOTA_KABUPATEN = "nameKotaKabupaten";
    private static final String KEY_ID_PROVINSI = "idProvinsi";
    private static final String KEY_NAME_PROVINSI = "nameProvinsi";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_NO_NPWP = "noNPWP";
    private static final String KEY_NAME_NPWP = "nameNPWP";
    private static final String KEY_ADDRESS_NPWP = "addressNPWP";
    private static final String KEY_STATUS_NPWP = "statusNPWP";
    private static final String KEY_NO_KTP = "noKtp";
    private static final String KEY_NAME_PEMILIK = "namePemilik";
    private static final String KEY_NIK_PEMILIK = "nikPemilik";
    private static final String KEY_STATUS_TOKO = "statusToko";
    private static final String KEY_LOKASI = "lokasi";
    private static final String KEY_JENIS_USAHA = "jenisUsaha";
    private static final String KEY_LAMA_USAHA = "lamaUsaha";
    private static final String KEY_SUKU = "suku";
    private static final String KEY_CREDIT_LIMIT = "creditLimit";
    private static final String KEY_ROUTE = "route";
//    private static final String KEY_PHOTO_KTP = "photoKtp";
//    private static final String KEY_PHOTO_NPWP = "photoNpwp";
//    private static final String KEY_PHOTO_OUTLET = "photoOutlet";

    // column table Customer
    private static final String KEY_ID_CUSTOMER_DB = "idCustomerDB";
    private static final String KEY_CUSTOMER_ADDRESS = "customerAddress";
    private static final String KEY_SISA_KREDIT_LIMIT = "sisaKreditLimit";
    private static final String KEY_UDF_5 = "udf5";
    private static final String KEY_UDF_5_DESC = "udf5Desc";
    private static final String KEY_TOTAL_TAGIHAN = "totalTagihan";

    // column table Customer promotion
    private static final String KEY_ID_CUSTOMER_PROMOTION_DB = "idCustomerPromotionDB";
    private static final String KEY_ID_PROMOTION = "idPromotion";
    private static final String KEY_NAME_PROMOTION = "namePromotion";
    private static final String KEY_NO_PROMOTION = "noPromotion";
    private static final String KEY_TOLERANSI = "toleransi";
    private static final String KEY_JENIS_PROMOSI = "jenisPromosi";
    private static final String KEY_SEGMEN = "segmen";
    private static final String KEY_VALID_FROM_PROMOTION = "validFromPromotion";
    private static final String KEY_VALID_TO_PROMOTION = "validToPromotion";

    // column table VisitSalesman
    private static final String KEY_ID_VISIT_SALESMAN_DB = "idVisitSalesmanDB";
    private static final String KEY_DATE = "date";
    private static final String KEY_ORDER_TYPE = "orderType";
    private static final String KEY_CHECK_IN_TIME = "CheckInTime";
    private static final String KEY_CHECK_OUT_TIME = "CheckOutTime";
    private static final String KEY_STATUS = "Status";
    private static final String KEY_IS_PAID = "isPaid";
    private static final String KEY_RESUME_TIME = "resumeTime";
    private static final String KEY_PAUSE_TIME = "pauseTime";
    private static final String KEY_LAT_CHECK_IN = "latCheckIn";
    private static final String KEY_LONG_CHECK_IN = "longCheckIn";
    private static final String KEY_LAT_CHECK_OUT = "latCheckOut";
    private static final String KEY_LONG_CHECK_OUT = "longCheckOut";
    private static final String KEY_INSIDE = "inside";
    private static final String KEY_INSIDE_CHECK_OUT = "insideCheckOut";
    private static final String KEY_ID_PAUSE_REASON = "idPauseReason";
    private static final String KEY_NAME_PAUSE_REASON = "namePauseReason";
    private static final String KEY_DESC_PAUSE_REASON = "descPauseReason";
    //    private static final String KEY_PHOTO_PAUSE_REASON = "photoPauseReason";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_ID_NOT_VISIT_REASON = "idNotVisitReason";
    private static final String KEY_NAME_NOT_VISIT_REASON = "nameNotVisitReason";
    private static final String KEY_DESC_NOT_VISIT_REASON = "descNotVisitReason";
    //    private static final String KEY_PHOTO_NOT_VISIT_REASON = "photoNotVisitReason";
    private static final String KEY_ID_NOT_BUY_REASON = "idNotBuyReason";
    private static final String KEY_NAME_NOT_BUY_REASON = "nameNotBuyReason";
    private static final String KEY_DESC_NOT_BUY_REASON = "descNotBuyReason";
    //    private static final String KEY_PHOTO_NOT_BUY_REASON = "photoNotBuyReason";
    // column table StoreCheck
    private static final String KEY_ID_STORE_CHECK_DB = "idStoreCheckDB";
    private static final String KEY_ID_MOBILE = "idMobile";

    // column table OrderHeader
    private static final String KEY_ID_ORDER_HEADER_DB = "idOrderHeaderDB";
    private static final String KEY_ID_ORDER_BACK_END = "idOrderBackEnd";
    private static final String KEY_OMZET = "omzet";

    // column table OrderDetail
    private static final String KEY_ID_ORDER_DETAIL_DB = "idOrderDetailDB";
    private static final String KEY_TOTAL_DISCOUNT = "totalDiscount";
    private static final String KEY_TOTAL = "total";

    // column table OrderDetailExtra
    private static final String KEY_ID_ORDER_DETAIL_EXTRA_DB = "idOrderDetailExtraDB";

    // column table OrderDetailDiscount
    private static final String KEY_ID_ORDER_DETAIL_DISCOUNT_DB = "idOrderDetailDiscountDB";
    private static final String KEY_DISCOUNT_ID = "discountID";
    private static final String KEY_DISCOUNT_NAME = "discountName";
    private static final String KEY_DISCOUNT_PRICE = "discountPrice";

    // column table OrderPaymentDetail
    private static final String KEY_TYPE_PAYMENT = "typePayment";
    private static final String KEY_TOTAL_PAYMENT = "totalPayment";
    private static final String KEY_LEFT = "leftPayment";
    private static final String KEY_NO = "nomor";
    private static final String KEY_ID_BANK = "idBank";
    private static final String KEY_NAME_BANK = "nameBank";
    private static final String KEY_ID_CUST_BANK = "idCustBank";
    private static final String KEY_NAME_CUST_BANK = "nameCustBank";

    // column table OrderPaymentItem
    private static final String KEY_AMOUNT_PAID = "amountPaid";

    // column table Return
    private static final String KEY_ID_RETURN_DB = "idReturnDB";
    private static final String KEY_EXPIRED_DATE = "expiredDate";
    private static final String KEY_CONDITION = "condition";
    private static final String KEY_ID_REASON_RETURN = "idReasonReturn";
    private static final String KEY_NAME_REASON_RETURN = "nameReasonReturn";
    private static final String KEY_DESC_REASON_RETURN = "descReasonReturn";
//    private static final String KEY_PHOTO_REASON_RETURN = "photoReasonReturn";

    // column table CollectionHeader
    private static final String KEY_ID_COLLECTION_HEADER_DB = "idCollectionHeaderDB";
    // column table CollectionDetail
    private static final String KEY_ID_COLLECTION_DETAIL_DB = "idCollectionDetailDB";

    // column table CollectionItem
    private static final String KEY_ID_COLLECTION_ITEM_DB = "idCollectionItemDB";

    // column table MasterReason
    private static final String KEY_ID_REASON_DB = "idReasonDB";
    private static final String KEY_ID_REASON_BE = "idReasonBE";
    private static final String KEY_NAME_REASON = "nameReason";
    private static final String KEY_CATEGORY_REASON = "categoryReason";
    private static final String KEY_IS_PHOTO = "isPhoto";
    private static final String KEY_IS_FREE_TEXT = "isFreeText";
    private static final String KEY_IS_BARCODE = "isBarcode";
    private static final String KEY_IS_SIGNATURE = "isSignature";

    // column table MasterPromotion
    private static final String KEY_ID_PROMOTION_DB = "idPromotionDB";
    // column table LogModel
    private static final String KEY_ID_LOG_DB = "idLogDB";
    private static final String KEY_DESC_LOG = "descLog";
    private static final String KEY_DATE_LOG = "dateLog";
    private static final String KEY_TIME_LOG = "timeLog";

    //column table MasterBank
    private static final String KEY_ID_BANK_DB = "idBankDB";
    private static final String KEY_ID_BANK_BE = "idBankBE";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_NO_REK = "noRekening";

    //column table MasterMaterial
    private static final String KEY_MATERIAL_ID_DB = "idMaterialDB";
    private static final String KEY_MATERIAL_SALES = "materialSales";
    private static final String KEY_MATERIAL_PRODUCT_ID = "materialProductId";
    private static final String KEY_MATERIAL_PRODUCT_NAME = "materialProductName";

    //column table MasterUom
    private static final String KEY_UOM_ID_DB = "idUomDB";
    //    private static final String KEY_UOM_ID = "uomId";
    private static final String KEY_CONVERSION = "conversion";
    private static final String KEY_QTY_MIN = "qtyMin";
    private static final String KEY_QTY_MAX = "qtyMax";

    //column table MasterDaerahTingkat
    private static final String KEY_DAERAH_TINGKAT_ID_DB = "idDaerahTingkatDB";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static String CREATE_TABLE_STOCK_REQUEST_HEADER = "CREATE TABLE " + TABLE_STOCK_REQUEST_HEADER + "("
            + KEY_ID_STOCK_REQUEST_HEADER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_STOCK_REQUEST_HEADER_BE + " TEXT,"
            + KEY_REQUEST_DATE + " TEXT,"
            + KEY_ID_SALESMAN + " TEXT,"
            + KEY_NO_DOC + " TEXT,"
            + KEY_TANGGAL_KIRIM + " TEXT,"
            + KEY_NO_SURAT_JALAN + " TEXT,"
            + KEY_SIGN + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_IS_UNLOADING + " INTEGER DEFAULT 0,"
            + KEY_IS_VERIF + " INTEGER DEFAULT 0,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0, "
            + " UNIQUE (" + KEY_ID_STOCK_REQUEST_HEADER_BE + ", " + KEY_REQUEST_DATE + "," + KEY_ID_SALESMAN + ")"
            + ")";

    public static String CREATE_TABLE_STOCK_REQUEST_DETAIL = "CREATE TABLE " + TABLE_STOCK_REQUEST_DETAIL + "("
            + KEY_ID_STOCK_REQUEST_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_STOCK_REQUEST_HEADER_DB + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " TEXT,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_LOAD_NUMBER + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_QTY_SISA + " REAL,"
            + KEY_UOM_SISA + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_ID_STOCK_REQUEST_HEADER_DB + ", " + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_NON_ROUTE_CUSTOMER = "CREATE TABLE " + TABLE_MASTER_NON_ROUTE_CUSTOMER + "("
            + KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_CUSTOMER_ADDRESS + " TEXT,"
            + KEY_TYPE_CUSTOMER + " TEXT,"
            + KEY_TYPE_CUSTOMER_NAME + " TEXT,"
            + KEY_TYPE_PRICE + " TEXT,"
            + KEY_UDF_5 + " TEXT,"
            + KEY_UDF_5_DESC + " TEXT,"
            + KEY_NAME_PEMILIK + " TEXT,"
            + KEY_LATITUDE + " REAL,"
            + KEY_LONGITUDE + " REAL,"
            + KEY_PHONE + " TEXT,"
            + KEY_SISA_KREDIT_LIMIT + " REAL,"
            + KEY_CREDIT_LIMIT + " REAL,"
            + KEY_TOTAL_TAGIHAN + " REAL,"
            + KEY_NO_KTP + " TEXT,"
            + KEY_NO_NPWP + " TEXT,"
            + KEY_ROUTE + " TEXT,"
            + KEY_KODE_POS + " TEXT,"
            + KEY_NAME_KOTA_KABUPATEN + " TEXT,"
            + KEY_IS_ROUTE + " INTEGER DEFAULT 0,"
//            + KEY_PHOTO_KTP + " TEXT,"
//            + KEY_PHOTO_NPWP + " TEXT,"
//            + KEY_PHOTO_OUTLET + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION = "CREATE TABLE " + TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION + "("
            + KEY_ID_MASTER_NON_ROUTE_CUSTOMER_PROMOTION_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB + " TEXT ,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_ID_PROMOTION + " TEXT,"
            + KEY_NO_PROMOTION + " TEXT,"
            + KEY_NAME_PROMOTION + " TEXT,"
            + KEY_DESC + " TEXT,"
            + KEY_TOLERANSI + " TEXT,"
            + KEY_JENIS_PROMOSI + " TEXT,"
            + KEY_SEGMEN + " TEXT,"
            + KEY_VALID_FROM_PROMOTION + " TEXT,"
            + KEY_VALID_TO_PROMOTION + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_ID_PROMOTION + ")"
            + ")";

    public static String CREATE_TABLE_NON_ROUTE_CUSTOMER_DCT = "CREATE TABLE " + TABLE_MASTER_NON_ROUTE_CUSTOMER_DCT + "("
            + KEY_ID_NON_ROUTE_CUSTOMER_TARGET_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_TARGET + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_MATERIAL_GROUP_ID + ")"
            + ")";

    public static String CREATE_TABLE_INVOICE_HEADER = "CREATE TABLE " + TABLE_INVOICE_HEADER + "("
            + KEY_ID_INVOICE_HEADER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DATE + " TEXT,"
            + KEY_INVOICE_NO + " TEXT,"
            + KEY_INVOICE_DATE + " TEXT,"
            + KEY_INVOICE_TOTAL + " REAL,"
            + KEY_DUE_DATE + " TEXT,"
            + KEY_PAID + " REAL,"
            + KEY_NETT + " REAL,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_SIGN + " TEXT,"
            + KEY_IS_VERIF + " INTEGER DEFAULT 0,"
            + KEY_IS_ROUTE + " INTEGER DEFAULT 0,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_INVOICE_NO + "," + KEY_DATE + ")"
            + ")";

    public static String CREATE_TABLE_INVOICE_DETAIL = "CREATE TABLE " + TABLE_INVOICE_DETAIL + "("
            + KEY_ID_INVOICE_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_INVOICE_HEADER_DB + " TEXT,"
            + KEY_INVOICE_NO + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " TEXT,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_PAID + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_INVOICE_NO + ", " + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_NOO = "CREATE TABLE " + TABLE_NOO + "("
            + KEY_ID_NOO_DB + " TEXT PRIMARY KEY,"
            + KEY_NAME_NOO + " TEXT,"
            + KEY_ADDRESS_NOO + " TEXT,"
            + KEY_STATUS + " INTEGER DEFAULT 0,"
            + KEY_LATITUDE + " REAL,"
            + KEY_LONGITUDE + " REAL,"
            + KEY_KODE_POS + " TEXT,"
            + KEY_UDF_5 + " TEXT,"
            + KEY_UDF_5_DESC + " TEXT,"
            + KEY_ID_DESA_KELURAHAN + " TEXT,"
            + KEY_NAME_DESA_KELURAHAN + " TEXT,"
            + KEY_ID_KECAMATAN + " TEXT,"
            + KEY_NAME_KECAMATAN + " TEXT,"
            + KEY_ID_KOTA_KABUPATEN + " TEXT,"
            + KEY_NAME_KOTA_KABUPATEN + " TEXT,"
            + KEY_ID_PROVINSI + " TEXT,"
            + KEY_NAME_PROVINSI + " TEXT,"
            + KEY_PHONE + " TEXT,"
            + KEY_NO_NPWP + " TEXT,"
            + KEY_NAME_NPWP + " TEXT,"
            + KEY_ADDRESS_NPWP + " TEXT,"
            + KEY_STATUS_NPWP + " TEXT,"
            + KEY_NO_KTP + " TEXT,"
            + KEY_NAME_PEMILIK + " TEXT,"
            + KEY_NIK_PEMILIK + " TEXT,"
            + KEY_STATUS_TOKO + " TEXT,"
            + KEY_LOKASI + " TEXT,"
            + KEY_JENIS_USAHA + " TEXT,"
            + KEY_LAMA_USAHA + " TEXT,"
            + KEY_SUKU + " TEXT,"
            + KEY_TYPE_CUSTOMER + " TEXT,"
            + KEY_TYPE_CUSTOMER_NAME + " TEXT,"
            + KEY_TYPE_PRICE + " TEXT,"
            + KEY_CREDIT_LIMIT + " REAL,"
            + KEY_SISA_KREDIT_LIMIT + " REAL,"
            + KEY_TOTAL_TAGIHAN + " REAL,"
            + KEY_ROUTE + " TEXT,"
//            + KEY_PHOTO_KTP + " TEXT,"
//            + KEY_PHOTO_NPWP + " TEXT,"
//            + KEY_PHOTO_OUTLET + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
//            + KEY_IS_SYNC_PHOTO_KTP + " INTEGER DEFAULT 0,"
//            + KEY_IS_SYNC_PHOTO_NPWP + " INTEGER DEFAULT 0,"
//            + KEY_IS_SYNC_PHOTO_OUTLET + " INTEGER DEFAULT 0,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0"
//            + " UNIQUE (" + KEY_ID_NOO_DB + ")"
            + ")";

    public static String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + "("
            + KEY_ID_CUSTOMER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_CUSTOMER_ADDRESS + " TEXT,"
            + KEY_STATUS + " INTEGER DEFAULT 0,"
            + KEY_TYPE_CUSTOMER + " TEXT,"
            + KEY_TYPE_CUSTOMER_NAME + " TEXT,"
            + KEY_TYPE_PRICE + " TEXT,"
            + KEY_UDF_5 + " TEXT,"
            + KEY_UDF_5_DESC + " TEXT,"
            + KEY_NAME_PEMILIK + " TEXT,"
            + KEY_LATITUDE + " REAL,"
            + KEY_LONGITUDE + " REAL,"
            + KEY_PHONE + " TEXT,"
            + KEY_SISA_KREDIT_LIMIT + " REAL,"
            + KEY_CREDIT_LIMIT + " REAL,"
            + KEY_TOTAL_TAGIHAN + " REAL,"
            + KEY_NO_KTP + " TEXT,"
            + KEY_NO_NPWP + " TEXT,"
            + KEY_ROUTE + " TEXT,"
            + KEY_KODE_POS + " TEXT,"
            + KEY_NAME_KOTA_KABUPATEN + " TEXT,"
            + KEY_IS_ROUTE + " INTEGER DEFAULT 0,"
//            + KEY_PHOTO_KTP + " TEXT,"
//            + KEY_PHOTO_NPWP + " TEXT,"
//            + KEY_PHOTO_OUTLET + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
//            + KEY_IS_SYNC_PHOTO_KTP + " INTEGER DEFAULT 0,"
//            + KEY_IS_SYNC_PHOTO_NPWP + " INTEGER DEFAULT 0,"
//            + KEY_IS_SYNC_PHOTO_OUTLET + " INTEGER DEFAULT 0,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ")"
            + ")";

    public static String CREATE_TABLE_CUSTOMER_PROMOTION = "CREATE TABLE " + TABLE_CUSTOMER_PROMOTION + "("
            + KEY_ID_CUSTOMER_PROMOTION_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_ID_PROMOTION + " TEXT,"
            + KEY_NAME_PROMOTION + " TEXT,"
            + KEY_NO_PROMOTION + " TEXT,"
            + KEY_DESC + " TEXT,"
            + KEY_TOLERANSI + " TEXT,"
            + KEY_JENIS_PROMOSI + " TEXT,"
            + KEY_SEGMEN + " TEXT,"
            + KEY_VALID_FROM_PROMOTION + " TEXT,"
            + KEY_VALID_TO_PROMOTION + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0"
//            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_ID_PROMOTION + ")"
            + ")";

    public static String CREATE_TABLE_CUSTOMER_DCT = "CREATE TABLE " + TABLE_CUSTOMER_DCT + "("
            + KEY_ID_CUSTOMER_TARGET_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_TARGET + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_MATERIAL_GROUP_ID + ")"
            + ")";

    public static String CREATE_TABLE_VISIT_SALESMAN = "CREATE TABLE " + TABLE_VISIT_SALESMAN + "("
            + KEY_ID_VISIT_SALESMAN_DB + " TEXT PRIMARY KEY ,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_ID_VISIT + " TEXT,"
            + KEY_ID_SALESMAN + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_CHECK_IN_TIME + " TEXT,"
            + KEY_CHECK_OUT_TIME + " TEXT,"
            + KEY_STATUS + " INTEGER DEFAULT 0,"
            + KEY_RESUME_TIME + " TEXT,"
            + KEY_PAUSE_TIME + " TEXT,"
            + KEY_LAT_CHECK_IN + " REAL,"
            + KEY_LONG_CHECK_IN + " REAL,"
            + KEY_LAT_CHECK_OUT + " REAL,"
            + KEY_LONG_CHECK_OUT + " REAL,"
            + KEY_INSIDE + " INTEGER DEFAULT 0,"
            + KEY_INSIDE_CHECK_OUT + " INTEGER DEFAULT 0,"
            + KEY_ID_PAUSE_REASON + " TEXT,"
            + KEY_NAME_PAUSE_REASON + " TEXT,"
            + KEY_DESC_PAUSE_REASON + " TEXT,"
//            + KEY_PHOTO_PAUSE_REASON + " TEXT,"
            + KEY_DURATION + " TEXT,"
            + KEY_ID_NOT_VISIT_REASON + " TEXT,"
            + KEY_NAME_NOT_VISIT_REASON + " TEXT,"
            + KEY_DESC_NOT_VISIT_REASON + " TEXT,"
//            + KEY_PHOTO_NOT_VISIT_REASON + " TEXT,"
            + KEY_ID_NOT_BUY_REASON + " TEXT,"
            + KEY_NAME_NOT_BUY_REASON + " TEXT,"
            + KEY_DESC_NOT_BUY_REASON + " TEXT,"
//            + KEY_PHOTO_NOT_BUY_REASON + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC_PHOTO_REASON_PAUSE + " INTEGER DEFAULT 0,"
            + KEY_IS_SYNC_PHOTO_REASON_NOT_VISIT + " INTEGER DEFAULT 0,"
            + KEY_IS_SYNC_PHOTO_REASON_NOT_BUY + " INTEGER DEFAULT 0,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_ID_SALESMAN + "," + KEY_DATE + ")"
            + ")";

    public static String CREATE_TABLE_STORE_CHECK = "CREATE TABLE " + TABLE_STORE_CHECK + "("
            + KEY_ID_STORE_CHECK_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_MOBILE + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " TEXT,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_DATE + "," + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_HEADER = "CREATE TABLE " + TABLE_ORDER_HEADER + "("
            + KEY_ID_ORDER_HEADER_DB + " TEXT PRIMARY KEY,"
            + KEY_ID_ORDER_BACK_END + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_ORDER_TYPE + " TEXT,"
            + KEY_ID_STOCK_REQUEST_HEADER_DB + " TEXT,"
            + KEY_ID_STOCK_REQUEST_HEADER_BE + " TEXT,"
            + KEY_TANGGAL_KIRIM + " TEXT,"
            + KEY_IS_PAID + " INTEGER DEFAULT 0,"
            + KEY_TOP + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_OMZET + " REAL,"
            + KEY_STATUS + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0"
//            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_IMEI + "," + KEY_DO_NUMBER + "," + KEY_SHIPMENT_NUMBER + "," + KEY_SHIPMENT_CATEGORY + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_DETAIL = "CREATE TABLE " + TABLE_ORDER_DETAIL + "("
            + KEY_ID_ORDER_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_HEADER_DB + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_PRICE_LIST_CODE + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " TEXT,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_ID_STOCK_REQUEST_HEADER_DB + " TEXT,"
            + KEY_ID_STOCK_REQUEST_HEADER_BE + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_TOTAL_DISCOUNT + " REAL,"
            + KEY_TOTAL + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_MATERIAL_ID + "," + KEY_ID_ORDER_HEADER_DB + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_DETAIL_EXTRA = "CREATE TABLE " + TABLE_ORDER_DETAIL_EXTRA + "("
            + KEY_ID_ORDER_DETAIL_EXTRA_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_HEADER_DB + " TEXT,"
            + KEY_ID_ORDER_DETAIL_DB + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " TEXT,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_TOTAL_DISCOUNT + " REAL,"
            + KEY_TOTAL + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_ID_ORDER_HEADER_DB + ", " + KEY_ID_ORDER_DETAIL_DB + "," + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_DETAIL_DISCOUNT = "CREATE TABLE " + TABLE_ORDER_DETAIL_DISCOUNT + "("
            + KEY_ID_ORDER_DETAIL_DISCOUNT_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_DETAIL_DB + " TEXT,"
            + KEY_ID_ORDER_HEADER_DB + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_DISCOUNT_ID + " TEXT,"
            + KEY_DISCOUNT_NAME + " TEXT,"
            + KEY_DISCOUNT_PRICE + " REAL,"
            + KEY_TOTAL + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_ID_ORDER_HEADER_DB + ", " + KEY_DISCOUNT_ID + "," + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_RETURN = "CREATE TABLE " + TABLE_RETURN + "("
            + KEY_ID_RETURN_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_MOBILE + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " TEXT,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_EXPIRED_DATE + " TEXT,"
            + KEY_CONDITION + " TEXT,"
            + KEY_ID_REASON_RETURN + " TEXT,"
            + KEY_NAME_REASON_RETURN + " TEXT,"
            + KEY_DESC_REASON_RETURN + " TEXT,"
//            + KEY_PHOTO_REASON_RETURN + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC_PHOTO + " INTEGER DEFAULT 0,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_DATE + "," + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_COLLECTION_HEADER = "CREATE TABLE " + TABLE_COLLECTION_HEADER + "("
            + KEY_ID_COLLECTION_HEADER_DB + " TEXT PRIMARY KEY,"
            + KEY_DATE + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_INVOICE_NO + " TEXT,"
            + KEY_INVOICE_DATE + " TEXT,"
            + KEY_INVOICE_TOTAL + " REAL,"
            + KEY_TOTAL_PAID + " REAL,"
            + KEY_STATUS + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0"
            + ")";

    public static String CREATE_TABLE_COLLECTION_DETAIL = "CREATE TABLE " + TABLE_COLLECTION_DETAIL + "("
            + KEY_ID_COLLECTION_DETAIL_DB + " INTEGER PRIMARY KEY,"
            + KEY_ID_COLLECTION_HEADER_DB + " TEXT,"
            + KEY_INVOICE_NO + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_TYPE_PAYMENT + " TEXT,"
            + KEY_TOTAL_PAYMENT + " REAL,"
            + KEY_LEFT + " REAL,"
            + KEY_DATE + " TEXT,"
            + KEY_NO + " TEXT,"
            + KEY_DUE_DATE + " TEXT,"
            + KEY_ID_BANK + " TEXT,"
            + KEY_NAME_BANK + " TEXT,"
            + KEY_ID_CUST_BANK + " TEXT,"
            + KEY_NAME_CUST_BANK + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_ID_COLLECTION_DETAIL_DB + ", " + KEY_ID_COLLECTION_HEADER_DB + ")"
            + ")";

    public static String CREATE_TABLE_COLLECTION_ITEM = "CREATE TABLE " + TABLE_COLLECTION_ITEM + "("
            + KEY_ID_COLLECTION_ITEM_DB + " INTEGER PRIMARY KEY,"
            + KEY_ID_COLLECTION_DETAIL_DB + " TEXT,"
            + KEY_ID_COLLECTION_HEADER_DB + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " TEXT,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_AMOUNT_PAID + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0,"
            + " UNIQUE (" + KEY_ID_COLLECTION_ITEM_DB + ", " + KEY_ID_COLLECTION_DETAIL_DB + "," + KEY_ID_COLLECTION_HEADER_DB + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_REASON = "CREATE TABLE " + TABLE_MASTER_REASON + "("
            + KEY_ID_REASON_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_REASON_BE + " TEXT,"
            + KEY_NAME_REASON + " TEXT,"
            + KEY_CATEGORY_REASON + " TEXT,"
            + KEY_IS_PHOTO + " INTEGER DEFAULT 0,"
            + KEY_IS_FREE_TEXT + " INTEGER DEFAULT 0,"
            + KEY_IS_BARCODE + " INTEGER DEFAULT 0,"
            + KEY_IS_SIGNATURE + " INTEGER DEFAULT 0,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_ID_REASON_BE + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_PROMOTION = "CREATE TABLE " + TABLE_MASTER_PROMOTION + "("
            + KEY_ID_PROMOTION_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_PROMOTION + " TEXT,"
            + KEY_NAME_PROMOTION + " TEXT,"
            + KEY_NO_PROMOTION + " TEXT,"
            + KEY_DESC + " TEXT,"
            + KEY_TOLERANSI + " TEXT,"
            + KEY_JENIS_PROMOSI + " TEXT,"
            + KEY_SEGMEN + " TEXT,"
            + KEY_VALID_FROM_PROMOTION + " TEXT,"
            + KEY_VALID_TO_PROMOTION + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_ID_PROMOTION + ")"
            + ")";

    public static String CREATE_TABLE_LOG = "CREATE TABLE " + TABLE_LOG + "("
            + KEY_ID_LOG_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DESC_LOG + " TEXT,"
            + KEY_DATE_LOG + " TEXT,"
            + KEY_TIME_LOG + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT "
            + ")";

    public static String CREATE_TABLE_MASTER_BANK = "CREATE TABLE " + TABLE_MASTER_BANK + "("
            + KEY_ID_BANK_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_BANK_BE + " TEXT,"
            + KEY_NAME_BANK + " TEXT,"
            + KEY_ID_DEPO + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_NO_REK + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_ID_BANK_BE + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_MATERIAL = "CREATE TABLE " + TABLE_MASTER_MATERIAL + "("
            + KEY_MATERIAL_ID_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_SALES + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " TEXT,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " TEXT,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_UOM = "CREATE TABLE " + TABLE_MASTER_UOM + "("
            + KEY_UOM_ID_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_UOM + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_CONVERSION + " INTEGER,"
            + KEY_QTY_MIN + " REAL,"
            + KEY_QTY_MAX + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_UOM + ", " + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_DAERAH_TINGKAT = "CREATE TABLE " + TABLE_MASTER_DAERAH_TINGKAT + "("
            + KEY_DAERAH_TINGKAT_ID_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_KODE_POS + " TEXT,"
            + KEY_ID_DESA_KELURAHAN + " TEXT,"
            + KEY_NAME_DESA_KELURAHAN + " TEXT,"
            + KEY_ID_KECAMATAN + " TEXT,"
            + KEY_NAME_KECAMATAN + " TEXT,"
            + KEY_ID_KOTA_KABUPATEN + " TEXT,"
            + KEY_NAME_KOTA_KABUPATEN + " TEXT,"
            + KEY_ID_PROVINSI + " TEXT,"
            + KEY_NAME_PROVINSI + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_KODE_POS + ", " + KEY_ID_DESA_KELURAHAN + "," + KEY_ID_KECAMATAN + "," + KEY_ID_KOTA_KABUPATEN + "," + KEY_ID_PROVINSI + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_PRICE_CODE = "CREATE TABLE " + TABLE_MASTER_PRICE_CODE + "("
            + KEY_ID_PRICE_CODE_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MATERIAL_PRODUCT_ID + " TEXT,"
            + KEY_UDF_5 + " TEXT,"
            + KEY_UDF_5_DESC + " TEXT,"
            + KEY_PRICE_LIST_CODE + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_MATERIAL_PRODUCT_ID + ", " + KEY_UDF_5 + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_SALES_PRICE_HEADER = "CREATE TABLE " + TABLE_MASTER_SALES_PRICE_HEADER + "("
            + KEY_ID_SALES_PRICE_HEADER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TOP + " TEXT,"
            + KEY_PRICE_LIST_CODE + " TEXT,"
            + KEY_VALID_FROM + " TEXT,"
            + KEY_VALID_TO + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_PRICE_LIST_CODE + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_SALES_PRICE_DETAIL = "CREATE TABLE " + TABLE_MASTER_SALES_PRICE_DETAIL + "("
            + KEY_ID_SALES_PRICE_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_PRICE_LIST_CODE + " TEXT,"
            + KEY_UOM + " TEXT,"
            + KEY_QTY + " REAL,"
            + SELLING_PRICE + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_MATERIAL_ID + ", " + KEY_PRICE_LIST_CODE + ")"
            + ")";

    public static String CREATE_TABLE_PARAMETER = "CREATE TABLE " + TABLE_MASTER_PARAMETER + "("
            + KEY_ID_PARAMETER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_KEY_PARAMETER + " TEXT,"
            + KEY_VALUE + " TEXT,"
            + KEY_DESC + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_KEY_PARAMETER + ")"
            + ")";

    public static String CREATE_TABLE_CUSTOMER_TYPE = "CREATE TABLE " + TABLE_MASTER_CUSTOMER_TYPE + "("
            + KEY_ID_CUSTOMER_TYPE + " TEXT PRIMARY KEY,"
            + KEY_ID_TYPE_PRICE + " TEXT,"
            + KEY_NAME_TYPE_PRICE + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_ID_TYPE_PRICE + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_LIMIT_BON = "CREATE TABLE " + TABLE_MASTER_LIMIT_BON + "("
            + KEY_ID_MINIMAL_ORDER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_BON_LIMIT + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_MATERIAL_ID + "," + KEY_CUSTOMER_ID + ")"
            + ")";

    public static String CREATE_TABLE_PHOTO = "CREATE TABLE " + TABLE_PHOTO + "("
            + KEY_ID_PHOTO_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PHOTO + " TEXT,"
            + KEY_TYPE_PHOTO + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_ID_DB + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER DEFAULT 0"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STOCK_REQUEST_HEADER);
        db.execSQL(CREATE_TABLE_STOCK_REQUEST_DETAIL);
        db.execSQL(CREATE_TABLE_INVOICE_HEADER);
        db.execSQL(CREATE_TABLE_INVOICE_DETAIL);
        db.execSQL(CREATE_TABLE_MASTER_NON_ROUTE_CUSTOMER);
        db.execSQL(CREATE_TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION);
        db.execSQL(CREATE_TABLE_NON_ROUTE_CUSTOMER_DCT);
        db.execSQL(CREATE_TABLE_NOO);
        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_CUSTOMER_PROMOTION);
        db.execSQL(CREATE_TABLE_CUSTOMER_DCT);
        db.execSQL(CREATE_TABLE_VISIT_SALESMAN);
        db.execSQL(CREATE_TABLE_STORE_CHECK);
        db.execSQL(CREATE_TABLE_ORDER_HEADER);
        db.execSQL(CREATE_TABLE_ORDER_DETAIL);
        db.execSQL(CREATE_TABLE_ORDER_DETAIL_DISCOUNT);
        db.execSQL(CREATE_TABLE_ORDER_DETAIL_EXTRA);
        db.execSQL(CREATE_TABLE_RETURN);
        db.execSQL(CREATE_TABLE_COLLECTION_HEADER);
        db.execSQL(CREATE_TABLE_COLLECTION_DETAIL);
        db.execSQL(CREATE_TABLE_COLLECTION_ITEM);
        db.execSQL(CREATE_TABLE_MASTER_REASON);
        db.execSQL(CREATE_TABLE_MASTER_BANK);
        db.execSQL(CREATE_TABLE_MASTER_MATERIAL);
        db.execSQL(CREATE_TABLE_MASTER_UOM);
        db.execSQL(CREATE_TABLE_MASTER_DAERAH_TINGKAT);
        db.execSQL(CREATE_TABLE_MASTER_PROMOTION);
        db.execSQL(CREATE_TABLE_MASTER_PRICE_CODE);
        db.execSQL(CREATE_TABLE_MASTER_SALES_PRICE_HEADER);
        db.execSQL(CREATE_TABLE_MASTER_SALES_PRICE_DETAIL);
        db.execSQL(CREATE_TABLE_MASTER_LIMIT_BON);
        db.execSQL(CREATE_TABLE_PARAMETER);
        db.execSQL(CREATE_TABLE_LOG);
        db.execSQL(CREATE_TABLE_CUSTOMER_TYPE);
        db.execSQL(CREATE_TABLE_PHOTO);
    }

    // on Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_REQUEST_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_REQUEST_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_PROMOTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_DCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISIT_SALESMAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_CHECK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAIL_DISCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAIL_EXTRA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETURN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_NON_ROUTE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_REASON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_BANK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_PROMOTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_MATERIAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_UOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_DAERAH_TINGKAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_PRICE_CODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_SALES_PRICE_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_SALES_PRICE_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_LIMIT_BON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_CUSTOMER_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_PARAMETER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
        onCreate(db);
    }

    //add
    public int addStockRequestHeader(StockRequest param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_STOCK_REQUEST_HEADER_BE, param.getId());
        values.put(KEY_REQUEST_DATE, param.getReq_date());
        values.put(KEY_ID_SALESMAN, idSales);
        values.put(KEY_NO_DOC, param.getNo_doc());
        values.put(KEY_TANGGAL_KIRIM, param.getTanggal_kirim());
        values.put(KEY_NO_SURAT_JALAN, param.getNo_surat_jalan());
        values.put(KEY_STATUS, param.getStatus().toLowerCase());
        values.put(KEY_SIGN, param.getSignature());
        values.put(KEY_IS_UNLOADING, param.getIs_unloading());
        values.put(KEY_IS_VERIF, param.getIs_verif());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_STOCK_REQUEST_HEADER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addStockRequestDetail(Material param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_STOCK_REQUEST_HEADER_DB, idHeader);
        values.put(KEY_MATERIAL_ID, param.getId());
        values.put(KEY_MATERIAL_NAME, param.getNama());
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
        values.put(KEY_MATERIAL_PRODUCT_NAME, param.getName_product_group());
        values.put(KEY_LOAD_NUMBER, param.getLoad_number());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_QTY_SISA, param.getQty());
        values.put(KEY_UOM_SISA, param.getUom());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_STOCK_REQUEST_DETAIL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addInvoiceHeader(Invoice param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_INVOICE_NO, param.getNo_invoice());
        values.put(KEY_INVOICE_DATE, param.getInvoice_date());
        values.put(KEY_INVOICE_TOTAL, param.getAmount());
        values.put(KEY_DUE_DATE, param.getTanggal_jatuh_tempo());
        values.put(KEY_PAID, param.getTotal_paid());
        values.put(KEY_NETT, param.getAmount() - param.getTotal_paid());
        values.put(KEY_CUSTOMER_ID, param.getId_customer());
        values.put(KEY_CUSTOMER_NAME, param.getNama());
        values.put(KEY_SIGN, param.getSignature());
        values.put(KEY_IS_VERIF, param.getIs_verif());
        values.put(KEY_IS_ROUTE, param.getIs_route());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_INVOICE_HEADER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addInvoiceDetail(Material param, Map header) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_INVOICE_HEADER_DB, header.get("idHeader").toString());
        values.put(KEY_INVOICE_NO, header.get("no_invoice").toString());
        values.put(KEY_MATERIAL_ID, param.getId());
        values.put(KEY_MATERIAL_NAME, param.getNama());
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
        values.put(KEY_MATERIAL_PRODUCT_NAME, param.getName_product_group());
        values.put(KEY_PRICE, param.getAmount());
        values.put(KEY_PAID, param.getNett());
        values.put(KEY_CREATED_BY, header.get("username").toString());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_INVOICE_DETAIL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addPhoto(Map req) {
        SQLiteDatabase db = getWritableDatabase();

        if (req.get("idDB") != null) {
            if (req.get("idMaterial") != null) {
                db.delete(TABLE_PHOTO, KEY_TYPE_PHOTO + " = ? and " + KEY_ID_DB + " = ?  and " + KEY_MATERIAL_ID + " = ? and " + KEY_CUSTOMER_ID + " = ?",
                        new String[]{req.get("typePhoto").toString(), req.get("idDB").toString(), req.get("idMaterial").toString(), req.get("customerID").toString()});
            } else {
                db.delete(TABLE_PHOTO, KEY_TYPE_PHOTO + " = ? and " + KEY_ID_DB + " = ?  and " + KEY_CUSTOMER_ID + " = ?",
                        new String[]{req.get("typePhoto").toString(), req.get("idDB").toString(), req.get("customerID").toString()});
            }
        }

        ContentValues values = new ContentValues();
        values.put(KEY_PHOTO, req.get("photo").toString());
        values.put(KEY_TYPE_PHOTO, req.get("typePhoto").toString());
        values.put(KEY_ID_DB, req.get("idDB").toString());
        values.put(KEY_MATERIAL_ID, req.get("idMaterial") != null ? req.get("idMaterial").toString() : null);
        values.put(KEY_CUSTOMER_ID, req.get("customerID").toString());
        values.put(KEY_CREATED_BY, req.get("username").toString());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_PHOTO, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addNoo(Customer param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_NOO_DB, param.getId());
        values.put(KEY_NAME_NOO, param.getNama());
        values.put(KEY_ADDRESS_NOO, param.getAddress());
        values.put(KEY_LATITUDE, param.getLatitude());
        values.put(KEY_LONGITUDE, param.getLongitude());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_KODE_POS, param.getKode_pos());
        values.put(KEY_UDF_5, param.getUdf_5());
        values.put(KEY_UDF_5_DESC, param.getUdf_5_desc());
        values.put(KEY_ID_DESA_KELURAHAN, param.getIdKelurahan());
        values.put(KEY_NAME_DESA_KELURAHAN, param.getKelurahan());
        values.put(KEY_ID_KECAMATAN, param.getIdKecamatan());
        values.put(KEY_NAME_KECAMATAN, param.getKecamatan());
        values.put(KEY_ID_KOTA_KABUPATEN, param.getIdKota());
        values.put(KEY_NAME_KOTA_KABUPATEN, param.getKota());
        values.put(KEY_ID_PROVINSI, param.getIdProvinsi());
        values.put(KEY_NAME_PROVINSI, param.getProvinsi());
        values.put(KEY_PHONE, param.getNo_tlp());
        values.put(KEY_NO_NPWP, param.getNo_npwp());
        values.put(KEY_NAME_NPWP, param.getNpwp_name());
        values.put(KEY_ADDRESS_NPWP, param.getNpwp_address());
        values.put(KEY_STATUS_NPWP, param.getStatus_npwp());
        values.put(KEY_NO_KTP, param.getNik());
        values.put(KEY_NAME_PEMILIK, param.getNama_pemilik());
        values.put(KEY_NIK_PEMILIK, param.getNik());
        values.put(KEY_STATUS_TOKO, param.getStatus_toko());
        values.put(KEY_LOKASI, param.getLocation());
        values.put(KEY_JENIS_USAHA, param.getJenis_usaha());
        values.put(KEY_LAMA_USAHA, param.getLama_usaha());
        values.put(KEY_SUKU, param.getSuku());
        values.put(KEY_TYPE_CUSTOMER_NAME, param.getName_type_customer());
        values.put(KEY_TYPE_CUSTOMER, param.getType_customer());
        values.put(KEY_TYPE_PRICE, param.getType_price());
        values.put(KEY_CREDIT_LIMIT, param.getLimit_kredit());
        values.put(KEY_ROUTE, param.getRute());
//        values.put(KEY_PHOTO_KTP, param.getPhotoKtp());
//        values.put(KEY_PHOTO_NPWP, param.getPhotoNpwp());
//        values.put(KEY_PHOTO_OUTLET, param.getPhotoOutlet());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_NOO, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addCustomer(Customer param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, param.getId());
        values.put(KEY_CUSTOMER_NAME, param.getNama());
        values.put(KEY_CUSTOMER_ADDRESS, param.getAddress());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_TYPE_CUSTOMER, param.getType_customer());
        values.put(KEY_TYPE_CUSTOMER_NAME, param.getName_type_customer());
        values.put(KEY_TYPE_PRICE, param.getType_price());
        values.put(KEY_UDF_5, param.getUdf_5());
        values.put(KEY_UDF_5_DESC, param.getUdf_5_desc());
        values.put(KEY_ROUTE, param.getRute());
        values.put(KEY_KODE_POS, param.getKode_pos());
        values.put(KEY_NAME_KOTA_KABUPATEN, param.getKota());
        values.put(KEY_LATITUDE, param.getLatitude());
        values.put(KEY_LONGITUDE, param.getLongitude());
        values.put(KEY_PHONE, param.getNo_tlp());
        values.put(KEY_NAME_PEMILIK, param.getNama_pemilik());
        values.put(KEY_CREDIT_LIMIT, param.getLimit_kredit());
        values.put(KEY_NO_KTP, param.getNik());
        values.put(KEY_NO_NPWP, param.getNo_npwp());
        values.put(KEY_IS_ROUTE, param.isRoute());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_CUSTOMER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addNonRouteCustomer(Customer param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, param.getId());
        values.put(KEY_CUSTOMER_NAME, param.getNama());
        values.put(KEY_CUSTOMER_ADDRESS, param.getAddress());
        values.put(KEY_TYPE_CUSTOMER, param.getType_customer());
        values.put(KEY_TYPE_CUSTOMER_NAME, param.getName_type_customer());
        values.put(KEY_TYPE_PRICE, param.getType_price());
        values.put(KEY_UDF_5, param.getUdf_5());
        values.put(KEY_UDF_5_DESC, param.getUdf_5_desc());
        values.put(KEY_ROUTE, param.getRute());
        values.put(KEY_KODE_POS, param.getKode_pos());
        values.put(KEY_NAME_KOTA_KABUPATEN, param.getKota());
        values.put(KEY_LATITUDE, param.getLatitude());
        values.put(KEY_LONGITUDE, param.getLongitude());
        values.put(KEY_PHONE, param.getNo_tlp());
        values.put(KEY_NAME_PEMILIK, param.getNama_pemilik());
        values.put(KEY_CREDIT_LIMIT, param.getLimit_kredit());
        values.put(KEY_NO_KTP, param.getNik());
        values.put(KEY_NO_NPWP, param.getNo_npwp());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_NON_ROUTE_CUSTOMER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addNonRouteCustomerPromotion(Promotion param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB, idHeader);
        values.put(KEY_CUSTOMER_ID, param.getIdCustomer());
        values.put(KEY_ID_PROMOTION, param.getId());
        values.put(KEY_NAME_PROMOTION, param.getNama_promo());
        values.put(KEY_NO_PROMOTION, param.getNo_promo());
        values.put(KEY_DESC, param.getKeterangan());
        values.put(KEY_TOLERANSI, param.getToleransi());
        values.put(KEY_JENIS_PROMOSI, param.getJenis_promosi());
        values.put(KEY_SEGMEN, param.getSegmen());
        values.put(KEY_VALID_FROM_PROMOTION, param.getValid_from());
        values.put(KEY_VALID_TO_PROMOTION, param.getValid_to());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
//        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addNonRouteCustomerDct(Material param, String idHeader, String idSales, String idCust) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB, idHeader);
        values.put(KEY_CUSTOMER_ID, idCust);
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_TARGET, param.getTarget());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
//        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_NON_ROUTE_CUSTOMER_DCT, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addCustomerPromotion(Promotion param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, idHeader);
        values.put(KEY_ID_PROMOTION, param.getId());
        values.put(KEY_NAME_PROMOTION, param.getNama_promo());
        values.put(KEY_NO_PROMOTION, param.getNo_promo());
        values.put(KEY_DESC, param.getKeterangan());
        values.put(KEY_TOLERANSI, param.getToleransi());
        values.put(KEY_JENIS_PROMOSI, param.getJenis_promosi());
        values.put(KEY_SEGMEN, param.getSegmen());
        values.put(KEY_VALID_FROM_PROMOTION, param.getValid_from());
        values.put(KEY_VALID_TO_PROMOTION, param.getValid_to());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
//        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_CUSTOMER_PROMOTION, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addCustomerDct(Material param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, idHeader);
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_TARGET, param.getTarget());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
//        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_CUSTOMER_DCT, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addVisitSalesmanAll(VisitSalesman param) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, param.getCustomerId());
        values.put(KEY_ID_SALESMAN, param.getIdSalesman());
        values.put(KEY_ID_VISIT, param.getIdVisit());
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_CHECK_IN_TIME, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_INSIDE, param.isInside());
        values.put(KEY_INSIDE_CHECK_OUT, param.isInsideCheckOut());
        values.put(KEY_LAT_CHECK_IN, param.getLatCheckIn());
        values.put(KEY_LONG_CHECK_IN, param.getLongCheckIn());
        values.put(KEY_LAT_CHECK_OUT, param.getLatCheckOut());
        values.put(KEY_LONG_CHECK_OUT, param.getLongCheckOut());
        values.put(KEY_ID_NOT_VISIT_REASON, param.getIdNotVisitReason());
        values.put(KEY_NAME_NOT_VISIT_REASON, param.getNameNotVisitReason());
        values.put(KEY_DESC_NOT_VISIT_REASON, param.getDescNotVisitReason());
//        values.put(KEY_PHOTO_NOT_VISIT_REASON, param.getPhotoNotVisitReason());
//        values.put(KEY_ID_NOT_BUY_REASON, param.getIdNotBuyReason());
//        values.put(KEY_NAME_NOT_BUY_REASON, param.getNameNotBuyReason());
//        values.put(KEY_DESC_NOT_BUY_REASON, param.getDescNotBuyReason());
//        values.put(KEY_PHOTO_NOT_BUY_REASON, param.getPhotoNotBuyReason());
        values.put(KEY_CREATED_BY, param.getIdSalesman());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_VISIT_SALESMAN, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addVisitSalesman(VisitSalesman param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_VISIT_SALESMAN_DB, param.getIdHeader());
        values.put(KEY_ID_VISIT, param.getIdVisit());
        values.put(KEY_CUSTOMER_ID, param.getCustomerId());
        values.put(KEY_ID_SALESMAN, param.getIdSalesman());
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_CHECK_IN_TIME, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_LAT_CHECK_IN, param.getLatCheckIn());
        values.put(KEY_LONG_CHECK_IN, param.getLongCheckIn());
        values.put(KEY_INSIDE, param.isInside());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_VISIT_SALESMAN, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addStoreCheck(Material param, Map header) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, header.get("id_customer").toString());
        values.put(KEY_DATE, header.get("date").toString());
        values.put(KEY_ID_MOBILE, header.get("id_header").toString());
        values.put(KEY_MATERIAL_ID, param.getId());
        values.put(KEY_MATERIAL_NAME, param.getNama());
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
        values.put(KEY_MATERIAL_PRODUCT_NAME, param.getName_product_group());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_CREATED_BY, header.get("username").toString());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_STORE_CHECK, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public boolean addOrder(Order request, User user) {
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransactionNonExclusive();
        ContentValues values = new ContentValues();

        try {
            values = new ContentValues();
            values.put(KEY_ID_ORDER_HEADER_DB, request.getIdHeader());
            values.put(KEY_ID_ORDER_BACK_END, request.getId());
            values.put(KEY_CUSTOMER_ID, request.getId_customer());
            values.put(KEY_ORDER_TYPE, user.getType_sales().equals("CO") ? "CO" : "TO");
            values.put(KEY_TOP, request.getTop());
            values.put(KEY_ID_STOCK_REQUEST_HEADER_DB, request.getIdStockHeaderDb());
            values.put(KEY_ID_STOCK_REQUEST_HEADER_BE, request.getIdStockHeaderBE());
            values.put(KEY_DATE, request.getOrder_date());
            values.put(KEY_TANGGAL_KIRIM, request.getTanggal_kirim());
            values.put(KEY_OMZET, request.getOmzet());
            values.put(KEY_STATUS, request.getStatus());
            values.put(KEY_IS_PAID, request.isStatusPaid() ? 1 : 0);
            values.put(KEY_CREATED_BY, user.getUsername());
            values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
            values.put(KEY_IS_SYNC, 0); //0 false, 1 true

            db.insert(TABLE_ORDER_HEADER, null, values);//return id yg ud d create

            if (Helper.isNotEmptyOrNull(request.getMaterialList())) {
                for (Material param : request.getMaterialList()) {
                    int idOrderDetail = -1;
                    values = new ContentValues();
                    values.put(KEY_ID_ORDER_HEADER_DB, request.getIdHeader());
                    values.put(KEY_CUSTOMER_ID, request.getId_customer());
                    values.put(KEY_MATERIAL_ID, param.getId());
                    values.put(KEY_MATERIAL_NAME, param.getNama());
                    values.put(KEY_PRICE_LIST_CODE, param.getPriceListCode());
                    values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
                    values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
                    values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
                    values.put(KEY_MATERIAL_PRODUCT_NAME, param.getName_product_group());
                    values.put(KEY_ID_STOCK_REQUEST_HEADER_DB, request.getIdStockHeaderDb());
                    values.put(KEY_ID_STOCK_REQUEST_HEADER_BE, request.getIdStockHeaderBE());
                    values.put(KEY_QTY, param.getQty());
                    values.put(KEY_UOM, param.getUom());
                    values.put(KEY_PRICE, param.getPrice());
                    values.put(KEY_TOTAL_DISCOUNT, param.getTotalDiscount());
                    values.put(KEY_TOTAL, param.getPrice() - param.getTotalDiscount());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                    idOrderDetail = (int) db.insert(TABLE_ORDER_DETAIL, null, values);//return id yg ud d create

                    if (Helper.isNotEmptyOrNull(param.getDiskonList())) {
                        for (Discount discount : param.getDiskonList()) {
                            values = new ContentValues();
                            values.put(KEY_ID_ORDER_DETAIL_DB, idOrderDetail);
                            values.put(KEY_ID_ORDER_HEADER_DB, request.getIdHeader());
                            values.put(KEY_CUSTOMER_ID, request.getId_customer());
                            values.put(KEY_MATERIAL_ID, param.getId());
                            values.put(KEY_DISCOUNT_ID, discount.getKeydiskon());
                            values.put(KEY_DISCOUNT_NAME, discount.getKeydiskon());
                            values.put(KEY_DISCOUNT_PRICE, discount.getValuediskon());
                            values.put(KEY_CREATED_BY, user.getUsername());
                            values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                            values.put(KEY_IS_SYNC, 0); //0 false, 1 true
                            int idDiscount = (int) db.insert(TABLE_ORDER_DETAIL_DISCOUNT, null, values);
                        }
                    }

                    if (Helper.isNotEmptyOrNull(param.getExtraItem())) {
                        for (Material matExtra : param.getExtraItem()) {
                            values = new ContentValues();
                            values.put(KEY_ID_ORDER_DETAIL_DB, idOrderDetail);
                            values.put(KEY_ID_ORDER_HEADER_DB, request.getIdHeader());
                            values.put(KEY_CUSTOMER_ID, request.getId_customer());
                            values.put(KEY_MATERIAL_ID, matExtra.getId());
                            values.put(KEY_MATERIAL_NAME, matExtra.getNama());
                            values.put(KEY_MATERIAL_GROUP_ID, matExtra.getId_material_group());
                            values.put(KEY_MATERIAL_GROUP_NAME, matExtra.getMaterial_group_name());
                            values.put(KEY_MATERIAL_PRODUCT_ID, matExtra.getId_product_group());
                            values.put(KEY_MATERIAL_PRODUCT_NAME, matExtra.getName_product_group());
                            values.put(KEY_QTY, matExtra.getQty());
                            values.put(KEY_UOM, matExtra.getUom());
                            values.put(KEY_PRICE, matExtra.getPrice());
                            values.put(KEY_TOTAL_DISCOUNT, matExtra.getTotalDiscount());
                            values.put(KEY_TOTAL, matExtra.getTotal());
                            values.put(KEY_CREATED_BY, user.getUsername());
                            values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                            values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                            int idExtra = (int) db.insert(TABLE_ORDER_DETAIL_EXTRA, null, values);//return id yg ud d create
                        }
                    }
                }
            }
            db.setTransactionSuccessful();
            result = true;
        } catch (Exception e) {
            Log.e("Order", e.getMessage());
        }

        db.endTransaction();
        return result;
    }

    public int addOrderHeader(Order param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_BACK_END, param.getId());
        values.put(KEY_CUSTOMER_ID, param.getId_customer());
//        values.put(KEY_ORDER_TYPE, user.getType_sales().equals("CO") ?"CO" :"TO");
        values.put(KEY_DATE, param.getOrder_date());
        values.put(KEY_OMZET, param.getOmzet());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_HEADER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderDetail(Material param, Map detailHeader) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, detailHeader.get("idHeader").toString());
        values.put(KEY_CUSTOMER_ID, detailHeader.get("idCust").toString());
        values.put(KEY_MATERIAL_ID, param.getId());
        values.put(KEY_MATERIAL_NAME, param.getNama());
        values.put(KEY_PRICE_LIST_CODE, param.getPriceListCode());
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
        values.put(KEY_MATERIAL_PRODUCT_NAME, param.getName_product_group());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_PRICE, param.getPrice());
        values.put(KEY_TOTAL_DISCOUNT, param.getTotalDiscount());
        values.put(KEY_TOTAL, param.getTotal());
        values.put(KEY_CREATED_BY, detailHeader.get("idSales").toString());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_DETAIL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderDetailExtra(Material param, Map detailHeader) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_DETAIL_DB, detailHeader.get("idHeader").toString());
        values.put(KEY_CUSTOMER_ID, detailHeader.get("idCust").toString());
        values.put(KEY_MATERIAL_ID, param.getId());
        values.put(KEY_MATERIAL_NAME, param.getNama());
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
        values.put(KEY_MATERIAL_PRODUCT_NAME, param.getName_product_group());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_PRICE, param.getPrice());
        values.put(KEY_TOTAL_DISCOUNT, param.getTotalDiscount());
        values.put(KEY_TOTAL, param.getTotal());
        values.put(KEY_CREATED_BY, detailHeader.get("idSales").toString());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_DETAIL_EXTRA, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderDetailDiscount(Discount param, String idHeader, String idCust, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_DETAIL_DB, idHeader);
        values.put(KEY_CUSTOMER_ID, idCust);
        values.put(KEY_MATERIAL_ID, param.getKeydiskon());
        values.put(KEY_DISCOUNT_ID, param.getKeydiskon());
        values.put(KEY_DISCOUNT_NAME, param.getKeydiskon());
        values.put(KEY_DISCOUNT_PRICE, param.getValuediskon());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_DETAIL_DISCOUNT, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addReturn(Material param, Map header) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, header.get("id_customer").toString());
        values.put(KEY_DATE, header.get("date").toString());
        values.put(KEY_ID_MOBILE, header.get("id_header").toString());
        values.put(KEY_MATERIAL_ID, param.getId());
        values.put(KEY_MATERIAL_NAME, param.getNama());
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
        values.put(KEY_MATERIAL_PRODUCT_NAME, param.getName_product_group());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_EXPIRED_DATE, param.getExpiredDate());
        values.put(KEY_CONDITION, param.getCondition());
        values.put(KEY_ID_REASON_RETURN, param.getIdReason());
        values.put(KEY_NAME_REASON_RETURN, param.getNameReason());
        values.put(KEY_DESC_REASON_RETURN, param.getDescReason());
//        values.put(KEY_PHOTO_REASON_RETURN, param.getPhotoReason());
        values.put(KEY_CREATED_BY, header.get("username").toString());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_RETURN, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }
    //TABLE_COLLECTION_HEADER, TABLE_COLLECTION_DETAIL, TABLE_COLLECTION_ITEM

    public int addCollectionHeader(Map param) {
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        /*
         * According to the docs http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
         * Writers should use beginTransactionNonExclusive() or beginTransactionWithListenerNonExclusive(SQLiteTransactionListener)
         * to start a transaction. Non-exclusive mode allows database file to be in readable by other threads executing queries.
         */
        db.beginTransactionNonExclusive();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, param.get("customer_id").toString());
        values.put(KEY_INVOICE_NO, param.get("no_invoice").toString());
        values.put(KEY_INVOICE_DATE, param.get("invoice_date").toString());
        values.put(KEY_STATUS, param.get("status").toString());
        values.put(KEY_INVOICE_TOTAL, (Double) param.get("amount"));
        values.put(KEY_TOTAL_PAID, (Double) param.get("total_paid"));
        values.put(KEY_CREATED_BY, param.get("username").toString());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0);

        int id = -1;
        try {
            id = (int) db.insert(TABLE_COLLECTION_HEADER, null, values);//return id yg ud d create
            db.setTransactionSuccessful();
            result = true;
        } catch (Exception e) {
            result = false;
            id = -1;
        }
        //db.close();
        db.endTransaction();
        return id;
    }

    public boolean addCollectionOrder(Map request) {
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransactionNonExclusive();

        int idCollHeader = -1;
        try {
            User user = Helper.ObjectToGSON(request.get("user"), User.class);
            Order header = Helper.ObjectToGSON(request.get("header"), Order.class);
            double totalAmountPaid = Helper.ObjectToGSON(request.get("totalAmountPaid"), double.class);
            double totalPaymentCash = Helper.ObjectToGSON(request.get("totalPaymentCash"), double.class);
            double leftCash = Helper.ObjectToGSON(request.get("leftCash"), double.class);

            List<Material> cashList = new ArrayList<>();
            Material[] cashListArray = Helper.ObjectToGSON(request.get("cashList"), Material[].class);
            if (cashListArray != null) {
                Collections.addAll(cashList, cashListArray);
            }

            double totalPaymentLain = Helper.ObjectToGSON(request.get("totalPaymentLain"), double.class);
            double leftLain = Helper.ObjectToGSON(request.get("leftLain"), double.class);

            List<Material> lainList = new ArrayList<>();
            Material[] lainListArray = Helper.ObjectToGSON(request.get("lainList"), Material[].class);
            if (lainListArray != null) {
                Collections.addAll(lainList, lainListArray);
            }

            List<Material> tfList = new ArrayList<>();
            Material[] tfListArray = Helper.ObjectToGSON(request.get("tfList"), Material[].class);
            if (tfListArray != null) {
                Collections.addAll(tfList, tfListArray);
            }

            List<CollectionDetail> mListTransfer = new ArrayList<>();
            CollectionDetail[] mListTransferArray = Helper.ObjectToGSON(request.get("mListTransfer"), CollectionDetail[].class);
            if (mListTransferArray != null) {
                Collections.addAll(mListTransfer, mListTransferArray);
            }

            List<Material> giroList = new ArrayList<>();
            Material[] giroArray = Helper.ObjectToGSON(request.get("giroList"), Material[].class);
            if (giroArray != null) {
                Collections.addAll(giroList, giroArray);
            }

            List<CollectionDetail> mListGiro = new ArrayList<>();
            CollectionDetail[] mListGiroArray = Helper.ObjectToGSON(request.get("mListGiro"), CollectionDetail[].class);
            if (mListGiroArray != null) {
                Collections.addAll(mListGiro, mListGiroArray);
            }

            List<Material> chequeList = new ArrayList<>();
            Material[] chequeListArray = Helper.ObjectToGSON(request.get("chequeList"), Material[].class);
            if (chequeListArray != null) {
                Collections.addAll(chequeList, chequeListArray);
            }

            List<CollectionDetail> mListCheque = new ArrayList<>();
            CollectionDetail[] mListChequeArray = Helper.ObjectToGSON(request.get("mListCheque"), CollectionDetail[].class);
            if (mListChequeArray != null) {
                Collections.addAll(mListCheque, mListChequeArray);
            }

            List<Material> mListCash = new ArrayList<>();
            Material[] mListCashArray = Helper.ObjectToGSON(request.get("mListCash"), Material[].class);
            if (mListCashArray != null) {
                Collections.addAll(mListCash, mListCashArray);
            }

            Map requestHeader = new HashMap();
            requestHeader.put("id_header", Constants.ID_CO_MOBILE.concat(user.getUsername()).concat(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
            requestHeader.put("customer_id", header.getId_customer());
            requestHeader.put("no_invoice", header.getIdHeader());
            requestHeader.put("invoice_date", header.getOrder_date());
            requestHeader.put("date", Helper.getTodayDate(Constants.DATE_FORMAT_3));
            requestHeader.put("status", "paid");
            requestHeader.put("total_paid", totalAmountPaid);
            requestHeader.put("amount", header.getOmzet());
            requestHeader.put("username", user.getUsername());
            requestHeader.put("isSync", request.get("isSync"));
            requestHeader.put("isPaid", request.get("isPaid"));

            //addCollectionHeader
            //int idCollHeader = database.addCollectionHeader(requestHeader);
            ContentValues values = new ContentValues();
            values.put(KEY_ID_COLLECTION_HEADER_DB, requestHeader.get("id_header").toString());
            values.put(KEY_CUSTOMER_ID, requestHeader.get("customer_id").toString());
            values.put(KEY_INVOICE_NO, requestHeader.get("no_invoice").toString());
            values.put(KEY_INVOICE_DATE, requestHeader.get("invoice_date").toString());
            values.put(KEY_DATE, requestHeader.get("date").toString());
            values.put(KEY_STATUS, requestHeader.get("status").toString());
            values.put(KEY_INVOICE_TOTAL, (Double) requestHeader.get("amount"));
            values.put(KEY_TOTAL_PAID, (Double) requestHeader.get("total_paid"));
            values.put(KEY_CREATED_BY, requestHeader.get("username").toString());
            values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
            values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync"));

//            idCollHeaderA = (int) db.insert(TABLE_COLLECTION_HEADER, null, values);//return id yg ud d create
            db.insert(TABLE_COLLECTION_HEADER, null, values);//return id yg ud d create

            Map requestDetail = new HashMap();
            requestDetail.put("id_header", requestHeader.get("id_header").toString());
            requestDetail.put("no_invoice", header.getIdHeader());
            requestDetail.put("status", "paid");
            requestDetail.put("username", user.getUsername());

            if (cashList.size() != 0) {
                requestDetail.put("type_payment", "cash");
                requestDetail.put("total_payment", totalPaymentCash);
                requestDetail.put("left", leftCash);
//            int idDetail = database.addCollectionCashLain(requestDetail);
                values = new ContentValues();
                values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                values.put(KEY_INVOICE_NO, requestDetail.get("no_invoice").toString());
                values.put(KEY_STATUS, requestDetail.get("status").toString());
                values.put(KEY_TYPE_PAYMENT, requestDetail.get("type_payment").toString());
                values.put(KEY_TOTAL_PAYMENT, (Double) requestDetail.get("total_payment"));
                values.put(KEY_LEFT, (Double) requestDetail.get("left"));
                values.put(KEY_CREATED_BY, requestDetail.get("username").toString());
                values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                int idDetail = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                for (Material material : cashList) {
//                database.addCollectionMaterial(material, String.valueOf(idDetail), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetail));
                    values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                    values.put(KEY_MATERIAL_ID, material.getId());
                    values.put(KEY_MATERIAL_NAME, material.getNama());
                    values.put(KEY_MATERIAL_GROUP_ID, material.getId_material_group());
                    values.put(KEY_MATERIAL_GROUP_NAME, material.getMaterial_group_name());
                    values.put(KEY_MATERIAL_PRODUCT_ID, material.getId_product_group());
                    values.put(KEY_MATERIAL_PRODUCT_NAME, material.getName_product_group());
                    values.put(KEY_PRICE, material.getNett());
                    values.put(KEY_AMOUNT_PAID, material.getAmountPaid());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                    int idItem = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                }
            }

            if (lainList.size() != 0) {
                requestDetail.put("type_payment", "lain");
                requestDetail.put("total_payment", totalPaymentLain);
                requestDetail.put("left", leftLain);

//            int idDetail = database.addCollectionCashLain(requestDetail);
                values = new ContentValues();
                values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                values.put(KEY_INVOICE_NO, requestDetail.get("no_invoice").toString());
                values.put(KEY_STATUS, requestDetail.get("status").toString());
                values.put(KEY_TYPE_PAYMENT, requestDetail.get("type_payment").toString());
                values.put(KEY_TOTAL_PAYMENT, (Double) requestDetail.get("total_payment"));
                values.put(KEY_LEFT, (Double) requestDetail.get("left"));
                values.put(KEY_CREATED_BY, requestDetail.get("username").toString());
                values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                int idDetailLain = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                for (Material material : lainList) {
//                database.addCollectionMaterial(material, String.valueOf(idDetailLain), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailLain));
                    values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                    values.put(KEY_MATERIAL_ID, material.getId());
                    values.put(KEY_MATERIAL_NAME, material.getNama());
                    values.put(KEY_MATERIAL_GROUP_ID, material.getId_material_group());
                    values.put(KEY_MATERIAL_GROUP_NAME, material.getMaterial_group_name());
                    values.put(KEY_MATERIAL_PRODUCT_ID, material.getId_product_group());
                    values.put(KEY_MATERIAL_PRODUCT_NAME, material.getName_product_group());
                    values.put(KEY_PRICE, material.getNett());
                    values.put(KEY_AMOUNT_PAID, material.getAmountPaid());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                    int idItemLain = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                }
            }

            if (tfList.size() != 0) {
                for (CollectionDetail collection : mListTransfer) {
                    collection.setInvoiceNo(header.getIdHeader());
//                int idDetail = database.addCollectionTransfer(collection, String.valueOf(idCollHeader), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                    values.put(KEY_INVOICE_NO, collection.getInvoiceNo());
                    values.put(KEY_TYPE_PAYMENT, "transfer");
                    values.put(KEY_TOTAL_PAYMENT, collection.getTotalPayment());
                    values.put(KEY_LEFT, collection.getLeft());
                    values.put(KEY_DATE, collection.getTgl());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                    int idDetailTf = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                    for (Material material : collection.getCheckedMaterialList()) {
//                    database.addCollectionMaterial(material, String.valueOf(idDetailTf), user.getUsername());
                        values = new ContentValues();
                        values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailTf));
                        values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                        values.put(KEY_MATERIAL_ID, material.getId());
                        values.put(KEY_MATERIAL_NAME, material.getNama());
                        values.put(KEY_MATERIAL_GROUP_ID, material.getId_material_group());
                        values.put(KEY_MATERIAL_GROUP_NAME, material.getMaterial_group_name());
                        values.put(KEY_MATERIAL_PRODUCT_ID, material.getId_product_group());
                        values.put(KEY_MATERIAL_PRODUCT_NAME, material.getName_product_group());
                        values.put(KEY_PRICE, material.getNett());
                        values.put(KEY_AMOUNT_PAID, material.getAmountPaid());
                        values.put(KEY_CREATED_BY, user.getUsername());
                        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                        values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                        int idItemTf = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                    }
                }
            }

            if (giroList.size() != 0) {
                for (CollectionDetail collection : mListGiro) {
                    collection.setInvoiceNo(header.getIdHeader());
//                int idDetail = database.addCollectionGiro(collection, String.valueOf(idCollHeader), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                    values.put(KEY_INVOICE_NO, collection.getInvoiceNo());
                    values.put(KEY_TYPE_PAYMENT, "giro");
                    values.put(KEY_TOTAL_PAYMENT, collection.getTotalPayment());
                    values.put(KEY_LEFT, collection.getLeft());
                    values.put(KEY_NO, collection.getNo());
                    values.put(KEY_DATE, collection.getTgl());
                    values.put(KEY_DUE_DATE, collection.getTglCair());
                    values.put(KEY_ID_BANK, collection.getIdBankASPP());
                    values.put(KEY_NAME_BANK, collection.getBankNameASPP());
                    values.put(KEY_ID_CUST_BANK, collection.getIdBankCust());
                    values.put(KEY_NAME_CUST_BANK, collection.getBankCust());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                    int idDetailGiro = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                    for (Material material : collection.getCheckedMaterialList()) {
//                    database.addCollectionMaterial(material, String.valueOf(idDetailGiro), user.getUsername());
                        values = new ContentValues();
                        values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailGiro));
                        values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                        values.put(KEY_MATERIAL_ID, material.getId());
                        values.put(KEY_MATERIAL_NAME, material.getNama());
                        values.put(KEY_MATERIAL_GROUP_ID, material.getId_material_group());
                        values.put(KEY_MATERIAL_GROUP_NAME, material.getMaterial_group_name());
                        values.put(KEY_MATERIAL_PRODUCT_ID, material.getId_product_group());
                        values.put(KEY_MATERIAL_PRODUCT_NAME, material.getName_product_group());
                        values.put(KEY_PRICE, material.getNett());
                        values.put(KEY_AMOUNT_PAID, material.getAmountPaid());
                        values.put(KEY_CREATED_BY, user.getUsername());
                        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                        values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                        int idItemGiro = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                    }
                }
            }

            if (chequeList.size() != 0) {
                for (CollectionDetail collection : mListCheque) {
                    collection.setInvoiceNo(header.getIdHeader());
//                int idDetail = database.addCollectionCheque(collection, String.valueOf(idCollHeader), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                    values.put(KEY_INVOICE_NO, collection.getInvoiceNo());
                    values.put(KEY_TYPE_PAYMENT, "cheque");
                    values.put(KEY_TOTAL_PAYMENT, collection.getTotalPayment());
                    values.put(KEY_LEFT, collection.getLeft());
                    values.put(KEY_NO, collection.getNo());
                    values.put(KEY_DATE, collection.getTgl());
                    values.put(KEY_DUE_DATE, collection.getTglCair());
                    values.put(KEY_ID_BANK, collection.getIdBankASPP());
                    values.put(KEY_NAME_BANK, collection.getBankNameASPP());
                    values.put(KEY_ID_CUST_BANK, collection.getIdBankCust());
                    values.put(KEY_NAME_CUST_BANK, collection.getBankCust());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                    int idDetailCheque = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                    for (Material material : collection.getCheckedMaterialList()) {
//                    database.addCollectionMaterial(material, String.valueOf(idDetailCheque), user.getUsername());
                        values = new ContentValues();
                        values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailCheque));
                        values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                        values.put(KEY_MATERIAL_ID, material.getId());
                        values.put(KEY_MATERIAL_NAME, material.getNama());
                        values.put(KEY_MATERIAL_GROUP_ID, material.getId_material_group());
                        values.put(KEY_MATERIAL_GROUP_NAME, material.getMaterial_group_name());
                        values.put(KEY_MATERIAL_PRODUCT_ID, material.getId_product_group());
                        values.put(KEY_MATERIAL_PRODUCT_NAME, material.getName_product_group());
                        values.put(KEY_PRICE, material.getNett());
                        values.put(KEY_AMOUNT_PAID, material.getAmountPaid());
                        values.put(KEY_CREATED_BY, user.getUsername());
                        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                        values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                        int idItemCheque = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                    }
                }
            }

            values = new ContentValues();
            values.put(KEY_IS_PAID, (int) request.get("isPaid"));
            values.put(KEY_UPDATED_BY, user.getUsername());
            values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

            db.update(TABLE_ORDER_HEADER, values, KEY_ID_ORDER_HEADER_DB + " = ?", new String[]{header.getIdHeader()});

            result = true;
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Collection", e.getMessage());
            result = false;
        }
        db.endTransaction();
        return result;
        //addCollectionHeader
    }

    public boolean addCollection(Map request) {
        boolean result = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransactionNonExclusive();

        int idCollHeader = -1;
        try {
            User user = Helper.ObjectToGSON(request.get("user"), User.class);
            Invoice header = Helper.ObjectToGSON(request.get("header"), Invoice.class);
            double totalAmountPaid = Helper.ObjectToGSON(request.get("totalAmountPaid"), double.class);
            double totalPaymentCash = Helper.ObjectToGSON(request.get("totalPaymentCash"), double.class);
            double leftCash = Helper.ObjectToGSON(request.get("leftCash"), double.class);

            List<Material> cashList = new ArrayList<>();
            Material[] cashListArray = Helper.ObjectToGSON(request.get("cashList"), Material[].class);
            if (cashListArray != null) {
                Collections.addAll(cashList, cashListArray);
            }

            double totalPaymentLain = Helper.ObjectToGSON(request.get("totalPaymentLain"), double.class);
            double leftLain = Helper.ObjectToGSON(request.get("leftLain"), double.class);

            List<Material> lainList = new ArrayList<>();
            Material[] lainListArray = Helper.ObjectToGSON(request.get("lainList"), Material[].class);
            if (lainListArray != null) {
                Collections.addAll(lainList, lainListArray);
            }

            List<Material> tfList = new ArrayList<>();
            Material[] tfListArray = Helper.ObjectToGSON(request.get("tfList"), Material[].class);
            if (tfListArray != null) {
                Collections.addAll(tfList, tfListArray);
            }

            List<CollectionDetail> mListTransfer = new ArrayList<>();
            CollectionDetail[] mListTransferArray = Helper.ObjectToGSON(request.get("mListTransfer"), CollectionDetail[].class);
            if (mListTransferArray != null) {
                Collections.addAll(mListTransfer, mListTransferArray);
            }

            List<Material> giroList = new ArrayList<>();
            Material[] giroArray = Helper.ObjectToGSON(request.get("giroList"), Material[].class);
            if (giroArray != null) {
                Collections.addAll(giroList, giroArray);
            }

            List<CollectionDetail> mListGiro = new ArrayList<>();
            CollectionDetail[] mListGiroArray = Helper.ObjectToGSON(request.get("mListGiro"), CollectionDetail[].class);
            if (mListGiroArray != null) {
                Collections.addAll(mListGiro, mListGiroArray);
            }

            List<Material> chequeList = new ArrayList<>();
            Material[] chequeListArray = Helper.ObjectToGSON(request.get("chequeList"), Material[].class);
            if (chequeListArray != null) {
                Collections.addAll(chequeList, chequeListArray);
            }

            List<CollectionDetail> mListCheque = new ArrayList<>();
            CollectionDetail[] mListChequeArray = Helper.ObjectToGSON(request.get("mListCheque"), CollectionDetail[].class);
            if (mListChequeArray != null) {
                Collections.addAll(mListCheque, mListChequeArray);
            }

            List<Material> mListCash = new ArrayList<>();
            Material[] mListCashArray = Helper.ObjectToGSON(request.get("mListCash"), Material[].class);
            if (mListCashArray != null) {
                Collections.addAll(mListCash, mListCashArray);
            }

            Map requestHeader = new HashMap();
            requestHeader.put("id_header", Constants.ID_CI_MOBILE.concat(user.getUsername()).concat(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime())));
            requestHeader.put("customer_id", header.getId_customer());
            requestHeader.put("no_invoice", header.getNo_invoice());
            requestHeader.put("invoice_date", header.getInvoice_date());
            requestHeader.put("status", "paid");
            requestHeader.put("total_paid", totalAmountPaid);
            requestHeader.put("amount", header.getAmount());
            requestHeader.put("username", user.getUsername());
            requestHeader.put("isSync", header.getIsSync());

            //addCollectionHeader
            //int idCollHeader = database.addCollectionHeader(requestHeader);
            ContentValues values = new ContentValues();
            values.put(KEY_ID_COLLECTION_HEADER_DB, requestHeader.get("id_header").toString());
            values.put(KEY_CUSTOMER_ID, requestHeader.get("customer_id").toString());
            values.put(KEY_INVOICE_NO, requestHeader.get("no_invoice").toString());
            values.put(KEY_INVOICE_DATE, requestHeader.get("invoice_date").toString());
            values.put(KEY_STATUS, requestHeader.get("status").toString());
            values.put(KEY_INVOICE_TOTAL, (Double) requestHeader.get("amount"));
            values.put(KEY_TOTAL_PAID, (Double) requestHeader.get("total_paid"));
            values.put(KEY_CREATED_BY, requestHeader.get("username").toString());
            values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
            values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync"));


            idCollHeader = (int) db.insert(TABLE_COLLECTION_HEADER, null, values);//return id yg ud d create

            //updatePaidInvoice
            //database.updatePaidInvoice(requestHeader);//update paid invoice header
            requestHeader.put("paid", header.getTotal_paid() + totalAmountPaid);
            requestHeader.put("nett", header.getAmount() - (header.getTotal_paid() + totalAmountPaid));

            values = new ContentValues();
            values.put(KEY_PAID, (Double) requestHeader.get("paid"));
            values.put(KEY_NETT, (Double) requestHeader.get("nett"));
            values.put(KEY_UPDATED_BY, requestHeader.get("username").toString());
            values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
            db.update(TABLE_INVOICE_HEADER, values, KEY_INVOICE_NO + " = ?", new String[]{requestHeader.get("no_invoice").toString()});
            //updatePaidInvoice

            Map requestDetail = new HashMap();
            requestDetail.put("id_header", requestHeader.get("id_header").toString());
            requestDetail.put("no_invoice", header.getNo_invoice());
            requestDetail.put("status", "paid");
            requestDetail.put("username", user.getUsername());

            if (cashList.size() != 0) {
                requestDetail.put("type_payment", "cash");
                requestDetail.put("total_payment", totalPaymentCash);
                requestDetail.put("left", leftCash);
//            int idDetail = database.addCollectionCashLain(requestDetail);
                values = new ContentValues();
                values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                values.put(KEY_INVOICE_NO, requestDetail.get("no_invoice").toString());
                values.put(KEY_STATUS, requestDetail.get("status").toString());
                values.put(KEY_TYPE_PAYMENT, requestDetail.get("type_payment").toString());
                values.put(KEY_TOTAL_PAYMENT, (Double) requestDetail.get("total_payment"));
                values.put(KEY_LEFT, (Double) requestDetail.get("left"));
                values.put(KEY_CREATED_BY, requestDetail.get("username").toString());
                values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                int idDetail = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                for (Material material : cashList) {
//                database.addCollectionMaterial(material, String.valueOf(idDetail), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetail));
                    values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                    values.put(KEY_MATERIAL_ID, material.getId());
                    values.put(KEY_MATERIAL_NAME, material.getNama());
                    values.put(KEY_MATERIAL_GROUP_ID, material.getId_material_group());
                    values.put(KEY_MATERIAL_GROUP_NAME, material.getMaterial_group_name());
                    values.put(KEY_MATERIAL_PRODUCT_ID, material.getId_product_group());
                    values.put(KEY_MATERIAL_PRODUCT_NAME, material.getName_product_group());
                    values.put(KEY_PRICE, material.getNett());
                    values.put(KEY_AMOUNT_PAID, material.getAmountPaid());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                    int idItem = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                }
            }

            if (lainList.size() != 0) {
                requestDetail.put("type_payment", "lain");
                requestDetail.put("total_payment", totalPaymentLain);
                requestDetail.put("left", leftLain);

//            int idDetail = database.addCollectionCashLain(requestDetail);
                values = new ContentValues();
                values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                values.put(KEY_INVOICE_NO, requestDetail.get("no_invoice").toString());
                values.put(KEY_STATUS, requestDetail.get("status").toString());
                values.put(KEY_TYPE_PAYMENT, requestDetail.get("type_payment").toString());
                values.put(KEY_TOTAL_PAYMENT, (Double) requestDetail.get("total_payment"));
                values.put(KEY_LEFT, (Double) requestDetail.get("left"));
                values.put(KEY_CREATED_BY, requestDetail.get("username").toString());
                values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                int idDetailLain = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                for (Material material : lainList) {
//                database.addCollectionMaterial(material, String.valueOf(idDetailLain), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailLain));
                    values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                    values.put(KEY_MATERIAL_ID, material.getId());
                    values.put(KEY_MATERIAL_NAME, material.getNama());
                    values.put(KEY_MATERIAL_GROUP_ID, material.getId_material_group());
                    values.put(KEY_MATERIAL_GROUP_NAME, material.getMaterial_group_name());
                    values.put(KEY_MATERIAL_PRODUCT_ID, material.getId_product_group());
                    values.put(KEY_MATERIAL_PRODUCT_NAME, material.getName_product_group());
                    values.put(KEY_PRICE, material.getNett());
                    values.put(KEY_AMOUNT_PAID, material.getAmountPaid());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                    int idItemLain = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                }
            }

            if (tfList.size() != 0) {
                for (CollectionDetail collection : mListTransfer) {
                    collection.setInvoiceNo(header.getNo_invoice());
//                int idDetail = database.addCollectionTransfer(collection, String.valueOf(idCollHeader), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                    values.put(KEY_INVOICE_NO, collection.getInvoiceNo());
                    values.put(KEY_TYPE_PAYMENT, "transfer");
                    values.put(KEY_STATUS, requestDetail.get("status").toString());
                    values.put(KEY_TOTAL_PAYMENT, collection.getTotalPayment());
                    values.put(KEY_LEFT, collection.getLeft());
                    values.put(KEY_DATE, collection.getTgl());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                    int idDetailTf = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                    for (Material material : collection.getCheckedMaterialList()) {
//                    database.addCollectionMaterial(material, String.valueOf(idDetailTf), user.getUsername());
                        values = new ContentValues();
                        values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailTf));
                        values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                        values.put(KEY_MATERIAL_ID, material.getId());
                        values.put(KEY_MATERIAL_NAME, material.getNama());
                        values.put(KEY_MATERIAL_GROUP_ID, material.getId_material_group());
                        values.put(KEY_MATERIAL_GROUP_NAME, material.getMaterial_group_name());
                        values.put(KEY_MATERIAL_PRODUCT_ID, material.getId_product_group());
                        values.put(KEY_MATERIAL_PRODUCT_NAME, material.getName_product_group());
                        values.put(KEY_PRICE, material.getNett());
                        values.put(KEY_AMOUNT_PAID, material.getAmountPaid());
                        values.put(KEY_CREATED_BY, user.getUsername());
                        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                        values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                        int idItemTf = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                    }
                }
            }

            if (giroList.size() != 0) {
                for (CollectionDetail collection : mListGiro) {
                    collection.setInvoiceNo(header.getNo_invoice());
//                int idDetail = database.addCollectionGiro(collection, String.valueOf(idCollHeader), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                    values.put(KEY_INVOICE_NO, collection.getInvoiceNo());
                    values.put(KEY_TYPE_PAYMENT, "giro");
                    values.put(KEY_STATUS, requestDetail.get("status").toString());
                    values.put(KEY_TOTAL_PAYMENT, collection.getTotalPayment());
                    values.put(KEY_LEFT, collection.getLeft());
                    values.put(KEY_NO, collection.getNo());
                    values.put(KEY_DATE, collection.getTgl());
                    values.put(KEY_DUE_DATE, collection.getTglCair());
                    values.put(KEY_ID_BANK, collection.getIdBankASPP());
                    values.put(KEY_NAME_BANK, collection.getBankNameASPP());
                    values.put(KEY_ID_CUST_BANK, collection.getIdBankCust());
                    values.put(KEY_NAME_CUST_BANK, collection.getBankCust());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                    int idDetailGiro = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                    for (Material material : collection.getCheckedMaterialList()) {
//                    database.addCollectionMaterial(material, String.valueOf(idDetailGiro), user.getUsername());
                        values = new ContentValues();
                        values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailGiro));
                        values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                        values.put(KEY_MATERIAL_ID, material.getId());
                        values.put(KEY_MATERIAL_NAME, material.getNama());
                        values.put(KEY_MATERIAL_GROUP_ID, material.getId_material_group());
                        values.put(KEY_MATERIAL_GROUP_NAME, material.getMaterial_group_name());
                        values.put(KEY_MATERIAL_PRODUCT_ID, material.getId_product_group());
                        values.put(KEY_MATERIAL_PRODUCT_NAME, material.getName_product_group());
                        values.put(KEY_PRICE, material.getNett());
                        values.put(KEY_AMOUNT_PAID, material.getAmountPaid());
                        values.put(KEY_CREATED_BY, user.getUsername());
                        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                        values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                        int idItemGiro = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                    }
                }
            }

            if (chequeList.size() != 0) {
                for (CollectionDetail collection : mListCheque) {
                    collection.setInvoiceNo(header.getNo_invoice());
//                int idDetail = database.addCollectionCheque(collection, String.valueOf(idCollHeader), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                    values.put(KEY_INVOICE_NO, collection.getInvoiceNo());
                    values.put(KEY_TYPE_PAYMENT, "cheque");
                    values.put(KEY_STATUS, requestDetail.get("status").toString());
                    values.put(KEY_TOTAL_PAYMENT, collection.getTotalPayment());
                    values.put(KEY_LEFT, collection.getLeft());
                    values.put(KEY_NO, collection.getNo());
                    values.put(KEY_DATE, collection.getTgl());
                    values.put(KEY_DUE_DATE, collection.getTglCair());
                    values.put(KEY_ID_BANK, collection.getIdBankASPP());
                    values.put(KEY_NAME_BANK, collection.getBankNameASPP());
                    values.put(KEY_ID_CUST_BANK, collection.getIdBankCust());
                    values.put(KEY_NAME_CUST_BANK, collection.getBankCust());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                    int idDetailCheque = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                    for (Material material : collection.getCheckedMaterialList()) {
//                    database.addCollectionMaterial(material, String.valueOf(idDetailCheque), user.getUsername());
                        values = new ContentValues();
                        values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailCheque));
                        values.put(KEY_ID_COLLECTION_HEADER_DB, requestDetail.get("id_header").toString());
                        values.put(KEY_MATERIAL_ID, material.getId());
                        values.put(KEY_MATERIAL_NAME, material.getNama());
                        values.put(KEY_MATERIAL_GROUP_ID, material.getId_material_group());
                        values.put(KEY_MATERIAL_GROUP_NAME, material.getMaterial_group_name());
                        values.put(KEY_MATERIAL_PRODUCT_ID, material.getId_product_group());
                        values.put(KEY_MATERIAL_PRODUCT_NAME, material.getName_product_group());
                        values.put(KEY_PRICE, material.getNett());
                        values.put(KEY_AMOUNT_PAID, material.getAmountPaid());
                        values.put(KEY_CREATED_BY, user.getUsername());
                        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                        values.put(KEY_IS_SYNC, (int) requestHeader.get("isSync")); //0 false, 1 true

                        int idItemCheque = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                    }
                }
            }

            if (Helper.isNotEmptyOrNull(mListCash)) {
                for (Material material : mListCash) {
//                database.updateNettPrice(material, user.getUsername(), header.getNo_invoice());//update paid invoice detail
                    values = new ContentValues();
                    double paid = getPaidInvoiceMaterial(header.getNo_invoice(), material.getId());
                    paid = paid + (material.getPrice() - material.getSisa());//material.getNett() - material.getSisa(),ambil semua jumlah paid nya
                    values.put(KEY_PAID, paid);
                    values.put(KEY_UPDATED_BY, user.getUsername());
                    values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

                    db.update(TABLE_INVOICE_DETAIL, values, KEY_INVOICE_NO + " = ? and "
                            + KEY_MATERIAL_ID + " = ?", new String[]{header.getNo_invoice(), material.getId()});
                    //db.close();
                }
            }
            result = true;
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Collection", e.getMessage());
            result = false;
        }
        db.endTransaction();
        return result;
        //addCollectionHeader
    }

    public double getPaidInvoiceMaterial(String invoiceNo, String idMaterial) {
        double result = 0.0;
        // Select All Query
        String selectQuery = "SELECT " + KEY_PAID + "  FROM " + TABLE_INVOICE_DETAIL + " WHERE " + KEY_INVOICE_NO + " = ? AND " + KEY_MATERIAL_ID + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{invoiceNo, idMaterial});

        if (cursor.moveToFirst()) {
            do {
                result = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public int addCollectionCashLain(Map param) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_COLLECTION_HEADER_DB, param.get("id_header").toString());
        values.put(KEY_INVOICE_NO, param.get("no_invoice").toString());
        values.put(KEY_STATUS, param.get("status").toString());
        values.put(KEY_TYPE_PAYMENT, param.get("type_payment").toString());
        values.put(KEY_TOTAL_PAYMENT, (Double) param.get("total_payment"));
        values.put(KEY_LEFT, (Double) param.get("left"));
        values.put(KEY_CREATED_BY, param.get("username").toString());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addCollectionTransfer(CollectionDetail param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_COLLECTION_HEADER_DB, idHeader);
        values.put(KEY_INVOICE_NO, param.getInvoiceNo());
        values.put(KEY_TYPE_PAYMENT, "transfer");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_DATE, param.getTgl());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addCollectionGiro(CollectionDetail param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_COLLECTION_HEADER_DB, idHeader);
        values.put(KEY_INVOICE_NO, param.getInvoiceNo());
        values.put(KEY_TYPE_PAYMENT, "giro");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_NO, param.getNo());
        values.put(KEY_DATE, param.getTgl());
        values.put(KEY_DUE_DATE, param.getTglCair());
        values.put(KEY_ID_BANK, param.getIdBankASPP());
        values.put(KEY_NAME_BANK, param.getBankNameASPP());
        values.put(KEY_ID_CUST_BANK, param.getIdBankCust());
        values.put(KEY_NAME_CUST_BANK, param.getBankCust());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addCollectionCheque(CollectionDetail param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_COLLECTION_HEADER_DB, idHeader);
        values.put(KEY_INVOICE_NO, param.getInvoiceNo());
        values.put(KEY_TYPE_PAYMENT, "cheque");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_NO, param.getNo());
        values.put(KEY_DATE, param.getTgl());
        values.put(KEY_DUE_DATE, param.getTglCair());
        values.put(KEY_ID_BANK, param.getIdBankASPP());
        values.put(KEY_NAME_BANK, param.getBankNameASPP());
        values.put(KEY_ID_CUST_BANK, param.getIdBankCust());
        values.put(KEY_NAME_CUST_BANK, param.getBankCust());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIsSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addCollectionMaterial(Material param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_COLLECTION_DETAIL_DB, idHeader);
        values.put(KEY_MATERIAL_ID, param.getId());
        values.put(KEY_MATERIAL_NAME, param.getNama());
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
        values.put(KEY_MATERIAL_PRODUCT_NAME, param.getName_product_group());
        values.put(KEY_PRICE, param.getNett());
        values.put(KEY_AMOUNT_PAID, param.getAmountPaid());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addMasterReason(Reason param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_REASON_BE, param.getId());
        values.put(KEY_NAME_REASON, param.getDescription());
        values.put(KEY_CATEGORY_REASON, param.getCategory());
        values.put(KEY_IS_PHOTO, param.getIs_photo());
        values.put(KEY_IS_FREE_TEXT, param.getIs_freetext());
        values.put(KEY_IS_BARCODE, param.getIs_barcode());
        values.put(KEY_IS_SIGNATURE, param.getIs_signature());
        values.put(KEY_CREATED_BY, idSales);

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_REASON, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addMasterPromotion(Promotion param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_PROMOTION, param.getId());
        values.put(KEY_NAME_PROMOTION, param.getNama_promo());
        values.put(KEY_NO_PROMOTION, param.getNo_promo());
        values.put(KEY_DESC, param.getKeterangan());
        values.put(KEY_TOLERANSI, param.getToleransi());
        values.put(KEY_JENIS_PROMOSI, param.getJenis_promosi());
        values.put(KEY_SEGMEN, param.getSegmen());
        values.put(KEY_VALID_FROM_PROMOTION, param.getValid_from());
        values.put(KEY_VALID_TO_PROMOTION, param.getValid_to());
        values.put(KEY_CREATED_BY, idSales);

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_PROMOTION, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addLog(WSMessage request) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DESC_LOG, request.getMessage());
        values.put(KEY_DATE_LOG, Helper.getTodayDate(Constants.DATE_FORMAT_3));
        values.put(KEY_TIME_LOG, Helper.getTodayDate(Constants.DATE_TYPE_6));
        values.put(KEY_CREATED_BY, SessionManagerQubes.getUserProfile() != null ? SessionManagerQubes.getUserProfile().getUsername() : null);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_LOG, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addMasterBank(Bank param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_BANK_BE, param.getId());
        values.put(KEY_NAME_BANK, param.getName());
        values.put(KEY_ID_DEPO, param.getId_depo());
        values.put(KEY_CATEGORY, param.getCategory());
        values.put(KEY_NO_REK, param.getNo_rek());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_BANK, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addMasterUom(Uom param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UOM, param.getId_uom());
        values.put(KEY_MATERIAL_ID, param.getId_material());
        values.put(KEY_CONVERSION, param.getConversion());
        values.put(KEY_QTY_MIN, param.getQty_min());
        values.put(KEY_QTY_MAX, param.getQty_max());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_UOM, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addMasterMaterial(Material param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATERIAL_ID, param.getId());
        values.put(KEY_MATERIAL_NAME, param.getNama());
        values.put(KEY_MATERIAL_SALES, param.getMaterial_sales());
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
        values.put(KEY_MATERIAL_PRODUCT_NAME, param.getName_product_group());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_MATERIAL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addMasterDaerahTingkat(DaerahTingkat param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_KODE_POS, param.getKode_pos());
        values.put(KEY_ID_DESA_KELURAHAN, param.getKode_kelurahan());
        values.put(KEY_NAME_DESA_KELURAHAN, param.getNama_kelurahan());
        values.put(KEY_ID_KECAMATAN, param.getKode_kecamatan());
        values.put(KEY_NAME_KECAMATAN, param.getNama_kecamatan());
        values.put(KEY_ID_KOTA_KABUPATEN, param.getKode_kabupaten());
        values.put(KEY_NAME_KOTA_KABUPATEN, param.getNama_kabupaten());
        values.put(KEY_ID_PROVINSI, param.getKode_provinsi());
        values.put(KEY_NAME_PROVINSI, param.getNama_provinsi());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_DAERAH_TINGKAT, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addMasterPriceCode(PriceCode param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
        values.put(KEY_UDF_5, param.getUdf_5());
        values.put(KEY_UDF_5_DESC, param.getUdf_5_desc());
        values.put(KEY_PRICE_LIST_CODE, param.getPrice_list_code());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_PRICE_CODE, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addMasterSalesPriceHeader(SalesPriceHeader param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOP, param.getTop());
        values.put(KEY_PRICE_LIST_CODE, param.getPrice_list_code());
        values.put(KEY_VALID_FROM, param.getValid_from());
        values.put(KEY_VALID_TO, param.getValid_to());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_SALES_PRICE_HEADER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public void addLimitBon(Material param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATERIAL_ID, param.getId());
        values.put(KEY_CUSTOMER_ID, param.getId_customer());
        values.put(KEY_BON_LIMIT, param.getMax_bon());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        try {
            db.insert(TABLE_MASTER_LIMIT_BON, null, values);//return id yg ud d create
        } catch (Exception e) {
            //done
        }
    }

    public int addMasterSalesPriceDetail(SalesPriceDetail param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATERIAL_ID, param.getId_material());
        values.put(KEY_PRICE_LIST_CODE, param.getPrice_list_code());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_QTY, param.getQty());
        values.put(SELLING_PRICE, param.getSelling_price());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_SALES_PRICE_DETAIL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addMasterCustomerType(CustomerType param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_CUSTOMER_TYPE, param.getId());
        values.put(KEY_ID_TYPE_PRICE, param.getType_price());
        values.put(KEY_NAME_TYPE_PRICE, param.getName());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_CUSTOMER_TYPE, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addMasterParameter(Parameter param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_KEY_PARAMETER, param.getKey());
        values.put(KEY_VALUE, param.getValue());
        values.put(KEY_DESC, param.getDescription());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_PARAMETER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public List<Promotion> getPromotionRouteByIdCustomer(String idCust) {
        List<Promotion> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER_PROMOTION + " WHERE " + KEY_CUSTOMER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCust});

        if (cursor.moveToFirst()) {
            do {
                Promotion paramModel = new Promotion();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CUSTOMER_PROMOTION_DB)));
                paramModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_PROMOTION)));
                paramModel.setIdCustomer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setNama_promo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROMOTION)));
                paramModel.setNo_promo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_PROMOTION)));
                paramModel.setKeterangan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC)));
                paramModel.setToleransi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TOLERANSI)));
                paramModel.setJenis_promosi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_JENIS_PROMOSI)));
                paramModel.setValid_from(cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALID_FROM_PROMOTION)));
                paramModel.setValid_to(cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALID_TO_PROMOTION)));
                paramModel.setSegmen(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SEGMEN)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    //get
    public List<Promotion> getPromotionNonRouteByIdCustomer(String idCust) {
        List<Promotion> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION + " WHERE " + KEY_CUSTOMER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCust});

        if (cursor.moveToFirst()) {
            do {
                Promotion paramModel = new Promotion();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_MASTER_NON_ROUTE_CUSTOMER_PROMOTION_DB)));
                paramModel.setIdParent(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB)));
                paramModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_PROMOTION)));
                paramModel.setIdCustomer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setNama_promo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROMOTION)));
                paramModel.setNo_promo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_PROMOTION)));
                paramModel.setKeterangan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC)));
                paramModel.setToleransi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TOLERANSI)));
                paramModel.setJenis_promosi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_JENIS_PROMOSI)));
                paramModel.setValid_from(cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALID_FROM_PROMOTION)));
                paramModel.setValid_to(cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALID_TO_PROMOTION)));
                paramModel.setSegmen(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SEGMEN)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getDctNonRouteByIdCustomer(String idCust) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_NON_ROUTE_CUSTOMER_DCT + " WHERE " + KEY_CUSTOMER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCust});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NON_ROUTE_CUSTOMER_TARGET_DB)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                paramModel.setTarget(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TARGET)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getDctByIdCustomer(String idCust) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER_DCT + " WHERE " + KEY_CUSTOMER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCust});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CUSTOMER_TARGET_DB)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                paramModel.setTarget(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TARGET)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllReturn(String idCust) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RETURN + " WHERE  " + KEY_CUSTOMER_ID + " = ?  order by " + KEY_ID_RETURN_DB + " asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCust});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_MOBILE)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                paramModel.setExpiredDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EXPIRED_DATE)));
                paramModel.setCondition(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONDITION)));
                paramModel.setIdReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_REASON_RETURN)));
                paramModel.setNameReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_REASON_RETURN)));
                paramModel.setDescReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_REASON_RETURN)));
                paramModel.setPhotoReason(getPhotoReturn(paramModel.getId_customer(), "return", paramModel.getId()));
//                paramModel.setPhotoReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_REASON_RETURN)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllStoreCheck(String idCust) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STORE_CHECK + " WHERE " + KEY_CUSTOMER_ID + " = ?  order by " + KEY_ID_STORE_CHECK_DB + " asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCust});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_MOBILE)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<StockRequest> getAllStockRequestHeader() {
        List<StockRequest> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_HEADER + " order by " + KEY_ID_STOCK_REQUEST_HEADER_DB + " desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                StockRequest paramModel = new StockRequest();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB)));
                paramModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_BE)));
                paramModel.setReq_date(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQUEST_DATE)));
                paramModel.setNo_doc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_DOC)));
                paramModel.setTanggal_kirim(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TANGGAL_KIRIM)));
                paramModel.setNo_surat_jalan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_SURAT_JALAN)));
                paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setIs_unloading(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_UNLOADING)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                paramModel.setIs_verif(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)));
//                paramModel.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public StockRequest getLastStockRequest() {
        StockRequest paramModel = new StockRequest();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_HEADER + " WHERE " + KEY_IS_UNLOADING + " = 0 and " + KEY_IS_VERIF + " = 1 order by " + KEY_ID_STOCK_REQUEST_HEADER_DB + " desc limit 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            paramModel = new StockRequest();
            paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB)));
            paramModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_BE)));
            paramModel.setReq_date(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQUEST_DATE)));
            paramModel.setNo_doc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_DOC)));
            paramModel.setTanggal_kirim(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TANGGAL_KIRIM)));
            paramModel.setNo_surat_jalan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_SURAT_JALAN)));
            paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
            paramModel.setIs_unloading(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_UNLOADING)));
            paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
            paramModel.setIs_verif(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)));

            String selectQueryDetail = "SELECT * FROM " + TABLE_STOCK_REQUEST_DETAIL + " WHERE " + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? ";
            cursor = db.rawQuery(selectQueryDetail, new String[]{paramModel.getIdHeader()});
            List<Material> arrayList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Material param = new Material();
                    param.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_DETAIL_DB)));
                    param.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                    param.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                    param.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                    param.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));

//                    Map req = new HashMap();
//                    req.put("idStockRequestHeaderDB", paramModel.getIdHeader());
//                    req.put("idStockRequestHeaderBE", paramModel.getId());
//                    req.put("idMaterial", param.getId());
//                    req.put("qty", param.getQty());
//                    req.put("uom", param.getQty());
//                    Material stockSisa = getStockSisa(req);
                    param.setQtySisa(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY_SISA)));
                    param.setUomSisa(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM_SISA)));
                    param.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                    arrayList.add(param);
                } while (cursor.moveToNext());
            }
            paramModel.setMaterialList(arrayList);
        }

        cursor.close();
        return paramModel;
    }

    public List<Material> getAllStockRequestDetailUnloading(String idHeader) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_DETAIL + " WHERE " + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idHeader});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_DETAIL_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                paramModel.setQtySisa(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY_SISA)));
                paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                paramModel.setUomSisa(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM_SISA)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));

//                Map req = new HashMap();
//                req.put("id_material", paramModel.getId());
//                req.put("id_header", idHeader);
//
//                double qty = getSumQtyByIdMaterial(req);
//
//                paramModel.setQtySisa(paramModel.getQty() - qty);
//                paramModel.setUomSisa(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllStockRequestDetail(String idHeader) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_DETAIL + " WHERE " + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idHeader});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_DETAIL_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public double getSumQtyByIdMaterial(Map request) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        double qty = 0;

//        select od.id_order_header, od.id_material, au.id_material, od.qty, od.uom, au.uom, au.conversion, (od.qty*au.conversion) as total
//        from order_detail od
//        join assign_uom au on od.id_material = au.id_material and od.uom = au.uom
//        where id_order_header in ('26','32');

        countQuery = "SELECT sum(" + KEY_QTY + ") as " + KEY_QTY
                + " FROM " + TABLE_ORDER_DETAIL
                + " WHERE " + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? and " + KEY_MATERIAL_ID + " = ? ";
        cursor = db.rawQuery(countQuery, new String[]{request.get("id_header").toString(), request.get("id_material").toString()});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                qty = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY));
            }
        }

        assert cursor != null;
        cursor.close();
        return qty;
    }

    public int getConversionMaterialUom(Map request) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        int qty = 0;

//        select od.id_order_header, od.id_material, au.id_material, od.qty, od.uom, au.uom, au.conversion, (od.qty*au.conversion) as total
//        from order_detail od
//        join assign_uom au on od.id_material = au.id_material and od.uom = au.uom
//        where id_order_header in ('26','32');

        countQuery = "SELECT " + KEY_CONVERSION + " from " + TABLE_MASTER_UOM + " where " + KEY_MATERIAL_ID + " = ? and " + KEY_UOM + " = ? ";
        cursor = db.rawQuery(countQuery, new String[]{request.get("id_material").toString(), request.get("uom").toString()});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                qty = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CONVERSION));
            }
        }

        assert cursor != null;
        cursor.close();
        return qty;
    }

    public Material getQtySmallUom(Material mat) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        int qty = 0;

        String smallUom = getSmallUom(mat.getId());

        countQuery = "SELECT " + KEY_CONVERSION + " from " + TABLE_MASTER_UOM + " where " + KEY_MATERIAL_ID + " = ? and " + KEY_UOM + " = ? ";
        cursor = db.rawQuery(countQuery, new String[]{mat.getId(), mat.getUom()});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                qty = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CONVERSION));
            }
        }

        assert cursor != null;
        cursor.close();

        Material result = new Material();
        result.setUom(smallUom);
        result.setQty(mat.getQty() * qty);
        return result;
    }

    public boolean checkUnloadingRequest() {
        // Select All Query
        boolean result = false;
        StockRequest request = null;
        String selectQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_HEADER + " order by " + KEY_ID_STOCK_REQUEST_HEADER_DB + " desc limit 1 ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                request = new StockRequest();
                request.setIs_unloading(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_UNLOADING)));
            }
        }

        if (request != null) {
            if (request.getIs_unloading() == 1) {
                result = true;
            } else {
                result = false;
            }
        } else {
            result = true;
        }

        assert cursor != null;
        cursor.close();
        return result;
    }

    public StockRequest getStockRequestHeader(String idHeader) {
        // Select All Query
        StockRequest result = null;
        String selectQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_HEADER + " WHERE " + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idHeader});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = new StockRequest();
                result.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB)));
                result.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_BE)));
                result.setReq_date(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQUEST_DATE)));
                result.setNo_doc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_DOC)));
                result.setTanggal_kirim(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TANGGAL_KIRIM)));
                result.setNo_surat_jalan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_SURAT_JALAN)));
                result.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                result.setIs_unloading(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_UNLOADING)));
                result.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                result.setIs_verif(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)));
//                result.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));
            }
        }

        assert cursor != null;
        cursor.close();
        return result;
    }

    public StockRequest getAllStockMaterial() {
        // Select All Query
        StockRequest result = new StockRequest();
        String selectQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_HEADER + " WHERE " + KEY_IS_UNLOADING + " = 0 and " + KEY_IS_VERIF + " = 1 and " + KEY_STATUS + " = ? order by " + KEY_ID_STOCK_REQUEST_HEADER_DB + " desc limit 1";
        String selectQueryDetail = "select a." + KEY_MATERIAL_ID + ", a." + KEY_MATERIAL_NAME + ", (a." + KEY_QTY + "*b." + KEY_CONVERSION + ") as " + KEY_QTY + " , (select " + KEY_UOM + " from " + TABLE_MASTER_UOM + " where " + KEY_MATERIAL_ID + " = a." + KEY_MATERIAL_ID + " order by " + KEY_CONVERSION + " asc limit 1) as " + KEY_UOM + " \n" +
                "from " + TABLE_STOCK_REQUEST_DETAIL + " a \n" +
                "left join " + TABLE_MASTER_UOM + " b on a." + KEY_MATERIAL_ID + " = b." + KEY_MATERIAL_ID + "  and b." + KEY_UOM + " = a." + KEY_UOM + "\n" +
                "where a." + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ?";
        String selectQueryOrder = "select sum(b." + KEY_QTY + "*c." + KEY_CONVERSION + ") as " + KEY_QTY + " ,(select " + KEY_UOM + " from " + TABLE_MASTER_UOM + " where " + KEY_MATERIAL_ID + " = ? order by " + KEY_CONVERSION + " asc limit 1) as " + KEY_UOM + " \n" +
                "from " + TABLE_ORDER_HEADER + " a join " + TABLE_ORDER_DETAIL + " b on a." + KEY_ID_ORDER_HEADER_DB + " = b." + KEY_ID_ORDER_HEADER_DB + "\n" +
                "left join " + TABLE_MASTER_UOM + " c on b." + KEY_MATERIAL_ID + " = c." + KEY_MATERIAL_ID + "  and b." + KEY_UOM + " = c." + KEY_UOM + "\n" +
                "left join " + TABLE_ORDER_DETAIL_EXTRA + " d on d." + KEY_ID_ORDER_HEADER_DB + " = a." + KEY_ID_ORDER_HEADER_DB + " and d." + KEY_MATERIAL_ID + " = b." + KEY_MATERIAL_ID + "\n" +
                "where a." + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? and b." + KEY_MATERIAL_ID + "=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{Constants.STATUS_APPROVE});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = new StockRequest();
                result.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB)));
                result.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_BE)));
                result.setReq_date(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQUEST_DATE)));
                result.setNo_doc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_DOC)));
                result.setTanggal_kirim(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TANGGAL_KIRIM)));
                result.setNo_surat_jalan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_SURAT_JALAN)));

                Cursor cursorDetail = db.rawQuery(selectQueryDetail, new String[]{result.getIdHeader()});
                List<Material> matList = new ArrayList<>();
                if (cursorDetail != null) {
                    if (cursorDetail.moveToFirst()) {
                        do {
                            Material ma = new Material();
                            ma.setId(cursorDetail.getString(cursorDetail.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                            ma.setNama(cursorDetail.getString(cursorDetail.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                            ma.setQty(cursorDetail.getDouble(cursorDetail.getColumnIndexOrThrow(KEY_QTY)));
                            ma.setUom(cursorDetail.getString(cursorDetail.getColumnIndexOrThrow(KEY_UOM)));

                            Cursor cursorOrder = db.rawQuery(selectQueryOrder, new String[]{String.valueOf(result.getId()), result.getIdHeader(), String.valueOf(result.getId())});
                            Material materialOrder = new Material();
                            if (cursorOrder != null) {
                                if (cursorOrder.moveToFirst()) {
                                    materialOrder.setQty(cursorOrder.getDouble(cursorOrder.getColumnIndexOrThrow(KEY_QTY)));
                                    materialOrder.setUom(cursorOrder.getString(cursorOrder.getColumnIndexOrThrow(KEY_UOM)));
                                }
                            }
                            cursorOrder.close();
                            double qty = ma.getQty() - materialOrder.getQty();
                            ma.setQty(qty);

                            matList.add(ma);
                        } while (cursorDetail.moveToNext());
                    }
                    cursorDetail.close();
                }
                result.setMaterialList(matList);
            }
        }

        assert cursor != null;
        cursor.close();
        return result;
    }

    public Material getStockMaterial(Map req) {
        // Select All Query
        Material result = null;
        String selectQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_HEADER + " WHERE " + KEY_IS_UNLOADING + " = 0 and " + KEY_IS_VERIF + " = 1 and " + KEY_STATUS + " = ? ";
//        String selectQueryDetail = "SELECT * FROM " + TABLE_STOCK_REQUEST_DETAIL + " WHERE " + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? and " + KEY_MATERIAL_ID + " = ? ";
        String selectQueryDetail = "select a." + KEY_MATERIAL_ID + ", a." + KEY_MATERIAL_NAME + ", (a." + KEY_QTY + "*b." + KEY_CONVERSION + ") as " + KEY_QTY + " , (select " + KEY_UOM + " from " + TABLE_MASTER_UOM + " where " + KEY_MATERIAL_ID + " = a." + KEY_MATERIAL_ID + " order by " + KEY_CONVERSION + " asc limit 1) as " + KEY_UOM + " \n" +
                "from " + TABLE_STOCK_REQUEST_DETAIL + " a \n" +
                "left join " + TABLE_MASTER_UOM + " b on a." + KEY_MATERIAL_ID + " = b." + KEY_MATERIAL_ID + "  and b." + KEY_UOM + " = a." + KEY_UOM + "\n" +
                "where a." + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? and a." + KEY_MATERIAL_ID + " = ? ";
        String selectQueryOrder = "select sum(b." + KEY_QTY + "*c." + KEY_CONVERSION + ") as " + KEY_QTY + " ,(select " + KEY_UOM + " from " + TABLE_MASTER_UOM + " where " + KEY_MATERIAL_ID + " = ? order by " + KEY_CONVERSION + " asc limit 1) as " + KEY_UOM + " \n" +
                "from " + TABLE_ORDER_HEADER + " a join " + TABLE_ORDER_DETAIL + " b on a." + KEY_ID_ORDER_HEADER_DB + " = b." + KEY_ID_ORDER_HEADER_DB + "\n" +
                "left join " + TABLE_MASTER_UOM + " c on b." + KEY_MATERIAL_ID + " = c." + KEY_MATERIAL_ID + "  and b." + KEY_UOM + " = c." + KEY_UOM + "\n" +
                "left join " + TABLE_ORDER_DETAIL_EXTRA + " d on d." + KEY_ID_ORDER_HEADER_DB + " = a." + KEY_ID_ORDER_HEADER_DB + " and d." + KEY_MATERIAL_ID + " = b." + KEY_MATERIAL_ID + "\n" +
                "where a." + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? and b." + KEY_MATERIAL_ID + "=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{Constants.STATUS_APPROVE});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String idHeader = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB));

                Cursor cursorDetail = db.rawQuery(selectQueryDetail, new String[]{idHeader, req.get("id_material").toString()});
                if (cursorDetail != null) {
                    if (cursorDetail.moveToFirst()) {
                        result = new Material();
                        result.setId(cursorDetail.getString(cursorDetail.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                        result.setNama(cursorDetail.getString(cursorDetail.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                        result.setQty(cursorDetail.getDouble(cursorDetail.getColumnIndexOrThrow(KEY_QTY)));
                        result.setUom(cursorDetail.getString(cursorDetail.getColumnIndexOrThrow(KEY_UOM)));

//                        Material conversion = getQtySmallUom(result);
//                        result.setQty(conversion.getQty());
//                        result.setUom(conversion.getUom());

                        Cursor cursorOrder = db.rawQuery(selectQueryOrder, new String[]{String.valueOf(result.getId()), idHeader, String.valueOf(result.getId())});
                        Material materialOrder = new Material();
                        if (cursorOrder != null) {
                            if (cursorOrder.moveToFirst()) {
                                materialOrder.setQty(cursorOrder.getDouble(cursorOrder.getColumnIndexOrThrow(KEY_QTY)));
                                materialOrder.setUom(cursorOrder.getString(cursorOrder.getColumnIndexOrThrow(KEY_UOM)));
                            }
                        }
                        cursorOrder.close();
                        double qty = result.getQty() - materialOrder.getQty();
                        result.setQty(qty);
                    }
                    cursorDetail.close();
                }
            }
        }

        assert cursor != null;
        cursor.close();
        return result;
    }

    public double getLKCustomer(Customer cust) {
        // Select All Query
        double result = 0;
        String lkQuery = "";
        if (cust.isNoo()) {
            lkQuery = "(SELECT " + KEY_CREDIT_LIMIT + " FROM " + TABLE_NOO + " WHERE " + KEY_ID_NOO_DB + " = ?) a  , ";
        } else {
            lkQuery = "(SELECT " + KEY_CREDIT_LIMIT + " FROM " + TABLE_CUSTOMER + " WHERE " + KEY_CUSTOMER_ID + " = ?) a  , ";
        }

        String selectQuery = "SELECT a." + KEY_CREDIT_LIMIT + " - b.value + c.value as " + KEY_CREDIT_LIMIT + " " +
                "from " +
                lkQuery +
                "(SELECT COALESCE(sum(" + KEY_OMZET + "),0) AS value FROM " + TABLE_ORDER_HEADER + " WHERE " + KEY_CUSTOMER_ID + " = ? and " + KEY_ORDER_TYPE + " = 'CO') b , " +
                "(SELECT COALESCE(sum(b." + KEY_TOTAL_PAYMENT + "), 0) AS value FROM " + TABLE_INVOICE_HEADER + " a  " +
                "INNER JOIN " + TABLE_COLLECTION_DETAIL + " b on b." + KEY_INVOICE_NO + " = a." + KEY_INVOICE_NO + " and " + KEY_TYPE_PAYMENT + " = 'cash'  " +
                "WHERE a." + KEY_CUSTOMER_ID + " = ?) c ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{cust.getId(), cust.getId(), cust.getId()});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_CREDIT_LIMIT));
            }
        }

        assert cursor != null;
        cursor.close();
        return result;
    }

    public double getLimitBon(String idMat, String idCust) {
        // Select All Query
        double result = 0;
        String selectQuery = "SELECT COALESCE(a.value,0) - COALESCE(b.value,0) + COALESCE(c.value,0) as " + KEY_BON_LIMIT + " " +
                "FROM " +
                "(SELECT a." + KEY_BON_LIMIT + " as value FROM " + TABLE_MASTER_LIMIT_BON + " a  WHERE " + KEY_MATERIAL_ID + " = ? AND " + KEY_CUSTOMER_ID + " = ?) a, " +
                "(SELECT COUNT(*) as value FROM " + TABLE_INVOICE_HEADER + " a " +
                "INNER JOIN " + TABLE_INVOICE_DETAIL + " b ON a." + KEY_INVOICE_NO + " = b." + KEY_INVOICE_NO + " AND b.  " + KEY_MATERIAL_ID + " = ? " +
                "WHERE a." + KEY_CUSTOMER_ID + " = ?) b, " +
                "(SELECT COUNT(*) as value FROM " + TABLE_INVOICE_HEADER + " a " +
                "INNER JOIN " + TABLE_COLLECTION_HEADER + " b ON a." + KEY_INVOICE_NO + " = b." + KEY_INVOICE_NO + " " +
                "INNER JOIN " + TABLE_COLLECTION_ITEM + " c ON b." + KEY_ID_COLLECTION_HEADER_DB + " = c." + KEY_ID_COLLECTION_HEADER_DB + " AND c.  " + KEY_MATERIAL_ID + " = ? " +
                "WHERE a." + KEY_CUSTOMER_ID + " = ?) c";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMat, idCust, idMat, idCust, idMat, idCust});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_BON_LIMIT));
            }
        }

        assert cursor != null;
        cursor.close();
        return result;
    }

    public StockRequest getLastStockRequest(Map req) {
        // Select All Query
        StockRequest result = null;
        String selectQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_HEADER + " WHERE " + KEY_IS_UNLOADING + " = 0 and " + KEY_IS_VERIF + " = 1 and " + KEY_STATUS + " = ? ";
        String selectQueryDetail = "SELECT * FROM " + TABLE_STOCK_REQUEST_DETAIL + " WHERE " + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{Constants.STATUS_APPROVE});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = new StockRequest();
                result.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB)));

                List<Material> materialList = new ArrayList<>();
                Cursor cursorDetail = db.rawQuery(selectQueryDetail, new String[]{req.get("id_material").toString()});
                if (cursorDetail != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            Material paramModel = new Material();
                            paramModel.setId(cursorDetail.getString(cursorDetail.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                            paramModel.setNama(cursorDetail.getString(cursorDetail.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                            paramModel.setQty(cursorDetail.getDouble(cursorDetail.getColumnIndexOrThrow(KEY_QTY)));
                            paramModel.setUom(cursorDetail.getString(cursorDetail.getColumnIndexOrThrow(KEY_UOM)));

                            Material conversion = getQtySmallUom(paramModel);
                            paramModel.setQty(conversion.getQty());
                            paramModel.setUom(conversion.getUom());
                            materialList.add(paramModel);
                        } while (cursorDetail.moveToNext());
                    }
                    cursorDetail.close();
                }
                result.setMaterialList(materialList);
            }
        }

        assert cursor != null;
        cursor.close();
        return result;
    }

    public List<Invoice> getAllInvoiceCustomer(String idCust) {
        List<Invoice> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_INVOICE_HEADER + " WHERE " + KEY_IS_VERIF + " = 1 and " + KEY_CUSTOMER_ID + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCust});

        if (cursor.moveToFirst()) {
            do {
                Invoice paramModel = new Invoice();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_INVOICE_HEADER_DB)));
                paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setNo_invoice(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_NO)));
                paramModel.setInvoice_date(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_DATE)));
                paramModel.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_INVOICE_TOTAL)));
                paramModel.setTanggal_jatuh_tempo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DUE_DATE)));
                paramModel.setTotal_paid(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID)));
                paramModel.setNett(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_NETT)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
//                paramModel.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));
                paramModel.setIs_verif(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)));
                paramModel.setIs_route(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_ROUTE)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Invoice> getAllInvoiceHeaderNotVerif() {
        List<Invoice> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_INVOICE_HEADER + " WHERE " + KEY_IS_VERIF + " = 0 order by " + KEY_INVOICE_NO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Invoice paramModel = new Invoice();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_INVOICE_HEADER_DB)));
                paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setNo_invoice(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_NO)));
                paramModel.setInvoice_date(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_DATE)));
                paramModel.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_INVOICE_TOTAL)));
                paramModel.setTanggal_jatuh_tempo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DUE_DATE)));
                paramModel.setTotal_paid(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID)));
                paramModel.setNett(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_NETT)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
//                paramModel.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));
                paramModel.setIs_verif(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)));
                paramModel.setIs_route(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_ROUTE)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllInvoiceDetail(String idHeader) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_INVOICE_DETAIL + " WHERE " + KEY_ID_INVOICE_HEADER_DB + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idHeader});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_INVOICE_DETAIL_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
//                paramModel.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                paramModel.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)) - cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID)));
                paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                paramModel.setSisa(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)) - cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID)));
                paramModel.setNett(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Customer> getTodayCustomer() {
        setFormatSeparator();
        SQLiteDatabase db = this.getWritableDatabase();
        List<Customer> arrayList = new ArrayList<>();
        String selectQuery = null;
        Cursor cursor = null;
        selectQuery = "select c.* from " + TABLE_MASTER_NON_ROUTE_CUSTOMER + " c WHERE c." + KEY_ROUTE + " like ? " +
                "union " +
                "select b.* from " + TABLE_INVOICE_HEADER + " a " +
                "inner join " + TABLE_MASTER_NON_ROUTE_CUSTOMER + " b on a." + KEY_CUSTOMER_ID + " = b." + KEY_CUSTOMER_ID + " and a." + KEY_IS_ROUTE + " = 0 " +
                "where a." + KEY_DATE + " = ? group by b." + KEY_CUSTOMER_ID;
        cursor = db.rawQuery(selectQuery, new String[]{"%" + Helper.getTodayRoute() + "%", Helper.getTodayDate(Constants.DATE_FORMAT_3)});

        if (cursor.moveToFirst()) {
            do {
                Customer paramModel = new Customer();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
                paramModel.setNama_pemilik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PEMILIK)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ADDRESS)));
                paramModel.setType_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER)));
                paramModel.setRute(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE)));
                paramModel.setName_type_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER_NAME)));
                paramModel.setType_price(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_PRICE)));
                paramModel.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                paramModel.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                paramModel.setKode_pos(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKota(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setUdf_5(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5)));
                paramModel.setUdf_5_desc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5_DESC)));
                paramModel.setNo_tlp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                paramModel.setSisaCreditLimit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_SISA_KREDIT_LIMIT)));
                paramModel.setLimit_kredit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_CREDIT_LIMIT)));
                paramModel.setTotalTagihan(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_TAGIHAN)));
                paramModel.setNik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_KTP)));
                paramModel.setNo_npwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_NPWP)));

                paramModel.setPhotoKtp(getPhoto(paramModel.getId(), "ktp", paramModel.getIdHeader()));
                paramModel.setPhotoNpwp(getPhoto(paramModel.getId(), "npwp", paramModel.getIdHeader()));
                paramModel.setPhotoOutlet(getPhoto(paramModel.getId(), "outlet", paramModel.getIdHeader()));

//                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
//                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
//                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                paramModel.setRoute(Helper.checkTodayRoute(paramModel.getRute()));
//                paramModel.setPromoList(getPromotionRouteByIdCustomer(paramModel.getId()));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Customer> getRouteCustomer(Location currentLocation, boolean coverage) {
        setFormatSeparator();
        SQLiteDatabase db = this.getWritableDatabase();
        List<Customer> arrayList = new ArrayList<>();
        String selectQuery = null;
        Cursor cursor = null;
        if (coverage) {
            selectQuery = "select c.* from " + TABLE_MASTER_NON_ROUTE_CUSTOMER + " c WHERE c." + KEY_ROUTE + " like ? ";
            cursor = db.rawQuery(selectQuery, new String[]{"%" + Helper.getTodayRoute() + "%"});
        } else {
            selectQuery = "select c.* from " + TABLE_MASTER_NON_ROUTE_CUSTOMER + " c ";
//            selectQuery = "select c.* from " + TABLE_MASTER_NON_ROUTE_CUSTOMER + " c WHERE c." + KEY_ROUTE + " like ? " +
//                    "union " +
//                    "select b.* from " + TABLE_INVOICE_HEADER + " a " +
//                    "inner join " + TABLE_MASTER_NON_ROUTE_CUSTOMER + " b on a." + KEY_CUSTOMER_ID + " = b." + KEY_CUSTOMER_ID + " and a." + KEY_IS_ROUTE + " = 0 " +
//                    "where a." + KEY_DATE + " = ? group by b." + KEY_CUSTOMER_ID;
            cursor = db.rawQuery(selectQuery, null);
        }


        if (cursor.moveToFirst()) {
            do {
                Customer paramModel = new Customer();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
                paramModel.setNama_pemilik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PEMILIK)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ADDRESS)));
                paramModel.setType_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER)));
                paramModel.setRute(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE)));
                paramModel.setName_type_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER_NAME)));
                paramModel.setType_price(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_PRICE)));
                paramModel.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                paramModel.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                paramModel.setKode_pos(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKota(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setUdf_5(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5)));
                paramModel.setUdf_5_desc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5_DESC)));
                paramModel.setNo_tlp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                paramModel.setSisaCreditLimit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_SISA_KREDIT_LIMIT)));
                paramModel.setLimit_kredit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_CREDIT_LIMIT)));
                paramModel.setTotalTagihan(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_TAGIHAN)));
                paramModel.setNik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_KTP)));
                paramModel.setNo_npwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_NPWP)));

                paramModel.setPhotoKtp(getPhoto(paramModel.getId(), "ktp", paramModel.getIdHeader()));
                paramModel.setPhotoNpwp(getPhoto(paramModel.getId(), "npwp", paramModel.getIdHeader()));
                paramModel.setPhotoOutlet(getPhoto(paramModel.getId(), "outlet", paramModel.getIdHeader()));

//                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
//                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
//                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                paramModel.setRoute(Helper.checkTodayRoute(paramModel.getRute()));

                if (currentLocation != null) {
                    double distance = Helper.distance(paramModel.getLatitude(), paramModel.getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude(), "K");
                    paramModel.setMileage(format.format(distance));
                }

//                paramModel.setPromoList(getPromotionRouteByIdCustomer(paramModel.getId()));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllDetailOrder(String idHeader) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
//        String selectQuery = "SELECT * FROM " + TABLE_ORDER_DETAIL + " WHERE " + KEY_ID_ORDER_HEADER_DB + " = ? ";
        String selectQuery = "select a." + KEY_ID_ORDER_HEADER_DB + ", a." + KEY_MATERIAL_ID + ", a." + KEY_MATERIAL_NAME + ", a." + KEY_MATERIAL_GROUP_ID + ", a." + KEY_MATERIAL_GROUP_NAME + ", a." +
                KEY_MATERIAL_PRODUCT_ID + ", a." + KEY_MATERIAL_PRODUCT_NAME + ", (a." + KEY_TOTAL + " - sum(ifnull(d." + KEY_AMOUNT_PAID + ", 0)))  as " + KEY_TOTAL + "," + " sum(ifnull(d." + KEY_AMOUNT_PAID + ", 0)) as " + KEY_PAID
                + " from " + TABLE_ORDER_DETAIL + " a \n" +
                "left join " + TABLE_COLLECTION_HEADER + " b on a." + KEY_ID_ORDER_HEADER_DB + " = b." + KEY_INVOICE_NO + " and b." + KEY_CUSTOMER_ID + " = a." + KEY_CUSTOMER_ID + "\n" +
                "left join " + TABLE_COLLECTION_DETAIL + " c on c." + KEY_ID_COLLECTION_HEADER_DB + " = b." + KEY_ID_COLLECTION_HEADER_DB + "\n" +
                "left join " + TABLE_COLLECTION_ITEM + " d on d." + KEY_ID_COLLECTION_DETAIL_DB + " = c." + KEY_ID_COLLECTION_DETAIL_DB + " and a." + KEY_MATERIAL_ID + " = d." + KEY_MATERIAL_ID + "\n" +
                "where a." + KEY_ID_ORDER_HEADER_DB + " = ? " +
                "group by a." + KEY_ID_ORDER_HEADER_DB + " , a." + KEY_MATERIAL_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idHeader});
        double totalPaid = 0;
        Material paramModel = null;
        if (cursor.moveToFirst()) {
            do {
                paramModel = new Material();
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_ORDER_HEADER_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL)));
                paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                paramModel.setSisa(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL)));
                paramModel.setNett(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL)));
                arrayList.add(paramModel);

                totalPaid = totalPaid + cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID));
            } while (cursor.moveToNext());
        }

        if (Helper.isNotEmptyOrNull(arrayList)) {
            arrayList.get(0).setAmountPaid(totalPaid);
        }
        cursor.close();
        return arrayList;
    }

    public List<Invoice> getAllInvoiceHeaderNotPaid() {
        List<Invoice> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_INVOICE_HEADER + " WHERE " + KEY_IS_VERIF + " = 1 and (" + KEY_INVOICE_TOTAL + " - " + KEY_PAID + " != 0)";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Invoice paramModel = new Invoice();
                paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_INVOICE_HEADER_DB)));
                paramModel.setNo_invoice(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_NO)));
                paramModel.setInvoice_date(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_DATE)));
                paramModel.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_INVOICE_TOTAL)));
                paramModel.setTanggal_jatuh_tempo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DUE_DATE)));
                paramModel.setTotal_paid(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID)));
                paramModel.setNett(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_NETT)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
//                paramModel.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));
                paramModel.setIs_verif(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)));
                paramModel.setIs_route(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_ROUTE)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Customer> getAllCustomerVisit() {
        setFormatSeparator();
        SQLiteDatabase db = this.getWritableDatabase();
        List<Customer> arrayList = new ArrayList<>();
        String selectQuery = null;
        Cursor cursor = null;
        // Select All Query
//        if (coverage) {
//            selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_ROUTE + " LIKE ?";
//            cursor = db.rawQuery(selectQuery, new String[]{"%" + Helper.getTodayRoute() + "%"});
//        } else {
        selectQuery = "SELECT * FROM " + TABLE_CUSTOMER;
        cursor = db.rawQuery(selectQuery, null);
//        }

        if (cursor.moveToFirst()) {
            do {
                Customer paramModel = new Customer();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CUSTOMER_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
                paramModel.setNama_pemilik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PEMILIK)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ADDRESS)));
                paramModel.setType_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER)));
                paramModel.setRute(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE)));
                paramModel.setName_type_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER_NAME)));
                paramModel.setType_price(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_PRICE)));
                paramModel.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                paramModel.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                paramModel.setKode_pos(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKota(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setUdf_5(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5)));
                paramModel.setUdf_5_desc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5_DESC)));
                paramModel.setNo_tlp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                paramModel.setSisaCreditLimit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_SISA_KREDIT_LIMIT)));
                paramModel.setLimit_kredit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_CREDIT_LIMIT)));
                paramModel.setTotalTagihan(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_TAGIHAN)));
                paramModel.setNik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_KTP)));
                paramModel.setNo_npwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_NPWP)));

                paramModel.setPhotoKtp(getPhoto(paramModel.getId(), "ktp", paramModel.getIdHeader()));
                paramModel.setPhotoNpwp(getPhoto(paramModel.getId(), "npwp", paramModel.getIdHeader()));
                paramModel.setPhotoOutlet(getPhoto(paramModel.getId(), "outlet", paramModel.getIdHeader()));

//                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
//                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
//                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                paramModel.setRoute(Helper.checkTodayRoute(paramModel.getRute()));

//                if (currentLocation != null) {
//                    double distance = Helper.distance(paramModel.getLatitude(), paramModel.getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude(), "K");
//                    paramModel.setMileage(format.format(distance));
//                }

                paramModel.setPromoList(getPromotionRouteByIdCustomer(paramModel.getId()));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Customer> getAllCustomerNoo() {
        List<Customer> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Customer paramModel = new Customer();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOO_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOO_DB)));
                paramModel.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_NOO)));
                paramModel.setNama_pemilik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PEMILIK)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS_NOO)));
                paramModel.setType_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER)));
                paramModel.setRute(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE)));
                paramModel.setName_type_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER_NAME)));
                paramModel.setType_price(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_PRICE)));
                paramModel.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                paramModel.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                paramModel.setKode_pos(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKota(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setUdf_5(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5)));
                paramModel.setUdf_5_desc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5_DESC)));
                paramModel.setNo_tlp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                paramModel.setSisaCreditLimit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_SISA_KREDIT_LIMIT)));
                paramModel.setLimit_kredit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_CREDIT_LIMIT)));
                paramModel.setTotalTagihan(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_TAGIHAN)));
                paramModel.setNik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_KTP)));
                paramModel.setNo_npwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_NPWP)));

                paramModel.setPhotoKtp(getPhoto(paramModel.getId(), "ktp", paramModel.getIdHeader()));
                paramModel.setPhotoNpwp(getPhoto(paramModel.getId(), "npwp", paramModel.getIdHeader()));
                paramModel.setPhotoOutlet(getPhoto(paramModel.getId(), "outlet", paramModel.getIdHeader()));

//                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
//                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
//                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                paramModel.setIsSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                paramModel.setRoute(Helper.checkTodayRoute(paramModel.getRute()));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Bank> getAllBank(String param) {
        List<Bank> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_BANK + " WHERE " + KEY_CATEGORY + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{param});

        if (cursor.moveToFirst()) {
            do {
                Bank paramModel = new Bank();
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_BANK_BE)));
                paramModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_BANK)));
                paramModel.setId_depo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_DEPO)));
                paramModel.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY)));
                paramModel.setNo_rek(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_REK)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<String> getAllStringReason(String param) {
        List<String> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_NAME_REASON + " FROM " + TABLE_MASTER_REASON + " WHERE " + KEY_CATEGORY_REASON + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{param});

        if (cursor.moveToFirst()) {
            do {
                String paramModel = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_REASON));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Reason> getAllReason(String param) {
        List<Reason> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_REASON + " WHERE " + KEY_CATEGORY_REASON + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{param});

        if (cursor.moveToFirst()) {
            do {
                Reason paramModel = new Reason();
                paramModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_REASON_BE)));
                paramModel.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_REASON)));
                paramModel.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY_REASON)));
                paramModel.setIs_photo(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_PHOTO)));
                paramModel.setIs_freetext(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_FREE_TEXT)));
                paramModel.setIs_barcode(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_BARCODE)));
                paramModel.setIs_signature(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SIGNATURE)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllDetailOrder(Order header) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_DETAIL + " WHERE " + KEY_ID_ORDER_HEADER_DB + " = ? ";
        String selectQueryDiscount = "SELECT * FROM " + TABLE_ORDER_DETAIL_DISCOUNT + " WHERE " + KEY_ID_ORDER_HEADER_DB + " = ? and " + KEY_MATERIAL_ID + " = ?";
        String selectQueryExtra = "SELECT * FROM " + TABLE_ORDER_DETAIL_EXTRA + " WHERE " + KEY_ID_ORDER_HEADER_DB + " = ? and " + KEY_ID_ORDER_DETAIL_DB + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{header.getIdHeader()});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_ORDER_DETAIL_DB)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                paramModel.setIdStockRequestHeaderDB(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB)));
                paramModel.setIdStockRequestHeaderBE(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_BE)));
                paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                paramModel.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                paramModel.setTotalDiscount(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_DISCOUNT)));
                paramModel.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL)));

                List<Discount> diskonList = new ArrayList<>();
                Cursor cursorDiscount = db.rawQuery(selectQueryDiscount, new String[]{header.getIdHeader(), paramModel.getId()});
                if (cursorDiscount.moveToFirst()) {
                    do {
                        Discount disc = new Discount();
                        disc.setKeydiskon(cursorDiscount.getString(cursorDiscount.getColumnIndexOrThrow(KEY_DISCOUNT_ID)));
                        disc.setKeydiskon(cursorDiscount.getString(cursorDiscount.getColumnIndexOrThrow(KEY_DISCOUNT_NAME)));
                        disc.setValuediskon(cursorDiscount.getString(cursorDiscount.getColumnIndexOrThrow(KEY_DISCOUNT_PRICE)));
                        diskonList.add(disc);
                    } while (cursorDiscount.moveToNext());
                }
                cursorDiscount.close();
                paramModel.setDiskonList(diskonList);

                List<Material> extraList = new ArrayList<>();
                Cursor cursorExtra = db.rawQuery(selectQueryExtra, new String[]{header.getIdHeader(), paramModel.getIdheader()});
                if (cursorExtra.moveToFirst()) {
                    do {
                        Material extra = new Material();
                        extra.setId(cursorExtra.getString(cursorExtra.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                        extra.setNama(cursorExtra.getString(cursorExtra.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                        extra.setId_material_group(cursorExtra.getString(cursorExtra.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                        extra.setMaterial_group_name(cursorExtra.getString(cursorExtra.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                        extra.setId_product_group(cursorExtra.getString(cursorExtra.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                        extra.setName_product_group(cursorExtra.getString(cursorExtra.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                        extra.setQty(cursorExtra.getDouble(cursorExtra.getColumnIndexOrThrow(KEY_QTY)));
                        extra.setUom(cursorExtra.getString(cursorExtra.getColumnIndexOrThrow(KEY_UOM)));
                        extraList.add(extra);
                    } while (cursorExtra.moveToNext());
                }
                cursorExtra.close();
                paramModel.setExtraItem(extraList);

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public Reason getDetailReason(String param, String nameReason) {
        Reason paramModel = new Reason();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_REASON + " WHERE " + KEY_CATEGORY_REASON + " = ? and " + KEY_NAME_REASON + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{param, nameReason});

        if (cursor.moveToFirst()) {
//            do {
            paramModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_REASON_BE)));
            paramModel.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_REASON)));
            paramModel.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY_REASON)));
            paramModel.setIs_photo(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_PHOTO)));
            paramModel.setIs_freetext(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_FREE_TEXT)));
            paramModel.setIs_barcode(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_BARCODE)));
            paramModel.setIs_signature(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SIGNATURE)));
//                arrayList.add(paramModel);
//            } while (cursor.moveToNext());
        }
        cursor.close();
        return paramModel;
    }

    public Reason getDetailReasonById(String param, String idReason) {
        Reason paramModel = new Reason();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_REASON + " WHERE " + KEY_CATEGORY_REASON + " = ? and " + KEY_ID_REASON_BE + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{param, idReason});

        if (cursor.moveToFirst()) {
//            do {
            paramModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_REASON_BE)));
            paramModel.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_REASON)));
            paramModel.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY_REASON)));
            paramModel.setIs_photo(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_PHOTO)));
            paramModel.setIs_freetext(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_FREE_TEXT)));
            paramModel.setIs_barcode(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_BARCODE)));
            paramModel.setIs_signature(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SIGNATURE)));
//                arrayList.add(paramModel);
//            } while (cursor.moveToNext());
        }
        cursor.close();
        return paramModel;
    }

    public double getTotalTagihanCustomer(String idCust) {
        double result = 0.0;
        // Select All Query
        String selectQuery = "SELECT (" + KEY_INVOICE_TOTAL + " - " + KEY_PAID + ") as total FROM " + TABLE_INVOICE_HEADER + " WHERE " + KEY_CUSTOMER_ID + " = ? and " + KEY_IS_VERIF + " = 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCust});

        if (cursor.moveToFirst()) {
            do {
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
                result = result + amount;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public double getTotalPaymentAmount() {
        double result = 0.0;
        // Select All Query
        String selectQuery = "SELECT " + KEY_PAID + "  FROM " + TABLE_INVOICE_HEADER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID));
                result = result + amount;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public double getTotalOutstandingAmount() {
        double result = 0.0;
        // Select All Query
        String selectQuery = "SELECT (" + KEY_INVOICE_TOTAL + " - " + KEY_PAID + ") as total FROM " + TABLE_INVOICE_HEADER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
                result = result + amount;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public double getTotalInvoiceAmount() {
        double result = 0.0;
        // Select All Query
        String selectQuery = "SELECT " + KEY_INVOICE_TOTAL + " FROM " + TABLE_INVOICE_HEADER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_INVOICE_TOTAL));
                result = result + amount;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public List<Customer> getAllSalesmanCustomer() {
        setFormatSeparator();
        List<Customer> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_NON_ROUTE_CUSTOMER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Customer paramModel = new Customer();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
                paramModel.setRute(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE)));
                paramModel.setType_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER)));
                paramModel.setName_type_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER_NAME)));
                paramModel.setType_price(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_PRICE)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ADDRESS)));
                paramModel.setKode_pos(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKota(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                paramModel.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                paramModel.setNama_pemilik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PEMILIK)));
                paramModel.setUdf_5(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5)));
                paramModel.setUdf_5_desc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5_DESC)));
                paramModel.setNo_tlp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                paramModel.setSisaCreditLimit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_SISA_KREDIT_LIMIT)));
                paramModel.setLimit_kredit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_CREDIT_LIMIT)));
                paramModel.setTotalTagihan(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_TAGIHAN)));
                paramModel.setNik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_KTP)));
                paramModel.setNo_npwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_NPWP)));

                paramModel.setPhotoKtp(getPhoto(paramModel.getId(), "ktp", paramModel.getIdHeader()));
                paramModel.setPhotoNpwp(getPhoto(paramModel.getId(), "npwp", paramModel.getIdHeader()));
                paramModel.setPhotoOutlet(getPhoto(paramModel.getId(), "outlet", paramModel.getIdHeader()));

//                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
//                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
//                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                paramModel.setRoute(false);

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Customer> getAllNonRouteCustomer() {
        setFormatSeparator();
        List<Customer> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT c.* FROM " + TABLE_MASTER_NON_ROUTE_CUSTOMER + " c " +
                "left join " + TABLE_CUSTOMER + " a on a." + KEY_CUSTOMER_ID + " = c." + KEY_CUSTOMER_ID +
                " WHERE c." + KEY_ROUTE + " not like ? and a." + KEY_CUSTOMER_ID + " is null";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + Helper.getTodayRoute() + "%"});

        if (cursor.moveToFirst()) {
            do {
                Customer paramModel = new Customer();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
                paramModel.setRute(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE)));
                paramModel.setType_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER)));
                paramModel.setName_type_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER_NAME)));
                paramModel.setType_price(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_PRICE)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ADDRESS)));
                paramModel.setKode_pos(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKota(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                paramModel.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                paramModel.setNama_pemilik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PEMILIK)));
                paramModel.setUdf_5(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5)));
                paramModel.setUdf_5_desc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5_DESC)));
                paramModel.setNo_tlp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                paramModel.setSisaCreditLimit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_SISA_KREDIT_LIMIT)));
                paramModel.setLimit_kredit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_CREDIT_LIMIT)));
                paramModel.setTotalTagihan(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_TAGIHAN)));
                paramModel.setNik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_KTP)));
                paramModel.setNo_npwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_NPWP)));

                paramModel.setPhotoKtp(getPhoto(paramModel.getId(), "ktp", paramModel.getIdHeader()));
                paramModel.setPhotoNpwp(getPhoto(paramModel.getId(), "npwp", paramModel.getIdHeader()));
                paramModel.setPhotoOutlet(getPhoto(paramModel.getId(), "outlet", paramModel.getIdHeader()));

//                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
//                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
//                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                paramModel.setRoute(false);

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<CollectionHeader> getAllInvoiceHistoryCustomer(String request) {
        List<CollectionHeader> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COLLECTION_HEADER + " WHERE " + KEY_CUSTOMER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{request});

        if (cursor.moveToFirst()) {
            do {
                CollectionHeader paramModel = new CollectionHeader();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_HEADER_DB)));
                paramModel.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setInvoiceNo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_NO)));
                paramModel.setInvoiceDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_DATE)));
                paramModel.setInvoiceTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_INVOICE_TOTAL)));
                paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setTotalPaid(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_PAID)));
                paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setCashList(getAllCollectionDetailCash(paramModel.getIdHeader()));
                paramModel.setTfList(getAllCollectionDetailTransfer(paramModel.getIdHeader()));
                paramModel.setGiroList(getAllCollectionDetailGiro(paramModel.getIdHeader()));
                paramModel.setChequeList(getAllCollectionDetailCheque(paramModel.getIdHeader()));
                paramModel.setLainList(getAllCollectionDetailLain(paramModel.getIdHeader()));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<CollectionDetail> getAllCollectionDetailCash(String id) {
        List<CollectionDetail> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COLLECTION_DETAIL + " WHERE " + KEY_ID_COLLECTION_HEADER_DB + " = ? and " + KEY_TYPE_PAYMENT + " = \'cash\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                CollectionDetail paramModel = new CollectionDetail();
                paramModel.setIdDetail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_DETAIL_DB)));
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_HEADER_DB)));
                paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setTotalPayment(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_PAYMENT)));
                paramModel.setLeft(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LEFT)));
                paramModel.setMaterialList(getAllCollectionItem(paramModel.getIdDetail()));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<CollectionDetail> getAllCollectionDetailTransfer(String id) {
        List<CollectionDetail> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COLLECTION_DETAIL + " WHERE " + KEY_ID_COLLECTION_HEADER_DB + " = ? and " + KEY_TYPE_PAYMENT + " = \'transfer\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                CollectionDetail paramModel = new CollectionDetail();
                paramModel.setIdDetail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_DETAIL_DB)));
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_HEADER_DB)));
                paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setTotalPayment(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_PAYMENT)));
                paramModel.setLeft(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LEFT)));
                paramModel.setTgl(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setMaterialList(getAllCollectionItem(paramModel.getIdDetail()));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<CollectionDetail> getAllCollectionDetailGiro(String id) {
        List<CollectionDetail> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COLLECTION_DETAIL + " WHERE " + KEY_ID_COLLECTION_HEADER_DB + " = ? and " + KEY_TYPE_PAYMENT + " = \'giro\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                CollectionDetail paramModel = new CollectionDetail();
                paramModel.setIdDetail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_DETAIL_DB)));
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_HEADER_DB)));
                paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setTotalPayment(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_PAYMENT)));
                paramModel.setLeft(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LEFT)));
                paramModel.setTgl(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setNo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO)));
                paramModel.setTglCair(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DUE_DATE)));
                paramModel.setIdBankASPP(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_BANK)));
                paramModel.setBankNameASPP(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_BANK)));
                paramModel.setIdBankCust(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CUST_BANK)));
                paramModel.setBankCust(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_CUST_BANK)));
                paramModel.setMaterialList(getAllCollectionItem(paramModel.getIdDetail()));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<CollectionDetail> getAllCollectionDetailCheque(String id) {
        List<CollectionDetail> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COLLECTION_DETAIL + " WHERE " + KEY_ID_COLLECTION_HEADER_DB + " = ? and " + KEY_TYPE_PAYMENT + " = \'cheque\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                CollectionDetail paramModel = new CollectionDetail();
                paramModel.setIdDetail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_DETAIL_DB)));
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_HEADER_DB)));
                paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setTotalPayment(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_PAYMENT)));
                paramModel.setLeft(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LEFT)));
                paramModel.setTgl(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setNo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO)));
                paramModel.setTglCair(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DUE_DATE)));
                paramModel.setIdBankASPP(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_BANK)));
                paramModel.setBankNameASPP(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_BANK)));
                paramModel.setIdBankCust(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CUST_BANK)));
                paramModel.setBankCust(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_CUST_BANK)));
                paramModel.setMaterialList(getAllCollectionItem(paramModel.getIdDetail()));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<CollectionDetail> getAllCollectionDetailLain(String id) {
        List<CollectionDetail> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COLLECTION_DETAIL + " WHERE " + KEY_ID_COLLECTION_HEADER_DB + " = ? and " + KEY_TYPE_PAYMENT + " = \'lain\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                CollectionDetail paramModel = new CollectionDetail();
                paramModel.setIdDetail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_DETAIL_DB)));
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_HEADER_DB)));
                paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setTotalPayment(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_PAYMENT)));
                paramModel.setLeft(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LEFT)));
                paramModel.setMaterialList(getAllCollectionItem(paramModel.getIdDetail()));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllCollectionItem(String id) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COLLECTION_ITEM + " WHERE " + KEY_ID_COLLECTION_DETAIL_DB + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdItem(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_ITEM_DB)));
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_DETAIL_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                paramModel.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                paramModel.setAmountPaid(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_AMOUNT_PAID)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Order> getAllOrder(Customer cust) {
        List<Order> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_HEADER + " WHERE " + KEY_CUSTOMER_ID + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{cust.getId()});

        if (cursor.moveToFirst()) {
            do {
                Order paramModel = new Order();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_ORDER_HEADER_DB)));
                paramModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_ORDER_BACK_END)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setOrder_date(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setTanggal_kirim(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TANGGAL_KIRIM)));
                paramModel.setOrder_type(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ORDER_TYPE)));
                paramModel.setTop(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TOP)));
                paramModel.setStatusPaid(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_PAID)) != 0);
                paramModel.setIdStockHeaderDb(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB)));
                paramModel.setIdStockHeaderBE(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_BE)));
                paramModel.setOmzet(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_OMZET)));
                paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return arrayList;
    }

    public List<VisitSalesman> getAllCheckInPauseVisit(Map currentLocation) {
        if (currentLocation == null) {
            currentLocation = new HashMap();
            currentLocation.put("latitude", null);
            currentLocation.put("longitude", null);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        List<VisitSalesman> arrayList = new ArrayList<>();
        String selectQuery = null;
        Cursor cursor = null;
        selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_STATUS + " = 0";
        cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                VisitSalesman paramModel = new VisitSalesman();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CUSTOMER_DB)));
                paramModel.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setCustomerName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ADDRESS)));
                paramModel.setLatCheckIn(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                paramModel.setLongCheckIn(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                paramModel.setLatCheckOut(currentLocation.get("latitude") != null ? (Double) currentLocation.get("latitude") : 0);
                paramModel.setLongCheckOut(currentLocation.get("longitude") != null ? (Double) currentLocation.get("longitude") : 0);
                paramModel.setNoo(false);
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<VisitSalesman> getAllCheckInPauseNoo(Map currentLocation) {
        if (currentLocation == null) {
            currentLocation = new HashMap();
            currentLocation.put("latitude", null);
            currentLocation.put("longitude", null);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        List<VisitSalesman> arrayList = new ArrayList<>();
        String selectQuery = null;
        Cursor cursor = null;
        selectQuery = "SELECT * FROM " + TABLE_NOO + " WHERE " + KEY_STATUS + " = 0";
        cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                VisitSalesman paramModel = new VisitSalesman();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOO_DB)));
                paramModel.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOO_DB)));
                String rute = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE));
                paramModel.setRoute(Helper.checkTodayRoute(rute));
                paramModel.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setCustomerName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_NOO)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS_NOO)));
                paramModel.setLatCheckIn(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                paramModel.setLongCheckIn(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                paramModel.setNoo(true);

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public int getCountPauseCustomer() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQueryVisit, countQueryNoo;
        Cursor cursorVisit, cursorNoo;
        countQueryVisit = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_STATUS + " = " + Constants.PAUSE_VISIT;
        countQueryNoo = "SELECT * FROM " + TABLE_NOO + " WHERE " + KEY_STATUS + " = " + Constants.PAUSE_VISIT;

        cursorVisit = db.rawQuery(countQueryVisit, null);
        int count = cursorVisit.getCount();
        cursorVisit.close();

        cursorNoo = db.rawQuery(countQueryNoo, null);
        int countNoo = cursorNoo.getCount();
        cursorNoo.close();

        // return count
        return count + countNoo;
    }

    public int getCountInvoiceToday() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_INVOICE_HEADER + " WHERE " + KEY_DATE + " = ? ";
        cursor = db.rawQuery(countQuery, new String[]{Helper.getTodayDate(Constants.DATE_FORMAT_3)});

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountStockRequestToday() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_HEADER + " WHERE " + KEY_REQUEST_DATE + " = ? and " + KEY_STATUS + " = ? and " + KEY_IS_UNLOADING + " = 0 and " + KEY_IS_VERIF + " = 1";
        cursor = db.rawQuery(countQuery, new String[]{Helper.getTodayDate(Constants.DATE_FORMAT_3), Constants.STATUS_APPROVE});

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountInvoiceVerifToday() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_INVOICE_HEADER + " WHERE " + KEY_DATE + " = ? and " + KEY_IS_VERIF + " = 1";
        cursor = db.rawQuery(countQuery, new String[]{Helper.getTodayDate(Constants.DATE_FORMAT_3)});

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountOrderCustomer(Map req) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_ORDER_HEADER + " WHERE " + KEY_CUSTOMER_ID + " = ? and " + KEY_DATE + " = ?";
        cursor = db.rawQuery(countQuery, new String[]{req.get("id").toString(), req.get("date").toString()});

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountPaymentCustomer(Map req) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_INVOICE_HEADER + " WHERE " + KEY_CUSTOMER_ID + " = ? and " + KEY_PAID + " != 0";
        cursor = db.rawQuery(countQuery, new String[]{req.get("id").toString()});

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountInvoiceCustomer(Map req) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_INVOICE_HEADER + " WHERE " + KEY_CUSTOMER_ID + " = ? ";
        cursor = db.rawQuery(countQuery, new String[]{req.get("id").toString()});

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountCheckInVisitNonRoute() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE (" + KEY_STATUS + " = " + Constants.CHECK_IN_VISIT
                + " or " + KEY_STATUS + " = " + Constants.PAUSE_VISIT
                + " or " + KEY_STATUS + " = " + Constants.CHECK_OUT_VISIT
                + ") and " + KEY_IS_ROUTE + " = 0 ";//non ruote
        cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountCheckInPauseVisit() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_STATUS + " = " + Constants.CHECK_IN_VISIT + " or " + KEY_STATUS + " = " + Constants.PAUSE_VISIT;
        cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountCheckInPauseNoo() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_NOO + " WHERE " + KEY_STATUS + " = " + Constants.CHECK_IN_VISIT + " or " + KEY_STATUS + " = " + Constants.PAUSE_VISIT;
        cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountCheckInVisit() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_STATUS + " = " + Constants.CHECK_IN_VISIT;
        cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountCheckInNoo() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_NOO + " WHERE " + KEY_STATUS + " = " + Constants.CHECK_IN_VISIT;
        cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountNonRoute() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_MASTER_NON_ROUTE_CUSTOMER;
        cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getECS() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;

        countQuery = "SELECT * FROM " + TABLE_ORDER_HEADER + " group by " + KEY_CUSTOMER_ID;
        cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCallRoute(int type) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor = null;
        switch (type) {
            case 1://rute check in
                countQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_STATUS + " != 0 and " + KEY_IS_ROUTE + " = 1";
                cursor = db.rawQuery(countQuery, null);
                break;
            case 2: // non rute check in
                countQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_STATUS + " != 0 and " + KEY_IS_ROUTE + " = 0";
                cursor = db.rawQuery(countQuery, null);
                break;
            case 3: // all rute check in
                countQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_STATUS + " != 0 ";
                cursor = db.rawQuery(countQuery, null);
                break;
            case 4: // all rute belum check in
                countQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_STATUS + " = 0 ";
                cursor = db.rawQuery(countQuery, null);
                break;
        }

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCountRouteCustomer(boolean allRoute) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        if (allRoute) {
            countQuery = "SELECT * FROM " + TABLE_CUSTOMER;
            cursor = db.rawQuery(countQuery, null);
        } else {// today route
            countQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_ROUTE + " LIKE ?";
            cursor = db.rawQuery(countQuery, new String[]{"%" + Helper.getTodayRoute() + "%"});
        }

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public List<LogModel> getAllLog() {
        List<LogModel> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_LOG + " order by " + KEY_ID_LOG_DB + " desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LogModel paramModel = new LogModel();
                paramModel.setDescLog(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_LOG)));
                paramModel.setDateLog(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_LOG)));
                paramModel.setTimeLog(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME_LOG)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllMasterMaterial() {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_MATERIAL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setMaterial_sales(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_SALES)));
                paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public Material getMinimalOrder(Map request) {
        Material paramModel = new Material();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_UOM + " WHERE " + KEY_MATERIAL_ID + " = ? AND " + KEY_UOM + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{request.get("id_material").toString(), request.get("uom").toString()});

        if (cursor.moveToFirst()) {
            paramModel = new Material();
            paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
            paramModel.setQtyMin(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY_MIN)));
            paramModel.setQtyMax(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY_MAX)));
            paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
//            paramModel.setBon_limit(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_BON_LIMIT)));
        }
        cursor.close();
        return paramModel;
    }

    public List<Material> getOutstandingFaktur(String custId) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "select mm." + KEY_MATERIAL_GROUP_NAME + ", mm." + KEY_MATERIAL_GROUP_ID + ", count(mm." + KEY_MATERIAL_GROUP_ID + ") as " + KEY_QTY +
                " from " + TABLE_INVOICE_HEADER + " ih " +
                " join " + TABLE_INVOICE_DETAIL + " id on ih." + KEY_INVOICE_NO + " = id." + KEY_INVOICE_NO +
                " join " + TABLE_MASTER_MATERIAL + " mm on id." + KEY_MATERIAL_ID + " = mm." + KEY_MATERIAL_ID +
                " where ih." + KEY_CUSTOMER_ID + " = ? and ih." + KEY_PAID + " = 0 " +
                " group by mm." + KEY_MATERIAL_GROUP_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{custId});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllMasterMaterialOrder(Map request) {
        List<Material> arrayList = new ArrayList<>();
        String priceListCode = null, top = null;

        // Select All Query
        String queryPriceListCode = "SELECT " + KEY_PRICE_LIST_CODE + " FROM " + TABLE_MASTER_PRICE_CODE + " WHERE " + KEY_UDF_5 + " = ? and " + KEY_MATERIAL_PRODUCT_ID + " = ? ";

        String queryTop = "SELECT " + KEY_TOP + " FROM " + TABLE_MASTER_SALES_PRICE_HEADER + " WHERE " + KEY_PRICE_LIST_CODE + " like ? ";

        String queryMaterialList = "SELECT spd." + KEY_MATERIAL_ID + ", spd." + KEY_PRICE_LIST_CODE + ", spd." + KEY_UOM + ", spd." + KEY_QTY + ", spd." + SELLING_PRICE
                + ", m." + KEY_MATERIAL_NAME + ", m." + KEY_MATERIAL_SALES + ", m." + KEY_MATERIAL_GROUP_ID + ", m." + KEY_MATERIAL_GROUP_NAME
                + ", m." + KEY_MATERIAL_PRODUCT_ID + ", m." + KEY_MATERIAL_PRODUCT_NAME
                + " FROM " + TABLE_MASTER_SALES_PRICE_DETAIL + " spd join " + TABLE_MASTER_MATERIAL + " m on spd." + KEY_MATERIAL_ID + " = m." + KEY_MATERIAL_ID
                + " WHERE spd." + KEY_PRICE_LIST_CODE + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorPriceListCode = db.rawQuery(queryPriceListCode, new String[]{request.get("udf5").toString(), request.get("productId").toString()});

        if (cursorPriceListCode.moveToFirst()) {
            priceListCode = cursorPriceListCode.getString(cursorPriceListCode.getColumnIndexOrThrow(KEY_PRICE_LIST_CODE));
        }
        cursorPriceListCode.close();

        if (priceListCode != null) {
            Cursor cursorTop = db.rawQuery(queryTop, new String[]{"%" + priceListCode + "%"});

            if (cursorTop.moveToFirst()) {
                top = cursorTop.getString(cursorTop.getColumnIndexOrThrow(KEY_TOP));
            }
            //SELECT top FROM MasterSalesPriceHeader WHERE priceListCode = 'GT - TOP 14'
            cursorTop.close();

            Cursor cursor = db.rawQuery(queryMaterialList, new String[]{priceListCode});

            if (cursor.moveToFirst()) {
                do {
                    Material paramModel = new Material();
                    paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                    paramModel.setUomSisa(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                    paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                    paramModel.setQtySisa(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                    paramModel.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(SELLING_PRICE)));
                    paramModel.setMaterial_sales(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_SALES)));
                    paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                    paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                    paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                    paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                    paramModel.setTop(top);
                    paramModel.setPriceListCode(priceListCode);

                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }
        }

        return arrayList;
    }

    public List<Material> getAllMasterMaterialByCustomer(Map request) {
        List<Material> arrayList = new ArrayList<>();
        List<String> priceListCodeList = new ArrayList<>();
        String top = null;

//        select a.
//        priceListCode, b.top, d.materialId, c.sellingPrice, c.uom, c.qty, d.materialName, d.materialGroupId, d.materialGroupName, d.materialProductId, d.materialProductName
//        from mastertoppricecode a
//        join mastersalespriceheader b on a.priceListCode = b.priceListCode and date ('2023-11-14')
//        between b.validFrom and b.validTo
//        join MasterSalesPriceDetail c on a.priceListCode = c.priceListCode
//        join mastermaterial d on c.materialId = d.materialId
//        where a.udf5 = 'G' and a.priceListCode = ifnull('GT - TOP 14', a.priceListCode) and d.
//        materialGroupId = ifnull('11', d.materialGroupId) GROUP BY d.materialId
        String allQuery = null;
        String query = "select a." + KEY_PRICE_LIST_CODE + ", b." + KEY_TOP + ", d." + KEY_MATERIAL_ID + ", c." + SELLING_PRICE + ", c." + KEY_UOM
                + ", c." + KEY_QTY + ", d." + KEY_MATERIAL_NAME + ", d." + KEY_MATERIAL_GROUP_ID + ", d." + KEY_MATERIAL_GROUP_NAME
                + ", d." + KEY_MATERIAL_PRODUCT_ID + ", d." + KEY_MATERIAL_PRODUCT_NAME + ", d." + KEY_MATERIAL_SALES + " "
                + "from " + TABLE_MASTER_PRICE_CODE + " a "
                + "join " + TABLE_MASTER_SALES_PRICE_HEADER + " b on a." + KEY_PRICE_LIST_CODE + " = b." + KEY_PRICE_LIST_CODE + " and date(?) between b." + KEY_VALID_FROM + " and  b." + KEY_VALID_TO + " "
                + "join " + TABLE_MASTER_SALES_PRICE_DETAIL + " c on  a." + KEY_PRICE_LIST_CODE + " = c." + KEY_PRICE_LIST_CODE + " "
                + "join " + TABLE_MASTER_MATERIAL + " d on c." + KEY_MATERIAL_ID + " = d." + KEY_MATERIAL_ID + " "
                + "where a." + KEY_UDF_5 + " = ? ";
        String queryPriceList = "and a." + KEY_PRICE_LIST_CODE + " = \'" + request.get("price_list_code") + "\' ";//"and a." + KEY_PRICE_LIST_CODE + " = ifnull(?,a." + KEY_PRICE_LIST_CODE + ") ";
        String querymaterialGroupId = "and d." + KEY_MATERIAL_GROUP_ID + " = \'" + request.get("material_group_id") + "\' ";//"and d." + KEY_MATERIAL_GROUP_ID + " = ifnull(?, d." + KEY_MATERIAL_GROUP_ID + ") ";
        String groupBy = " GROUP BY d." + KEY_MATERIAL_ID + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (request.get("price_list_code") != null) {
            allQuery = query + queryPriceList;
        } else {
            allQuery = query;
        }
        if (request.get("material_group_id") != null) {
            allQuery = allQuery + querymaterialGroupId;
        }
        allQuery = allQuery + groupBy;
        // Select All Query

        try {
            cursor = db.rawQuery(allQuery, new String[]{Helper.getTodayDate(Constants.DATE_FORMAT_3), request.get("udf_5").toString()});
            if (cursor.moveToFirst()) {
                do {
                    Material paramModel = new Material();
                    paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                    paramModel.setUomSisa(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                    paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                    paramModel.setQtySisa(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                    paramModel.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(SELLING_PRICE)));
                    paramModel.setMaterial_sales(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_SALES)));
                    paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                    paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                    paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                    paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                    paramModel.setTop(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TOP)));
                    paramModel.setPriceListCode(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRICE_LIST_CODE)));

                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            arrayList = new ArrayList<>();
        }
        cursor.close();
        return arrayList;
    }

    public List<String> getUom(String idMat) {
        List<String> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_UOM + " FROM " + TABLE_MASTER_UOM + " WHERE " + KEY_MATERIAL_ID + " = ? order by " + KEY_CONVERSION + " asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMat});

        if (cursor.moveToFirst()) {
            do {
                String paramModel = cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public String getPhotoReturn(String idCust, String type, String idMat) {
        String paramModel = null;
        // Select All Query
        String selectQuery = "SELECT " + KEY_PHOTO + " FROM " + TABLE_PHOTO + " WHERE " + KEY_MATERIAL_ID + " = ? and " + KEY_CUSTOMER_ID + " = ? and " + KEY_TYPE_PHOTO + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMat, idCust, type});

        if (cursor.moveToFirst()) {
            paramModel = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO));
        }
        cursor.close();
        return paramModel;
    }

    public String getPhoto(String idCust, String type, String idDB) {
        String paramModel = null;
        // Select All Query
        String selectQuery = "SELECT " + KEY_PHOTO + " FROM " + TABLE_PHOTO + " WHERE " + KEY_ID_DB + " = ? and " + KEY_CUSTOMER_ID + " = ? and " + KEY_TYPE_PHOTO + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idDB, idCust, type});

        if (cursor.moveToFirst()) {
            paramModel = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO));
        }
        cursor.close();
        return paramModel;
    }

    public String getSmallUom(String idMat) {
        String paramModel = null;
        // Select All Query
        String selectQuery = "SELECT " + KEY_UOM + " FROM " + TABLE_MASTER_UOM + " WHERE " + KEY_MATERIAL_ID + " = ? order by " + KEY_CONVERSION + " asc limit 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMat});

        if (cursor.moveToFirst()) {
            paramModel = cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM));
        }
        cursor.close();
        return paramModel;
    }

    public StockRequest getDetailStockRequest(int idHeader) {
        StockRequest paramModel = new StockRequest();
        // Select All Query
        String selectQuery = "SELECT* FROM " + TABLE_STOCK_REQUEST_HEADER + " WHERE " + KEY_ID_STOCK_REQUEST_HEADER_DB + " = ? limit 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idHeader)});

        if (cursor.moveToFirst()) {
            paramModel.setReq_date(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQUEST_DATE)));
            paramModel.setNo_doc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_DOC)));
            paramModel.setTanggal_kirim(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TANGGAL_KIRIM)));
            paramModel.setNo_surat_jalan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_SURAT_JALAN)));
        }
        cursor.close();
        return paramModel;
    }

    public Customer getDetailCustomer(String idCustomer) {
        Customer paramModel = new Customer();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_CUSTOMER_ID + " = ? ";
        String selectQueryNoo = "SELECT * FROM " + TABLE_NOO + " WHERE " + KEY_ID_NOO_DB + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCustomer});

        if (cursor.moveToFirst()) {
            paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
            paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ADDRESS)));
        } else {
            cursor = db.rawQuery(selectQueryNoo, new String[]{idCustomer});
            if (cursor.moveToFirst()) {
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_NOO)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS_NOO)));
            }
        }

        cursor.close();
        return paramModel;
    }

    public double getPrice(Material material) {
        double price = 0;
        int conversion = 0;
        // Select All Query
        String selectQuery = "SELECT " + KEY_CONVERSION + " FROM " + TABLE_MASTER_UOM + " WHERE " + KEY_MATERIAL_ID + " = ? AND " + KEY_UOM + " = ? ";
        String smallUom = getSmallUom(material.getId());
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{material.getId(), material.getUom()});

        if (cursor.moveToFirst()) {
            do {
                conversion = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CONVERSION));
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (conversion == 0) {
            conversion = 1;
        }
        price = material.getQty() * conversion * material.getAmount();
        return price;
    }

    public Material getPriceMaterial(Map request) {
        Material result = new Material();
        String priceListCode = null, top = null;

        // Select All Query
        String queryPriceListCode = "SELECT " + KEY_PRICE_LIST_CODE + " FROM " + TABLE_MASTER_PRICE_CODE + " WHERE " + KEY_UDF_5 + " = ? and " + KEY_MATERIAL_PRODUCT_ID + " = ? ";

        String queryTop = "SELECT " + KEY_TOP + " FROM " + TABLE_MASTER_SALES_PRICE_HEADER + " WHERE " + KEY_PRICE_LIST_CODE + " like ? ";

        String queryMaterialList = "SELECT spd." + KEY_MATERIAL_ID + ", spd." + KEY_PRICE_LIST_CODE + ", spd." + KEY_UOM + ", spd." + KEY_QTY + ", spd." + SELLING_PRICE
                + ", m." + KEY_MATERIAL_NAME + ", m." + KEY_MATERIAL_SALES + ", m." + KEY_MATERIAL_GROUP_ID + ", m." + KEY_MATERIAL_GROUP_NAME
                + ", m." + KEY_MATERIAL_PRODUCT_ID + ", m." + KEY_MATERIAL_PRODUCT_NAME
                + " FROM " + TABLE_MASTER_SALES_PRICE_DETAIL + " spd join " + TABLE_MASTER_MATERIAL + " m on spd." + KEY_MATERIAL_ID + " = m." + KEY_MATERIAL_ID
                + " WHERE spd." + KEY_PRICE_LIST_CODE + " like ? and spd." + KEY_MATERIAL_ID + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursorPriceListCode = db.rawQuery(queryPriceListCode, new String[]{request.get("udf5").toString(), request.get("productId").toString()});

        if (cursorPriceListCode.moveToFirst()) {
            priceListCode = cursorPriceListCode.getString(cursorPriceListCode.getColumnIndexOrThrow(KEY_PRICE_LIST_CODE));
        }
        cursorPriceListCode.close();

        if (priceListCode != null) {
            Cursor cursorTop = db.rawQuery(queryTop, new String[]{"%" + priceListCode + "%"});

            if (cursorTop.moveToFirst()) {
                top = cursorTop.getString(cursorTop.getColumnIndexOrThrow(KEY_TOP));
            }
            //SELECT top FROM MasterSalesPriceHeader WHERE priceListCode = 'GT - TOP 14'
            cursorTop.close();

            Cursor cursor = db.rawQuery(queryMaterialList, new String[]{"%" + priceListCode + "%", request.get("matId").toString()});

            if (cursor.moveToFirst()) {
                do {
                    result = new Material();
                    result.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                    result.setUomSisa(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                    result.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                    result.setQtySisa(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                    result.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(SELLING_PRICE)));
                    result.setMaterial_sales(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_SALES)));
                    result.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                    result.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                    result.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                    result.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                    result.setTop(top);
                    result.setPriceListCode(priceListCode);

                } while (cursor.moveToNext());
            }
        }

        return result;
    }

//    public List<Uom> getUom(String idMat) {
//        List<Uom> arrayList = new ArrayList<>();
//        // Select All Query
//        String selectQuery = "SELECT "+KEY_UOM+" FROM " + TABLE_MASTER_UOM + " WHERE " + KEY_MATERIAL_ID + " = ?";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMat});
//
//        if (cursor.moveToFirst()) {
//            do {
//                Uom paramModel = new Uom();
//                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM_ID_DB)));
//                paramModel.setId_uom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
//                paramModel.setId_material(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
//                paramModel.setConversion(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CONVERSION)));
//
//                arrayList.add(paramModel);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return arrayList;
//    }

    public List<DaerahTingkat> getAllMasterDaerahTingkat() {
        List<DaerahTingkat> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_DAERAH_TINGKAT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DaerahTingkat paramModel = new DaerahTingkat();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DAERAH_TINGKAT_ID_DB)));
                paramModel.setKode_pos(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKode_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_DESA_KELURAHAN)));
                paramModel.setNama_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_DESA_KELURAHAN)));
                paramModel.setKode_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KECAMATAN)));
                paramModel.setNama_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KECAMATAN)));
                paramModel.setKode_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KOTA_KABUPATEN)));
                paramModel.setNama_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setKode_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PROVINSI)));
                paramModel.setNama_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROVINSI)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<DaerahTingkat> getAllKodePos(DaerahTingkat daerahTingkat) {
        List<DaerahTingkat> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "select * from " + TABLE_MASTER_DAERAH_TINGKAT
//                + " where " + KEY_NAME_DESA_KELURAHAN + " like ? and "
//                + KEY_NAME_KECAMATAN + " like ? and "
//                + KEY_NAME_KOTA_KABUPATEN + " like ? and "
//                + KEY_NAME_PROVINSI + " like ? "
                + " group by " + KEY_KODE_POS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DaerahTingkat paramModel = new DaerahTingkat();
                paramModel.setKode_pos(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKode_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_DESA_KELURAHAN)));
                paramModel.setNama_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_DESA_KELURAHAN)));
                paramModel.setKode_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KECAMATAN)));
                paramModel.setNama_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KECAMATAN)));
                paramModel.setKode_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KOTA_KABUPATEN)));
                paramModel.setNama_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setKode_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PROVINSI)));
                paramModel.setNama_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROVINSI)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public DaerahTingkat getAllDaerahTingkat(String kodePos) {
        DaerahTingkat paramModel = new DaerahTingkat();
        // Select All Query
        String selectQuery = "select * from " + TABLE_MASTER_DAERAH_TINGKAT
                + " where " + KEY_KODE_POS + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{kodePos});

        if (cursor.moveToFirst()) {
            paramModel = new DaerahTingkat();
            paramModel.setKode_pos(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
            paramModel.setKode_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_DESA_KELURAHAN)));
            paramModel.setNama_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_DESA_KELURAHAN)));
            paramModel.setKode_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KECAMATAN)));
            paramModel.setNama_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KECAMATAN)));
            paramModel.setKode_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KOTA_KABUPATEN)));
            paramModel.setNama_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
            paramModel.setKode_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PROVINSI)));
            paramModel.setNama_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROVINSI)));
        } else {
            paramModel = null;
        }
        cursor.close();
        return paramModel;
    }

    public List<DaerahTingkat> getAllKelurahan(DaerahTingkat daerahTingkat) {
        List<DaerahTingkat> arrayList = new ArrayList<>();
        String selectQuery = null, kodePosQuery = null;
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        selectQuery = "select * from " + TABLE_MASTER_DAERAH_TINGKAT
//                + " where " + KEY_KODE_POS + " = ? "
//                + KEY_NAME_KECAMATAN + " like ? and "
//                + KEY_NAME_KOTA_KABUPATEN + " like ? and "
//                + KEY_NAME_PROVINSI + " like ? "
                + " group by " + KEY_ID_DESA_KELURAHAN;
        cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DaerahTingkat paramModel = new DaerahTingkat();
                paramModel.setKode_pos(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKode_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_DESA_KELURAHAN)));
                paramModel.setNama_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_DESA_KELURAHAN)));
                paramModel.setKode_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KECAMATAN)));
                paramModel.setNama_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KECAMATAN)));
                paramModel.setKode_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KOTA_KABUPATEN)));
                paramModel.setNama_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setKode_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PROVINSI)));
                paramModel.setNama_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROVINSI)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<DaerahTingkat> getAllKecamatan(DaerahTingkat daerahTingkat) {
        List<DaerahTingkat> arrayList = new ArrayList<>();
        String selectQuery = null;
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        // Select All Query
        selectQuery = "select * from " + TABLE_MASTER_DAERAH_TINGKAT
//                + " where " + KEY_KODE_POS + " like ? and "
//                + KEY_NAME_DESA_KELURAHAN + " like ? and "
//                + KEY_NAME_KOTA_KABUPATEN + " like ? and "
//                + KEY_NAME_PROVINSI + " like ? "
                + " group by " + KEY_ID_KECAMATAN;

        cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DaerahTingkat paramModel = new DaerahTingkat();
                paramModel.setKode_pos(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKode_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_DESA_KELURAHAN)));
                paramModel.setNama_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_DESA_KELURAHAN)));
                paramModel.setKode_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KECAMATAN)));
                paramModel.setNama_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KECAMATAN)));
                paramModel.setKode_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KOTA_KABUPATEN)));
                paramModel.setNama_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setKode_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PROVINSI)));
                paramModel.setNama_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROVINSI)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<DaerahTingkat> getAllKabupaten(DaerahTingkat daerahTingkat) {
        List<DaerahTingkat> arrayList = new ArrayList<>();
        String selectQuery = null;
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        // Select All Query
        selectQuery = "select * from " + TABLE_MASTER_DAERAH_TINGKAT
//                + " where " + KEY_KODE_POS + " like ? and "
//                + KEY_NAME_DESA_KELURAHAN + " like ? and "
//                + KEY_NAME_KECAMATAN + " like ? and "
//                + KEY_NAME_PROVINSI + " like ? "
                + " group by " + KEY_ID_KOTA_KABUPATEN;

        cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DaerahTingkat paramModel = new DaerahTingkat();
                paramModel.setKode_pos(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKode_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_DESA_KELURAHAN)));
                paramModel.setNama_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_DESA_KELURAHAN)));
                paramModel.setKode_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KECAMATAN)));
                paramModel.setNama_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KECAMATAN)));
                paramModel.setKode_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KOTA_KABUPATEN)));
                paramModel.setNama_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setKode_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PROVINSI)));
                paramModel.setNama_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROVINSI)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<DaerahTingkat> getAllProvinsi(DaerahTingkat daerahTingkat) {
        List<DaerahTingkat> arrayList = new ArrayList<>();
        String selectQuery = null;
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        // Select All Query
        selectQuery = "select * from " + TABLE_MASTER_DAERAH_TINGKAT
//                + " where " + KEY_KODE_POS + " like ? and "
//                + KEY_NAME_DESA_KELURAHAN + " like ? and "
//                + KEY_NAME_KECAMATAN + " like ? and "
//                + KEY_NAME_KOTA_KABUPATEN + " like ? "
                + " group by " + KEY_ID_PROVINSI;

        cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DaerahTingkat paramModel = new DaerahTingkat();
                paramModel.setKode_pos(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                paramModel.setKode_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_DESA_KELURAHAN)));
                paramModel.setNama_kelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_DESA_KELURAHAN)));
                paramModel.setKode_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KECAMATAN)));
                paramModel.setNama_kecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KECAMATAN)));
                paramModel.setKode_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KOTA_KABUPATEN)));
                paramModel.setNama_kabupaten(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                paramModel.setKode_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PROVINSI)));
                paramModel.setNama_provinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROVINSI)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Promotion> getAllPromotion() {
        List<Promotion> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_PROMOTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Promotion paramModel = new Promotion();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PROMOTION_DB)));
                paramModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_PROMOTION)));
                paramModel.setNama_promo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROMOTION)));
                paramModel.setNo_promo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_PROMOTION)));
                paramModel.setKeterangan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC)));
                paramModel.setToleransi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TOLERANSI)));
                paramModel.setJenis_promosi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_JENIS_PROMOSI)));
                paramModel.setValid_from(cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALID_FROM_PROMOTION)));
                paramModel.setValid_to(cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALID_TO_PROMOTION)));
                paramModel.setSegmen(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SEGMEN)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public String getCreditLimit() {
        String result = null;
        // Select All Query
        String selectQuery = "SELECT " + KEY_VALUE + " FROM " + TABLE_MASTER_PARAMETER + " WHERE " + KEY_KEY_PARAMETER + " = \'LIMIT_CREDIT\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                result = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALUE));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public int getMaxPause() {
        int result = 0;
        // Select All Query
        String selectQuery = "SELECT " + KEY_VALUE + " FROM " + TABLE_MASTER_PARAMETER + " WHERE " + KEY_KEY_PARAMETER + " = \'MAX_PAUSE\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
//            do {
            result = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALUE)));
//            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public float getRadius() {
        float radius = 0;
        // Select All Query
        String selectQuery = "SELECT " + KEY_VALUE + " FROM " + TABLE_MASTER_PARAMETER + " WHERE " + KEY_KEY_PARAMETER + " = \'RADIUS\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
//            do {
            radius = cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_VALUE));
//            } while (cursor.moveToNext());
        }
        cursor.close();
        return radius;
    }

    public List<CustomerType> getAllCustomerType() {
        List<CustomerType> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_CUSTOMER_TYPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomerType paramModel = new CustomerType();
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CUSTOMER_TYPE)));
                paramModel.setType_price(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_TYPE_PRICE)));
                paramModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_TYPE_PRICE)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public VisitSalesman getVisitSalesman(Customer request) {
        VisitSalesman result = new VisitSalesman();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_VISIT_SALESMAN + " WHERE " + KEY_CUSTOMER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{request.getId()});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = new VisitSalesman();
                result.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_VISIT_SALESMAN_DB)));
                result.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                result.setIdVisit(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_VISIT)));
                result.setIdSalesman(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_SALESMAN)));
                result.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                result.setCheckInTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHECK_IN_TIME)));
                result.setCheckOutTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHECK_OUT_TIME)));
                result.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                result.setResumeTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_RESUME_TIME)));
                result.setPauseTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PAUSE_TIME)));
                result.setLatCheckIn(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LAT_CHECK_IN)));
                result.setLongCheckIn(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONG_CHECK_IN)));
                result.setLatCheckOut(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LAT_CHECK_OUT)));
                result.setLongCheckOut(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONG_CHECK_OUT)));
                result.setInside(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_INSIDE)) != 0);
                result.setInsideCheckOut(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_INSIDE_CHECK_OUT)) != 0);
                result.setIdPauseReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PAUSE_REASON)));
                result.setNamePauseReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PAUSE_REASON)));
                result.setDescPauseReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_PAUSE_REASON)));

                result.setPhotoPauseReason(getPhoto(result.getCustomerId(), "pause", result.getIdHeader()));

//                result.setPhotoPauseReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_PAUSE_REASON)));
                result.setTimer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DURATION)));
                result.setIdNotVisitReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOT_VISIT_REASON)));
                result.setNameNotVisitReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_NOT_VISIT_REASON)));
                result.setDescNotVisitReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_NOT_VISIT_REASON)));

                result.setPhotoNotVisitReason(getPhoto(result.getCustomerId(), "not_visit", result.getIdHeader()));

//                result.setPhotoNotVisitReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NOT_VISIT_REASON)));
                result.setIdNotBuyReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOT_BUY_REASON)));
                result.setNameNotBuyReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_NOT_BUY_REASON)));
                result.setDescNotBuyReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_NOT_BUY_REASON)));
//                result.setPhotoNotBuyReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NOT_BUY_REASON)));
                result.setPhotoNotBuyReason(getPhoto(result.getCustomerId(), "not_buy", result.getIdHeader()));
            }
        }
        cursor.close();
        return result;
    }

    //sync offline
    public List<Customer> getAllCustomerCheckOut() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Customer> arrayList = new ArrayList<>();
        String selectQuery = null, selectQueryNoo = null;
        Cursor cursor = null;
        selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " where " + KEY_STATUS + " = " + Constants.CHECK_OUT_VISIT;
        selectQueryNoo = "SELECT * FROM " + TABLE_NOO + " where " + KEY_STATUS + " = " + Constants.CHECK_OUT_VISIT;
        cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Customer paramModel = new Customer();
                    paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CUSTOMER_DB)));
                    paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));

                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }

            cursor = db.rawQuery(selectQueryNoo, null);

            if (cursor.moveToFirst()) {
                do {
                    Customer paramModel = new Customer();
                    paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOO_DB)));
                    paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOO_DB)));

                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    public List<Customer> getAllNoo() {
        List<Customer> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOO + " WHERE " + KEY_IS_SYNC + " = 0 and " + KEY_STATUS + " = " + Constants.CHECK_OUT_VISIT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Customer paramModel = new Customer();
                    paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOO_DB)));
                    paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_NOO)));
                    paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS_NOO)));
                    paramModel.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                    paramModel.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                    paramModel.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                    paramModel.setKode_pos(cursor.getString(cursor.getColumnIndexOrThrow(KEY_KODE_POS)));
                    paramModel.setUdf_5(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5)));
                    paramModel.setUdf_5_desc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UDF_5_DESC)));
                    paramModel.setIdKelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_DESA_KELURAHAN)));
                    paramModel.setKelurahan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_DESA_KELURAHAN)));
                    paramModel.setIdKecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KECAMATAN)));
                    paramModel.setKecamatan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KECAMATAN)));
                    paramModel.setIdKota(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_KOTA_KABUPATEN)));
                    paramModel.setKota(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_KOTA_KABUPATEN)));
                    paramModel.setIdProvinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PROVINSI)));
                    paramModel.setProvinsi(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PROVINSI)));
                    paramModel.setNo_tlp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                    paramModel.setNo_npwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_NPWP)));
                    paramModel.setNpwp_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_NPWP)));
                    paramModel.setNpwp_address(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS_NPWP)));
                    paramModel.setStatus_npwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS_NPWP)));
                    paramModel.setNik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_KTP)));
                    paramModel.setNama_pemilik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PEMILIK)));
//                paramModel.setNik(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NIK_PEMILIK)));
                    paramModel.setStatus_toko(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS_TOKO)));
                    paramModel.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOKASI)));
                    paramModel.setJenis_usaha(cursor.getString(cursor.getColumnIndexOrThrow(KEY_JENIS_USAHA)));
                    paramModel.setLama_usaha(cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAMA_USAHA)));
                    paramModel.setSuku(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SUKU)));
                    paramModel.setType_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER)));
                    paramModel.setName_type_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_CUSTOMER_NAME)));
                    paramModel.setType_price(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_PRICE)));
                    paramModel.setLimit_kredit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_CREDIT_LIMIT)));
                    paramModel.setRute(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROUTE)));
                    paramModel.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CREATED_DATE)));
//                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
//                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
//                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    public List<VisitSalesman> getAllVisitSalesman() {
        List<VisitSalesman> arrayList = new ArrayList<>();
        // Select All Query
//        String selectQuery = "SELECT * FROM " + TABLE_VISIT_SALESMAN + " WHERE " + KEY_IS_SYNC + " = 0 and " + KEY_CHECK_OUT_TIME + " is not null";
        String selectQuery = "select  sum(coalesce(b." + KEY_OMZET + ",0)) as " + KEY_OMZET + ", a.* from " + TABLE_VISIT_SALESMAN + " a " +
                "left join " + TABLE_ORDER_HEADER + " b on a." + KEY_CUSTOMER_ID + " = b." + KEY_CUSTOMER_ID + " " +
                "where a." + KEY_IS_SYNC + " = 0 and a." + KEY_CHECK_OUT_TIME + " is not null " +
                "group by a." + KEY_CUSTOMER_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    VisitSalesman result = new VisitSalesman();
                    result.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_VISIT_SALESMAN_DB)));
                    result.setTotalOrder(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_OMZET)));
                    result.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                    result.setIdVisit(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_VISIT)));
                    result.setIdSalesman(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_SALESMAN)));
                    result.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                    result.setCheckInTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHECK_IN_TIME)));
                    result.setCheckOutTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHECK_OUT_TIME)));
                    result.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                    result.setResumeTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_RESUME_TIME)));
                    result.setPauseTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PAUSE_TIME)));
                    result.setLatCheckIn(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LAT_CHECK_IN)));
                    result.setLongCheckIn(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONG_CHECK_IN)));
                    result.setLatCheckOut(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LAT_CHECK_OUT)));
                    result.setLongCheckOut(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONG_CHECK_OUT)));
                    result.setInside(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_INSIDE)) != 0);
                    result.setInsideCheckOut(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_INSIDE_CHECK_OUT)) != 0);
                    result.setIdPauseReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PAUSE_REASON)));
                    result.setNamePauseReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PAUSE_REASON)));
                    result.setDescPauseReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_PAUSE_REASON)));
                    result.setPhotoPauseReason(getPhoto(result.getCustomerId(), "pause", result.getIdHeader()));
//                    result.setPhotoPauseReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_PAUSE_REASON)));
                    result.setTimer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DURATION)));
                    result.setIdNotVisitReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOT_VISIT_REASON)));
                    result.setNameNotVisitReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_NOT_VISIT_REASON)));
                    result.setDescNotVisitReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_NOT_VISIT_REASON)));
//                    result.setPhotoNotVisitReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NOT_VISIT_REASON)));
                    result.setPhotoNotVisitReason(getPhoto(result.getCustomerId(), "not_visit", result.getIdHeader()));
                    result.setIdNotBuyReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOT_BUY_REASON)));
                    result.setNameNotBuyReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_NOT_BUY_REASON)));
                    result.setDescNotBuyReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_NOT_BUY_REASON)));
//                    result.setPhotoNotBuyReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NOT_BUY_REASON)));
                    result.setPhotoNotBuyReason(getPhoto(result.getCustomerId(), "not_buy", result.getIdHeader()));
                    arrayList.add(result);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllStoreCheckCheckOut(String id) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "select * from " + TABLE_STORE_CHECK + " where " + KEY_IS_SYNC + " = 0 and " + KEY_CUSTOMER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        try {
            if (cursor.moveToFirst()) {
                do {
                    Material paramModel = new Material();
                    paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_MOBILE)));
                    paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                    paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                    paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                    paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                    paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                    paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                    paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                    paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                    paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                    paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    public List<Order> getAllOrderHeader(String idCust, String username) {
        List<Order> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_HEADER + " WHERE " + KEY_IS_SYNC + " = 0 and " + KEY_CUSTOMER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCust});

        try {
            if (cursor.moveToFirst()) {
                do {
                    Order paramModel = new Order();
                    paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_ORDER_HEADER_DB)));
                    paramModel.setId_salesman(username);
                    paramModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_ORDER_BACK_END)));
                    paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                    paramModel.setOrder_type(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ORDER_TYPE)));
                    paramModel.setIdStockHeaderDb(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB)));
                    paramModel.setIdStockHeaderBE(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_BE)));
                    paramModel.setOrder_date(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                    paramModel.setTanggal_kirim(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TANGGAL_KIRIM)));
                    paramModel.setTop(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TOP)));
                    paramModel.setStatusPaid(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_PAID)) != 0);
                    paramModel.setOmzet(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_OMZET)));
                    paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                    paramModel.setMaterialList(getAllOrderDetail(paramModel.getIdHeader()));
                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllOrderDetail(String idHeader) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_DETAIL + " WHERE " + KEY_ID_ORDER_HEADER_DB + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idHeader});

        try {
            if (cursor.moveToFirst()) {
                do {
                    Material paramModel = new Material();
                    paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_ORDER_DETAIL_DB)));
                    paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                    paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                    paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                    paramModel.setPriceListCode(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRICE_LIST_CODE)));
                    paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                    paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                    paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                    paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                    paramModel.setIdStockRequestHeaderDB(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB)));
                    paramModel.setIdStockRequestHeaderBE(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_BE)));
                    paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                    paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                    paramModel.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                    paramModel.setTotalDiscount(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_DISCOUNT)));
                    paramModel.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL)));
                    paramModel.setDiskonList(getAllOrderDetailDiscount(paramModel.getIdheader()));
                    paramModel.setExtraItem(getAllOrderDetailExtra(paramModel.getIdheader()));
                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    public List<Discount> getAllOrderDetailDiscount(String idDetail) {
        List<Discount> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_DETAIL_DISCOUNT + " WHERE " + KEY_ID_ORDER_DETAIL_DB + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idDetail});

        try {
            if (cursor.moveToFirst()) {
                do {
                    Discount disc = new Discount();
                    disc.setKeydiskon(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DISCOUNT_ID)));
                    disc.setKeydiskon(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DISCOUNT_NAME)));
                    disc.setValuediskon(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DISCOUNT_PRICE)));
                    arrayList.add(disc);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllOrderDetailExtra(String idDetail) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_DETAIL_EXTRA + " WHERE " + KEY_ID_ORDER_DETAIL_DB + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idDetail});

        try {
            if (cursor.moveToFirst()) {
                do {
                    Material extra = new Material();
                    extra.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                    extra.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                    extra.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                    extra.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                    extra.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                    extra.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                    extra.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                    extra.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                    arrayList.add(extra);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    public List<Material> getAllReturnCheckOut(String id) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RETURN + " where " + KEY_IS_SYNC + " = 0 and " + KEY_CUSTOMER_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        try {
            if (cursor.moveToFirst()) {
                do {
                    Material paramModel = new Material();
                    paramModel.setIdheader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_MOBILE)));
                    paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                    paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                    paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                    paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                    paramModel.setId_material_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                    paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                    paramModel.setId_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                    paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                    paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                    paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                    paramModel.setExpiredDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EXPIRED_DATE)));
                    paramModel.setCondition(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONDITION)));
                    paramModel.setIdReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_REASON_RETURN)));
                    paramModel.setNameReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_REASON_RETURN)));
                    paramModel.setDescReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_REASON_RETURN)));
//                    paramModel.setPhotoReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_REASON_RETURN)));
                    paramModel.setPhotoReason(getPhotoReturn(paramModel.getId_customer(), "return", paramModel.getId()));
                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    public List<CollectionHeader> getAllCollectionHeader(String id) {
        List<CollectionHeader> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COLLECTION_HEADER + " WHERE " + KEY_IS_SYNC + " = 0 and " + KEY_CUSTOMER_ID + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        try {
            if (cursor.moveToFirst()) {
                do {
                    CollectionHeader paramModel = new CollectionHeader();
                    paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_HEADER_DB)));
                    paramModel.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                    paramModel.setInvoiceNo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_NO)));
                    paramModel.setInvoiceDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_DATE)));
                    paramModel.setInvoiceTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_INVOICE_TOTAL)));
                    paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                    paramModel.setTotalPaid(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_PAID)));
                    paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                    paramModel.setCashList(getAllCollectionDetailCash(paramModel.getIdHeader()));
                    paramModel.setTfList(getAllCollectionDetailTransfer(paramModel.getIdHeader()));
                    paramModel.setGiroList(getAllCollectionDetailGiro(paramModel.getIdHeader()));
                    paramModel.setChequeList(getAllCollectionDetailCheque(paramModel.getIdHeader()));
                    paramModel.setLainList(getAllCollectionDetailLain(paramModel.getIdHeader()));
                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    public List<Map> getAllPhoto(String id) {
        List<Map> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PHOTO + " WHERE " + KEY_IS_SYNC + " = 0 and " + KEY_CUSTOMER_ID + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        try {
            if (cursor.moveToFirst()) {
                do {
                    Map paramModel = new HashMap();
                    paramModel.put("idHeader", cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_PHOTO_DB)));
                    paramModel.put("photo", cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO)));
                    paramModel.put("idMaterial", cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                    paramModel.put("typePhoto", cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_PHOTO)));
                    paramModel.put("idDB", cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_DB)));
                    paramModel.put("customerId", cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                    arrayList.add(paramModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Helper.setItemParam(Constants.LOG_EXCEPTION, e.getMessage());
            arrayList = null;
        }
        cursor.close();
        return arrayList;
    }

    //sync offline

    //update
    public void updateVisit(VisitSalesman param, String username, boolean notBuy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, param.getStatus());
        switch (param.getStatus()) {
            case Constants.PAUSE_VISIT:
                values.put(KEY_PAUSE_TIME, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                values.put(KEY_ID_PAUSE_REASON, param.getIdPauseReason());
                values.put(KEY_NAME_PAUSE_REASON, param.getNamePauseReason());
                values.put(KEY_DESC_PAUSE_REASON, param.getDescPauseReason());
//                values.put(KEY_PHOTO_PAUSE_REASON, param.getPhotoPauseReason());
                values.put(KEY_DURATION, param.getTimer());
                values.put(KEY_RESUME_TIME, param.getResumeTime());
                break;
            case Constants.CHECK_IN_VISIT:
                values.put(KEY_RESUME_TIME, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                values.put(KEY_DURATION, param.getTimer());
                break;
            case Constants.CHECK_OUT_VISIT:
                values.put(KEY_CHECK_OUT_TIME, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                values.put(KEY_LAT_CHECK_OUT, param.getLatCheckOut());
                values.put(KEY_LONG_CHECK_OUT, param.getLongCheckOut());
                values.put(KEY_INSIDE_CHECK_OUT, param.isInsideCheckOut());

                if (notBuy) {
                    values.put(KEY_ID_NOT_BUY_REASON, param.getIdNotBuyReason());
                    values.put(KEY_NAME_NOT_BUY_REASON, param.getNameNotBuyReason());
                    values.put(KEY_DESC_NOT_BUY_REASON, param.getDescNotBuyReason());
//                    values.put(KEY_PHOTO_NOT_BUY_REASON, param.getPhotoNotBuyReason());
                } else {
                    values.put(KEY_ID_NOT_VISIT_REASON, param.getIdNotVisitReason());
                    values.put(KEY_NAME_NOT_VISIT_REASON, param.getNameNotVisitReason());
                    values.put(KEY_DESC_NOT_VISIT_REASON, param.getDescNotVisitReason());
//                    values.put(KEY_PHOTO_NOT_VISIT_REASON, param.getPhotoNotVisitReason());
                }
                break;
        }
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_VISIT_SALESMAN, values, KEY_CUSTOMER_ID + " = ? ", new String[]{param.getCustomerId()});
        //db.close();
    }

    public void updateSyncNoo(Customer req) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_SYNC, 1);
        values.put(KEY_UPDATED_BY, SessionManagerQubes.getUserProfile() != null ? SessionManagerQubes.getUserProfile().getUsername() : null);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_NOO, values, KEY_ID_NOO_DB + " = ?", new String[]{req.getId()});
        //db.close();
    }

    public void updateSyncVisitSalesman(VisitSalesman req) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_SYNC, 1);
        values.put(KEY_UPDATED_BY, SessionManagerQubes.getUserProfile() != null ? SessionManagerQubes.getUserProfile().getUsername() : null);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_VISIT_SALESMAN, values, KEY_ID_VISIT_SALESMAN_DB + " = ?", new String[]{req.getIdHeader()});
        //db.close();
    }

    public void updateSyncOrderHeader(Order req) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (req.getOrder_type().equals("CO")) {
            values.put(KEY_STATUS, "Approve");
        } else {
            values.put(KEY_STATUS, "Pending");
        }
        values.put(KEY_IS_SYNC, 1);
        values.put(KEY_UPDATED_BY, SessionManagerQubes.getUserProfile() != null ? SessionManagerQubes.getUserProfile().getUsername() : null);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_ORDER_HEADER, values, KEY_ID_ORDER_HEADER_DB + " = ?", new String[]{req.getIdHeader()});
        //db.close();
    }

    public void updateSyncCollectionHeader(CollectionHeader req) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_SYNC, 1);
        values.put(KEY_UPDATED_BY, SessionManagerQubes.getUserProfile() != null ? SessionManagerQubes.getUserProfile().getUsername() : null);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_COLLECTION_HEADER, values, KEY_ID_COLLECTION_HEADER_DB + " = ?", new String[]{req.getIdHeader()});
        //db.close();
    }

    public void updateSyncStoreCheck(Map req) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_SYNC, 1);
        values.put(KEY_UPDATED_BY, SessionManagerQubes.getUserProfile() != null ? SessionManagerQubes.getUserProfile().getUsername() : null);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        db.update(TABLE_STORE_CHECK, values, KEY_ID_MOBILE + " = ? ", new String[]{req.get("id_mobile").toString()});
    }

    public void updateSyncReturn(Map req) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_SYNC, 1);
        values.put(KEY_UPDATED_BY, SessionManagerQubes.getUserProfile() != null ? SessionManagerQubes.getUserProfile().getUsername() : null);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        db.update(TABLE_RETURN, values, KEY_ID_MOBILE + " = ? ", new String[]{req.get("id_mobile").toString()});
    }

    public void updateSyncPhoto(Map req) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_SYNC, 1);
        values.put(KEY_UPDATED_BY, SessionManagerQubes.getUserProfile() != null ? SessionManagerQubes.getUserProfile().getUsername() : null);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        db.update(TABLE_PHOTO, values, KEY_ID_PHOTO_DB + " = ? ", new String[]{req.get("idHeader").toString()});
    }

    public void updatePaidInvoice(Map request) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PAID, (Double) request.get("paid"));
        values.put(KEY_NETT, (Double) request.get("nett"));
        values.put(KEY_UPDATED_BY, request.get("username").toString());
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_INVOICE_HEADER, values, KEY_INVOICE_NO + " = ?", new String[]{request.get("no_invoice").toString()});
        //db.close();
    }

    public void updateNettPrice(Material request, String username, String invoiceNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        double paid = getPaidInvoiceMaterial(invoiceNo, request.getId());
        paid = paid + request.getAmountPaid();
        values.put(KEY_PAID, paid);
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_INVOICE_DETAIL, values, KEY_INVOICE_NO + " = ? and " + KEY_MATERIAL_ID + " = ?", new String[]{invoiceNo, request.getId()});
        //db.close();
    }

    public void updateStatusOutletNoo(Customer param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_NOO, values, KEY_ID_NOO_DB + " = ?", new String[]{param.getId()});
        //db.close();
    }

    public void updateStatusPaid(boolean paid, Order header, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_PAID, paid);
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_ORDER_HEADER, values, KEY_ID_ORDER_HEADER_DB + " = ?", new String[]{header.getIdHeader()});
        //db.close();
    }

    public void updateSisaStock(Map request) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PAID, (Double) request.get("paid"));
        values.put(KEY_NETT, (Double) request.get("nett"));
        values.put(KEY_UPDATED_BY, request.get("username").toString());
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_INVOICE_HEADER, values, KEY_INVOICE_NO + " = ?", new String[]{request.get("no_invoice").toString()});
        //db.close();
    }

    public void updateStatusOutletVisit(Customer param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_CUSTOMER, values, KEY_CUSTOMER_ID + " = ?", new String[]{param.getId()});
        //db.close();
    }

//    public void updatePhoto(Customer param, String username) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_PHOTO_KTP, param.getPhotoKtp());
//        values.put(KEY_PHOTO_NPWP, param.getPhotoNpwp());
//        values.put(KEY_PHOTO_OUTLET, param.getPhotoOutlet());
//        values.put(KEY_UPDATED_BY, username);
//        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
//
//        db.update(TABLE_CUSTOMER, values, KEY_CUSTOMER_ID + " = ?", new String[]{param.getId()});
//        //db.close();
//    }

    public void updatePhoto(Map req) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PHOTO, req.get("photo").toString());
        values.put(KEY_UPDATED_BY, req.get("username").toString());
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_PHOTO, values, KEY_CUSTOMER_ID + " = ? and " + KEY_TYPE_PHOTO + " = ? ", new String[]{req.get("customerID").toString(), req.get("typePhoto").toString()});
        //db.close();
    }

//    public void updatePhotoNoo(Customer param, String username) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_PHOTO_KTP, param.getPhotoKtp());
//        values.put(KEY_PHOTO_NPWP, param.getPhotoNpwp());
//        values.put(KEY_PHOTO_OUTLET, param.getPhotoOutlet());
//        values.put(KEY_UPDATED_BY, username);
//        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
//
//        db.update(TABLE_NOO, values, KEY_ID_NOO_DB + " = ?", new String[]{param.getIdHeader()});
//        //db.close();
//    }

    public void updateStockRequestVerification(StockRequest param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_VERIF, param.getIs_verif());
        values.put(KEY_SIGN, param.getSignature());
        values.put(KEY_IS_SYNC, param.getIsSync());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_STOCK_REQUEST_HEADER, values, KEY_ID_STOCK_REQUEST_HEADER_DB + " = ?",
                new String[]{param.getIdHeader()});
        //db.close();
    }

    public void updateUnloading(StockRequest param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_UNLOADING, param.getIs_unloading());
        values.put(KEY_IS_SYNC, param.getIsSync());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_STOCK_REQUEST_HEADER, values, KEY_ID_STOCK_REQUEST_HEADER_DB + " = ?",
                new String[]{param.getIdHeader()});
        //db.close();
    }

    public void updateInvoiceVerification(Invoice param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_VERIF, param.getIs_verif());
        values.put(KEY_SIGN, param.getSignature());
        values.put(KEY_IS_SYNC, param.getIsSync());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_INVOICE_HEADER, values, KEY_ID_INVOICE_HEADER_DB + " = ?",
                new String[]{param.getIdHeader()});
        //db.close();
    }

    //delete

    public void deleteAllDatabase() {
        deleteStockRequestHeader();
        deleteStockRequestDetail();
        deleteInvoiceHeader();
        deleteInvoiceDetail();
        deleteNoo();
        deleteCustomer();
        deleteCustomerPromotion();
        deleteCustomerDct();
        deleteVisitSalesman();
        deleteStoreCheck();
        deleteOrderHeader();
        deleteOrderDetail();
        deleteOrderDetailExtra();
        deleteReturn();
        deleteCollectionHeader();
        deleteCollectionDetail();
        deleteCollectionItem();
        deleteMasterReason();
        deleteMasterPromotion();
        deleteMasterLog();
        deleteMasterBank();
        deleteMasterNonRouteCustomer();
        deleteMasterNonRouteCustomerPromotion();
        deleteMasterNonRouteCustomerDct();
        deleteMasterMaterial();
        deleteMasterUom();
        deleteMasterDaerahTingkat();
        deleteMasterPriceCode();
        deleteMasterSalesPriceHeader();
        deleteMasterSalesPriceDetail();
        deleteMasterMinimalOrder();
        deleteMasterParameter();
        deleteMasterCustomerType();
        deletePhoto();
    }

    public void deleteStockRequestHeader() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_STOCK_REQUEST_HEADER); //+ " WHERE " + KEY_SHIPMENT_CATEGORY + " = \'" + type + "\'");
        //db.close();
    }

    public void deleteStoreCheck(Map header) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STORE_CHECK, KEY_DATE + " = ? and " + KEY_CUSTOMER_ID + " = ? ", new String[]{header.get("date").toString(), header.get("id_customer").toString()});
        db.close();
    }

    public void deleteReturn(Map header) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RETURN, KEY_DATE + " = ? and " + KEY_CUSTOMER_ID + " = ? ", new String[]{header.get("date").toString(), header.get("id_customer").toString()});
        db.close();
    }

    public void deletePhotoReturn() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTO, KEY_TYPE_PHOTO + " = ? ", new String[]{"return"});
        db.close();
    }

    public void deleteParameterByKey(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MASTER_PARAMETER, KEY_KEY_PARAMETER + " = ? ", new String[]{key});
        db.close();
    }

    public void deleteStockRequestDetail() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_STOCK_REQUEST_DETAIL);
    }

    public void deleteInvoiceHeader() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_INVOICE_HEADER);
    }

    public void deleteInvoiceDetail() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_INVOICE_DETAIL);
    }

    public void deleteNoo() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_NOO);
    }

    public void deleteCustomer() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_CUSTOMER);
    }

    public void deleteCustomerPromotion() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_CUSTOMER_PROMOTION);
    }

    public void deleteCustomerDct() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_CUSTOMER_DCT);
    }

    public void deleteVisitSalesman() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_VISIT_SALESMAN);
    }

    public void deleteStoreCheck() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_STORE_CHECK);
    }

    public void deleteOrderHeader() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_ORDER_HEADER);
    }

    public void deleteOrderDetail() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_ORDER_DETAIL);
    }

    public void deleteOrderDetailExtra() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_ORDER_DETAIL_EXTRA);
    }

    public void deleteReturn() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_RETURN);
    }

    public void deleteCollectionHeader() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_COLLECTION_HEADER);
    }

    public void deleteCollectionDetail() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_COLLECTION_DETAIL);
    }

    public void deleteCollectionItem() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_COLLECTION_ITEM);
    }

    public void deleteMasterReason() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_REASON);
    }

    public void deleteMasterPromotion() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_PROMOTION);
    }

    public void deleteMasterLog() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_LOG);
    }

    public void deleteMasterBank() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_BANK);
    }

    public void deleteMasterNonRouteCustomerById(String idHeader) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_MASTER_NON_ROUTE_CUSTOMER + " WHERE " + KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB + " = " + idHeader);
        //db.close();
    }

    public void deleteMasterNonRouteCustomerPromotionById(String idHeader) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION + " WHERE " + KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB + " = " + idHeader);
        //db.close();
    }

    public void deleteMasterNonRouteCustomerDctById(String idHeader) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_MASTER_NON_ROUTE_CUSTOMER_DCT + " WHERE " + KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB + " = " + idHeader);
        //db.close();
    }

    public void deleteMasterNonRouteCustomer() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_NON_ROUTE_CUSTOMER);
    }

    public void deleteMasterNonRouteCustomerPromotion() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION);
    }

    public void deleteMasterNonRouteCustomerDct() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_NON_ROUTE_CUSTOMER_DCT);
    }

    public void deleteMasterMaterial() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_MATERIAL);
    }

    public void deleteMasterUom() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_UOM);
    }

    public void deleteMasterDaerahTingkat() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_DAERAH_TINGKAT);
    }

    public void deleteMasterPriceCode() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_PRICE_CODE);
    }

    public void deleteMasterSalesPriceHeader() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_SALES_PRICE_HEADER);
    }

    public void deleteMasterSalesPriceDetail() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_SALES_PRICE_DETAIL);
    }

    public void deleteMasterMinimalOrder() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_LIMIT_BON);
    }

    public void deleteMasterParameter() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_PARAMETER);
    }

    public void deleteMasterCustomerType() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_CUSTOMER_TYPE);
    }

    public void deletePhoto() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_PHOTO);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}

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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Bank;
import id.co.qualitas.qubes.model.CollectionDetail;
import id.co.qualitas.qubes.model.CollectionDetail;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.CollectionDetail;
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
import id.co.qualitas.qubes.model.Uom;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;

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
    private static final String TABLE_VISIT_SALESMAN = "VisitSalesman";
    private static final String TABLE_VISIT_SALESMAN_NOO = "VisitSalesmanNOO";
    private static final String TABLE_STORE_CHECK = "StoreCheck";
    private static final String TABLE_ORDER_HEADER = "OrderHeader";
    private static final String TABLE_ORDER_DETAIL = "OrderDetail";
    private static final String TABLE_ORDER_DETAIL_EXTRA = "OrderDetailExtra";
    private static final String TABLE_ORDER_DETAIL_DISCOUNT = "OrderDetailDiscount";
    private static final String TABLE_ORDER_PAYMENT_HEADER = "OrderPaymentHeader";
    private static final String TABLE_ORDER_PAYMENT_DETAIL = "OrderPaymentDetail";
    private static final String TABLE_ORDER_PAYMENT_ITEM = "OrderPaymentItem";
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
    private static final String TABLE_MASTER_PRICE_CODE = "MasterTopPriceCode";
    private static final String TABLE_MASTER_SALES_PRICE_HEADER = "MasterSalesPriceHeader";
    private static final String TABLE_MASTER_SALES_PRICE_DETAIL = "MasterSalesPriceDetail";
    private static final String TABLE_MASTER_PARAMETER = "MasterParameter";
    private static final String TABLE_MASTER_CUSTOMER_TYPE = "MasterCustomerType";
    private static final String TABLE_LOG = "Log";

    private static final String KEY_ID_CUSTOMER_TYPE = "idCustomerType";
    private static final String KEY_ID_TYPE_PRICE = "idTypePrice";
    private static final String KEY_NAME_TYPE_PRICE = "nameTypePrice";
    private static final String KEY_ID_MASTER_NON_ROUTE_CUSTOMER_PROMOTION_DB = "idMasterNonRouteCustomer";

    // column table parameter
    private static final String KEY_ID_PARAMETER_DB = "idParameterDB";
    private static final String KEY_KEY_PARAMETER = "keyParameter";
    private static final String KEY_VALUE = "value";
    private static final String KEY_DESC = "description";

    // column table price code
    private static final String KEY_ID_PRICE_CODE_DB = "idPriceCodeDB";
    //    private static final String KEY_MATERIAL_PRODUCT_ID = "materialProductId";
//    private static final String KEY_UDF_5 = "udf5";
//    private static final String KEY_UDF_5_DESC = "udf5Desc";
    private static final String KEY_PRICE_LIST_CODE = "priceListCode";

    //MasterSalesPriceHeader
    private static final String KEY_ID_SALES_PRICE_HEADER_DB = "idSalesPriceHeaderDB";
    private static final String KEY_TOP = "top";
//    private static final String KEY_PRICE_LIST_CODE = "priceListCode";
    //    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    //MasterSalesPriceDetail
    private static final String KEY_ID_SALES_PRICE_DETAIL_DB = "idSalesPriceDetailDB";
    //    private static final String KEY_MATERIAL_ID = "materialId";
//    private static final String KEY_PRICE_LIST_CODE = "priceListCode";
//    private static final String KEY_UOM = "uom";
//    private static final String KEY_QTY = "qty";
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
    //    private static final String KEY_ID_STOCK_REQUEST_HEADER_DB = "idStockRequestHeaderDB";
    private static final String KEY_MATERIAL_ID = "materialId";
    private static final String KEY_MATERIAL_NAME = "materialName";
    private static final String KEY_MATERIAL_GROUP_ID = "materialGroupId";
    private static final String KEY_MATERIAL_GROUP_NAME = "materialGroupName";
    private static final String KEY_LOAD_NUMBER = "loadNumber";
    private static final String KEY_QTY = "qty";
    private static final String KEY_UOM = "uom";
    private static final String KEY_QTY_SISA = "qtySisa";
    private static final String KEY_UOM_SISA = "uomSisa";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

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
    private static final String KEY_CUSTOMER_NAME = "customerName";
    private static final String KEY_TYPE_CUSTOMER = "typeCustomer";
    private static final String KEY_TYPE_CUSTOMER_NAME = "typeCustomerName";
    private static final String KEY_TYPE_PRICE = "typePrice";
    //    private static final String KEY_SIGN = "signature";
//    private static final String KEY_IS_VERIF = "isVerif";
    private static final String KEY_IS_ROUTE = "isRoute";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table InvoiceDetail
    private static final String KEY_ID_INVOICE_DETAIL_DB = "idInvoiceDetailDB";
    //    private static final String KEY_ID_INVOICE_HEADER_DB = "idInvoiceHeaderDB";
//    private static final String KEY_MATERIAL_ID = "materialId";
//    private static final String KEY_MATERIAL_NAME = "materialName";
    private static final String KEY_PRICE = "price";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

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
    //    private static final String KEY_TYPE_CUSTOMER = "typeTokoOutlet";
//    private static final String KEY_TYPE_PRICE = "priceListType";
    private static final String KEY_CREDIT_LIMIT = "creditLimit";
    private static final String KEY_ROUTE = "route";
    private static final String KEY_PHOTO_KTP = "photoKtp";
    private static final String KEY_PHOTO_NPWP = "photoNpwp";
    private static final String KEY_PHOTO_OUTLET = "photoOutlet";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table Customer
    private static final String KEY_ID_CUSTOMER_DB = "idCustomerDB";
    //    private static final String KEY_CUSTOMER_ID = "customerId";
//    private static final String KEY_CUSTOMER_NAME = "customerName";
    private static final String KEY_CUSTOMER_ADDRESS = "customerAddress";
    //    private static final String KEY_PHONE = "phone";
    private static final String KEY_SISA_KREDIT_LIMIT = "sisaKreditLimit";
    //    private static final String KEY_CREDIT_LIMIT = "creditLimit";
    private static final String KEY_UDF_5 = "udf5";
    private static final String KEY_UDF_5_DESC = "udf5Desc";
    private static final String KEY_TOTAL_TAGIHAN = "totalTagihan";
//    private static final String KEY_NO_KTP = "noKtp";
//    private static final String KEY_NO_NPWP = "noNPWP";
//    private static final String KEY_IS_ROUTE = "isRoute";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table Customer promotion
    private static final String KEY_ID_CUSTOMER_PROMOTION_DB = "idCustomerPromotionDB";
    //    private static final String KEY_CUSTOMER_ID = "customerId";
    private static final String KEY_ID_PROMOTION = "idPromotion";
    private static final String KEY_NAME_PROMOTION = "namePromotion";
    private static final String KEY_NO_PROMOTION = "noPromotion";
    private static final String KEY_TOLERANSI = "toleransi";
    private static final String KEY_JENIS_PROMOSI = "jenisPromosi";
    private static final String KEY_SEGMEN = "segmen";
    private static final String KEY_VALID_FROM_PROMOTION = "validFromPromotion";
    private static final String KEY_VALID_TO_PROMOTION = "validToPromotion";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table VisitSalesman
    private static final String KEY_ID_VISIT_SALESMAN_DB = "idVisitSalesmanDB";
    //    private static final String KEY_CUSTOMER_ID = "customerId";
//    private static final String KEY_ID_SALESMAN = "idSalesman";
    private static final String KEY_DATE = "date";
    private static final String KEY_CHECK_IN_TIME = "CheckInTime";
    private static final String KEY_CHECK_OUT_TIME = "CheckOutTime";
    private static final String KEY_STATUS = "Status";
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
    private static final String KEY_PHOTO_PAUSE_REASON = "photoPauseReason";
    private static final String KEY_ID_CHECK_OUT_REASON = "idCheckOutReason";
    private static final String KEY_NAME_CHECK_OUT_REASON = "nameCheckOutReason";
    private static final String KEY_DESC_CHECK_OUT_REASON = "descCheckOutReason";
    private static final String KEY_PHOTO_CHECK_OUT_REASON = "photoCheckOutReason";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table VisitSalesmanNOO
    private static final String KEY_ID_VISIT_SALESMAN_NOO_DB = "idVisitSalesmanNooDB";
    //    private static final String KEY_ID_NOO_DB = "idNooDB";
//    private static final String KEY_ID_SALESMAN = "idSalesman";
//    private static final String KEY_DATE = "date";
//    private static final String KEY_CHECK_IN_TIME = "CheckInTime";
//    private static final String KEY_CHECK_OUT_TIME = "CheckOutTime";
//    private static final String KEY_RESUME_TIME = "resumeTime";
//    private static final String KEY_PAUSE_TIME = "pauseTime";
//    private static final String KEY_LAT_CHECK_IN = "latCheckIn";
//    private static final String KEY_LONG_CHECK_IN = "longCheckIn";
//    private static final String KEY_LAT_CHECK_OUT = "latCheckOut";
//    private static final String KEY_LONG_CHECK_OUT = "longCheckOut";
//    private static final String KEY_INSIDE = "inside";
//    private static final String KEY_INSIDE_CHECK_OUT = "insideCheckOut";
//    private static final String KEY_ID_PAUSE_REASON = "pauseReason";
//    private static final String KEY_NAME_PAUSE_REASON = "pauseReason";
//    private static final String KEY_DESC_PAUSE_REASON = "descPauseReason";
//    private static final String KEY_PHOTO_PAUSE_REASON = "photoPauseReason";
//    private static final String KEY_ID_CHECK_OUT_REASON = "checkOutReason";
//    private static final String KEY_NAME_CHECK_OUT_REASON = "checkOutReason";
//    private static final String KEY_DESC_CHECK_OUT_REASON = "descCheckOutReason";
//    private static final String KEY_PHOTO_CHECK_OUT_REASON = "photoCheckOutReason";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table StoreCheck
    private static final String KEY_ID_STORE_CHECK_DB = "idStoreCheckDB";
//    private static final String KEY_CUSTOMER_ID = "customerId";
//    private static final String KEY_DATE = "date";
//    private static final String KEY_MATERIAL_ID = "materialId";
//    private static final String KEY_MATERIAL_NAME = "materialName";
//    private static final String KEY_QTY = "qty";
//    private static final String KEY_UOM = "uom";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table OrderHeader
    private static final String KEY_ID_ORDER_HEADER_DB = "idOrderHeaderDB";
    //    private static final String KEY_CUSTOMER_ID = "customerId";
//    private static final String KEY_DATE = "date";
    private static final String KEY_ID_ORDER_BACK_END = "idOrderBackEnd";
    private static final String KEY_OMZET = "omzet";
//    private static final String KEY_STATUS = "status";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table OrderDetail
    private static final String KEY_ID_ORDER_DETAIL_DB = "idOrderDetailDB";
    //    private static final String KEY_ID_ORDER_HEADER_DB = "idOrderHeaderDB";
//    private static final String KEY_CUSTOMER_ID = "customerId";
//    private static final String KEY_MATERIAL_ID = "materialId";
//    private static final String KEY_MATERIAL_NAME = "materialName";
//    private static final String KEY_QTY = "qty";
//    private static final String KEY_UOM = "uom";
//    private static final String KEY_PRICE = "price";
    private static final String KEY_TOTAL_DISCOUNT = "totalDiscount";
    private static final String KEY_TOTAL = "total";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table OrderDetailExtra
    private static final String KEY_ID_ORDER_DETAIL_EXTRA_DB = "idOrderDetailExtraDB";
    //    private static final String KEY_ID_ORDER_DETAIL_DB = "idOrderDetailDB";
//    private static final String KEY_CUSTOMER_ID = "customerId";
//    private static final String KEY_MATERIAL_ID = "materialId";
//    private static final String KEY_MATERIAL_NAME = "materialName";
//    private static final String KEY_QTY = "qty";
//    private static final String KEY_UOM = "uom";
//    private static final String KEY_PRICE = "price";
//    private static final String KEY_TOTAL_DISCOUNT = "totalDiscount";
//    private static final String KEY_TOTAL = "total";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table OrderDetailDiscount
    private static final String KEY_ID_ORDER_DETAIL_DISCOUNT_DB = "idOrderDetailDiscountDB";
    //    private static final String KEY_ID_ORDER_DETAIL_DB = "idOrderDetailDB";
    //    private static final String KEY_CUSTOMER_ID = "customerId";
    //    private static final String KEY_MATERIAL_ID = "materialId";
    private static final String KEY_DISCOUNT_ID = "discountID";
    private static final String KEY_DISCOUNT_NAME = "discountName";
    private static final String KEY_DISCOUNT_PRICE = "discountPrice";
//    private static final String KEY_TOTAL = "total";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table OrderPaymentHeader
    private static final String KEY_ID_ORDER_PAYMENT_HEADER_DB = "idOrderPaymentHeaderDB";
    //    private static final String KEY_ID_ORDER_HEADER_DB = "idOrderHeaderDB";
    //    private static final String KEY_DATE = "date";
    //    private static final String KEY_OMZET = "omzet";
    //    private static final String KEY_STATUS = "status";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table OrderPaymentDetail
    private static final String KEY_ID_ORDER_PAYMENT_DETAIL_DB = "idOrderPaymentDetailDB";
    //    private static final String KEY_ID_ORDER_PAYMENT_HEADER_DB = "idOrderPaymentHeaderDB";`
    //    private static final String KEY_STATUS = "status";
    private static final String KEY_TYPE_PAYMENT = "typePayment";
    private static final String KEY_TOTAL_PAYMENT = "totalPayment";
    private static final String KEY_LEFT = "leftPayment";
    private static final String KEY_NO = "nomor";
    //    private static final String KEY_DUE_DATE = "dueDate";
    private static final String KEY_ID_BANK = "idBank";
    private static final String KEY_NAME_BANK = "nameBank";
    private static final String KEY_ID_CUST_BANK = "idCustBank";
    private static final String KEY_NAME_CUST_BANK = "nameCustBank";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table OrderPaymentItem
    private static final String KEY_ID_ORDER_PAYMENT_ITEM_DB = "idOrderPaymentItemDB";
    //    private static final String KEY_ID_ORDER_PAYMENT_DETAIL_DB = "idOrderPaymentDetailDB";
//    private static final String KEY_MATERIAL_ID = "materialId";
//    private static final String KEY_MATERIAL_NAME = "materialName";
//    private static final String KEY_PRICE = "price";
    private static final String KEY_AMOUNT_PAID = "amountPaid";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";;

    // column table Return
    private static final String KEY_ID_RETURN_DB = "idReturnDB";
    //    private static final String KEY_CUSTOMER_ID = "customerId";
//    private static final String KEY_DATE = "date";
//    private static final String KEY_MATERIAL_ID = "materialId";
//    private static final String KEY_MATERIAL_NAME = "materialName";
//    private static final String KEY_QTY = "qty";
//    private static final String KEY_UOM = "uom";
    private static final String KEY_EXPIRED_DATE = "expiredDate";
    private static final String KEY_CONDITION = "condition";
    private static final String KEY_ID_REASON_RETURN = "idReasonReturn";
    private static final String KEY_NAME_REASON_RETURN = "nameReasonReturn";
    private static final String KEY_DESC_REASON_RETURN = "descReasonReturn";
    private static final String KEY_PHOTO_REASON_RETURN = "photoReasonReturn";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table CollectionHeader
    private static final String KEY_ID_COLLECTION_HEADER_DB = "idCollectionHeaderDB";
    //    private static final String KEY_INVOICE_NO = "invoiceNo";
    //    private static final String KEY_INVOICE_DATE = "invoiceDate";
    //    private static final String KEY_INVOICE_TOTAL = "invoiceTotal";
    //    private static final String KEY_STATUS = "status";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table CollectionDetail
    private static final String KEY_ID_COLLECTION_DETAIL_DB = "idCollectionDetailDB";
    //    private static final String KEY_ID_COLLECTION_HEADER_DB = "idCollectionHeaderDB";
    //    private static final String KEY_STATUS = "status";
    //    private static final String KEY_TYPE_PAYMENT = "typePayment";
    //    private static final String KEY_TOTAL_PAYMENT = "totalPayment";
    //    private static final String KEY_LEFT = "left";
    //    private static final String KEY_NO = "giroNo";
    //    private static final String KEY_DUE_DATE = "dueDate";
    //    private static final String KEY_ID_BANK = "idBank";
    //    private static final String KEY_NAME_BANK = "nameBank";
    //    private static final String KEY_ID_CUST_BANK = "idCustBank";
    //    private static final String KEY_NAME_CUST_BANK = "nameCustBank";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table CollectionItem
    private static final String KEY_ID_COLLECTION_ITEM_DB = "idCollectionItemDB";
    //    private static final String KEY_ID_COLLECTION_DETAIL_DB = "idCollectionDetailDB";
    //    private static final String KEY_MATERIAL_ID = "materialId";
    //    private static final String KEY_MATERIAL_NAME = "materialName";
    //    private static final String KEY_PRICE = "price";
    //    private static final String KEY_AMOUNT_PAID = "amountPaid";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table MasterReason
    private static final String KEY_ID_REASON_DB = "idReasonDB";
    private static final String KEY_ID_REASON_BE = "idReasonBE";
    private static final String KEY_NAME_REASON = "nameReason";
    private static final String KEY_CATEGORY_REASON = "categoryReason";
    private static final String KEY_IS_PHOTO = "isPhoto";
    private static final String KEY_IS_FREE_TEXT = "isFreeText";
    private static final String KEY_IS_BARCODE = "isBarcode";
    private static final String KEY_IS_SIGNATURE = "isSignature";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table MasterPromotion
    private static final String KEY_ID_PROMOTION_DB = "idPromotionDB";
//    private static final String KEY_ID_PROMOTION = "idPromotion";
//    private static final String KEY_NAME_PROMOTION = "namePromotion";
//    private static final String KEY_VALID_FROM_PROMOTION = "validFromPromotion";
//    private static final String KEY_VALID_TO_PROMOTION = "validToPromotion";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    // column table LogModel
    private static final String KEY_ID_LOG_DB = "idLogDB";
    private static final String KEY_DESC_LOG = "descLog";
    private static final String KEY_DATE_LOG = "dateLog";
    private static final String KEY_TIME_LOG = "timeLog";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    //column table MasterBank
    private static final String KEY_ID_BANK_DB = "idBankDB";
    private static final String KEY_ID_BANK_BE = "idBankBE";
    //    private static final String KEY_NAME_BANK = "nameBank";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_NO_REK = "noRekening";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    //column table MasterMaterial
    private static final String KEY_MATERIAL_ID_DB = "idMaterialDB";
    //    private static final String KEY_MATERIAL_ID = "materialId";
//    private static final String KEY_MATERIAL_NAME = "materialName";
    private static final String KEY_MATERIAL_SALES = "materialSales";
    //    private static final String KEY_MATERIAL_GROUP_ID = "materialGroupId";
//    private static final String KEY_MATERIAL_GROUP_NAME = "materialGroupName";
    private static final String KEY_MATERIAL_PRODUCT_ID = "materialProductId";
    private static final String KEY_MATERIAL_PRODUCT_NAME = "materialProductName";
//    private static final String KEY_CREATED_BY = "createdBy";
//    private static final String KEY_CREATED_DATE = "createdDate";
//    private static final String KEY_UPDATED_BY = "updatedBy";
//    private static final String KEY_UPDATED_DATE = "updatedDate";
//    private static final String KEY_IS_SYNC = "isSync";

    //column table MasterUom
    private static final String KEY_UOM_ID_DB = "idUomDB";
    private static final String KEY_UOM_ID = "uomId";
    //    private static final String KEY_MATERIAL_ID = "materialId";
    private static final String KEY_CONVERSION = "conversion";

    //column table MasterDaerahTingkat
    private static final String KEY_DAERAH_TINGKAT_ID_DB = "idDaerahTingkatDB";
    //    private static final String KEY_KODE_POS = "kodePos";
//    private static final String KEY_ID_DESA_KELURAHAN = "idDesaKelurahan";
//    private static final String KEY_NAME_DESA_KELURAHAN = "nameDesaKelurahan";
//    private static final String KEY_ID_KECAMATAN = "idKecamatan";
//    private static final String KEY_NAME_KECAMATAN = "nameKecamatan";
//    private static final String KEY_ID_KOTA_KABUPATEN = "idKotaKabupaten";
//    private static final String KEY_NAME_KOTA_KABUPATEN = "nameKotaKabupaten";
//    private static final String KEY_ID_PROVINSI = "idProvinsi";
//    private static final String KEY_NAME_PROVINSI = "nameProvinsi";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static String CREATE_TABLE_STOCK_REQUEST_HEADER = "CREATE TABLE " + TABLE_STOCK_REQUEST_HEADER + "("
            + KEY_ID_STOCK_REQUEST_HEADER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_STOCK_REQUEST_HEADER_BE + " INTEGER,"
            + KEY_REQUEST_DATE + " TEXT,"
            + KEY_ID_SALESMAN + " TEXT,"
            + KEY_NO_DOC + " TEXT,"
            + KEY_TANGGAL_KIRIM + " TEXT,"
            + KEY_NO_SURAT_JALAN + " TEXT,"
            + KEY_SIGN + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_IS_UNLOADING + " INTEGER,"
            + KEY_IS_VERIF + " INTEGER,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
//            + " UNIQUE (" + KEY_REQUEST_DATE + ", " + KEY_ID_SALESMAN + "," + KEY_DO_NUMBER + "," + KEY_SHIPMENT_NUMBER + "," + KEY_SHIPMENT_CATEGORY + ")"
            + ")";

    public static String CREATE_TABLE_STOCK_REQUEST_DETAIL = "CREATE TABLE " + TABLE_STOCK_REQUEST_DETAIL + "("
            + KEY_ID_STOCK_REQUEST_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_STOCK_REQUEST_HEADER_DB + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " INTEGER,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " INTEGER,"
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
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_ID_STOCK_REQUEST_DETAIL_DB + ", " + KEY_MATERIAL_ID + ")"
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
            + KEY_IS_ROUTE + " INTEGER,"
            + KEY_PHOTO_KTP + " TEXT,"
            + KEY_PHOTO_NPWP + " TEXT,"
            + KEY_PHOTO_OUTLET + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION = "CREATE TABLE " + TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION + "("
            + KEY_ID_MASTER_NON_ROUTE_CUSTOMER_PROMOTION_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_MASTER_NON_ROUTE_CUSTOMER_HEADER_DB + " INTEGER ,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_ID_PROMOTION + " INTEGER,"
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
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_ID_PROMOTION + ")"
            + ")";

    public static String CREATE_TABLE_INVOICE_HEADER = "CREATE TABLE " + TABLE_INVOICE_HEADER + "("
            + KEY_ID_INVOICE_HEADER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_INVOICE_NO + " TEXT,"
            + KEY_INVOICE_DATE + " TEXT,"
            + KEY_INVOICE_TOTAL + " REAL,"
            + KEY_DUE_DATE + " TEXT,"
            + KEY_PAID + " REAL,"
            + KEY_NETT + " REAL,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_SIGN + " TEXT,"
            + KEY_IS_VERIF + " INTEGER,"
            + KEY_IS_ROUTE + " INTEGER,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_INVOICE_NO + ")"
            + ")";

    public static String CREATE_TABLE_INVOICE_DETAIL = "CREATE TABLE " + TABLE_INVOICE_DETAIL + "("
            + KEY_ID_INVOICE_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_INVOICE_HEADER_DB + " TEXT,"
            + KEY_INVOICE_NO + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " INTEGER,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " INTEGER,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_PAID + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_ID_INVOICE_HEADER_DB + ", " + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_NOO = "CREATE TABLE " + TABLE_NOO + "("
            + KEY_ID_NOO_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME_NOO + " TEXT,"
            + KEY_ADDRESS_NOO + " TEXT,"
            + KEY_STATUS + " INTEGER,"
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
            + KEY_PHOTO_KTP + " TEXT,"
            + KEY_PHOTO_NPWP + " TEXT,"
            + KEY_PHOTO_OUTLET + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
//            + " UNIQUE (" + KEY_ID_NOO_DB + ")"
            + ")";

    public static String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + "("
            + KEY_ID_CUSTOMER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_CUSTOMER_ADDRESS + " TEXT,"
            + KEY_STATUS + " INTEGER,"
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
            + KEY_IS_ROUTE + " INTEGER,"
            + KEY_PHOTO_KTP + " TEXT,"
            + KEY_PHOTO_NPWP + " TEXT,"
            + KEY_PHOTO_OUTLET + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ")"
            + ")";

    public static String CREATE_TABLE_CUSTOMER_PROMOTION = "CREATE TABLE " + TABLE_CUSTOMER_PROMOTION + "("
            + KEY_ID_CUSTOMER_PROMOTION_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_ID_PROMOTION + " INTEGER,"
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
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_ID_PROMOTION + ")"
            + ")";

    public static String CREATE_TABLE_VISIT_SALESMAN = "CREATE TABLE " + TABLE_VISIT_SALESMAN + "("
            + KEY_ID_VISIT_SALESMAN_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_ID_SALESMAN + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_CHECK_IN_TIME + " TEXT,"
            + KEY_CHECK_OUT_TIME + " TEXT,"
            + KEY_STATUS + " INTEGER,"
            + KEY_RESUME_TIME + " TEXT,"
            + KEY_PAUSE_TIME + " TEXT,"
            + KEY_LAT_CHECK_IN + " REAL,"
            + KEY_LONG_CHECK_IN + " REAL,"
            + KEY_LAT_CHECK_OUT + " REAL,"
            + KEY_LONG_CHECK_OUT + " REAL,"
            + KEY_INSIDE + " INTEGER,"
            + KEY_INSIDE_CHECK_OUT + " INTEGER,"
            + KEY_ID_PAUSE_REASON + " TEXT,"
            + KEY_NAME_PAUSE_REASON + " TEXT,"
            + KEY_DESC_PAUSE_REASON + " TEXT,"
            + KEY_PHOTO_PAUSE_REASON + " TEXT,"
            + KEY_ID_CHECK_OUT_REASON + " TEXT,"
            + KEY_NAME_CHECK_OUT_REASON + " TEXT,"
            + KEY_DESC_CHECK_OUT_REASON + " TEXT,"
            + KEY_PHOTO_CHECK_OUT_REASON + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_ID_SALESMAN + "," + KEY_DATE + ")"
            + ")";

    public static String CREATE_TABLE_VISIT_SALESMAN_NOO = "CREATE TABLE " + TABLE_VISIT_SALESMAN_NOO + "("
            + KEY_ID_VISIT_SALESMAN_NOO_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_NOO_DB + " TEXT,"
            + KEY_ID_SALESMAN + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_CHECK_IN_TIME + " TEXT,"
            + KEY_CHECK_OUT_TIME + " TEXT,"
            + KEY_STATUS + " INTEGER,"
            + KEY_RESUME_TIME + " TEXT,"
            + KEY_PAUSE_TIME + " TEXT,"
            + KEY_LAT_CHECK_IN + " REAL,"
            + KEY_LONG_CHECK_IN + " REAL,"
            + KEY_LAT_CHECK_OUT + " REAL,"
            + KEY_LONG_CHECK_OUT + " REAL,"
            + KEY_INSIDE + " INTEGER,"
            + KEY_INSIDE_CHECK_OUT + " INTEGER,"
            + KEY_ID_PAUSE_REASON + " TEXT,"
            + KEY_NAME_PAUSE_REASON + " TEXT,"
            + KEY_DESC_PAUSE_REASON + " TEXT,"
            + KEY_PHOTO_PAUSE_REASON + " TEXT,"
            + KEY_ID_CHECK_OUT_REASON + " TEXT,"
            + KEY_NAME_CHECK_OUT_REASON + " TEXT,"
            + KEY_DESC_CHECK_OUT_REASON + " TEXT,"
            + KEY_PHOTO_CHECK_OUT_REASON + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_ID_NOO_DB + ", " + KEY_ID_SALESMAN + "," + KEY_DATE + ")"
            + ")";

    public static String CREATE_TABLE_STORE_CHECK = "CREATE TABLE " + TABLE_STORE_CHECK + "("
            + KEY_ID_STORE_CHECK_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " INTEGER,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " INTEGER,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_DATE + "," + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_HEADER = "CREATE TABLE " + TABLE_ORDER_HEADER + "("
            + KEY_ID_ORDER_HEADER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_BACK_END + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_OMZET + " REAL,"
            + KEY_STATUS + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
//            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_IMEI + "," + KEY_DO_NUMBER + "," + KEY_SHIPMENT_NUMBER + "," + KEY_SHIPMENT_CATEGORY + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_DETAIL = "CREATE TABLE " + TABLE_ORDER_DETAIL + "("
            + KEY_ID_ORDER_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_HEADER_DB + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " INTEGER,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " INTEGER,"
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
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_MATERIAL_ID + "," + KEY_ID_ORDER_HEADER_DB + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_DETAIL_EXTRA = "CREATE TABLE " + TABLE_ORDER_DETAIL_EXTRA + "("
            + KEY_ID_ORDER_DETAIL_EXTRA_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_DETAIL_DB + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " INTEGER,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " INTEGER,"
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
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_ID_ORDER_DETAIL_DB + ", " + KEY_CUSTOMER_ID + "," + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_DETAIL_DISCOUNT = "CREATE TABLE " + TABLE_ORDER_DETAIL_DISCOUNT + "("
            + KEY_ID_ORDER_DETAIL_DISCOUNT_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_DETAIL_DB + " TEXT,"
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
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_ID_ORDER_DETAIL_DB + ", " + KEY_CUSTOMER_ID + "," + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_PAYMENT_HEADER = "CREATE TABLE " + TABLE_ORDER_PAYMENT_HEADER + "("
            + KEY_ID_ORDER_PAYMENT_HEADER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_HEADER_DB + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_OMZET + " REAL,"
            + KEY_STATUS + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
//            + " UNIQUE (" + KEY_ITEM_NR + ", " + KEY_IMEI + "," + KEY_DO_NUMBER + "," + KEY_SHIPMENT_NUMBER + "," + KEY_SHIPMENT_CATEGORY + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_PAYMENT_DETAIL = "CREATE TABLE " + TABLE_ORDER_PAYMENT_DETAIL + "("
            + KEY_ID_ORDER_PAYMENT_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_PAYMENT_HEADER_DB + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_TYPE_PAYMENT + " REAL,"
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
            + KEY_IS_SYNC + " INTEGER"
//            + " UNIQUE (" + KEY_ID_ORDER_PAYMENT_HEADER_DB + ", " + KEY_TYPE_PAYMENT + "," + KEY_DO_NUMBER + "," + KEY_SHIPMENT_NUMBER + "," + KEY_SHIPMENT_CATEGORY + ")"
            + ")";

    public static String CREATE_TABLE_ORDER_PAYMENT_ITEM = "CREATE TABLE " + TABLE_ORDER_PAYMENT_ITEM + "("
            + KEY_ID_ORDER_PAYMENT_ITEM_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_PAYMENT_DETAIL_DB + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_AMOUNT_PAID + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_ID_ORDER_PAYMENT_DETAIL_DB + ", " + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_RETURN = "CREATE TABLE " + TABLE_RETURN + "("
            + KEY_ID_RETURN_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " INTEGER,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " INTEGER,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_EXPIRED_DATE + " TEXT,"
            + KEY_CONDITION + " TEXT,"
            + KEY_ID_REASON_RETURN + " TEXT,"
            + KEY_NAME_REASON_RETURN + " TEXT,"
            + KEY_DESC_REASON_RETURN + " TEXT,"
            + KEY_PHOTO_REASON_RETURN + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_CUSTOMER_ID + ", " + KEY_DATE + "," + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_COLLECTION_HEADER = "CREATE TABLE " + TABLE_COLLECTION_HEADER + "("
            + KEY_ID_COLLECTION_HEADER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
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
            + KEY_IS_SYNC + " INTEGER"
//            + " UNIQUE (" + KEY_ITEM_NR + ", " + KEY_IMEI + "," + KEY_DO_NUMBER + "," + KEY_SHIPMENT_NUMBER + "," + KEY_SHIPMENT_CATEGORY + ")"
            + ")";

    public static String CREATE_TABLE_COLLECTION_DETAIL = "CREATE TABLE " + TABLE_COLLECTION_DETAIL + "("
            + KEY_ID_COLLECTION_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_COLLECTION_HEADER_DB + " TEXT,"
            + KEY_INVOICE_NO + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_TYPE_PAYMENT + " REAL,"
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
            + KEY_IS_SYNC + " INTEGER"
//            + " UNIQUE (" + KEY_ITEM_NR + ", " + KEY_IMEI + "," + KEY_DO_NUMBER + "," + KEY_SHIPMENT_NUMBER + "," + KEY_SHIPMENT_CATEGORY + ")"
            + ")";

    public static String CREATE_TABLE_COLLECTION_ITEM = "CREATE TABLE " + TABLE_COLLECTION_ITEM + "("
            + KEY_ID_COLLECTION_ITEM_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_COLLECTION_DETAIL_DB + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " INTEGER,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " INTEGER,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_AMOUNT_PAID + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_ID_COLLECTION_DETAIL_DB + ", " + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_REASON = "CREATE TABLE " + TABLE_MASTER_REASON + "("
            + KEY_ID_REASON_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_REASON_BE + " INTEGER,"
            + KEY_NAME_REASON + " TEXT,"
            + KEY_CATEGORY_REASON + " TEXT,"
            + KEY_IS_PHOTO + " INTEGER,"
            + KEY_IS_FREE_TEXT + " INTEGER,"
            + KEY_IS_BARCODE + " INTEGER,"
            + KEY_IS_SIGNATURE + " INTEGER,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_ID_REASON_BE + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_PROMOTION = "CREATE TABLE " + TABLE_MASTER_PROMOTION + "("
            + KEY_ID_PROMOTION_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_PROMOTION + " INTEGER,"
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
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_ID_PROMOTION + ")"
            + ")";

    public static String CREATE_TABLE_LOG = "CREATE TABLE " + TABLE_LOG + "("
            + KEY_ID_LOG_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DESC_LOG + " TEXT,"
            + KEY_DATE_LOG + " TEXT,"
            + KEY_TIME_LOG + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
//            + " UNIQUE (" + KEY_ITEM_NR + ", " + KEY_IMEI + "," + KEY_DO_NUMBER + "," + KEY_SHIPMENT_NUMBER + "," + KEY_SHIPMENT_CATEGORY + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_BANK = "CREATE TABLE " + TABLE_MASTER_BANK + "("
            + KEY_ID_BANK_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_BANK_BE + " TEXT,"
            + KEY_NAME_BANK + " TEXT,"
            + KEY_ID_DEPO + " INTEGER,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_NO_REK + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_ID_BANK_BE + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_MATERIAL = "CREATE TABLE " + TABLE_MASTER_MATERIAL + "("
            + KEY_MATERIAL_ID_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_MATERIAL_SALES + " TEXT,"
            + KEY_MATERIAL_GROUP_ID + " INTEGER,"
            + KEY_MATERIAL_GROUP_NAME + " TEXT,"
            + KEY_MATERIAL_PRODUCT_ID + " INTEGER,"
            + KEY_MATERIAL_PRODUCT_NAME + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_UOM = "CREATE TABLE " + TABLE_MASTER_UOM + "("
            + KEY_UOM_ID_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_UOM_ID + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_CONVERSION + " INTEGER,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_UOM_ID + ", " + KEY_MATERIAL_ID + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_DAERAH_TINGKAT = "CREATE TABLE " + TABLE_MASTER_DAERAH_TINGKAT + "("
            + KEY_DAERAH_TINGKAT_ID_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_KODE_POS + " INTEGER,"
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
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER,"
            + " UNIQUE (" + KEY_KODE_POS + ", " + KEY_ID_DESA_KELURAHAN + "," + KEY_ID_KECAMATAN + "," + KEY_ID_KOTA_KABUPATEN + "," + KEY_ID_PROVINSI + ")"
            + ")";

    public static String CREATE_TABLE_MASTER_PRICE_CODE = "CREATE TABLE " + TABLE_MASTER_PRICE_CODE + "("
            + KEY_ID_PRICE_CODE_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MATERIAL_PRODUCT_ID + " INTEGER,"
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
            + KEY_ID_CUSTOMER_TYPE + " INTEGER PRIMARY KEY,"
            + KEY_ID_TYPE_PRICE + " TEXT,"
            + KEY_NAME_TYPE_PRICE + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + " UNIQUE (" + KEY_ID_TYPE_PRICE + ")"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STOCK_REQUEST_HEADER);
        db.execSQL(CREATE_TABLE_STOCK_REQUEST_DETAIL);
        db.execSQL(CREATE_TABLE_INVOICE_HEADER);
        db.execSQL(CREATE_TABLE_INVOICE_DETAIL);
        db.execSQL(CREATE_TABLE_MASTER_NON_ROUTE_CUSTOMER);
        db.execSQL(CREATE_TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION);
        db.execSQL(CREATE_TABLE_NOO);
        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_CUSTOMER_PROMOTION);
        db.execSQL(CREATE_TABLE_VISIT_SALESMAN);
        db.execSQL(CREATE_TABLE_VISIT_SALESMAN_NOO);
        db.execSQL(CREATE_TABLE_STORE_CHECK);
        db.execSQL(CREATE_TABLE_ORDER_HEADER);
        db.execSQL(CREATE_TABLE_ORDER_DETAIL);
        db.execSQL(CREATE_TABLE_ORDER_DETAIL_DISCOUNT);
        db.execSQL(CREATE_TABLE_ORDER_DETAIL_EXTRA);
        db.execSQL(CREATE_TABLE_ORDER_PAYMENT_HEADER);
        db.execSQL(CREATE_TABLE_ORDER_PAYMENT_DETAIL);
        db.execSQL(CREATE_TABLE_ORDER_PAYMENT_ITEM);
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
        db.execSQL(CREATE_TABLE_PARAMETER);
        db.execSQL(CREATE_TABLE_LOG);
        db.execSQL(CREATE_TABLE_CUSTOMER_TYPE);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISIT_SALESMAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISIT_SALESMAN_NOO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_CHECK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAIL_DISCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAIL_EXTRA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_PAYMENT_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_PAYMENT_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_PAYMENT_ITEM);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_CUSTOMER_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_PARAMETER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
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
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_SIGN, param.getSignature());
        values.put(KEY_IS_UNLOADING, param.getIs_unloading());
        values.put(KEY_IS_VERIF, param.getIs_verif());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

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
        values.put(KEY_QTY_SISA, param.getQtySisa());
        values.put(KEY_UOM_SISA, param.getUomSisa());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

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
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

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
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_INVOICE_DETAIL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addNoo(Customer param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
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
        values.put(KEY_TYPE_CUSTOMER, param.getType_customer());
        values.put(KEY_TYPE_PRICE, param.getType_price());
        values.put(KEY_CREDIT_LIMIT, param.getLimit_kredit());
        values.put(KEY_ROUTE, param.getRute());
        values.put(KEY_PHOTO_KTP, param.getPhotoKtp());
        values.put(KEY_PHOTO_NPWP, param.getPhotoNpwp());
        values.put(KEY_PHOTO_OUTLET, param.getPhotoOutlet());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

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
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

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
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

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
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true


        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION, null, values);//return id yg ud d create
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
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_CUSTOMER_PROMOTION, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addVisitSalesman(VisitSalesman param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
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

    public int addVisitSalesmanNoo(VisitSalesman param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_NOO_DB, param.getCustomerId());
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
            id = (int) db.insert(TABLE_VISIT_SALESMAN_NOO, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int insertUpdateReturn(Material material, Map header) {
        SQLiteDatabase db = getWritableDatabase();
        Integer id = null;

        try {
            String queryUpdateInsert = "INSERT OR IGNORE INTO " + TABLE_RETURN + " ("
                    + KEY_CUSTOMER_ID + ", " + KEY_DATE + ", " + KEY_MATERIAL_ID + ", "
                    + KEY_MATERIAL_NAME + ", " + KEY_MATERIAL_GROUP_ID + ", "
                    + KEY_MATERIAL_GROUP_NAME + ", " + KEY_MATERIAL_PRODUCT_ID + ", "
                    + KEY_MATERIAL_PRODUCT_NAME + ", " + KEY_QTY + ", " + KEY_UOM + ", "
                    + KEY_EXPIRED_DATE + ", " + KEY_CONDITION + ", " + KEY_ID_REASON_RETURN + ", "
                    + KEY_NAME_REASON_RETURN + ", " + KEY_DESC_REASON_RETURN + ", " + KEY_PHOTO_REASON_RETURN + ", "
                    + KEY_CREATED_BY + ", " + KEY_CREATED_DATE + ") "

                    + "VALUES (" + header.get("id_customer").toString() + ", " + header.get("date").toString() + ", " + material.getId()
                    + ", " + material.getNama() + ", " + material.getId_material_group()
                    + ", " + material.getMaterial_group_name() + ", " + material.getId_product_group()
                    + ", " + material.getName_product_group() + ", " + material.getQty() + ", " + material.getUom()
                    + ", " + material.getExpiredDate() + ", " + material.getCondition() + ", " + material.getIdReason()
                    + ", " + material.getNameReason() + ", " + material.getDescReason() + ", " + material.getPhotoReason()
                    + ", " + header.get("username").toString() + "," + Helper.getTodayDate(Constants.DATE_FORMAT_2) + ")";

            String update = "UPDATE " + TABLE_RETURN + " SET "
                    + KEY_QTY + " = " + material.getQty() + ","
                    + KEY_UOM + " = " + material.getUom() + ", "
                    + KEY_EXPIRED_DATE + " = " + material.getExpiredDate() + ", "
                    + KEY_CONDITION + " = " + material.getCondition() + ", "
                    + KEY_ID_REASON_RETURN + " = " + material.getIdReason() + ", "
                    + KEY_NAME_REASON_RETURN + " = " + material.getNameReason() + ", "
                    + KEY_DESC_REASON_RETURN + " = " + material.getDescReason() + ", "
                    + KEY_PHOTO_REASON_RETURN + " = " + material.getPhotoReason() + ", "
                    + KEY_UPDATED_BY + " = " + header.get("username").toString() + ","
                    + KEY_UPDATED_DATE + " = " + Helper.getTodayDate(Constants.DATE_FORMAT_2)
                    + " WHERE " + KEY_CUSTOMER_ID + "= " + header.get("id_customer").toString()
                    + " and " + KEY_MATERIAL_ID + " = " + material.getId()
                    + " and " + KEY_DATE + " = " + header.get("date").toString();

            db.execSQL(queryUpdateInsert, null);

            db.execSQL(update, null);

            Cursor cursor = db.query(TABLE_RETURN, new String[]{KEY_ID_RETURN_DB}, KEY_DATE + " = ? and "
                            + KEY_MATERIAL_ID + " = ? and " + KEY_CUSTOMER_ID + " = ? ",
                    new String[]{header.get("date").toString(), String.valueOf(material.getId()),
                            header.get("id_customer").toString()}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            if (cursor.getCount() != 0) {
                id = Integer.parseInt(cursor.getString(0));
            }
            db.close();
        } catch (Exception ex) {
            id = null;
        }
        return id;
    }

    public int addUpdateStoreCheck(Material param, Map header) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, header.get("id_customer").toString());
        values.put(KEY_DATE, header.get("date").toString());
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
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insertWithOnConflict(TABLE_STORE_CHECK, null, values, SQLiteDatabase.CONFLICT_IGNORE);//return id yg ud d create

            if (id == -1) {
                ContentValues valuesUpdate = new ContentValues();
                valuesUpdate.put(KEY_QTY, param.getQty());
                valuesUpdate.put(KEY_UOM, param.getUom());
                valuesUpdate.put(KEY_UPDATED_BY, header.get("username").toString());
                valuesUpdate.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

                db.update(TABLE_STORE_CHECK, values, KEY_CUSTOMER_ID + " = ? and " + KEY_DATE + " = ? and " + KEY_MATERIAL_ID + " = ?",
                        new String[]{header.get("id_customer").toString(), header.get("date").toString(), param.getId()});
            }
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
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_STORE_CHECK, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderHeader(Order param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_BACK_END, param.getIdOrderBE());
        values.put(KEY_CUSTOMER_ID, param.getCustomerId());
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_OMZET, param.getOmzet());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_HEADER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderDetail(Material param, String idHeader, String idCust, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, idHeader);
        values.put(KEY_CUSTOMER_ID, idCust);
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
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_DETAIL, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderDetailExtra(Material param, String idHeader, String idCust, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_DETAIL_DB, idHeader);
        values.put(KEY_CUSTOMER_ID, idCust);
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
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

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
//        values.put(KEY_MATERIAL_ID, param.getMaterialId());
//        values.put(KEY_DISCOUNT_ID, param.getMaterialName());
//        values.put(KEY_DISCOUNT_NAME, param.getQty());
//        values.put(KEY_DISCOUNT_PRICE, param.getUom());
//        values.put(KEY_TOTAL, param.getPrice());
//        values.put(KEY_CREATED_BY, idSales);
//        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
//        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_DETAIL_DISCOUNT, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderPayment(CollectionDetail param, String typePayment, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, idHeader);
        values.put(KEY_DATE, param.getTgl());
        values.put(KEY_OMZET, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, typePayment);
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_PAYMENT_HEADER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderPaymentTransfer(CollectionDetail param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, idHeader);
        values.put(KEY_DATE, param.getTgl());
        values.put(KEY_OMZET, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, "transfer");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_PAYMENT_HEADER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderPaymentGiro(CollectionDetail param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, idHeader);
        values.put(KEY_DATE, param.getTgl());
        values.put(KEY_OMZET, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, "giro");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_NO, param.getNo());
        values.put(KEY_DUE_DATE, param.getTglCair());
        values.put(KEY_ID_BANK, param.getIdBankASPP());
        values.put(KEY_NAME_BANK, param.getBankNameASPP());
        values.put(KEY_ID_CUST_BANK, param.getIdBankCust());
        values.put(KEY_NAME_CUST_BANK, param.getBankCust());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_PAYMENT_HEADER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderPaymentCheque(CollectionDetail param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, idHeader);
        values.put(KEY_DATE, param.getTgl());
        values.put(KEY_OMZET, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, "giro");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_NO, param.getNo());
        values.put(KEY_DUE_DATE, param.getTglCair());
        values.put(KEY_ID_BANK, param.getIdBankASPP());
        values.put(KEY_NAME_BANK, param.getBankNameASPP());
        values.put(KEY_ID_CUST_BANK, param.getIdBankCust());
        values.put(KEY_NAME_CUST_BANK, param.getBankCust());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_PAYMENT_HEADER, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addOrderPaymentDetail(Material param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_PAYMENT_HEADER_DB, idHeader);
        values.put(KEY_MATERIAL_ID, param.getId());
        values.put(KEY_MATERIAL_NAME, param.getNama());
        values.put(KEY_MATERIAL_GROUP_ID, param.getId_material_group());
        values.put(KEY_MATERIAL_GROUP_NAME, param.getMaterial_group_name());
        values.put(KEY_MATERIAL_PRODUCT_ID, param.getId_product_group());
        values.put(KEY_MATERIAL_PRODUCT_NAME, param.getName_product_group());
        values.put(KEY_PRICE, param.getPrice());
        values.put(KEY_AMOUNT_PAID, param.getAmountPaid());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_ORDER_PAYMENT_DETAIL, null, values);//return id yg ud d create
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
        values.put(KEY_PHOTO_REASON_RETURN, param.getPhotoReason());
        values.put(KEY_CREATED_BY, header.get("username").toString());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

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
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        db.endTransaction();
        return id;
    }

    public void addCollection(Map request) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransactionNonExclusive();

        User user = Helper.ObjectToGSON(request.get("user"), User.class);
        Invoice header = Helper.ObjectToGSON(request.get("header"), Invoice.class);
        double totalAmountPaid = Helper.ObjectToGSON(request.get("totalAmountPaid"), double.class);
        double totalPaymentCash = Helper.ObjectToGSON(request.get("totalPaymentCash"), double.class);
        double leftCash = Helper.ObjectToGSON(request.get("leftCash"), double.class);

        List<Material> cashList = new ArrayList<>();
        Material[] cashListArray = Helper.ObjectToGSON(request.get("cashList"), Material[].class);
        Collections.addAll(cashList, cashListArray);

        double totalPaymentLain = Helper.ObjectToGSON(request.get("totalPaymentLain"), double.class);
        double leftLain = Helper.ObjectToGSON(request.get("leftLain"), double.class);

        List<Material> lainList = new ArrayList<>();
        Material[] lainListArray = Helper.ObjectToGSON(request.get("lainList"), Material[].class);
        Collections.addAll(lainList, lainListArray);

        List<Material> tfList = new ArrayList<>();
        Material[] tfListArray = Helper.ObjectToGSON(request.get("tfList"), Material[].class);
        Collections.addAll(tfList, tfListArray);

        List<CollectionDetail> mListTransfer = new ArrayList<>();
        CollectionDetail[] mListTransferArray = Helper.ObjectToGSON(request.get("mListTransfer"), CollectionDetail[].class);
        Collections.addAll(mListTransfer, mListTransferArray);

        List<Material> giroList = new ArrayList<>();
        Material[] giroArray = Helper.ObjectToGSON(request.get("giroList"), Material[].class);
        Collections.addAll(giroList, giroArray);

        List<CollectionDetail> mListGiro = new ArrayList<>();
        CollectionDetail[] mListGiroArray = Helper.ObjectToGSON(request.get("mListGiro"), CollectionDetail[].class);
        Collections.addAll(mListGiro, mListGiroArray);

        List<Material> chequeList = new ArrayList<>();
        Material[] chequeListArray = Helper.ObjectToGSON(request.get("chequeList"), Material[].class);
        Collections.addAll(chequeList, chequeListArray);

        List<CollectionDetail> mListCheque = new ArrayList<>();
        CollectionDetail[] mListChequeArray = Helper.ObjectToGSON(request.get("mListCheque"), CollectionDetail[].class);
        Collections.addAll(mListCheque, mListChequeArray);

        List<Material> mListCash = new ArrayList<>();
        Material[] mListCashArray = Helper.ObjectToGSON(request.get("mListCash"), Material[].class);
        Collections.addAll(mListCash, mListCashArray);

        Map requestHeader = new HashMap();
        requestHeader.put("customer_id", header.getId_customer());
        requestHeader.put("no_invoice", header.getNo_invoice());
        requestHeader.put("invoice_date", header.getInvoice_date());
        requestHeader.put("status", "paid");
        requestHeader.put("total_paid", totalAmountPaid);
        requestHeader.put("amount", header.getAmount());
        requestHeader.put("username", user.getUsername());

        //addCollectionHeader
        //int idCollHeader = database.addCollectionHeader(requestHeader);
        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, requestHeader.get("customer_id").toString());
        values.put(KEY_INVOICE_NO, requestHeader.get("no_invoice").toString());
        values.put(KEY_INVOICE_DATE, requestHeader.get("invoice_date").toString());
        values.put(KEY_STATUS, requestHeader.get("status").toString());
        values.put(KEY_INVOICE_TOTAL, (Double) requestHeader.get("amount"));
        values.put(KEY_TOTAL_PAID, (Double) requestHeader.get("total_paid"));
        values.put(KEY_CREATED_BY, requestHeader.get("username").toString());
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, 0);

        int idCollHeader = -1;
        try {
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
            requestDetail.put("id_header", idCollHeader);
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
                values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                int idDetail = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                for (Material material : cashList) {
//                database.addCollectionMaterial(material, String.valueOf(idDetail), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetail));
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
                    values.put(KEY_IS_SYNC, 0); //0 false, 1 true

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
                values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                int idDetailLain = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                for (Material material : lainList) {
//                database.addCollectionMaterial(material, String.valueOf(idDetailLain), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailLain));
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
                    values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                    int idItemLain = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                }
            }

            if (tfList.size() != 0) {
                for (CollectionDetail collection : mListTransfer) {
                    collection.setInvoiceNo(header.getNo_invoice());
//                int idDetail = database.addCollectionTransfer(collection, String.valueOf(idCollHeader), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_HEADER_DB, String.valueOf(idCollHeader));
                    values.put(KEY_INVOICE_NO, collection.getInvoiceNo());
                    values.put(KEY_TYPE_PAYMENT, "transfer");
                    values.put(KEY_TOTAL_PAYMENT, collection.getTotalPayment());
                    values.put(KEY_LEFT, collection.getLeft());
                    values.put(KEY_DATE, collection.getTgl());
                    values.put(KEY_CREATED_BY, user.getUsername());
                    values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                    values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                    int idDetailTf = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                    for (Material material : collection.getCheckedMaterialList()) {
//                    database.addCollectionMaterial(material, String.valueOf(idDetailTf), user.getUsername());
                        values = new ContentValues();
                        values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailTf));
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
                        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                        int idItemTf = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                    }
                }
            }

            if (giroList.size() != 0) {
                for (CollectionDetail collection : mListGiro) {
                    collection.setInvoiceNo(header.getNo_invoice());
//                int idDetail = database.addCollectionGiro(collection, String.valueOf(idCollHeader), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_HEADER_DB, String.valueOf(idCollHeader));
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
                    values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                    int idDetailGiro = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                    for (Material material : collection.getCheckedMaterialList()) {
//                    database.addCollectionMaterial(material, String.valueOf(idDetailGiro), user.getUsername());
                        values = new ContentValues();
                        values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailGiro));
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
                        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                        int idItemGiro = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                    }
                }
            }

            if (chequeList.size() != 0) {
                for (CollectionDetail collection : mListCheque) {
                    collection.setInvoiceNo(header.getNo_invoice());
//                int idDetail = database.addCollectionCheque(collection, String.valueOf(idCollHeader), user.getUsername());
                    values = new ContentValues();
                    values.put(KEY_ID_COLLECTION_HEADER_DB, String.valueOf(idCollHeader));
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
                    values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                    int idDetailCheque = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create

                    for (Material material : collection.getCheckedMaterialList()) {
//                    database.addCollectionMaterial(material, String.valueOf(idDetailCheque), user.getUsername());
                        values = new ContentValues();
                        values.put(KEY_ID_COLLECTION_DETAIL_DB, String.valueOf(idDetailCheque));
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
                        values.put(KEY_IS_SYNC, 0); //0 false, 1 true

                        int idItemCheque = (int) db.insert(TABLE_COLLECTION_ITEM, null, values);//return id yg ud d create
                    }
                }
            }

            if (Helper.isNotEmptyOrNull(mListCash)) {
                for (Material material : mListCash) {
//                database.updateNettPrice(material, user.getUsername(), header.getNo_invoice());//update paid invoice detail
                        values = new ContentValues();
                        double paid = getPaidInvoiceMaterial(header.getNo_invoice(), material.getId());
                        paid = paid + (material.getNett() - material.getSisa());//material.getNett() - material.getSisa(),ambil semua jumlah paid nya
                        values.put(KEY_PAID, paid);
                        values.put(KEY_UPDATED_BY, user.getUsername());
                        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

                        db.update(TABLE_INVOICE_DETAIL, values, KEY_INVOICE_NO + " = ? and "
                                + KEY_MATERIAL_ID + " = ?", new String[]{header.getNo_invoice(), material.getId()});
                        //db.close();
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Collection", e.getMessage());
        }
        db.endTransaction();
        //addCollectionHeader
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
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

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
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

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
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

        int id = -1;
        try {
            id = (int) db.insert(TABLE_MASTER_PROMOTION, null, values);//return id yg ud d create
        } catch (Exception e) {
            id = -1;
        }
        //db.close();
        return id;
    }

    public int addLog(LogModel param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DESC_LOG, param.getDescLog());
        values.put(KEY_DATE_LOG, param.getDateLog());
        values.put(KEY_TIME_LOG, param.getTimeLog());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

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
        values.put(KEY_IS_SYNC, param.getIs_sync()); //0 false, 1 true

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
        values.put(KEY_UOM_ID, param.getId_uom());
        values.put(KEY_MATERIAL_ID, param.getId_material());
        values.put(KEY_CONVERSION, param.getConversion());
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

    public List<Material> getAllReturn(String idCust) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RETURN + " WHERE  " + KEY_CUSTOMER_ID + " = ?  order by " + KEY_ID_RETURN_DB + " asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idCust});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_RETURN_DB)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setId_material_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                paramModel.setQty(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_QTY)));
                paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                paramModel.setExpiredDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EXPIRED_DATE)));
                paramModel.setCondition(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONDITION)));
                paramModel.setIdReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_REASON_RETURN)));
                paramModel.setNameReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_REASON_RETURN)));
                paramModel.setDescReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_REASON_RETURN)));
                paramModel.setPhotoReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_REASON_RETURN)));
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
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STORE_CHECK_DB)));
                paramModel.setId_customer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setId_material_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
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
                paramModel.setIs_sync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                paramModel.setIs_verif(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)));
//                paramModel.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));

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
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_DETAIL_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setQty(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_QTY)));
                paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                paramModel.setIs_sync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
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
                result.setIs_sync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                result.setIs_verif(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)));
//                result.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));
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
                paramModel.setIs_sync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
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
                paramModel.setIs_sync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
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
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_INVOICE_DETAIL_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
//                paramModel.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                paramModel.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)) - cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID)));
                paramModel.setId_material_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                paramModel.setSisa(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)) - cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID)));
                paramModel.setNett(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                paramModel.setIs_sync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
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
                paramModel.setIs_sync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Customer> getAllCustomerVisit(Location currentLocation, boolean coverage) {
        setFormatSeparator();
        SQLiteDatabase db = this.getWritableDatabase();
        List<Customer> arrayList = new ArrayList<>();
        String selectQuery = null;
        Cursor cursor = null;
        // Select All Query
        if (coverage) {
            selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + KEY_ROUTE + " LIKE ?";
            cursor = db.rawQuery(selectQuery, new String[]{"%" + Helper.getTodayRoute() + "%"});
        } else {
            selectQuery = "SELECT * FROM " + TABLE_CUSTOMER;
            cursor = db.rawQuery(selectQuery, null);
        }

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
                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                paramModel.setIs_sync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
                paramModel.setRoute(Helper.checkTodayRoute(paramModel.getRute()));

                if (currentLocation != null) {
                    double distance = Helper.distance(paramModel.getLatitude(), paramModel.getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude(), "K");
                    paramModel.setMileage(format.format(distance));
                }

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
                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                paramModel.setIs_sync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)));
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
                paramModel.setId_depo(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_DEPO)));
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

    public List<Material> getOutstandingProductFaktur(String param) {
        List<Material> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "select id." + KEY_MATERIAL_PRODUCT_ID + ", id." + KEY_MATERIAL_PRODUCT_NAME + " from " + TABLE_INVOICE_DETAIL + " id " +
                "join " + TABLE_INVOICE_HEADER + " ih on id." + KEY_ID_INVOICE_HEADER_DB + " = ih." + KEY_ID_INVOICE_HEADER_DB + " " +
                "where ih." + KEY_CUSTOMER_ID + " = ? and (ih." + KEY_INVOICE_TOTAL + " - ih." + KEY_PAID + " > 0)";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{param});

        if (cursor.moveToFirst()) {
            do {
                Material paramModel = new Material();
                paramModel.setId_product_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));

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

    public double getPaidInvoiceMaterial(String invoiceNo, String idMaterial) {
        double result = 0.0;
        // Select All Query
        String selectQuery = "SELECT " + KEY_PAID + "  FROM " + TABLE_INVOICE_DETAIL + " WHERE " + KEY_INVOICE_NO + " = ? AND " + KEY_MATERIAL_ID + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{invoiceNo, idMaterial});

        if (cursor.moveToFirst()) {
            do {
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID));
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

    public List<Customer> getAllNonRouteCustomer() {
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
                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
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
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_COLLECTION_DETAIL_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setId_material_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));
                paramModel.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                paramModel.setAmountPaid(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_AMOUNT_PAID)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public int getCountCheckInVisitNonRoute() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery;
        Cursor cursor;
        countQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE (" + KEY_STATUS + " = " + Constants.CHECK_IN_VISIT
                + " or " + KEY_STATUS + " = " + Constants.PAUSE_VISIT
                + " or " + KEY_STATUS + " = " + Constants.CHECK_OUT_VISIT
                + ") and " + KEY_IS_ROUTE + " = 1 ";
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
        String selectQuery = "SELECT * FROM " + TABLE_LOG;

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
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setNama(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setMaterial_sales(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_SALES)));
                paramModel.setId_material_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_ID)));
                paramModel.setMaterial_group_name(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_GROUP_NAME)));
                paramModel.setId_product_group(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_ID)));
                paramModel.setName_product_group(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_PRODUCT_NAME)));

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<String> getUom(String idMat) {
        List<String> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_UOM_ID + " FROM " + TABLE_MASTER_UOM + " WHERE " + KEY_MATERIAL_ID + " = ? order by " + KEY_CONVERSION + " asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMat});

        if (cursor.moveToFirst()) {
            do {
                String paramModel = cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM_ID));
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

//    public List<Uom> getUom(String idMat) {
//        List<Uom> arrayList = new ArrayList<>();
//        // Select All Query
//        String selectQuery = "SELECT "+KEY_UOM_ID+" FROM " + TABLE_MASTER_UOM + " WHERE " + KEY_MATERIAL_ID + " = ?";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMat});
//
//        if (cursor.moveToFirst()) {
//            do {
//                Uom paramModel = new Uom();
//                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM_ID_DB)));
//                paramModel.setId_uom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM_ID)));
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

    public float getRadius() {
        float radius = 0;
        // Select All Query
        String selectQuery = "SELECT " + KEY_VALUE + " FROM " + TABLE_MASTER_PARAMETER + " WHERE " + KEY_KEY_PARAMETER + " = \'RADIUS\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                radius = cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_VALUE));
            } while (cursor.moveToNext());
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

    public VisitSalesman getVisitSalesmanNoo(Customer request) {
        VisitSalesman result = new VisitSalesman();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_VISIT_SALESMAN_NOO + " WHERE " + KEY_ID_NOO_DB + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{request.getId()});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = new VisitSalesman();
                result.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_VISIT_SALESMAN_DB)));
                result.setCustomerId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
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
                result.setDescCheckOutReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_PAUSE_REASON)));
                result.setPhotoPauseReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_PAUSE_REASON)));
                result.setIdCheckOutReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CHECK_OUT_REASON)));
                result.setNameCheckOutReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_CHECK_OUT_REASON)));
                result.setDescCheckOutReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_CHECK_OUT_REASON)));
                result.setPhotoCheckOutReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_CHECK_OUT_REASON)));
            }
        }
        cursor.close();
        return result;
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
                result.setDescCheckOutReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_PAUSE_REASON)));
                result.setPhotoPauseReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_PAUSE_REASON)));
                result.setIdCheckOutReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CHECK_OUT_REASON)));
                result.setNameCheckOutReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_CHECK_OUT_REASON)));
                result.setDescCheckOutReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESC_CHECK_OUT_REASON)));
                result.setPhotoCheckOutReason(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_CHECK_OUT_REASON)));
            }
        }
        cursor.close();
        return result;
    }

    //update
    public void updateVisit(VisitSalesman param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, param.getStatus());
        switch (param.getStatus()) {
            case Constants.PAUSE_VISIT:
                values.put(KEY_PAUSE_TIME, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                values.put(KEY_ID_PAUSE_REASON, param.getIdPauseReason());
                values.put(KEY_NAME_PAUSE_REASON, param.getNamePauseReason());
                values.put(KEY_DESC_PAUSE_REASON, param.getDescPauseReason());
                values.put(KEY_PHOTO_PAUSE_REASON, param.getPhotoPauseReason());
                break;
            case Constants.CHECK_IN_VISIT:
                values.put(KEY_RESUME_TIME, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                break;
            case Constants.CHECK_OUT_VISIT:
                values.put(KEY_CHECK_OUT_TIME, Helper.getTodayDate(Constants.DATE_FORMAT_2));
                values.put(KEY_LAT_CHECK_OUT, param.getLatCheckOut());
                values.put(KEY_LONG_CHECK_OUT, param.getLongCheckOut());
                values.put(KEY_INSIDE_CHECK_OUT, param.isInsideCheckOut());
                values.put(KEY_ID_CHECK_OUT_REASON, param.getIdCheckOutReason());
                values.put(KEY_NAME_CHECK_OUT_REASON, param.getNameCheckOutReason());
                values.put(KEY_DESC_CHECK_OUT_REASON, param.getDescCheckOutReason());
                values.put(KEY_PHOTO_CHECK_OUT_REASON, param.getPhotoCheckOutReason());
                break;
        }
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_VISIT_SALESMAN, values, KEY_CUSTOMER_ID + " = ? ", new String[]{param.getCustomerId()});
        //db.close();
    }

    public void updateVisitNoo(VisitSalesman param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, param.getStatus());
        switch (param.getStatus()) {
            case Constants.PAUSE_VISIT:
                values.put(KEY_PAUSE_TIME, param.getPauseTime());
                values.put(KEY_ID_PAUSE_REASON, param.getIdPauseReason());
                values.put(KEY_NAME_PAUSE_REASON, param.getNamePauseReason());
                values.put(KEY_NAME_PAUSE_REASON, param.getNamePauseReason());
                values.put(KEY_DESC_PAUSE_REASON, param.getDescPauseReason());
                values.put(KEY_PHOTO_PAUSE_REASON, param.getPhotoPauseReason());
                break;
            case Constants.CHECK_IN_VISIT:
                values.put(KEY_RESUME_TIME, param.getResumeTime());
                break;
            case Constants.CHECK_OUT_VISIT:
                values.put(KEY_CHECK_OUT_TIME, param.getResumeTime());
                values.put(KEY_LAT_CHECK_OUT, param.getResumeTime());
                values.put(KEY_LONG_CHECK_OUT, param.getResumeTime());
                values.put(KEY_INSIDE_CHECK_OUT, param.getResumeTime());
                values.put(KEY_ID_CHECK_OUT_REASON, param.getResumeTime());
                values.put(KEY_NAME_CHECK_OUT_REASON, param.getResumeTime());
                values.put(KEY_DESC_CHECK_OUT_REASON, param.getResumeTime());
                values.put(KEY_PHOTO_CHECK_OUT_REASON, param.getResumeTime());
                break;
        }

        db.update(TABLE_VISIT_SALESMAN_NOO, values, KEY_ID_NOO_DB + " = ? ", new String[]{param.getCustomerId()});
        //db.close();
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

        db.update(TABLE_NOO, values, KEY_ID_NOO_DB + " = ?", new String[]{param.getIdHeader()});
        //db.close();
    }

    public void pauseVisitSalesman(VisitSalesman param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_STATUS, param.getIdPauseReason());
        values.put(KEY_STATUS, param.getNamePauseReason());
        values.put(KEY_STATUS, param.getPauseTime());
        values.put(KEY_STATUS, param.getResumeTime());
        values.put(KEY_STATUS, param.getTimer());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_VISIT_SALESMAN, values, KEY_ID_NOO_DB + " = ?", new String[]{param.getIdHeader()});
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

    public void updatePhoto(Customer param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PHOTO_KTP, param.getPhotoKtp());
        values.put(KEY_PHOTO_NPWP, param.getPhotoNpwp());
        values.put(KEY_PHOTO_OUTLET, param.getPhotoOutlet());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_CUSTOMER, values, KEY_CUSTOMER_ID + " = ?", new String[]{param.getId()});
        //db.close();
    }

    public void updatePhotoNoo(Customer param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PHOTO_KTP, param.getPhotoKtp());
        values.put(KEY_PHOTO_NPWP, param.getPhotoNpwp());
        values.put(KEY_PHOTO_OUTLET, param.getPhotoOutlet());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_NOO, values, KEY_ID_NOO_DB + " = ?", new String[]{param.getIdHeader()});
        //db.close();
    }

    public void updateStockRequestVerification(StockRequest param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_VERIF, param.getIs_verif());
        values.put(KEY_SIGN, param.getSignature());
        values.put(KEY_IS_SYNC, param.getIs_sync());
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
        values.put(KEY_IS_SYNC, param.getIs_sync());
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
        values.put(KEY_IS_SYNC, param.getIs_sync());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_2));

        db.update(TABLE_INVOICE_HEADER, values, KEY_ID_INVOICE_HEADER_DB + " = ?",
                new String[]{param.getIdHeader()});
        //db.close();
    }

    //delete
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

    public void deleteVisitSalesman() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_VISIT_SALESMAN);
    }

    public void deleteVisitSalesmanNoo() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_VISIT_SALESMAN_NOO);
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

    public void deleteOrderPaymentHeader() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_ORDER_PAYMENT_HEADER);
    }

    public void deleteOrderPaymentDetail() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_ORDER_PAYMENT_DETAIL);
    }

    public void deleteOrderPaymentItem() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_ORDER_PAYMENT_ITEM);
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

    public void deleteMasterNonRouteCustomer() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_NON_ROUTE_CUSTOMER);
    }

    public void deleteMasterNonRouteCustomerPromotion() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_NON_ROUTE_CUSTOMER_PROMOTION);
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

    public void deleteMasterParameter() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_PARAMETER);
    }

    public void deleteMasterCustomerType() {
        this.getWritableDatabase().execSQL("delete from " + TABLE_MASTER_CUSTOMER_TYPE);
    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}

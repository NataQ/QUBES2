package id.co.qualitas.qubes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Bank;
import id.co.qualitas.qubes.model.CollectionCheque;
import id.co.qualitas.qubes.model.CollectionGiro;
import id.co.qualitas.qubes.model.CollectionTransfer;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.CustomerNoo;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Log;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.StoreCheck;
import id.co.qualitas.qubes.model.VisitSalesman;

public class Database extends SQLiteOpenHelper {

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
    private static final String TABLE_LOG = "Log";

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

    // column table StockRequestDetail
    private static final String KEY_ID_STOCK_REQUEST_DETAIL_DB = "idStockRequestDetailDB";
    //    private static final String KEY_ID_STOCK_REQUEST_HEADER_DB = "idStockRequestHeaderDB";
    private static final String KEY_MATERIAL_ID = "materialId";
    private static final String KEY_MATERIAL_NAME = "materialName";
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
    private static final String KEY_NETT = "nett";
    private static final String KEY_CUSTOMER_ID = "customerId";
    private static final String KEY_CUSTOMER_NAME = "customerName";
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
    private static final String KEY_ID_KOTA = "idKota";
    private static final String KEY_NAME_KOTA = "nameKota";
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
    private static final String KEY_TYPE_TOKO_OUTLET = "typeTokoOutlet";
    private static final String KEY_PRICE_LIST_TYPE = "priceListType";
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
    private static final String KEY_TRANSFER_DATE = "transferDate";
    private static final String KEY_GIRO_NO = "giroNo";
    private static final String KEY_GIRO_DATE = "giroDate";
    //    private static final String KEY_DUE_DATE = "dueDate";
    private static final String KEY_ID_BANK = "idBank";
    private static final String KEY_NAME_BANK = "nameBank";
    private static final String KEY_ID_CUST_BANK = "idCustBank";
    private static final String KEY_NAME_CUST_BANK = "nameCustBank";
    private static final String KEY_CHEQUE_NO = "chequeNo";
    private static final String KEY_CHEQUE_DATE = "chequeDate";
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
    //    private static final String KEY_TRANSFER_DATE = "transferDate";
    //    private static final String KEY_GIRO_NO = "giroNo";
    //    private static final String KEY_GIRO_DATE = "giroDate";
    //    private static final String KEY_DUE_DATE = "dueDate";
    //    private static final String KEY_ID_BANK = "idBank";
    //    private static final String KEY_NAME_BANK = "nameBank";
    //    private static final String KEY_ID_CUST_BANK = "idCustBank";
    //    private static final String KEY_NAME_CUST_BANK = "nameCustBank";
    //    private static final String KEY_CHEQUE_NO = "chequeNo";
    //    private static final String KEY_CHEQUE_DATE = "chequeDate";
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

    // column table Log
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
            + KEY_IS_UNLOADING + " INTEGER,"
            + KEY_IS_VERIF + " INTEGER,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_STOCK_REQUEST_DETAIL = "CREATE TABLE " + TABLE_STOCK_REQUEST_DETAIL + "("
            + KEY_ID_STOCK_REQUEST_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_STOCK_REQUEST_HEADER_DB + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_QTY_SISA + " REAL,"
            + KEY_UOM_SISA + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
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
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_INVOICE_DETAIL = "CREATE TABLE " + TABLE_INVOICE_DETAIL + "("
            + KEY_ID_INVOICE_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_INVOICE_HEADER_DB + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_NOO = "CREATE TABLE " + TABLE_NOO + "("
            + KEY_ID_NOO_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME_NOO + " TEXT,"
            + KEY_ADDRESS_NOO + " TEXT,"
            + KEY_LATITUDE + " REAL,"
            + KEY_LONGITUDE + " REAL,"
            + KEY_KODE_POS + " TEXT,"
            + KEY_ID_DESA_KELURAHAN + " TEXT,"
            + KEY_NAME_DESA_KELURAHAN + " TEXT,"
            + KEY_ID_KECAMATAN + " TEXT,"
            + KEY_NAME_KECAMATAN + " TEXT,"
            + KEY_ID_KOTA + " TEXT,"
            + KEY_NAME_KOTA + " TEXT,"
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
            + KEY_TYPE_TOKO_OUTLET + " TEXT,"
            + KEY_PRICE_LIST_TYPE + " TEXT,"
            + KEY_CREDIT_LIMIT + " TEXT,"
            + KEY_ROUTE + " TEXT,"
            + KEY_PHOTO_KTP + " TEXT,"
            + KEY_PHOTO_NPWP + " TEXT,"
            + KEY_PHOTO_OUTLET + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + "("
            + KEY_ID_CUSTOMER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_CUSTOMER_ADDRESS + " TEXT,"
            + KEY_NAME_PEMILIK + " TEXT,"
            + KEY_LATITUDE + " REAL,"
            + KEY_LONGITUDE + " REAL,"
            + KEY_PHONE + " TEXT,"
            + KEY_SISA_KREDIT_LIMIT + " REAL,"
            + KEY_CREDIT_LIMIT + " REAL,"
            + KEY_TOTAL_TAGIHAN + " REAL,"
            + KEY_NO_KTP + " TEXT,"
            + KEY_NO_NPWP + " TEXT,"
            + KEY_IS_ROUTE + " INTEGER,"
            + KEY_PHOTO_KTP + " TEXT,"
            + KEY_PHOTO_NPWP + " TEXT,"
            + KEY_PHOTO_OUTLET + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_CUSTOMER_PROMOTION = "CREATE TABLE " + TABLE_CUSTOMER_PROMOTION + "("
            + KEY_ID_CUSTOMER_PROMOTION_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_ID_PROMOTION + " TEXT,"
            + KEY_NAME_PROMOTION + " TEXT,"
            + KEY_VALID_FROM_PROMOTION + " TEXT,"
            + KEY_VALID_TO_PROMOTION + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_VISIT_SALESMAN = "CREATE TABLE " + TABLE_VISIT_SALESMAN + "("
            + KEY_ID_VISIT_SALESMAN_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_ID_SALESMAN + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_CHECK_IN_TIME + " TEXT,"
            + KEY_CHECK_OUT_TIME + " TEXT,"
            + KEY_STATUS + " TEXT,"
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
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_VISIT_SALESMAN_NOO = "CREATE TABLE " + TABLE_VISIT_SALESMAN_NOO + "("
            + KEY_ID_VISIT_SALESMAN_NOO_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_NOO_DB + " TEXT,"
            + KEY_ID_SALESMAN + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_CHECK_IN_TIME + " TEXT,"
            + KEY_CHECK_OUT_TIME + " TEXT,"
            + KEY_STATUS + " TEXT,"
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
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_STORE_CHECK = "CREATE TABLE " + TABLE_STORE_CHECK + "("
            + KEY_ID_STORE_CHECK_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
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
            + ")";

    public static String CREATE_TABLE_ORDER_DETAIL = "CREATE TABLE " + TABLE_ORDER_DETAIL + "("
            + KEY_ID_ORDER_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_HEADER_DB + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_TOTAL_DISCOUNT + " REAL,"
            + KEY_TOTAL + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_ORDER_DETAIL_EXTRA = "CREATE TABLE " + TABLE_ORDER_DETAIL_EXTRA + "("
            + KEY_ID_ORDER_DETAIL_EXTRA_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_DETAIL_DB + " TEXT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_QTY + " REAL,"
            + KEY_UOM + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_TOTAL_DISCOUNT + " REAL,"
            + KEY_TOTAL + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
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
            + KEY_IS_SYNC + " INTEGER"
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
            + ")";

    public static String CREATE_TABLE_ORDER_PAYMENT_DETAIL = "CREATE TABLE " + TABLE_ORDER_PAYMENT_DETAIL + "("
            + KEY_ID_ORDER_PAYMENT_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_ORDER_PAYMENT_HEADER_DB + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_TYPE_PAYMENT + " REAL,"
            + KEY_TOTAL_PAYMENT + " REAL,"
            + KEY_LEFT + " REAL,"
            + KEY_TRANSFER_DATE + " TEXT,"
            + KEY_GIRO_NO + " TEXT,"
            + KEY_GIRO_DATE + " TEXT,"
            + KEY_DUE_DATE + " TEXT,"
            + KEY_ID_BANK + " TEXT,"
            + KEY_NAME_BANK + " TEXT,"
            + KEY_ID_CUST_BANK + " TEXT,"
            + KEY_NAME_CUST_BANK + " TEXT,"
            + KEY_CHEQUE_NO + " TEXT,"
            + KEY_CHEQUE_DATE + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
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
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_RETURN = "CREATE TABLE " + TABLE_RETURN + "("
            + KEY_ID_RETURN_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CUSTOMER_ID + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
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
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_COLLECTION_HEADER = "CREATE TABLE " + TABLE_COLLECTION_HEADER + "("
            + KEY_ID_COLLECTION_HEADER_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_INVOICE_NO + " TEXT,"
            + KEY_INVOICE_DATE + " TEXT,"
            + KEY_INVOICE_TOTAL + " REAL,"
            + KEY_STATUS + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_COLLECTION_DETAIL = "CREATE TABLE " + TABLE_COLLECTION_DETAIL + "("
            + KEY_ID_COLLECTION_DETAIL_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_COLLECTION_HEADER_DB + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_TYPE_PAYMENT + " REAL,"
            + KEY_TOTAL_PAYMENT + " REAL,"
            + KEY_LEFT + " REAL,"
            + KEY_TRANSFER_DATE + " TEXT,"
            + KEY_GIRO_NO + " TEXT,"
            + KEY_GIRO_DATE + " TEXT,"
            + KEY_DUE_DATE + " TEXT,"
            + KEY_ID_BANK + " TEXT,"
            + KEY_NAME_BANK + " TEXT,"
            + KEY_ID_CUST_BANK + " TEXT,"
            + KEY_NAME_CUST_BANK + " TEXT,"
            + KEY_CHEQUE_NO + " TEXT,"
            + KEY_CHEQUE_DATE + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_COLLECTION_ITEM = "CREATE TABLE " + TABLE_COLLECTION_ITEM + "("
            + KEY_ID_COLLECTION_ITEM_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_COLLECTION_DETAIL_DB + " TEXT,"
            + KEY_MATERIAL_ID + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_PRICE + " REAL,"
            + KEY_AMOUNT_PAID + " REAL,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_REASON = "CREATE TABLE " + TABLE_MASTER_REASON + "("
            + KEY_ID_REASON_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_REASON_BE + " TEXT,"
            + KEY_NAME_REASON + " TEXT,"
            + KEY_CATEGORY_REASON + " TEXT,"
            + KEY_IS_PHOTO + " INTEGER,"
            + KEY_IS_FREE_TEXT + " INTEGER,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    public static String CREATE_TABLE_MASTER_PROMOTION = "CREATE TABLE " + TABLE_MASTER_PROMOTION + "("
            + KEY_ID_PROMOTION_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_PROMOTION + " TEXT,"
            + KEY_NAME_PROMOTION + " TEXT,"
            + KEY_VALID_FROM_PROMOTION + " TEXT,"
            + KEY_VALID_TO_PROMOTION + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
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
            + ")";

    public static String CREATE_TABLE_BANK = "CREATE TABLE " + TABLE_MASTER_BANK + "("
            + KEY_ID_BANK_DB + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ID_BANK_BE + " TEXT,"
            + KEY_NAME_BANK + " TEXT,"
            + KEY_ID_DEPO + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_NO_REK + " TEXT,"
            + KEY_CREATED_BY + " TEXT,"
            + KEY_CREATED_DATE + " TEXT,"
            + KEY_UPDATED_BY + " TEXT,"
            + KEY_UPDATED_DATE + " TEXT,"
            + KEY_IS_SYNC + " INTEGER"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STOCK_REQUEST_HEADER);
        db.execSQL(CREATE_TABLE_STOCK_REQUEST_DETAIL);
        db.execSQL(CREATE_TABLE_INVOICE_HEADER);
        db.execSQL(CREATE_TABLE_INVOICE_DETAIL);
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
        db.execSQL(CREATE_TABLE_REASON);
        db.execSQL(CREATE_TABLE_BANK);
        db.execSQL(CREATE_TABLE_MASTER_PROMOTION);
        db.execSQL(CREATE_TABLE_LOG);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_REASON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_BANK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_PROMOTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
        onCreate(db);
    }

    //add
    public int addStockRequestHeader(StockRequest param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_STOCK_REQUEST_HEADER_BE, param.getId());
        values.put(KEY_REQUEST_DATE, param.getReqdate());
        values.put(KEY_ID_SALESMAN, idSales);
        values.put(KEY_NO_DOC, param.getNodoc());
        values.put(KEY_TANGGAL_KIRIM, param.getTanggalkirim());
        values.put(KEY_NO_SURAT_JALAN, param.getNosuratjalan());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_SIGN, param.getSignature());
        values.put(KEY_IS_UNLOADING, param.isIsunloading());
        values.put(KEY_IS_VERIF, param.isIsverif());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_STOCK_REQUEST_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addStockRequestDetail(Material param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_STOCK_REQUEST_HEADER_DB, idHeader);
        values.put(KEY_MATERIAL_ID, param.getMaterialid());
        values.put(KEY_MATERIAL_NAME, param.getMaterialname());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_QTY_SISA, param.getQtySisa());
        values.put(KEY_UOM_SISA, param.getUomSisa());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_STOCK_REQUEST_DETAIL, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addInvoiceHeader(Invoice param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INVOICE_NO, param.getInvoiceNo());
        values.put(KEY_INVOICE_DATE, param.getInvoiceDate());
        values.put(KEY_INVOICE_TOTAL, param.getAmount());
        values.put(KEY_DUE_DATE, param.getDueDate());
        values.put(KEY_PAID, param.getPaid());
        values.put(KEY_NETT, param.getNett());
        values.put(KEY_CUSTOMER_ID, param.getCustomerID());
        values.put(KEY_CUSTOMER_NAME, param.getCustomerName());
        values.put(KEY_SIGN, param.getSignature());
        values.put(KEY_IS_VERIF, param.isVerification());
        values.put(KEY_IS_ROUTE, param.isRoute());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_INVOICE_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addInvoiceDetail(Material param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_INVOICE_HEADER_DB, idHeader);
        values.put(KEY_MATERIAL_ID, param.getMaterialid());
        values.put(KEY_MATERIAL_NAME, param.getMaterialname());
        values.put(KEY_PRICE, param.getPrice());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_INVOICE_DETAIL, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addNoo(CustomerNoo param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_NOO, param.getName());
        values.put(KEY_ADDRESS_NOO, param.getAddress());
        values.put(KEY_LATITUDE, param.getLatitude());
        values.put(KEY_LONGITUDE, param.getLongitude());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_KODE_POS, param.getKodePos());
        values.put(KEY_ID_DESA_KELURAHAN, param.getIdDesaKelurahan());
        values.put(KEY_NAME_DESA_KELURAHAN, param.getNameDesaKelurahan());
        values.put(KEY_ID_KECAMATAN, param.getIdKecamatan());
        values.put(KEY_NAME_KECAMATAN, param.getNameKecamatan());
        values.put(KEY_ID_KOTA, param.getIdKota());
        values.put(KEY_NAME_KOTA, param.getNameKota());
        values.put(KEY_ID_PROVINSI, param.getIdProvinsi());
        values.put(KEY_NAME_PROVINSI, param.getNameProvinsi());
        values.put(KEY_PHONE, param.getPhone());
        values.put(KEY_NO_NPWP, param.getNoNpwp());
        values.put(KEY_NAME_NPWP, param.getNameNpwp());
        values.put(KEY_ADDRESS_NPWP, param.getAddressNpwp());
        values.put(KEY_STATUS_NPWP, param.getStatusNpwp());
        values.put(KEY_NO_KTP, param.getNoKtp());
        values.put(KEY_NAME_PEMILIK, param.getNamePemilik());
        values.put(KEY_NIK_PEMILIK, param.getNikPemilik());
        values.put(KEY_STATUS_TOKO, param.getStatusToko());
        values.put(KEY_LOKASI, param.getLokasi());
        values.put(KEY_JENIS_USAHA, param.getJenisUsaha());
        values.put(KEY_LAMA_USAHA, param.getLamaUsaha());
        values.put(KEY_SUKU, param.getSuku());
        values.put(KEY_TYPE_TOKO_OUTLET, param.getTypeTokoOutlet());
        values.put(KEY_PRICE_LIST_TYPE, param.getPriceListType());
        values.put(KEY_CREDIT_LIMIT, param.getCreditLimit());
        values.put(KEY_ROUTE, param.getRoute());
        values.put(KEY_PHOTO_KTP, param.getPhotoKtp());
        values.put(KEY_PHOTO_NPWP, param.getPhotoNpwp());
        values.put(KEY_PHOTO_OUTLET, param.getPhotoOutlet());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_NOO, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addCustomer(Customer param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, param.getIdCustomer());
        values.put(KEY_CUSTOMER_NAME, param.getNameCustomer());
        values.put(KEY_CUSTOMER_ADDRESS, param.getAddress());
        values.put(KEY_LATITUDE, param.getLatitude());
        values.put(KEY_LONGITUDE, param.getLongitude());
        values.put(KEY_PHONE, param.getPhone());
        values.put(KEY_NAME_PEMILIK, param.getNamaPemilik());
        values.put(KEY_CREDIT_LIMIT, param.getCreditLimit());
        values.put(KEY_NO_KTP, param.getNoKtp());
        values.put(KEY_NO_NPWP, param.getNoNpwp());
        values.put(KEY_IS_ROUTE, param.isRoute());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_CUSTOMER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addCustomerPromotion(Promotion param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, idHeader);
        values.put(KEY_ID_PROMOTION, param.getPromotionId());
        values.put(KEY_NAME_PROMOTION, param.getPromotionName());
        values.put(KEY_VALID_FROM_PROMOTION, param.getPromotionValidFrom());
        values.put(KEY_VALID_TO_PROMOTION, param.getPromotionValidTo());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_CUSTOMER_PROMOTION, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addVisitSalesman(VisitSalesman param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, param.getCustomerId());
        values.put(KEY_ID_SALESMAN, param.getIdSalesman());
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_CHECK_IN_TIME, param.getCheckInTime());
        values.put(KEY_CHECK_OUT_TIME, param.getCheckOutTime());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_RESUME_TIME, param.getResumeTime());
        values.put(KEY_PAUSE_TIME, param.getPauseTime());
        values.put(KEY_LAT_CHECK_IN, param.getLatCheckIn());
        values.put(KEY_LONG_CHECK_IN, param.getLongCheckIn());
        values.put(KEY_LAT_CHECK_OUT, param.getLatCheckOut());
        values.put(KEY_LONG_CHECK_OUT, param.getLongCheckOut());
        values.put(KEY_INSIDE, param.isInside());
        values.put(KEY_INSIDE_CHECK_OUT, param.isInsideCheckOut());
        values.put(KEY_ID_PAUSE_REASON, param.getIdPauseReason());
        values.put(KEY_NAME_PAUSE_REASON, param.getNamePauseReason());
        values.put(KEY_DESC_PAUSE_REASON, param.getDescPauseReason());
        values.put(KEY_PHOTO_PAUSE_REASON, param.getPhotoPauseReason());
        values.put(KEY_ID_CHECK_OUT_REASON, param.getIdCheckOutReason());
        values.put(KEY_DESC_CHECK_OUT_REASON, param.getDescCheckOutReason());
        values.put(KEY_NAME_CHECK_OUT_REASON, param.getNameCheckOutReason());
        values.put(KEY_PHOTO_CHECK_OUT_REASON, param.getPhotoCheckOutReason());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_VISIT_SALESMAN, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addVisitSalesmanNoo(VisitSalesman param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_NOO_DB, param.getCustomerId());
        values.put(KEY_ID_SALESMAN, param.getIdSalesman());
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_CHECK_IN_TIME, param.getCheckInTime());
        values.put(KEY_CHECK_OUT_TIME, param.getCheckOutTime());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_RESUME_TIME, param.getResumeTime());
        values.put(KEY_PAUSE_TIME, param.getPauseTime());
        values.put(KEY_LAT_CHECK_IN, param.getLatCheckIn());
        values.put(KEY_LONG_CHECK_IN, param.getLongCheckIn());
        values.put(KEY_LAT_CHECK_OUT, param.getLatCheckOut());
        values.put(KEY_LONG_CHECK_OUT, param.getLongCheckOut());
        values.put(KEY_INSIDE, param.isInside());
        values.put(KEY_INSIDE_CHECK_OUT, param.isInsideCheckOut());
        values.put(KEY_ID_PAUSE_REASON, param.getIdPauseReason());
        values.put(KEY_NAME_PAUSE_REASON, param.getNamePauseReason());
        values.put(KEY_DESC_PAUSE_REASON, param.getDescPauseReason());
        values.put(KEY_PHOTO_PAUSE_REASON, param.getPhotoPauseReason());
        values.put(KEY_ID_CHECK_OUT_REASON, param.getIdCheckOutReason());
        values.put(KEY_DESC_CHECK_OUT_REASON, param.getDescCheckOutReason());
        values.put(KEY_NAME_CHECK_OUT_REASON, param.getNameCheckOutReason());
        values.put(KEY_PHOTO_CHECK_OUT_REASON, param.getPhotoCheckOutReason());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_VISIT_SALESMAN_NOO, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addStoreCheck(StoreCheck param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, param.getCustomerId());
        values.put(KEY_MATERIAL_ID, param.getMaterialId());
        values.put(KEY_MATERIAL_NAME, param.getMaterialName());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_STORE_CHECK, null, values);//return id yg ud d create
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
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_ORDER_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addOrderDetail(Material param, String idHeader, String idCust, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, idHeader);
        values.put(KEY_CUSTOMER_ID, idCust);
        values.put(KEY_MATERIAL_ID, param.getMaterialid());
        values.put(KEY_MATERIAL_NAME, param.getMaterialname());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_PRICE, param.getPrice());
        values.put(KEY_TOTAL_DISCOUNT, param.getTotalDiscount());
        values.put(KEY_TOTAL, param.getTotal());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_ORDER_DETAIL, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addOrderDetailExtra(Material param, String idHeader, String idCust, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_DETAIL_DB, idHeader);
        values.put(KEY_CUSTOMER_ID, idCust);
        values.put(KEY_MATERIAL_ID, param.getMaterialid());
        values.put(KEY_MATERIAL_NAME, param.getMaterialname());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_PRICE, param.getPrice());
        values.put(KEY_TOTAL_DISCOUNT, param.getTotalDiscount());
        values.put(KEY_TOTAL, param.getTotal());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_ORDER_DETAIL_EXTRA, null, values);//return id yg ud d create
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
//        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
//        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_ORDER_DETAIL_DISCOUNT, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addOrderPayment(CollectionTransfer param, String typePayment, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, idHeader);
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_OMZET, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, typePayment);
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_ORDER_PAYMENT_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addOrderPaymentTransfer(CollectionTransfer param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, idHeader);
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_OMZET, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, "transfer");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_TRANSFER_DATE, param.getTglTransfer());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_ORDER_PAYMENT_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addOrderPaymentGiro(CollectionGiro param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, idHeader);
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_OMZET, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, "giro");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_GIRO_NO, param.getNoGiro());
        values.put(KEY_GIRO_DATE, param.getTglGiro());
        values.put(KEY_DUE_DATE, param.getTglCair());
        values.put(KEY_ID_BANK, param.getIdBankASPP());
        values.put(KEY_NAME_BANK, param.getBankNameASPP());
        values.put(KEY_ID_CUST_BANK, param.getIdBankCust());
        values.put(KEY_NAME_CUST_BANK, param.getBankCust());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_ORDER_PAYMENT_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addOrderPaymentCheque(CollectionCheque param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_HEADER_DB, idHeader);
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_OMZET, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, "giro");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_CHEQUE_NO, param.getNoCheque());
        values.put(KEY_CHEQUE_DATE, param.getTglCheque());
        values.put(KEY_DUE_DATE, param.getTglCair());
        values.put(KEY_ID_BANK, param.getIdBankASPP());
        values.put(KEY_NAME_BANK, param.getBankNameASPP());
        values.put(KEY_ID_CUST_BANK, param.getIdBankCust());
        values.put(KEY_NAME_CUST_BANK, param.getBankCust());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_ORDER_PAYMENT_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addOrderPaymentDetail(Material param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_PAYMENT_HEADER_DB, idHeader);
        values.put(KEY_MATERIAL_ID, param.getMaterialid());
        values.put(KEY_MATERIAL_NAME, param.getMaterialname());
        values.put(KEY_PRICE, param.getPrice());
        values.put(KEY_AMOUNT_PAID, param.getAmountPaid());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_ORDER_PAYMENT_DETAIL, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addReturn(Return param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_ID, param.getCustomerId());
        values.put(KEY_DATE, param.getDate());
        values.put(KEY_MATERIAL_ID, param.getMaterialId());
        values.put(KEY_MATERIAL_NAME, param.getMaterialName());
        values.put(KEY_QTY, param.getQty());
        values.put(KEY_UOM, param.getUom());
        values.put(KEY_EXPIRED_DATE, param.getExpiredDate());
        values.put(KEY_CONDITION, param.getCondition());
        values.put(KEY_ID_REASON_RETURN, param.getIdReason());
        values.put(KEY_NAME_REASON_RETURN, param.getNameReason());
        values.put(KEY_DESC_REASON_RETURN, param.getDescReason());
        values.put(KEY_PHOTO_REASON_RETURN, param.getPhotoReason());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_RETURN, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addCollection(CollectionTransfer param, String typePayment, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INVOICE_NO, idHeader);
        values.put(KEY_INVOICE_DATE, param.getDate());
        values.put(KEY_INVOICE_TOTAL, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, typePayment);
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_COLLECTION_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addCollectionTransfer(CollectionTransfer param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INVOICE_NO, idHeader);
        values.put(KEY_INVOICE_DATE, param.getDate());
        values.put(KEY_INVOICE_TOTAL, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, "transfer");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_TRANSFER_DATE, param.getTglTransfer());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_COLLECTION_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addCollectionGiro(CollectionGiro param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INVOICE_NO, idHeader);
        values.put(KEY_INVOICE_DATE, param.getDate());
        values.put(KEY_INVOICE_TOTAL, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, "giro");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_GIRO_NO, param.getNoGiro());
        values.put(KEY_GIRO_DATE, param.getTglGiro());
        values.put(KEY_DUE_DATE, param.getTglCair());
        values.put(KEY_ID_BANK, param.getIdBankASPP());
        values.put(KEY_NAME_BANK, param.getBankNameASPP());
        values.put(KEY_ID_CUST_BANK, param.getIdBankCust());
        values.put(KEY_NAME_CUST_BANK, param.getBankCust());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_COLLECTION_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addCollectionCheque(CollectionCheque param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INVOICE_NO, idHeader);
        values.put(KEY_INVOICE_DATE, param.getDate());
        values.put(KEY_INVOICE_TOTAL, param.getOmzet());
        values.put(KEY_TYPE_PAYMENT, "giro");
        values.put(KEY_TOTAL_PAYMENT, param.getTotalPayment());
        values.put(KEY_LEFT, param.getLeft());
        values.put(KEY_CHEQUE_NO, param.getNoCheque());
        values.put(KEY_CHEQUE_DATE, param.getTglCheque());
        values.put(KEY_DUE_DATE, param.getTglCair());
        values.put(KEY_ID_BANK, param.getIdBankASPP());
        values.put(KEY_NAME_BANK, param.getBankNameASPP());
        values.put(KEY_ID_CUST_BANK, param.getIdBankCust());
        values.put(KEY_NAME_CUST_BANK, param.getBankCust());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_COLLECTION_HEADER, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addCollectionDetail(Material param, String idHeader, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_COLLECTION_HEADER_DB, idHeader);
        values.put(KEY_MATERIAL_ID, param.getMaterialid());
        values.put(KEY_MATERIAL_NAME, param.getMaterialname());
        values.put(KEY_PRICE, param.getPrice());
        values.put(KEY_AMOUNT_PAID, param.getAmountPaid());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_COLLECTION_DETAIL, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addMasterReason(Reason param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_REASON_BE, param.getId());
        values.put(KEY_NAME_REASON, param.getDesc());
        values.put(KEY_CATEGORY_REASON, param.getCategory());
        values.put(KEY_IS_PHOTO, param.isPhoto());
        values.put(KEY_IS_FREE_TEXT, param.isFreeText());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_MASTER_REASON, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addMasterPromotion(Promotion param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_PROMOTION, param.getPromotionId());
        values.put(KEY_NAME_PROMOTION, param.getPromotionName());
        values.put(KEY_VALID_FROM_PROMOTION, param.getPromotionValidFrom());
        values.put(KEY_VALID_TO_PROMOTION, param.getPromotionValidTo());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_MASTER_PROMOTION, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    public int addLog(Log param, String idSales) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DESC_LOG, param.getDescLog());
        values.put(KEY_DATE_LOG, param.getDateLog());
        values.put(KEY_TIME_LOG, param.getTimeLog());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_MASTER_PROMOTION, null, values);//return id yg ud d create
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
        values.put(KEY_NO_REK, param.getNoRekening());
        values.put(KEY_CREATED_BY, idSales);
        values.put(KEY_CREATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));
        values.put(KEY_IS_SYNC, param.isSync()); //0 false, 1 true

        int id = (int) db.insert(TABLE_MASTER_BANK, null, values);//return id yg ud d create
        //db.close();
        return id;
    }

    //get
    public List<StockRequest> getAllStockRequestHeader() {
        List<StockRequest> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STOCK_REQUEST_HEADER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                StockRequest paramModel = new StockRequest();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_DB)));
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_BE)));
                paramModel.setReqdate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQUEST_DATE)));
                paramModel.setNodoc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_DOC)));
                paramModel.setTanggalkirim(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TANGGAL_KIRIM)));
                paramModel.setNosuratjalan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_SURAT_JALAN)));
                paramModel.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                paramModel.setIsunloading(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_UNLOADING)) != 0);
                paramModel.setSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)) != 0);
                paramModel.setIsverif(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)) != 0);
                paramModel.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));

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
                paramModel.setMaterialid(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setMaterialname(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setQty(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_QTY)));
                paramModel.setUom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_UOM)));
                paramModel.setSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)) != 0);

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
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
                result.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_STOCK_REQUEST_HEADER_BE)));
                result.setReqdate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_REQUEST_DATE)));
                result.setNodoc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_DOC)));
                result.setTanggalkirim(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TANGGAL_KIRIM)));
                result.setNosuratjalan(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_SURAT_JALAN)));
                result.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUS)));
                result.setIsunloading(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_UNLOADING)) != 0);
                result.setSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)) != 0);
                result.setIsverif(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)) != 0);
                result.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));
            }
        }

        assert cursor != null;
        cursor.close();
        return result;
    }

    public List<Invoice> getAllInvoiceHeaderNotVerif() {
        List<Invoice> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_INVOICE_HEADER + " WHERE " + KEY_IS_VERIF + " = 0";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Invoice paramModel = new Invoice();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_INVOICE_HEADER_DB)));
                paramModel.setInvoiceNo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_NO)));
                paramModel.setInvoiceDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_DATE)));
                paramModel.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_INVOICE_TOTAL)));
                paramModel.setDueDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DUE_DATE)));
                paramModel.setPaid(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID)));
                paramModel.setNett(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_NETT)));
                paramModel.setCustomerID(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setCustomerName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
                paramModel.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));
                paramModel.setVerification(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)) != 0);
                paramModel.setRoute(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_ROUTE)) != 0);
                paramModel.setSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)) != 0);
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
                paramModel.setMaterialid(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_ID)));
                paramModel.setMaterialname(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MATERIAL_NAME)));
                paramModel.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                paramModel.setSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)) != 0);

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
                paramModel.setInvoiceNo(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_NO)));
                paramModel.setInvoiceDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_INVOICE_DATE)));
                paramModel.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_INVOICE_TOTAL)));
                paramModel.setDueDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DUE_DATE)));
                paramModel.setPaid(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PAID)));
                paramModel.setNett(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_NETT)));
                paramModel.setCustomerID(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setCustomerName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
                paramModel.setSignature(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIGN)));
                paramModel.setVerification(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_VERIF)) != 0);
                paramModel.setRoute(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_ROUTE)) != 0);
                paramModel.setSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)) != 0);
                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<Customer> getAllCustomerVisit() {
        List<Customer> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Customer paramModel = new Customer();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_CUSTOMER_DB)));
                paramModel.setIdCustomer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ID)));
                paramModel.setNameCustomer(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_NAME)));
                paramModel.setNamaPemilik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PEMILIK)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CUSTOMER_ADDRESS)));
                paramModel.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                paramModel.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                paramModel.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                paramModel.setSisaCreditLimit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_SISA_KREDIT_LIMIT)));
                paramModel.setCreditLimit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_CREDIT_LIMIT)));
                paramModel.setTotalTagihan(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_TOTAL_TAGIHAN)));
                paramModel.setNoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_KTP)));
                paramModel.setNoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_NPWP)));
                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                paramModel.setRoute(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_ROUTE)) != 0);
                paramModel.setSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)) != 0);

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public List<CustomerNoo> getAllCustomerNoo() {
        List<CustomerNoo> arrayList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomerNoo paramModel = new CustomerNoo();
                paramModel.setIdHeader(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_NOO_DB)));
                paramModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_NOO)));
                paramModel.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADDRESS_NOO)));
                paramModel.setNamePemilik(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_PEMILIK)));
                paramModel.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)));
                paramModel.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)));
                paramModel.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
                paramModel.setCreditLimit(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_CREDIT_LIMIT)));
                paramModel.setPhotoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_KTP)));
                paramModel.setPhotoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_NPWP)));
                paramModel.setPhotoOutlet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHOTO_OUTLET)));
                paramModel.setNoKtp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_KTP)));
                paramModel.setNoNpwp(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_NPWP)));
                paramModel.setSync(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_SYNC)) != 0);

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
                paramModel.setNoRekening(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NO_REK)));

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
                paramModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID_REASON_BE)));
                paramModel.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME_REASON)));
                paramModel.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORY_REASON)));
                paramModel.setPhoto(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_PHOTO)) != 0);
                paramModel.setFreeText(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_FREE_TEXT)) != 0);

                arrayList.add(paramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    //update
    public void updateStockRequestVerification(StockRequest param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_VERIF, param.isIsverif());
        values.put(KEY_SIGN, param.getSignature());
        values.put(KEY_IS_SYNC, param.isSync());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));

        db.update(TABLE_STOCK_REQUEST_HEADER, values, KEY_ID_STOCK_REQUEST_HEADER_DB + " = ?",
                new String[]{param.getIdHeader()});
        //db.close();
    }

    public void updateUnloading(StockRequest param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_UNLOADING, param.isIsunloading());
        values.put(KEY_IS_SYNC, param.isSync());
        values.put(KEY_STATUS, param.getStatus());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));

        db.update(TABLE_STOCK_REQUEST_HEADER, values, KEY_ID_STOCK_REQUEST_HEADER_DB + " = ?",
                new String[]{param.getIdHeader()});
        //db.close();
    }

    public void updateInvoiceVerification(Invoice param, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_VERIF, param.isVerification());
        values.put(KEY_SIGN, param.getSignature());
        values.put(KEY_IS_SYNC, param.isSync());
        values.put(KEY_UPDATED_BY, username);
        values.put(KEY_UPDATED_DATE, Helper.getTodayDate(Constants.DATE_FORMAT_6));

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
}

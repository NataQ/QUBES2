package id.co.qualitas.qubes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.model.CheckInOutRequest;
import id.co.qualitas.qubes.model.FreeGoods;
import id.co.qualitas.qubes.model.GCMResponse;
import id.co.qualitas.qubes.model.JenisJualandTop;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.MaterialResponse;
import id.co.qualitas.qubes.model.OptionFreeGoods;
import id.co.qualitas.qubes.model.OrderPlanDetailRequest;
import id.co.qualitas.qubes.model.OrderPlanHeader;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Partner;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.Return;
import id.co.qualitas.qubes.model.ReturnRequest;
import id.co.qualitas.qubes.model.ReturnResponse;
import id.co.qualitas.qubes.model.SalesOffice;
import id.co.qualitas.qubes.model.StandardDeliveryOrder;
import id.co.qualitas.qubes.model.ToPrice;
import id.co.qualitas.qubes.model.UnitOfMeasure;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitDateResponse;
import id.co.qualitas.qubes.model.VisitOrderDetailResponse;
import id.co.qualitas.qubes.model.VisitOrderHeader;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 9;

    // Database Name
    private static final String DATABASE_NAME = "QUBES";

    // Database Path
    private static final String DATABASE_PATH = "";
    private static final String KEY_TAX = "tax";
    private static String KEY_DATE_FOR_SYNC = "DateForSync";

    private Boolean flagCreate = false;

    // Notification Table - column names
    private static final String KEY_ID_STANDARD_DELIVERY_ORDER = "IdStandardDeliveryOrder";
    private static final String KEY_CUSTOMER_NO = "CustomerNo";
    private static final String KEY_CUSTOMER_NAME = "CustomerName";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_POSTAL_CODE = "PostalCode";
    private static final String KEY_LONGITUDE = "Longitude";
    private static final String KEY_LATITUDE = "Latitude";
    private static final String KEY_MATERIAL_NO = "MaterialNo";
    private static final String KEY_MATERIAL_DESC = "MaterialDesc";
    private static final String KEY_QTY = "QTY";
    private static final String KEY_BATCH = "Batch";
    private static final String KEY_CHECK_IN_DATE = "CheckInDate";
    private static final String KEY_CHECK_IN_TIME = "CheckInTime";
    private static final String KEY_CHECK_OUT_DATE = "CheckOutDate";
    private static final String KEY_CHECK_OUT_TIME = "checkOutTime";
    private static final String KEY_QTY_RETURN = "QtyReturn";
    private static final String KEY_REASON = "Reason";
    private static final String KEY_STATUS = "Status";
    private static final String KEY_PRIORITY = "Priority";
    private static final String KEY_REASON_DELIVERY = "ReasonDelivery";
    private static final String KEY_QTY_DELIVERY = "QtyDelivery";
    private static final String KEY_CONTACT_NAME = "ContactName";
    private static final String KEY_STATUS_ORDER = "StatusOrder";
    private static final String KEY_TIMER = "Timer";
    private static final String KEY_VISIT_DATE = "VisitDate";
    private static final String KEY_DELETED = "Deleted";

    private static final String KEY_ID_MATERIAL = "IdMaterial";
    private static final String KEY_MATERIAL_CODE = "MaterialCode";
    private static final String KEY_ATTACHMENT = "Attachment";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_DELIVERY_NUMBER = "DeliveryNumber";
    private static final String KEY_ID_MAT_CLASS = "idMatClass";

    private static final String KEY_ID_COST_CATEGORY = "IdCategory";

    private static final String KEY_ID_COLLECTION = "IdCollection";
    private static final String KEY_AMOUNT_TO_PAY = "AmountToPay";
    private static final String KEY_TEMP_MONEY = "TempMoney";
    private static final String KEY_TANGGAL = "Tanggal";
    private static final String KEY_ID_VISIT = "IdVisit";

    private static final String KEY_ID_ORDER_PLAN_LIST = "idOrderPlanList";
    private static final String KEY_KODE_OUTLET = "KodeOutlet";
    private static final String KEY_NAMA_OUTLET = "NamaOutlet";
    private static final String KEY_KONTAK = "Kontak";

    private static final String KEY_ID_MATERIAL_RETURN = "IdMaterial";
    private static final String KEY_MATERIAL_RETURN_NAME = "MaterialName";
    private static final String KEY_QTY_MATERIAL_RETURN = "QtyReturn";
    private static final String KEY_UOM_RETURN = "UOM";
    private static final String KEY_BATCH_RETURN = "Batch";
    private static final String KEY_EXPIRED_DATE = "expired_date";
    private static final String KEY_GOOD_STOCK = "GoodStock";
    private static final String KEY_REASON_RETURN = "reason";
    private static final String KEY_DESCRIPTION_RETURN = "description";

    //Table Return Header
    private static final String KEY_ID_HEADER_RETURN = "id";
    private static final String KEY_NO_BRB = "no_brb";
    private static final String KEY_NO_RETURN_REPORT = "no_rr";
    private static final String KEY_TANGGAL_BRB = "tgl_brb";
    private static final String KEY_TANGGAL_RR = "tgl_rr";
    private static final String KEY_CATEGORY = "category";

    //material code, material name
    private static final String KEY_LAST_ORDER_QTY_1 = "loQty1";
    private static final String KEY_LAST_ORDER_QTY_2 = "loQty2";
    private static final String KEY_LAST_ORDER_UOM_1 = "loUom1";
    private static final String KEY_LAST_ORDER_UOM_2 = "loUom2";
    private static final String KEY_LAST_STOCK_QTY_1 = "lsQty1";
    private static final String KEY_LAST_STOCK_QTY_2 = "lsQty2";
    private static final String KEY_LAST_STOCK_UOM_1 = "lsUOM1";
    private static final String KEY_LAST_STOCK_UOM_2 = "lsUOM2";
    private static final String KEY_QTY_1 = "QTY1";
    private static final String KEY_QTY_2 = "QTY2";
    private static final String KEY_UOM_1 = "UOM1";
    private static final String KEY_UOM_2 = "UOM2";
    private static final String KEY_PRICE = "price";

    private static final String KEY_TARGET_DATE = "targetDate";

    private static final String KEY_STATUS_CHECK_STORE = "Status Check Store";

    //NEW field Master Material
    //idMaterial
    private static final String KEY_MATERIAL_NAME = "materialName";
    private static final String KEY_IS_LAST_TO = "isLastTO";
    //klasifikasi

    //NEW field Master UOM
    private static final String KEY_ID_MUOM = "muomId";
    private static final String KEY_ID_MUOMR = "muomRId";
    private static final String KEY_UOM = "uomName";
    private static final String KEY_CONVERSION = "conversion";

    //NEW field TOP
    private static final String KEY_JENIS_JUAL = "jenisJual";
    private static final String KEY_TOP_SAP = "top_sap";
    private static final String KEY_ID_TOP = "top_id";
    private static final String KEY_ID_TOP_N = "idTop";
    private static final String KEY_TOP = "top";
    private static final String KEY_TOP_F = "topf";
    private static final String KEY_TOP_K = "topk";

    //NEW field storecheck
    private static final String KEY_ID_STORE_CHECK = "idStoreCheck";
    private static final String KEY_ID_EMPLOYEE = "idEmployee";
    private static final String KEY_NIK = "NIK";

    //NEW field credit info
    private static final String KEY_ID_CREDIT_INFO = "idCreditInfo";
    private static final String KEY_CREDIT_LIMIT = "creditLimit";
    private static final String KEY_CREDIT_EXPOSURE = "creditExposure";
    private static final String KEY_OVERDUE = "overdue";

    //NEW field order detail
    private static final String KEY_ID_TO_DETAIL = "idOrderDetail";
    private static final String KEY_ORDER_PLAN_QTY1 = "orderPlanQty1";
    private static final String KEY_ORDER_PLAN_QTY2 = "orderPlanQty2";
    private static final String KEY_ORDER_PLAN_UOM1 = "orderPlanUom1";
    private static final String KEY_ORDER_PLAN_UOM2 = "orderPlanUom2";

    //NEW field jenis jual
    private static final String KEY_ID_JJ = "idJJ";
    private static final String KEY_ID_JENIS_JUAL = "idJenisJual";
    private static final String KEY_NAMA_JENIS_JUAL = "namaJenisJual";

    //NEW field ToHeader
    private static final String KEY_ID_TO_HEADER = "idToHeader";
    private static final String KEY_NO_TO = "noTo";
    private static final String KEY_BILLING_VALUE = "billingValue";
    //klasifikasi
    private static String KEY_DATE = "date";
    private static final String KEY_ORDER_TYPE = "order_type";
    private static final String KEY_SIGNATURE = "signature";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_COMMENT = "comment";

    //Sales Office
    private static final String KEY_NUM_SALES_OFFICE = "numSalesOffice";
    private static final String KEY_ID_SALES_OFFICE = "idSalesOffice";
    private static final String KEY_SALES_OFFICE_NAME = "salesOfficeName";

    //AssignPartner
    private static final String KEY_NUM_PARTNER = "numPartner";
    private static final String KEY_ID_PARTNER = "id_partner";

    //ReturnDetail
    private static final String KEY_ITEM_NR = "item_nr";
    private static final String KEY_ID_PRICE = "id_price";

    private static final String KEY_TOTAL_AMOUNT = "totalAmount";

    private static final String KEY_ID_VISIT_PLAN = "idVisitPlan";

    private static final String KEY_SYNC_STATUS = "sync_status";

    //table fcm======
    private static final String KEY_ID_FCM = "idFcm";
    private static final String KEY_USERNAME = "username";
    //table fcm======


    //Tambahan di table UOM
    private static final String KEY_UOM_NAME = "uom_name";

    private static final String KEY_HARGA_JADI = "harga_jadi";

    // Table Names
    private static final String TABLE_STANDARD_DELIVERY_ORDER = "StandardDeliveryOrder"; //harus disederhanain
    private static final String TABLE_DETAIL_ORDER_PLAN = "DetailOrderPlan";

    private static final String TABLE_MATERIAL = "Material"; //cek table master material
    private static final String TABLE_UOM = "UOM"; //cek table masteruomorder sama uomreturn
    private static final String TABLE_OUTLET = "Outlet"; //cek table standarddeliveryorder
    private static final String TABLE_VISIT_PLAN = "VisitPlan"; //cek table visit
    private static final String TABLE_STORE_CHECK = "StoreCheck";
    private static final String TABLE_ORDER_PLAN = "OrderPlan";
    private static final String TABLE_ORDER_HEADER = "OrderHeader"; // cek table to header
    private static final String TABLE_ORDER_DETAIL = "OrderDetail"; //cek table to detail
    private static final String TABLE_RETURN_HEADER = "ReturnHeader"; //cek table return material
    private static final String TABLE_RETURN_DETAIL = "ReturnDetail";
    private static final String TABLE_TOP = "TOP";
    private static final String TABLE_JENIS_JUAL = "JenisJual";
    private static final String TABLE_SALES_OFFICE = "SalesOffice";
    private static final String TABLE_PARTNER = "Partner";
    private static final String TABLE_TO_PRICE = "TO_Price";
    private static final String TABLE_REASON = "Reason";
    private static final String TABLE_ATTENDANCE = "Attendance";
    private static final String TABLE_MASTER_UOM = "MasterUom";

    private static final String TABLE_FREE_GOODS = "FreeGoods";

    private static final String TABLE_USER = "User";


    //table fcm======
    private static final String TABLE_FCM = "Fcm";
    //table fcm======

    private static final String KEY_KLASIFIKASI = "Klasifikasi";

    private static final String KEY_STOCK_QTY_1 = "StockQty1";
    private static final String KEY_STOCK_UOM_1 = "StockUOM1";
    private static final String KEY_STOCK_QTY_2 = "StockQty2";
    private static final String KEY_STOCK_UOM_2 = "StockUOM2";

    private static final String KEY_STOCK_QTY_1_C = "StockQty1C";
    private static final String KEY_STOCK_UOM_1_C = "StockUOM1C";
    private static final String KEY_STOCK_QTY_2_C = "StockQty2C";
    private static final String KEY_STOCK_UOM_2_C = "StockUOM2C";

    private static final String KEY_ID_UOM = "idUOM";
    private static final String KEY_IS_ORDER = "isOrder";
    private static final String KEY_IS_RETURN = "isReturn";

    private static final String KEY_ID_OUTLET = "idOutlet";
    private static final String KEY_OUTLET_NAME = "outletName";
    private static final String KEY_ID_PLANT = "idPlant";


    private static final String KEY_RESUME_TIME = "resumeTime";
    private static final String KEY_PAUSE_TIME = "pauseTime";
    private static final String KEY_LAT_CHECK_IN = "latCheckIn";


    private static final String KEY_LONG_CHECK_IN = "longCheckIn";
    private static final String KEY_LAT_CHECK_OUT = "latCheckOut";
    private static final String KEY_LONG_CHECK_OUT = "longCheckOut";
    private static final String KEY_PAUSE_REASON = "pauseReason";
    private static final String KEY_CHECK_OUT_REASON = "checkOutReason";

    private static final String KEY_ID_TO = "idTO";
    private static final String KEY_SOLD_TO = "soldTO";
    private static final String KEY_SHIP_TO = "shopTO";
    private static final String KEY_NO_PO = "noPO";
    private static final String KEY_TGL_PO = "tglPO";
    private static final String KEY_ED_PO = "edPO";
    private static final String KEY_SALES_OFFICE = "salesOffice";
    private static final String KEY_REQ_DISCOUNT = "reqDisc";
    private static final String KEY_DESC_DISCOUNT = "descDisc";


    private static final String KEY_TGL_BRB = "tglBRB";
    private static final String KEY_TGL_RR = "tglRR";

    private static final String KEY_STEP = "step";
    private static final String KEY_COND_TYPE = "cond_type";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_TABLE_NAME = "table_name";
    private static final String KEY_ONE_TIME_DISCOUNT = "one_time_discount";
    private static final String KEY_COND_TYPE_NAME = "cond_type_name";

    private static final String KEY_ID_ORDER_PLAN = "id_order_plan";
    private static final String KEY_PARTNER_NAME = "namePartner";

    private static final String KEY_ENABLED = "enabled";

    private static final String KEY_ID_REASON = "idReason";
    private static final String KEY_REASON_DESC = "reasonDesc";
    private static final String KEY_TYPE = "type";

    private static final String KEY_DURATION = "duration";
    private static final String KEY_ID_VISIT_SALESMAN = "idVisitSalesman";
    private static final String KEY_ID_TO_PRICE = "idToPrice";
    private static final String KEY_SEGMENT = "segment";

    private static final String KEY_ELAPSED_REALTIME = "mElapsedRealtime";

    private static final String KEY_AMOUNT_UOM = "amountUom";

    private static final String KEY_DOC_NO = "DOC_NO";
    private static final String KEY_KC_NO = "KC_NO";
    private static final String KEY_DISC_VALUE = "DISC_VALUE";

    private static final String KEY_ID_FREE_GOODS = "ID_FG";
    private static final String KEY_STATUS_PRICE = "status_price";

    private static final String KEY_TARGET_MONTH = "target_month";
    private static final String KEY_TARGET_CALL = "target_call";
    private static final String KEY_TARGET_SISA = "target_sisa";
    private static final String KEY_TARGET_ACHIEV = "target_achiev";
    private static final String KEY_TARGET_ORDER_PLAN = "target_order_plan";

    private static final String KEY_ID_MOBILE = "id_mobile";
    private static final String KEY_ID_MASTER = "id_master";

    private static final String KEY_QTY_P = "qty_p";
    private static final String KEY_AMOUNT_P = "amount_p";

    private static final String CURDATE = "";

    //table fcm======
    private static final String CREATE_TABLE_FCM = "CREATE TABLE "
            + TABLE_FCM + "(" + KEY_ID_FCM + " INTEGER PRIMARY KEY,"
            + KEY_DESCRIPTION + " TEXT, " + KEY_USERNAME + " TEXT"
            + ")";
    //table fcm======

    /*Table Create Statements*/
    private static final String CREATE_TABLE_ATTENDANCE = "CREATE TABLE "
            + TABLE_ATTENDANCE + "(" + KEY_ID_EMPLOYEE + " TEXT PRIMARY KEY,"
            + KEY_DATE + " TEXT,"
            + KEY_ELAPSED_REALTIME + " TEXT,"
            + KEY_ID_PLANT + " TEXT,"
            + KEY_NIK + " TEXT"
            + ")";

    private static final String CREATE_TABLE_MATERIAL = "CREATE TABLE "
            + TABLE_MATERIAL + "(" + KEY_ID_MATERIAL + " TEXT PRIMARY KEY,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_ID_MAT_CLASS + " TEXT,"
            + KEY_STOCK_QTY_1 + " TEXT,"
            + KEY_STOCK_UOM_1 + " TEXT,"
            + KEY_STOCK_QTY_2 + " TEXT,"
            + KEY_STOCK_UOM_2 + " TEXT,"
            + KEY_STOCK_QTY_1_C + " TEXT,"
            + KEY_STOCK_UOM_1_C + " TEXT,"
            + KEY_STOCK_QTY_2_C + " TEXT,"
            + KEY_STOCK_UOM_2_C + " TEXT" + ")";

    private static final String CREATE_TABLE_UOM = "CREATE TABLE "
            + TABLE_UOM + "(" + KEY_ID_MATERIAL + " TEXT,"
            + KEY_ID_UOM + " TEXT,"
            + KEY_CONVERSION + " TEXT,"
            + KEY_IS_ORDER + " INTEGER,"
            + KEY_IS_RETURN + " INTEGER" + ")";

    //New
    private static final String CREATE_TABLE_MASTER_UOM = "CREATE TABLE "
            + TABLE_MASTER_UOM + "(" + KEY_ID_UOM + " TEXT,"
            + KEY_UOM_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_OUTLET = "CREATE TABLE "
            + TABLE_OUTLET + "("
            + KEY_ID_OUTLET + " TEXT,"
            + KEY_OUTLET_NAME + " TEXT,"
            + KEY_ID_PLANT + " TEXT,"
            + KEY_CREDIT_LIMIT + " TEXT,"
            + KEY_CREDIT_EXPOSURE + " TEXT,"
            + KEY_OVERDUE + " TEXT,"
            + KEY_SEGMENT + " TEXT,"
            + KEY_ID_MASTER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ADDRESS + " TEXT "
            + ")";

    private static final String CREATE_TABLE_VISIT_PLAN = "CREATE TABLE "
            + TABLE_VISIT_PLAN + "(" + KEY_ID_VISIT_PLAN + " TEXT,"
            + KEY_ID_VISIT_SALESMAN + " TEXT,"
            + KEY_ID_OUTLET + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_CHECK_IN_TIME + " TEXT,"
            + KEY_RESUME_TIME + " TEXT,"
            + KEY_PAUSE_TIME + " TEXT,"
            + KEY_CHECK_OUT_TIME + " TEXT,"
            + KEY_LAT_CHECK_IN + " TEXT,"
            + KEY_LONG_CHECK_IN + " TEXT,"
            + KEY_LAT_CHECK_OUT + " TEXT,"
            + KEY_LONG_CHECK_OUT + " TEXT,"
            + KEY_PAUSE_REASON + " TEXT,"
            + KEY_CHECK_OUT_REASON + " TEXT,"
            + KEY_ENABLED + " TEXT,"
            + KEY_SYNC_STATUS + " TEXT,"
            + KEY_DELETED + " TEXT,"
            + KEY_DURATION + " TEXT,"
            + KEY_ID_MOBILE + " TEXT,"
            + KEY_STATUS_ORDER + " TEXT,"
            + KEY_DATE_FOR_SYNC + " TEXT"
            + ")";

    private static final String CREATE_TABLE_STORE_CHECK_N = "CREATE TABLE "
            + TABLE_STORE_CHECK + "(" + KEY_ID_STORE_CHECK + " TEXT,"
            + KEY_ID_OUTLET + " TEXT,"
            + KEY_ID_MATERIAL + " TEXT,"
            + KEY_QTY_1 + " TEXT,"
            + KEY_UOM_1 + " TEXT,"
            + KEY_QTY_2 + " TEXT,"
            + KEY_UOM_2 + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_ID_MOBILE + " TEXT,"
            + KEY_DATE_FOR_SYNC + " TEXT"
            + ")";

    private static final String CREATE_TABLE_ORDER_PLAN_N = "CREATE TABLE "
            + TABLE_ORDER_PLAN + "(" + KEY_ID_ORDER_PLAN + " TEXT,"
            + KEY_ID_OUTLET + " TEXT,"
            + KEY_ID_MATERIAL + " TEXT,"
            + KEY_QTY_1 + " TEXT,"
            + KEY_UOM_1 + " TEXT,"
            + KEY_QTY_2 + " TEXT,"
            + KEY_UOM_2 + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_PRICE + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_DELETED + " TEXT,"
            + KEY_TARGET_MONTH + " TEXT,"
            + KEY_TARGET_CALL + " TEXT,"
            + KEY_TARGET_SISA + " TEXT,"
            + KEY_TARGET_ACHIEV + " TEXT,"
            + KEY_TARGET_ORDER_PLAN + " TEXT,"
            + KEY_ID_MOBILE + " TEXT,"
            + KEY_DATE_FOR_SYNC + " TEXT"
            + ")";

    private static final String CREATE_TABLE_ORDER_HEADER = "CREATE TABLE "
            + TABLE_ORDER_HEADER + "(" + KEY_ID_TO + " TEXT PRIMARY KEY,"
            + KEY_ID_OUTLET + " TEXT,"
            + KEY_SOLD_TO + " TEXT,"
            + KEY_SHIP_TO + " TEXT,"
            + KEY_NO_PO + " TEXT,"
            + KEY_TGL_PO + " TEXT,"
            + KEY_ED_PO + " TEXT,"
            + KEY_SALES_OFFICE + " TEXT,"
            + KEY_ORDER_TYPE + " TEXT,"
            + KEY_SIGNATURE + " TEXT,"
            + KEY_PHOTO + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_REQ_DISCOUNT + " TEXT,"
            + KEY_DESC_DISCOUNT + " TEXT,"
            + KEY_TOTAL_AMOUNT + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_STATUS_PRICE + " TEXT,"
            + KEY_ID_MOBILE + " TEXT,"
            + KEY_DELETED + " TEXT,"
            + KEY_HARGA_JADI + " TEXT,"
            + KEY_DATE_FOR_SYNC + " TEXT"
            + ")";

    private static final String CREATE_TABLE_ORDER_DETAIL = "CREATE TABLE "
            + TABLE_ORDER_DETAIL + "(" + KEY_ID_TO + " TEXT,"
            + KEY_ITEM_NR + " TEXT,"
            + KEY_ID_TO_PRICE + " TEXT,"
            + KEY_ID_PRICE + " TEXT,"
            + KEY_ID_MATERIAL + " TEXT,"
            + KEY_QTY_1 + " TEXT,"
            + KEY_UOM_1 + " TEXT,"
            + KEY_QTY_2 + " TEXT,"
            + KEY_UOM_2 + " TEXT,"
            + KEY_TOP + " TEXT,"
            + KEY_PRICE + " TEXT,"
            + KEY_TOP_SAP + " TEXT,"
            + KEY_JENIS_JUAL + " TEXT"
            + ")";

    private static final String CREATE_TABLE_RETURN_HEADER_N = "CREATE TABLE "
            + TABLE_RETURN_HEADER + "(" + KEY_ID_HEADER_RETURN + " TEXT PRIMARY KEY,"
            + KEY_ID_OUTLET + " TEXT,"
            + KEY_NO_BRB + " TEXT,"
            + KEY_TGL_BRB + " TEXT,"
            + KEY_NO_RETURN_REPORT + " TEXT,"
            + KEY_TGL_RR + " TEXT,"
            + KEY_SYNC_STATUS + " TEXT,"
            + KEY_ID_MOBILE + " TEXT,"
            + KEY_DELETED + " TEXT,"
            + KEY_DATE_FOR_SYNC + " TEXT"
            + ")";

    private static final String CREATE_TABLE_RETURN_DETAIL_N = "CREATE TABLE "
            + TABLE_RETURN_DETAIL + "(" + KEY_ID_HEADER_RETURN + " TEXT,"
            + KEY_ID_MATERIAL + " TEXT,"
            + KEY_QTY_1 + " TEXT,"
            + KEY_UOM_1 + " TEXT,"
            + KEY_QTY_2 + " TEXT,"
            + KEY_UOM_2 + " TEXT,"
            + KEY_EXPIRED_DATE + " TEXT,"
            + KEY_BATCH + " TEXT,"
            + KEY_REASON + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_CATEGORY + " TEXT"
            + ")";

    private static final String CREATE_TABLE_TOP_N = "CREATE TABLE "
            + TABLE_TOP + "(" + KEY_ID_TOP + " TEXT,"
            + KEY_ID_OUTLET + " TEXT,"
            + KEY_KLASIFIKASI + " TEXT,"
            + KEY_ID_JENIS_JUAL + " TEXT,"
            + KEY_TOP_SAP + " TEXT,"
            + KEY_TOP_F + " INTEGER,"
            + KEY_TOP_K + " INTEGER" + ")";

    private static final String CREATE_TABLE_JENIS_JUAL_N = "CREATE TABLE "
            + TABLE_JENIS_JUAL + "(" + KEY_ID_JENIS_JUAL + " TEXT PRIMARY KEY,"
            + KEY_NAMA_JENIS_JUAL + " TEXT" + ")";

    private static final String CREATE_TABLE_SALES_OFFICE_N = "CREATE TABLE "
            + TABLE_SALES_OFFICE + "(" + KEY_ID_SALES_OFFICE + " TEXT PRIMARY KEY,"
            + KEY_SALES_OFFICE_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_PARTNER = "CREATE TABLE "
            + TABLE_PARTNER + "(" + KEY_ID_OUTLET + " TEXT,"
            + KEY_ID_PARTNER + " TEXT,"
            + KEY_PARTNER_NAME + " TEXT" + ")";


    private static final String CREATE_TABLE_TO_PRICE = "CREATE TABLE "
            + TABLE_TO_PRICE + "(" + KEY_ID_OUTLET + " TEXT,"
            + KEY_ID_MATERIAL + " TEXT,"
            + KEY_ID_TO + " TEXT,"
            + KEY_ID_PRICE + " TEXT,"
            + KEY_STEP + " INTEGER,"
            + KEY_COND_TYPE + " TEXT,"
            + KEY_AMOUNT + " TEXT,"
            + KEY_TABLE_NAME + " TEXT,"
            + KEY_ITEM_NR + " TEXT,"
            + KEY_ONE_TIME_DISCOUNT + " BOOLEAN,"
            + KEY_KC_NO + " TEXT,"
            + KEY_DISC_VALUE + " TEXT,"
            + KEY_COND_TYPE_NAME + " TEXT"
            + ")";

    private static final String CREATE_TABLE_TOP = "CREATE TABLE "
            + TABLE_TOP + "(" + KEY_ID_TOP_N + " INTEGER PRIMARY KEY,"
            + KEY_ID_TOP + " TEXT,"
            + KEY_MATERIAL_CODE + " TEXT,"
            + KEY_TOP_SAP + " TEXT,"
            + KEY_TOP_F + " TEXT,"
            + KEY_TOP_K + " TEXT,"
            + KEY_JENIS_JUAL + " TEXT" + ")";


    private static final String CREATE_TABLE_STORE_CHECK = "CREATE TABLE "
            + TABLE_STORE_CHECK + "(" + KEY_ID_STORE_CHECK + " INTEGER PRIMARY KEY,"
            + KEY_ID_EMPLOYEE + " TEXT,"
            + KEY_KODE_OUTLET + " TEXT,"
            + KEY_MATERIAL_CODE + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_QTY_1 + " TEXT,"
            + KEY_UOM_1 + " TEXT,"
            + KEY_QTY_2 + " TEXT,"
            + KEY_UOM_2 + " TEXT" + ")";

    private static final String CREATE_TABLE_STANDARD_DELIVERY_ORDER = "CREATE TABLE "
            + TABLE_STANDARD_DELIVERY_ORDER + "(" + KEY_ID_STANDARD_DELIVERY_ORDER + " INTEGER PRIMARY KEY,"
            + KEY_CUSTOMER_NO + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT," + KEY_ADDRESS + " TEXT,"
            + KEY_POSTAL_CODE + " TEXT," + KEY_LONGITUDE + " REAL,"
            + KEY_LATITUDE + " REAL,"
            + KEY_STATUS + " TEXT," + KEY_REASON_DELIVERY + " TEXT," + KEY_CONTACT_NAME + " TEXT," + KEY_STATUS_ORDER + " TEXT," + KEY_TIMER + " TEXT,"
            + KEY_VISIT_DATE + " TEXT" + ")";

    private static final String CREATE_TABLE_RETURN_HEADER = "CREATE TABLE "
            + TABLE_RETURN_HEADER + "(" + KEY_ID_HEADER_RETURN + " TEXT,"
            + KEY_KODE_OUTLET + " TEXT,"
            + KEY_NO_BRB + " TEXT,"
            + KEY_TANGGAL_BRB + " TEXT,"
            + KEY_NO_RETURN_REPORT + " TEXT,"
            + KEY_TANGGAL_RR + " TEXT,"
            + KEY_CATEGORY + " TEXT" + ")";

    private static final String CREATE_TABLE_DETAIL_ORDER_PLAN = "CREATE TABLE "
            + TABLE_DETAIL_ORDER_PLAN + "(" + KEY_ID_ORDER_PLAN + " TEXT,"
            + KEY_KODE_OUTLET + " TEXT,"
            + KEY_MATERIAL_CODE + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_LAST_ORDER_QTY_1 + " TEXT,"
            + KEY_LAST_ORDER_QTY_2 + " TEXT,"
            + KEY_LAST_ORDER_UOM_1 + " TEXT,"
            + KEY_LAST_ORDER_UOM_2 + " TEXT,"
            + KEY_LAST_STOCK_QTY_1 + " TEXT,"
            + KEY_LAST_STOCK_QTY_2 + " TEXT,"
            + KEY_LAST_STOCK_UOM_1 + " TEXT,"
            + KEY_LAST_STOCK_UOM_2 + " TEXT,"
            + KEY_QTY_1 + " TEXT,"
            + KEY_UOM_1 + " TEXT,"
            + KEY_QTY_2 + " TEXT,"
            + KEY_UOM_2 + " TEXT,"
            + KEY_PRICE + " TEXT" + ")";

    private static final String CREATE_TABLE_REASON = "CREATE TABLE "
            + TABLE_REASON + "(" + KEY_ID_REASON + " TEXT PRIMARY KEY,"
            + KEY_REASON_DESC + " TEXT,"
            + KEY_TYPE + " TEXT"
            + ")";

    private static final String CREATE_TABLE_FREE_GOODS = "CREATE TABLE "
            + TABLE_FREE_GOODS + "(" + KEY_ID_TO + " TEXT," //0
            + KEY_ID_FREE_GOODS + " TEXT," //1
            + KEY_JENIS_JUAL + " TEXT," //2
            + KEY_KLASIFIKASI + " TEXT," //3
            + KEY_ID_MATERIAL + " TEXT," //4
            + KEY_COND_TYPE + " TEXT," //5
            + KEY_DOC_NO + " TEXT," //6
            + KEY_KC_NO + " TEXT," //7
            + KEY_MATERIAL_NAME + " TEXT," //8
            + KEY_QTY + " TEXT," //9
            + KEY_TABLE_NAME + " TEXT," //10
            + KEY_TOP + " TEXT," //11
            + KEY_ONE_TIME_DISCOUNT + " ,"//12
            + KEY_AMOUNT + " TEXT," //13
            + KEY_AMOUNT_UOM + " TEXT," //14
            + KEY_TAX + " TEXT,"//15
            + KEY_ITEM_NR + " TEXT,"//16
            + KEY_UOM + " TEXT,"//17
            + KEY_QTY_P + " TEXT,"//18
            + KEY_AMOUNT_P + " TEXT"//19
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        flagCreate = true;
        init(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STANDARD_DELIVERY_ORDER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETURN_HEADER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAIL_ORDER_PLAN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_CHECK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETURN_DETAIL);

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_UOM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISIT_PLAN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_PLAN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOP);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_JENIS_JUAL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES_OFFICE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_HEADER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAIL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETURN_HEADER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETURN_DETAIL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TO_PRICE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_UOM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FREE_GOODS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FCM);
//
            init(db);
        }
    }

    private void init(SQLiteDatabase db) {
        /*Create Tables*/
        db.execSQL(CREATE_TABLE_STANDARD_DELIVERY_ORDER);
        db.execSQL(CREATE_TABLE_DETAIL_ORDER_PLAN);

        db.execSQL(CREATE_TABLE_MATERIAL);
        db.execSQL(CREATE_TABLE_UOM);

        db.execSQL(CREATE_TABLE_OUTLET);
        db.execSQL(CREATE_TABLE_VISIT_PLAN);
        db.execSQL(CREATE_TABLE_ORDER_PLAN_N);
        db.execSQL(CREATE_TABLE_TOP_N);
        db.execSQL(CREATE_TABLE_JENIS_JUAL_N);
        db.execSQL(CREATE_TABLE_SALES_OFFICE_N);
        db.execSQL(CREATE_TABLE_PARTNER);
        db.execSQL(CREATE_TABLE_STORE_CHECK_N);
        db.execSQL(CREATE_TABLE_ORDER_HEADER);
        db.execSQL(CREATE_TABLE_ORDER_DETAIL);

        db.execSQL(CREATE_TABLE_RETURN_HEADER_N);
        db.execSQL(CREATE_TABLE_RETURN_DETAIL_N);
        db.execSQL(CREATE_TABLE_REASON);
        db.execSQL(CREATE_TABLE_ATTENDANCE);

        db.execSQL(CREATE_TABLE_TO_PRICE);
        db.execSQL(CREATE_TABLE_MASTER_UOM);
        db.execSQL(CREATE_TABLE_FCM);
        db.execSQL(CREATE_TABLE_FREE_GOODS);
    }

    public void dropTable(SQLiteDatabase db, String tableName,
                          String createQuery) {
        if (!flagCreate) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
            db.execSQL(createQuery);
        }
    }

    public void deleteTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DELETE FROM " + tableName);
    }

    public int checkOut(StandardDeliveryOrder standardDeliveryOrder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHECK_OUT_DATE, standardDeliveryOrder.getCheck_out_date());
        values.put(KEY_CHECK_OUT_TIME, standardDeliveryOrder.getCheck_out_time());
        values.put(KEY_STATUS, standardDeliveryOrder.getStatus());
        values.put(KEY_REASON_DELIVERY, standardDeliveryOrder.getReason());

        // updating row
        return db.update(TABLE_STANDARD_DELIVERY_ORDER, values, KEY_CUSTOMER_NO + " = ?",
                new String[]{String.valueOf((standardDeliveryOrder).getCustomer_no())});
    }

    public int pass(StandardDeliveryOrder standardDeliveryOrder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_STATUS, standardDeliveryOrder.getStatus());
        // updating row
        return db.update(TABLE_STANDARD_DELIVERY_ORDER, values, KEY_CUSTOMER_NO + " = ?",
                new String[]{String.valueOf((standardDeliveryOrder).getCustomer_no())});
    }

    public int updateStatusCheckIn(OutletResponse outletResponse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, outletResponse.getStatusCheckIn());

        // updating row
        return db.update(TABLE_STANDARD_DELIVERY_ORDER, values, KEY_CUSTOMER_NO + " = ? ",
                new String[]{String.valueOf((outletResponse).getIdOutlet())});
    }

    public int updateMaterial(Material material) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MATERIAL_CODE, material.getMaterialCode());
        values.put(KEY_QTY, material.getQty());
        values.put(KEY_DESCRIPTION, material.getDesc());
        values.put(KEY_BATCH, material.getBatch());
        values.put(KEY_ATTACHMENT, material.getAttachment());
        values.put(KEY_DELIVERY_NUMBER, material.getDeliveryNumber());

        // updating row
        return db.update(TABLE_MATERIAL, values, KEY_ID_MATERIAL + " = ?",
                new String[]{String.valueOf((material).getMaterialId())});
    }

    public void addMaterial(Material material) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATERIAL_CODE, material.getMaterialCode());
        values.put(KEY_QTY, material.getQty());
        values.put(KEY_DESCRIPTION, material.getDesc());
        values.put(KEY_BATCH, material.getBatch());
        values.put(KEY_ATTACHMENT, material.getAttachment());
        values.put(KEY_DELIVERY_NUMBER, material.getDeliveryNumber());

        db.insert(TABLE_MATERIAL, null, values);
        db.close();
    }

    public void addOutlet(OutletResponse outletResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUSTOMER_NO, outletResponse.getIdOutlet());
        values.put(KEY_CUSTOMER_NAME, outletResponse.getOutletName());
        values.put(KEY_ADDRESS, outletResponse.getArea());
        values.put(KEY_POSTAL_CODE, outletResponse.getPostalCode());
//        values.put(KEY_LONGITUDE, outletResponse.getLongitude());
//        values.put(KEY_LATITUDE, outletResponse.getLatitude());
        values.put(KEY_STATUS, outletResponse.getStatusCheckIn());
        values.put(KEY_REASON_DELIVERY, outletResponse.getReason());
        values.put(KEY_CONTACT_NAME, outletResponse.getCustomerName());
        values.put(KEY_STATUS_ORDER, outletResponse.getStatusOrder());
        db.insert(TABLE_STANDARD_DELIVERY_ORDER, null, values);
        db.close();
    }

    public void addDetailOrderPlan(MaterialResponse materialResponse, String idOutlet) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_KODE_OUTLET, idOutlet);
        values.put(KEY_MATERIAL_CODE, materialResponse.getIdMaterial());
        values.put(KEY_MATERIAL_NAME, materialResponse.getMaterialName());
        values.put(KEY_LAST_ORDER_QTY_1, materialResponse.getQty1());
        values.put(KEY_LAST_ORDER_QTY_2, materialResponse.getQty2());
        values.put(KEY_LAST_ORDER_UOM_1, materialResponse.getUom1());
        values.put(KEY_LAST_ORDER_UOM_2, materialResponse.getUom2());
        values.put(KEY_LAST_STOCK_QTY_1, String.valueOf(materialResponse.getStockQty1()));
        values.put(KEY_LAST_STOCK_QTY_2, String.valueOf(materialResponse.getStockQty2()));
        values.put(KEY_LAST_STOCK_UOM_1, materialResponse.getStockUom1());
        values.put(KEY_LAST_STOCK_UOM_2, materialResponse.getStockUom2());
        values.put(KEY_QTY_1, String.valueOf(materialResponse.getNewQty1()));
        values.put(KEY_UOM_1, materialResponse.getNewUom1());
        values.put(KEY_QTY_2, String.valueOf(materialResponse.getNewQty2()));
        values.put(KEY_UOM_2, materialResponse.getNewUom2());
        values.put(KEY_PRICE, String.valueOf(materialResponse.getPrice()));


        db.insert(TABLE_DETAIL_ORDER_PLAN, null, values);
        db.close();
    }

    public ArrayList<OrderPlanDetailRequest> getAllOrderPlanDetailReq(String idOutlet) {
        ArrayList<OrderPlanDetailRequest> orderPlanList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " +
                TABLE_ORDER_PLAN + " WHERE " + KEY_ID_OUTLET +
                " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderPlanDetailRequest orderPlanDetailRequest = new OrderPlanDetailRequest();
                orderPlanDetailRequest.setId(cursor.getString(0));
                orderPlanDetailRequest.setIdOutlet(cursor.getString(1));
                orderPlanDetailRequest.setIdMaterial(cursor.getString(2));
                if (cursor.getString(3) != null) {
                    orderPlanDetailRequest.setQty1(cursor.getString(3));
                } else {
                    orderPlanDetailRequest.setQty1("0");
                }
                orderPlanDetailRequest.setUom1(cursor.getString(4));

                if (cursor.getString(5) != null) {
                    orderPlanDetailRequest.setQty2(cursor.getString(5));
                } else {
                    orderPlanDetailRequest.setQty2("0");
                }
                orderPlanDetailRequest.setUom2(cursor.getString(6));
                orderPlanDetailRequest.setPrice(cursor.getString(8));
                // Adding contact to list
                orderPlanList.add(orderPlanDetailRequest);
            } while (cursor.moveToNext());
        }

        // return contact list
        return orderPlanList;
    }

    public Material getMaterial() {
        String selectQuery = "SELECT * FROM " + TABLE_MATERIAL + " ORDER BY " + KEY_MATERIAL_CODE + " DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Material material = new Material();
        if (cursor.moveToFirst()) {
            material.setMaterialId(cursor.getString(0));
            material.setMaterialCode(cursor.getString(1));
            material.setQty(cursor.getInt(2));
            material.setDesc(cursor.getString(3));
            material.setBatch(cursor.getString(4));
            material.setAttachment(cursor.getBlob(5));
            material.setDeliveryNumber(cursor.getString(6));
        } else {
            material = null;
        }
        return material;
    }

    public ArrayList<Material> getListMaterial() {
        ArrayList<Material> materialList = new ArrayList<Material>();

        String selectQuery = "SELECT  * FROM " + TABLE_MATERIAL + " ORDER BY " + KEY_ID_MATERIAL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Material material = new Material();
                material.setMaterialId(cursor.getString(0));
                material.setMaterialCode(cursor.getString(1));
                material.setQty(cursor.getInt(2));
                material.setDesc(cursor.getString(3));
                material.setBatch(cursor.getString(4));
                material.setAttachment(cursor.getBlob(5));
                material.setDeliveryNumber(cursor.getString(6));
                materialList.add(material);
            } while (cursor.moveToNext());
        }

        return materialList;
    }

    public ArrayList<StandardDeliveryOrder> getListStandardDeliveryOrder() {
        ArrayList<StandardDeliveryOrder> listStandardDeliveryOrder = new ArrayList<StandardDeliveryOrder>();

        String selectQuery = "SELECT  * FROM " + TABLE_STANDARD_DELIVERY_ORDER + " ORDER BY " + KEY_ID_STANDARD_DELIVERY_ORDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                StandardDeliveryOrder standardDeliveryOrder = new StandardDeliveryOrder();
                standardDeliveryOrder.setDelivery_no(cursor.getString(1));
                standardDeliveryOrder.setCustomer_no(cursor.getString(2));
                standardDeliveryOrder.setCustomer_name(cursor.getString(3));
                standardDeliveryOrder.setAddress(cursor.getString(4));
                standardDeliveryOrder.setPostal_code(cursor.getString(5));
                standardDeliveryOrder.setLongitude(cursor.getDouble(6));
                standardDeliveryOrder.setLatitude(cursor.getDouble(7));
                standardDeliveryOrder.setCheck_in_date(cursor.getString(8));
                standardDeliveryOrder.setCheck_in_time(cursor.getString(9));
                standardDeliveryOrder.setCheck_out_date(cursor.getString(10));
                standardDeliveryOrder.setCheck_out_time(cursor.getString(11));
                standardDeliveryOrder.setStatus(cursor.getString(12));
                standardDeliveryOrder.setReason(cursor.getString(13));
                standardDeliveryOrder.setPriority(cursor.getString(14));
                standardDeliveryOrder.setContact_name(cursor.getString(15));
                listStandardDeliveryOrder.add(standardDeliveryOrder);
            } while (cursor.moveToNext());
        }

        return listStandardDeliveryOrder;
    }

    public ArrayList<OutletResponse> getListOutletResponse() {
        ArrayList<OutletResponse> outletResponseArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_STANDARD_DELIVERY_ORDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OutletResponse outletResponse = new OutletResponse();
                outletResponse.setIdOutlet(cursor.getString(1));
                outletResponse.setOutletName(cursor.getString(2));
                outletResponse.setArea(cursor.getString(3));
                outletResponse.setPostalCode(cursor.getString(4));
//                outletResponse.setLongitude(cursor.getDouble(5));
//                outletResponse.setLatitude(cursor.getDouble(6));
                outletResponse.setStatusCheckIn(cursor.getString(7));
                outletResponse.setReason(cursor.getString(8));
                outletResponse.setCustomerName(cursor.getString(9));
                outletResponse.setStatusOrder(cursor.getString(10));
                outletResponse.setTimer(cursor.getString(11));
                outletResponseArrayList.add(outletResponse);
            } while (cursor.moveToNext());
        }

        return outletResponseArrayList;
    }

    public int updateEnableVisit(String idOutlet, String enable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ENABLED, enable);

        return db.update(TABLE_VISIT_PLAN, values, KEY_ID_OUTLET + " = ?",
                new String[]{String.valueOf((idOutlet))});
    }

    public void deleteOrderPlanById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDER_PLAN, KEY_ID_ORDER_PLAN + " = ?",
                new String[]{id});
        db.close();
    }

    public void deleteReturnById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RETURN_HEADER, KEY_KODE_OUTLET + " = ?",
                new String[]{id});
        db.close();
    }

    public void deleteOrderPlan() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_PLAN;
        db.execSQL(delete);
        db.close();
    }

    public void deleteDetailOrderPlan() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_DETAIL_ORDER_PLAN;
        db.execSQL(delete);
        db.close();
    }

    public void deleteStoreCheck(String idOutlet) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_STORE_CHECK + " WHERE " + KEY_ID_OUTLET + " = " + "'" + idOutlet + "'";
        db.execSQL(delete);
        db.close();
    }

    public void deleteToDetail(String idOutlet) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_DETAIL
                + " WHERE " + KEY_ID_OUTLET + " = " + "'" + idOutlet + "'";
        db.execSQL(delete);
        db.close();
    }

    public void deleteReturnHeaderByIdOutlet(String idOutlet) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_RETURN_HEADER + " WHERE " + KEY_KODE_OUTLET + " = " + "'" + idOutlet + "'";
        db.execSQL(delete);
        db.close();
    }


    public void deleteReturnDetailByIdOutlet(String idOutlet) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_RETURN_DETAIL + " WHERE " + KEY_KODE_OUTLET + " = " + "'" + idOutlet + "'";
        db.execSQL(delete);
        db.close();
    }


    public void deleteReturnHeaderById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_RETURN_HEADER + " WHERE " + KEY_ID_HEADER_RETURN + " = " + "'" + id + "'";
        db.execSQL(delete);
        db.close();
    }


    public void deleteReturnDetailById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_RETURN_DETAIL + " WHERE " + KEY_ID_HEADER_RETURN + " = " + "'" + id + "'";
        db.execSQL(delete);
        db.close();
    }

    public ArrayList<VisitDateResponse> getVisitByClient(String client_name) {
        ArrayList<VisitDateResponse> visitDateResponseArrayList = new ArrayList<VisitDateResponse>();


        String selectQuery =
                "SELECT DISTINCT "
                        + "b." + KEY_OUTLET_NAME + " ,"
                        + "a." + KEY_DATE
                        + " FROM " + TABLE_VISIT_PLAN + " AS a INNER JOIN " + TABLE_OUTLET + " AS b "
                        + " ON " + " a." + KEY_ID_OUTLET + " = b." + KEY_ID_OUTLET
                        + " WHERE " + " b." + KEY_OUTLET_NAME + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{client_name});

        if (cursor.moveToFirst()) {
            do {
                VisitDateResponse visitDateResponse = new VisitDateResponse();
                visitDateResponse.setOutletName(cursor.getString(0));
                visitDateResponse.setVisitDate(cursor.getString(1));

                visitDateResponseArrayList.add(visitDateResponse);
            } while (cursor.moveToNext());
        }

        return visitDateResponseArrayList;
    }

    public List<String> getListVisitOutletByDate(String visitDate) {
        List<String> clientName = new ArrayList<>();

        String selectQuery =
                "SELECT DISTINCT "
                        + "b." + KEY_OUTLET_NAME
                        + " FROM " + TABLE_VISIT_PLAN + " AS a INNER JOIN " + TABLE_OUTLET + " AS b "
                        + " ON " + "a." + KEY_ID_OUTLET + " = b." + KEY_ID_OUTLET
                        + " WHERE a." + KEY_DATE + " = ? AND a." + KEY_STATUS_ORDER + " = 'false'"
                        + "AND (" + KEY_DELETED + " == '0' OR " + KEY_DELETED + " == NULL)";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{visitDate});

        if (cursor.moveToFirst()) {
            do {
                String temp = cursor.getString(0);
                clientName.add(temp);
            } while (cursor.moveToNext());
        }


        return clientName;
    }

    public List<OutletResponse> getListAvailableOutletByDate(String visitDate) {
        List<OutletResponse> outletResponseList = new ArrayList<>();

        String selectQuery =
                "SELECT DISTINCT "
                        + "b." + KEY_OUTLET_NAME
                        + " ,"
                        + "b." + KEY_ID_OUTLET
                        + " FROM " + TABLE_VISIT_PLAN + " AS a INNER JOIN " + TABLE_OUTLET + " AS b "
                        + " ON " + "a." + KEY_ID_OUTLET + " = b." + KEY_ID_OUTLET
                        + " WHERE a." + KEY_DATE + " = ? AND a." + KEY_STATUS_ORDER + " = 'false'"
                        + "AND (" + KEY_DELETED + " == '0' OR " + KEY_DELETED + " == NULL)";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{visitDate});

        if (cursor.moveToFirst()) {
            do {
                OutletResponse outletResponse = new OutletResponse();
                outletResponse.setOutletName(cursor.getString(0));
                outletResponse.setIdOutlet(cursor.getString(1));

                outletResponseList.add(outletResponse);
            } while (cursor.moveToNext());
        }


        return outletResponseList;
    }

    public List<String> getAllListVisitOutletByDate(String visitDate) {
        List<String> clientName = new ArrayList<>();

        String selectQuery =
                "SELECT DISTINCT "
                        + "b." + KEY_OUTLET_NAME
                        + " FROM " + TABLE_VISIT_PLAN + " AS a INNER JOIN " + TABLE_OUTLET + " AS b "
                        + " ON " + "a." + KEY_ID_OUTLET + " = b." + KEY_ID_OUTLET
                        + " WHERE a." + KEY_DATE + " = ? ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{visitDate});

        if (cursor.moveToFirst()) {
            do {
                String temp = cursor.getString(0);
                clientName.add(temp);
            } while (cursor.moveToNext());
        }


        return clientName;
    }

    public void updateStatusOrderPlanNew(OrderPlanHeader orderPlan) {
        try {
            String statusOrder = DatabaseUtils.sqlEscapeString(orderPlan.getStatusOrder());
            String idOutlet = DatabaseUtils.sqlEscapeString(orderPlan.getIdOutlet());
            String date = DatabaseUtils.sqlEscapeString(orderPlan.getDate());
            String selectQuery =
                    "UPDATE " + TABLE_VISIT_PLAN
                            + " SET " + KEY_STATUS_ORDER + " = " + statusOrder
                            + " WHERE " + KEY_ID_OUTLET + " = " + idOutlet + " AND " + KEY_DATE + " = " + date;

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(selectQuery);
        } catch (NullPointerException e) {

        }

    }

    public void addStoreCheck(MaterialResponse material) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_STORE_CHECK, material.getId_store_check());
        values.put(KEY_ID_OUTLET, material.getIdOutlet());
        values.put(KEY_ID_MATERIAL, material.getIdMaterial());
        if (material.getQty1() != null) {
            values.put(KEY_QTY_1, String.valueOf(material.getQty1()));
        }

        if (material.getUom1() != null) {
            values.put(KEY_UOM_1, String.valueOf(material.getUom1()));
        }

        if (material.getQty2() != null) {
            values.put(KEY_QTY_2, String.valueOf(material.getQty2()));
        }

        if (material.getUom2() != null) {
            values.put(KEY_UOM_2, String.valueOf(material.getUom2()));
        }

        if (material.isDeleted()) {
            values.put(KEY_STATUS, "1");
        } else {
            values.put(KEY_STATUS, "0");
        }

        if (material.getDate_mobile() != null) {
            values.put(KEY_DATE_FOR_SYNC, material.getDate_mobile());
        }

        db.insert(TABLE_STORE_CHECK, null, values);
        db.close();
    }


    public void addStoreCheckN(List<MaterialResponse> list) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_STORE_CHECK, CREATE_TABLE_STORE_CHECK_N);
        deleteTable(db, TABLE_STORE_CHECK);
        String selectQuery = "INSERT INTO " + TABLE_STORE_CHECK + "(" + KEY_ID_STORE_CHECK
                + "," + KEY_ID_OUTLET + "," + KEY_ID_MATERIAL
                + "," + KEY_QTY_1 + "," + KEY_UOM_1
                + "," + KEY_QTY_2 + "," + KEY_UOM_2 + "," + KEY_STATUS + "," + KEY_DATE_FOR_SYNC
                + ")" + "VALUES ";
        int count = 0;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String idSc = null, idOutlet = null, idMaterial = null, qty1 = null, uom1 = null, qty2 = null, uom2 = null, status = null,
                        date_mobile = null;

                if (list.get(i).getId_store_check() != null)
                    idSc = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getId_store_check());
                if (list.get(i).getIdOutlet() != null)
                    idOutlet = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getIdOutlet());
                if (list.get(i).getIdMaterial() != null)
                    idMaterial = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getIdMaterial());
                if (list.get(i).getQty1() != null)
                    qty1 = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getQty1());
                if (list.get(i).getUom1() != null)
                    uom1 = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getUom1());
                if (list.get(i).getQty2() != null)
                    qty2 = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getQty2());
                if (list.get(i).getUom2() != null)
                    uom2 = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getUom2());
                if (list.get(i).getStatus() != null) {
                    status = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getStatus());
                } else {
                    status = DatabaseUtils.sqlEscapeString(Constants.STATUS_FOR_UPDATE_BACKEND);
                }

                if (list.get(i).getDate_mobile() != null) {
                    date_mobile = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getDate_mobile());
                }

                selectQuery = selectQuery.concat("("
                        + idSc + "," + idOutlet + "," + idMaterial + ","
                        + qty1 + "," + uom1 + "," + qty2 + "," + uom2 + "," + status + "," + date_mobile
                        + ")");
                count++;
                if (count != 495) {
                    if (i != list.size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_STORE_CHECK + "(" + KEY_ID_STORE_CHECK
                            + "," + KEY_ID_OUTLET + "," + KEY_ID_MATERIAL
                            + "," + KEY_QTY_1 + "," + KEY_UOM_1
                            + "," + KEY_QTY_2 + "," + KEY_UOM_2 + "," + KEY_STATUS + "," + KEY_DATE_FOR_SYNC
                            + ")" + "VALUES ";
                }
            }
        }


        db.close();
    }

    //N
    public ArrayList<UnitOfMeasure> getListUomByIdMat(String idMaterial, String type) {
        ArrayList<UnitOfMeasure> uomList = new ArrayList<>();
        String selectQuery = null;

        if (type.equals(Constants.IS_ORDER)) {
            selectQuery = "SELECT DISTINCT a." + KEY_ID_UOM + " , b." + KEY_UOM_NAME + " , a." + KEY_CONVERSION
                    + " FROM " + TABLE_UOM + " AS a "
                    + " INNER JOIN " + TABLE_MASTER_UOM + " AS b ON a." + KEY_ID_UOM + " = b." + KEY_ID_UOM
                    + " WHERE a." + KEY_ID_MATERIAL + " = ? AND a." + KEY_IS_ORDER + " = 1"
                    + " ORDER BY CAST(a." + KEY_CONVERSION + " as int) ASC"
            ;
        } else {
            selectQuery = "SELECT DISTINCT a." + KEY_ID_UOM + " , b." + KEY_UOM_NAME + " , a." + KEY_CONVERSION
                    + " FROM " + TABLE_UOM + " AS a "
                    + " INNER JOIN " + TABLE_MASTER_UOM + " AS b ON a." + KEY_ID_UOM + " = b." + KEY_ID_UOM
                    + " WHERE a." + KEY_ID_MATERIAL + " = ? AND a." + KEY_IS_RETURN + " = 1"
                    + " ORDER BY CAST(a." + KEY_CONVERSION + " as int) ASC"
            ;

        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMaterial});

        try {
            if (cursor.moveToFirst()) {
                do {
                    UnitOfMeasure uom = new UnitOfMeasure();
                    uom.setId(cursor.getString(0));
                    uom.setUomName(cursor.getString(1));
                    uom.setConversion(cursor.getInt(2));
                    uomList.add(uom);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }


        return uomList;
    }

    public ArrayList<MaterialResponse> getListMaterialStoreCheck(String idOutlet) {
        ArrayList<MaterialResponse> materialList = new ArrayList<MaterialResponse>();

        String selectQuery = "SELECT * FROM " + TABLE_STORE_CHECK + " WHERE " + KEY_ID_OUTLET + " = ? AND " + KEY_STATUS + " != '1'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        if (cursor.moveToFirst()) {
            do {
                MaterialResponse material = new MaterialResponse();
                material.setId_store_check(cursor.getString(0));
                material.setIdOutlet(cursor.getString(1));
                material.setIdMaterial(cursor.getString(2));
                material.setQty1(cursor.getString(3));
                material.setUom1(cursor.getString(4));
                material.setQty2(cursor.getString(5));
                material.setUom2(cursor.getString(6));
                material.setStatus(cursor.getString(7));
                materialList.add(material);
            } while (cursor.moveToNext());
        }

        return materialList;
    }

    //JENIS JUAL
    public void addJenisJual(String idMaterial, JenisJualandTop jenisJualandTop) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MATERIAL_CODE, idMaterial);
        values.put(KEY_NAMA_JENIS_JUAL, jenisJualandTop.getJenisJual());
        values.put(KEY_TOP, jenisJualandTop.getTop());
        values.put(KEY_TOP_F, jenisJualandTop.getTopf());
        values.put(KEY_TOP_K, jenisJualandTop.getTopk());
        values.put(KEY_TOP_SAP, jenisJualandTop.getTop_sap());

        db.insert(TABLE_JENIS_JUAL, null, values);
        db.close();
    }

    public String getNamaJenisJual(String id) {//ini

        String selectQuery = "SELECT * FROM " + TABLE_JENIS_JUAL +
                " WHERE " + KEY_ID_JENIS_JUAL + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        JenisJualandTop jj = new JenisJualandTop();

        if (cursor.moveToFirst()) {
            jj.setJenisJualName(cursor.getString(1));
            return jj.getJenisJualName();
        } else {
            jj = null;
        }
        return jj.getJenisJualName();
    }


    //TO HEADER

    public void deleteToHeader() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_HEADER;
        db.execSQL(delete);
        db.close();
    }

    public void deleteToHeaderWhereIdIn(String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_HEADER + " WHERE " + KEY_ID_TO + " IN " + values;
        db.execSQL(delete);
        db.close();
    }

    public void deleteToHeaderNotIn(String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_HEADER + " WHERE " + KEY_ID_TO + " NOT IN " + values;
        db.execSQL(delete);
        db.close();
    }

    public void deleteToDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_DETAIL;
        db.execSQL(delete);
        db.close();
    }

    public void deleteToDetailNotIn(String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_DETAIL + " WHERE " + KEY_ID_TO + " NOT IN " + values;
        db.execSQL(delete);
        db.close();
    }

    //Sales Office
    public void addSalesOffice(String idOutlet, OutletResponse outletResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_KODE_OUTLET, idOutlet);
        values.put(KEY_ID_SALES_OFFICE, outletResponse.getId());
        values.put(KEY_SALES_OFFICE_NAME, outletResponse.getSalesOfficeName());

        db.insert(TABLE_SALES_OFFICE, null, values);
        db.close();
    }

    //getListPartner
    public ArrayList<Partner> getListPartner(String idOutlet) {
        ArrayList<Partner> listPartner = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PARTNER + " WHERE " + KEY_ID_OUTLET + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        if (cursor.moveToFirst()) {
            do {
                Partner partner = new Partner();
                partner.setId_partner(cursor.getString(1));
                partner.setNamePartner(cursor.getString(2));
                listPartner.add(partner);
            } while (cursor.moveToNext());
        }

        return listPartner;
    }

    public String getPartnerName(String idOutlet) {
        String result;
        String selectQuery = "SELECT " + KEY_PARTNER_NAME + " FROM " + TABLE_PARTNER + " WHERE " + KEY_ID_PARTNER + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
        } else {
            result = Constants.EMPTY_STRING;
        }

        return result;
    }

    //getListSalesOffice
    public ArrayList<OutletResponse> getListSalesOffice(String idOutlet) {
        ArrayList<OutletResponse> listSalesOffice = new ArrayList<OutletResponse>();

        String selectQuery = "SELECT * FROM " + TABLE_SALES_OFFICE + " WHERE " + KEY_KODE_OUTLET + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        if (cursor.moveToFirst()) {
            do {
                OutletResponse outletResponse = new OutletResponse();
                outletResponse.setIdOutlet(cursor.getString(1));
                outletResponse.setId(cursor.getString(2));
                outletResponse.setSalesOfficeName(cursor.getString(3));
                listSalesOffice.add(outletResponse);
            } while (cursor.moveToNext());
        }

        return listSalesOffice;
    }

    public int updatePriceOrderPlan(String idOutlet, String idMat, String basePrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRICE, basePrice);

        return db.update(TABLE_DETAIL_ORDER_PLAN, values, KEY_KODE_OUTLET + " = ? AND " + KEY_MATERIAL_CODE + " = ?",
                new String[]{String.valueOf(idOutlet), String.valueOf(idMat)});
    }

    /*-===========================================Wil============================================-*/
    /*------------------------------------------OrderHeader--------------------------------------*/
    public void addOrderHeader(VisitOrderHeader visitOrderHeader) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_OUTLET, visitOrderHeader.getIdOutlet());
        if (visitOrderHeader.getId() != null) {
            values.put(KEY_ID_TO, visitOrderHeader.getId());
        }
        values.put(KEY_SOLD_TO, visitOrderHeader.getSoldTo());
        values.put(KEY_SHIP_TO, visitOrderHeader.getShipTo());
        values.put(KEY_NO_PO, visitOrderHeader.getNoPo());
        if (visitOrderHeader.getTglPoString() != null) {
            values.put(KEY_TGL_PO, visitOrderHeader.getTglPoString());
        } else if (visitOrderHeader.getTanggalPo() != null) {
            values.put(KEY_TGL_PO, visitOrderHeader.getTanggalPo());
        }
        if (visitOrderHeader.getEdPoString() != null) {
            values.put(KEY_ED_PO, visitOrderHeader.getEdPoString());
        } else {
            values.put(KEY_ED_PO, visitOrderHeader.getEdPo());
        }
        values.put(KEY_SALES_OFFICE, visitOrderHeader.getSalesOffice());
        values.put(KEY_ORDER_TYPE, visitOrderHeader.getOrderType());
        values.put(KEY_SIGNATURE, visitOrderHeader.getSignatureString());
        values.put(KEY_PHOTO, visitOrderHeader.getPhotoString());
        values.put(KEY_COMMENT, visitOrderHeader.getComment());
        if (visitOrderHeader.isRequest_disc()) {
            values.put(KEY_REQ_DISCOUNT, visitOrderHeader.isRequest_disc());
        }
        values.put(KEY_DESC_DISCOUNT, visitOrderHeader.getDescriptionDisc());
        values.put(KEY_TOTAL_AMOUNT, String.valueOf(visitOrderHeader.getTotalAmount()));
        values.put(KEY_STATUS, visitOrderHeader.getStatusString());
        values.put(KEY_STATUS_PRICE, visitOrderHeader.getStatusPrice());
        if (visitOrderHeader.getDeleted() != null) {
            if (visitOrderHeader.getDeleted()) {
                values.put(KEY_DELETED, "1");
            } else {
                values.put(KEY_DELETED, "0");
            }
        }
        if (visitOrderHeader.getHargaJadi() != null) {
            if (visitOrderHeader.getHargaJadi()) {
                values.put(KEY_HARGA_JADI, "1");
            } else {
                values.put(KEY_HARGA_JADI, "0");
            }
        }

        if (visitOrderHeader.getDate_mobile() != null) {
            values.put(KEY_DATE_FOR_SYNC, visitOrderHeader.getDate_mobile());
        }

        db.insert(TABLE_ORDER_HEADER, null, values);
        db.close();
    }

    public int updateOrderHeader(VisitOrderHeader visitOrderHeader) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SHIP_TO, visitOrderHeader.getShipTo());
        values.put(KEY_NO_PO, visitOrderHeader.getNoPo());
        values.put(KEY_TGL_PO, visitOrderHeader.getTglPoString());
        values.put(KEY_ED_PO, visitOrderHeader.getEdPoString());
        values.put(KEY_SALES_OFFICE, visitOrderHeader.getSalesOffice());
        values.put(KEY_ORDER_TYPE, visitOrderHeader.getOrderType());
        values.put(KEY_SIGNATURE, visitOrderHeader.getSignatureString());
        values.put(KEY_PHOTO, visitOrderHeader.getPhotoString());
        values.put(KEY_COMMENT, visitOrderHeader.getComment());
        values.put(KEY_REQ_DISCOUNT, visitOrderHeader.getRequestDisc());
        values.put(KEY_DESC_DISCOUNT, visitOrderHeader.getDescriptionDisc());
        values.put(KEY_STATUS_PRICE, visitOrderHeader.getStatusPrice());
        values.put(KEY_TOTAL_AMOUNT, String.valueOf(visitOrderHeader.getTotalAmount()));
        if (visitOrderHeader.getHargaJadi() != null) {
            if (visitOrderHeader.getHargaJadi()) {
                values.put(KEY_HARGA_JADI, "1");
            } else {
                values.put(KEY_HARGA_JADI, "0");
            }
        }

        // updating row
        return db.update(TABLE_ORDER_HEADER, values, KEY_ID_OUTLET + " = ? AND " + KEY_ID_TO + " = ? ",
                new String[]{String.valueOf((visitOrderHeader).getIdOutlet()), String.valueOf((visitOrderHeader).getId())});
    }

    public ArrayList<VisitOrderHeader> getAllListOrderHeader() {
        ArrayList<VisitOrderHeader> listVisitOrderHeader = new ArrayList<>();

        String selectQuery =
                "SELECT * FROM " + TABLE_ORDER_HEADER
                        + " WHERE (" + KEY_DELETED + " IS NULL OR " + KEY_DELETED + " != 'true')";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                VisitOrderHeader visitOrderHeader = new VisitOrderHeader();
                visitOrderHeader.setId(cursor.getString(0));
                visitOrderHeader.setIdOutlet(cursor.getString(1));
                visitOrderHeader.setSoldTo(cursor.getString(2));
                visitOrderHeader.setShipTo(cursor.getString(3));
                visitOrderHeader.setNoPo(cursor.getString(4));
                visitOrderHeader.setTglPoString(cursor.getString(5));
                visitOrderHeader.setEdPoString(cursor.getString(6));
                visitOrderHeader.setSalesOffice(cursor.getString(7));
                visitOrderHeader.setOrderType(cursor.getString(8));
                visitOrderHeader.setSignatureString(cursor.getString(9));
                visitOrderHeader.setPhotoString(cursor.getString(10));
                visitOrderHeader.setComment(cursor.getString(11));
                if (cursor.getString(12) != null) {
                    if (cursor.getString(12).equals("1")) {
                        visitOrderHeader.setRequest_disc(true);
                    } else {
                        visitOrderHeader.setRequest_disc(false);
                    }
                }
                visitOrderHeader.setDescriptionDisc(cursor.getString(13));
                if (cursor.getString(14) != null && !cursor.getString(14).equals(Constants.EMPTY_STRING) && !cursor.getString(14).equals(Constants.NULL)) {
                    visitOrderHeader.setTotalAmount(new BigDecimal(cursor.getString(14)));
                } else {
                    visitOrderHeader.setTotalAmount(BigDecimal.ZERO);
                }
                visitOrderHeader.setStatusString(cursor.getString(15));
                visitOrderHeader.setStatusPrice(cursor.getString(16));
                if (cursor.getString(19) != null) {
                    if (cursor.getString(19).equals("1")) {
                        visitOrderHeader.setHargaJadi(true);
                    } else {
                        visitOrderHeader.setHargaJadi(false);
                    }
                }
                if(cursor.getString(20) != null){
                    visitOrderHeader.setDate_mobile(cursor.getString(20));
                }
                listVisitOrderHeader.add(visitOrderHeader);
            } while (cursor.moveToNext());
        }

        return listVisitOrderHeader;
    }

    public ArrayList<VisitOrderHeader> getAllOrderHeader() {
        ArrayList<VisitOrderHeader> listVisitOrderHeader = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_ORDER_HEADER
                + " WHERE "
                + KEY_ID_TO + " NOT LIKE 'TO%' " +
                " AND (" + KEY_DELETED + " IS NULL OR " + KEY_DELETED + " != '1' )";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                VisitOrderHeader visitOrderHeader = new VisitOrderHeader();
                visitOrderHeader.setId(cursor.getString(0));
                visitOrderHeader.setIdOutlet(cursor.getString(1));
                visitOrderHeader.setSoldTo(cursor.getString(2));
                visitOrderHeader.setShipTo(cursor.getString(3));
                visitOrderHeader.setNoPo(cursor.getString(4));
                visitOrderHeader.setTglPoString(cursor.getString(5));
                visitOrderHeader.setEdPoString(cursor.getString(6));
                visitOrderHeader.setSalesOffice(cursor.getString(7));
                visitOrderHeader.setOrderType(cursor.getString(8));
                visitOrderHeader.setSignatureString(cursor.getString(9));
                visitOrderHeader.setPhotoString(cursor.getString(10));
                visitOrderHeader.setComment(cursor.getString(11));
                if (cursor.getString(12) != null) {
                    if (cursor.getString(12).equals("1")) {
                        visitOrderHeader.setRequest_disc(true);
                    } else {
                        visitOrderHeader.setRequest_disc(false);
                    }
                } else {
                    visitOrderHeader.setRequest_disc(false);
                }
                visitOrderHeader.setDescriptionDisc(cursor.getString(13));
                if (cursor.getString(14) != null && !cursor.getString(14).equals(Constants.EMPTY_STRING) && !cursor.getString(14).equals(Constants.NULL)) {
                    visitOrderHeader.setTotalAmount(new BigDecimal(cursor.getString(14)));
                } else {
                    visitOrderHeader.setTotalAmount(BigDecimal.ZERO);
                }
                visitOrderHeader.setStatusString(cursor.getString(15));
                visitOrderHeader.setStatusPrice(cursor.getString(16));
                if (cursor.getString(19) != null) {
                    if (cursor.getString(19).equals("1")) {
                        visitOrderHeader.setHargaJadi(true);
                    } else {
                        visitOrderHeader.setHargaJadi(false);
                    }
                }
                if (cursor.getString(20) != null) {
                    if (cursor.getString(20) != null) {
                        visitOrderHeader.setDate_mobile(cursor.getString(20));
                    }
                }
                listVisitOrderHeader.add(visitOrderHeader);
            } while (cursor.moveToNext());
        }

        return listVisitOrderHeader;
    }

    public ArrayList<VisitOrderHeader> getAllListOrderHeaderByIdOutlet(String idOutlet) {
        ArrayList<VisitOrderHeader> listVisitOrderHeader = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_ORDER_HEADER
                + " WHERE " + KEY_ID_OUTLET + " = ? AND "
                + KEY_ID_TO + " NOT LIKE 'TO%' " +
                " AND (" + KEY_DELETED + " IS NULL OR " + KEY_DELETED + " != '1' )";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        if (cursor.moveToFirst()) {
            do {
                VisitOrderHeader visitOrderHeader = new VisitOrderHeader();
                visitOrderHeader.setId(cursor.getString(0));
                visitOrderHeader.setIdOutlet(cursor.getString(1));
                visitOrderHeader.setSoldTo(cursor.getString(2));
                visitOrderHeader.setShipTo(cursor.getString(3));
                visitOrderHeader.setNoPo(cursor.getString(4));
                visitOrderHeader.setTglPoString(cursor.getString(5));
                visitOrderHeader.setEdPoString(cursor.getString(6));
                visitOrderHeader.setSalesOffice(cursor.getString(7));
                visitOrderHeader.setOrderType(cursor.getString(8));
                visitOrderHeader.setSignatureString(cursor.getString(9));
                visitOrderHeader.setPhotoString(cursor.getString(10));
                visitOrderHeader.setComment(cursor.getString(11));
                if (cursor.getString(12).equals("1")) {
                    visitOrderHeader.setRequest_disc(true);
                } else {
                    visitOrderHeader.setRequest_disc(false);
                }
                visitOrderHeader.setDescriptionDisc(cursor.getString(13));
                if (cursor.getString(14) != null && !cursor.getString(14).equals(Constants.EMPTY_STRING) && !cursor.getString(14).equals(Constants.NULL)) {
                    visitOrderHeader.setTotalAmount(new BigDecimal(cursor.getString(14)));
                } else {
                    visitOrderHeader.setTotalAmount(BigDecimal.ZERO);
                }
                visitOrderHeader.setStatusString(cursor.getString(15));
                visitOrderHeader.setStatusPrice(cursor.getString(16));
                if (cursor.getString(19) != null) {
                    if (cursor.getString(19).equals("1")) {
                        visitOrderHeader.setHargaJadi(true);
                    } else {
                        visitOrderHeader.setHargaJadi(false);
                    }
                }
                listVisitOrderHeader.add(visitOrderHeader);
            } while (cursor.moveToNext());
        }

        return listVisitOrderHeader;
    }

    public VisitOrderHeader getOrderHeaderById(String idTo) {
        VisitOrderHeader header = new VisitOrderHeader();

        String selectQuery = "SELECT * FROM " + TABLE_ORDER_HEADER
                + " WHERE " + KEY_ID_TO + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idTo});

        if (cursor.moveToFirst()) {
            do {
                header.setId(cursor.getString(0));
                header.setIdOutlet(cursor.getString(1));
                header.setSoldTo(cursor.getString(2));
                header.setShipTo(cursor.getString(3));
                header.setNoPo(cursor.getString(4));
                header.setTglPoString(cursor.getString(5));
                header.setEdPoString(cursor.getString(6));
                header.setSalesOffice(cursor.getString(7));
                header.setOrderType(cursor.getString(8));
                header.setSignatureString(cursor.getString(9));
                header.setPhotoString(cursor.getString(10));
                if (cursor.getString(11) != null) {
                    header.setComment(cursor.getString(11));
                }
                if (cursor.getString(12) != null) {
                    if (cursor.getString(12).equals("1")) {
                        header.setRequest_disc(true);
                    } else {
                        header.setRequest_disc(false);
                    }
                }

                header.setDescriptionDisc(cursor.getString(13));
                if (cursor.getString(14) != null && !cursor.getString(14).equals(Constants.EMPTY_STRING) && !cursor.getString(14).equals(Constants.NULL)) {
                    header.setTotalAmount(new BigDecimal(cursor.getString(14)));
                } else {
                    header.setTotalAmount(BigDecimal.ZERO);
                }
                header.setStatusString(cursor.getString(15));
                header.setStatusPrice(cursor.getString(16));
                if (cursor.getString(19) != null) {
                    if (cursor.getString(19).equals("1")) {
                        header.setHargaJadi(true);
                    } else {
                        header.setHargaJadi(false);
                    }
                }
            } while (cursor.moveToNext());
        }

        return header;
    }

    public ArrayList<VisitOrderHeader> getListOrderHeader(String idOutlet, String orderType) {
        ArrayList<VisitOrderHeader> listVisitOrderHeader = new ArrayList<>();
        String selectQuery;
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();

        if (orderType.equals(Constants.ALL_TYPE)) {
            selectQuery = "SELECT * FROM " + TABLE_ORDER_HEADER + " WHERE " + KEY_ID_OUTLET + " = ?" +
                    " AND (" + KEY_DELETED + " IS NULL OR " + KEY_DELETED + " != '1' )";
            cursor = db.rawQuery(selectQuery, new String[]{idOutlet});
        } else {
            selectQuery = "SELECT * FROM " + TABLE_ORDER_HEADER + " WHERE " + KEY_ID_OUTLET + " = ?" + " AND " + KEY_ORDER_TYPE + " = ?" +
                    " AND (" + KEY_DELETED + " IS NULL OR " + KEY_DELETED + " != '1' )";
            cursor = db.rawQuery(selectQuery, new String[]{idOutlet, orderType});
        }


        if (cursor.moveToFirst()) {
            do {
                VisitOrderHeader visitOrderHeader = new VisitOrderHeader();
                visitOrderHeader.setId(cursor.getString(0));
                visitOrderHeader.setIdOutlet(cursor.getString(1));
                visitOrderHeader.setSoldTo(cursor.getString(2));
                visitOrderHeader.setShipTo(cursor.getString(3));
                visitOrderHeader.setNoPo(cursor.getString(4));
                visitOrderHeader.setTglPoString(cursor.getString(5));
                visitOrderHeader.setEdPoString(cursor.getString(6));
                visitOrderHeader.setSalesOffice(cursor.getString(7));
                visitOrderHeader.setOrderType(cursor.getString(8));
                visitOrderHeader.setSignatureString(cursor.getString(9));
                visitOrderHeader.setPhotoString(cursor.getString(10));
                visitOrderHeader.setComment(cursor.getString(11));
                if (cursor.getString(12) != null) {
                    if (cursor.getString(12).equals("1")) {
                        visitOrderHeader.setRequest_disc(true);
                    } else {
                        visitOrderHeader.setRequest_disc(false);
                    }
                }
                visitOrderHeader.setDescriptionDisc(cursor.getString(13));
                if (cursor.getString(14) != null && !cursor.getString(14).equals(Constants.EMPTY_STRING) && !cursor.getString(14).equals(Constants.NULL)) {
                    visitOrderHeader.setTotalAmount(new BigDecimal(cursor.getString(14)));
                } else {
                    visitOrderHeader.setTotalAmount(BigDecimal.ZERO);
                }
                visitOrderHeader.setStatusString(cursor.getString(15));
                visitOrderHeader.setHargaJadi(Boolean.valueOf(cursor.getString(19)));
                listVisitOrderHeader.add(visitOrderHeader);
            } while (cursor.moveToNext());
        }

        return listVisitOrderHeader;
    }

    public void deleteToHeaderTypeC() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_HEADER + " WHERE " + KEY_ORDER_TYPE + " = 'C'";
        db.execSQL(delete);
        db.close();
    }


    public void deleteToHeaderTypeT() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_HEADER + " WHERE " + KEY_ORDER_TYPE + " = 'T'";
        db.execSQL(delete);
        db.close();
    }

    public void deleteOrderHeader() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_HEADER;
        db.execSQL(delete);
        db.close();
    }

    /*-----------------------------------------OrderDetail----------------------------------------*/
    public void addOrderDetail(String id, VisitOrderDetailResponse materialResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_TO, id);
        values.put(KEY_ID_PRICE, materialResponse.getId_price());
        values.put(KEY_ID_MATERIAL, materialResponse.getIdMaterial());
        values.put(KEY_QTY_1, String.valueOf(materialResponse.getQty1()));
        values.put(KEY_UOM_1, materialResponse.getUom1());
        values.put(KEY_QTY_2, String.valueOf(materialResponse.getQty2()));
        values.put(KEY_UOM_2, materialResponse.getUom2());
        if (materialResponse.getPrice() != null) {
            values.put(KEY_PRICE, String.valueOf(materialResponse.getPrice()));
        }
        values.put(KEY_TOP, materialResponse.getTop());
        values.put(KEY_TOP_SAP, materialResponse.getTop_sap());
        values.put(KEY_JENIS_JUAL, materialResponse.getJenisJual());
        db.insert(TABLE_ORDER_DETAIL, null, values);
        db.close();
    }

    public void addOrderDetail(VisitOrderDetailResponse materialResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_TO, materialResponse.getIdTo());
        values.put(KEY_ID_TO_PRICE, materialResponse.getId_price());
        values.put(KEY_ID_PRICE, materialResponse.getId_price());
        values.put(KEY_ID_MATERIAL, materialResponse.getIdMaterial());
        values.put(KEY_QTY_1, String.valueOf(materialResponse.getQty1()));
        values.put(KEY_UOM_1, materialResponse.getUom1());
        values.put(KEY_QTY_2, String.valueOf(materialResponse.getQty2()));
        values.put(KEY_UOM_2, materialResponse.getUom2());
        if (materialResponse.getPrice() != null) {
            values.put(KEY_PRICE, String.valueOf(materialResponse.getPrice()));
        }
        values.put(KEY_TOP, materialResponse.getTop());

        if (materialResponse.getJenisJual() != null) {
            values.put(KEY_JENIS_JUAL, materialResponse.getJenisJual());
        }
        if (materialResponse.getTop_sap() != null) {
            values.put(KEY_TOP_SAP, materialResponse.getTop_sap());
        }

        db.insert(TABLE_ORDER_DETAIL, null, values);
        db.close();
    }

    public int updateOrderDetail(String idTO, VisitOrderDetailResponse materialResponse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QTY_1, String.valueOf(materialResponse.getQty1()));
        values.put(KEY_UOM_1, materialResponse.getUom1());
        values.put(KEY_QTY_2, String.valueOf(materialResponse.getQty2()));
        values.put(KEY_UOM_2, materialResponse.getUom2());
        values.put(KEY_PRICE, String.valueOf(materialResponse.getPrice()));
        values.put(KEY_TOP, materialResponse.getTop());
        values.put(KEY_TOP_SAP, materialResponse.getTop_sap());
        values.put(KEY_JENIS_JUAL, materialResponse.getJenisJual());

        // updating row
        return db.update(TABLE_ORDER_DETAIL, values, KEY_ID_TO + " = ? AND " + KEY_ID_MATERIAL + " = ? ",
                new String[]{String.valueOf(idTO), String.valueOf(materialResponse.getIdMaterial())});
    }

    public ArrayList<VisitOrderDetailResponse> getListOrderDetail(String idTO) {
        ArrayList<VisitOrderDetailResponse> listVisitOrderHeader = new ArrayList<>();

//        String selectQuery = "SELECT * FROM " + TABLE_ORDER_DETAIL + " WHERE " + KEY_ID_TO + " = ?";
        String selectQuery = "SELECT " +
                KEY_ID_TO + "," +
                KEY_ID_PRICE + "," +
                KEY_ID_MATERIAL + "," +
                KEY_QTY_1 + "," +
                KEY_UOM_1 + "," +
                KEY_QTY_2 + "," +
                KEY_UOM_2 + "," +
                KEY_TOP + "," +
                KEY_PRICE + "," +
                KEY_TOP_SAP + "," +
                KEY_JENIS_JUAL +
                " FROM " + TABLE_ORDER_DETAIL +
                " WHERE " + KEY_ID_TO + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idTO});

        if (cursor.moveToFirst()) {
            do {
                VisitOrderDetailResponse materialResponse = new VisitOrderDetailResponse();
                materialResponse.setIdToPrice(cursor.getString(0));
                materialResponse.setId_price(cursor.getString(1));
                materialResponse.setIdMaterial(cursor.getString(2));
                materialResponse.setQty1(new BigDecimal(cursor.getString(3)));
                materialResponse.setUom1(cursor.getString(4));
                if (cursor.getString(5) != null && !cursor.getString(5).equals("null")) {
                    materialResponse.setQty2(new BigDecimal(cursor.getString(5)));
                    materialResponse.setUom2(cursor.getString(6));
                }
                materialResponse.setTop(cursor.getString(7));
                if (cursor.getString(8) != null && !cursor.getString(8).equals(Constants.NULL)) {
                    materialResponse.setPrice(new BigDecimal(cursor.getString(8)));
                }
                if (cursor.getString(9) != null) {
                    materialResponse.setTop_sap(cursor.getString(9));
                }
                if (cursor.getString(10) != null) {
                    materialResponse.setJenisJual(cursor.getString(10));
                }
                listVisitOrderHeader.add(materialResponse);
            } while (cursor.moveToNext());
        }

        return listVisitOrderHeader;
    }

    public void deleteOrderHeaderBy(String idTO) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDER_HEADER, KEY_ID_TO + " = ?",
                new String[]{idTO});
        db.close();
    }

    public void deleteOrderDetailBy(String idTO) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDER_DETAIL, KEY_ID_TO + " = ?",
                new String[]{idTO});
        db.close();
    }

    public void deleteOrderDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_DETAIL;
        db.execSQL(delete);
        db.close();
    }

    /*---------------------------------------ReturnHeader---------------------------------------*/
    public void addReturnHeader(ReturnRequest returnRequest) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_HEADER_RETURN, returnRequest.getIdHeader());
        values.put(KEY_ID_OUTLET, returnRequest.getIdOutlet());
        values.put(KEY_NO_BRB, returnRequest.getNoBrb());
        values.put(KEY_TGL_BRB, returnRequest.getTanggalBrbString());
        values.put(KEY_NO_RETURN_REPORT, returnRequest.getNoRr());
        values.put(KEY_TGL_RR, returnRequest.getTanggalRrString());
        values.put(KEY_SYNC_STATUS, returnRequest.getSyncStatus());
        values.put(KEY_DATE_FOR_SYNC, returnRequest.getDate_mobile());

        db.insert(TABLE_RETURN_HEADER, null, values);
        db.close();
    }

    public void addReturnHeader(ReturnResponse returns) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_HEADER_RETURN, returns.getId());
        values.put(KEY_ID_OUTLET, returns.getIdOutlet());
        values.put(KEY_NO_BRB, returns.getNoBrb());
        values.put(KEY_TGL_BRB, returns.getTanggalBrb());
        values.put(KEY_NO_RETURN_REPORT, returns.getNoRr());
        values.put(KEY_TGL_RR, returns.getTanggalRr());
        values.put(KEY_SYNC_STATUS, returns.getStatusSync());
        if (returns.isDeleted()) {
            values.put(KEY_DELETED, "1");
        } else {
            values.put(KEY_DELETED, "0");
        }
        values.put(KEY_DATE_FOR_SYNC, returns.getDate_mobile());

        db.insert(TABLE_RETURN_HEADER, null, values);
        db.close();
    }


    public int updateReturnHeader(ReturnResponse data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_HEADER_RETURN, data.getId());

        // updating row
        return db.update(TABLE_RETURN_HEADER, values, KEY_ID_OUTLET + " = ? AND " +
                        KEY_NO_BRB + " = ? ",
                new String[]{String.valueOf((data).getIdOutlet()),
                        String.valueOf((data).getNoBrb())});
    }


    public int updateOrderHeaderId(VisitOrderHeader data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_TO, data.getId());

        // updating row
        return db.update(TABLE_ORDER_HEADER, values,
                KEY_ID_OUTLET + " = ? AND " +
                        KEY_ID_TO + " = ?",
                new String[]{(data).getIdOutlet(), (data).getId_mobile()});
    }

    public int updateOrderDetailId(VisitOrderHeader data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_TO, data.getId());

        // updating row
        return db.update(TABLE_ORDER_DETAIL, values,
                KEY_ID_TO + " = ?",
                new String[]{(data).getId_mobile()});
    }

    public ArrayList<String> getListBRB() {
        ArrayList<String> listBrb = new ArrayList<String>();

        String selectQuery = "SELECT " + KEY_NO_BRB + " FROM " + TABLE_RETURN_HEADER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                listBrb.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return listBrb;
    }


    public ArrayList<ReturnRequest> getAllListReturnHeader(String idOutlet) {
        ArrayList<ReturnRequest> listReturnHeader = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_RETURN_HEADER
                + " WHERE " + KEY_ID_OUTLET + " = ?"
                + " AND (" + KEY_DELETED + " IS NULL OR " + KEY_DELETED + " != '1' )";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        if (cursor.moveToFirst()) {
            do {
                ReturnRequest rt = new ReturnRequest();
                rt.setIdHeader(cursor.getString(0));
                rt.setIdOutlet(cursor.getString(1));
                rt.setNoBrb(cursor.getString(2));
                rt.setTanggalBrb(cursor.getString(3));
                rt.setNoRr(cursor.getString(4));
                rt.setTanggalRr(cursor.getString(5));
                rt.setSyncStatus(cursor.getString(6));
                listReturnHeader.add(rt);
            } while (cursor.moveToNext());
        }

        return listReturnHeader;
    }

    public ArrayList<ReturnRequest> getAllListReturnHeader() {
        ArrayList<ReturnRequest> listReturnHeader = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_RETURN_HEADER
                + " WHERE (" + KEY_DELETED + " IS NULL OR " + KEY_DELETED + " != '1' )";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                ReturnRequest rt = new ReturnRequest();
                rt.setIdHeader(cursor.getString(0));
                rt.setIdOutlet(cursor.getString(1));
                rt.setNoBrb(cursor.getString(2));
                rt.setTanggalBrb(cursor.getString(3));
                rt.setNoRr(cursor.getString(4));
                rt.setTanggalRr(cursor.getString(5));
                rt.setSyncStatus(cursor.getString(6));
                rt.setDate_mobile(cursor.getString(9));
                listReturnHeader.add(rt);
            } while (cursor.moveToNext());
        }

        return listReturnHeader;
    }

    public ArrayList<ReturnResponse> getAllListReturnHeaders(String idOutlet) {
        ArrayList<ReturnResponse> listReturnHeader = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_RETURN_HEADER
                + " WHERE " + KEY_ID_OUTLET + " = ? "
                + " AND (" + KEY_DELETED + " IS NULL OR " + KEY_DELETED + " != '1' )";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        if (cursor.moveToFirst()) {
            do {
                ReturnResponse rt = new ReturnResponse();

                if (cursor.getString(0) != null) {
                    rt.setId(cursor.getString(0));
                }

                if (cursor.getString(2) != null) {
                    rt.setNoBrb(cursor.getString(2));
                }

                if (cursor.getString(3) != null) {
                    rt.setTanggalBrb(cursor.getString(3));
                }

                if (cursor.getString(4) != null) {
                    rt.setNoRr(cursor.getString(4));
                }

                if (cursor.getString(5) != null) {
                    rt.setTanggalRr(cursor.getString(5));
                }
                if (cursor.getString(6) != null) {
                    rt.setStatusSync(cursor.getString(6));
                }
                listReturnHeader.add(rt);
            } while (cursor.moveToNext());
        }

        return listReturnHeader;
    }

    public int updateStatusSyncReturn(String idBrb, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SYNC_STATUS, status);

        return db.update(TABLE_RETURN_HEADER, values, KEY_NO_BRB + " = ?",
                new String[]{String.valueOf((idBrb))});
    }

    public void deleteReturnHeader() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_RETURN_HEADER;
        db.execSQL(delete);
        db.close();
    }

    /*---------------------------------------ReturnDetail---------------------------------------*/
    public void addReturnDetail(Return rt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_HEADER_RETURN, rt.getIdReturn());
        values.put(KEY_ID_MATERIAL, rt.getMaterialId());
//        if (rt.getQty1() != null && !rt.getQty1().equals(BigDecimal.ZERO)) {
//            values.put(KEY_QTY_1, String.valueOf(rt.getQty1()));
//            values.put(KEY_UOM_1, rt.getUom1());
//        }
//        if (rt.getQty2() != null && !rt.getQty2().equals(BigDecimal.ZERO)) {
//            values.put(KEY_QTY_2, String.valueOf(rt.getQty2()));
//            values.put(KEY_UOM_2, rt.getUom2());
//        }
        if (rt.getExpiredDate() != null) {
            values.put(KEY_EXPIRED_DATE, rt.getExpiredDate());
        } else if (rt.getExpiredDateString() != null) {
            values.put(KEY_EXPIRED_DATE, rt.getExpiredDateString());
        }
        values.put(KEY_BATCH, rt.getBatch());
        values.put(KEY_REASON, rt.getReason());
        values.put(KEY_DESCRIPTION, rt.getDescription());
        values.put(KEY_CATEGORY, rt.getCategory());

        db.insert(TABLE_RETURN_DETAIL, null, values);
        db.close();
    }

    public ArrayList<Return> getListReturnDetail(String idReturn) {
        ArrayList<Return> listReturnDetail = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_RETURN_DETAIL + " WHERE " + KEY_ID_HEADER_RETURN + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idReturn});

        if (cursor.moveToFirst()) {
            do {
                Return rt = new Return();
                rt.setIdReturn(cursor.getString(0));
                rt.setMaterialId(cursor.getString(1));
//                if (cursor.getString(2) != null) {
//                    rt.setQty1(new BigDecimal(cursor.getString(2)));
//                }
                rt.setUom1(cursor.getString(3));
                if (cursor.getString(4) != null && !cursor.getString(4).equals("null")) {
                    rt.setQty2(new BigDecimal(cursor.getString(4)));
                }
                rt.setUom2(cursor.getString(5));
                rt.setExpiredDate(cursor.getString(6));
                rt.setBatch(cursor.getString(7));
                rt.setReason(cursor.getString(8));
                rt.setDescription(cursor.getString(9));
                rt.setCategory(cursor.getString(10));
                listReturnDetail.add(rt);
            } while (cursor.moveToNext());
        }

        return listReturnDetail;
    }

    public void deleteReturnDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_RETURN_DETAIL;
        db.execSQL(delete);
        db.close();
    }

    /*---------------------------------------TOP---------------------------------------*/
    public void addTOP(JenisJualandTop top) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_TOP, top.getId());
        values.put(KEY_ID_OUTLET, top.getIdOutlet());
        values.put(KEY_KLASIFIKASI, top.getKlasifikasi());
        values.put(KEY_ID_JENIS_JUAL, top.getJenisJual());
        values.put(KEY_TOP_SAP, top.getTop_sap());
        values.put(KEY_TOP_F, top.getTopf());
        values.put(KEY_TOP_K, top.getTopk());

        db.insert(TABLE_TOP, null, values);
        db.close();
    }

    public void addTOPN(List<JenisJualandTop> listTop) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_TOP, CREATE_TABLE_TOP_N);
        deleteTable(db, TABLE_TOP);
        String selectQuery = "INSERT INTO " + TABLE_TOP + "(" + KEY_ID_TOP
                + "," + KEY_ID_OUTLET + "," + KEY_KLASIFIKASI + "," + KEY_ID_JENIS_JUAL
                + "," + KEY_TOP_SAP + "," + KEY_TOP_F + "," + KEY_TOP_K + ")" + "VALUES ";
        int count = 0;
        if (listTop != null && !listTop.isEmpty()) {
            for (int i = 0; i < listTop.size(); i++) {
                String id = null, idOutlet = null, idMatClass = null, jenisJual = null, topSap = null;
                BigDecimal stockQty1 = BigDecimal.ZERO, stockQty2 = BigDecimal.ZERO, stockQty1C = BigDecimal.ZERO, stockQty2C = BigDecimal.ZERO;
                int topF = 0, topK = 0;

                if (listTop.get(i).getId() != null)
                    id = DatabaseUtils.sqlEscapeString(listTop.get(i)
                            .getId());
                if (listTop.get(i).getIdOutlet() != null)
                    idOutlet = DatabaseUtils.sqlEscapeString(listTop.get(i)
                            .getIdOutlet());
                if (listTop.get(i).getKlasifikasi() != null)
                    idMatClass = DatabaseUtils.sqlEscapeString(listTop.get(i)
                            .getKlasifikasi());
                if (listTop.get(i).getJenisJual() != null)
                    jenisJual = DatabaseUtils.sqlEscapeString(listTop.get(i)
                            .getJenisJual());
                if (listTop.get(i).getTop_sap() != null)
                    topSap = DatabaseUtils.sqlEscapeString(listTop.get(i)
                            .getTop_sap());

                if (listTop.get(i).getTopf() > 0) {
                    topF = listTop.get(i).getTopf();
                }
                if (listTop.get(i).getTopk() > 0) {
                    topK = listTop.get(i).getTopk();
                }


                selectQuery = selectQuery.concat("("
                        + id + "," + idOutlet + "," + idMatClass + ","
                        + jenisJual + "," + topSap + "," + topF + "," + topK
                        + ")");
                count++;
                if (count != 495) {
                    if (i != listTop.size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_TOP + "(" + KEY_ID_TOP
                            + "," + KEY_ID_OUTLET + "," + KEY_KLASIFIKASI + "," + KEY_ID_JENIS_JUAL
                            + "," + KEY_TOP_SAP + "," + KEY_TOP_F + "," + KEY_TOP_K + ")" + "VALUES ";
                }
            }
        }
        db.close();
    }


    public ArrayList<JenisJualandTop> getListTOP(String idOutlet) {
        ArrayList<JenisJualandTop> jenisJualandTopArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_TOP + " WHERE " + KEY_ID_OUTLET + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        if (cursor.moveToFirst()) {
            do {
                JenisJualandTop jenisJualandTop = new JenisJualandTop();
                jenisJualandTop.setKlasifikasi(cursor.getString(1));
                jenisJualandTop.setJenisJual(cursor.getString(2));
                jenisJualandTop.setTop_sap(cursor.getString(3));
                jenisJualandTop.setTopf(cursor.getInt(4));
                jenisJualandTop.setTopk(cursor.getInt(5));
                jenisJualandTopArrayList.add(jenisJualandTop);
            } while (cursor.moveToNext());
        }

        return jenisJualandTopArrayList;
    }


    public void deleteTOP() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_TOP;
        db.execSQL(delete);
        db.close();
    }

    /*---------------------------------------JenisJual---------------------------------------*/
    public void addJenisJual(JenisJualandTop jenisJual) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_JENIS_JUAL, jenisJual.getJenisJual());
        values.put(KEY_NAMA_JENIS_JUAL, jenisJual.getJenisJualName());

        db.insert(TABLE_JENIS_JUAL, null, values);
        db.close();
    }

    public ArrayList<JenisJualandTop> getJenisJual(String idOutlet, String idMaterial, String orderType) {
        ArrayList<JenisJualandTop> jenisJualandTopArrayList = new ArrayList<JenisJualandTop>();
        String selectQuery;
        if (orderType.equalsIgnoreCase(Constants.ORDER_CANVAS_TYPE)) {
            selectQuery = "SELECT a." + KEY_ID_JENIS_JUAL + ", a." + KEY_TOP_SAP + ", a." + KEY_TOP_F + ",  b." + KEY_NAMA_JENIS_JUAL + ", b." + KEY_ID_JENIS_JUAL + ", a." + KEY_ID_TOP + " FROM " + TABLE_TOP + " AS a "
                    + " INNER JOIN " + TABLE_JENIS_JUAL + " AS b ON a." + KEY_ID_JENIS_JUAL + " = " + " b." + KEY_ID_JENIS_JUAL
                    + " INNER JOIN " + TABLE_MATERIAL + " AS c ON a." + KEY_KLASIFIKASI + " = " + " c." + KEY_ID_MAT_CLASS
                    + " WHERE a." + KEY_ID_OUTLET + " = ? " + " AND "
                    + "c." + KEY_ID_MATERIAL + " = ? AND "
                    + "a." + KEY_ID_JENIS_JUAL + " = 'ZB07'";
        } else {
            selectQuery = "SELECT a." + KEY_ID_JENIS_JUAL + ", a." + KEY_TOP_SAP + ", a." + KEY_TOP_F + ",  b." + KEY_NAMA_JENIS_JUAL + ", b." + KEY_ID_JENIS_JUAL + ", a." + KEY_ID_TOP + " FROM " + TABLE_TOP + " AS a "
                    + " INNER JOIN " + TABLE_JENIS_JUAL + " AS b ON a." + KEY_ID_JENIS_JUAL + " = " + " b." + KEY_ID_JENIS_JUAL
                    + " INNER JOIN " + TABLE_MATERIAL + " AS c ON a." + KEY_KLASIFIKASI + " = " + " c." + KEY_ID_MAT_CLASS
                    + " WHERE a." + KEY_ID_OUTLET + " = ? " + " AND "
                    + "c." + KEY_ID_MATERIAL + " = ? AND "
                    + "a." + KEY_ID_JENIS_JUAL + " != 'ZB07'";
        }
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, idMaterial});

        if (cursor.moveToFirst()) {
            do {
                JenisJualandTop jenisJualandTop = new JenisJualandTop();
                jenisJualandTop.setJenisJual(cursor.getString(4));
                jenisJualandTop.setJenisJualName(cursor.getString(3));
                jenisJualandTop.setTop_sap(cursor.getString(1));
                jenisJualandTop.setTopf(cursor.getInt(2));
                jenisJualandTop.setId(cursor.getString(5));
                jenisJualandTopArrayList.add(jenisJualandTop);
            } while (cursor.moveToNext());
        }

        return jenisJualandTopArrayList;
    }

    public void deleteJenisJual() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_JENIS_JUAL;
        db.execSQL(delete);
        db.close();
    }

    /*---------------------------------------SalesOffice---------------------------------------*/
    public void addSalesOffice(SalesOffice salesOffice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_SALES_OFFICE, salesOffice.getId());
        values.put(KEY_SALES_OFFICE_NAME, salesOffice.getName());
        values.put(KEY_ID_PLANT, salesOffice.getIdArea());

        db.insert(TABLE_SALES_OFFICE, null, values);
        db.close();
    }

    public void addSalesOfficeN(List<SalesOffice> list) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_SALES_OFFICE, CREATE_TABLE_SALES_OFFICE_N);
        deleteTable(db, TABLE_SALES_OFFICE);
        String selectQuery = "INSERT INTO " + TABLE_SALES_OFFICE + "(" + KEY_ID_SALES_OFFICE
                + "," + KEY_SALES_OFFICE_NAME + ")" + "VALUES ";
        int count = 0;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String idSalesOffice = null, salesOfficeName = null;

                if (list.get(i).getId() != null)
                    idSalesOffice = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getId());
                if (list.get(i).getName() != null)
                    salesOfficeName = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getName());
//                if (list.get(i).getIdArea() != null)
//                    idPlant = DatabaseUtils.sqlEscapeString(list.get(i)
//                            .getIdArea());

                selectQuery = selectQuery.concat("("
                        + idSalesOffice + "," + salesOfficeName + ")");
                count++;
                if (count != 495) {
                    if (i != list.size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_SALES_OFFICE + "(" + KEY_ID_SALES_OFFICE
                            + "," + KEY_SALES_OFFICE_NAME + ")" + "VALUES ";
                }
            }
        }
        db.close();
    }

    public ArrayList<SalesOffice> getAllSalesOffice() {
        ArrayList<SalesOffice> salesOfficeArrayLists = new ArrayList<SalesOffice>();

        String selectQuery = "SELECT * FROM " + TABLE_SALES_OFFICE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SalesOffice salesOffice = new SalesOffice();
                salesOffice.setId(cursor.getString(0));
                salesOffice.setName(cursor.getString(1));
                salesOfficeArrayLists.add(salesOffice);
            } while (cursor.moveToNext());
        }

        return salesOfficeArrayLists;
    }

    public ArrayList<SalesOffice> getSalesOfficeByIdPlant(String id) {
        ArrayList<SalesOffice> salesOfficeArrayLists = new ArrayList<SalesOffice>();

        String selectQuery = "SELECT * FROM " + TABLE_SALES_OFFICE + " WHERE " + KEY_ID_SALES_OFFICE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor.moveToFirst()) {
            do {
                SalesOffice salesOffice = new SalesOffice();
                salesOffice.setId(cursor.getString(0));
                salesOffice.setName(cursor.getString(1));
                salesOfficeArrayLists.add(salesOffice);
            } while (cursor.moveToNext());
        }

        return salesOfficeArrayLists;
    }

    public SalesOffice getSalesOfficeByIdSalesOffice(String id) {
        String selectQuery = "SELECT * FROM " + TABLE_SALES_OFFICE + " WHERE " + KEY_ID_SALES_OFFICE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        SalesOffice salesOffice = new SalesOffice();
        if (cursor.moveToFirst()) {
            salesOffice.setId(cursor.getString(0));
            salesOffice.setName(cursor.getString(1));
        }

        return salesOffice;
    }

    public String getSalesOfficeName(String id) {

        String selectQuery = "SELECT * FROM " + TABLE_SALES_OFFICE +
                " WHERE " + KEY_ID_SALES_OFFICE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        SalesOffice salesOffice = new SalesOffice();

        if (cursor.moveToFirst()) {
            salesOffice.setName(cursor.getString(1));
            return salesOffice.getName();
        } else {
            salesOffice = null;
        }
        return salesOffice.getName();
    }

    public String getJenisJualByTop(String top) {

        String selectQuery = "SELECT " + KEY_ID_JENIS_JUAL + " FROM " + TABLE_TOP +
                " WHERE " + KEY_TOP_SAP + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{top});

        String result = Constants.EMPTY_STRING;

        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
            return result;
        } else {
            result = Constants.EMPTY_STRING;
        }
        return result;
    }

    public void deleteSalesOffice() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_SALES_OFFICE;
        db.execSQL(delete);
        db.close();
    }

    /*-=====================================NATALIA=============================================-*/
    //    Material (Insert n Delete All)
    public void addMaterial(MaterialResponse materialResponse) {
        SQLiteDatabase db = this.getWritableDatabase();
        BigDecimal qty1, qty2, qty1C, qty2C;
        if (materialResponse.getStockQty1() != null) {
            qty1 = materialResponse.getStockQty1();
        } else {
            qty1 = BigDecimal.ZERO;
        }

        if (materialResponse.getStockQty2() != null) {
            qty2 = materialResponse.getStockQty2();
        } else {
            qty2 = BigDecimal.ZERO;
        }
        if (materialResponse.getStockQty1C() != null) {
            qty1C = materialResponse.getStockQty1C();
        } else {
            qty1C = BigDecimal.ZERO;
        }

        if (materialResponse.getStockQty2C() != null) {
            qty2C = materialResponse.getStockQty2C();
        } else {
            qty2C = BigDecimal.ZERO;
        }

        ContentValues values = new ContentValues();
        values.put(KEY_ID_MATERIAL, materialResponse.getIdMaterial());
        values.put(KEY_MATERIAL_NAME, materialResponse.getMaterialName());
        values.put(KEY_ID_MAT_CLASS, materialResponse.getIdMaterialClass());
        values.put(KEY_STOCK_QTY_1, String.valueOf(qty1));
        values.put(KEY_STOCK_UOM_1, materialResponse.getStockUom1());
        values.put(KEY_STOCK_QTY_2, String.valueOf(qty2));
        values.put(KEY_STOCK_UOM_2, materialResponse.getStockUom2());
        values.put(KEY_STOCK_QTY_1_C, String.valueOf(qty1C));
        values.put(KEY_STOCK_UOM_1_C, materialResponse.getStockUom1C());
        values.put(KEY_STOCK_QTY_2_C, String.valueOf(qty2C));
        values.put(KEY_STOCK_UOM_2_C, materialResponse.getStockUom2C());

        db.insert(TABLE_MATERIAL, null, values);
        db.close();
    }

    public void addMaterialN(List<MaterialResponse> materialList) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_MATERIAL, CREATE_TABLE_MATERIAL);
        deleteTable(db, TABLE_MATERIAL);
        String selectQuery = "INSERT INTO " + TABLE_MATERIAL + "(" + KEY_ID_MATERIAL
                + "," + KEY_MATERIAL_NAME + "," + KEY_ID_MAT_CLASS + "," + KEY_STOCK_QTY_1
                + "," + KEY_STOCK_UOM_1 + "," + KEY_STOCK_QTY_2 + "," + KEY_STOCK_UOM_2
                + "," + KEY_STOCK_QTY_1_C + "," + KEY_STOCK_UOM_1_C + "," + KEY_STOCK_QTY_2_C
                + "," + KEY_STOCK_UOM_2_C + ")" + "VALUES ";
        int count = 0;
        if (materialList != null && !materialList.isEmpty()) {
            for (int i = 0; i < materialList.size(); i++) {
                String idMat = null, matName = null, idMatClass = null, stockUom1 = null, stockUom2 = null, stockUom1C = null, stockUom2C = null;
                BigDecimal stockQty1 = BigDecimal.ZERO, stockQty2 = BigDecimal.ZERO, stockQty1C = BigDecimal.ZERO, stockQty2C = BigDecimal.ZERO;

                if (materialList.get(i).getIdMaterial() != null)
                    idMat = DatabaseUtils.sqlEscapeString(materialList.get(i)
                            .getIdMaterial());
                if (materialList.get(i).getMaterialName() != null)
                    matName = DatabaseUtils.sqlEscapeString(materialList.get(i)
                            .getMaterialName());
                if (materialList.get(i).getIdMaterialClass() != null)
                    idMatClass = DatabaseUtils.sqlEscapeString(materialList.get(i)
                            .getIdMaterialClass());

                if (materialList.get(i).getStockQty1() != null)
                    stockQty1 = materialList.get(i).getStockQty1();
                if (materialList.get(i).getStockUom1() != null)
                    stockUom1 = DatabaseUtils.sqlEscapeString(materialList.get(i)
                            .getStockUom1());
                if (materialList.get(i).getStockQty2() != null)
                    stockQty2 = materialList.get(i).getStockQty2();
                if (materialList.get(i).getStockUom2() != null)
                    stockUom2 = DatabaseUtils.sqlEscapeString(materialList.get(i)
                            .getStockUom2());


                if (materialList.get(i).getStockQty1C() != null)
                    stockQty1C = materialList.get(i).getStockQty1C();
                if (materialList.get(i).getStockUom1C() != null)
                    stockUom1C = DatabaseUtils.sqlEscapeString(materialList.get(i)
                            .getStockUom1C());
                if (materialList.get(i).getStockQty2C() != null)
                    stockQty2C = materialList.get(i).getStockQty2C();
                if (materialList.get(i).getStockUom2C() != null)
                    stockUom2C = DatabaseUtils.sqlEscapeString(materialList.get(i)
                            .getStockUom2C());

                selectQuery = selectQuery.concat("("
                        + idMat + "," + matName + "," + idMatClass + ","
                        + stockQty1 + "," + stockUom1 + "," + stockQty2 + "," + stockUom2 + ","
                        + stockQty1C + "," + stockUom1C + "," + stockQty2C + "," + stockUom2C
                        + ")");
                count++;
                if (count != 495) {
                    if (i != materialList.size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_MATERIAL + "(" + KEY_ID_MATERIAL
                            + "," + KEY_MATERIAL_NAME + "," + KEY_ID_MAT_CLASS + "," + KEY_STOCK_QTY_1
                            + "," + KEY_STOCK_UOM_1 + "," + KEY_STOCK_QTY_2 + "," + KEY_STOCK_UOM_2
                            + "," + KEY_STOCK_QTY_1_C + "," + KEY_STOCK_UOM_1_C + "," + KEY_STOCK_QTY_2_C
                            + "," + KEY_STOCK_UOM_2_C + ")" + " VALUES ";
                }
            }
        }
        db.close();
    }

    public void deleteMaterial() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_MATERIAL;
        db.execSQL(delete);
        db.close();
    }

    public ArrayList<MaterialResponse> getAllMaterialOrder() {
        ArrayList<MaterialResponse> listMaterial = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_MATERIAL +
                " where substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X1' " +
                " and substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X2' " +
                " and substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X3'" +
                " ORDER BY " + KEY_MATERIAL_NAME + " DESC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MaterialResponse materialResponse = new MaterialResponse();
                materialResponse.setIdMaterial(cursor.getString(0));
                materialResponse.setMaterialName(cursor.getString(1));
                materialResponse.setIdMaterialClass(cursor.getString(2));
                if (cursor.getString(3) != null) {
                    materialResponse.setStockQty1(
                            new BigDecimal(cursor.getString(3)));
                }
                materialResponse.setStockUom1(cursor.getString(4));
                if (cursor.getString(5) != null) {
                    materialResponse.setStockQty2(
                            new BigDecimal(cursor.getString(5)));
                }
                materialResponse.setStockUom2(cursor.getString(6));

                listMaterial.add(materialResponse);
            } while (cursor.moveToNext());
        }

        return listMaterial;
    }


    public ArrayList<String> getMasterMaterialName() {
        ArrayList<String> material = new ArrayList<String>();

        String selectQuery = "SELECT " + KEY_MATERIAL_NAME + " FROM " + TABLE_MATERIAL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                material.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return material;
    }

    public ArrayList<Material> getMasterMaterialNameCodeForOrder() {
        ArrayList<Material> material = new ArrayList<Material>();

        String selectQuery = "SELECT " + KEY_ID_MATERIAL + ", " + KEY_MATERIAL_NAME + " FROM " + TABLE_MATERIAL +
                " where substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X1' " +
                " and substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X2' " +
                " and substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X3'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Material data = new Material();
                data.setMaterialId(cursor.getString(0));
                data.setMaterialCode(cursor.getString(1));
                material.add(data);
            } while (cursor.moveToNext());
        }

        return material;
    }

    public ArrayList<String> getMasterMaterialNameForOrder() {
        ArrayList<String> material = new ArrayList<String>();

        String selectQuery = "SELECT " + KEY_MATERIAL_NAME + " FROM " + TABLE_MATERIAL +
                " where substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X1' " +
                " and substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X2' " +
                " and substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X3'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                material.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return material;
    }

    public ArrayList<String> getMasterMaterialCodeForOrder() {
        ArrayList<String> material = new ArrayList<String>();

        String selectQuery = "SELECT " + KEY_ID_MATERIAL + " FROM " + TABLE_MATERIAL +
                " where substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X1' " +
                " and substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X2' " +
                " and substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X3'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                material.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return material;
    }


    public ArrayList<String> getMasterMaterialCode() {
        ArrayList<String> material = new ArrayList<String>();

        String selectQuery = "SELECT " + KEY_ID_MATERIAL + " FROM " + TABLE_MATERIAL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                material.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return material;
    }


    public MaterialResponse getMasterMaterialByName(String name) {
        MaterialResponse material = new MaterialResponse();

        String selectQuery = "SELECT " + KEY_ID_MATERIAL + "," + KEY_MATERIAL_NAME + "," + KEY_ID_MAT_CLASS + " FROM " + TABLE_MATERIAL
                + " WHERE " + KEY_MATERIAL_NAME + " = ? AND " +
                " substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X1' " +
                " and substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X2' " +
                " and substr(" + KEY_ID_MATERIAL + ", instr(" + KEY_ID_MATERIAL + " ,'-')+1, 2) != 'X3'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{name});

        if (cursor.moveToFirst()) {
            do {
                material.setIdMaterial(cursor.getString(0));
                material.setMaterialName(cursor.getString(1));
                material.setIdMaterialClass(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        return material;
    }

    public MaterialResponse getMasterMaterialByCode(String code) {
        MaterialResponse material = new MaterialResponse();

        String selectQuery = "SELECT " + KEY_ID_MATERIAL + "," + KEY_MATERIAL_NAME + "," + KEY_ID_MAT_CLASS + " FROM " + TABLE_MATERIAL
                + " WHERE " + KEY_ID_MATERIAL + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{code});

        if (cursor.moveToFirst()) {
            do {
                material.setIdMaterial(cursor.getString(0));
                material.setMaterialName(cursor.getString(1));
                material.setIdMaterialClass(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        return material;
    }

    public ArrayList<MaterialResponse> getMasterMaterial() {
        ArrayList<MaterialResponse> lastOrderList = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT " + KEY_ID_MATERIAL + ", " + KEY_MATERIAL_NAME + " FROM " + TABLE_MATERIAL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                MaterialResponse materialResponse = new MaterialResponse();
                materialResponse.setIdMaterial(cursor.getString(0));
                materialResponse.setMaterialName(cursor.getString(1));
                lastOrderList.add(materialResponse);
            } while (cursor.moveToNext());
        }

        return lastOrderList;
    }

    public String getMaterialName(String idMaterial) {

        String selectQuery = "SELECT * FROM " + TABLE_MATERIAL +
                " WHERE " + KEY_ID_MATERIAL + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMaterial});

        MaterialResponse material = new MaterialResponse();

        if (cursor.moveToFirst()) {
            material.setMaterialName(cursor.getString(1));
            return material.getMaterialName();
        } else {
            material.setMaterialName("");

        }
        return material.getMaterialName();
    }

    //    UOM,insert,delete all
    public void addUOM(UnitOfMeasure unitOfMeasure) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_MATERIAL, unitOfMeasure.getIdMaterial());
        values.put(KEY_ID_UOM, unitOfMeasure.getId());
        values.put(KEY_UOM_NAME, unitOfMeasure.getUomName());
        values.put(KEY_CONVERSION, unitOfMeasure.getConversion());
        values.put(KEY_IS_ORDER, unitOfMeasure.is_order());
        values.put(KEY_IS_RETURN, unitOfMeasure.is_return());

        db.insert(TABLE_UOM, null, values);
        db.close();
    }

    public void addUOMN(List<UnitOfMeasure> list) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_UOM, CREATE_TABLE_UOM);
        deleteTable(db, TABLE_UOM);
        String selectQuery = "INSERT INTO " + TABLE_UOM + "(" + KEY_ID_MATERIAL
                + "," + KEY_ID_UOM + "," + KEY_CONVERSION + "," + KEY_IS_ORDER
                + "," + KEY_IS_RETURN + ")" + "VALUES ";
        int count = 0;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String idMat = null, idUom = null;
                double conversion = 0;
                int isOrder = 0, isReturn = 0;

                if (list.get(i).getIdMaterial() != null)
                    idMat = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getIdMaterial());
                if (list.get(i).getUomName() != null)
                    idUom = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getUomName());

                conversion = list.get(i).getConversion();
                if (list.get(i).is_order()) {
                    isOrder = 1;
                }

                if (list.get(i).is_return()) {
                    isReturn = 1;
                }

                selectQuery = selectQuery.concat("("
                        + idMat + "," + idUom + "," + conversion + ","
                        + isOrder + "," + isReturn
                        + ")");
                count++;
                if (count != 495) {
                    if (i != list.size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_UOM + "(" + KEY_ID_MATERIAL
                            + "," + KEY_ID_UOM + "," + KEY_CONVERSION + "," + KEY_IS_ORDER
                            + "," + KEY_IS_RETURN + ")" + "VALUES ";
                }
            }
        }

        db.close();
    }

    public void addMasterUomN(List<UnitOfMeasure> listUom) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_MASTER_UOM, CREATE_TABLE_MASTER_UOM);
        deleteTable(db, TABLE_MASTER_UOM);
        String selectQuery = "INSERT INTO " + TABLE_MASTER_UOM + "(" + KEY_ID_UOM
                + "," + KEY_UOM_NAME + ")" + "VALUES ";
        int count = 0;
        if (listUom != null && !listUom.isEmpty()) {
            for (int i = 0; i < listUom.size(); i++) {
                String idUom = null, uomName = null;

                if (listUom.get(i).getId() != null)
                    idUom = DatabaseUtils.sqlEscapeString(listUom.get(i)
                            .getId());
                if (listUom.get(i).getUomName() != null)
                    uomName = DatabaseUtils.sqlEscapeString(listUom.get(i)
                            .getUomName());

                selectQuery = selectQuery.concat("(" + idUom + "," + uomName + ")");
                count++;
                if (count != 495) {
                    if (i != listUom.size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_MASTER_UOM + "(" + KEY_ID_UOM + "," + KEY_UOM_NAME + ")" + " VALUES ";
                }
            }
        }

        db.close();
    }

    public ArrayList<UnitOfMeasure> getAllUOMReturn(String idMaterial) {
        ArrayList<UnitOfMeasure> listUom = new ArrayList<UnitOfMeasure>();

        String selectQuery = "SELECT * FROM " + TABLE_UOM +
                " WHERE " + KEY_ID_MATERIAL + " = ? AND " + KEY_IS_RETURN + " = '1'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMaterial});

        if (cursor.moveToFirst()) {
            do {
                boolean isOrder = false, isReturn = false;
                UnitOfMeasure uom = new UnitOfMeasure();
                uom.setIdMaterial(cursor.getString(0));
                uom.setUomName(cursor.getString(1));
                uom.setConversion(new Double(cursor.getString(2)));
                if (cursor.getString(3).toLowerCase().equals("true")) {
                    isOrder = true;
                } else {
                    isOrder = false;
                }
                if (cursor.getString(4).toLowerCase().equals("false")) {
                    isReturn = true;
                } else {
                    isReturn = false;
                }
                uom.setIs_order(isOrder);
                uom.setIs_return(isReturn);

                listUom.add(uom);
            } while (cursor.moveToNext());
        }

        return listUom;
    }

    public ArrayList<UnitOfMeasure> getAllUOMorder(String idMaterial) {
        ArrayList<UnitOfMeasure> listUom = new ArrayList<UnitOfMeasure>();

        String selectQuery = "SELECT * FROM " + TABLE_UOM +
                " WHERE " + KEY_ID_MATERIAL + " = ? AND " + KEY_IS_ORDER + " = '1'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMaterial});

        if (cursor.moveToFirst()) {
            do {
                boolean isOrder = false, isReturn = false;
                UnitOfMeasure uom = new UnitOfMeasure();
                uom.setIdMaterial(cursor.getString(0));
                uom.setId(cursor.getString(1));
                uom.setUomName(cursor.getString(2));
                uom.setConversion(new Double(cursor.getString(3)));
                if (cursor.getString(4).toLowerCase().equals("true")) {
                    isOrder = true;
                } else {
                    isOrder = false;
                }
                if (cursor.getString(5).toLowerCase().equals("false")) {
                    isReturn = true;
                } else {
                    isReturn = false;
                }
                uom.setIs_order(isOrder);
                uom.setIs_return(isReturn);

                listUom.add(uom);
            } while (cursor.moveToNext());
        }

        return listUom;
    }

    public String getIdUom(String uomName) {

        String selectQuery = "SELECT * FROM " + TABLE_MASTER_UOM +
                " WHERE " + KEY_UOM_NAME + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{uomName});

        UnitOfMeasure uom = new UnitOfMeasure();

        if (cursor.moveToFirst()) {
            uom.setId(cursor.getString(0));
            return uom.getId();
        } else {
            uom = null;
        }
        return uom.getId();
    }

    public String getUomName(String idUom) {

        String selectQuery = "SELECT " + KEY_UOM_NAME + " FROM " + TABLE_MASTER_UOM +
                " WHERE " + KEY_ID_UOM + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idUom});

        UnitOfMeasure uom = new UnitOfMeasure();

        if (cursor.moveToFirst()) {
            uom.setUomName(cursor.getString(0));
            return uom.getUomName();
        } else {
            uom.setUomName(idUom);
        }
        return uom.getUomName();
    }

    public void deleteUOM() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_UOM;
        db.execSQL(delete);
        db.close();
    }

    public void deleteMasterUom() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_MASTER_UOM;
        db.execSQL(delete);
        db.close();
    }

    //    outlet insert, delete
    public void addOutletNew(OutletResponse outletResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (outletResponse.getIdOutlet() != null) {
            values.put(KEY_ID_OUTLET, outletResponse.getIdOutlet());
        }

        if (outletResponse.getOutletName() != null) {
            values.put(KEY_OUTLET_NAME, outletResponse.getOutletName());
        }

        if (outletResponse.getPlant() != null) {
            values.put(KEY_ID_PLANT, outletResponse.getPlant());
        }

        if (outletResponse.getCredit_limit() != null) {
            values.put(KEY_CREDIT_LIMIT, String.valueOf(outletResponse.getCredit_limit()));
        }

        if (outletResponse.getCredit_exposure() != null) {
            values.put(KEY_CREDIT_EXPOSURE, String.valueOf(outletResponse.getCredit_exposure()));
        }

        if (outletResponse.getOverdue() != null) {
            values.put(KEY_OVERDUE, String.valueOf(outletResponse.getOverdue()));
        }

        if (outletResponse.getSegment() != null) {
            values.put(KEY_SEGMENT, outletResponse.getSegment());
        }

        if (outletResponse.getStreet1() != null) {
            values.put(KEY_ADDRESS, outletResponse.getStreet1());
        }

        db.insert(TABLE_OUTLET, null, values);
        db.close();
    }

    public ArrayList<OutletResponse> getAllOutlet() {
        ArrayList<OutletResponse> listOutlet = new ArrayList<OutletResponse>();

        String selectQuery = "SELECT * FROM " + TABLE_OUTLET;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                boolean isOrder = false, isReturn = false;
                OutletResponse outletResponse = new OutletResponse();
                outletResponse.setIdOutlet(cursor.getString(0));
                outletResponse.setOutletName(cursor.getString(1));
                outletResponse.setPlant(cursor.getString(2));
                if (cursor.getString(3) != null) {
                    outletResponse.setCredit_limit(new BigDecimal(cursor.getString(3)));
                }
                if (cursor.getString(4) != null) {
                    outletResponse.setCredit_exposure(new BigDecimal(cursor.getString(4)));
                }

                if (cursor.getString(5) != null) {
                    outletResponse.setOverdue(new BigDecimal(cursor.getString(5)));
                }

                listOutlet.add(outletResponse);
            } while (cursor.moveToNext());
        }

        return listOutlet;
    }

    public ArrayList<OutletResponse> getAllOutletName() {
        ArrayList<OutletResponse> listOutlet = new ArrayList<OutletResponse>();

        String selectQuery = "SELECT " + KEY_ID_OUTLET + " , " + KEY_OUTLET_NAME + " FROM " + TABLE_OUTLET;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                boolean isOrder = false, isReturn = false;
                OutletResponse outletResponse = new OutletResponse();
                outletResponse.setIdOutlet(cursor.getString(0));
                outletResponse.setOutletName(cursor.getString(1));

                listOutlet.add(outletResponse);
            } while (cursor.moveToNext());
        }

        return listOutlet;
    }

    public int getAllOutletSize() {
        ArrayList<OutletResponse> listOutlet = new ArrayList<OutletResponse>();

        String selectQuery = "SELECT count (*) FROM " + TABLE_OUTLET;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int size = 0;

        if (cursor.moveToFirst()) {
            size = cursor.getInt(0);
            return size;
        } else {
            size = 0;
        }

        return size;
    }

    public ArrayList<OutletResponse> getLoadOutletData(String beginIndex) {
        ArrayList<OutletResponse> listOutlet = new ArrayList<OutletResponse>();

        String selectQuery =
                "SELECT " + KEY_OUTLET_NAME + ", " + KEY_ID_MASTER + ", " + KEY_ID_OUTLET + ", " + KEY_ADDRESS
                        + " FROM " + TABLE_OUTLET
                        + " WHERE " + KEY_ID_MASTER + " >= " + beginIndex
                        + " LIMIT 10 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                boolean isOrder = false, isReturn = false;
                OutletResponse outletResponse = new OutletResponse();
                outletResponse.setOutletName(cursor.getString(0));
                outletResponse.setIndex(String.valueOf(cursor.getInt(1)));
                outletResponse.setIdOutlet(cursor.getString(2));
                outletResponse.setStreet1(cursor.getString(3));

                listOutlet.add(outletResponse);
            } while (cursor.moveToNext());
        }

        return listOutlet;
    }

    public String getTopIndexOutlet() {
        String selectQuery = "SELECT " + KEY_ID_MASTER + " FROM " + TABLE_OUTLET + " LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        String index = null;

        if (cursor.moveToFirst()) {
            index = cursor.getString(0);

            if (index == null) {
                index = "0";
            }
            return index;
        } else {
            index = "0";
        }

        return index;
    }

    public ArrayList<OutletResponse> getAllLoadedData() {
        ArrayList<OutletResponse> listOutlet = new ArrayList<OutletResponse>();

        String selectQuery =
                "SELECT DISTINCT " + KEY_OUTLET_NAME + ", " + KEY_ID_MASTER + ", " + KEY_ID_OUTLET
                        + " FROM " + TABLE_OUTLET;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                boolean isOrder = false, isReturn = false;
                OutletResponse outletResponse = new OutletResponse();
                outletResponse.setOutletName(cursor.getString(0));
                outletResponse.setIndex(String.valueOf(cursor.getInt(1)));
                outletResponse.setIdOutlet(cursor.getString(2));

                listOutlet.add(outletResponse);
            } while (cursor.moveToNext());
        }

        return listOutlet;
    }

    public OutletResponse getDetailOutlet(String idOutlet) {
        String selectQuery = "SELECT * FROM " + TABLE_OUTLET + " WHERE " + KEY_ID_OUTLET + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        OutletResponse outletResponse = new OutletResponse();
        if (cursor.moveToFirst()) {
            outletResponse.setIdOutlet(cursor.getString(0));
            outletResponse.setOutletName(cursor.getString(1));
            outletResponse.setPlant(cursor.getString(2));
            if (cursor.getString(3) != null) {
                outletResponse.setCredit_limit(new BigDecimal(cursor.getString(3)));
            }
            if (cursor.getString(4) != null) {
                outletResponse.setCredit_exposure(new BigDecimal(cursor.getString(4)));
            }
            if (cursor.getString(5) != null) {
                outletResponse.setOverdue(new BigDecimal(cursor.getString(5)));
            }
            outletResponse.setSegment(cursor.getString(6));
        } else {
            outletResponse = null;
        }
        return outletResponse;
    }

    public String getOutletName(String idOutlet) {

        String selectQuery = "SELECT " + KEY_OUTLET_NAME + " FROM " + TABLE_OUTLET +
                " WHERE " + KEY_ID_OUTLET + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        OutletResponse outletResponse = new OutletResponse();

        if (cursor.moveToFirst()) {
            outletResponse.setOutletName(cursor.getString(0));
            return outletResponse.getOutletName();
        } else {
            outletResponse.setOutletName("");
        }
        return outletResponse.getOutletName();
    }

    public String getOutletCode(String outletName) {

        String selectQuery = "SELECT " + KEY_ID_OUTLET + " FROM " + TABLE_OUTLET +
                " WHERE " + KEY_OUTLET_NAME + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{outletName});

        OutletResponse outletResponse = new OutletResponse();

        if (cursor.moveToFirst()) {
            outletResponse.setIdOutlet(cursor.getString(0));
            return outletResponse.getIdOutlet();
        } else {
            outletResponse.setIdOutlet("");
        }
        return outletResponse.getIdOutlet();
    }

    public void deleteOutlet() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_OUTLET;
        db.execSQL(delete);
        db.close();
    }

    //visit plan insert, delete all, update, get
    public void addVisitPlan(OutletResponse outletResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (outletResponse.getId() != null) {
            values.put(KEY_ID_VISIT_PLAN, outletResponse.getId());
        }
        if (outletResponse.getIdVisitSalesman() != null) {
            values.put(KEY_ID_VISIT_SALESMAN, outletResponse.getIdVisitSalesman());
        }
        if (outletResponse.getIdOutlet() != null) {
            values.put(KEY_ID_OUTLET, outletResponse.getIdOutlet());
        }
        if (outletResponse.getVisitDate() != null) {
            values.put(KEY_DATE, outletResponse.getVisitDate());
            values.put(KEY_DATE_FOR_SYNC, outletResponse.getVisitDate());
        }
        if (outletResponse.getCheck_in_time() != null) {
            values.put(KEY_CHECK_IN_TIME, outletResponse.getCheck_in_time());
        }
        if (outletResponse.getContinue_time() != null) {
            values.put(KEY_RESUME_TIME, outletResponse.getContinue_time());
        }
        if (outletResponse.getPause_time() != null) {
            values.put(KEY_PAUSE_TIME, outletResponse.getPause_time());
        }
        if (outletResponse.getCheck_out_time() != null) {
            values.put(KEY_CHECK_OUT_TIME, outletResponse.getCheck_out_time());
        }
        if (outletResponse.getLat_check_in() != null) {
            values.put(KEY_LAT_CHECK_IN, String.valueOf(outletResponse.getLat_check_in()));
        }
        if (outletResponse.getLong_check_in() != null) {
            values.put(KEY_LONG_CHECK_IN, String.valueOf(outletResponse.getLong_check_in()));
        }
        if (outletResponse.getLat_check_out() != null) {
            values.put(KEY_LAT_CHECK_OUT, String.valueOf(outletResponse.getLat_check_out()));
        }
        if (outletResponse.getLong_check_out() != null) {
            values.put(KEY_LONG_CHECK_OUT, String.valueOf(outletResponse.getLong_check_out()));
        }
        if (outletResponse.isEnabled()) {
            values.put(KEY_ENABLED, Constants.TRUE_NUM);
        } else {
            values.put(KEY_ENABLED, Constants.ZERO);
        }
        if (outletResponse.isDeleted()) {
            values.put(KEY_DELETED, Constants.TRUE_NUM);
        } else {
            values.put(KEY_DELETED, Constants.ZERO);
        }
        if (outletResponse.getTimer() != null) {
            values.put(KEY_DURATION, String.valueOf(outletResponse.getTimer()));
        }
        db.insert(TABLE_VISIT_PLAN, null, values);
        db.close();
    }

    public void addVisitPlanN(List<OutletResponse> list) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_VISIT_PLAN, CREATE_TABLE_VISIT_PLAN);
        deleteTable(db, TABLE_VISIT_PLAN);
        String selectQuery = "INSERT INTO " + TABLE_VISIT_PLAN + "(" + KEY_ID_VISIT_PLAN
                + "," + KEY_ID_VISIT_SALESMAN + "," + KEY_ID_OUTLET + "," + KEY_DATE
                + "," + KEY_CHECK_IN_TIME + "," + KEY_RESUME_TIME + "," + KEY_PAUSE_TIME
                + "," + KEY_CHECK_OUT_TIME + "," + KEY_LAT_CHECK_IN + "," + KEY_LONG_CHECK_IN
                + "," + KEY_LAT_CHECK_OUT + "," + KEY_LONG_CHECK_OUT + "," + KEY_ENABLED
                + "," + KEY_SYNC_STATUS + "," + KEY_DELETED + "," + KEY_DURATION + "," + KEY_STATUS_ORDER + "," + KEY_DATE_FOR_SYNC
                + ")" + "VALUES ";
        int count = 0;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String idVP = null, idVS = null, idOutlet = null, visitDate = null, checkInTime = null,
                        continueTime = null, pauseTime = null, checkOutTime = null, enabled = null,
                        deleted = null, duration = null, syncStatus = null, statusOrder = null, date_mobile = null;
                Double latCheckIn = null, longCheckIn = null, latCheckOut = null, longCheckOut = null;

                if (list.get(i).getId() != null)
                    idVP = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getId());
                if (list.get(i).getIdVisitSalesman() != null)
                    idVS = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getIdVisitSalesman());
                if (list.get(i).getIdOutlet() != null)
                    idOutlet = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getIdOutlet());

                if (list.get(i).getVisitDate() != null)
                    visitDate = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getVisitDate());
                if (list.get(i).getCheck_in_time() != null) {
                    checkInTime = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getCheck_in_time());
                    syncStatus = DatabaseUtils.sqlEscapeString(Constants.SYNCED);
                }
                if (list.get(i).getContinue_time() != null)
                    continueTime = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getContinue_time());
                if (list.get(i).getPause_time() != null)
                    pauseTime = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getPause_time());
                if (list.get(i).getCheck_out_time() != null)
                    checkOutTime = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getCheck_out_time());
                if (list.get(i).getLat_check_in() != null)
                    latCheckIn = list.get(i).getLat_check_in();
                if (list.get(i).getLong_check_in() != null)
                    longCheckIn = list.get(i)
                            .getLong_check_in();
                if (list.get(i).getLat_check_out() != null)
                    latCheckOut = list.get(i)
                            .getLat_check_out();
                if (list.get(i).getLong_check_out() != null)
                    longCheckOut = list.get(i)
                            .getLong_check_out();
                if (list.get(i).isEnabled()) {
                    enabled = Constants.TRUE_NUM;
                } else {
                    syncStatus = DatabaseUtils.sqlEscapeString(Constants.SYNCED);
                    enabled = Constants.ZERO;
                }
                if (list.get(i).isDeleted()) {
                    deleted = Constants.TRUE_NUM;
                } else {
                    deleted = Constants.ZERO;
                }
                if (list.get(i).getTimer() != null)
                    duration = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getTimer());
                if (list.get(i).getDate_mobile() != null)
                    date_mobile = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getDate_mobile());

                statusOrder = DatabaseUtils.sqlEscapeString("false");

                selectQuery = selectQuery.concat("("
                        + idVP + "," + idVS + "," + idOutlet + ","
                        + visitDate + "," + checkInTime + "," + continueTime + "," + pauseTime + ","
                        + checkOutTime + "," + latCheckIn + "," + longCheckIn + "," + latCheckOut + "," + longCheckOut + "," + enabled + ","
                        + syncStatus + "," + deleted + "," + duration + "," + statusOrder + "," + date_mobile
                        + ")");
                count++;
                if (count != 495) {
                    if (i != list.size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_VISIT_PLAN + "(" + KEY_ID_VISIT_PLAN
                            + "," + KEY_ID_VISIT_SALESMAN + "," + KEY_ID_OUTLET + "," + KEY_DATE
                            + "," + KEY_CHECK_IN_TIME + "," + KEY_RESUME_TIME + "," + KEY_PAUSE_TIME
                            + "," + KEY_CHECK_OUT_TIME + "," + KEY_LAT_CHECK_IN + "," + KEY_LONG_CHECK_IN
                            + "," + KEY_LAT_CHECK_OUT + "," + KEY_LONG_CHECK_OUT + "," + KEY_ENABLED
                            + "," + KEY_SYNC_STATUS + "," + KEY_DELETED + "," + KEY_DURATION + "," + KEY_STATUS_ORDER + "," + KEY_DATE_FOR_SYNC
                            + ")" + "VALUES ";
                }
            }
        }

        db.close();
    }

    public int sizeVisitPlan() {
        String selectQuery = "select count(*) from " + TABLE_VISIT_PLAN;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int size = 0;

        if (cursor.moveToFirst()) {
            size = cursor.getInt(0);
            return size;
        } else {
            size = 0;
        }

        return size;
    }

    public ArrayList<OutletResponse> getAllVisitPlan(String date) {
        ArrayList<OutletResponse> listOutlet = new ArrayList<>();

        String selectQuery =
                "SELECT "
                        + " a." + KEY_ID_VISIT_PLAN + ","
                        + " a." + KEY_ID_VISIT_SALESMAN + ","
                        + " b." + KEY_ID_OUTLET + ","
                        + " a." + KEY_DATE + ","
                        + " a." + KEY_CHECK_IN_TIME + ","
                        + " a." + KEY_RESUME_TIME + ","
                        + " a." + KEY_PAUSE_TIME + ","
                        + " a." + KEY_CHECK_OUT_TIME + ","
                        + " a." + KEY_LAT_CHECK_IN + ","
                        + " a." + KEY_LONG_CHECK_IN + ","
                        + " a." + KEY_LAT_CHECK_OUT + ","
                        + " a." + KEY_LONG_CHECK_OUT + ","
                        + " a." + KEY_PAUSE_REASON + ","
                        + " a." + KEY_CHECK_OUT_REASON + ","
                        + " a." + KEY_ENABLED + ","
                        + " a." + KEY_SYNC_STATUS + ","
                        + " a." + KEY_DURATION + ","
                        + " b." + KEY_OUTLET_NAME + ","
                        + " b." + KEY_ADDRESS + ","
                        + " a." + KEY_DATE_FOR_SYNC
                        + " FROM " + TABLE_VISIT_PLAN + " AS a "
                        + " LEFT JOIN " + TABLE_OUTLET + " AS b "
                        + " ON a." + KEY_ID_OUTLET + " = b." + KEY_ID_OUTLET
                        + " WHERE a." + KEY_DATE + " = ? AND (a." + KEY_DELETED + " == '0' OR a." + KEY_DELETED + " == NULL)"
                        + " ORDER BY TRIM( b." + KEY_OUTLET_NAME + ")";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                OutletResponse outletResponse = new OutletResponse();
                outletResponse.setId(cursor.getString(0));
                outletResponse.setIdVisitSalesman(cursor.getString(1));
                outletResponse.setIdOutlet(cursor.getString(2));
                outletResponse.setVisitDate(cursor.getString(3));
                if (cursor.getString(4) != null) {
                    outletResponse.setCheckInTime(cursor.getString(4));
                }
                outletResponse.setContinue_time(cursor.getString(5));
                outletResponse.setPause_time(cursor.getString(6));
                outletResponse.setCheck_out_time(cursor.getString(7));
                if (cursor.getString(8) != null && !cursor.getString(8).equals(Constants.NULL)) {
                    outletResponse.setLat_check_in(Double.valueOf(cursor.getString(8)));
                }
                if (cursor.getString(9) != null && !cursor.getString(9).equals(Constants.NULL)) {
                    outletResponse.setLong_check_in(Double.valueOf(cursor.getString(9)));
                }
                if (cursor.getString(10) != null && !cursor.getString(10).equals(Constants.NULL)) {
                    outletResponse.setLat_check_out(Double.valueOf(cursor.getString(10)));
                }
                if (cursor.getString(11) != null && !cursor.getString(11).equals(Constants.NULL)) {
                    outletResponse.setLong_check_out(Double.valueOf(cursor.getString(11)));
                }
                if (cursor.getString(12) != null && !cursor.getString(12).equals(Constants.NULL)) {
                    outletResponse.setPauseReason(cursor.getString(12));
                }
                if (cursor.getString(13) != null && !cursor.getString(13).equals(Constants.NULL)) {
                    outletResponse.setReason(cursor.getString(13));
                }
                if (cursor.getString(14).equals(Constants.ZERO)) {
                    outletResponse.setEnabled(false);
                } else {
                    outletResponse.setEnabled(true);
                }

                if (cursor.getString(15) != null) {
                    outletResponse.setStatusSync(cursor.getString(15));
                }

                if (cursor.getString(16) != null) {
                    outletResponse.setTimer(cursor.getString(16));
                }
                if (cursor.getString(17) != null) {
                    outletResponse.setOutletName(cursor.getString(17));
                }

                if (cursor.getString(18) != null) {
                    outletResponse.setStreet1(cursor.getString(18));
                }


                if (cursor.getString(19) != null) {
                    outletResponse.setDate_mobile(cursor.getString(19));
                }
                listOutlet.add(outletResponse);
            } while (cursor.moveToNext());
        }

        return listOutlet;
    }

    public int updateVisitPlan(OutletResponse outletResponse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (outletResponse.getId_mobile() != null) {
            values.put(KEY_ID_VISIT_PLAN, outletResponse.getId_mobile());
        }

        if (outletResponse.getCheckInTime() != null) {
            values.put(KEY_CHECK_IN_TIME, outletResponse.getCheckInTime());
        }

//        if (outletResponse.getContinue_time() != null) {
        values.put(KEY_RESUME_TIME, outletResponse.getContinue_time());
//        }
        if (outletResponse.getPause_time() != null) {
            values.put(KEY_PAUSE_TIME, outletResponse.getPause_time());
        }
        if (outletResponse.getCheck_out_time() != null) {
            values.put(KEY_CHECK_OUT_TIME, outletResponse.getCheck_out_time());
        }

        if (outletResponse.getLat_check_in() != null) {
            values.put(KEY_LAT_CHECK_IN, outletResponse.getLat_check_in());
        }

        if (outletResponse.getLong_check_in() != null) {
            values.put(KEY_LONG_CHECK_IN, outletResponse.getLong_check_in());
        }

        if (outletResponse.getLat_check_out() != null) {
            values.put(KEY_LAT_CHECK_OUT, outletResponse.getLat_check_out());
        }

        if (outletResponse.getLong_check_out() != null) {
            values.put(KEY_LONG_CHECK_OUT, outletResponse.getLong_check_out());
        }
        if (outletResponse.getPauseReason() != null) {
            values.put(KEY_PAUSE_REASON, outletResponse.getPauseReason());
        }

        if (outletResponse.getReason() != null) {
            values.put(KEY_CHECK_OUT_REASON, outletResponse.getReason());
        }

        if (outletResponse.getTimer() != null) {
            values.put(KEY_DURATION, outletResponse.getTimer());
        }
        // updating row
        return db.update(TABLE_VISIT_PLAN, values, KEY_ID_OUTLET + " = ? AND " +
                        KEY_DATE + " = ? ",
                new String[]{String.valueOf((outletResponse).getIdOutlet()),
                        String.valueOf((outletResponse).getVisitDate())});
    }

    public int updateVisitPlanById(OutletResponse outletResponse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (outletResponse.getId() != null) {
            values.put(KEY_ID_VISIT_PLAN, outletResponse.getId());
        }

        if (outletResponse.getCheckInTime() != null) {
            values.put(KEY_CHECK_IN_TIME, outletResponse.getCheckInTime());
        }

        if (outletResponse.getContinue_time() != null) {
            values.put(KEY_RESUME_TIME, outletResponse.getContinue_time());
        }
        if (outletResponse.getPause_time() != null) {
            values.put(KEY_PAUSE_TIME, outletResponse.getPause_time());
        }
        if (outletResponse.getCheck_out_time() != null) {
            values.put(KEY_CHECK_OUT_TIME, outletResponse.getCheck_out_time());
        }

        if (outletResponse.getLat_check_in() != null) {
            values.put(KEY_LAT_CHECK_IN, outletResponse.getLat_check_in());
        }

        if (outletResponse.getLong_check_in() != null) {
            values.put(KEY_LONG_CHECK_IN, outletResponse.getLong_check_in());
        }

        if (outletResponse.getLat_check_out() != null) {
            values.put(KEY_LAT_CHECK_OUT, outletResponse.getLat_check_out());
        }

        if (outletResponse.getLong_check_out() != null) {
            values.put(KEY_LONG_CHECK_OUT, outletResponse.getLong_check_out());
        }
        if (outletResponse.getPauseReason() != null) {
            values.put(KEY_PAUSE_REASON, outletResponse.getPauseReason());
        }

        if (outletResponse.getReason() != null) {
            values.put(KEY_CHECK_OUT_REASON, outletResponse.getReason());
        }

        if (outletResponse.getTimer() != null) {
            values.put(KEY_DURATION, outletResponse.getTimer());
        }
        if (outletResponse.getIdVisitSalesman() != null) {
            values.put(KEY_ID_VISIT_SALESMAN, outletResponse.getIdVisitSalesman());
        }
        // updating row
        return db.update(TABLE_VISIT_PLAN, values, KEY_ID_OUTLET + " = ? AND " +
                        KEY_DATE + " = ? ",
                new String[]{String.valueOf((outletResponse).getIdOutlet()),
                        String.valueOf((outletResponse).getVisitDate())});
    }

    public int updateEnableVisitPlan(String idOutlet, String enable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ENABLED, enable);

        return db.update(TABLE_VISIT_PLAN, values, KEY_ID_OUTLET + " = ?",
                new String[]{String.valueOf((idOutlet))});
    }

    public int updateDeletedVisitPlan(OutletResponse outlet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (outlet.isDeleted()) {
            values.put(KEY_DELETED, Constants.TRUE_NUM);
        } else {
            values.put(KEY_DELETED, Constants.ZERO);
        }

        return db.update(TABLE_VISIT_PLAN, values, KEY_ID_OUTLET + " = ? AND " +
                        KEY_DATE + " = ? ",
                new String[]{String.valueOf((outlet).getIdOutlet()),
                        String.valueOf((outlet).getVisitDate())});
    }

    public String getTimer(String idOutlet, String date) {

        String selectQuery = "SELECT " + KEY_DURATION + " FROM " + TABLE_VISIT_PLAN +
                " WHERE " + KEY_ID_OUTLET + " = ? AND " +
                KEY_DATE + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, date});

        OutletResponse outlet = new OutletResponse();
        String timer = "";
        if (cursor.moveToFirst()) {
            timer = cursor.getString(0);
        }
        return timer;
    }

    public CheckInOutRequest getOutletTimeDetail(String idOutlet, String date) {

        String selectQuery = "SELECT "
                + KEY_ID_OUTLET + ", "
                + KEY_DATE + ", "
                + KEY_CHECK_IN_TIME + ","
                + KEY_RESUME_TIME + ", "
                + KEY_PAUSE_TIME + ", "
                + KEY_CHECK_OUT_TIME + ", "
                + KEY_DURATION
                + " FROM " + TABLE_VISIT_PLAN
                + " WHERE " + KEY_ID_OUTLET + " = ? AND " +
                KEY_DATE + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, date});

        CheckInOutRequest outlet = new CheckInOutRequest();

        if (cursor.moveToFirst()) {
            outlet.setIdOutlet(cursor.getString(0));
            outlet.setDateString(cursor.getString(1));
            outlet.setCheckInTime(cursor.getString(2));
            outlet.setContinueTime(cursor.getString(3));
            outlet.setPauseTime(cursor.getString(4));
            outlet.setTimer(cursor.getString(6));
            return outlet;
        } else {
            outlet = null;
        }
        return outlet;
    }

    public int updateStatusSyncVP(String idOutlet, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SYNC_STATUS, status);

        return db.update(TABLE_VISIT_PLAN, values, KEY_ID_OUTLET + " = ? " + " AND " + KEY_DATE + " = ? ",
                new String[]{String.valueOf((idOutlet)), date});
    }

    public int updateDeletedVP(String idOutlet, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SYNC_STATUS, status);

        return db.update(TABLE_VISIT_PLAN, values, KEY_ID_OUTLET + " = ? " + " AND " + KEY_DATE + " = ? ",
                new String[]{String.valueOf((idOutlet)), date});
    }


    public void deleteVisitPlanBy(String idOutlet, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VISIT_PLAN, KEY_ID_OUTLET + " = ?" + " AND " + KEY_DATE + " = ? ",
                new String[]{idOutlet, date});
        db.close();

    }

    public void deleteVisitPlanByDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VISIT_PLAN, KEY_DATE + " = ? ",
                new String[]{date});
        db.close();

    }

    public void deleteVisitPlanNotIn(String values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_VISIT_PLAN + " WHERE " + KEY_ID_VISIT_PLAN + " NOT IN " + values;
        db.execSQL(delete);
        db.close();
    }

    public void deleteVisitPlan() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_VISIT_PLAN;
        db.execSQL(delete);
        db.close();
    }

    public void deleteVisitPlanBy(String idVp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VISIT_PLAN, KEY_ID_VISIT_PLAN + " = ?",
                new String[]{idVp});
        db.close();
    }

    //Store check , insert, delete, update, read

    public ArrayList<MaterialResponse> getAllStoreCheck(String idOutlet) {
        ArrayList<MaterialResponse> listOutlet = new ArrayList<MaterialResponse>();

        String selectQuery = "SELECT * FROM " + TABLE_STORE_CHECK +
                " WHERE " + KEY_ID_OUTLET + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        if (cursor.moveToFirst()) {
            do {
                MaterialResponse outletResponse = new MaterialResponse();
                outletResponse.setIdOutlet(cursor.getString(0));
                outletResponse.setIdMaterial(cursor.getString(1));
                outletResponse.setQty1(cursor.getString(2));
                outletResponse.setUom1(cursor.getString(3));
                outletResponse.setQty2(cursor.getString(4));
                outletResponse.setUom2(cursor.getString(5));
                outletResponse.setDate_mobile(cursor.getString(8));

                listOutlet.add(outletResponse);
            } while (cursor.moveToNext());
        }

        return listOutlet;
    }

    public ArrayList<MaterialResponse> getListStoreCheck() {
        ArrayList<MaterialResponse> listStoreCheck = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_STORE_CHECK;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                MaterialResponse material = new MaterialResponse();
                material.setId_store_check(cursor.getString(0));
                material.setIdOutlet(cursor.getString(1));
                material.setIdMaterial(cursor.getString(2));
                material.setQty1(cursor.getString(3));
                material.setUom1(cursor.getString(4));
                material.setQty2(cursor.getString(5));
                if (cursor.getString(6) != null) {
                    if (!cursor.getString(6).equals(Constants.NULL)) {
                        material.setUom2(cursor.getString(6));
                    } else {
                        material.setUom2(null);
                    }
                } else {
                    material.setUom2(null);
                }

                if (cursor.getString(7) != null) {
                    if (cursor.getString(7).equals("1")) {
                        material.setDeleted(true);
                    }
                }
                material.setDate_mobile(cursor.getString(9));

                listStoreCheck.add(material);
            } while (cursor.moveToNext());
        }

        return listStoreCheck;
    }

    public VisitOrderDetailResponse getMaterialStoreCheck(String idOutlet, String idMaterial) {
        VisitOrderDetailResponse material = new VisitOrderDetailResponse();

        String selectQuery = "SELECT " + KEY_QTY_1 + "," + KEY_QTY_2 + "," + KEY_UOM_1 + "," + KEY_UOM_2 + " FROM " + TABLE_STORE_CHECK
                + " WHERE " + KEY_ID_OUTLET + " = ? AND " + KEY_ID_MATERIAL + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, idMaterial});

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) != null) {
                    material.setStockQty1(new BigDecimal(cursor.getString(0)));
                }
                if (cursor.getString(1) != null) {
                    material.setStockQty2(new BigDecimal(cursor.getString(1)));
                }
                material.setStockUom1(cursor.getString(2));
                material.setStockUom2(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        return material;
    }

    public int updateStoreCheck(MaterialResponse materialResponse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //TODO MESTI CEK LAGI 1542019
        values.put(KEY_ID_STORE_CHECK, materialResponse.getId_store_check());
        values.put(KEY_QTY_1, materialResponse.getQty1());
        values.put(KEY_UOM_1, materialResponse.getUom1());
        values.put(KEY_QTY_2, materialResponse.getQty2());
        values.put(KEY_UOM_2, materialResponse.getUom2());
        values.put(KEY_DATE_FOR_SYNC, materialResponse.getDate_mobile());

        // updating row
        return db.update(TABLE_STORE_CHECK, values, KEY_ID_OUTLET + " = ? AND " +
                        KEY_ID_MATERIAL + " = ? ",
                new String[]{String.valueOf((materialResponse).getIdOutlet()),
                        String.valueOf((materialResponse).getIdMaterial())});
    }

    public int deleteStoreCheckByIdSoft(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, "1");

        // updating row
        return db.update(TABLE_STORE_CHECK, values, KEY_ID_STORE_CHECK + " = ? ",
                new String[]{id});
    }

    public void deleteStoreCheckByIdHard(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_STORE_CHECK + " WHERE " + KEY_ID_STORE_CHECK + " = '" + id + "'";
        db.execSQL(delete);
        db.close();
    }

    public void deleteStoreCheck() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_STORE_CHECK;
        db.execSQL(delete);
        db.close();
    }

    //Order Plan
    public void addOrderPlan(OrderPlanHeader orderPlanHeader) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ORDER_PLAN, orderPlanHeader.getId());
        values.put(KEY_ID_OUTLET, orderPlanHeader.getIdOutlet());
        values.put(KEY_ID_MATERIAL, orderPlanHeader.getIdMaterial());
        values.put(KEY_QTY_1, orderPlanHeader.getQty1());
        values.put(KEY_UOM_1, orderPlanHeader.getUom1());
        values.put(KEY_QTY_2, orderPlanHeader.getQty2());
        values.put(KEY_UOM_2, orderPlanHeader.getUom2());
        values.put(KEY_DATE, orderPlanHeader.getOutletDate());
        if (orderPlanHeader.getPrice() != null) {
            values.put(KEY_PRICE, String.valueOf(orderPlanHeader.getPrice()));
        }
        if (orderPlanHeader.getStatus() != null) {
            values.put(KEY_STATUS, String.valueOf(orderPlanHeader.getStatus()));
        }
        values.put(KEY_DELETED, "false");
        values.put(KEY_TARGET_MONTH, orderPlanHeader.getTargetMonth());
        values.put(KEY_TARGET_CALL, orderPlanHeader.getTargetCall());
        values.put(KEY_TARGET_ACHIEV, orderPlanHeader.getAchiev());
        values.put(KEY_TARGET_ORDER_PLAN, orderPlanHeader.getPlan());
        values.put(KEY_ID_MOBILE, orderPlanHeader.getId());
        values.put(KEY_DATE_FOR_SYNC, orderPlanHeader.getDate_mobile());

        db.insert(TABLE_ORDER_PLAN, null, values);
        db.close();
    }

    public void addOrderPlanN(List<OrderPlanHeader> list) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_ORDER_PLAN, CREATE_TABLE_ORDER_PLAN_N);
        deleteTable(db, TABLE_ORDER_PLAN);
        String selectQuery = "INSERT INTO " + TABLE_ORDER_PLAN + "(" + KEY_ID_ORDER_PLAN
                + "," + KEY_ID_OUTLET + "," + KEY_ID_MATERIAL + "," + KEY_QTY_1
                + "," + KEY_UOM_1 + "," + KEY_QTY_2 + "," + KEY_UOM_2
                + "," + KEY_DATE + "," + KEY_PRICE + "," + KEY_STATUS
                + "," + KEY_TARGET_MONTH + "," + KEY_TARGET_CALL + "," + KEY_TARGET_SISA
                + "," + KEY_TARGET_ACHIEV + "," + KEY_TARGET_ORDER_PLAN + "," + KEY_DELETED + "," + KEY_DATE_FOR_SYNC + ")" + "VALUES ";
        int count = 0;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String id = null, idOutlet = null, idMaterial = null, qty1 = null, uom1 = null, qty2 = null, uom2 = null, date = null, status = null,
                        targetMonth = null, targetCall = null, targetSisa = null, targetAchiev = null, targetOrderPlan = null, date_mobile = null;
                BigDecimal price = BigDecimal.ZERO;
                String deleted = "false";

                if (list.get(i).getId() != null)
                    id = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getId());
                if (list.get(i).getIdOutlet() != null)
                    idOutlet = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getIdOutlet());
                if (list.get(i).getIdMaterial() != null)
                    idMaterial = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getIdMaterial());

                if (list.get(i).getQty1() != null)
                    qty1 = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getQty1());
                if (list.get(i).getUom1() != null)
                    uom1 = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getUom1());
                if (list.get(i).getQty2() != null)
                    qty2 = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getQty2());
                if (list.get(i).getUom2() != null)
                    uom2 = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getUom2());
                if (list.get(i).getDate() != null)
                    date = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getDate());
                if (list.get(i).getPrice() != null)
                    price = list.get(i).getPrice();
                if (list.get(i).getStatus() == null)
                    status = DatabaseUtils.sqlEscapeString(Constants.STATUS_OP_FOR_UPDATE);
                if (list.get(i).getTargetMonth() != null)
                    targetMonth = list.get(i).getTargetMonth();
                if (list.get(i).getTargetCall() != null)
                    targetCall = list.get(i).getTargetCall();
                if (list.get(i).getTargetSisa() != null)
                    targetSisa = list.get(i).getTargetSisa();
                if (list.get(i).getAchiev() != null)
                    targetAchiev = list.get(i).getAchiev();
                if (list.get(i).getPlan() != null)
                    targetOrderPlan = list.get(i).getPlan();
                if (list.get(i).getDeleted() != null) {
                    deleted = DatabaseUtils.sqlEscapeString(list.get(i).getDeleted());
                }
                if (list.get(i).getDate_mobile() != null) {
                    date_mobile = DatabaseUtils.sqlEscapeString(list.get(i).getDate_mobile());
                }

                selectQuery = selectQuery.concat("("
                        + id + "," + idOutlet + "," + idMaterial + ","
                        + qty1 + "," + uom1 + "," + qty2 + "," + uom2 + ","
                        + date + "," + price + "," + status + ","
                        + targetMonth + "," + targetCall + "," + targetSisa + ","
                        + targetAchiev + "," + targetOrderPlan + "," + deleted + "," + date_mobile
                        + ")");
                count++;
                if (count != 495) {
                    if (i != list.size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_ORDER_PLAN + "(" + KEY_ID_ORDER_PLAN
                            + "," + KEY_ID_OUTLET + "," + KEY_ID_MATERIAL + "," + KEY_QTY_1
                            + "," + KEY_UOM_1 + "," + KEY_QTY_2 + "," + KEY_UOM_2
                            + "," + KEY_DATE + "," + KEY_PRICE + "," + KEY_STATUS
                            + "," + KEY_TARGET_MONTH + "," + KEY_TARGET_CALL + "," + KEY_TARGET_SISA
                            + "," + KEY_TARGET_ACHIEV + "," + KEY_TARGET_ORDER_PLAN + "," + KEY_DELETED + "," + KEY_DATE_FOR_SYNC + ")" + "VALUES ";
                }
            }
        }

        db.close();
    }

    public int updateOrderPlan(OrderPlanHeader orderPlanHeader) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_MATERIAL, orderPlanHeader.getIdMaterial());
        values.put(KEY_QTY_1, orderPlanHeader.getQty1());
        values.put(KEY_UOM_1, orderPlanHeader.getUom1());
        values.put(KEY_QTY_2, orderPlanHeader.getQty2());
        values.put(KEY_UOM_2, orderPlanHeader.getUom2());
        values.put(KEY_DATE, orderPlanHeader.getOutletDate());
        values.put(KEY_PRICE, String.valueOf(orderPlanHeader.getPrice()));
        if (orderPlanHeader.getStatus() != null) {
            values.put(KEY_STATUS, String.valueOf(orderPlanHeader.getStatus()));
        }
        if (orderPlanHeader.getDeleted() != null) {
            values.put(KEY_DELETED, String.valueOf(orderPlanHeader.getDeleted()));
        }

        if (orderPlanHeader.getId() != null) {
            values.put(KEY_ID_ORDER_PLAN, orderPlanHeader.getId());
        }

        if (orderPlanHeader.getTargetMonth() != null) {
            values.put(KEY_TARGET_MONTH, orderPlanHeader.getTargetMonth());
        }

        if (orderPlanHeader.getTargetCall() != null) {
            values.put(KEY_TARGET_CALL, orderPlanHeader.getTargetCall());
        }

        if (orderPlanHeader.getAchiev() != null) {
            values.put(KEY_TARGET_ACHIEV, orderPlanHeader.getAchiev());
        }

        if (orderPlanHeader.getPlan() != null) {
            values.put(KEY_TARGET_ORDER_PLAN, orderPlanHeader.getPlan());
        }

        // updating row
        return db.update(TABLE_ORDER_PLAN, values,
                KEY_ID_OUTLET + " = ? AND " +
                        KEY_DATE + " = ? AND " +
                        KEY_ID_MATERIAL + " = ? AND " +
                        KEY_DELETED + " = 'false'"
                ,
                new String[]{String.valueOf((orderPlanHeader).getIdOutlet()),
                        String.valueOf((orderPlanHeader).getOutletDate()),
                        String.valueOf((orderPlanHeader).getIdMaterial())
                });
    }

    public int updateOrderPlanFull(OrderPlanHeader orderPlanHeader) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_MATERIAL, orderPlanHeader.getIdMaterial());
        values.put(KEY_QTY_1, orderPlanHeader.getQty1());
        values.put(KEY_UOM_1, orderPlanHeader.getUom1());
        values.put(KEY_QTY_2, orderPlanHeader.getQty2());
        values.put(KEY_UOM_2, orderPlanHeader.getUom2());
        values.put(KEY_DATE, orderPlanHeader.getOutletDate());
        values.put(KEY_PRICE, String.valueOf(orderPlanHeader.getPrice()));
        if (orderPlanHeader.getStatus() != null) {
            values.put(KEY_STATUS, String.valueOf(orderPlanHeader.getStatus()));
        }
        if (orderPlanHeader.getDeleted() != null) {
            values.put(KEY_DELETED, String.valueOf(orderPlanHeader.getDeleted()));
        }
        // updating row
        return db.update(TABLE_ORDER_PLAN, values,
                KEY_ID_OUTLET + " = ? AND " +
                        KEY_DATE + " = ? "
                ,
                new String[]{String.valueOf((orderPlanHeader).getIdOutlet()),
                        String.valueOf((orderPlanHeader).getOutletDate())
                });
    }

    public int updateOrderPlanDeletedById(OrderPlanHeader orderPlanHeader) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (orderPlanHeader.getDeleted() != null) {
            values.put(KEY_DELETED, String.valueOf(orderPlanHeader.getDeleted()));
        }
        // updating row
        return db.update(TABLE_ORDER_PLAN, values,
                KEY_ID_OUTLET + " = ? AND " + KEY_DATE + " = ? "
                ,
                new String[]{orderPlanHeader.getIdOutlet(), orderPlanHeader.getOutletDate()
                });
    }

    public ArrayList<OrderPlanHeader> getAllOrderPlanHeader() {
        ArrayList<OrderPlanHeader> listOutlet = new ArrayList<>();

        String selectQuery = "SELECT " + KEY_ID_ORDER_PLAN + "," + KEY_ID_OUTLET + "," + KEY_DATE + "," + KEY_ID_MATERIAL + "," + KEY_QTY_1
                + "," + KEY_UOM_1 + "," + KEY_QTY_2 + "," + KEY_UOM_2 + "," + KEY_STATUS + "  FROM " + TABLE_ORDER_PLAN
                + " WHERE " + KEY_DELETED + " = 'false' OR " + KEY_DELETED + " IS NULL "
                + " GROUP BY " + KEY_ID_OUTLET + "," + KEY_DATE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                OrderPlanHeader orderPlanHeader = new OrderPlanHeader();
                orderPlanHeader.setId(cursor.getString(0));
                orderPlanHeader.setIdOutlet(cursor.getString(1));
                orderPlanHeader.setDate(cursor.getString(2));
                orderPlanHeader.setIdMaterial(cursor.getString(3));
                orderPlanHeader.setQty1(cursor.getString(4));
                orderPlanHeader.setUom1(cursor.getString(5));
                orderPlanHeader.setQty2(cursor.getString(6));
                orderPlanHeader.setUom2(cursor.getString(7));
                orderPlanHeader.setStatus(cursor.getString(8));

                listOutlet.add(orderPlanHeader);
            } while (cursor.moveToNext());
        }

        return listOutlet;
    }

    public int sizeOrderPlan() {
        String selectQuery = "select count(*) from " + TABLE_ORDER_PLAN;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int size = 0;

        if (cursor.moveToFirst()) {
            size = cursor.getInt(0);
            return size;
        } else {
            size = 0;
        }

        return size;
    }

    /*For new layout*/
    public ArrayList<OrderPlanHeader> getAllOrderPlanDate() {
        ArrayList<OrderPlanHeader> listOutlet = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT " + KEY_ID_ORDER_PLAN + "," + KEY_ID_OUTLET + "," + KEY_DATE
                + " FROM " + TABLE_ORDER_PLAN
                + " WHERE (" + KEY_DELETED + " = 'false' OR " + KEY_DELETED + " IS NULL ) "
                + " AND " + KEY_DATE + " IS NOT NULL "
                + " GROUP BY " + KEY_DATE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                OrderPlanHeader orderPlanHeader = new OrderPlanHeader();
                orderPlanHeader.setId(cursor.getString(0));
                orderPlanHeader.setIdOutlet(cursor.getString(1));
                orderPlanHeader.setOutletDate(cursor.getString(2));

                listOutlet.add(orderPlanHeader);
            } while (cursor.moveToNext());
        }
        return listOutlet;
    }

    public ArrayList<OrderPlanHeader> getListOrderPlanContentByDate(String date) {
        ArrayList<OrderPlanHeader> list = new ArrayList<>();

        String selectQuery = "SELECT " + KEY_ID_ORDER_PLAN + "," + KEY_ID_OUTLET + "," + KEY_DATE + ","
                + "SUM(" + KEY_PRICE + ")" + ","
                + KEY_TARGET_MONTH + ","
                + KEY_TARGET_CALL + ","
                + KEY_TARGET_ACHIEV + ","
                + KEY_DATE_FOR_SYNC
                + " FROM " + TABLE_ORDER_PLAN
                + " WHERE " + KEY_DATE + " = ? AND (" + KEY_DELETED + " = 'false')"
                + " GROUP BY " + KEY_ID_OUTLET + "," + KEY_DATE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{date});

        if (cursor.moveToFirst()) {
            do {
                OrderPlanHeader orderPlanHeader = new OrderPlanHeader();
                orderPlanHeader.setId(cursor.getString(0));
                orderPlanHeader.setIdOutlet(cursor.getString(1));
                orderPlanHeader.setOutletDate(cursor.getString(2));
                if (cursor.getString(3) != null) {
                    orderPlanHeader.setPlan(String.valueOf(Double.valueOf(cursor.getString(3)).longValue()));
                }
                if (cursor.getString(4) != null) {
                    orderPlanHeader.setTargetMonth(cursor.getString(4));
                }
                if (cursor.getString(5) != null) {
                    orderPlanHeader.setTargetCall(cursor.getString(5));
                }
                if (cursor.getString(6) != null) {
                    orderPlanHeader.setAchiev(cursor.getString(6));
                }
                if (cursor.getString(7) != null) {
                    orderPlanHeader.setDate_mobile(cursor.getString(7));
                }

                list.add(orderPlanHeader);
            } while (cursor.moveToNext());
        }

        return list;
    }

    /*untuk sync*/
    public ArrayList<OrderPlanHeader> getListOrderPlanHeader() {
        ArrayList<OrderPlanHeader> listOutlet = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT " + KEY_ID_ORDER_PLAN + "," + KEY_ID_OUTLET + "," + KEY_DATE + "," + KEY_ID_MATERIAL + ","
                + KEY_QTY_1 + "," + KEY_UOM_1 + "," + KEY_QTY_2 + "," + KEY_UOM_2 + "," + KEY_STATUS + "," + KEY_DELETED + ","
                + KEY_PRICE + "," + KEY_DATE_FOR_SYNC
                + " FROM " + TABLE_ORDER_PLAN;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                OrderPlanHeader orderPlanHeader = new OrderPlanHeader();
                orderPlanHeader.setId(cursor.getString(0));
                orderPlanHeader.setIdOutlet(cursor.getString(1));
                orderPlanHeader.setDate(cursor.getString(2));
                orderPlanHeader.setIdMaterial(cursor.getString(3));
                orderPlanHeader.setQty1(cursor.getString(4));
                orderPlanHeader.setUom1(cursor.getString(5));
                orderPlanHeader.setQty2(cursor.getString(6));
                orderPlanHeader.setUom2(cursor.getString(7));
                orderPlanHeader.setStatus(cursor.getString(8));
                orderPlanHeader.setDeleted(cursor.getString(9));
                if (cursor.getString(10) != null && !cursor.getString(10).equals("null")) {
                    orderPlanHeader.setPrice(new BigDecimal(cursor.getString(10)));
                }
                if (cursor.getString(11) != null) {
                    orderPlanHeader.setDate_mobile(cursor.getString(11));
                }

                listOutlet.add(orderPlanHeader);
            } while (cursor.moveToNext());
        }

        return listOutlet;
    }


    public ArrayList<OrderPlanHeader> getListAvailableOrderPlanHeader() {
        ArrayList<OrderPlanHeader> listOutlet = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT " + KEY_ID_ORDER_PLAN + "," + KEY_ID_OUTLET + "," + KEY_DATE + "," + KEY_ID_MATERIAL + "," + KEY_QTY_1 + "," + KEY_UOM_1 + "," + KEY_QTY_2 + "," + KEY_UOM_2 + "," + KEY_STATUS + "," + KEY_DELETED + "," + KEY_PRICE
                + " FROM " + TABLE_ORDER_PLAN
                + " WHERE " + KEY_DELETED + " = 'false'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                OrderPlanHeader orderPlanHeader = new OrderPlanHeader();
                orderPlanHeader.setId(cursor.getString(0));
                orderPlanHeader.setIdOutlet(cursor.getString(1));
                orderPlanHeader.setDate(cursor.getString(2));
                orderPlanHeader.setIdMaterial(cursor.getString(3));
                orderPlanHeader.setQty1(cursor.getString(4));
                orderPlanHeader.setUom1(cursor.getString(5));
                orderPlanHeader.setQty2(cursor.getString(6));
                orderPlanHeader.setUom2(cursor.getString(7));
                orderPlanHeader.setStatus(cursor.getString(8));
                orderPlanHeader.setDeleted(cursor.getString(9));
                if (cursor.getString(10) != null && !cursor.getString(10).equals("null")) {
                    orderPlanHeader.setPrice(new BigDecimal(cursor.getString(10)));
                }

                listOutlet.add(orderPlanHeader);
            } while (cursor.moveToNext());
        }

        return listOutlet;
    }

    public ArrayList<MaterialResponse> getAllOrderPlan(String idOutlet, String date) {
        ArrayList<MaterialResponse> listOutlet = new ArrayList<MaterialResponse>();

        String selectQuery = "SELECT * FROM " + TABLE_ORDER_PLAN +
                " WHERE " + KEY_ID_OUTLET + " = ? AND " + KEY_DATE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, date});

        if (cursor.moveToFirst()) {
            do {
                MaterialResponse materialResponse = new MaterialResponse();
                materialResponse.setIdOutlet(cursor.getString(0));
                materialResponse.setIdMaterial(cursor.getString(1));
                materialResponse.setQty1(cursor.getString(2));
                materialResponse.setUom1(cursor.getString(3));
                materialResponse.setQty2(cursor.getString(4));
                materialResponse.setUom2(cursor.getString(5));
                materialResponse.setDateString(cursor.getString(6));
                materialResponse.setPrice(new BigDecimal(cursor.getString(7)));

                listOutlet.add(materialResponse);
            } while (cursor.moveToNext());
        }

        return listOutlet;
    }

    public VisitOrderDetailResponse getMaterialOrderPlan(String idOutlet, String idMaterial, String date) {
        VisitOrderDetailResponse material = new VisitOrderDetailResponse();

        String selectQuery = "SELECT " + KEY_QTY_1 + "," + KEY_QTY_2 + "," + KEY_UOM_1 + "," + KEY_UOM_2 + " FROM " + TABLE_ORDER_PLAN
                + " WHERE " + KEY_ID_OUTLET + " = ? AND " + KEY_ID_MATERIAL + " = ? AND " + KEY_DATE + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, idMaterial, date});

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) != null) {
                    material.setOrderPlanQty1(new BigDecimal(cursor.getString(0)));
                }
                if (cursor.getString(1) != null) {
                    material.setOrderPlanQty2(new BigDecimal(cursor.getString(1)));
                }
                if (cursor.getString(2) != null) {
                    material.setOrderPlanUom1(cursor.getString(2));
                }

                if (cursor.getString(3) != null) {
                    material.setOrderPlanUom2(cursor.getString(3));

                }
            } while (cursor.moveToNext());
        }

        return material;
    }


    public ArrayList<OrderPlanHeader> getAllOrderPlanById(String idOrderPlan) {
        ArrayList<OrderPlanHeader> listOrderPlan = new ArrayList<OrderPlanHeader>();

        String selectQuery = "SELECT * FROM " + TABLE_ORDER_PLAN +
                " WHERE " + KEY_ID_ORDER_PLAN + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOrderPlan});

        if (cursor.moveToFirst()) {
            do {
                OrderPlanHeader orderPlanHeader = new OrderPlanHeader();
                orderPlanHeader.setIdMaterial(cursor.getString(2));
                orderPlanHeader.setQty1(cursor.getString(3));
                orderPlanHeader.setUom1(cursor.getString(4));
                orderPlanHeader.setQty2(cursor.getString(5));
                orderPlanHeader.setUom2(cursor.getString(6));
                if (cursor.getString(8) != null && !cursor.getString(8).equals("null")) {
                    orderPlanHeader.setPrice(new BigDecimal(cursor.getString(8)));
                }
                listOrderPlan.add(orderPlanHeader);
            } while (cursor.moveToNext());
        }

        return listOrderPlan;
    }

    public ArrayList<OrderPlanHeader> getAllOrderPlanByOutlet(String idOutlet, String date) {
        ArrayList<OrderPlanHeader> listOrderPlan = new ArrayList<OrderPlanHeader>();

        String selectQuery = "SELECT * FROM " + TABLE_ORDER_PLAN +
                " WHERE " + KEY_ID_OUTLET + " = ?" + " AND " + KEY_DATE + " = ? AND " +
                KEY_DELETED + " = 'false'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, date});

        if (cursor.moveToFirst()) {
            do {
                OrderPlanHeader orderPlanHeader = new OrderPlanHeader();
                orderPlanHeader.setIdMaterial(cursor.getString(2));
                orderPlanHeader.setQty1(cursor.getString(3));
                orderPlanHeader.setUom1(cursor.getString(4));
                orderPlanHeader.setQty2(cursor.getString(5));
                orderPlanHeader.setUom2(cursor.getString(6));
                if (cursor.getString(8) != null && !cursor.getString(8).equals("null")) {
                    orderPlanHeader.setPrice(new BigDecimal(cursor.getString(8)));
                }
                listOrderPlan.add(orderPlanHeader);
            } while (cursor.moveToNext());
        }

        return listOrderPlan;
    }

    public ArrayList<MaterialResponse> getAllOrderPlanByOutletInDate(String idOutlet, String date) {
        ArrayList<MaterialResponse> listOrderPlan = new ArrayList<MaterialResponse>();

        String selectQuery = "SELECT * FROM " + TABLE_ORDER_PLAN +
                " WHERE " + KEY_ID_OUTLET + " = ?" + " AND " + KEY_DATE + " = ? AND " +
                KEY_DELETED + " = 'false'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, date});

        if (cursor.moveToFirst()) {
            do {
                MaterialResponse data = new MaterialResponse();
                if (cursor.getString(2) != null) {
                    data.setIdMaterial(cursor.getString(2));
                }

                if (cursor.getString(3) != null) {
                    data.setNewQty1(new BigDecimal(cursor.getString(3)));
                }

                if (cursor.getString(4) != null) {
                    data.setNewUom1(cursor.getString(4));
                }

                if (cursor.getString(5) != null) {
                    data.setNewQty2(new BigDecimal(cursor.getString(5)));
                }

                if (cursor.getString(6) != null) {
                    data.setNewUom2(cursor.getString(6));
                }

                if (cursor.getString(8) != null && !cursor.getString(8).equals("null")) {
                    data.setPrice(new BigDecimal(cursor.getString(8)));
                }
                listOrderPlan.add(data);
            } while (cursor.moveToNext());
        }

        return listOrderPlan;
    }

    public int updateOrderPlan(MaterialResponse materialResponse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QTY_1, materialResponse.getQty1());
        values.put(KEY_UOM_1, materialResponse.getUom1());
        values.put(KEY_QTY_2, materialResponse.getQty2());
        values.put(KEY_UOM_2, materialResponse.getUom2());
        values.put(KEY_PRICE, String.valueOf(materialResponse.getPrice()));

        // updating row
        return db.update(TABLE_ORDER_PLAN, values, KEY_ID_OUTLET + " = ? AND " +
                        KEY_ID_MATERIAL + " = ? ",
                new String[]{String.valueOf((materialResponse).getIdOutlet()),
                        String.valueOf((materialResponse).getIdMaterial())});
    }

    public void deleteAllOrderPlan() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ORDER_PLAN;
        db.execSQL(delete);
        db.close();
    }


    /*=========================================TOPRICE======================================*/
    public void addToPrice(VisitOrderDetailResponse material, ToPrice toPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_OUTLET, material.getIdOutlet());
        values.put(KEY_ID_MATERIAL, material.getIdMaterial());
        values.put(KEY_ID_TO, material.getIdTo());
        values.put(KEY_ID_PRICE, material.getId_price());
        values.put(KEY_STEP, toPrice.getStep());
        values.put(KEY_COND_TYPE, toPrice.getId_cond_type());
        values.put(KEY_AMOUNT, String.valueOf(toPrice.getAmount()));
        values.put(KEY_TABLE_NAME, toPrice.getTable_name());
        values.put(KEY_ITEM_NR, toPrice.getItem_nr());
        values.put(KEY_ONE_TIME_DISCOUNT, toPrice.getOne_time_discount());
        values.put(KEY_KC_NO, toPrice.getKc_no());
        values.put(KEY_DISC_VALUE, toPrice.getDiscValueString());

        db.insert(TABLE_TO_PRICE, null, values);
        db.close();
    }

    public void addToPrice(ToPrice toPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_PRICE, toPrice.getId());
        values.put(KEY_STEP, toPrice.getStep());
        values.put(KEY_COND_TYPE, toPrice.getId_cond_type());
        values.put(KEY_AMOUNT, String.valueOf(toPrice.getAmount()));
        values.put(KEY_TABLE_NAME, toPrice.getTable_name());
        values.put(KEY_ITEM_NR, toPrice.getItem_nr());
        values.put(KEY_ONE_TIME_DISCOUNT, toPrice.getOne_time_discount());
        values.put(KEY_KC_NO, toPrice.getKc_no());
        values.put(KEY_DISC_VALUE, toPrice.getDiscValueString());
        values.put(KEY_COND_TYPE_NAME, toPrice.getCond_type());

        db.insert(TABLE_TO_PRICE, null, values);
        db.close();
    }

    public ToPrice getOneTimeDiscByIdPrice(String idPrice) {
        ToPrice data = new ToPrice();

        String selectQuery = "SELECT * FROM " + TABLE_TO_PRICE +
                " WHERE " + KEY_ID_PRICE + " = ? AND " + KEY_ONE_TIME_DISCOUNT + " = '1'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idPrice});

        if (cursor.moveToFirst()) {
            do {
                data.setId(cursor.getString(3));
                data.setStep(cursor.getInt(4));
                data.setCond_type(cursor.getString(5));
                if (cursor.getString(6) != null && !cursor.getString(6).equals("null")) {
                    data.setAmount(new BigDecimal(cursor.getString(6)));
                }
                data.setTable_name(cursor.getString(7));
                data.setItem_nr(cursor.getString(8));
                if (cursor.getInt(9) > 0) {
                    data.setOne_time_discount(true);
                } else {
                    data.setOne_time_discount(false);
                }

                data.setKc_no(cursor.getString(10));
                data.setDiscValueString(cursor.getString(11));
            } while (cursor.moveToNext());
        }

        return data;
    }

    public ArrayList<ToPrice> getToPricewithIdPriceAndTo(String idTo, String idPrice) {
        ArrayList<ToPrice> listToPrice = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_TO_PRICE +
                " WHERE " + KEY_ID_TO + " = ? AND " + KEY_ID_PRICE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idTo, idPrice});

        if (cursor.moveToFirst()) {
            do {
                ToPrice toPrice = new ToPrice();
                toPrice.setId(cursor.getString(3));
                toPrice.setStep(cursor.getInt(4));
                toPrice.setCond_type(cursor.getString(5));
                if (cursor.getString(6) != null && !cursor.getString(6).equals("null")) {
                    toPrice.setAmount(new BigDecimal(cursor.getString(6)));
                }
                toPrice.setTable_name(cursor.getString(7));
                toPrice.setItem_nr(cursor.getString(8));
                if (cursor.getInt(9) > 0) {
                    toPrice.setOne_time_discount(true);
                } else {
                    toPrice.setOne_time_discount(false);
                }
                toPrice.setKc_no(cursor.getString(10));
                toPrice.setDiscValueString(cursor.getString(11));
                listToPrice.add(toPrice);
            } while (cursor.moveToNext());
        }

        return listToPrice;
    }

    public ArrayList<ToPrice> getToPricewithIdPrice(String idPrice) {
        ArrayList<ToPrice> listToPrice = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_TO_PRICE +
                " WHERE " + KEY_ID_PRICE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idPrice});

        if (cursor.moveToFirst()) {
            do {
                ToPrice toPrice = new ToPrice();
                toPrice.setId(cursor.getString(3));
                toPrice.setStep(cursor.getInt(4));
                toPrice.setCond_type(cursor.getString(12));
                toPrice.setId_cond_type(cursor.getString(5));
                if (cursor.getString(6) != null && !cursor.getString(6).equals("null")) {
                    toPrice.setAmount(new BigDecimal(cursor.getString(6)));
                }
                toPrice.setTable_name(cursor.getString(7));
                toPrice.setItem_nr(cursor.getString(8));
                if (cursor.getInt(9) > 0) {
                    toPrice.setOne_time_discount(true);
                } else {
                    toPrice.setOne_time_discount(false);
                }
                toPrice.setKc_no(cursor.getString(10));
                toPrice.setDiscValueString(cursor.getString(11));
                listToPrice.add(toPrice);
            } while (cursor.moveToNext());
        }

        return listToPrice;
    }

    public void deleteToPrice() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_TO_PRICE;
        db.execSQL(delete);
        db.close();
    }

    /*Partner*/
    public void addPartner(Partner partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_OUTLET, partner.getId_outlet());
        values.put(KEY_ID_PARTNER, partner.getId_partner());
        values.put(KEY_PARTNER_NAME, partner.getNamePartner());

        db.insert(TABLE_PARTNER, null, values);
        db.close();
    }

    public void addPartnerN(List<Partner> list) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_PARTNER, CREATE_TABLE_PARTNER);
        deleteTable(db, TABLE_PARTNER);
        String selectQuery = "INSERT INTO " + TABLE_PARTNER + "(" + KEY_ID_OUTLET
                + "," + KEY_ID_PARTNER + "," + KEY_PARTNER_NAME + ")" + "VALUES ";
        int count = 0;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String idOutlet = null, idPartner = null, partnerName = null;

                if (list.get(i).getId_outlet() != null)
                    idOutlet = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getId_outlet());
                if (list.get(i).getId_partner() != null)
                    idPartner = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getId_partner());
                if (list.get(i).getNamePartner() != null)
                    partnerName = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getNamePartner());

                selectQuery = selectQuery.concat("("
                        + idOutlet + "," + idPartner + "," + partnerName + ")");
                count++;
                if (count != 495) {
                    if (i != list.size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_PARTNER + "(" + KEY_ID_OUTLET
                            + "," + KEY_ID_PARTNER + "," + KEY_PARTNER_NAME + ")" + "VALUES ";
                }
            }
        }

        db.close();
    }

    public void deletePartner() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_PARTNER;
        db.execSQL(delete);
        db.close();
    }

    public String getNamaJenisJual(String idOutlet, String idTOP) {//ini

        String selectQuery = "SELECT b." + KEY_NAMA_JENIS_JUAL + " FROM " + TABLE_TOP + " AS a "
                + " INNER JOIN " + TABLE_JENIS_JUAL + " AS b ON a." + KEY_ID_JENIS_JUAL + " = " + " b." + KEY_ID_JENIS_JUAL
                + " WHERE a." + KEY_ID_OUTLET + " = ? " + " AND " + "a." + KEY_ID_TOP + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, idTOP});

        JenisJualandTop jj = new JenisJualandTop();

        if (cursor.moveToFirst()) {
            jj.setJenisJualName(cursor.getString(0));
            return jj.getJenisJualName();
        } else {
            jj.setJenisJualName(null);
        }
        return jj.getJenisJualName();
    }

    public String getIdJj(String idOutlet, String idTOP) {//ini

        String selectQuery = "SELECT b." + KEY_ID_JENIS_JUAL + " FROM " + TABLE_TOP + " AS a "
                + " INNER JOIN " + TABLE_JENIS_JUAL + " AS b ON a." + KEY_ID_JENIS_JUAL + " = " + " b." + KEY_ID_JENIS_JUAL
                + " WHERE a." + KEY_ID_OUTLET + " = ? " + " AND " + "a." + KEY_ID_TOP + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, idTOP});

        JenisJualandTop jj = new JenisJualandTop();

        if (cursor.moveToFirst()) {
            jj.setJenisJualName(cursor.getString(0));
            return jj.getJenisJualName();
        } else {
            jj.setJenisJualName(null);
        }
        return jj.getJenisJualName();
    }

    /*Reason*/

    public void addReason(Reason reason) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_REASON, reason.getId());
        values.put(KEY_REASON_DESC, reason.getDesc());
        values.put(KEY_TYPE, reason.getType());

        db.insert(TABLE_REASON, null, values);
        db.close();
    }

    public ArrayList<Reason> getAllReason() {
        ArrayList<Reason> listReason = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_REASON;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Reason reason = new Reason();
                reason.setId(cursor.getString(0));
                reason.setDesc(cursor.getString(1));
                reason.setType(cursor.getString(2));

                listReason.add(reason);
            } while (cursor.moveToNext());
        }

        return listReason;
    }

    public String getIdReason(String reasonName) {//ini

        String selectQuery = "SELECT " + KEY_ID_REASON + " FROM " + TABLE_REASON +
                " WHERE " + KEY_REASON_DESC + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{reasonName});

        Reason jj = new Reason();

        if (cursor.moveToFirst()) {
            jj.setId(cursor.getString(0));
            return jj.getId();
        } else {
            jj.setId(null);
        }
        return jj.getId();
    }

    public void deleteReason() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_REASON;
        db.execSQL(delete);
        db.close();
    }

    /*User*/

    public void addAttendance(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_EMPLOYEE, user.getIdEmployee());
        values.put(KEY_DATE, user.getDateTimeNow());
        values.put(KEY_ELAPSED_REALTIME, String.valueOf(user.getmElapsedRealTime()));
        if (user.getIdPlant() != null) {
            values.put(KEY_ID_PLANT, user.getIdPlant());
        }
        values.put(KEY_NIK, user.getNik());

        db.insert(TABLE_ATTENDANCE, null, values);
        db.close();
    }

    public ArrayList<User> getAttendance() {
        ArrayList<User> listUser = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_ATTENDANCE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setIdEmployee(cursor.getString(0));
                user.setDateTimeNow(cursor.getString(1));
                user.setmElapsedRealTime(Long.valueOf(cursor.getString(2)));
                user.setIdPlant(cursor.getString(3));

                listUser.add(user);
            } while (cursor.moveToNext());
        }

        return listUser;
    }

    public int updateIdPlantUser(String idEmployee, String idPlant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_PLANT, idPlant);

        // updating row
        return db.update(TABLE_ATTENDANCE, values, KEY_ID_EMPLOYEE + " = ? ",
                new String[]{idEmployee});
    }

    public User getAttendance(String idEmployee) {
        User user = new User();

        String selectQuery = "SELECT * FROM " + TABLE_ATTENDANCE + " WHERE " + KEY_ID_EMPLOYEE + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idEmployee});

        if (cursor.moveToFirst()) {
            do {
                user.setIdEmployee(cursor.getString(0));
                user.setDateTimeNow(cursor.getString(1));
                user.setmElapsedRealTime(Long.valueOf(cursor.getString(2)));
            } while (cursor.moveToNext());
        }

        return user;
    }

    /*newest6Nov*/
    public Boolean isLogin(String nik) {
        boolean isLogged = false;

        String selectQuery = "SELECT " + KEY_NIK + " FROM " + TABLE_ATTENDANCE + " WHERE " + KEY_NIK + " =? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{nik});

        if (cursor.moveToFirst()) {
            isLogged = true;
        }

        return isLogged;
    }

    public void deleteAttendance() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_ATTENDANCE;
        db.execSQL(delete);
        db.close();
    }

    /*FreeGoods*/
    public int updateFreeGood(String id, OptionFreeGoods opt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID_TO, id);
        values.put(KEY_ID_FREE_GOODS, opt.getId());
        values.put(KEY_JENIS_JUAL, opt.getJenisJual());
        values.put(KEY_KLASIFIKASI, opt.getKlasifikasi());
        values.put(KEY_ID_MATERIAL, opt.getId_material());
        values.put(KEY_COND_TYPE, opt.getCond_type());
        values.put(KEY_DOC_NO, opt.getDoc_no());
        values.put(KEY_KC_NO, opt.getKc_no());
        values.put(KEY_MATERIAL_NAME, opt.getMaterialName());
        values.put(KEY_QTY, String.valueOf(opt.getQty()));
        values.put(KEY_TABLE_NAME, opt.getTableName());
        values.put(KEY_TOP, opt.getTop());
        if (opt.isOne_time_discount()) {
            values.put(KEY_ONE_TIME_DISCOUNT, 1);
        } else {
            values.put(KEY_ONE_TIME_DISCOUNT, 0);
        }
        values.put(KEY_AMOUNT, String.valueOf(opt.getAmount()));
        values.put(KEY_AMOUNT_UOM, opt.getAmountUom());
        values.put(KEY_TAX, String.valueOf(opt.getTax()));
        values.put(KEY_ITEM_NR, opt.getItem_nr());
        values.put(KEY_UOM, opt.getUom());

        return db.update(TABLE_FREE_GOODS, values, KEY_ID_TO + " = ?",
                new String[]{String.valueOf((id))});
    }

    public void addFreeGood(String id, OptionFreeGoods opt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID_TO, id);
        values.put(KEY_ID_FREE_GOODS, opt.getId());
        values.put(KEY_JENIS_JUAL, opt.getJenisJual());
        values.put(KEY_KLASIFIKASI, opt.getKlasifikasi());
        values.put(KEY_ID_MATERIAL, opt.getId_material());
        values.put(KEY_COND_TYPE, opt.getCond_type());
        values.put(KEY_DOC_NO, opt.getDoc_no());
        values.put(KEY_KC_NO, opt.getKc_no());
        values.put(KEY_MATERIAL_NAME, opt.getMaterialName());
        values.put(KEY_QTY, String.valueOf(opt.getQty()));
        values.put(KEY_TABLE_NAME, opt.getTableName());
        values.put(KEY_TOP, opt.getTop());
        if (opt.isOne_time_discount()) {
            values.put(KEY_ONE_TIME_DISCOUNT, 1);
        } else {
            values.put(KEY_ONE_TIME_DISCOUNT, 0);
        }
        values.put(KEY_AMOUNT, String.valueOf(opt.getAmount()));
        values.put(KEY_AMOUNT_UOM, opt.getAmountUom());
        values.put(KEY_TAX, String.valueOf(opt.getTax()));
        values.put(KEY_ITEM_NR, opt.getItem_nr());
        values.put(KEY_UOM, opt.getUom());

        db.insert(TABLE_FREE_GOODS, null, values);
        db.close();
    }

//    public void addFreeGoods(String idTO, ArrayList<List<OptionFreeGoods>> list) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_FREE_GOODS, CREATE_TABLE_FREE_GOODS);
//        String selectQuery = "INSERT INTO " + TABLE_FREE_GOODS + "(" + KEY_ID_TO
//                + "," + KEY_ID_FREE_GOODS + "," + KEY_JENIS_JUAL + "," + KEY_KLASIFIKASI + "," + KEY_ID_MATERIAL
//                + "," + KEY_COND_TYPE + "," + KEY_DOC_NO + "," + KEY_KC_NO
//                + "," + KEY_MATERIAL_NAME + "," + KEY_QTY + "," + KEY_TABLE_NAME
//                + "," + KEY_TOP + "," + KEY_ONE_TIME_DISCOUNT + "," + KEY_AMOUNT + "," + KEY_AMOUNT_UOM + "," + KEY_TAX + "," + KEY_ITEM_NR + "," + KEY_UOM
//                + ")" + "VALUES ";
//        int count = 0;
//        for (int i = 0; i < list.size(); i++) {
//            String idTo = null, idFg = null, jenisJual = null, klasifikasi = null,
//                    idMaterial = null, condType = null, docNo = null, kcNo = null,
//                    matName = null, tableName = null, top = null, idMat = null,
//                    amountUom = null, itemNr = null, uom = null;
//            BigDecimal qty = BigDecimal.ZERO, amount = BigDecimal.ZERO, tax = BigDecimal.ZERO;
//            int oneTimeDiscount = 0;
//
//            if (idTO != null)
//                idTo = DatabaseUtils.sqlEscapeString(idTO);
//
//            if (list.get(i).getId() != null)
//                idFg = DatabaseUtils.sqlEscapeString(list.get(i).getId());
//            if (list.get(i).getJenisJual() != null)
//                jenisJual = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getJenisJual());
//            if (list.get(i).getKlasifikasi() != null)
//                klasifikasi = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getKlasifikasi());
//            if (list.get(i).getId_material() != null)
//                idMaterial = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getId_material());
//
//            if (list.get(i).getCond_type() != null)
//                condType = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getCond_type());
//
//            if (list.get(i).getDoc_no() != null)
//                docNo = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getDoc_no());
//
//            if (list.get(i).getKc_no() != null)
//                kcNo = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getKc_no());
//
//            if (list.get(i).getMaterialName() != null)
//                matName = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getMaterialName());
//
//            if (list.get(i).getItem_nr() != null)
//                itemNr = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getItem_nr());
//
//            if (list.get(i).getTableName() != null)
//                tableName = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getTableName());
//
//            if (list.get(i).getTop() != null)
//                top = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getTop());
//
//            if (list.get(i).getQty() != null)
//                qty = list.get(i).getQty();
//
//
//            if (list.get(i).getAmount() != null)
//                amount = list.get(i).getAmount();
//
//            if (list.get(i).getTax() != null)
//                tax = list.get(i).getTax();
//
//
//            if (list.get(i).getAmountUom() != null)
//                amountUom = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getAmountUom());
//
//            if (list.get(i).isOne_time_discount()) {
//                oneTimeDiscount = 1;
//            } else {
//                oneTimeDiscount = 0;
//            }
//
//            if (list.get(i).getUom() != null)
//                uom = DatabaseUtils.sqlEscapeString(list.get(i)
//                        .getUom());
//
//            selectQuery = selectQuery.concat("("
//                    + idTo + "," + idFg + "," + jenisJual + ","
//                    + klasifikasi + "," + idMaterial + "," + condType + ","
//                    + docNo + "," + kcNo + "," + matName + ","
//                    + qty + "," + tableName + "," + top + ","
//                    + oneTimeDiscount + "," + amount + "," + amountUom + ","
//                    + tax + "," + itemNr + "," + uom
//                    + ")");
//            count++;
//            if (count != 495) {
//                if (i != list.size() - 1) {
//                    selectQuery = selectQuery.concat(",");
//                } else {
//                    db.execSQL(selectQuery);
//                }
//            }
//            if (count == 495) {
//                count = 0;
//                db.execSQL(selectQuery);
//                selectQuery = "INSERT INTO " + TABLE_FREE_GOODS + "(" + KEY_ID_TO
//                        + "," + KEY_ID_FREE_GOODS + "," + KEY_JENIS_JUAL + "," + KEY_KLASIFIKASI + "," + KEY_ID_MATERIAL
//                        + "," + KEY_COND_TYPE + "," + KEY_DOC_NO + "," + KEY_KC_NO
//                        + "," + KEY_MATERIAL_NAME + "," + KEY_QTY + "," + KEY_TABLE_NAME
//                        + "," + KEY_TOP + "," + KEY_ONE_TIME_DISCOUNT + "," + KEY_AMOUNT + "," + KEY_AMOUNT_UOM + ","
//                        + KEY_TAX + "," + KEY_ITEM_NR + "," + KEY_UOM
//                        + ")" + "VALUES ";
//            }
//        }
//
//        db.close();
//    }

    public void addFreeGoods(String idTO, ArrayList<List<OptionFreeGoods>> list) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_FREE_GOODS, CREATE_TABLE_FREE_GOODS);
        deleteTable(db, TABLE_FREE_GOODS);
        String selectQuery = "INSERT INTO " + TABLE_FREE_GOODS + "(" + KEY_ID_TO
                + "," + KEY_ID_FREE_GOODS + "," + KEY_JENIS_JUAL + "," + KEY_KLASIFIKASI + "," + KEY_ID_MATERIAL
                + "," + KEY_COND_TYPE + "," + KEY_DOC_NO + "," + KEY_KC_NO
                + "," + KEY_MATERIAL_NAME + "," + KEY_QTY + "," + KEY_TABLE_NAME
                + "," + KEY_TOP + "," + KEY_ONE_TIME_DISCOUNT + "," + KEY_AMOUNT + "," + KEY_AMOUNT_UOM + "," + KEY_TAX + "," + KEY_ITEM_NR + "," + KEY_UOM
                + ")" + "VALUES ";
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                String idTo = null, idFg = null, jenisJual = null, klasifikasi = null,
                        idMaterial = null, condType = null, docNo = null, kcNo = null,
                        matName = null, tableName = null, top = null, idMat = null,
                        amountUom = null, itemNr = null, uom = null;
                BigDecimal qty = BigDecimal.ZERO, amount = BigDecimal.ZERO, tax = BigDecimal.ZERO;
                int oneTimeDiscount = 0;

                if (idTO != null)
                    idTo = DatabaseUtils.sqlEscapeString(idTO);

                if (list.get(i).get(j).getId() != null)
                    idFg = DatabaseUtils.sqlEscapeString(list.get(i).get(j).getId());
                if (list.get(i).get(j).getJenisJual() != null)
                    jenisJual = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getJenisJual());
                if (list.get(i).get(j).getKlasifikasi() != null)
                    klasifikasi = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getKlasifikasi());
                if (list.get(i).get(j).getId_material() != null)
                    idMaterial = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getId_material());

                if (list.get(i).get(j).getCond_type() != null)
                    condType = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getCond_type());

                if (list.get(i).get(j).getDoc_no() != null)
                    docNo = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getDoc_no());

                if (list.get(i).get(j).getKc_no() != null)
                    kcNo = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getKc_no());

                if (list.get(i).get(j).getMaterialName() != null)
                    matName = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getMaterialName());

                if (list.get(i).get(j).getItem_nr() != null)
                    itemNr = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getItem_nr());

                if (list.get(i).get(j).getTableName() != null)
                    tableName = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getTableName());

                if (list.get(i).get(j).getTop() != null)
                    top = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getTop());

                if (list.get(i).get(j).getQty() != null)
                    qty = list.get(i).get(j).getQty();


                if (list.get(i).get(j).getAmount() != null)
                    amount = list.get(i).get(j).getAmount();

                if (list.get(i).get(j).getTax() != null)
                    tax = list.get(i).get(j).getTax();


                if (list.get(i).get(j).getAmountUom() != null)
                    amountUom = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getAmountUom());

                if (list.get(i).get(j).isOne_time_discount()) {
                    oneTimeDiscount = 1;
                } else {
                    oneTimeDiscount = 0;
                }

                if (list.get(i).get(j).getUom() != null)
                    uom = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getUom());

                selectQuery = selectQuery.concat("("
                        + idTo + "," + idFg + "," + jenisJual + ","
                        + klasifikasi + "," + idMaterial + "," + condType + ","
                        + docNo + "," + kcNo + "," + matName + ","
                        + qty + "," + tableName + "," + top + ","
                        + oneTimeDiscount + "," + amount + "," + amountUom + ","
                        + tax + "," + itemNr + "," + uom
                        + ")");
                count++;
                if (count != 495) {
                    if (j != list.get(i).size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_FREE_GOODS + "(" + KEY_ID_TO
                            + "," + KEY_ID_FREE_GOODS + "," + KEY_JENIS_JUAL + "," + KEY_KLASIFIKASI + "," + KEY_ID_MATERIAL
                            + "," + KEY_COND_TYPE + "," + KEY_DOC_NO + "," + KEY_KC_NO
                            + "," + KEY_MATERIAL_NAME + "," + KEY_QTY + "," + KEY_TABLE_NAME
                            + "," + KEY_TOP + "," + KEY_ONE_TIME_DISCOUNT + "," + KEY_AMOUNT + "," + KEY_AMOUNT_UOM + ","
                            + KEY_TAX + "," + KEY_ITEM_NR + "," + KEY_UOM
                            + ")" + "VALUES ";
                }
            }

        }

        db.close();
    }

    public void addFreeGoodsWODrop(String idTO, ArrayList<ArrayList<OptionFreeGoods>> list) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_FREE_GOODS, CREATE_TABLE_FREE_GOODS);
        String selectQuery = "INSERT INTO " + TABLE_FREE_GOODS + "(" + KEY_ID_TO
                + "," + KEY_ID_FREE_GOODS + "," + KEY_JENIS_JUAL + "," + KEY_KLASIFIKASI + "," + KEY_ID_MATERIAL
                + "," + KEY_COND_TYPE + "," + KEY_DOC_NO + "," + KEY_KC_NO
                + "," + KEY_MATERIAL_NAME + "," + KEY_QTY + "," + KEY_TABLE_NAME
                + "," + KEY_TOP + "," + KEY_ONE_TIME_DISCOUNT + "," + KEY_AMOUNT + "," + KEY_AMOUNT_UOM + "," + KEY_TAX + "," + KEY_ITEM_NR + "," + KEY_UOM
                + "," + KEY_QTY_P + "," + KEY_AMOUNT_P
                + ")" + "VALUES ";
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                String idTo = null, idFg = null, jenisJual = null, klasifikasi = null,
                        idMaterial = null, condType = null, docNo = null, kcNo = null,
                        matName = null, tableName = null, top = null, idMat = null,
                        amountUom = null, itemNr = null, uom = null;
                BigDecimal qty = BigDecimal.ZERO, amount = BigDecimal.ZERO, tax = BigDecimal.ZERO,
                        qtyP = BigDecimal.ZERO, amountP = BigDecimal.ZERO;
                int oneTimeDiscount = 0;

                if (idTO != null)
                    idTo = DatabaseUtils.sqlEscapeString(idTO);

                if (list.get(i).get(j).getId() != null)
                    idFg = DatabaseUtils.sqlEscapeString(list.get(i).get(j).getId());
                if (list.get(i).get(j).getJenisJual() != null)
                    jenisJual = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getJenisJual());
                if (list.get(i).get(j).getKlasifikasi() != null)
                    klasifikasi = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getKlasifikasi());
                if (list.get(i).get(j).getId_material() != null)
                    idMaterial = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getId_material());

                if (list.get(i).get(j).getCond_type() != null)
                    condType = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getCond_type());

                if (list.get(i).get(j).getDoc_no() != null)
                    docNo = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getDoc_no());

                if (list.get(i).get(j).getKc_no() != null)
                    kcNo = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getKc_no());

                if (list.get(i).get(j).getMaterialName() != null)
                    matName = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getMaterialName());

                if (list.get(i).get(j).getItem_nr() != null)
                    itemNr = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getItem_nr());

                if (list.get(i).get(j).getTableName() != null)
                    tableName = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getTableName());

                if (list.get(i).get(j).getTop() != null)
                    top = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getTop());

                if (list.get(i).get(j).getQty() != null)
                    qty = list.get(i).get(j).getQty();


                if (list.get(i).get(j).getAmount() != null)
                    amount = list.get(i).get(j).getAmount();

                if (list.get(i).get(j).getTax() != null)
                    tax = list.get(i).get(j).getTax();


                if (list.get(i).get(j).getAmountUom() != null)
                    amountUom = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getAmountUom());

                if (list.get(i).get(j).isOne_time_discount()) {
                    oneTimeDiscount = 1;
                } else {
                    oneTimeDiscount = 0;
                }

                if (list.get(i).get(j).getUom() != null)
                    uom = DatabaseUtils.sqlEscapeString(list.get(i)
                            .get(j).getUom());

                if (list.get(i).get(j).getQtyP() != null) {
                    qtyP = list.get(i).get(j).getQtyP();
                }

                if (list.get(i).get(j).getAmountP() != null) {
                    amountP = list.get(i).get(j).getAmountP();
                }

                selectQuery = selectQuery.concat("("
                        + idTo + "," + idFg + "," + jenisJual + ","
                        + klasifikasi + "," + idMaterial + "," + condType + ","
                        + docNo + "," + kcNo + "," + matName + ","
                        + qty + "," + tableName + "," + top + ","
                        + oneTimeDiscount + "," + amount + "," + amountUom + ","
                        + tax + "," + itemNr + "," + uom + ","
                        + qtyP + "," + amountP
                        + ")");
                count++;
                if (count != 495) {
                    if (j != list.get(i).size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        if (i != list.size() - 1) {
                            selectQuery = selectQuery.concat(",");
                        } else {
                            db.execSQL(selectQuery);
                        }
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_FREE_GOODS + "(" + KEY_ID_TO
                            + "," + KEY_ID_FREE_GOODS + "," + KEY_JENIS_JUAL + ","
                            + KEY_KLASIFIKASI + "," + KEY_ID_MATERIAL
                            + "," + KEY_COND_TYPE + "," + KEY_DOC_NO + "," + KEY_KC_NO
                            + "," + KEY_MATERIAL_NAME + "," + KEY_QTY + "," + KEY_TABLE_NAME
                            + "," + KEY_TOP + "," + KEY_ONE_TIME_DISCOUNT + "," + KEY_AMOUNT + "," + KEY_AMOUNT_UOM
                            + "," + KEY_TAX + "," + KEY_ITEM_NR + "," + KEY_UOM
                            + "," + KEY_QTY_P + "," + KEY_AMOUNT_P
                            + ")" + "VALUES ";
                }
            }

        }

        db.close();
    }

    public void addFreeGoods(List<OptionFreeGoods> list) {
        SQLiteDatabase db = this.getWritableDatabase();
//        dropTable(db, TABLE_FREE_GOODS, CREATE_TABLE_FREE_GOODS);
        deleteTable(db, TABLE_FREE_GOODS);
        String selectQuery = "INSERT INTO " + TABLE_FREE_GOODS + "(" + KEY_ID_TO
                + "," + KEY_ID_FREE_GOODS + "," + KEY_JENIS_JUAL + "," + KEY_KLASIFIKASI + "," + KEY_ID_MATERIAL
                + "," + KEY_COND_TYPE + "," + KEY_DOC_NO + "," + KEY_KC_NO
                + "," + KEY_MATERIAL_NAME + "," + KEY_QTY + "," + KEY_TABLE_NAME
                + "," + KEY_TOP + "," + KEY_ONE_TIME_DISCOUNT + "," + KEY_AMOUNT + "," + KEY_AMOUNT_UOM + "," + KEY_TAX + "," + KEY_ITEM_NR + "," + KEY_UOM
                + "," + KEY_QTY_P + "," + KEY_AMOUNT_P
                + ")" + "VALUES ";
        int count = 0;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String idTo = null, idFg = null, jenisJual = null, klasifikasi = null,
                        idMaterial = null, condType = null, docNo = null, kcNo = null,
                        matName = null, tableName = null, top = null, idMat = null,
                        amountUom = null, itemNr = null, uom = null;
                BigDecimal qty = BigDecimal.ZERO, amount = BigDecimal.ZERO, tax = BigDecimal.ZERO,
                        qtyP = BigDecimal.ZERO, amountP = BigDecimal.ZERO;
                int oneTimeDiscount = 0;

                if (list.get(i).getIdTo() != null)
                    idTo = DatabaseUtils.sqlEscapeString(list.get(i).getIdTo());

                if (list.get(i).getId() != null)
                    idFg = DatabaseUtils.sqlEscapeString(list.get(i).getId());
                if (list.get(i).getJenisJual() != null)
                    jenisJual = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getJenisJual());
                if (list.get(i).getKlasifikasi() != null)
                    klasifikasi = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getKlasifikasi());
                if (list.get(i).getId_material() != null)
                    idMaterial = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getId_material());

                if (list.get(i).getCond_type() != null)
                    condType = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getCond_type());

                if (list.get(i).getDoc_no() != null)
                    docNo = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getDoc_no());

                if (list.get(i).getKc_no() != null)
                    kcNo = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getKc_no());

                if (list.get(i).getMaterialName() != null)
                    matName = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getMaterialName());

                if (list.get(i).getItem_nr() != null)
                    itemNr = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getItem_nr());

                if (list.get(i).getTableName() != null)
                    tableName = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getTableName());

                if (list.get(i).getTop() != null)
                    top = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getTop());

                if (list.get(i).getQty() != null)
                    qty = list.get(i).getQty();


                if (list.get(i).getAmount() != null)
                    amount = list.get(i).getAmount();

                if (list.get(i).getTax() != null)
                    tax = list.get(i).getTax();


                if (list.get(i).getAmountUom() != null)
                    amountUom = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getAmountUom());

                if (list.get(i).isOne_time_discount()) {
                    oneTimeDiscount = 1;
                } else {
                    oneTimeDiscount = 0;
                }

                if (list.get(i).getUom() != null)
                    uom = DatabaseUtils.sqlEscapeString(list.get(i)
                            .getUom());

                if (list.get(i).getQtyP() != null) {
                    qtyP = list.get(i).getQtyP();
                }

                if (list.get(i).getAmountP() != null) {
                    amountP = list.get(i).getAmountP();
                }

                selectQuery = selectQuery.concat("("
                        + idTo + "," + idFg + "," + jenisJual + ","
                        + klasifikasi + "," + idMaterial + "," + condType + ","
                        + docNo + "," + kcNo + "," + matName + ","
                        + qty + "," + tableName + "," + top + ","
                        + oneTimeDiscount + "," + amount + "," + amountUom + ","
                        + tax + "," + itemNr + "," + uom + ","
                        + qtyP + "," + amountP
                        + ")");
                count++;
                if (count != 495) {
                    if (i != list.size() - 1) {
                        selectQuery = selectQuery.concat(",");
                    } else {
                        db.execSQL(selectQuery);
                    }
                }
                if (count == 495) {
                    count = 0;
                    db.execSQL(selectQuery);
                    selectQuery = "INSERT INTO " + TABLE_FREE_GOODS + "(" + KEY_ID_TO
                            + "," + KEY_ID_FREE_GOODS + "," + KEY_JENIS_JUAL + "," + KEY_KLASIFIKASI + "," + KEY_ID_MATERIAL
                            + "," + KEY_COND_TYPE + "," + KEY_DOC_NO + "," + KEY_KC_NO
                            + "," + KEY_MATERIAL_NAME + "," + KEY_QTY + "," + KEY_TABLE_NAME
                            + "," + KEY_TOP + "," + KEY_ONE_TIME_DISCOUNT + "," + KEY_AMOUNT + "," + KEY_AMOUNT_UOM + ","
                            + KEY_TAX + "," + KEY_ITEM_NR + "," + KEY_UOM
                            + "," + KEY_QTY_P + "," + KEY_AMOUNT_P
                            + ")" + "VALUES ";
                }
            }
        }

        db.close();
    }

    public ArrayList<FreeGoods> getFreeGoodsByIdTo(String idTo) {
        ArrayList<FreeGoods> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_FREE_GOODS
                + " WHERE " + KEY_ID_TO + " = ? GROUP BY " + KEY_ID_TO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idTo});

        if (cursor.moveToFirst()) {
            do {
                FreeGoods freeGoods = new FreeGoods();
                freeGoods.setJenisJual(cursor.getString(2));
                freeGoods.setKlasifikasi(cursor.getString(3));
                freeGoods.setTax(new BigDecimal(cursor.getString(15)));

                list.add(freeGoods);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<FreeGoods> getFreeGoodsByIdFG(String idTo) {
        ArrayList<FreeGoods> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_FREE_GOODS
                + " WHERE " + KEY_ID_TO + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idTo});

        if (cursor.moveToFirst()) {
            do {
                FreeGoods freeGoods = new FreeGoods();
                freeGoods.setId(cursor.getString(1));
                freeGoods.setJenisJual(cursor.getString(2));
                freeGoods.setKlasifikasi(cursor.getString(3));
                freeGoods.setTax(new BigDecimal(cursor.getString(15)));

                list.add(freeGoods);
            } while (cursor.moveToNext());
        }

        return list;
    }


    public ArrayList<OptionFreeGoods> getOptionFreeGoodsById(String idTo) {
        ArrayList<OptionFreeGoods> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_FREE_GOODS
                + " WHERE " + KEY_ID_TO + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idTo});

        if (cursor.moveToFirst()) {
            do {
                OptionFreeGoods opt = new OptionFreeGoods();
                opt.setId(cursor.getString(1));
                opt.setTableName(cursor.getString(10));
                opt.setItem_nr(cursor.getString(16));
                opt.setDoc_no(cursor.getString(6));
                opt.setCond_type(cursor.getString(5));
                opt.setId_material(cursor.getString(4));
                if (cursor.getString(9) != null) {
                    opt.setQty(new BigDecimal(cursor.getString(9)));
                }
                if (cursor.getInt(12) == 0) {
                    opt.setOne_time_discount(false);
                } else {
                    opt.setOne_time_discount(true);
                }
                opt.setKc_no(cursor.getString(7));
                opt.setKlasifikasi(cursor.getString(3));
                opt.setTop(cursor.getString(11));
                opt.setJenisJual(cursor.getString(2));
                opt.setMaterialName(cursor.getString(8));
                if (cursor.getString(13) != null) {
                    opt.setAmount(new BigDecimal(cursor.getString(13)));
                }
                opt.setAmountUom(cursor.getString(14));
                if (cursor.getString(15) != null) {
                    opt.setTax(new BigDecimal(cursor.getString(15)));
                }
                opt.setUom(cursor.getString(17));
                opt.setQtyP(new BigDecimal(cursor.getString(18)));
                opt.setAmountP(new BigDecimal(cursor.getString(19)));

                list.add(opt);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<OptionFreeGoods> getOptionFreeGoodsByIdGroupedBy(String idTo) {
        ArrayList<OptionFreeGoods> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_FREE_GOODS
                + " WHERE " + KEY_ID_TO + " = ? "
                + " GROUP BY " + KEY_KLASIFIKASI + ", " + KEY_JENIS_JUAL + ", " + KEY_TOP;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idTo});

        if (cursor.moveToFirst()) {
            do {
                OptionFreeGoods opt = new OptionFreeGoods();
                opt.setId(cursor.getString(1));
                opt.setTableName(cursor.getString(10));
                opt.setItem_nr(cursor.getString(16));
                opt.setDoc_no(cursor.getString(6));
                opt.setCond_type(cursor.getString(5));
                opt.setId_material(cursor.getString(4));
                if (cursor.getString(9) != null) {
                    opt.setQty(new BigDecimal(cursor.getString(9)));
                }
                if (cursor.getInt(12) == 0) {
                    opt.setOne_time_discount(false);
                } else {
                    opt.setOne_time_discount(true);
                }
                opt.setKc_no(cursor.getString(7));
                opt.setKlasifikasi(cursor.getString(3));
                opt.setTop(cursor.getString(11));
                opt.setJenisJual(cursor.getString(2));
                opt.setMaterialName(cursor.getString(8));
                if (cursor.getString(13) != null) {
                    opt.setAmount(new BigDecimal(cursor.getString(13)));
                }
                opt.setAmountUom(cursor.getString(14));
                if (cursor.getString(15) != null) {
                    opt.setTax(new BigDecimal(cursor.getString(15)));
                }
                opt.setUom(cursor.getString(17));
                opt.setQtyP(new BigDecimal(cursor.getString(18)));
                opt.setAmountP(new BigDecimal(cursor.getString(19)));

                list.add(opt);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<OptionFreeGoods> getOptionFreeGoodsBy4Id(String idTo, String jj, String klas, String top) {
        ArrayList<OptionFreeGoods> list = new ArrayList<>();
        Cursor cursor;
        if (klas != null) {
            String selectQuery = "SELECT * FROM " + TABLE_FREE_GOODS
                    + " WHERE " + KEY_ID_TO + " = ? " +
                    " AND " + KEY_JENIS_JUAL + " = ? " +
                    " AND " + KEY_KLASIFIKASI + " = ? " +
                    " AND " + KEY_TOP + " = ? ";
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{idTo, jj, klas, top});
        } else {
            String selectQuery = "SELECT * FROM " + TABLE_FREE_GOODS
                    + " WHERE " + KEY_ID_TO + " = ? " +
                    " AND " + KEY_JENIS_JUAL + " = ? " +
                    " AND " + KEY_TOP + " = ? ";
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, new String[]{idTo, jj, top});
        }

        if (cursor.moveToFirst()) {
            do {
                OptionFreeGoods opt = new OptionFreeGoods();
                opt.setId(cursor.getString(1));
                opt.setTableName(cursor.getString(10));
                opt.setItem_nr(cursor.getString(16));
                opt.setDoc_no(cursor.getString(6));
                opt.setCond_type(cursor.getString(5));
                opt.setId_material(cursor.getString(4));
                if (cursor.getString(9) != null) {
                    opt.setQty(new BigDecimal(cursor.getString(9)));
                }
                if (cursor.getInt(12) == 0) {
                    opt.setOne_time_discount(false);
                } else {
                    opt.setOne_time_discount(true);
                }
                opt.setKc_no(cursor.getString(7));
                opt.setKlasifikasi(cursor.getString(3));
                opt.setTop(cursor.getString(11));
                opt.setJenisJual(cursor.getString(2));
                opt.setMaterialName(cursor.getString(8));
                if (cursor.getString(13) != null) {
                    opt.setAmount(new BigDecimal(cursor.getString(13)));
                }
                opt.setAmountUom(cursor.getString(14));
                if (cursor.getString(15) != null) {
                    opt.setTax(new BigDecimal(cursor.getString(15)));
                }
                opt.setUom(cursor.getString(17));
                opt.setQtyP(new BigDecimal(cursor.getString(18)));
                opt.setAmountP(new BigDecimal(cursor.getString(19)));

                list.add(opt);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<OptionFreeGoods> getOptionFreeGoodsByIdFG(String idTo, String idFg) {
        ArrayList<OptionFreeGoods> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_FREE_GOODS
                + " WHERE " + KEY_ID_TO + " = ? AND " + KEY_ID_FREE_GOODS + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idTo, idFg});

        if (cursor.moveToFirst()) {
            do {
                OptionFreeGoods opt = new OptionFreeGoods();
                opt.setId(cursor.getString(1));
                opt.setTableName(cursor.getString(10));
                opt.setItem_nr(cursor.getString(16));
                opt.setDoc_no(cursor.getString(6));
                opt.setCond_type(cursor.getString(5));
                opt.setId_material(cursor.getString(4));
                if (cursor.getString(9) != null) {
                    opt.setQty(new BigDecimal(cursor.getString(9)));
                }
                if (cursor.getInt(12) == 0) {
                    opt.setOne_time_discount(false);
                } else {
                    opt.setOne_time_discount(true);
                }
                opt.setKc_no(cursor.getString(7));
                opt.setKlasifikasi(cursor.getString(3));
                opt.setTop(cursor.getString(11));
                opt.setJenisJual(cursor.getString(2));
                opt.setMaterialName(cursor.getString(8));
                if (cursor.getString(13) != null) {
                    opt.setAmount(new BigDecimal(cursor.getString(13)));
                }
                opt.setAmountUom(cursor.getString(14));
                if (cursor.getString(15) != null) {
                    opt.setTax(new BigDecimal(cursor.getString(15)));
                }
                opt.setUom(cursor.getString(17));
                opt.setQtyP(new BigDecimal(cursor.getString(18)));
                opt.setAmountP(new BigDecimal(cursor.getString(19)));

                list.add(opt);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public void deleteFreeGoodsByIdTo(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FREE_GOODS, KEY_ID_TO + " = ?",
                new String[]{id});
        db.close();
    }

    public void deleteFreeGoods() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_FREE_GOODS;
        db.execSQL(delete);
        db.close();
    }

    public ArrayList<String> getAllClassification() {
        ArrayList<String> list = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT " + KEY_ID_MAT_CLASS + " FROM " + TABLE_MATERIAL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return list;
    }

    public VisitOrderDetailResponse getAvailableStock(String idMaterial, String orderType) {
        String selectQuery;
        if (orderType.equals(Constants.ORDER_TAKING_TYPE)) {
            selectQuery = "SELECT " +
                    KEY_STOCK_QTY_1 + ", " +
                    KEY_STOCK_UOM_1 +
                    " FROM " + TABLE_MATERIAL +
                    " WHERE " + KEY_ID_MATERIAL + " = ?";
        } else {
            selectQuery = "SELECT " +
                    KEY_STOCK_QTY_1_C + ", " +
                    KEY_STOCK_UOM_1_C +
                    " FROM " + TABLE_MATERIAL +
                    " WHERE " + KEY_ID_MATERIAL + " = ?";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMaterial});

        VisitOrderDetailResponse material = new VisitOrderDetailResponse();
        if (cursor.moveToFirst()) {
            if (cursor.getString(0) != null) {
                material.setStockQty1(new BigDecimal(cursor.getString(0)));
            }
            material.setStockUom1(cursor.getString(1));
        }

        return material;
    }

    public int sizeReturnHeader() {
        String selectQuery = "select count(*) from " + TABLE_RETURN_HEADER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int size = 0;

        if (cursor.moveToFirst()) {
            size = cursor.getInt(0);
            return size;
        } else {
            size = 0;
        }

        return size;
    }

    public int sizeOrderHeader(String idOutlet) {
        String selectQuery = "select count(*) from " + TABLE_ORDER_HEADER + " WHERE " + KEY_ID_OUTLET + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        int size = 0;

        if (cursor.moveToFirst()) {
            size = cursor.getInt(0);
            return size;
        } else {
            size = 0;
        }

        return size;
    }

    public int sizeStoreCheckBy(String idOutlet) {
        String selectQuery = "select count(*) from " + TABLE_STORE_CHECK + " WHERE " + KEY_ID_OUTLET + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet});

        int size = 0;

        if (cursor.moveToFirst()) {
            size = cursor.getInt(0);
            return size;
        } else {
            size = 0;
        }

        return size;
    }

    public int deleteReturN(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DELETED, "1");

        // updating row
        return db.update(TABLE_RETURN_HEADER, values, KEY_ID_HEADER_RETURN + " = ? ",
                new String[]{data});
    }

    public String totalAmountPlanBy(String idOutlet, String outletDate) {
        String selectQuery = "SELECT SUM(" + KEY_PRICE + ")" +
                " FROM " + TABLE_ORDER_PLAN +
                " WHERE " + KEY_ID_OUTLET + " = ? " +
                " AND " + KEY_DATE + " = ? " +
                " AND " + KEY_DELETED + " = 'false'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, outletDate});

        String total;

        if (cursor.moveToFirst()) {
            total = cursor.getString(0);

            if (total == null) {
                total = "0";
            }
            return total;
        } else {
            total = "0";
        }

        return total;
    }

    public String getAttachmentSaved(String id) {
        String selectQuery = "SELECT " + KEY_PHOTO +
                " FROM " + TABLE_ORDER_HEADER +
                " WHERE " + KEY_ID_TO + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        String result;

        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
            return result;
        } else {
            result = Constants.NO;
        }

        return result;
    }

    public BigDecimal getConversionBy(String idMaterial, String idUom) {
        String selectQuery = "SELECT " + KEY_CONVERSION +
                " FROM " + TABLE_UOM +
                " WHERE " + KEY_ID_MATERIAL + " = ? AND " + KEY_ID_UOM + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMaterial, idUom});

        BigDecimal result = BigDecimal.ZERO;

        if (cursor.moveToFirst()) {
            result = new BigDecimal(cursor.getString(0));
            return result;
        }

        return result;
    }


    public Reason getReasonById(String id) {
        String selectQuery = "SELECT " +
                KEY_REASON_DESC + ", " +
                KEY_TYPE +
                " FROM " + TABLE_REASON +
                " WHERE " + KEY_ID_REASON + " = ?";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        Reason reason = new Reason();
        if (cursor.moveToFirst()) {
            if (cursor.getString(0) != null) {
                reason.setDesc(cursor.getString(0));
            }

            if (cursor.getString(1) != null) {
                reason.setType(cursor.getString(1));
            }
        }

        return reason;
    }

    public String getUomNameBy(String id) {
        String selectQuery = "SELECT " + KEY_UOM_NAME +
                " FROM " + TABLE_MASTER_UOM +
                " WHERE " + KEY_ID_UOM + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        String result = "-";

        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
            if (result == null) {
                result = "-";
            }
            return result;
        }

        return result;
    }

    public String getTopSap(String id) {
        String selectQuery = "SELECT " + KEY_TOP_SAP +
                " FROM " + TABLE_TOP +
                " WHERE " + KEY_ID_TOP + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        String result = "-";

        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
            if (result == null) {
                result = "-";
            }
            return result;
        }

        return result;
    }

    public Boolean checkHargaJadi(String id) {
        String selectQuery = "SELECT " + KEY_HARGA_JADI +
                " FROM " + TABLE_ORDER_HEADER +
                " WHERE " + KEY_ID_TO + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        Boolean result = false;

        if (cursor.moveToFirst()) {
            if (cursor.getString(0) != null) {
                if (cursor.getString(0).equals("1")) {
                    result = true;
                }
            }
        }

        return result;
    }

    public String getIdJj(String jj) {
        String selectQuery = "SELECT " + KEY_ID_JENIS_JUAL +
                " FROM " + TABLE_JENIS_JUAL +
                " WHERE " + KEY_NAMA_JENIS_JUAL + " = ? ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{jj});

        String result = "-";

        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
            if (result == null) {
                result = "-";
            }
            return result;
        }

        return result;
    }

    public String getKlasifikasi(String idMat) {
        String selectQuery = "SELECT " + KEY_ID_MAT_CLASS +
                " FROM " + TABLE_MATERIAL +
                " WHERE " + KEY_ID_MATERIAL + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idMat});

        String result = "-";

        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
            if (result == null) {
                result = "-";
            }
            return result;
        }

        return result;
    }

    public int sizeOrderHeader() {
        String selectQuery = "select count(*) from " + TABLE_ORDER_HEADER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int size = 0;

        if (cursor.moveToFirst()) {
            size = cursor.getInt(0);
            return size;
        } else {
            size = 0;
        }

        return size;
    }

    public String lastIndexOutlet() {
        String selectQuery = "SELECT " + KEY_ID_MASTER + " FROM " + TABLE_OUTLET + " ORDER BY " + KEY_ID_MASTER + " DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String index = "";

        if (cursor.moveToFirst()) {
            index = cursor.getString(0);
            return index;
        }

        return index;
    }

    public VisitOrderDetailResponse getCurOrderPlanBy(String idOutlet, String idMaterial) {
        String selectQuery = "SELECT " +
                KEY_QTY_1 + ", " +
                KEY_UOM_1 + ", " +
                KEY_QTY_2 + ", " +
                KEY_UOM_2 +
                " FROM " + TABLE_ORDER_PLAN +
                " WHERE " + KEY_ID_OUTLET + " = ? AND " + KEY_ID_MATERIAL + " = ? ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{idOutlet, idMaterial});

        VisitOrderDetailResponse material = new VisitOrderDetailResponse();
        if (cursor.moveToFirst()) {
            if (cursor.getString(0) != null) {
                material.setOrderPlanQty1(new BigDecimal(cursor.getString(0)));
            }
            if (cursor.getString(1) != null) {
                material.setOrderPlanUom1(cursor.getString(1));
            }
            if (cursor.getString(2) != null) {
                material.setOrderPlanQty2(new BigDecimal(cursor.getString(2)));
            }
            if (cursor.getString(3) != null) {
                material.setOrderPlanUom2(cursor.getString(3));
            }
        }

        return material;
    }

    //table fcm======
    public void addFCM(GCMResponse gcmResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID_FCM, "1");
        values.put(KEY_DESCRIPTION, gcmResponse.getDesc());
        values.put(KEY_USERNAME, gcmResponse.getUsername());

        db.insert(TABLE_FCM, null, values);
        db.close();
    }

    public GCMResponse getGCM() {
        GCMResponse fcm = new GCMResponse();

        String selectQuery = "SELECT " + KEY_DESCRIPTION + ", " + KEY_USERNAME + " FROM " + TABLE_FCM + " desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                fcm.setDesc(cursor.getString(0));
                fcm.setUsername(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return fcm;
    }

    public void deleteFCM() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_FCM;
        db.execSQL(delete);
        db.close();
    }

    //table fcm======


}

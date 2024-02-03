package id.co.qualitas.qubes.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.ImageType;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.StartVisit;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.Target;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.printer.AbstractConnector;
import id.co.qualitas.qubes.printer.BluetoothConnector;

@SuppressLint("CommitPrefEdits")
public abstract class SessionManagerQubes {
    private static final Gson gson = new Gson();
    private static final Object sync = new Object();
    private static final String PREF_VISIT_SALESMAN_REASON = "pref_visit_salesman_reason";
    private static final String PREF_ORDER = "pref_order";
    private static final String PREF_TARGET = "pref_target";
    private static final String PREF_CUSTOMER_NOO = "pref_customer_noo";
    private static final String PREF_IMAGE_TYPE = "pref_image_type";
    private static final String PREF_OUTLET_HEADER = "pref_outlet_header";
//    private static final String PREF_START_VISIT = "pref_start_visit";
    private static final String PREF_LOGIN = "pref_login";
    private static final String PREF_RETURN = "pref_return";
    private static final String PREF_ALREADY_PRINT = "pref_already_print";
    private static final String PREF_STOCK_REQUEST_HEADER = "pref_stock_request_header";
    private static final String PREF_COLLECTION_HEADER = "pref_collection_header";
    private static final String PREF_ROUTE_CUSTOMER_HEADER = "pref_route_customer_header";
    private static final String PREF_COLLECTION = "PREF_COLLECTION";
    private static final String PREF_PRINT = "PREF_PRINT";
    private static final String PREF_COLLECTION_HISTORY = "PREF_COLLECTION_HISTORY";
    private static final String KEY_VISIT_SALESMAN_REASON = "key_visit_salesman_reason";
    private static final String KEY_TARGET = "key_target";
    private static final String KEY_ORDER = "key_order";
    private static final String KEY_CUSTOMER_NOO = "key_customer_noo";
    private static final String KEY_IMAGE_TYPE = "key_image_type";
    private static final String KEY_OUTLET_HEADER = "key_outlet_header";
//    private static final String KEY_START_VISIT = "key_start_visit";
    private static final String KEY_USER_PROFILE = "key_user_profile";
    private static final String KEY_STOCK_REQUEST_HEADER = "key_stock_request_header";
    private static final String KEY_ROUTE_CUSTOMER_HEADER = "key_route_customer_header";
    private static final String KEY_COLLECTION_HEADER = "key_collection_header";
    private static final String KEY_COLLECTION = "key_collection";
    private static final String KEY_COLLECTION_HISTORY = "key_collection_history";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_URL = "key_url";
    private static final String KEY_IMEI = "key_imei";
    private static final String KEY_RETURN = "key_return";
    private static final String KEY_ALREADY_PRINT = "key_already_print";
    private static final String KEY_PRINT = "key_print";
    private static SharedPreferences visitSalesmanReasonPrefs;
    private static SharedPreferences orderPrefs;
    private static SharedPreferences customerNooPrefs;
    private static SharedPreferences imageTypePrefs;
    private static SharedPreferences returnPrefs;
    private static SharedPreferences outletHeaderPrefs;
//    private static SharedPreferences startDayPrefs;
    private static SharedPreferences prefs;
    private static SharedPreferences loginPrefs;
    private static SharedPreferences stockRequestHeaderPrefs;
    private static SharedPreferences collectionHeaderPrefs;
    private static SharedPreferences collectionPrefs;
    private static SharedPreferences alreadyPrintPrefs;
    private static SharedPreferences collectionHistoryPrefs;
    private static SharedPreferences routeCustomerHeaderPrefs;
    private static SharedPreferences targetPrefs;
    private static SharedPreferences printerPrefs;

    public static void init(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        targetPrefs = context.getSharedPreferences(PREF_TARGET, Context.MODE_PRIVATE);
        loginPrefs = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        stockRequestHeaderPrefs = context.getSharedPreferences(PREF_STOCK_REQUEST_HEADER, Context.MODE_PRIVATE);
        collectionHistoryPrefs = context.getSharedPreferences(PREF_COLLECTION_HISTORY, Context.MODE_PRIVATE);
        collectionHeaderPrefs = context.getSharedPreferences(PREF_COLLECTION_HEADER, Context.MODE_PRIVATE);
        collectionPrefs = context.getSharedPreferences(PREF_COLLECTION, Context.MODE_PRIVATE);
        routeCustomerHeaderPrefs = context.getSharedPreferences(PREF_ROUTE_CUSTOMER_HEADER, Context.MODE_PRIVATE);
//        startDayPrefs = context.getSharedPreferences(PREF_START_VISIT, Context.MODE_PRIVATE);
        outletHeaderPrefs = context.getSharedPreferences(PREF_OUTLET_HEADER, Context.MODE_PRIVATE);
        imageTypePrefs = context.getSharedPreferences(PREF_IMAGE_TYPE, Context.MODE_PRIVATE);
        customerNooPrefs = context.getSharedPreferences(PREF_CUSTOMER_NOO, Context.MODE_PRIVATE);
        returnPrefs = context.getSharedPreferences(PREF_RETURN, Context.MODE_PRIVATE);
        orderPrefs = context.getSharedPreferences(PREF_ORDER, Context.MODE_PRIVATE);
        visitSalesmanReasonPrefs = context.getSharedPreferences(PREF_VISIT_SALESMAN_REASON, Context.MODE_PRIVATE);
        alreadyPrintPrefs = context.getSharedPreferences(PREF_ALREADY_PRINT, Context.MODE_PRIVATE);
        printerPrefs = context.getSharedPreferences(PREF_PRINT, Context.MODE_PRIVATE);
    }

    public static void setPrinter(String param) {
        if (param != null) {
            synchronized (sync) {
                printerPrefs.edit().putString(KEY_PRINT, gson.toJson(param)).apply();
            }
        }
    }

//    public static void setStartDay(StartVisit param) {
//        if (param != null) {
//            synchronized (sync) {
//                startDayPrefs.edit().putString(KEY_START_VISIT, gson.toJson(param)).apply();
//            }
//        }
//    }

    public static void setUrl(String url) {
        synchronized (sync) {
            prefs.edit().putString(KEY_URL, url).apply();
        }
    }

    public static void setTargetDashboard(List<Target> param) {
        synchronized (sync) {
            targetPrefs.edit().putString(KEY_TARGET, gson.toJson(param)).apply();
        }
    }

    public static void setVisitSalesmanReason(List<VisitSalesman> param) {
        synchronized (sync) {
            visitSalesmanReasonPrefs.edit().putString(KEY_VISIT_SALESMAN_REASON, gson.toJson(param)).apply();
        }
    }

    public static void setReturn(List<Material> param) {
        synchronized (sync) {
            returnPrefs.edit().putString(KEY_RETURN, gson.toJson(param)).apply();
        }
    }

    public static void setOrder(Order param) {
        if (param != null) {
            synchronized (sync) {
                orderPrefs.edit().putString(KEY_ORDER, gson.toJson(param)).apply();
            }
        }
    }

    public static void setCustomerNoo(Customer param) {
        if (param != null) {
            synchronized (sync) {
                customerNooPrefs.edit().putString(KEY_CUSTOMER_NOO, gson.toJson(param)).apply();
            }
        }
    }

    public static void setImageType(ImageType param) {
        if (param != null) {
            synchronized (sync) {
                imageTypePrefs.edit().putString(KEY_IMAGE_TYPE, gson.toJson(param)).apply();
            }
        }
    }

    public static void setOutletHeader(Customer param) {
        if (param != null) {
            synchronized (sync) {
                outletHeaderPrefs.edit().putString(KEY_OUTLET_HEADER, gson.toJson(param)).apply();
            }
        }
    }

    public static void setUserProfile(User param) {
        if (param != null) {
            synchronized (sync) {
                loginPrefs.edit().putString(KEY_USER_PROFILE, gson.toJson(param)).apply();
            }
        }
    }

    public static void setStockRequestHeader(StockRequest param) {
        if (param != null) {
            synchronized (sync) {
                stockRequestHeaderPrefs.edit().putString(KEY_STOCK_REQUEST_HEADER, gson.toJson(param)).apply();
            }
        }
    }

    public static void setRouteCustomerHeader(Customer param) {
        if (param != null) {
            synchronized (sync) {
                routeCustomerHeaderPrefs.edit().putString(KEY_ROUTE_CUSTOMER_HEADER, gson.toJson(param)).apply();
            }
        }
    }

    public static void setCollectionHeader(Invoice param) {
        if (param != null) {
            synchronized (sync) {
                collectionHeaderPrefs.edit().putString(KEY_COLLECTION_HEADER, gson.toJson(param)).apply();
            }
        }
    }

    public static void setCollectionHistoryHeader(CollectionHeader param) {
        if (param != null) {
            synchronized (sync) {
                collectionHistoryPrefs.edit().putString(KEY_COLLECTION_HISTORY, gson.toJson(param)).apply();
            }
        }
    }

    public static void setCollectionSource(int param) {
        synchronized (sync) {
            collectionPrefs.edit().putString(KEY_COLLECTION, gson.toJson(param)).apply();
        }
    }

    public static void setToken(String token) {
        synchronized (sync) {
            loginPrefs.edit().putString(KEY_TOKEN, token).apply();
        }
    }

    public static void setImei(String imei) {
        synchronized (sync) {
            loginPrefs.edit().putString(KEY_IMEI, imei).apply();
        }
    }

    public static void setAlreadyPrint(boolean alreadyPrint) {
        synchronized (sync) {
            alreadyPrintPrefs.edit().putBoolean(KEY_ALREADY_PRINT, alreadyPrint).apply();
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------

    public static List<VisitSalesman> getVisitSalesmanReason() {
        return new LinkedList<>(Arrays.asList(gson.fromJson(visitSalesmanReasonPrefs.getString(KEY_VISIT_SALESMAN_REASON, null), VisitSalesman[].class)));
    }

    public static List<Target> getTarget() {
        return new LinkedList<>(Arrays.asList(gson.fromJson(targetPrefs.getString(KEY_TARGET, null), Target[].class)));
    }

    public static List<Material> getReturn() {
        return new LinkedList<>(Arrays.asList(gson.fromJson(returnPrefs.getString(KEY_RETURN, null), Material[].class)));
    }

    public static Invoice getCollectionHeader() {
        return gson.fromJson(collectionHeaderPrefs.getString(KEY_COLLECTION_HEADER, null), Invoice.class);
    }

    public static CollectionHeader getCollectionHistoryHeader() {
        return gson.fromJson(collectionHistoryPrefs.getString(KEY_COLLECTION_HISTORY, null), CollectionHeader.class);
    }

    public static StockRequest getStockRequestHeader() {
        return gson.fromJson(stockRequestHeaderPrefs.getString(KEY_STOCK_REQUEST_HEADER, null), StockRequest.class);
    }

    public static Customer getRouteCustomerHeader() {
        return gson.fromJson(routeCustomerHeaderPrefs.getString(KEY_ROUTE_CUSTOMER_HEADER, null), Customer.class);
    }

    public static String getToken() {
        return loginPrefs.getString(KEY_TOKEN, null);
    }

    public static boolean getAlreadyPrint() {
        return alreadyPrintPrefs.getBoolean(KEY_ALREADY_PRINT, false);
    }

    public static int getCollectionSource() {
        return Integer.parseInt(collectionPrefs.getString(KEY_COLLECTION, null));
    }

    public static User getUserProfile() {
        return gson.fromJson(loginPrefs.getString(KEY_USER_PROFILE, null), User.class);
    }

    public static Customer getOutletHeader() {
        return gson.fromJson(outletHeaderPrefs.getString(KEY_OUTLET_HEADER, null), Customer.class);
    }

    public static ImageType getImageType() {
        return gson.fromJson(imageTypePrefs.getString(KEY_IMAGE_TYPE, null), ImageType.class);
    }

    public static Customer getCustomerNoo() {
        return gson.fromJson(customerNooPrefs.getString(KEY_CUSTOMER_NOO, null), Customer.class);
    }

    public static Order getOrder() {
        return gson.fromJson(orderPrefs.getString(KEY_ORDER, null), Order.class);
    }

//    public static StartVisit getStartDay() {
//        return gson.fromJson(startDayPrefs.getString(KEY_START_VISIT, null), StartVisit.class);
//    }

    public static String getPrinter() {
        return printerPrefs.getString(KEY_PRINT, null);
    }

    public static String getImei() {
        return loginPrefs.getString(KEY_IMEI, null);
    }

    public static String getUrl() {
        return prefs.getString(KEY_URL, Constants.URL);
    }

    //------------------------------------------------------------------------------

    public static void clearAllSession() {
        clearCollectionHistorySession();
        clearReturnSession();
        clearAlreadyPrintSession();
        clearImageTypeSession();
        clearCustomerNooSession();
        clearOrderSession();
        clearOutletHeaderSession();
//        clearStartDaySession();
        clearPrinterSession();
        clearLoginSession();
        clearStockRequestHeaderSession();
        clearCollectionHeaderSession();
        clearRouteCustomerHeaderSession();
        clearVisitSalesmanReasonSession();
        clearTargetSession();
    }

    public static void clearCollectionHistorySession() {
        collectionHistoryPrefs.edit().clear().apply();
    }

    public static void clearVisitSalesmanReasonSession() {
        visitSalesmanReasonPrefs.edit().clear().apply();
    }

    public static void clearReturnSession() {
        returnPrefs.edit().clear().apply();
    }

    public static void clearTargetSession() {
        targetPrefs.edit().clear().apply();
    }

    public static void clearAlreadyPrintSession() {
        alreadyPrintPrefs.edit().clear().apply();
    }

    public static void clearImageTypeSession() {
        imageTypePrefs.edit().clear().apply();
    }

    public static void clearCustomerNooSession() {
        customerNooPrefs.edit().clear().apply();
    }

    public static void clearOrderSession() {
        orderPrefs.edit().clear().apply();
    }

    public static void clearOutletHeaderSession() {
        outletHeaderPrefs.edit().clear().apply();
    }

//    public static void clearStartDaySession() {
//        startDayPrefs.edit().clear().apply();
//    }

    public static void clearPrinterSession() {
        printerPrefs.edit().clear().apply();
    }

    public static void clearLoginSession() {
        loginPrefs.edit().clear().apply();
    }

    public static void clearStockRequestHeaderSession() {
        stockRequestHeaderPrefs.edit().clear().apply();
    }

    public static void clearCollectionHeaderSession() {
        collectionHeaderPrefs.edit().clear().apply();
    }

    public static void clearRouteCustomerHeaderSession() {
        routeCustomerHeaderPrefs.edit().clear().apply();
    }
}

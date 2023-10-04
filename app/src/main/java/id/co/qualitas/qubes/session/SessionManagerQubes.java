package id.co.qualitas.qubes.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.model.Invoice;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;

@SuppressLint("CommitPrefEdits")
public abstract class SessionManagerQubes {
    private static final Gson gson = new Gson();
    private static final Object sync = new Object();

    private static final String PREF_LOGIN = "pref_login";
    private static final String PREF_STOCK_REQUEST_HEADER = "pref_stock_request_header";
    private static final String PREF_COLLECTION_HEADER = "pref_collection_header";
    private static final String PREF_COLLECTION_SOURCE = "pref_collection_source";
    private static final String KEY_USER_PROFILE = "key_user_profile";
    private static final String KEY_STOCK_REQUEST_HEADER = "key_stock_request_header";
    private static final String KEY_COLLECTION_HEADER = "key_collection_header";
    private static final String KEY_COLLECTION_SOURCE = "key_collection_source";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_URL = "key_url";
    private static final String KEY_IMEI = "key_imei";

    private static SharedPreferences prefs;
    private static SharedPreferences loginPrefs;
    private static SharedPreferences stockRequestHeaderPrefs;
    private static SharedPreferences collectionHeaderPrefs;
    private static SharedPreferences collectionSourcePrefs;

    public static void init(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        loginPrefs = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        stockRequestHeaderPrefs = context.getSharedPreferences(PREF_STOCK_REQUEST_HEADER, Context.MODE_PRIVATE);
        collectionHeaderPrefs = context.getSharedPreferences(PREF_COLLECTION_HEADER, Context.MODE_PRIVATE);
        collectionSourcePrefs = context.getSharedPreferences(PREF_COLLECTION_SOURCE, Context.MODE_PRIVATE);
    }

    public static void setUrl(String url) {
        synchronized (sync) {
            prefs.edit().putString(KEY_URL, url).apply();
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

    public static void setCollectionHeader(Invoice param) {
        if (param != null) {
            synchronized (sync) {
                collectionHeaderPrefs.edit().putString(KEY_COLLECTION_HEADER, gson.toJson(param)).apply();
            }
        }
    }

    public static void setCollectionSource(int param) {
        synchronized (sync) {
            collectionSourcePrefs.edit().putString(KEY_COLLECTION_SOURCE, gson.toJson(param)).apply();
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

    //---------------------------------------------------------------------------------------------------------------------------------------

    public static Invoice getCollectionHeader() {
        return gson.fromJson(collectionHeaderPrefs.getString(KEY_COLLECTION_HEADER, null), Invoice.class);
    }

    public static StockRequest getStockRequestHeader() {
        return gson.fromJson(stockRequestHeaderPrefs.getString(KEY_STOCK_REQUEST_HEADER, null), StockRequest.class);
    }

    public static String getToken() {
        return loginPrefs.getString(KEY_TOKEN, null);
    }

    public static int getCollectionSource() {
        return Integer.parseInt(collectionSourcePrefs.getString(KEY_COLLECTION_SOURCE, null));
    }

    public static User getUserProfile() {
        return gson.fromJson(loginPrefs.getString(KEY_USER_PROFILE, null), User.class);
    }

    public static String getImei() {
        return loginPrefs.getString(KEY_IMEI, null);
    }

    public static String getUrl() {
        return prefs.getString(KEY_URL, Constants.URL);
    }

    //------------------------------------------------------------------------------

    public static void clearLoginSession() {
        loginPrefs.edit().clear().apply();
    }

    public static void clearStockRequestHeaderSession() {
        stockRequestHeaderPrefs.edit().clear().apply();
    }

    public static void clearInvoiceHeaderSession() {
        collectionHeaderPrefs.edit().clear().apply();
    }
}

package id.co.qualitas.qubes.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map;

import id.co.qualitas.qubes.constants.Constants;

//import android.support.multidex.MultiDex;


public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;
    private SharedPreferences prefProfile;
    private SharedPreferences prefRememberPassword;
    private SharedPreferences prefURL;
    private SharedPreferences prefDate;
    private SharedPreferences prefLog;

    // Editor for Shared preferences
    private Editor editor;
    private Editor editorProfile;
    private Editor editorRememberPassword;
    private Editor editorURL;
    private Editor editorDate;
    private Editor editorLog;

    // Context
    private Context _context;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Constants.PREF_NAME, PRIVATE_MODE);
        prefRememberPassword = _context.getSharedPreferences(Constants.PREF_REMEMBERME, PRIVATE_MODE);
        prefProfile = _context.getSharedPreferences(Constants.PREF_PROFILE, PRIVATE_MODE);
        prefURL = _context.getSharedPreferences(Constants.PREF_NAME_URL, PRIVATE_MODE);
        prefDate = _context.getSharedPreferences(Constants.PREF_DATE, PRIVATE_MODE);
        prefLog = _context.getSharedPreferences(Constants.PREF_LOG, PRIVATE_MODE);

        editor = pref.edit();
        editorProfile= prefProfile.edit();
        editorURL = prefURL.edit();
        editorRememberPassword = prefRememberPassword.edit();
        editorDate = prefDate.edit();
        editorLog = prefLog.edit();
    }

    public SessionManager() {
        editor = pref.edit();
        editorProfile= prefProfile.edit();
        editorURL = prefURL.edit();
        editorRememberPassword = prefRememberPassword.edit();
        editorDate = prefDate.edit();
        editorLog = prefLog.edit();
    }

    public void createLoginSession(String ex) {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // Storing email in pref
        editor.putBoolean(Constants.IS_LOGIN, true);
        editor.putString(Constants.KEY_USER, ex);
        // commit changes
        editor.commit();
    }

    public void createRememberPassword(String email, String password, String clientID) {
        editorRememberPassword.clear();
        editorRememberPassword.commit();
        editorRememberPassword.putBoolean(Constants.IS_REMEMBERME, true);
        editorRememberPassword.putString(Constants.KEY_REMEMBERME_PASSWORD, password);
        editorRememberPassword.putString(Constants.KEY_REMEMBERME_EMAIL, email);
        editorRememberPassword.putString(Constants.KEY_REMEMBERME_CLIENTID, clientID);
        editorRememberPassword.commit();
    }

    public void createProfile(String dataResponse) {
        editorProfile.clear();
        editorProfile.commit();
        editorProfile.putBoolean(Constants.IS_PROFILE, true);
        editorProfile.putString(Constants.KEY_PROFILE, dataResponse);

        editorProfile.commit();
    }
    public void createUrlSession(String url) {
        editorURL.clear();
        editorURL.commit();
        editorURL.putBoolean(Constants.IS_URL, true);
        editorURL.putString(Constants.KEY_URL, url);
        editorURL.commit();
    }
    public void createDateSession(String url) {
        editorDate.clear();
        editorDate.commit();
        editorDate.putBoolean(Constants.IS_DATE, true);
        editorDate.putString(Constants.KEY_DATE, url);
        editorDate.commit();
    }
    /**
     * Get stored session data
     */

    public Map<String, String> getRememberMeDetails() {
        HashMap<String, String> rememberMe = new HashMap<>();
        rememberMe.put(Constants.KEY_REMEMBERME_EMAIL,
                prefRememberPassword.getString(Constants.KEY_REMEMBERME_EMAIL, null));
        rememberMe.put(Constants.KEY_REMEMBERME_PASSWORD,
                prefRememberPassword.getString(Constants.KEY_REMEMBERME_PASSWORD, null));
        rememberMe.put(Constants.KEY_REMEMBERME_CLIENTID,
                prefRememberPassword.getString(Constants.KEY_REMEMBERME_CLIENTID, null));
        return rememberMe;
    }

    public Map<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        // user email id
        user.put(Constants.KEY_USER, pref.getString(Constants.KEY_USER, null));
        // return user
        return user;
    }

    public Map<String, String> getProfileDetail() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Constants.KEY_PROFILE, prefProfile.getString(Constants.KEY_PROFILE, null));
        return user;
    }

    public Map<String, String> getUrl() {
        HashMap<String, String> url = new HashMap<>();
        url.put(Constants.KEY_URL,
                prefURL.getString(Constants.KEY_URL, null));
        return url;
    }
    public Map<String, String> getDate() {
        HashMap<String, String> url = new HashMap<>();
        url.put(Constants.KEY_DATE,
                prefDate.getString(Constants.KEY_DATE, null));
        return url;
    }
    /**
     * Clear session details
     */
    public void clearRememberMe() {
        editorRememberPassword.clear();
        editorRememberPassword.commit();
    }

    public void clearProfile() {
        editorProfile.clear();
        editorProfile.commit();
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        editor.putBoolean(Constants.IS_LOGIN, false);
    }

    /**
     *
     * Quick check for login
     * *
     */
    // Get LoginRequest State
    public boolean isLoggedIn() {
        return pref.getBoolean(Constants.IS_LOGIN, false);
    }

    public boolean isUrlEmpty() {
        return prefURL.getBoolean(Constants.IS_URL, false);
    }


}


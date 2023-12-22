package id.co.qualitas.qubes.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.CameraActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Role;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.utils.Utils;

public class Helper extends BaseFragment {
    public static int totalItem;
    public static String customerNo;
    public static String dialogCase = "";
    public static String materialName = "";
    private static Map<String, Object> param = new HashMap<String, Object>();
    private static Map<String, Object> cart = new HashMap<String, Object>();
    public static User user;
    public static long tempTime = 0;
    public static long valTime = 0;

    public static boolean resume = false;
    public static int tempMinute = 0;
    public static int tempSecond = 0;
    public static String lastTimePause;
    public static String mm, ss;

    public static Date date;

    public static Map<String, Object> getParam() {
        return param;
    }

    public static Object getItemParam(String key) {
        return param.get(key);
    }

    public static void setItemParam(String key, Object object) {
        param.put(key, object);
    }

    public static void removeItemParam(String key) {
        param.remove(key);
    }

    public static void trustSSL() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

//            public void checkClientTrusted(X509Certificate[] certs, String authType) {
//            }
//
//            public void checkServerTrusted(X509Certificate[] certs, String authType) {
//            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = null; // Add in try catch block if you get error.
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom()); // Add in try catch block if you get error.
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmptyEditText(Object obj) {
        boolean bool = false;
        if (obj instanceof EditText) {
            if (TextUtils.isEmpty(((EditText) obj).getText()))
                bool = true;
        }
        return bool;
    }

    public static String isEmpty(String input, String placeHolder) {
        if (input != null) {
            if (input.length() != 0) {
                return input;
            } else {
                return placeHolder;
            }
        } else {
            return placeHolder;
        }
//        return input != null ? input : placeHolder;
    }

    public static boolean isEmpty(Object obj) {
        if (obj != null)
            return false;
        return true;
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

    public static int getWitdh(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return width;
    }
    public static Calendar todayDate() {
        Calendar c = Calendar.getInstance();
        return c;
    }

    public static String todayDateAddDate(String format, int add) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, add);
        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static Object getWebservice(String url, Class<?> responseType) {
        int flag = 0;

        HttpEntity<?> response = null;
//        while (flag == 0) {
        flag = 1;
        //kalo get,hanya menerima data dari back end
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        String token = (String) Helper.getItemParam(Constants.TOKEN);
        String bearerToken = Constants.BEARER.concat(token.toString());
        requestHeaders.set("Authorization", bearerToken);

        HttpEntity<?> entity = new HttpEntity<Object>(requestHeaders);
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        restTemplate.getMessageConverters().add(gsonHttpMessageConverter);
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        } catch (Exception e) {
            if (e.getMessage().equals("401 Unauthorized"))
                flag = 0;

        }
//        }
        return response.getBody();
    }

    public static void postWebserviceWithBodyWOReturn(String url, Class<?> responseType, Object body) {
        //kalo post, kita kasih class k back end
        int flag = 0;
        ResponseEntity<?> responseEntity = null;
        while (flag == 0) {
            flag = 1;

            String token = (String) Helper.getItemParam(Constants.TOKEN);
            String bearerToken = Constants.BEARER.concat(token);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            requestHeaders.set("Authorization", bearerToken);
            HttpEntity<?> requestEntity = new HttpEntity<>(body, requestHeaders);
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            restTemplate.setMessageConverters(messageConverters);
//            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
            } catch (Exception e) {
                Helper.removeItemParam(Constants.ERROR_LOG);
                if (e.getMessage() != null) {
                    Helper.setItemParam(Constants.ERROR_LOG, e.getMessage());
                }
                if (e.getMessage().equals("401 Unauthorized"))
                    flag = 0;
            }
        }
    }

    public static Object postWebserviceWithBody(String url, Class<?> responseType, Object body) {
        //kalo post, kita kasih class k back end
        int flag = 0;
        ResponseEntity<?> responseEntity = null;
        while (flag == 0) {
            flag = 1;

            String token = (String) Helper.getItemParam(Constants.TOKEN);
            String bearerToken = Constants.BEARER.concat(token);
//            String bearerToken = Constants.BEARER.concat("3e7bcbc2-fb8e-47a0-8270-671796ebc1ce");

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            requestHeaders.set("Authorization", bearerToken);
            HttpEntity<?> requestEntity = new HttpEntity<>(body, requestHeaders);
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            restTemplate.setMessageConverters(messageConverters);
//            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
            } catch (Exception e) {
                Helper.removeItemParam(Constants.ERROR_LOG);
                if (e.getMessage() != null) {
                    Helper.setItemParam(Constants.ERROR_LOG, e.getMessage());
                }
                if (e.getMessage().equals("401 Unauthorized"))
                    flag = 0;
            }
        }
        return responseEntity.getBody();
    }

    public static Object postWebserviceWithBodyWithoutHeaders(String url, Class<?> responseType, Object body) {
        int flag = 0;
        ResponseEntity<?> responseEntity = null;
        flag = 1;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        HttpEntity<?> requestEntity = new HttpEntity<Object>(body, requestHeaders);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
//        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
        } catch (Exception e) {
            Log.e("worker", e.getMessage());
            if (e.getMessage().equals("401 Unauthorized")) {

            }
        }

        return responseEntity != null ? responseEntity.getBody() : null;
    }

    public static Object postWebserviceLogin(String url, Class<?> responseType, Object body) {
        //kalo post, kita kasih class k back end
        int flag = 0;
        ResponseEntity<?> responseEntity = null;
        while (flag == 0) {
            flag = 1;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            requestHeaders.set("Authorization", Constants.AUTHORIZATION_LOGIN);
            requestHeaders.set("Content-Type", Constants.HTTP_HEADER_CONTENT_TYPE);
//            requestHeaders.set("grant_type", "password");

            HttpEntity<?> requestEntity = new HttpEntity<>(body, requestHeaders);
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            restTemplate.setMessageConverters(messageConverters);
//            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
            } catch (Exception e) {
                if (e.getMessage().contains("400")) {
                    Helper.setItemParam(Constants.ERROR_LOGIN, "1");
                }
                if (e.getMessage().equals("401 Unauthorized")) ;
            }

        }
        return responseEntity.getBody();
    }


    public static Object stringToObject(String str) {
        try {
            return new ObjectInputStream(new Base64InputStream(
                    new ByteArrayInputStream(str.getBytes()), Base64.NO_PADDING
                    | Base64.NO_WRAP)).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getTime() {
        Calendar calendar = Helper.todayDate();
        String time = String.valueOf(calendar.get(Calendar.HOUR)) + " : " + String.valueOf(calendar.get(Calendar.MINUTE));
        return time;
    }

    public static String objectToString(Serializable obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(
                    new Base64OutputStream(baos, Base64.DEFAULT));
            oos.writeObject(obj);
            oos.close();
            return baos.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String changeDateFormat(String temp) {
        Date dateTimesheet = null;
        try {
            dateTimesheet = new SimpleDateFormat("yyyy-MM-dd").parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateTimesheetString2 = new SimpleDateFormat("dd MMMM yyyy").format(dateTimesheet);
        return dateTimesheetString2;
    }


    public static String toRupiahFormat(String nominal) {
        String rupiah = "";
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat
                .getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        kursIndonesia.setMaximumFractionDigits(0);
        kursIndonesia.setMinimumFractionDigits(0);
        formatRp.setCurrencySymbol("");
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        try {
            rupiah = kursIndonesia.format(Double.parseDouble(nominal));
        } catch (NumberFormatException ignored) {

        }

        return rupiah;
    }

    public static String toRupiahFormat(String nominal, char formatSeparator) {
        String rupiah = "";
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat
                .getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        kursIndonesia.setMaximumFractionDigits(0);
        kursIndonesia.setMinimumFractionDigits(0);
        formatRp.setCurrencySymbol("");
        formatRp.setGroupingSeparator(formatSeparator);

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        try {
            rupiah = kursIndonesia.format(Double.parseDouble(nominal));
        } catch (NumberFormatException e) {

        }

        return rupiah;
    }

    public static String toRupiahFormat2(String nominal) {
        String part1 = "";
        String part2 = "";
        String rupiah = "";
        if (nominal.contains(".")) {
            String[] parts = nominal.split("[.]");
            part1 = parts[0];
            part2 = parts[1];
        }

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat
                .getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        kursIndonesia.setMaximumFractionDigits(0);
        kursIndonesia.setMinimumFractionDigits(0);
        formatRp.setCurrencySymbol("");
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        try {
            if (!part2.equals("")) {
                rupiah = kursIndonesia.format(Double.parseDouble(nominal)).concat(",").concat(String.valueOf(part2));
            } else {
                rupiah = kursIndonesia.format(Double.parseDouble(nominal));
            }
        } catch (NumberFormatException e) {

        }
        return rupiah;
    }

    public static void setDotCurrency(EditText e, TextWatcher w, Editable s) {
        e.removeTextChangedListener(w);

        try {
            String originalString = s.toString();

            Long longval;
            if (originalString.contains(",")) {
                originalString = originalString.replaceAll(",", "");
            }
            longval = Long.parseLong(originalString);

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
            formatter.applyPattern("#,###,###,###");
            String formattedString = formatter.format(longval);

            //setting text after format to EditText
            e.setText(formattedString);
            e.setSelection(e.getText().length());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        e.addTextChangedListener(w);
    }

    public static String setDotCurrencyAmount(double amount) {
        String part1 = "";
        String part2 = "";
        try {
            String orig = String.valueOf(amount);
            if (orig.contains(".")) {
                String[] parts = orig.split("[.]");
                part1 = parts[0];
                part2 = parts[1];
            }

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
            formatter.applyPattern("#,###,###,###");
            Long longval = Long.parseLong(part1);
            String formattedString = formatter.format(longval);
            return formattedString;
        } catch (
                NumberFormatException nfe) {
            nfe.printStackTrace();
            return "0";
        }
    }

    public static String getPath(Uri uri, Activity activity) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    public static <T> T ObjectToGSON(Object object, Class<T> responseType) {
        final Gson gson = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        String json = gson.toJson(object);
        return gson.fromJson(json, responseType);
    }

    public static String convertDateToString(String format, Date temp) {
        String dateTimesheetString2 = new SimpleDateFormat(format).format(temp);
        return dateTimesheetString2;
    }

    public static Date convertStringtoDate(String format, String temp) {
        Date dateTimesheet = null;
        try {
            dateTimesheet = new SimpleDateFormat(format, Locale.getDefault()).parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimesheet;
    }

    public static Date convertStringtoDateUS(String format, String temp) {
        Date dateTimesheet = null;
        try {
            dateTimesheet = new SimpleDateFormat(format, Locale.US).parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimesheet;
    }

    public static String parseDateString(String time, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String convertDateToStringNew(String format, Date temp) {
        String dateTimesheetString2 = new SimpleDateFormat(format, Locale.getDefault()).format(temp);
        return dateTimesheetString2;
    }

    public static String changeDateFormat(String before, String after, String temp) {
        Date dateTimesheet = null;
        try {
            dateTimesheet = new SimpleDateFormat(before, Locale.US).parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateTimesheetString2 = new SimpleDateFormat(after, Locale.US).format(dateTimesheet);
        return dateTimesheetString2;
    }

    public static String changeDateFormatDefa(String before, String after, String temp) {
        Date dateTimesheet = null;
        try {
            dateTimesheet = new SimpleDateFormat(before, Locale.getDefault()).parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateTimesheetString2 = new SimpleDateFormat(after, Locale.getDefault()).format(dateTimesheet);
        return dateTimesheetString2;
    }

    public static String getTotalPrice(String price, String disc, String ppn) {
        Double d1 = Double.parseDouble(price);
        Double d2 = Double.parseDouble(disc);
        Double dHasil = d1 - d2;
        String sHasil = String.valueOf(String.format("%.0f", dHasil));
        return sHasil;
    }

    public static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    // && !BigDecimal.valueOf(Long.parseLong(response)).equals(BigDecimal.ZERO)
    public static String validateResponseEmpty(String response) {
        String result;

        if (response != null && !response.equals(Constants.NULL)) {
            result = response;
        } else {
            result = "-";
        }

        return result;
    }

    public static BigDecimal validateAmountLessThanZero(BigDecimal temp) {
        if (temp.compareTo(BigDecimal.ZERO) == -1) {
            temp = BigDecimal.ZERO;
        }

        return temp;
    }

    public static long convertDateToLong(String input, String format) {
        long milliseconds = 0;
        SimpleDateFormat f = new SimpleDateFormat(format);
        try {
            Date d = f.parse(input);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return milliseconds;
    }

    public static BigDecimal convertQtyUom(BigDecimal beginQty, BigDecimal beginConv, BigDecimal targetConv) {
        BigDecimal convResult = BigDecimal.ZERO;

        convResult = (beginQty.multiply(beginConv)).divide(targetConv);

        return convResult;
    }


    public static String ltrim(String s) {
        Pattern LTRIM = Pattern.compile("^\\s+");
        return LTRIM.matcher(s).replaceAll("");
    }

    public static String mixNumber(Date curDate) {
        return new BigInteger(String.valueOf((int) Math.floor((1 + Math.random()) * 0x1000)))
                .toString(16)
                .substring(1) + String.valueOf(curDate.getTime());
    }

    public static String getUUID() {
//        String result = UUID.nameUUIDFromBytes(text.getBytes()).toString();
        String result = UUID.randomUUID().toString();
        return result;
    }

    public static String splitIdFailed(String input, int pos) {
        return input.split(";")[pos];
    }

    public static Date getTodayDate() {
        final Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static String getTodayDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                format, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean isGPSOn(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void turnOnGPS(Activity activity) {
        Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivity(gpsOptionsIntent);
    }

    public static GeoPoint computeCentroid(List<Customer> points) {
        double latitude = 0;
        double longitude = 0;
        int n = points.size();

        for (Customer point : points) {
            latitude += point.getLatitude();
            longitude += point.getLongitude();
        }

        return new GeoPoint(latitude / n, longitude / n);
    }

    public static ArrayList<OverlayItem> setOverLayItems(List<Customer> customers, Activity activity) {
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

        List<GeoPoint> geoPointList = new ArrayList<>();

        for (Customer cust : customers) {
            OverlayItem ov = new OverlayItem(cust.getId() + "-" + cust.getNama(), cust.getAddress(), new GeoPoint(cust.getLatitude(), cust.getLongitude()));
            ov.setMarker(new BitmapDrawable(activity.getResources(), getMarkerBitmapFromView(cust, activity)));
            items.add(ov);

            geoPointList.add(new GeoPoint(cust.getLatitude(), cust.getLongitude()));
        }
        return items;
    }

    public static Bitmap getMarkerBitmapFromView(Customer cust, Activity activity) {
        //HERE YOU CAN ADD YOUR CUSTOM VIEW
        View customMarkerView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);

        //IN THIS EXAMPLE WE ARE TAKING TEXTVIEW BUT YOU CAN ALSO TAKE ANY KIND OF VIEW LIKE IMAGEVIEW, BUTTON ETC.
        TextView txt_name = customMarkerView.findViewById(R.id.txt_name);
        TextView txt_add = customMarkerView.findViewById(R.id.txt_add);
        ImageView imgStore = customMarkerView.findViewById(R.id.imgStore);

        txt_name.setText(cust.getId() + " - " + cust.getNama());
//        txt_add.setText(cust.getAddress());
        if (cust.isRoute()) {
            imgStore.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_marker_blue));
        } else {
            imgStore.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_marker_red));
        }

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null) drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }

    public static void takePhoto(Activity activity) {
        Intent camera = new Intent(activity, CameraActivity.class);
        activity.startActivityForResult(camera, Constants.REQUEST_CAMERA_CODE);
    }

    public static void removeImage(Context context, String path) {
        File fileName = new File(path);
        try {
            fileName.delete();
            Log.e("Delete img : ", String.valueOf(path).toString() + " success");
        } catch (Exception e) {
            Log.e("Delete img : ", e.getMessage());
        }
    }

    public static void deleteFolder(String path) {
        File dir = new File(path);
        try {
            FileUtils.cleanDirectory(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTodayRoute() {
        String result = "";
//        String tempDate = getTodayDate(Constants.DATE_FORMAT_3);
//        LocalDate date = LocalDate.parse(tempDate);
//        DayOfWeek day = date.getDayOfWeek();

        int weekYear = (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) % 4;
        int dayWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (weekYear == 0) {
            weekYear = 4;
        }

        if (dayWeek == 0) {
            dayWeek = 7;
            weekYear = weekYear - 1;
        }

        result = "P" + String.valueOf(weekYear) + "H" + String.valueOf(dayWeek);
        return result;
    }

    public static String getTodayRouteDouble() {
        String result = "", result2 = "";
//        String tempDate = getTodayDate(Constants.DATE_FORMAT_3);
//        LocalDate date = LocalDate.parse(tempDate);
//        DayOfWeek day = date.getDayOfWeek();

        int weekYear = (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) % 4;
        int dayWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (weekYear == 0) {
            weekYear = 4;
        }

        if (dayWeek == 0) {
            dayWeek = 7;
            weekYear = weekYear - 1;
        }

        result = "P" + String.valueOf(weekYear) + "H" + String.valueOf(dayWeek);
        result2 = "P" + String.valueOf(weekYear + 2) + "H" + String.valueOf(dayWeek);
        return result + "-" + result2;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit.equals("K")) {
            dist = dist * 1.609344;
        } else if (unit.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static String getImei(Context context) {
        String imei = null;
        try {
            imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    public static boolean checkTodayRoute(String rute) {
        String todayRute = getTodayRoute();
        if (rute != null) {
            if (rute.contains(todayRute)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmptyOrNull(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNotEmptyOrNull(Collection<?> collection) {
        return !isEmptyOrNull(collection);
    }

    public static boolean checkRadius(Location currentLocation, Location custLocation) {
        boolean outside = false;
        float radius = database.getRadius(); //in meters
        float distance = custLocation.distanceTo(currentLocation);
        if (distance < radius) {
            outside = false;
        } else {
            outside = true;
        }
        return outside;
    }

    public static File exportDB(Context context, String username) {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
//        String currentDBPath1 = "/data/data/" + context.getPackageName() + "//databases//Qubes.db";
//        String currentDBPath = getDbPath(context, "Qubes.db");
        String currentDBPath = context.getDatabasePath("Qubes").getPath();
        String backupDBPath = Utils.getDirLocPDF(context) + "/Qubes_backup_" + username + ".db";
        File currentDB = new File(currentDBPath);
        File backupDB = new File(backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "Your Database is Exported !!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return backupDB;
    }

    public static void importDB(Context context, String username) {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File sd = new File(dir);
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
//        String currentDBPath1 = "/data/data/" + context.getPackageName() + "//databases//Qubes.db";
//        String currentDBPath = getDbPath(context, "Qubes.db");
        String currentDBPath = context.getDatabasePath("Qubes").getPath();
        String backupDBPath = Utils.getDirLocPDF(context) + "/Qubes_backup_" + username + ".db";
        File currentDB = new File(currentDBPath);
        File backupDB = new File(backupDBPath);

        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "Your Database is Imported !!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static String getDbPath(Context context, String YourDbName) {
        return context.getDatabasePath(YourDbName).getAbsolutePath();
    }

    public static boolean findRole(List<Role> roleList, String param) {
        Role result = roleList.stream()
                .filter(role -> param.equals(role.getAuthority()))
                .findAny()
                .orElse(null);
        return result != null;
    }
}

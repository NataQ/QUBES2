package id.co.qualitas.qubes.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;
import android.widget.EditText;

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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.model.User;

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


    public static boolean isEmptyEditText(Object obj) {
        boolean bool = false;
        if (obj instanceof EditText) {
            if (TextUtils.isEmpty(((EditText) obj).getText()))
                bool = true;
        }
        return bool;
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

    public static Calendar todayDate() {
        Calendar c = Calendar.getInstance();
        return c;
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

    public static Object postWebserviceWithBody(String url, Class<?> responseType, Object body) {
        //kalo post, kita kasih class k back end
        int flag = 0;
        ResponseEntity<?> responseEntity = null;
        while (flag == 0) {
            flag = 1;

            String token = (String) Helper.getItemParam(Constants.TOKEN);
//            String bearerToken = Constants.BEARER.concat(token);
            String bearerToken = Constants.BEARER.concat("3e7bcbc2-fb8e-47a0-8270-671796ebc1ce");

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
                if(e.getMessage() != null){
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
        return new BigInteger(String.valueOf((int)Math.floor((1 + Math.random()) * 0x1000)))
                .toString(16)
                .substring(1) + String.valueOf(curDate.getTime());
    }

    public static String splitIdFailed(String input, int pos){
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
}

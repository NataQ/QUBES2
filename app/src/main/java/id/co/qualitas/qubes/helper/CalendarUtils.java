package id.co.qualitas.qubes.helper;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.co.qualitas.qubes.constants.Constants;

/**
 * Created by Wiliam on 8/30/2017.
 */

public class CalendarUtils {

    public static String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    public static String ConvertMilliSecondsToFormattedDate(String milliSeconds){
        Calendar calendar = Calendar.getInstance();
//        String myDate = "2014/10/29 18:10:45";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = sdf.parse(myDate);
//        long millis = date.getTime();
        try {
            calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        }catch (Exception e){
            Log.e("CAL", e.getMessage());
        }
        return simpleDateFormat.format(calendar.getTime());
    }
}
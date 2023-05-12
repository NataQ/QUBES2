package id.co.qualitas.qubes.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Wiliam on 8/30/2017.
 */

public class CalendarUtils {

    public static String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    public static String ConvertMilliSecondsToFormattedDate(String milliSeconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return simpleDateFormat.format(calendar.getTime());
    }
}
package ndk.utils;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Date_Utils {

    public static SimpleDateFormat mysql_Date_Format = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
    public static SimpleDateFormat normal_Date_Format = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
    public static SimpleDateFormat normal_Date_Format_words = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.UK);
    public static SimpleDateFormat mysql_date_time_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat normal_date_time_format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public static SimpleDateFormat normal_date_time_short_year_format = new SimpleDateFormat("dd-MM-yy HH:mm");
    public static SimpleDateFormat normal_date_format = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat normal_date_short_year_format = new SimpleDateFormat("dd-MM-yy");
    public static SimpleDateFormat normal_date_time_format_words = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm");
    public static SimpleDateFormat normal_stripped_date_format = new SimpleDateFormat("MMM dd", Locale.UK);

    public static String get_current_date_string_in_mysql_format() {
        return mysql_Date_Format.format(new Date());
    }

    //TODO : Use Apache Commons lang API
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static String date_to_mysql_date_string(Date date) {
        return mysql_Date_Format.format(date);
    }

    public static String date_to_normal_date_string(Date date) {
        return normal_Date_Format.format(date);
    }

    public static String mysql_date_string_to_date_string(String mysql_date) {
        try {
            return normal_Date_Format.format(normal_Date_Format.parse(mysql_date));
        } catch (ParseException e) {
            return normal_Date_Format.format(new Date());
        }
    }

    public static String get_current_date_string_in_normal_format() {
        return normal_Date_Format.format(new Date());
    }

    public static String get_tomorrow_date_string_in_normal_format() {
        return normal_Date_Format.format(addDays(new Date(), 1));
    }

    public static String normal_date_string_to_mysql_date_string(String normal_date, String APPLICATION_NAME) {
        try {
            return mysql_Date_Format.format(normal_Date_Format.parse(normal_date));
        } catch (ParseException e) {
            Log.d(APPLICATION_NAME, "Unable to convert Normal Date String " + normal_date + " to MySQL Date String, Error : " + e.getLocalizedMessage());
            return mysql_Date_Format.format(new Date());
        }
    }

    public static String normal_date_time_words_string_to_mysql_date_time_string(String normal_date_time_words, String APPLICATION_NAME) {
        try {
            return mysql_date_time_format.format(normal_date_time_format_words.parse(normal_date_time_words));
        } catch (ParseException e) {
            Log.d(APPLICATION_NAME, "Unable to convert Normal Date Time Words String " + normal_date_time_words + " to MySQL Date Time String, Error : " + e.getLocalizedMessage());
            return mysql_date_time_format.format(new Date());
        }
    }

    public static String mysql_date_string_to_normal_date_string(String mysql_date, String APPLICATION_NAME) {
        try {
            return normal_Date_Format.format(mysql_Date_Format.parse(mysql_date));
        } catch (ParseException e) {
            Log.d(APPLICATION_NAME, "Unable to convert MySQL Date String " + mysql_date + " to Normal Date String, Error : " + e.getLocalizedMessage());
            return normal_Date_Format.format(new Date());
        }
    }

    public static Date mysql_date_string_to_date(String mysql_date, Context context, String APPLICATION_NAME) {
        try {
            Log.d(APPLICATION_NAME, mysql_Date_Format.parse(mysql_date).toString());
            return mysql_Date_Format.parse(mysql_date);
        } catch (ParseException e) {
            Toast_Utils.longToast(context, "Date Conversion Error");
            return new Date();
        }
    }

    public static Date normal_date_string_to_date(String normal_date, Context context, String APPLICATION_NAME) {
        try {
            Log.d(APPLICATION_NAME, normal_Date_Format.parse(normal_date).toString());
            return normal_Date_Format.parse(normal_date);
        } catch (ParseException e) {
            Toast_Utils.longToast(context, "Date Conversion Error");
            return new Date();
        }
    }

    public static String date_to_mysql_date_time_string(Date date) {
        return mysql_date_time_format.format(date);
    }
}

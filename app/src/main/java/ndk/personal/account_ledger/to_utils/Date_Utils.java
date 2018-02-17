package ndk.personal.account_ledger.to_utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nabeel on 17-02-2018.
 */

public class Date_Utils extends ndk.utils.Date_Utils {
    public static SimpleDateFormat normal_date_time_format_words = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm");
    public static SimpleDateFormat normal_stripped_date_format = new SimpleDateFormat("MMM dd", Locale.UK);

    public static String date_to_mysql_date_time_string(Date date) {
        return mysql_date_time_format.format(date);
    }
}

package ndk.personal.account_ledger.activities;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;

import ndk.personal.account_ledger.constants.API;
import ndk.personal.account_ledger.constants.API_Wrapper;
import ndk.personal.account_ledger.constants.Application_Specification;
import ndk.utils_android16.Date_Utils;
import ndk.utils_android16.NetworkUtils;
import ndk.utils_android16.network_task.REST_Insert_Task_Wrapper;


class Insert_Transaction_v2_Utils {

    static void execute_insert_Transaction_Task(ProgressBar login_progress, ScrollView login_form, Context context, AppCompatActivity activity, String user_id, String purpose, Double amount, int from_selected_account_id, int to_selected_account_id, EditText edit_purpose, EditText edit_amount, Button button_date, Calendar calendar) {

        NetworkUtils.further_Actions further_actions = () -> associate_button_with_time_stamp_plus_one_minute(button_date, calendar);

        REST_Insert_Task_Wrapper.execute(context, API_Wrapper.get_http_API(API.insert_Transaction_v2), activity, login_progress, login_form, Application_Specification.APPLICATION_NAME, new Pair[]{new Pair<>("event_date_time", Date_Utils.date_to_mysql_date_time_string(calendar.getTime())), new Pair<>("user_id", user_id), new Pair<>("particulars", purpose), new Pair<>("amount", amount), new Pair<>("from_account_id", from_selected_account_id), new Pair<>("to_account_id", to_selected_account_id)}, edit_purpose, new EditText[]{edit_purpose, edit_amount}, further_actions);
    }

    private static void associate_button_with_time_stamp_plus_one_minute(Button button_date, Calendar calendar) {

        calendar.setTime(DateUtils.addMinutes(calendar.getTime(), 5));
        associate_button_with_time_stamp(button_date, calendar);
    }

    static void associate_button_with_time_stamp(Button button_date, Calendar calendar) {

        button_date.setText(Date_Utils.normal_date_time_format_words.format(calendar.getTime()));
    }
}

package ndk.personal.account_ledger.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.constants.API;
import ndk.personal.account_ledger.constants.API_Wrapper;
import ndk.personal.account_ledger.constants.Application_Specification;
import ndk.utils.Activity_Utils;
import ndk.utils.Date_Utils;
import ndk.utils.Spinner_Utils;
import ndk.utils.Validation_Utils;
import ndk.utils.network_task.REST_Insert_Task_Wrapper;

public class Insert_Transaction extends AppCompatActivity {

    Context application_context, activity_context = this;
    SharedPreferences settings;
    private ProgressBar login_progress;
    private Button button_date;
    private Spinner spinner_section;
    private EditText edit_purpose;
    private EditText edit_amount;
    private Calendar calendar = Calendar.getInstance();
    private ScrollView login_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

        application_context = getApplicationContext();

        settings = getApplicationContext().getSharedPreferences(Application_Specification.APPLICATION_NAME, Context.MODE_PRIVATE);

        login_form = findViewById(R.id.login_form);
        Button button_submit = findViewById(R.id.button_submit);
        edit_amount = findViewById(R.id.edit_amount);
        edit_purpose = findViewById(R.id.edit_purpose);
        spinner_section = findViewById(R.id.spinner_section);
        button_date = findViewById(R.id.button_date);
        login_progress = findViewById(R.id.login_progress);

        associate_button_with_time_stamp();

        // Initialize
        final SwitchDateTimeDialogFragment dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                "Pick Time",
                "OK",
                "Cancel"
        );

        // Assign values
        dateTimeFragment.startAtCalendarView();
        dateTimeFragment.set24HoursMode(true);
//        dateTimeFragment.setMaximumDateTime(calendar.getTime());

//        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
//        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(2017, Calendar.MARCH, 4, 15, 20).getTime());

        // Or assign each element, default element is the current moment
//        dateTimeFragment.setDefaultHourOfDay(15);
//        dateTimeFragment.setDefaultMinute(20);
//        dateTimeFragment.setDefaultDay(4);
//        dateTimeFragment.setDefaultMonth(Calendar.MARCH);
//        dateTimeFragment.setDefaultYear(2017);

        // Define new day and month format
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(Date_Utils.normal_stripped_date_format);
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(Application_Specification.APPLICATION_NAME, e.getMessage());
        }

        // Set listener
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {

            @Override
            public void onPositiveButtonClick(Date date) {
                // Date is get on positive button click
                calendar.set(Calendar.YEAR, dateTimeFragment.getYear());
                calendar.set(Calendar.MONTH, dateTimeFragment.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, dateTimeFragment.getDay());
                calendar.set(Calendar.HOUR_OF_DAY, dateTimeFragment.getHourOfDay());
                calendar.set(Calendar.MINUTE, dateTimeFragment.getMinute());

                associate_button_with_time_stamp();

                Log.d(Application_Specification.APPLICATION_NAME, "Slected : " + Date_Utils.date_to_mysql_date_time_string((calendar.getTime())));
                // dateTimeFragment.setDefaultDateTime(calendar.getTime());
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
            }
        });

        button_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Show
                dateTimeFragment.show(getSupportFragmentManager(), "dialog_time");
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_insert_Transaction();
            }
        });

        Spinner_Utils.attach_items_to_simple_spinner(this, spinner_section, new ArrayList<>(Arrays.asList("Debit", "Credit")));
    }

    private void associate_button_with_time_stamp() {
        button_date.setText(Date_Utils.normal_date_time_format_words.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.insert_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_item_view_pass_book) {
            Activity_Utils.start_activity_with_string_extras(activity_context, Clickable_Pass_Book_Bundle.class, new Pair[]{new Pair<>("URL", API_Wrapper.get_http_API(API.select_User_Transactions)), new Pair<>("application_name", Application_Specification.APPLICATION_NAME), new Pair<>("user_id", settings.getString("user_id", "0"))}, false, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void attempt_insert_Transaction() {
        Validation_Utils.reset_errors(new EditText[]{edit_purpose, edit_amount});
        Pair<Boolean, EditText> empty_check_result = Validation_Utils.empty_check(new Pair[]{new Pair<>(edit_amount, "Please Enter Valid Amount..."), new Pair<>(edit_purpose, "Please Enter Purpose...")});

        if (empty_check_result.first) {
            // There was an error; don't attempt login and focus the first form field with an error.
            if (empty_check_result.second != null) {
                empty_check_result.second.requestFocus();
            }
        } else {

            Pair<Boolean, EditText> zero_check_result = Validation_Utils.zero_check(new Pair[]{new Pair<>(edit_amount, "Please Enter Valid Amount...")});
            if (zero_check_result.first) {
                if (zero_check_result.second != null) {
                    zero_check_result.second.requestFocus();
                }
            } else {
                execute_insert_Transaction_Task();
            }
        }
    }

    private void execute_insert_Transaction_Task() {

        REST_Insert_Task_Wrapper.execute(this, API_Wrapper.get_http_API(API.insert_Transaction), this, login_progress, login_form, Application_Specification.APPLICATION_NAME, new Pair[]{new Pair<>("event_date_time", Date_Utils.date_to_mysql_date_time_string(calendar.getTime())), new Pair<>("user_id", settings.getString("user_id", "0")), new Pair<>("particulars", spinner_section.getSelectedItem().toString() + " : " + edit_purpose.getText().toString()), new Pair<>("amount", edit_amount.getText().toString())}, edit_purpose, new EditText[]{edit_purpose, edit_amount});
    }
}

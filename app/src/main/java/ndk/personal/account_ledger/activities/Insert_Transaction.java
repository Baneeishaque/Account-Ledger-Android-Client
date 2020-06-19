package ndk.personal.account_ledger.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.constants.Api;
import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android14.ActivityUtils;
import ndk.utils_android16.DateUtils;
import ndk.utils_android16.Spinner_Utils;
import ndk.utils_android16.ValidationUtils;
import ndk.utils_android16.network_task.RestInsertTaskWrapper;


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

        settings = getApplicationContext().getSharedPreferences(ApplicationSpecification.APPLICATION_NAME, Context.MODE_PRIVATE);

        login_form = findViewById(R.id.login_form);
        Button button_submit = findViewById(R.id.buttonSubmit);
        edit_amount = findViewById(R.id.editTextAmount);
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
            dateTimeFragment.setSimpleDateMonthAndDayFormat(DateUtils.normalStrippedDateFormat);
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(ApplicationSpecification.APPLICATION_NAME, e.getMessage());
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

                Log.d(ApplicationSpecification.APPLICATION_NAME, "Slected : " + DateUtils.dateToMysqlDateTimeString((calendar.getTime())));
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
        button_date.setText(DateUtils.normalDateTimeFormatWords.format(calendar.getTime()));
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

        if (id == R.id.menu_item_view_from_account_pass_book) {
            ActivityUtils.startActivityWithStringExtras(activity_context, ClickablePassBookBundle.class, new Pair[]{new Pair<>("URL", ApiWrapper.getHttpApi(Api.select_User_Transactions)), new Pair<>("application_name", ApplicationSpecification.APPLICATION_NAME), new Pair<>("user_id", settings.getString("user_id", "0"))});
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void attempt_insert_Transaction() {
        ValidationUtils.resetErrors(new EditText[]{edit_purpose, edit_amount});
        Pair<Boolean, EditText> empty_check_result = ValidationUtils.emptyCheckEditTextPairs(new Pair[]{new Pair<>(edit_amount, "Please Enter Valid Amount..."), new Pair<>(edit_purpose, "Please Enter Purpose...")});

        if (empty_check_result.first) {
            // There was an error; don't attempt login and focus the first form field with an error.
            if (empty_check_result.second != null) {
                empty_check_result.second.requestFocus();
            }
        } else {

            Pair<Boolean, EditText> zero_check_result = ValidationUtils.zeroCheckEditTextPairs(new Pair[]{new Pair<>(edit_amount, "Please Enter Valid Amount...")});
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

        RestInsertTaskWrapper.execute(this, ApiWrapper.getHttpApi(Api.insert_Transaction), this, login_progress, login_form, ApplicationSpecification.APPLICATION_NAME, new Pair[]{new Pair<>("event_date_time", DateUtils.dateToMysqlDateTimeString(calendar.getTime())), new Pair<>("user_id", settings.getString("user_id", "0")), new Pair<>("particulars", spinner_section.getSelectedItem().toString() + " : " + edit_purpose.getText().toString()), new Pair<>("amount", edit_amount.getText().toString())}, edit_purpose, new EditText[]{edit_purpose, edit_amount});
    }
}

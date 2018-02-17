package ndk.personal.account_ledger.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.constants.API;
import ndk.personal.account_ledger.constants.Application_Specification;
import ndk.personal.account_ledger.to_utils.Date_Utils;
import ndk.personal.account_ledger.to_utils.REST_Insert_Task;
import ndk.utils.Activity_Utils;
import ndk.utils.Spinner_Utils;
import ndk.utils.Toast_Utils;
import ndk.utils.Validation_Utils;
import ndk.utils.activities.Pass_Book;

import static ndk.utils.Network_Utils.isOnline;
import static ndk.utils.ProgressBar_Utils.showProgress;

public class Insert_Transaction extends AppCompatActivity {

    Context application_context, activity_context = this;
    REST_Insert_Task REST_Insert_Task = null;
    private android.widget.ProgressBar loginprogress;
    private android.widget.Button buttondate;
    private android.widget.Spinner spinnersection;
    private android.widget.EditText editpurpose;
    private android.widget.EditText editamount;
    private android.widget.Button buttonsubmit;
    private android.widget.ScrollView loginform;
    private Calendar calendar = Calendar.getInstance();
    private String current_date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        application_context = getApplicationContext();

        this.loginform = findViewById(R.id.login_form);
        this.buttonsubmit = findViewById(R.id.button_submit);
        this.editamount = findViewById(R.id.edit_amount);
        this.editpurpose = findViewById(R.id.edit_purpose);
        this.spinnersection = findViewById(R.id.spinner_section);
        this.buttondate = findViewById(R.id.button_date);
        this.loginprogress = findViewById(R.id.login_progress);

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
                // Do something
                calendar.set(Calendar.YEAR, dateTimeFragment.getYear());
                calendar.set(Calendar.MONTH, dateTimeFragment.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, dateTimeFragment.getDay());
                calendar.set(Calendar.HOUR_OF_DAY, dateTimeFragment.getHourOfDay());
                calendar.set(Calendar.MINUTE, dateTimeFragment.getMinute());

                associate_button_with_time_stamp();

                Log.d(Application_Specification.APPLICATION_NAME, Date_Utils.date_to_mysql_date_time_string((calendar.getTime())));
                //                dateTimeFragment.setDefaultDateTime(calendar.getTime());
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
            }
        });


        buttondate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show
                dateTimeFragment.show(getSupportFragmentManager(), "dialog_time");
            }
        });

        buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_insert_Transaction();
            }
        });

        Spinner_Utils.attach_items_to_simple_spinner(this, spinnersection, new ArrayList<>(Arrays.asList("Debit", "Credit")));

    }

    private void associate_button_with_time_stamp() {
        buttondate.setText(Date_Utils.normal_date_time_format_words.format(calendar.getTime()));
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
            Activity_Utils.start_activity_with_string_extras(activity_context, Pass_Book.class, new Pair[]{new Pair<>("URL", API.get_Android_API(API.select_User_Transactions)), new Pair<>("application_name", Application_Specification.APPLICATION_NAME), new Pair<>("user_id", "1")});
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void attempt_insert_Transaction() {
        Validation_Utils.reset_errors(new EditText[]{editpurpose, editamount});
        Pair<Boolean, EditText> empty_check_result = Validation_Utils.empty_check(new Pair[]{new Pair<>(editamount, "Please Enter Valid Amount..."), new Pair<>(editpurpose, "Please Enter Purpose...")});

        if (empty_check_result.first) {
            // There was an error; don't attempt login and focus the first form field with an error.
            if (empty_check_result.second != null) {
                empty_check_result.second.requestFocus();
            }
        } else {

            Pair<Boolean, EditText> zero_check_result = Validation_Utils.zero_check(new Pair[]{new Pair<>(editamount, "Please Enter Valid Amount...")});
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
        if (isOnline(application_context)) {
            showProgress(true, application_context, loginprogress, loginform);
            REST_Insert_Task = new REST_Insert_Task(API.get_Android_API(API.insert_Transaction), REST_Insert_Task, this, loginprogress, loginform, Application_Specification.APPLICATION_NAME, new Pair[]{new Pair<>("event_date_time", Date_Utils.date_to_mysql_date_time_string(calendar.getTime())), new Pair<>("user_id", "1"), new Pair<>("particulars", spinnersection.getSelectedItem().toString() + " : " + editpurpose.getText().toString()), new Pair<>("amount", editamount.getText().toString())}, editamount, Insert_Transaction.class, false, new EditText[]{editpurpose, editamount});
            REST_Insert_Task.execute((Void) null);
        } else {
            Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
        }
    }
}

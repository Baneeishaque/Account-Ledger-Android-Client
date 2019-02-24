package ndk.personal.account_ledger.activities;

import android.content.Context;
import android.content.Intent;
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

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.util.Calendar;
import java.util.Date;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.constants.API;
import ndk.personal.account_ledger.constants.API_Wrapper;
import ndk.personal.account_ledger.constants.Application_Specification;
import ndk.utils.Activity_Utils;
import ndk.utils.Date_Utils;
import ndk.utils.Toast_Utils;
import ndk.utils.Validation_Utils;
import ndk.utils.network_task.REST_GET_Task;

public class Insert_Transaction_v2_Quick extends AppCompatActivity {

    Context application_context;
    SharedPreferences settings;
    boolean to_account_selection_flag = true;
    String from_selected_account_id;
    String to_selected_account_id = "0";
    private ProgressBar login_progress;
    private Button button_date, button_to, button_from;
    private EditText edit_purpose;
    private EditText edit_amount;
    private Calendar calendar = Calendar.getInstance();
    private ScrollView login_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction_v2);

        application_context = getApplicationContext();

        settings = getApplicationContext().getSharedPreferences(Application_Specification.APPLICATION_NAME, Context.MODE_PRIVATE);

        login_form = findViewById(R.id.login_form);
        Button button_submit = findViewById(R.id.button_submit);
        button_from = findViewById(R.id.button_from);
        button_to = findViewById(R.id.button_to);
        edit_amount = findViewById(R.id.edit_amount);
        edit_purpose = findViewById(R.id.edit_purpose);
        button_date = findViewById(R.id.button_date);
        login_progress = findViewById(R.id.login_progress);

//        associate_button_with_time_stamp();
        Insert_Transaction_v2_Utils.associate_button_with_time_stamp(button_date, calendar);

        button_from.setText("From : " + getIntent().getStringExtra("CURRENT_ACCOUNT_FULL_NAME"));
        from_selected_account_id = getIntent().getStringExtra("CURRENT_ACCOUNT_ID");

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

//                associate_button_with_time_stamp();
                Insert_Transaction_v2_Utils.associate_button_with_time_stamp(button_date, calendar);

                Log.d(Application_Specification.APPLICATION_NAME, "Selected : " + Date_Utils.date_to_mysql_date_time_string((calendar.getTime())));
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

        button_submit.setOnClickListener(v -> attempt_insert_Transaction());

        button_to.setOnClickListener(v -> {
            to_account_selection_flag = true;
            select_account();
        });

        button_from.setOnClickListener(v -> {
            to_account_selection_flag = false;
            select_account();
        });

    }

    private void select_account() {

        Activity_Utils.start_activity_with_string_extras(this, List_Accounts.class, new Pair[]{new Pair<>("HEADER_TITLE", "NA"), new Pair<>("PARENT_ACCOUNT_ID", "0"), new Pair<>("ACTIVITY_FOR_RESULT_FLAG", String.valueOf(true)), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", "CURRENCY"), new Pair<>("CURRENT_ACCOUNT_TYPE", "Assets"), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", "INR"), new Pair<>("CURRENT_ACCOUNT_TAXABLE", String.valueOf(false)), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", String.valueOf(false))}, true, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (to_account_selection_flag) {
                button_to.setText("To : " + data.getStringExtra("SELECTED_ACCOUNT_FULL_NAME"));
                to_selected_account_id = data.getStringExtra("SELECTED_ACCOUNT_ID");
            } else {
                button_from.setText("From : " + data.getStringExtra("SELECTED_ACCOUNT_FULL_NAME"));
                from_selected_account_id = data.getStringExtra("SELECTED_ACCOUNT_ID");
            }
        }
    }

//    private void associate_button_with_time_stamp() {
//        button_date.setText(Date_Utils.normal_date_time_format_words.format(calendar.getTime()));
//    }

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

            Activity_Utils.start_activity_with_string_extras(this, Clickable_Pass_Book_Bundle.class, new Pair[]{new Pair<>("URL", REST_GET_Task.get_Get_URL(API_Wrapper.get_http_API(API.select_User_Transactions_v2), new Pair[]{new Pair<>("user_id", settings.getString("user_id", "0")), new Pair<>("account_id", getIntent().getStringExtra("CURRENT_ACCOUNT_ID"))})), new Pair<>("application_name", Application_Specification.APPLICATION_NAME), new Pair<>("V2_FLAG", getIntent().getStringExtra("CURRENT_ACCOUNT_ID"))
            }, false, 0);
        }

        return super.onOptionsItemSelected(item);
    }

    private void attempt_insert_Transaction() {

        if (to_selected_account_id.equals("0")) {

            Toast_Utils.longToast(this, "Please select To A/C...");

        } else {

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
//                    execute_insert_Transaction_Task();
                    Insert_Transaction_v2_Utils.execute_insert_Transaction_Task(login_progress, login_form, this, this, settings.getString("user_id", "0"), edit_purpose.getText().toString().trim(), Double.parseDouble(edit_amount.getText().toString().trim()), Integer.parseInt(from_selected_account_id), Integer.parseInt(to_selected_account_id), edit_purpose, edit_amount, button_date, calendar);
                }
            }
        }
    }

//    private void execute_insert_Transaction_Task() {
//
////        REST_Insert_Task_Wrapper.execute(this, API_Wrapper.get_http_API(API.insert_Transaction_v2), this, login_progress, login_form, Application_Specification.APPLICATION_NAME, new Pair[]{new Pair<>("event_date_time", Date_Utils.date_to_mysql_date_time_string(calendar.getTime())), new Pair<>("user_id", settings.getString("user_id", "0")), new Pair<>("particulars", edit_purpose.getText().toString()), new Pair<>("amount", edit_amount.getText().toString()), new Pair<>("from_account_id", from_selected_account_id), new Pair<>("to_account_id", to_selected_account_id)}, edit_purpose, new EditText[]{edit_purpose, edit_amount});
//
//        Insert_Transaction_v2_Utils.execute_insert_Transaction_Task(login_progress, login_form, this, this, settings.getString("user_id", "0"), edit_purpose.getText().toString().trim(), Double.parseDouble(edit_amount.getText().toString().trim()), Integer.parseInt(from_selected_account_id), Integer.parseInt(to_selected_account_id), edit_purpose, edit_amount, button_date, calendar);
//    }
    //TODO : Derive Insert_Transaction_v2 Quick & AutoComplete from a common class
}

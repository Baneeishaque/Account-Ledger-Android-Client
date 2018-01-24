package ndk.personal.account_ledger.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.constants.API;
import ndk.personal.account_ledger.constants.Application_Specification;
import ndk.utils.Activity_Utils;
import ndk.utils.Date_Picker_Utils;
import ndk.utils.Date_Utils;
import ndk.utils.Spinner_Utils;
import ndk.utils.Toast_Utils;
import ndk.utils.Validation_Utils;
import ndk.utils.activities.Pass_Book;
import ndk.utils.network_task.REST_Insert_Task;

import static ndk.utils.Network_Utils.isOnline;
import static ndk.utils.ProgressBar_Utils.showProgress;

public class Insert_Transaction extends AppCompatActivity {

    private android.widget.ProgressBar loginprogress;
    private android.widget.Button buttondate;
    private android.widget.Spinner spinnersection;
    private android.widget.EditText editpurpose;
    private android.widget.EditText editamount;
    private android.widget.Button buttonsubmit;
    private android.widget.ScrollView loginform;
    private Calendar calendar = Calendar.getInstance();
    Context application_context, activity_context = this;
    REST_Insert_Task REST_Insert_Task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        application_context = getApplicationContext();

        this.loginform = (ScrollView) findViewById(R.id.login_form);
        this.buttonsubmit = (Button) findViewById(R.id.button_submit);
        this.editamount = (EditText) findViewById(R.id.edit_amount);
        this.editpurpose = (EditText) findViewById(R.id.edit_purpose);
        this.spinnersection = (Spinner) findViewById(R.id.spinner_section);
        this.buttondate = (Button) findViewById(R.id.button_date);
        this.loginprogress = (ProgressBar) findViewById(R.id.login_progress);

        buttondate.setText(Date_Utils.normal_Date_Format_words.format(calendar.getTime()));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                buttondate.setText(Date_Utils.normal_Date_Format_words.format(calendar.getTime()));
            }
        };
        buttondate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date_Picker_Utils.show_date_picker_upto_today(activity_context, date, calendar);
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
            Activity_Utils.start_activity_with_string_extras(activity_context, Pass_Book.class,new Pair[]{new Pair<>("URL",API.get_Android_API(API.select_User_Transactions)),new Pair<>("application_name",Application_Specification.APPLICATION_NAME),new Pair<>("user_id","1")});
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
            try {
                REST_Insert_Task = new REST_Insert_Task(API.get_Android_API(API.insert_Transaction), REST_Insert_Task, this, loginprogress, loginform, Application_Specification.APPLICATION_NAME, new Pair[]{new Pair<>("event_date_time", Date_Utils.date_to_mysql_date_string(Date_Utils.normal_Date_Format_words.parse(buttondate.getText().toString()))), new Pair<>("user_id", "1"), new Pair<>("particulars", spinnersection.getSelectedItem().toString() + " : " + editpurpose.getText().toString()), new Pair<>("amount", editamount.getText().toString())}, editamount, Insert_Transaction.class);
            } catch (ParseException e) {
                Toast_Utils.longToast(getApplicationContext(), "Date conversion error");
                Log.d(Application_Specification.APPLICATION_NAME, e.getLocalizedMessage());
            }
            REST_Insert_Task.execute((Void) null);
        } else {
            Toast_Utils.longToast(getApplicationContext(), "Internet is unavailable");
        }
    }
}

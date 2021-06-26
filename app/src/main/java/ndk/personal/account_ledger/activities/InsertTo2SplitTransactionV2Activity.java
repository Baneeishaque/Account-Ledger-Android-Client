package ndk.personal.account_ledger.activities;

import android.os.Bundle;

import ndk.personal.account_ledger.R;
import ndk.utils_android14.ActivityWithContexts14;

public class InsertTo2SplitTransactionV2Activity extends ActivityWithContexts14 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

//         application_context = getApplicationContext();

//         settings = getApplicationContext().getSharedPreferences(ApplicationSpecification.APPLICATION_NAME, Context.MODE_PRIVATE);

//         login_form = findViewById(R.id.login_form);
//         Button button_submit = findViewById(R.id.buttonSubmit);
//         edit_amount = findViewById(R.id.editTextAmount);
//         edit_purpose = findViewById(R.id.edit_purpose);
//         spinner_section = findViewById(R.id.spinner_section);
//         button_date = findViewById(R.id.button_date);
//         login_progress = findViewById(R.id.login_progress);

//         associate_button_with_time_stamp();

//         // Initialize
//         final SwitchDateTimeDialogFragment dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
//                 "Pick Time",
//                 "OK",
//                 "Cancel"
//         );

//         // Assign values
//         dateTimeFragment.startAtCalendarView();
//         dateTimeFragment.set24HoursMode(true);
// //        dateTimeFragment.setMaximumDateTime(calendar.getTime());

// //        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
// //        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(2017, Calendar.MARCH, 4, 15, 20).getTime());

//         // Or assign each element, default element is the current moment
// //        dateTimeFragment.setDefaultHourOfDay(15);
// //        dateTimeFragment.setDefaultMinute(20);
// //        dateTimeFragment.setDefaultDay(4);
// //        dateTimeFragment.setDefaultMonth(Calendar.MARCH);
// //        dateTimeFragment.setDefaultYear(2017);

//         // Define new day and month format
//         try {
//             dateTimeFragment.setSimpleDateMonthAndDayFormat(DateUtils1.normalStrippedDateFormat);
//         } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
//             Log.e(ApplicationSpecification.APPLICATION_NAME, e.getMessage());
//         }

//         // Set listener
//         dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {

//             @Override
//             public void onPositiveButtonClick(Date date) {
//                 // Date is get on positive button click
//                 calendar.set(Calendar.YEAR, dateTimeFragment.getYear());
//                 calendar.set(Calendar.MONTH, dateTimeFragment.getMonth());
//                 calendar.set(Calendar.DAY_OF_MONTH, dateTimeFragment.getDay());
//                 calendar.set(Calendar.HOUR_OF_DAY, dateTimeFragment.getHourOfDay());
//                 calendar.set(Calendar.MINUTE, dateTimeFragment.getMinute());

//                 associate_button_with_time_stamp();

//                 Log.d(ApplicationSpecification.APPLICATION_NAME, "Slected : " + DateUtils1.dateToMysqlDateTimeString((calendar.getTime())));
//                 // dateTimeFragment.setDefaultDateTime(calendar.getTime());
//             }

//             @Override
//             public void onNegativeButtonClick(Date date) {
//                 // Date is get on negative button click
//             }
//         });

//         button_date.setOnClickListener(new View.OnClickListener() {

//             @Override
//             public void onClick(View v) {
//                 // Show
//                 dateTimeFragment.show(getSupportFragmentManager(), "dialog_time");
//             }
//         });

//         button_submit.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 attempt_insert_Transaction();
//             }
//         });

//         Spinner_Utils.attach_items_to_simple_spinner(this, spinner_section, new ArrayList<>(Arrays.asList("Debit", "Credit")));
    }
}
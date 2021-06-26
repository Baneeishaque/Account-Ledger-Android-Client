package ndk.personal.account_ledger.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.personal.account_ledger.models.Account;
import ndk.personal.account_ledger.utils.AccountLedgerLogUtils;
import ndk.utils_android1.DateUtils1;
import ndk.utils_android1.ToastUtils1;
import ndk.utils_android14.ActivityUtils14;
import ndk.utils_android14.RestGetTask;
import ndk.utils_android16.ValidationUtils16;
import ndk.utils_android16.network_task.HttpApiSelectTask;
import ndk.utils_android16.network_task.HttpApiSelectTaskWrapper;

import static ndk.utils_android1.ButtonUtils.associateButtonWithTimeStamp;

public class InsertTransactionV2 extends AppCompatActivity {

    public Calendar calendar = Calendar.getInstance();
    public EditText editTextPurpose;
    public EditText editTextAmount;
    public ProgressBar progressBarView;
    public ScrollView formView;
    public Button buttonDate;
    Context currentApplicationContext, currentActivityContext = this;
    SharedPreferences sharedPreferences;
    String currentToAccountIdParent = "0", currentToAccountType, currentToAccountCommodityType, currentToAccountCommodityValue;
    String currentFromAccountIdParent, currentFromAccountType, currentFromAccountCommodityType, currentFromAccountCommodityValue, currentFromAccountTaxable, currentFromAccountPlaceHolder;
    AutoCompleteTextView autoCompleteTextViewToAccount, autoCompleteTextViewFromAccount;
    String selectedFromAccountId;
    String selectedToAccountId = "0";
    Stack<Account> fromAccountsStack;
    Stack<Account> toAccountsStack;
    private Button buttonToAccount, buttonFromAccount;
    private ArrayList<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction_v2);
        fromAccountsStack = new Stack<>();
        toAccountsStack = new Stack<>();
        currentApplicationContext = getApplicationContext();
        sharedPreferences = getApplicationContext().getSharedPreferences(ApplicationSpecification.APPLICATION_NAME, Context.MODE_PRIVATE);
        formView = findViewById(R.id.login_form);
        progressBarView = findViewById(R.id.login_progress);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonFromAccount = findViewById(R.id.button_from);
        buttonToAccount = findViewById(R.id.button_to);
        buttonDate = findViewById(R.id.button_date);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextPurpose = findViewById(R.id.edit_purpose);
        autoCompleteTextViewToAccount = findViewById(R.id.autoCompleteTextView_to);
        autoCompleteTextViewFromAccount = findViewById(R.id.autoCompleteTextView_from);
        Button buttonAddToAccount = findViewById(R.id.button_tplus);
        Button buttonAddFromAccount = findViewById(R.id.button_fplus);
        associateButtonWithTimeStamp(buttonDate, calendar);
        buttonFromAccount.setText("From : " + getIntent().getStringExtra("CURRENT_ACCOUNT_FULL_NAME"));
        autoCompleteTextViewFromAccount.setText(getIntent().getStringExtra("CURRENT_ACCOUNT_FULL_NAME"), false);
        selectedFromAccountId = getIntent().getStringExtra("CURRENT_ACCOUNT_ID");
        currentFromAccountIdParent = selectedFromAccountId;
        currentFromAccountType = getIntent().getStringExtra("CURRENT_ACCOUNT_TYPE");
        currentFromAccountCommodityType = getIntent().getStringExtra("CURRENT_ACCOUNT_COMMODITY_TYPE");
        currentFromAccountCommodityValue = getIntent().getStringExtra("CURRENT_ACCOUNT_COMMODITY_VALUE");
        currentFromAccountTaxable = getIntent().getStringExtra("CURRENT_ACCOUNT_TAXABLE");
        currentFromAccountPlaceHolder = getIntent().getStringExtra("CURRENT_ACCOUNT_PLACE_HOLDER");

        // Initialize
        final SwitchDateTimeDialogFragment dateTimeFragment = SwitchDateTimeDialogFragment.newInstance("Pick Time", "OK", "Cancel");

        // Assign values
        dateTimeFragment.startAtCalendarView();
        dateTimeFragment.set24HoursMode(true);
//        dateTimeFragment.setMaximumDateTime(calendar.getTime());

        // Define new day and month format
        try {

            dateTimeFragment.setSimpleDateMonthAndDayFormat(DateUtils1.normalStrippedDateFormat);

        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {

            AccountLedgerLogUtils.debug(e.getMessage());
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

                associateButtonWithTimeStamp(buttonDate, calendar);

                AccountLedgerLogUtils.debug("Selected : " + DateUtils1.dateToMysqlDateTimeString((calendar.getTime())));
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
            }
        });

        buttonDate.setOnClickListener(v -> {

            // Show
            dateTimeFragment.show(getSupportFragmentManager(), "dialog_time");
        });

        buttonSubmit.setOnClickListener(v -> attemptInsertTransaction());

        buttonToAccount.setOnLongClickListener(v -> {

            initializeToAccount();
            return true;
        });

        buttonFromAccount.setOnLongClickListener(v -> {

            initializeFromAccount();
            return true;
        });

        bindAutoTextViewOfToAccount();

        autoCompleteTextViewToAccount.setOnItemClickListener((parent, view, position, id) -> {

            AccountLedgerLogUtils.debug("Item Position : " + position);
            AccountLedgerLogUtils.debug("Selected Account : " + accounts.get(position).toString());

            buttonToAccount.setText(buttonToAccount.getText().equals("To : ") ? buttonToAccount.getText() + autoCompleteTextViewToAccount.getText().toString() : buttonToAccount.getText() + " : " + autoCompleteTextViewToAccount.getText().toString());
            autoCompleteTextViewToAccount.setHint(autoCompleteTextViewToAccount.getText().toString() + " : ");

            currentToAccountIdParent = accounts.get(position).getAccountId();
            currentToAccountType = accounts.get(position).getAccountType();
            currentToAccountCommodityType = accounts.get(position).getCommodityType();
            currentToAccountCommodityValue = accounts.get(position).getCommodityValue();

            selectedToAccountId = currentToAccountIdParent;

            bindAutoTextViewOfToAccount();

            toAccountsStack.push(new Account(currentToAccountType, currentToAccountIdParent, accounts.get(position).getNotes(), accounts.get(position).getParentAccountId(), accounts.get(position).getOwnerId(), autoCompleteTextViewToAccount.getHint().toString(), currentToAccountCommodityType, currentToAccountCommodityValue, buttonToAccount.getText().toString()));
        });

        autoCompleteTextViewFromAccount.setOnItemClickListener((parent, view, position, id) -> {

            AccountLedgerLogUtils.debug("Item Position : " + position);
            AccountLedgerLogUtils.debug("Selected Account : " + accounts.get(position).toString());

            buttonFromAccount.setText(buttonFromAccount.getText().equals("From : ") ? buttonFromAccount.getText() + autoCompleteTextViewFromAccount.getText().toString() : buttonFromAccount.getText() + " : " + autoCompleteTextViewFromAccount.getText().toString());
            autoCompleteTextViewFromAccount.setHint(autoCompleteTextViewFromAccount.getText().toString() + " : ");

            currentFromAccountIdParent = accounts.get(position).getAccountId();
            currentFromAccountType = accounts.get(position).getAccountType();
            currentFromAccountCommodityType = accounts.get(position).getCommodityType();
            currentFromAccountCommodityValue = accounts.get(position).getCommodityValue();

            selectedFromAccountId = currentFromAccountIdParent;

            bindAutoTextViewOfFromAccount();

            fromAccountsStack.push(new Account(currentFromAccountType, currentFromAccountIdParent, accounts.get(position).getNotes(), accounts.get(position).getParentAccountId(), accounts.get(position).getOwnerId(), autoCompleteTextViewFromAccount.getHint().toString(), currentFromAccountCommodityType, currentFromAccountCommodityValue, buttonFromAccount.getText().toString()));
        });

        autoCompleteTextViewToAccount.setOnClickListener(v -> autoCompleteTextViewToAccount.showDropDown());

        autoCompleteTextViewFromAccount.setOnClickListener(v -> autoCompleteTextViewFromAccount.showDropDown());

        autoCompleteTextViewToAccount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //TODO : 2 Letter Delete : Previous to Two A/Cs & So on
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextViewFromAccount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO : 2 Letter Delete : Previous to Two A/Cs & So on
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextViewToAccount.setOnDismissListener(() -> {

            if (autoCompleteTextViewToAccount.getListSelection() != ListView.INVALID_POSITION) {

                autoCompleteTextViewToAccount.setText(autoCompleteTextViewToAccount.getHint().toString().substring(0, autoCompleteTextViewToAccount.getHint().length() - 3), false);
                autoCompleteTextViewToAccount.setSelection(autoCompleteTextViewToAccount.getText().length());
            }
        });

        autoCompleteTextViewFromAccount.setOnDismissListener(() -> {

            if (autoCompleteTextViewFromAccount.getListSelection() != ListView.INVALID_POSITION) {

                autoCompleteTextViewFromAccount.setText(autoCompleteTextViewFromAccount.getHint().toString().substring(0, autoCompleteTextViewFromAccount.getHint().length() - 3), false);
                autoCompleteTextViewFromAccount.setSelection(autoCompleteTextViewFromAccount.getText().length());
            }
        });

        buttonAddToAccount.setOnLongClickListener(v -> {

            if (!currentToAccountIdParent.equals("0")) {

                ActivityUtils14.startActivityForClassWithStringExtras(currentActivityContext, Insert_Account.class, new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID", currentToAccountIdParent), new Pair<>("CURRENT_ACCOUNT_FULL_NAME", buttonToAccount.getText().toString().replace("To : ", "")), new Pair<>("CURRENT_ACCOUNT_TYPE", currentToAccountType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", currentToAccountCommodityType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", currentToAccountCommodityValue), new Pair<>("CURRENT_ACCOUNT_TAXABLE", "F"), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", "F"), new Pair<>("ACTIVITY_FOR_RESULT_FLAG", String.valueOf(true))});
            } else {
                ToastUtils1.longToast(getApplicationContext(), "Please Select a parent account...");
            }

            return true;
        });

        buttonAddToAccount.setOnClickListener(v -> {

            bindPreviousToAccount();
        });

        buttonAddFromAccount.setOnLongClickListener(v -> {

            if (!currentFromAccountIdParent.equals("0")) {

                ActivityUtils14.startActivityForClassWithStringExtras(currentActivityContext, Insert_Account.class, new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID", currentFromAccountIdParent), new Pair<>("CURRENT_ACCOUNT_FULL_NAME", buttonFromAccount.getText().toString().replace("From : ", "")), new Pair<>("CURRENT_ACCOUNT_TYPE", currentFromAccountType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", currentFromAccountCommodityType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", currentFromAccountCommodityValue), new Pair<>("CURRENT_ACCOUNT_TAXABLE", currentFromAccountTaxable), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", currentFromAccountPlaceHolder), new Pair<>("ACTIVITY_FOR_RESULT_FLAG", String.valueOf(true))});
            } else {
                ToastUtils1.longToast(getApplicationContext(), "Please Select a parent account...");
            }

            return true;
        });

        buttonAddFromAccount.setOnClickListener(v -> {

            bindPreviousFromAccount();
        });

        buttonDate.setOnLongClickListener(v -> {

            exchangeAccounts();
            return true;
        });
    }

    private void bindPreviousFromAccount() {

        if (fromAccountsStack.isEmpty()) {

            initializeFromAccount();

        } else {

            Account currentFromAccount = fromAccountsStack.pop();

            if (fromAccountsStack.isEmpty()) {

                initializeFromAccount();

            } else {

                Account previousFromAccount = fromAccountsStack.peek();

                currentFromAccountIdParent = previousFromAccount.getAccountId();
                currentFromAccountType = previousFromAccount.getAccountType();
                currentFromAccountCommodityType = previousFromAccount.getCommodityType();
                currentFromAccountCommodityValue = previousFromAccount.getCommodityValue();

                selectedFromAccountId = currentFromAccountIdParent;

                buttonFromAccount.setText(previousFromAccount.getFullName());

                autoCompleteTextViewFromAccount.setHint(previousFromAccount.getName());

                autoCompleteTextViewFromAccount.setText(autoCompleteTextViewFromAccount.getHint().toString().substring(0, autoCompleteTextViewFromAccount.getHint().length() - 3), false);
                autoCompleteTextViewFromAccount.setSelection(autoCompleteTextViewFromAccount.getText().length());

                bindAutoTextViewOfFromAccount();
            }

        }
    }

    private void bindPreviousToAccount() {

        if (toAccountsStack.isEmpty()) {

            initializeToAccount();

        } else {

            Account currentToAccount = toAccountsStack.pop();

            if (toAccountsStack.isEmpty()) {

                initializeToAccount();

            } else {

                Account previousToAccount = toAccountsStack.peek();

                currentToAccountIdParent = previousToAccount.getAccountId();
                currentToAccountType = previousToAccount.getAccountType();
                currentToAccountCommodityType = previousToAccount.getCommodityType();
                currentToAccountCommodityValue = previousToAccount.getCommodityValue();

                selectedToAccountId = currentToAccountIdParent;

                buttonToAccount.setText(previousToAccount.getFullName());

                autoCompleteTextViewToAccount.setHint(previousToAccount.getName());

                autoCompleteTextViewToAccount.setText(autoCompleteTextViewToAccount.getHint().toString().substring(0, autoCompleteTextViewToAccount.getHint().length() - 3), false);

                autoCompleteTextViewToAccount.setSelection(autoCompleteTextViewToAccount.getText().length());

                bindAutoTextViewOfToAccount();
            }
        }
    }

    private void initializeFromAccount() {

        currentFromAccountIdParent = "0";
        buttonFromAccount.setText("From : ");
        autoCompleteTextViewFromAccount.setHint("");

        boolean fromAccountEditFlag = false;
        autoCompleteTextViewFromAccount.setText("", false);
        fromAccountEditFlag = true;

        bindAutoTextViewOfFromAccount();
    }

    private void initializeToAccount() {

        currentToAccountIdParent = "0";
        buttonToAccount.setText("To : ");
        autoCompleteTextViewToAccount.setHint("");

        boolean toAccountEditFlag = false;
        autoCompleteTextViewToAccount.setText("", false);
        toAccountEditFlag = true;

        bindAutoTextViewOfToAccount();
    }

    private void exchangeAccounts() {

        String temp = currentFromAccountIdParent;
        currentFromAccountIdParent = currentToAccountIdParent;
        currentToAccountIdParent = temp;

        temp = currentFromAccountType;
        currentFromAccountType = currentToAccountType;
        currentToAccountType = temp;

        temp = currentFromAccountCommodityType;
        currentFromAccountCommodityType = currentToAccountCommodityType;
        currentToAccountCommodityType = temp;

        temp = currentFromAccountCommodityValue;
        currentFromAccountCommodityValue = currentToAccountCommodityValue;
        currentToAccountCommodityValue = temp;

        temp = selectedFromAccountId;
        selectedFromAccountId = selectedToAccountId;
        selectedToAccountId = temp;

        temp = buttonFromAccount.getText().toString();
        buttonFromAccount.setText(buttonToAccount.getText().toString().replace("To", "From"));
        buttonToAccount.setText(temp.replace("From", "To"));

        temp = autoCompleteTextViewFromAccount.getText().toString();
        autoCompleteTextViewFromAccount.setText(autoCompleteTextViewToAccount.getText().toString(), false);
        autoCompleteTextViewToAccount.setText(temp, false);

        Stack<Account> tempStack = fromAccountsStack;
        fromAccountsStack = toAccountsStack;
        toAccountsStack = tempStack;
    }

    private void bindAutoTextViewOfToAccount() {

        HttpApiSelectTask.AsyncResponseJsonArray asyncResponseJsonArray = jsonArray -> {

            accounts = new ArrayList<>();
            ArrayList<String> accountFullNames = new ArrayList<>();

            try {

                if (!jsonArray.getJSONObject(0).getString("status").equals("1")) {

                    for (int i = 1; i < jsonArray.length(); i++) {

                        accounts.add(new Account(jsonArray.getJSONObject(i).getString("account_type"), jsonArray.getJSONObject(i).getString("account_id"), jsonArray.getJSONObject(i).getString("notes"), jsonArray.getJSONObject(i).getString("parent_account_id"), jsonArray.getJSONObject(i).getString("owner_id"), jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("commodity_type"), jsonArray.getJSONObject(i).getString("commodity_value"), jsonArray.getJSONObject(i).getString("name")));
                        accountFullNames.add(jsonArray.getJSONObject(i).getString("name"));
                    }
                } else {
                    editTextPurpose.requestFocus();
                }

            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(ApplicationSpecification.APPLICATION_NAME, "Error : " + e.getLocalizedMessage());
            }

            //Creating the instance of ArrayAdapter containing list of fruit names
            ArrayAdapter<String> adapter = new ArrayAdapter<>(currentActivityContext, android.R.layout.select_dialog_item, accountFullNames);

            //will start working from first character
            autoCompleteTextViewToAccount.setThreshold(1);
            //setting the adapter data into the AutoCompleteTextView
            autoCompleteTextViewToAccount.setAdapter(adapter);
            autoCompleteTextViewToAccount.setTextColor(Color.RED);
            autoCompleteTextViewToAccount.showDropDown();
        };

        HttpApiSelectTaskWrapper.executePostThenReturnJsonArrayWithErrorStatusAndBackgroundWorkStatus(RestGetTask.prepareGetUrl(ApiWrapper.selectUserAccounts(), new Pair[]{new Pair<>("user_id", sharedPreferences.getString("user_id", "0")), new Pair<>("parent_account_id", currentToAccountIdParent)}), this, ApplicationSpecification.APPLICATION_NAME, asyncResponseJsonArray, false, true);
    }

    private void bindAutoTextViewOfFromAccount() {

        HttpApiSelectTask.AsyncResponseJsonArray asyncResponseJsonArray = jsonArray -> {

            accounts = new ArrayList<>();
            ArrayList<String> accountFullNames = new ArrayList<>();

            try {

                if (!jsonArray.getJSONObject(0).getString("status").equals("1")) {

                    for (int i = 1; i < jsonArray.length(); i++) {

                        accounts.add(new Account(jsonArray.getJSONObject(i).getString("account_type"), jsonArray.getJSONObject(i).getString("account_id"), jsonArray.getJSONObject(i).getString("notes"), jsonArray.getJSONObject(i).getString("parent_account_id"), jsonArray.getJSONObject(i).getString("owner_id"), jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("commodity_type"), jsonArray.getJSONObject(i).getString("commodity_value"), jsonArray.getJSONObject(i).getString("name")));
                        accountFullNames.add(jsonArray.getJSONObject(i).getString("name"));
                    }
                } else {
                    autoCompleteTextViewToAccount.requestFocus();
                }

            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(ApplicationSpecification.APPLICATION_NAME, "Error : " + e.getLocalizedMessage());
            }

            //Creating the instance of ArrayAdapter containing list of fruit names
            ArrayAdapter<String> adapter = new ArrayAdapter<>(currentActivityContext, android.R.layout.select_dialog_item, accountFullNames);

            autoCompleteTextViewFromAccount.setThreshold(1);
            autoCompleteTextViewFromAccount.setAdapter(adapter);
            autoCompleteTextViewFromAccount.setTextColor(Color.RED);
            autoCompleteTextViewFromAccount.showDropDown();
        };

        HttpApiSelectTaskWrapper.executePostThenReturnJsonArrayWithErrorStatusAndBackgroundWorkStatus(RestGetTask.prepareGetUrl(ApiWrapper.selectUserAccounts(), new Pair[]{new Pair<>("user_id", sharedPreferences.getString("user_id", "0")), new Pair<>("parent_account_id", currentFromAccountIdParent)}), this, ApplicationSpecification.APPLICATION_NAME, asyncResponseJsonArray, false, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case 0:
                    bindAutoTextViewOfToAccount();
                    break;

                case 1:
                    bindAutoTextViewOfFromAccount();
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.insert_transaction, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_item_view_from_account_pass_book) {

            ActivityUtils14.startActivityForClassWithStringExtras(this, ClickablePassBookBundle.class, new Pair[]{new Pair<>("URL", RestGetTask.prepareGetUrl(ApiWrapper.selectUserAccounts(), new Pair[]{new Pair<>("user_id", sharedPreferences.getString("user_id", "0")), new Pair<>("account_id", getIntent().getStringExtra("CURRENT_ACCOUNT_ID"))})), new Pair<>("application_name", ApplicationSpecification.APPLICATION_NAME), new Pair<>("V2_FLAG", getIntent().getStringExtra("CURRENT_ACCOUNT_ID"))});

        } else if (id == R.id.menu_item_insert_via_transaction) {

            ActivityUtils14.startActivityForClassWithStringExtras(currentActivityContext, InsertTransactionV2Via.class, new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID", currentFromAccountIdParent), new Pair<>("CURRENT_ACCOUNT_FULL_NAME", buttonFromAccount.getText().toString().replace("From : ", "")), new Pair<>("CURRENT_ACCOUNT_TYPE", currentFromAccountType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", currentFromAccountCommodityType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", currentFromAccountCommodityValue), new Pair<>("CURRENT_ACCOUNT_TAXABLE", currentFromAccountTaxable), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", currentFromAccountPlaceHolder)});

        } else if (id == R.id.menu_item_insert_two_way_transaction) {

            ActivityUtils14.startActivityWithStringExtrasAndFinish(currentActivityContext, InsertTransactionV2TwoWay.class, new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID", currentFromAccountIdParent), new Pair<>("CURRENT_ACCOUNT_FULL_NAME", buttonFromAccount.getText().toString().replace("From : ", "")), new Pair<>("CURRENT_ACCOUNT_TYPE", currentFromAccountType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", currentFromAccountCommodityType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", currentFromAccountCommodityValue), new Pair<>("CURRENT_ACCOUNT_TAXABLE", currentFromAccountTaxable), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", currentFromAccountPlaceHolder)});
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptInsertTransaction() {

        if (selectedToAccountId.equals("0")) {

            ToastUtils1.longToast(this, "Please select To A/C...");

        } else {

            ValidationUtils16.resetErrors(new EditText[]{editTextPurpose, editTextAmount});
            Pair<Boolean, EditText> emptyCheckResult = ValidationUtils16.emptyCheckEditTextPairs(new Pair[]{new Pair<>(editTextAmount, "Please Enter Valid Amount..."), new Pair<>(editTextPurpose, "Please Enter Purpose...")});

            if (emptyCheckResult.first) {

                if (emptyCheckResult.second != null) {

                    emptyCheckResult.second.requestFocus();
                }

            } else {

                Pair<Boolean, EditText> zeroCheckResult = ValidationUtils16.zeroCheckEditTextPairs(new Pair[]{new Pair<>(editTextAmount, "Please Enter Valid Amount...")});

                if (zeroCheckResult.first) {

                    if (zeroCheckResult.second != null) {

                        zeroCheckResult.second.requestFocus();
                    }

                } else {

                    executeInsertTransaction();
                }
            }
        }
    }

    public void executeInsertTransaction() {

        InsertTransactionV2Utils.executeInsertTransactionTaskWithClearingOfEditTextsAndIncrementingOfButtonTextTimeStampForFiveMinutes(progressBarView, formView, this, this, sharedPreferences.getString("user_id", "0"), editTextPurpose.getText().toString().trim(), Double.parseDouble(editTextAmount.getText().toString().trim()), Integer.parseInt(selectedFromAccountId), Integer.parseInt(selectedToAccountId), editTextPurpose, editTextAmount, buttonDate, calendar);
    }
}

package ndk.personal.account_ledger.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.core.util.Pair;

import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Stack;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.constants.ApiMethodParameters;
import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.personal.account_ledger.constants.SharedPreferenceKeys;
import ndk.personal.account_ledger.models.Account;
import ndk.personal.account_ledger.utils.AccountLedgerErrorUtils;
import ndk.personal.account_ledger.utils.AccountLedgerExceptionUtils;
import ndk.personal.account_ledger.utils.AccountLedgerLogUtils;
import ndk.utils_android1.DateUtils1;
import ndk.utils_android1.ToastUtils1;
import ndk.utils_android14.ActivityWithContexts14;
import ndk.utils_android14.NetworkUtils14;
import ndk.utils_android14.RestGetTask;
import ndk.utils_android16.CalendarUtils;
import ndk.utils_android16.ValidationUtils16;
import ndk.utils_android16.network_task.HttpApiSelectTask;
import ndk.utils_android16.network_task.HttpApiSelectTaskWrapper;
import ndk.utils_android19.ActivityUtils19;

import static ndk.utils_android1.ButtonUtils.associateButtonWithTimeStamp;

public class InsertTransactionV2Via extends ActivityWithContexts14 {

    private SharedPreferences sharedPreferences;

    private String currentFromAccountIdParent, currentFromAccountType, currentFromAccountCommodityType, currentFromAccountCommodityValue, currentFromAccountTaxable, currentFromAccountPlaceHolder;
    private String currentToAccountIdParent = "0", currentToAccountType, currentToAccountCommodityType, currentToAccountCommodityValue;
    private String currentViaAccountIdParent = "0", currentViaAccountType, currentViaAccountCommodityType, currentViaAccountCommodityValue;

    private Calendar calendar = Calendar.getInstance();
    private EditText editTextParticulars;
    private EditText editTextAmount;
    private ScrollView formView;
    private ProgressBar progressBarView;

    private AutoCompleteTextView autoCompleteTextViewToAccount, autoCompleteTextViewFromAccount;
    private AutoCompleteTextView autoCompleteTextViewViaAccount;

    private Button buttonDate;

    private Button buttonToAccount, buttonFromAccount;
    private Button buttonViaAccount;

    private ArrayList<Account> accounts;

    private String selectedFromAccountId;
    private String selectedToAccountId = "0";
    private String selectedViaAccountId = "0";

    private Stack<Account> fromAccountsStack;
    private Stack<Account> viaAccountsStack;
    private Stack<Account> toAccountsStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_via_transaction_v2);

        fromAccountsStack = new Stack<>();
        viaAccountsStack = new Stack<>();

        sharedPreferences = getApplicationContext().getSharedPreferences(ApplicationSpecification.APPLICATION_NAME, Context.MODE_PRIVATE);

        formView = findViewById(R.id.scrollView);

        progressBarView = findViewById(R.id.progressBar);

        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonFromAccount = findViewById(R.id.buttonFromAccount);
        buttonToAccount = findViewById(R.id.buttonToAccount);

        buttonDate = findViewById(R.id.buttonDate);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextParticulars = findViewById(R.id.editTextParticulars);

        autoCompleteTextViewToAccount = findViewById(R.id.autoCompleteTextViewToAccount);
        autoCompleteTextViewFromAccount = findViewById(R.id.autoCompleteTextViewFromAccount);

        Button buttonAddToAccount = findViewById(R.id.buttonAddToAccount);
        Button buttonAddFromAccount = findViewById(R.id.buttonAddFromAccount);

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

            AccountLedgerLogUtils.debug(e.getMessage(), currentApplicationContext);
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

                AccountLedgerLogUtils.debug("Selected : " + DateUtils1.dateToMysqlDateTimeString((calendar.getTime())), currentApplicationContext);
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

        autoCompleteTextViewToAccount.setOnItemClickListener((parent, view, position, id) -> {

            AccountLedgerLogUtils.debug("Item Position : " + position, currentApplicationContext);
            AccountLedgerLogUtils.debug("Selected Account : " + accounts.get(position).toString(), currentApplicationContext);

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

            AccountLedgerLogUtils.debug("Item Position : " + position, currentApplicationContext);
            AccountLedgerLogUtils.debug("Selected Account : " + accounts.get(position).toString(), currentApplicationContext);

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

                ActivityUtils19.startActivityForClassWithStringExtras(currentActivityContext, Insert_Account.class, new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID", currentToAccountIdParent), new Pair<>("CURRENT_ACCOUNT_FULL_NAME", buttonToAccount.getText().toString().replace("To : ", "")), new Pair<>("CURRENT_ACCOUNT_TYPE", currentToAccountType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", currentToAccountCommodityType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", currentToAccountCommodityValue), new Pair<>("CURRENT_ACCOUNT_TAXABLE", "F"), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", "F"), new Pair<>("ACTIVITY_FOR_RESULT_FLAG", String.valueOf(true))});

            } else {

                ToastUtils1.longToast(getApplicationContext(), "Please Select a parent account...");
            }
            return true;
        });
        buttonAddToAccount.setOnClickListener(v -> bindPreviousToAccount());

        buttonAddFromAccount.setOnLongClickListener(v -> {

            if (!currentFromAccountIdParent.equals("0")) {

                ActivityUtils19.startActivityForClassWithStringExtras(currentActivityContext, Insert_Account.class, new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID", currentFromAccountIdParent), new Pair<>("CURRENT_ACCOUNT_FULL_NAME", buttonFromAccount.getText().toString().replace("From : ", "")), new Pair<>("CURRENT_ACCOUNT_TYPE", currentFromAccountType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", currentFromAccountCommodityType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", currentFromAccountCommodityValue), new Pair<>("CURRENT_ACCOUNT_TAXABLE", currentFromAccountTaxable), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", currentFromAccountPlaceHolder), new Pair<>("ACTIVITY_FOR_RESULT_FLAG", String.valueOf(true))});

            } else {

                ToastUtils1.longToast(getApplicationContext(), "Please Select a parent account...");
            }
            return true;
        });
        buttonAddFromAccount.setOnClickListener(v -> bindPreviousFromAccount());

        //Exchange Accounts Not Available in Via.
//        buttonDate.setOnLongClickListener(v -> {
//
//            exchangeAccounts();
//            return true;
//        });

        furtherInitializations();
    }

    public void furtherInitializations() {

        toAccountsStack = new Stack<>();
        buttonViaAccount = findViewById(R.id.buttonViaAccount);
        autoCompleteTextViewViaAccount = findViewById(R.id.autoCompleteTextViewViaAccount);
        Button buttonAddViaAccount = findViewById(R.id.buttonAddViaAccount);
        buttonViaAccount.setOnLongClickListener(v -> {

            initializeViaAccount();
            return true;
        });

        autoCompleteTextViewViaAccount.setOnItemClickListener((parent, view, position, id) -> {

            AccountLedgerLogUtils.debug("Item Position : " + position, currentApplicationContext);
            AccountLedgerLogUtils.debug("Selected Account : " + accounts.get(position).toString(), currentApplicationContext);

            buttonViaAccount.setText(buttonViaAccount.getText().equals(getString(R.string.via)) ? getString(R.string.via) + autoCompleteTextViewViaAccount.getText().toString() : buttonViaAccount.getText() + " : " + autoCompleteTextViewViaAccount.getText().toString());
            autoCompleteTextViewViaAccount.setHint(autoCompleteTextViewViaAccount.getText().toString() + " : ");

            currentViaAccountIdParent = accounts.get(position).getAccountId();
            currentViaAccountType = accounts.get(position).getAccountType();
            currentViaAccountCommodityType = accounts.get(position).getCommodityType();
            currentViaAccountCommodityValue = accounts.get(position).getCommodityValue();

            selectedViaAccountId = currentViaAccountIdParent;

            bindAutoTextViewOfViaAccount();

            viaAccountsStack.push(new Account(currentViaAccountType, currentViaAccountIdParent, accounts.get(position).getNotes(), accounts.get(position).getParentAccountId(), accounts.get(position).getOwnerId(), autoCompleteTextViewViaAccount.getHint().toString(), currentViaAccountCommodityType, currentViaAccountCommodityValue, buttonViaAccount.getText().toString()));
        });
        autoCompleteTextViewViaAccount.setOnClickListener(v -> autoCompleteTextViewViaAccount.showDropDown());
        autoCompleteTextViewViaAccount.addTextChangedListener(new TextWatcher() {

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
        autoCompleteTextViewViaAccount.setOnDismissListener(() -> {

            if (autoCompleteTextViewViaAccount.getListSelection() != ListView.INVALID_POSITION) {

                autoCompleteTextViewViaAccount.setText(autoCompleteTextViewViaAccount.getHint().toString().substring(0, autoCompleteTextViewViaAccount.getHint().length() - 3), false);
                autoCompleteTextViewViaAccount.setSelection(autoCompleteTextViewViaAccount.getText().length());
                furtherViaAccountSelectedActions();
            }
        });
        buttonAddViaAccount.setOnLongClickListener(v -> {

            if (!currentViaAccountIdParent.equals("0")) {

                ActivityUtils19.startActivityForClassWithStringExtras(currentActivityContext, Insert_Account.class, new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID", currentViaAccountIdParent), new Pair<>("CURRENT_ACCOUNT_FULL_NAME", buttonViaAccount.getText().toString().replace(getString(R.string.via), "")), new Pair<>("CURRENT_ACCOUNT_TYPE", currentViaAccountType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", currentViaAccountCommodityType), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", currentViaAccountCommodityValue), new Pair<>("CURRENT_ACCOUNT_TAXABLE", "F"), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", "F"), new Pair<>("ACTIVITY_FOR_RESULT_FLAG", String.valueOf(true))});

            } else {

                ToastUtils1.longToast(getApplicationContext(), "Please Select a parent account...");
            }
            return true;
        });

        buttonAddViaAccount.setOnClickListener(v -> bindPreviousViaAccount());

        bindAutoTextViewOfViaAccount();
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

    private void bindPreviousViaAccount() {

        if (viaAccountsStack.isEmpty()) {

            initializeViaAccount();

        } else {

            Account currentViaAccount = viaAccountsStack.pop();

            if (viaAccountsStack.isEmpty()) {

                initializeViaAccount();

            } else {

                Account previousViaAccount = viaAccountsStack.peek();

                currentViaAccountIdParent = previousViaAccount.getAccountId();
                currentViaAccountType = previousViaAccount.getAccountType();
                currentViaAccountCommodityType = previousViaAccount.getCommodityType();
                currentViaAccountCommodityValue = previousViaAccount.getCommodityValue();

                selectedViaAccountId = currentViaAccountIdParent;

                buttonViaAccount.setText(previousViaAccount.getFullName());

                autoCompleteTextViewViaAccount.setHint(previousViaAccount.getName());

                autoCompleteTextViewViaAccount.setText(autoCompleteTextViewViaAccount.getHint().toString().substring(0, autoCompleteTextViewViaAccount.getHint().length() - 3), false);

                autoCompleteTextViewViaAccount.setSelection(autoCompleteTextViewViaAccount.getText().length());

                bindAutoTextViewOfViaAccount();
            }
        }
    }

    private void initializeFromAccount() {

        currentFromAccountIdParent = "0";
        buttonFromAccount.setText(getResources().getString(R.string.from));
        autoCompleteTextViewFromAccount.setHint("");
        autoCompleteTextViewFromAccount.setText("", false);
        bindAutoTextViewOfFromAccount();
    }

    private void initializeToAccount() {

        currentToAccountIdParent = "0";
        buttonToAccount.setText(getResources().getString(R.string.to));
        autoCompleteTextViewToAccount.setHint("");
        autoCompleteTextViewToAccount.setText("", false);
        bindAutoTextViewOfToAccount();
    }

    private void initializeViaAccount() {

        currentViaAccountIdParent = "0";
        buttonViaAccount.setText(getResources().getString(R.string.via));
        autoCompleteTextViewViaAccount.setHint("");
        autoCompleteTextViewViaAccount.setText("", false);
        bindAutoTextViewOfViaAccount();
    }

    private void bindAutoTextViewOfToAccount() {

        HttpApiSelectTask.AsyncResponseJsonArray asyncResponseJsonArray = jsonArray -> {

            accounts = new ArrayList<>();
            ArrayList<String> accountFullNames = new ArrayList<>();

            try {
                if (!jsonArray.getJSONObject(0).getString("status").equals("1")) {

                    for (int i = 1; i < jsonArray.length(); i++) {

                        JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                        accounts.add(new Account(tempJsonObject.getString("account_type"), tempJsonObject.getString("account_id"), tempJsonObject.getString("notes"), tempJsonObject.getString("parent_account_id"), tempJsonObject.getString("owner_id"), tempJsonObject.getString("name"), tempJsonObject.getString("commodity_type"), tempJsonObject.getString("commodity_value"), tempJsonObject.getString("name")));
                        accountFullNames.add(tempJsonObject.getString("name"));
                    }
                } else {

                    editTextParticulars.requestFocus();
                }

            } catch (JSONException e) {

                AccountLedgerExceptionUtils.handleExceptionOnGui(currentActivityContext, e);
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

        HttpApiSelectTaskWrapper.executePostThenReturnJsonArrayWithErrorStatusAndBackgroundWorkStatus(
                RestGetTask.prepareGetUrl(
                        ApiWrapper.selectUserAccounts(),
                        new Pair[]{
                                new Pair<>(ApiMethodParameters.API_METHOD_PARAMETER_USER_ID, sharedPreferences.getString(SharedPreferenceKeys.SHARED_PREFERENCES_KEY_USER_ID, "0")),
                                new Pair<>("parent_account_id", currentToAccountIdParent)
                        }
                ),
                this,
                ApplicationSpecification.APPLICATION_NAME,
                asyncResponseJsonArray,
                false,
                true
        );
    }

    private void bindAutoTextViewOfFromAccount() {

        HttpApiSelectTask.AsyncResponseJsonArray asyncResponseJsonArray = jsonArray -> {

            accounts = new ArrayList<>();
            ArrayList<String> accountFullNames = new ArrayList<>();

            try {

                if (!jsonArray.getJSONObject(0).getString("status").equals("1")) {

                    for (int i = 1; i < jsonArray.length(); i++) {

                        JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                        accounts.add(new Account(tempJsonObject.getString("account_type"), tempJsonObject.getString("account_id"), tempJsonObject.getString("notes"), tempJsonObject.getString("parent_account_id"), tempJsonObject.getString("owner_id"), tempJsonObject.getString("name"), tempJsonObject.getString("commodity_type"), tempJsonObject.getString("commodity_value"), tempJsonObject.getString("name")));
                        accountFullNames.add(tempJsonObject.getString("name"));
                    }
                } else {

                    autoCompleteTextViewToAccount.requestFocus();
                }

            } catch (JSONException e) {

                AccountLedgerExceptionUtils.handleExceptionOnGui(currentActivityContext, e);
            }

            //Creating the instance of ArrayAdapter containing list of fruit names
            ArrayAdapter<String> adapter = new ArrayAdapter<>(currentActivityContext, android.R.layout.select_dialog_item, accountFullNames);

            autoCompleteTextViewFromAccount.setThreshold(1);
            autoCompleteTextViewFromAccount.setAdapter(adapter);
            autoCompleteTextViewFromAccount.setTextColor(Color.RED);
            autoCompleteTextViewFromAccount.showDropDown();
        };

        HttpApiSelectTaskWrapper.executePostThenReturnJsonArrayWithErrorStatusAndBackgroundWorkStatus(
                RestGetTask.prepareGetUrl(
                        ApiWrapper.selectUserAccounts(),
                        new Pair[]{
                                new Pair<>(ApiMethodParameters.API_METHOD_PARAMETER_USER_ID, sharedPreferences.getString(SharedPreferenceKeys.SHARED_PREFERENCES_KEY_USER_ID, "0")),
                                new Pair<>("parent_account_id", currentFromAccountIdParent)
                        }
                ),
                this,
                ApplicationSpecification.APPLICATION_NAME,
                asyncResponseJsonArray,
                false,
                true
        );
    }

    public void furtherViaAccountSelectedActions() {

        bindAutoTextViewOfToAccount();
    }

    private void bindAutoTextViewOfViaAccount() {

        HttpApiSelectTask.AsyncResponseJsonArray asyncResponseJsonArray = jsonArray -> {

            accounts = new ArrayList<>();
            ArrayList<String> accountFullNames = new ArrayList<>();

            try {
                if (!jsonArray.getJSONObject(0).getString("status").equals("1")) {

                    for (int i = 1; i < jsonArray.length(); i++) {

                        JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                        accounts.add(new Account(tempJsonObject.getString("account_type"), tempJsonObject.getString("account_id"), tempJsonObject.getString("notes"), tempJsonObject.getString("parent_account_id"), tempJsonObject.getString("owner_id"), tempJsonObject.getString("name"), tempJsonObject.getString("commodity_type"), tempJsonObject.getString("commodity_value"), tempJsonObject.getString("name")));
                        accountFullNames.add(tempJsonObject.getString("name"));
                    }
                } else {

                    autoCompleteTextViewToAccount.requestFocus();
                }

            } catch (JSONException e) {

                AccountLedgerExceptionUtils.handleExceptionOnGui(currentActivityContext, e);
            }

            //Creating the instance of ArrayAdapter containing list of fruit names
            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(currentActivityContext, android.R.layout.select_dialog_item, accountFullNames);

            autoCompleteTextViewViaAccount.setThreshold(1);
            autoCompleteTextViewViaAccount.setAdapter(stringArrayAdapter);
            autoCompleteTextViewViaAccount.setTextColor(Color.RED);
            autoCompleteTextViewViaAccount.showDropDown();
        };

        HttpApiSelectTaskWrapper.executePostThenReturnJsonArrayWithErrorStatusAndBackgroundWorkStatus(
                RestGetTask.prepareGetUrl(
                        ApiWrapper.selectUserAccounts(),
                        new Pair[]{
                                new Pair<>(ApiMethodParameters.API_METHOD_PARAMETER_USER_ID, sharedPreferences.getString(SharedPreferenceKeys.SHARED_PREFERENCES_KEY_USER_ID, "0")),
                                new Pair<>("parent_account_id", currentViaAccountIdParent)
                        }
                ),
                this,
                ApplicationSpecification.APPLICATION_NAME,
                asyncResponseJsonArray,
                false,
                true
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.insert_transaction, menu);
        menu.findItem(R.id.menu_item_insert_via_transaction).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_item_view_from_account_pass_book) {

            ActivityUtils19.startActivityForClassWithStringExtras(
                    this,
                    ClickablePassBookBundle.class,
                    new Pair[]{
                            new Pair<>(
                                    "URL",
                                    RestGetTask.prepareGetUrl(
                                            ApiWrapper.selectUserTransactionsV2(),
                                            new Pair[]{
                                                    new Pair<>(ApiMethodParameters.API_METHOD_PARAMETER_USER_ID, sharedPreferences.getString(SharedPreferenceKeys.SHARED_PREFERENCES_KEY_USER_ID, "0")),
                                                    new Pair<>("account_id", getIntent().getStringExtra("CURRENT_ACCOUNT_ID"))
                                            }
                                    )
                            ),
                            new Pair<>("application_name", ApplicationSpecification.APPLICATION_NAME),
                            new Pair<>("V2_FLAG", getIntent().getStringExtra("CURRENT_ACCOUNT_ID"))
                    }
            );
        }
        return super.onOptionsItemSelected(item);
    }

    public void attemptInsertTransaction() {


        if (selectedViaAccountId.equals("0")) {

            ToastUtils1.longToast(this, "Please select Via A/C…");

        } else if (selectedToAccountId.equals("0")) {

            ToastUtils1.longToast(this, "Please select To A/C…");

        } else {

            ValidationUtils16.resetErrors(new EditText[]{editTextParticulars, editTextAmount});
            Pair<Boolean, EditText> emptyCheckResult = ValidationUtils16.emptyCheckEditTextPairs(new Pair[]{new Pair<>(editTextAmount, "Please Enter Valid Amount…"), new Pair<>(editTextParticulars, "Please Enter Particulars…")});

            if (Objects.requireNonNull(emptyCheckResult.first)) {

                if (emptyCheckResult.second != null) {

                    emptyCheckResult.second.requestFocus();
                }

            } else {

                Pair<Boolean, EditText> zeroCheckResult = ValidationUtils16.zeroCheckEditTextPairs(new Pair[]{new Pair<>(editTextAmount, "Please Enter Valid Amount…")});

                if (Objects.requireNonNull(zeroCheckResult.first)) {

                    if (zeroCheckResult.second != null) {

                        zeroCheckResult.second.requestFocus();
                    }

                } else {

                    String viaAccountFullName = buttonViaAccount.getText().toString();
                    String particulars = editTextParticulars.getText().toString().trim() + " Via. " + viaAccountFullName.substring(viaAccountFullName.lastIndexOf(" : ") + 3);
                    double amount = Double.parseDouble(editTextAmount.getText().toString().trim());

                    NetworkUtils14.FurtherActions furtherActions = () -> InsertTransactionV2Utils.executeInsertTransactionTaskWithClearingOfEditTextsAndIncrementingOfButtonTextTimeStampForFiveMinutes(
                            progressBarView,
                            formView,
                            currentActivityContext,
                            currentAppCompatActivity,
                            sharedPreferences.getString(SharedPreferenceKeys.SHARED_PREFERENCES_KEY_USER_ID, "0"),
                            particulars,
                            amount,
                            Integer.parseInt(selectedViaAccountId),
                            Integer.parseInt(selectedToAccountId),
                            editTextParticulars,
                            editTextAmount,
                            buttonDate,
                            CalendarUtils.addFiveMinutesToCalendar(calendar)
                    );

                    InsertTransactionV2Utils.executeInsertTransactionTaskWithFurtherActions(
                            progressBarView,
                            formView,
                            this,
                            this,
                            sharedPreferences.getString(SharedPreferenceKeys.SHARED_PREFERENCES_KEY_USER_ID, "0"),
                            particulars,
                            amount,
                            Integer.parseInt(selectedFromAccountId),
                            Integer.parseInt(selectedViaAccountId),
                            editTextParticulars,
                            calendar,
                            furtherActions
                    );
                }
            }
        }
    }
}

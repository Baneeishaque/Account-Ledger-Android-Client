package ndk.personal.account_ledger.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.constants.Api;
import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android1.NetworkUtils;
import ndk.utils_android16.Spinner_Utils;
import ndk.utils_android16.ValidationUtils;
import ndk.utils_android16.network_task.RestInsertTaskWrapper;

public class Insert_Account extends AppCompatActivity {

    Context application_context;
    SharedPreferences settings;
    Spinner spinner_account_type, spinner_commodity_type, spinner_commodity_value;
    CheckBox checkBox_taxable, checkBox_place_holder;
    private ProgressBar login_progress;
    private Button button_full_name;
    private EditText edit_name, edit_notes;
    private ScrollView login_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_account);

        application_context = getApplicationContext();

        settings = getApplicationContext().getSharedPreferences(ApplicationSpecification.APPLICATION_NAME, Context.MODE_PRIVATE);

        login_form = findViewById(R.id.login_form);
        Button button_submit = findViewById(R.id.buttonSubmit);
        button_full_name = findViewById(R.id.button_full_name);
        edit_name = findViewById(R.id.name);
        edit_notes = findViewById(R.id.notes);
        spinner_account_type = findViewById(R.id.spinner_account_type);
        spinner_commodity_type = findViewById(R.id.spinner_commodity_type);
        spinner_commodity_value = findViewById(R.id.spinner_commodity_value);
        login_progress = findViewById(R.id.login_progress);
        checkBox_taxable = findViewById(R.id.checkBox_taxable);
        checkBox_place_holder = findViewById(R.id.checkBox_place_holder);

        button_full_name.setText(getIntent().getStringExtra("CURRENT_ACCOUNT_FULL_NAME"));

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_insert_Account();
            }
        });

        Spinner_Utils.attach_items_to_simple_spinner(this, spinner_account_type, new ArrayList<>(Collections.singletonList(getIntent().getStringExtra("CURRENT_ACCOUNT_TYPE"))));

        Spinner_Utils.attach_items_to_simple_spinner(this, spinner_commodity_type, new ArrayList<>(Collections.singletonList(getIntent().getStringExtra("CURRENT_ACCOUNT_COMMODITY_TYPE"))));

        Spinner_Utils.attach_items_to_simple_spinner(this, spinner_commodity_value, new ArrayList<>(Collections.singletonList(getIntent().getStringExtra("CURRENT_ACCOUNT_COMMODITY_VALUE"))));

        checkBox_taxable.setSelected(getIntent().getStringExtra("CURRENT_ACCOUNT_TAXABLE").equals("T"));
        checkBox_place_holder.setSelected(getIntent().getStringExtra("CURRENT_ACCOUNT_PLACE_HOLDER").equals("T"));

        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edit_name.getText().toString().isEmpty()) {
                    button_full_name.setText(getIntent().getStringExtra("CURRENT_ACCOUNT_FULL_NAME") + " : " + edit_name.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void attempt_insert_Account() {

        ValidationUtils.resetErrors(new EditText[]{edit_name});
        Pair<Boolean, EditText> empty_check_result = ValidationUtils.emptyCheckEditTextPairs(new Pair[]{new Pair<>(edit_name, "Please Enter A/C Name...")});

        if (empty_check_result.first) {
            // There was an error; don't attempt login and focus the first form field with an error.
            if (empty_check_result.second != null) {
                empty_check_result.second.requestFocus();
            }
        } else {

            execute_insert_Account_Task();
        }
    }

    private void execute_insert_Account_Task() {

        /*
        $full_name = filter_input(INPUT_POST, 'full_name');
        $name = filter_input(INPUT_POST, 'name');
        $parent_account_id = filter_input(INPUT_POST, 'parent_account_id');
        $account_type = filter_input(INPUT_POST, 'account_type');
        $notes = filter_input(INPUT_POST, 'notes');
        $commodity_type = filter_input(INPUT_POST, 'commodity_type');
        $commodity_value = filter_input(INPUT_POST, 'commodity_value');
        $owner_id = filter_input(INPUT_POST, 'owner_id');
        $taxable = filter_input(INPUT_POST, 'taxable');
        $place_holder = filter_input(INPUT_POST, 'place_holder');
         */


        NetworkUtils.FurtherActions further_actions = () -> {

            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        };

        if (getIntent().getStringExtra("ACTIVITY_FOR_RESULT_FLAG") != null) {

            RestInsertTaskWrapper.execute(this, ApiWrapper.insertAccount(), this, login_progress, login_form, ApplicationSpecification.APPLICATION_NAME, new Pair[]{new Pair<>("full_name", button_full_name.getText().toString().replace(" : ", ":")), new Pair<>("name", edit_name.getText().toString().trim()), new Pair<>("parent_account_id", getIntent().getStringExtra("CURRENT_ACCOUNT_ID")), new Pair<>("account_type", spinner_account_type.getSelectedItem().toString()), new Pair<>("notes", edit_notes.getText().toString().trim()), new Pair<>("commodity_type", spinner_commodity_type.getSelectedItem().toString()), new Pair<>("commodity_value", spinner_commodity_value.getSelectedItem().toString()), new Pair<>("owner_id", settings.getString("user_id", "0")), new Pair<>("taxable", checkBox_taxable.isSelected() ? "T" : "F"), new Pair<>("place_holder", checkBox_place_holder.isSelected() ? "T" : "F")}, edit_name, further_actions);


        } else {

            RestInsertTaskWrapper.execute(this, ApiWrapper.insertAccount(), this, login_progress, login_form, ApplicationSpecification.APPLICATION_NAME, new Pair[]{new Pair<>("full_name", button_full_name.getText().toString().replace(" : ", ":")), new Pair<>("name", edit_name.getText().toString().trim()), new Pair<>("parent_account_id", getIntent().getStringExtra("CURRENT_ACCOUNT_ID")), new Pair<>("account_type", spinner_account_type.getSelectedItem().toString()), new Pair<>("notes", edit_notes.getText().toString().trim()), new Pair<>("commodity_type", spinner_commodity_type.getSelectedItem().toString()), new Pair<>("commodity_value", spinner_commodity_value.getSelectedItem().toString()), new Pair<>("owner_id", settings.getString("user_id", "0")), new Pair<>("taxable", checkBox_taxable.isSelected() ? "T" : "F"), new Pair<>("place_holder", checkBox_place_holder.isSelected() ? "T" : "F")}, edit_name);

        }
    }
}

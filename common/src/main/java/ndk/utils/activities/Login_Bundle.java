package ndk.utils.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ndk.utils.Activity_Utils;
import ndk.utils.Exception_Utils;
import ndk.utils.R;
import ndk.utils.SharedPreference_Utils;
import ndk.utils.Toast_Utils;
import ndk.utils.Validation_Utils;
import ndk.utils.network_task.REST_GET_Task;
import ndk.utils.network_task.REST_Select_Task;
import ndk.utils.network_task.REST_Select_Task_Wrapper;

public class Login_Bundle extends AppCompatActivity {

    Context activity_context = this;

    // UI references.
    private EditText username;
    private EditText password;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        username = findViewById(R.id.username);
        password = findViewById(R.id.passcode);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button sign_in_button = findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        Validation_Utils.reset_errors(new EditText[]{username, password});

        Pair<Boolean, EditText> empty_check_result = Validation_Utils.empty_check(new Pair[]{new Pair<>(username, "Please Enter Username..."), new Pair<>(password, "Please Enter Passcode...")});

        if (empty_check_result.first) {
            // There was an error; don't attempt login and focus the first form field with an error.
            if (empty_check_result.second != null) {
                empty_check_result.second.requestFocus();
            }
        } else {
            InputMethodManager inputManager =
                    (InputMethodManager) getApplicationContext().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(
                        Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            REST_Select_Task.Async_Response_JSON_object async_response_json_object = new REST_Select_Task.Async_Response_JSON_object() {

                @Override
                public void processFinish(JSONObject json_object) {

                    try {
                        String user_count = json_object.getString("user_count");
                        switch (user_count) {
                            case "1":
                                SharedPreference_Utils.commit_Shared_Preferences(getApplicationContext(), getIntent().getStringExtra("APPLICATION_NAME"), new Pair[]{new Pair<>("user_id", json_object.getString("id"))});
                                Log.d(getIntent().getStringExtra("APPLICATION_NAME"), "Next Activity : " + getIntent().getStringExtra("NEXT_ACTIVITY_CLASS"));
                                Activity_Utils.start_activity_with_finish(activity_context, Class.forName(getIntent().getStringExtra("NEXT_ACTIVITY_CLASS")), getIntent().getStringExtra("APPLICATION_NAME"));
                                break;

                            case "0":
                                Toast.makeText(activity_context, "Login Failure!", Toast.LENGTH_LONG).show();
                                username.requestFocus();
                                break;

                            default:
                                Toast.makeText(activity_context, "Error : Check json", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast_Utils.longToast(getApplicationContext(), "JSON Response Error...");
                        Log.d(getIntent().getStringExtra("APPLICATION_NAME"), Exception_Utils.get_exception_details(e));
                    } catch (ClassNotFoundException e) {
                        Toast_Utils.longToast(getApplicationContext(), "Unknown Class Error...");
                        Log.d(getIntent().getStringExtra("APPLICATION_NAME"), Exception_Utils.get_exception_details(e));
                    }
                }
            };

            Log.d(getIntent().getStringExtra("APPLICATION_NAME"), "Username : " + username.getText().toString());
            Log.d(getIntent().getStringExtra("APPLICATION_NAME"), "Password : " + password.getText().toString());

            REST_Select_Task_Wrapper.execute(REST_GET_Task.get_Get_URL(getIntent().getStringExtra("SELECT_USER_URL"), new Pair[]{new Pair<>("username", username.getText().toString()), new Pair<>("password", password.getText().toString())}), activity_context, mProgressView, mLoginFormView, getIntent().getStringExtra("APPLICATION_NAME"), new Pair[]{}, async_response_json_object);
        }
    }
}


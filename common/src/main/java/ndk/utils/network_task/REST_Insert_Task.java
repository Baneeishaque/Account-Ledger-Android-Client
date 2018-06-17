package ndk.utils.network_task;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import ndk.utils.Network_Utils;
import ndk.utils.Network_Utils.further_Actions;

import static ndk.utils.Network_Utils.perform_http_client_network_task;
import static ndk.utils.ProgressBar_Utils.showProgress;

public class REST_Insert_Task extends AsyncTask<Void, Void, String[]> {

    private String URL, TAG;
    private AppCompatActivity current_activity;
    private View progressBar, form, focus_on_error;
    private Pair[] name_value_pair;
    private Class next_activity;
    private boolean finish_flag = true;
    private boolean self_finish_flag = true;
    boolean further_actions_flag = true;
    private EditText[] texts_to_clear;
    private boolean clear_fields_flag = true;
    further_Actions further_actions;
    private Pair[] next_class_extras;

    //finish with next activity
    public REST_Insert_Task(String URL, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pair[] name_value_pair, View focus_on_error, Class next_activity) {
        this.URL = URL;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.focus_on_error = focus_on_error;
        this.next_activity = next_activity;
    }

    //finish with next activity and extras
    public REST_Insert_Task(String URL, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pair[] name_value_pair, View focus_on_error, Class next_activity, Pair[] next_class_extras) {
        this.URL = URL;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.focus_on_error = focus_on_error;
        this.next_activity = next_activity;
        this.next_class_extras = next_class_extras;

        this.finish_flag = false;
        this.self_finish_flag = false;
        this.clear_fields_flag = false;
        this.further_actions_flag = false;
    }

    //further actions
    public REST_Insert_Task(String URL, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pair[] name_value_pair, View focus_on_error, further_Actions further_actions) {

        this.URL = URL;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.focus_on_error = focus_on_error;

        this.finish_flag = false;
        this.self_finish_flag = false;
        this.clear_fields_flag = false;

        this.further_actions = further_actions;
    }

    //clear fields
    public REST_Insert_Task(String URL, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pair[] name_value_pair, View focus_on_error, EditText[] texts_to_clear) {
        this.URL = URL;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.focus_on_error = focus_on_error;
        this.finish_flag = false;
        this.self_finish_flag = false;
        this.texts_to_clear = texts_to_clear;
    }

    //self finish
    public REST_Insert_Task(String URL, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pair[] name_value_pair, View focus_on_error) {
        this.URL = URL;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.focus_on_error = focus_on_error;
        this.finish_flag = false;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        return perform_http_client_network_task(URL, name_value_pair);
    }

    @Override
    protected void onPostExecute(final String[] network_action_response_array) {
        showProgress(false, current_activity, progressBar, form);
        if (finish_flag) {
            Network_Utils.handle_json_insertion_response_and_switch_with_finish_or_clear_fields(network_action_response_array, current_activity, next_activity, new EditText[]{}, focus_on_error, TAG, 1, new Pair[]{}, further_actions);
        } else if (self_finish_flag) {
            Network_Utils.handle_json_insertion_response_and_switch_with_finish_or_clear_fields(network_action_response_array, current_activity, next_activity, new EditText[]{}, focus_on_error, TAG, 3, new Pair[]{}, further_actions);
        } else if (clear_fields_flag) {
            Network_Utils.handle_json_insertion_response_and_switch_with_finish_or_clear_fields(network_action_response_array, current_activity, next_activity, texts_to_clear, focus_on_error, TAG, 2, new Pair[]{}, further_actions);
        } else if (further_actions_flag) {
            Network_Utils.handle_json_insertion_response_and_switch_with_finish_or_clear_fields(network_action_response_array, current_activity, next_activity, new EditText[]{}, focus_on_error, TAG, 5, next_class_extras, further_actions);
        } else {
            Network_Utils.handle_json_insertion_response_and_switch_with_finish_or_clear_fields(network_action_response_array, current_activity, next_activity, new EditText[]{}, focus_on_error, TAG, 4, next_class_extras, further_actions);
        }
    }

    @Override
    protected void onCancelled() {
        showProgress(false, current_activity, progressBar, form);
    }
}
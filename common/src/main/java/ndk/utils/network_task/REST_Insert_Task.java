package ndk.utils.network_task;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import ndk.utils.Network_Utils;

import static ndk.utils.Network_Utils.perform_http_client_network_task;
import static ndk.utils.ProgressBar_Utils.showProgress;

public class REST_Insert_Task extends AsyncTask<Void, Void, String[]> {

    private String URL, TAG;
    private AppCompatActivity current_activity;
    private View progressBar, form, focus_on_error;
    private Pair[] name_value_pair;
    private Class next_activity;
    private boolean finish_flag = true;
    private EditText[] texts_to_clear;

    REST_Insert_Task(String URL, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pair[] name_value_pair, View focus_on_error, Class next_activity) {
        this.URL = URL;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.focus_on_error = focus_on_error;
        this.next_activity = next_activity;
    }

    public REST_Insert_Task(String URL, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pair[] name_value_pair, View focus_on_error, EditText[] texts_to_clear) {
        this.URL = URL;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.focus_on_error = focus_on_error;
        this.finish_flag = false;
        this.texts_to_clear = texts_to_clear;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        return perform_http_client_network_task(URL, name_value_pair);
    }

    @Override
    protected void onPostExecute(final String[] network_action_response_array) {
        showProgress(false, current_activity, progressBar, form);
        if (finish_flag) {
            Network_Utils.handle_json_insertion_response_and_switch_with_finish_or_clear_fields(network_action_response_array, current_activity, next_activity, new EditText[]{}, focus_on_error, TAG, 1);
        } else {
            Network_Utils.handle_json_insertion_response_and_switch_with_finish_or_clear_fields(network_action_response_array, current_activity, next_activity, texts_to_clear, focus_on_error, TAG, 2);
        }
    }

    @Override
    protected void onCancelled() {
        showProgress(false, current_activity, progressBar, form);
    }
}
package ndk.personal.account_ledger.to_utils;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import ndk.utils.ProgressBar_Utils;

public class REST_Insert_Task extends AsyncTask<Void, Void, String[]> {
    Class next_activity;
    boolean finish_flag;
    EditText[] texts_to_clear;
    private String URL;
    private String TAG;
    private REST_Insert_Task REST_Insert_task;
    private AppCompatActivity current_activity;
    private View progressBar;
    private View form;
    private View focus_on_error;
    private Pair[] name_value_pair;

    public REST_Insert_Task(String URL, REST_Insert_Task REST_Insert_task, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pair[] name_value_pair, View focus_on_error, Class next_activity, boolean finish_flag, EditText[] texts_to_clear) {
        this.URL = URL;
        this.REST_Insert_task = REST_Insert_task;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.focus_on_error = focus_on_error;
        this.next_activity = next_activity;
        this.finish_flag = finish_flag;
        this.texts_to_clear = texts_to_clear;
    }

    protected String[] doInBackground(Void... params) {
        return Network_Utils.perform_http_client_network_task(this.URL, this.name_value_pair);
    }

    protected void onPostExecute(String[] network_action_response_array) {
        this.REST_Insert_task = null;
        ProgressBar_Utils.showProgress(false, this.current_activity, this.progressBar, this.form);
        if (finish_flag) {

            Network_Utils.handle_json_insertion_response_and_switch_with_finish(network_action_response_array, this.current_activity, this.next_activity, this.focus_on_error, this.TAG);

        } else {
            Network_Utils.handle_json_insertion_response_and_clear_fields(network_action_response_array, this.current_activity, this.next_activity, texts_to_clear, this.focus_on_error, this.TAG);
        }
    }

    protected void onCancelled() {
        this.REST_Insert_task = null;
        ProgressBar_Utils.showProgress(false, this.current_activity, this.progressBar, this.form);
    }
}
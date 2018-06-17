package ndk.utils.network_task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ndk.utils.Network_Utils;

import static ndk.utils.Network_Utils.perform_http_client_network_task;
import static ndk.utils.ProgressBar_Utils.showProgress;

public class REST_Select_Task extends AsyncTask<Void, Void, String[]> {

    private String URL, TAG;
    private Context context;
    private View progressBar, form;

    private int progress_flag = 0;
    private int response_flag = 0;
    private int splash_flag = 0;
    private boolean background_flag = false;

    private Pair[] name_value_pair;

    private boolean error_flag = true;
    private Async_Response_JSON_array async_response_json_array = null;
    private Async_Response async_response = null;
    private Async_Response_JSON_object async_response_json_object = null;

    public REST_Select_Task(String URL, Context context, View progressBar, View form, String TAG, Pair[] name_value_pair, Async_Response_JSON_array async_response_json_array
    ) {

        this.URL = URL;
        this.context = context;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.async_response_json_array = async_response_json_array;
    }

    REST_Select_Task(String URL, Context context, View progressBar, View form, String TAG, Pair[] name_value_pair, Async_Response async_response) {

        this.URL = URL;
        this.context = context;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.async_response = async_response;
        response_flag = 1;
    }

    REST_Select_Task(String URL, Context context, View progressBar, View form, String TAG, Pair[] name_value_pair, Async_Response_JSON_object async_response_json_object) {

        this.URL = URL;
        this.context = context;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.async_response_json_object = async_response_json_object;
        response_flag = 2;
    }

    public REST_Select_Task(String URL, Context context, String TAG, Pair[] name_value_pair, Async_Response async_response) {

        this.URL = URL;
        this.context = context;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.async_response = async_response;
        progress_flag = 1;
        response_flag = 1;
    }

    public REST_Select_Task(String URL, Context context, String TAG, Pair[] name_value_pair, Async_Response_JSON_array async_response_json_array) {

        this.URL = URL;
        this.context = context;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.async_response_json_array = async_response_json_array;
        progress_flag = 1;
        splash_flag = 1;
    }

    public REST_Select_Task(String URL, Context context, View progressBar, View form, String TAG, Pair[] name_value_pair, Async_Response_JSON_array async_response_json_array, boolean error_flag) {

        this.URL = URL;
        this.context = context;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.async_response_json_array = async_response_json_array;
        this.progressBar = progressBar;
        this.form = form;
        this.error_flag = error_flag;
    }

    public REST_Select_Task(String URL, Context context, String TAG, Pair[] name_value_pair, Async_Response_JSON_array async_response_json_array, boolean error_flag) {

        this.URL = URL;
        this.context = context;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.async_response_json_array = async_response_json_array;
        this.progress_flag = 1;
        this.error_flag = error_flag;
    }

    public REST_Select_Task(String URL, Context context, String TAG, Pair[] name_value_pair, Async_Response_JSON_array async_response_json_array, boolean error_flag, boolean background_flag) {

        this.URL = URL;
        this.context = context;
        this.TAG = TAG;
        this.name_value_pair = name_value_pair;
        this.async_response_json_array = async_response_json_array;
        this.progress_flag = 1;
        this.error_flag = error_flag;
        this.background_flag = background_flag;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        Log.d(TAG, "URL is " + URL);
        return perform_http_client_network_task(URL, name_value_pair);
    }

    @Override
    protected void onPostExecute(final String[] network_action_response_array) {

        if (progress_flag == 0) {
            showProgress(false, context, progressBar, form);
        }

        if (response_flag == 1) {

            Log.d(TAG, "Network Action status is " + network_action_response_array[0]);
            Log.d(TAG, "Network Action response is " + network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {

                Network_Utils.display_Friendly_Exception_Message(context, network_action_response_array[1]);
                Log.d(TAG, "Network Action response is " + network_action_response_array[1]);
                async_response.processFinish("exception");

            } else {
                async_response.processFinish(network_action_response_array[1]);
            }

        } else if (response_flag == 2) {

            Log.d(TAG, "Network Action status is " + network_action_response_array[0]);
            Log.d(TAG, "Network Action response is " + network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {

                Network_Utils.display_Friendly_Exception_Message(context, network_action_response_array[1]);
                Log.d(TAG, "Network Action response is " + network_action_response_array[1]);

            } else {
                try {
                    JSONObject json_object = new JSONObject(network_action_response_array[1]);
                    async_response_json_object.processFinish(json_object);

                } catch (JSONException e) {
                    Toast.makeText(context, "Error...", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Error : " + e.getLocalizedMessage());
                }
            }

        } else {

            Log.d(TAG, "Network Action Response Array 0 : " + network_action_response_array[0]);
            Log.d(TAG, "Network Action Response Array 1 : " + network_action_response_array[1]);

            if (network_action_response_array[0].equals("1")) {

                if (background_flag) {
                    Log.d(TAG, "Error...");
                } else {
                    Toast.makeText(context, "Error...", Toast.LENGTH_LONG).show();
                }

                Log.d(TAG, "Network Action Response Array 1 : " + network_action_response_array[1]);

                if (splash_flag == 1) {
                    ((AppCompatActivity) context).finish();
                }

            } else {

                try {
                    JSONArray json_array = new JSONArray(network_action_response_array[1]);

                    if ((splash_flag == 1) || (!error_flag)) {
                        async_response_json_array.processFinish(json_array);
                    } else {
                        if (json_array.getJSONObject(0).getString("status").equals("1")) {
                            if (background_flag) {
                                Log.d(TAG, "No Entries...");
                            } else {
                                Toast.makeText(context, "No Entries...", Toast.LENGTH_LONG).show();
                            }
                        } else if (json_array.getJSONObject(0).getString("status").equals("0")) {
                            async_response_json_array.processFinish(json_array);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Error...", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Error : " + e.getLocalizedMessage());
                }
            }
        }
    }

    @Override
    protected void onCancelled() {
        if (progress_flag == 0) {
            showProgress(false, context, progressBar, form);
        }
    }

    public interface Async_Response_JSON_array {
        void processFinish(JSONArray json_array);
    }

    public interface Async_Response {
        void processFinish(String response);
    }

    public interface Async_Response_JSON_object {
        void processFinish(JSONObject json_object);
    }
}
package ndk.utils.network_task;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ndk.utils.Network_Utils;
import ndk.utils.ProgressBar_Utils;
import ndk.utils.Toast_Utils;

import static ndk.utils.Network_Utils.isOnline;

public class REST_Select_Task_Wrapper {

    public static void execute(String task_URL, Context context, View mProgressView, View mLoginFormView, String application_Name, Pair[] name_value_pairs, REST_Select_Task.Async_Response async_response) {

        if (isOnline(context)) {
            ProgressBar_Utils.showProgress(true, context, mProgressView, mLoginFormView);
            REST_Select_Task rest_select_task = new REST_Select_Task(task_URL, context, mProgressView, mLoginFormView, application_Name, name_value_pairs, async_response);

            rest_select_task.execute();
        } else {
            Toast_Utils.longToast(context, "Internet is unavailable");
        }
    }

    public static void execute(String task_URL, Context context, View mProgressView, View mLoginFormView, String application_Name, Pair[] name_value_pairs, REST_Select_Task.Async_Response_JSON_object async_response_json_object) {

        if (isOnline(context)) {
            ProgressBar_Utils.showProgress(true, context, mProgressView, mLoginFormView);
            REST_Select_Task rest_select_task = new REST_Select_Task(task_URL, context, mProgressView, mLoginFormView, application_Name, name_value_pairs, async_response_json_object);

            rest_select_task.execute();
        } else {
            Toast_Utils.longToast(context, "Internet is unavailable");
        }
    }

    public static void execute(String task_URL, Context context, View mProgressView, View mLoginFormView, String application_Name, Pair[] name_value_pairs, REST_Select_Task.Async_Response_JSON_array async_response_json_array_with_error_status_delegate) {

        if (isOnline(context)) {
            ProgressBar_Utils.showProgress(true, context, mProgressView, mLoginFormView);
            REST_Select_Task rest_select_task = new REST_Select_Task(task_URL, context, mProgressView, mLoginFormView, application_Name, name_value_pairs, async_response_json_array_with_error_status_delegate);

            rest_select_task.execute();
        } else {
            Toast_Utils.longToast(context, "Internet is unavailable");
        }
    }

    public static void execute_splash(final Context context, final String task_URL, final String application_Name, final Pair[] name_value_pairs, final REST_Select_Task.Async_Response_JSON_array async_response_json_array) {

        if (Network_Utils.isOnline(context)) {
            REST_Select_Task rest_select_task = new REST_Select_Task(task_URL, context, application_Name, name_value_pairs, async_response_json_array);
            rest_select_task.execute();
        } else {
            View.OnClickListener retry_Failed_Network_Task = new View.OnClickListener() {
                public void onClick(View view) {
                    execute_splash(context, task_URL, application_Name, name_value_pairs, async_response_json_array);
                }
            };
            Network_Utils.display_Long_no_FAB_no_network_bottom_SnackBar(((AppCompatActivity) context).getWindow().getDecorView(), retry_Failed_Network_Task);
        }
    }

}

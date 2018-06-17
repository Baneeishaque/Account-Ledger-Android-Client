package ndk.utils.network_task;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import ndk.utils.ProgressBar_Utils;
import ndk.utils.Toast_Utils;

import static ndk.utils.Network_Utils.further_Actions;
import static ndk.utils.Network_Utils.isOnline;

public class REST_Insert_Task_Wrapper {

    public static void execute(Context context, String task_URL, AppCompatActivity current_activity, View mProgressView, View mLoginFormView, String APPLICATION_NAME, Pair[] name_value_pairs, View view_to_focus_on_error, Class next_activity) {

        Log.d(APPLICATION_NAME, "REST Insert TASK URL : " + task_URL);

        if (isOnline(context)) {

            ProgressBar_Utils.showProgress(true, context, mProgressView, mLoginFormView);

            REST_Insert_Task rest_insert_task = new REST_Insert_Task(task_URL, current_activity, mProgressView, mLoginFormView, APPLICATION_NAME, name_value_pairs, view_to_focus_on_error, next_activity);

            rest_insert_task.execute();
        } else {
            Toast_Utils.longToast(context, "Internet is unavailable");
        }
    }

    public static void execute(Context context, String task_URL, AppCompatActivity current_activity, View mProgressView, View mLoginFormView, String APPLICATION_NAME, Pair[] name_value_pairs, View view_to_focus_on_error, Class next_activity, Pair[] next_class_extras) {

        Log.d(APPLICATION_NAME, "REST Insert TASK URL : " + task_URL);

        if (isOnline(context)) {

            ProgressBar_Utils.showProgress(true, context, mProgressView, mLoginFormView);

            REST_Insert_Task rest_insert_task = new REST_Insert_Task(task_URL, current_activity, mProgressView, mLoginFormView, APPLICATION_NAME, name_value_pairs, view_to_focus_on_error, next_activity, next_class_extras);

            rest_insert_task.execute();
        } else {
            Toast_Utils.longToast(context, "Internet is unavailable");
        }
    }

    public static void execute(Context context, String task_URL, AppCompatActivity current_activity, View mProgressView, View mLoginFormView, String APPLICATION_NAME, Pair[] name_value_pairs, View view_to_focus_on_error, EditText[] texts_to_clear) {

        Log.d(APPLICATION_NAME, "REST Insert TASK URL : " + task_URL);

        if (isOnline(context)) {

            ProgressBar_Utils.showProgress(true, context, mProgressView, mLoginFormView);

            REST_Insert_Task rest_insert_task = new REST_Insert_Task(task_URL, current_activity, mProgressView, mLoginFormView, APPLICATION_NAME, name_value_pairs, view_to_focus_on_error, texts_to_clear);

            rest_insert_task.execute();
        } else {
            Toast_Utils.longToast(context, "Internet is unavailable");
        }
    }

    public static void execute(Context context, String task_URL, AppCompatActivity current_activity, View mProgressView, View mLoginFormView, String APPLICATION_NAME, Pair[] name_value_pairs, View view_to_focus_on_error) {

        Log.d(APPLICATION_NAME, "REST Insert TASK URL : " + task_URL);

        if (isOnline(context)) {

            ProgressBar_Utils.showProgress(true, context, mProgressView, mLoginFormView);

            REST_Insert_Task rest_insert_task = new REST_Insert_Task(task_URL, current_activity, mProgressView, mLoginFormView, APPLICATION_NAME, name_value_pairs, view_to_focus_on_error);

            rest_insert_task.execute();
        } else {
            Toast_Utils.longToast(context, "Internet is unavailable");
        }
    }

    public static void execute(Context context, String task_URL, AppCompatActivity current_activity, View mProgressView, View mLoginFormView, String APPLICATION_NAME, Pair[] name_value_pairs, View view_to_focus_on_error, further_Actions further_actions) {

        Log.d(APPLICATION_NAME, "REST Insert TASK URL : " + task_URL);

        if (isOnline(context)) {

            ProgressBar_Utils.showProgress(true, context, mProgressView, mLoginFormView);

            REST_Insert_Task rest_insert_task = new REST_Insert_Task(task_URL, current_activity, mProgressView, mLoginFormView, APPLICATION_NAME, name_value_pairs, view_to_focus_on_error, further_actions);

            rest_insert_task.execute();
        } else {
            Toast_Utils.longToast(context, "Internet is unavailable");
        }
    }
}

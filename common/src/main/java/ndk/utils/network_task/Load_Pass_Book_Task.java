package ndk.utils.network_task;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

import ndk.utils.Network_Utils;
import ndk.utils.Pass_Book_Utils;
import ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry;
import ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry_v2;
import ndk.utils.widgets.pass_book.Pass_Book_TableView;
import ndk.utils.widgets.pass_book.Pass_Book_TableView_v2;

import static ndk.utils.Date_Utils.mysql_date_time_format;
import static ndk.utils.ProgressBar_Utils.showProgress;

/**
 * Created by Nabeel on 23-01-2018.
 */

public class Load_Pass_Book_Task extends AsyncTask<Void, Void, String[]> {
    private String URL, TAG;
    private AppCompatActivity current_activity;
    private View progressBar, form;
    private Pass_Book_TableView pass_book_tableView;
    private Pass_Book_TableView_v2 pass_book_tableView_v2;
    private Pair[] name_value_pair;
    private boolean v2_flag;

    public Load_Pass_Book_Task(String URL, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pass_Book_TableView pass_book_tableView, Pair[] name_value_pair) {

        this.URL = URL;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.pass_book_tableView = pass_book_tableView;
        this.name_value_pair = name_value_pair;
        this.v2_flag = false;
    }

    private String current_account_id;

    public Load_Pass_Book_Task(String URL, AppCompatActivity current_activity, View progressBar, View form, String TAG, Pass_Book_TableView_v2 pass_book_tableView_v2, String current_account_id) {

        this.URL = URL;
        this.current_activity = current_activity;
        this.progressBar = progressBar;
        this.form = form;
        this.TAG = TAG;
        this.pass_book_tableView_v2 = pass_book_tableView_v2;
        this.current_account_id = current_account_id;
        this.v2_flag = true;
    }

    @Override
    protected String[] doInBackground(Void... params) {

        if (v2_flag) {
            return Network_Utils.perform_http_client_network_task(URL, new Pair[]{});
        } else {
            return Network_Utils.perform_http_client_network_task(URL, name_value_pair);
        }
    }


    @Override
    protected void onPostExecute(final String[] network_action_response_array) {

        showProgress(false, current_activity, progressBar, form);

        Log.d(TAG, network_action_response_array[0]);
        Log.d(TAG, network_action_response_array[1]);

        ArrayList<Pass_Book_Entry> pass_book_entries = new ArrayList<>();
        ArrayList<Pass_Book_Entry_v2> pass_book_entries_v2 = new ArrayList<>();

        if (network_action_response_array[0].equals("1")) {
            Toast.makeText(current_activity, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
            Log.d(TAG, network_action_response_array[1]);
        } else {

            try {
                JSONArray json_array = new JSONArray(network_action_response_array[1]);
                if (json_array.getJSONObject(0).getString("status").equals("2")) {
                    Toast.makeText(current_activity, "No Entries...", Toast.LENGTH_LONG).show();
                } else if (json_array.getJSONObject(0).getString("status").equals("0")) {

                    if (v2_flag) {
                        double balance = 0;
                        for (int i = 1; i < json_array.length(); i++) {

                            if (json_array.getJSONObject(i).getString("from_account_id").equals(current_account_id)) {
                                balance = balance - Double.parseDouble(json_array.getJSONObject(i).getString("amount"));
                                pass_book_entries_v2.add(new Pass_Book_Entry_v2(mysql_date_time_format.parse(json_array.getJSONObject(i).getString("event_date_time")), json_array.getJSONObject(i).getString("particulars"), json_array.getJSONObject(i).getString("to_account_name"), 0, Double.parseDouble(json_array.getJSONObject(i).getString("amount")), balance));
                            } else {
                                balance = balance + Double.parseDouble(json_array.getJSONObject(i).getString("amount"));
                                pass_book_entries_v2.add(new Pass_Book_Entry_v2(mysql_date_time_format.parse(json_array.getJSONObject(i).getString("event_date_time")), json_array.getJSONObject(i).getString("particulars"), json_array.getJSONObject(i).getString("from_account_name"), Double.parseDouble(json_array.getJSONObject(i).getString("amount")), 0, balance));
                            }
                        }

                        Pass_Book_Utils.bindv2(pass_book_tableView_v2, current_activity, pass_book_entries_v2);

                    } else {
                        double balance = 0;
                        for (int i = 1; i < json_array.length(); i++) {

                            if (json_array.getJSONObject(i).getString("particulars").contains("Credit")) {
                                balance = balance + Double.parseDouble(json_array.getJSONObject(i).getString("amount"));
                                pass_book_entries.add(new Pass_Book_Entry(mysql_date_time_format.parse(json_array.getJSONObject(i).getString("event_date_time")), json_array.getJSONObject(i).getString("particulars"), 0, Double.parseDouble(json_array.getJSONObject(i).getString("amount")), balance));
                                Log.d(TAG, String.valueOf(balance));
                            }
                            if (json_array.getJSONObject(i).getString("particulars").contains("Debit")) {
                                balance = balance - Double.parseDouble(json_array.getJSONObject(i).getString("amount"));
                                pass_book_entries.add(new Pass_Book_Entry(mysql_date_time_format.parse(json_array.getJSONObject(i).getString("event_date_time")), json_array.getJSONObject(i).getString("particulars"), Double.parseDouble(json_array.getJSONObject(i).getString("amount")), 0, balance));
                                Log.d(TAG, String.valueOf(balance));
                            }
                        }

                        Pass_Book_Utils.bind(pass_book_tableView, current_activity, pass_book_entries);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(current_activity, "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, e.getLocalizedMessage());
            } catch (ParseException e) {

                Toast.makeText(current_activity, "Date Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, e.getLocalizedMessage());
            }
        }
    }

    @Override
    protected void onCancelled() {
        showProgress(false, current_activity, progressBar, form);
    }
}
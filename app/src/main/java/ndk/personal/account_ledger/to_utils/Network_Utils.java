package ndk.personal.account_ledger.to_utils;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nabeel on 17-02-2018.
 */

public class Network_Utils extends ndk.utils.Network_Utils {

    public static void handle_json_insertion_response_and_clear_fields(String[] network_action_response_array, AppCompatActivity current_activity, Class to_switch_activity, EditText[] texts_to_clear, View view_to_focus_on_error, String TAG) {

        Log.d(TAG, network_action_response_array[0]);
        Log.d(TAG, network_action_response_array[1]);

        if (network_action_response_array[0].equals("1")) {
            Toast.makeText(current_activity, "Error : " + network_action_response_array[1], Toast.LENGTH_LONG).show();
            Log.d(TAG, network_action_response_array[1]);
        } else {
            try {
                JSONObject json = new JSONObject(network_action_response_array[1]);
                String response_code = json.getString("status");
                byte var8 = -1;
                switch (response_code.hashCode()) {
                    case 48:
                        if (response_code.equals("0")) {
                            var8 = 0;
                        }
                        break;
                    case 49:
                        if (response_code.equals("1")) {
                            var8 = 1;
                        }
                }

                switch (var8) {
                    case 0:
                        Toast.makeText(current_activity, "OK", Toast.LENGTH_LONG).show();
//                        Activity_Utils.start_activity_with_finish(current_activity, to_switch_activity);
                        Text_Clear_Utils.reset_fields(texts_to_clear);
                        break;
                    case 1:
                        Toast.makeText(current_activity, "Error : " + json.getString("error"), Toast.LENGTH_LONG).show();
                        view_to_focus_on_error.requestFocus();
                        break;
                    default:
                        Toast.makeText(current_activity, "Error : Check json", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException var9) {
                Toast.makeText(current_activity, "Error : " + var9.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, var9.getLocalizedMessage());
            }
        }

    }
}

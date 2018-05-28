package ndk.utils.activities;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import ndk.utils.Activity_Utils;
import ndk.utils.Exception_Utils;
import ndk.utils.R;
import ndk.utils.Server_Utils;
import ndk.utils.Toast_Utils;
import ndk.utils.Update_Utils;
import ndk.utils.network_task.REST_Select_Task;
import ndk.utils.network_task.REST_Select_Task_Wrapper;
import ndk.utils.update.Update_Application;

//TODO : Full screen splash
//TODO : Implement hiding of fields - in case of layout
//TODO : Develop tests

public abstract class Splash_Base_URL extends AppCompatActivity {

    AppCompatActivity current_activity = this;

    protected abstract String configure_GET_CONFIGURATION_URL();

    protected abstract String configure_UPDATE_URL();

    protected abstract String configure_APPLICATION_NAME();

    protected abstract Class configure_NEXT_ACTIVITY_CLASS();

    protected abstract Pair[] configure_NEXT_ACTIVITY_CLASS_EXTRAS();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        REST_Select_Task_Wrapper.execute_splash(this, configure_GET_CONFIGURATION_URL(), configure_APPLICATION_NAME(), new Pair[]{}, new REST_Select_Task.Async_Response_JSON_array() {

            public void processFinish(JSONArray json_array) {
                try {
                    if (Server_Utils.check_system_status(getApplicationContext(), json_array.getJSONObject(0).getString("system_status"))) {
                        if (Integer.parseInt(json_array.getJSONObject(0).getString("version_code")) != Update_Utils.getVersionCode(getApplicationContext())) {
                            Update_Application.update_application(configure_APPLICATION_NAME(), current_activity, Float.parseFloat(json_array.getJSONObject(0).getString("version_name")), configure_UPDATE_URL());
                        } else if (Float.parseFloat(json_array.getJSONObject(0).getString("version_name")) != Update_Utils.getVersionName(getApplicationContext())) {
                            Update_Application.update_application(configure_APPLICATION_NAME(), current_activity, Float.parseFloat(json_array.getJSONObject(0).getString("version_name")), configure_UPDATE_URL());
                        } else {
                            Toast_Utils.longToast(getApplicationContext(), "Latest Version...");
                            Activity_Utils.start_activity_with_string_extras_and_finish(current_activity, configure_NEXT_ACTIVITY_CLASS(), configure_NEXT_ACTIVITY_CLASS_EXTRAS());
                        }
                    }
                } catch (JSONException exception) {
                    Toast_Utils.longToast(getApplicationContext(), "JSON Response Error...");
                    Log.d(configure_APPLICATION_NAME(), Exception_Utils.get_exception_details(exception));
                }
            }
        });


    }
}

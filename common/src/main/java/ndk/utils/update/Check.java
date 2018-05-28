package ndk.utils.update;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ndk.utils.Network_Utils;
import ndk.utils.network_task.update.Update_Check_Task;

import static ndk.utils.Network_Utils.display_Long_no_FAB_no_network_bottom_SnackBar;

/**
 * Created by Nabeel on 21-01-2018.
 */

public class Check {

    public static void attempt_Update_Check(final String application_name, final AppCompatActivity current_activity, final String URL, Update_Check_Task Update_Task, final String update_URL, final Class next_activity) {

        if (Update_Task != null) {
            return;
        }

        if (Network_Utils.isOnline(current_activity)) {
            Update_Task = new Update_Check_Task(application_name, current_activity, URL, Update_Task, update_URL, next_activity);
            Update_Task.execute((Void) null);
        } else {
            final Update_Check_Task final_Update_Task = Update_Task;
            View.OnClickListener retry_Failed_Network_Task = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attempt_Update_Check(application_name, current_activity, URL, final_Update_Task, update_URL, next_activity);
                }
            };
            display_Long_no_FAB_no_network_bottom_SnackBar(current_activity.getWindow().getDecorView(), retry_Failed_Network_Task);
        }

    }
}

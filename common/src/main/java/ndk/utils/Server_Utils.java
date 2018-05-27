package ndk.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created on 02-12-2017 12:46 under Caventa_Android.
 */
public class Server_Utils {
    public static boolean check_system_status(Context context, String system_status) {
        if (Integer.parseInt(system_status) == 0) {
            Toast.makeText(context, "System is in Maintenance, Try Again later...", Toast.LENGTH_LONG).show();
            ((AppCompatActivity) context).finish();
        } else if (Integer.parseInt(system_status) == 1) {
            Toast.makeText(context, "System Status is OK", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}

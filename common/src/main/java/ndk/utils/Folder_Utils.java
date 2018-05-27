package ndk.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Nabeel on 23-01-2018.
 */

public class Folder_Utils {
    public static boolean create_Documents_application_sub_folder(String TAG, Context context, String application_name) {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        boolean is_documents_Present = true;
        if (!docsFolder.exists()) {
            is_documents_Present = docsFolder.mkdir();
        }
        if (is_documents_Present) {
            File sub_folder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), application_name);
            boolean is_sub_folder_Present = true;
            if (!sub_folder.exists()) {
                is_sub_folder_Present = sub_folder.mkdir();
            }
            if (is_sub_folder_Present) {
                Log.i(TAG, "Pdf Directory created");
                return true;
            } else {
                Log.i(TAG, "Folder Creation failure ");
                Toast_Utils.longToast(context, "Folder fail");
                return false;
            }

        } else {
            Log.i(TAG, "Folder Creation failure ");
            Toast_Utils.longToast(context, "Folder fail");
            return false;
        }
    }
}

package ndk.utils.network_task.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import ndk.utils.Toast_Utils;

/**
 * Created on 02-12-2017 12:49 under Caventa_Android.
 */

public class Application_VCS_Utils {

    public static void download_and_install_apk(String application_name, float version_name, String update_URL, final Context context, String TAG) {

        //get destination to update file and set Uri.
        //Download directory in external storage.
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = application_name + "_" + version_name + ".apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists()) {
            if (!file.delete()) {
                Toast_Utils.longToast(context, "Deletion failure, please clear your downloads...");
            }
        }

        //get url of app on server
        String url = update_URL;

        //set download manager
        Log.d(TAG, url);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Downloading Update...");
        request.setTitle(application_name + " " + version_name);

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(uri,
                        manager.getMimeTypeForDownloadedFile(downloadId));
                context.startActivity(install);

                context.unregisterReceiver(this);
                ((AppCompatActivity) context).finish();
            }
        };

        //register receiver for when .apk download is compete
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}

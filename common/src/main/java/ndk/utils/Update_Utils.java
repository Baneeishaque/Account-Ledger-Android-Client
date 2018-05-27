package ndk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Update_Utils {

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            return 0;
        }

    }

    public static float getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return Float.parseFloat(pi.versionName);
        } catch (PackageManager.NameNotFoundException ex) {
            return 0;
        }

    }


    @SuppressWarnings("deprecation")
    public static String[] get_flavoured_server_version(String flavour, String full_version_check_URL) {
        String network_action_response;
        try {
            // Network access.
            DefaultHttpClient http_client = new DefaultHttpClient();
//            HttpPost http_post = new HttpPost("http://" + General_Data.SERVER_IP_ADDRESS + "/android/get_version.php");
            HttpPost http_post = new HttpPost(full_version_check_URL);
            ArrayList<NameValuePair> name_pair_value = new ArrayList<>(1);
            name_pair_value.add(new BasicNameValuePair("flavour", flavour));
            http_post.setEntity(new UrlEncodedFormEntity(name_pair_value));

            ResponseHandler<String> response_handler = new BasicResponseHandler();
            network_action_response = http_client.execute(http_post, response_handler);
            Log.d("Server Version", network_action_response);
            return new String[]{"0", network_action_response};

        } catch (UnsupportedEncodingException e) {
            return new String[]{"1", "UnsupportedEncodingException : " + e.getLocalizedMessage()};
        } catch (ClientProtocolException e) {
            return new String[]{"1", "ClientProtocolException : " + e.getLocalizedMessage()};
        } catch (IOException e) {
            return new String[]{"1", "IOException : " + e.getLocalizedMessage()};
        }
    }

    @SuppressWarnings("deprecation")
    public static String[] get_server_version(String full_version_check_URL) {
        String network_action_response;
        try {
            // Network access.
            DefaultHttpClient http_client = new DefaultHttpClient();
//            HttpPost http_post = new HttpPost("http://" + General_Data.SERVER_IP_ADDRESS + "/android/get_version.php");
            HttpPost http_post = new HttpPost(full_version_check_URL);
//            ArrayList<NameValuePair> name_pair_value = new ArrayList<>(1);
//            name_pair_value.add(new BasicNameValuePair("flavour", flavour));
//            http_post.setEntity(new UrlEncodedFormEntity(name_pair_value));

            ResponseHandler<String> response_handler = new BasicResponseHandler();
            network_action_response = http_client.execute(http_post, response_handler);
            Log.d("Server Version", network_action_response);
            return new String[]{"0", network_action_response};

        } catch (UnsupportedEncodingException e) {
            return new String[]{"1", "UnsupportedEncodingException : " + e.getLocalizedMessage()};
        } catch (ClientProtocolException e) {
            return new String[]{"1", "ClientProtocolException : " + e.getLocalizedMessage()};
        } catch (IOException e) {
            return new String[]{"1", "IOException : " + e.getLocalizedMessage()};
        }
    }

}

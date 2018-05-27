package ndk.utils;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class Fragment_Utils {

    public static void display_fragment(AppCompatActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
    }
}

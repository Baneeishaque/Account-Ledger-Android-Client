package ndk.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 30-11-2017 22:25 under Caventa_Android.
 */
public class Visibility_Utils {
    public static void set_visible(View[] views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }

    }

    public static void remove_from_parent_layout(View[] views) {
        for (View view : views) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

    }


}

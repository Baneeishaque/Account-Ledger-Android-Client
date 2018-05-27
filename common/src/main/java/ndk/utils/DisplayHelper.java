package ndk.utils;

import android.content.Context;

/**
 * Created by prism on 30-08-2017.
 */

public class DisplayHelper {
    private static Float scale;

    public static int dpToPixel(int dp, Context context) {
        if (scale == null)
            scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) dp * scale);
    }
}


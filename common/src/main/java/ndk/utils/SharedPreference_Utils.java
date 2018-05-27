package ndk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;

public class SharedPreference_Utils {

    public static void commit_Shared_Preferences(Context context, String application_name, Pair[] shared_preference_pairs) {

        if (shared_preference_pairs.length != 0) {
            SharedPreferences settings = context.getSharedPreferences(application_name, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            for (Pair shared_preference_pair : shared_preference_pairs) {
                editor.putString(shared_preference_pair.first != null ? shared_preference_pair.first.toString() : null, shared_preference_pair.second != null ? shared_preference_pair.second.toString() : null);
            }
            editor.apply();
        }
    }
}

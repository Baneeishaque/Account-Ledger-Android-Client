package ndk.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.kimkevin.cachepot.CachePot;

import java.util.Objects;

public class Activity_Utils {

    public static Intent construct_Intent_With_String_Extras(Context context, Class activity, Pair[] extras) {
        Intent intent = new Intent(context, activity);
        if (extras.length != 0) {
            for (Pair extra : extras) {
                intent.putExtra(extra.first != null ? extra.first.toString() : null, extra.second != null ? extra.second.toString() : null);
            }
        }
        return intent;
    }

    public static void start_activity(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void start_activity_with_integer_extras(Context context, Class activity, Pair[] extras) {
        Intent intent = new Intent(context, activity);
        if (extras.length != 0) {
            for (Pair extra : extras) {
                intent.putExtra(extra.first != null ? extra.first.toString() : null, Integer.parseInt(extra.second != null ? extra.second.toString() : null));
            }
        }
        context.startActivity(intent);
    }

    public static void start_activity_with_string_extras(Context context, Class activity, Pair[] extras, boolean for_result_flag, int request_code) {
        Intent intent = new Intent(context, activity);
        if (extras.length != 0) {
            for (Pair extra : extras) {
                intent.putExtra(extra.first != null ? extra.first.toString() : null, extra.second != null ? extra.second.toString() : null);
            }
        }
        if (for_result_flag) {
            ((AppCompatActivity) context).startActivityForResult(intent, request_code);
        } else {
            context.startActivity(intent);
        }
    }

    public static void start_activity_with_integer_extras_and_finish(Context context, Class activity, Pair[] extras) {
        Intent intent = new Intent(context, activity);
        if (extras.length != 0) {
            for (Pair extra : extras) {
                intent.putExtra(extra.first != null ? extra.first.toString() : null, Integer.parseInt(extra.second != null ? extra.second.toString() : null));
            }
        }
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void start_activity_with_finish(Context context, Class activity, String APPLICATION_NAME) {

        Log.d(APPLICATION_NAME, "Next Activity : " + activity.getCanonicalName());
        Log.d(APPLICATION_NAME, "Next Activity : " + activity.getName());
        Log.d(APPLICATION_NAME, "Next Activity : " + activity.getSimpleName());

        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void start_activity_with_finish_and_tab_index(Context context, Class activity, int tab_index) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("tab_index", tab_index);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void start_activity_with_object_push_and_finish(Context context, Class activity, Object object_to_push) {
        Intent intent = new Intent(context, activity);
        CachePot.getInstance().push(object_to_push);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void start_activity_with_object_push(Context context, Class activity, Object object_to_push) {
        Intent intent = new Intent(context, activity);
        CachePot.getInstance().push(object_to_push);
        context.startActivity(intent);
    }

    public static void start_activity_with_object_push_and_integer_extras(Context context, Class activity, Pair[] extras, Object object_to_push) {
        Intent intent = new Intent(context, activity);
        if (extras.length != 0) {
            for (Pair extra : extras) {
                intent.putExtra(extra.first != null ? extra.first.toString() : null, Integer.parseInt(extra.second != null ? extra.second.toString() : null));
            }
        }
        CachePot.getInstance().push(object_to_push);
        context.startActivity(intent);
    }

    public static void start_activity_with_object_push_and_integer_extras_and_finish(Context context, Class activity, Pair[] extras, Object object_to_push) {
        Intent intent = new Intent(context, activity);
        if (extras.length != 0) {
            for (Pair extra : extras) {
                intent.putExtra(extra.first != null ? extra.first.toString() : null, Integer.parseInt(extra.second != null ? extra.second.toString() : null));
            }
        }
        CachePot.getInstance().push(object_to_push);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void start_activity_with_object_push_and_origin(Context context, Class activity, Object object_to_push, String origin) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("origin", origin);
        CachePot.getInstance().push(object_to_push);
        context.startActivity(intent);
    }

    public static void start_activity_with_object_push_and_finish_and_origin(Context context, Class activity, Object object_to_push, String origin) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("origin", origin);
        CachePot.getInstance().push(object_to_push);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

    public static void start_activity_with_string_extras_and_finish(Context context, Class activity, Pair[] extras) {
        Intent intent = new Intent(context, activity);
        if (extras.length != 0) {

            for (Pair extra : extras) {
                intent.putExtra(Objects.requireNonNull(extra.first).toString(), Objects.requireNonNull(extra.second).toString());
            }
        }
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }

}

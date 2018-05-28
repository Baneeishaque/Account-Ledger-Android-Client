package ndk.utils;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;

import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;

public class Snackbar_Utils {

    public static void display_Short_no_FAB_success_bottom_SnackBar(Context context, String message) {
        com.chootdev.csnackbar.Snackbar.with(context, null)
                .type(Type.SUCCESS)
                .message(message)
                .duration(Duration.SHORT)
                .show();
    }

    public static void display_Short_no_FAB_error_bottom_SnackBar(Context context, String message) {
        com.chootdev.csnackbar.Snackbar.with(context, null)
                .type(Type.ERROR)
                .message(message)
                .duration(Duration.SHORT)
                .show();
    }

    public static void display_Short_no_FAB_update_bottom_SnackBar(Context context, String message) {
        com.chootdev.csnackbar.Snackbar.with(context, null)
                .type(Type.UPDATE)
                .message(message)
                .duration(Duration.SHORT)
                .show();
    }

    public static void display_Short_no_FAB_warning_bottom_SnackBar(Context context, String message) {
        com.chootdev.csnackbar.Snackbar.with(context, null)
                .type(Type.WARNING)
                .message(message)
                .duration(Duration.SHORT)
                .show();
    }

    public static void display_Short_no_FAB_custom_bottom_SnackBar(Context context, String message, int color) {
        com.chootdev.csnackbar.Snackbar.with(context, null)
                .type(Type.CUSTOM, color)
                .message(message)
                .duration(Duration.SHORT)
                .show();
    }

    public static void display_Long_no_FAB_success_bottom_SnackBar(Context context, String message) {
        com.chootdev.csnackbar.Snackbar.with(context, null)
                .type(Type.SUCCESS)
                .message(message)
                .duration(Duration.LONG)
                .show();
    }

    public static void display_Long_no_FAB_error_bottom_SnackBar(Context context, String message) {
        com.chootdev.csnackbar.Snackbar.with(context, null)
                .type(Type.ERROR)
                .message(message)
                .duration(Duration.LONG)
                .show();
    }

    public static void display_Long_no_FAB_update_bottom_SnackBar(Context context, String message) {
        com.chootdev.csnackbar.Snackbar.with(context, null)
                .type(Type.UPDATE)
                .message(message)
                .duration(Duration.LONG)
                .show();
    }

    public static void display_Long_no_FAB_warning_bottom_SnackBar(Context context, String message) {
        com.chootdev.csnackbar.Snackbar.with(context, null)
                .type(Type.WARNING)
                .message(message)
                .duration(Duration.LONG)
                .show();
    }

    public static void display_Long_no_FAB_custom_bottom_SnackBar(Context context, String message, int color) {
        com.chootdev.csnackbar.Snackbar.with(context, null)
                .type(Type.CUSTOM, color)
                .message(message)
                .duration(Duration.LONG)
                .show();
    }


    public static void display_Short_FAB_success_bottom_SnackBar(Context context, String message, FloatingActionButton FAB) {
        com.chootdev.csnackbar.Snackbar.with(context, FAB)
                .type(Type.SUCCESS)
                .message(message)
                .duration(Duration.SHORT)
                .show();
    }

    public static void display_Short_FAB_error_bottom_SnackBar(Context context, String message, FloatingActionButton FAB) {
        com.chootdev.csnackbar.Snackbar.with(context, FAB)
                .type(Type.ERROR)
                .message(message)
                .duration(Duration.SHORT)
                .show();
    }

    public static void display_Short_FAB_update_bottom_SnackBar(Context context, String message, FloatingActionButton FAB) {
        com.chootdev.csnackbar.Snackbar.with(context, FAB)
                .type(Type.UPDATE)
                .message(message)
                .duration(Duration.SHORT)
                .show();
    }

    public static void display_Short_FAB_warning_bottom_SnackBar(Context context, String message, FloatingActionButton FAB) {
        com.chootdev.csnackbar.Snackbar.with(context, FAB)
                .type(Type.WARNING)
                .message(message)
                .duration(Duration.SHORT)
                .show();
    }

    public static void display_Short_FAB_custom_bottom_SnackBar(Context context, String message, int color, FloatingActionButton FAB) {
        com.chootdev.csnackbar.Snackbar.with(context, FAB)
                .type(Type.CUSTOM, color)
                .message(message)
                .duration(Duration.SHORT)
                .show();
    }

    public static void display_Long_FAB_success_bottom_SnackBar(Context context, String message, FloatingActionButton FAB) {
        com.chootdev.csnackbar.Snackbar.with(context, FAB)
                .type(Type.SUCCESS)
                .message(message)
                .duration(Duration.LONG)
                .show();
    }

    public static void display_Long_FAB_error_bottom_SnackBar(Context context, String message, FloatingActionButton FAB) {
        com.chootdev.csnackbar.Snackbar.with(context, FAB)
                .type(Type.ERROR)
                .message(message)
                .duration(Duration.LONG)
                .show();
    }

    public static void display_Long_FAB_update_bottom_SnackBar(Context context, String message, FloatingActionButton FAB) {
        com.chootdev.csnackbar.Snackbar.with(context, FAB)
                .type(Type.UPDATE)
                .message(message)
                .duration(Duration.LONG)
                .show();
    }

    public static void display_Long_FAB_warning_bottom_SnackBar(Context context, String message, FloatingActionButton FAB) {
        com.chootdev.csnackbar.Snackbar.with(context, FAB)
                .type(Type.WARNING)
                .message(message)
                .duration(Duration.LONG)
                .show();
    }

    public static void display_Long_FAB_custom_bottom_SnackBar(Context context, String message, int color, FloatingActionButton FAB) {
        com.chootdev.csnackbar.Snackbar.with(context, FAB)
                .type(Type.CUSTOM, color)
                .message(message)
                .duration(Duration.LONG)
                .show();
    }
}

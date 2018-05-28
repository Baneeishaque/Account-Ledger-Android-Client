package ndk.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;

/**
 * Created by Nabeel on 23-01-2018.
 */

public class ProgressBar_Utils {
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Context context, final View Progress_Bar, final View Form) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

        Form.setVisibility(show ? View.GONE : View.VISIBLE);
        Form.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Form.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        Progress_Bar.setVisibility(show ? View.VISIBLE : View.GONE);
        Progress_Bar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Progress_Bar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}

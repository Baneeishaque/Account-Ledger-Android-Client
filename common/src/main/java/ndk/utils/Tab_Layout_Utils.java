package ndk.utils;

import android.support.design.widget.TabLayout;

/**
 * Created on 02-12-2017 17:00 under Caventa_Android.
 */
public class Tab_Layout_Utils {
    public static void select_Tab(TabLayout tabLayout, int tab_index) {
        (tabLayout.getTabAt(tab_index)).select();
    }
}

package ndk.personal.account_ledger.to_utils;

import android.widget.EditText;

/**
 * Created by Nabeel on 17-02-2018.
 */

public class Text_Clear_Utils {
    public static void reset_fields(EditText[] edit_texts) {
        EditText[] var1 = edit_texts;
        int var2 = edit_texts.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            EditText edit_text = var1[var3];
            edit_text.setText("");
        }

    }
}

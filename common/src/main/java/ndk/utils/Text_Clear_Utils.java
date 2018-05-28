package ndk.utils;

import android.widget.EditText;

public class Text_Clear_Utils {

    public static void reset_fields(EditText[] edit_texts) {
        if (edit_texts.length != 0) {
            for (EditText edit_text : edit_texts) {
                edit_text.setText("");
            }
        }
    }

    public static void reset_fields_and_focus(EditText[] edit_texts, EditText view_to_focus) {
        reset_fields(edit_texts);
        view_to_focus.requestFocus();
    }
}

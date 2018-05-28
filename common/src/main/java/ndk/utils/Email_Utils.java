package ndk.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class Email_Utils {
    public static void email_attachment(String email_subject, String email_text, File email_attachment, Context context) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT, email_subject);
        email.putExtra(Intent.EXTRA_TEXT, email_text);
        Uri uri = Uri.parse(email_attachment.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        context.startActivity(email);
    }
}

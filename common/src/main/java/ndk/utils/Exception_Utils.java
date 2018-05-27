package ndk.utils;

import java.util.Arrays;

public class Exception_Utils {

    public static String get_exception_details(Exception e) {

        return "Exception Message : " + e.getLocalizedMessage()
                + "\n" + "Exception Code : " + e.hashCode()
                + "\n" + "Exception Class : " + e.getClass()
                + "\n" + "Exception Cause : " + e.getCause()
                + "\n" + "Exception StackTrace : " + Arrays.toString(e.getStackTrace())
                + "\n" + "Exception Suppressed : " + Arrays.toString(e.getSuppressed())
                + "\n" + "Exception : " + e.toString();

    }

}

package ndk.personal.account_ledger.utils;

import android.content.Context;

import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android19.ExceptionUtils19;

public class AccountLedgerExceptionUtils {

    public static void handleExceptionOnGui(Context applicationContext, String exceptionDetails) {

        ExceptionUtils19.handleExceptionOnGui(applicationContext, ApplicationSpecification.APPLICATION_NAME, exceptionDetails);
    }

    public static void handleExceptionOnGui(Context applicationContext, Exception exception) {

        ExceptionUtils19.handleExceptionOnGui(applicationContext, ApplicationSpecification.APPLICATION_NAME, exception);
    }
}

package ndk.personal.account_ledger.utils;

import android.content.Context;

import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android1.ExceptionUtils1;

public class AccountLedgerExceptionUtils {
    public static void handleExceptionOnGui(Context applicationContext, String exceptionDetails) {

        ExceptionUtils1.handleExceptionOnGui( applicationContext, ApplicationSpecification.APPLICATION_NAME, exceptionDetails);
    }
}


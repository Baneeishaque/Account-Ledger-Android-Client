package ndk.personal.account_ledger.utils;

import android.content.Context;

import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android1.ErrorUtils;

public class AccountLedgerErrorUtils {

    public static void displayException(Context currentActivityContext, Exception exception) {

        ErrorUtils.displayException(currentActivityContext, exception, ApplicationSpecification.APPLICATION_NAME);
    }
}

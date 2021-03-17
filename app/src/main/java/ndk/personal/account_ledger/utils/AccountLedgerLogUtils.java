package ndk.personal.account_ledger.utils;

import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android1.LogUtils;

public class AccountLedgerLogUtils {

    public static void debug(String message) {

        LogUtils.debug(ApplicationSpecification.APPLICATION_NAME, message);
    }
}

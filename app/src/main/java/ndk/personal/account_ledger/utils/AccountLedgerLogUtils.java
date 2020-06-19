package ndk.personal.account_ledger.utils;

import ndk.personal.account_ledger.BuildConfig;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android14.LogUtilsWrapperBase;

public class AccountLedgerLogUtils extends LogUtilsWrapperBase {

    public AccountLedgerLogUtils() {

        super(ApplicationSpecification.APPLICATION_NAME, BuildConfig.DEBUG);
    }
}

package ndk.personal.account_ledger.activities;

import static ndk.personal.account_ledger.constants.ServerEndpoint.UPDATED_APK_URL;

import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.personal.account_ledger.constants.SharedPreferenceKeys;
import ndk.utils_android1.DebugUtils;
import ndk.utils_android19.activities.LoginBundleActivity;
import ndk.utils_android16.constants.IntentExtendedDataItemNames;
import ndk.utils_android19.activities.SplashWithAutomatedUpdateActivity;
import ndk.utils_android19.models.PairOfStringsModel;

public class SplashV2 extends SplashWithAutomatedUpdateActivity {

    @Override
    public String configureGetConfigurationUrl() {

        return ApiWrapper.selectConfiguration();
    }

    @Override
    public String configureUpdateUrl() {
        return UPDATED_APK_URL;
    }

    @Override
    public String configureApplicationName() {

        return ApplicationSpecification.APPLICATION_NAME;
    }

    @Override
    public Class<LoginBundleActivity> configureNextActivityClass() {
        return LoginBundleActivity.class;
    }

    @Override
    public PairOfStringsModel[] configureNextActivityClassExtras() {

        return new PairOfStringsModel[]{
                new PairOfStringsModel(IntentExtendedDataItemNames.INTENT_EXTENDED_DATA_ITEM_NAME_APPLICATION_NAME, ApplicationSpecification.APPLICATION_NAME),
                new PairOfStringsModel(IntentExtendedDataItemNames.INTENT_EXTENDED_DATA_ITEM_NAME_NEXT_ACTIVITY_CLASS, ListAccounts.class.getName()),
                new PairOfStringsModel(IntentExtendedDataItemNames.INTENT_EXTENDED_DATA_ITEM_NAME_SELECT_USER_URL, ApiWrapper.selectUser()),
                new PairOfStringsModel(IntentExtendedDataItemNames.INTENT_EXTENDED_DATA_ITEM_NAME_TEST_USERNAME, "test"),
                new PairOfStringsModel(IntentExtendedDataItemNames.INTENT_EXTENDED_DATA_ITEM_NAME_TEST_PASSWORD, "test"),
                new PairOfStringsModel(IntentExtendedDataItemNames.INTENT_EXTENDED_DATA_ITEM_NAME_SHARED_PREFERENCES_KEY_USER_ID, SharedPreferenceKeys.SHARED_PREFERENCES_KEY_USER_ID)
        };
    }

    @Override
    public boolean configureSecurityFlag() {

        return !DebugUtils.isDebugBuild(getApplicationContext());
    }
}

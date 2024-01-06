package ndk.personal.account_ledger.activities;

import static ndk.personal.account_ledger.constants.ServerEndpoint.UPDATED_APK_URL;

import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android1.DebugUtils;
import ndk.utils_android16.activities.LoginBundleActivity;
import ndk.utils_android16.constants.IntentExtraFields;
import ndk.utils_android19.activities.SplashWithAutomatedUpdateActivity;
import ndk.utils_android19.models.PairOfStringsModel;

public class SplashV2 extends SplashWithAutomatedUpdateActivity {

    @Override
    public String configure_GET_CONFIGURATION_URL() {

        return ApiWrapper.selectConfiguration();
    }

    @Override
    public String configure_UPDATE_URL() {
        return UPDATED_APK_URL;
    }

    @Override
    public String configure_APPLICATION_NAME() {

        return ApplicationSpecification.APPLICATION_NAME;
    }

    @Override
    public Class<LoginBundleActivity> configureNextActivityClass() {
        return LoginBundleActivity.class;
    }

    @Override
    public PairOfStringsModel[] configureNextActivityClassExtras() {

        return new PairOfStringsModel[]{new PairOfStringsModel(IntentExtraFields.APPLICATION_NAME, ApplicationSpecification.APPLICATION_NAME), new PairOfStringsModel(IntentExtraFields.NEXT_ACTIVITY_CLASS, ListAccounts.class.getName()), new PairOfStringsModel(IntentExtraFields.SELECT_USER_URL, ApiWrapper.selectUser()), new PairOfStringsModel(IntentExtraFields.TEST_USERNAME, "test"), new PairOfStringsModel(IntentExtraFields.TEST_PASSWORD, "test")};
    }

    @Override
    public boolean configure_SECURITY_FLAG() {

        return !DebugUtils.isDebugBuild(getApplicationContext());
    }
}

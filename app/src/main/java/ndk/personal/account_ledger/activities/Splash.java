package ndk.personal.account_ledger.activities;

import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android1.DebugUtils;
import ndk.utils_android19.activities.LoginBundleActivity;
import ndk.utils_android19.activities.SplashWithAutomatedUpdateActivity;
import ndk.utils_android16.constants.IntentExtraFields;
import ndk.utils_android19.models.PairOfStringsModel;

import static ndk.personal.account_ledger.constants.ServerEndpoint.UPDATED_APK_URL;

public class Splash extends SplashWithAutomatedUpdateActivity {

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

        return new PairOfStringsModel[]{new PairOfStringsModel(IntentExtraFields.APPLICATION_NAME, ApplicationSpecification.APPLICATION_NAME), new PairOfStringsModel(IntentExtraFields.NEXT_ACTIVITY_CLASS, Insert_Transaction.class.getName()), new PairOfStringsModel(IntentExtraFields.SELECT_USER_URL, ApiWrapper.selectUser())};
    }

    @Override
    public boolean configureSecurityFlag() {

        return DebugUtils.isDebugBuild(getApplicationContext());
    }
}

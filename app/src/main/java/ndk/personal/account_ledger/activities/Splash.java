package ndk.personal.account_ledger.activities;

import androidx.core.util.Pair;

import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android1.DebugUtils;
import ndk.utils_android16.activities.LoginBundleActivity;
import ndk.utils_android16.activities.SplashWithAutomatedUpdateActivity;
import ndk.utils_android16.constants.IntentExtraFields;

import static ndk.personal.account_ledger.constants.ServerEndpoint.UPDATED_APK_URL;

public class Splash extends SplashWithAutomatedUpdateActivity {

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
    public Class configure_NEXT_ACTIVITY_CLASS() {
        return LoginBundleActivity.class;
    }

    @Override
    public Pair[] configure_NEXT_ACTIVITY_CLASS_EXTRAS() {

        return new Pair[]{new Pair<>(IntentExtraFields.APPLICATION_NAME, ApplicationSpecification.APPLICATION_NAME), new Pair<>(IntentExtraFields.NEXT_ACTIVITY_CLASS, Insert_Transaction.class.getName()), new Pair<>(IntentExtraFields.SELECT_USER_URL, ApiWrapper.selectUser())};
    }

    @Override
    public boolean configure_SECURITY_FLAG() {

        return DebugUtils.isDebugBuild(getApplicationContext());
    }
}

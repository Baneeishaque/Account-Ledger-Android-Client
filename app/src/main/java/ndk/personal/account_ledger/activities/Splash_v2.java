package ndk.personal.account_ledger.activities;

import androidx.core.util.Pair;

import ndk.personal.account_ledger.BuildConfig;
import ndk.personal.account_ledger.constants.Api;
import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android16.activities.LoginBundleActivity;
import ndk.utils_android16.activities.SplashWithAutomatedUpdateActivity;

import static ndk.personal.account_ledger.constants.ServerEndpoint.UPDATE_URL;

public class Splash_v2 extends SplashWithAutomatedUpdateActivity {

    @Override
    protected String configure_GET_CONFIGURATION_URL() {

        return ApiWrapper.getHttpApi(Api.select_Configuration);
    }

    @Override
    protected String configure_UPDATE_URL() {
        return UPDATE_URL;
    }

    @Override
    protected String configure_APPLICATION_NAME() {

        return ApplicationSpecification.APPLICATION_NAME;
    }

    @Override
    protected Class configure_NEXT_ACTIVITY_CLASS() {
        return LoginBundleActivity.class;
    }

    @Override
    protected Pair[] configure_NEXT_ACTIVITY_CLASS_EXTRAS() {

        return new Pair[]{new Pair<>("APPLICATION_NAME", ApplicationSpecification.APPLICATION_NAME), new Pair<>("NEXT_ACTIVITY_CLASS", List_Accounts.class.getName()), new Pair<>("SELECT_USER_URL", ApiWrapper.getHttpApi(Api.select_User))};
    }

    @Override
    protected boolean configure_SECURITY_FLAG() {

        return BuildConfig.DEBUG;
    }
}

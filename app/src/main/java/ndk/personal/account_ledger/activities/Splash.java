package ndk.personal.account_ledger.activities;

import android.support.v4.util.Pair;

import ndk.personal.account_ledger.BuildConfig;
import ndk.personal.account_ledger.constants.API;
import ndk.personal.account_ledger.constants.API_Wrapper;
import ndk.personal.account_ledger.constants.Application_Specification;
import ndk.utils.activities.Login_Bundle;
import ndk.utils.activities.Splash_Base_URL;

import static ndk.personal.account_ledger.constants.Server_Endpoint.UPDATE_URL;

public class Splash extends Splash_Base_URL {

    @Override
    protected String configure_GET_CONFIGURATION_URL() {
        return API_Wrapper.get_http_API(API.select_Configuration);
    }

    @Override
    protected String configure_UPDATE_URL() {
        return UPDATE_URL;
    }

    @Override
    protected String configure_APPLICATION_NAME() {
        return Application_Specification.APPLICATION_NAME;
    }

    @Override
    protected Class configure_NEXT_ACTIVITY_CLASS() {
        return Login_Bundle.class;
    }

    @Override
    protected Pair[] configure_NEXT_ACTIVITY_CLASS_EXTRAS() {
//        return new Pair[]{new Pair<>("APPLICATION_NAME", Application_Specification.APPLICATION_NAME), new Pair<>("NEXT_ACTIVITY_CLASS", List_Accounts.class.getName()), new Pair<>("SELECT_USER_URL", API_Wrapper.get_http_API(API.select_User))};

        return new Pair[]{new Pair<>("APPLICATION_NAME", Application_Specification.APPLICATION_NAME), new Pair<>("NEXT_ACTIVITY_CLASS", Insert_Transaction.class.getName()), new Pair<>("SELECT_USER_URL", API_Wrapper.get_http_API(API.select_User))};
    }

    @Override
    protected boolean configure_is_debug() {
        return BuildConfig.DEBUG;
    }
}

package ndk.personal.account_ledger.activities;

import androidx.core.util.Pair;

import com.google.firebase.firestore.FirebaseFirestore;

import ndk.personal.account_ledger.BuildConfig;
import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android16.activities.LoginBundleActivity;
import ndk.utils_android16.activities.SplashWithAutomatedUpdateFireStoreActivity;

import static ndk.personal.account_ledger.constants.ServerEndpoint.UPDATED_APK_URL;

public class SplashV2FireStore extends SplashWithAutomatedUpdateFireStoreActivity {

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

        return new Pair[]{new Pair<>("APPLICATION_NAME", ApplicationSpecification.APPLICATION_NAME), new Pair<>("NEXT_ACTIVITY_CLASS", ListAccounts.class.getName()), new Pair<>("SELECT_USER_URL", ApiWrapper.selectUser())};
    }

    @Override
    public boolean configure_SECURITY_FLAG() {

        return !BuildConfig.DEBUG;
    }

    @Override
    public FirebaseFirestore configureFireStoreDb() {

        return FirebaseFirestore.getInstance();
    }
}

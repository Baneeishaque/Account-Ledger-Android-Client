package ndk.personal.account_ledger.constants;

public class ServerEndpoint {

    public static final String SERVER_ADDRESS = "http://account-ledger-server.herokuapp.com";
    public static final String HTTP_API_FOLDER = "http_API";
    public static final String FILE_EXTENSION = ".php";

    public static final String BUILD_FOLDER = "builds";
    public static final String MODULE_NAME = "app";
    public static final String APK_PREFIX = "-production-release.apk";

    public static final String BUILD_SERVER_ADDRESS = SERVER_ADDRESS;
    public static final String UPDATED_APK_URL = BUILD_SERVER_ADDRESS.equals(SERVER_ADDRESS) ? BUILD_SERVER_ADDRESS + "/" + MODULE_NAME + APK_PREFIX : SERVER_ADDRESS + "/" + BUILD_FOLDER + "/" + MODULE_NAME + APK_PREFIX;
}


package ndk.personal.account_ledger.constants;

public class ServerEndpoint {

    public static String SERVER_IP_ADDRESS = "http://account-ledger-server.herokuapp.com";
    public static String HTTP_API_FOLDER = "http_API";
    public static String FILE_EXTENSION = ".php";

    public static String BUILD_FOLDER = "builds";
    public static String MODULE_NAME = "app";
    public static String APK_PREFIX = "-debug.apk";
    public static String UPDATED_APK_URL = SERVER_IP_ADDRESS + "/" + BUILD_FOLDER + "/" + MODULE_NAME + APK_PREFIX;
}


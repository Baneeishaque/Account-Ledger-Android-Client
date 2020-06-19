package ndk.personal.account_ledger.constants;


import ndk.utils_android16.ApiUtils;

public class ApiWrapper {

    public static String getHttpApi(String method_name) {

        //TODO : Eliminate Method name from API_Utils
        return ApiUtils.getHttpApi(method_name, ServerEndpoint.SERVER_IP_ADDRESS, ServerEndpoint.HTTP_API_FOLDER, ServerEndpoint.FILE_EXTENSION);
    }
}

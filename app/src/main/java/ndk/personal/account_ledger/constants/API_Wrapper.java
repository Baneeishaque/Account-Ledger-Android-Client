package ndk.personal.account_ledger.constants;


import ndk.utils_android16.API_Utils;

public class API_Wrapper {

    public static String get_http_API(String method_name) {
        //TODO : Eliminate Method name from API_Utils
        return API_Utils.get_http_API(method_name, Server_Endpoint.SERVER_IP_ADDRESS, Server_Endpoint.HTTP_API_FOLDER, Server_Endpoint.FILE_EXTENSION);
    }
}

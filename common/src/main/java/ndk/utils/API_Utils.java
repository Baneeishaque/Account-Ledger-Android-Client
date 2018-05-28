package ndk.utils;

public class API_Utils {

    public static String get_Sub_sectioned_http_API(String section, String sub_section, String method_name, String SERVER_IP_ADDRESS, String HTTP_API_FOLDER, String FILE_EXTENSION) {
        return SERVER_IP_ADDRESS + "/" + HTTP_API_FOLDER + "/" + section + "/" + sub_section + "/" + method_name + FILE_EXTENSION;
    }

    public static String get_Sectioned_http_API(String section, String method_name, String SERVER_IP_ADDRESS, String HTTP_API_FOLDER, String FILE_EXTENSION) {
        return SERVER_IP_ADDRESS + "/" + HTTP_API_FOLDER + "/" + section + "/" + method_name + FILE_EXTENSION;
    }

    public static String get_http_API(String method_name, String SERVER_IP_ADDRESS, String HTTP_API_FOLDER, String FILE_EXTENSION) {
        return SERVER_IP_ADDRESS + "/" + HTTP_API_FOLDER + "/" + method_name + FILE_EXTENSION;
    }

}

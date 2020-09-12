package ndk.personal.account_ledger.constants;

import ndk.utils_android16.ApiUtils;

public class ApiWrapper {

    private static String getHttpApi(String methodName) {

        //TODO : Eliminate Method name from API_Utils
        return ApiUtils.getHttpApi(methodName, ServerEndpoint.SERVER_ADDRESS, ServerEndpoint.HTTP_API_FOLDER, ServerEndpoint.FILE_EXTENSION);
    }

    public static String deleteTransactionV2(){

        return getHttpApi(Api.deleteTransactionV2);
    }

    public static String selectUserTransactionsV2(){

        return getHttpApi(Api.selectUserTransactionsV2);
    }
    public static String updateTransactionV2(){

        return getHttpApi(Api.updateTransactionV2);
    }
    public static String selectUserAccounts(){

        return getHttpApi(Api.selectUserAccounts);
    }
    public static String insertTransactionV2(){

        return getHttpApi(Api.insertTransactionV2);
    }
    public static String insertAccount(){

        return getHttpApi(Api.insertAccount);
    }
    public static String selectUserTransactions(){

        return getHttpApi(Api.selectUserTransactions);
    }
    public static String insertTransaction(){

        return getHttpApi(Api.insertTransaction);
    }
    public static String selectConfiguration(){

        return getHttpApi(Api.selectConfiguration);
    }
    public static String selectUser(){

        return getHttpApi(Api.selectUser);
    }

    public static String selectUserTransactionsV3() {

        return getHttpApi(Api.selectUserTransactionsV3);
    }
}

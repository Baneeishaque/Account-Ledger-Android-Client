package ndk.personal.account_ledger.constants;

import ndk.utils_android16.ApiUtils;

public class ApiWrapper {

    private static String getHttpApi(String methodName) {

        //TODO : Eliminate Method name from API_Utils
        return ApiUtils.getHttpApi(methodName, ServerEndpoint.SERVER_ADDRESS, ServerEndpoint.HTTP_API_FOLDER, ServerEndpoint.FILE_EXTENSION);
    }

    public static String deleteTransactionV2() {

        return getHttpApi(ApiMethods.API_METHOD_DELETE_TRANSACTION_V_2);
    }

    public static String selectUserTransactionsV2() {

        return getHttpApi(ApiMethods.API_METHOD_SELECT_USER_TRANSACTIONS_V_2);
    }

    public static String updateTransactionV2() {

        return getHttpApi(ApiMethods.API_METHOD_UPDATE_TRANSACTION_V_2);
    }

    public static String selectUserAccounts() {

        return getHttpApi(ApiMethods.API_METHOD_SELECT_USER_ACCOUNTS);
    }

    public static String insertTransactionV2() {

        return getHttpApi(ApiMethods.API_METHOD_INSERT_TRANSACTION_V_2);
    }

    public static String insertAccount() {

        return getHttpApi(ApiMethods.API_METHOD_INSERT_ACCOUNT);
    }

    public static String selectUserTransactions() {

        return getHttpApi(ApiMethods.API_METHOD_SELECT_USER_TRANSACTIONS);
    }

    public static String insertTransaction() {

        return getHttpApi(ApiMethods.API_METHOD_INSERT_TRANSACTION);
    }

    public static String selectConfiguration() {

        return getHttpApi(ApiMethods.API_METHOD_SELECT_CONFIGURATION);
    }

    public static String selectUser() {

        return getHttpApi(ApiMethods.API_METHOD_SELECT_USER);
    }

    public static String selectUserTransactionsV3() {

        return getHttpApi(ApiMethods.API_METHOD_SELECT_USER_TRANSACTIONS_V_3);
    }
}

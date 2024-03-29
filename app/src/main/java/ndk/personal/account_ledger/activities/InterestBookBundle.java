package ndk.personal.account_ledger.activities;

import ndk.personal.account_ledger.constants.ApiMethodParameters;
import ndk.utils_android16.models.sortable_tableView.pass_book.PassBookEntryV2;
import ndk.utils_android19.PassBookBundle;

public class InterestBookBundle extends PassBookBundle {

    private static final int writeExternalStoragePermissionRequestCode = 0;

    @Override
    public void configure_ROW_LONG_CLICK_ACTIONS(PassBookEntryV2 clickedData) {

    }

    @Override
    protected String configureApiMethodParameterNameForUserId() {
        return ApiMethodParameters.API_METHOD_PARAMETER_USER_ID;
    }

    @Override
    public String configureCurrentAccountLongName() {

        //        TODO : Use this
        return "";
    }

    @Override
    public String configureCurrentAccountShortName() {

        //        TODO : Use this
        return "";
    }

    @Override
    public int configureWriteExternalStoragePermissionRequestCode() {

        return writeExternalStoragePermissionRequestCode;
    }
}

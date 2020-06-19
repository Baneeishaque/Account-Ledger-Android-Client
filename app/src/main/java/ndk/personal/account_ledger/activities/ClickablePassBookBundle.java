package ndk.personal.account_ledger.activities;

import androidx.core.util.Pair;

import ndk.utils_android14.ActivityUtils;
import ndk.utils_android16.DateUtils;
import ndk.utils_android19.PassBookBundle;

public class ClickablePassBookBundle extends PassBookBundle {

    private int writeExternalStoragePermissionRequestCode = 0;

    @Override
    protected void configure_ROW_LONG_CLICK_ACTIONS(ndk.utils_android16.models.sortable_tableView.pass_book.PassBookEntryV2 clickedData) {

        ActivityUtils.startActivityWithStringExtrasAndFinish(activityContext, Edit_Transaction_v2.class, new Pair[]{new Pair<>("FROM_ACCOUNT_FULL_NAME", clickedData.getFromAccountFullName().replace(":", " : ")), new Pair<>("FROM_ACCOUNT_ID", String.valueOf(clickedData.getFromAccountId())), new Pair<>("TO_ACCOUNT_FULL_NAME", clickedData.getToAccountFullName().replace(":", " : ")), new Pair<>("TO_ACCOUNT_ID", String.valueOf(clickedData.getToAccountId())), new Pair<>("TRANSACTION_ID", String.valueOf(clickedData.getId())), new Pair<>("EVENT_DATE_TIME", DateUtils.normalDateTimeFormatWords.format(clickedData.getInsertionDate())), new Pair<>("EVENT_PURPOSE", clickedData.getParticulars()), new Pair<>("EVENT_AMOUNT", String.valueOf(clickedData.getCreditAmount() == 0 ? clickedData.getDebitAmount() : clickedData.getCreditAmount()))});
    }

    @Override
    protected String configureCurrentAccountLongName() {

        return "";
    }

    @Override
    protected String configureCurrentAccountShortName() {

        return "";
    }

    @Override
    protected int configureWriteExternalStoragePermissionRequestCode() {

        return writeExternalStoragePermissionRequestCode;
    }

    @Override
    protected String configurePermissionRequiredMessage() {

        return "Storage Permission is required to store ledger file...";
    }
}

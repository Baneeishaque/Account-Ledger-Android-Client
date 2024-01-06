package ndk.personal.account_ledger.activities;

import androidx.core.util.Pair;

import ndk.utils_android1.DateUtils1;
import ndk.utils_android19.ActivityUtils19;
import ndk.utils_android19.PassBookBundle;
import ndk.utils_android19.models.PairOfStringsModel;

public class ClickablePassBookBundle extends PassBookBundle {

    private int writeExternalStoragePermissionRequestCode = 0;

    @Override
    public void configure_ROW_LONG_CLICK_ACTIONS(ndk.utils_android16.models.sortable_tableView.pass_book.PassBookEntryV2 clickedData) {

        ActivityUtils19.startActivityWithStringExtrasAndFinish(currentActivityContext, Edit_Transaction_v2.class, new PairOfStringsModel[]{new PairOfStringsModel("FROM_ACCOUNT_FULL_NAME", clickedData.getFromAccountFullName().replace(":", " : ")), new PairOfStringsModel("FROM_ACCOUNT_ID", String.valueOf(clickedData.getFromAccountId())), new PairOfStringsModel("TO_ACCOUNT_FULL_NAME", clickedData.getToAccountFullName().replace(":", " : ")), new PairOfStringsModel("TO_ACCOUNT_ID", String.valueOf(clickedData.getToAccountId())), new PairOfStringsModel("TRANSACTION_ID", String.valueOf(clickedData.getId())), new PairOfStringsModel("EVENT_DATE_TIME", DateUtils1.normalDateTimeInWordsFormat.format(clickedData.getInsertionDate())), new PairOfStringsModel("EVENT_PURPOSE", clickedData.getParticulars()), new PairOfStringsModel("EVENT_AMOUNT", String.valueOf(clickedData.getCreditAmount() == 0 ? clickedData.getDebitAmount() : clickedData.getCreditAmount()))});
    }

    @Override
    public String configureCurrentAccountLongName() {

        return "";
    }

    @Override
    public String configureCurrentAccountShortName() {

        return "";
    }

    @Override
    public int configureWriteExternalStoragePermissionRequestCode() {

        return writeExternalStoragePermissionRequestCode;
    }

    @Override
    public String configurePermissionRequiredMessage() {

        return "Storage Permission is required to store ledger file...";
    }
}

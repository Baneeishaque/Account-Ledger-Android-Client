package ndk.personal.account_ledger.activities;

import android.util.Log;

import androidx.core.util.Pair;

import ndk.personal.account_ledger.constants.Application_Specification;
import ndk.utils_android14.ActivityUtils;
import ndk.utils_android16.Date_Utils;
import ndk.utils_android19.PassBookBundle;

public class Clickable_Pass_Book_Bundle extends PassBookBundle {

    private int writeExternalStoragePermissionRequestCode = 0;

    @Override
    protected void configure_ROW_LONG_CLICK_ACTIONS(ndk.utils_android16.models.sortable_tableView.pass_book.PassBookEntryV2 clickedData) {

//        Log.d(Application_Specification.APPLICATION_NAME, "From Custom Activity : " + clickedData.toString());
//        Log.d(Application_Specification.APPLICATION_NAME, "MySQL Date Time String : " + Date_Utils.mysql_date_time_format.format(clickedData.getInsertion_date()));
//        Log.d(Application_Specification.APPLICATION_NAME, "Normal Date Time String : " + Date_Utils.normal_date_time_format.format(clickedData.getInsertion_date()));
//        Log.d(Application_Specification.APPLICATION_NAME, "Normal Date Time Words String : " + Date_Utils.normal_date_time_format_words.format(clickedData.getInsertion_date()));

        ActivityUtils.startActivityWithStringExtrasAndFinish(activityContext, Edit_Transaction_v2.class, new Pair[]{new Pair<>("FROM_ACCOUNT_FULL_NAME", clickedData.getFromAccountFullName().replace(":", " : ")), new Pair<>("FROM_ACCOUNT_ID", String.valueOf(clickedData.getFromAccountId())), new Pair<>("TO_ACCOUNT_FULL_NAME", clickedData.getToAccountFullName().replace(":", " : ")), new Pair<>("TO_ACCOUNT_ID", String.valueOf(clickedData.getToAccountId())), new Pair<>("TRANSACTION_ID", String.valueOf(clickedData.getId())), new Pair<>("EVENT_DATE_TIME", Date_Utils.normal_date_time_format_words.format(clickedData.getInsertionDate())), new Pair<>("EVENT_PURPOSE", clickedData.getParticulars()), new Pair<>("EVENT_AMOUNT", String.valueOf(clickedData.getCreditAmount() == 0 ? clickedData.getDebitAmount() : clickedData.getCreditAmount()))});
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
}

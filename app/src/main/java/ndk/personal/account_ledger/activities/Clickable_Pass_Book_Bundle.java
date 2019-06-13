package ndk.personal.account_ledger.activities;

import android.util.Log;

import androidx.core.util.Pair;

import ndk.personal.account_ledger.constants.Application_Specification;
import ndk.utils_android14.ActivityUtils;
import ndk.utils_android16.Date_Utils;
import ndk.utils_android19.PassBookBundle;

public class Clickable_Pass_Book_Bundle extends PassBookBundle {

    @Override
    protected void configure_ROW_LONG_CLICK_ACTIONS(ndk.utils_android16.models.sortable_tableView.pass_book.Pass_Book_Entry_v2 clickedData) {
        Log.d(Application_Specification.APPLICATION_NAME, "From Custom Activity : " + clickedData.toString());

        Log.d(Application_Specification.APPLICATION_NAME, "MySQL Date Time String : " + Date_Utils.mysql_date_time_format.format(clickedData.getInsertion_date()));

        Log.d(Application_Specification.APPLICATION_NAME, "Normal Date Time String : " + Date_Utils.normal_date_time_format.format(clickedData.getInsertion_date()));

        Log.d(Application_Specification.APPLICATION_NAME, "Normal Date Time Words String : " + Date_Utils.normal_date_time_format_words.format(clickedData.getInsertion_date()));

        ActivityUtils.start_activity_with_string_extras_and_finish(activityContext, Edit_Transaction_v2.class, new Pair[]{new Pair<>("FROM_ACCOUNT_FULL_NAME", clickedData.getFrom_account_full_name().replace(":", " : ")), new Pair<>("FROM_ACCOUNT_ID", String.valueOf(clickedData.getFrom_account_id())), new Pair<>("TO_ACCOUNT_FULL_NAME", clickedData.getTo_account_full_name().replace(":", " : ")), new Pair<>("TO_ACCOUNT_ID", String.valueOf(clickedData.getTo_account_id())), new Pair<>("TRANSACTION_ID", String.valueOf(clickedData.getId())), new Pair<>("EVENT_DATE_TIME", Date_Utils.normal_date_time_format_words.format(clickedData.getInsertion_date())), new Pair<>("EVENT_PURPOSE", clickedData.getParticulars()), new Pair<>("EVENT_AMOUNT", String.valueOf(clickedData.getCredit_amount() == 0 ? clickedData.getDebit_amount() : clickedData.getCredit_amount()))});
    }

    @Override
    protected String configureCurrentAccountLongName() {
//        return getIntent().getStringExtra("account_name");
        return "";
    }

    @Override
    protected String configureCurrentAccountShortName() {
//        return getIntent().getStringExtra("account_full_name");
        return "";
    }
}

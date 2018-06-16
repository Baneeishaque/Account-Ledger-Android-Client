package ndk.personal.account_ledger.activities;

import android.support.v4.util.Pair;
import android.util.Log;

import ndk.personal.account_ledger.constants.Application_Specification;
import ndk.utils.Activity_Utils;
import ndk.utils.Date_Utils;
import ndk.utils.activities.Pass_Book_Bundle;
import ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry_v2;

public class Clickable_Pass_Book_Bundle extends Pass_Book_Bundle {

    @Override
    protected void configure_ROW_LONG_CLICK_ACTIONS(Pass_Book_Entry_v2 clickedData) {
        Log.d(Application_Specification.APPLICATION_NAME, "From Custom Activity : " + clickedData.toString());

        Log.d(Application_Specification.APPLICATION_NAME, "MySQL Date Time String : " + Date_Utils.mysql_date_time_format.format(clickedData.getInsertion_date()));

        Log.d(Application_Specification.APPLICATION_NAME, "Normal Date Time String : " + Date_Utils.normal_date_time_format.format(clickedData.getInsertion_date()));

        Log.d(Application_Specification.APPLICATION_NAME, "Normal Date Time Words String : " + Date_Utils.normal_date_time_format_words.format(clickedData.getInsertion_date()));

        Activity_Utils.start_activity_with_string_extras_and_finish(this, Edit_Transaction_v2.class, new Pair[]{new Pair<>("FROM_ACCOUNT_FULL_NAME", clickedData.getFrom_account_full_name().replace(":", " : ")), new Pair<>("FROM_ACCOUNT_ID", String.valueOf(clickedData.getFrom_account_id())), new Pair<>("TO_ACCOUNT_FULL_NAME", clickedData.getTo_account_full_name().replace(":", " : ")), new Pair<>("TO_ACCOUNT_ID", String.valueOf(clickedData.getTo_account_id())), new Pair<>("TRANSACTION_ID", String.valueOf(clickedData.getId())), new Pair<>("EVENT_DATE_TIME", Date_Utils.normal_date_time_format_words.format(clickedData.getInsertion_date())), new Pair<>("EVENT_PURPOSE", clickedData.getParticulars()), new Pair<>("EVENT_AMOUNT", String.valueOf(clickedData.getCredit_amount() == 0 ? clickedData.getDebit_amount() : clickedData.getCredit_amount()))});
    }
}

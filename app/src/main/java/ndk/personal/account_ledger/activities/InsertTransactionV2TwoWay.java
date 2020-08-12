package ndk.personal.account_ledger.activities;

import android.view.Menu;

import ndk.personal.account_ledger.R;
import ndk.utils_android16.CalendarUtils;
import ndk.utils_android1.NetworkUtils;

public class InsertTransactionV2TwoWay extends InsertTransactionV2 {

    @Override
    public void executeInsertTransaction() {

        NetworkUtils.FurtherActions furtherActions = () -> InsertTransactionV2Utils.executeInsertTransactionTaskWithClearingOfEditTextsAndIncrementingOfButtonTextTimeStampForFiveMinutes(progressBarView, formView, this, this, sharedPreferences.getString("user_id", "0"), editTextPurpose.getText().toString().trim(), Double.parseDouble(editTextAmount.getText().toString().trim()), Integer.parseInt(selectedToAccountId), Integer.parseInt(selectedFromAccountId), editTextPurpose, editTextAmount, buttonDate, CalendarUtils.addFiveMinutesToCalendar(calendar));

        InsertTransactionV2Utils.executeInsertTransactionTaskWithFurtherActions(progressBarView, formView, this, this, sharedPreferences.getString("user_id", "0"), editTextPurpose.getText().toString().trim(), Double.parseDouble(editTextAmount.getText().toString().trim()), Integer.parseInt(selectedFromAccountId), Integer.parseInt(selectedToAccountId), editTextPurpose, calendar, furtherActions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        boolean result = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_insert_two_way_transaction).setVisible(false);
        return result;
    }
}

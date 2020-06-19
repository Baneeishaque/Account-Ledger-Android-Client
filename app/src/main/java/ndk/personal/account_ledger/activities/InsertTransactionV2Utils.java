package ndk.personal.account_ledger.activities;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import java.util.Calendar;

import ndk.personal.account_ledger.constants.Api;
import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android16.ButtonUtils;
import ndk.utils_android16.DateUtils;
import ndk.utils_android16.NetworkUtils;
import ndk.utils_android16.network_task.RestInsertTaskWrapper;

import static ndk.utils_android16.ButtonUtils.associateButtonWithTimeStampPlusOneMinute;


class InsertTransactionV2Utils {

    static void executeInsertTransactionTask(View progressBarView, View formView, Context context, AppCompatActivity currentActivity, String userId, String particulars, Double amount, int fromAccountId, int toAccountId, EditText editTextParticulars, EditText editTextAmount, Button buttonDate, Calendar calendar) {

        NetworkUtils.FurtherActions furtherActions = () -> associateButtonWithTimeStampPlusOneMinute(buttonDate, calendar);

        RestInsertTaskWrapper.execute(context, ApiWrapper.getHttpApi(Api.insert_Transaction_v2), currentActivity, progressBarView, formView, ApplicationSpecification.APPLICATION_NAME, new Pair[]{new Pair<>("event_date_time", DateUtils.dateToMysqlDateTimeString(calendar.getTime())), new Pair<>("user_id", userId), new Pair<>("particulars", particulars), new Pair<>("amount", amount), new Pair<>("from_account_id", fromAccountId), new Pair<>("to_account_id", toAccountId)}, editTextParticulars, new EditText[]{editTextParticulars, editTextAmount}, furtherActions);
    }

    static void executeInsertTransactionTaskWithFurtherActions(View progressBarView, View formView, Context context, AppCompatActivity currentActivity, String userId, String particulars, Double amount, int fromAccountId, int toAccountId, EditText editTextParticulars, EditText editTextAmount, Button buttonDate, Calendar calendar, NetworkUtils.FurtherActions furtherActions) {

        NetworkUtils.FurtherActions furtherActionsWithButtonAssociationOfTimeStampWithPlusOneMinute = () -> {

            associateButtonWithTimeStampPlusOneMinute(buttonDate, calendar);
            furtherActions.onSuccess();
        };

        RestInsertTaskWrapper.execute(context, ApiWrapper.getHttpApi(Api.insert_Transaction_v2), currentActivity, progressBarView, formView, ApplicationSpecification.APPLICATION_NAME, new Pair[]{new Pair<>("event_date_time", DateUtils.dateToMysqlDateTimeString(calendar.getTime())), new Pair<>("user_id", userId), new Pair<>("particulars", particulars), new Pair<>("amount", amount), new Pair<>("from_account_id", fromAccountId), new Pair<>("to_account_id", toAccountId)}, editTextParticulars, new EditText[]{editTextParticulars, editTextAmount}, furtherActionsWithButtonAssociationOfTimeStampWithPlusOneMinute);
    }
}

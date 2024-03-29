package ndk.personal.account_ledger.activities;

import static ndk.utils_android1.ButtonUtils.associateButtonWithIncrementedTimeStampOfFiveMinutes;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import java.util.Calendar;

import ndk.personal.account_ledger.constants.ApiMethodParameters;
import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.utils_android1.DateUtils1;
import ndk.utils_android14.NetworkUtils14;
import ndk.utils_android19.network_task.RestInsertTaskWrapper;


class InsertTransactionV2Utils {

    static void executeInsertTransactionTaskWithClearingOfEditTextsAndIncrementingOfButtonTextTimeStampForFiveMinutes(View progressBarView, View formView, Context context, AppCompatActivity currentActivity, String userId, String particulars, Double amount, int fromAccountId, int toAccountId, EditText editTextParticulars, EditText editTextAmount, Button buttonDate, Calendar calendar) {

        NetworkUtils14.FurtherActions furtherActions = () -> associateButtonWithIncrementedTimeStampOfFiveMinutes(buttonDate, calendar);

        RestInsertTaskWrapper.execute(
                context,
                ApiWrapper.insertTransactionV2(),
                currentActivity,
                progressBarView,
                formView,
                ApplicationSpecification.APPLICATION_NAME,
                new Pair[]{
                        new Pair<>("event_date_time", DateUtils1.dateToMysqlDateTimeString(calendar.getTime())),
                        new Pair<>(ApiMethodParameters.API_METHOD_PARAMETER_USER_ID, userId),
                        new Pair<>("particulars", particulars),
                        new Pair<>("amount", amount),
                        new Pair<>("from_account_id", fromAccountId),
                        new Pair<>("to_account_id", toAccountId)
                },
                editTextParticulars,
                new EditText[]{
                        editTextParticulars,
                        editTextAmount
                },
                furtherActions
        );
    }

    static void executeInsertTransactionTaskWithFurtherActionsAndClearingOfEditTexts(View progressBarView, View formView, Context context, AppCompatActivity currentActivity, String userId, String particulars, Double amount, int fromAccountId, int toAccountId, EditText editTextParticulars, EditText editTextAmount, Calendar calendar, NetworkUtils14.FurtherActions furtherActions) {

        RestInsertTaskWrapper.execute(
                context,
                ApiWrapper.insertTransactionV2(),
                currentActivity,
                progressBarView,
                formView,
                ApplicationSpecification.APPLICATION_NAME,
                new Pair[]{
                        new Pair<>("event_date_time", DateUtils1.dateToMysqlDateTimeString(calendar.getTime())),
                        new Pair<>(ApiMethodParameters.API_METHOD_PARAMETER_USER_ID, userId),
                        new Pair<>("particulars", particulars),
                        new Pair<>("amount", amount),
                        new Pair<>("from_account_id", fromAccountId),
                        new Pair<>("to_account_id", toAccountId)
                },
                editTextParticulars,
                new EditText[]{
                        editTextParticulars,
                        editTextAmount
                },
                furtherActions
        );
    }

    static void executeInsertTransactionTaskWithFurtherActions(View progressBarView, View formView, Context context, AppCompatActivity currentActivity, String userId, String particulars, Double amount, int fromAccountId, int toAccountId, EditText editTextParticulars, Calendar calendar, NetworkUtils14.FurtherActions furtherActions) {

        RestInsertTaskWrapper.execute(
                context,
                ApiWrapper.insertTransactionV2(),
                currentActivity,
                progressBarView,
                formView,
                ApplicationSpecification.APPLICATION_NAME,
                new Pair[]{
                        new Pair<>("event_date_time", DateUtils1.dateToMysqlDateTimeString(calendar.getTime())),
                        new Pair<>(ApiMethodParameters.API_METHOD_PARAMETER_USER_ID, userId),
                        new Pair<>("particulars", particulars),
                        new Pair<>("amount", amount),
                        new Pair<>("from_account_id", fromAccountId),
                        new Pair<>("to_account_id", toAccountId)
                },
                editTextParticulars,
                furtherActions
        );
    }
}

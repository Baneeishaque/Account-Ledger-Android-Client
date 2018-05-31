package ndk.personal.account_ledger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.fragments.Fragment_List_Accounts;

public class List_Accounts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_accounts);

        // create a FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // replace the FrameLayout with new Fragment
        if (getIntent().getExtras() != null) {
            fragmentTransaction.replace(R.id.frameLayout, Fragment_List_Accounts.newInstance(getIntent().getStringExtra("HEADER_TITLE"),getIntent().getStringExtra("PARENT_ACCOUNT_ID"),getIntent().getStringExtra("ACTIVITY_FOR_RESULT_FLAG")));
        } else {
            fragmentTransaction.replace(R.id.frameLayout, Fragment_List_Accounts.newInstance("NA","0",String.valueOf(false)));
        }

        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            Intent returnIntent = new Intent();
            returnIntent.putExtra("SELECTED_ACCOUNT_FULL_NAME",data.getStringExtra("SELECTED_ACCOUNT_FULL_NAME"));
            returnIntent.putExtra("SELECTED_ACCOUNT_ID",data.getStringExtra("SELECTED_ACCOUNT_ID"));
            setResult(RESULT_OK,returnIntent);
            finish();

        }
    }


}

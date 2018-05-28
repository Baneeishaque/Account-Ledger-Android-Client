package ndk.personal.account_ledger.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ndk.personal.account_ledger.Fragment_List_Accounts;
import ndk.personal.account_ledger.R;

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
            fragmentTransaction.replace(R.id.frameLayout, Fragment_List_Accounts.newInstance(getIntent().getStringExtra("HEADER_TITLE")));
        } else {
            fragmentTransaction.replace(R.id.frameLayout, Fragment_List_Accounts.newInstance("NA"));
        }

        fragmentTransaction.commit(); // save the changes
    }

}

package ndk.personal.account_ledger.activities;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ndk.personal.account_ledger.Fragment_List_Accounts;
import ndk.personal.account_ledger.R;

public class List_Accounts extends AppCompatActivity implements Fragment_List_Accounts.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_accounts);

        // create a FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, Fragment_List_Accounts.newInstance());
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

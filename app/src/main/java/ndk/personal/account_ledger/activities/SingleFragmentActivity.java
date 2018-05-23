package ndk.personal.account_ledger.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ndk.personal.account_ledger.R;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_single_fragment);

        Fragment f = Fragment.instantiate(this, FolderStructureFragment.class.getName());
        getFragmentManager().beginTransaction().replace(R.id.fragment, f, FolderStructureFragment.class.getName()).commit();
    }
}

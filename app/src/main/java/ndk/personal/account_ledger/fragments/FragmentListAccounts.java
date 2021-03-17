package ndk.personal.account_ledger.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.activities.ClickablePassBookBundle;
import ndk.personal.account_ledger.activities.InsertTransactionV2;
import ndk.personal.account_ledger.activities.InsertTransactionV2Quick;
import ndk.personal.account_ledger.activities.Insert_Account;
import ndk.personal.account_ledger.activities.ListAccounts;
import ndk.personal.account_ledger.adapters.ListAccountsAdapter;
import ndk.personal.account_ledger.constants.ApiWrapper;
import ndk.personal.account_ledger.constants.ApplicationSpecification;
import ndk.personal.account_ledger.models.Account;
import ndk.personal.account_ledger.utils.AccountLedgerErrorUtils;
import ndk.utils_android14.ActivityUtils14;
import ndk.utils_android14.RestGetTask;
import ndk.utils_android16.TodoUtils;
import ndk.utils_android16.network_task.HttpApiSelectTask;
import ndk.utils_android16.network_task.HttpApiSelectTaskWrapper;

import static android.app.Activity.RESULT_OK;

public class FragmentListAccounts extends Fragment {

    private String current_header_title, current_parent_account_id, current_account_type, current_account_commodity_type, current_account_commodity_value, current_account_taxable, current_account_place_holder, currentAccountName, currentAccountFullName;

    private boolean activity_for_result_flag;

    private RecyclerView recyclerView;

    private ProgressBar login_progressBar;

    private SharedPreferences sharedPreferences;

    private ListAccountsAdapter mAdapter;

    private ArrayList<Account> accounts = new ArrayList<>();

    public FragmentListAccounts() {
        // Required empty public constructor
    }

    public static FragmentListAccounts newInstance(String header_title, String parent_account_id, String activity_for_result_flag, String account_type, String account_commodity_type, String account_commodity_value, String account_taxable, String account_place_holder, String accountName, String accountFullName) {

        FragmentListAccounts fragment = new FragmentListAccounts();
        Bundle args = new Bundle();
        args.putString("HEADER_TITLE", header_title);
        args.putString("PARENT_ACCOUNT_ID", parent_account_id);
        args.putString("ACTIVITY_FOR_RESULT_FLAG", activity_for_result_flag);
        args.putString("CURRENT_ACCOUNT_TYPE", account_type);
        args.putString("CURRENT_ACCOUNT_COMMODITY_TYPE", account_commodity_type);
        args.putString("CURRENT_ACCOUNT_COMMODITY_VALUE", account_commodity_value);
        args.putString("CURRENT_ACCOUNT_TAXABLE", account_taxable);
        args.putString("CURRENT_ACCOUNT_PLACE_HOLDER", account_place_holder);
        args.putString("CURRENT_ACCOUNT_NAME", accountName);
        args.putString("CURRENT_ACCOUNT_FULL_NAME", accountFullName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {

            current_header_title = getArguments().getString("HEADER_TITLE");
            current_parent_account_id = getArguments().getString("PARENT_ACCOUNT_ID");
            activity_for_result_flag = Boolean.parseBoolean(getArguments().getString("ACTIVITY_FOR_RESULT_FLAG"));
            current_account_type = getArguments().getString("CURRENT_ACCOUNT_TYPE");
            current_account_commodity_type = getArguments().getString("CURRENT_ACCOUNT_COMMODITY_TYPE");
            current_account_commodity_value = getArguments().getString("CURRENT_ACCOUNT_COMMODITY_VALUE");
            current_account_taxable = getArguments().getString("CURRENT_ACCOUNT_TAXABLE");
            current_account_place_holder = getArguments().getString("CURRENT_ACCOUNT_PLACE_HOLDER");
            currentAccountName = getArguments().getString("CURRENT_ACCOUNT_NAME");
            currentAccountFullName = getArguments().getString("CURRENT_ACCOUNT_FULL_NAME");
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_accounts, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        login_progressBar = view.findViewById(R.id.login_progress);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        setAdapter();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        //            TODO : No Transaction in place holder A/Cs

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list_accounts, menu);

        if (activity_for_result_flag) {

            menu.findItem(R.id.action_add_transaction).setVisible(false);
            menu.findItem(R.id.action_quick_add_transaction).setVisible(false);
            menu.findItem(R.id.action_interest_calculator_pnb).setVisible(false);

        } else if (!current_header_title.equals("NA")) {

            menu.findItem(R.id.action_quick_add_transaction).setVisible(false);
//            TODO : Sub Menu of Savings Accounts
            menu.findItem(R.id.action_interest_calculator_pnb).setVisible(false);

        } else {

            menu.findItem(R.id.action_add_transaction).setVisible(false);
        }

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getActivity().getComponentName()));

        //Changing edittext color
        EditText searchEdit = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = (source, start, end, dest, dstart, dend) -> {

            for (int i = start; i < end; i++) {

                if (!Character.isLetterOrDigit(source.charAt(i)))
                    return "";
            }

            return null;
        };
        searchEdit.setFilters(fArray);
        View v = searchView.findViewById(androidx.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Account> filterList = new ArrayList<>();
                if (s.length() > 0) {
                    for (int i = 0; i < accounts.size(); i++) {
                        if (accounts.get(i).getName().toLowerCase().contains(s.toLowerCase())) {
                            filterList.add(accounts.get(i));
                            mAdapter.updateList(filterList);
                        }
                    }

                } else {
                    mAdapter.updateList(accounts);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add_transaction:

                ActivityUtils14.startActivityForClassWithStringExtras(Objects.requireNonNull(getActivity()), InsertTransactionV2.class, new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID", current_parent_account_id), new Pair<>("CURRENT_ACCOUNT_FULL_NAME", current_header_title), new Pair<>("CURRENT_ACCOUNT_TYPE", current_account_type), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", current_account_commodity_type), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", current_account_commodity_value), new Pair<>("CURRENT_ACCOUNT_TAXABLE", current_account_taxable), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", current_account_place_holder)});
                break;

            case R.id.action_quick_add_transaction:
                ActivityUtils14.startActivityForClassWithStringExtras(Objects.requireNonNull(getActivity()), InsertTransactionV2Quick.class, new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID", "6"), new Pair<>("CURRENT_ACCOUNT_FULL_NAME", "Assets : Current Assets : Cash in Wallet")});
                break;

            case R.id.action_add_account:
                ActivityUtils14.startActivityWithStringExtrasAndFinish(Objects.requireNonNull(getActivity()), Insert_Account.class, new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID", current_parent_account_id), new Pair<>("CURRENT_ACCOUNT_FULL_NAME", current_header_title), new Pair<>("CURRENT_ACCOUNT_TYPE", current_account_type), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", current_account_commodity_type), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", current_account_commodity_value), new Pair<>("CURRENT_ACCOUNT_TAXABLE", current_account_taxable), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", current_account_place_holder)});
                break;

            case R.id.action_interest_calculator_pnb:
                TodoUtils.displayTodoSnackBar(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdapter() {

        HttpApiSelectTask.AsyncResponseJsonArray async_response_json_array = json_array -> {

            for (int i = 1; i < json_array.length(); i++) {

                try {

                    accounts.add(new Account(json_array.getJSONObject(i).getString("account_type"), json_array.getJSONObject(i).getString("account_id"), json_array.getJSONObject(i).getString("notes"), json_array.getJSONObject(i).getString("parent_account_id"), json_array.getJSONObject(i).getString("owner_id"), json_array.getJSONObject(i).getString("name"), json_array.getJSONObject(i).getString("commodity_type"), json_array.getJSONObject(i).getString("commodity_value"), json_array.getJSONObject(i).getString("name")));

                } catch (JSONException e) {

                    AccountLedgerErrorUtils.displayException(getActivity(), e);
                }
            }

            viewRecyclerView();
        };

        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(ApplicationSpecification.APPLICATION_NAME, Context.MODE_PRIVATE);

        HttpApiSelectTaskWrapper.executeGetThenReturnJsonArrayWithErrorStatus(RestGetTask.prepareGetUrl(ApiWrapper.selectUserAccounts(), new Pair[]{new Pair<>("user_id", sharedPreferences.getString("user_id", "0")), new Pair<>("parent_account_id", current_parent_account_id)}), getContext(), login_progressBar, recyclerView, ApplicationSpecification.APPLICATION_NAME, async_response_json_array, false);
    }

    private void viewRecyclerView() {

        if (current_header_title.equals("NA")) {
            mAdapter = new ListAccountsAdapter(accounts);
        } else {
            mAdapter = new ListAccountsAdapter(accounts, current_header_title);
        }

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener((view, position, model) -> {

            //handle item click events here
            Toast.makeText(getActivity(), "Selected Accounts Ledger : " + model.getName(), Toast.LENGTH_SHORT).show();

            //TODO : Include Taxable & place holder in account object
            ActivityUtils14.startActivityForClassWithStringExtras(Objects.requireNonNull(getActivity()), ListAccounts.class, new Pair[]{new Pair<>("HEADER_TITLE", current_header_title.equals("NA") ? model.getName() : current_header_title + " : " + model.getName()), new Pair<>("PARENT_ACCOUNT_ID", model.getAccountId()), new Pair<>("ACTIVITY_FOR_RESULT_FLAG", String.valueOf(activity_for_result_flag)), new Pair<>("CURRENT_ACCOUNT_TYPE", model.getAccountType()), new Pair<>("CURRENT_ACCOUNT_COMMODITY_TYPE", model.getCommodityType()), new Pair<>("CURRENT_ACCOUNT_COMMODITY_VALUE", model.getCommodityValue()), new Pair<>("CURRENT_ACCOUNT_TAXABLE", current_account_taxable), new Pair<>("CURRENT_ACCOUNT_PLACE_HOLDER", current_account_place_holder), new Pair<>("ACCOUNT_NAME", model.getName()), new Pair<>("ACCOUNT_FULL_NAME", model.getFullName())});

        });

        mAdapter.SetOnHeaderClickListener((view, headerTitle) -> {

            if (activity_for_result_flag) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("SELECTED_ACCOUNT_FULL_NAME", current_header_title);
                returnIntent.putExtra("SELECTED_ACCOUNT_ID", current_parent_account_id);
                Objects.requireNonNull(getActivity()).setResult(RESULT_OK, returnIntent);
                getActivity().finish();

            } else {

                //handle item click events here
                Toast.makeText(getActivity(), "Selected Transactions Ledger : " + headerTitle, Toast.LENGTH_SHORT).show();

                ActivityUtils14.startActivityForClassWithStringExtras(Objects.requireNonNull(getActivity()), ClickablePassBookBundle.class, new Pair[]{new Pair<>("URL", RestGetTask.prepareGetUrl(ApiWrapper.selectUserTransactionsV2(), new Pair[]{new Pair<>("user_id", sharedPreferences.getString("user_id", "0")), new Pair<>("account_id", current_parent_account_id)})), new Pair<>("application_name", ApplicationSpecification.APPLICATION_NAME), new Pair<>("V2_FLAG", String.valueOf(true)), new Pair<>("SORT_FLAG", String.valueOf(true)), new Pair<>("account_name", currentAccountName), new Pair<>("account_full_name", currentAccountFullName), new Pair<>("current_account_id", current_parent_account_id)});
            }

        });

        mAdapter.SetOnHeaderLongClickListener((view, headerTitle) -> {

            if (!activity_for_result_flag) {

                Toast.makeText(getActivity(), "Selected Transactions Ledger : " + headerTitle, Toast.LENGTH_SHORT).show();

                ActivityUtils14.startActivityForClassWithStringExtras(Objects.requireNonNull(getActivity()), ClickablePassBookBundle.class, new Pair[]{new Pair<>("URL", RestGetTask.prepareGetUrl(ApiWrapper.selectUserTransactionsV3(), new Pair[]{new Pair<>("user_id", sharedPreferences.getString("user_id", "0")), new Pair<>("account_id", current_parent_account_id)})), new Pair<>("application_name", ApplicationSpecification.APPLICATION_NAME), new Pair<>("V2_FLAG", String.valueOf(true)), new Pair<>("SORT_FLAG", String.valueOf(true)), new Pair<>("account_name", currentAccountName), new Pair<>("account_full_name", currentAccountFullName), new Pair<>("current_account_id", current_parent_account_id)});
            }
        });
    }
}

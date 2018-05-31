package ndk.personal.account_ledger.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

import ndk.personal.account_ledger.R;
import ndk.personal.account_ledger.activities.Insert_Transaction_v2;
import ndk.personal.account_ledger.activities.List_Accounts;
import ndk.personal.account_ledger.adapters.List_Accounts_Adapter;
import ndk.personal.account_ledger.constants.API;
import ndk.personal.account_ledger.constants.API_Wrapper;
import ndk.personal.account_ledger.constants.Application_Specification;
import ndk.personal.account_ledger.models.Account;
import ndk.utils.Activity_Utils;
import ndk.utils.network_task.REST_GET_Task;
import ndk.utils.network_task.REST_Select_Task;
import ndk.utils.network_task.REST_Select_Task_Wrapper;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_List_Accounts#newInstance} factory method to
 * create an instance of this fragment.
 */


public class Fragment_List_Accounts extends Fragment {

    String current_header_title,current_parent_account_id;

    boolean activity_for_result_flag;

    private RecyclerView recyclerView;

    private ProgressBar login_progressBar;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    private List_Accounts_Adapter mAdapter;

    private ArrayList<Account> accounts = new ArrayList<>();

    public Fragment_List_Accounts() {
        // Required empty public constructor
    }

    public static Fragment_List_Accounts newInstance(String header_title,String parent_account_id, String activity_for_result_flag) {
        Fragment_List_Accounts fragment = new Fragment_List_Accounts();
        Bundle args = new Bundle();
        args.putString("HEADER_TITLE", header_title);
        args.putString("PARENT_ACCOUNT_ID", parent_account_id);
        args.putString("ACTIVITY_FOR_RESULT_FLAG", activity_for_result_flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            current_header_title = getArguments().getString("HEADER_TITLE");
            current_parent_account_id=getArguments().getString("PARENT_ACCOUNT_ID");
            activity_for_result_flag= Boolean.parseBoolean(getArguments().getString("ACTIVITY_FOR_RESULT_FLAG"));
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_accounts, container, false);

        // ButterKnife.bind(this);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list_accounts, menu);

        if(activity_for_result_flag)
        {
            menu.findItem(R.id.action_add_transaction).setVisible(false);
        }

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getActivity().getComponentName()));

        //changing edittext color
        EditText searchEdit = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {

                    if (!Character.isLetterOrDigit(source.charAt(i)))
                        return "";
                }

                return null;
            }
        };
        searchEdit.setFilters(fArray);
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
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

        if(item.getItemId()==R.id.action_add_transaction)
        {
            Activity_Utils.start_activity_with_string_extras(getActivity(), Insert_Transaction_v2.class,new Pair[]{new Pair<>("CURRENT_ACCOUNT_ID",current_parent_account_id),new Pair<>("CURRENT_ACCOUNT_FULL_NAME",current_header_title)},false,0);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setAdapter() {

        REST_Select_Task.Async_Response_JSON_array async_response_json_array = new REST_Select_Task.Async_Response_JSON_array() {

            @Override
            public void processFinish(JSONArray json_array) {

                for (int i = 1; i < json_array.length(); i++) {

                    try {

//                        return
//                                "Account{" +
//                                        "account_type = '" + accountType + '\'' +
//                                        ",account_id = '" + accountId + '\'' +
//                                        ",notes = '" + notes + '\'' +
//                                        ",parent_account_id = '" + parentAccountId + '\'' +
//                                        ",owner_id = '" + ownerId + '\'' +
//                                        ",name = '" + name + '\'' +
//                                        ",commodity_type = '" + commodityType + '\'' +
//                                        ",commodity_value = '" + commodityValue + '\'' +
//                                        "}";

                        accounts.add(new Account(json_array.getJSONObject(i).getString("account_type"), json_array.getJSONObject(i).getString("account_id"), json_array.getJSONObject(i).getString("notes"), json_array.getJSONObject(i).getString("parent_account_id"), json_array.getJSONObject(i).getString("owner_id"), json_array.getJSONObject(i).getString("name"), json_array.getJSONObject(i).getString("commodity_type"), json_array.getJSONObject(i).getString("commodity_value")));

                    } catch (JSONException e) {

                        Toast.makeText(getContext(), "Error : " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        Log.d(Application_Specification.APPLICATION_NAME, "Error : " + e.getLocalizedMessage());
                    }
                }

                view_recyclerView();
            }
        };

        SharedPreferences settings;

        settings = Objects.requireNonNull(getContext()).getSharedPreferences(Application_Specification.APPLICATION_NAME, Context.MODE_PRIVATE);

        REST_Select_Task_Wrapper.execute(REST_GET_Task.get_Get_URL(API_Wrapper.get_http_API(API.select_User_Accounts),new Pair[]{new Pair<>("user_id", settings.getString("user_id", "0")),new Pair<>("parent_account_id", current_parent_account_id)}), getContext(), login_progressBar, recyclerView, Application_Specification.APPLICATION_NAME, new Pair[]{}, async_response_json_array,false);

//        accounts.add(new Account("Asset", "1", " Assets", "Assets1", "NA", "0", "Assets", "Currency", "Rs."));
//        accounts.add(new Account("Bsset", "1", " Assets", "Assets2", "NA", "0", "Bssets", "Currency", "Rs."));
//        accounts.add(new Account("Csset", "1", " Assets", "Assets3", "NA", "0", "Cssets", "Currency", "Rs."));
//        accounts.add(new Account("Dsset", "1", " Assets", "Assets4", "NA", "0", "Dssets", "Currency", "Rs."));

    }

    private void view_recyclerView() {

        if (current_header_title.equals("NA")) {
            mAdapter = new List_Accounts_Adapter(accounts);
        } else {
            mAdapter = new List_Accounts_Adapter(accounts, current_header_title);
        }

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new List_Accounts_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Account model) {

                //handle item click events here
                Toast.makeText(getActivity(), "Selected Accounts Ledger : " + model.getName(), Toast.LENGTH_SHORT).show();

                Activity_Utils.start_activity_with_string_extras(getActivity(), List_Accounts.class, new Pair[]{new Pair<>("HEADER_TITLE", current_header_title.equals("NA") ? model.getName() : current_header_title + ":" + model.getName()),new Pair<>("PARENT_ACCOUNT_ID",model.getAccountId()),new Pair<>("ACTIVITY_FOR_RESULT_FLAG",String.valueOf(activity_for_result_flag))},activity_for_result_flag,0);

            }
        });

        mAdapter.SetOnHeaderClickListener(new List_Accounts_Adapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(View view, String headerTitle) {

                if(activity_for_result_flag)
                {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("SELECTED_ACCOUNT_FULL_NAME",current_header_title);
                    returnIntent.putExtra("SELECTED_ACCOUNT_ID",current_parent_account_id);
                    Objects.requireNonNull(getActivity()).setResult(RESULT_OK,returnIntent);
                    getActivity().finish();
                }
                else {
                    //handle item click events here
                    Toast.makeText(getActivity(), "Selected Transactions Ledger : " + headerTitle, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}

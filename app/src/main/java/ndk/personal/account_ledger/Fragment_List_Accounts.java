package ndk.personal.account_ledger;

import android.app.SearchManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ndk.personal.account_ledger.activities.List_Accounts;
import ndk.personal.account_ledger.models.Account;
import ndk.utils.Activity_Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_List_Accounts#newInstance} factory method to
 * create an instance of this fragment.
 */


public class Fragment_List_Accounts extends Fragment {

    String current_header_title;

    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;

    private List_Accounts_Adapter mAdapter;

    private ArrayList<Account> modelList = new ArrayList<>();


    public Fragment_List_Accounts() {
        // Required empty public constructor
    }

    public static Fragment_List_Accounts newInstance(String header_title) {
        Fragment_List_Accounts fragment = new Fragment_List_Accounts();
        Bundle args = new Bundle();
        args.putString("HEADER_TITLE", header_title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            current_header_title = getArguments().getString("HEADER_TITLE");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_accounts, container, false);

        // ButterKnife.bind(this);
        findViews(view);

        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAdapter();

    }


    private void findViews(View view) {

        recyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
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
                ArrayList<Account> filterList = new ArrayList<Account>();
                if (s.length() > 0) {
                    for (int i = 0; i < modelList.size(); i++) {
                        if (modelList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            filterList.add(modelList.get(i));
                            mAdapter.updateList(filterList);
                        }
                    }

                } else {
                    mAdapter.updateList(modelList);
                }
                return false;
            }
        });

    }

    private void setAdapter() {

        modelList.add(new Account("Asset", "1", " Assets", "Assets1", "NA", "0", "Assets", "Currency", "Rs."));
        modelList.add(new Account("Bsset", "1", " Assets", "Assets2", "NA", "0", "Bssets", "Currency", "Rs."));
        modelList.add(new Account("Csset", "1", " Assets", "Assets3", "NA", "0", "Cssets", "Currency", "Rs."));
        modelList.add(new Account("Dsset", "1", " Assets", "Assets4", "NA", "0", "Dssets", "Currency", "Rs."));

        if (current_header_title.equals("NA")) {
            mAdapter = new List_Accounts_Adapter(getActivity(), modelList);
        } else {
            mAdapter = new List_Accounts_Adapter(getActivity(), modelList, current_header_title);
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
                Activity_Utils.start_activity_with_string_extras(getActivity(), List_Accounts.class, new Pair[]{new Pair<>("HEADER_TITLE", current_header_title.equals("NA") ? model.getName() : current_header_title + ":" + model.getName())});

            }
        });


        mAdapter.SetOnHeaderClickListener(new List_Accounts_Adapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(View view, String headerTitle) {

                //handle item click events here
                Toast.makeText(getActivity(), "Selected Transactions Ledger : " + headerTitle, Toast.LENGTH_SHORT).show();

            }
        });

    }

}

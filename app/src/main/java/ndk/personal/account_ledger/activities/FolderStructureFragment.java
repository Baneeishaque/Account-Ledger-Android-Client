package ndk.personal.account_ledger.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import ndk.personal.account_ledger.R;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class FolderStructureFragment extends Fragment {
    String previous = "NA";
    private TextView statusBar;
    private AndroidTreeView tView;
    private int counter = 0;
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            if (!previous.equals(item.text)) {
                statusBar.setText(statusBar.getText().equals("NA") ? item.text : statusBar.getText() + ":" + item.text);
            }
            previous = item.text;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        ViewGroup containerView = rootView.findViewById(R.id.container);

        statusBar = rootView.findViewById(R.id.status_bar);

        TreeNode root = TreeNode.root();
        TreeNode computerRoot = new TreeNode(new IconTreeItemHolder.IconTreeItem("My Computer"));

        TreeNode myDocuments = new TreeNode(new IconTreeItemHolder.IconTreeItem("My Documents"));
        TreeNode downloads = new TreeNode(new IconTreeItemHolder.IconTreeItem("Downloads"));
        TreeNode file1 = new TreeNode(new IconTreeItemHolder.IconTreeItem("Folder 1"));
        TreeNode file2 = new TreeNode(new IconTreeItemHolder.IconTreeItem("Folder 2"));
        TreeNode file3 = new TreeNode(new IconTreeItemHolder.IconTreeItem("Folder 3"));
        TreeNode file4 = new TreeNode(new IconTreeItemHolder.IconTreeItem("Folder 4"));
        fillDownloadsFolder(downloads);
        downloads.addChildren(file1, file2, file3, file4);

        TreeNode myMedia = new TreeNode(new IconTreeItemHolder.IconTreeItem("Photos"));
        TreeNode photo1 = new TreeNode(new IconTreeItemHolder.IconTreeItem("Folder 1"));
        TreeNode photo2 = new TreeNode(new IconTreeItemHolder.IconTreeItem("Folder 2"));
        TreeNode photo3 = new TreeNode(new IconTreeItemHolder.IconTreeItem("Folder 3"));
        myMedia.addChildren(photo1, photo2, photo3);

        myDocuments.addChild(downloads);
        computerRoot.addChildren(myDocuments, myMedia);

        root.addChildren(computerRoot);

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(IconTreeItemHolder.class);
        tView.setDefaultNodeClickListener(nodeClickListener);

        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expandAll:
                tView.expandAll();
                break;

            case R.id.collapseAll:
                tView.collapseAll();
                break;
        }
        return true;
    }

    private void fillDownloadsFolder(TreeNode node) {
        TreeNode downloads = new TreeNode(new IconTreeItemHolder.IconTreeItem("Downloads" + (counter++)));
        node.addChild(downloads);
        if (counter < 5) {
            fillDownloadsFolder(downloads);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }
}

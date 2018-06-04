package ndk.utils.widgets.pass_book;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ListView;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.OnScrollListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import ndk.utils.R;
import ndk.utils.Toast_Utils;
import ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry;


/**
 * An extension of the {@link SortableTableView} that handles {@link Pass_Book_Entry}s.
 *
 * @author ISchwarz
 */
public class Pass_Book_TableView extends SortableTableView<Pass_Book_Entry> {

    public Pass_Book_TableView(final Context context) {
        this(context, null);
    }

    public Pass_Book_TableView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public Pass_Book_TableView(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, "#", "Par.", "Deb.", "Cre.", "Bal.");
        simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.table_header_text));
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven = ContextCompat.getColor(context, R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.table_data_row_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(5);
        tableColumnWeightModel.setColumnWeight(0, 3);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, Pass_Book_TableView_Comparators.get_Insertion_Date_Comparator());
        setColumnComparator(1, Pass_Book_TableView_Comparators.get_Particulars_Comparator());
        setColumnComparator(2, Pass_Book_TableView_Comparators.get_Debit_Amount_Comparator());
        setColumnComparator(3, Pass_Book_TableView_Comparators.get_Credit_Amount_Comparator());
        setColumnComparator(4, Pass_Book_TableView_Comparators.get_Balance_Comparator());

        addDataClickListener(new TableDataClickListener<Pass_Book_Entry>() {
            @Override
            public void onDataClicked(int rowIndex, Pass_Book_Entry clickedData) {

                Toast_Utils.longToast(context, clickedData.toString());
            }
        });

        addDataLongClickListener(new TableDataLongClickListener<Pass_Book_Entry>() {
            @Override
            public boolean onDataLongClicked(int rowIndex, Pass_Book_Entry clickedData) {

                Toast_Utils.longToast(context, clickedData.toString());
                return false;
            }
        });

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(ListView tableDataView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Toast_Utils.longToast(context, "Contents : First Visible Item - " + firstVisibleItem + ", Visible Item Count - " + visibleItemCount + ", Total Item Count - " + totalItemCount);
            }

            @Override
            public void onScrollStateChanged(ListView tableDateView, ScrollState scrollState) {
                Toast_Utils.longToast(context, "Scroll State " + scrollState.getValue());
            }
        });

    }

}

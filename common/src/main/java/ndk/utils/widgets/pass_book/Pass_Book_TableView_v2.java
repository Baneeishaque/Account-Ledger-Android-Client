package ndk.utils.widgets.pass_book;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import ndk.utils.R;
import ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry_v2;


/**
 * An extension of the {@link SortableTableView} that handles {@link Pass_Book_Entry_v2}s.
 *
 * @author ISchwarz
 */
public class Pass_Book_TableView_v2 extends SortableTableView<Pass_Book_Entry_v2> {

    public Pass_Book_TableView_v2(final Context context) {
        this(context, null);
    }

    public Pass_Book_TableView_v2(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public Pass_Book_TableView_v2(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, "#", "Par.", "To.", "CAm.","DAm.", "Bal.");
        simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.table_header_text));
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven = ContextCompat.getColor(context, R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.table_data_row_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(6);
        tableColumnWeightModel.setColumnWeight(0, 3);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 2);
        tableColumnWeightModel.setColumnWeight(4, 2);
        tableColumnWeightModel.setColumnWeight(5, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, Pass_Book_TableView_Comparators_v2.get_Insertion_Date_Comparator());
        setColumnComparator(1, Pass_Book_TableView_Comparators_v2.get_Particulars_Comparator());
        setColumnComparator(2, Pass_Book_TableView_Comparators_v2.get_To_Account_Comparator());
        setColumnComparator(3, Pass_Book_TableView_Comparators_v2.get_Credit_Amount_Comparator());
        setColumnComparator(4, Pass_Book_TableView_Comparators_v2.get_Debit_Amount_Comparator());
        setColumnComparator(5, Pass_Book_TableView_Comparators_v2.get_Balance_Comparator());
    }

}

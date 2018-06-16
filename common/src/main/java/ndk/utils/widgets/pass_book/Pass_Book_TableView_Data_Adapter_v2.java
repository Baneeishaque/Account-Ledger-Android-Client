package ndk.utils.widgets.pass_book;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;
import ndk.utils.Date_Utils;
import ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry_v2;

import static android.graphics.Color.BLACK;

public class Pass_Book_TableView_Data_Adapter_v2 extends LongPressAwareTableDataAdapter<Pass_Book_Entry_v2> {

    private static final int TEXT_SIZE = 14;

    public Pass_Book_TableView_Data_Adapter_v2(final Context context, final List<Pass_Book_Entry_v2> data, final TableView<Pass_Book_Entry_v2> tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {

        final Pass_Book_Entry_v2 pass_book_entry_v2 = getRowData(rowIndex);

        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderString(Date_Utils.normal_date_time_short_year_format.format(pass_book_entry_v2.getInsertion_date()));
                break;
            case 1:
                renderedView = renderString(pass_book_entry_v2.getParticulars());
                break;
            case 2:
                renderedView = renderString(String.valueOf(pass_book_entry_v2.getSecond_account_name()));
                break;
            case 3:
                renderedView = renderString(String.valueOf(pass_book_entry_v2.getCredit_amount()));
                break;
            case 4:
                renderedView = renderString(String.valueOf(pass_book_entry_v2.getDebit_amount()));
                break;
            case 5:
                renderedView = renderString(String.valueOf(pass_book_entry_v2.getBalance()));
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {

        return getDefaultCellView(rowIndex, columnIndex, parentView);
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setTextColor(BLACK);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

}

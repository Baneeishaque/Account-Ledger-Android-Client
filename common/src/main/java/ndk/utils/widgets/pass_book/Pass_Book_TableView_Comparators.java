package ndk.utils.widgets.pass_book;

import java.util.Comparator;

import ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry;

/**
 * A collection of {@link Comparator}s for {@link Pass_Book_Entry} objects.
 *
 * @author ISchwarz
 */
final class Pass_Book_TableView_Comparators {

    private Pass_Book_TableView_Comparators() {
        //no instance
    }

    static Comparator<Pass_Book_Entry> get_Insertion_Date_Comparator() {
        return new Insertion_Date_Comparator();
    }

    static Comparator<Pass_Book_Entry> get_Particulars_Comparator() {
        return new Particulars_Comparator();
    }

    static Comparator<Pass_Book_Entry> get_Debit_Amount_Comparator() {
        return new Debit_Amount_Comparator();
    }

    static Comparator<Pass_Book_Entry> get_Credit_Amount_Comparator() {
        return new Credit_Amount_Comparator();
    }

    static Comparator<Pass_Book_Entry> get_Balance_Comparator() {
        return new Balance_Comparator();
    }

    private static class Insertion_Date_Comparator implements Comparator<Pass_Book_Entry> {

        @Override
        public int compare(final Pass_Book_Entry pass_book_entry1, final Pass_Book_Entry pass_book_entry2) {
            if (pass_book_entry1.getInsertion_date().before(pass_book_entry2.getInsertion_date()))
                return -1;
            if (pass_book_entry1.getInsertion_date().after(pass_book_entry2.getInsertion_date()))
                return 1;
            return 0;
        }
    }


    private static class Debit_Amount_Comparator implements Comparator<Pass_Book_Entry> {

        @Override
        public int compare(final Pass_Book_Entry pass_book_entry1, final Pass_Book_Entry pass_book_entry2) {
            if (pass_book_entry1.getDebit_amount() < pass_book_entry2.getDebit_amount())
                return -1;
            if (pass_book_entry1.getDebit_amount() > pass_book_entry2.getDebit_amount())
                return 1;
            return 0;
        }
    }

    private static class Credit_Amount_Comparator implements Comparator<Pass_Book_Entry> {

        @Override
        public int compare(final Pass_Book_Entry pass_book_entry1, final Pass_Book_Entry pass_book_entry2) {
            if (pass_book_entry1.getCredit_amount() < pass_book_entry2.getCredit_amount())
                return -1;
            if (pass_book_entry1.getCredit_amount() > pass_book_entry2.getCredit_amount())
                return 1;
            return 0;
        }
    }

    private static class Balance_Comparator implements Comparator<Pass_Book_Entry> {

        @Override
        public int compare(final Pass_Book_Entry pass_book_entry1, final Pass_Book_Entry pass_book_entry2) {
            if (pass_book_entry1.getBalance() < pass_book_entry2.getBalance())
                return -1;
            if (pass_book_entry1.getBalance() > pass_book_entry2.getBalance())
                return 1;
            return 0;
        }
    }

    private static class Particulars_Comparator implements Comparator<Pass_Book_Entry> {

        @Override
        public int compare(final Pass_Book_Entry pass_book_entry1, final Pass_Book_Entry pass_book_entry2) {
            return pass_book_entry1.getParticulars().compareTo(pass_book_entry2.getParticulars());
        }
    }


}

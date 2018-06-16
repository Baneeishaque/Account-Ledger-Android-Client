package ndk.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry;
import ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry_v2;
import ndk.utils.widgets.pass_book.Pass_Book_TableView;
import ndk.utils.widgets.pass_book.Pass_Book_TableView_Data_Adapter;
import ndk.utils.widgets.pass_book.Pass_Book_TableView_Data_Adapter_v2;
import ndk.utils.widgets.pass_book.Pass_Book_TableView_v2;

import static ndk.utils.Pdf_Utils.addEmptyLine;

/**
 * Created by Nabeel on 23-01-2018.
 */

public class Pass_Book_Utils {

    private static ArrayList<ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry> current_pass_book_entries;
    private static ArrayList<ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry_v2> current_pass_book_entries_v2;

    private static boolean v2_flag;

    public static void bind(Pass_Book_TableView pass_book_tableView, Context context, ArrayList<ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry> pass_book_entries) {
        if (pass_book_tableView != null) {
            final Pass_Book_TableView_Data_Adapter pass_book_tableView_data_adapter = new Pass_Book_TableView_Data_Adapter(context, pass_book_entries, pass_book_tableView);
            pass_book_tableView.setDataAdapter(pass_book_tableView_data_adapter);
            current_pass_book_entries = pass_book_entries;
            v2_flag = false;
        }
    }

    public static void bindv2(Pass_Book_TableView_v2 pass_book_tableView_v2, Context context, ArrayList<ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry_v2> pass_book_entries_v2) {
        if (pass_book_tableView_v2 != null) {
            final Pass_Book_TableView_Data_Adapter_v2 pass_book_tableView_data_adapter_v2 = new Pass_Book_TableView_Data_Adapter_v2(context, pass_book_entries_v2, pass_book_tableView_v2);
            pass_book_tableView_v2.setDataAdapter(pass_book_tableView_data_adapter_v2);

//            ListView internalListView=((AppCompatActivity)context).findViewById(pass_book_tableView_v2.getId());
//            internalListView.setSelection(pass_book_entries_v2.size()-1);

            pass_book_tableView_v2.setScrollY(pass_book_tableView_v2.getBottom());
            current_pass_book_entries_v2 = pass_book_entries_v2;
            v2_flag = true;
        }
    }

    static void email_Pass_Book(String application_name, String time_stamp, String email_subject, String email_text, File pass_book_pdf, Context context) {
        Intent email = new Intent(Intent.ACTION_SEND);
        if (email_subject.isEmpty() && email_text.isEmpty()) {
            Email_Utils.email_attachment(application_name + " : Pass Book : " + time_stamp, "See attachment...", pass_book_pdf, context);
        } else if (email_text.isEmpty()) {
            Email_Utils.email_attachment(email_subject, "See attachment...", pass_book_pdf, context);
        } else {
            Email_Utils.email_attachment(email_subject, email_text + "\nSee attachment...", pass_book_pdf, context);
        }
    }

    public static boolean create_Pass_Book_Pdf(String TAG, Context context, File pass_book_pdf, String application_name) {

        if (Folder_Utils.create_Documents_application_sub_folder(TAG, context, application_name)) {
            try {
                OutputStream output = new FileOutputStream(pass_book_pdf);

                //Step 1
                Document document = new Document(PageSize.A4);

                //Step 2
                PdfWriter.getInstance(document, output);

                //Step 3
                document.open();

                //Step 4 Add content

                Paragraph title = new Paragraph(application_name + ", Pass Book", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK));

                addEmptyLine(title, 1);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                PdfPTable table = new PdfPTable(5);

                PdfPCell c1 = new PdfPCell(new Phrase("Date"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                PdfPCell c2 = new PdfPCell(new Phrase("Particulars"));
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c2);

                if (v2_flag) {
                    PdfPCell c3 = new PdfPCell(new Phrase("To A/C"));
                    c3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c3);

                    PdfPCell c4 = new PdfPCell(new Phrase("Amount"));
                    c4.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c4);

                } else {

                    PdfPCell c3 = new PdfPCell(new Phrase("Debit"));
                    c3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c3);

                    PdfPCell c4 = new PdfPCell(new Phrase("Credit"));
                    c4.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(c4);

                }

                PdfPCell c5 = new PdfPCell(new Phrase("Balance"));
                c5.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c5);

//                    table.setHeaderRows(1);

//                    table.addCell("1.1");
//                    table.addCell("1.2");
//                    table.addCell("1.3");

                if (v2_flag) {
                    if (!current_pass_book_entries_v2.isEmpty()) {
                        for (Pass_Book_Entry_v2 pass_book_entry_v2 : current_pass_book_entries_v2) {
                            table.addCell(Date_Utils.normal_date_time_short_year_format.format(pass_book_entry_v2.getInsertion_date()));
                            table.addCell(pass_book_entry_v2.getParticulars());
                            table.addCell(String.valueOf(pass_book_entry_v2.getSecond_account_name()));
                            table.addCell(String.valueOf(pass_book_entry_v2.getCredit_amount()));
                            table.addCell(String.valueOf(pass_book_entry_v2.getBalance()));
                        }
                    }
                } else {
                    if (!current_pass_book_entries.isEmpty()) {
                        for (Pass_Book_Entry pass_book_entry : current_pass_book_entries) {
                            table.addCell(Date_Utils.normal_date_time_short_year_format.format(pass_book_entry.getInsertion_date()));
                            table.addCell(pass_book_entry.getParticulars());
                            table.addCell(String.valueOf(pass_book_entry.getDebit_amount()));
                            table.addCell(String.valueOf(pass_book_entry.getCredit_amount()));
                            table.addCell(String.valueOf(pass_book_entry.getBalance()));
                        }
                    }

                }

                document.add(table);

                //Step 5: Close the document
                document.close();
                return true;

            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
                Log.i(TAG, "Pdf Creation failure " + e.getLocalizedMessage());
                Toast_Utils.longToast(context, "Pdf fail");
            }
        }
        return false;
    }

}

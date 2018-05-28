package ndk.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ndk.utils.Pass_Book_Utils.email_Pass_Book;

/**
 * Created by Nabeel on 23-01-2018.
 */

public class Pdf_Utils {

    static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public static void prompt_For_Next_Action_After_Creation(final Context context, String dialog_Title, final File pass_book_pdf, final String application_name, final String time_stamp, final String email_subject, final String email_text) {
        final String[] options = {
                "Preview It",
                "Cancel"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Pass Book Saved, What Next?");
        builder.setTitle(dialog_Title);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (options[which]) {
                    case "Email It":
                        email_Pass_Book(application_name, time_stamp, email_subject, email_text, pass_book_pdf, context);
                        break;
                    case "Preview It":
                        viewPdf(pass_book_pdf, context);
                        break;
                    case "Cancel":
                        dialog.dismiss();
                        break;
                }
            }
        });

        builder.show();

    }

    private static void viewPdf(File pass_book_pdf, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(pass_book_pdf), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    boolean createPdf(String TAG, Context context) {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        boolean isPresent = true;
        if (!docsFolder.exists()) {
            isPresent = docsFolder.mkdir();
        }
        if (isPresent) {
            File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i(TAG, "Pdf Directory created");
            }

            //Create time stamp
            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

            File sample_pdf = new File(pdfFolder + "/" + timeStamp + ".pdf");


            try {
                OutputStream output = new FileOutputStream(sample_pdf);

                //Step 1
//                Document document = new Document();
                Document document = new Document(PageSize.A4, 50, 50, 50, 50);

                //Step 2
                PdfWriter.getInstance(document, output);
                //Step 3
                document.open();

                //Step 4 Add content

                Anchor anchorTarget = new Anchor("First page of the document.");
                anchorTarget.setName("BackToTop");
                Paragraph paragraph1 = new Paragraph();

                paragraph1.setSpacingBefore(50);

                paragraph1.add(anchorTarget);
                document.add(paragraph1);

                document.add(new Paragraph("Some more text on the first page with different color and font type.", FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, new CMYKColor(0, 255, 0, 0))));

                Paragraph title1 = new Paragraph("Chapter 1", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, new CMYKColor(0, 255, 255, 17)));

                Chapter chapter1 = new Chapter(title1, 1);

                chapter1.setNumberDepth(0);

                Paragraph title11 = new Paragraph("This is Section 1 in Chapter 1", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new CMYKColor(0, 255, 255, 17)));

                Section section1 = chapter1.addSection(title11);

                Paragraph someSectionText = new Paragraph("This text comes as part of section 1 of chapter 1.");

                section1.add(someSectionText);

                someSectionText = new Paragraph("Following is a 3 X 2 table.");

                section1.add(someSectionText);

                PdfPTable t = new PdfPTable(3);

                t.setSpacingBefore(25);

                t.setSpacingAfter(25);

                PdfPCell c1 = new PdfPCell(new Phrase("Header1"));

                t.addCell(c1);

                PdfPCell c2 = new PdfPCell(new Phrase("Header2"));

                t.addCell(c2);

                PdfPCell c3 = new PdfPCell(new Phrase("Header3"));

                t.addCell(c3);

                t.addCell("1.1");

                t.addCell("1.2");

                t.addCell("1.3");

                section1.add(t);

                com.itextpdf.text.List l = new com.itextpdf.text.List(true, false, 10);

                l.add(new ListItem("First item of list"));

                l.add(new ListItem("Second item of list"));

                section1.add(l);

//                Image image2 = null;
//                try {
//                    image2 = Image.getInstance("IBMLogo.bmp");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                image2.scaleAbsolute(120f, 120f);
//
//                section1.add(image2);

                Paragraph title2 = new Paragraph("Using Anchor",

                        FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,

                                new CMYKColor(0, 255, 0, 0)));

                section1.add(title2);

                title2.setSpacingBefore(5000);

                Anchor anchor2 = new Anchor("Back To Top");

                anchor2.setReference("#BackToTop");

                section1.add(anchor2);

                document.add(chapter1);

                document.add(new Paragraph("mSubjectEditText.getText().toString()"));
                document.add(new Paragraph("mBodyEditText.getText().toString()"));

                //Step 5: Close the document
                document.close();
                return true;

            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
                Log.i(TAG, "Pdf Creation failure " + e.getLocalizedMessage());
                Toast_Utils.longToast(context, "Pdf fail");
            }
        } else {
            Log.i(TAG, "Folder Creation failure ");
            Toast_Utils.longToast(context, "Folder fail");
        }
        return false;
    }

}

package ndk.utils.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import ndk.utils.R;
import ndk.utils.models.sortable_tableView.pass_book.Pass_Book_Entry_v2;
import ndk.utils.network_task.Load_Pass_Book_Task;
import ndk.utils.widgets.pass_book.Pass_Book_TableView;
import ndk.utils.widgets.pass_book.Pass_Book_TableView_v2;

import static ndk.utils.Pass_Book_Utils.create_Pass_Book_Pdf;
import static ndk.utils.Pdf_Utils.prompt_For_Next_Action_After_Creation;
import static ndk.utils.ProgressBar_Utils.showProgress;

//TODO: Use new code structure
//TODO: Adjust the width of Pass Book fields in pdf

public abstract class Pass_Book_Bundle extends AppCompatActivity {

    private ProgressBar mProgressView;
    private Pass_Book_TableView pass_book_tableView;
    private Pass_Book_TableView_v2 pass_book_tableView_v2;
    private Load_Pass_Book_Task load_pass_Book_task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra("V2_FLAG") == null) {
            setContentView(R.layout.pass_book);
        } else {
            setContentView(R.layout.pass_book_v2);
        }

        initView();

        if (load_pass_Book_task != null) {
            load_pass_Book_task.cancel(true);
            load_pass_Book_task = null;
        }

        if (getIntent().getStringExtra("V2_FLAG") == null) {

            showProgress(true, this, mProgressView, pass_book_tableView);
            load_pass_Book_task = new Load_Pass_Book_Task(getIntent().getStringExtra("URL"), this, mProgressView, pass_book_tableView, getIntent().getStringExtra("application_name"), pass_book_tableView, new Pair[]{new Pair<>("user_id", getIntent().getStringExtra("user_id"))});

        } else {

            showProgress(true, this, mProgressView, pass_book_tableView_v2);
            load_pass_Book_task = new Load_Pass_Book_Task(getIntent().getStringExtra("URL"), this, mProgressView, pass_book_tableView_v2, getIntent().getStringExtra("application_name"), pass_book_tableView_v2, getIntent().getStringExtra("V2_FLAG"));


            pass_book_tableView_v2.SetOnRowLongClickListener(new Pass_Book_TableView_v2.OnRowLongClickListener() {
                @Override
                public void onRowLongClick(Pass_Book_Entry_v2 clickedData) {
                    Log.d(getIntent().getStringExtra("application_name"), "From Activity : " + clickedData.toString());
                    configure_ROW_LONG_CLICK_ACTIONS(clickedData);
                }
            });
        }

        load_pass_Book_task.execute();

    }

    protected abstract void configure_ROW_LONG_CLICK_ACTIONS(Pass_Book_Entry_v2 clickedData);

    private void initView() {
        mProgressView = findViewById(R.id.login_progress);
        if (getIntent().getStringExtra("V2_FLAG") == null) {
            pass_book_tableView = findViewById(R.id.tableView);
        } else {
            pass_book_tableView_v2 = findViewById(R.id.tableView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pass_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        String time_stamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
        File pass_book_pdf = new File(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), getIntent().getStringExtra("application_name")) + "/Pass_Book_" + time_stamp + ".pdf");

        if (id == R.id.menu_item_save) {
            if (create_Pass_Book_Pdf(getIntent().getStringExtra("application_name"), this, pass_book_pdf, getIntent().getStringExtra("application_name"))) {
                prompt_For_Next_Action_After_Creation(this, "Pass Book Saved, What Next?", pass_book_pdf, getIntent().getStringExtra("application_name"), time_stamp, "", "");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

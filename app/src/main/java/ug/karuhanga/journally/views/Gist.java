package ug.karuhanga.journally.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import ug.karuhanga.journally.R;
import ug.karuhanga.journally.controllers.GistController;
import ug.karuhanga.journally.controllers.GistControllerDash;
import ug.karuhanga.journally.models.Entry;
import ug.karuhanga.journally.utils.Adapter;
import ug.karuhanga.journally.utils.EntryInteractionListener;

@EActivity(R.layout.activity_gist)
public class Gist extends AppCompatActivity implements GistControllerDash, EntryInteractionListener {

    @ViewById(R.id.fab_gist)
    FloatingActionButton fabGist;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.entry_list)
    RecyclerView recyclerViewEntryList;

    Adapter adapter;

    GistExternalInterface controller;
    private ItemTouchHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        adapter = null;
        setSupportActionBar(toolbar);
        setController(new GistController(this));
        recyclerViewEntryList.setLayoutManager(new LinearLayoutManager(this));
        fetchData();
    }

    void fetchData() {
        getController().fetchData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Click(R.id.fab_gist)
    void onFabClick() {
        addEntry();
    }

    private void addEntry() {
        EntryEditor.launch(this);
    }

    public GistExternalInterface getController() {
        if (controller == null) {
            return new GistController(this);
        }
        return controller;
    }

    public void setController(GistExternalInterface controller) {
        this.controller = controller;
    }

    @Override
    public Context requestContext() {
        return this;
    }

    @Override
    @UiThread
    public void mNotifyItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void share(Entry entry) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, entry.sendify());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    @UiThread
    public void onDataReady(List<Entry> values) {
        adapter = new Adapter(values, this);
        recyclerViewEntryList.setAdapter(null);
        recyclerViewEntryList.setAdapter(adapter);

        // Setup swipe actions
        // Setup onItemTouchHandler to enable drag and drop , swipe left or right
        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(adapter, false, true, true);
        if (helper != null) {
            helper.attachToRecyclerView(null);
        }
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerViewEntryList);
    }

    @Override
    public void onEntryClicked(long pk) {
        EntryEditor.launch(this, pk);
    }

    @Override
    public void areYouSure(final Entry entry, final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        completeDelete(entry, position);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        adapter.refreshView(position);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?").setMessage("Delete " + entry.toString()).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Background
    void completeDelete(Entry entry, int position) {
        adapter.completeDelete(entry, position);
    }

    public interface GistExternalInterface {
        void fetchData();
    }

}

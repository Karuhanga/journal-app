package ug.karuhanga.journally.views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import ug.karuhanga.journally.R;
import ug.karuhanga.journally.controllers.GistController;
import ug.karuhanga.journally.controllers.GistControllerDash;
import ug.karuhanga.journally.models.Entry;
import ug.karuhanga.journally.utils.Adapter;
import ug.karuhanga.journally.utils.OnEntryClickListener;

import static ug.karuhanga.journally.utils.Utils.log;

@EActivity(R.layout.activity_gist)
public class Gist extends AppCompatActivity implements GistControllerDash, OnEntryClickListener {

    @ViewById(R.id.fab_gist)
    FloatingActionButton fabGist;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.entry_list)
    RecyclerView recyclerViewEntryList;

    Adapter adapter;

    GistExternalInterface controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
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
    public void onDataReady(List<Entry> values) {
        adapter = new Adapter(values, this);
        recyclerViewEntryList.setAdapter(adapter);
        log(values);
    }

    @Override
    public void onEntryClicked(long pk) {
        EntryEditor.launch(this, pk);
    }

    public interface GistExternalInterface {
        void fetchData();
    }

}

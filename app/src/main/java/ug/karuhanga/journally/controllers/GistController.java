package ug.karuhanga.journally.controllers;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

import ug.karuhanga.journally.models.Database;
import ug.karuhanga.journally.models.Entry;
import ug.karuhanga.journally.views.Gist;

public class GistController implements Gist.GistExternalInterface, Observer<List<Entry>> {

    GistControllerDash dashboard;
    LiveData<List<Entry>> entryLiveData;

    public GistController(GistControllerDash dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public void fetchData() {
        entryLiveData = Database.getDb(dashboard.requestContext()).getEntryDao().getEntries();
        entryLiveData.observe((LifecycleOwner) dashboard, this);
    }

    private void onDataReady(List<Entry> values) {
        dashboard.onDataReady(values);
    }

    @Override
    public void onChanged(@Nullable List<Entry> entries) {
        onDataReady(entries);
    }
}

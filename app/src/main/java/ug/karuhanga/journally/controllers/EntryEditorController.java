package ug.karuhanga.journally.controllers;

import ug.karuhanga.journally.models.Database;
import ug.karuhanga.journally.models.Entry;
import ug.karuhanga.journally.views.EntryEditor;

public class EntryEditorController implements EntryEditor.EntryEditorController {

    private EditorControllerDash dashboard;
    private boolean isNew;
    private Entry entry;

    public EntryEditorController(EditorControllerDash dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public void fetchData() {
        if (isNew) {
            setEntry(new Entry("Journal Entry", ""));
            onNewDataReady();
        } else {
            getEntry(dashboard.getPk());
            onDataReady();
        }
    }

    private void onNewDataReady() {
        dashboard.onNewDataReady(getEntry());
    }

    private void onDataReady() {
        dashboard.onDataReady(getEntry());
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        if (entry == null) {
            setEntry(new Entry("Journal Entry", ""));
            return;
        }
        this.entry = entry;
    }

    private void getEntry(long pk) {
        setEntry(Database.getDb(dashboard.requestContext()).getEntryDao().getEntry(pk));
    }

    @Override
    public void setStatus(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public void save(String title, String text) {
        getEntry().setTitle(title).setText(text).save(dashboard.requestContext());
    }
}

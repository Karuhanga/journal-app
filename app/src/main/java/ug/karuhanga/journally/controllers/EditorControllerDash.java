package ug.karuhanga.journally.controllers;

import android.content.Context;

import ug.karuhanga.journally.models.Entry;

public interface EditorControllerDash {
    long getPk();

    Context requestContext();

    void onDataReady(Entry entry);

    void onNewDataReady(Entry entry);
}

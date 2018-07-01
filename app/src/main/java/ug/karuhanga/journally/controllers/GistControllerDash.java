package ug.karuhanga.journally.controllers;

import android.content.Context;

import java.util.List;

import ug.karuhanga.journally.models.Entry;

public interface GistControllerDash {
    Context requestContext();

    void onDataReady(List<Entry> values);
}

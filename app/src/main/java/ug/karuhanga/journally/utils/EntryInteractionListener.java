package ug.karuhanga.journally.utils;

import android.content.Context;

import ug.karuhanga.journally.models.Entry;

public interface EntryInteractionListener {

    void onEntryClicked(long pk);

    void areYouSure(Entry entry, int position);

    Context requestContext();

    void mNotifyItemRemoved(int position);

    void share(Entry entry);
}

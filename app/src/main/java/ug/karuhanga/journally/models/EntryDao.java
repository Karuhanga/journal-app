package ug.karuhanga.journally.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
import static ug.karuhanga.journally.models.Entry.PK;
import static ug.karuhanga.journally.models.Entry.TABLE_ENTRY;

@Dao
public interface EntryDao {
    @Insert(onConflict = REPLACE)
    void insertEntry(Entry entry);

    @Update
    void updateEntry(Entry entry);

    @Delete
    void deleteEntry(Entry entry);

    @Query("SELECT * FROM " + TABLE_ENTRY + " WHERE " + PK + " = :pk")
    Entry getEntry(long pk);

    @Query("SELECT * FROM " + TABLE_ENTRY)
    LiveData<List<Entry>> getEntries();

    @Query("SELECT * FROM " + TABLE_ENTRY)
    List<Entry> getRawEntries();
}

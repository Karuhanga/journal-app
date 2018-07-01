package ug.karuhanga.journally.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import static ug.karuhanga.journally.models.Entry.TABLE_ENTRY;

@Entity(tableName = TABLE_ENTRY)
public class Entry {
    public static final String TEXT = "text";
    public static final String TITLE = "title";
    public static final String PK = "pk";
    public static final String TABLE_ENTRY = "entries";
    @ColumnInfo(name = PK)
    @PrimaryKey(autoGenerate = true)
    private long pk;
    @ColumnInfo(name = TITLE)
    private String title;
    @ColumnInfo(name = TEXT)
    private String text;

    public Entry(long pk, String title, String text) {
        this.pk = pk;
        this.title = title;
        this.text = text;
    }

    @Ignore
    public Entry(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public long getPk() {
        return pk;
    }

    public String getTitle() {
        return title;
    }

    public Entry setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getText() {
        return text;
    }

    public Entry setText(String text) {
        this.text = text;
        return this;
    }

    public String toString() {
        return this.title;
    }

    public void save(Context context) {
        Database.getDb(context).getEntryDao().insertEntry(this);
    }

    public String sendify() {
        // TODO
        return toString();
    }
}

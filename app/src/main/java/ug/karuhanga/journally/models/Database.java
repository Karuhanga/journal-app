package ug.karuhanga.journally.models;


import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@android.arch.persistence.room.Database(entities = {Entry.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    private static volatile Database dbInstance;
    private static String DATABASE_NAME = "journally_db";

    public static synchronized Database getDb(Context context) {
        if (dbInstance == null) {
            dbInstance = createDb(context);
        }
        return dbInstance;
    }

    private static Database createDb(final Context context) {
        return Room.databaseBuilder(context, Database.class, DATABASE_NAME).build();
    }

    public abstract EntryDao getEntryDao();
}
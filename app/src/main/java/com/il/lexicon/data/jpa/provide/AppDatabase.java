package com.il.lexicon.data.jpa.provide;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.il.lexicon.data.jpa.convert.*;
import com.il.lexicon.data.jpa.entity.Word;

@Database(
    entities = {
        Word.class,
    },
    version = 1
)
@TypeConverters({
    DateConverter.class,
    StringArrayConverter.class,
    BigDecimalConverter.class,
})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "il_vcb";
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null || !instance.isOpen()) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .build();
        }
        return instance;
    }

    public abstract WordDao getWordDao();

}

package com.il.vcb.data.jpa.provide;

import android.content.Context;
import androidx.room.*;
import com.il.vcb.data.jpa.convert.*;
import com.il.vcb.data.jpa.entity.Word;

@Database(
    entities = {
        Word.class,
    },
//    autoMigrations = {
//        @AutoMigration(from = 1, to = 2),
//    },
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
            instance = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
        }
        return instance;
    }

    public abstract WordDao getWordDao();
}
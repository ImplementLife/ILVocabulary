package com.il.vcb.data.jpa.provide;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.il.vcb.data.jpa.convert.*;
import com.il.vcb.data.jpa.entity.Word;

@Database(
    entities = {
        Word.class,
    },
    version = 2
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
                .addMigrations(MIGRATION_1_2)
                .build();
        }
        return instance;
    }

    public abstract WordDao getWordDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE il_word RENAME COLUMN mistakes TO countMistakes");
        }
    };
}

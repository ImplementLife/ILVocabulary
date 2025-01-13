package com.il.vcb.data.jpa.provide;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;
import com.il.vcb.data.jpa.entity.Word;

import java.util.List;

public interface RestDao<K, T extends WithId<K>> {
    @Insert
    void insert(T t);
    @Insert
    void insertAll(List<Word> words);
    @Update
    void update(T t);
    @Delete
    void delete(T t);
}

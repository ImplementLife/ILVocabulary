package com.il.vcb.data.jpa.provide;

import androidx.room.Dao;
import androidx.room.Query;
import com.il.vcb.data.jpa.entity.Word;

import java.util.List;

@Dao
public interface WordDao extends RestDao<Integer, Word> {
    @Query("SELECT * FROM il_word WHERE id = :id")
    Word findById(int id);

    @Query("DELETE FROM il_word WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM il_word")
    List<Word> getAll();

    @Query("SELECT COUNT(*) FROM il_word")
    int getCount();

    @Query("SELECT * FROM il_word ORDER BY countCompleteRepeats ASC LIMIT 10")
    List<Word> getSomeByCountCompleteRepeatsDesc();
}

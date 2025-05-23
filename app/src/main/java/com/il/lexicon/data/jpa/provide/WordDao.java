package com.il.lexicon.data.jpa.provide;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import com.il.lexicon.data.jpa.entity.Word;

import java.util.List;

@Dao
public interface WordDao extends RestDao<Integer, Word> {
    @Query("SELECT * FROM il_word WHERE id = :id")
    Word findById(int id);

    @Query("DELETE FROM il_word WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM il_word")
    List<Word> getAll();

    @Query("SELECT * FROM il_word WHERE learnLangWord LIKE '%' || :string || '%' OR nativeLangWord LIKE '%' || :string || '%' ORDER BY learnLangWord ASC")
    List<Word> search(String string);

    @Query("SELECT COUNT(*) FROM il_word")
    int getCount();

    @Query("SELECT COUNT(*) FROM il_word WHERE countCompleteRepeats > :repeatsCount")
    int getCount(int repeatsCount);

    @Query("DELETE FROM il_word")
    void deleteAll();

    @Query("SELECT * FROM il_word WHERE countMistakes > 0 ORDER BY RANDOM() LIMIT :limit")
    List<Word> getRandomWithCountMistakesMoreThanZero(int limit);

    @Query("SELECT * FROM il_word WHERE countCompleteRepeats = 0 ORDER BY RANDOM() LIMIT :limit")
    List<Word> getRandomWithZeroRepeats(int limit);

    @Query("SELECT * FROM il_word WHERE countCompleteRepeats >= 1 AND countCompleteRepeats <= :repeatsCount ORDER BY RANDOM() LIMIT :limit")
    List<Word> getRandomWithLimitedRepeats(int repeatsCount, int limit);

    @Query("SELECT * FROM il_word ORDER BY RANDOM() LIMIT :limit")
    List<Word> getRandom(int limit);

    @Query("SELECT * FROM il_word WHERE id NOT IN (:excludeIds) ORDER BY RANDOM() LIMIT :limit")
    List<Word> getRandomExclude(int limit, List<Integer> excludeIds);

    @Query("SELECT * FROM il_word WHERE id IN " +
        "(SELECT id FROM il_word WHERE countCompleteRepeats <= :repeatsCount " +
        "ORDER BY addDate DESC LIMIT 10) " +
        "ORDER BY RANDOM() LIMIT :limit")
    List<Word> getRandomLatestWords(int repeatsCount, int limit);


    @Query("DELETE FROM il_word")
    void clearTable();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'il_word'")
    void resetIdSequence();

    @Transaction
    default void resetTable() {
        clearTable();
        resetIdSequence();
    }
}

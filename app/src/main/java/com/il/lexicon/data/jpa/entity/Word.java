package com.il.lexicon.data.jpa.entity;

import androidx.room.Entity;

import java.util.Date;

@Entity(tableName = "il_word")
public class Word extends EntityWithId {
    private String learnLangWord;
    private String nativeLangWord;
    private int countCompleteRepeats;
    private int countMistakes;
    private Date addDate;

    public Word() {
    }

    public String getLearnLangWord() {
        return learnLangWord;
    }

    public void setLearnLangWord(String learnLangWord) {
        this.learnLangWord = learnLangWord;
    }

    public String getNativeLangWord() {
        return nativeLangWord;
    }

    public void setNativeLangWord(String nativeLangWord) {
        this.nativeLangWord = nativeLangWord;
    }

    public int getCountCompleteRepeats() {
        return countCompleteRepeats;
    }

    public void setCountCompleteRepeats(int countCompleteRepeats) {
        this.countCompleteRepeats = countCompleteRepeats;
    }

    public int getCountMistakes() {
        return countMistakes;
    }

    public void setCountMistakes(int countMistakes) {
        this.countMistakes = countMistakes;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }
}

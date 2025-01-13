package com.il.vcb.data.jpa.entity;

import androidx.room.Entity;

@Entity(tableName = "il_word")
public class Word extends EntityWithId {
    private String learnLangWord;
    private String nativeLangWord;
    private int countCompleteRepeats;
    private int mistakes;

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

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }
}

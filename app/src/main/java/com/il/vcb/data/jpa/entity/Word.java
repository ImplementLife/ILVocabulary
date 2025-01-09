package com.il.vcb.data.jpa.entity;

import androidx.room.Entity;

@Entity(tableName = "il_word")
public class Word extends EntityWithId {
    private String learnLangWord;
    private String nativeLangWord;
    private int completeRepeats;
    private int mistakes;

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

    public int getCompleteRepeats() {
        return completeRepeats;
    }

    public void setCompleteRepeats(int completeRepeats) {
        this.completeRepeats = completeRepeats;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }
}

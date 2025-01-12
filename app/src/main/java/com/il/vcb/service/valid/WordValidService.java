package com.il.vcb.service.valid;

import com.il.vcb.data.jpa.entity.Word;

public class WordValidService {
    public static boolean isValid(Word word) {
        return word.getLearnLangWord().trim().length() > 0
            && word.getNativeLangWord().trim().length() > 0
            && word.getCountCompleteRepeats() >= 0
            && word.getMistakes() >= 0;
    }
}

package com.il.vcb.service;

import com.il.vcb.data.jpa.entity.Word;
import com.il.vcb.data.jpa.provide.AppDatabase;
import com.il.vcb.data.jpa.provide.WordDao;
import com.il.vcb.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WordLearnLevelService {
    private static final WordDao wordDao = AppDatabase.getInstance(MainActivity.getInstance()).getWordDao();

    public List<Word> getLevelWords() {
        int levelSize = 10;
        Set<Word> set = new HashSet<>();
        List<Word> mistakes = wordDao.getRandomWithCountMistakesMoreThanZero(2);
        List<Word> zeroRepeats = wordDao.getRandomWithZeroRepeats(2);

        set.addAll(mistakes);
        set.addAll(zeroRepeats);

        List<Word> limitedRepeats = wordDao.getRandomWithLimitedRepeats(levelSize - set.size());
        set.addAll(limitedRepeats);
        if (set.size() < levelSize) {
            List<Integer> excludeIdList = set.stream().map(Word::getId).collect(Collectors.toList());
            List<Word> randomExclude = wordDao.getRandomExclude(levelSize - set.size(), excludeIdList);
            set.addAll(randomExclude);
        }

        return new ArrayList<>(set);
    }

}

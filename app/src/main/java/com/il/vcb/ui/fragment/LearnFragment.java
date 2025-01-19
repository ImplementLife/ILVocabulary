package com.il.vcb.ui.fragment;

import com.il.vcb.R;
import com.il.vcb.data.jpa.entity.Word;
import com.il.vcb.data.jpa.provide.AppDatabase;
import com.il.vcb.data.jpa.provide.WordDao;
import com.il.vcb.service.WordLearnLevelService;
import com.il.vcb.ui.activity.MainActivity;
import com.il.vcb.ui.custom.component.BaseFragment;
import com.il.vcb.ui.custom.component.CustomRecyclerView;
import com.il.vcb.ui.view.WordBtnView;

import java.util.*;
import java.util.stream.Collectors;

public class LearnFragment extends BaseFragment {
    private static final WordDao wordDao = AppDatabase.getInstance(MainActivity.getInstance()).getWordDao();
    private static final WordLearnLevelService wordLearnLevelService = new WordLearnLevelService();

    private List<Word> words;
    private List<ButtonWrapper> buttonWrappers = new ArrayList<>();
    private Map<Integer, ButtonWrapper> buttonWrappersMap = new HashMap<>();
    private ButtonWrapper lastSelectedWrapper = null;

    public LearnFragment() {
        super(R.layout.fragment_learn);
    }

    @Override
    public void init() {
        CustomRecyclerView learnColumn = findViewById(R.id.learnColumn);
        CustomRecyclerView nativeColumn = findViewById(R.id.nativeColumn);

        runAsync(() -> {
            loadWords();
            prepareButtonWrappers();
            post(() -> {
                Collections.shuffle(buttonWrappers);
                List<WordBtnView> learnButtons = buttonWrappers.stream()
                    .map(e -> e.learnButton)
                    .collect(Collectors.toList());
                learnColumn.addAll(learnButtons);

                Collections.shuffle(buttonWrappers);

                List<WordBtnView> nativeButtons = buttonWrappers.stream()
                    .map(e -> e.nativeButton)
                    .collect(Collectors.toList());
                nativeColumn.addAll(nativeButtons);
            });
        });
    }

    private void loadWords() {
        words = wordLearnLevelService.getLevelWords();
    }

    private void prepareButtonWrappers() {
        for (int i = 0; i < words.size(); i++) {
            Word word = words.get(i);
            ButtonWrapper wrapper = new ButtonWrapper();

            WordBtnView learnButton = new WordBtnView();
            learnButton.setText(word.getLearnLangWord());
            learnButton.setOnClickListener(() -> handleButtonClick(wrapper, learnButton));
            wrapper.learnButton = learnButton;

            WordBtnView nativeButton = new WordBtnView();
            nativeButton.setText(word.getNativeLangWord());
            nativeButton.setOnClickListener(() -> handleButtonClick(wrapper, nativeButton));
            wrapper.nativeButton = nativeButton;

            wrapper.index = i;
            wrapper.word = word;

            buttonWrappers.add(wrapper);
            buttonWrappersMap.put(wrapper.index, wrapper);
        }
    }

    private void handleButtonClick(ButtonWrapper wrapper, WordBtnView button) {
        if (lastSelectedWrapper == null) {
            lastSelectedWrapper = wrapper;
            button.setEnabled(false);
        } else {
            if (lastSelectedWrapper.index == wrapper.index) {
                button.setEnabled(false);
                int countCompleteRepeats = wrapper.word.getCountCompleteRepeats();
                wrapper.word.setCountCompleteRepeats(countCompleteRepeats + 1);
                int countMistakes = wrapper.word.getCountMistakes();
                if (countMistakes > 0) {
                    wrapper.word.setCountMistakes(countMistakes - 1);
                }
            } else {
                wrapper = buttonWrappersMap.get(lastSelectedWrapper.index);
                wrapper.learnButton.setEnabled(true);
                wrapper.nativeButton.setEnabled(true);

                int mistakes = wrapper.word.getCountMistakes();
                wrapper.word.setCountMistakes(mistakes + 1);
            }
            ButtonWrapper finalWrapper = wrapper;
            runAsync(() -> {
                wordDao.update(finalWrapper.word);
            });
            lastSelectedWrapper = null;
        }
    }

    private static class ButtonWrapper {
        WordBtnView learnButton;
        WordBtnView nativeButton;
        Word word;
        int index;
    }
}

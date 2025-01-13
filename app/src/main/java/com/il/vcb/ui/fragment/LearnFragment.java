package com.il.vcb.ui.fragment;

import android.widget.Button;
import android.widget.LinearLayout;

import com.il.vcb.R;
import com.il.vcb.data.jpa.entity.Word;
import com.il.vcb.data.jpa.provide.AppDatabase;
import com.il.vcb.data.jpa.provide.WordDao;
import com.il.vcb.service.WordLearnLevelService;
import com.il.vcb.ui.activity.MainActivity;
import com.il.vcb.ui.custom.component.BaseFragment;

import java.util.*;

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
        LinearLayout learnColumn = findViewById(R.id.learnColumn);
        LinearLayout nativeColumn = findViewById(R.id.nativeColumn);

        runAsync(() -> {
            loadWords();
            prepareButtonWrappers();
            post(() -> {
                Collections.shuffle(buttonWrappers);
                for (ButtonWrapper wrapper : buttonWrappers) {
                    learnColumn.addView(wrapper.learnButton);
                }

                Collections.shuffle(buttonWrappers);
                for (ButtonWrapper wrapper : buttonWrappers) {
                    nativeColumn.addView(wrapper.nativeButton);
                }
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

            Button learnButton = new Button(getContext());
            learnButton.setText(word.getLearnLangWord());
            learnButton.setOnClickListener(v -> handleButtonClick(wrapper, learnButton));
            wrapper.learnButton = learnButton;

            Button nativeButton = new Button(getContext());
            nativeButton.setText(word.getNativeLangWord());
            nativeButton.setOnClickListener(v -> handleButtonClick(wrapper, nativeButton));
            wrapper.nativeButton = nativeButton;

            wrapper.index = i;
            wrapper.word = word;

            buttonWrappers.add(wrapper);
            buttonWrappersMap.put(wrapper.index, wrapper);
        }
    }

    private void handleButtonClick(ButtonWrapper wrapper, Button button) {
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
        Button learnButton;
        Button nativeButton;
        Word word;
        int index;
    }
}

package com.il.lexicon.ui.fragment;

import com.il.lexicon.R;
import com.il.lexicon.data.jpa.entity.Word;
import com.il.lexicon.data.jpa.provide.AppDatabase;
import com.il.lexicon.data.jpa.provide.WordDao;
import com.il.lexicon.service.WordLearnLevelService;
import com.il.lexicon.ui.activity.MainActivity;
import com.il.lexicon.ui.custom.component.BaseFragment;
import com.il.lexicon.ui.custom.component.CustomRecyclerView;
import com.il.lexicon.ui.view.WordBtnView;

import java.util.*;
import java.util.stream.Collectors;

public class LearnPairsFragment extends BaseFragment {
    private static final WordDao wordDao = AppDatabase.getInstance(MainActivity.getInstance()).getWordDao();
    private static final WordLearnLevelService wordLearnLevelService = new WordLearnLevelService();

    private List<Word> words;
    private List<ButtonWrapper> buttonWrappers = new ArrayList<>();
    private Map<Integer, ButtonWrapper> buttonWrappersMap = new HashMap<>();
    private ButtonWrapper lastSelectedWrapper = null;
    private boolean isLearnLangColumnButtonSelected;

    public LearnPairsFragment() {
        super(R.layout.fragment_learn_pairs);
    }

    @Override
    public void init() {
        CustomRecyclerView learnColumn = findViewById(R.id.learnColumn);
        CustomRecyclerView nativeColumn = findViewById(R.id.nativeColumn);

        runAsync(() -> {
            loadWords();
            prepareButtonWrappers();

            Collections.shuffle(buttonWrappers);
            List<WordBtnView> learnButtons = buttonWrappers.stream()
                .map(ButtonWrapper::getLearnButton)
                .collect(Collectors.toList());

            Collections.shuffle(buttonWrappers);
            List<WordBtnView> nativeButtons = buttonWrappers.stream()
                .map(ButtonWrapper::getNativeButton)
                .collect(Collectors.toList());

            postDelayed(() -> {
                learnColumn.addAll(learnButtons);
                nativeColumn.addAll(nativeButtons);
            }, 500);
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
            learnButton.setOnClickListener(() -> handleButtonClick(wrapper, learnButton, true));
            wrapper.setLearnButton(learnButton);

            WordBtnView nativeButton = new WordBtnView();
            nativeButton.setText(word.getNativeLangWord());
            nativeButton.setOnClickListener(() -> handleButtonClick(wrapper, nativeButton, false));
            wrapper.setNativeButton(nativeButton);

            wrapper.setIndex(i);
            wrapper.setWord(word);

            buttonWrappers.add(wrapper);
            buttonWrappersMap.put(wrapper.getIndex(), wrapper);
        }
    }


    private void handleButtonClick(ButtonWrapper wrapper, WordBtnView button, boolean isLearnLangColumn) {
        if (lastSelectedWrapper == null) {
            lastSelectedWrapper = wrapper;
            button.setStatusSelected();
            isLearnLangColumnButtonSelected = isLearnLangColumn;
        } else {
            if (isLearnLangColumnButtonSelected == isLearnLangColumn) {
                lastSelectedWrapper.getLearnButton().setStatusDefault();
                lastSelectedWrapper.getNativeButton().setStatusDefault();
                lastSelectedWrapper = null;
                handleButtonClick(wrapper, button, isLearnLangColumn);
                return;
            }
            if (lastSelectedWrapper.getIndex() == wrapper.getIndex()) {
                lastSelectedWrapper.getLearnButton().setStatusComplete();
                lastSelectedWrapper.getNativeButton().setStatusComplete();
                int countCompleteRepeats = wrapper.getWord().getCountCompleteRepeats();
                wrapper.getWord().setCountCompleteRepeats(countCompleteRepeats + 1);
                int countMistakes = wrapper.getWord().getCountMistakes();
                if (countMistakes > 0) {
                    wrapper.getWord().setCountMistakes(countMistakes - 1);
                }
                final ButtonWrapper w1 = wrapper;
                runAsync(() -> wordDao.update(w1.getWord()));
            } else {
                wrapper.getLearnButton().setStatusMistake();
                wrapper.getNativeButton().setStatusMistake();

                lastSelectedWrapper.getLearnButton().setStatusMistake();
                lastSelectedWrapper.getNativeButton().setStatusMistake();
                final ButtonWrapper w1 = wrapper;
                final ButtonWrapper w2 = lastSelectedWrapper;
                postDelayed(() -> {
                    w1.getLearnButton().setStatusDefault();
                    w1.getNativeButton().setStatusDefault();

                    w2.getLearnButton().setStatusDefault();
                    w2.getNativeButton().setStatusDefault();
                }, 700);

                int mistakes = wrapper.getWord().getCountMistakes();
                wrapper.getWord().setCountMistakes(mistakes + 1);
                runAsync(() -> {
                    wordDao.update(w1.getWord());
                    wordDao.update(w2.getWord());
                });
            }
            lastSelectedWrapper = null;
        }
    }

    private static class ButtonWrapper {
        private WordBtnView learnButton;
        private WordBtnView nativeButton;
        private Word word;
        private int index;

        public WordBtnView getNativeButton() {
            return nativeButton;
        }
        public void setNativeButton(WordBtnView nativeButton) {
            this.nativeButton = nativeButton;
        }

        public WordBtnView getLearnButton() {
            return learnButton;
        }
        public void setLearnButton(WordBtnView learnButton) {
            this.learnButton = learnButton;
        }

        public Word getWord() {
            return word;
        }

        public void setWord(Word word) {
            this.word = word;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}

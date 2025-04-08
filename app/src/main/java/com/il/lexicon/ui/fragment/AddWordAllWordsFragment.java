package com.il.lexicon.ui.fragment;

import com.il.lexicon.R;
import com.il.lexicon.data.jpa.entity.Word;
import com.il.lexicon.data.jpa.provide.AppDatabase;
import com.il.lexicon.data.jpa.provide.WordDao;
import com.il.lexicon.ui.custom.component.CustomRecyclerView;
import com.il.lexicon.ui.custom.component.NavFragment;
import com.il.lexicon.ui.view.AddWordAllWordsListItemView;

import java.util.List;
import java.util.stream.Collectors;

public class AddWordAllWordsFragment extends NavFragment {
    private WordDao wordDao;
    public AddWordAllWordsFragment() {
        super(R.layout.fragment__add_word__all_words);
    }

    @Override
    protected void init() {
        wordDao = AppDatabase.getInstance(getContext()).getWordDao();
        CustomRecyclerView crwList = findViewById(R.id.crw_list);
        runAsync(() -> {
            List<Word> all = wordDao.getAll();
            List<AddWordAllWordsListItemView> binders = all.stream()
                .map(e -> {
                    AddWordAllWordsListItemView addWordAllWordsListItemView = new AddWordAllWordsListItemView();
                    addWordAllWordsListItemView.setData(e);
                    return addWordAllWordsListItemView;
                })
                .collect(Collectors.toList());
            post(() -> {
                crwList.addAll(binders);
            });
        });

    }
}
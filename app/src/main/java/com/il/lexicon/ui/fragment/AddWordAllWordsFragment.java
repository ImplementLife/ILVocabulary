package com.il.lexicon.ui.fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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
    private CustomRecyclerView crwList;

    public AddWordAllWordsFragment() {
        super(R.layout.fragment__add_word__all_words);
    }

    private void search(String searchString) {
        runAsync(() -> {
            List<Word> all = wordDao.search(searchString);
            List<AddWordAllWordsListItemView> binders = all.stream()
                .map(e -> {
                    AddWordAllWordsListItemView addWordAllWordsListItemView = new AddWordAllWordsListItemView();
                    addWordAllWordsListItemView.setData(e);
                    addWordAllWordsListItemView.setNavigate(navController);
                    return addWordAllWordsListItemView;
                })
                .collect(Collectors.toList());
            post(() -> {
                crwList.replaceAll(binders);
                crwList.scrollToPosition(0);
            });
        });
    }

    @Override
    protected void init() {
        wordDao = AppDatabase.getInstance(getContext()).getWordDao();
        crwList = findViewById(R.id.crw_list);
        search("");

        EditText etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            private final Handler handler = new Handler(Looper.getMainLooper());
            private Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (runnable != null) handler.removeCallbacks(runnable);
                runnable = () -> {
                    String input = s.toString();
                    search(input);
                };
                handler.postDelayed(runnable, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }
}
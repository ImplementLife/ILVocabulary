package com.il.lexicon.ui.fragment;

import android.widget.Button;
import android.widget.TextView;
import com.il.lexicon.R;
import com.il.lexicon.data.jpa.provide.AppDatabase;
import com.il.lexicon.data.jpa.provide.WordDao;
import com.il.lexicon.ui.activity.MainActivity;
import com.il.lexicon.ui.custom.component.NavFragment;

public class AddWordMenuFragment extends NavFragment {
    private static final WordDao wordDao = AppDatabase.getInstance(MainActivity.getInstance()).getWordDao();
    public AddWordMenuFragment() {
        super(R.layout.fragment_add_word_menu);
    }

    @Override
    protected void init() {
        Button btnAddOne = findViewById(R.id.btn_add_one);
        btnAddOne.setOnClickListener(v -> {navigate(R.id.fragment_add_word_simple_one);});

        TextView tvTotalCount = findViewById(R.id.tv_total_count);
        TextView tvLearnedCount = findViewById(R.id.tv_learned_count);

        runAsync(() -> {
            int totalCount = wordDao.getCount();
            int learnedCount = wordDao.getCount(2);
            post(() -> {
                tvTotalCount.setText("Total words: " + totalCount);
                tvLearnedCount.setText("Learned words: " + learnedCount);
            });
        });
    }
}
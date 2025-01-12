package com.il.vcb.ui.fragment;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.il.vcb.R;
import com.il.vcb.data.jpa.entity.Word;
import com.il.vcb.data.jpa.provide.AppDatabase;
import com.il.vcb.data.jpa.provide.WordDao;
import com.il.vcb.service.valid.WordValidService;
import com.il.vcb.ui.activity.MainActivity;
import com.il.vcb.ui.custom.component.BaseFragment;

public class AddWordFragment extends BaseFragment {
    private static final WordDao wordDao = AppDatabase.getInstance(MainActivity.getInstance()).getWordDao();

    private EditText etLearnWord;
    private EditText etNativeWord;
    private Button btnNew;
    private Button btnSave;
    private TextView tvCountOfWords;

    public AddWordFragment() {
        super(R.layout.fragment_add_word);
    }

    @Override
    protected void init() {
        etLearnWord = findViewById(R.id.et_learn_word);
        etNativeWord = findViewById(R.id.et_native_word);
        btnNew = findViewById(R.id.btn_new);
        btnSave = findViewById(R.id.btn_save);
        tvCountOfWords = findViewById(R.id.tv_count_of_words);

        btnNew.setOnClickListener(v -> {
            etLearnWord.setText("");
            etNativeWord.setText("");
        });
        btnSave.setOnClickListener(v -> {
            Word word = new Word();
            word.setLearnLangWord(etLearnWord.getText().toString().trim());
            word.setNativeLangWord(etNativeWord.getText().toString().trim());
            if (!WordValidService.isValid(word)) return;
            runAsync(() -> {
                wordDao.insert(word);
                updateCountOfWords();
            });

            etLearnWord.setText("");
            etNativeWord.setText("");
        });

        updateCountOfWords();
    }

    private void updateCountOfWords() {
        runAsync(() -> {
            Integer count = wordDao.getCount();
            post(() -> {
                tvCountOfWords.setText(count.toString());
            });
        });
    }


}
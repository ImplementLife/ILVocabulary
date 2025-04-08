package com.il.lexicon.ui.fragment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.il.lexicon.R;
import com.il.lexicon.data.jpa.entity.Word;
import com.il.lexicon.data.jpa.provide.AppDatabase;
import com.il.lexicon.data.jpa.provide.WordDao;
import com.il.lexicon.service.valid.WordValidService;
import com.il.lexicon.ui.activity.MainActivity;
import com.il.lexicon.ui.custom.component.NavFragment;

import java.util.Date;

public class AddWordSimpleOneFragment extends NavFragment {
    private static final WordDao wordDao = AppDatabase.getInstance(MainActivity.getInstance()).getWordDao();

    private EditText etLearnWord;
    private EditText etNativeWord;

    private Word word;
    private boolean isUpdate = false;

    public AddWordSimpleOneFragment() {
        super(R.layout.fragment_add_word_simple_one);
    }

    @Override
    protected void init() {
        etLearnWord = findViewById(R.id.et_learn_word);
        etNativeWord = findViewById(R.id.et_native_word);

        Button btnBack = findViewById(R.id.btn_back);
        Button btnSaveAndBack = findViewById(R.id.btn_new);
        Button btnSaveAndNew = findViewById(R.id.btn_save);

        btnBack.setOnClickListener(v -> {
            navigateUp();
        });

        btnSaveAndBack.setOnClickListener(v -> {
            if (save()) return;
            navigateUp();
        });
        btnSaveAndNew.setOnClickListener(v -> {
            if (save()) return;

            etLearnWord.setText("");
            etNativeWord.setText("");

            isUpdate = false;
        });

    }

    @Override
    protected void argumentProcessing(Bundle args) {
        int word_id = args.getInt("word_id", -1);
        if (word_id > -1) {
            isUpdate = true;
            runAsync(() -> {
                word = wordDao.findById(word_id);
                post(() -> {
                    etLearnWord.setText(word.getLearnLangWord());
                    etNativeWord.setText(word.getNativeLangWord());
                });
            });
        }
    }
    private boolean save() {
        if (!isUpdate) word = new Word();
        word.setLearnLangWord(etLearnWord.getText().toString().trim());
        word.setNativeLangWord(etNativeWord.getText().toString().trim());
        word.setAddDate(new Date());
        if (!WordValidService.isValid(word)) return true;
        runAsync(() -> {
            if (isUpdate) {
                wordDao.update(word);
            } else {
                wordDao.insert(word);
            }
        });
        return false;
    }
}
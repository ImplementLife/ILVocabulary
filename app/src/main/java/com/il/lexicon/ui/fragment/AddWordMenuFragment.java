package com.il.lexicon.ui.fragment;

import android.widget.Button;
import com.il.lexicon.R;
import com.il.lexicon.ui.custom.component.NavFragment;

public class AddWordMenuFragment extends NavFragment {
    public AddWordMenuFragment() {
        super(R.layout.fragment_add_word_menu);
    }

    @Override
    protected void init() {
        Button btnAddOne = findViewById(R.id.btn_add_one);
        btnAddOne.setOnClickListener(v -> {navigate(R.id.fragment_add_word_simple_one);});
    }
}
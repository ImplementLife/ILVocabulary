package com.il.lexicon.ui.fragment;

import android.widget.Button;
import com.il.lexicon.R;
import com.il.lexicon.ui.custom.component.BaseFragment;
import com.il.lexicon.ui.custom.component.NavFragment;

public class LearnMenuFragment extends NavFragment {

    public LearnMenuFragment() {
        super(R.layout.fragment_learn_menu);
    }

    @Override
    public void init() {
        Button btnPairsLevel = findViewById(R.id.btn_pairs_level);
        Button btnQuizLevel = findViewById(R.id.btn_quiz_level);

        btnPairsLevel.setOnClickListener(v -> navigate(R.id.fragment_learn_pairs));
        btnQuizLevel.setOnClickListener(v -> navigate(R.id.fragment_learn_quiz));
    }

}

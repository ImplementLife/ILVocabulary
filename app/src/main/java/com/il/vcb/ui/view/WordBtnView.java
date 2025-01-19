package com.il.vcb.ui.view;

import android.widget.TextView;
import com.il.vcb.R;
import com.il.vcb.data.jpa.entity.Word;
import com.il.vcb.ui.custom.adapter.RecyclerViewListAdapter.Data;
import com.il.vcb.ui.custom.component.BaseView;

public class WordBtnView extends Data<Word> {
    private Runnable action;
    private String word;
    private boolean enabled;

    public WordBtnView() {
        super(R.layout.view_word_btn);
    }

    @Override
    public void bindData(BaseView view, Word word) {
        view.setOnClickListener(v -> {
            if (enabled) {
                action.run();
            }
        });
        TextView tvWord = view.findViewById(R.id.tv_word);
        tvWord.setText(this.word);

    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        callBind();
    }

    public void setText(String nativeLangWord) {
        word = nativeLangWord;
    }
    public void setOnClickListener(Runnable action) {
        this.action = action;
    }
}

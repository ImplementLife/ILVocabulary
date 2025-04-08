package com.il.lexicon.ui.view;

import android.widget.TextView;
import com.il.lexicon.R;
import com.il.lexicon.data.jpa.entity.Word;
import com.il.lexicon.ui.custom.adapter.RecyclerViewListAdapter;
import com.il.lexicon.ui.custom.component.BaseViewAdapter;

public class AddWordAllWordsListItemView extends RecyclerViewListAdapter.ViewDataBinder<Word> {
    public AddWordAllWordsListItemView() {
        super(R.layout.view__add_word__all_words__list_item);
    }

    @Override
    public void bindData(BaseViewAdapter view, Word data) {
        TextView tvLearnLangWord = view.findViewById(R.id.tv_learn_lang_word);
        TextView tvNativeLangWord = view.findViewById(R.id.tv_native_lang_word);

        tvLearnLangWord.setText(data.getLearnLangWord());
        tvNativeLangWord.setText(data.getNativeLangWord());
    }
}

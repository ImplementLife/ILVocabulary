package com.il.lexicon.ui.view;

import android.os.Bundle;
import android.widget.TextView;
import androidx.navigation.NavController;
import com.il.lexicon.R;
import com.il.lexicon.data.jpa.entity.Word;
import com.il.lexicon.ui.custom.adapter.RecyclerViewListAdapter;
import com.il.lexicon.ui.custom.component.BaseViewAdapter;

public class AddWordAllWordsListItemView extends RecyclerViewListAdapter.ViewDataBinder<Word> {
    private NavController navController;

    public AddWordAllWordsListItemView() {
        super(R.layout.view__add_word__all_words__list_item);
    }

    @Override
    public void bindData(BaseViewAdapter view, Word data) {
        TextView tvLearnLangWord = view.findViewById(R.id.tv_learn_lang_word);
        TextView tvNativeLangWord = view.findViewById(R.id.tv_native_lang_word);

        tvLearnLangWord.setText(data.getLearnLangWord());
        tvNativeLangWord.setText(data.getNativeLangWord());
        view.getRoot().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("word_id", getData().getId());
            navController.navigate(R.id.fragment_add_word_simple_one, bundle);
        });
    }

    public void setNavigate(NavController navController) {
        this.navController = navController;
    }
}

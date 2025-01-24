package com.il.lexicon.ui.view;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;
import com.il.lexicon.R;
import com.il.lexicon.data.jpa.entity.Word;
import com.il.lexicon.ui.custom.adapter.RecyclerViewListAdapter.ViewDataBinder;
import com.il.lexicon.ui.custom.component.BaseViewAdapter;

import static androidx.core.content.ContextCompat.getColor;


public class WordBtnView extends ViewDataBinder<Word> {
    private Runnable action;
    private String word;
    private boolean enabled = true;

    private GradientDrawable borderDrawable;
    private TextView tvWord;

    public WordBtnView() {
        super(R.layout.view_word_btn);
    }

    @Override
    public void bindData(BaseViewAdapter view, Word word) {
        view.setOnClickListener(() -> { if (enabled) action.run(); });
        tvWord = view.findViewById(R.id.tv_word);
        tvWord.setText(this.word);
        borderDrawable = (GradientDrawable) view.getRoot().getBackground();
        setStatusDefault();
    }

    public void animateDotColor(int startColor, int endColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnimation.setDuration(1000);
        colorAnimation.addUpdateListener(animator -> {
            int color = (int) animator.getAnimatedValue();
            borderDrawable.setStroke((int) getResources().getDisplayMetrics().density, color);
            tvWord.setTextColor(color);
        });
        colorAnimation.start();
    }

    public void setStatusDefault() {
        int color = getColor(getContext(), R.color.background_primary);
        borderDrawable.setStroke(
            (int) getResources().getDisplayMetrics().density,
            color
        );
        tvWord.setTextColor(color);
        this.enabled = true;
    }

    public void setStatusSelected() {
        int color = getColor(getContext(), R.color.yellow);
        borderDrawable.setStroke(
            (int) getResources().getDisplayMetrics().density,
            color
        );
        tvWord.setTextColor(color);
        this.enabled = false;
    }

    public void setStatusComplete() {
        int color = getColor(getContext(), R.color.green);
        borderDrawable.setStroke(
            (int) getResources().getDisplayMetrics().density,
            color
        );
        tvWord.setTextColor(color);

        this.enabled = false;
    }

    public void setStatusMistake() {
        int color = getColor(getContext(), R.color.red);
        borderDrawable.setStroke(
            (int) getResources().getDisplayMetrics().density,
            color
        );
        tvWord.setTextColor(color);
        animateDotColor(
            color,
            getColor(getContext(), R.color.background_primary)
        );
    }

    public void setText(String nativeLangWord) {
        word = nativeLangWord;
    }

    public void setOnClickListener(Runnable action) {
        this.action = action;
    }
}

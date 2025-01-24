package com.il.lexicon.ui.view;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.il.lexicon.R;
import com.il.lexicon.data.jpa.entity.Word;
import com.il.lexicon.ui.custom.adapter.RecyclerViewListAdapter.ViewDataBinder;
import com.il.lexicon.ui.custom.component.BaseViewAdapter;

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
        view.setOnClickListener(() -> {
            if (enabled) action.run();
        });
        tvWord = view.findViewById(R.id.tv_word);
        tvWord.setText(this.word);
        borderDrawable = (GradientDrawable) view.getRoot().getBackground();
        setStatusDefault();
    }

    public void setStatusDefault() {
        setStrokeColor(borderDrawable, R.color.background_primary);
        tvWord.setTextColor(getColor(R.color.background_primary));
        this.enabled = true;
    }

    public void setStatusSelected() {
        setStrokeColor(borderDrawable, R.color.yellow);
        tvWord.setTextColor(getColor(R.color.yellow));
        this.enabled = false;
    }

    public void setStatusComplete() {
        setStrokeColor(borderDrawable, R.color.green);
        tvWord.setTextColor(getColor(R.color.green));
        animateColorChange(
            getColor(R.color.red),
            getColor(R.color.none)
        );
        this.enabled = false;
    }

    public void setStatusMistake() {
        setStrokeColor(borderDrawable, R.color.red);
        tvWord.setTextColor(getColor(R.color.red));
        animateColorChange(
            getColor(R.color.red),
            getColor(R.color.background_primary)
        );
    }

    public void setText(String nativeLangWord) {
        word = nativeLangWord;
    }

    public void setOnClickListener(Runnable action) {
        this.action = action;
    }

    //region util

    private int getColor(int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    private void setStrokeColor(View view, int color) {
        setStrokeColor((GradientDrawable) view.getBackground(), color);
    }

    private void setStrokeColor(GradientDrawable drawable, int colorId) {
        int color = getColor(colorId);
        drawable.setStroke(
            (int) getResources().getDisplayMetrics().density,
            color
        );
    }

    public void animateColorChange(int startColor, int endColor) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnimation.setDuration(1000);
        colorAnimation.addUpdateListener(animator -> {
            int color = (int) animator.getAnimatedValue();
            borderDrawable.setStroke((int) getResources().getDisplayMetrics().density, color);
            tvWord.setTextColor(color);
        });
        colorAnimation.start();
    }

    //endregion util
}

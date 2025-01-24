package com.il.lexicon.ui.custom.component;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.il.lexicon.ui.activity.MainActivity;

import java.util.concurrent.CompletableFuture;

public class BaseViewAdapter {
    protected View root;

    public BaseViewAdapter(LayoutInflater inflater, int viewId, ViewGroup rootForThis) {
        this.root = inflater.inflate(viewId, rootForThis, false);
    }

    public BaseViewAdapter(int viewId, ViewGroup rootForThis) {
        this(LayoutInflater.from(MainActivity.getInstance()), viewId, rootForThis);
    }

    public View getRoot() {
        return root;
    }
    protected void setRoot(View root) {
        this.root = root;
    }

    public boolean post(Runnable action) {
        return root.post(action);
    }

    protected void runAsync(Runnable runnable) {
        CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                Log.e("runAsync", "", t);
            }
        });
    }

    public <T extends View> T findViewById(int id) {
        return root.findViewById(id);
    }

    public void setOnClickListener(Runnable action) {
        getRoot().setOnClickListener(v -> action.run());
    }

    public void setOnClickListener(View.OnClickListener listener) {
        getRoot().setOnClickListener(listener);
    }

    public void setTextViewById(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }

    public void setImgResById(int viewId, int resId) {
        ((ImageView) findViewById(viewId)).setImageResource(resId);
    }
}

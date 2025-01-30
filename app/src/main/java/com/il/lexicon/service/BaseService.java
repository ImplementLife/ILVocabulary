package com.il.lexicon.service;

import android.content.ContentResolver;
import android.content.Context;

public class BaseService {
    private final Context context;

    public BaseService(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
    public ContentResolver getContentResolver() {
        return getContext().getContentResolver();
    }
}
